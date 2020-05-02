package com.panaceasoft.pscity.api;

import androidx.lifecycle.LiveData;

import com.panaceasoft.pscity.viewobject.AboutUs;
import com.panaceasoft.pscity.viewobject.ApiStatus;
import com.panaceasoft.pscity.viewobject.Blog;
import com.panaceasoft.pscity.viewobject.City;
import com.panaceasoft.pscity.viewobject.Comment;
import com.panaceasoft.pscity.viewobject.CommentDetail;
import com.panaceasoft.pscity.viewobject.Image;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.ItemCollectionHeader;
import com.panaceasoft.pscity.viewobject.ItemPaidHistory;
import com.panaceasoft.pscity.viewobject.ItemSpecs;
import com.panaceasoft.pscity.viewobject.ItemStatus;
import com.panaceasoft.pscity.viewobject.ItemSubCategory;
import com.panaceasoft.pscity.viewobject.Noti;
import com.panaceasoft.pscity.viewobject.PSAppInfo;
import com.panaceasoft.pscity.viewobject.Rating;
import com.panaceasoft.pscity.viewobject.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * REST API access points
 */
public interface PSApiService {

    //region Get favourite product list

    @GET("rest/items/get_favourite/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Item>>> getFavouriteList(@Path("API_KEY") String apiKey, @Path("login_user_id") String login_user_id, @Path("limit") String limit, @Path("offset") String offset);

    //endregion

    //region Post Favourite Product
    @FormUrlEncoded
    @POST("rest/favourites/press/api_key/{API_KEY}")
    Call<Item> setPostFavourite(
            @Path("API_KEY") String api_key,
            @Field("item_id") String itemId,
            @Field("user_id") String userId);

    //endregion

    //region Get Product Detail

    @GET("rest/items/get/api_key/{API_KEY}/id/{id}/login_user_id/{login_user_id}")
    LiveData<ApiResponse<Item>> getItemDetail(@Path("API_KEY") String apiKey, @Path("id") String Id, @Path("login_user_id") String login_user_id);

    //endregion


    //region Get Image List

    @GET("rest/images/get/api_key/{API_KEY}/img_parent_id/{img_parent_id}/img_type/{img_type}")
    LiveData<ApiResponse<List<Image>>> getImageList(@Path("API_KEY") String apiKey, @Path("img_parent_id") String img_parent_id, @Path("img_type") String imageType);


    //endregion


    //region Comments

    //region Get commentlist

    @GET("rest/commentheaders/get/api_key/{API_KEY}/item_id/{item_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<com.panaceasoft.pscity.viewobject.Comment>>> getCommentList(@Path("API_KEY") String apiKey, @Path("item_id") String itemId, @Path("limit") String limit, @Path("offset") String offset);

    //endregion

    //region Get comment detail list
    @GET("rest/commentdetails/get/api_key/{API_KEY}/header_id/{header_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<com.panaceasoft.pscity.viewobject.CommentDetail>>> getCommentDetailList(@Path("API_KEY") String apiKey, @Path("header_id") String headerId, @Path("limit") String limit, @Path("offset") String offset);

    //endregion

    //region Get comment detail count

    @GET("rest/commentheaders/get/api_key/{API_KEY}/id/{id}")
    Call<Comment> getRawCommentDetailCount(@Path("API_KEY") String apiKey, @Path("id") String id);

    //endregion

    //region Post comment header

    @FormUrlEncoded
    @POST("rest/commentheaders/press/api_key/{API_KEY}")
    Call<List<Comment>> rawCommentHeaderPost(
            @Path("API_KEY") String apiKey,
            @Field("item_id") String itemId,
            @Field("user_id") String userId,
            @Field("header_comment") String headerComment);

    //endregion

    //region Post comment detail

    @FormUrlEncoded
    @POST("rest/commentdetails/press/api_key/{API_KEY}")
    Call<List<CommentDetail>> rawCommentDetailPost(
            @Path("API_KEY") String apiKey,
            @Field("header_id") String headerId,
            @Field("user_id") String userId,
            @Field("detail_comment") String detailComment);

    //endregion

    //endregion


    //region Notification

    //region Submit Notification Token
    @FormUrlEncoded
    @POST("rest/notis/register/api_key/{API_KEY}")
    Call<ApiStatus> rawRegisterNotiToken(@Path("API_KEY") String apiKey, @Field("platform_name") String platform, @Field("device_id") String deviceId);


    @FormUrlEncoded
    @POST("rest/notis/unregister/api_key/{API_KEY}")
    Call<ApiStatus> rawUnregisterNotiToken(@Path("API_KEY") String apiKey, @Field("platform_name") String platform, @Field("device_id") String deviceId);

    //endregion

    //region Get Notification List

    @FormUrlEncoded
    @POST("rest/notis/all_notis/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Noti>>> getNotificationList(@Path("API_KEY") String apiKey,
                                                          @Path("limit") String limit,
                                                          @Path("offset") String offset,
                                                          @Field("user_id") String userId,
                                                          @Field("device_token") String deviceToken);

    //endregion

    //region Get Notification detail

    @GET("rest/notis/get/api_key/{API_KEY}/id/{id}")
    LiveData<ApiResponse<Noti>> getNotificationDetail(@Path("API_KEY") String apiKey, @Path("id") String id);

    //endregion

    //region Is Read Notificaiton
    @FormUrlEncoded
    @POST("rest/notis/is_read/api_key/{API_KEY}")
    Call<Noti> isReadNoti(
            @Path("API_KEY") String apiKey,
            @Field("noti_id") String noti_id,
            @Field("user_id") String userId,
            @Field("device_token") String device_token);

    //endregion

    @FormUrlEncoded
    @POST("rest/users/google_register/api_key/{API_KEY}")
    Call<User> postGoogleLogin(
            @Path("API_KEY") String API_KEY,
            @Field("google_id") String googleId,
            @Field("user_name") String userName,
            @Field("user_email") String userEmail,
            @Field("profile_photo_url") String profilePhotoUrl,
            @Field("device_token") String deviceToken
    );

    @FormUrlEncoded
    @POST("rest/users/verify/api_key/{API_KEY}")
    Call<User> verifyEmail(
            @Path("API_KEY") String API_KEY,
            @Field("user_id") String userId,
            @Field("code") String code);


    @FormUrlEncoded
    @POST("rest/users/request_code/api_key/{API_KEY}")
    Call<ApiStatus> resentCodeAgain(
            @Path("API_KEY") String API_KEY,
            @Field("user_email") String user_email
    );

    //region Get category list

    @FormUrlEncoded
    @POST("rest/categories/search/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemCategory>>> getSearchCategory(@Path("API_KEY") String apiKey, @Path("limit") String limit,
                                                                @Path("offset") String offset, @Field("order_by") String order_by, @Field("shop_id") String shopId);

    //endregion

    //region GET User
    @GET("rest/users/get/api_key/{API_KEY}/user_id/{user_id}")
    LiveData<ApiResponse<List<User>>> getUser(@Path("API_KEY") String apiKey, @Path("user_id") String userId);
    //endregion

    //region POST Upload Image
    @Multipart
    @POST("rest/images/upload/api_key/{API_KEY}")
    LiveData<ApiResponse<User>> doUploadImage(@Path("API_KEY") String apiKey, @Part("user_id") RequestBody userId, @Part("file") RequestBody name, @Part MultipartBody.Part file, @Part("platform_name") RequestBody platformName);
    //endregion

    //region POST User for Login
    @FormUrlEncoded
    @POST("rest/users/login/api_key/{API_KEY}")
    LiveData<ApiResponse<User>> postUserLogin(@Path("API_KEY") String apiKey, @Field("user_email") String userEmail, @Field("user_password") String userPassword);
    //endregion

    //region POST User for Register

    @FormUrlEncoded
    @POST("rest/users/register/api_key/{API_KEY}")
    Call<User> postFBUser(@Path("API_KEY") String apiKey, @Field("facebook_id") String facebookId, @Field("user_name") String userName, @Field("user_email") String userEmail, @Field("profile_photo_url") String profilePhotoUrl);

    @FormUrlEncoded
    @POST("rest/users/add/api_key/{API_KEY}")
    Call<User> postUser(@Path("API_KEY") String apiKey, @Field("user_id") String userId, @Field("user_name") String userName, @Field("user_email") String userEmail, @Field("user_password") String userPassword, @Field("user_phone") String userPhone, @Field("device_token") String deviceToken);
    //endregion

    //region POST Forgot Password
    @FormUrlEncoded
    @POST("rest/users/reset/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> postForgotPassword(@Path("API_KEY") String apiKey, @Field("user_email") String userEmail);
    //endregion

    //region PUT User for User Update
    @FormUrlEncoded
    @POST("rest/users/profile_update/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> putUser(@Path("API_KEY") String apiKey,
                                             @Field("user_id") String loginUserId,
                                             @Field("user_name") String userName,
                                             @Field("user_email") String userEmail,
                                             @Field("user_phone") String userPhone,
                                             @Field("user_about_me") String userAboutMe);

    //endregion

    //region PUT for Password Update
    @FormUrlEncoded
    @POST("rest/users/password_update/api_key/{API_KEY}")
    LiveData<ApiResponse<ApiStatus>> postPasswordUpdate(@Path("API_KEY") String apiKey, @Field("user_id") String loginUserId, @Field("user_password") String password);
    //endregion

    //endregion


    //region About Us

    @GET("rest/abouts/get/api_key/{API_KEY}")
    LiveData<ApiResponse<List<AboutUs>>> getAboutUs(@Path("API_KEY") String apiKey);

    //endregion


    //region Contact Us

    @FormUrlEncoded
    @POST("rest/contacts/add/api_key/{API_KEY}")
    Call<ApiStatus> rawPostContact(@Path("API_KEY") String apiKey, @Field("contact_name") String contactName, @Field("contact_email") String contactEmail, @Field("contact_message") String contactMessage, @Field("contact_phone") String contactPhone);

    //endregion


    //region GET SubCategory List

    @GET("rest/subcategories/get/api_key/{API_KEY}")
    LiveData<ApiResponse<List<ItemSubCategory>>> getAllSubCategoryList(@Path("API_KEY") String apiKey);

    @GET("rest/subcategories/get/api_key/{API_KEY}/cat_id/{cat_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemSubCategory>>> getSubCategoryList(@Path("API_KEY") String apiKey, @Path("cat_id") String catId, @Path("limit") String limit, @Path("offset") String offset);

    @GET("rest/subcategories/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}/cat_id/{cat_id}")
    LiveData<ApiResponse<List<ItemSubCategory>>> getSubCategoryListWithCatId(@Path("API_KEY") String apiKey, @Path("cat_id") String catId, @Path("limit") String limit, @Path("offset") String offset);

    //endregion

    //region Delete Shop list by date

    @FormUrlEncoded
    @POST("rest/appinfo/get_delete_history/api_key/{API_KEY}")
    Call<PSAppInfo> getDeletedHistory(
            @Path("API_KEY") String apiKey,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date);

    //endregion

    //region Get All Rating List

    @GET("rest/rates/get/api_key/{API_KEY}/item_id/{item_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Rating>>> getAllRatingList(@Path("API_KEY") String apiKey, @Path("item_id") String itemId, @Path("limit") String limit, @Path("offset") String offset);

    //endregion

    //region Post Rating

    @FormUrlEncoded
    @POST("rest/rates/add_rating/api_key/{API_KEY}")
    Call<Rating> postRating(
            @Path("API_KEY") String api_key,
            @Field("title") String title,
            @Field("description") String description,
            @Field("rating") String rating,
            @Field("user_id") String userId,
            @Field("item_id") String item_id);

    //endregion

    //endregion


    //region Touch Count

    @FormUrlEncoded
    @POST("rest/touches/add_touch/api_key/{API_KEY}")
    Call<ApiStatus> setrawPostTouchCount(
            @Path("API_KEY") String apiKey,
            @Field("type_id") String typeId,
            @Field("type_name") String typeName,
            @Field("user_id") String userId);

    //endregion


    //region News|Blog

    @GET("rest/feeds/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Blog>>> getAllNewsFeed(@Path("API_KEY") String api_key, @Path("limit") String limit, @Path("offset") String offset);

    @GET("rest/feeds/get/api_key/{API_KEY}/id/{id}")
    LiveData<ApiResponse<Blog>> getNewsById(@Path("API_KEY") String api_key, @Path("id") String id);

    //endregion

    ///////////////////////////////////////////////////////////////////////////////////////////////////MultiCity/////////////////////////////////////////////////////////////////


    //region City Info
    @GET("rest/cities/get_city_info/api_key/{API_KEY}")
    LiveData<ApiResponse<City>> getCityInfo(@Path("API_KEY") String api_key);

//    @FormUrlEncoded
//    @POST("rest/cities/search/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
//    LiveData<ApiResponse<List<City>>> searchCity(
//            @Path("API_KEY") String API_KEY,
//            @Path("limit") String limit,
//            @Path("offset") String offset,
//            @Field("id") String id,
//            @Field("keyword") String keyword,
//            @Field("is_featured") String is_featured,
//            @Field("order_by") String order_by,
//            @Field("order_type") String order_type);


    //endregion


    //region SearchItem
    @FormUrlEncoded
    @POST("rest/items/search/api_key/{API_KEY}/limit/{limit}/offset/{offset}/login_user_id/{login_user_id}")
    LiveData<ApiResponse<List<Item>>> searchItem(
            @Path("API_KEY") String API_KEY,
            @Path("limit") String limit,
            @Path("offset") String offset,
            @Path("login_user_id") String login_user_id,
            @Field("keyword") String keyword,
            @Field("cat_id") String cat_id,
            @Field("sub_cat_id") String sub_cat_id,
            @Field("order_by") String order_by,
            @Field("order_type") String order_type,
            @Field("rating_value") String rating_value,
            @Field("is_featured") String is_featured,
            @Field("is_promotion") String is_promotion,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("miles") String miles,
            @Field("added_user_id") String addedUserId,
            @Field("is_paid") String isPaid);


    //endregion

    //region ItemCategory

    @GET("rest/categories/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemCategory>>> getCityCategory(

            @Path("API_KEY") String API_KEY,
            @Path("limit") String limit,
            @Path("offset") String offset

    );

    //region ItemCategory

    //region Collection

    @GET("rest/collections/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemCollectionHeader>>> getCollectionHeaderByCityId(

            @Path("API_KEY") String API_KEY,
            @Path("limit") String limit,
            @Path("offset") String offset
    );

    @GET("rest/items/all_collection_items/api_key/{API_KEY}/limit/{limit}/offset/{offset}/id/{id}")
    LiveData<ApiResponse<List<Item>>> getCollectionItems(@Path("API_KEY") String apiKey, @Path("limit") String limit, @Path("offset") String offset, @Path("id") String id);


    //regionFor ItemUpload
    @FormUrlEncoded
    @POST("rest/items/submit_items/api_key/{API_KEY}")
    LiveData<ApiResponse<Item>> saveItem(
            @Path("API_KEY") String API_KEY,
            @Field("user_id") String userId,
            @Field("city_id") String cityId,
            @Field("cat_id") String categoryId,
            @Field("sub_cat_id") String subCategoryId,
            @Field("status") String status,
            @Field("name") String name,
            @Field("description") String description,
            @Field("search_tag") String searchTag,
            @Field("highlight_information") String highlightInformation,
            @Field("is_featured") String isFeatured,
            @Field("lat") String latitude,
            @Field("lng") String longitude,
            @Field("opening_hour") String openingHour,
            @Field("closing_hour") String closingHour,
            @Field("is_promotion") String isPromotion,
            @Field("phone1") String phoneOne,
            @Field("phone2") String phoneTwo,
            @Field("phone3") String phoneThree,
            @Field("email") String email,
            @Field("address") String address,
            @Field("facebook") String facebook,
            @Field("google_plus") String googlePlus,
            @Field("twitter") String twitter,
            @Field("youtube") String youtube,
            @Field("instagram") String instagram,
            @Field("pinterest") String pinterest,
            @Field("website") String website,
            @Field("whatsapp") String whatsapp,
            @Field("messenger") String messenger,
            @Field("time_remark") String timeRemark,
            @Field("terms") String terms,
            @Field("cancelation_policy") String cancelationPolicy,
            @Field("additional_info") String additionalInfo,
            @Field("id") String id);
    //endregion

        //regionFor item image
        @Multipart
        @POST("rest/images/upload_item/api_key/{API_KEY}")
        LiveData<ApiResponse<Image>> uploadItemImage(
                @Path("API_KEY") String API_KEY,
                @Part("item_id") RequestBody itemId,
                @Part("img_desc") RequestBody imgDesc,
                @Part MultipartBody.Part file,
                @Part("img_id") RequestBody id
        );
        //endregion


    //regionFor specification list
    @GET("rest/items/item_specification/api_key/{API_KEY}/item_id/{item_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemSpecs>>> getAllSpecificationByItemId(@Path("API_KEY") String API_KEY, @Path("item_id") String itemId, @Path("limit") String limit, @Path("offset") String offset);
    //endregion


    //regionFor add specification
    @FormUrlEncoded
    @POST("rest/items/add_spec/api_key/{API_KEY}")
    LiveData<ApiResponse<ItemSpecs>> addSpecification (@Path("API_KEY") String API_KEY, @Field("item_id") String itemId, @Field("name") String name, @Field("description") String description, @Field("id") String id);
    //endregion


    //regionFor delete specification
    @FormUrlEncoded
    @POST("rest/items/delete_spec/api_key/{API_KEY}")
    Call<ApiStatus> deleteSpecification(@Path("API_KEY") String API_KEY, @Field("item_id") String itemId, @Field("id") String id);
    //endregion

    //regionFor image in item upload after image upload
    @GET("rest/images/item_image/api_key/{API_KEY}/item_id/{item_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<Image>>> getImagesInItemUploadAfterImageUpload(@Path("API_KEY") String API_KEY, @Path("item_id") String itemId, @Path("limit") String limit, @Path("offset") String offset);
    //endregion

    //regionFor delete image
    @FormUrlEncoded
    @POST("rest/images/delete_image/api_key/{API_KEY}")
    Call<ApiStatus> deleteImage(
            @Path("API_KEY") String API_KEY,
            @Field("item_id") String shopId,
            @Field("img_id") String imgId
    );
    //endregion

    //regionFor delete item
    @FormUrlEncoded
    @POST("rest/items/item_delete/api_key/{API_KEY}")
    Call<ApiStatus> deleteItem(
            @Path("API_KEY") String API_KEY,
            @Field("item_id") String itemId,
            @Field("user_id") String userId
    );
    //endregion

    //regionFor item status
    @GET("rest/items/item_status/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemStatus>>> getItemStatus(
            @Path("API_KEY") String API_KEY,
            @Path("limit") String limit,
            @Path("offset") String offset

    );

    @FormUrlEncoded
    @POST("rest/users/phone_register/api_key/{API_KEY}")
    Call<User> postPhoneLogin(
            @Path("API_KEY") String API_KEY,
            @Field("phone_id") String phoneId,
            @Field("user_name") String userName,
            @Field("user_phone") String userPhone,
            @Field("device_token") String deviceToken
    );
    //endregion

    //region upload item paid history
    @FormUrlEncoded
    @POST("rest/paid_items/add/api_key/{API_KEY}")
    Call<ItemPaidHistory> uploadItemPaidHistory(
            @Path("API_KEY") String API_KEY,
            @Field("item_id") String itemId,
            @Field("amount") String amount,
            @Field("start_date") String startDate,
            @Field("how_many_day") String howManyDay,
            @Field("payment_method") String paymentMethod,
            @Field("payment_method_nonce") String paymentMethodNonce,
            @Field("start_timestamp") String startTimeStamp);

    // endregion


    //region get paid ad

    @GET("rest/paid_items/get/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}")
    LiveData<ApiResponse<List<ItemPaidHistory>>> getPaidAd(
            @Path("API_KEY") String apiKey,
            @Path("login_user_id") String login_user_id,
            @Path("limit") String limit,
            @Path("offset") String offset
    );

    //endregion


    //region Paypal

    @GET("rest/paypal/get_token/api_key/{API_KEY}")
    Call<ApiStatus> getPaypalToken(@Path("API_KEY") String apiKey);

    //endregion
}