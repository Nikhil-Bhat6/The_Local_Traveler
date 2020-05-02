package com.panaceasoft.pscity.ui.item.rating;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityRatingListBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;

public class RatingListActivity extends PSAppCompactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRatingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_list);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //region Private Methods

    private void initUI(ActivityRatingListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.rating__list));

        // setup Fragment
        setupFragment(new RatingListFragment());

    }

    //endregion
}
