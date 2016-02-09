package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by laura on 04/02/16.
 */
public class AchievementsFragment extends Fragment {

    @Override
    public GridView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GridView gridview = (GridView) inflater.inflate(R.layout.fragment_achievements, container, false);
        gridview.setAdapter(new ImageAdapter(getContext())) ;
        return gridview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
