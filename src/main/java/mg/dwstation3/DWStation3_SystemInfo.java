package mg.dwstation3;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by micha on 14.02.2018.
 */

public class DWStation3_SystemInfo {

    public DWStation3_SystemInfo() {

    }

    private String executeShell(String cmd) {
        StringBuffer output = new StringBuffer();
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;
    }

    public String getCPUtemp(String temp_path) {
        float temp_float = 0;
        String tempStr = "cat " + temp_path;
        String tempValue = executeShell(tempStr);

        if (tempValue.charAt(tempValue.length()-1)=='n'){
            tempValue = tempValue.replace(tempValue.substring(tempValue.length()-1), "");
        } else {
            tempValue = "";
        }

        temp_float = Float.parseFloat(tempValue);
        temp_float = temp_float / 1000;

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        String temp = decimalFormat.format(temp_float);

        //tempValue = tempValue.substring(0,2);
        //tempValue = tempValue + "°C";
        return temp;
    }

    public String getOS_Version() {
        return Build.FINGERPRINT;
    }

}
