package com.panaceasoft.pscity.ui.privacyandpolicy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.pscity.MainActivity;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentPrivacyPolicyBinding;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.pscity.viewobject.AboutUs;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends PSFragment {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private AboutUsViewModel aboutUsViewModel;

    private String privacyPolicyName;


    @VisibleForTesting
    private AutoClearedValue<FragmentPrivacyPolicyBinding> binding;



    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentPrivacyPolicyBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }
    }

    @Override
    protected void initViewModels() {
        aboutUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutUsViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if(getActivity() != null) {
            privacyPolicyName  = getActivity().getIntent().getStringExtra(Constants.PRIVACY_POLICY_NAME);

            if(getArguments() != null){
                privacyPolicyName = getArguments().getString(Constants.PRIVACY_POLICY_NAME);
            }
        }

        aboutUsViewModel.setAboutUsObj("about us");
        aboutUsViewModel.getAboutUsData().observe(this, resource -> {

            if (resource != null) {

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            setAboutUsData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.");


            } else {
                //noinspection Constant Conditions
                Utils.psLog("No Data of About Us.");
            }
        });
    }


    private void setAboutUsData(AboutUs aboutUs) {
        binding.get().setAboutUs(aboutUs);

        if(privacyPolicyName.isEmpty()) {
            binding.get().termsAndConditionTextView.setText(aboutUs.privacyPolicy);
        }else {
            binding.get().termsAndConditionTextView.setText(privacyPolicyName);
        }

    }
    //endregion

}

