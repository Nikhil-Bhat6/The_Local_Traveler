package com.panaceasoft.pscity.ui.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.di.Injectable;
import com.panaceasoft.pscity.utils.Connectivity;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;

import javax.inject.Inject;

/**
 * Parent class for all fragment in this project.
 * Created by Panacea-Soft on 12/2/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public abstract class PSFragment extends Fragment implements Injectable {

    //region Variables

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected NavigationController navigationController;

    @Inject
    protected Connectivity connectivity;

    @Inject
    protected SharedPreferences pref;

    protected String loginUserId;

    protected String selectedCityId;

    protected String selectedCityName;

    protected String selectedCityLat;

    protected String selectedCityLng;

    protected String versionNo;

    protected Boolean force_update = false;

    protected String force_update_title;

    protected String force_update_msg;

    private boolean isFadeIn = false;

    protected  String messenger, whatsappNo;

    protected String consent_status;

    protected String userEmailToVerify, userPasswordToVerify, userNameToVerify, userIdToVerify;
//    protected String cod, paypal, stripe;


    //endregion


    //region Override Methods
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadLoginUserId();

        initViewModels();

        initUIAndActions();

        initAdapters();

        initData();
    }

    //endregion


    //region Methods

    protected void loadLoginUserId(){
        try {

            if(getActivity() != null && getActivity().getBaseContext() != null) {
                loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
                selectedCityId = pref.getString(Constants.CITY_ID, "" );
                selectedCityName = pref.getString(Constants.CITY_NAME, "" );
                selectedCityLat = pref.getString(Constants.CITY_LAT, "" );
                selectedCityLng = pref.getString(Constants.CITY_LNG, "" );
                versionNo = pref.getString(Constants.APPINFO_PREF_VERSION_NO, "");
                force_update = pref.getBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false);
                force_update_msg = pref.getString(Constants.APPINFO_FORCE_UPDATE_MSG, "");
                force_update_title = pref.getString(Constants.APPINFO_FORCE_UPDATE_TITLE, "");
                consent_status = pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS);
                userEmailToVerify = pref.getString(Constants.USER_EMAIL_TO_VERIFY, Constants.EMPTY_STRING);
                userPasswordToVerify = pref.getString(Constants.USER_PASSWORD_TO_VERIFY, Constants.EMPTY_STRING);
                userNameToVerify = pref.getString(Constants.USER_NAME_TO_VERIFY, Constants.EMPTY_STRING);
                userIdToVerify = pref.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING);
            }

        }catch (NullPointerException ne){
            Utils.psErrorLog("Null Pointer Exception.", ne);
        }catch(Exception e){
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
    }

    protected abstract void initUIAndActions();

    protected abstract void initViewModels();

    protected abstract void initAdapters();

    protected abstract void initData();

    protected void fadeIn(View view) {

        if(!isFadeIn) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            isFadeIn = true; // Fade in will do only one time.
        }
    }
    //endregion

}
