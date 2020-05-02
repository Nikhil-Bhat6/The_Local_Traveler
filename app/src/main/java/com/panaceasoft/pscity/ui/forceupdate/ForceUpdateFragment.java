package com.panaceasoft.pscity.ui.forceupdate;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;

import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.binding.FragmentDataBindingComponent;
import com.panaceasoft.pscity.databinding.FragmentForceUpdateBinding;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;

public class ForceUpdateFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    private AutoClearedValue<FragmentForceUpdateBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentForceUpdateBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_force_update, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() != null) {
            if (getActivity().getIntent().hasExtra(Constants.APPINFO_FORCE_UPDATE_MSG)) {
                binding.get().descriptionTextView.setText(getActivity().getIntent().getStringExtra(Constants.APPINFO_FORCE_UPDATE_MSG));
            }
            if (getActivity().getIntent().hasExtra(Constants.APPINFO_FORCE_UPDATE_TITLE)) {
                binding.get().titleTextView.setText(getActivity().getIntent().getStringExtra(Constants.APPINFO_FORCE_UPDATE_TITLE));
            }

        }

        binding.get().button4.setOnClickListener(v -> navigationController.navigateToPlayStore(getActivity()));
        binding.get().descriptionTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

    }
}
