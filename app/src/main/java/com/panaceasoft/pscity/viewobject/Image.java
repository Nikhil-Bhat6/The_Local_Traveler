package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Panacea-Soft on 11/25/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "imgId")
public class Image {

    @SerializedName("img_id")
    @NonNull
    public  String imgId;

    @SerializedName("img_parent_id")
    public  String imgParentId;

    @SerializedName("img_type")
    public  String imgType;

    @SerializedName("img_path")
    public  String imgPath;

    @SerializedName("img_width")
    public  String imgWidth;

    @SerializedName("img_height")
    public  String imgHeight;

    @SerializedName("img_desc")
    public  String imgDesc;

    public Image(@NonNull String imgId, String imgParentId, String imgType, String imgPath, String imgWidth, String imgHeight, String imgDesc) {
        this.imgId = imgId;
        this.imgParentId = imgParentId;
        this.imgType = imgType;
        this.imgPath = imgPath;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.imgDesc = imgDesc;

    }
}
