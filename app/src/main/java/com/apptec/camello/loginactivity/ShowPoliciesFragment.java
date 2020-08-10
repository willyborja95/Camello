package com.apptec.camello.loginactivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentShowPoliciesBinding;
import com.apptec.camello.util.Constants;

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
        hideKeyboardFrom(getContext(), binding.getRoot());
        binding.webView.loadUrl(Constants.URL_PRIVACY_POLICY);

        // Setup the listener for set the button available
        binding.webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Timber.d("Scroll on x.%s", scrollY);

            }
        });


        return binding.getRoot();
    }


    public void hideKeyboardFrom(Context context, View view) {
        Timber.d("Hiding the keyboard");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException npe) {
            Timber.d(npe);
        }

    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        hideKeyboardFrom(getContext(), binding.getRoot());
    }
}
