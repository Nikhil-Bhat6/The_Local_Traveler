package com.panaceasoft.pscity.ui.item.uploaded;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemUploadedItemBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.Item;

public class ItemUploadedAdapter extends DataBoundListAdapter<Item, ItemUploadedItemBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemUploadedAdapter.ItemListClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    
    

    ItemUploadedAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                    ItemUploadedAdapter.ItemListClickCallback callback , DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemUploadedItemBinding createBinding(ViewGroup parent) {
        ItemUploadedItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_uploaded_item, parent, false,
                        dataBindingComponent);
        
        binding.delteImageView.setOnClickListener(v -> callback.onDeleteClick(binding.getItemList()));

        binding.getRoot().setOnClickListener(v -> {
            Item itemList = binding.getItemList();
            if (itemList != null && callback != null) {
                callback.onClick(itemList);
            }
        });

        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemUploadedItemBinding> holder, int position) {
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
    protected void bind(ItemUploadedItemBinding binding, Item item) {
        binding.setItemList(item);

        binding.catAndSubcatTextView.setText(binding.getRoot().getContext().getString(R.string.uploaded_list__cat_and_subcat, item.category.name,item.subCategory.name));
    }

    @Override
    protected boolean areItemsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && Objects.equals(oldItem.defaultPhoto.imgPath, newItem.defaultPhoto.imgPath)
                && Objects.equals(oldItem.name, newItem.name)
                && Objects.equals(oldItem.category.name, newItem.category.name)
                && Objects.equals(oldItem.subCategory.name, newItem.subCategory.name)
                && Objects.equals(oldItem.defaultPhoto.imgId, newItem.defaultPhoto.imgId);
    }

    @Override
    protected boolean areContentsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && Objects.equals(oldItem.defaultPhoto.imgPath, newItem.defaultPhoto.imgPath)
                && Objects.equals(oldItem.name, newItem.name)
                && Objects.equals(oldItem.category.name, newItem.category.name)
                && Objects.equals(oldItem.subCategory.name, newItem.subCategory.name)
                && Objects.equals(oldItem.defaultPhoto.imgId, newItem.defaultPhoto.imgId);
    }

    public interface ItemListClickCallback {
        void onClick(Item item);
        void onDeleteClick(Item item);
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
