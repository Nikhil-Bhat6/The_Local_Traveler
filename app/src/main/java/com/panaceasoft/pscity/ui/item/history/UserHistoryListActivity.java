package com.panaceasoft.pscity.ui.item.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityUserHistoryListBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.ui.item.promote.LoginUserPaidItemFragment;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;

public class UserHistoryListActivity extends PSAppCompactActivity {


    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserHistoryListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_history_list);

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

    private void initUI(ActivityUserHistoryListBinding binding) {

        String paidOrNot = getIntent().getExtras().getString(Constants.FLAGPAIDORNOT);

        if(paidOrNot.equals(Constants.FLAGPAID)){
            // Toolbar
            initToolbar(binding.toolbar, getString(R.string.profile__paid_ad));

            // setup Fragment
            setupFragment(new LoginUserPaidItemFragment());
        }else {
            // Toolbar
            initToolbar(binding.toolbar, getString(R.string.history__title));

            // setup Fragment
            setupFragment(new HistoryFragment());
            // Or you can call like this
            //setupFragment(new NewsListFragment(), R.id.content_frame);
        }
    }


}
