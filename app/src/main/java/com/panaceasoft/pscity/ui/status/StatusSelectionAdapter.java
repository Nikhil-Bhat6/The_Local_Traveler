package com.panaceasoft.pscity.ui.status;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemStatusSelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.ItemStatus;

public class StatusSelectionAdapter extends DataBoundListAdapter<ItemStatus, ItemStatusSelectionBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    public StatusSelectionAdapter.CategoryExpClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private String selectId;

    StatusSelectionAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                             String selectId,
                             StatusSelectionAdapter.CategoryExpClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.selectId = selectId;
        this.callback = callback;
        this.diffUtilDispatchedInterface=diffUtilDispatchedInterface;
    }

    
    @Override
    protected ItemStatusSelectionBinding createBinding(ViewGroup parent) {
        ItemStatusSelectionBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_status_selection, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            ItemStatus category = binding.getStatusexpand();
            if (category != null && callback != null) {
                binding.constraintLayout.setBackgroundResource(R.color.md_green_100);
                binding.clickImageView.setVisibility(View.VISIBLE);
                callback.onClick(category);
            }
        });

        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemStatusSelectionBinding binding, ItemStatus itemStatus) {
        binding.setStatusexpand(itemStatus);

//        binding.setImage(itemStatus.defaultPhoto.path);

        if(itemStatus.id.equals(selectId)){
            binding.constraintLayout.setBackgroundResource(R.color.md_green_100);
            binding.clickImageView.setVisibility(View.VISIBLE);
        }else {
            binding.constraintLayout.setBackgroundResource(R.color.md_white_1000);
            binding.clickImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected boolean areItemsTheSame(ItemStatus oldItem, ItemStatus newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.addedDate.equals(newItem.addedDate);
    }

    @Override
    protected boolean areContentsTheSame(ItemStatus oldItem, ItemStatus newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.addedDate.equals(newItem.addedDate);
    }

    public interface CategoryExpClickCallback {
        void onClick(ItemStatus category);
    }
}


