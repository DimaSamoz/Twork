package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Dima on 28/01/16.
 */
public class SettingsFragment extends Fragment {

    Button redoSetup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        redoSetup = (Button) view.findViewById(R.id.redo_setup);

        redoSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetupActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
