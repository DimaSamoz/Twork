package uk.ac.cam.grp_proj.mike.twork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Dima on 31/01/16.
 */
public class TworkDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TworkDB";
    public static final String COMPS_TABLE_NAME = "comp";
    public static final String COMPS_COLUMN_ID = "id";
    public static final String COMPS_COLUMN_NAME = "name";
    public static final String COMPS_COLUMN_STATUS = "status";
    public static final String COMPS_COLUMN_START = "start";
    public static final String COMPS_COLUMN_END = "end";


    public TworkDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
