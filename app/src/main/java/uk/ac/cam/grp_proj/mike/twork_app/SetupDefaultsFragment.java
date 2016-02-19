package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Dima on 09/02/16.
 */
public class SetupDefaultsFragment extends Fragment implements View.OnClickListener {

    private CheckBox mobileDataSwitch;
    private CheckBox batterySwitch;
    private CheckBox locationSwitch;
    private SharedPreferences sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_defaults, container, false);

        mobileDataSwitch = (CheckBox) view.findViewById(R.id.mobile);
        batterySwitch= (CheckBox) view.findViewById(R.id.battery);
        locationSwitch = (CheckBox) view.findViewById(R.id.location);

        sharedPrefs = getActivity().getSharedPreferences(String.valueOf(R.string.shared_preference), Context.MODE_PRIVATE);

        mobileDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(String.valueOf(R.string.mobile_def), isChecked);
                Log.i("comp_save_m", String.valueOf(isChecked));
            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(String.valueOf(R.string.loc_def), isChecked);
                Log.i("comp_save_l", String.valueOf(isChecked));
            }
        });

        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(String.valueOf(R.string.battery_def), isChecked);
                Log.i("comp_save+b", String.valueOf(isChecked));
            }
        });

        Button nextButton = (Button) view.findViewById(R.id.next_button3);

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        SetupFinalFragment setupFinalFragment = new SetupFinalFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.exit_to_left)
                .replace(R.id.setup_fragment_container, setupFinalFragment)
                .commit();
    }

    private void editSharedPreference(String name, boolean value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(name, value);
        editor.apply();

    }
}
