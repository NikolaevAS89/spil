package spil.core;

import jssc.SerialNativeInterface;
import jssc.SerialPortException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Alexander on 18.02.2017.
 */
public class SerialPort implements SerialPortConstants {

    private String portName;
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;
    private boolean isRTS = true;
    private boolean isDTR = true;

    private long portHandle = -5L;

    private SerialNativeInterface serialInterface;

    /**
     * Constructor
     */
    private SerialPort() {
        serialInterface = new SerialNativeInterface();
    }

    /**
     * Try to open port/
     *
     * @throws SerialPortException if port not opened
     */
    public void open() throws SerialPortException {
        portHandle = serialInterface.openPort(portName, true);
        this.checkPortStatus();
        serialInterface.setParams(portHandle, baudRate, dataBits, stopBits, parity, isRTS, isDTR, 1);
    }

    /**
     * Check port status
     *
     * @throws SerialPortException if port not available
     */
    private void checkPortStatus() throws SerialPortException {
        if (this.portHandle == -1L) {
            throw new SerialPortException(this.portName, "checkPortStatus()", "Port busy");
        } else if (this.portHandle == -2L) {
            throw new SerialPortException(this.portName, "checkPortStatus()", "Port not found");
        } else if (this.portHandle == -3L) {
            throw new SerialPortException(this.portName, "checkPortStatus()", "Permission denied");
        } else if (this.portHandle == -4L) {
            throw new SerialPortException(this.portName, "checkPortStatus()", "Incorrect serial port");
        } else if (this.portHandle == -5L) {
            throw new SerialPortException(this.portName, "checkPortStatus()", "Port is closed");
        }
    }

    /**
     * TODO
     *
     * @param mask
     * @return
     * @throws SerialPortException
     */
    public boolean setFlowControlMode(int mask) throws SerialPortException {
        this.checkPortStatus();
        return this.serialInterface.setFlowControlMode(this.portHandle, mask);
    }

    /**
     * TODO
     *
     * @return
     * @throws SerialPortException
     */
    public int getFlowControlMode() throws SerialPortException {
        this.checkPortStatus();
        return this.serialInterface.getFlowControlMode(this.portHandle);
    }

    /**
     * TODO
     *
     * @return
     * @throws SerialPortException
     */
    public boolean sendBreak(int duration) throws SerialPortException {
        this.checkPortStatus();
        return this.serialInterface.sendBreak(this.portHandle, duration);
    }

    /**
     * TODO
     *
     * @return
     * @throws SerialPortException
     */
    public int[] getLinesStatus() throws SerialPortException {
        this.checkPortStatus();
        return this.serialInterface.getLinesStatus(this.portHandle);
    }

    public InputStream getInputStream() {
        return this.getInputStream(FINAL_CLOSE_MODE);
    }

    public InputStream getInputStream(int mode) {
        return new PortInpuStream(mode);
    }

    public OutputStream getOutputStream() {
        return this.getOutputStream(FINAL_CLOSE_MODE);
    }

    public OutputStream getOutputStream(int mode) {
        return new PortOutputStream(mode);
    }

    private class PortOutputStream extends OutputStream {
        private int mode = 0x00;

        public PortOutputStream(int mode) {
            this.mode = mode;
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[]{(byte) b});
        }

        @Override
        public void write(byte buffer[]) throws IOException {
            try {
                serialInterface.writeBytes(portHandle, buffer);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        public void write(byte[] var1, int var2, int var3) throws IOException {
            if (var1 == null) {
                throw new NullPointerException();
            } else if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
                if (var3 != 0) {
                    byte[] range = Arrays.copyOfRange(var1, var2, var3);
                    this.write(range);
                }
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public void flush() throws IOException {
            try {
                serialInterface.purgePort(portHandle, PURGE_TXCLEAR);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        @Override
        public void close() throws IOException {
            try {
                if ((this.mode & FLUSH_ON_CLOSE_MODE) == FLUSH_ON_CLOSE_MODE) {
                    this.flush();
                }
                if ((this.mode & FREE_ON_CLOSE_MODE) == FREE_ON_CLOSE_MODE) {
                    serialInterface.closePort(portHandle);
                }
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private class PortInpuStream extends InputStream {
        private int mode = 0x00;

        public PortInpuStream(int mode) {
            this.mode = mode;
        }

        @Override
        public int read() throws IOException {
            try {
                return serialInterface.readBytes(portHandle, 1)[0];
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        @Override
        public int read(byte b[]) throws IOException {
            return read(b, 0, b.length);
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            byte[] readedBytes = null;
            try {
                readedBytes = serialInterface.readBytes(portHandle, len);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            int c = readedBytes[0];
            if (c == -1) {
                return -1;
            }
            b[off] = (byte) c;
            int i = 1;
            for (; i < len; i++) {
                c = readedBytes[i];
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
            return i;
        }

        @Override
        public int available() throws IOException {
            try {
                return serialInterface.getBuffersBytesCount(portHandle)[0];
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        @Override
        public void close() throws IOException {
            try {
                if ((this.mode & FLUSH_ON_CLOSE_MODE) == FLUSH_ON_CLOSE_MODE) {
                    serialInterface.purgePort(portHandle, PURGE_RXCLEAR);
                }
                if ((this.mode & FREE_ON_CLOSE_MODE) == FREE_ON_CLOSE_MODE) {
                    serialInterface.closePort(portHandle);
                }
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    public static class ATMComServiceBuilder {
        private String portName;
        private int baudRate;
        private int dataBits;
        private int stopBits;
        private int parity;
        private boolean isRTS = true;
        private boolean isDTR = true;

        public ATMComServiceBuilder(String portName) {
            this.portName = portName;
        }

        /**
         * @param baudRate data transfer rate
         * @return this
         */
        public ATMComServiceBuilder setBaudRate(int baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        /**
         * @param dataBits number of data bits
         * @return this
         */
        public ATMComServiceBuilder setDataBits(int dataBits) {
            this.dataBits = dataBits;
            return this;
        }

        /**
         * @param stopBits number of stop bits
         * @return this
         */
        public ATMComServiceBuilder setStopBits(int stopBits) {
            if (stopBits == 1) {
                stopBits = 0;
            } else if (stopBits == 3) {
                stopBits = 1;
            }
            this.stopBits = stopBits;
            return this;
        }

        /**
         * @param parity parity
         * @return this
         */
        public ATMComServiceBuilder setParity(int parity) {
            this.parity = parity;
            return this;
        }

        /**
         * @param isRTS initial state of RTS line (ON/OFF)
         * @return this
         */
        public ATMComServiceBuilder setRTS(boolean isRTS) {
            this.isRTS = isRTS;
            return this;
        }

        /**
         * @param isDTR initial state of DTR line (ON/OFF)
         * @return this
         */
        public ATMComServiceBuilder setDTR(boolean isDTR) {
            this.isDTR = isDTR;
            return this;
        }


        public SerialPort build() throws SerialPortException {
            SerialPort atmComService = new SerialPort();
            atmComService.portName = portName;
            atmComService.baudRate = baudRate;
            atmComService.dataBits = dataBits;
            atmComService.stopBits = stopBits;
            atmComService.parity = parity;
            atmComService.isRTS = isRTS;
            atmComService.isDTR = isDTR;
            return atmComService;
        }
    }
}
