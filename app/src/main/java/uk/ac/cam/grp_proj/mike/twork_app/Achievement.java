package uk.ac.cam.grp_proj.mike.twork_app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import uk.ac.cam.grp_proj.mike.twork_data.TworkDBHelper;

/**
 * Created by laura on 04/02/16.
 */
public class Achievement extends BaseAdapter {
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp,
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp,
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp,
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp,
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp,
            R.drawable.ic_grade_24dp, R.drawable.ic_grade_24dp};

    private String[] name = {"First Job","First 10 Jobs","First 50 Jobs","First 100 Jobs",
                            "First 500 Jobs","First 1000 Jobs","First 5000 Jobs",
                            "First 10000 Jobs", "First 15000 Jobs", "First 30000 Jobs",
                            "First 50000 Jobs",  "First 100000 Jobs"};
    private int[] values = {1,10,50,100,500,1000,5000,10000,15000,30000,50000,100000};

    private Context mContext;
    private long numberOfJobs = 0;

    public Achievement(Context c) {
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
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            imageView.setImageResource(mThumbIds[position]);
            addDataEntries();
            if (numberOfJobs >= values[position]) imageView.setColorFilter(Color.parseColor("#FFEB3B"));
        } else {
            grid = (View) convertView;
        }
        grid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (numberOfJobs >= values[position]) {
                    String obj_title;
                    if (numberOfJobs == 1) {
                        obj_title = "1 Job Completed!";
                    } else {
                        obj_title = values[position] + " Jobs Completed!";
                    }
                    Uri imageUri = Uri.parse("http://ec2-52-36-182-104.us-west-2.compute.amazonaws.com:9000/assets/twork_icon.jpg");

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://ec2-52-36-182-104.us-west-2.compute.amazonaws.com:9000/"))
                            .setContentTitle(obj_title)
                            .setContentDescription("Twork is a platform for distributed computation. Try it out now!")
                            .setImageUrl(imageUri)
                            .build();

                    ShareDialog.show((Activity) mContext, content);

                } else Toast.makeText(v.getContext(), "Not there yet. Keep tworking!", Toast.LENGTH_SHORT).show();
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
        } while(cursor.moveToNext());
        Log.d("Number of jobs", " "  + numberOfJobs);
    }
}
