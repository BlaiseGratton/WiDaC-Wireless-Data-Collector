package widac.cis350.upenn.edu.widac;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 * =============================================
 * Store the ids of entries that have been modified durring current session
 * Also add
 */

public class Session {
    private static Set<String> entries = new HashSet<String>();

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

    public static Set<String> getCurrentSession() {
        return entries;
    }
}
