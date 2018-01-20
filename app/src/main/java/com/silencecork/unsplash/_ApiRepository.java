package com.silencecork.unsplash;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.silencecork.unsplash.model.Collection;
import com.silencecork.unsplash.model._FeatureCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Justin on 2017/12/19.
 */

public class _ApiRepository {

    private static final String TAG = "ApiRepository";
    private static final String RESPONSE_FORMAT = "json";

    private ApiService mApiService;
    private static _ApiRepository sInstance;
    private OkHttpClient mClient;
    private Handler mMainHandler;

    private _ApiRepository(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        CacheControlInterceptor cacheControlInterceptor = new CacheControlInterceptor();

        File cacheDir = context.getCacheDir();
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 100);

        mClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(logging)
//                .addInterceptor(cacheControlInterceptor)
//                .addNetworkInterceptor(cacheControlInterceptor)
                .build();
        mMainHandler = new Handler(context.getMainLooper());
    }

    public static _ApiRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new _ApiRepository(context.getApplicationContext());
        }
        return sInstance;
    }

    public void getFeaturedCollections(int page, final Observer<_FeatureCollection> observer) {
        Log.i("LiveData", "call api " + page);
        String url = Uri.parse(ApiService.HTTPS_API_URL + "collections/featured").buildUpon()
                .appendQueryParameter("page", String.valueOf(page)).appendQueryParameter("per_page", "20")
                .build().toString();
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String content = response.body().string();
                try {
                    final _FeatureCollection collections = new _FeatureCollection();
                    collections.collections = new ArrayList<Collection>();
                    JSONObject json = new JSONObject(content);
                    JSONArray ary = json.optJSONArray("collections");
                    if (ary != null) {
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject item = ary.optJSONObject(i);
                            Collection collection = new Collection();
                            collection.id = item.optInt("id");
                            collection.cover_photo = item.optString("cover_photo");
                            collection.published_at = item.optString("published_at");
                            collection.title = item.optString("title");
                            collection.total_photos = item.optInt("total_photos");
                            collections.collections.add(collection);
                        }
                    }
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            observer.onChanged(collections);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
