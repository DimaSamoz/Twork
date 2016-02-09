package uk.ac.cam.grp_proj.mike.twork_app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by laura on 04/02/16.
 */
public class ImageAdapter extends BaseAdapter {
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_star_black_24dp, R.drawable.ic_star_black_24dp,
            R.drawable.ic_star_black_24dp, R.drawable.ic_star_black_24dp};

    private String[] name = {"lalal","sdsd","sasa","fdfd"};

    private Context mContext;

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
            imageView.setColorFilter(Color.parseColor("#FFEB3B"));
        } else {
            grid = (View) convertView;
        }
        grid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Make a random message appear: Congrats if the achievement is yellow and
                // Keep working if it is grey
                Toast.makeText(v.getContext(), "You Clicked " + name[position], Toast.LENGTH_SHORT).show();
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
}
