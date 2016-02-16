package uk.ac.cam.grp_proj.mike.twork_app;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by laura on 11/02/16.
 */
public class LineChartFragment extends Fragment {

    LineChart chart;
    ArrayList<Entry> entries;
    ArrayList<String> labels;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);
        entries = new ArrayList<>();
        labels = new ArrayList<String>();
        addDataEntries();
        setUpChart();
        return view;
    }

    private void setUpChart() {
        LineDataSet dataset = new LineDataSet(entries, "# of Jobs solved");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setDrawCubic(true);
        dataset.setColor(Color.WHITE);
        LineData data = new LineData(labels, dataset);
        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        YAxis yAxis = chart.getAxisLeft();
        chart.getAxisRight().setTextColor(Color.TRANSPARENT);
        yAxis.setTextColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setScaleYEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setDescription("");
        chart.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addDataEntries() {
        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        Cursor cursor = db.readDataFromJobTable();
        int indexTime = cursor.getColumnIndex(TworkDBHelper.TABLE_JOB_START_TIME);
        db.addJob(123,231,System.currentTimeMillis(),344343,4324334);
        Log.v("index", "" + indexTime);
        int i = 0;
        int nr = 0;
        String currentLocalTime = null;
        cursor.moveToFirst();
        do {
                long value = Long.parseLong(cursor.getString(indexTime));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            String localTime = formatter.format(new Date(value));
                Log.v("time from table", "date: " + localTime);
                Log.v("time from table", "value: " + value);
                if (localTime.equals(currentLocalTime)) {
                    nr++;
                } else {
                    if (currentLocalTime != null) {
                        Log.v("cursor", "entry" + nr);
                        entries.add(new Entry(nr, i));
                        labels.add(currentLocalTime);
                        i++;
                    }
                    nr = 1;
                    currentLocalTime = localTime;
            }
        }while(cursor.moveToNext());
        Log.v("cursor", "entry" + nr);
        entries.add(new Entry(nr, i));
        labels.add(currentLocalTime);
    }

}
