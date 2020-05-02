package com.panaceasoft.pscity.ui.item.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentFavouriteListBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.adapter.ItemVerticalListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;


public class FavouriteListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private FavouriteViewModel favouriteViewModel;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentFavouriteListBinding> binding;
    private AutoClearedValue<ItemVerticalListAdapter> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFavouriteListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();

    }

    @Override
    public void onDispatched() {
        if (favouriteViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().favouriteList != null) {

                GridLayoutManager layoutManager = (GridLayoutManager)
                        binding.get().favouriteList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(),false);

        binding.get().favouriteList.setNestedScrollingEnabled(false);
        binding.get().favouriteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !favouriteViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                favouriteViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.ITEM_COUNT;
                                favouriteViewModel.offset = favouriteViewModel.offset + limit;

                                favouriteViewModel.setNextPageLoadingFavouriteObj(String.valueOf(favouriteViewModel.offset), loginUserId);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            favouriteViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            favouriteViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            favouriteViewModel.forceEndLoading = false;

            // update live data
            favouriteViewModel.setItemFavouriteListObj(loginUserId, String.valueOf(favouriteViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        favouriteViewModel = new ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent, new ItemVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToDetailActivity(getActivity(), item);
            }

            @Override
            public void onFavLikeClick(Item item, LikeButton likeButton) {
                favFunction(item,likeButton);
            }

            @Override
            public void onFavUnlikeClick(Item item, LikeButton likeButton) {
                unFavFunction(item,likeButton);
            }

        }, this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().favouriteList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        favouriteViewModel.getNextPageFavouriteLoadingData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {

                    favouriteViewModel.setLoadingState(false);//hide
                    favouriteViewModel.forceEndLoading = true;//stop

                }
            }
        });

        favouriteViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(favouriteViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

        favouriteViewModel.setItemFavouriteListObj(loginUserId, String.valueOf(favouriteViewModel.offset));

        LiveData<Resource<List<Item>>> news = favouriteViewModel.getItemFavouriteData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

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

                            favouriteViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            favouriteViewModel.setLoadingState(false);
                            favouriteViewModel.forceEndLoading = true;

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data

                    if (favouriteViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        favouriteViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (FavouriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }

                } else if (result.status == Status.ERROR) {
                    if (FavouriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void replaceData(List<Item> favouriteList) {

        adapter.get().replace(favouriteList);
        binding.get().executePendingBindings();

    }

    private void unFavFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if(!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });

    }

    private void favFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if(!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
    }
}
