package com.panaceasoft.pscity.ui.city.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityCityBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;

public class CityActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCityBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_city);

        initUI(activityFilteringBinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);

        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    private void initUI(ActivityCityBinding binding) {

        initToolbar(binding.toolbar, getIntent().getStringExtra(Constants.CITY_NAME));

        setupFragment(new CityFragment());
    }



}
