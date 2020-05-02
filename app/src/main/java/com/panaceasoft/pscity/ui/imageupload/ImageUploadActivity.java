package com.panaceasoft.pscity.ui.imageupload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.databinding.ActivityImageUploadBinding;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;
import com.panaceasoft.pscity.utils.Utils;

import java.util.Objects;

public class ImageUploadActivity extends PSAppCompactActivity {

    public int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityImageUploadBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_upload);

        flag = Objects.requireNonNull(getIntent().getIntExtra(Constants.FLAG, 0));
        initUI(dataBinding);
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

    public void initUI(ActivityImageUploadBinding binding) {

        if (flag == Constants.IMAGE_UPLOAD_ITEM) {
            initToolbar(binding.categoryToolBar, getResources().getString(R.string.image_upload__title_item));
            setupFragment(new ItemEntryImageUploadFragment());

        }
    }
}

