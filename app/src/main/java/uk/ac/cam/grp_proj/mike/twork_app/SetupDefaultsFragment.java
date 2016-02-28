package uk.ac.cam.grp_proj.mike.twork_app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dima on 09/02/16.
 */
public class SetupDefaultsFragment extends Fragment implements View.OnClickListener {

    private CheckBox mobileDataSwitch;
    private CheckBox batterySwitch;
    private CheckBox locationSwitch;
    private LinearLayout batterySliderLayout;
    private LinearLayout locationButtonLayout;

    private SeekBar batterySlider;
    private Button batteryInfo;
    private TextView batteryLimit;
    private Button locationButton;

    private SharedPreferences sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_defaults, container, false);

        mobileDataSwitch = (CheckBox) view.findViewById(R.id.mobile);
        batterySwitch= (CheckBox) view.findViewById(R.id.battery);
        locationSwitch = (CheckBox) view.findViewById(R.id.location);

        batterySliderLayout = (LinearLayout) view.findViewById(R.id.batt_limit);
        locationButtonLayout = (LinearLayout) view.findViewById(R.id.loc_charging);

        batterySlider = (SeekBar) view.findViewById(R.id.batt_limit_slider);
        batteryInfo = (Button) view.findViewById(R.id.batt_info);
        batteryLimit = (TextView) view.findViewById(R.id.batt_progress);
        locationButton = (Button) view.findViewById(R.id.add_loc);

        ViewGroup layout = (ViewGroup) view.findViewById(R.id.frame_layout);
        LayoutTransition layoutTransition = layout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);

        mobileDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(getString(R.string.mobile_def), isChecked);
            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(getString(R.string.loc_def), isChecked);
                locationButtonLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editSharedPreference(getString(R.string.battery_def), isChecked);
                batterySliderLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        batteryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "When charge gets below this limit, all computations will pause", Toast.LENGTH_LONG).show();
            }
        });

        batterySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = String.valueOf(progress) + "%";
                batteryLimit.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(getString(R.string.batt_lim), seekBar.getProgress());
                editor.apply();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment charging_points = new ManageChargingPointsFragment();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_right)
                        .replace(R.id.setup_fragment_container, charging_points)
                        .commit();
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
                .setCustomAnimations(R.anim.fade_in, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.fade_out)
                .replace(R.id.setup_fragment_container, setupFinalFragment)
                .addToBackStack("Defaults")
                .commit();
    }

    private void editSharedPreference(String name, boolean value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(name, value);
        editor.apply();

    }
}
