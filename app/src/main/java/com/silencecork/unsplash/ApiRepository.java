package com.silencecork.unsplash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.util.Log;

import com.silencecork.unsplash.model.Collection;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Justin on 2017/12/19.
 */

public class ApiRepository {

    private ApiService mApiService;
    private static ApiRepository sInstance;

    private List<Collection> mData = new ArrayList<>();

    private ApiRepository() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.HTTPS_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    public static ApiRepository getInstance() {
        if (sInstance == null) {
            sInstance = new ApiRepository();
        }
        return sInstance;
    }

    public void getFeaturedCollections(int page, final Observer<List<Collection>> observer) {
        Log.i("LiveData", "call api " + page);
        mApiService.getFeaturedList(ApiService.AUTH_KEY, page, 20).enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                List<Collection> data = response.body();
                observer.onChanged(data);
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                observer.onChanged(null);
            }
        });

    }

}
