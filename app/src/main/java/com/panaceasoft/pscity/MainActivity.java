package com.panaceasoft.pscity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.login.LoginManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.panaceasoft.pscity.databinding.ActivityMainBinding;
import com.panaceasoft.pscity.ui.city.selectedcity.SelectedCityFragment;
import com.panaceasoft.pscity.ui.common.NavigationController;
import com.panaceasoft.pscity.ui.common.PSAppCompactActivity;
import com.panaceasoft.pscity.utils.AppLanguage;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.MyContextWrapper;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.NotificationViewModel;
import com.panaceasoft.pscity.viewmodel.user.UserViewModel;
import com.panaceasoft.pscity.viewobject.User;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

/**
 * MainActivity of Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 *
 * @author Panacea-soft
 * @version 1.0
 * @since 11/15/17.
 */

public class MainActivity extends PSAppCompactActivity {


    //region Variables

    @Inject
    SharedPreferences pref;

    @Inject
    AppLanguage appLanguage;
    private Boolean notiSetting = false;
    private String token = "";
    private UserViewModel userViewModel;
    private NotificationViewModel notificationViewModel;
    private User user;
    private PSDialogMsg psDialogMsg;
    private boolean isLogout = false;
    Drawable yourdrawable = null;
    ActionBarDrawerToggle drawerToggle;
    private String token1;
    private ConsentForm form;
    private GoogleSignInClient mGoogleSignInClient;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    public ActivityMainBinding binding;

    //endregion


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_PSTheme);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUIAndActions();

        initModels();

        initData();

        checkConsentStatus();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            if (pref.getBoolean(Constants.NOTI_EXISTS_TO_SHOW, false) ||
                    getIntent().getBooleanExtra(Constants.NOTI_EXISTS_TO_SHOW, false)) {

                String message = pref.getString(Constants.NOTI_MSG, "");

                if (!message.equals("")) {

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.NOTI_EXISTS_TO_SHOW, false).apply();

                    showAlertMessage(message);

                }
            }
        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            if(fragment != null)
            {
                if(fragment instanceof SelectedCityFragment)
                {
                    String message = getBaseContext().getString(R.string.message__want_to_quit);
                    String okStr =getBaseContext().getString(R.string.message__ok_close);
                    String cancelStr = getBaseContext().getString(R.string.message__cancel_close);

                    psDialogMsg.showConfirmDialog(message, okStr,cancelStr );

                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(view -> {
                        psDialogMsg.cancel();
                        MainActivity.this.finish();
                        System.exit(0);
                    });
                    psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());
                }else {
                    setSelectMenu(R.id.nav_home);
                    showBottomNavigation();
                    binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));
                    navigationController.navigateToHome(MainActivity.this);

                }

            }
        }
        return  true;
    }


    //endregion


    //region Private Methods

    /**
     * Initialize Models
     */
    private void initModels() {

        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        notificationViewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationViewModel.class);
    }


    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private void showAlertMessage(String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.ps_dialog, null);

        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null);

        TextView message = view.findViewById(R.id.messageTextView);

        message.setText(msg);

        builder.create();

        builder.show();

    }

    /**
     * This function will initialize UI and Event Listeners
     */
    private void initUIAndActions() {

        Menu navViewMenu = binding.navView.getMenu();

        if(!Config.ENABLE_ITEM_UPLOAD){
            navViewMenu.findItem(R.id.nav_upload_item_login).setVisible(false);
        }else{
            navViewMenu.findItem(R.id.nav_upload_item_login).setVisible(true);
        }

        psDialogMsg = new PSDialogMsg(this, false);

        initToolbar(binding.toolbar, getResources().getString(R.string.app__app_name));

        initDrawerLayout();

        initNavigationView();

        navigationController.navigateToCityList(this);
        showBottomNavigation();

        setSelectMenu(R.id.nav_home);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_menu:
                    navigationController.navigateToHome(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
                case R.id.explorer_menu:
                    navigationController.navigateToExplore(MainActivity.this, new ItemParameterHolder().getRecentItem());
                    setToolbarText(binding.toolbar, getString(R.string.menu__exployer));

                    break;

                case R.id.interest_menu:

                    navigationController.navigateToInterest(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__interest));

                    break;

                case R.id.search_menu:

                    navigationController.navigateToFilter(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;

                case R.id.me_menu:

                    Utils.navigateOnUserVerificationFragment(pref, user,navigationController,this);

                    break;

                default:

                    break;
            }

            return true;
        });

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


    }


    private void initDrawerLayout() {

        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app__drawer_open, R.string.app__drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_grey_24);

        drawerToggle.setToolbarNavigationClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.drawerLayout.addDrawerListener(drawerToggle);
        binding.drawerLayout.post(drawerToggle::syncState);

    }

    private void initNavigationView() {

        if (binding.navView != null) {

            // Updating Custom Fonts
            Menu m = binding.navView.getMenu();
            try {
                if (m != null) {

                    for (int i = 0; i < m.size(); i++) {
                        MenuItem mi = m.getItem(i);

                        //for applying a font to subMenu ...
                        SubMenu subMenu = mi.getSubMenu();
                        if (subMenu != null && subMenu.size() > 0) {
                            for (int j = 0; j < subMenu.size(); j++) {
                                MenuItem subMenuItem = subMenu.getItem(j);

                                subMenuItem.setTitle(subMenuItem.getTitle());
                                // update font

                                subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));

                            }
                        }

                        mi.setTitle(mi.getTitle());
                        // update font

                        mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
                    }
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in Setting Custom Font", e);
            }

            binding.navView.setNavigationItemSelectedListener(menuItem -> {
                navigationMenuChanged(menuItem);
                return true;
            });

        }

        if (binding.bottomNavigationView != null) {

            // Updating Custom Fonts
            Menu m = binding.bottomNavigationView.getMenu();
            try {

                for (int i = 0; i < m.size(); i++) {
                    MenuItem mi = m.getItem(i);

                    //for applying a font to subMenu ...
                    SubMenu subMenu = mi.getSubMenu();
                    if (subMenu != null && subMenu.size() > 0) {
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);

                            subMenuItem.setTitle(subMenuItem.getTitle());
                            // update font

                            subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));

                        }
                    }

                    mi.setTitle(mi.getTitle());
                    // update font

                    mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
                }
            } catch (Exception e) {
                Utils.psErrorLog("Error in Setting Custom Font", e);
            }

            binding.navView.setNavigationItemSelectedListener(menuItem -> {
                navigationMenuChanged(menuItem);
                return true;
            });

        }

    }


    private void hideBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    private void showBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void navigationMenuChanged(MenuItem menuItem) {
        openFragment(menuItem.getItemId());

        if (menuItem.getItemId() != R.id.nav_logout_login) {
            menuItem.setChecked(true);
            binding.drawerLayout.closeDrawers();
        }
    }

    public void setSelectMenu(int id) {
        binding.navView.setCheckedItem(id);
    }

    private int menuId = 0;

    /**
     * Open Fragment
     *
     * @param menuId To know which fragment to open.
     */
    private void openFragment(int menuId) {

        this.menuId = menuId;
        switch (menuId) {
            case R.id.nav_home:
            case R.id.nav_home_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__home));
                navigationController.navigateToHome(this);
                showBottomNavigation();
                break;

            case R.id.nav_category:
            case R.id.nav_category_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__category));
                navigationController.navigateToCategory(this);
                hideBottomNavigation();
                break;

            case R.id.nav_latest:
            case R.id.nav_latest_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__latest_item));
                navigationController.navigateToHomeLatestFiltering(MainActivity.this, new ItemParameterHolder().getRecentItem());
                hideBottomNavigation();
                break;

            case R.id.nav_promotion:
            case R.id.nav_promotion_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__discount));
                navigationController.navigateToDiscountProduct(MainActivity.this, new ItemParameterHolder().getDiscountItem());
                hideBottomNavigation();
                break;

            case R.id.nav_best_thing:
            case R.id.nav_best_thing_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__featured_item));
                navigationController.navigateToFeatureProduct(MainActivity.this, new ItemParameterHolder().getFeaturedItem());
                hideBottomNavigation();
                break;

            case R.id.nav_popular:
            case R.id.nav_popular_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__trending_item));
                navigationController.navigateToHomePopularFiltering(MainActivity.this, new ItemParameterHolder().getPopularItem());
                hideBottomNavigation();
                break;

            case R.id.nav_collection:
            case R.id.nav_collection_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__collections));
                navigationController.navigateToCollection(this);
                hideBottomNavigation();
                break;

            case R.id.nav_profile:
            case R.id.nav_profile_login:

                Utils.navigateOnUserVerificationFragment(pref,user,navigationController,this);

                Utils.psLog("nav_profile");

                hideBottomNavigation();

                break;

            case R.id.nav_upload_item_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__uploaded_items));
                navigationController.navigateToItemUpdated(this);
                Utils.psLog("nav_favourite_news");

                hideBottomNavigation();
                break;

            case R.id.nav_transaction_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__paid_ad_transaction));
                navigationController.navigateToTransactions(this);
                Utils.psLog("nav_transactions_news");

                hideBottomNavigation();
                break;

            case R.id.nav_favourite_news_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__favourite_items));
                navigationController.navigateToFavourite(this);
                Utils.psLog("nav_item_updated_news");

                hideBottomNavigation();
                break;

            case R.id.nav_user_history_login:
                setToolbarText(binding.toolbar, getString(R.string.menu__user_history));
                navigationController.navigateToHistory(this);
                Utils.psLog("nav_history");

                hideBottomNavigation();
                break;

            case R.id.nav_logout_login:

                psDialogMsg.showConfirmDialog(getString(R.string.edit_setting__logout_question), getString(R.string.app__ok), getString(R.string.app__cancel));

                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(view -> {

                    psDialogMsg.cancel();

                    hideBottomNavigation();

                    userViewModel.deleteUserLogin(user).observe(this, status -> {
                        if (status != null) {
                            this.menuId = 0;

                            setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                            isLogout = true;

//                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,gso);
                            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    Utils.psLog("nav_logout_login");
                });

                psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

                break;

            case R.id.nav_setting:
            case R.id.nav_setting_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__setting));
                navigationController.navigateToSetting(this);
                Utils.psLog("nav_setting");

                hideBottomNavigation();
                break;

            case R.id.nav_language:
            case R.id.nav_language_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__language));
                navigationController.navigateToLanguageSetting(this);
                Utils.psLog("nav_language");
                hideBottomNavigation();

                break;
            case R.id.nav_rate_this_app:
            case R.id.nav_rate_this_app_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__rate));
                navigationController.navigateToPlayStore(this);
                hideBottomNavigation();

                break;

            case R.id.nav_privacy_policy:
            case R.id.nav_privacy_policy_login:

                setToolbarText(binding.toolbar, getString(R.string.menu__privacy_policy));
                navigationController.navigateToPrivacyPolicy(this);
                hideBottomNavigation();

                break;
        }

    }


    /**
     * Initialize Data
     */
    private void initData() {

        try {

            notiSetting = pref.getBoolean(Constants.NOTI_SETTING, false);
            token = pref.getString(Constants.NOTI_TOKEN, "");

        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    user = data.get(0).user;

                    pref.edit().putString(Constants.USER_ID, user.userId).apply();
                    pref.edit().putString(Constants.USER_NAME, user.userName).apply();
                    pref.edit().putString(Constants.USER_EMAIL, user.userEmail).apply();

                } else {
                    user = null;

                    pref.edit().remove(Constants.USER_ID).apply();
                    pref.edit().remove(Constants.USER_NAME).apply();
                    pref.edit().remove(Constants.USER_EMAIL).apply();
                }

            } else {

                user = null;
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();

            }
            updateMenu();

            if (isLogout) {
                navigationController.navigateToHome(MainActivity.this);
                showBottomNavigation();
                isLogout = false;
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.revokeAccess()
                        .addOnCompleteListener(this, task -> {
                            // ...
                        });

                LoginManager.getInstance().logOut();
            }

        });


        registerNotificationToken(); // Just send "" because don't have token to sent. It will get token itself.
    }

    /**
     * This function will change the menu based on the user is logged in or not.
     */
    private void updateMenu() {

        if (user == null) {

            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, false);

            setSelectMenu(R.id.nav_home);

        } else {
            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, true);
            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, false);

            if (menuId == R.id.nav_profile) {
                setSelectMenu(R.id.nav_profile_login);
            } else if (menuId == R.id.nav_profile_login) {
                setSelectMenu(R.id.nav_profile_login);
            } else {
                setSelectMenu(R.id.nav_home_login);
            }

        }


    }

    private void registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notiSetting) {

            if (this.token.equals("")) {

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {

                                return;
                            }

                            // Get new Instance ID token
                            if (task.getResult() != null) {
                                token1 = task.getResult().getToken();
                            }

                            notificationViewModel.registerNotification(getBaseContext(), Constants.PLATFORM, token1);
                        });


            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.");
        }
    }

    //endregion

    public void updateMenuIconWhite() {
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_white_24);
    }

    public void updateMenuIconGrey() {
        drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_grey_24);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        yourdrawable = menu.getItem(0).getIcon();
        yourdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_notification) {

            navigationController.navigateToNotificationList(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__MAIN_ACTIVITY
                && resultCode == Constants.RESULT_CODE__RESTART_MAIN_ACTIVITY) {

            finish();
            startActivity(new Intent(this, MainActivity.class));

        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    private void checkConsentStatus()
    {

        // For Testing Open this
//        ConsentInformation.getInstance(this).
//                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] publisherIds = {getString(R.string.adview_publisher_key)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.

                Utils.psLog(consentStatus.name());

                if(!consentStatus.name().equals(pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS)) || consentStatus.name().equals(Config.CONSENTSTATUS_UNKNOWN))
                {
                    collectConsent();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.

                Utils.psLog("Failed to updateeee");
            }
        });
    }

    private void collectConsent(){
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if(form != null)
                        {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }
}
