package com.panaceasoft.pscity.ui.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentDashboardSearchBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;

public class DashBoardSearchFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String catId = Constants.NO_DATA;
    private String subCatId = Constants.NO_DATA;
    private PSDialogMsg psDialogMsg;
    private ItemViewModel itemViewModel;


    @VisibleForTesting
    private AutoClearedValue<FragmentDashboardSearchBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDashboardSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_search, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CATEGORY) {

            this.catId = data.getStringExtra(Constants.CATEGORY_ID);
            binding.get().categoryTextView.setText(data.getStringExtra(Constants.CATEGORY_NAME));
            itemViewModel.holder.cat_id = this.catId;
            this.subCatId = "";
            itemViewModel.holder.sub_cat_id = this.subCatId;
            binding.get().subCategoryTextView.setText("");

        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY) {

            this.subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID);
            binding.get().subCategoryTextView.setText(data.getStringExtra(Constants.SUBCATEGORY_NAME));
            itemViewModel.holder.sub_cat_id = this.subCatId;
        }
    }

    @Override
    public void onDispatched() {


    }

    @Override
    protected void initUIAndActions() {
        if(getActivity() instanceof MainActivity)  {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().setItemName.setHint(R.string.search__notSet);
        binding.get().categoryTextView.setHint(R.string.search__notSet);
        binding.get().subCategoryTextView.setHint(R.string.search__notSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        AutoClearedValue<AlertDialog.Builder> alertDialog = new AutoClearedValue<>(this, builder);
        alertDialog.get().setTitle(getResources().getString(R.string.Feature_UI__search_alert_cat_title));

//        binding.get().setItemName.setText(binding.get().setItemName.getText().toString());
//        binding.get().productTypeTextView.setText(binding.get().productTypeTextView.getText().toString());
//        binding.get().subProductTypeTextView.setText(binding.get().subProductTypeTextView.getText().toString());
//        binding.get().priceTextView.setText(binding.get().priceTextView.getText().toString());
//        binding.get().lowestPriceTextView.setText(binding.get().lowestPriceTextView.getText().toString());
//        binding.get().highestPriceTextView.setText(binding.get().highestPriceTextView.getText().toString());
//        binding.get().specialCheckTextView.setText(binding.get().specialCheckTextView.getText().toString());
//        binding.get().selectionTextView.setText(binding.get().selectionTextView.getText().toString());
//        binding.get().discountPriceTextView.setText(binding.get().discountPriceTextView.getText().toString());
//        binding.get().searchButton.setText(binding.get().searchButton.getText().toString());

        binding.get().categoryTextView.setText("");
        binding.get().subCategoryTextView.setText("");

        binding.get().categorySelectionView.setOnClickListener(view -> navigationController.navigateToSearchActivityCategoryFragment(this.getActivity(), Constants.CATEGORY, catId, subCatId));

        binding.get().subCategorySelectionView.setOnClickListener(view -> {

            if (catId.equals(Constants.NO_DATA) || catId.isEmpty()) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_category), getString(R.string.app__ok));

                psDialogMsg.show();

            } else {
                navigationController.navigateToSearchActivityCategoryFragment(this.getActivity(), Constants.SUBCATEGORY, catId, subCatId);
            }
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


        binding.get().filter.setOnClickListener(view -> {

            // Get Name
            itemViewModel.holder.keyword = binding.get().setItemName.getText().toString();
//            itemViewModel.holder.city_id = selectedCityId;

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
            navigationController.navigateToHomeFilteringActivity(this.getActivity(), itemViewModel.holder, null);

        });

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
}
