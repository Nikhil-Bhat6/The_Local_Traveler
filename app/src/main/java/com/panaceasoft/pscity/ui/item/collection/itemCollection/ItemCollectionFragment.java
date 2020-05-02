package com.panaceasoft.pscity.ui.item.collection.itemCollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentCollectionItemBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.search.searchlist.SearchListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.itemcollection.ItemCollectionViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class ItemCollectionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCollectionViewModel itemCollectionViewModel;

    @VisibleForTesting
    private
    AutoClearedValue<FragmentCollectionItemBinding> binding;
    private AutoClearedValue<SearchListAdapter> adapter;
    private AutoClearedValue<Intent> intent;
    private String id;

    //endregion


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCollectionItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_item, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        binding.get().newsList.setNestedScrollingEnabled(false);
        binding.get().newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
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

                                itemCollectionViewModel.setNextPageitemsByCollectionIdObj(String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset), id);
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
            itemCollectionViewModel.setItemsByCollectionIdObj(String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset), id);

        });
    }

    @Override
    protected void initViewModels() {
        itemCollectionViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCollectionViewModel.class);
    }

    @Override
    protected void initAdapters() {

        SearchListAdapter nvAdapter = new SearchListAdapter(dataBindingComponent, item -> navigationController.navigateToSelectedItemDetail(getActivity(), item.id, item.name), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().newsList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        getIntentData();

        loadData();
    }

    //region Private Methods

    private void loadData() {

        // Load Item By Collection Id
        itemCollectionViewModel.setItemsByCollectionIdObj(String.valueOf(Config.ITEM_COUNT), String.valueOf(itemCollectionViewModel.offset), id);

        LiveData<Resource<List<Item>>> news = itemCollectionViewModel.getItemsByCollectionIdData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

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

        itemCollectionViewModel.getNextPageitemsByCollectionIdData().observe(this, state -> {
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

    private void replaceData(List<Item> itemList) {

        adapter.get().replace(itemList);
        binding.get().executePendingBindings();

    }

    private void getIntentData()
    {
        if (this.getActivity() != null) {
            Intent intent1 = getActivity().getIntent();
            intent = new AutoClearedValue<>(this, intent1);
        }

        this.id = intent.get().getStringExtra(Constants.COLLECTION_ID);
        String image = intent.get().getStringExtra(Constants.COLLECTION_IMAGE);

        binding.get().setImage(image);
    }


    @Override
    public void onDispatched() {
        if (itemCollectionViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().newsList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().newsList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
    
}
