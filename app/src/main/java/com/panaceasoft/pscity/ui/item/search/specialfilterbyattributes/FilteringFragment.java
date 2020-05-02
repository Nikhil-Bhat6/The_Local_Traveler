package com.panaceasoft.pscity.ui.item.search.specialfilterbyattributes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentFilterBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

public class FilteringFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ItemViewModel itemViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentFilterBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentFilterBinding fragmentFilterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, fragmentFilterBinding);
        setHasOptionsMenu(true);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
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

        binding.get().setItemName.setHint(R.string.sf__notSet);

        if (getActivity() != null) {

            // Get Data from Intent

            Intent intent = getActivity().getIntent();
            itemViewModel.holder = (ItemParameterHolder) intent.getSerializableExtra(Constants.FILTERING_HOLDER);


            // Name Binding

            if (itemViewModel.holder != null) {
                binding.get().setItemName.setText(itemViewModel.holder.keyword);

                // Feature Switch Binding
                if (itemViewModel.holder.is_featured != null) {
                    if (itemViewModel.holder.is_featured.equals(Constants.ONE)) {
                        binding.get().featuredSwitch.setChecked(true);
                    } else {
                        binding.get().featuredSwitch.setChecked(false);
                    }
                }

                // Discount Switch Binding
                if (itemViewModel.holder.is_promotion != null) {
                    if (itemViewModel.holder.is_promotion.equals(Constants.ONE)) {
                        binding.get().discountSwitch.setChecked(true);
                    } else {
                        binding.get().discountSwitch.setChecked(false);
                    }

                }

                // Sorting Buttons Binding
                if (itemViewModel.holder.order_by != null) {

                    switch (itemViewModel.holder.order_by) {
                        case Constants.FILTERING_ADDED_DATE:
                            setSortingSelection(0);
                            break;

                        case Constants.FILTERING_FEATURE:
                            setSortingSelection(0);
                            break;

                        case Constants.FILTERING_TRENDING:
                            setSortingSelection(1);
                            break;

                        case Constants.FILTERING_NAME:

                            if (itemViewModel.holder.order_type != null) {
                                if (itemViewModel.holder.order_type.equals(Constants.FILTERING_ASC)) {
                                    setSortingSelection(2);
                                } else {
                                    setSortingSelection(3);
                                }
                            }

                            break;

                    }
                }

                //Rating Binding

                switch (itemViewModel.holder.rating_value) {
                    case Constants.RATING_ONE:

                        selectStar(binding.get().oneStar);

                        break;
                    case Constants.RATING_TWO:

                        selectStar(binding.get().twoStar);

                        break;
                    case Constants.RATING_THREE:

                        selectStar(binding.get().threeStar);

                        break;
                    case Constants.RATING_FOUR:

                        selectStar(binding.get().fourStar);

                        break;
                    case Constants.RATING_FIVE:

                        selectStar(binding.get().fiveStar);

                        break;
                }
            }
        }

        binding.get().filter.setOnClickListener(view -> {

            // Get Name
            itemViewModel.holder.keyword = binding.get().setItemName.getText().toString();

            // Get Feature Switch Data
            if (binding.get().featuredSwitch.isChecked()) {
                itemViewModel.holder.is_featured = Constants.ONE;
            } else {
                itemViewModel.holder.is_featured = "";
            }

            // Get Discount Switch Data
            if (binding.get().discountSwitch.isChecked()) {
                itemViewModel.holder.is_promotion = Constants.ONE;
            } else {
                itemViewModel.holder.is_promotion = "";
            }

            // For Sorting
            if (itemViewModel.holder.is_featured.equals(Constants.ONE) && itemViewModel.holder.order_by.equals(Constants.FILTERING_ADDED_DATE)) {
                itemViewModel.holder.order_by = Constants.FILTERING_FEATURE;
            }

            // Set to Intent

            navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(FilteringFragment.this.getActivity(), itemViewModel.holder);
            FilteringFragment.this.getActivity().finish();

        });

        binding.get().oneStar.setOnClickListener(v -> {

            if (itemViewModel.holder.rating_value.equals("") || !itemViewModel.holder.rating_value.equals(Constants.RATING_ONE)) {
                selectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_ONE);
                itemViewModel.holder.rating_value = "";
            }

        });

        binding.get().twoStar.setOnClickListener(v -> {
            if (itemViewModel.holder.rating_value.equals("") || !itemViewModel.holder.rating_value.equals(Constants.RATING_TWO)) {
                selectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_TWO);
                itemViewModel.holder.rating_value = "";
            }

        });

        binding.get().threeStar.setOnClickListener(v -> {
            if (itemViewModel.holder.rating_value.equals("") || !itemViewModel.holder.rating_value.equals(Constants.RATING_THREE)) {
                selectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_THREE);
                itemViewModel.holder.rating_value = "";
            }
        });

        binding.get().fourStar.setOnClickListener(v -> {
            if (itemViewModel.holder.rating_value.equals("") || !itemViewModel.holder.rating_value.equals(Constants.RATING_FOUR)) {
                selectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_FOUR);
                itemViewModel.holder.rating_value = "";
            }
        });

        binding.get().fiveStar.setOnClickListener(v -> {
            if (itemViewModel.holder.rating_value.equals("") || !itemViewModel.holder.rating_value.equals(Constants.RATING_FIVE)) {
                selectStar(Constants.RATING_FIVE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_ONE);
            } else {
                unSelectStar(Constants.RATING_FIVE);
                itemViewModel.holder.rating_value = "";
            }
        });

        binding.get().recentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSortingSelection(0);
            }
        });

        binding.get().popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSortingSelection(1);
            }
        });

        binding.get().lowestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSortingSelection(3);
            }
        });

        binding.get().highestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSortingSelection(2);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ok_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clearButton) {

            binding.get().setItemName.setText("");
            binding.get().featuredSwitch.setChecked(false);
            binding.get().discountSwitch.setChecked(false);
            setSortingSelection(0);

            unSelectStar(Constants.RATING_ONE);
            unSelectStar(Constants.RATING_TWO);
            unSelectStar(Constants.RATING_THREE);
            unSelectStar(Constants.RATING_FOUR);
            unSelectStar(Constants.RATING_FIVE);

            itemViewModel.holder.keyword = "";
            itemViewModel.holder.order_by = Constants.FILTERING_ADDED_DATE;
            itemViewModel.holder.order_type = Constants.FILTERING_DESC;
            itemViewModel.holder.rating_value = "";
            itemViewModel.holder.is_featured = "";
            itemViewModel.holder.is_promotion = "";


            //navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(SpecialFilteringFragment.this.getActivity(), itemViewModel.holder);
        }
        return super.onOptionsItemSelected(item);
    }

    private void unSelectStar(Button star) {
        star.setTextColor(getResources().getColor(R.color.text__primary));
        star.setBackgroundResource(R.drawable.button_border);
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full_gray, 0, 0);
    }

    private void selectStar(Button star) {
        star.setTextColor(getResources().getColor(R.color.text__white));
        star.setBackgroundResource(R.drawable.button_border_pressed);
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_white, 0, 0);
    }

    private void unSelectStar(String stars) {

        switch (stars) {
            case Constants.RATING_ONE:

                unSelectStar(binding.get().oneStar);

                break;
            case Constants.RATING_TWO:

                unSelectStar(binding.get().twoStar);

                break;
            case Constants.RATING_THREE:

                unSelectStar(binding.get().threeStar);

                break;
            case Constants.RATING_FOUR:

                unSelectStar(binding.get().fourStar);

                break;
            case Constants.RATING_FIVE:

                unSelectStar(binding.get().fiveStar);

                break;
        }

    }

    private void selectStar(String stars) {
        switch (stars) {
            case Constants.RATING_ONE:

                selectStar(binding.get().oneStar);
                itemViewModel.holder.rating_value = Constants.RATING_ONE;


                break;
            case Constants.RATING_TWO:


                selectStar(binding.get().twoStar);
                itemViewModel.holder.rating_value = Constants.RATING_TWO;


                break;

            case Constants.RATING_THREE:


                selectStar(binding.get().threeStar);
                itemViewModel.holder.rating_value = Constants.RATING_THREE;


                break;
            case Constants.RATING_FOUR:


                selectStar(binding.get().fourStar);
                itemViewModel.holder.rating_value = Constants.RATING_FOUR;


                break;

            case Constants.RATING_FIVE:


                selectStar(binding.get().fiveStar);
                itemViewModel.holder.rating_value = Constants.RATING_FIVE;


                break;

        }
    }


    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void onDispatched() {

    }

    private void setSortingSelection(int index) {

        if (index == 0) {
            binding.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baesline_access_time_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);

            itemViewModel.holder.order_by = Constants.FILTERING_ADDED_DATE;
            itemViewModel.holder.order_type = Constants.FILTERING_DESC;

        } else {
            binding.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baesline_access_time_black_24), null, null, null);
        }

        if (index == 1) {
            binding.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_graph_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);

            itemViewModel.holder.order_by = Constants.FILTERING_TRENDING;
            itemViewModel.holder.order_type = Constants.FILTERING_DESC;

        } else {
            binding.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_graph_black_24), null, null, null);
        }

        if (index == 2) {
            binding.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_up_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);

            itemViewModel.holder.order_by = Constants.FILTERING_NAME;
            itemViewModel.holder.order_type = Constants.FILTERING_ASC;

        } else {
            binding.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_up_black_24), null, null, null);
        }

        if (index == 3) {
            binding.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_down_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);

            itemViewModel.holder.order_by = Constants.FILTERING_NAME;
            itemViewModel.holder.order_type = Constants.FILTERING_DESC;

        } else {
            binding.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_down_black_24), null, null, null);
        }
    }
}
