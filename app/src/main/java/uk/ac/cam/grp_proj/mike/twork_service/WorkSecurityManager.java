package uk.ac.cam.grp_proj.mike.twork_service;

import java.io.FileDescriptor;
import java.lang.SecurityManager;
import java.security.Permission;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by James on 2016-02-07
 * A security manager allowing code to modify only a specific set of files.
 * A job may need scratch space, and we may put output in a file.
 * Alternatively, it may be safer to not allow this, but we can decide.
 */
// TODO: make sure this does what I think it does
// (test that the files can be edited and other IO can't be done)
class WorkSecurityManager extends SecurityManager {
    private Set<String> filePaths;
    private Set<String> actions;

    public WorkSecurityManager(Set<String> filePaths)  {
        this.filePaths = filePaths;
        this.actions = new HashSet<>();
        this.actions.add("read");
        this.actions.add("read,write");
        this.actions.add("write");
    }

    @Override
    public void checkPermission(Permission p) throws SecurityException {
        if (this.filePaths.contains(p.getName()) && actions.contains(p.getActions()))
            return;
        throw new SecurityException("Sandboxed code requested forbidden permission");
    }
}

// vim:sw=4
