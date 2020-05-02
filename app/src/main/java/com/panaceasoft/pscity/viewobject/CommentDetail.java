package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
@Entity(primaryKeys = "id")

public class CommentDetail {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("header_id")
    public final String headerId;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("detail_comment")
    public final String detailComment;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @Embedded(prefix = "user_")
    @SerializedName("user")
    public final User user;

    public CommentDetail(@NonNull String id,String headerId, String userId, String detailComment, String status, String addedDate, String updatedDate, String addedDateStr, User user) {
        this.id = id;
        this.headerId = headerId;
        this.userId = userId;
        this.detailComment = detailComment;
        this.status = status;
        this.addedDate = addedDate;
        this.updatedDate = updatedDate;
        this.addedDateStr = addedDateStr;
        this.user = user;
    }
}
