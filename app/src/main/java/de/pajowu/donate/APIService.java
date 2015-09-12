package de.pajowu.donate;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by patricebecker on 12/09/15.
 */
public interface APIService {
    @GET("/api/category/DE")
    Call<ResponseBody> getDataFromAPI();
}
