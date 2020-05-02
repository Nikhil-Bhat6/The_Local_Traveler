package com.panaceasoft.pscity.ui.category.categoryselection;

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
import com.panaceasoft.pscity.databinding.FragmentCategorySelectionBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class CategorySelectionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {
    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ItemCategoryViewModel itemCategoryViewModel;

    private ItemViewModel itemViewModel;

    @VisibleForTesting
    AutoClearedValue<FragmentCategorySelectionBinding> binding;
    AutoClearedValue<CategorySelectionAdapter> adapter;

    //endregion

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        FragmentCategorySelectionBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_selection, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        binding.get().categoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !itemCategoryViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            itemCategoryViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_CATEGORY_COUNT;
                            itemCategoryViewModel.offset = itemCategoryViewModel.offset + limit;

                            itemCategoryViewModel.setNextPageLoadingStateObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemCategoryViewModel.offset));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initViewModels() {
        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
    }

    @Override
    protected void initAdapters() {

        getIntentData();

        CategorySelectionAdapter nvAdapter = new CategorySelectionAdapter(dataBindingComponent,
                itemViewModel.catSelectId,
                category -> navigationController.navigateBackToEntryItemFragment(CategorySelectionFragment.this.getActivity(),category.name,
                        category.id, Constants.SELECT_CATEGORY),this);
//
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().categoryList.setAdapter(nvAdapter);

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

        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemCategoryViewModel.offset));
        itemCategoryViewModel.getCategoryListData().observe(this, resource -> {

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

                        itemCategoryViewModel.setLoadingState(false);
                        break;
                    case ERROR:
                        // Error State
                        itemCategoryViewModel.setLoadingState(false);
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
                Utils.psLog("Got Data Of Category.");


            } else {
                //noinspection ConstantConditions
                Utils.psLog("No Data of Category.");
            }
        });

        itemCategoryViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemCategoryViewModel.setLoadingState(false);//hide
                    itemCategoryViewModel.forceEndLoading = true;//stop
                }
            }
        });

        itemCategoryViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(itemCategoryViewModel.isLoading));
    }

    //endregion
    private void replaceData(List<ItemCategory> categoryList) {

        adapter.get().replace(categoryList);
        binding.get().executePendingBindings();

    }

    @Override
    public void onResume() {
        Resource<List<ItemCategory>> resource = itemCategoryViewModel.getCategoryListData().getValue();

        if(resource != null) {
            List<ItemCategory> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                itemCategoryViewModel.offset=0;
                itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(itemCategoryViewModel.offset));
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }
        super.onResume();
    }

    @Override
    public void onDispatched() {
        if (itemCategoryViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().categoryList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }
}
