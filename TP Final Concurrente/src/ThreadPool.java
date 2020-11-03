public class ThreadPool {

    public ThreadPool(Buffer buffer, int cantWorkers, int difficulty, byte[] prefixInBytes){
        for (int w = 0; w < cantWorkers; w++) {
            new PowWorker(buffer, difficulty, prefixInBytes).start();
        }
    }
}