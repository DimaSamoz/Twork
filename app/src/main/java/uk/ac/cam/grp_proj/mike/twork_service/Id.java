package uk.ac.cam.grp_proj.mike.twork_service;

/**
 * Created by James on 11/02/16.
 */
public class Id {
    private int x;

    @Override
    public String toString() {
        return Integer.toString(x, 36);
    }

    public Id(int id) { x = id; }
}
