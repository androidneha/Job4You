package in.co.job4you;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("api/jobs")
    Call<JSONResponse> getJSON();
}
