package uk.ac.cam.grp_proj.mike.twork_service;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TerribleURLClassLoader extends ClassLoader {


    private URL url;

    public TerribleURLClassLoader(URL u) {
        url = u;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class<?> result;

        try {
            URL codeURL = new URL(url + name.replace('.', '/') + ".dex");
            HttpURLConnection codeCon = (HttpURLConnection) codeURL.openConnection();

            //codeCon.setRequestProperty("Cookie", cookie);
            codeCon.connect();

            InputStream classDefinition = codeCon.getInputStream();
            byte[] bytes = IOUtils.toByteArray(classDefinition);

            result = defineClass(name, bytes, 0, bytes.length);
        } catch(IOException e) {
            throw new ClassNotFoundException("IOException when trying to fetch code.");
        }

        return result;
    }
}

