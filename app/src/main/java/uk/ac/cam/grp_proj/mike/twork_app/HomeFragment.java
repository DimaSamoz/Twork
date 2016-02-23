package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by Dima on 28/01/16.
 */
public class HomeFragment extends Fragment {

    private Switch mobileDataSwitch;
    private Switch batterySwitch;
    private Switch locationSwitch;
    private ToggleButton compToggle;
    private SharedPreferences sharedPref;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.comp_list);


        sharedPref = getActivity().getSharedPreferences(String.valueOf(R.string.shared_preference), Context.MODE_PRIVATE);
        mobileDataSwitch = (Switch) view.findViewById(R.id.mobileDataSwitch);
        batterySwitch = (Switch) view.findViewById(R.id.batterySwitch);
        locationSwitch = (Switch) view.findViewById(R.id.locationSwitch);
        compToggle = (ToggleButton) view.findViewById(R.id.toggleButton);

        Log.i("comp_34", String.valueOf(getDataFromSharedPref(String.valueOf(R.string.battery_def))));

        mobileDataSwitch.setChecked(getDataFromSharedPref(String.valueOf(R.string.mobile_def)));
        batterySwitch.setChecked(getDataFromSharedPref(String.valueOf(R.string.battery_def)));
        locationSwitch.setChecked(getDataFromSharedPref(String.valueOf(R.string.loc_def)));


        compToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((MainActivity) getActivity()).startComputation();
                } else {
                    ((MainActivity) getActivity()).stopComputation();
                }
            }
        });

        return view;
    }

    private boolean getDataFromSharedPref(String name) {
        return sharedPref.getBoolean(name, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Temporary hardcoded values



//        ArrayList<String> comps = new ArrayList<>();
//        comps.add("SETI@home");
//        comps.add("Prime Search");
//        comps.add("Ray Tracing");
//        comps.add("Compute π");

        TworkDBHelper db = TworkDBHelper.getHelper(getContext());
        List<String> comps = db.getActiveComps();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, comps);
        listView.setAdapter(adapter);

    }


}
