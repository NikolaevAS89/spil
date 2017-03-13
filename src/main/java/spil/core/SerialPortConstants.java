package spil.core;

/**
 * Created by timestop on 13.03.17.
 */
interface SerialPortConstants {
    int BAUDRATE_110 = 110;
    int BAUDRATE_300 = 300;
    int BAUDRATE_600 = 600;
    int BAUDRATE_1200 = 1200;
    int BAUDRATE_4800 = 4800;
    int BAUDRATE_9600 = 9600;
    int BAUDRATE_14400 = 14400;
    int BAUDRATE_19200 = 19200;
    int BAUDRATE_38400 = 38400;
    int BAUDRATE_57600 = 57600;
    int BAUDRATE_115200 = 115200;
    int BAUDRATE_128000 = 128000;
    int BAUDRATE_256000 = 256000;
    int DATABITS_5 = 5;
    int DATABITS_6 = 6;
    int DATABITS_7 = 7;
    int DATABITS_8 = 8;
    int STOPBITS_1 = 1;
    int STOPBITS_2 = 2;
    int STOPBITS_1_5 = 3;
    int PARITY_NONE = 0;
    int PARITY_ODD = 1;
    int PARITY_EVEN = 2;
    int PARITY_MARK = 3;
    int PARITY_SPACE = 4;
    int PURGE_RXABORT = 2;
    int PURGE_RXCLEAR = 8;
    int PURGE_TXABORT = 1;
    int PURGE_TXCLEAR = 4;
    int MASK_RXCHAR = 1;
    int MASK_RXFLAG = 2;
    int MASK_TXEMPTY = 4;
    int MASK_CTS = 8;
    int MASK_DSR = 16;
    int MASK_RLSD = 32;
    int MASK_BREAK = 64;
    int MASK_ERR = 128;
    int MASK_RING = 256;
    int FLOWCONTROL_NONE = 0;
    int FLOWCONTROL_RTSCTS_IN = 1;
    int FLOWCONTROL_RTSCTS_OUT = 2;
    int FLOWCONTROL_XONXOFF_IN = 4;
    int FLOWCONTROL_XONXOFF_OUT = 8;
    int ERROR_FRAME = 8;
    int ERROR_OVERRUN = 2;
    int ERROR_PARITY = 4;
    int PARAMS_FLAG_IGNPAR = 1;
    int PARAMS_FLAG_PARMRK = 2;

    int DO_NOTHING_ON_CLOSE_MODE = 0x00;
    int FLUSH_ON_CLOSE_MODE = 0x01;
    int FREE_ON_CLOSE_MODE = 0x02;
    int FINAL_CLOSE_MODE = 0x03;
}
