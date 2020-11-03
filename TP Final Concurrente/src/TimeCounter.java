public class TimeCounter {
    private final long start;
    private int cantThreads;

    public TimeCounter(long start, int cantThreads) {
        this.start = start;
        this.cantThreads = cantThreads;
    }

    public synchronized void printEndTime(long end) {
        cantThreads--;
        if (cantThreads == 0){
            double tiempo = (double) (end - start);
            System.out.println(tiempo + " milisegundos");
        }
    }
}
