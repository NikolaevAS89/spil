package ru.timestop.spi;

/**
 * Created by Alexander on 26.02.2017.
 */
public class SerialPort implements ISerialPort {

    private long heandle = -1L;

    public native void openPort(String portName, int baudRate, int dataBits, int stopBits, int parity) throws Exception;

    public native void purgePort(int flags) throws Exception;

    public native void closePort() throws Exception;

    public native int readBytes(byte[] buff, int off, int len) throws Exception;

    public native void writeBytes(byte[] buffer, int off, int len) throws Exception;

    public native int[] getBuffersBytesCount();

    public native void sendBreak(long handle, int duration) throws Exception;

    public native int[][] waitEvents();

    public native int[] getLinesStatus();

    public native void setFlowControlMode(int mask);

    private void setHandler(long heandle) {
        this.heandle = heandle;
    }

    public long getHandler() {
        return heandle;
    }
}