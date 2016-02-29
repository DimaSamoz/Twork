package uk.ac.cam.grp_proj.mike.twork_service;

import java.io.InputStream;
import java.io.OutputStream;

//A Java object to hold the code for a computation.
public interface ComputationCode {
    //This method contains the code to complete the job.
    void run(InputStream input, OutputStream output);
}