package com.panaceasoft.pscity.ui.item.rating;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentRatingListBinding;
import com.panaceasoft.pscity.databinding.ItemRatingEntryBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.rating.adapter.RatingListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewmodel.rating.RatingViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.Rating;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ItemViewModel itemViewModel;
    private RatingViewModel ratingViewModel;
    private PSDialogMsg psDialogMsg , psDialogRatingMsg;

    @VisibleForTesting
    private AutoClearedValue<RatingListAdapter> adapter;
    private AutoClearedValue<FragmentRatingListBinding> binding;
    private AutoClearedValue<Dialog> dialog;
    private AutoClearedValue<ProgressDialog> prgDialog;
    private AutoClearedValue<ItemRatingEntryBinding> itemRatingEntryBinding;

    public RatingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRatingListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);
        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_rating, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.edit_rating) {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this.getActivity(), navigationController, this::getCustomLayoutDialog);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(),false);
        psDialogRatingMsg = new PSDialogMsg(getActivity(),false);

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().ratingListRecyclerView.setNestedScrollingEnabled(false);

        binding.get().writeReviewButton.setOnClickListener(v -> Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, RatingListFragment.this.getActivity(), navigationController, this::getCustomLayoutDialog));


        binding.get().ratingListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!ratingViewModel.forceEndLoading) {

                            ratingViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.ITEM_COUNT;

                            ratingViewModel.offset = ratingViewModel.offset + limit;

                            ratingViewModel.setNextPageLoadingStateObj(itemViewModel.itemId, String.valueOf(Config.RATING_COUNT), Constants.ZERO);

                        }
                    }
                }
            }
        });

    }

    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        ratingViewModel = new ViewModelProvider(this, viewModelFactory).get(RatingViewModel.class);

    }

    @Override
    protected void initAdapters() {
        RatingListAdapter nvAdapter = new RatingListAdapter(dataBindingComponent,
                rating -> {
//                        navigationController.navigateToAboutUs(getContext());
                }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().ratingListRecyclerView.setAdapter(nvAdapter);
    }

    @Override
    public void onDispatched() {

    }


    @Override
    protected void initData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent() != null) {
                    if (getActivity().getIntent().getExtras() != null) {

                        itemViewModel.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load item detail
        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

        LiveData<Resource<Item>> itemDetail = itemViewModel.getItemDetailData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                bindingRatingData(listResource.data);
                                bindingProgressBar(listResource.data);

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }
                    // itemViewModel.isProductDetailData=true;

                } else {

                    //itemViewModel.isProductDetailData=false;
                    itemViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");
                }
            });
        }

        //rating list
        ratingViewModel.setRatingListObj(itemViewModel.itemId, String.valueOf(Config.RATING_COUNT), Constants.ZERO);
        LiveData<Resource<List<Rating>>> news = ratingViewModel.getRatingList();

        if (news != null) {

            news.observe(this, (Resource<List<Rating>> listResource) -> {
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
                                replaceRatingData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceRatingData(listResource.data);
                                ratingViewModel.isData = listResource.data.size() == 0;


                            }

                            ratingViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            ratingViewModel.setLoadingState(false);


                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (ratingViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        ratingViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        //get rating post method
        ratingViewModel.getRatingPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (RatingListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);

                        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

                        dialog.get().dismiss();
                        dialog.get().cancel();
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }

                } else if (result.status == Status.ERROR) {
                    if (RatingListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }
                }
            }
        });

        ratingViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    ratingViewModel.setLoadingState(false);
                    ratingViewModel.forceEndLoading = true;
                }
            }
        });

        ratingViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(ratingViewModel.isLoading));

        //end region

    }

    private void bindingProgressBar(Item item) {
        binding.get().ProgressBar1.setProgress((int) item.ratingDetails.fiveStarPercent);
        binding.get().ProgressBar2.setProgress((int) item.ratingDetails.fourStarPercent);
        binding.get().ProgressBar3.setProgress((int) item.ratingDetails.threeStarPercent);
        binding.get().ProgressBar4.setProgress((int) item.ratingDetails.twoStarPercent);
        binding.get().ProgressBar5.setProgress((int) item.ratingDetails.oneStarPercent);

        setDrawableTint(binding.get().ProgressBar1);
        setDrawableTint(binding.get().ProgressBar2);
        setDrawableTint(binding.get().ProgressBar3);
        setDrawableTint(binding.get().ProgressBar4);
        setDrawableTint(binding.get().ProgressBar5);

    }

    private void setDrawableTint(ProgressBar progressBar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(binding.get().getRoot().getContext(), R.color.md_yellow_A700));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(binding.get().getRoot().getContext(), R.color.md_yellow_A700), PorterDuff.Mode.SRC_IN);
        }
    }

    private void getCustomLayoutDialog() {
        dialog = new AutoClearedValue<>(this, new Dialog(binding.get().getRoot().getContext()));
        itemRatingEntryBinding = new AutoClearedValue<>(this, DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_rating_entry, null, false, dataBindingComponent));

        dialog.get().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.item_rating_entry);
        dialog.get().setContentView(itemRatingEntryBinding.get().getRoot());

        itemRatingEntryBinding.get().ratingBarDialog.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            ratingViewModel.numStar = rating;
            itemRatingEntryBinding.get().ratingBarDialog.setRating(rating);
        });

        itemRatingEntryBinding.get().cancelButton.setOnClickListener(v -> {
            dialog.get().dismiss();
            dialog.get().cancel();
        });

        itemRatingEntryBinding.get().submitButton.setOnClickListener(v -> {

            if(itemRatingEntryBinding.get().titleEditText.getText().toString().isEmpty() ||
                    itemRatingEntryBinding.get().messageEditText.getText().toString().isEmpty() || String.valueOf(itemRatingEntryBinding.get().ratingBarDialog.getRating()).equals("0.0")) {

                psDialogRatingMsg.showErrorDialog(getString(R.string.error_message__rating), getString(R.string.app__ok));
                psDialogRatingMsg.show();
                psDialogRatingMsg.okButton.setOnClickListener(v1 -> psDialogRatingMsg.cancel());
            }
            else {

                ratingViewModel.setRatingPostObj(itemRatingEntryBinding.get().titleEditText.getText().toString(),
                        itemRatingEntryBinding.get().messageEditText.getText().toString(),
                        ratingViewModel.numStar + "",
                        loginUserId, itemViewModel.itemId);

                prgDialog.get().show();

                if (!ratingViewModel.isData) {
                    ratingViewModel.setLoadingState(false);
                    ratingViewModel.setRatingListObj(itemViewModel.itemId, String.valueOf(Config.RATING_COUNT), Constants.ZERO);
                }
            }

        });


        Window window = dialog.get().getWindow();
        if (dialog.get() != null && window != null) {
            WindowManager.LayoutParams params = getLayoutParams(dialog.get());
            if (params != null) {
                window.setAttributes(params);
            }
        }

        dialog.get().show();

    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }


    private void bindingRatingData(Item item) {

        binding.get().totalRatingCountTextView.setText(getString(R.string.rating__total_count, String.valueOf(item.ratingDetails.totalRatingCount)));
        binding.get().totalRatingValueTextView.setText(getString(R.string.rating__total_value, String.valueOf((int) item.ratingDetails.totalRatingValue)));

        binding.get().ratingCountTextView1.setText(getString(R.string.rating__five_star));
        binding.get().ratingPercentTextView1.setText(getString(R.string.rating__percent, String.valueOf((int) item.ratingDetails.fiveStarPercent)));

        binding.get().ratingCountTextView2.setText(getString(R.string.rating__four_star));
        binding.get().ratingPercentTextView2.setText(getString(R.string.rating__percent, String.valueOf((int) item.ratingDetails.fourStarPercent)));

        binding.get().ratingCountTextView3.setText(getString(R.string.rating__three_star));
        binding.get().ratingPercentTextView3.setText(getString(R.string.rating__percent, String.valueOf((int) item.ratingDetails.threeStarPercent)));

        binding.get().ratingCountTextView4.setText(getString(R.string.rating__two_star));
        binding.get().ratingPercentTextView4.setText(getString(R.string.rating__percent, String.valueOf((int) item.ratingDetails.twoStarPercent)));

        binding.get().ratingCountTextView5.setText(getString(R.string.rating__one_star));
        binding.get().ratingPercentTextView5.setText(getString(R.string.rating__percent, String.valueOf((int) item.ratingDetails.oneStarPercent)));

        binding.get().ratingBar.setRating(item.ratingDetails.totalRatingValue);


    }

    private void replaceRatingData(List<Rating> ratingList) {
        adapter.get().replace(ratingList);
        binding.get().executePendingBindings();
    }

    @Override
    public void onResume() {
        loadLoginUserId();
        super.onResume();
    }
}
