import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final int noncePossibilities = (int) Math.pow(2,32);
    private static final Buffer buffer = new Buffer(2);
    public static boolean found = false;
    public static TimeCounter timeCounter;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Por favor, indique cantidad de threads a utilizar (máximo 16):");
        int cantThreads = Integer.parseInt(br.readLine());

        System.out.println("Ahora la dificultad en cantidad de bytes (1-4):");
        int difficultyInBytes = Integer.parseInt(br.readLine());

        System.out.println("Dé un prefijo a hashear (opcional):");
        byte[] prefix = br.readLine().getBytes();

        timeCounter = new TimeCounter(System.currentTimeMillis(), cantThreads);
        startPoolExecution(cantThreads, difficultyInBytes, prefix);
    }

    private static void startPoolExecution(int cantThreads, int difficultyInBytes, byte[] prefix) {
        new ThreadPool(buffer, cantThreads, difficultyInBytes, prefix);

        int dividedNonce = noncePossibilities / cantThreads;
        for (int i = 1; i <= cantThreads; i++) {
            Tupla temp_tuple = new Tupla(dividedNonce * (i - 1), dividedNonce * i);
            buffer.write(temp_tuple);
        }
    }
}