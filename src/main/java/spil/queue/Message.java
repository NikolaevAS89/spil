package spil.queue;

/**
 * Created by timestop on 13.03.17.
 */
public class Message {
    private long portHandle;
    private String portName;
    private byte[] message;

    public Message(long portHandle, String portName, byte[] message) {
        this.message = message;
        this.portHandle = portHandle;
        this.portName = portName;
    }

    public long getPortHandle() {
        return portHandle;
    }

    public String getPortName() {
        return portName;
    }

    public byte[] getMessage() {
        return message;
    }
}
