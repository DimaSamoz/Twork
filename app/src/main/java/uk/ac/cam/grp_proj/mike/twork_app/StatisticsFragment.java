package uk.ac.cam.grp_proj.mike.twork_app;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by laura on 11/02/16.
 */
public class StatisticsFragment extends Fragment {

    private LineChart lineChart;
    private ArrayList<Entry> lineChartEntries;
    private ArrayList<String> lineChartLabels;

    private BarChart barChart;
    private ArrayList<BarEntry> barChartEntries;
    private ArrayList<String> barChartLabels;

    private TworkDBHelper db = TworkDBHelper.getHelper(getContext());

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

        //Line Chart
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChartEntries = new ArrayList<>();
        lineChartLabels = new ArrayList<>();

        //Bar Chart
        barChart = (BarChart) view.findViewById(R.id.barChart);
        barChartEntries = new ArrayList<>();
        barChartLabels = new ArrayList<>();

        addLineDataEntries();
        setUpLineChart();

        addBarDataEntries();
        setUpBarChart();
        return view;
    }

    private void setUpLineChart() {
        LineDataSet dataset = new LineDataSet(lineChartEntries, "Number of Jobs solved");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.WHITE);
        dataset.setValueFormatter(new YAxisValueFormatterToInt());
        dataset.setValueTextSize(10);
        LineData data = new LineData(lineChartLabels, dataset);
        lineChart.setData(data);
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        lineChart.getAxisRight().setTextColor(Color.TRANSPARENT);
        yAxis.setTextColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        lineChart.setVisibleXRange(10);
        lineChart.setClickable(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setScaleYEnabled(false);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDescription("");
        lineChart.getAxisLeft().setValueFormatter(new YAxisValueFormatterToInt());
        lineChart.getAxisRight().setEnabled(false);
        //chart.getAxisLeft().setTextColor((Color.parseColor("#313131")));
        lineChart.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addLineDataEntries() {
        Cursor cursor = db.readDataFromJobTable();
        int indexTime = cursor.getColumnIndex(TworkDBHelper.TABLE_JOB_START_TIME);
        int i = 0;
        int nr = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentLocalTime = null;
        if (cursor.moveToFirst()) {
            do {
                long value = Long.parseLong(cursor.getString(indexTime));

                String localTime = formatter.format(new Date(value));

                if (localTime.equals(currentLocalTime)) {
                    nr++;
                } else {
                    if (currentLocalTime != null) {
                        lineChartEntries.add(new Entry(nr, i));
                        lineChartLabels.add(currentLocalTime);
                        i++;
                    }
                    nr = 1;
                    currentLocalTime = localTime;
                }
            } while (cursor.moveToNext());
        }
        if (currentLocalTime == null) currentLocalTime = formatter.format(new Date());
        lineChartEntries.add(new Entry(nr, i));
        lineChartLabels.add(currentLocalTime);
    }


    private void addBarDataEntries() {
        List<Computation> computationList = db.getActiveComps();
        Map<String, Integer> map = db.getJobCounts();
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            barChartEntries.add(new BarEntry(entry.getValue(),i));
            barChartLabels.add(entry.getKey());
            i++;
        }
    }

    private void setUpBarChart() {
        BarDataSet dataset = new BarDataSet(barChartEntries, null);
        dataset.setValueTextColor(Color.WHITE);
        dataset.setValueTextSize(10);
        dataset.setLabel(null);
        dataset.setValueFormatter(new YAxisValueFormatterToInt());
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(barChartLabels,dataset);
        barChart.setData(data);
        barChart.setDrawGridBackground(false);
        barChart.setGridBackgroundColor(android.R.color.transparent);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setVisibleXRange(5);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setValueFormatter(new YAxisValueFormatterToInt());
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        data.setValueTextColor(Color.WHITE);
//        Log.v("barchart", "entries: " + barChartEntries.size());
//        Log.v("barchart", "labels: " + barChartLabels.size());

    }

}
