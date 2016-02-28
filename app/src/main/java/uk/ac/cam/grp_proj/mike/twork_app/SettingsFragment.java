package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Dima on 28/01/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if(v != null) {
//            ListView lv = (ListView) v.findViewById(android.R.id.list);
//            lv.setPadding(0, 0, 0, 0);
//            ListView lv = getListView();
//            ViewGroup vg = (ViewGroup) lv.getParent();
//            vg.setPadding(0,0,0,0);
//            lv.setPaddingRelative(0, 0, 0, 0);


        }
        return v;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        setPreferencesFromResource(R.xml.preferences, "preferences");

    }
}
