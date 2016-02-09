package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import uk.ac.cam.grp_proj.mike.twork_data.Computation;
import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SetupIntroFragment siFragment = new SetupIntroFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.setup_fragment_container, siFragment).commit();

    }

    public void introToComps(View view) {
        EditText text = (EditText) findViewById(R.id.setup_name);

        String name = String.valueOf(text.getText());

        if (!name.equals("")) {
            // User has entered their name

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.user_name), name);
        }

        SetupCompsFragment setupCompsFragment = new SetupCompsFragment();
        getSupportFragmentManager().beginTransaction() // TODO fix animation
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.setup_fragment_container, setupCompsFragment)
                .commit();
    }

    public void compsToDefaults(View view) {
        List<Computation> selected = SetupCompsFragment.getSelectedComps();

        TworkDBHelper db = TworkDBHelper.getHelper(this);

        for (Computation comp :
                selected) {
            //db.addComputation(comp.getId(), comp.getName(), "active", System.currentTimeMillis(), 0);
            Log.i("SQLite", "added" + comp.getName());
        }

        SetupDefaultsFragment setupDefaultsFragment = new SetupDefaultsFragment();
        getSupportFragmentManager().beginTransaction() // TODO fix animation
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.setup_fragment_container, setupDefaultsFragment)
                .commit();

        
    }

    public void defaultsToFinal(View view) {

        SetupFinalFragment setupFinalFragment = new SetupFinalFragment();
        getSupportFragmentManager().beginTransaction() // TODO fix animation
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.setup_fragment_container, setupFinalFragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
    }
}
