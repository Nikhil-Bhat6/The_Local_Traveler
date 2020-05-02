package com.panaceasoft.pscity.ui.item.search.specialfilterbyattributes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityFilteringBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.ui.category.categoryfilter.CategoryFilterFragment;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;

public class FilteringActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFilteringBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_filtering);

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

    //endregion


    //region Private Methods

    private void initUI(ActivityFilteringBinding binding) {

        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.FILTERING_FILTER_NAME);

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {

            // Toolbar
            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI_Type__Button));

            // setup Fragment
            setupFragment(new CategoryFilterFragment());
            // Or you can call like this
            //setupFragment(new NewsListFragment(), R.id.content_frame);
        } else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI_Tune__Button));

            setupFragment(new FilteringFragment());
        }

    }

    //endregion
}
