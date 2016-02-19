package uk.ac.cam.grp_proj.mike.twork_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by laura on 01/02/16.
 */
public class TworkDBHelper extends SQLiteOpenHelper {

    private static TworkDBHelper instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tworkDB.db";

    public static final String TABLE_COMPUTATION_TABLE_NAME = "computation";
    public static final String TABLE_JOB_TABLE_NAME = "job";

    public static final String TABLE_COMPUTATION_NAME = "computation_name";
    public static final String TABLE_COMPUTATION_ID = "_id";
    public static final String TABLE_COMPUTATION_STATUS = "computation_status";
    public static final String TABLE_COMPUTATION_START_TIME = "computation_start_time";
    public static final String TABLE_COMPUTATION_END_TIME = "computation_end_time";

    public static final String TABLE_JOB_COMPUTATION_ID = "job_computation_id";
    public static final String TABLE_JOB_ID = "_id";
    public static final String TABLE_JOB_DURATION = "job_duration";
    public static final String TABLE_JOB_START_TIME = "job_start_time";
    public static final String TABLE_JOB_NUMBER_OF_BYTES_SENT = "job_number_of_bytes_sent";
    public static final String TABLE_JOB_NUMBER_OF_BYTES_ANALYSED = "job_number_of_bytes_analysed";

    private static final String SQL_CREATE_ENTRIES_COMPUTATION_TABLE =
            "CREATE TABLE " + TABLE_COMPUTATION_TABLE_NAME + " (" +
                    TABLE_COMPUTATION_NAME + " TEXT," +
                    TABLE_COMPUTATION_ID + " INTEGER PRIMARY KEY," +
                    TABLE_COMPUTATION_STATUS + " TEXT," +
                    TABLE_COMPUTATION_START_TIME + " TEXT," +
                    TABLE_COMPUTATION_END_TIME + " DATETIME" + " );";

    private static final String SQL_CREATE_ENTRIES_JOB_TABLE =
            "CREATE TABLE " + TABLE_JOB_TABLE_NAME + " (" +
                    TABLE_JOB_ID + " INTEGER PRIMARY KEY, " +
                    TABLE_JOB_COMPUTATION_ID + " INTEGER, " +
                    TABLE_JOB_DURATION + " INTEGER, " +
                    TABLE_JOB_START_TIME + " DATETIME, " +
                    TABLE_JOB_NUMBER_OF_BYTES_SENT + " INTEGER," +
                    TABLE_JOB_NUMBER_OF_BYTES_ANALYSED + " INTEGER" + " );";

    private TworkDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TworkDBHelper getHelper(Context context) {
        if (instance == null) {
            instance = new TworkDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_CREATE_ENTRIES_COMPUTATION_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_JOB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_ENTRIES_COMPUTATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_ENTRIES_JOB_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addComputation(int id,String name, String status,long startTime,
                               long endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_COMPUTATION_NAME,name);
        values.put(TABLE_COMPUTATION_ID,id);
        values.put(TABLE_COMPUTATION_STATUS,status);
        values.put(TABLE_COMPUTATION_START_TIME,startTime);
        values.put(TABLE_COMPUTATION_END_TIME, endTime);

        db.insert(TABLE_COMPUTATION_TABLE_NAME, null, values);
        db.close();
    }

    public void addJob( int computationId, int jobId, long duration, long startTime,
                       long numberOfBytesSent,long numberOfBytesAnalysed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_JOB_ID, jobId);
        values.put(TABLE_JOB_COMPUTATION_ID, computationId);
        values.put(TABLE_JOB_DURATION, duration);
        values.put(TABLE_JOB_START_TIME, startTime);
        values.put(TABLE_JOB_NUMBER_OF_BYTES_SENT, numberOfBytesSent);
        values.put(TABLE_JOB_NUMBER_OF_BYTES_ANALYSED, numberOfBytesAnalysed);

        db.insert(TABLE_JOB_TABLE_NAME, null, values);
      //  db.close();
    }

    public Cursor readDataFromComputationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPUTATION_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor readDataFromJobTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_JOB_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
}
