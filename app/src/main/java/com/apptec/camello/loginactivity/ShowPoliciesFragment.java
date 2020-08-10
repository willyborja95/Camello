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
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
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
        disableDisclaimerButton();
        hideKeyboardFrom(getContext(), binding.getRoot());
        binding.webView.loadUrl(Constants.URL_PRIVACY_POLICY);

        // Setup the listener for set the button available
        binding.webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = (int) Math.floor(binding.webView.getContentHeight() * binding.webView.getScale());
                int webViewHeight = binding.webView.getHeight();
                int cutoff = height - webViewHeight - 30; // Don't be too strict on the cutoff point
                if (scrollY >= cutoff) {

                    Timber.d("Scrolled to the bottom");
                    // Set the button enable
                    setDisclaimerButtonEnable();
                }
            }
        });

        return binding.getRoot();
    }

    /**
     * Method to disable the disclaimer button
     */
    private void disableDisclaimerButton() {
        binding.acceptButton.setEnabled(false);
    }

    /**
     * Method to enable the disclaimer button
     */
    private void setDisclaimerButtonEnable() {
        binding.acceptButton.setEnabled(true);
        binding.setButtonEnabled(true);
        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to form fragment
                loginViewModel.setNewDestination(ShowPoliciesFragmentDirections.actionShowPoliciesFragmentToFormFragment().getActionId());
                SharedPreferencesHelper.putBooleanValue(Constants.USER_ACCEPTED_PRIVACY_POLICY, true);
            }
        });

    }

    /**
     * Crap method is not working
     *
     * @param context
     * @param view
     */
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
