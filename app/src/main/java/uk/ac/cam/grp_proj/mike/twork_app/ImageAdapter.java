package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by laura on 04/02/16.
 */
public class ImageAdapter extends BaseAdapter {
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_star_black_24dp, R.drawable.ic_star_black_24dp,
            R.drawable.ic_star_black_24dp, R.drawable.ic_star_black_24dp};

    private String[] name = {"First Job","First 10 Jobs","First 50 Jobs","First 100 Jobs"};
    private int[] values = {1,10,50,100};

    private Context mContext;
    private long numberOfJobs = 0;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.textView1);
            imageView = (ImageView)grid.findViewById(R.id.imageView1);
            textView.setText(name[position]);
            imageView.setImageResource(mThumbIds[position]);
            addDataEntries();
            if (numberOfJobs >= values[position]) imageView.setColorFilter(Color.parseColor("#FFEB3B"));
        } else {
            grid = (View) convertView;
        }
        grid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Make a random message appear: Congrats if the achievement is yellow and
                // Keep working if it is grey
                if (numberOfJobs >= values[position]) {
                    if (values[position] > 1) Toast.makeText(v.getContext(), "Congratulations! You have computed " +
                            values[position] + " jobs", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(v.getContext(), "Congratulations! You have computed " +
                            values[position] + " job", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(v.getContext(), "Not there yet. Keep tworking!", Toast.LENGTH_SHORT).show();
            }
        });


        return grid;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private void addDataEntries() {
        numberOfJobs = 0;
        TworkDBHelper db = TworkDBHelper.getHelper(mContext);
        Cursor cursor = db.readDataFromJobTable();
        cursor.moveToFirst();
        do {
            numberOfJobs++;
        }while(cursor.moveToNext());
        Log.d("Number of jobs", " "  + numberOfJobs);
    }
}
