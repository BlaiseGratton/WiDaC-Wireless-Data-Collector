package widac.cis350.upenn.edu.widac;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by ashutosh on 2/20/17.
 */

public class DatabaseUtil {

    public static void addNewSample() {
        Sample sample = new Sample(143, 111, 12, 1, "ceramic", 12.3);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference("sample");
        mDatabase.child("sample").child(sample.create_key());
    }

//    public static void retrieveSample(String compositeKey) {
//
//    }
//
//    public static void isValidSample() {
//
//    }
//
//    public static void updateSample() {
//
//    }
}
