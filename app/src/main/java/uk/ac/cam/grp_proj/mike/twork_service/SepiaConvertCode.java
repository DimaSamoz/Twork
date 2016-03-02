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

        for (int i = 0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                int c = mimg.getPixel(j, i);
                int red = Color.red(c);
                int green = Color.green(c);
                int blue = Color.blue(c);

                mimg.setPixel(j, i, Color.rgb(
                        coerce(.393 * red + .769 * green + .189 * blue),
                        coerce(.349 * red + .686 * green + .168 * blue),
                        coerce(.272 * red + .534 * green + .131 * blue)
                ));
            }
        }

        mimg.compress(Bitmap.CompressFormat.JPEG, 50, output);
    }

    private static int coerce(double x) {
        return coerceByte(new Double(x).intValue());
    }

    private static int coerceByte(int x) {
        if (x < 0) return 0;
        else if (x >= 256) return 255;
        else return x;
    }
}
