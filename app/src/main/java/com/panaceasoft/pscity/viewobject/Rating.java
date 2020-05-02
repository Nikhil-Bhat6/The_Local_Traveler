package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Rating {
    @SerializedName("id")
    @NonNull
    @PrimaryKey
    public final String id;

    @SerializedName("city_id")
    public final String cityId;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("item_id")
    public final String itemId;

    @SerializedName("rating")
    public final String rating;

    @SerializedName("title")
    public final String title;

    @SerializedName("description")
    public final String description;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @SerializedName("user")
    @Embedded(prefix = "user")
    public final User user;

    public Rating(@NonNull String id, String cityId, String userId, String itemId, String rating, String title, String description, String addedDate, String addedDateStr, User user) {
        this.id = id;
        this.cityId = cityId;
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.addedDate = addedDate;
        this.addedDateStr = addedDateStr;
        this.user = user;
    }
}
