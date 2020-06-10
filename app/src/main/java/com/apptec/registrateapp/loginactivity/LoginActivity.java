package com.apptec.registrateapp.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.databinding.ActivityLoginBinding;
import com.apptec.registrateapp.mainactivity.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {
    /**
     * Login activity
     * <p>
     * This activity is the manager for the user authentication
     */

    // View model
    LoginViewModel loginViewModel;

    // Using data binding
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);                                                  // Showing the splash screen for until the activity is ready
        super.onCreate(savedInstanceState);

        // Verify here if is there a previous user logged


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);            // Set the content view

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);         // Getting the view model


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState.isDataValid()) {
                    //
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    // Data invalid, set errors
//                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.email.setError(getString(loginFormState.getUsernameError()));
                    binding.password.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        binding.setLoginViewModel(loginViewModel);
        Timber.d("Finished on create");
    }

    @Override
    protected void onResume() {
        /**
         * We attach the login view model to prevent that the activity dint't build correctly
         */
        // Setup the result listener for the result
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            Timber.d("Login result has changed");

            // Verify is the result is success
            if (loginResult.getSuccess()) {
                // Log in the user
                // - navigate to logged activity
                navigateToLoggedView();
            } else {
                // Show the errors
                binding.progressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar
                        .make(binding.getRoot(), getString(R.string.invalid_credentials), Snackbar.LENGTH_LONG);
                snackbar.show();
            }


        });
        super.onResume();

    }

    public void navigateToLoggedView() {
        /**
         * Navigate to the next activity
         */
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
