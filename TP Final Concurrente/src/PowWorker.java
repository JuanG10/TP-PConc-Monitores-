import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PowWorker extends Thread {
    private final Buffer myBuffer;
    int difficulty;
    byte[] prefix;
    byte[] nonce;
    Tupla nonceRange;

    public PowWorker(Buffer buffer, int difficulty, byte[] prefixInBytes) {
        this.myBuffer = buffer;
        this.difficulty = difficulty;
        this.prefix = prefixInBytes;
    }

    @Override
    public void run() {
        super.run();
        nonceRange = myBuffer.read();
        try {
            foundNonce();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void foundNonce() throws NoSuchAlgorithmException {
        int actualPos = nonceRange.inicio;
        byte[] newHash;
        while (!Main.found && actualPos < nonceRange.fin) {
            newHash = getNewHash(actualPos);
            checkIfFoundNonce(newHash);
            actualPos++;
        }

        Main.timeCounter.printEndTime(System.currentTimeMillis());
    }

    private byte[] getNewHash(int inicio) throws NoSuchAlgorithmException {
        nonce = toBytes(inicio);
        return toSHA256(concatByteArrays(this.prefix,nonce));
    }

    private void checkIfFoundNonce(byte[] hash) {
        boolean found = true;
        if (!Main.found) {
            for (int i = 0; i < difficulty; i++) {
                found = found && hash[i] == 0;
            }
            Main.found = found;
        }
    }

    private static byte[] concatByteArrays(byte[] prefix, byte[] nonce){
        byte[] c = new byte[prefix.length + nonce.length];
        System.arraycopy(prefix, 0, c, 0, prefix.length);
        System.arraycopy(nonce, 0, c, prefix.length, nonce.length);
        return c;
    }

    private static byte[] toSHA256(byte[] toHash) throws NoSuchAlgorithmException {
        byte[] result;
        result = MessageDigest.getInstance("SHA-256").digest(toHash);
        return result;
    }

    public static byte[] toBytes(long number) {
        byte[] bytes = new byte[32];
        int index = 31;
        long copyOfInput = number;
        while (copyOfInput > 0) {
            if(index >= 0) {
                bytes[index--] = (byte) (copyOfInput % 2);
                copyOfInput = copyOfInput / 2;
            }
            else {
                copyOfInput = 0;
            }
        }
        return bytes;
    }
}