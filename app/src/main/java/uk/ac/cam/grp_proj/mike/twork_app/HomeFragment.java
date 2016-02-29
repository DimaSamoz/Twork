package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import uk.ac.cam.grp_proj.mike.twork_service.CompService;
import uk.ac.cam.grp_proj.mike.twork_service.JobFetcher;


/**
 * Created by Dima on 28/01/16.
 */
public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Switch mobileDataSwitch;
    private Switch batterySwitch;
    private Switch locationSwitch;
    private ToggleButton compToggle;
    private SharedPreferences sharedPref;
    private ListView listView;
    private LineChart chart;
    private ArrayList<Entry> entries;
    private ArrayList<String> labels;
    List<Computation> activeComps;

    private BroadcastReceiver compStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CompService.COMP_PAUSED)) {
                // Computation paused
                compToggle.setText("Paused");
            } else if (intent.getAction().equals(CompService.COMP_RESUMED)) {
                // Computation resumed
                compToggle.setText("Running");
            }
        }
    };

    private BroadcastReceiver jobStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String compID = intent.getStringExtra(JobFetcher.COMP_NAME);
            if (action.equals(JobFetcher.JOB_RECEIVED)) {
                updateRow(compID, false);
            } else if (action.equals(JobFetcher.JOB_DONE)) {
                updateRow(compID, true);
            }
        }
    };

    private void updateRow(final String compID, final boolean jobDone) {

        listView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < activeComps.size(); i++) {
                    if (activeComps.get(i).getId().equals(compID)) {
                        String compName = activeComps.get(i).getName();
                        TextView item = ((TextView) getViewByPosition(i, listView));
                        if (!jobDone) {
                            // job is received
                            String newText = compName + " | Job received";
                            item.setText(newText);
                        } else {
                            // job is done - received text needs to be removed
                            String newText = compName + " | Job done";
                            item.setText(newText);

                        }
                    }
                }
            }
        });
    }

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
        IntentFilter compStatusFilter = new IntentFilter();
        compStatusFilter.addAction(CompService.COMP_PAUSED);
        compStatusFilter.addAction(CompService.COMP_RESUMED);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(compStatusReceiver, compStatusFilter);

        IntentFilter jobStatusFilter = new IntentFilter();
        jobStatusFilter.addAction(JobFetcher.JOB_RECEIVED);
        jobStatusFilter.addAction(JobFetcher.JOB_DONE);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(jobStatusReceiver, jobStatusFilter);

        addDataEntries();
        setUpChart();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(compStatusReceiver);
    }

    private boolean getDataFromSharedPref(String name) {
        return sharedPref.getBoolean(name, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        activeComps = db.getActiveComps();
        List<String> compNames = Computation.getCompNames(activeComps);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, compNames);
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
        int i = 0;
        int nr = 0;
        String currentLocalTime = "00:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        // String currentLocalTime = formatter2.format(new Date());
        String todayDay = formatter.format(new Date(System.currentTimeMillis()));
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                long value = Long.parseLong(cursor.getString(indexTime));
                String localTime = formatter.format(new Date(value));
                if (localTime.equals(todayDay)) {
                    String localHour = formatter2.format(new Date(value));
                    if ((localHour.substring(0, 2)).equals((currentLocalTime.substring(0, 2)))) {
                        nr++;
                    } else {
                        entries.add(new Entry(nr, i));
                        labels.add(currentLocalTime.substring(0, 2));
                        i++;
                        nr = 1;
                        currentLocalTime = localHour;
                    }
                }
            } while (cursor.moveToNext());
        }
        entries.add(new Entry(nr, i));
        labels.add(currentLocalTime.substring(0, 2));
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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
