package com.silencecork.unsplash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.silencecork.unsplash.model._FeatureCollection;

/**
 * Created by Justin on 2017/12/19.
 */

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<_FeatureCollection> mCollectionListObservable = new MutableLiveData<>();

    private MutableLiveData<Long> mListClickEvent = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
    }

    public LiveData<_FeatureCollection> getCurrentFeaturedList() {
        return mCollectionListObservable;
    }

    public MutableLiveData<Long> getItemClickEvent() {
        return mListClickEvent;
    }

    public void getObservableFeaturedList(int page) {
        ApiRepository.getInstance(this.getApplication().getApplicationContext()).getFeaturedCollections(page, new Observer<_FeatureCollection>() {
            @Override
            public void onChanged(@Nullable _FeatureCollection collections) {
                mCollectionListObservable.setValue(collections);
            }
        });
    }

    public void reportClick(String param) {
        ApiRepository.getInstance(this.getApplication().getApplicationContext()).reportClick(param);
    }
}
