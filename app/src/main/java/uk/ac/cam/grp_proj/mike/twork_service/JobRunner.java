package uk.ac.cam.grp_proj.mike.twork_service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by James on 10/02/16.
 */
public class JobRunner extends Thread {
    private Job job;
    private SecurityManager securityManager;
    private Class<?> computation;

    public JobRunner(Job job, Class<?> computation) {
        this.job = job;
        Set<String> filePaths = new HashSet<>();
        filePaths.add("/tmp/" + job);
        filePaths.add("/tmp/" + job + "-scratch");
        this.securityManager = new WorkSecurityManager(filePaths);
        this.computation = computation;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        SecurityManager oldSM = System.getSecurityManager();
        System.setSecurityManager(this.securityManager);

        // TODO: run stuff
        try {
            ;
        }
        catch (Exception e) { e.printStackTrace(); }

        System.setSecurityManager(oldSM);
    }
}
