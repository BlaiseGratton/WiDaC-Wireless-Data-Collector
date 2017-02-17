package widac.cis350.upenn.edu.widac;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 */

public class Session {
    private Set<String> entries = new HashSet<String>();

    public boolean  addEntry(String id) {
        entries.add(id);
        return true;
    }

    public boolean updateEntry(String oldID, String newID) {
        if (entries.contains(oldID)) {
            entries.remove(oldID);
            entries.add(newID);
            return true;
        }
        return false;
    }
}
