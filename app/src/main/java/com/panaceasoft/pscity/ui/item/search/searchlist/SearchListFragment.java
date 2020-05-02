package com.panaceasoft.pscity.ui.item.search.searchlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentItemListBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.ArrayList;
import java.util.List;


public class SearchListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private boolean typeClicked = false;
    private boolean filterClicked = false;
    private boolean mapFilterClicked = false;
    private List<Item> clearRecyclerView = new ArrayList<>();
    private ItemViewModel itemViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemListBinding> binding;
    private AutoClearedValue<SearchListAdapter> adapter;

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        FragmentItemListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__CATEGORY_FILTER) {

            String catId = data.getStringExtra(Constants.CATEGORY_ID);
            if(catId != null){
                itemViewModel.holder.cat_id = catId;
            }
            String subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID);
            if(subCatId != null){
                itemViewModel.holder.sub_cat_id = subCatId;
            }

            typeClicked = (itemViewModel.holder.cat_id != null && !itemViewModel.holder.cat_id.equals(""))
                    || (itemViewModel.holder.sub_cat_id != null && !itemViewModel.holder.sub_cat_id.equals(""));

            typeButtonClicked(typeClicked);

            replaceData(clearRecyclerView);

            loadItemList();

        } else if (requestCode == Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__SPECIAL_FILTER) {

            // Result From Filter
//
            if (data.getSerializableExtra(Constants.FILTERING_HOLDER) != null) {

                itemViewModel.holder = (ItemParameterHolder) data.getSerializableExtra(Constants.FILTERING_HOLDER);

            }

            filterClicked = !itemViewModel.holder.keyword.equals("") || !itemViewModel.holder.order_by.equals(Constants.FILTERING_FEATURE) ||
                    !itemViewModel.holder.is_promotion.equals("") || !itemViewModel.holder.order_type.equals(Constants.FILTERING_DESC) ||
                    !itemViewModel.holder.is_featured.equals("");

            tuneButtonClicked(filterClicked);

            replaceData(clearRecyclerView);

            loadItemList();

        } else if (requestCode == Constants.REQUEST_CODE__MAP_FILTERING
                && resultCode == Constants.RESULT_CODE__MAP_FILTERING) {

            if (data.getSerializableExtra(Constants.ITEM_HOLDER) != null) {

                itemViewModel.holder = (ItemParameterHolder) data.getSerializableExtra(Constants.ITEM_HOLDER);

            }

            mapFilterClicked = !itemViewModel.holder.lat.equals("") || !itemViewModel.holder.lng.equals("") ||
                    !itemViewModel.holder.miles.equals("");

            mapFilterButtonClicked(mapFilterClicked);

            replaceData(clearRecyclerView);

            loadItemList();

        }

    }


    @Override
    protected void initUIAndActions() {
        if(getActivity() instanceof MainActivity)  {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        binding.get().typeButton.setOnClickListener(this::ButtonClick);

        binding.get().tuneButton.setOnClickListener(this::ButtonClick);

        binding.get().sortButton.setOnClickListener(this::ButtonClick);

        binding.get().newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !itemViewModel.forceEndLoading) {

                            itemViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.ITEM_COUNT;

                            itemViewModel.offset = itemViewModel.offset + limit;

                            loadNextPageItemList(String.valueOf(itemViewModel.offset));

                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemViewModel.forceEndLoading = false;

            itemViewModel.loadingDirection = Utils.LoadingDirection.top;

            loadItemList();

        });
    }


    @Override
    protected void initViewModels() {
        // ViewModel need to get from ViewModelProviders
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);

    }

    @Override
    protected void initAdapters() {

        SearchListAdapter nvAdapter = new SearchListAdapter(dataBindingComponent, item -> navigationController.navigateToSelectedItemDetail(getActivity(), item.id, item.name), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().newsList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        if (getActivity() != null) {

//            if (this.getActivity() instanceof SearchListActivity) {
                itemViewModel.holder = (ItemParameterHolder) getActivity().getIntent().getSerializableExtra(Constants.ITEM_PARAM_HOLDER_KEY);

//            }else if(this.getActivity() instanceof SelectedCityActivity){
                if(getArguments() != null) {
                    itemViewModel.holder = (ItemParameterHolder) getArguments().getSerializable(Constants.ITEM_PARAM_HOLDER_KEY);
                }
//            }

            if(itemViewModel.holder != null) {
                if (!itemViewModel.holder.keyword.isEmpty()) {
                    binding.get().buttonLayout.setVisibility(View.GONE);
                }
                else {
                    binding.get().buttonLayout.setVisibility(View.VISIBLE);
                }
            }

            initItemData();

//            setTouchCount();

            filterClicked = !itemViewModel.holder.keyword.equals(Constants.FILTERING_INACTIVE) ||
                    !itemViewModel.holder.is_featured.equals(Constants.FILTERING_INACTIVE) ||
                    !itemViewModel.holder.is_promotion.equals(Constants.FILTERING_INACTIVE) ||
                    !itemViewModel.holder.rating_value.equals(Constants.FILTERING_INACTIVE) ||
                    !itemViewModel.holder.order_by.equals(Constants.FILTERING_INACTIVE) ||
                    !itemViewModel.holder.order_type.equals(Constants.FILTERING_INACTIVE);

            typeClicked = !itemViewModel.holder.cat_id.equals(Constants.FILTERING_INACTIVE) || !itemViewModel.holder.sub_cat_id.equals(Constants.FILTERING_INACTIVE);

            mapFilterClicked = !itemViewModel.holder.lat.equals("") || !itemViewModel.holder.lng.equals("") ||
                    !itemViewModel.holder.miles.equals("");

            typeButtonClicked(typeClicked);

            tuneButtonClicked(filterClicked);

            mapFilterButtonClicked(mapFilterClicked);

        }

        // touch count post method
//        if (connectivity.isConnected()) {
//
//            touchCountViewModel.setTouchCountPostDataObj(loginUserId, itemViewModel.holder.cat_id, Constants.FILTERING_TYPE_NAME_CAT, selectedCityId);
//
//        }
//        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
//            if (result != null) {
//                if (result.status == Status.SUCCESS) {
//                    if (SearchListFragment.this.getActivity() != null) {
//                        Utils.psLog(result.status.toString());
//                    }
//
//                } else if (result.status == Status.ERROR) {
//                    if (SearchListFragment.this.getActivity() != null) {
//                        Utils.psLog(result.status.toString());
//                    }
//                }
//            }
//        });
    }

    //endregion


    //region Private Methods

    private void initItemData() {

        loadItemList();

        LiveData<Resource<List<Item>>> news = itemViewModel.getItemListByKeyData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {
                    switch (listResource.status) {
                        case SUCCESS:

                            itemViewModel.setLoadingState(false);

                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {

                                    if (!binding.get().getLoadingMore()) {
                                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {

                                    binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                }

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                onDispatched();
                            }
                            break;

                        case LOADING:
                            if (listResource.data != null) {
//                                if (listResource.data.size() == 0) {
////
////                                    if (!binding.get().getLoadingMore()) {
////                                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
////                                    }
////
////                                } else {
////                                    binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);
////
////                                }

                                binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                itemViewModel.setLoadingState(true);

                                if (itemViewModel.forceEndLoading) {
                                    itemViewModel.forceEndLoading = false;
                                }

                                onDispatched();
                            }
                            break;

                        case ERROR:

                            itemViewModel.setLoadingState(false);
                            itemViewModel.forceEndLoading = true;

                            if (itemViewModel.getItemListByKeyData() != null) {
                                if (itemViewModel.getItemListByKeyData().getValue() != null) {
                                    if (itemViewModel.getItemListByKeyData().getValue().data != null) {
                                        if (!binding.get().getLoadingMore() && itemViewModel.getItemListByKeyData().getValue().data.size() == 0) {
                                            binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        itemViewModel.getNextPageItemListByKeyData().observe(this, state -> {

            if (state != null) {
                if (state.status == Status.ERROR) {

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

    private void replaceData(List<Item> newsList) {
        adapter.get().replace(newsList);
        binding.get().executePendingBindings();
    }


    private void typeButtonClicked(Boolean b) {
        if (b) {
            binding.get().typeButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_list_with_check_black_24), null, null);
        } else {
            binding.get().typeButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_list_black_24), null, null);
        }
    }

    private void tuneButtonClicked(Boolean b) {
        if (b) {

            binding.get().tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_tune_with_check_black_24), null, null);

        } else {
            binding.get().tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_tune_black_24), null, null);
        }
    }

    private void mapFilterButtonClicked(Boolean b) {
        if (b) {

            binding.get().sortButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_filter_map_with_check_black_24), null, null);

        } else {
            binding.get().sortButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_filter_map_black_24), null, null);
        }
    }

    private void ButtonClick(View view) {

        switch (view.getId()) {
            case R.id.typeButton:

                navigationController.navigateToTypeFilterFragment(SearchListFragment.this.getActivity(), itemViewModel.holder.cat_id,
                        itemViewModel.holder.sub_cat_id, itemViewModel.holder, Constants.FILTERING_TYPE_FILTER);

                typeButtonClicked(typeClicked);

                break;

            case R.id.tuneButton:

                navigationController.navigateToTypeFilterFragment(SearchListFragment.this.getActivity(), itemViewModel.holder.cat_id,
                        itemViewModel.holder.sub_cat_id, itemViewModel.holder, Constants.FILTERING_SPECIAL_FILTER);

                tuneButtonClicked(filterClicked);

                break;

            case R.id.sortButton:

//                mBottomSheetDialog.get().show();
//                ButtonSheetClick();

                navigationController.navigateToMapFiltering(this.getActivity(), itemViewModel.holder);

                break;

            default:
                Utils.psLog("No ID for Buttons");
        }
    }

    @Override
    public void onDispatched() {

        if (itemViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get() != null) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        binding.get().newsList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        }
    }

    private void resetLimitAndOffset() {
        itemViewModel.offset = 0;
    }

    private void loadNextPageItemList(String offset) {

        checkAndUpdateFeatureSorting();

        itemViewModel.setNextPageItemListByKeyObj(String.valueOf(Config.ITEM_COUNT), offset, Utils.checkUserId(loginUserId), itemViewModel.holder);


    }

    private void loadItemList() {

        resetLimitAndOffset();

        checkAndUpdateFeatureSorting();

        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

    }

    private void checkAndUpdateFeatureSorting() {

        if (itemViewModel.holder.is_featured.equals(Constants.ONE)
                && itemViewModel.holder.order_by.equals(Constants.FILTERING_ADDED_DATE)) {
            itemViewModel.holder.order_by = Constants.FILTERING_FEATURE;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
    }
}

