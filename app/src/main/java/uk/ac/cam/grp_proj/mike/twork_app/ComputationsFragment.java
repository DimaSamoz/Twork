package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
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

/**
 * Created by Dima on 28/01/16.
 */
public class ComputationsFragment extends ListFragment {

    ListView listView;
    List<Computation> selectedComps;

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
                ((MainActivity) getActivity()).enteredMenu();
                getActivity().setTitle("Add new computation");
                AddCompFragment settingsFragment = new AddCompFragment();
                FragmentTransaction fragTran = getActivity().getSupportFragmentManager().beginTransaction();
                fragTran.setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_right);
                fragTran.replace(R.id.fragment_container, settingsFragment).addToBackStack("Computations").commit();

            }
        });

        listView = getListView();

        selectedComps = TworkDBHelper.getHelper(getContext()).getSelectedComps();
        List<String> selectedCompNames = Computation.getCompNames(selectedComps);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, selectedCompNames);
        setListAdapter(adapter);

        markPaused();

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CompDetailActivity.class);
                intent.putExtra(CompDetailFragment.ARG_ITEM_ID, position);
                intent.putExtra(CompDetailFragment.ARG_COMPS_LIST, CompDetailFragment.LIST_SELECTED_COMPS);

                startActivity(intent);
            }
        });

    }

    private void markPaused() {

        listView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectedComps.size(); i++) {
                    if (selectedComps.get(i).getStatus().equals(TworkDBHelper.COMP_STATUS_PAUSED)) {
                        getViewByPosition(i, listView).setAlpha(0.5f);
                    }
                }
            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
            case R.id.pause_resume_comp:
                Computation target = selectedComps.get(info.position);
                String status = target.getStatus();
                if (status.equals(TworkDBHelper.COMP_STATUS_ACTIVE)) {
                    target.setStatus(TworkDBHelper.COMP_STATUS_PAUSED);
                    info.targetView.setAlpha(0.5f);
                } else if (status.equals(TworkDBHelper.COMP_STATUS_PAUSED)) {
                    target.setStatus(TworkDBHelper.COMP_STATUS_ACTIVE);
                    info.targetView.setAlpha(1);
                }
                target.flushToDatabase(TworkDBHelper.getHelper(getContext()));
                return true;
            case R.id.remove_comp:
                Computation toRemove = selectedComps.get(info.position);
                TworkDBHelper.getHelper(getContext()).removeComputation(toRemove);
                selectedComps.remove(toRemove);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Computation.getCompNames(selectedComps));
                setListAdapter(adapter);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                markPaused();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
