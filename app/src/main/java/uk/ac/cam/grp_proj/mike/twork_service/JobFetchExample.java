package uk.ac.cam.grp_proj.mike.twork_service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

public class JobFetchExample {
    private static final String TAG = "JobFetchExample";
    private static long timeout = 1000;
    private static int retries = 256;
    private static final Charset charset = Charset.forName("UTF-8");

    public static void doJob(CompService context) throws InterruptedException {
        String hostURL = "http://ec2-52-36-182-104.us-west-2.compute.amazonaws.com:9000/";

        //Send GET /available
        HttpURLConnection con = null;
        for (int i = 0; i < retries; i++) {
            try {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String mac = info.getMacAddress();
                Log.i(TAG, mac);

                JSONObject req = new JSONObject();
                req.accumulate("message", "available");
                req.accumulate("phone-id", mac);
                URL availableURL = new URL(hostURL + "available");
                con = (HttpURLConnection) availableURL.openConnection();
                con.connect();
                Log.i(TAG, "Available response: " + con.getResponseCode());
                break;
            }
            catch (JSONException e) {
                Log.e(TAG, "init exception", e);
                return;
            }
            catch (IOException e) {
                Log.e(TAG, "init exception", e);
                Thread.sleep(timeout);
            }
        }

        if (con == null) {
            Log.e(TAG, "failed to establish connection");
            return;
        }

        //Get the cookie from the response
        String cookie = con.getHeaderField("Set-Cookie");


        while (context.getShouldBeRunning()) {

            /*
             * Complete fetch and execute to run on device.
             * Lots of error handling code and stuff is needed, but this should work if the server is up.
             * This should be running in a loop (with a small delay between each one), ignoring errors.
             */

            try {
                //Send GET /job with cookie
                URL jobURL = new URL(hostURL + "job");
                HttpURLConnection jobCon = (HttpURLConnection) jobURL.openConnection();
                jobCon.setRequestProperty("Cookie", cookie);
                jobCon.connect();
                int responseCode = jobCon.getResponseCode();
                Log.i(TAG, "Job response: " + jobCon.getResponseCode());
                switch (responseCode) {
                    case 200:
                        // Has job
                        Log.i(TAG, "Have job");
                        break;
                    case 204:
                        // No jobs
                        Thread.sleep(timeout);
                        continue;
                    default:
                        Log.w(TAG, "weird response code " + Integer.toString(responseCode));
                        continue;
                }

                //Parse the JSON describing the job
                InputStream in = jobCon.getInputStream();
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, charset);
                String str = writer.toString();

                try {
                    JSONObject j = new JSONObject(str);
                    String functionName = j.getString("function-class");
                    long jobID = j.getLong("job-id");

                    //Get the data for the job
                    URL dataURL = new URL(hostURL + "data/" + Long.toString(jobID));
                    HttpURLConnection dataCon = (HttpURLConnection) dataURL.openConnection();
                    dataCon.setRequestProperty("Cookie", cookie);
                    dataCon.connect();
                    Log.i(TAG, "Data response: " + dataCon.getResponseCode());

                    //Set up input/output for job
                    InputStream jobInput = dataCon.getInputStream();
                    ByteArrayOutputStream jobOutput = new ByteArrayOutputStream();

                    Log.i(TAG, "Streams created");

                    //Run the job
                    //codeToRun.invoke(o, jobInput, jobOutput);

                    // We're not supporting class loading, so the SecurityManager is not needed.
                    //SecurityManager oldSM = System.getSecurityManager();
                    //HashSet<String> filePaths = new HashSet<>();
                    // TODO: give some scratch space
                    //System.setSecurityManager(new WorkSecurityManager(filePaths));
                    switch (functionName) {
                        case "PrimeComputationCode":
                            new PrimeComputationCode().run(jobInput, jobOutput);
                            break;
                        case "GrayscaleConvertCode":
                            new GrayscaleConvertCode().run(jobInput, jobOutput);
                            break;
                        case "SepiaConvertCode":
                            new SepiaConvertCode().run(jobInput, jobOutput);
                            break;
                        case "EdgeDetect":
                            new EdgeDetect().run(jobInput, jobOutput);
                            break;
                        default:
                            Log.w(TAG, "Unknown computation type");
                            // TODO: maybe should send an explicit fail response
                            continue;
                    }
                    //System.setSecurityManager(oldSM);

                    //Get the output from the job
                    Log.i(TAG, "Output from job ready");


                    //Send result back
                    URL resultURL = new URL(hostURL + "result/" + Long.toString(jobID));
                    HttpURLConnection resultCon = (HttpURLConnection) resultURL.openConnection();
                    resultCon.setRequestProperty("Cookie", cookie);
                    resultCon.setRequestMethod("POST");
                    resultCon.setRequestProperty("content-type", "application/octet-stream");
                    resultCon.setDoOutput(true);
                    OutputStream osw = resultCon.getOutputStream();
                    osw.write(jobOutput.toByteArray());
                    osw.close();
                    Log.i(TAG, "Result response: " + resultCon.getResponseCode());
                } catch (JSONException e) {
                    Log.e(TAG, "bad response", e);
                }
            }
            catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
    }
}

