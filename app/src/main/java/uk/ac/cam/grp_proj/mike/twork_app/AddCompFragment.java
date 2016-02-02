package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Dima on 29/01/16.
 */
public class AddCompFragment extends ListFragment {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // TODO Temporary hardcoded values
        String[] comps = {"DreamLab", "SETI@home", "Galaxy Zoo", "RNA World", "Malaria Control", "Leiden Classical", "GIMPS", "Electric Sheep", "DistributedDataMining", "Compute For Humanity"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, comps);
        setListAdapter(adapter);
//        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle("Add new computation");
    }
}
