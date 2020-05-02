package com.panaceasoft.pscity.ui.common;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.ui.blog.detail.BlogDetailActivity;
import com.panaceasoft.pscity.ui.blog.listbycityid.BlogListByCityIdActivity;
import com.panaceasoft.pscity.ui.category.list.CategoryListActivity;
import com.panaceasoft.pscity.ui.category.list.CategoryListFragment;
import com.panaceasoft.pscity.ui.city.detail.CityActivity;
import com.panaceasoft.pscity.ui.city.selectedcity.SelectedCityFragment;
import com.panaceasoft.pscity.ui.comment.detail.CommentDetailActivity;
import com.panaceasoft.pscity.ui.comment.list.CommentListActivity;
import com.panaceasoft.pscity.ui.dashboard.DashBoardSearchFragment;
import com.panaceasoft.pscity.ui.dashboard.DashboardSearchByCategoryActivity;
import com.panaceasoft.pscity.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.pscity.ui.gallery.GalleryActivity;
import com.panaceasoft.pscity.ui.gallery.detail.GalleryDetailActivity;
import com.panaceasoft.pscity.ui.imageupload.ImageUploadActivity;
import com.panaceasoft.pscity.ui.imageupload.itemimagelist.ItemImageListActivity;
import com.panaceasoft.pscity.ui.item.collection.header.CollectionHeaderListFragment;
import com.panaceasoft.pscity.ui.item.collection.itemCollection.ItemCollectionActivity;
import com.panaceasoft.pscity.ui.item.detail.ItemActivity;
import com.panaceasoft.pscity.ui.item.favourite.FavouriteListActivity;
import com.panaceasoft.pscity.ui.item.favourite.FavouriteListFragment;
import com.panaceasoft.pscity.ui.item.history.HistoryFragment;
import com.panaceasoft.pscity.ui.item.history.UserHistoryListActivity;
import com.panaceasoft.pscity.ui.item.map.MapActivity;
import com.panaceasoft.pscity.ui.item.map.mapFilter.MapFilteringActivity;
import com.panaceasoft.pscity.ui.item.promote.ItemPromoteActivity;
import com.panaceasoft.pscity.ui.item.promote.LoginUserPaidItemFragment;
import com.panaceasoft.pscity.ui.item.rating.RatingListActivity;
import com.panaceasoft.pscity.ui.item.readmore.ReadMoreActivity;
import com.panaceasoft.pscity.ui.item.search.searchlist.SearchListActivity;
import com.panaceasoft.pscity.ui.item.search.searchlist.SearchListFragment;
import com.panaceasoft.pscity.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.panaceasoft.pscity.ui.item.upload.ItemUploadActivity;
import com.panaceasoft.pscity.ui.item.upload.SelectionActivity;
import com.panaceasoft.pscity.ui.item.upload.map.MapItemEntryActivity;
import com.panaceasoft.pscity.ui.item.uploaded.ItemUploadedListFragment;
import com.panaceasoft.pscity.ui.language.LanguageFragment;
import com.panaceasoft.pscity.ui.notification.detail.NotificationActivity;
import com.panaceasoft.pscity.ui.notification.list.NotificationListActivity;
import com.panaceasoft.pscity.ui.notification.setting.NotificationSettingActivity;
import com.panaceasoft.pscity.ui.privacyandpolicy.PrivacyPolicyActivity;
import com.panaceasoft.pscity.ui.privacyandpolicy.PrivacyPolicyFragment;
import com.panaceasoft.pscity.ui.setting.SettingActivity;
import com.panaceasoft.pscity.ui.setting.SettingFragment;
import com.panaceasoft.pscity.ui.setting.appinfo.AppInfoActivity;
import com.panaceasoft.pscity.ui.specification.SpecificationListActivity;
import com.panaceasoft.pscity.ui.specification.addspecification.AddAndEditSpecificationActivity;
import com.panaceasoft.pscity.ui.stripe.StripeActivity;
import com.panaceasoft.pscity.ui.subcategory.SubCategoryActivity;
import com.panaceasoft.pscity.ui.user.PasswordChangeActivity;
import com.panaceasoft.pscity.ui.user.ProfileEditActivity;
import com.panaceasoft.pscity.ui.user.ProfileFragment;
import com.panaceasoft.pscity.ui.user.UserForgotPasswordActivity;
import com.panaceasoft.pscity.ui.user.UserForgotPasswordFragment;
import com.panaceasoft.pscity.ui.user.UserLoginActivity;
import com.panaceasoft.pscity.ui.user.UserLoginFragment;
import com.panaceasoft.pscity.ui.user.UserRegisterActivity;
import com.panaceasoft.pscity.ui.user.UserRegisterFragment;
import com.panaceasoft.pscity.ui.user.phonelogin.PhoneLoginActivity;
import com.panaceasoft.pscity.ui.user.phonelogin.PhoneLoginFragment;
import com.panaceasoft.pscity.ui.user.verifyemail.VerifyEmailActivity;
import com.panaceasoft.pscity.ui.user.verifyemail.VerifyEmailFragment;
import com.panaceasoft.pscity.ui.user.verifyphone.VerifyMobileActivity;
import com.panaceasoft.pscity.ui.user.verifyphone.VerifyMobileFragment;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewobject.Comment;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemHistory;
import com.panaceasoft.pscity.viewobject.Noti;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 11/17/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class NavigationController {

    //region Variables

    private final int containerId;
    private RegFragments currentFragment;

    //endregion


    //region Constructor
    @Inject
    public NavigationController() {

        // This setup is for MainActivity
        this.containerId = R.id.content_frame;
    }

    //endregion


    //region default navigation

    public void navigateToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserLogin(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                UserLoginFragment fragment = new UserLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserProfile(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                ProfileFragment fragment = new ProfileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToVerifyEmail(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_EMAIL_VERIFY)) {
            try {
                VerifyEmailFragment fragment = new VerifyEmailFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToFavourite(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FAVOURITE)) {
            try {
                FavouriteListFragment fragment = new FavouriteListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToItemUpdated(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_ITEM_UPDATED)) {
            try {
                ItemUploadedListFragment fragment = new ItemUploadedListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHistory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_HISTORY)) {
            try {
                HistoryFragment fragment = new HistoryFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToUserRegister(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_REGISTER)) {
            try {
                UserRegisterFragment fragment = new UserRegisterFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserForgotPassword(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_FOGOT_PASSWORD)) {
            try {
                UserForgotPasswordFragment fragment = new UserForgotPasswordFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToSetting(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_SETTING)) {
            try {
                SettingFragment fragment = new SettingFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToLanguageSetting(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_LANGUAGE_SETTING)) {
            try {
                LanguageFragment fragment = new LanguageFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHome(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_HOME)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToExplore(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_SEARCH)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
                Bundle args = new Bundle();

                args.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(args);
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToInterest(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToFilter(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FILTER)) {
            try {
                DashBoardSearchFragment fragment = new DashBoardSearchFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToCityList(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CITY_LIST)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToSearch(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_SEARCH)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

                Bundle args = new Bundle();
                args.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(args);

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToGalleryActivity(Activity activity, String imgType, String imgParentId) {
        Intent intent = new Intent(activity, GalleryActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!imgParentId.equals("")) {
            intent.putExtra(Constants.IMAGE_PARENT_ID, imgParentId);
        }

        activity.startActivity(intent);

    }

    public void navigateToDetailGalleryActivity(Activity activity, String imgType, String newsId, String imgId) {
        Intent intent = new Intent(activity, GalleryDetailActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!newsId.equals("")) {
            intent.putExtra(Constants.ITEM_ID, newsId);
        }

        if (!imgId.equals("")) {
            intent.putExtra(Constants.IMAGE_ID, imgId);
        }

        activity.startActivity(intent);

    }

    public void navigateToCommentListActivity(Activity activity, Item item) {
        Intent intent = new Intent(activity, CommentListActivity.class);
        intent.putExtra(Constants.ITEM_ID, item.id);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_FRAGMENT);
    }

    public void navigateToSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__PROFILE_FRAGMENT);
    }

    public void navigateToNotificationSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, NotificationSettingActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToEditProfileActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        activity.startActivity(intent);
    }


    public void navigateToAppInfoActivity(Activity activity) {
        Intent intent = new Intent(activity, AppInfoActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCityActivity(Activity activity, String cityName) {
        Intent intent = new Intent(activity, CityActivity.class);
        intent.putExtra(Constants.CITY_NAME, cityName);
        activity.startActivity(intent);
    }

    public void navigateToProfileEditActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToReadMoreActivity(Activity activity, String title, String description,Item item) {
        Intent intent = new Intent(activity, ReadMoreActivity.class);
        intent.putExtra(Constants.ITEM_TITLE, title);
        intent.putExtra(Constants.ITEM_DESCRIPTION, description);
        intent.putExtra(Constants.ITEM_IMAGE_URL,item.defaultPhoto.imgPath);
        activity.startActivity(intent);
    }

    public void navigateToUserLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, UserLoginActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToVerifyEmailActivity(Activity activity) {
        Intent intent = new Intent(activity, VerifyEmailActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, UserRegisterActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserForgotPasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, UserForgotPasswordActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPasswordChangeActivity(Activity activity) {
        Intent intent = new Intent(activity, PasswordChangeActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToNotificationList(Activity activity) {
        Intent intent = new Intent(activity, NotificationListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPrivacyPolicyActivity(Activity activity, String description) {
        Intent intent = new Intent(activity, PrivacyPolicyActivity.class);
        intent.putExtra(Constants.PRIVACY_POLICY_NAME, description);
        activity.startActivity(intent);
    }

    public void navigateToRatingList(Activity activity, String itemId) {
        Intent intent = new Intent(activity, RatingListActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        activity.startActivity(intent);
    }


    public void navigateToNotificationDetail(Activity activity, Noti noti, String token) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        intent.putExtra(Constants.NOTI_ID, noti.id);
        intent.putExtra(Constants.NOTI_TOKEN, token);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT);
    }

    public void navigateToCommentDetailActivity(Activity activity, Comment comment) {
        Intent intent = new Intent(activity, CommentDetailActivity.class);
        intent.putExtra(Constants.COMMENT_ID, comment.id);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE__COMMENT_LIST_FRAGMENT);

    }

    public void navigateToDetailActivity(Activity activity, Item item) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(Constants.ITEM_ID, item.id);
        intent.putExtra(Constants.ITEM_NAME, item.name);
        intent.putExtra(Constants.CITY_ID, item.cityId);
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);

        activity.startActivity(intent);
    }

    public void navigateToItemDetailActivity(Activity activity, ItemHistory
            historyProduct, String selectedCityId) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(Constants.ITEM_ID, historyProduct.id);
        intent.putExtra(Constants.CITY_ID, selectedCityId);
        intent.putExtra(Constants.ITEM_NAME, historyProduct.historyName);
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ZERO);

        activity.startActivity(intent);
    }

    public void navigateToUserHistoryActivity(Activity activity, String userId, String flagPaidOrNot) {
        Intent intent = new Intent(activity, UserHistoryListActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        intent.putExtra(Constants.FLAGPAIDORNOT, flagPaidOrNot);
        activity.startActivity(intent);
    }


    public void navigateToFavouriteActivity(Activity activity) {
        Intent intent = new Intent(activity, FavouriteListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCategoryActivity(Activity activity) {
        Intent intent = new Intent(activity, CategoryListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToSubCategoryActivity(Activity activity, String catId, String catName) {
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.CATEGORY_NAME, catName);
        activity.startActivity(intent);
    }

    public void navigateToMapActivity(Activity activity, String LNG, String LAT, String itemName) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(Constants.LNG, LNG);
        intent.putExtra(Constants.LAT, LAT);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        activity.startActivity(intent);
    }

    public void navigateToTypeFilterFragment(FragmentActivity mainActivity, String
            catId, String subCatId, ItemParameterHolder itemParameterHolder, String name) {

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);
            intent.putExtra(Constants.CATEGORY_ID, catId);
            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        } else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);
            intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);


            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        }

    }

    public void navigateBackToCommentListFragment(FragmentActivity
                                                          commentDetailActivity, String comment_headerId) {
        Intent intent = new Intent();
        intent.putExtra(Constants.COMMENT_HEADER_ID, comment_headerId);

        commentDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent);
    }

    public void navigateBackToProductDetailFragment(FragmentActivity
                                                            productDetailActivity, String product_id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_ID, product_id);

        productDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent);
    }

    public void navigateBackToHomeFeaturedFragment(FragmentActivity mainActivity, String
            catId, String subCatId) {
        Intent intent = new Intent();

        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);

        mainActivity.setResult(Constants.RESULT_CODE__CATEGORY_FILTER, intent);
    }

    public void navigateBackToHomeFeaturedFragmentFromFiltering(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);

        mainActivity.setResult(Constants.RESULT_CODE__SPECIAL_FILTER, intent);
    }

    public void navigateToCollectionItemList(FragmentActivity fragmentActivity, String
            id, String Name, String image_path) {
        Intent intent = new Intent(fragmentActivity, ItemCollectionActivity.class);
        intent.putExtra(Constants.COLLECTION_ID, id);
        intent.putExtra(Constants.COLLECTION_NAME, Name);
        intent.putExtra(Constants.COLLECTION_IMAGE, image_path);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToCategory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToCollection(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_PRODUCT_COLLECTION)) {
            try {
                CollectionHeaderListFragment fragment = new CollectionHeaderListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeLatestFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_LATEST_PRODUCTS)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToFeatureProduct(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_FEATURED_PRODUCTS)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToDiscountProduct(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_DISCOUNT)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomePopularFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_POPULAR_CITIES)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeFilteringActivity(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder, String title) {

        Intent intent = new Intent(mainActivity, SearchListActivity.class);

        intent.putExtra(Constants.ITEM_NAME, title);
        intent.putExtra(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);

        mainActivity.startActivity(intent);
    }

    public void navigateToSearchActivityCategoryFragment(FragmentActivity
                                                                 fragmentActivity, String fragName, String catId, String subCatId) {
        Intent intent = new Intent(fragmentActivity, DashboardSearchByCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_FLAG, fragName);

        if (!catId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.CATEGORY_ID, catId);
        }

        if (!subCatId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
        }

        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_FRAGMENT);
    }

    public void navigateBackToSearchFragment(FragmentActivity fragmentActivity, String
            catId, String cat_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CATEGORY_NAME, cat_Name);
        intent.putExtra(Constants.CATEGORY_ID, catId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_CATEGORY, intent);
    }

    public void navigateBackToSearchFragmentFromSubCategory(FragmentActivity
                                                                    fragmentActivity, String sub_id, String sub_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.SUBCATEGORY_NAME, sub_Name);
        intent.putExtra(Constants.SUBCATEGORY_ID, sub_id);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY, intent);
    }

    public void navigateBackToProfileFragment(FragmentActivity fragmentActivity) {
        Intent intent = new Intent();

        fragmentActivity.setResult(Constants.RESULT_CODE__LOGOUT_ACTIVATED, intent);
    }

    public void navigateToSelectedItemDetail(FragmentActivity fragmentActivity, String
            itemId, String itemName) {

        Intent intent = new Intent(fragmentActivity, ItemActivity.class);

        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToBlogListBySelectedCity(FragmentActivity fragmentActivity, String
            selectedCityId) {

        Intent intent = new Intent(fragmentActivity, BlogListByCityIdActivity.class);
        intent.putExtra(Constants.CITY_ID, selectedCityId);
        fragmentActivity.startActivity(intent);
    }

    public void navigateToBlogDetailActivity(FragmentActivity fragmentActivity, String blogId) {

        Intent intent = new Intent(fragmentActivity, BlogDetailActivity.class);

        intent.putExtra(Constants.BLOG_ID, blogId);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToForceUpdateActivity(FragmentActivity fragmentActivity, String title, String msg) {

        Intent intent = new Intent(fragmentActivity, ForceUpdateActivity.class);

        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_MSG, msg);

        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_TITLE, title);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToPlayStore(FragmentActivity fragmentActivity) {

        Uri uri = Uri.parse(Config.PLAYSTORE_MARKET_URL_FIX + fragmentActivity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            fragmentActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL_FIX + fragmentActivity.getPackageName())));
        }
    }

    public void navigateToPrivacyPolicy(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_PRIVACY_POLICY
        )) {
            try {
                PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PRIVACY_POLICY_NAME,Constants.EMPTY_STRING);
                fragment.setArguments(bundle);

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent(activity, MapFilteringActivity.class);

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE__MAP_FILTERING);
    }

    public void navigateBackToSearchFromMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.setResult(Constants.RESULT_CODE__MAP_FILTERING, intent);

        activity.finish();
    }

    //region Private methods
    private Boolean checkFragmentChange(RegFragments regFragments) {
        if (currentFragment != regFragments) {
            currentFragment = regFragments;
            return true;
        }

        return false;
    }


    public void navigateToItemUploadActivity(Activity activity, Item itemList) {
        Intent intent = new Intent(activity, ItemUploadActivity.class);
        if(itemList != null){
            intent.putExtra(Constants.ITEM_ID, itemList.id);
            intent.putExtra(Constants.ITEM_NAME, itemList.name);
        }
        activity.startActivity(intent);
    }

    public void navigateBackToEntryItemFragment(FragmentActivity expandActivity, String name, String id, int flag) {
        Intent intent = new Intent();

        if (flag == Constants.SELECT_CATEGORY) {
            intent.putExtra(Constants.CATEGORY_ID, id);
            intent.putExtra(Constants.CATEGORY_NAME, name);
            expandActivity.setResult(Constants.SELECT_CATEGORY, intent);
        }
        else if (flag == Constants.SELECT_SUBCATEGORY) {
            intent.putExtra(Constants.SUBCATEGORY_ID, id);
            intent.putExtra(Constants.SUBCATEGORY_NAME, name);
            expandActivity.setResult(Constants.SELECT_SUBCATEGORY, intent);
        }else if (flag == Constants.SELECT_STATUS){
            intent.putExtra(Constants.STATUS_ID,id);
            intent.putExtra(Constants.STATUS_NAME,name);
            expandActivity.setResult(Constants.SELECT_STATUS, intent);
        }
        expandActivity.finish();
    }

    public void navigateToExpandActivity(Activity activity, int flag, String selectId, String Id) {
        Intent intent = new Intent(activity, SelectionActivity.class);
        intent.putExtra(Constants.FLAG, flag);
        if (flag == Constants.SELECT_CATEGORY) {
            intent.putExtra(Constants.CATEGORY_ID, selectId);
        }
        if (flag == Constants.SELECT_SUBCATEGORY) {
            intent.putExtra(Constants.SUBCATEGORY_ID, selectId);
            intent.putExtra(Constants.CATEGORY_ID, Id);
        }
        if (flag == Constants.SELECT_STATUS){
            intent.putExtra(Constants.STATUS_ID,selectId);
        }

        activity.startActivityForResult(intent, 1);
    }

    public void getImageFromGallery(Activity activity) {
        if (Utils.isStoragePermissionGranted(activity)) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activity.startActivityForResult(Intent.createChooser(intent, "Select Photo"), Constants.RESULT_LOAD_IMAGE_CATEGORY);
        }

    }

    public void navigateBackToUpdateCategoryActivity(Activity activity, String status, String img_id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IMAGE_UPLOAD_STATUS, status);
        intent.putExtra(Constants.IMG_ID, img_id);

        activity.setResult(Constants.RESULT_CODE_FROM_IMAGE_UPLOAD, intent);
    }

    public void navigateToImageUploadActivity(Activity activity, String img_id, String img_path, String img_desc, int flag, String selectId) {
        Intent intent = new Intent(activity, ImageUploadActivity.class);

        intent.putExtra(Constants.IMG_ID, img_id);
        intent.putExtra(Constants.IMGPATH, img_path);
        intent.putExtra(Constants.IMGDESC, img_desc);
        intent.putExtra(Constants.FLAG, flag);
        intent.putExtra(Constants.SELECTED_ID, selectId);

        activity.startActivityForResult(intent, Constants.RESULT_GO_TO_IMAGE_UPLOAD);
    }

    public void navigateToSpecificationListActivity(Activity activity, String itemId, String itemName) {
        Intent intent = new Intent(activity, SpecificationListActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        activity.startActivity(intent);
    }

    public void navigateToAddingSpecification(Activity activity, String itemId, String specificationId, String specificationName, String specificationDescription) {
        Intent intent = new Intent(activity, AddAndEditSpecificationActivity.class);

        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.SPECIFICATION_ID, specificationId);
        intent.putExtra(Constants.SPECIFICATION_NAME, specificationName);
        intent.putExtra(Constants.SPECIFICATION_DESCRIPTION,specificationDescription);
        activity.startActivity(intent);
    }

    public void navigateToImageList(Activity activity, String item_id) {
        Intent intent = new Intent(activity, ItemImageListActivity.class);

        intent.putExtra(Constants.ITEM_ID, item_id);

        activity.startActivity(intent);
    }

    public void navigateToLists(Activity activity, String flag, String lat, String lng) {

        if (flag.equals(Constants.MAP)) {
            Intent intent = new Intent(activity, MapItemEntryActivity.class);
            intent.putExtra(Constants.FLAG, flag);
            intent.putExtra(Constants.LNG, lng);
            intent.putExtra(Constants.LAT, lat);

            activity.startActivityForResult(intent, Constants.REQUEST_CODE_TO_MAP_VIEW);

        }

    }

    public void navigateBackFromMapView(Activity activity, String lat, String lng) {
        Intent intent = new Intent();
        intent.putExtra(Constants.LAT, lat);
        intent.putExtra(Constants.LNG, lng);

        activity.setResult(Constants.RESULT_CODE_FROM_MAP_VIEW, intent);
    }

    public void navigateToPhoneVerifyFragment(MainActivity mainActivity, String number, String userName) {
        if (checkFragmentChange(RegFragments.HOME_PHONE_VERIFY)) {
            try {
                VerifyMobileFragment fragment = new VerifyMobileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

                Bundle args = new Bundle();
                args.putString(Constants.USER_PHONE, number);
                args.putString(Constants.USER_NAME, userName);
                fragment.setArguments(args);
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToPhoneVerifyActivity(Activity activity,String number,String userName) {
        Intent intent = new Intent(activity, VerifyMobileActivity.class);
        intent.putExtra(Constants.USER_PHONE, number);
        intent.putExtra(Constants.USER_NAME, userName);
        activity.startActivity(intent);
    }


    public void navigateToPhoneLoginFragment(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_PHONE_LOGIN)) {
            try {
                PhoneLoginFragment fragment = new PhoneLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToPhoneLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, PhoneLoginActivity.class);
        activity.startActivity(intent);
    }

    public void navigateBackToCheckoutFragment(Activity activity, String stripeToken) {
        Intent intent = new Intent();

        intent.putExtra(Constants.PAYMENT_TOKEN, stripeToken);

        activity.setResult(Constants.RESULT_CODE__STRIPE_ACTIVITY, intent);
    }

    public void navigateToStripeActivity(Activity fragmentActivity, String stripePublishableKey) {
        Intent intent = new Intent(fragmentActivity, StripeActivity.class);
        intent.putExtra(Constants.STRIPEPUBLISHABLEKEY, stripePublishableKey);
        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__STRIPE_ACTIVITY);
    }


    public void navigateToItemPromoteActivity(Activity activity, String itemId) {
        Intent intent = new Intent(activity, ItemPromoteActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);

        activity.startActivity(intent);
    }

    public void navigateToTransactions(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_TRANSACTION)) {
            try {
                LoginUserPaidItemFragment fragment = new LoginUserPaidItemFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    /**
     * Remark : This enum is only for MainActivity,
     * For the other fragments, no need to register here
     **/
    private enum RegFragments {
        HOME_FRAGMENT,
        HOME_USER_LOGIN,
        HOME_USER_EMAIL_VERIFY,
        HOME_FB_USER_REGISTER,
        HOME_BASKET,
        HOME_USER_REGISTER,
        HOME_USER_FOGOT_PASSWORD,
        HOME_ABOUTUS,
        HOME_CONTACTUS,
        HOME_NOTI_SETTING,
        HOME_APP_INFO,
        HOME_LANGUAGE_SETTING,
        HOME_LATEST_PRODUCTS,
        HOME_DISCOUNT,
        HOME_FEATURED_PRODUCTS,
        HOME_CATEGORY,
        HOME_SUBCATEGORY,
        HOME_HOME,
        HOME_TRENDINGPRODUCTS,
        HOME_COMMENTLISTS,
        HOME_SEARCH,
        HOME_NOTIFICATION,
        HOME_PRODUCT_COLLECTION,
        HOME_TRANSACTION,
        HOME_HISTORY,
        HOME_SETTING,
        HOME_FAVOURITE,
        HOME_ITEM_UPDATED,
        HOME_CITY_LIST,
        HOME_CITY_MENU,
        HOME_FILTER,
        HOME_CITIES,
        HOME_POPULAR_CITIES,
        HOME_RECOMMENDED_CITIES,
        HOME_PRIVACY_POLICY,
        HOME_PHONE_LOGIN,
        HOME_PHONE_VERIFY

    }
}
