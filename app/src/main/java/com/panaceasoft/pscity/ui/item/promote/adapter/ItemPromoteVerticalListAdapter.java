package com.panaceasoft.pscity.ui.item.promote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemPromoteVerticalWithUserBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewobject.ItemPaidHistory;

public class ItemPromoteVerticalListAdapter extends DataBoundListAdapter<ItemPaidHistory, ItemPromoteVerticalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemPromoteVerticalListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemPromoteVerticalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                          ItemPromoteVerticalListAdapter.NewsClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemPromoteVerticalWithUserBinding createBinding(ViewGroup parent) {
        ItemPromoteVerticalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_promote_vertical_with_user, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ItemPaidHistory itemPaidHistory = binding.getItemPaidHistory();
            if (itemPaidHistory != null && callback != null) {
                callback.onClick(itemPaidHistory);
            }
        });
        

        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemPromoteVerticalWithUserBinding> holder, int position) {
        super.bindView(holder, position);


        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemPromoteVerticalWithUserBinding binding, ItemPaidHistory itemPaidHistory) {
        binding.setItemPaidHistory(itemPaidHistory);

        String amount;
        String currencySymbol = itemPaidHistory.item.currencySymbol;

        try{
            amount = Utils.format(Double.parseDouble(itemPaidHistory.amount));
        }catch (Exception e) {
            amount = itemPaidHistory.amount;
        }

        String currencyAmount;

        currencyAmount = currencySymbol + " " + amount;

        binding.amountTextView.setText(currencyAmount);


        switch (itemPaidHistory.paidStatus) {
            case Constants.ADSPROGRESS:
                binding.isPaidTextView.setText(R.string.paid__ads_in_progress);
                binding.isPaidTextView.setBackgroundColor( binding.getRoot().getResources().getColor( R.color.paid_ad));

                break;
            case Constants.ADSFINISHED:
                binding.isPaidTextView.setText(R.string.paid__ads_in_completed);
                binding.isPaidTextView.setBackgroundColor( binding.getRoot().getResources().getColor( R.color.paid_ad_completed));
                break;
            case Constants.ADSNOTYETSTART:
                binding.isPaidTextView.setText(R.string.paid__ads_is_not_yet_start);
                binding.isPaidTextView.setBackgroundColor( binding.getRoot().getResources().getColor( R.color.paid_ad_is_not_start));

                break;
            default:
                binding.isPaidTextView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected boolean areItemsTheSame(ItemPaidHistory oldItem, ItemPaidHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.item.name.equals(newItem.item.name)
                && oldItem.item.isFavourited.equals(newItem.item.isFavourited)
                && oldItem.item.favouriteCount.equals(newItem.item.favouriteCount);
    }

    @Override
    protected boolean areContentsTheSame(ItemPaidHistory oldItem, ItemPaidHistory newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.item.name.equals(newItem.item.name)
                && oldItem.item.isFavourited.equals(newItem.item.isFavourited)
                && oldItem.item.favouriteCount.equals(newItem.item.favouriteCount);
    }

    public interface NewsClickCallback {
        void onClick(ItemPaidHistory itemPaidHistory);

    }


}
