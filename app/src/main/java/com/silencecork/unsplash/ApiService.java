package com.silencecork.unsplash;

import com.silencecork.unsplash.model._FeatureCollection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Justin on 2017/12/19.
 */

public interface ApiService {

//    String HTTPS_API_URL = "https://api.unsplash.com/";
    String HTTPS_API_URL = "http://10.0.2.2:8080/";
    String AUTH_KEY = "Client-ID 274b0e0761718fcc3c520c2e56825102bd3d121d93e427c06ef43ef7cd80f853";

//    @GET("collections/featured")
//    Call<_FeatureCollection> getFeaturedList(@Header("Authorization") String auth, @Query("page") int page, @Query("per_page") int per_page, @Query("format") String format);
@GET("collections/featured")
    Call<_FeatureCollection> getFeaturedList(@Query("page") int page, @Query("per_page") int per_page, @Query("format") String format);

//    @GET("/repos/{user}/{reponame}")
//    Call<Project> getProjectDetails(@Path("user") String user, @Path("reponame") String projectName);

}
