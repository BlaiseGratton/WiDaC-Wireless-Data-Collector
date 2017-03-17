package widac.cis350.upenn.edu.widac;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 * =============================================
 * Store the ids of entries that have been modified durring current session
 * Also add
 */

public class Session {
    private static DBConnection DBC = new DBConnection();
    private static Set<String> entries =  new HashSet<String>();

    private Session() {}

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

    // Pull an entry from the database
    public static Sample pullNewEntryFromDB(String id) {
        // add entry to current session and return data
        entries.add(id);
        return DBC.retrieveSample(id);
    }

    public static Set<Sample> pullFromDB() {
        Set<Sample> samples = new HashSet<Sample>();
        for (String id: entries) {
            samples.add(DBC.retrieveSample(id));
        }
        return samples;
    }

    public static Set<String> getCurrentSessionIDs() {
        return entries;
    }

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
