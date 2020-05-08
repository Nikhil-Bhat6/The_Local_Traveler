package com.panaceasoft.pscity.ui.item.upload;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentItemUploadBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.image.ImageViewModel;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewobject.Image;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ItemUploadFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface, OnMapReadyCallback {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    private ItemViewModel itemViewModel;
    private ImageViewModel imageViewModel;
    private PSDialogMsg psDialogMsg;
    public String itemName;
    private GoogleMap map;
    private Marker marker;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemUploadBinding> binding;
    private AutoClearedValue<ProgressDialog> progressDialog;
    private AutoClearedValue<ItemEntryImageAdapter> itemEntryImageAdapter;

    private Calendar dateTime = Calendar.getInstance();
    //endregion

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.get().mapView.onResume();
        binding.get().mapView.getMapAsync(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentItemUploadBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_upload, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().mapView.onCreate(savedInstanceState);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        binding.get().itemImageRecyclerView.setNestedScrollingEnabled(false);

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // for alert box
        progressDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        progressDialog.get().setMessage(getString(R.string.message__please_wait));
        progressDialog.get().setCancelable(false);

        // click save button
        binding.get().saveButton.setOnClickListener(v -> {

            if (checkNameTheSame()) {
                if (checkCondition()) {
                    saveItem();
                    progressDialog.get().show();
                }
            } else {
                psDialogMsg.showErrorDialog(getString(R.string.item_upload__already_saved), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });


        binding.get().mapViewButton.setOnClickListener(v -> navigationController.navigateToLists(getActivity(), Constants.MAP, itemViewModel.lat, itemViewModel.lng));


        // for category
        binding.get().categoryTextView.setOnClickListener(v -> navigationController.navigateToExpandActivity(getActivity(), Constants.SELECT_CATEGORY, itemViewModel.catSelectId, ""));

        // region for sub_category
        binding.get().subCatTextView.setOnClickListener(v -> {
            if (itemViewModel.catSelectId.isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_category), getString(R.string.app__ok));
                psDialogMsg.show();
            } else {
                navigationController.navigateToExpandActivity(getActivity(), Constants.SELECT_SUBCATEGORY, itemViewModel.subCatSelectId, itemViewModel.catSelectId);
            }
        });

        //region for status
        binding.get().statusTextView.setOnClickListener(v -> navigationController.navigateToExpandActivity(getActivity(), Constants.SELECT_STATUS, itemViewModel.statusSelectId, ""));

        // for openTime
        binding.get().openTimeTextView.setOnClickListener(v -> openTimePicker(binding.get().openTimeTextView));

        // for closeTime
        binding.get().closeTimeTextView.setOnClickListener(v -> openTimePicker(binding.get().closeTimeTextView));

        // for latitude
        binding.get().latitudeTextView.setText(selectedCityLat);

        // for longitude
        binding.get().longitudeTextView.setText(selectedCityLng);

        binding.get().attributeHeaderButton.setOnClickListener(v -> navigationController.navigateToSpecificationListActivity(getActivity(), itemViewModel.itemSelectId, itemName));

        binding.get().uploadImageButton.setOnClickListener(v -> navigationController.navigateToImageUploadActivity(getActivity(), itemViewModel.img_id, itemViewModel.img_path,
                itemViewModel.img_desc, Constants.IMAGE_UPLOAD_ITEM, itemViewModel.img_id));

        binding.get().uploadImageButton.setOnClickListener(v -> navigationController.navigateToImageUploadActivity(getActivity(), "", "",
                "", Constants.IMAGE_UPLOAD_ITEM, itemViewModel.itemSelectId));

        binding.get().viewAllTextView.setOnClickListener(v -> navigationController.navigateToImageList(getActivity(), itemViewModel.itemSelectId));
    }

    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        imageViewModel = new ViewModelProvider(this, viewModelFactory).get(ImageViewModel.class);

    }

    @Override
    protected void initAdapters() {
        ItemEntryImageAdapter nvAdapter = new ItemEntryImageAdapter(dataBindingComponent, new ItemEntryImageAdapter.ItemImageViewClickCallback() {
            @Override
            public void onClick(Image image) {
                navigationController.navigateToImageUploadActivity(getActivity(), image.imgId, image.imgPath, image.imgDesc, Constants.IMAGE_UPLOAD_ITEM, itemViewModel.itemSelectId);

            }

            @Override
            public void onDeleteClick(Image deleteImage) {
                imageViewModel.setDeleteImageObj(itemViewModel.itemSelectId, deleteImage.imgId);

            }
        }, this);

        itemEntryImageAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().itemImageRecyclerView.setAdapter(itemEntryImageAdapter.get());
    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemViewModel.itemSelectId = getActivity().getIntent().getStringExtra(Constants.ITEM_ID);
                    itemName = getActivity().getIntent().getStringExtra(Constants.ITEM_NAME);

                    if (!itemName.isEmpty()) {
                        itemViewModel.edit_mode = true;
                    }

                    editMode(itemViewModel.itemSelectId);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        //region Getting ImageList
        imageViewModel.getImageListByIdLiveData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case LOADING:
                        if (result.data != null) {
                            replaceImages(result.data);
                        }
                    case SUCCESS:
                        if (result.data != null) {
                            replaceImages(result.data);
                        }
                        imageViewModel.setLoadingState(false);
                        break;

                    case ERROR:
                        imageViewModel.setLoadingState(false);
                        break;

                }
            }
        });
        //endregion

        //region image delete
        imageViewModel.getDeleteImageData().observe(this, result -> {

            if (result != null) {

                switch (result.status) {
                    case SUCCESS:
                        Toast.makeText(ItemUploadFragment.this.getContext(), "Succeed", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(ItemUploadFragment.this.getActivity(), result.message, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        //endregion


        //Getting One Item

        itemViewModel.getItemDetailData().observe(this, result -> {
            if (result != null) {
                switch (result.status) {
                    case LOADING:

                        if (result.data != null) {
                            binding.get().itemNameEditText.setText(result.data.name);
                            binding.get().categoryTextView.setText(result.data.category.name);
                            binding.get().subCatTextView.setText(result.data.subCategory.name);
                            binding.get().itemDescriptionTextView.setText(result.data.description);
                            binding.get().searchTagEditText.setText(result.data.searchTag);
                            binding.get().itemHighlightInformationTextView.setText(result.data.highlightInformation);
                            binding.get().latitudeTextView.setText(result.data.lat);
                            binding.get().longitudeTextView.setText(result.data.lng);
                            binding.get().openTimeTextView.setText(result.data.openingHour);
                            binding.get().closeTimeTextView.setText(result.data.closingHour);
//                            binding.get().phoneOneTextView.setText(result.data.phone1);
//                            binding.get().phoneTwoTextView.setText(result.data.phone2);
                            binding.get().phoneThreeTextView.setText(result.data.phone3);
                            binding.get().emailTextView.setText(result.data.email);
                            binding.get().addressTextView.setText(result.data.address);
//                            binding.get().facebookTextView.setText(result.data.facebook);
//                            binding.get().googlePlusTextView.setText(result.data.google_plus);
//                            binding.get().twitterTextView.setText(result.data.twitter);
//                            binding.get().youtubeTextView.setText(result.data.youtube);
//                            binding.get().instagrmTextView.setText(result.data.instagram);
//                            binding.get().pinterestTextView.setText(result.data.pinterest);
//                            binding.get().websiteTextView.setText(result.data.website);
//                            binding.get().whatappsTextView.setText(result.data.whatsapp);
//                            binding.get().messangerTextView2.setText(result.data.messenger);
                            binding.get().timeRemarkTextView.setText(result.data.time_remark);
                            binding.get().termsAndConditionTextView.setText(result.data.terms);
//                            binding.get().cancelationTextView.setText(result.data.cancelation_policy);
//                            binding.get().additionalTextView.setText(result.data.additional_info);
                            binding.get().statusTextView.setText(result.data.itemStatusId);

                            itemViewModel.savedItemName = result.data.name;
                            itemViewModel.savedCategoryName = result.data.category.name;
                            itemViewModel.savedSubCategoryName = result.data.subCategory.name;
                            itemViewModel.savedDescription = result.data.description;
                            itemViewModel.savedSearchTag = result.data.searchTag;
                            itemViewModel.savedHighLightInformation = result.data.highlightInformation;
                            itemViewModel.lat = result.data.lat;
                            itemViewModel.lng = result.data.lng;
                            itemViewModel.savedOpeningHour = result.data.openingHour;
                            itemViewModel.savedClosingHour = result.data.closingHour;
                            itemViewModel.savedPhoneOne = result.data.phone1;
                            itemViewModel.savedPhoneTwo = result.data.phone2;
                            itemViewModel.savedPhoneThree = result.data.phone3;
                            itemViewModel.savedEmail = result.data.email;
                            itemViewModel.savedAddress = result.data.address;
                            itemViewModel.savedFacebook = result.data.facebook;
                            itemViewModel.savedGooglePlus = result.data.google_plus;
                            itemViewModel.savedTwitter = result.data.twitter;
                            itemViewModel.savedYoutube = result.data.youtube;
                            itemViewModel.savedInstagram = result.data.instagram;
                            itemViewModel.savedPinterest = result.data.pinterest;
                            itemViewModel.savedWebsite = result.data.website;
                            itemViewModel.savedWhatsapp = result.data.whatsapp;
                            itemViewModel.savedMessenger = result.data.messenger;
                            itemViewModel.savedTimeRemark = result.data.time_remark;
                            itemViewModel.savedTerms = result.data.terms;
                            itemViewModel.savedCancelationPolicy = result.data.cancelation_policy;
                            itemViewModel.savedAdditionalInfo = result.data.additional_info;


                            if (result.data.isFeatured.equals("1")) {
                                binding.get().isFeature.setChecked(true);
                                itemViewModel.savedIsFeatured = true;
                            } else if (result.data.isFeatured.equals("0")) {
                                binding.get().isFeature.setChecked(false);
                                itemViewModel.savedIsFeatured = false;
                            }

                            if (result.data.isPromotion.equals("1")) {
                                binding.get().isPromotion.setChecked(true);
                                itemViewModel.savedIsPromotion = true;
                            } else if (result.data.isPromotion.equals("0")) {
                                binding.get().isPromotion.setChecked(false);
                                itemViewModel.savedIsPromotion = false;
                            }

                            switch (result.data.itemStatusId) {
                                case "1":
                                    binding.get().statusTextView.setText(Constants.CHECKED_PUBLISH);
                                    itemViewModel.savedStatusSelectedId = Constants.CHECKED_PUBLISH;
                                    break;
                                case "0":
                                    binding.get().statusTextView.setText(Constants.CHECKED_UNPUBLISH);
                                    itemViewModel.savedStatusSelectedId = Constants.CHECKED_UNPUBLISH;
                                    break;
                                case "2":
                                case "3":
                                    pendingOrRejectItem(result.data.itemStatusId);
                                    binding.get().statusTextView.setText("");
//                                    itemViewModel.savedStatusSelectedId = result.data.itemStatusId;
                                    break;
                            }
                            changeCamera();
                        }
                        break;

                    case SUCCESS:
                        if (result.data != null) {
                            binding.get().itemNameEditText.setText(result.data.name);
                            binding.get().categoryTextView.setText(result.data.category.name);
                            binding.get().subCatTextView.setText(result.data.subCategory.name);
                            binding.get().itemDescriptionTextView.setText(result.data.description);
                            binding.get().searchTagEditText.setText(result.data.searchTag);
                            binding.get().itemHighlightInformationTextView.setText(result.data.highlightInformation);
                            binding.get().latitudeTextView.setText(result.data.lat);
                            binding.get().longitudeTextView.setText(result.data.lng);
                            binding.get().openTimeTextView.setText(result.data.openingHour);
                            binding.get().closeTimeTextView.setText(result.data.closingHour);
//                            binding.get().phoneOneTextView.setText(result.data.phone1);
//                            binding.get().phoneTwoTextView.setText(result.data.phone2);
                            binding.get().phoneThreeTextView.setText(result.data.phone3);
                            binding.get().emailTextView.setText(result.data.email);
                            binding.get().addressTextView.setText(result.data.address);
//                            binding.get().facebookTextView.setText(result.data.facebook);
//                            binding.get().googlePlusTextView.setText(result.data.google_plus);
//                            binding.get().twitterTextView.setText(result.data.twitter);
//                            binding.get().youtubeTextView.setText(result.data.youtube);
//                            binding.get().instagrmTextView.setText(result.data.instagram);
//                            binding.get().pinterestTextView.setText(result.data.pinterest);
//                            binding.get().websiteTextView.setText(result.data.website);
//                            binding.get().whatappsTextView.setText(result.data.whatsapp);
//                            binding.get().messangerTextView2.setText(result.data.messenger);
                            binding.get().timeRemarkTextView.setText(result.data.time_remark);
                            binding.get().termsAndConditionTextView.setText(result.data.terms);
//                            binding.get().cancelationTextView.setText(result.data.cancelation_policy);
//                            binding.get().additionalTextView.setText(result.data.additional_info);

                            if (result.data.itemStatusId.equals("1")) {
                                binding.get().statusTextView.setText(Constants.CHECKED_PUBLISH);

                            } else if (result.data.itemStatusId.equals("0")) {
                                binding.get().statusTextView.setText(Constants.CHECKED_UNPUBLISH);

                            }
                            itemViewModel.catSelectId = result.data.catId;
                            itemViewModel.subCatSelectId = result.data.subCatId;
                            itemViewModel.img_desc = result.data.defaultPhoto.imgDesc;
                            itemViewModel.img_id = result.data.defaultPhoto.imgId;
                            itemViewModel.img_path = result.data.defaultPhoto.imgPath;

                            itemViewModel.savedItemName = result.data.name;
                            itemViewModel.savedCategoryName = result.data.category.name;
                            itemViewModel.savedSubCategoryName = result.data.subCategory.name;
                            itemViewModel.savedDescription = result.data.description;
                            itemViewModel.savedSearchTag = result.data.searchTag;
                            itemViewModel.savedHighLightInformation = result.data.highlightInformation;
                            itemViewModel.lat = result.data.lat;
                            itemViewModel.lng = result.data.lng;
                            itemViewModel.savedOpeningHour = result.data.openingHour;
                            itemViewModel.savedClosingHour = result.data.closingHour;
                            itemViewModel.savedPhoneOne = result.data.phone1;
                            itemViewModel.savedPhoneTwo = result.data.phone2;
                            itemViewModel.savedPhoneThree = result.data.phone3;
                            itemViewModel.savedEmail = result.data.email;
                            itemViewModel.savedAddress = result.data.address;
                            itemViewModel.savedFacebook = result.data.facebook;
                            itemViewModel.savedGooglePlus = result.data.google_plus;
                            itemViewModel.savedTwitter = result.data.twitter;
                            itemViewModel.savedYoutube = result.data.youtube;
                            itemViewModel.savedInstagram = result.data.instagram;
                            itemViewModel.savedPinterest = result.data.pinterest;
                            itemViewModel.savedWebsite = result.data.website;
                            itemViewModel.savedWhatsapp = result.data.whatsapp;
                            itemViewModel.savedMessenger = result.data.messenger;
                            itemViewModel.savedTimeRemark = result.data.time_remark;
                            itemViewModel.savedTerms = result.data.terms;
                            itemViewModel.savedCancelationPolicy = result.data.cancelation_policy;
                            itemViewModel.savedAdditionalInfo = result.data.additional_info;

                            if (result.data.isFeatured.equals("1")) {
                                binding.get().isFeature.setChecked(true);
                                itemViewModel.savedIsFeatured = true;
                            } else if (result.data.isFeatured.equals("0")) {
                                binding.get().isFeature.setChecked(false);
                                itemViewModel.savedIsFeatured = false;
                            }

                            if (result.data.isPromotion.equals("1")) {
                                binding.get().isPromotion.setChecked(true);
                                itemViewModel.savedIsPromotion = true;
                            } else if (result.data.isPromotion.equals("0")) {
                                binding.get().isPromotion.setChecked(false);
                                itemViewModel.savedIsPromotion = false;
                            }

                            switch (result.data.itemStatusId) {
                                case "1":
                                    binding.get().statusTextView.setText(Constants.CHECKED_PUBLISH);
                                    itemViewModel.savedStatusSelectedId = Constants.CHECKED_PUBLISH;
                                    break;
                                case "0":
                                    binding.get().statusTextView.setText(Constants.CHECKED_UNPUBLISH);
                                    itemViewModel.savedStatusSelectedId = Constants.CHECKED_UNPUBLISH;
                                    break;
                                case "2":
                                case "3":
                                    pendingOrRejectItem(result.data.itemStatusId);
//                                itemViewModel.savedStatusSelectedId = result.data.itemStatusId;
                                    break;
                            }
                            changeCamera();

                            itemViewModel.setLoadingState(false);
                        }
                        break;

                    case ERROR:
                        itemViewModel.setLoadingState(false);
                        break;
                }
            }
        });


        //for saveItem
        progressDialog.get().cancel();

        PSDialogMsg psDialogMsgError = new PSDialogMsg(getActivity(), false);

        PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showSuccessDialog(getString(R.string.item_upload__item_saved), getString(R.string.app__ok));

        itemViewModel.getSaveOneItemData().observe(this, itemResource -> {

            if (itemResource != null) {

                switch (itemResource.status) {
                    case LOADING:
                        break;

                    case ERROR:
                        progressDialog.get().cancel();

                        psDialogMsgError.showErrorDialog(itemResource.message, getString(R.string.app__ok));
                        psDialogMsgError.show();

                        psDialogMsgError.okButton.setOnClickListener(v -> psDialogMsgError.cancel());
                        break;

                    case SUCCESS:

                        itemViewModel.savedItemName = binding.get().itemNameEditText.getText().toString();
                        itemViewModel.savedCategoryName = binding.get().categoryTextView.getText().toString();
                        itemViewModel.savedSubCategoryName = binding.get().subCatTextView.getText().toString();
                        itemViewModel.savedDescription = binding.get().itemDescriptionTextView.getText().toString();
                        itemViewModel.savedSearchTag = binding.get().searchTagEditText.getText().toString();
                        itemViewModel.savedHighLightInformation = binding.get().itemHighlightInformationTextView.getText().toString();
                        itemViewModel.savedIsFeatured = binding.get().isFeature.isChecked();
                        itemViewModel.savedLatitude = binding.get().latitudeTextView.getText().toString();
                        itemViewModel.savedLongitude = binding.get().longitudeTextView.getText().toString();
                        itemViewModel.savedOpeningHour = binding.get().openTimeTextView.getText().toString();
                        itemViewModel.savedClosingHour = binding.get().closeTimeTextView.getText().toString();
                        itemViewModel.savedIsPromotion = binding.get().isPromotion.isChecked();
//                        itemViewModel.savedPhoneOne = binding.get().phoneOneTextView.getText().toString();
//                        itemViewModel.savedPhoneTwo = binding.get().phoneTwoTextView.getText().toString();
                        itemViewModel.savedPhoneThree = binding.get().phoneThreeTextView.getText().toString();
                        itemViewModel.savedEmail = binding.get().emailTextView.getText().toString();
                        itemViewModel.savedAddress = binding.get().addressTextView.getText().toString();
//                        itemViewModel.savedFacebook = binding.get().facebookTextView.getText().toString();
//                        itemViewModel.savedGooglePlus = binding.get().googlePlusTextView.getText().toString();
//                        itemViewModel.savedTwitter = binding.get().twitterTextView.getText().toString();
//                        itemViewModel.savedYoutube = binding.get().youtubeTextView.getText().toString();
//                        itemViewModel.savedInstagram = binding.get().instagrmTextView.getText().toString();
//                        itemViewModel.savedPinterest = binding.get().pinterestTextView.getText().toString();
//                        itemViewModel.savedWebsite = binding.get().websiteTextView.getText().toString();
//                        itemViewModel.savedWhatsapp = binding.get().whatappsTextView.getText().toString();
//                        itemViewModel.savedMessenger = binding.get().messangerTextView2.getText().toString();
                        itemViewModel.savedTimeRemark = binding.get().timeRemarkTextView.getText().toString();
                        itemViewModel.savedTerms = binding.get().termsAndConditionTextView.getText().toString();
//                        itemViewModel.savedCancelationPolicy = binding.get().cancelationTextView.getText().toString();
//                        itemViewModel.savedAdditionalInfo = binding.get().additionalTextView.getText().toString();
                        itemViewModel.savedStatusSelectedId = binding.get().statusTextView.getText().toString();

                        itemViewModel.saved = true;

                        progressDialog.get().cancel();

                        if (!psDialogMsg.isShowing()) {
                            psDialogMsg.show();
                        }

                        psDialogMsg.okButton.setOnClickListener(v -> {
                            psDialogMsg.cancel();

                            if (!itemViewModel.edit_mode) {

                                if (itemResource.data != null) {
                                    itemViewModel.itemSelectId = itemResource.data.id;
                                    navigationController.navigateToImageUploadActivity(getActivity(), "", "", "", Constants.IMAGE_UPLOAD_ITEM, itemViewModel.itemSelectId);
                                }
                            }
                        });
                        break;
                }
            }
        });

        //Save Item
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.psLog("Request code " + requestCode);
        Utils.psLog("Result code " + resultCode);

        if (requestCode == 1) {
            if (resultCode == Constants.SELECT_CATEGORY) {
                itemViewModel.catSelectId = data.getStringExtra(Constants.CATEGORY_ID);
                binding.get().categoryTextView.setText(data.getStringExtra(Constants.CATEGORY_NAME));

            } else if (resultCode == Constants.SELECT_SUBCATEGORY) {
                itemViewModel.subCatSelectId = data.getStringExtra(Constants.SUBCATEGORY_ID);
                binding.get().subCatTextView.setText(data.getStringExtra(Constants.SUBCATEGORY_NAME));
            } else if (resultCode == Constants.SELECT_STATUS) {
                itemViewModel.statusSelectId = data.getStringExtra(Constants.STATUS_ID);
                binding.get().statusTextView.setText(data.getStringExtra(Constants.STATUS_NAME));
            }
        } else if (requestCode == Constants.RESULT_GO_TO_IMAGE_UPLOAD && resultCode == Constants.RESULT_CODE_FROM_IMAGE_UPLOAD) {
            itemViewModel.img_id = data.getStringExtra(Constants.IMG_ID);
            editMode(itemViewModel.itemSelectId);
            itemViewModel.edit_mode = true;

        } else if (requestCode == Constants.REQUEST_CODE_TO_MAP_VIEW && resultCode == Constants.RESULT_CODE_FROM_MAP_VIEW) {
            itemViewModel.lat = data.getStringExtra(Constants.LAT);
            itemViewModel.lng = data.getStringExtra(Constants.LNG);

            changeCamera();

            binding.get().latitudeTextView.setText(itemViewModel.lat);
            binding.get().longitudeTextView.setText(itemViewModel.lng);
        }


    }

    private void saveItem() {


        String checkedPromotion;
        if (binding.get().isPromotion.isChecked()) {
            checkedPromotion = Constants.CHECKED_PROMOTION;
        } else {
            checkedPromotion = Constants.NOT_CHECKED_PROMOTION;
        }

        String checkedFeatured;
        if (binding.get().isFeature.isChecked()) {
            checkedFeatured = Constants.CHECKED_FEATURED;
        } else {
            checkedFeatured = Constants.NOT_CHECKED_FEATURED;
        }

        String publishOrUnpublish;
        if (binding.get().statusTextView.getText().toString().equals(Constants.CHECKED_UNPUBLISH)) {
            publishOrUnpublish = Constants.UNPUBLISH;
            binding.get().statusTextView.setText(publishOrUnpublish);
        } else {
            publishOrUnpublish = Constants.PUBLISH;
            binding.get().statusTextView.setText(publishOrUnpublish);
        }
        itemViewModel.setSaveOneItemObj(
                loginUserId,
                selectedCityId,
                itemViewModel.catSelectId,
                itemViewModel.subCatSelectId,
                publishOrUnpublish,
                binding.get().itemNameEditText.getText().toString(),
                binding.get().itemDescriptionTextView.getText().toString(),
                binding.get().searchTagEditText.getText().toString(),
                binding.get().itemHighlightInformationTextView.getText().toString(),
                checkedFeatured,
                binding.get().latitudeTextView.getText().toString(),
                binding.get().longitudeTextView.getText().toString(),
                binding.get().openTimeTextView.getText().toString(),
                binding.get().closeTimeTextView.getText().toString(),
                checkedPromotion,
//                binding.get().phoneOneTextView.getText().toString(),
//                binding.get().phoneTwoTextView.getText().toString(),
                "123",
                "456",
                binding.get().phoneThreeTextView.getText().toString(),
                binding.get().emailTextView.getText().toString(),
                binding.get().addressTextView.getText().toString(),
//                binding.get().facebookTextView.getText().toString(),
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
                "qwerty",
//                binding.get().googlePlusTextView.getText().toString(),
//                binding.get().twitterTextView.getText().toString(),
//                binding.get().youtubeTextView.getText().toString(),
//                binding.get().instagrmTextView.getText().toString(),
//                binding.get().pinterestTextView.getText().toString(),
//                binding.get().websiteTextView.getText().toString(),
//                binding.get().whatappsTextView.getText().toString(),
//                binding.get().messangerTextView2.getText().toString(),
                binding.get().timeRemarkTextView.getText().toString(),
                binding.get().termsAndConditionTextView.getText().toString(),
//                binding.get().cancelationTextView.getText().toString(),
                "qwerty",
                "qwerty",
//                binding.get().additionalTextView.getText().toString(),
                itemViewModel.itemSelectId

        );
    }

    private void openTimePicker(EditText editText) {
        TimePickerDialog.OnTimeSetListener timePickerDialog = (view, hourOfDay, minute) -> {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTime(editText);
        };

        new TimePickerDialog(getContext(), timePickerDialog, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    private void updateTime(EditText editText) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa", Locale.US);
        String shortTimeStr = sdf.format(dateTime.getTime());
        editText.setText(shortTimeStr);
    }

    private boolean checkNameTheSame() {

        Utils.psLog("***** Item Category ViewModel : " + itemViewModel.savedCategoryName);
        Utils.psLog("***** Item SubCategory ViewModel : " + itemViewModel.savedSubCategoryName);
        Utils.psLog("***** Item Category : " + binding.get().categoryTextView.getText().toString());
        Utils.psLog("***** Item SubCategory : " + binding.get().subCatTextView.getText().toString());


        return !binding.get().itemNameEditText.getText().toString().equals(itemViewModel.savedItemName) ||
                !binding.get().itemDescriptionTextView.getText().toString().equals(itemViewModel.savedDescription) ||
                !binding.get().categoryTextView.getText().toString().equals(itemViewModel.savedCategoryName) ||
                !binding.get().subCatTextView.getText().toString().equals(itemViewModel.savedSubCategoryName) ||
                !binding.get().searchTagEditText.getText().toString().equals(itemViewModel.savedSearchTag) ||
                !binding.get().itemHighlightInformationTextView.getText().toString().equals(itemViewModel.savedHighLightInformation) ||
                !binding.get().latitudeTextView.getText().toString().equals(itemViewModel.savedLatitude) ||
                !binding.get().longitudeTextView.getText().toString().equals(itemViewModel.savedLongitude) ||
                !binding.get().openTimeTextView.getText().toString().equals(itemViewModel.savedOpeningHour) ||
                !binding.get().closeTimeTextView.getText().toString().equals(itemViewModel.savedClosingHour) ||
//                !binding.get().phoneOneTextView.getText().toString().equals(itemViewModel.savedPhoneOne) ||
//                !binding.get().phoneTwoTextView.getText().toString().equals(itemViewModel.savedPhoneTwo) ||
                !binding.get().phoneThreeTextView.getText().toString().equals(itemViewModel.savedPhoneThree) ||
                !binding.get().emailTextView.getText().toString().equals(itemViewModel.savedEmail) ||
                !binding.get().addressTextView.getText().toString().equals(itemViewModel.savedAddress) ||
//                !binding.get().facebookTextView.getText().toString().equals(itemViewModel.savedFacebook) ||
//                !binding.get().googlePlusTextView.getText().toString().equals(itemViewModel.savedGooglePlus) ||
//                !binding.get().twitterTextView.getText().toString().equals(itemViewModel.savedTwitter) ||
//                !binding.get().youtubeTextView.getText().toString().equals(itemViewModel.savedYoutube) ||
//                !binding.get().instagrmTextView.getText().toString().equals(itemViewModel.savedInstagram) ||
//                !binding.get().pinterestTextView.getText().toString().equals(itemViewModel.savedPinterest) ||
//                !binding.get().websiteTextView.getText().toString().equals(itemViewModel.savedWebsite) ||
//                !binding.get().whatappsTextView.getText().toString().equals(itemViewModel.savedWhatsapp) ||
//                !binding.get().messangerTextView2.getText().toString().equals(itemViewModel.savedMessenger) ||
                !binding.get().timeRemarkTextView.getText().toString().equals(itemViewModel.savedTimeRemark) ||
                !binding.get().termsAndConditionTextView.getText().toString().equals(itemViewModel.savedTerms) ||
//                !binding.get().cancelationTextView.getText().toString().equals(itemViewModel.savedCancelationPolicy) ||
//                !binding.get().additionalTextView.getText().toString().equals(itemViewModel.savedAdditionalInfo) ||
                !binding.get().statusTextView.getText().toString().equals(itemViewModel.savedStatusSelectedId) ||
                binding.get().isPromotion.isChecked() != itemViewModel.savedIsPromotion ||
                binding.get().isFeature.isChecked() != itemViewModel.savedIsFeatured;
    }

    private boolean checkCondition() {
        boolean result = true;

        int itemNameLength = binding.get().itemNameEditText.getText().toString().length();

        if (binding.get().itemNameEditText.getText().toString().isEmpty()) {
            PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
            psDialogMsg.showErrorDialog(getString(R.string.item_upload__item_name_required), getString(R.string.app__ok));

            if (!psDialogMsg.isShowing()) {
                psDialogMsg.show();
            }

            psDialogMsg.okButton.setOnClickListener(v -> psDialogMsg.cancel());

            result = false;
        } else if (itemNameLength < 4) {
            PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
            psDialogMsg.showErrorDialog(getString(R.string.item_upload__item_name_length), getString(R.string.app__ok));

            if (!psDialogMsg.isShowing()) {
                psDialogMsg.show();
            }

            psDialogMsg.okButton.setOnClickListener(v -> psDialogMsg.cancel());

            result = false;
        } else if (binding.get().categoryTextView.getText().toString().isEmpty()) {
            PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
            psDialogMsg.showErrorDialog(getString(R.string.item_upload__item_category_required), getString(R.string.app__ok));

            if (!psDialogMsg.isShowing()) {
                psDialogMsg.show();
            }

            psDialogMsg.okButton.setOnClickListener(v -> psDialogMsg.cancel());

            result = false;

        } else if (binding.get().subCatTextView.getText().toString().isEmpty()) {
            PSDialogMsg psDialogMsg = new PSDialogMsg(getActivity(), false);
            psDialogMsg.showErrorDialog(getString(R.string.item_upload__item_sub_category_required), getString(R.string.app__ok));

            if (!psDialogMsg.isShowing()) {
                psDialogMsg.show();
            }

            psDialogMsg.okButton.setOnClickListener(v -> psDialogMsg.cancel());

            result = false;
        }


        return result;
    }

    private void editMode(String itemIdToGet) {
        binding.get().imageTextView.setVisibility(View.VISIBLE);
        binding.get().viewAllTextView.setVisibility(View.VISIBLE);
        binding.get().itemImageRecyclerView.setVisibility(View.VISIBLE);
        binding.get().attributeHeaderButton.setVisibility(View.VISIBLE);
        binding.get().uploadImageButton.setVisibility(View.VISIBLE);

        itemViewModel.setItemDetailObj(itemIdToGet, itemViewModel.historyFlag, loginUserId);
        getImage(itemIdToGet);
    }

    private void getImage(String itemId) {
        imageViewModel.setImageListByIdObj(itemId, Constants.IMAGE_COUNT_ENTRY2, String.valueOf(imageViewModel.offset), Constants.IMAGE_COUNT_ENTRY2);

    }



    private void pendingOrRejectItem(String itemStatusId) {
        binding.get().rejectOrPendingTextView.setVisibility(View.VISIBLE);
        binding.get().saveButton.setEnabled(false);
        binding.get().attributeHeaderButton.setEnabled(false);
        binding.get().uploadImageButton.setEnabled(false);
        binding.get().viewAllTextView.setEnabled(false);
        binding.get().saveButton.setVisibility(View.GONE);

        if (itemStatusId.equals("2")) {
            binding.get().rejectOrPendingTextView.setText(getString(R.string.item_upload__pending));
            binding.get().rejectOrPendingTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_pending_transparent_shape));
        }
        if (itemStatusId.equals("3")) {
            binding.get().rejectOrPendingTextView.setText(getString(R.string.item_upload__reject));
            binding.get().rejectOrPendingTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_reject_transparent_shape));
        }

    }

    private void replaceImages(List<Image> imageList) {
        if (imageList.size() == 0) {
            binding.get().noImagesTextView.setVisibility(View.VISIBLE);
            itemEntryImageAdapter.get().replace(imageList);
            binding.get().executePendingBindings();
        } else {
            binding.get().noImagesTextView.setVisibility(View.GONE);
            itemEntryImageAdapter.get().replace(imageList);
            binding.get().executePendingBindings();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onDestroyView() {

        binding.get().mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        binding.get().mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.get().mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        Resource<List<Image>> resource = imageViewModel.getImageListByIdLiveData().getValue();

        if(resource != null) {
            List<Image> dataList = resource.data;

            if(dataList != null && dataList.size() == 0) {
                Utils.psLog("First Record Reload.");
                getImage(itemViewModel.itemSelectId);
            }else {
                Utils.psLog("Not First Record Reload.");
            }
        }

        binding.get().mapView.onResume();
        super.onResume();
    }

    private void changeCamera() {

        if (marker != null) {
            marker.remove();
        }

        try {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.valueOf(itemViewModel.lat), Double.valueOf(itemViewModel.lng))).zoom(10).bearing(10).tilt(10).build()));

            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(itemViewModel.lat), Double.valueOf(itemViewModel.lng)))
                    .title("Shop Name"));
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }
}
