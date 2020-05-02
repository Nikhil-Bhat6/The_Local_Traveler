package com.panaceasoft.pscity.ui.item.collection.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemCollectionHeaderListAdapterBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Panacea-Soft on 10/27/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class CollectionHeaderListAdapter extends DataBoundListAdapter<ItemCollectionHeader, ItemCollectionHeaderListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemCollectionHeaderClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private int lastPosition = -1;

    public CollectionHeaderListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                       ItemCollectionHeaderClickCallback callback,
                                       DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemCollectionHeaderListAdapterBinding createBinding(ViewGroup parent) {
        ItemCollectionHeaderListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_collection_header_list_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ItemCollectionHeader itemCollectionHeader = binding.getItemCollectinHeader();
            if (itemCollectionHeader != null && callback != null) {
                callback.onClick(itemCollectionHeader);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemCollectionHeaderListAdapterBinding> holder, int position) {
        super.bindView(holder, position);

        setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemCollectionHeaderListAdapterBinding binding, ItemCollectionHeader item) {
        binding.setItemCollectinHeader(item);

        binding.newsTitleTextView.setText(item.name);

    }

    @Override
    protected boolean areItemsTheSame(ItemCollectionHeader oldItem, ItemCollectionHeader newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ItemCollectionHeader oldItem, ItemCollectionHeader newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface ItemCollectionHeaderClickCallback {
        void onClick(ItemCollectionHeader itemCollectionHeader);
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            lastPosition = position;
        }
    }
}
