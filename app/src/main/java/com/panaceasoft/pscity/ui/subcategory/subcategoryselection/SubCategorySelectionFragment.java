package com.panaceasoft.pscity.ui.subcategory.subcategoryselection;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.panaceasoft.pscity.databinding.FragmentSubCategorySelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewmodel.itemsubcategory.ItemSubCategoryViewModel;
import com.panaceasoft.pscity.viewobject.ItemSubCategory;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class SubCategorySelectionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemSubCategoryViewModel subCategoryViewModel;
    ItemViewModel itemListViewModel;


    @VisibleForTesting
    AutoClearedValue<FragmentSubCategorySelectionBinding> binding;
    AutoClearedValue<SubCategorySelectionAdapter> subCategoryAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSubCategorySelectionBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_category_selection, container, false, dataBindingComponent);


        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

        if (subCategoryViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().shopListSelectRecycler.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void initUIAndActions() {
        binding.get().shopListSelectRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == subCategoryAdapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !subCategoryViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            subCategoryViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_SUB_CATEGORY_COUNT;
                            subCategoryViewModel.offset = subCategoryViewModel.offset + limit;

                            subCategoryViewModel.setNextPageLoadingStateObj(itemListViewModel.catSelectId, String.valueOf(Config.LIST_SUB_CATEGORY_COUNT), String.valueOf(subCategoryViewModel.offset));
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void initViewModels() {
        subCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemSubCategoryViewModel.class);
        itemListViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
    }

    @Override
    protected void initAdapters() {
        getIntentData();

        SubCategorySelectionAdapter nvAdapter = new SubCategorySelectionAdapter(dataBindingComponent, itemListViewModel.subCatSelectId,
                //todo: Change to Detail Activity
                subCategory -> navigationController.navigateBackToEntryItemFragment(getActivity(), subCategory.name, subCategory.id,
                        Constants.SELECT_SUBCATEGORY),this);
        this.subCategoryAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().shopListSelectRecycler.setAdapter(nvAdapter);
    }

    private void getIntentData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemListViewModel.subCatSelectId = getActivity().getIntent().getExtras().getString(Constants.SUBCATEGORY_ID);
                    itemListViewModel.catSelectId = getActivity().getIntent().getStringExtra(Constants.CATEGORY_ID);

                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    @Override
    protected void initData() {

        loadData();
    }

    private void loadData() {

        // Load Sub Category
        subCategoryViewModel.setSubCategoryListByCatIdObj(itemListViewModel.catSelectId, String.valueOf(Config.LIST_SUB_CATEGORY_COUNT), String.valueOf(subCategoryViewModel.offset));

        LiveData<Resource<List<ItemSubCategory>>> news = subCategoryViewModel.getSubCategoryListByCatIdData();

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

                            subCategoryViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            subCategoryViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (subCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        subCategoryViewModel.forceEndLoading = true;
                    }
                }
            });
        }

        subCategoryViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    subCategoryViewModel.setLoadingState(false);
                    subCategoryViewModel.forceEndLoading = true;
                }
            }
        });

        subCategoryViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(subCategoryViewModel.isLoading));

    }

    private void replaceData(List<ItemSubCategory> newsList) {
        subCategoryAdapter.get().replace(newsList);
        binding.get().executePendingBindings();
    }
    //endregion

    @Override
    public void onResume() {
        Resource<List<ItemSubCategory>> resource = subCategoryViewModel.getSubCategoryListByCatIdData().getValue();

        if(resource != null) {
            List<ItemSubCategory> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                subCategoryViewModel.offset=0;
                subCategoryViewModel.setSubCategoryListByCatIdObj(itemListViewModel.catSelectId, String.valueOf(Config.LIST_SUB_CATEGORY_COUNT), String.valueOf(subCategoryViewModel.offset));

            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }
        super.onResume();
    }

}

