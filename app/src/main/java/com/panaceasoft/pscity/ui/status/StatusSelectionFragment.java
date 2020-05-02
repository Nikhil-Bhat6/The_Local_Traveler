package com.panaceasoft.pscity.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentStatusSelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewmodel.itemstatus.ItemStatusViewModel;
import com.panaceasoft.pscity.viewobject.ItemStatus;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class StatusSelectionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {
    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ItemStatusViewModel itemStatusViewModel;

    private ItemViewModel itemViewModel;

    @VisibleForTesting
    AutoClearedValue<FragmentStatusSelectionBinding> binding;
    AutoClearedValue<StatusSelectionAdapter> adapter;

    //endregion

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        FragmentStatusSelectionBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_selection, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        binding.get().statusList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !itemStatusViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            itemStatusViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_CATEGORY_COUNT;
                            itemStatusViewModel.offset = itemStatusViewModel.offset + limit;

                            itemStatusViewModel.setNextPageLoadingStateObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemStatusViewModel.offset));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initViewModels() {
        itemStatusViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemStatusViewModel.class);
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
    }

    @Override
    protected void initAdapters() {

        getIntentData();

        StatusSelectionAdapter nvAdapter = new StatusSelectionAdapter(dataBindingComponent,
                itemViewModel.catSelectId,
                new StatusSelectionAdapter.CategoryExpClickCallback() {
                    @Override
                    public void onClick(ItemStatus status) {
                        navigationController.navigateBackToEntryItemFragment(StatusSelectionFragment.this.getActivity(), status.title,
                                status.id, Constants.SELECT_STATUS);
                    }
                },this);
//
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().statusList.setAdapter(nvAdapter);

    }

    private void getIntentData() {
        try {
            if(getActivity() != null) {
                if(getActivity().getIntent().getExtras() != null) {
                    itemViewModel.catSelectId = getActivity().getIntent().getExtras().getString(Constants.CATEGORY_ID);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    @Override
    protected void initData() {

        itemStatusViewModel.setItemStatusListObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemStatusViewModel.offset));
        itemStatusViewModel.getItemStatusListData().observe(this, resource -> {

            if (resource != null) {

                Utils.psLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                            replaceData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            replaceData(resource.data);
                        }

                        itemStatusViewModel.setLoadingState(false);
                        break;
                    case ERROR:
                        // Error State
                        itemStatusViewModel.setLoadingState(false);
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of Status.");


            } else {
                //noinspection ConstantConditions
                Utils.psLog("No Data of Status.");
            }
        });

        itemStatusViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemStatusViewModel.setLoadingState(false);//hide
                    itemStatusViewModel.forceEndLoading = true;//stop
                }
            }
        });

        itemStatusViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(itemStatusViewModel.isLoading));
    }

    //endregion
    private void replaceData(List<ItemStatus> statusList) {

        adapter.get().replace(statusList);
        binding.get().executePendingBindings();

    }

    @Override
    public void onResume() {
        Resource<List<ItemStatus>> resource = itemStatusViewModel.getItemStatusListData().getValue();

        if(resource != null) {
            List<ItemStatus> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                itemStatusViewModel.offset=0;
                itemStatusViewModel.setItemStatusListObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemStatusViewModel.offset));
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }
        super.onResume();
    }

    @Override
    public void onDispatched() {
        if (itemStatusViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().statusList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }
}
