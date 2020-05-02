package com.panaceasoft.pscity.ui.item.collection.header;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentCollectionHeaderListBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.collection.adapter.CollectionHeaderListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.itemcollection.ItemCollectionViewModel;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionHeaderListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCollectionViewModel itemCollectionViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentCollectionHeaderListBinding> binding;
    private AutoClearedValue<CollectionHeaderListAdapter> adapter;

    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCollectionHeaderListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_header_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        if(getActivity() instanceof MainActivity)  {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().itemCollectionHeaderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {


                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !itemCollectionViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                itemCollectionViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.ITEM_COUNT;
                                itemCollectionViewModel.offset = itemCollectionViewModel.offset + limit;

                                itemCollectionViewModel.setNextPageCollectionHeaderListObj( String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset));
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemCollectionViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            itemCollectionViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            itemCollectionViewModel.forceEndLoading = false;

            // update live data
            itemCollectionViewModel.setCollectionHeaderListObj(  String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        itemCollectionViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCollectionViewModel.class);
    }

    @Override
    protected void initAdapters() {
        CollectionHeaderListAdapter nvAdapter = new CollectionHeaderListAdapter(dataBindingComponent,
                itemCollectionHeader -> navigationController.navigateToCollectionItemList(CollectionHeaderListFragment.this.getActivity(), itemCollectionHeader.id, itemCollectionHeader.name, itemCollectionHeader.defaultPhoto.imgPath),
                this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().itemCollectionHeaderList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        loadDiscount();
    }


    //region Private Methods

    private void loadDiscount() {

        // Load Latest ProductCollectionHeader
        itemCollectionViewModel.setCollectionHeaderListObj(String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset));

        LiveData<Resource<List<ItemCollectionHeader>>> news = itemCollectionViewModel.getCollectionHeaderList();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {

                                if(!connectivity.isConnected()) {

                                    //fadeIn Animation
                                    fadeIn(binding.get().getRoot());

                                    // Update the data
                                    replaceData(listResource.data);
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            itemCollectionViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            itemCollectionViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (itemCollectionViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemCollectionViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        itemCollectionViewModel.getNextPageCollectionHeaderListData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemCollectionViewModel.setLoadingState(false);
                    itemCollectionViewModel.forceEndLoading = true;
                }
            }
        });

        itemCollectionViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(itemCollectionViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceData(List<ItemCollectionHeader> itemCollectionHeaders) {

        adapter.get().replace(itemCollectionHeaders);
        binding.get().executePendingBindings();

    }


    @Override
    public void onDispatched() {
        if (itemCollectionViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().itemCollectionHeaderList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().itemCollectionHeaderList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
}
