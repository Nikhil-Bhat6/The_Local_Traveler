package com.panaceasoft.pscity.ui.stripe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityStripeBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;
public class StripeActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStripeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_stripe);

        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));

    }

    private void initUI(ActivityStripeBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.item_promote__stripe));

        // setup Fragment
        setupFragment(new StripeFragment());

    }
}

