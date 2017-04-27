package widac.cis350.upenn.edu.widac.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by ashutosh on 2/24/17.
 */

public interface WidacService {

    final String ENDPOINT = "https://widac-db-service.herokuapp.com";

    @GET("/widac/api/v1.0/samples/{id}")
    Call<Sample> getSample(@Path("id") String id);
}
