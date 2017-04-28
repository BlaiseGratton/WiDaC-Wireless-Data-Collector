package widac.cis350.upenn.edu.widac.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import widac.cis350.upenn.edu.widac.models.Sample;
import widac.cis350.upenn.edu.widac.models.Samples;

/**
 * Created by ashutosh on 2/24/17.
 */

public interface WidacService {

//    final String ENDPOINT = "https://widac-db-service.herokuapp.com";
    final String ENDPOINT = "http://10.0.2.2:5000";

    @GET("/widac/api/v1.0/samples/{composite_key}")
    Call<Sample> getSample(@Path("composite_key") String composite_key);

    @GET("/widac/api/v1.0/samples")
    Call<Samples> getAllSamples();
}
