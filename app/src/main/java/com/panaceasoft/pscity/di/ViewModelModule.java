package com.panaceasoft.pscity.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.viewmodel.ItemPaidHistoryViewModel.ItemPaidHistoryViewModel;
import com.panaceasoft.pscity.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.pscity.viewmodel.apploading.AppLoadingViewModel;
import com.panaceasoft.pscity.viewmodel.blog.BlogViewModel;
import com.panaceasoft.pscity.viewmodel.city.CityViewModel;
import com.panaceasoft.pscity.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.pscity.viewmodel.comment.CommentDetailListViewModel;
import com.panaceasoft.pscity.viewmodel.comment.CommentListViewModel;
import com.panaceasoft.pscity.viewmodel.common.NotificationViewModel;
import com.panaceasoft.pscity.viewmodel.common.PSNewsViewModelFactory;
import com.panaceasoft.pscity.viewmodel.contactus.ContactUsViewModel;
import com.panaceasoft.pscity.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.panaceasoft.pscity.viewmodel.image.ImageViewModel;
import com.panaceasoft.pscity.viewmodel.item.DiscountItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.pscity.viewmodel.item.FeaturedItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.HistoryViewModel;
import com.panaceasoft.pscity.viewmodel.item.PopularItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.RecentItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.SpecsViewModel;
import com.panaceasoft.pscity.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.pscity.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.pscity.viewmodel.itemcollection.ItemCollectionViewModel;
import com.panaceasoft.pscity.viewmodel.itemstatus.ItemStatusViewModel;
import com.panaceasoft.pscity.viewmodel.itemsubcategory.ItemSubCategoryViewModel;
import com.panaceasoft.pscity.viewmodel.notification.NotificationsViewModel;
import com.panaceasoft.pscity.viewmodel.paypal.PaypalViewModel;
import com.panaceasoft.pscity.viewmodel.rating.RatingViewModel;
import com.panaceasoft.pscity.viewmodel.user.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Panacea-Soft on 11/16/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PSNewsViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel.class)
    abstract ViewModel bindAboutUsViewModel(AboutUsViewModel aboutUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel.class)
    abstract ViewModel bindImageViewModel(ImageViewModel imageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel.class)
    abstract ViewModel bindRatingViewModel(RatingViewModel ratingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel notificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    abstract ViewModel bindContactUsViewModel(ContactUsViewModel contactUsViewModel);

  /*  @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);*/

    @Binds
    @IntoMap
    @ViewModelKey(CommentListViewModel.class)
    abstract ViewModel bindCommentViewModel(CommentListViewModel commentListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CommentDetailListViewModel.class)
    abstract ViewModel bindCommentDetailViewModel(CommentDetailListViewModel commentDetailListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel.class)
    abstract ViewModel bindFavouriteViewModel(FavouriteViewModel favouriteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel.class)
    abstract ViewModel bindTouchCountViewModel(TouchCountViewModel touchCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SpecsViewModel.class)
    abstract ViewModel bindProductSpecsViewModel(SpecsViewModel specsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    abstract ViewModel bindHistoryProductViewModel(HistoryViewModel historyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCategoryViewModel.class)
    abstract ViewModel bindCityCategoryViewModel(ItemCategoryViewModel itemCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    abstract ViewModel bindNotificationListViewModel(NotificationsViewModel notificationListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingCategoryListViewModel.class)
    abstract ViewModel bindHomeTrendingCategoryListViewModel(HomeTrendingCategoryListViewModel transactionListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(BlogViewModel blogViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel.class)
    abstract ViewModel bindClearAllDataViewModel(ClearAllDataViewModel clearAllDataViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel.class)
    abstract ViewModel bindCityViewModel(CityViewModel cityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(com.panaceasoft.pscity.viewmodel.item.ItemViewModel.class)
    abstract ViewModel bindItemViewModel(com.panaceasoft.pscity.viewmodel.item.ItemViewModel itemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DiscountItemViewModel.class)
    abstract ViewModel bindDiscountItemViewModel(DiscountItemViewModel discountItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedItemViewModel.class)
    abstract ViewModel bindFeaturedItemViewModel(FeaturedItemViewModel featuredItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularItemViewModel.class)
    abstract ViewModel bindPopularItemViewModel(PopularItemViewModel popularItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecentItemViewModel.class)
    abstract ViewModel bindRecentItemViewModel(RecentItemViewModel recentItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCollectionViewModel.class)
    abstract ViewModel bindItemCollectionViewModel(ItemCollectionViewModel itemCollectionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemSubCategoryViewModel.class)
    abstract ViewModel bindItemSubCategoryViewModel(ItemSubCategoryViewModel itemSubCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppLoadingViewModel.class)
    abstract ViewModel bindAppLoadingViewModel(AppLoadingViewModel appLoadingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemStatusViewModel.class)
    abstract ViewModel bindItemStatusViewModel(ItemStatusViewModel itemStatusViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaypalViewModel.class)
    abstract ViewModel bindPaypalViewModel(PaypalViewModel paypalViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemPaidHistoryViewModel.class)
    abstract ViewModel bindItemPaidHistoryViewModel(ItemPaidHistoryViewModel itemPaidHistoryViewModel);
}


