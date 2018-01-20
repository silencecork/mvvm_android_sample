package com.silencecork.unsplash;

import com.silencecork.unsplash.model._FeatureCollection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Justin on 2017/12/19.
 */

public interface ApiService {

    String HTTPS_API_URL = "http://10.0.2.2:8080/";

    //    String HTTPS_API_URL = "https://api.unsplash.com/";
//    String AUTH_KEY = "Client-ID 274b0e0761718fcc3c520c2e56825102bd3d121d93e427c06ef43ef7cd80f853";

    // Test Cache-Control and ETag
    @GET("collections/featured")
    Call<_FeatureCollection> getFeaturedList(@Query("page") int page, @Query("per_page") int per_page, @Query("format") String format);

    // Test error 404 will be cached
    @GET("clk/android")
    Call<String> reportClick(@Query("param") String param);

}
