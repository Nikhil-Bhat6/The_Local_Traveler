package com.panaceasoft.pscity.ui.category.categoryselection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemCategorySelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.viewobject.ItemCategory;

public class CategorySelectionAdapter extends DataBoundListAdapter<ItemCategory, ItemCategorySelectionBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    public CategorySelectionAdapter.CategoryExpClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private String selectId;

    CategorySelectionAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                             String selectId,
                             CategorySelectionAdapter.CategoryExpClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.selectId = selectId;
        this.callback = callback;
        this.diffUtilDispatchedInterface=diffUtilDispatchedInterface;
    }

    @Override
    protected ItemCategorySelectionBinding createBinding(ViewGroup parent) {
        ItemCategorySelectionBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_category_selection, parent, false,
                        dataBindingComponent);
        
        binding.getRoot().setOnClickListener(v -> {
            ItemCategory category = binding.getCategoryexpand();
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
    protected void bind(ItemCategorySelectionBinding binding, ItemCategory item) {
        binding.setCategoryexpand(item);

//        binding.setImage(item.defaultPhoto.path);

        if(item.id.equals(selectId)){
            binding.constraintLayout.setBackgroundResource(R.color.md_green_100);
            binding.clickImageView.setVisibility(View.VISIBLE);
        }else {
            binding.constraintLayout.setBackgroundResource(R.color.md_white_1000);
            binding.clickImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected boolean areItemsTheSame(ItemCategory oldItem, ItemCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ItemCategory oldItem, ItemCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface CategoryExpClickCallback {
        void onClick(ItemCategory category);
    }
}

