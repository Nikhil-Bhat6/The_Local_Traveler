package com.panaceasoft.pscity.ui.apploading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentAppLoadingBinding;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.apploading.AppLoadingViewModel;
import com.panaceasoft.pscity.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.pscity.viewobject.PSAppInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppLoadingFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private PSDialogMsg psDialogMsg;
    private String startDate = Constants.ZERO;
    private String endDate = Constants.ZERO;
    private AppLoadingViewModel appLoadingViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentAppLoadingBinding> binding;

    //endregion Variables

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAppLoadingBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_loading, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

//        if (force_update) {
//            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_title, force_update_msg);
//        }
    }

    @Override
    protected void initViewModels() {
        appLoadingViewModel =  new ViewModelProvider(this, viewModelFactory).get(AppLoadingViewModel.class);
        clearAllDataViewModel = new ViewModelProvider(this, viewModelFactory).get(ClearAllDataViewModel.class);
    }

    @Override
    protected void initAdapters() {
    }

    @Override
    protected void initData() {

        if (connectivity.isConnected()) {
            if (startDate.equals(Constants.ZERO)) {

                startDate = getDateTime();
                Utils.setDatesToShared(startDate, endDate, pref);
            }

            endDate = getDateTime();
            appLoadingViewModel.setDeleteHistoryObj(startDate, endDate);

        } else {
            navigationController.navigateToMainActivity(AppLoadingFragment.this.getActivity());

            if (getActivity() != null) {
                getActivity().finish();
            }
        }

        appLoadingViewModel.getDeleteHistoryData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case SUCCESS:

                        if (result.data != null) {
                            appLoadingViewModel.psAppInfo = result.data;
                            checkVersionNumber(result.data);
                            startDate = endDate;
                        }
                        break;

                    case ERROR:

                        break;
                }
            }

        });

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        checkForceUpdate(appLoadingViewModel.psAppInfo);
                        break;
                }
            }
        });

    }

    private void checkVersionNumber(PSAppInfo psAppInfo) {

        if (!Config.APP_VERSION.equals(psAppInfo.psAppVersion.versionNo)) {
        if (psAppInfo.psAppVersion.versionNeedClearData.equals(Constants.ONE)) {
            psDialogMsg.cancel();
            clearAllDataViewModel.setDeleteAllDataObj();
        } else {
            checkForceUpdate(appLoadingViewModel.psAppInfo);
        }
    } else {
        pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false).apply();
        navigationController.navigateToMainActivity(AppLoadingFragment.this.getActivity());
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

}


    private void checkForceUpdate(PSAppInfo psAppInfo) {
        if (!Config.APP_VERSION.equals(psAppInfo.psAppVersion.versionNo)) {
            if (psAppInfo.psAppVersion.versionForceUpdate.equals(Constants.ONE)) {

                pref.edit().putString(Constants.APPINFO_PREF_VERSION_NO, psAppInfo.psAppVersion.versionNo).apply();
                pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, true).apply();
                pref.edit().putString(Constants.APPINFO_FORCE_UPDATE_TITLE, psAppInfo.psAppVersion.versionTitle).apply();
                pref.edit().putString(Constants.APPINFO_FORCE_UPDATE_MSG, psAppInfo.psAppVersion.versionMessage).apply();

                navigationController.navigateToForceUpdateActivity(this.getActivity(), psAppInfo.psAppVersion.versionTitle, psAppInfo.psAppVersion.versionMessage);

            } else if (psAppInfo.psAppVersion.versionForceUpdate.equals(Constants.ZERO)) {

                pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false).apply();

                psDialogMsg.showAppInfoDialog(getString(R.string.update), getString(R.string.app__cancel), psAppInfo.psAppVersion.versionTitle, psAppInfo.psAppVersion.versionMessage);
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToMainActivity(AppLoadingFragment.this.getActivity());
                    navigationController.navigateToPlayStore(AppLoadingFragment.this.getActivity());
                    if (getActivity() != null) {
                        getActivity().finish();
                    }

                });

                psDialogMsg.cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        psDialogMsg.cancel();

                        navigationController.navigateToMainActivity(AppLoadingFragment.this.getActivity());
                        if (AppLoadingFragment.this.getActivity() != null) {
                            AppLoadingFragment.this.getActivity().finish();
                        }
                    }
                });

                psDialogMsg.getDialog().setCancelable(false);
            }

        }
    }



    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
        Date date = new Date();
        return dateFormat.format(date);
    }


}
