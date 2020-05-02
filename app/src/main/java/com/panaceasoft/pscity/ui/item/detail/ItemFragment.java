package com.panaceasoft.pscity.ui.item.detail;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentItemBinding;
import com.panaceasoft.pscity.databinding.ItemRatingEntryBinding;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.adapter.ItemSpecsAdapter;
import com.panaceasoft.pscity.ui.item.adapter.ItemTagAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.utils.ViewAnimationUtil;
import com.panaceasoft.pscity.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.pscity.viewmodel.item.ItemViewModel;
import com.panaceasoft.pscity.viewmodel.item.SpecsViewModel;
import com.panaceasoft.pscity.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.pscity.viewmodel.rating.RatingViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemSpecs;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.common.Status;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;
import com.panaceasoft.pscity.viewobject.holder.TabObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private TouchCountViewModel touchCountViewModel;
    private FavouriteViewModel favouriteViewModel;
    private SpecsViewModel specsViewModel;
    private RatingViewModel ratingViewModel;
    private static final int REQUEST_CALL = 1;
    private PSDialogMsg psDialogMsg;
    private ImageView imageView;
    private ItemParameterHolder itemParameterHolder = new ItemParameterHolder();

    private boolean twist = false;
    private PSDialogMsg psDialogRatingMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemBinding> binding;
    private AutoClearedValue<Dialog> dialog;
    private AutoClearedValue<ProgressDialog> prgDialog;
    private AutoClearedValue<ItemRatingEntryBinding> itemRatingEntryBinding;
    private AutoClearedValue<ItemSpecsAdapter> specsAdapter;
    private AutoClearedValue<ItemTagAdapter> tabAdapter;
    private AutoClearedValue<List<TabObject>> tabObjectList;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        List<TabObject> tabObjectList1 = new ArrayList<>();
        tabObjectList = new AutoClearedValue<>(this, tabObjectList1);
        imageView = binding.get().coverUserImageView;


        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        psDialogRatingMsg = new PSDialogMsg(getActivity(), false);

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        hideFloatingButton();

        binding.get().shareImageView.setOnClickListener(v -> {

            Bitmap bitmap = getBitmapFromView(getCurrentImageView());
            shareImageUri(saveImageExternal(bitmap));

        });

        NestedScrollView nestedScrollView = binding.get().nestedScrollView;
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                binding.get().mainFloatingActionButton.hide();

                if (twist) {
                    if (!binding.get().getItem().messenger.isEmpty()) {
                        binding.get().messengerFloatingActionButton.hide();
                    }
                    if (!binding.get().getItem().whatsapp.isEmpty()) {
                        binding.get().whatsappFloatingActionButton.hide();
                    }

                }

            } else {
                binding.get().mainFloatingActionButton.show();
                if (twist) {

                    if (!binding.get().getItem().messenger.isEmpty()) {
                        binding.get().messengerFloatingActionButton.show();
                    }
                    if (!binding.get().getItem().whatsapp.isEmpty()) {
                        binding.get().whatsappFloatingActionButton.show();
                    }


                }
            }
        });

        binding.get().viewOnMapTextView.setOnClickListener(v -> navigationController.navigateToMapActivity(getActivity(), itemViewModel.LNG, itemViewModel.LAT, binding.get().nameTextView.getText().toString()));
        binding.get().favoriteImageView.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    favFunction(item, likeButton);
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    unFavFunction(item, likeButton);
                }
            }
        });

        binding.get().rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {

            getCustomLayoutDialog(rating);

        });

        binding.get().ratingBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                navigationController.navigateToRatingList(ItemFragment.this.getActivity(), itemViewModel.itemId);
            }
            return true;
        });


        binding.get().starTextView.setOnClickListener(v -> {
            navigationController.navigateToRatingList(ItemFragment.this.getActivity(), itemViewModel.itemId);
        });

        binding.get().backImageView.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        //For phone 1
        binding.get().phoneCall1TextView.setOnClickListener(view -> {

            String number1 = binding.get().phone1TextView.getText().toString();

            if (ContextCompat.checkSelfPermission(binding.get().getRoot().getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (this.getActivity() != null) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CALL_PHONE
                    }, REQUEST_CALL);
                }
            } else {
                String dial = Constants.CITY_TEL + number1;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        });

        //For phone 2
        binding.get().phoneCall2TextView.setOnClickListener(view -> {

            String number2 = binding.get().phone2TextView.getText().toString();

            if (ContextCompat.checkSelfPermission(binding.get().getRoot().getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (this.getActivity() != null) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CALL_PHONE
                    }, REQUEST_CALL);
                }
            } else {
                String dial = Constants.CITY_TEL + number2;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        });

        //For phone 3
        binding.get().phoneCall3TextView.setOnClickListener(view -> {

            String number3 = binding.get().phone3TextView.getText().toString();

            if (ContextCompat.checkSelfPermission(binding.get().getRoot().getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (this.getActivity() != null) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CALL_PHONE
                    }, REQUEST_CALL);
                }
            } else {
                String dial = Constants.CITY_TEL + number3;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        });

        //For website
        binding.get().WebsiteTextView.setOnClickListener(view -> {

            if (binding.get().WebsiteTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().WebsiteTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().WebsiteTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For facebook
        binding.get().facebookTextView.setOnClickListener(view -> {

            if (binding.get().facebookTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().facebookTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().facebookTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For google plus
        binding.get().gplusTextView.setOnClickListener(view -> {

            if (binding.get().gplusTextView.getText().toString().startsWith(Constants.HTTP)  || binding.get().gplusTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().gplusTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For twitter
        binding.get().twitterTextView.setOnClickListener(view -> {

            if (binding.get().twitterTextView.getText().toString().startsWith(Constants.HTTP)  || binding.get().twitterTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().twitterTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For instagram
        binding.get().instaTextView.setOnClickListener(view -> {

            if (binding.get().instaTextView.getText().toString().startsWith(Constants.HTTP)  || binding.get().instaTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().instaTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For youtube
        binding.get().youtubeTextView.setOnClickListener(view -> {

            if (binding.get().youtubeTextView.getText().toString().startsWith(Constants.HTTP)  || binding.get().youtubeTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().youtubeTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For pinterest
        binding.get().pinterestTextView.setOnClickListener(view -> {

            if (binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTP)  || binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().pinterestTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        binding.get().statisticDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandStatisticFunction();
            } else {
                collapseStatisticFunction();
            }
        });

        binding.get().statisticTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().statisticDownImageView);
            if (show) {
                expandStatisticFunction();
            } else {
                collapseStatisticFunction();
            }
        });

        binding.get().contactInfoDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandContactInfoFunction();
            } else {
                collapseContactInfoFunction();
            }
        });

        binding.get().contactInfoTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().contactInfoDownImageView);
            if (show) {
                expandContactInfoFunction();
            } else {
                collapseContactInfoFunction();
            }
        });

        binding.get().whatYouGetDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandWhatYouGetFunction();
            } else {
                collapseWhatYouGetFunction();
            }
        });

        binding.get().whatYouGetTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().whatYouGetDownImageView);
            if (show) {
                expandWhatYouGetFunction();
            } else {
                collapseWhatYouGetFunction();
            }
        });

        binding.get().tagDownImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandTabFunction();
            } else {
                collapseTabFunction();
            }
        });

        binding.get().tagTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().tagDownImageView);
            if (show) {
                ItemFragment.this.expandTabFunction();
            } else {
                ItemFragment.this.collapseTabFunction();
            }
        });

        binding.get().categoryDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().catAndSubcatTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().catAndSubcatTextView);
            }
        });

        binding.get().categoryTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().categoryDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().catAndSubcatTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().catAndSubcatTextView);
            }
        });

        binding.get().promoteDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().itemPromoteConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().itemPromoteConstraintLayout);
            }
        });

        binding.get().promoteTitleTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().promoteDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().itemPromoteConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().itemPromoteConstraintLayout);
            }
        });
        binding.get().highLightDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().highLightDescTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().highLightDescTextView);
            }
        });

        binding.get().highLightTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().highLightDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().highLightDescTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().highLightDescTextView);
            }
        });


        binding.get().termAndConDownImageView.setOnClickListener(this::toggleTermAndConDescription);

        binding.get().policyDownImageView.setOnClickListener(this::togglePolicyDescription);

        binding.get().addInfoDownImageView.setOnClickListener(this::toggleAddInfoDescription);

        binding.get().addInfoTextView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().addInfoDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().addInfoDescriptionTextView);
                ViewAnimationUtil.expand(binding.get().addInfoReadMoreButton);
            } else {
                ViewAnimationUtil.collapse(binding.get().addInfoDescriptionTextView);
                ViewAnimationUtil.collapse(binding.get().addInfoReadMoreButton);
            }
        });

        binding.get().termAndConTextView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().termAndConDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().termAndCondescriptionTextView);
                ViewAnimationUtil.expand(binding.get().termAndConReadMoreButton);
            } else {
                ViewAnimationUtil.collapse(binding.get().termAndCondescriptionTextView);
                ViewAnimationUtil.collapse(binding.get().termAndConReadMoreButton);
            }
        });

        binding.get().policyTextView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().policyDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().policyDescriptionTextView);
                ViewAnimationUtil.expand(binding.get().policyReadMoreButton);
            } else {
                ViewAnimationUtil.collapse(binding.get().policyDescriptionTextView);
                ViewAnimationUtil.collapse(binding.get().policyReadMoreButton);
            }
        });

        binding.get().readMoreButton.setOnClickListener(v -> navigationController.navigateToReadMoreActivity(ItemFragment.this.getActivity(), binding.get().nameTextView.getText().toString(), binding.get().descriptionTextView.getText().toString(), binding.get().getItem()));

        binding.get().termAndConReadMoreButton.setOnClickListener(v -> navigationController.navigateToPrivacyPolicyActivity(ItemFragment.this.getActivity(), binding.get().termAndCondescriptionTextView.getText().toString()));

        binding.get().policyReadMoreButton.setOnClickListener(v -> navigationController.navigateToPrivacyPolicyActivity(ItemFragment.this.getActivity(), binding.get().policyDescriptionTextView.getText().toString()));

        binding.get().addInfoReadMoreButton.setOnClickListener(v -> navigationController.navigateToPrivacyPolicyActivity(ItemFragment.this.getActivity(), binding.get().addInfoDescriptionTextView.getText().toString()));

        binding.get().coverUserImageView.setOnClickListener(v -> navigationController.navigateToGalleryActivity(ItemFragment.this.getActivity(), Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId));

        binding.get().mainFloatingActionButton.setOnClickListener(v -> {
            twist = Utils.twistFab(v, !twist);

            messenger = binding.get().getItem().messenger;
            whatsappNo = binding.get().getItem().whatsapp;

            if (messenger.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding.get().whatsappFloatingActionButton);
                } else {
                    Utils.hideFab(binding.get().whatsappFloatingActionButton);
                }
            } else if (whatsappNo.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding.get().messengerFloatingActionButton);
                } else {
                    Utils.hideFab(binding.get().messengerFloatingActionButton);
                }
            } else {
                if (twist) {
                    Utils.showFab(binding.get().messengerFloatingActionButton);
                    Utils.showFab(binding.get().whatsappFloatingActionButton);
                } else {
                    Utils.hideFab(binding.get().messengerFloatingActionButton);
                    Utils.hideFab(binding.get().whatsappFloatingActionButton);
                }
            }
        });

        binding.get().messengerFloatingActionButton.setOnClickListener(v -> {

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setPackage("com.facebook.orca");
                intent.setData(Uri.parse("https://m.me/" + messenger));
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.item_detail__install_messenger_app), Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().whatsappFloatingActionButton.setOnClickListener(v -> {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(whatsappNo) + "@s.whatsapp.net");//phone number without "+" prefix
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.item_detail__install_whatsapp_app), Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().promoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationController.navigateToItemPromoteActivity(getActivity(),itemViewModel.itemId);
            }
        });
    }


    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        ratingViewModel = new ViewModelProvider(this, viewModelFactory).get(RatingViewModel.class);
        specsViewModel = new ViewModelProvider(this, viewModelFactory).get(SpecsViewModel.class);
        favouriteViewModel = new ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel.class);
        touchCountViewModel = new ViewModelProvider(this, viewModelFactory).get(TouchCountViewModel.class);

    }

    @Override
    protected void initAdapters() {

        //tab layout
        ItemTagAdapter tabAdapter = new ItemTagAdapter(dataBindingComponent,
                (tabObject, selectedTabId) -> {

                    switch (tabObject.field_name) {
                        case Constants.CATEGORY:

                            itemParameterHolder.resetTheHolder();
                            itemParameterHolder.cat_id = tabObjectList.get().get(0).tag_id;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), itemParameterHolder, tabObjectList.get().get(0).tag_name);
                            break;

                        case Constants.SUBCATEGORY:

                            itemParameterHolder.resetTheHolder();
                            itemParameterHolder.cat_id = tabObjectList.get().get(0).tag_id;
                            itemParameterHolder.sub_cat_id = tabObjectList.get().get(1).tag_id;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), itemParameterHolder, tabObjectList.get().get(1).tag_name);

                            break;
                        case Constants.ITEM_TAG:

                            itemParameterHolder.resetTheHolder();
                            itemParameterHolder.keyword = tabObject.tag_name;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), itemParameterHolder, tabObject.tag_name);
                            break;
                    }
                });
        this.tabAdapter = new AutoClearedValue<>(this, tabAdapter);
        binding.get().tagRecyclerView.setAdapter(tabAdapter);

        //specs
        ItemSpecsAdapter specsAdapter = new ItemSpecsAdapter(dataBindingComponent, itemSpecs -> {
        });

        this.specsAdapter = new AutoClearedValue<>(this, specsAdapter);
        binding.get().specRecyclerView.setAdapter(specsAdapter);
    }

    @Override
    protected void initData() {

        getIntentData();

        getItemDetail();

        getFavData();

        getTouchCount();

    }

    private void setTouchCount() {

        if (connectivity.isConnected()) {
            touchCountViewModel.setTouchCountPostDataObj(loginUserId, itemViewModel.itemId, Constants.FILTERING_TYPE_NAME);
        }
    }

    private void getTouchCount() {
        setTouchCount();
        //get touch count post method
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void getFavData() {
        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void getIntentData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemViewModel.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    itemViewModel.historyFlag = getActivity().getIntent().getExtras().getString(Constants.HISTORY_FLAG);

                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    private void shareImageUri(Uri uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                Objects.requireNonNull(getContext()).startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmapFromView(ImageView view) {
        Drawable drawable = view.getDrawable();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private ImageView getCurrentImageView() {
        return imageView;
    }

    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void getItemDetail() {

        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

        LiveData<Resource<Item>> itemDetail = itemViewModel.getItemDetailData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);

                                replaceItemData(listResource.data);
                                bindingRatingData(listResource.data);
                                bindingCountData(listResource.data);
                                bindingTimeData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingMapData(listResource.data);
                                checkText(listResource.data);
                                bindingPaidStatus(listResource.data);
                                bindingPromoteConstraintLayout(listResource.data);
                                if (listResource.data.messenger.isEmpty() && listResource.data.whatsapp.isEmpty()) {
                                    Utils.hideFirstFab(binding.get().mainFloatingActionButton);
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);
//
//                                // Update the data
                                replaceItemData(listResource.data);
//
                                setTagData(listResource.data);
//
                                bindingRatingData(listResource.data);
                                bindingCountData(listResource.data);
                                bindingTimeData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingMapData(listResource.data);
                                checkText(listResource.data);
                                bindingPaidStatus(listResource.data);
                                bindingPromoteConstraintLayout(listResource.data);
                                if (listResource.data.messenger.isEmpty() && listResource.data.whatsapp.isEmpty()) {
                                    Utils.hideFirstFab(binding.get().mainFloatingActionButton);
                                }

//                                if (binding.get().contactInfoDownImageView.getRotation() == 0) {
//                                    collapseContactInfoFunction();
//                                } else {
//                                    expandContactInfoFunction();
//                                }
                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }
                    // itemDetailViewModel.isProductDetailData=true;

                } else {

                    //itemDetailViewModel.isProductDetailData=false;
                    itemViewModel.setLoadingState(false);
                    // Init Object or Empty Data

                }
            });
        }

        //get rating post method
        ratingViewModel.getRatingPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        dialog.get().dismiss();
                        dialog.get().cancel();
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                        navigationController.navigateToRatingList(ItemFragment.this.getActivity(), itemViewModel.itemId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }
                }
            }
        });

        //load product specs

        LiveData<List<ItemSpecs>> itemSpecs = specsViewModel.getSpecsListData();
        if (itemSpecs != null) {
            itemSpecs.observe(this, listResource -> {
                if (listResource != null && listResource.size() > 0) {

                    ItemFragment.this.replaceItemSpecsData(listResource);
                    specsViewModel.isSpecsData = true;

                } else {
                    specsViewModel.isSpecsData = false;
                    binding.get().fiveCardView.setVisibility(View.GONE);

                }

                showOrHideSpecs();
            });
        }
    }

    private void bindingMapData(Item item) {
        itemViewModel.LAT = item.lat;
        itemViewModel.LNG = item.lng;
    }


    private void getCustomLayoutDialog(float ratingNum) {
        dialog = new AutoClearedValue<>(this, new Dialog(binding.get().getRoot().getContext()));
        itemRatingEntryBinding = new AutoClearedValue<>(this, DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_rating_entry, null, false, dataBindingComponent));

        dialog.get().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.item_rating_entry);
        dialog.get().setContentView(itemRatingEntryBinding.get().getRoot());
        itemRatingEntryBinding.get().ratingBarDialog.setRating(ratingNum);

        itemRatingEntryBinding.get().ratingBarDialog.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            ratingViewModel.numStar = rating;
            itemRatingEntryBinding.get().ratingBarDialog.setRating(rating);
        });

        itemRatingEntryBinding.get().cancelButton.setOnClickListener(v -> {
            setRatingZero();
        });

        itemRatingEntryBinding.get().submitButton.setOnClickListener(v -> Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, ItemFragment.this.getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
            @Override
            public void onSuccess() {
                if (itemRatingEntryBinding.get().titleEditText.getText().toString().isEmpty() ||
                        itemRatingEntryBinding.get().messageEditText.getText().toString().isEmpty() || String.valueOf(itemRatingEntryBinding.get().ratingBarDialog.getRating()).equals("0.0")) {

                    psDialogRatingMsg.showErrorDialog(getString(R.string.error_message__rating), getString(R.string.app__ok));
                    psDialogRatingMsg.show();
                    psDialogRatingMsg.okButton.setOnClickListener(v1 -> {
                        psDialogRatingMsg.cancel();
                    });
                } else {
                    setRatingPost();
                    setRatingZero();
                }
            }
        }));

        Window window = dialog.get().getWindow();
        if (dialog.get() != null && window != null) {
            WindowManager.LayoutParams params = getLayoutParams(dialog.get());
            if (params != null) {
                window.setAttributes(params);
            }
        }

        dialog.get().show();
    }

    private void setRatingZero() {
        dialog.get().dismiss();
        dialog.get().cancel();
        binding.get().rating.setRating(0.0f);
        dialog.get().dismiss();
        dialog.get().cancel();
    }

    private void setRatingPost() {
        ratingViewModel.setRatingPostObj(itemRatingEntryBinding.get().titleEditText.getText().toString(),
                itemRatingEntryBinding.get().messageEditText.getText().toString(),
                itemRatingEntryBinding.get().ratingBarDialog.getRating() + "",
                loginUserId, itemViewModel.itemId);
        prgDialog.get().show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    private void replaceItemData(Item item) {
        binding.get().setItem(item);

        binding.get().catAndSubcatTextView.setText(getString(R.string.item_detail__category, item.category.name, item.subCategory.name));
        ratingViewModel.ratingValue = item.ratingDetails.totalRatingValue;

        if (!item.isFeatured.equals(Constants.ONE)) {
            binding.get().featureTextView.setVisibility(View.GONE);
            binding.get().featureIconImageView.setVisibility(View.GONE);
        }

        binding.get().commentCountTextView.setOnClickListener(v -> {
            navigationController.navigateToCommentListActivity(ItemFragment.this.getActivity(), item);
        });

        binding.get().reviewCountTextView.setOnClickListener(v -> {
            navigationController.navigateToRatingList(ItemFragment.this.getActivity(), item.id);
        });

    }

    private void replaceItemTabData(List<TabObject> tabObject) {
        tabAdapter.get().replace(tabObject);
        binding.get().executePendingBindings();
    }

    private void replaceItemSpecsData(List<ItemSpecs> itemSpecsList) {
        specsAdapter.get().replace(itemSpecsList);
        binding.get().executePendingBindings();
    }

    private void setTagData(Item listResource) {

        if (tabObjectList.get().size() > 0) {
            tabObjectList.get().clear();
        }

        tabObjectList.get().add(new TabObject(Constants.CATEGORY, listResource.catId, listResource.category.name));
        tabObjectList.get().add(new TabObject(Constants.SUBCATEGORY, listResource.subCatId, listResource.subCategory.name));

        String[] tags = listResource.searchTag.split(",");

        for (String tag : tags) {
            tabObjectList.get().add(new TabObject(Constants.ITEM_TAG, tag, tag));
        }
        replaceItemTabData(tabObjectList.get());

    }

    private void bindingTimeData(Item item) {
        binding.get().hourTextView.setText(getString(R.string.item_detail__time, item.openingHour, item.closingHour));
        binding.get().remarkTextView.setText(getString(R.string.item_detail__time_remark, item.time_remark));
    }

    private void bindingCountData(Item item) {
        binding.get().commentCountTextView.setText(getString(R.string.item_detail__comment_count, item.commentHeaderCount));
        binding.get().favCountTextView.setText(getString(R.string.item_detail__fav_count, item.favouriteCount));
        binding.get().reviewCountTextView.setText(getString(R.string.item_detail__review_count, String.valueOf(item.ratingDetails.totalRatingCount)));
        binding.get().viewCountTextView.setText(getString(R.string.item_detail__view_count, item.touchCount));
    }

    private void bindingRatingData(Item item) {

        if (item.ratingDetails.totalRatingValue == 0.0) {
            binding.get().starTextView.setText(getString(R.string.item_detail__rating));
        } else {
            binding.get().starTextView.setText(getString(R.string.rating__total_count_n_value, item.ratingDetails.totalRatingValue + "", item.ratingDetails.totalRatingCount + ""));
        }
        binding.get().ratingBar.setRating(item.ratingDetails.totalRatingValue);

    }

    private void bindingFavoriteData(Item item) {
        if (item.isFavourited.equals(Constants.ONE)) {
            binding.get().favoriteImageView.setLiked(true);
        } else {
            binding.get().favoriteImageView.setLiked(false);
        }
    }

    private void showOrHideSpecs() {

        if (specsViewModel.isSpecsData) {
            binding.get().specRecyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.get().specRecyclerView.setVisibility(View.GONE);
        }

    }

    private void toggleCategoryDescription(View v) {
        boolean show = Utils.toggleUpDownWithAnimation(v);
        if (show) {
            ViewAnimationUtil.expand(binding.get().catAndSubcatTextView);
        } else {
            ViewAnimationUtil.collapse(binding.get().catAndSubcatTextView);
        }
    }

    private void toggleAddInfoDescription(View v) {
        boolean show = Utils.toggleUpDownWithAnimation(v);
        if (show) {
            ViewAnimationUtil.expand(binding.get().addInfoDescriptionTextView);
            ViewAnimationUtil.expand(binding.get().addInfoReadMoreButton);
        } else {
            ViewAnimationUtil.collapse(binding.get().addInfoDescriptionTextView);
            ViewAnimationUtil.collapse(binding.get().addInfoReadMoreButton);
        }
    }

    private void togglePolicyDescription(View v) {
        boolean show = Utils.toggleUpDownWithAnimation(v);
        if (show) {
            ViewAnimationUtil.expand(binding.get().policyDescriptionTextView);
            ViewAnimationUtil.expand(binding.get().policyReadMoreButton);
        } else {
            ViewAnimationUtil.collapse(binding.get().policyDescriptionTextView);
            ViewAnimationUtil.collapse(binding.get().policyReadMoreButton);
        }
    }

    private void toggleTermAndConDescription(View v) {
        boolean show = Utils.toggleUpDownWithAnimation(v);
        if (show) {
            ViewAnimationUtil.expand(binding.get().termAndCondescriptionTextView);
            ViewAnimationUtil.expand(binding.get().termAndConReadMoreButton);
        } else {
            ViewAnimationUtil.collapse(binding.get().termAndCondescriptionTextView);
            ViewAnimationUtil.collapse(binding.get().termAndConReadMoreButton);
        }
    }

    private void expandContactInfoFunction() {


        ViewAnimationUtil.expand(binding.get().fourConstraintLayout);
    }

    private void collapseContactInfoFunction() {

        ViewAnimationUtil.collapse(binding.get().fourConstraintLayout);

    }

    private void expandStatisticFunction() {
        ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
        ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
        ViewAnimationUtil.expand(binding.get().favConstraintLayout);
        ViewAnimationUtil.expand(binding.get().commentConstraintLayout);
    }

    private void collapseStatisticFunction() {
        ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
        ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
        ViewAnimationUtil.collapse(binding.get().favConstraintLayout);
        ViewAnimationUtil.collapse(binding.get().commentConstraintLayout);
    }

    private void expandTabFunction() {
        binding.get().tagRecyclerView.setVisibility(View.VISIBLE);
    }

    private void collapseTabFunction() {
        binding.get().tagRecyclerView.setVisibility(View.GONE);
    }

    private void expandWhatYouGetFunction() {
        binding.get().specRecyclerView.setVisibility(View.VISIBLE);
    }

    private void collapseWhatYouGetFunction() {
        binding.get().specRecyclerView.setVisibility(View.GONE);
    }

    private void unFavFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });

    }

    private void favFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }

        });

    }


    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
        if (loginUserId != null) {
            itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
        }
        psDialogMsg.cancel();
//        binding.get().rating.setRating(0);
    }

    int ii = 1;

    public void checkText(Item item) {

        if (item.terms.equals("")) {
            binding.get().termsAndConCardView.setVisibility(View.GONE);
        } else {
            binding.get().termsAndConCardView.setVisibility(View.VISIBLE);
            binding.get().termAndCondescriptionTextView.setText(item.terms);
        }

        if (item.cancelation_policy.equals("")) {
            binding.get().policyCardView.setVisibility(View.GONE);
        } else {
            binding.get().policyCardView.setVisibility(View.VISIBLE);
            binding.get().policyDescriptionTextView.setText(item.cancelation_policy);
        }
        if (item.additional_info.equals("")) {
            binding.get().additionCardView.setVisibility(View.GONE);
        } else {
            binding.get().additionCardView.setVisibility(View.VISIBLE);
            binding.get().addInfoDescriptionTextView.setText(item.additional_info);
        }
        if (item.highlightInformation.equals("")) {
            binding.get().thirdCardView.setVisibility(View.GONE);
        } else {
            binding.get().thirdCardView.setVisibility(View.VISIBLE);
            binding.get().highLightDescTextView.setText(item.highlightInformation);
        }

        if (item.pinterest.equals("")) {
            binding.get().constraintPinterest.setVisibility(View.GONE);
        } else {
            binding.get().constraintPinterest.setVisibility(View.VISIBLE);
            binding.get().pinterestTextView.setText(item.pinterest);
        }
        if (item.website.equals("")) {
            binding.get().constraintWebsite.setVisibility(View.GONE);
        } else {
            binding.get().constraintWebsite.setVisibility(View.VISIBLE);
            binding.get().WebsiteTextView.setText(item.website);
        }
        if (item.facebook.equals("")) {
            binding.get().constraintFacebook.setVisibility(View.GONE);
        } else {
            binding.get().constraintFacebook.setVisibility(View.VISIBLE);
            binding.get().facebookTextView.setText(item.facebook);
        }
        if (item.google_plus.equals("")) {
            binding.get().constraintGoogleplus.setVisibility(View.GONE);
        } else {
            binding.get().constraintGoogleplus.setVisibility(View.VISIBLE);
            binding.get().gplusTextView.setText(item.google_plus);
        }
        if (item.twitter.equals("")) {
            binding.get().constraintTwitter.setVisibility(View.GONE);
        } else {
            binding.get().constraintTwitter.setVisibility(View.VISIBLE);
            binding.get().twitterTextView.setText(item.twitter);
        }
        if (item.instagram.equals("")) {
            binding.get().constraintinst.setVisibility(View.GONE);
        } else {
            binding.get().constraintinst.setVisibility(View.VISIBLE);
            binding.get().instaTextView.setText(item.instagram);
        }
        if (item.youtube.equals("")) {
            binding.get().constraintYoutube.setVisibility(View.GONE);
        } else {
            binding.get().constraintYoutube.setVisibility(View.VISIBLE);
            binding.get().youtubeTextView.setText(item.youtube);
        }
        if (item.time_remark.equals("")) {
            binding.get().remarkTextView.setVisibility(View.GONE);
        } else {
            binding.get().remarkTextView.setVisibility(View.VISIBLE);
        }
        if (item.openingHour.equals("") && item.closingHour.equals("")) {

            binding.get().hourTextView.setVisibility(View.GONE);
        } else {
            if(ii == 1) {
                binding.get().hourTextView.setVisibility(View.VISIBLE);
            }
        }
        if (item.openingHour.equals("") && item.closingHour.equals("")) {
            binding.get().timeImage.setVisibility(View.GONE);
            binding.get().timeTextView.setVisibility(View.GONE);
        } else {
            if(ii == 1) {
                binding.get().timeImage.setVisibility(View.VISIBLE);
                binding.get().timeTextView.setVisibility(View.VISIBLE);
                ii++;
            }
        }
        if (item.lat.equals("") && item.lng.equals("")) {
            binding.get().viewOnMapTextView.setVisibility(View.GONE);
        } else {
            binding.get().viewOnMapTextView.setVisibility(View.VISIBLE);
        }
        if (item.address.equals("")) {
            binding.get().viewOnMapTextView.setVisibility(View.GONE);
            binding.get().addressImageView.setVisibility(View.GONE);
            binding.get().addressTextView.setVisibility(View.GONE);
            binding.get().addressStreetTextView.setVisibility(View.GONE);
        } else {
            binding.get().viewOnMapTextView.setVisibility(View.VISIBLE);
            binding.get().addressImageView.setVisibility(View.VISIBLE);
            binding.get().addressTextView.setVisibility(View.VISIBLE);
            binding.get().addressStreetTextView.setVisibility(View.VISIBLE);
            binding.get().addressStreetTextView.setText(item.address);
        }
        if (item.phone1.equals("")) {
            binding.get().phone1TextView.setVisibility(View.GONE);
            binding.get().phoneCall1TextView.setVisibility(View.GONE);
        } else {
            binding.get().phone1TextView.setVisibility(View.VISIBLE);
            binding.get().phoneCall1TextView.setVisibility(View.VISIBLE);
            binding.get().phone1TextView.setText(item.phone1);
        }
        if (item.phone2.equals("")) {
            binding.get().phone2TextView.setVisibility(View.GONE);
            binding.get().phoneCall2TextView.setVisibility(View.GONE);
        } else {
            binding.get().phone2TextView.setVisibility(View.VISIBLE);
            binding.get().phoneCall2TextView.setVisibility(View.VISIBLE);
            binding.get().phone2TextView.setText(item.phone2);
        }
        if (item.phone3.equals("")) {
            binding.get().phone3TextView.setVisibility(View.GONE);
            binding.get().phoneCall3TextView.setVisibility(View.GONE);
        } else {
            binding.get().phone3TextView.setVisibility(View.VISIBLE);
            binding.get().phoneCall3TextView.setVisibility(View.VISIBLE);
            binding.get().phone3TextView.setText(item.phone3);
        }
        if (item.phone1.equals("") && item.phone2.equals("") && item.phone3.equals("")) {
            binding.get().phoneTitleTextView.setVisibility(View.GONE);
            binding.get().phoneImage.setVisibility(View.GONE);
        } else {
            binding.get().phoneTitleTextView.setVisibility(View.VISIBLE);
            binding.get().phoneImage.setVisibility(View.VISIBLE);
        }

        if (item.pinterest.equals("") && item.website.equals("")
                && item.facebook.equals("") && item.google_plus.equals("") && item.twitter.equals("") && item.instagram.equals("")
                && item.youtube.equals("") && item.time_remark.equals("") && item.openingHour.equals("") && item.closingHour.equals("")
                && item.address.equals("") && item.phone1.equals("") && item.phone2.equals("") && item.phone3.equals("")) {

            binding.get().fourCardView.setVisibility(View.GONE);
        } else {
            binding.get().fourCardView.setVisibility(View.VISIBLE);
        }

        if (item.addedUserId != null && item.addedUserId.equals(loginUserId)) {
            binding.get().adsCheckingTextView.setVisibility(View.VISIBLE);
        }else{
            binding.get().adsCheckingTextView.setVisibility(View.GONE);
        }
    }

    private void hideFloatingButton() {
        Utils.hideFirstFab(binding.get().messengerFloatingActionButton);
        Utils.hideFirstFab(binding.get().whatsappFloatingActionButton);
    }

    private void bindingPaidStatus(Item item) {
        switch (item.paidStatus) {
            case Constants.ADSPROGRESS:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_in_progress));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad));
                break;
            case Constants.ADSFINISHED:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_in_completed));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad_completed));
                break;
            case Constants.ADSNOTYETSTART:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_is_not_yet_start));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad_is_not_start));
                break;
            default:
                binding.get().adsCheckingTextView.setVisibility(View.GONE);
                break;
        }
    }
    private void bindingPromoteConstraintLayout(Item item) {
        if (item.user.userId.equals(loginUserId) && (item.paidStatus.equals(Constants.ADSFINISHED) || item.paidStatus.equals(Constants.ADSNOTAVAILABLE))) {
            binding.get().itemPromoteCardView.setVisibility(View.VISIBLE);
        } else {
            binding.get().itemPromoteCardView.setVisibility(View.GONE);
        }
    }

}
