package uk.ac.cam.grp_proj.mike.twork_app;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by laura on 26/02/16.
 */
public class YAxisValueFormatterToInt implements ValueFormatter {


    private DecimalFormat mFormat;

    public YAxisValueFormatterToInt() {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        // write your logic here
       // if ((value - (int(value)))
        return mFormat.format(value); // e.g. append a dollar-sign
    }
}
