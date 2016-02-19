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
import android.widget.EditText;

/**
 * Created by Dima on 04/02/16.
 */
public class SetupIntroFragment extends Fragment implements View.OnClickListener {

    EditText text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_intro, container, false);

        text = (EditText) view.findViewById(R.id.setup_name);

        Button nextButton = (Button) view.findViewById(R.id.next_button1);
        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Log.i("comp", "onClick");
        switch (v.getId()) {
            case R.id.next_button1: {
                Log.i("comp", "entered");

                String name = String.valueOf(text.getText());

                if (!name.equals("")) {
                    // User has entered their name

                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString(getString(R.string.user_name), name).apply();
                }

                SetupCompsFragment setupCompsFragment = new SetupCompsFragment();
                getActivity().getSupportFragmentManager().beginTransaction() // TODO fix animation
                        .setCustomAnimations(R.anim.fade_in, R.anim.exit_to_left)
                        .replace(R.id.setup_fragment_container, setupCompsFragment)
                        .commit();
            }
            break;
        }
    }
}
