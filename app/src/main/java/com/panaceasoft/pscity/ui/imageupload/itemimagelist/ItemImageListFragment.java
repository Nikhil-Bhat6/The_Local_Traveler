package com.panaceasoft.pscity.ui.imageupload.itemimagelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentItemImageListBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.upload.ItemEntryImageAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.image.ImageViewModel;
import com.panaceasoft.pscity.viewobject.Image;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

public class ItemImageListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    ImageViewModel imageViewModel;

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    AutoClearedValue<FragmentItemImageListBinding> binding;
    AutoClearedValue<ItemEntryImageAdapter> itemEntryImageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentItemImageListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_image_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
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

            navigationController.navigateToImageUploadActivity(getActivity(), "", "", "", Constants.IMAGE_UPLOAD_ITEM, imageViewModel.item_id);
            imageViewModel.loadingDirection = Utils.LoadingDirection.top;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        binding.get().imageListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == itemEntryImageAdapter.get().getItemCount() - 1) {

                    if (!binding.get().getLoadingMore() && !imageViewModel.forceEndLoading) {

                        if (connectivity.isConnected()) {

                            imageViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.IMAGE_COUNT;
                            imageViewModel.offset = imageViewModel.offset + limit;

                            imageViewModel.setNextImageListByIdObj( imageViewModel.item_id, Config.IMAGE_COUNT_ENTRY, String.valueOf(imageViewModel.offset));

                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            imageViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            imageViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            imageViewModel.forceEndLoading = false;

            // update live data

            imageViewModel.setNextImageListByIdObj(imageViewModel.item_id, Config.IMAGE_COUNT_ENTRY, String.valueOf(imageViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        imageViewModel = new ViewModelProvider(this, viewModelFactory).get(ImageViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemEntryImageAdapter nvAdapter = new ItemEntryImageAdapter(dataBindingComponent, new ItemEntryImageAdapter.ItemImageViewClickCallback() {
            @Override
            public void onClick(Image item) {
//                Toast.makeText(getContext(),"On click",Toast.LENGTH_SHORT).show();
                navigationController.navigateToImageUploadActivity(getActivity(),item.imgId, item.imgPath, item.imgDesc, Constants.IMAGE_UPLOAD_ITEM, imageViewModel.item_id);
            }

            @Override
            public void onDeleteClick(Image deleteImage) {
                imageViewModel.setDeleteImageObj(imageViewModel.item_id,deleteImage.imgId);
            }
        },this);

        itemEntryImageAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().imageListRecyclerView.setAdapter(itemEntryImageAdapter.get());
    }


    @Override
    protected void initData() {

        if(getActivity() != null)
        {
            if(getActivity().getIntent().getExtras() != null)
            {
                imageViewModel.item_id = getActivity().getIntent().getStringExtra(Constants.ITEM_ID);
            }

        }

        imageViewModel.setImageListByIdObj(imageViewModel.item_id, Config.IMAGE_COUNT_ENTRY, String.valueOf(imageViewModel.offset),Config.IMAGE_COUNT_ENTRY);

        imageViewModel.getImageListByIdLiveData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case LOADING:
                        if (result.data != null) {
                            replaceImages(result.data);
                        }
                        break;

                    case SUCCESS:
                        if (result.data != null) {
                            replaceImages(result.data);
                        }
                        imageViewModel.setLoadingState(false);
                        break;

                    case ERROR:
                        Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();
                        imageViewModel.setLoadingState(false);
                        break;

                }
            }
        });

        imageViewModel.getNextImageListByIdLiveData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    imageViewModel.setLoadingState(false);//hide
                    imageViewModel.forceEndLoading = true;//stop
                }
            }
        });

        imageViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(imageViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

        //Deleting Image

        imageViewModel.getDeleteImageData().observe(this, result -> {

            if (result != null) {

                switch (result.status) {
                    case SUCCESS:
                        Toast.makeText(getContext(), "Succeed", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR:
                        Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        //Deleting Image
    }

    public void replaceImages(List<Image> images)
    {
        itemEntryImageAdapter.get().replace(images);
        binding.get().executePendingBindings();
    }

    @Override
    public void onResume() {
        Resource<List<Image>> resource = imageViewModel.getImageListByIdLiveData().getValue();

        if(resource != null) {
            List<Image> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                imageViewModel.setImageListByIdObj(imageViewModel.item_id, Config.IMAGE_COUNT_ENTRY, "0",Config.IMAGE_COUNT_ENTRY);
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }
        super.onResume();
    }

    @Override
    public void onDispatched() {
        if (imageViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().imageListRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }
}
