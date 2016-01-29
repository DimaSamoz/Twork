package uk.ac.cam.grp_proj.mike.twork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Dima on 28/01/16.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
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

//        getListView().setOnItemClickListener(this);
    }
}
