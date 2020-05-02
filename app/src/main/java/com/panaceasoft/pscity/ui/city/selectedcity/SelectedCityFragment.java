package com.panaceasoft.pscity.ui.city.selectedcity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentSelectedCityBinding;
import com.panaceasoft.pscity.ui.category.adapter.CityCategoryAdapter;
import com.panaceasoft.pscity.ui.city.selectedcity.adapter.ItemCollectionRowAdapter;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.dashboard.adapter.DashBoardViewPagerAdapter;
import com.panaceasoft.pscity.ui.item.adapter.ItemListAdapter;
import com.panaceasoft.pscity.ui.item.adapter.ItemPopularListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.blog.BlogViewModel;
import com.panaceasoft.pscity.viewmodel.city.CityViewModel;
import com.panaceasoft.pscity.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.pscity.viewmodel.item.DiscountItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.FeaturedItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.PopularItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.RecentItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.pscity.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.pscity.viewmodel.itemcollection.ItemCollectionViewModel;
import com.panaceasoft.pscity.viewobject.Blog;
import com.panaceasoft.pscity.viewobject.City;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;
import com.panaceasoft.pscity.viewobject.common.Status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class SelectedCityFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private CityViewModel cityViewModel;
    private ItemCategoryViewModel itemCategoryViewModel;
    private FeaturedItemViewModel featuredItemViewModel;
    private PopularItemViewModel popularItemViewModel;
    private RecentItemViewModel recentItemViewModel;
    private DiscountItemViewModel discountItemViewModel;
    private TouchCountViewModel touchCountViewModel;
    private BlogViewModel blogViewModel;
    private ItemCollectionViewModel itemCollectionViewModel;
    private ImageView[] dots;
    private boolean layoutDone = false;
    private int loadingCount = 0;
    private PSDialogMsg psDialogMsg;
    private ClearAllDataViewModel clearAllDataViewModel;

    private Runnable update;
    private int NUM_PAGES = 10;
    private int currentPage = 0;
    private boolean touched = false;
    private Timer unTouchedTimer;
    private Handler handler = new Handler();

    @Inject
    protected SharedPreferences pref;
    private String startDate = Constants.ZERO;
    private String endDate = Constants.ZERO;

    @VisibleForTesting
    private AutoClearedValue<FragmentSelectedCityBinding> binding;
    private AutoClearedValue<ItemListAdapter> featuredItemListAdapter;
    private AutoClearedValue<ItemPopularListAdapter> popularItemListAdapter;
    private AutoClearedValue<ItemListAdapter> recentItemListAdapter;
    private AutoClearedValue<ItemListAdapter> discountItemListAdapter;
    private AutoClearedValue<DashBoardViewPagerAdapter> dashBoardViewPagerAdapter;
    private AutoClearedValue<CityCategoryAdapter> cityCategoryAdapter;
    private AutoClearedValue<ItemCollectionRowAdapter> verticalRowAdapter;
    private AutoClearedValue<ViewPager> viewPager;
    private AutoClearedValue<LinearLayout> pageIndicatorLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSelectedCityBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_city, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.blog_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem blogMenuItem = menu.findItem(R.id.action_blog);
        blogMenuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_blog) {
            navigationController.navigateToBlogListBySelectedCity(getActivity(), selectedCityId);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if(!Config.ENABLE_ITEM_UPLOAD){
            binding.get().floatingActionButton.setVisibility(View.GONE);
        }else{
            binding.get().floatingActionButton.setVisibility(View.VISIBLE);
        }

        if (getActivity() instanceof MainActivity) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity) getActivity()).updateMenuIconWhite();
        }

        binding.get().shareImageView.setVisibility(View.GONE);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
            AdRequest adRequest2 = new AdRequest.Builder()
                    .build();
            binding.get().adView2.loadAd(adRequest2);
        } else {
            binding.get().adView.setVisibility(View.GONE);
            binding.get().adView2.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        viewPager = new AutoClearedValue<>(this, binding.get().blogViewPager);

        pageIndicatorLayout = new AutoClearedValue<>(this, binding.get().pagerIndicator);


        binding.get().blogViewAllTextView.setOnClickListener(v -> navigationController.navigateToBlogListBySelectedCity(getActivity(), selectedCityId));

        binding.get().knowMoreButton.setOnClickListener(v -> navigationController.navigateToCityActivity(getActivity(), cityViewModel.cityName));

        binding.get().featuredViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), featuredItemViewModel.featuredItemParameterHolder, getString(R.string.dashboard_best_things)));

        binding.get().popularViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), popularItemViewModel.popularItemParameterHolder, getString(R.string.dashboard_popular_places)));

        binding.get().recentItemViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), recentItemViewModel.recentItemParameterHolder, getString(R.string.dashboard_new_places)));

        binding.get().promoViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), discountItemViewModel.discountItemParameterHolder, getString(R.string.dashboard_promo_list)));

        binding.get().shareImageView.setOnClickListener(v -> {

        });

        binding.get().categoryViewAllTextView.setOnClickListener(v -> navigationController.navigateToCategoryActivity(getActivity()));

        if (viewPager.get() != null && viewPager.get() != null && viewPager.get() != null) {
            viewPager.get().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    currentPage = position;

                    if (pageIndicatorLayout.get() != null) {

                        setupSliderPagination();
                    }

                    for (ImageView dot : dots) {
                        if (dots != null) {
                            dot.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dots != null && dots.length > position) {
                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }

                    touched = true;

                    handler.removeCallbacks(update);

                    setUnTouchedTimer();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        startPagerAutoSwipe();
        setCityTouchCount();

        if (force_update) {
            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_title, force_update_msg);
        }

        binding.get().floatingActionButton.setOnClickListener(view ->
                Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, () ->
                        navigationController.navigateToItemUploadActivity(getActivity(),null)));
    }

    @Override
    protected void initViewModels() {

        cityViewModel = new ViewModelProvider(this, viewModelFactory).get(CityViewModel.class);
        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
        featuredItemViewModel = new ViewModelProvider(this, viewModelFactory).get(FeaturedItemViewModel.class);
        popularItemViewModel = new ViewModelProvider(this, viewModelFactory).get(PopularItemViewModel.class);
        recentItemViewModel = new ViewModelProvider(this, viewModelFactory).get(RecentItemViewModel.class);
        popularItemViewModel = new ViewModelProvider(this, viewModelFactory).get(PopularItemViewModel.class);
        discountItemViewModel = new ViewModelProvider(this, viewModelFactory).get(DiscountItemViewModel.class);
        blogViewModel = new ViewModelProvider(this, viewModelFactory).get(BlogViewModel.class);
        touchCountViewModel = new ViewModelProvider(this, viewModelFactory).get(TouchCountViewModel.class);
        itemCollectionViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCollectionViewModel.class);
        clearAllDataViewModel = new ViewModelProvider(this, viewModelFactory).get(ClearAllDataViewModel.class);
    }

    @Override
    protected void initAdapters() {

        DashBoardViewPagerAdapter nvAdapter3 = new DashBoardViewPagerAdapter(dataBindingComponent, blog -> navigationController.navigateToBlogDetailActivity(SelectedCityFragment.this.getActivity(), blog.id));

        this.dashBoardViewPagerAdapter = new AutoClearedValue<>(this, nvAdapter3);
        viewPager.get().setAdapter(dashBoardViewPagerAdapter.get());

        CityCategoryAdapter cityCategoryAdapter = new CityCategoryAdapter(dataBindingComponent,
                category -> navigationController.navigateToSubCategoryActivity(getActivity(), category.id, category.name), this);

        this.cityCategoryAdapter = new AutoClearedValue<>(this, cityCategoryAdapter);
        binding.get().cityCategoryRecyclerView.setAdapter(cityCategoryAdapter);

        ItemCollectionRowAdapter verticalRowAdapter1 = new ItemCollectionRowAdapter(dataBindingComponent, new ItemCollectionRowAdapter.ItemClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToSelectedItemDetail(getActivity(), item.id, item.name);
            }

            @Override
            public void onViewAllClick(ItemCollectionHeader itemCollectionHeader) {
                navigationController.navigateToCollectionItemList(getActivity(), itemCollectionHeader.id, itemCollectionHeader.name, itemCollectionHeader.defaultPhoto.imgPath);
            }
        });

        this.verticalRowAdapter = new AutoClearedValue<>(this, verticalRowAdapter1);
        binding.get().collectionRecyclerView.setAdapter(verticalRowAdapter1);
        binding.get().collectionRecyclerView.setNestedScrollingEnabled(false);


        ItemListAdapter featuredItemListAdapter1 = new ItemListAdapter(dataBindingComponent, item -> navigationController.navigateToSelectedItemDetail(SelectedCityFragment.this.getActivity(), item.id, item.name), this);

        this.featuredItemListAdapter = new AutoClearedValue<>(this, featuredItemListAdapter1);
        binding.get().featuredItemRecyclerView.setAdapter(featuredItemListAdapter1);

        ItemPopularListAdapter popularAdapter = new ItemPopularListAdapter(dataBindingComponent, item -> navigationController.navigateToSelectedItemDetail(SelectedCityFragment.this.getActivity(), item.id, item.name), this);

        this.popularItemListAdapter = new AutoClearedValue<>(this, popularAdapter);
        binding.get().popularItemRecyclerView.setAdapter(popularAdapter);

        ItemListAdapter recentAdapter = new ItemListAdapter(dataBindingComponent, item ->
                navigationController.navigateToSelectedItemDetail(this.getActivity(), item.id, item.name), this);

        this.recentItemListAdapter = new AutoClearedValue<>(this, recentAdapter);
        binding.get().recentItemRecyclerView.setAdapter(recentAdapter);

        ItemListAdapter discountAdapter = new ItemListAdapter(dataBindingComponent, item ->
                navigationController.navigateToSelectedItemDetail(this.getActivity(), item.id, item.name), this);

        this.discountItemListAdapter = new AutoClearedValue<>(this, discountAdapter);
        binding.get().promoRecyclerView.setAdapter(discountAdapter);


    }

    private void replaceDiscountItemList(List<Item> itemList) {
        this.discountItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceRecentItemList(List<Item> itemList) {
        this.recentItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replacePopularItemList(List<Item> itemList) {
        this.popularItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceFeaturedItemList(List<Item> itemList) {

        this.featuredItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceCityCategory(List<ItemCategory> categories) {
        cityCategoryAdapter.get().replace(categories);
        binding.get().executePendingBindings();
    }

    private void replaceCollection(List<ItemCollectionHeader> itemCollectionHeaders) {
        verticalRowAdapter.get().replaceCollectionHeader(itemCollectionHeaders);
        binding.get().executePendingBindings();
    }


    @Override
    protected void initData() {

        if (connectivity.isConnected()) {
            if (startDate.equals(Constants.ZERO)) {

                startDate = getDateTime();
                Utils.setDatesToShared(startDate, endDate, pref);
            }

        }

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        break;
                }
            }
        });

        getIntentData();

        loadData();
    }

    private void getIntentData() {

    }

    private void loadData() {

        //City Detail

        cityViewModel.setCityInfoObj();

        cityViewModel.getCityInfoData().observe(this, result -> {

            if (result != null) {

                switch (result.status) {

                    case ERROR:
                        break;

                    case LOADING:

                        if (result.data != null) {

                            binding.get().cityNameTextView.setText(result.data.name);
                            cityViewModel.cityName = result.data.name;
                            binding.get().cityDescriptionTextView.setText(result.data.description);
                            cityViewModel.lat = result.data.lat;
                            cityViewModel.lng = result.data.lng;
                            cityViewModel.cityName = result.data.name;
                            dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().cityImageView, result.data.defaultPhoto.imgPath);

                            updateCityPref(result.data);

                        }

                        break;

                    case SUCCESS:

                        if (result.data != null) {

                                binding.get().cityNameTextView.setText(result.data.name);
                                cityViewModel.cityName = result.data.name;
                                binding.get().cityDescriptionTextView.setText(result.data.description);
                                cityViewModel.lat = result.data.lat;
                                cityViewModel.lng = result.data.lng;
                                cityViewModel.cityName = result.data.name;
                                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().cityImageView, result.data.defaultPhoto.imgPath);

                                updateCityPref(result.data);

                        }

                        break;

                }
            }
        });


        //Blog

        if (getActivity() != null) {
            selectedCityId = getActivity().getIntent().getStringExtra(Constants.CITY_ID);
        }

        blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT_BY_CITY_ID), String.valueOf(blogViewModel.offset));

        blogViewModel.getNewsFeedData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsFeedList(result.data);
                        blogViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceNewsFeedList(result.data);
                        break;

                    case ERROR:

                        blogViewModel.setLoadingState(false);
                        break;
                }
            }

        });


        //City Category

        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIMIT_FROM_DB_COUNT), Constants.ZERO);

        itemCategoryViewModel.getCategoryListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //Featured Item

        featuredItemViewModel.setFeaturedItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LIMIT_FROM_DB_COUNT), Constants.ZERO, featuredItemViewModel.featuredItemParameterHolder);

        featuredItemViewModel.getFeaturedItemListByKeyData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:

                        break;

                    case LOADING:

                        if (result.data != null) {
                            if (result.data.size() > 0) {
                                replaceFeaturedItemList(result.data);
                            }
                        }

                        break;

                    case SUCCESS:

                        if (result.data != null) {
                            if (result.data.size() > 0) {
                                replaceFeaturedItemList(result.data);
                            }
                        }

                        break;
                }
            }
        });

        //Popular Item

        popularItemViewModel.setPopularItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LIMIT_FROM_DB_COUNT), Constants.ZERO, popularItemViewModel.popularItemParameterHolder);

        popularItemViewModel.getPopularItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replacePopularItemList(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replacePopularItemList(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //Recent Item

        recentItemViewModel.setRecentItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LIMIT_FROM_DB_COUNT), Constants.ZERO, recentItemViewModel.recentItemParameterHolder);

        recentItemViewModel.getRecentItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceRecentItemList(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceRecentItemList(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //Discount Item

        discountItemViewModel.setDiscountItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LIMIT_FROM_DB_COUNT), Constants.ZERO, discountItemViewModel.discountItemParameterHolder);

        discountItemViewModel.getDiscountItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceDiscountItemList(listResource.data);
                            } else {
                                binding.get().promoTitleTextView.setVisibility(View.GONE);
                                binding.get().promoViewAllTextView.setVisibility(View.GONE);
                                binding.get().textView12.setVisibility(View.GONE);
                                binding.get().promoRecyclerView.setVisibility(View.GONE);
                            }
                        }


                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceDiscountItemList(listResource.data);
                            } else {
                                binding.get().promoTitleTextView.setVisibility(View.GONE);
                                binding.get().promoViewAllTextView.setVisibility(View.GONE);
                                binding.get().textView12.setVisibility(View.GONE);
                                binding.get().promoRecyclerView.setVisibility(View.GONE);
                            }
                        }


                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //Discount Item

        //ItemCollection

        itemCollectionViewModel.setAllItemCollectionObj(String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT), Constants.ZERO);

        itemCollectionViewModel.getAllItemCollectionHeader().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case ERROR:
                        Utils.psLog("Error is " + listResource.message);
                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceCollection(listResource.data);
                            }
                        }

                        break;

                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceCollection(listResource.data);
                            }
                        }

                        break;
                }
            }

        });

        //ItemCollection

        viewPager.get().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if (binding.get() != null && viewPager.get() != null) {
                    if (viewPager.get().getChildCount() > 0) {
                        layoutDone = true;
                        loadingCount++;
                        hideLoading();
                        viewPager.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    @Override
    public void onDispatched() {

    }

    private void setupSliderPagination() {

        int dotsCount = dashBoardViewPagerAdapter.get().getCount();

        if (dotsCount > 0 && dots == null) {

            dots = new ImageView[dotsCount];

            if (binding.get() != null) {
                if (pageIndicatorLayout.get().getChildCount() > 0) {
                    pageIndicatorLayout.get().removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    private void hideLoading() {

        if (loadingCount == 3 && layoutDone) {

            binding.get().loadingView.setVisibility(View.GONE);
            binding.get().loadHolder.setVisibility(View.GONE);
        }
    }

    private void startPagerAutoSwipe() {

        update = () -> {
            if (!touched) {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }

                if (viewPager.get() != null) {
                    viewPager.get().setCurrentItem(currentPage++, true);
                }

            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);
    }

    private void setUnTouchedTimer() {

        if (unTouchedTimer == null) {
            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;

                    handler.post(update);
                }
            }, 3000, 6000);
        } else {
            unTouchedTimer.cancel();
            unTouchedTimer.purge();

            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;

                    handler.post(update);
                }
            }, 3000, 6000);
        }
    }

    private void updateCityPref(City city) {
        pref.edit().putString(Constants.CITY_NAME, city.name).apply();
        pref.edit().putString(Constants.CITY_LAT, city.lat).apply();
        pref.edit().putString(Constants.CITY_LNG, city.lng).apply();
    }

    private void setCityTouchCount() {
        touchCountViewModel.setTouchCountPostDataObj(loginUserId, selectedCityId, Constants.CITY);

        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void replaceNewsFeedList(List<Blog> blogs) {
        this.dashBoardViewPagerAdapter.get().replaceNewsFeedList(blogs);
        binding.get().executePendingBindings();
    }

    @Override
    public void onResume() {
        loadLoginUserId();
        super.onResume();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
        Date date = new Date();
        return dateFormat.format(date);
    }
}
