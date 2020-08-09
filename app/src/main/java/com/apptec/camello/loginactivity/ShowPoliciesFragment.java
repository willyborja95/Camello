package com.apptec.camello.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentShowPoliciesBinding;

import timber.log.Timber;

/**
 * The fragment that show a web view of the privacy policies
 */
public class ShowPoliciesFragment extends BaseLoginFragment {

    // Using data binding
    FragmentShowPoliciesBinding binding;

    /**
     * BInd the layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_policies, container, false);

        // Setup the listener for set the button available
        binding.webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Timber.d("Scroll on x." + scrollY);
            }
        });


        return binding.getRoot();
    }
}
