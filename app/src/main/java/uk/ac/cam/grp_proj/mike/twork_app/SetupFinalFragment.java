package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Dima on 09/02/16.
 */
public class SetupFinalFragment extends Fragment {

    private ProgressBar spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_final, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.progressBar);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinner.setVisibility(View.VISIBLE);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(getContext().getApplicationContext(),
                            MainActivity.class);
                    startActivity(i);
                }
            }
        };
        welcomeThread.start();

    }
}
