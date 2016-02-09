package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    public void getStarted(View view) {

        // Access shared preferences and check if first launch flag is set or return true if it doesn't exist
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstLaunch = sharedPref.getBoolean(getString(R.string.first_launch), true);
        Intent i;

        if (firstLaunch) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.first_launch), false); // change this to TRUE and negate the if condition for one launch to see the setup screen every time
            editor.apply();

            i = new Intent(getApplicationContext(), SetupActivity.class);

        } else {
            i = new Intent(getApplicationContext(), MainActivity.class);
        }

        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
