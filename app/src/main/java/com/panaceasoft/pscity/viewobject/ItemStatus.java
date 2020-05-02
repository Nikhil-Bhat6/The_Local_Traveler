package com.panaceasoft.pscity.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class ItemStatus {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("added_date")
    public String addedDate;

    public  ItemStatus(@NonNull String id, String title, String addedDate){
        this.id = id;
        this.title = title;
        this.addedDate = addedDate;
    }
}
