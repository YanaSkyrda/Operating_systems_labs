import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    private static List<Integer> readListOfInts(int listSize, BufferedReader reader) throws IOException {
        List<Integer> args = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            int readValue = Character.getNumericValue(reader.read());
            while (readValue == -1) {
                readValue = Character.getNumericValue(reader.read());
            }
            args.add(readValue);
        }
        return args;
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter number of functions");
        int functionsAmount = Integer.parseInt(reader.readLine());
        System.out.println("Enter args for functions");
        List<Integer> functionsArgs = readListOfInts(functionsAmount, reader);

        ComputationManager computationManager = new ComputationManager(functionsAmount, functionsArgs);
        computationManager.run();
    }
}
