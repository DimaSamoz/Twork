package uk.ac.cam.grp_proj.mike.twork_service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SepiaConvertCode implements ComputationCode {

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
        int sepiaDepth = 20;
        int sepiaIntensity = 80;

        for (int i = 0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                int c = mimg.getPixel(j, i);
                int red = Color.red(c);
                int green = Color.green(c);
                int blue = Color.blue(c);

                int gray = (red + green + blue) / 3;

                red = green = blue = gray;
                red += sepiaDepth * 2;
                green += sepiaDepth;
                blue -= sepiaIntensity;

                red = coerceByte(red);
                green = coerceByte(green);
                blue = coerceByte(blue);

                mimg.setPixel(j, i, gray);
            }
        }

        mimg.compress(Bitmap.CompressFormat.PNG, 100, output);
    }

    private int coerceByte(int x) {
        if (x < 0) return 0;
        else if (x >= 256) return 255;
        else return x;
    }
}
