package uk.ac.cam.grp_proj.mike.twork_service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GrayscaleConvertCode implements ComputationCode {

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

        int width = mimg.getWidth();
        int height = mimg.getHeight();

        for (int i = 0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                int c = mimg.getPixel(j, i);
                int red = (int)(Color.red(c) * 0.299);
                int green = (int)(Color.green(c) * 0.587);
                int blue = (int)(Color.blue(c) * 0.114);

                int gray = Color.rgb(red + green + blue, red + green + blue, red + green + blue);
                mimg.setPixel(j, i, gray);
            }
        }

        mimg.compress(Bitmap.CompressFormat.PNG, 100, output);
    }
}
