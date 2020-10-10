import spos.lab1.demo.Conjunction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;

class CalculationProcess {
    private SocketChannel socketChannel;
    private boolean result;
    private long calculationTime;

    private void connectToServer(int port) throws IOException {
        socketChannel = SocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
        socketChannel.connect(hostAddress);
    }

    private int readArgument() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        socketChannel.read(buffer);
        buffer.rewind();
        return buffer.getInt();
    }

    CalculationProcess(char func, int port) throws Exception {
        connectToServer(port);
        int x = readArgument();
        long start, end;
        start = System.nanoTime();
        switch (func) {
            case 'f':
            case 'F': {
                this.result = Conjunction.funcF(x);
                break;
            }
            case 'g':
            case 'G': {
                this.result = Conjunction.funcG(x);
                break;
            }
        }
        end = System.nanoTime();
        calculationTime = end - start;
        sendResult(func);
    }

    public void sendResult(char func) throws IOException, ExecutionException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + Long.BYTES + Character.BYTES);
        buffer.putInt(result ? 1 : 0);
        buffer.putLong(calculationTime);
        buffer.putChar(func);
        buffer.rewind();
        socketChannel.write(buffer);
        socketChannel.close();
    }
}
