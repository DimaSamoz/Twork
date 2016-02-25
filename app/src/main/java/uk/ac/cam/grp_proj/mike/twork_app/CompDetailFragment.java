package uk.ac.cam.grp_proj.mike.twork_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import uk.ac.cam.grp_proj.mike.twork_service.CompService;

/**
 * Created by Dima on 12/02/16.
 */
public class CompDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "comp_id";
    private Computation comp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            comp = CompService.getComputations(TworkDBHelper.getHelper(getContext())).get(getArguments().getInt(ARG_ITEM_ID));
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
            Log.i("comp", name);
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
