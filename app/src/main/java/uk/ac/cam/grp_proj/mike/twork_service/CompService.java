package uk.ac.cam.grp_proj.mike.twork_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.cam.grp_proj.mike.twork_app.MainActivity;
import uk.ac.cam.grp_proj.mike.twork_app.R;
import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by Dima on 02/02/16.
 */
public class CompService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{
    // Binder given to clients
    private final IBinder mBinder = new CompBinder();
    private Thread thread;

    public static final int RUNNING_NOTIFICATION_ID = 1;
    public static final int PAUSED_NOTIFICATION_ID = 2;
    private boolean shouldBeRunning;
    private boolean paused;
    private boolean onlyWhileCharging;
    private boolean onlyViaWiFi;

    private float batteryLimit;

    private static final String TAG = "JobFetcher";
    public static final String HOST_URL =
            "http://ec2-52-36-182-104.us-west-2.compute.amazonaws.com:9000/";
    public static final String COMP_PAUSED = "computation_pausd";
    public static final String COMP_RESUMED = "computation resumed";
    private ConnectivityManager cm;

    private static final Charset charset = Charset.forName("UTF-8");

    public CompService() {
    }

    private final BroadcastReceiver constraintsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent batteryStatus) {
            if (shouldBeRunning) {
                // Only consider changes when computation is supposed to run
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

                int rawLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float percentage = rawLevel/(float) scale;
                boolean enoughCharge = batteryLimit < percentage;


                if (!paused) {
                    if (onlyViaWiFi && !isWifi()) {
                        pauseComputation("No WiFi connection available");
                    } else if (!onlyViaWiFi && !isOnline()) {
                        pauseComputation("No internet connection");
                    } else if (onlyWhileCharging && !isCharging) {
                        pauseComputation("Phone is not charging");
                    } else if (!onlyWhileCharging && !enoughCharge) {
                        pauseComputation("Charge is below battery limit");
                    }
                    if (paused) {
                        Intent pausedIntent = new Intent(COMP_PAUSED);
                        LocalBroadcastManager.getInstance(CompService.this).sendBroadcast(pausedIntent);
                    }
                } else {
                    boolean connectionCorrect = (onlyViaWiFi && isWifi())
                                            || (!onlyViaWiFi && isOnline());
                    boolean chargeCorrect = (onlyWhileCharging && isCharging)
                                         || (!onlyWhileCharging && enoughCharge);
                    if (    (onlyWhileCharging && isCharging && connectionCorrect) ||
                            (!onlyWhileCharging && chargeCorrect && connectionCorrect) ||
                            (onlyViaWiFi && isWifi() && chargeCorrect) ||
                            (!onlyViaWiFi && connectionCorrect && chargeCorrect)) {
                        resumeComputation();
                        Intent resumedIntent = new Intent(COMP_RESUMED);
                        LocalBroadcastManager.getInstance(CompService.this).sendBroadcast(resumedIntent);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        registerReceiver(this.constraintsReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);

        cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);


        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        onlyWhileCharging = !sharedPrefs.getBoolean(getString(R.string.battery_def), true);

        batteryLimit = sharedPrefs.getInt(getString(R.string.batt_lim), 60) / (float) 100;

        onlyViaWiFi = !sharedPrefs.getBoolean(getString(R.string.mobile_def), true);

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(this.constraintsReceiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.battery_def))) {
            onlyWhileCharging = !sharedPreferences.getBoolean(getString(R.string.battery_def), true);
        } else if (key.equals(getString(R.string.mobile_def))) {
            onlyViaWiFi = !sharedPreferences.getBoolean(getString(R.string.mobile_def), true);
        }
    }

    public boolean isOnline() {
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public boolean isWifi() {
        return isOnline() && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class CompBinder extends Binder {
        public CompService getService() {
            // Return this instance of CompService so clients can call public methods
            return CompService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    private Notification createNotification(String title, String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_white_not)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_color_512))
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    /** method for clients */
    public void startComputation() {

        // Notification
        startForeground(
                RUNNING_NOTIFICATION_ID,
                createNotification("Tworking...", "Computation running")
        );

        shouldBeRunning = true;
        final TworkDBHelper db = TworkDBHelper.getHelper(this);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

                try {

                    JobFetcher.doJob(CompService.this);

                } catch (Throwable throwable) {
                    try {
                        Log.e("CompService", "", throwable);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "", e);
                    }
                }

            }
        });

        thread.start();
    }

    public void pauseComputation(String text) {
        paused = true;
        stopForeground(true);
        Notification pausedNotification = createNotification("Paused", text);
        startForeground(PAUSED_NOTIFICATION_ID, pausedNotification);
    }

    public void resumeComputation() {
        paused = false;
        stopForeground(true);
        startComputation();
    }

    public void stopComputation() {
        shouldBeRunning = false;
        stopForeground(true);
    }

    private static class JSONRetriever extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String retString = "";

            try {
                URL jobURL = new URL(params[0] + "computations");
                HttpURLConnection jobCon = (HttpURLConnection) jobURL.openConnection();
                jobCon.connect();
                //Parse the JSON describing the job
                InputStream in = jobCon.getInputStream();
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, charset);

                retString =  writer.toString();
            } catch (IOException e) {
//                Log.e(TAG, "", e);
            }
            return retString;
        }
    }

    /**
     * Return the list of available computations from the server
     * @param db The database (helper) containing the computations the user already selected
     * @return A list of available computations
     */
    public static List<Computation> getComputations(TworkDBHelper db) {
        List<Computation> serverComps = new ArrayList<>();

        try {
            List<String> alreadySelectedComps = Computation.getCompNames(db.getSelectedComps());

            String str = new JSONRetriever().execute(HOST_URL).get();
            JSONObject j = new JSONObject(str);
            JSONArray compArray = j.getJSONArray("computations");

            for (int i = 0; i < compArray.length(); i++) {
                JSONObject comp = compArray.getJSONObject(i);
                String compID = comp.getString("id");
                String compName = comp.getString("name");
                String compDesc = comp.getString("description");
                String compTopics = comp.getString("topics");
//                String compTopics = "Lattice-theoretic astrogeography";
                Computation newComp = new Computation(
                        compID,
                        compName,
                        compDesc,
                        compTopics,
                        null
                );

                if (!alreadySelectedComps.contains(newComp.getName())) {
                    serverComps.add(newComp);
                }
            }


        } catch (JSONException e) {
            Log.e(TAG, "bad response", e);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "connection interrupted", e);
        }


        // MOCK VALUES
        String[] compNames = {"DreamLab", "SETI@home", "Galaxy Zoo", "RNA World", "Malaria Control", "Leiden Classical", "GIMPS", "Electric Sheep", "DistributedDataMining", "Compute For Humanity"};

        String[] compDescs = {
                "Breast, ovarian, prostate and pancreatic cancer",
                "Search for extraterrestrial life by analyzing specific radio frequencies emanating from space",
                "Classifies galaxy types from the Sloan Digital Sky Survey",
                "Uses bioinformatics software to study RNA structure",
                "Simulate the transmission dynamics and health effects of malaria",
                "General classical mechanics for students or scientists",
                "Searches for Mersenne primes of world record size",
                "Fractal flame generation",
                "Research in the various fields of data analysis and machine learning, such as stock market prediction and analysis of medical data",
                "Generating cryptocurrencies to sell for money to be donated to charities"
        };

        String[] compAreas = {
                "Cancer research",
                "Astrobiology",
                "Astronomy, Cosmology",
                "Molecular biology",
                "Epidemiology",
                "Chemistry",
                "Mathematics",
                "Computational art",
                "Data analysis, Machine learning",
                "Criptocurrencies, Charitable organisations"

        };
        for (int i = 0; i < compNames.length; i++) {
            // Filter out already selected computations
            List<String> activeComps = Computation.getCompNames(db.getSelectedComps());
            if (!activeComps.contains(compNames[i])) {
                serverComps.add(new Computation("" + i, compNames[i], compDescs[i], compAreas[i], null));
            }
        }

        return serverComps;
    }

    public boolean shouldBeRunning() {
        return shouldBeRunning;
    }

    public boolean isPaused() {
        return paused;
    }


}
