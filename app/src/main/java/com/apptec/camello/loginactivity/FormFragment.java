package com.apptec.camello.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentFormBinding;
import com.apptec.camello.util.EventListener;
import com.apptec.camello.util.EventObserver;
import com.apptec.camello.util.Process;

import timber.log.Timber;

/**
 * The principal fragment that shows the form to the user to insert his password and
 * email.
 */
public class FormFragment extends BaseLoginFragment {

    // Using data binding
    FragmentFormBinding binding;

    /**
     * Use data biding
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form, container, false);

        // Attach the view model
        binding.setLoginViewModel(loginViewModel);

        // Control the message to show to the user interacts with the form
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState.isDataValid()) {
                    //
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    // Data invalid, set errors
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.email.setError(getString(loginFormState.getUsernameError()));
                    binding.password.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        return binding.getRoot();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Setup the result listener for the result
        loginViewModel.getLoginProgress().observe(this, new EventObserver<>(new EventListener<Process>() {
            @Override
            public void onEvent(Process process) {
                Timber.d("Login result has changed");

                // Verify is the result is success
                if (process != null) {
                    Timber.d(process.toString());
                    if (process.getProcessStatus() == Process.SUCCESSFUL) {
                        // Log in the user
                        // - navigate to logged activity
                        loginViewModel.navigateToMainActivity();

                    } else if (process.getProcessStatus() == Process.PROCESSING) {
                        // Processing
                        binding.progressBar.setVisibility(View.VISIBLE);

                    } else if (process.getProcessStatus() == Process.FAILED) {
                        // Show the errors
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        ResultDialog resultDialog = new ResultDialog(process.getTitleMessage(), process.getMessage());
                        resultDialog.show(getChildFragmentManager(), "Result dialog");
                    }
                }


            }
        }));

    }
}
