package uk.ac.cam.grp_proj.mike.twork_service;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class JobFetchExample {

	public static void doJob() throws Throwable {
		String hostURL = "http://52.36.156.147:9000/";

		//Send GET /available
		//At some point this will contain JSON about the phone, but it can be empty for now. 
		URL availableURL = new URL(hostURL + "available");
		HttpURLConnection con = (HttpURLConnection) availableURL.openConnection();
		con.connect();
		System.out.println("Available response: " + con.getResponseCode());


		//Get the cookie from the response
		String cookie = con.getHeaderField("Set-Cookie");


		for(int i = 0; i< 100; i++) {
		
		
			/*
			 * Complete fetch and execute to run on device.
			 * Lots of error handling code and stuff is needed, but this should work if the server is up.
			 * This should be running in a loop (with a small delay between each one), ignoring errors.
			 */





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
			//Will get 204 response if no jobs are available
			URL jobURL = new URL(hostURL + "job");
			HttpURLConnection jobCon = (HttpURLConnection) jobURL.openConnection();
			jobCon.setRequestProperty("Cookie", cookie);
			jobCon.connect();
			System.out.println("Job response: " + jobCon.getResponseCode());

			//Parse the JSON describing the job
			InputStream in = jobCon.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(in, writer, StandardCharsets.UTF_8);
			String str = writer.toString();

			JSONObject j = new JSONObject(str);
			String functionName = j.getString("function-class");
			long jobID = j.getLong("job-id");


			//Load and instantiate the class
//			//TerribleURLClassLoader loader = new TerribleURLClassLoader(new URL(hostURL + "test/code/"));
//			ClassLoader loader = ClassLoader.getSystemClassLoader();
//			Class<?> codeClass = loader.loadClass("uk.ac.cam.grp_proj.mike.twork_service." + functionName);
//					Object o = codeClass.newInstance();
//			Method codeToRun = codeClass.getDeclaredMethod("run", new Class<?>[] {InputStream.class, OutputStream.class});




			//Get the data for the job
			URL dataURL = new URL(hostURL + "data/" + Long.toString(jobID));
			HttpURLConnection dataCon = (HttpURLConnection) dataURL.openConnection();
			dataCon.setRequestProperty("Cookie", cookie);
			dataCon.connect();
			System.out.println("Data response: " + dataCon.getResponseCode());

			//Set up input/output for job
			InputStream jobInput = dataCon.getInputStream();
			ByteArrayOutputStream jobOutput = new ByteArrayOutputStream();

			//Run the job
//			codeToRun.invoke(o, jobInput, jobOutput);

			new PrimeComputationCode().run(jobInput, jobOutput);

			//Get the output from the job
			String outStr = new String(jobOutput.toByteArray(), StandardCharsets.UTF_8);
			System.out.println("Output from job: " + outStr);


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
			System.out.println("Result response: " + resultCon.getResponseCode());
		}
	}
}

