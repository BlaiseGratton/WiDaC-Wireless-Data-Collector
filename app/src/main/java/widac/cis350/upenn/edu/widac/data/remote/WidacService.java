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

    @GET("/widac/api/v1.0/samples/{composite_key}")
    Call<Sample> retrieveSample(@Path("composite_key") String composite_key);
}
