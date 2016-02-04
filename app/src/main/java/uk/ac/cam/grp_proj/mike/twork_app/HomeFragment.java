package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by Dima on 28/01/16.
 */
public class HomeFragment extends Fragment {

    private Switch mobileDataSwitch;
    private Switch batterySwitch;
    private Switch locationSwitch;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preference), Context.MODE_PRIVATE);
        mobileDataSwitch = (Switch) view.findViewById(R.id.mobileDataSwitch);
        batterySwitch = (Switch) view.findViewById(R.id.batterySwitch);
        locationSwitch = (Switch) view.findViewById(R.id.locationSwitch);
        mobileDataSwitch.setChecked(getDataFromSharedPref("mobileDataSwitch"));
        batterySwitch.setChecked(getDataFromSharedPref("batterSwitch"));
        locationSwitch.setChecked(getDataFromSharedPref("locationSwitch"));

        mobileDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                    editSharedPreference("mobileDataSwitch", true);
                } else {
                    //TODO
                    editSharedPreference("mobileDataSwitch", false);
                }

            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                    editSharedPreference("locationSwitch", true);
                } else {
                    //TODO
                    editSharedPreference("locationSwitch", false);
                }
            }
        });

        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                    editSharedPreference("batterSwitch", true);
                } else {
                    //TODO
                    editSharedPreference("batterSwitch", false);
                }
            }
        });

        return view;
    }

    private boolean getDataFromSharedPref(String name) {
        boolean value = sharedPref.getBoolean(name, false);
        return value;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Temporary hardcoded values
        ArrayList<String> comps = new ArrayList<>();
        comps.add("SETI@home");
        comps.add("Prime Search");
        comps.add("Ray Tracing");
        comps.add("Compute π");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, comps);
        ListView listView = (ListView) getView().findViewById(R.id.comp_list);
        listView.setAdapter(adapter);

    }

    private void editSharedPreference(String name, boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(name, value);
        editor.commit();

    }
}
