package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dima on 27/02/16.
 */
public class ManageChargingPointsFragment extends Fragment {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_charging_points, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.loc_fab);
        getActivity().setTitle("Manage charging points");



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent(getContext(), AddLocationActivity.class);
                startActivity(mapsIntent);

            }
        });*/
    }
}
