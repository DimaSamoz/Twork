package uk.ac.cam.grp_proj.mike.twork_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import uk.ac.cam.grp_proj.mike.twork_app.MainActivity;
import uk.ac.cam.grp_proj.mike.twork_app.R;
import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by Dima on 02/02/16.
 */
public class CompService extends Service {
    private Queue<Job> pendingJobs = new LinkedList<>();

    // Binder given to clients
    private final IBinder mBinder = new CompBinder();
    private Thread thread;

    public static final int RUNNING_NOTIFICATION_ID = 51;
    private int iterLimit;
    private boolean shouldBeRunning;

    // A temporary cache for the available computations from the server
    private static List<Computation> cachedComps;

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

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_white_not)
                .setContentTitle("Tworking")
                .setContentText("Twork computation running...")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    /** method for clients */
    public void startComputation() {

        // Notification
        startForeground(RUNNING_NOTIFICATION_ID, createNotification());

        shouldBeRunning = true;

        iterLimit = 500;

        // TODO replace with proper code
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                while (shouldBeRunning) {
                    try {
                        JobFetchExample.doJob(CompService.this);
                    } catch (Throwable throwable) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                for (int i = 0; i < iterLimit; i++) {
//                    Log.i("Service", Integer.toString(i));
//
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });

        thread.start();
    }

    public void stopComputation() {
//        iterLimit = 0;
        shouldBeRunning = false;
        stopForeground(true);
    }

    /**
     * Return the list of available computations from the server
     * @param db The database (helper) containing the computations the user already selected
     * @return A list of available computations
     */
    public static List<Computation> getComputations(TworkDBHelper db) {
        if (cachedComps == null) {
            cachedComps = new ArrayList<>();

            // TEMPORARY VALUES
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
                List<String> activeComps = Computation.getCompNames(db.getUnfinishedComps());
                if (!activeComps.contains(compNames[i])) {
                    cachedComps.add(new Computation(i, compNames[i], compDescs[i], compAreas[i], null, null, null));
                }
            }
        }

        return cachedComps;
    }

    public static void updateComps() {
        cachedComps = null;
    }

    // TODO temporary service bypass
    public void submitMacAddress(long number) {

    }

    // TODO: use parts of Ben's JobFetchExample
    private void fetchJobs(String hostName) {

    }

}
