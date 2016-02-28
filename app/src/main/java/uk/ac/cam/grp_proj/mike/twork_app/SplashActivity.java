package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;

    public void getStarted(View view) {

        // Access shared preferences and check if first launch flag is set or return true if it doesn't exist
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstLaunch = sharedPref.getBoolean(getString(R.string.first_launch), true);
        Intent i;

//        logo.setAnimation(null);

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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        logo = (ImageView) findViewById(R.id.logo);

        RotateAnimation anim = new RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(7000);

// Start animating the image
        logo.startAnimation(anim);

// Later.. stop the animation

        TextView txt = (TextView) findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "Museo 100.otf");
        txt.setTypeface(font);

        TextView welcome = (TextView) findViewById(R.id.welcome_text);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.user_name), "");

        if (!username.equals("")) {
            String text = "Welcome back, " + username + "!";
            Log.i("comp", text);
            welcome.setText(text);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
