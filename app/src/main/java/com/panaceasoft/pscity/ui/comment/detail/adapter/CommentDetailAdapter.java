package com.panaceasoft.pscity.ui.comment.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ItemCommentDetailAdapterBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundViewHolder;
import com.panaceasoft.pscity.utils.Objects;
import com.panaceasoft.pscity.viewobject.CommentDetail;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class CommentDetailAdapter extends DataBoundListAdapter<CommentDetail, ItemCommentDetailAdapterBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private CommentDetailClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;


    public CommentDetailAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                CommentDetailClickCallback callback,
                                DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemCommentDetailAdapterBinding createBinding(ViewGroup parent) {
        ItemCommentDetailAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_comment_detail_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            CommentDetail commentDetail = binding.getCommentDetail();
            if (commentDetail != null && callback != null) {
                callback.onClick(commentDetail);
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
    protected void bind(ItemCommentDetailAdapterBinding binding, CommentDetail item) {
        binding.setCommentDetail(item);

        binding.commentTextView.setText(item.detailComment);

    }

    @Override
    public void bindView(DataBoundViewHolder<ItemCommentDetailAdapterBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected boolean areItemsTheSame(CommentDetail oldItem, CommentDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.headerId.equals(newItem.headerId);
    }

    @Override
    protected boolean areContentsTheSame(CommentDetail oldItem, CommentDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.headerId.equals(newItem.headerId);
    }

    public interface CommentDetailClickCallback {
        void onClick(CommentDetail comment);
    }
}
