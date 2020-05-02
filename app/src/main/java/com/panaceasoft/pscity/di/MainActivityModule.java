package com.panaceasoft.pscity.di;


import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.ui.apploading.AppLoadingActivity;
import com.panaceasoft.pscity.ui.apploading.AppLoadingFragment;
import com.panaceasoft.pscity.ui.blog.detail.BlogDetailActivity;
import com.panaceasoft.pscity.ui.blog.detail.BlogDetailFragment;
import com.panaceasoft.pscity.ui.blog.listbycityid.BlogListByCityIdActivity;
import com.panaceasoft.pscity.ui.blog.listbycityid.BlogListByCityIdFragment;
import com.panaceasoft.pscity.ui.category.categoryfilter.CategoryFilterFragment;
import com.panaceasoft.pscity.ui.category.categoryselection.CategorySelectionFragment;
import com.panaceasoft.pscity.ui.category.list.CategoryListActivity;
import com.panaceasoft.pscity.ui.category.list.CategoryListFragment;
import com.panaceasoft.pscity.ui.city.detail.CityActivity;
import com.panaceasoft.pscity.ui.city.detail.CityFragment;
import com.panaceasoft.pscity.ui.city.selectedcity.SelectedCityFragment;
import com.panaceasoft.pscity.ui.comment.detail.CommentDetailActivity;
import com.panaceasoft.pscity.ui.comment.detail.CommentDetailFragment;
import com.panaceasoft.pscity.ui.comment.list.CommentListActivity;
import com.panaceasoft.pscity.ui.comment.list.CommentListFragment;
import com.panaceasoft.pscity.ui.contactus.ContactUsFragment;
import com.panaceasoft.pscity.ui.dashboard.DashBoardSearchCategoryFragment;
import com.panaceasoft.pscity.ui.dashboard.DashBoardSearchFragment;
import com.panaceasoft.pscity.ui.dashboard.DashBoardSearchSubCategoryFragment;
import com.panaceasoft.pscity.ui.dashboard.DashboardSearchByCategoryActivity;
import com.panaceasoft.pscity.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.pscity.ui.forceupdate.ForceUpdateFragment;
import com.panaceasoft.pscity.ui.gallery.GalleryActivity;
import com.panaceasoft.pscity.ui.gallery.GalleryFragment;
import com.panaceasoft.pscity.ui.gallery.detail.GalleryDetailActivity;
import com.panaceasoft.pscity.ui.gallery.detail.GalleryDetailFragment;
import com.panaceasoft.pscity.ui.imageupload.ImageUploadActivity;
import com.panaceasoft.pscity.ui.imageupload.ImageUploadFragment;
import com.panaceasoft.pscity.ui.imageupload.ItemEntryImageUploadFragment;
import com.panaceasoft.pscity.ui.imageupload.itemimagelist.ItemImageListActivity;
import com.panaceasoft.pscity.ui.imageupload.itemimagelist.ItemImageListFragment;
import com.panaceasoft.pscity.ui.item.collection.header.CollectionHeaderListActivity;
import com.panaceasoft.pscity.ui.item.collection.header.CollectionHeaderListFragment;
import com.panaceasoft.pscity.ui.item.collection.itemCollection.ItemCollectionActivity;
import com.panaceasoft.pscity.ui.item.collection.itemCollection.ItemCollectionFragment;
import com.panaceasoft.pscity.ui.item.detail.ItemActivity;
import com.panaceasoft.pscity.ui.item.detail.ItemFragment;
import com.panaceasoft.pscity.ui.item.favourite.FavouriteListActivity;
import com.panaceasoft.pscity.ui.item.favourite.FavouriteListFragment;
import com.panaceasoft.pscity.ui.item.history.HistoryFragment;
import com.panaceasoft.pscity.ui.item.history.UserHistoryListActivity;
import com.panaceasoft.pscity.ui.item.map.MapActivity;
import com.panaceasoft.pscity.ui.item.map.MapFragment;
import com.panaceasoft.pscity.ui.item.map.mapFilter.MapFilteringActivity;
import com.panaceasoft.pscity.ui.item.map.mapFilter.MapFilteringFragment;
import com.panaceasoft.pscity.ui.item.promote.ItemPromoteActivity;
import com.panaceasoft.pscity.ui.item.promote.ItemPromoteFragment;
import com.panaceasoft.pscity.ui.item.promote.LoginUserPaidItemFragment;
import com.panaceasoft.pscity.ui.item.rating.RatingListActivity;
import com.panaceasoft.pscity.ui.item.rating.RatingListFragment;
import com.panaceasoft.pscity.ui.item.readmore.ReadMoreActivity;
import com.panaceasoft.pscity.ui.item.readmore.ReadMoreFragment;
import com.panaceasoft.pscity.ui.item.search.searchlist.SearchListActivity;
import com.panaceasoft.pscity.ui.item.search.searchlist.SearchListFragment;
import com.panaceasoft.pscity.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.panaceasoft.pscity.ui.item.search.specialfilterbyattributes.FilteringFragment;
import com.panaceasoft.pscity.ui.item.upload.ItemUploadActivity;
import com.panaceasoft.pscity.ui.item.upload.ItemUploadFragment;
import com.panaceasoft.pscity.ui.item.upload.SelectionActivity;
import com.panaceasoft.pscity.ui.item.upload.map.MapItemEntryActivity;
import com.panaceasoft.pscity.ui.item.uploaded.ItemUploadedListFragment;
import com.panaceasoft.pscity.ui.language.LanguageFragment;
import com.panaceasoft.pscity.ui.notification.detail.NotificationActivity;
import com.panaceasoft.pscity.ui.notification.detail.NotificationFragment;
import com.panaceasoft.pscity.ui.notification.list.NotificationListActivity;
import com.panaceasoft.pscity.ui.notification.list.NotificationListFragment;
import com.panaceasoft.pscity.ui.notification.setting.NotificationSettingActivity;
import com.panaceasoft.pscity.ui.notification.setting.NotificationSettingFragment;
import com.panaceasoft.pscity.ui.privacyandpolicy.PrivacyPolicyActivity;
import com.panaceasoft.pscity.ui.privacyandpolicy.PrivacyPolicyFragment;
import com.panaceasoft.pscity.ui.setting.SettingActivity;
import com.panaceasoft.pscity.ui.setting.SettingFragment;
import com.panaceasoft.pscity.ui.setting.appinfo.AppInfoActivity;
import com.panaceasoft.pscity.ui.setting.appinfo.AppInfoFragment;
import com.panaceasoft.pscity.ui.specification.SpecificationListActivity;
import com.panaceasoft.pscity.ui.specification.SpecificationListFragment;
import com.panaceasoft.pscity.ui.specification.addspecification.AddAndEditSpecificationActivity;
import com.panaceasoft.pscity.ui.specification.addspecification.AddAndEditSpecificationFragment;
import com.panaceasoft.pscity.ui.status.StatusSelectionFragment;
import com.panaceasoft.pscity.ui.stripe.StripeActivity;
import com.panaceasoft.pscity.ui.stripe.StripeFragment;
import com.panaceasoft.pscity.ui.subcategory.SubCategoryActivity;
import com.panaceasoft.pscity.ui.subcategory.SubCategoryFragment;
import com.panaceasoft.pscity.ui.subcategory.subcategoryselection.SubCategorySelectionFragment;
import com.panaceasoft.pscity.ui.user.PasswordChangeActivity;
import com.panaceasoft.pscity.ui.user.PasswordChangeFragment;
import com.panaceasoft.pscity.ui.user.ProfileEditActivity;
import com.panaceasoft.pscity.ui.user.ProfileEditFragment;
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

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

//    @ContributesAndroidInjector(modules = UserHistoryModule.class)
//    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = PrivacyPolicyActivityModule.class)
    abstract PrivacyPolicyActivity contributePrivacyPolicyActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = SubCategoryActivityModule.class)
    abstract SubCategoryActivity subCategoryActivity();

    @ContributesAndroidInjector(modules = CommentDetailModule.class)
    abstract CommentDetailActivity commentDetailActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = SearchActivityModule.class)
    abstract SearchListActivity contributeSearchListActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = ItemActivityModule.class)
    abstract ItemActivity itemActivity();

    @ContributesAndroidInjector(modules = CommentListActivityModule.class)
    abstract CommentListActivity commentListActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract DashboardSearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = readMoreActivityModule.class)
    abstract ReadMoreActivity readMoreActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = VerifyEmailActivityModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = ProductCollectionModule.class)
    abstract CollectionHeaderListActivity productCollectionHeaderListActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = CollectionModule.class)
    abstract ItemCollectionActivity collectionActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = MapActivityModule.class)
    abstract MapActivity mapActivity();

    @ContributesAndroidInjector(modules = CityActivityModule.class)
    abstract CityActivity cityActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = blogListByShopIdActivityModule.class)
    abstract BlogListByCityIdActivity forceBlogListByShopIdActivity();

    @ContributesAndroidInjector(modules = MapFilteringModule.class)
    abstract MapFilteringActivity mapFilteringActivity();

    @ContributesAndroidInjector(modules = AppLoadingActivityModule.class)
    abstract AppLoadingActivity appLoadingActivity();

    @ContributesAndroidInjector(modules = ItemUploadActivityModule.class)
    abstract ItemUploadActivity itemUploadActivity();

    @ContributesAndroidInjector(modules = SelectionActivityModule.class)
    abstract SelectionActivity selectionActivity();

    @ContributesAndroidInjector(modules = ImageUploadModule.class)
    abstract ImageUploadActivity contributeImageUploadActivity();

    @ContributesAndroidInjector(modules = SpecificationListModule.class)
    abstract SpecificationListActivity contributeSpecificationListActivity();

    @ContributesAndroidInjector(modules = AddAndEditSpecificationModule.class)
    abstract AddAndEditSpecificationActivity attributeHeaderAddActivity();

    @ContributesAndroidInjector(modules =ItemImageListModule.class)
    abstract ItemImageListActivity itemImageListActivity();

    @ContributesAndroidInjector(modules = MapItemEntryModule.class)
    abstract MapItemEntryActivity contributeMapItemEntryActivity();

    @ContributesAndroidInjector(modules = PhoneLoginActivityModule.class)
    abstract PhoneLoginActivity contributePhoneLoginActivity();

    @ContributesAndroidInjector(modules = VerifyMobileModule.class)
    abstract VerifyMobileActivity contributeVerifyMobileActivity();

    @ContributesAndroidInjector(modules = StripeeModule.class)
    abstract StripeActivity stripeActivity();

    @ContributesAndroidInjector(modules = ItemPromoteEntryActivityModule.class)
    abstract ItemPromoteActivity contributeItemPromoteEntryActivity();

    @ContributesAndroidInjector(modules = LoginUserHistoryListActivityModule.class)
    abstract UserHistoryListActivity userHistoryListActivity();
}

@Module
abstract class ImageUploadModule {
    @ContributesAndroidInjector
    abstract ImageUploadFragment contributeImageUploadFragment();

    @ContributesAndroidInjector
    abstract ItemEntryImageUploadFragment contributeItemEntryImageUploadFragment();
}

@Module
abstract class SelectionActivityModule {

    @ContributesAndroidInjector
    abstract CategorySelectionFragment contributeCategoryExpFragment();

    @ContributesAndroidInjector
    abstract SubCategorySelectionFragment contributeSubCategoryExpFragment();

    @ContributesAndroidInjector
    abstract StatusSelectionFragment contributeStatusSelectionExpFragment();
}

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();

    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();

    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeShopListFragment();

    @ContributesAndroidInjector
    abstract SearchListFragment contributeSearchListFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryListFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchFragment contributeDashBoardSearchFragment();

    @ContributesAndroidInjector
    abstract CollectionHeaderListFragment contributeCollectionHeaderListFragment();

    @ContributesAndroidInjector
    abstract ItemUploadedListFragment contributeItemUploadedFragment();

    @ContributesAndroidInjector
    abstract PhoneLoginFragment contributePhoneLoginFragment();

    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();

    @ContributesAndroidInjector
    abstract LoginUserPaidItemFragment contributeLoginUserPaidItemFragment();

}


@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class PrivacyPolicyActivityModule {
    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();
}

@Module
abstract class ItemActivityModule {
    @ContributesAndroidInjector
    abstract ItemFragment contributeItemFragment();
}

@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}

@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class CommentDetailModule {
    @ContributesAndroidInjector
    abstract CommentDetailFragment commentDetailFragment();
}


@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();


}


@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class ProductCollectionModule {
    @ContributesAndroidInjector
    abstract CollectionHeaderListFragment contributeProductCollectionHeaderListFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class readMoreActivityModule {
    @ContributesAndroidInjector
    abstract ReadMoreFragment contributeReadMoreFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class VerifyEmailActivityModule {
    @ContributesAndroidInjector
    abstract VerifyEmailFragment VerifyEmailFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class SubCategoryActivityModule {
    @ContributesAndroidInjector
    abstract SubCategoryFragment contributeSubCategoryFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilteringFragment contributeSpecialFilteringFragment();

}

@Module
abstract class SearchActivityModule {
    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract ItemCollectionFragment contributeCollectionFragment();
}

@Module
abstract class CommentListActivityModule {
    @ContributesAndroidInjector
    abstract CommentListFragment contributeCommentListFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {

    @ContributesAndroidInjector
    abstract DashBoardSearchCategoryFragment contributeDashBoardSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchSubCategoryFragment contributeDashBoardSearchSubCategoryFragment();
}

@Module
abstract class LoginUserHistoryListActivityModule {

    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();

    @ContributesAndroidInjector
    abstract  LoginUserPaidItemFragment contributeLoginUserPaidItemFragment();

}

@Module
abstract class SelectedCityModule {

    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CityFragment contributeAboutUsFragmentFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment categoryListFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeSelectedCityFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CollectionHeaderListFragment contributeProductCollectionHeaderListFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchFragment contributeDashBoardSearchFragment();
}

@Module
abstract class CollectionModule {

    @ContributesAndroidInjector
    abstract ItemCollectionFragment contributeCollectionFragment();

}

@Module
abstract class StripeModule {

    @ContributesAndroidInjector
    abstract ItemCollectionFragment contributeCollectionFragment();

}

@Module
abstract class BlogDetailModule {

    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class MapActivityModule {

    @ContributesAndroidInjector
    abstract MapFragment contributeMapFragment();
}

@Module
abstract class CityActivityModule {

    @ContributesAndroidInjector
    abstract CityFragment contributeCityFragment();
}

@Module
abstract class forceUpdateModule {

    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class blogListByShopIdActivityModule {

    @ContributesAndroidInjector
    abstract BlogListByCityIdFragment contributeBlogListByShopIdFragment();
}

@Module
abstract class MapFilteringModule {

    @ContributesAndroidInjector
    abstract MapFilteringFragment contributeMapFilteringFragment();
}

@Module
abstract class AppLoadingActivityModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}

@Module
abstract class ItemUploadActivityModule {

    @ContributesAndroidInjector
    abstract ItemUploadFragment contributeItemUploadFragment();
}

@Module
abstract class SpecificationListModule {

    @ContributesAndroidInjector
    abstract SpecificationListFragment contributeSpecificationListFragment();
}

@Module
abstract class AddAndEditSpecificationModule {

    @ContributesAndroidInjector
    abstract AddAndEditSpecificationFragment contributeAddAndEditSpecificationFragment();
}

@Module
abstract class ItemImageListModule {
    @ContributesAndroidInjector
    abstract ItemImageListFragment contributeItemImageListFragment();
}

@Module
abstract class MapItemEntryModule {
    @ContributesAndroidInjector
    abstract com.panaceasoft.pscity.ui.item.upload.map.MapFragment contributeUploadMapFragment();

}

@Module
abstract class PhoneLoginActivityModule {
    @ContributesAndroidInjector
    abstract PhoneLoginFragment cameraPhoneLoginFragment();
}

@Module
abstract class VerifyMobileModule {
    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}

@Module
abstract class StripeeModule {

    @ContributesAndroidInjector
    abstract StripeFragment contributeStripeFragment();

}

@Module
abstract class ItemPromoteEntryActivityModule {
    @ContributesAndroidInjector
    abstract ItemPromoteFragment contributeItemPromoteFragment();
}
