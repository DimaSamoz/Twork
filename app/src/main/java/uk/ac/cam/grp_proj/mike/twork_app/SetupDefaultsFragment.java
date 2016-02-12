package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Dima on 09/02/16.
 */
public class SetupDefaultsFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_defaults, container, false);

        Button nextButton = (Button) view.findViewById(R.id.next_button3);

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        SetupFinalFragment setupFinalFragment = new SetupFinalFragment();
        getActivity().getSupportFragmentManager().beginTransaction() // TODO fix animation
                .setCustomAnimations(R.anim.fade_in, R.anim.exit_to_left)
                .replace(R.id.setup_fragment_container, setupFinalFragment)
                .commit();
    }
}
