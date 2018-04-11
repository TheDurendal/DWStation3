package mg.dwstation3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.things.device.TimeManager;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    TimeManager timeManager;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set time for Raspberry Pi
        this.setTime();

        //Start Worker service
        intent = new Intent(this,DWStation3_DataWorker.class);
        intent.setData(Uri.parse(""));
        this.startService(intent);
    }

    public void onDestory() {
        super.onDestroy();
        intent = new Intent(this,DWStation3_DataWorker.class);
        intent.setData(Uri.parse(""));
        this.stopService(intent);
System.out.println("Stopped Service");
    }

    private void setTime() {
        timeManager = TimeManager.getInstance();
        timeManager.setTimeFormat(TimeManager.FORMAT_24);
        timeManager.setAutoTimeEnabled(true);
        timeManager.setTimeZone("Europe/Warsaw");
    }
}
