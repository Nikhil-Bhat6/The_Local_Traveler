package com.panaceasoft.pscity.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentProfileBinding;
import com.panaceasoft.pscity.ui.common.DataBoundListAdapter;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.ui.item.history.adapter.HistoryAdapter;
import com.panaceasoft.pscity.ui.item.promote.adapter.ItemPromoteHorizontalListAdapter;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.ItemPaidHistoryViewModel.ItemPaidHistoryViewModel;
import com.panaceasoft.pscity.viewmodel.item.HistoryViewModel;
import com.panaceasoft.pscity.viewmodel.user.UserViewModel;
import com.panaceasoft.pscity.viewobject.ItemHistory;
import com.panaceasoft.pscity.viewobject.ItemPaidHistory;
import com.panaceasoft.pscity.viewobject.User;

import java.util.List;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HistoryViewModel historyViewModel;
    private UserViewModel userViewModel;
    public PSDialogMsg psDialogMsg;
    private ItemPaidHistoryViewModel itemPaidHistoryViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileBinding> binding;
    private AutoClearedValue<HistoryAdapter> adapter;
    private AutoClearedValue<ItemPromoteHorizontalListAdapter> itemPromoteHorizontalListAdapter;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().transactionList.setNestedScrollingEnabled(false);
        binding.get().editTextView.setOnClickListener(view -> navigationController.navigateToProfileEditActivity(getActivity()));
        binding.get().seeAllTextView.setOnClickListener(view -> navigationController.navigateToUserHistoryActivity(getActivity(),"",""));
        binding.get().userHistoryTextView.setOnClickListener(view -> navigationController.navigateToUserHistoryActivity(getActivity(),"",""));
        binding.get().favouriteTextView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().heartImageView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().settingTextView.setOnClickListener(view -> navigationController.navigateToSettingActivity(getActivity()));
        binding.get().userNotificatinTextView.setOnClickListener(view -> navigationController.navigateToNotificationList(getActivity()));
//        binding.get().paidAdViewAllTextView.setOnClickListener(view -> navigationController.navigateToUserHistoryActivity(getActivity(), loginUserId, Constants.FLAGPAID));

    }

    @Override
    protected void initViewModels() {
        historyViewModel = new ViewModelProvider(this, viewModelFactory).get(HistoryViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        itemPaidHistoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemPaidHistoryViewModel.class);
    }

    @Override
    protected void initAdapters() {

        HistoryAdapter nvAdapter = new HistoryAdapter(dataBindingComponent, historyProduct -> navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), historyProduct, selectedCityId));
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionList.setAdapter(nvAdapter);


        ItemPromoteHorizontalListAdapter itemPromoteAdapter = new ItemPromoteHorizontalListAdapter(dataBindingComponent, new ItemPromoteHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(ItemPaidHistory itemPaidHistory) {
                navigationController.navigateToDetailActivity(ProfileFragment.this.getActivity(), itemPaidHistory.item);
            }
        }, this);
        this.itemPromoteHorizontalListAdapter = new AutoClearedValue<>(this, itemPromoteAdapter);
        binding.get().userPaidItemRecyclerView.setAdapter(itemPromoteAdapter);
    }

    @Override
    protected void initData() {

        //load basket
        historyViewModel.offset = Config.HISTORY_COUNT;
        historyViewModel.setHistoryItemListObj(String.valueOf(historyViewModel.offset));
        LiveData<List<ItemHistory>> historyProductList = historyViewModel.getAllHistoryItemList();
        if (historyProductList != null) {
            historyProductList.observe(this, listResource -> {
                if (listResource != null) {

                    replaceProductHistoryData(listResource);

                }

            });
        }

        //User
        userViewModel.getUser(loginUserId).observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);

                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.isLoading = false;

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");


            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");

            }
        });

        // Get Paid Item History List
        itemPaidHistoryViewModel.setPaidItemHistory(Utils.checkUserId(loginUserId), String.valueOf(Config.PAID_ITEM_COUNT), String.valueOf(itemPaidHistoryViewModel.offset));

        itemPaidHistoryViewModel.getPaidItemHistory().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        replacePaidItemHistoryList(result.data);
                        itemPaidHistoryViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replacePaidItemHistoryList(result.data);

                        break;
                    case ERROR:
                        itemPaidHistoryViewModel.setLoadingState(false);
                        break;

                    default:
                        break;
                }
            }

        });

    }

    @Override
    public void onDispatched() {

    }

    private void replacePaidItemHistoryList(List<ItemPaidHistory> itemPaidHistories) {
        this.itemPromoteHorizontalListAdapter.get().replace(itemPaidHistories);
        binding.get().executePendingBindings();
    }


    private void replaceProductHistoryData(List<ItemHistory> historyProductList) {
        adapter.get().replace(historyProductList);
        binding.get().executePendingBindings();

    }

    private void replaceUserData(User user) {

        binding.get().editTextView.setText(binding.get().editTextView.getText().toString());
        binding.get().userNotificatinTextView.setText(binding.get().userNotificatinTextView.getText().toString());
        binding.get().userHistoryTextView.setText(binding.get().userHistoryTextView.getText().toString());
        binding.get().favouriteTextView.setText(binding.get().favouriteTextView.getText().toString());
        binding.get().settingTextView.setText(binding.get().settingTextView.getText().toString());
        binding.get().historyTextView.setText(binding.get().historyTextView.getText().toString());
        binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());
        binding.get().joinedDateTitleTextView.setText(binding.get().joinedDateTitleTextView.getText().toString());
        binding.get().joinedDateTextView.setText(user.addedDate);
        binding.get().nameTextView.setText(user.userName);
        binding.get().phoneTextView.setText(user.userPhone);
        binding.get().statusTextView.setText(user.userAboutMe);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE__PROFILE_FRAGMENT
                && resultCode == Constants.RESULT_CODE__LOGOUT_ACTIVATED) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                //navigationController.navigateToUserFBRegister((MainActivity) getActivity());
                navigationController.navigateToUserLogin((MainActivity) getActivity());
            }
        }
    }

}
