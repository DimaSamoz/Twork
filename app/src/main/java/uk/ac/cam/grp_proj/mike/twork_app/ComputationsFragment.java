package uk.ac.cam.grp_proj.mike.twork_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import uk.ac.cam.grp_proj.mike.twork_service.CompService;

/**
 * Created by Dima on 28/01/16.
 */
public class ComputationsFragment extends ListFragment {

    ListView listView;
    List<Computation> unfinishedComps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_computations, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = getListView();

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).enteredMenu();
                getActivity().setTitle("Add new computation");
                AddCompFragment settingsFragment = new AddCompFragment();
                FragmentTransaction fragTran = getActivity().getSupportFragmentManager().beginTransaction();
                fragTran.setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_right);
                fragTran.replace(R.id.fragment_container, settingsFragment).addToBackStack("Computations").commit();

            }
        });

        // TODO Temporary hardcoded values
        unfinishedComps = TworkDBHelper.getHelper(getContext()).getUnfinishedComps();
        List<String> unfinishedCompNames = Computation.getCompNames(unfinishedComps);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, unfinishedCompNames);
        setListAdapter(adapter);

        registerForContextMenu(listView);

    }


    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((MainActivity) getActivity()).exitedMenu();
        getActivity().setTitle("Computations");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.comp_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.pause_comp:
                Log.i("comp", "pause");
                Computation toPause = unfinishedComps.get(info.position);
                toPause.setStatus(TworkDBHelper.COMP_STATUS_WAITING);
                toPause.flushToDatabase(TworkDBHelper.getHelper(getContext()));
                return true;
            case R.id.remove_comp:
                Log.i("comp", "remove");
                Computation toRemove = unfinishedComps.get(info.position);
                TworkDBHelper.getHelper(getContext()).removeComputation(toRemove);
                CompService.updateComps();
                unfinishedComps.remove(toRemove);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Computation.getCompNames(unfinishedComps));
                setListAdapter(adapter);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
