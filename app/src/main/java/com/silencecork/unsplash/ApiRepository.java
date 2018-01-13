package com.silencecork.unsplash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.util.Log;

import com.silencecork.unsplash.model.Collection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    private static final String TAG = "ApiRepository";

    private ApiService mApiService;
    private static ApiRepository sInstance;

    private ApiRepository(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        CacheControlInterceptor cacheControlInterceptor = new CacheControlInterceptor();

        File cacheDir = context.getCacheDir();
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 100);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(logging)
                .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.HTTPS_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    public static ApiRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ApiRepository(context.getApplicationContext());
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

    class CacheControlInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            /*if(!NetUtils.hasNetwork(context)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.w(TAG,"no network");
            }*/
            okhttp3.Response originalResponse = chain.proceed(request);
            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .header("Cache-Control", "public, max-age=60,max-stale=120")
                    .removeHeader("Pragma")
                    .build();
        }
    }

}
