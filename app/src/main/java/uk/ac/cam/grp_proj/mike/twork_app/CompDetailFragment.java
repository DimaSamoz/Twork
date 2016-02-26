package uk.ac.cam.grp_proj.mike.twork_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import uk.ac.cam.grp_proj.mike.twork_service.CompService;

/**
 * Created by Dima on 12/02/16.
 */
public class CompDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "uk.ac.cam.grp_proj.mike.twork.COMP_ID";
    public static final String ARG_COMPS_LIST = "uk.ac.cam.grp_proj.mike.twork.COMPS_LIST";

    // Constants for using specific computation lists
    public static final int LIST_ALL_COMPS = 1;
    public static final int LIST_SELECTED_COMPS = 2;
    public static final int LIST_ACTIVE_COMPS = 3;

    private Computation comp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Computation> comps = new LinkedList<>();
        Intent i = getActivity().getIntent();
        int listChoice = i.getIntExtra(ARG_COMPS_LIST, -1);

        if (listChoice >= 0) {

            switch (listChoice) {
                case LIST_ALL_COMPS: comps = CompService.getComputations(TworkDBHelper.getHelper(getContext()));
                    break;
                case LIST_SELECTED_COMPS: comps = TworkDBHelper.getHelper(getContext()).getSelectedComps();
                    break;
                case LIST_ACTIVE_COMPS: comps = TworkDBHelper.getHelper(getContext()).getActiveComps();
                    break;
            }
        }

        int index = i.getIntExtra(ARG_ITEM_ID, -1);

        if (index >= 0) {
            comp = comps.get(index);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comp_detail, container, false);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null) {
            String name = comp.getName();
            appBarLayout.setTitle(name);
        }

        if (comp != null) {
            ((TextView) view.findViewById(R.id.computation_detail)).setText(comp.getDescription());
            ((TextView) view.findViewById(R.id.comp_areas)).setText(comp.getTopics());

        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(comp.getName());
    }
}
