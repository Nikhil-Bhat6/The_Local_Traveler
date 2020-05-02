package com.panaceasoft.pscity.ui.city.selectedcity.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemItemCollectionRowAdapterBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.item.adapter.ItemListAdapter;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;

import java.util.List;

public class ItemCollectionRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private List<ItemCollectionHeader> itemCollectionHeaderList;
    public ItemClickCallback callback;

    public ItemCollectionRowAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, ItemClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    public void replaceCollectionHeader(List<ItemCollectionHeader> itemCollectionHeaders) {
        this.itemCollectionHeaderList = itemCollectionHeaders;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemCollectionRowAdapterBinding binding;

        private MyViewHolder(ItemItemCollectionRowAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemCollectionHeader itemCollectionHeader) {
            binding.setItemCollectionHeader(itemCollectionHeader);
            binding.executePendingBindings();
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemItemCollectionRowAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_item_collection_row_adapter, parent, false, dataBindingComponent);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            ItemCollectionHeader itemCollectionHeader = itemCollectionHeaderList.get(position);

            ((MyViewHolder) holder).binding.titleTextView.setText(itemCollectionHeader.name);

            ((MyViewHolder) holder).binding.setItemCollectionHeader(itemCollectionHeader);

            ((MyViewHolder) holder).binding.viewAllTextView.setOnClickListener(view -> callback.onViewAllClick(itemCollectionHeaderList.get(position)));


            if (itemCollectionHeader.itemList != null) {
                if (itemCollectionHeader.itemList.size() > 0) {

                    ItemListAdapter itemListAdapter = new ItemListAdapter(dataBindingComponent, item -> {

                        callback.onClick(item);
                    }, this);

                    ((MyViewHolder) holder).binding.collectionList.setAdapter(itemListAdapter);
                    itemListAdapter.replace(itemCollectionHeaderList.get(position).itemList);

                }
            }

        }

    }

    public interface ItemClickCallback {
        void onClick(Item item);

        void onViewAllClick(ItemCollectionHeader itemCollectionHeader);

    }

    @Override
    public int getItemCount() {
        if (itemCollectionHeaderList != null && itemCollectionHeaderList.size() > 0) {
            return itemCollectionHeaderList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onDispatched() {

    }
}
