package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;
import uk.ac.cam.grp_proj.mike.twork_service.CompService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Twork local database helper
    private TworkDBHelper mDB;

    // Bound to service flag
    private boolean mBound = false;

    // Computation service
    private CompService mService;

    private boolean isInsideMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        mDB = TworkDBHelper.getHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, homeFragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView header_text = (TextView) headerView.findViewById(R.id.header_text);
        Typeface font = Typeface.createFromAsset(getAssets(), "Museo 100.otf");
        header_text.setTypeface(font);

        // Bind to CompService
        Intent intent = new Intent(this, CompService.class);
        getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unbind from the service
        if (mBound) {
            getApplicationContext().unbindService(serviceConnection);
            mBound = false;
        }
    }

    // Defines callbacks for service binding, passed to bindService()
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to CompService, cast the IBinder and get CompService instance
            CompService.CompBinder binder = (CompService.CompBinder) service;
            mService = binder.getService();

            Log.i("Service", "bound");
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i("Service", "unbound");
            mBound = false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isInsideMenu) {
                super.onBackPressed();
            }
        }


    }


    public void startComputation() {
        if (mBound) {
            mService.startComputation();
        }
    }

    public void stopComputation() {
        if (mBound) {
            mService.stopComputation();
        }
    }

    public boolean isRunning() {
        return mService != null && mService.shouldBeRunning();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                Fragment visible = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                Fragment fragment = new Fragment();

                if (id == R.id.nav_home) {
                    setTitle("Home");
                    fragment = new HomeFragment();

                } else if (id == R.id.nav_comps) {
                    setTitle("Computations");
                    fragment = new ComputationsFragment();

                } else if (id == R.id.nav_stats) {
                    setTitle("Statistics");
                    fragment = new StatisticsFragment();

                } else if (id == R.id.nav_achievements) {
                    setTitle("Achievements");
                    fragment = new AchievementsFragment();

                } else if (id == R.id.nav_about) {
                    setTitle("About");
                    fragment = new SettingsFragment();

                }

                if (!visible.getClass().equals(fragment.getClass())) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out)
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            }
        }, 250);



        return true;
    }



    public void enteredMenu() { isInsideMenu = true; }
    public void exitedMenu() { isInsideMenu = false; }

}