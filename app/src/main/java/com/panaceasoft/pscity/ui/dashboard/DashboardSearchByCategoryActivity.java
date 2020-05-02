package com.panaceasoft.pscity.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivitySearchByCategoryBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;

public class DashboardSearchByCategoryActivity extends PSAppCompactActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchByCategoryBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_category);

        initUI(databinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);

        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    protected void initUI(ActivitySearchByCategoryBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.CATEGORY_FLAG);

        if (fragName.equals(Constants.CATEGORY)) {
            setupFragment(new DashBoardSearchCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_cat_title));
        } else if (fragName.equals(Constants.SUBCATEGORY)) {
            setupFragment(new DashBoardSearchSubCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_sub_cat_title));
        }


    }
}
