package com.panaceasoft.pscity.ui.item.upload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivitySelectionBinding;
import com.panaceasoft.pscity.ui.category.categoryselection.CategorySelectionFragment;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.ui.status.StatusSelectionFragment;
import com.panaceasoft.pscity.ui.subcategory.subcategoryselection.SubCategorySelectionFragment;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;
import com.panaceasoft.pscity.utils.Utils;

import java.util.Objects;

public class SelectionActivity extends PSAppCompactActivity {

    public int flagType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySelectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_selection);

        flagType = Objects.requireNonNull(getIntent().getIntExtra((Constants.FLAG),1));

        // Init all UI
        initUI(binding);
    }
    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);

        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Utils.psLog("Inside Result MainActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        assert fragment != null;
        fragment.onActivityResult(requestCode, resultCode, data);
    }


    //region Private Methods

    private void initUI(ActivitySelectionBinding binding) {

        // setup Fragment
        if(flagType == Constants.SELECT_CATEGORY) {
            CategorySelectionFragment categoryExpFragment = new CategorySelectionFragment();
            setupFragment(categoryExpFragment);
            initToolbar(binding.toolbar, "Category List");

        }
        else if(flagType == Constants.SELECT_SUBCATEGORY){
            SubCategorySelectionFragment subCategoryExpFragment = new SubCategorySelectionFragment();
            setupFragment(subCategoryExpFragment);
            initToolbar(binding.toolbar, "Sub Category List");

        }else if(flagType == Constants.SELECT_STATUS){
            StatusSelectionFragment categoryExpFragment = new StatusSelectionFragment();
            setupFragment(categoryExpFragment);
            initToolbar(binding.toolbar, "Status List");
        }
//        else if(flagType == Config.BACK_TO_EXPAND_DISCOUNT){
//            DiscountFragment discountFragment = new DiscountFragment();
//            setupFragment(discountFragment);
//            initToolbar(binding.toolbar, "Discount List");
//
//        }else if(flagType == Config.BACK_TO_EXPAND_ITEM) {
//            ItemSelectionFragment productFragment = new ItemSelectionFragment();
//            setupFragment(productFragment);
//            initToolbar(binding.toolbar, "Item List");
//
//        }else if(flagType == Config.BACK_TO_EXPAND_SHOP_TYPE) {
//            setupFragment(new ShopTypeSelectionFragment());
//            initToolbar(binding.toolbar, "Shop Type List");
//        }

        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }


}
