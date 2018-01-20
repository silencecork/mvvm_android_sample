package com.silencecork.unsplash;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silencecork.unsplash.databinding.FragmentMainBinding;
import com.silencecork.unsplash.model.Collection;
import com.silencecork.unsplash.model._FeatureCollection;

import java.util.List;

/**
 * Created by Justin on 2017/12/19.
 */

public class MainFragment extends Fragment {

    private FragmentMainBinding mBinding;
    private MainViewModel mMainViewModel;
    private FeatureListAdapter mFeatureListAdapter;
    private int mCurrentPage = 0;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // isLoading defined in xml
        mBinding.setIsLoading(true);

        // get LiveData from ViewModel and register the observer
        mMainViewModel.getCurrentFeaturedList().observe(this, new Observer<_FeatureCollection>() {
            @Override
            public void onChanged(@Nullable _FeatureCollection featureCollection) {
                if (featureCollection != null) {
                    List<Collection> collections = featureCollection.collections;
                    if (collections != null && collections.size() > 0) {
                        mCurrentPage = mCurrentPage + 1;

                        // set data to adapter
                        mFeatureListAdapter.setData(collections);
                    }
                }

                // when change completed, stop loading
                mBinding.setIsLoading(false);
            }
        });

        mMainViewModel.getItemClickEvent().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long id) {
                Log.i("Click", "click id " + id);
                mMainViewModel.reportClick(String.valueOf(id));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create ViewModel use ViewModelProviders
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // binding class name will be the layout file name. e.g. fragment_main -> FragmentMainBinding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        // get binding root view
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFeatureListAdapter = new FeatureListAdapter(mMainViewModel.getItemClickEvent());

        // add scroller for pagination
        mBinding.list.addOnScrollListener(mOnScrollListener);

        // set adapter
        mBinding.list.setAdapter(mFeatureListAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // call api load data
        loadFeatureList(mCurrentPage + 1);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // remove scroller
        if (mBinding != null) {
            mBinding.list.removeOnScrollListener(mOnScrollListener);
        }

        // remove observer
        if (mMainViewModel != null) {
            mMainViewModel.getCurrentFeaturedList().removeObservers(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    private void loadFeatureList(int page) {
        Log.i("Main", "load page " + page);
        // start show loading indicator
        mBinding.setIsLoading(true);

        // call api with page parameters
        mMainViewModel.getObservableFeaturedList(page);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!mBinding.getIsLoading()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadFeatureList(mCurrentPage + 1);
                }
            }
        }
    };
}
