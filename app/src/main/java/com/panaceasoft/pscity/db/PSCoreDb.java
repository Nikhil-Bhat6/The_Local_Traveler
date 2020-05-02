package com.panaceasoft.pscity.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.panaceasoft.pscity.db.common.Converters;
import com.panaceasoft.pscity.viewobject.AboutUs;
import com.panaceasoft.pscity.viewobject.Blog;
import com.panaceasoft.pscity.viewobject.City;
import com.panaceasoft.pscity.viewobject.Comment;
import com.panaceasoft.pscity.viewobject.CommentDetail;
import com.panaceasoft.pscity.viewobject.DeletedObject;
import com.panaceasoft.pscity.viewobject.Image;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.ItemCollection;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;
import com.panaceasoft.pscity.viewobject.ItemFavourite;
import com.panaceasoft.pscity.viewobject.ItemHistory;
import com.panaceasoft.pscity.viewobject.ItemMap;
import com.panaceasoft.pscity.viewobject.ItemPaidHistory;
import com.panaceasoft.pscity.viewobject.ItemSpecs;
import com.panaceasoft.pscity.viewobject.ItemStatus;
import com.panaceasoft.pscity.viewobject.ItemSubCategory;
import com.panaceasoft.pscity.viewobject.Noti;
import com.panaceasoft.pscity.viewobject.PSAppInfo;
import com.panaceasoft.pscity.viewobject.PSAppVersion;
import com.panaceasoft.pscity.viewobject.Rating;
import com.panaceasoft.pscity.viewobject.User;
import com.panaceasoft.pscity.viewobject.UserLogin;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        ItemFavourite.class,
        Comment.class,
        CommentDetail.class,
        Noti.class,
        ItemHistory.class,
        Blog.class,
        Rating.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class,
        City.class,
        Item.class,
        ItemMap.class,
        ItemCategory.class,
        ItemCollectionHeader.class,
        ItemCollection.class,
        ItemSubCategory.class,
        ItemSpecs.class,
        ItemStatus.class,
        ItemPaidHistory.class

}, version = 5, exportSchema = false)
//1.7 = 5
//1.5 = 5
//1.4 = 5
//1.3 = 4
//1.2 = 3
//1.1 = 2

@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public HistoryDao historyDao();

    abstract public SpecsDao specsDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public RatingDao ratingDao();

    abstract public CommentDao commentDao();

    abstract public CommentDetailDao commentDetailDao();

    abstract public NotificationDao notificationDao();

    abstract public BlogDao blogDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();

    abstract public CityDao cityDao();

    abstract public ItemDao itemDao();

    abstract public ItemMapDao itemMapDao();

    abstract public ItemCategoryDao itemCategoryDao();

    abstract public ItemCollectionHeaderDao itemCollectionHeaderDao();

    abstract public ItemSubCategoryDao itemSubCategoryDao();

    abstract public ItemStatusDao itemStatusDao();

    abstract public ItemPaidHistoryDao itemPaidHistoryDao();

//    /**
//     * Migrate from:
//     * version 1 - using Room
//     * to
//     * version 2 - using Room where the {@link } has an extra field: addedDateStr
//     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}