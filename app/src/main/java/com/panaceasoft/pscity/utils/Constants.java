package com.panaceasoft.pscity.utils;

/**
 * Created by Panacea-Soft on 3/19/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


public interface Constants {

    //region General

    String EMPTY_STRING = "";
    String SPACE_STRING = " ";
    String ZERO = "0";
    String ONE = "1";
    String NO_DATA = "NO_DATA";
    String DASH = "-";

    //endregion

    // region City

    String CITY_ID = "CITY_ID";
    String CITY_NAME = "CITY_NAME";
    String CITY = "CITY"; // Don't Change
    String CITY_HOLDER = "CITY_HOLDER";
    String CITY_TITLE = "CITY_TITLE";
    String CITY_START_DATE = "CITY_START_DATE";
    String CITY_END_DATE = "CITY_END_DATE";
    String CITY_TEL = "tel:"; // Don't Change
    String CITY_LAT = "CITY_LAT";
    String CITY_LNG = "CITY_LNG";

    //endregion

    // region Blog/News

    String BLOG_ID = "BLOG_ID";

    //endregion

    //region Rating

    String RATING_ZERO = "0";
    String RATING_ONE = "1";
    String RATING_TWO = "2";
    String RATING_THREE = "3";
    String RATING_FOUR = "4";
    String RATING_FIVE = "5";

    //endregion

    //region REQUEST CODE AND RESULT CODE

    int REQUEST_CODE__MAIN_ACTIVITY = 1000;
    int REQUEST_CODE__SEARCH_FRAGMENT = 1001;
    int REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT = 1003;
    int REQUEST_CODE__ITEM_LIST_FRAGMENT = 1004;
    int REQUEST_CODE__COMMENT_LIST_FRAGMENT = 1005;
    int REQUEST_CODE__ITEM_FRAGMENT = 1006;
    int REQUEST_CODE__PROFILE_FRAGMENT = 1007;
    int REQUEST_CODE__MAP_FILTERING = 1009;
    int REQUEST_CODE__GOOGLE_SIGN = 1010;
    int REQUEST_CODE__PAYPAL = 1030;
    int REQUEST_CODE__STRIPE_ACTIVITY = 1031;


    int RESULT_CODE__RESTART_MAIN_ACTIVITY = 2000;
    int RESULT_CODE__SEARCH_WITH_CATEGORY = 2001;
    int RESULT_CODE__SEARCH_WITH_SUBCATEGORY = 2002;
    int RESULT_CODE__REFRESH_NOTIFICATION = 2004;
    int RESULT_CODE__SPECIAL_FILTER = 2005;
    int RESULT_CODE__CATEGORY_FILTER = 2006;
    int RESULT_CODE__REFRESH_COMMENT_LIST = 2007;
    int RESULT_CODE__LOGOUT_ACTIVATED = 2008;
    int RESULT_LOAD_IMAGE = 1;
    int RESULT_OK = -1;
    int RESULT_CODE__MAP_FILTERING = 20010;

    int RESULT_CODE__STRIPE_ACTIVITY = 2020;
    //endregion

    //region Platform

    String PLATFORM = "android"; // Please don't change!

    //endregion

    //region Social
    String MESSENGER = "MESSENGER";
    String WHATSAPP = "WHATSAPP";
    //endregion

    //region AppInfo type_name

    String APPINFO_NAME_CITY = "APPINFO_NAME_CITY";
    String APPINFO_NAME_ITEM = "APPINFO_NAME_ITEM";
    String APPINFO_NAME_CATEGORY = "APPINFO_NAME_CATEGORY";
    String APPINFO_PREF_VERSION_NO = "APPINFO_PREF_VERSION_NO";
    String APPINFO_PREF_FORCE_UPDATE = "APPINFO_PREF_FORCE_UPDATE";
    String APPINFO_FORCE_UPDATE_MSG = "APPINFO_FORCE_UPDATE_MSG";
    String APPINFO_FORCE_UPDATE_TITLE = "APPINFO_FORCE_UPDATE_TITLE";


    //endregion

    //region User
    String FACEBOOK_ID = "FACEBOOK_ID";
    String GOOGLE_ID = "GOOGLE_ID";
    String USER_ID = "USER_ID";
    String USER_NAME = "USER_NAME";
    String USER_EMAIL = "USER_EMAIL";
    String USER_PHONE = "USER_PHONE";
    String USER_NO_USER = "nologinuser"; // Don't Change
    String USER_NO_DEVICE_TOKEN = "nodevicetoken"; // Don't Change
    String USER_PASSWORD = "password";
    String USER_EMAIL_TO_VERIFY = "USER_EMAIL_TO_VERIFY";
    String USER_PASSWORD_TO_VERIFY = "USER_PASSWORD_TO_VERIFY";
    String USER_NAME_TO_VERIFY = "USER_NAME_TO_VERIFY";
    String USER_ID_TO_VERIFY = "USER_ID_TO_VERIFY";


    //endregion

    //region Language

    String LANGUAGE_CODE = "Language";
    String LANGUAGE_COUNTRY_CODE ="Language_Country_Code";

    //endregion

    //region Image
    String IMG_ID = "img_id";
    String IMGPATH = "img_path";
    String IMGDESC = "img_desc";
    //endregion

    //region Product

    String ITEM_PARAM_HOLDER_KEY = "ITEM_PARAM_HOLDER_KEY";

    String ITEM_NAME = "ITEM_NAME";
    String ITEM_COUNT = "ITEM_COUNT";
    String ITEM_TAG = "ITEM_TAG";
    String ITEM_ID = "ITEM_id";
    String ITEM_HOLDER = "ITEM_HOLDER";
    String SPECIFICATION_ID = "specification_id";
    String SPECIFICATION_NAME = "specification_name";
    String SPECIFICATION_DESCRIPTION = "specification_description";
    String SELECTED_ID = "selectId";
    String SUCCESSFUL = "SUCCESSFUL";
    String IMAGE_UPLOAD_STATUS = "upload_status";
    //endregion

    String ITEM_TITLE = "item_title";
    String ITEM_DESCRIPTION = "item_desc";
    String ITEM_IMAGE_URL = "item_image";


    //region Collection

    String COLLECTION_NAME = "COLLECTION_NAME";
    String COLLECTION_IMAGE = "COLLECTION_IMAGE";
    String COLLECTION_ID = "COLLECTION_ID";

    //endregion

    //region Filtering Don't Change

    String FILTERING_FILTER_NAME = "name"; // Don't Change
    String FILTERING_TYPE_FILTER = "tf"; // Don't Change
    String FILTERING_SPECIAL_FILTER = "sf"; // Don't Change
    String FILTERING_TYPE_NAME = "item"; // Don't Change
    String FILTERING_INACTIVE = ""; // Don't Change
    String FILTERING_TRENDING = "touch_count"; // Don't Change
    String FILTERING_FEATURE = "featured_date"; // Don't Change
    String FILTERING_ASC = "asc"; // Don't Change
    String FILTERING_DESC = "desc"; // Don't Change
    String FILTERING_ADDED_DATE = "added_date"; // Don't Change
    String FILTERING_HOLDER = "filter_holder"; // Don't Change
    String FILTERING_NAME = "name"; // Don't Change

    //endregion

    //region Category

    String CATEGORY_NAME = "CATEGORY_NAME";
    String CATEGORY_ID = "CATEGORY_ID";
    String CATEGORY = "CATEGORY";
    String CATEGORY_ALL = "ALL";
    String CATEGORY_FLAG = "CATEGORY_FLAG";

    //endregion

    String PRIVACY_POLICY_NAME = "PRIVACY_POLICY_NAME";

    //region SubCategory
    String SUBCATEGORY_ID = "SUBCATEGORY_ID";
    String SUBCATEGORY = "SUBCATEGORY";
    String SUBCATEGORY_NAME = "SUBCATEGORY_NAME";

    //endregion

    //region Image

    String IMAGE_TYPE = "IMAGE_TYPE";
    String IMAGE_PARENT_ID = "IMAGE_PARENT_ID";
    String IMAGE_ID = "IMAGE_ID";
    String IMAGE_TYPE_PRODUCT = "item";

    //endregion

    //region Language

    String LANGUAGE = "Language";

    //endregion

    //region Comment

    String COMMENT_ID = "COMMENT_ID";
    String COMMENT_HEADER_ID = "COMMENT_HEADER_ID";

    //endregion


    //regionHistory

    String HISTORY_FLAG = "history_flag";

    //endregion


    //region Noti

    String NOTI_NEW_ID = "NOTI_NEW_ID";
    String NOTI_ID = "NOTI_ID";
    String NOTI_TOKEN = "NOTI_TOKEN";
    String NOTI_EXISTS_TO_SHOW = "IS_NOTI_EXISTS_TO_SHOW";
    String NOTI_MSG = "NOTI_MSG";
    String NOTI_SETTING = "NOTI_SETTING";
    String NOTI_HEADER_ID = "NOTI_HEADER_ID";

    //endregion


    //region FB Register

    String FB_FIELDS = "fields"; // Don't Change
    String FB_EMAILNAMEID = "email,name,id"; // Don't Change
    String FB_NAME_KEY = "name"; // Don't Change
    String FB_EMAIL_KEK = "email"; // Don't Change
    String FB_ID_KEY = "id"; // Don't Change

    //endregion

    //region Email Type

    String EMAIL_TYPE = "plain/text"; // Don't Change
    String HTTP = "http://"; // Don't Change
    String HTTPS = "https://"; // Don't Change

    //endregion

    //region BoostManger

    String GALLERY_BOOST = "android.gestureboost.GestureBoostManager"; // Don't Change
    String GALLERY_GESTURE = "sGestureBoostManager"; // Don't Change
    String GALLERY_ID = "id"; // Don't Change
    String GALLERY_CONTEXT = "mContext"; // Don't Change

    //endregion
    String MAP = "map";
    String LAT = "lat";
    String LNG = "lng";
    String FLAG = "flag";
    int REQUEST_CODE_TO_MAP_VIEW = 26;
    int RESULT_CODE_FROM_MAP_VIEW = 27;

    //region for onActivityResult clicked category or subcategory in item upload
    int SELECT_CATEGORY = 111;
    int SELECT_SUBCATEGORY = 222;
    int SELECT_STATUS = 333;

    //endregion

    //region
    String STATUS_ID = "STATUS_ID";
    String STATUS_NAME = "STATUS_NAME";
    //endregion

    //region checkFeature and checkPromotion
    String CHECKED_FEATURED = "1";
    String CHECKED_PROMOTION = "1";
    String NOT_CHECKED_FEATURED = "0";
    String NOT_CHECKED_PROMOTION = "0";
    //endregion

    //region image upload
    int RESULT_LOAD_IMAGE_CATEGORY = 10;
    int IMAGE_UPLOAD_ITEM = 1118;
    int RESULT_CODE_FROM_IMAGE_UPLOAD = 21;
    int RESULT_GO_TO_IMAGE_UPLOAD = 20;
    String IMAGE_COUNT_ENTRY2 = "6";
    //endregion

    //region public or unPublic
    String CHECKED_PUBLISH = "Publish";
    String CHECKED_UNPUBLISH = "Unpublish";
    String CHECKED_PENDING = "Pending";
    String CHECKED_REJECT = "Reject";
    String PUBLISH = "1";
    String UNPUBLISH = "0";


    String  PAIDITEMFIRST = "paid_item_first";
    String  ONLYPAIDITEM  = "only_paid_item";
    String  NOPAIDITEM = "no_paid_item";

    String ADSPROGRESS = "Progress";
    String ADSFINISHED = "Finished";
    String ADSNOTYETSTART = "Not yet start";
    String ADSNOTAVAILABLE = "not_available";

    String STRIPEPUBLISHABLEKEY = "STRIPEPUBLISHABLEKEY";
    String FLAGPAID = "FLAGPAID";
    String FLAGNOPAID = "FLAGNOPAID";
    String FLAGPAIDORNOT = "FLAGPAIDORNOT";

    //payment
    String PAYPAL = "paypal";
    String STRIPE = "stripe";
    String PAYMENT_TOKEN = "TOKEN";

}

