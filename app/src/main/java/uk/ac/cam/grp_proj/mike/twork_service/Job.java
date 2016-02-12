package uk.ac.cam.grp_proj.mike.twork_service;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by James on 10/02/16.
 */
public class Job {
    private Id compId;
    private Id id;
    private Context context;

    public Id getId() { return this.id; }
    public String getInputFileName() { return this.id.toString() + "-input"; }
    public String getOutputFileName() { return this.id.toString() + "-output"; }

    public String getComputationClassName() { return "C" + this.compId.toString(); }

    private static Class<?> loadClassFromDirectory(File dir) {
        // TODO: we don't know how to do class loading yet
        return null;
    }

    public Class<?> getCodeClass()
            throws FileNotFoundException, MalformedURLException, ClassNotFoundException {
        File dir = context.getExternalFilesDir(null);
        assert dir != null;
        //try (URLClassLoader loader = new URLClassLoader(new URL[] { dir.toURI().toURL() })) {
        //    return loader.loadClass(this.getComputationClassName());
        //}
        return loadClassFromDirectory(dir);
    }

    public void run() throws ComputationException, JobException {
        try {
            Class<?> cls = this.getCodeClass();
            Object instance = cls.newInstance();
            Method m = cls.getMethod("run", InputStream.class, OutputStream.class);
            InputStream is = this.context.openFileInput(this.getInputFileName());
            OutputStream os = this.context.openFileOutput(this.getOutputFileName(), Context.MODE_PRIVATE);
            try { m.invoke(instance, is, os); }
            catch (Exception e) { throw new JobException(e); }
        } catch (ClassNotFoundException e) {
            throw new ComputationException(e);
        } catch (FileNotFoundException e) {
            throw new ComputationException(e);
        } catch (MalformedURLException e) {
            throw new ComputationException(e);
        } catch (NoSuchMethodException e) {
            throw new ComputationException(e);
        } catch (InstantiationException e) {
            throw new ComputationException(e);
        } catch (IllegalAccessException e) {
            throw new ComputationException(e);
        }
    }

    public Job(Id id, Id compId, Context context) {
        this.id = id;
        this.compId = compId;
        this.context = context;
    }
}
