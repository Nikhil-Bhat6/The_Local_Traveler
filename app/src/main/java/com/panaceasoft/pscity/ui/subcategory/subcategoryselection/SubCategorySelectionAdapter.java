package com.panaceasoft.pscity.ui.subcategory.subcategoryselection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemSubCategorySelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.ItemSubCategory;

public class SubCategorySelectionAdapter extends DataBoundListAdapter<ItemSubCategory, ItemSubCategorySelectionBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    public final SubCategorySelectionAdapter.SubCategoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    private String selectId;

    public SubCategorySelectionAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, String selectId,
                                       SubCategorySelectionAdapter.SubCategoryClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.selectId = selectId;
        this.diffUtilDispatchedInterface=diffUtilDispatchedInterface;
    }

    @Override
    protected ItemSubCategorySelectionBinding createBinding(ViewGroup parent) {
        ItemSubCategorySelectionBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_sub_category_selection, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ItemSubCategory subCategory = binding.getSubcategoryexpand();
            if (subCategory != null && callback != null) {
                binding.constraintLayoutSubCat.setBackgroundResource(R.color.md_green_100);
                binding.clickImageView.setVisibility(View.VISIBLE);
                callback.onClick(subCategory);
            }
        });
        
        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemSubCategorySelectionBinding> holder, int position) {
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
    protected void bind(ItemSubCategorySelectionBinding binding, ItemSubCategory item) {
        binding.setSubcategoryexpand(item);

//        binding.setImage(item.defaultPhoto.path);

        if(item.id.equals( selectId)){
            binding.constraintLayoutSubCat.setBackgroundResource(R.color.md_green_100);
            binding.clickImageView.setVisibility(View.VISIBLE);
        }else {
            binding.constraintLayoutSubCat.setBackgroundResource(R.color.md_white_1000);
            binding.clickImageView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected boolean areItemsTheSame(ItemSubCategory oldItem, ItemSubCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ItemSubCategory oldItem, ItemSubCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface SubCategoryClickCallback {
        void onClick(ItemSubCategory subCategory);
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
