package com.silencecork.unsplash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.silencecork.unsplash.model.Collection;

import java.util.List;

/**
 * Created by Justin on 2017/12/19.
 */

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<Collection>> mCollectionListObservable = new MutableLiveData<>();

    private MutableLiveData<Long> mListClickEvent = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Collection>> getCurrentFeaturedList() {
        return mCollectionListObservable;
    }

    public MutableLiveData<Long> getItemClickEvent() {
        return mListClickEvent;
    }

    public void getObservableFeaturedList(int page) {
        ApiRepository.getInstance().getFeaturedCollections(page, new Observer<List<Collection>>() {
            @Override
            public void onChanged(@Nullable List<Collection> collections) {
                mCollectionListObservable.setValue(collections);
            }
        });
    }

}
