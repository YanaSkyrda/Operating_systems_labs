import sun.misc.Signal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ComputationManager {
    private AsynchronousServerSocketChannel server;
    private static final int PORT = 4004;

    private int FAmount;
    private int GAmount;
    private List<Integer> argsForF;
    private List<Integer> argsForG;

    private boolean cancel = false;
    private List<Process> processes;
    private boolean result = true;


    public ComputationManager(int FAmount, int GAmount,
                              List<Integer> argsForF, List<Integer> argsForG) throws IOException {
        server = AsynchronousServerSocketChannel.open();
        server.bind(new InetSocketAddress("localhost", PORT));
        this.FAmount = FAmount;
        this.GAmount = GAmount;
        this.argsForF = argsForF;
        this.argsForG = argsForG;
        this.processes = new ArrayList<>();
    }

    public ResultInfo getResult(Future<Integer> resultFuture, ByteBuffer buffer)
            throws ExecutionException, InterruptedException {
        resultFuture.get();
        buffer.rewind();
        return new ResultInfo(buffer);
    }

    private static class ResultInfo {
        boolean result;
        char functionType;
        long calculationTime;

        ResultInfo(ByteBuffer buffer) {
            buffer.rewind();
            this.result = buffer.getInt() == 1;
            this.calculationTime = buffer.getLong();
            this.functionType = buffer.getChar();
        }
    }

    private void runAllProcesses() throws IOException {
        for (int i = 0; i < FAmount; i++) {
            runProcess("functionprocesses.FProcess", argsForF.get(i));
        }

        for (int i = 0; i < GAmount; i++) {
            runProcess("functionprocesses.GProcess", argsForG.get(i));
        }

    }
    private void runProcess(String fileWithProcess, int arg) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        Process process = Runtime.getRuntime().exec(
                "java -cp " + currentDirectory+ "\\lab1.main.jar " + fileWithProcess + " "
                + arg + " " + PORT);
        processes.add(process);
    }

    private void printResult(Future<Integer> resultFuture, Future<AsynchronousSocketChannel> channelFuture,
                             ByteBuffer resultBuffer, boolean clientAlreadyInit) throws ExecutionException, InterruptedException {
        if (cancel && result) {
            if (clientAlreadyInit && resultFuture.isDone()) {
                ResultInfo currResult = getResult(resultFuture, resultBuffer);
                result = currResult.result;
            } else if (!clientAlreadyInit && channelFuture.isDone()) {
                AsynchronousSocketChannel client = channelFuture.get();
                resultFuture = client.read(resultBuffer);
                ResultInfo currResult = getResult(resultFuture, resultBuffer);
                result = currResult.result;
            } else {
                System.out.println("Computation was cancelled.");
                return;
            }
        } else {
            if (result) {
                System.out.println("Result was calculated without short-circuit evaluation.");
            }
        }

        System.out.println("Your result of computation: " + result + ".");
    }

    public void run() throws IOException, ExecutionException, InterruptedException {
        Signal.handle(new Signal("INT"), signal -> cancel = true);

        runAllProcesses();

        Future<AsynchronousSocketChannel> channelFuture = server.accept();
        AsynchronousSocketChannel client = null;
        Future<Integer> resultFuture = null;
        ByteBuffer resultBuffer = ByteBuffer.allocate(Integer.BYTES + Long.BYTES + Character.BYTES);

        int stopCounter = 0;
        boolean clientAlreadyInit = false;

        while (stopCounter != FAmount + GAmount) {
            if (cancel) {
                break;
            }

            if (channelFuture.isDone()) {
                if (!clientAlreadyInit) {
                    client = channelFuture.get();
                    resultFuture = client.read(resultBuffer);
                    clientAlreadyInit = true;
                } else {
                    if (resultFuture.isDone()) {
                        ResultInfo currResult = getResult(resultFuture, resultBuffer);
                        System.out.println("Got result from function " + currResult.functionType + ": "
                                + currResult.result + ". Calculation time: "
                                + ((int) (currResult.calculationTime * Math.pow(10, -6))) + " ms.");
                        resultBuffer.clear();

                        if (!currResult.result) {
                            result = false;
                            for (Process process : processes) {
                                process.destroy();
                            }
                            System.out.println("Result was calculated with short-circuit evaluation.");
                            break;
                        } else {
                            stopCounter++;
                            client.close();
                            channelFuture = server.accept();
                            clientAlreadyInit = false;
                        }
                    }
                }
            }
        }

        printResult(resultFuture, channelFuture, resultBuffer, clientAlreadyInit);
    }
}
