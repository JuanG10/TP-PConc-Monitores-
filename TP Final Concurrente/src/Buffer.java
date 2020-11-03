public class Buffer {

    private final Tupla[] data;
    private final int N;
    private int begin = 0, end = 0;

    public Buffer(int tamano) {
        this.N = tamano;
        this.data = new Tupla[this.N+1];
    }

    public synchronized void write(Tupla o) {
        while (isFull())
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        data[begin] = o;
        begin = next(begin);
        notifyAll();
    }

    public synchronized Tupla read() {
        while (isEmpty())
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        Tupla result = data[end];
        end = next(end);
        notifyAll();
        return result;
    }

    private boolean isEmpty() {
        return begin == end;
    }

    private boolean isFull() {
        return next(begin) == end;
    }

    private int next(int i) {
        return (i + 1) % (N + 1);
    }
}