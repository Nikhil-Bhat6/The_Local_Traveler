package com.panaceasoft.pscity.viewobject;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(primaryKeys = "id")
public class PSAppInfo {

    @NonNull
    public String id;

    @Embedded(prefix = "version_")
    @SerializedName("version")
    public PSAppVersion psAppVersion;

    @SerializedName("delete_history")
    @Ignore
    public List<DeletedObject> deletedObjects;

    @SerializedName("oneday")
    public String oneDay;

    @SerializedName("currency_symbol")
    public String currencySymbol;

    @SerializedName("currency_short_form")
    public String currencyShortForm;

    @SerializedName("stripe_publishable_key")
    public String stripePublishableKey;

    public PSAppInfo(@NonNull String id, PSAppVersion psAppVersion, String oneDay, String currencySymbol, String currencyShortForm, String stripePublishableKey) {
        this.id = id;
        this.psAppVersion = psAppVersion;
        this.oneDay = oneDay;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.stripePublishableKey = stripePublishableKey;
    }
}
