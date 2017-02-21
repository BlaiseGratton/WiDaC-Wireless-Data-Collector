package widac.cis350.upenn.edu.widac;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by ashutosh on 2/20/17.
 */

public class DatabaseUtil {

    private static final String TAG = "DatabaseUtil";

    private FirebaseDatabase database;
    private DatabaseReference allSamples;

    public DatabaseUtil() {
        this.database = FirebaseDatabase.getInstance();
        this.allSamples = this.database.getReference("sample");
    }

    public void addNewSample(Sample newSample) {
//        Sample newSample = new Sample(344, 120, 1, 2, "ceramic", 0.002);
        allSamples.child(newSample.getCompositeKey()).setValue(newSample);
    }

    public void updateSampleMaterial(String compositeKey, String newMaterial) {
//        String compositeKey = "344-120-1-2";
//        String newMaterial = "ceramic";
        allSamples.child(compositeKey).child("material").setValue(newMaterial);
    }

    public void updateSampleWeight(String compositeKey, double newWeight) {
//        String compositeKey = "344-120-1-2";
//        double newWeight = 0.002;
        allSamples.child(compositeKey).child("weight").setValue(newWeight);
    }

//    public Sample retrieveSample() {
//        String compositeKey = "344-120-1-2";
//        DatabaseReference thisSample = this.database.getReference("sample/" + compositeKey);
//        final Sample[] sample = {null};
//        thisSample.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                sample[0] = dataSnapshot.getValue(Sample.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "loadSample:onCancelled", databaseError.toException());
//            }
//        });
//        return sample[0];
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
