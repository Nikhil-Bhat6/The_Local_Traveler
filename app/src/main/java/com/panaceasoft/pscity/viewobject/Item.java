package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(primaryKeys = "id")
public class Item {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("city_id")
    public String cityId;

    @SerializedName("cat_id")
    public String catId;

    @SerializedName("sub_cat_id")
    public String subCatId;

//    @Embedded(prefix = "item_status_id_")
    @SerializedName("item_status_id")
    public String itemStatusId;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("search_tag")
    public String searchTag;

    @SerializedName("highlight_information")
    public String highlightInformation;

    @SerializedName("is_featured")
    public String isFeatured;

    @SerializedName("is_promotion")
    public String isPromotion;

    @SerializedName("added_date")
    public String addedDate;

    @SerializedName("added_user_id")
    public String addedUserId;

    @SerializedName("updated_date")
    public String updatedDate;

    @SerializedName("updated_user_id")
    public String updatedUserId;

    @SerializedName("updated_flag")
    public String updatedFlag;

    @SerializedName("overall_rating")
    public String overallRating;

    @SerializedName("touch_count")
    public String touchCount;

    @SerializedName("favourite_count")
    public String favouriteCount;

    @SerializedName("like_count")
    public String likeCount;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;

    @SerializedName("opening_hour")
    public String openingHour;

    @SerializedName("closing_hour")
    public String closingHour;

    @SerializedName("phone1")
    public String phone1;

    @SerializedName("phone2")
    public String phone2;

    @SerializedName("phone3")
    public String phone3;

    @SerializedName("email")
    public String email;

    @SerializedName("website")
    public String website;

    @SerializedName("facebook")
    public String facebook;

    @SerializedName("google_plus")
    public String google_plus;

    @SerializedName("twitter")
    public String twitter;

    @SerializedName("youtube")
    public String youtube;

    @SerializedName("instagram")
    public String instagram;

    @SerializedName("pinterest")
    public String pinterest;

    @SerializedName("whatsapp")
    public String whatsapp;

    @SerializedName("messenger")
    public String messenger;

    @SerializedName("terms")
    public String terms;

    @SerializedName("cancelation_policy")
    public String cancelation_policy;

    @SerializedName("additional_info")
    public String additional_info;

    @SerializedName("time_remark")
    public String time_remark;

    @SerializedName("address")
    public String address;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @SerializedName("trans_status")
    public String transStatus;

    @Embedded(prefix = "default_photo_")
    @SerializedName("default_photo")
    public Image defaultPhoto;

    @Embedded(prefix = "category_")
    @SerializedName("category")
    public ItemCategory category;

    @Embedded(prefix = "sub_category_")
    @SerializedName("sub_category")
    public ItemSubCategory subCategory;

//    @SerializedName("spec")
//    public List<Object> spec = null;

    @SerializedName("is_liked")
    public String isLiked;

    @SerializedName("is_favourited")
    public String isFavourited;

    @SerializedName("image_count")
    public String imageCount;

    @SerializedName("comment_header_count")
    public String commentHeaderCount;

    @SerializedName("currency_symbol")
    public String currencySymbol;

    @SerializedName("currency_short_form")
    public String currencyShortForm;

    @SerializedName("paid_status")
    public String paidStatus;

    @Embedded(prefix = "city_")
    @SerializedName("city")
    public City city;

    @Embedded(prefix = "rating_details_")
    @SerializedName("rating_details")
    public RatingDetail ratingDetails;

    @Embedded(prefix = "user_")
    @SerializedName("user")
    public User user;

    @SerializedName("specs")
    @Ignore
    public List<ItemSpecs> productSpecsList;

    public Item(@NonNull String id, String cityId, String catId, String subCatId, String itemStatusId, String name, String description, String searchTag, String highlightInformation, String isFeatured, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String overallRating, String touchCount, String favouriteCount, String likeCount, String lat, String lng, String openingHour, String closingHour, String phone1, String phone2, String phone3, String website, String facebook, String google_plus, String twitter, String youtube, String instagram, String pinterest, String terms, String cancelation_policy, String additional_info, String time_remark, String address, String addedDateStr, String transStatus, Image defaultPhoto, ItemCategory category, ItemSubCategory subCategory, String isLiked, String isFavourited, String imageCount, String commentHeaderCount, String currencySymbol, String currencyShortForm, City city, RatingDetail ratingDetails,String messenger,String whatsapp,String email,String isPromotion,User user,String paidStatus) {
        this.id = id;
        this.cityId = cityId;
        this.catId = catId;
        this.subCatId = subCatId;
        this.itemStatusId = itemStatusId;
        this.name = name;
        this.description = description;
        this.searchTag = searchTag;
        this.highlightInformation = highlightInformation;
        this.isFeatured = isFeatured;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.overallRating = overallRating;
        this.touchCount = touchCount;
        this.favouriteCount = favouriteCount;
        this.likeCount = likeCount;
        this.lat = lat;
        this.lng = lng;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.website = website;
        this.facebook = facebook;
        this.google_plus = google_plus;
        this.twitter = twitter;
        this.youtube = youtube;
        this.instagram = instagram;
        this.pinterest = pinterest;
        this.terms = terms;
        this.cancelation_policy = cancelation_policy;
        this.additional_info = additional_info;
        this.time_remark = time_remark;
        this.address = address;
        this.addedDateStr = addedDateStr;
        this.transStatus = transStatus;
        this.defaultPhoto = defaultPhoto;
        this.category = category;
        this.subCategory = subCategory;
        this.isLiked = isLiked;
        this.isFavourited = isFavourited;
        this.imageCount = imageCount;
        this.commentHeaderCount = commentHeaderCount;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.city = city;
        this.ratingDetails = ratingDetails;
        this.messenger = messenger;
        this.whatsapp = whatsapp;
        this.email = email;
        this.isPromotion = isPromotion;
        this.user = user;
        this.paidStatus = paidStatus;
    }
}
