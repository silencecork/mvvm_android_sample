package com.silencecork.unsplash;

import com.silencecork.unsplash.model.Collection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Justin on 2017/12/19.
 */

public interface ApiService {

    String HTTPS_API_URL = "https://api.unsplash.com/";
    String AUTH_KEY = "Client-ID 274b0e0761718fcc3c520c2e56825102bd3d121d93e427c06ef43ef7cd80f853";

    @GET("collections/featured")
    Call<List<Collection>> getFeaturedList(@Header("Authorization") String auth, @Query("page") int page, @Query("per_page") int per_page);

//    @GET("/repos/{user}/{reponame}")
//    Call<Project> getProjectDetails(@Path("user") String user, @Path("reponame") String projectName);

}
