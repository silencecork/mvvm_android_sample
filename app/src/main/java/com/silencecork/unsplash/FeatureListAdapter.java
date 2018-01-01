package com.silencecork.unsplash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.silencecork.unsplash.databinding.FeaturedCollectionItemBinding;
import com.silencecork.unsplash.model.Collection;

import java.util.List;

/**
 * Created by Justin on 2017/12/30.
 */

public class FeatureListAdapter extends RecyclerView.Adapter<FeatureListAdapter.FeaturedViewHolder> {

    private List<Collection> mData;
    private MutableLiveData<Long> mItemClickEvent;

    public FeatureListAdapter(MutableLiveData<Long> itemClickEvent) {
        mItemClickEvent = itemClickEvent;
    }

    @Override
    public FeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FeaturedCollectionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.featured_collection_item, parent, false);
        binding.setListener(mItemClickEvent);
        return new FeaturedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FeaturedViewHolder holder, int position) {
        Collection collection = mData.get(position);
        holder.mBinding.setCollection(collection);
        holder.mBinding.executePendingBindings();
        GlideApp.with(holder.mBinding.getRoot().getContext()).load(collection.cover_photo.urls.regular).centerCrop().into(holder.mBinding.image);
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }

    public void setData(List<Collection> data) {
        if (data == null || data.size() <= 0) {
            return;
        }
        int originPos = 0;
        if (mData == null) {
            mData = data;
        } else {
            originPos = mData.size();
            mData.addAll(data);
        }
        notifyItemRangeInserted(originPos, data.size());
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {
        private FeaturedCollectionItemBinding mBinding;

        public FeaturedViewHolder(FeaturedCollectionItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
