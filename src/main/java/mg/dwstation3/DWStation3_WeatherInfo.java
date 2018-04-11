package mg.dwstation3;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;
import android.os.HandlerThread;
import android.os.Handler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by micha on 23.02.2018.
 */

public class DWStation3_WeatherInfo {
    private Date date;
    private String PM_25;
    private String PM_10;
    private String PM_1;
    private String TEMPERATURE;
    private String HUMIDITY;
    private String PRESSURE;

    //TODO: zmienic na konfigurowalne
    private static final int BAUD_RATE = 9600;
    private static final int STOP_BITS = 1;
    private static final String NAME = "UART0";

    private UartDevice uartDevice;

    private Runnable uartRunnable = new Runnable() {
        @Override
        public void run() {
            ReadAirData();
        }
    };

    private HandlerThread mInputThread;
    private Handler mInputHandler;

    byte[] message_buffer;
    int message_buffer_counter;

    DWStation3_DataWorker dwStation3_dataWorker;
    FirestoreExportData firestoreExportData;

    public DWStation3_WeatherInfo() {
        message_buffer = new byte[32];
        message_buffer_counter = 0;
    }


    public void StartReading(FirestoreExportData firestoreExportData, DWStation3_DataWorker dwStation3_dataWorker) {
        System.out.println("UART: StartReading");
        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());
        this.dwStation3_dataWorker = dwStation3_dataWorker;
        this.firestoreExportData = firestoreExportData;


        try {
            this.OpenUart();
            System.out.println("UART: Opening UART");
            // Read any initially buffered data
            mInputHandler.post(uartRunnable);
        } catch (IOException e) {
            System.out.println("Unable to open UART device "+ e);
        }
    }

    private void OpenUart() throws IOException {
        System.out.println("UART: Open");
        uartDevice = PeripheralManager.getInstance().openUartDevice(NAME);

        uartDevice.setBaudrate(BAUD_RATE);
        uartDevice.setParity(UartDevice.PARITY_NONE);
        uartDevice.setStopBits(STOP_BITS);
        uartDevice.setDataSize(8);

        uartDevice.registerUartDeviceCallback(uartDeviceCallback, mInputHandler);
    }

    private void CloseUart() throws  IOException {
            if (uartDevice != null) {
                uartDevice.unregisterUartDeviceCallback(uartDeviceCallback);
                try {
                    uartDevice.close();
                } finally {
                    uartDevice = null;
                }
            }
    }

    private UartDeviceCallback uartDeviceCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uartDevice) {
            //System.out.println("UART: Callback");
            ReadAirData();

            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            System.out.println(uart + ": Error event " + error);
        }
    };

     private static String print(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (byte b : bytes) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append("]");
        return sb.toString();
    }

    private void ReadAirData() {
        //System.out.println("ReadAirData");
        if(uartDevice != null) {
           // this.SetToActive();
            try {
                //System.out.println("UART: Reading...");
                byte[] buffer = new byte[8];
                int read;
                while((read = uartDevice.read(buffer,buffer.length)) > 0) {
//System.out.println("UART:" + buffer.length);
                    for(int i = 0; i<buffer.length;i++) {
                        //System.out.println("Index: " + i);

                        if(buffer[i] == 0x42 && i != 7) {
                            if(buffer[i+1] == 0x4d) {
                                //System.out.println(String.format("0x%02X ", buffer[0]));
                                //System.out.println(String.format("0x%02X ", buffer[1]));
                                //message_buffer is full, parse and clear
                                if (message_buffer_counter == 32) {
                                    this.Parse(message_buffer);
                                }

                                for (int j = 0; j < 32; j++) {
                                    message_buffer[j] = 0x00;
                                }

                                message_buffer_counter = 0;
                                message_buffer[i] = buffer[i];
                                message_buffer_counter++;
                                // System.out.println("UART 1: " + message_buffer_counter);
                            }
                        } else if(message_buffer_counter < 32) {
                            message_buffer[message_buffer_counter] = buffer[i];
                            message_buffer_counter++;
                            //System.out.println("UART 2:" + message_buffer_counter);
                        }
                    }

                    //System.out.println("UART: READ:"+ print(buffer));

                }
            } catch (IOException e) {
                System.out.println("Unable to transfer data over UART " + e);
            }
        }
    }

    private void Parse(byte[] buffer) {
        //System.out.println("UART:" + print(buffer));

        byte suma = 0x00;
        for(int i = 0; i < 30; i++) {
            suma+=buffer[i];
        }

        if(buffer[31] == suma) {
            int PM1_0 = buffer[4] * 256 + buffer[5];
            int PM2_5 = buffer[6] * 256 + buffer[7];
            int PM_10 = buffer[8] * 256 + buffer[9];

            System.out.println("PM1: " + PM1_0 + " PM25: " + PM2_5 + " PM10: " + PM_10);


            //System.out.println("UART: checksum: " + String.format("0x%02X ", suma));
            //firestoreExportData.setPM1_0(Integer.toString(PM1_0));
           // firestoreExportData.setPM_10(Integer.toString(PM_10));
           // firestoreExportData.setPM_25(Integer.toString(PM2_5));
           // dwStation3_dataWorker.exportFirestoreCloud(firestoreExportData);
        }

    }

    private void SetToActive() {
        System.out.println("UART SetToActive");
        byte active[] = {0x42, 0x4d, (byte)0xe1, 0x00, 0x01, 0x01, 0x71};

        try {
            uartDevice.write(active, active.length);
            System.out.println("UART: WRITE: "+ Arrays.toString(active));
        } catch (IOException e) {

        }

    }



    public Date getDate() {
        return date;
    }

    public String getPM_25() {
        return PM_25;
    }

    public String getPM_10() {
        return PM_10;
    }

    public String getPM_1() { return PM_1; }

    public String getTEMPERATURE() {
        return TEMPERATURE;
    }

    public String getHUMIDITY() {
        return HUMIDITY;
    }

    public String getPRESSURE() {
        return PRESSURE;
    }
}
