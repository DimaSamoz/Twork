package uk.ac.cam.grp_proj.mike.twork_service;

/**
 * Created by James on 11/02/16.
 */
public class JobException extends Exception {
    Exception e;

    public Exception getUnderlyingException() { return e; }

    public JobException(Exception e) {
        this.e = e;
    }
}
