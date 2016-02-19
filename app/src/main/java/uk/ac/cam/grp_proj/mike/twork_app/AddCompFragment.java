package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Dima on 29/01/16.
 */
public class AddCompFragment extends Fragment implements View.OnClickListener{

    private SetupCompsFragment sc = new SetupCompsFragment();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setup_comps, container, false);

        TextView textView = (TextView) view.findViewById(R.id.add_comps_title);
        textView.setText("Add new computations");

        Button button = (Button) view.findViewById(R.id.next_button2);
        button.setText("Add");
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sc.populateList(getContext(), getView(), R.id.fragment_container);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle("Add new computation");
    }

    @Override
    public void onClick(View v) {
        sc.addSelectedToDatabase(getContext().getApplicationContext());

        ComputationsFragment computationsFragment = new ComputationsFragment();
        getActivity().getSupportFragmentManager().beginTransaction() // TODO fix animation
                .setCustomAnimations(R.anim.fade_in, R.anim.exit_to_right)
                .replace(R.id.fragment_container, computationsFragment)
                .commit();
    }
}
