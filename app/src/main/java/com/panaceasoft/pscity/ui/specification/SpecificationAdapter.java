package com.panaceasoft.pscity.ui.specification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.databinding.DataBindingUtil;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemSpecificationBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.ItemSpecs;

public class SpecificationAdapter extends DataBoundListAdapter<ItemSpecs, ItemSpecificationBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final SpecificationAdapter.AttributeHeaderClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;

    
    SpecificationAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                           SpecificationAdapter.AttributeHeaderClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface=diffUtilDispatchedInterface;
    }

    @Override
    protected ItemSpecificationBinding createBinding(ViewGroup parent) {
        ItemSpecificationBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_specification, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ItemSpecs itemList = binding.getItemspecs();
            if (itemList != null && callback != null) {
                callback.onClick(itemList);
            }
        });

        
        binding.editTextView.setOnClickListener(v -> callback.onEditClick(binding.getItemspecs()));

        binding.delteImageView.setOnClickListener(v -> callback.onDeleteClick(binding.getItemspecs()));

        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemSpecificationBinding> holder, int position) {
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
    protected void bind(ItemSpecificationBinding binding, ItemSpecs item) {
        binding.setItemspecs(item);

    }

    @Override
    protected boolean areItemsTheSame(ItemSpecs oldItem, ItemSpecs newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && Objects.equals(oldItem.description, newItem.description)
                && Objects.equals(oldItem.name, newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ItemSpecs oldItem, ItemSpecs newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && Objects.equals(oldItem.description, newItem.description)
                && Objects.equals(oldItem.name, newItem.name);
    }

    public interface AttributeHeaderClickCallback {
        void onClick(ItemSpecs subCategory);
        void onEditClick(ItemSpecs attributeHeader);
        void onDeleteClick(ItemSpecs attributeHeader);
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
