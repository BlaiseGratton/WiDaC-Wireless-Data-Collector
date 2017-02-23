package widac.cis350.upenn.edu.widac;

/**
 * Created by J. Patrick Taggart on 2/21/2017.
 */

public class DummyEntry {
    String ID;
    String type; // Could define an enum for types
    String locationData;
    double weight; // assume in grams
    double size;

    DummyEntry(String ID, String type, String locationData, double weight, double size) {
        this.ID = ID;
        this.type = type;
        this.locationData = locationData;
        this.weight = weight;
        this.size = size;
    }
}
