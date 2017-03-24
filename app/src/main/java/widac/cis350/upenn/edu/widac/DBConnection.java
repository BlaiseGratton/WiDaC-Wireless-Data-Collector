package widac.cis350.upenn.edu.widac;

import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import widac.cis350.upenn.edu.widac.data.remote.RetrofitClient;
import widac.cis350.upenn.edu.widac.data.remote.WidacService;
import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by ashutosh on 2/23/17.
 */

public class DBConnection {

    Retrofit retrofit;
    WidacService widacService;

    public DBConnection() {
        retrofit = RetrofitClient.getClient();
        widacService = retrofit.create(WidacService.class);
    }

    public void addNewSample(Sample newSample) {
//        Sample newSample = new Sample(344, 120, 1, 2, "ceramic", 0.002);
    }

    public void updateSampleMaterial(String compositeKey, String newMaterial) {
//        String compositeKey = "344-120-1-2";
//        String newMaterial = "ceramic";
    }

    public void updateSampleWeight(String compositeKey, double newWeight) {
//        String compositeKey = "344-120-1-2";
//        double newWeight = 0.002;
    }

    public Sample retrieveSample(String compositeKey) {
        Log.d("DBConnection", "Retrieving sample");

        String[] types = {"A", "B", "C", "D", "E"};
        String type = types[(int)(Math.random() * types.length)];
        Log.d("DBConnection", "Type: " + type);
        return new Sample((int)(Math.random() * 1000), (int)(Math.random() * 1000), -1, -1, type, Math.random() * 2, Math.random() * 4, (int)(Math.random() * 1000));
    }

    public void getSample(String id, Callback<Sample> callback) {
        Call<Sample> callSample = widacService.getSample(id);
        callSample.enqueue(callback);
    }


}
