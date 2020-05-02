package com.panaceasoft.pscity.ui.item.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentHistoryBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.history.adapter.HistoryAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.HistoryViewModel;
import com.panaceasoft.pscity.viewobject.ItemHistory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HistoryViewModel historyViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentHistoryBinding> binding;
    private AutoClearedValue<HistoryAdapter> historyAdapter;

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentHistoryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {
        if (historyViewModel.loadingDirection == Utils.LoadingDirection.bottom) {

            if (binding.get().historyRecycler != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().historyRecycler.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        binding.get().historyRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == historyAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !historyViewModel.forceEndLoading) {

                            historyViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.HISTORY_COUNT;
                            historyViewModel.offset = historyViewModel.offset + limit;
                            historyViewModel.setHistoryItemListObj(String.valueOf(historyViewModel.offset));
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void initViewModels() {
        historyViewModel = new ViewModelProvider(this, viewModelFactory).get(HistoryViewModel.class);
    }

    @Override
    protected void initAdapters() {
        HistoryAdapter historyAdapter = new HistoryAdapter(dataBindingComponent,
                itemHistory -> navigationController.navigateToItemDetailActivity(HistoryFragment.this.getActivity(), itemHistory, selectedCityId));
        this.historyAdapter = new AutoClearedValue<>(this, historyAdapter);
        binding.get().historyRecycler.setAdapter(historyAdapter);
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {

        //load basket
        historyViewModel.offset = Config.HISTORY_COUNT;
        historyViewModel.setHistoryItemListObj(String.valueOf(historyViewModel.offset));
        LiveData<List<ItemHistory>> historyItemList = historyViewModel.getAllHistoryItemList();
        if (historyItemList != null) {
            historyItemList.observe(this, listResource -> {
                if (listResource != null) {

                    replaceItemHistoryData(listResource);

                }

            });
        }


        historyViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(historyViewModel.isLoading));

    }

    private void replaceItemHistoryData(List<ItemHistory> historyItemList) {
        historyAdapter.get().replace(historyItemList);
        binding.get().executePendingBindings();

    }
}
