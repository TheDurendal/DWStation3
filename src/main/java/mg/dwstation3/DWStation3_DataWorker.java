package mg.dwstation3;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DWStation3_DataWorker extends Service {

    DWStation3_SystemInfo dwStation3_systemInfo;
    DWStation3_WeatherInfo dwStation3_weatherInfo;
    Handler handler;
    Runnable runnable;
    FirebaseFirestore db;
    FirestoreExportData firestoreExportData;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * Creating system info and firebase export objects
    * Starting service worker
     */
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service Started");
        dwStation3_systemInfo = new DWStation3_SystemInfo();
        firestoreExportData = new FirestoreExportData();
        this.startWorker();
    }

    /*
    * Service Worker, started on app start
    * Getting system info (temp, memory usage, etc.), Weather info (from sensors) and sending
    * to Firebase
    * handler.postDelayed to set refresh time - 30 min
    *
     */
    private void startWorker() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getSystemInfo(firestoreExportData);
                getWeatherInfo(firestoreExportData);
                //exportFirestoreCloud(firestoreExportData);
                //System.out.println(temp);
                handler.postDelayed(runnable, 30*60000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    /*
    * Getting system info
     */
    private FirestoreExportData getSystemInfo(FirestoreExportData firestoreExportData) {

        //Get CPU temp
        String CPUtemp = dwStation3_systemInfo.getCPUtemp("sys/class/thermal/thermal_zone0/temp");

        //Get OS version
        String osVERSION = dwStation3_systemInfo.getOS_Version();

        //Get Free RAM
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        double availableMegs = mi.availMem / 0x100000L;
        double totalMegs = mi.totalMem / 0x100000L;

        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;

        String freeRAM = Double.toString(availableMegs);
        String freePercent = Double.toString(percentAvail);
        String totalRAM = Double.toString(totalMegs);


        //Filling export data object - to be send to Firebase
        firestoreExportData.setCPU_TEMP(CPUtemp);
        firestoreExportData.setOS_VERSION(osVERSION);
        firestoreExportData.setFREE_RAM(freeRAM + " " + freePercent + " " + totalRAM);
        return firestoreExportData;
    }

    /*
    * Getting weather info from sensors - tbd
     */
    private FirestoreExportData getWeatherInfo(FirestoreExportData firestoreExportData) {

        dwStation3_weatherInfo = new DWStation3_WeatherInfo();

        dwStation3_weatherInfo.StartReading(firestoreExportData,this);

        //firestoreExportData.setPM_10("54.4");
        //firestoreExportData.setPM_25("12.5");
        return firestoreExportData;
    }

    /*
    * export to Firestore cloud
     */
    public void exportFirestoreCloud(FirestoreExportData firestoreExportData) {

        //Setting date for new document and collection
        Date date = Calendar.getInstance().getTime();

        firestoreExportData.setDate(date);

        DateFormat df_day = new SimpleDateFormat("yyyy-MM-dd");
        String day = df_day.format(Calendar.getInstance().getTime());

        DateFormat df_time = new SimpleDateFormat("HH:mm:ss");
        String time = df_time.format(Calendar.getInstance().getTime());

        //Get instance of Firebase Firestore Cloud
        db = FirebaseFirestore.getInstance();

        //Insert Firestore Export Data to Firestore
        //Collection - day, document - time
        db.collection(day).document(time).set(firestoreExportData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document");
                        e.printStackTrace();
                    }
                });

    }

}
