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
        addEntries();
        addLabels();
       // addDataEntries();
        setUpChart();
        return view;
    }

    private void setUpChart() {
        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        dataset.setDrawCubic(true);
        dataset.setColor(Color.WHITE);
        LineData data = new LineData(labels, dataset);
        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setScaleYEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addEntries() {
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
    }

    private void addLabels() {
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
    }

   /* private void addDataEntries() {
       // numberOfJobs = 0;
        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        Cursor cursor = db.readDataFromJobTable();
        int indexTime = cursor.getColumnIndex(TworkDBHelper.TABLE_JOB_START_TIME);
      //  TworkDBHelper.onUpgrade(this,1,2);
        Log.v("index", "" + indexTime);
        //cursor.moveToFirst();
        int i = 0;
        int nr = 0;
        String currentLocalTime = null;
        do {
            System.out.println(cursor.moveToFirst());
            if(cursor != null && cursor.moveToFirst()) {
                Log.v("cursor", "here");
                long value = cursor.getInt(indexTime);
                DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
                String localTime = date.format(value);
                if (localTime.equals(currentLocalTime)) {
                    nr++;
                } else {
                    if (currentLocalTime != null) {
                        entries.add(new Entry(nr, i));
                        labels.add(currentLocalTime);
                    }
                    nr = 0;
                    currentLocalTime = localTime;
                }
            }
        }while(cursor.moveToNext());
        cursor.close();
       // Log.d("Number of jobs", " " + numberOfJobs);
    }*/

}
