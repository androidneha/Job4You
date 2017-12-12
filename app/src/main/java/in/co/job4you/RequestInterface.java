package in.co.job4you;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("api/jobs")
    Call<JSONResponse> getJSON();
}
