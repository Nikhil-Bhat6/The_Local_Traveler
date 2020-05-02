package com.panaceasoft.pscity.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.api.PSApiService;
import com.panaceasoft.pscity.db.AboutUsDao;
import com.panaceasoft.pscity.db.BlogDao;
import com.panaceasoft.pscity.db.CityDao;
import com.panaceasoft.pscity.db.CommentDao;
import com.panaceasoft.pscity.db.CommentDetailDao;
import com.panaceasoft.pscity.db.DeletedObjectDao;
import com.panaceasoft.pscity.db.HistoryDao;
import com.panaceasoft.pscity.db.ImageDao;
import com.panaceasoft.pscity.db.ItemCategoryDao;
import com.panaceasoft.pscity.db.ItemCollectionHeaderDao;
import com.panaceasoft.pscity.db.ItemDao;
import com.panaceasoft.pscity.db.ItemMapDao;
import com.panaceasoft.pscity.db.ItemPaidHistoryDao;
import com.panaceasoft.pscity.db.ItemStatusDao;
import com.panaceasoft.pscity.db.ItemSubCategoryDao;
import com.panaceasoft.pscity.db.NotificationDao;
import com.panaceasoft.pscity.db.PSAppInfoDao;
import com.panaceasoft.pscity.db.PSAppVersionDao;
import com.panaceasoft.pscity.db.PSCoreDb;
import com.panaceasoft.pscity.db.RatingDao;
import com.panaceasoft.pscity.db.UserDao;
import com.panaceasoft.pscity.utils.AppLanguage;
import com.panaceasoft.pscity.utils.Connectivity;
import com.panaceasoft.pscity.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    PSApiService providePSApiService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(PSApiService.class);

    }

    @Singleton
    @Provides
    PSCoreDb provideDb(Application app) {
        return Room.databaseBuilder(app, PSCoreDb.class, "pscity.db")
                //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    Connectivity provideConnectivity(Application app) {
        return new Connectivity(app);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Singleton
    @Provides
    UserDao provideUserDao(PSCoreDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    AppLanguage provideCurrentLanguage(SharedPreferences sharedPreferences) {
        return new AppLanguage(sharedPreferences);
    }

    @Singleton
    @Provides
    AboutUsDao provideAboutUsDao(PSCoreDb db) {
        return db.aboutUsDao();
    }

    @Singleton
    @Provides
    ImageDao provideImageDao(PSCoreDb db) {
        return db.imageDao();
    }

    @Singleton
    @Provides
    HistoryDao provideHistoryDao(PSCoreDb db) {
        return db.historyDao();
    }

    @Singleton
    @Provides
    RatingDao provideRatingDao(PSCoreDb db) {
        return db.ratingDao();
    }

    @Singleton
    @Provides
    CommentDao provideCommentDao(PSCoreDb db) {
        return db.commentDao();
    }

    @Singleton
    @Provides
    CommentDetailDao provideCommentDetailDao(PSCoreDb db) {
        return db.commentDetailDao();
    }

    @Singleton
    @Provides
    NotificationDao provideNotificationDao(PSCoreDb db){return db.notificationDao();}

    @Singleton
    @Provides
    BlogDao provideNewsFeedDao(PSCoreDb db){return db.blogDao();}

    @Singleton
    @Provides
    PSAppInfoDao providePSAppInfoDao(PSCoreDb db){return db.psAppInfoDao();}

    @Singleton
    @Provides
    PSAppVersionDao providePSAppVersionDao(PSCoreDb db){return db.psAppVersionDao();}

    @Singleton
    @Provides
    DeletedObjectDao provideDeletedObjectDao(PSCoreDb db){return db.deletedObjectDao();}

    @Singleton
    @Provides
    CityDao provideCityDao(PSCoreDb db){return db.cityDao();}

    @Singleton
    @Provides
    ItemDao provideItemDao(PSCoreDb db){return db.itemDao();}

    @Singleton
    @Provides
    ItemMapDao provideItemMapDao(PSCoreDb db){return db.itemMapDao();}

    @Singleton
    @Provides
    ItemCategoryDao provideCityCategoryDao(PSCoreDb db){return db.itemCategoryDao();}

    @Singleton
    @Provides
    ItemCollectionHeaderDao provideItemCollectionHeaderDao(PSCoreDb db){return db.itemCollectionHeaderDao();}

    @Singleton
    @Provides
    ItemSubCategoryDao provideItemSubCategoryDao(PSCoreDb db){return db.itemSubCategoryDao();}

    @Singleton
    @Provides
    ItemStatusDao provideItemStatusDao(PSCoreDb db){return db.itemStatusDao();}

    @Singleton
    @Provides
    ItemPaidHistoryDao provideItemPaidHistoryDao(PSCoreDb db){return db.itemPaidHistoryDao();}
}
