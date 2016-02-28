package uk.ac.cam.grp_proj.mike.twork_data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dima on 06/02/16.
 */
public class Computation {
    private String id;
    private String name;
    private String description;
    private String topics;
    private String status;

    public Computation(String id, String name, String description, String topics, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.topics = topics;
        this.status = status;
    }

    public Computation(String id) {
        // Mock computation to check for equality
        this.id = id;
        this.name = "";
        this.description = "";
        this.topics = "";
        this.status = "";
    }

    public static List<String> getCompNames(List<Computation> comps) {
        List<String> compNames = new LinkedList<>();
        for (Computation comp :
                comps) {
            compNames.add(comp.getName());
        }
        return compNames;
    }

    // Update database entry based on the fields in the computation
    public void flushToDatabase(TworkDBHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TworkDBHelper.TABLE_COMPUTATION_ID, id);
        values.put(TworkDBHelper.TABLE_COMPUTATION_NAME, name);
        values.put(TworkDBHelper.TABLE_COMPUTATION_DESC, description);
        values.put(TworkDBHelper.TABLE_COMPUTATION_TOPICS, topics);
        values.put(TworkDBHelper.TABLE_COMPUTATION_STATUS, status);


        db.update(
                TworkDBHelper.TABLE_COMPUTATION_TABLE_NAME,
                values,
                TworkDBHelper.TABLE_COMPUTATION_ID + " = " + id,
                null);

    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getTopics() {
        return topics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Computation that = (Computation) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
