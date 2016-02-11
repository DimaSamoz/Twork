package uk.ac.cam.grp_proj.mike.twork_service;

/**
 * Created by James on 11/02/16.
 */
public class ComputationException extends Exception {
    Exception e;

    public Exception getUnderlyingException() { return e; }

    public ComputationException(Exception e) { this.e = e; }
}
