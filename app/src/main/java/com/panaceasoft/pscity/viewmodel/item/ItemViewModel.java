package com.panaceasoft.pscity.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.item.ItemRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemViewModel extends PSViewModel {

    private final LiveData<Resource<List<Item>>> itemListByKeyData;
    private final MutableLiveData<ItemViewModel.ItemTmpDataHolder> itemListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Item>> productDetailListData;
    private MutableLiveData<ItemViewModel.TmpDataHolder> productDetailObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageItemListByKeyData;
    private final MutableLiveData<ItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    private LiveData<Resource<Item>> saveOneItemData;
    private MutableLiveData<ItemViewModel.saveOneTmpDataHolder> saveOneItemObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> deleteOneItemData;
    private MutableLiveData<ItemViewModel.deleteOneTmpDataHolder> deleteOneItemObj = new MutableLiveData<>();

    // for image upload
    private MutableLiveData<String> imgObj = new MutableLiveData<>();

    private ItemRepository repository;

    public ItemParameterHolder holder = new ItemParameterHolder();

    public String itemDescription = "";
    public String itemId = "";
    public String cityId = "";
    public String LAT = "";
    public String LNG = "";
    public String historyFlag = "";

    public String catSelectId = "";
    public String statusSelectId = "";
    public String subCatSelectId;
    public String itemSelectId;
    public String savedItemName = "";
    public String savedCategoryName = "";
    public String savedSubCategoryName = "";
    public String savedDescription = "";
    public String savedSearchTag = "";
    public String savedHighLightInformation = "";
    public Boolean savedIsFeatured = false;
    public String savedLatitude = "";
    public String savedLongitude = "";
    public String savedOpeningHour = "";
    public String savedClosingHour = "";
    public Boolean savedIsPromotion = false;
    public String savedPhoneOne = "";
    public String savedPhoneTwo = "";
    public String savedPhoneThree = "";
    public String savedEmail = "";
    public String savedAddress = "";
    public String savedFacebook = "";
    public String savedGooglePlus = "";
    public String savedTwitter = "";
    public String savedYoutube = "";
    public String savedInstagram = "";
    public String savedPinterest = "";
    public String savedWebsite = "";
    public String savedWhatsapp = "";
    public String savedMessenger = "";
    public String savedTimeRemark = "";
    public String savedTerms = "";
    public String savedCancelationPolicy = "";
    public String savedAdditionalInfo = "";
    public String savedStatusSelectedId = "";
    public boolean saved = false;
    public boolean edit_mode = false;
    public String img_id = "";
    public String img_desc = "";
    public String img_path = "";
    public String lat = "48.856452647178386";
    public String lng = "2.3523519560694695";

    @Inject
    ItemViewModel(ItemRepository repository)
    {

        this.repository = repository;
        itemListByKeyData = Transformations.switchMap(itemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);

        });

        nextPageItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });

        //  item detail List
        productDetailListData = Transformations.switchMap(productDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product detail List.");
            return repository.getItemDetail(Config.API_KEY, obj.itemId, obj.historyFlag, obj.userId);
        });

        // item upload
        saveOneItemData = Transformations.switchMap(saveOneItemObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.saveOneItem(obj.userId,obj.cityId,obj.categoryId,obj.subCategoryId,obj.status,obj.name,obj.description,obj.searchTag,obj.highlightInformation,obj.isFeatured,obj.latitude,obj.longitude,
                    obj.openingHour, obj.closingHour,obj.isPromotion,obj.phoneOne,obj.phoneTwo,obj.phoneThree,obj.email,obj.address,obj.facebook,obj.googlePlus,obj.twitter,obj.youtube,obj.instagram,
                    obj.pinterest, obj.website,obj.whatsapp,obj.messenger,obj.timeRemark,obj.terms,obj.cancelationPolicy,obj.additionalInfo,obj.itemId);
        });

        // item delete
        deleteOneItemData = Transformations.switchMap(deleteOneItemObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.deleteOneItem(obj.itemId, obj.userId);
        });
    }

    //region getItemList

    public void setItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {

        ItemTmpDataHolder tmpDataHolder = new ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.itemListByKeyObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Item>>> getItemListByKeyData() {
        return itemListByKeyData;
    }

    public void setNextPageItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

        if(!isLoading)
        {
            ItemTmpDataHolder tmpDataHolder = new ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

            setLoadingState(true);

            this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageItemListByKeyData() {
        return nextPageItemListByKeyData;
    }

    //endregion

    class ItemTmpDataHolder {

        private String limit, offset, loginUserId;
        private ItemParameterHolder itemParameterHolder;

        public ItemTmpDataHolder(String limit, String offset, String loginUserId, ItemParameterHolder itemParameterHolder) {
            this.limit = limit;
            this.offset = offset;
            this.loginUserId = loginUserId;
            this.itemParameterHolder = itemParameterHolder;
        }
    }
    //region Getter And Setter for item detail List

    public void setItemDetailObj(String itemId, String historyFlag, String userId) {
        if (!isLoading) {
            ItemViewModel.TmpDataHolder tmpDataHolder = new ItemViewModel.TmpDataHolder();
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.historyFlag = historyFlag;
            tmpDataHolder.userId = userId;
            productDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Item>> getItemDetailData() {
        return productDetailListData;
    }

    //endregion

    //region getter and setter for item upload
    public void setSaveOneItemObj(String userId, String cityId, String categoryId, String subCategoryId,String status, String name, String description, String searchTag, String highlightInformation,
                                  String isFeatured, String latitude, String longitude, String openHour, String closeHour, String isPromotion, String phoneOne, String phoneTwo, String phoneThree,
                                  String email, String address, String facebook, String googlePlus, String twitter, String youtube, String instagram, String pinterest, String website, String whatesapp,
                                  String messenger, String timeRemark, String terms, String cancelationPolicy, String additionalInfo,String itemId)
    {

        ItemViewModel.saveOneTmpDataHolder tmpDataHolder = new ItemViewModel.saveOneTmpDataHolder(userId,cityId,categoryId,subCategoryId,status,name,description,searchTag,highlightInformation,isFeatured,
                latitude,longitude,openHour,closeHour,isPromotion,phoneOne,phoneTwo,phoneThree,email,address,facebook,googlePlus,twitter,youtube,instagram,pinterest,website,whatesapp,messenger,
                timeRemark,terms,cancelationPolicy,additionalInfo,itemId);

        saveOneItemObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Item>> getSaveOneItemData(){
        return saveOneItemData;
    }

    //endregion

    // Upload Image
    public LiveData<Resource<Item>> uploadImage(String itemId, String imgDesc, String filePath, String imgId) {

        imgObj.setValue("PS");

        return Transformations.switchMap(imgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return ItemViewModel.this.repository.uploadImage(itemId, imgDesc, filePath, imgId);
        });

    }
    //endregion

    //Delete Item
    public void setDeleteOneItemObj(String itemId, String userId)
    {
        ItemViewModel.deleteOneTmpDataHolder tmpDataHolder = new ItemViewModel.deleteOneTmpDataHolder(itemId,userId);

        deleteOneItemObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getDeleteOneItemData()
    {
        return deleteOneItemData;
    }
    //end region


    class saveOneTmpDataHolder {

        String userId,cityId,categoryId,subCategoryId,status,name,description,searchTag,highlightInformation,isFeatured,latitude,longitude,openingHour,closingHour,isPromotion,phoneOne,phoneTwo,phoneThree,
        email,address,facebook,googlePlus,twitter,youtube,instagram,pinterest,website,whatsapp,messenger,timeRemark,terms,cancelationPolicy,additionalInfo,itemId;

        private saveOneTmpDataHolder( String userId, String cityId, String categoryId, String subCategoryId, String status, String name, String description, String searchTag, String highlightInformation,
                                      String isFeatured, String latitude, String longitude, String openHour, String closeHour, String isPromotion, String phoneOne, String phoneTwo, String phoneThree,
                                      String email, String address, String facebook, String googlePlus, String twitter, String youtube, String instagram, String pinterest, String website, String whatesapp,
                                      String messenger, String timeRemark, String terms, String cancelationPolicy, String additionalInfo,String itemId) {
            this.userId = userId;
            this.cityId = cityId;
            this.categoryId = categoryId;
            this.subCategoryId = subCategoryId;
            this.name = name;
            this.description = description;
            this.searchTag = searchTag;
            this.highlightInformation = highlightInformation;
            this.isFeatured = isFeatured;
            this.latitude = latitude;
            this.longitude = longitude;
            this.openingHour = openHour;
            this.closingHour = closeHour;
            this.isPromotion = isPromotion;
            this.phoneOne = phoneOne;
            this.phoneTwo = phoneTwo;
            this.phoneThree = phoneThree;
            this.email = email;
            this.address = address;
            this.facebook = facebook;
            this.googlePlus = googlePlus;
            this.twitter = twitter;
            this.youtube = youtube;
            this.instagram = instagram;
            this.pinterest = pinterest;
            this.website = website;
            this.whatsapp = whatesapp;
            this.messenger = messenger;
            this.timeRemark = timeRemark;
            this.terms = terms;
            this.cancelationPolicy = cancelationPolicy;
            this.additionalInfo = additionalInfo;
            this.itemId = itemId;
            this.status = status;
        }
    }

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String itemId = "";
        public String historyFlag = "";
        public String userId = "";
        public String cityId = "";
        public String categoryId = "";
        public String subCategoryId = "";
        public String name = "";
        public String description = "";
        public String searchTag = "";
        public String highlightInformation = "";
        public String isFeatured = "";
        public String latitude = "";
        public String longtitude = "";
        public String openingHour = "";
        public String closingHour = "";
        public String isPromotion = "";
        public String phoneOne = "";
        public String phoneTwo = "";
        public String phoneThree = "";
        public String email = "";
        public String address = "";
        public String facebook = "";
        public String googlePlus = "";
        public String twitter = "";
        public String youtube = "";
        public String instagram = "";
        public String pinterest = "";
        public String website = "";
        public String whatsapp = "";
        public String messenger = "";
        public String timeRemark = "";
        public String terms = "";
        public String cancelationPolicy = "";
        public String additionalInfo = "";
        public Boolean isConnected = false;
    }
    //endregion

    class getOneTmpDataHolder {
        String userName, password, shopId, id ;

        private getOneTmpDataHolder(String userName, String password, String shopId, String id) {
            this.userName = userName;
            this.password = password;
            this.shopId = shopId;
            this.id = id;
        }
    }

    class deleteOneTmpDataHolder {

        String itemId, userId;

        private deleteOneTmpDataHolder(String itemId, String userId) {
         this.itemId = itemId;
         this.userId = userId;
        }
    }

}
