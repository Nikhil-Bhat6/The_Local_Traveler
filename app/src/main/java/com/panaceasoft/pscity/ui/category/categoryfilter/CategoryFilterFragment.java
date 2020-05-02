package com.panaceasoft.pscity.ui.category.categoryfilter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.TypeFilterBinding;
import com.panaceasoft.pscity.ui.category.adapter.CategoryFilterAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.pscity.viewmodel.itemsubcategory.ItemSubCategoryViewModel;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.ItemSubCategory;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.util.List;

public class CategoryFilterFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ItemCategoryViewModel itemCategoryViewModel;
    private ItemSubCategoryViewModel subCategoryViewModel;
    private String catId, subCatId;
    public Intent intent = new Intent();

    @VisibleForTesting
    private AutoClearedValue<TypeFilterBinding> binding;
    private AutoClearedValue<CategoryFilterAdapter> adapter;
    private AutoClearedValue<List<ItemCategory>> lastCategoryData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        TypeFilterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.type_filter, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.clear_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clear) {
            this.catId = "";
            this.subCatId = "";

            initializeAdapter();

            initData();

            navigationController.navigateBackToHomeFeaturedFragment(CategoryFilterFragment.this.getActivity(), this.catId, this.subCatId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewModels() {
        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
        subCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemSubCategoryViewModel.class);

    }

    @Override
    protected void initAdapters() {

        try {
            if (getActivity() != null) {

                intent = getActivity().getIntent();

                this.catId = intent.getStringExtra(Constants.CATEGORY_ID);
                this.subCatId = intent.getStringExtra(Constants.SUBCATEGORY_ID);

            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        initializeAdapter();
    }

    private void initializeAdapter() {
        CategoryFilterAdapter nvAdapter = new CategoryFilterAdapter(dataBindingComponent, (catId, subCatId) -> {

            CategoryFilterFragment.this.assignCategoryId(catId, subCatId);

            navigationController.navigateBackToHomeFeaturedFragment(CategoryFilterFragment.this.getActivity(), catId, subCatId);

            if (getActivity() != null) {
                CategoryFilterFragment.this.getActivity().finish();
            }

        }, this.catId, this.subCatId);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().CategoryList.setAdapter(nvAdapter);
    }

    private void assignCategoryId(String catId, String subCatId) {
        this.catId = catId;
        this.subCatId = subCatId;

    }

    @Override
    protected void initData() {

        itemCategoryViewModel.categoryParameterHolder.cityId = selectedCityId;

        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(itemCategoryViewModel.offset));
        subCategoryViewModel.setAllSubCategoryListObj();

        LiveData<Resource<List<ItemCategory>>> categories = itemCategoryViewModel.getCategoryListData();
        LiveData<Resource<List<ItemSubCategory>>> subCategories = subCategoryViewModel.getAllSubCategoryListData();


        if (categories != null) {

            categories.observe(this, listResource -> {
                if (listResource != null) {

                    if (listResource.data != null && listResource.data.size() > 0) {

                        lastCategoryData = new AutoClearedValue<>(this, listResource.data);
                        replaceCategory(lastCategoryData.get());

                    }

                } else {

                    // Init Object or Empty Data

                    if (itemCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemCategoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        if (subCategories != null) {

            subCategories.observe(this, listResource -> {
                if (listResource != null) {


                    if (listResource.data != null && listResource.data.size() > 0) {

                        replaceSubCategory(listResource.data);
                    }


                } else {

                    // Init Object or Empty Data

                    if (subCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        subCategoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

    }


    private void replaceCategory(List<ItemCategory> CategoryList) {

        adapter.get().replaceCategory(CategoryList);
        adapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();

    }

    private void replaceSubCategory(List<ItemSubCategory> subCategoryList) {

        adapter.get().replaceSubCategory(subCategoryList);
        adapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();

    }

    @Override
    public void onDispatched() {

//      if  (itemCategoryViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
////            if (binding.get().CategoryList != null) {
////
////                LinearLayoutManager layoutManager = (LinearLayoutManager)
////                        binding.get().CategoryList.getLayoutManager();
////
////
////                if (layoutManager != null) {
////                    layoutManager.scrollToPosition(0);
////                }
////            }
//        }
//
    }
}
