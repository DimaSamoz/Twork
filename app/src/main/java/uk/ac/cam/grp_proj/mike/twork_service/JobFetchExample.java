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
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;


public class JobFetchExample {
    private static final String TAG = "JobFetchExample";
    private static long timeout = 1000;

    public static void doJob(Context context) throws InterruptedException {
		String hostURL = "http://52.11.247.46:9000/";

		//Send GET /available
		//At some point this will contain JSON about the phone, but it can be empty for now.
        HttpURLConnection con;
        while (true) {
            try {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String mac = info.getMacAddress();

                JSONObject req = new JSONObject();
                req.accumulate("message", "available");
                req.accumulate("mac", mac);
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


		//Get the cookie from the response
		String cookie = con.getHeaderField("Set-Cookie");


		for(int i = 0; i< 100; i++) {

			/*
			 * Complete fetch and execute to run on device.
			 * Lots of error handling code and stuff is needed, but this should work if the server is up.
			 * This should be running in a loop (with a small delay between each one), ignoring errors.
			 */

            try {

                //### The service doesn't need to do this, but it's here for testing
			    /*
			    //Add a computation that has one job
			    URL addComputationURL = new URL(hostURL + "test/add/" + "John_Smith" + "/" + "4");
			    HttpURLConnection addCon = (HttpURLConnection) addComputationURL.openConnection();
			    addCon.setRequestMethod("POST");
			    addCon.connect();
			    System.out.println("Add computation response: " + addCon.getResponseCode());
			    */
                //###

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
                IOUtils.copy(in, writer, StandardCharsets.UTF_8);
                String str = writer.toString();

                try {
                    JSONObject j = new JSONObject(str);
                    String functionName = j.getString("function-class");
                    long jobID = j.getLong("job-id");

                    //Load and instantiate the class
                    //TerribleURLClassLoader loader = new TerribleURLClassLoader(new URL(hostURL + "test/code/"));
                    //ClassLoader loader = ClassLoader.getSystemClassLoader();
                    //Class<?> codeClass = loader.loadClass("uk.ac.cam.grp_proj.mike.twork_service." + functionName);
                    //		Object o = codeClass.newInstance();
                    //Method codeToRun = codeClass.getDeclaredMethod("run", new Class<?>[] {InputStream.class, OutputStream.class});


                    //Get the data for the job
                    URL dataURL = new URL(hostURL + "data/" + Long.toString(jobID));
                    HttpURLConnection dataCon = (HttpURLConnection) dataURL.openConnection();
                    dataCon.setRequestProperty("Cookie", cookie);
                    dataCon.connect();
                    Log.i(TAG, "Data response: " + dataCon.getResponseCode());

                    //Set up input/output for job
                    InputStream jobInput = dataCon.getInputStream();
                    ByteArrayOutputStream jobOutput = new ByteArrayOutputStream();

                    //Run the job
                    //codeToRun.invoke(o, jobInput, jobOutput);

                    SecurityManager oldSM = System.getSecurityManager();
                    HashSet<String> filePaths = new HashSet<>();
                    // TODO: give some scratch space
                    System.setSecurityManager(new WorkSecurityManager(filePaths));
                    new PrimeComputationCode().run(jobInput, jobOutput);
                    System.setSecurityManager(oldSM);

                    //Get the output from the job
                    String outStr = new String(jobOutput.toByteArray(), StandardCharsets.UTF_8);
                    Log.i(TAG, "Output from job: " + outStr);


                    //Send result back
                    URL resultURL = new URL(hostURL + "result/" + Long.toString(jobID));
                    HttpURLConnection resultCon = (HttpURLConnection) resultURL.openConnection();
                    resultCon.setRequestProperty("Cookie", cookie);
                    resultCon.setRequestMethod("POST");
                    resultCon.setRequestProperty("content-type", "text/plain");
                    resultCon.setDoOutput(true);
                    OutputStream osw = resultCon.getOutputStream();
                    osw.write(outStr.getBytes(StandardCharsets.UTF_8));
                    osw.close();
                    Log.i(TAG, "Result response: " + resultCon.getResponseCode());
                } catch (JSONException e) {
                    Log.e(TAG, "bad response", e);
                }
            }
            catch (IOException e) {
                Log.e(TAG, "", e);
            }
		}
	}
}

