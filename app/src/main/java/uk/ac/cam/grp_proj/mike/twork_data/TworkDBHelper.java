package uk.ac.cam.grp_proj.mike.twork_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by laura on 01/02/16.
 */
public class TworkDBHelper extends SQLiteOpenHelper {

    private static TworkDBHelper instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tworkDB.db";

    public static final String TABLE_COMPUTATION_TABLE_NAME = "computation";
    public static final String TABLE_JOB_TABLE_NAME = "job";

    public static final String TABLE_COMPUTATION_ID = "_id";
    public static final String TABLE_COMPUTATION_NAME = "computation_name";
    public static final String TABLE_COMPUTATION_DESC = "computation_description";
    public static final String TABLE_COMPUTATION_TOPICS = "computation_topics";
    public static final String TABLE_COMPUTATION_STATUS = "computation_status";

    public static final String TABLE_JOB_ID = "_id";
    public static final String TABLE_JOB_COMPUTATION_ID = "job_computation_id";
    public static final String TABLE_JOB_START_TIME = "job_start_time";

    public static final String COMP_STATUS_ACTIVE = "active";
    public static final String COMP_STATUS_PAUSED = "paused";
    public static final String COMP_STATUS_COMPLETE = "complete";

    // Creating the db and tables
    private static final String SQL_CREATE_ENTRIES_COMPUTATION_TABLE =
            "CREATE TABLE " + TABLE_COMPUTATION_TABLE_NAME + " (" +
                    TABLE_COMPUTATION_ID + " TEXT PRIMARY KEY," +
                    TABLE_COMPUTATION_NAME + " TEXT," +
                    TABLE_COMPUTATION_DESC + " TEXT, " +
                    TABLE_COMPUTATION_TOPICS + " TEXT, " +
                    TABLE_COMPUTATION_STATUS + " TEXT" +" );";

    private static final String SQL_CREATE_ENTRIES_JOB_TABLE =
            "CREATE TABLE " + TABLE_JOB_TABLE_NAME + " (" +
                    TABLE_JOB_ID + " TEXT PRIMARY KEY, " +
                    TABLE_JOB_COMPUTATION_ID + " TEXT, " +
                    TABLE_JOB_START_TIME + " DATETIME " + " );";

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
        db.execSQL(SQL_CREATE_ENTRIES_COMPUTATION_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_JOB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_ENTRIES_COMPUTATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_ENTRIES_JOB_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Add computation to computation table
    public void addComputation(String id,String name, String desc, String topics, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_COMPUTATION_ID,id);
        values.put(TABLE_COMPUTATION_NAME,name);
        values.put(TABLE_COMPUTATION_DESC,desc);
        values.put(TABLE_COMPUTATION_TOPICS,topics);
        values.put(TABLE_COMPUTATION_STATUS,status);

        db.insert(TABLE_COMPUTATION_TABLE_NAME, null, values);
    }

    public void removeComputation(Computation comp) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_COMPUTATION_TABLE_NAME, TABLE_COMPUTATION_ID + "= '" + comp.getId() + "'", null);
    }

    // Add job to job table
    public void addJob(long jobId, String computationId, long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_JOB_ID, jobId);
        values.put(TABLE_JOB_COMPUTATION_ID, computationId);
        values.put(TABLE_JOB_START_TIME, startTime);

        db.insertWithOnConflict(TABLE_JOB_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Computation> getActiveComps() {
        return queryComputations(TABLE_COMPUTATION_STATUS + " = '" + COMP_STATUS_ACTIVE + "'");
    }

    public List<Computation> getSelectedComps() {
        return queryComputations(TABLE_COMPUTATION_STATUS + " <> '" + COMP_STATUS_COMPLETE + "'");
    }

    public Map<String, Integer> getJobCounts() {

        SQLiteDatabase db = this.getReadableDatabase();
        String jobQuery =
                "SELECT "+ TABLE_COMPUTATION_NAME +", COUNT(" + TABLE_JOB_TABLE_NAME + "." + TABLE_JOB_ID + ") FROM " + TABLE_COMPUTATION_TABLE_NAME + ", " + TABLE_JOB_TABLE_NAME + " WHERE " + TABLE_COMPUTATION_TABLE_NAME + "." + TABLE_COMPUTATION_ID + " = " + TABLE_JOB_COMPUTATION_ID + " GROUP BY " + TABLE_COMPUTATION_NAME + ";";
        Cursor cursor = db.rawQuery(jobQuery, null);

        Map<String, Integer> jobCounts = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                String compName = cursor.getString(0);
                int jobCount = cursor.getInt(1);
                jobCounts.put(compName, jobCount);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jobCounts;
    }

    // Returns a list of computations based on the query
    // SELECT * FROM computation WHERE <attribute> = <value>
    private List<Computation> queryComputations(String whereClause) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "SELECT * FROM " + TABLE_COMPUTATION_TABLE_NAME +
                        " WHERE " + whereClause + ";";
        Cursor cursor = db.rawQuery(query, null);
        LinkedList<Computation> comps = new LinkedList<>();
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                // Create new computation from the record
                Computation comp = new Computation(
                        cursor.getString(0), // id
                        cursor.getString(1), // name
                        cursor.getString(2), // description
                        cursor.getString(3), // topics
                        cursor.getString(4)  // status
                );

                comps.add(comp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return comps;
    }


    public Cursor readDataFromComputationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPUTATION_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor readDataFromJobTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQueryOrdered = "SELECT  * FROM " + TABLE_JOB_TABLE_NAME + " ORDER BY " +
                TABLE_JOB_START_TIME + ";";
        Cursor cursor = db.rawQuery(selectQueryOrdered, null);
        return cursor;
    }
}
