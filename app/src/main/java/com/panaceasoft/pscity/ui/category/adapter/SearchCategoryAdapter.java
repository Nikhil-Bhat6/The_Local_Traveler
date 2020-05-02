package com.panaceasoft.pscity.ui.category.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemSearchCategoryAdapterBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.ItemCategory;

public class SearchCategoryAdapter extends DataBoundListAdapter<ItemCategory, ItemSearchCategoryAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final SearchCategoryAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    public String catId = "";

    public SearchCategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                 SearchCategoryAdapter.NewsClickCallback callback,
                                 DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    public SearchCategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                 SearchCategoryAdapter.NewsClickCallback callback, String catId) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.catId = catId;
    }

    @Override
    protected ItemSearchCategoryAdapterBinding createBinding(ViewGroup parent) {
        ItemSearchCategoryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_search_category_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {

            ItemCategory category = binding.getCategory();

            if (category != null && callback != null) {

                binding.groupview.setBackgroundColor(parent.getResources().getColor(R.color.md_green_50));

                callback.onClick(category, category.id);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemSearchCategoryAdapterBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemSearchCategoryAdapterBinding binding, ItemCategory item) {
        binding.setCategory(item);

        if (catId != null) {
            if (item.id.equals(catId)) {
                binding.groupview.setBackgroundColor(binding.groupview.getResources().getColor((R.color.md_green_50)));
            }
        }

    }

    @Override
    protected boolean areItemsTheSame(ItemCategory oldItem, ItemCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(ItemCategory oldItem, ItemCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    public interface NewsClickCallback {
        void onClick(ItemCategory category, String id);
    }


//    private void setAnimation(View viewToAnimate, int position) {
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        } else {
//            lastPosition = position;
//        }
//    }
}
