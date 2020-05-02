package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "userId")
public class User {

    @NonNull
    @SerializedName("user_id")
    public String userId;

    @SerializedName("user_is_sys_admin")
    public String userIsSysAdmin;

    @SerializedName("is_city_admin")
    public String isCityAdmin;

    @SerializedName("facebook_id")
    public String facebookId;

    @SerializedName("google_id")
    public String googleId;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_email")
    public String userEmail;

    @SerializedName("user_phone")
    public String userPhone;

    @SerializedName("user_password")
    public String userPassword;

    @SerializedName("user_about_me")
    public String userAboutMe;

    @SerializedName("user_cover_photo")
    public String userCoverPhoto;

    @SerializedName("user_profile_photo")
    public String userProfilePhoto;

    @SerializedName("role_id")
    public String roleId;

    @SerializedName("status")
    public String status;

    @SerializedName("is_banned")
    public String isBanned;

    @SerializedName("added_date")
    public String addedDate;

    @SerializedName("device_token")
    public String deviceToken;

    public User(@NonNull String userId, String userIsSysAdmin, String isCityAdmin, String facebookId, String googleId, String userName, String userEmail, String userPhone, String userPassword, String userAboutMe, String userCoverPhoto, String userProfilePhoto, String roleId, String status, String isBanned, String addedDate, String deviceToken) {
        this.userId = userId;
        this.userIsSysAdmin = userIsSysAdmin;
        this.isCityAdmin = isCityAdmin;
        this.facebookId = facebookId;
        this.googleId = googleId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userAboutMe = userAboutMe;
        this.userCoverPhoto = userCoverPhoto;
        this.userProfilePhoto = userProfilePhoto;
        this.roleId = roleId;
        this.status = status;
        this.isBanned = isBanned;
        this.addedDate = addedDate;
        this.deviceToken = deviceToken;
    }
}
