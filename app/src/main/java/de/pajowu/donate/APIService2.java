package de.pajowu.donate;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by patricebecker on 12/09/15.
 */
public interface APIService2 {
    @GET("/api/question/EN")
    Call<ResponseBody> getDataFromAPI();
}

