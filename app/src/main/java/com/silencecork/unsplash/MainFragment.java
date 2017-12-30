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

import java.util.List;

/**
 * Created by Justin on 2017/12/19.
 */

public class MainFragment extends Fragment {

    FragmentMainBinding mBinding;
    MainViewModel mainViewModel;
    FeatureListAdapter mFeatureListAdapter;
    int page = 1;
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
                    loadFeatureList(++page);
                }
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mBinding.setIsLoading(true);
        mBinding.executePendingBindings();
        mainViewModel.getCurrentFeaturedList().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(@Nullable List<Collection> collections) {
                mBinding.setIsLoading(false);
                if (mFeatureListAdapter == null) {
                    mFeatureListAdapter = new FeatureListAdapter();
                    mBinding.list.setAdapter(mFeatureListAdapter);
                }
                Log.i("Main", "data size " + collections.size());
                mFeatureListAdapter.setData(collections);
                mFeatureListAdapter.notifyDataSetChanged();
//                for (Collection collection : collections) {
//                    Log.i("LiveData", collection.toString());
//                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.list.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadFeatureList(page);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadFeatureList(int page) {
        Log.i("Main", "load page " + page);
        mBinding.setIsLoading(true);
        mBinding.executePendingBindings();
        mainViewModel.getObservableFeaturedList(page);
    }

}
