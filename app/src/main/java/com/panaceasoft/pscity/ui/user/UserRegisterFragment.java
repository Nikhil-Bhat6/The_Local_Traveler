package com.panaceasoft.pscity.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentUserRegisterBinding;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.PSDialogMsg;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.user.UserViewModel;
import com.panaceasoft.pscity.viewobject.User;

/**
 * UserRegisterFragment
 */
public class UserRegisterFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private String token;

    private boolean checkFlag;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserRegisterBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        dataBindingComponent.getFragmentBindingAdapters().bindFullImageDrawable(binding.get().bgImageView, getResources().getDrawable(R.drawable.login_app_bg));

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            if (connectivity.isConnected()) {

                Utils.navigateToLogin(UserRegisterFragment.this.getActivity(), navigationController);

            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));

                psDialogMsg.show();
            }

        });

        //for check privacy and policy
        binding.get().privacyPolicyCheckbox.setOnClickListener(v -> {
            if (binding.get().privacyPolicyCheckbox.isChecked()) {
                navigationController.navigateToPrivacyPolicyActivity(getActivity(),Constants.EMPTY_STRING);
                checkFlag = true;
            } else {
                checkFlag = false;
            }
        });

        binding.get().registerButton.setOnClickListener(view -> {

            if (checkFlag) {
                UserRegisterFragment.this.registerUser();
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__to_check_agreement), getString(R.string.app__ok));
                psDialogMsg.show();

            }
        });
    }


    @Override
    protected void initViewModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        userViewModel.getRegisterUser().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.registerUserLoginData(pref,listResource.data,binding.get().passwordEditText.getText().toString());
                                Utils.navigateAfterUserRegister(getActivity(),navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();
                            updateRegisterBtnStatus();

                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showWarningDialog(listResource.message, getString(R.string.app__ok));
                        binding.get().registerButton.setText(getResources().getString(R.string.register__register));
                        psDialogMsg.show();

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });
    }

        //endregion


        //region Private Methods

        private void updateRegisterBtnStatus () {
            if (userViewModel.isLoading) {
                binding.get().registerButton.setText(getResources().getString(R.string.message__loading));
            } else {
                binding.get().registerButton.setText(getResources().getString(R.string.register__register));
            }
        }

        private void registerUser () {

            Utils.hideKeyboard(getActivity());

            String userName = binding.get().nameEditText.getText().toString().trim();
            if (userName.equals("")) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));

                psDialogMsg.show();
                return;
            }

            String userEmail = binding.get().emailEditText.getText().toString().trim();
            if (userEmail.equals("")) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));

                psDialogMsg.show();
                return;
            }

            String userPassword = binding.get().passwordEditText.getText().toString().trim();
            if (userPassword.equals("")) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));

                psDialogMsg.show();
                return;
            }


            userViewModel.isLoading = true;
            updateRegisterBtnStatus();

            userViewModel.setRegisterUser(new User(
                    "",
                    "",
                    "",
                    "",
                    "",
                    userName,
                    userEmail,
                    "",
                    userPassword,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",token));
        }

        //endregion

}

