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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//
////        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
                    fragment = new LineChartFragment();

                } else if (id == R.id.nav_achievements) {
                    setTitle("Achievements");
                    fragment = new AchievementsFragment();

                } else if (id == R.id.nav_settings) {
                    setTitle("Settings");
                    fragment = new SettingsFragment();

                } else if (id == R.id.nav_share) {
                    setTitle("Share");
                    fragment = new SocialFragment();

                } else if (id == R.id.nav_send) {

                }

                if (!visible.getClass().equals(fragment.getClass())) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out)
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            }
        }, 200);



        return true;
    }



    public void enteredMenu() { isInsideMenu = true; }
    public void exitedMenu() { isInsideMenu = false; }

}