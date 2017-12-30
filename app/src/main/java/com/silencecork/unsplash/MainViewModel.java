package com.silencecork.unsplash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.silencecork.unsplash.model.Collection;

import java.util.List;

/**
 * Created by Justin on 2017/12/19.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Collection>> collectionListObservable;

    public MainViewModel(Application application) {
        super(application);

        collectionListObservable = ApiRepository.getInstance().getInitObervalbeList();
    }

    public LiveData<List<Collection>> getCurrentFeaturedList() {
        return collectionListObservable;
    }

    public LiveData<List<Collection>> getObservableFeaturedList(int page) {
        collectionListObservable = ApiRepository.getInstance().getObservableList(page);
        return collectionListObservable;
    }

}
