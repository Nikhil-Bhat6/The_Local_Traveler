package com.panaceasoft.pscity.ui.specification;

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
import com.panaceasoft.pscity.databinding.FragmentSpecificationListBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.SpecsViewModel;
import com.panaceasoft.pscity.viewobject.ItemSpecs;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class SpecificationListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private SpecsViewModel specsViewModel;
    public String itemId;
    public String itemName;


    @VisibleForTesting
    AutoClearedValue<FragmentSpecificationListBinding> binding;
    AutoClearedValue<SpecificationAdapter> specificationAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSpecificationListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_specification_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        
        binding.get().setLoadingMore(connectivity.isConnected());

        setHasOptionsMenu(true);

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

            navigationController.navigateToAddingSpecification(getActivity(), itemId, "", "","");
            specsViewModel.loadingDirection = Utils.LoadingDirection.top;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDispatched() {

        if (specsViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().attributeHeaderList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void initUIAndActions() {
        binding.get().attributeHeaderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == specificationAdapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !specsViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            specsViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.SPECIFICATION_COUNT;
                            specsViewModel.offset = specsViewModel.offset + limit;

                            specsViewModel.setNextPageLoadingStateObj(itemId, String.valueOf(Config.SPECIFICATION_COUNT), String.valueOf(specsViewModel.offset));
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            specsViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            specsViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            specsViewModel.forceEndLoading = false;

            // update live data
            specsViewModel.setSpecificObj(itemId, String.valueOf(Config.SPECIFICATION_COUNT), String.valueOf(specsViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        specsViewModel = new ViewModelProvider(this, viewModelFactory).get(SpecsViewModel.class);
    }

    @Override
    protected void initAdapters() {

        SpecificationAdapter nvAdapter = new SpecificationAdapter(dataBindingComponent, new SpecificationAdapter.AttributeHeaderClickCallback() {
            @Override
            public void onClick(ItemSpecs itemSpecs) {
                navigationController.navigateToAddingSpecification(getActivity(), itemId, itemSpecs.id, itemSpecs.name, itemSpecs.description);
            }

            @Override
            public void onEditClick(ItemSpecs itemSpecs) {
                navigationController.navigateToAddingSpecification(getActivity(), itemId, itemSpecs.id, itemSpecs.name, itemSpecs.description);
            }

            @Override
            public void onDeleteClick(ItemSpecs itemSpecs) {


                PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
                psDialogMsg.showConfirmDialog(getString(R.string.specification__warning_delete),getString(R.string.app__ok),getString(R.string.app__cancel));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v -> {
                    psDialogMsg.cancel();
                    specsViewModel.setDeleteSpecificationObj(itemId, itemSpecs.id);
                });

                psDialogMsg.cancelButton.setOnClickListener(v -> psDialogMsg.cancel());
            }
        },this);

        this.specificationAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().attributeHeaderList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    itemName = getActivity().getIntent().getExtras().getString(Constants.ITEM_NAME);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        loadData();

        specsViewModel.getDeleteSpecificationData().observe(this, result -> {
            if( result != null )
            {
                switch (result.status)
                {
                    case SUCCESS:
                        break;

                    case ERROR:
                        break;
                }
            }
        });
    }

    private void loadData() {

        // Load attribute header list
        specsViewModel.setSpecificObj(itemId, String.valueOf(Config.SPECIFICATION_COUNT), String.valueOf(specsViewModel.offset));

        LiveData<Resource<List<ItemSpecs>>> news = specsViewModel.getSpecificObj();

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

                            specsViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            specsViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (specsViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        specsViewModel.forceEndLoading = true;
                    }
                }
            });
        }

        specsViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    specsViewModel.setLoadingState(false);
                    specsViewModel.forceEndLoading = true;
                }
            }
        });

        specsViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(specsViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void replaceData(List<ItemSpecs> attributeHeaderList) {
        specificationAdapter.get().replace(attributeHeaderList);
        binding.get().executePendingBindings();
    }
    //endregion

    @Override
    public void onResume() {
        Resource<List<ItemSpecs>> resource = specsViewModel.getSpecificObj().getValue();

        if(resource != null) {
            List<ItemSpecs> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                specsViewModel.offset=0;
                specsViewModel.setSpecificObj(itemId, String.valueOf(Config.SPECIFICATION_COUNT), String.valueOf(specsViewModel.offset));
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }
        super.onResume();
    }

}
