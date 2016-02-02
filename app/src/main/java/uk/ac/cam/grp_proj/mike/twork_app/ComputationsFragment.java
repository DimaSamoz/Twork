package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Dima on 28/01/16.
 */
public class ComputationsFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_computations, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setTitle("Add new computation");
                AddCompFragment settingsFragment = new AddCompFragment();
                FragmentTransaction fragTran = getActivity().getSupportFragmentManager().beginTransaction();
                fragTran.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragTran.replace(R.id.fragment_container, settingsFragment).addToBackStack("Computations").commit();

            }
        });

        // TODO Temporary hardcoded values
        String[] comps = {"SETI@home", "Galaxy Zoo", "Leiden Classical", "GIMPS", "Compute For Humanity"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, comps);
        setListAdapter(adapter);
//        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle("Computations");
    }
}
