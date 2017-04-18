package widac.cis350.upenn.edu.widac;

import android.util.Log;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.models.Sample;
import widac.cis350.upenn.edu.widac.models.SampleStaging;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 * =============================================
 * Store the ids of entries that have been modified durring current session
 * Also add
 */

public class Session {
    private static DBConnection DBC = new DBConnection();
    private static Set<String> entries =  new HashSet<String>();

    // Temp
    private static Callback<Sample> tempCB;
    private static String currId;
    // private static List<Set<String>> pastSessions = new LinkedList<Set<String>>();

    private Session() {}

    /*
        Session instance methods
     */
    public static void newSession() {
        entries.clear();
    }

    // Return an unmodifiable of all session created during current use
    //public static List<Set<String>> getPastSessions() {
    //    return Collections.unmodifiableList(pastSessions);
    //}

    // Add method for switching to a past session?

    /*
        ENTRY METHODS
     */
    public static boolean  addEntry(String id) {
        entries.add(id);
        return true;
    }

    public static boolean updateEntry(String oldID, String newID) {
        if (entries.contains(oldID)) {
            entries.remove(oldID);
            entries.add(newID);
            return true;
        }
        return false;
    }

    public static Set<String> getCurrentSessionIDs() { // Consider rename to getCurrentSession
        return entries;
    }

    /*
        DATABASE INTERACTIONS
     */
    // CHANGING CONNECTION
    public static DBConnection getDBC() { return DBC; };
    public static void changeDBC(String newDBC) {
        // Somehow update DBC
    }
    // PULLING METHODS
    public static Set<Sample> pullFromDB() {
        Set<Sample> samples = new HashSet<Sample>();
        // Check for elements that were deleted?
        for (String id: entries) {
            samples.add(DBC.retrieveSample(id));
        }
        return samples;
    }

    public static void asyncPullFromDB(Callback<Sample> callback) {
        Set<Sample> samples = new HashSet<Sample>();

        // Begin staging samples
        // Set to execute callback when all entries received
        Log.d("Session", "Begin Staging: " + entries.size() + " to stage");
        Log.d("Session", "Callback: " + callback);
        SampleStaging.beginStaging(entries.size(), callback);
        Callback<Sample> cb = SampleStaging.getStageCB();
        for (String id: entries) {
            DBC.getSample(id, cb);
        }
    }

    // Pull an entry from the database
    public static Sample pullNewEntryFromDB(String id) {
        // add entry to current session and return data
        Log.d("Session", "Id: " + id);
        Log.d("Session", "Session size: " + entries.size());

        // Add sample to session if exists
        Sample s = DBC.retrieveSample(id);
        if (s != null) {
            entries.add(id);
        }
        return s;
    }

    public static void asyncPullNewEntry(String id, Callback<Sample> callback) {
        Log.d("Session", "Id: " + id);
        Log.d("Session", "Session size: " + entries.size());
        currId = id;                    // Temporary workaround until composite id is used to query
        tempCB = callback;
        DBC.getSample(id, addEntry);
    }

    static Callback addEntry = new Callback<Sample>(){

        @Override
        public void onResponse(Call<Sample> call, Response<Sample> response) {
            int code = response.code();
            if (code == 200) {
                if (currId != null) {
                    entries.add(currId);
                    currId = null;
                    if (tempCB != null) {
                        call.clone().enqueue(tempCB);
                        tempCB = null;
                    }
                }
            } else {
                currId = null;
                tempCB = null;
                Log.d("DBConnection", "Did not work: " + String.valueOf(code));
            }
        }

        @Override
        public void onFailure(Call<Sample> call, Throwable t) {
            currId = null;
            tempCB = null;
            Log.d("DBConnection", "Get sample failure");
        }
    };
    // *********************************************************************************************
    // TESTING-ONLY METHOD TO POPULATE SESSION WITH DUMMY DATA
    public static void initalizeTest() {
        Log.d("Session", "initializeTest: initializing");
        entries.clear();
        entries.add("a1");
        entries.add("b1");
        entries.add("a2");
        entries.add("c1");
        entries.add("x1");
        entries.add("e1");
        entries.add("r2");
        entries.add("t1");
        entries.add("y1");
        entries.add("u1");
        entries.add("i2");
        entries.add("o1");
    }
}
