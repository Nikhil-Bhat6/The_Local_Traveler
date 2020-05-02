package com.panaceasoft.pscity.ui.item.uploaded;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentUploadedItemBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.List;

public class ItemUploadedListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    
    private ItemViewModel itemViewModel;
    private ItemParameterHolder itemParameterHolder;
    PSDialogMsg psDialogMsg;
    @VisibleForTesting
    AutoClearedValue<FragmentUploadedItemBinding> binding;
    AutoClearedValue<ItemUploadedAdapter> itemListAdapter;
    AutoClearedValue<ProgressDialog> progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentUploadedItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_uploaded_item, container, false, dataBindingComponent);

        
        setHasOptionsMenu(true);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clearButton) {
            Utils.psLog("I am here for ok Button");

            navigationController.navigateToItemUploadActivity(getActivity(),null);
            itemViewModel.loadingDirection = Utils.LoadingDirection.top;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDispatched() {

        if (itemViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().itemLists.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }


    @Override
    protected void initUIAndActions() {

        progressDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        progressDialog.get().setMessage(getString(R.string.message__please_wait));
        progressDialog.get().setCancelable(false);


        binding.get().itemLists.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == itemListAdapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !itemViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            itemViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.ITEM_COUNT;
                            itemViewModel.offset = itemViewModel.offset + limit;

                            itemViewModel.setNextPageItemListByKeyObj( String.valueOf(Config.ITEM_COUNT),String.valueOf(itemViewModel.offset),Utils.checkUserId(loginUserId), itemParameterHolder);
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            itemViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            itemViewModel.forceEndLoading = false;

            // update live data
            itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), String.valueOf(itemViewModel.offset), itemParameterHolder);

        });
    }

    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemUploadedAdapter nvAdapter = new ItemUploadedAdapter(dataBindingComponent, new ItemUploadedAdapter.ItemListClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemUploadActivity(ItemUploadedListFragment.this.getActivity(), item);
            }

            @Override
            public void onDeleteClick(Item item) {
                PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
                psDialogMsg.showConfirmDialog(getString(R.string.specification__warning_delete),getString(R.string.app__ok),getString(R.string.app__cancel));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v -> {
                    psDialogMsg.cancel();
                    itemViewModel.setDeleteOneItemObj(item.id,loginUserId);
                });

                psDialogMsg.cancelButton.setOnClickListener(v -> psDialogMsg.cancel());

            }

        },this);
        this.itemListAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().itemLists.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        loadData();

        //Change Publish

        itemViewModel.getSaveOneItemData().observe(this, itemResource -> {

            if (itemResource != null) {

                switch (itemResource.status) {
                    case LOADING:
                        break;

                    case ERROR:
                        psDialogMsg.showErrorDialog(itemResource.message, getString(R.string.message__ok_close));

                        progressDialog.get().cancel();
                        break;

                    case SUCCESS:
                        progressDialog.get().cancel();
                        break;
                }
            }
        });

        //Delete Item

        itemViewModel.getDeleteOneItemData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case LOADING:
                        break;

                    case SUCCESS:
                        progressDialog.get().cancel();
                        break;

                    case ERROR:
                        PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
                        psDialogMsg.showErrorDialog(result.message, getString(R.string.message__ok_close));

                        progressDialog.get().cancel();
                        psDialogMsg.show();

                        break;
                }
            }
        });

        //Delete Item
    }

    private void loadData() {


        itemParameterHolder = new ItemParameterHolder().getUploadedItemClickMenu();
        itemParameterHolder.added_user_id = loginUserId;
        // Load item list
        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), String.valueOf(itemViewModel.offset), itemParameterHolder);

        LiveData<Resource<List<Item>>> news = itemViewModel.getItemListByKeyData();

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
                                binding.get().itemLists.setVisibility(View.VISIBLE);
                                binding.get().noDataImageView.setVisibility(View.GONE);
                                binding.get().noDataTextView.setVisibility(View.GONE);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                                binding.get().itemLists.setVisibility(View.VISIBLE);
                                binding.get().noDataImageView.setVisibility(View.GONE);
                                binding.get().noDataTextView.setVisibility(View.GONE);
                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            binding.get().itemLists.setVisibility(View.GONE);
                            binding.get().noDataImageView.setVisibility(View.VISIBLE);
                            binding.get().noDataTextView.setVisibility(View.VISIBLE);

                            itemViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (itemViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemViewModel.forceEndLoading = true;
                    }
                }
            });
        }

        itemViewModel.getNextPageItemListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemViewModel.setLoadingState(false);
                    itemViewModel.forceEndLoading = true;
                }
            }
        });

        itemViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(itemViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }
        });

    }


    private void replaceData(List<Item> itemLists) {
        itemListAdapter.get().replace(itemLists);
        binding.get().executePendingBindings();
    }
    //endregion


    @Override
    public void onResume() {
        Resource<List<Item>> resource = itemViewModel.getItemListByKeyData().getValue();

        if(resource != null) {
            List<Item> dataList = resource.data;

            if(dataList != null) {
                Utils.psLog("First Record Reload.");
                itemViewModel.offset=0;
                itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), String.valueOf(itemViewModel.offset), itemParameterHolder);
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }

        super.onResume();
    }

}
