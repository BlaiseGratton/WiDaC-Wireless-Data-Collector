package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothDevice;
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
 * Stores ids of artifacts worked with during app use
 * Can pull data relevant to artifacts looked at in current period
 * Stores instance of database connection
 * Follows Singleton class pattern
 */

public class Session {
    private static DBConnection DBC = new DBConnection();
    private static Set<String> entries =  new HashSet<String>();

    // Temp
    private static Callback<Sample> tempCB;
    private static String currId;

    public static String deviceName = null;

    public static String searchQuery;

    private Session() {}

    /*
        Session instance methods
     */
    public static void newSession() {
        entries.clear();
    }

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
    // Below method now defunct
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
    public static void asyncPullNewEntry(String id, Callback<Sample> callback) {
        Log.d("Session", "Id: " + id);
        Log.d("Session", "Session size: " + entries.size());
        currId = id;                    // Temporary workaround until composite id is used to query
        tempCB = callback;
        DBC.getSample(id, addEntry);
    }

    // Callback called on pull from database
    static Callback addEntry = new Callback<Sample>(){
        @Override
        public void onResponse(Call<Sample> call, Response<Sample> response) {
            int code = response.code();
            if (code == 200) {
                // If able to find the entry then add it to recovered entries
                if (currId != null) {
                    entries.add(currId);
                    currId = null;

                    // Make call to callback passed from caller if one exists
                    if (tempCB != null) {
                        call.clone().enqueue(tempCB);
                        tempCB = null;
                    }
                }
            } else {
                currId = null;
                tempCB = null;
                Log.d("Session:DBConnection", "Did not work: " + String.valueOf(code));
            }
        }

        @Override
        public void onFailure(Call<Sample> call, Throwable t) {
            currId = null;
            tempCB = null;
            Log.d("Session:DBConnection", "Get sample failure");
        }
    };
}
