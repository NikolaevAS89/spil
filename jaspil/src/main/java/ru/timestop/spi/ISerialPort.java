package ru.timestop.spi;

/**
 * Created by Alexander on 26.02.2017.
 */
public interface ISerialPort {

    /**
     * Try to open oprt
     *
     * @throws Exception if port not opened
     */
    void openPort(String portName, int baudRate, int dataBits, int stopBits, int parity) throws Exception;

    /**
     * Purge of input and/or output buffer
     *
     * @param flags flags specifying required actions for purgePort method
     */
    void purgePort(int flags) throws Exception;

    /**
     * Close port
     */
    void closePort() throws Exception;

    /**
     * Read data from port
     *
     * @param buff where byte readed
     * @param off  start position
     * @param len  byte count for read
     * @return count of really readed bytes
     */
    int readBytes(byte[] buff, int off, int len) throws Exception;

    /**
     * Write data to port
     *
     * @param buffer array of bytes to write
     * @param off    start position in array
     * @param len    byte count to be sended
     */
    void writeBytes(byte[] buffer, int off, int len) throws Exception;

    /**
     * Get bytes count in buffers of port
     *
     * @return Method returns the array that contains info about bytes count in buffers:
     * <br><b>element 0</b> - input buffer, can be -1</br>
     * <br><b>element 1</b> - output buffer, can be -1</br>
     */
    int[] getBuffersBytesCount();

    /**
     * Send Break singnal for setted duration
     *
     * @param handle   handle of opened port
     * @param duration duration of Break signal
     */
    void sendBreak(long handle, int duration) throws Exception;

    /**
     * return port handle value
     *
     * @return port handle value, return -1 if port not opened
     */
    long getHandler();

    /**
     * Wait events
     *
     * @return Method returns two-dimensional array containing event types and their values
     * (<b>events[i][0] - event type</b>, <b>events[i][1] - event value</b>).
     */
    int[][] waitEvents();

    /**
     * Getting lines states
     *
     * @return Method returns the array containing information about lines in following order:
     * <br><b>element 0</b> - <b>CTS</b> line state</br>
     * <br><b>element 1</b> - <b>DSR</b> line state</br>
     * <br><b>element 2</b> - <b>RING</b> line state</br>
     * <br><b>element 3</b> - <b>RLSD</b> line state</br>
     */
    int[] getLinesStatus();

    /**
     * Set flow control mode
     *
     * @param mask mask of flow control mode
     */
    void setFlowControlMode(int mask);
}
