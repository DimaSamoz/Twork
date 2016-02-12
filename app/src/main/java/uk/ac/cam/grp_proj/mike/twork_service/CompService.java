package uk.ac.cam.grp_proj.mike.twork_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import uk.ac.cam.grp_proj.mike.twork_app.MainActivity;
import uk.ac.cam.grp_proj.mike.twork_app.R;

/**
 * Created by Dima on 02/02/16.
 */
public class CompService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new CompBinder();
    private Thread thread;

    public static final int RUNNING_NOTIFICATION_ID = 51;
    private int iterLimit;
    private boolean shouldBeRunning;

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

        // TODO for some reason the notification is only visible when the app is open
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_cached_black_24dp)
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
                        JobFetchExample.doJob();
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

    // TODO temporary service bypass
    public void submitMacAddress(long number) {

    }



}
