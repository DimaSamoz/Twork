package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by Dima on 28/01/16.
 */
public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private Switch mobileDataSwitch;
    private Switch batterySwitch;
    private Switch locationSwitch;
    private ToggleButton compToggle;
    private SharedPreferences sharedPref;
    private ListView listView;
    private LineChart chart;
    private ArrayList<Entry> entries;
    private ArrayList<String> labels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.comp_list);


        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        mobileDataSwitch = (Switch) view.findViewById(R.id.mobileDataSwitch);
        batterySwitch = (Switch) view.findViewById(R.id.batterySwitch);
        locationSwitch = (Switch) view.findViewById(R.id.locationSwitch);
        compToggle = (ToggleButton) view.findViewById(R.id.toggleButton);

        mobileDataSwitch.setChecked(getDataFromSharedPref(getString(R.string.mobile_def)));
        batterySwitch.setChecked(getDataFromSharedPref(getString(R.string.battery_def)));
        locationSwitch.setChecked(getDataFromSharedPref(getString(R.string.loc_def)));

        mobileDataSwitch.setOnCheckedChangeListener(this);
        batterySwitch.setOnCheckedChangeListener(this);

        compToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TransitionDrawable transition = (TransitionDrawable) compToggle.getBackground();
                    transition.startTransition(200);
                    ((MainActivity) getActivity()).startComputation();
                } else {
                    TransitionDrawable transition = (TransitionDrawable) compToggle.getBackground();
                    transition.reverseTransition(200);
                    ((MainActivity) getActivity()).stopComputation();
                }
            }
        });

        chart = (LineChart) view.findViewById(R.id.chart_daily_statistics);
        entries = new ArrayList<>();
        labels = new ArrayList<>();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addDataEntries();
        setUpChart();
    }

    private boolean getDataFromSharedPref(String name) {
        return sharedPref.getBoolean(name, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        List<String> comps = Computation.getCompNames(db.getActiveComps());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, comps);
        ListView listView = (ListView) getView().findViewById(R.id.comp_list);
        listView.setAdapter(adapter);

        compToggle.setChecked(((MainActivity) getActivity()).isRunning());


    }

    private void setUpChart() {
        LineDataSet dataset = new LineDataSet(entries, "Number of Jobs Solved/Hour");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setDrawCubic(false);
        dataset.setValueFormatter(new YAxisValueFormatterToInt());
        dataset.setValueTextSize(10);
        dataset.setColor(Color.WHITE);
        LineData data = new LineData(labels, dataset);
        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        YAxis yAxis = chart.getAxisLeft();
        chart.getAxisRight().setTextColor(Color.TRANSPARENT);
        yAxis.setTextColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setScaleYEnabled(true);
        chart.setVisibleXRange(4);
        chart.setHorizontalScrollBarEnabled(true);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setValueFormatter(new YAxisValueFormatterToInt());
        chart.setDescription("");
        chart.setClickable(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addDataEntries() {
        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        Cursor cursor = db.readDataFromJobTable();
        int indexTime = cursor.getColumnIndex(TworkDBHelper.TABLE_JOB_START_TIME);
//        db.addJob(new Random().nextInt(),44, 2313,System.currentTimeMillis(),344343,4324334);
//        db.addJob(new Random().nextInt(),43, 2313,System.currentTimeMillis(),344343,4324334);
        Log.v("index", "" + indexTime);
        int i = 0;
        int nr = 0;
        String currentLocalTime = "00:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
       // String currentLocalTime = formatter2.format(new Date());
        String todayDay = formatter.format(new Date(System.currentTimeMillis()));
        cursor.moveToFirst();
        Log.v("Today", "" + todayDay);

        if (cursor.moveToFirst()) {
            do {
                long value = Long.parseLong(cursor.getString(indexTime));
                String localTime = formatter.format(new Date(value));
                Log.v("localTime", "" + localTime);
                if (localTime.equals(todayDay)) {
                    String localHour = formatter2.format(new Date(value));
                    Log.v("localTime", "" + localHour);
                    if ((localHour.substring(0, 2)).equals((currentLocalTime.substring(0, 2)))) {
                        nr++;
                    } else {
                        entries.add(new Entry(nr, i));
                        labels.add(currentLocalTime.substring(0, 2));
                        i++;
                        Log.v("Numbers", "" + nr);
                        nr = 1;
                        currentLocalTime = localHour;
                        Log.v("currentLocalTime", "" + currentLocalTime);
                    }
                }
            } while (cursor.moveToNext());
        }
        Log.v("cursor", "entry" + nr);
        entries.add(new Entry(nr, i));
        labels.add(currentLocalTime.substring(0,2));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor =
                getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE).edit();
        switch (buttonView.getId()) {
            case R.id.mobileDataSwitch:
                editor.putBoolean(getString(R.string.mobile_def), isChecked);
                editor.apply();
                break;
            case R.id.batterySwitch:
                editor.putBoolean(getString(R.string.battery_def), isChecked);
                editor.apply();
                break;
        }

    }
}
