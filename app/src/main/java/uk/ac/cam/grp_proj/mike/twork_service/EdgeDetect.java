package uk.ac.cam.grp_proj.mike.twork_service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EdgeDetect implements ComputationCode {

    @Override
    public void run(InputStream input, OutputStream output) {
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(input);
            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get something mutable
        Bitmap mimg = image.copy(Bitmap.Config.RGB_565, true);

        mimg.compress(Bitmap.CompressFormat.PNG, 100, output);
    }
}
