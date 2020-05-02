package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class Comment {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("city_id")
    public final String cityId;

    @SerializedName("item_id")
    public final String itemId;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("header_comment")
    public final String headerComment;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("comment_reply_count")
    public final String commentReplyCount;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @Embedded(prefix = "user_")
    @SerializedName("user")
    public final User user;

    public Comment(@NonNull String id, String cityId, String itemId, String userId, String headerComment, String status, String addedDate, String updatedDate, String commentReplyCount, String addedDateStr, User user) {
        this.id = id;
        this.cityId = cityId;
        this.itemId = itemId;
        this.userId = userId;
        this.headerComment = headerComment;
        this.status = status;
        this.addedDate = addedDate;
        this.updatedDate = updatedDate;
        this.commentReplyCount = commentReplyCount;
        this.addedDateStr = addedDateStr;
        this.user = user;
    }
}
