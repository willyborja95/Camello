package com.apptec.registrateapp.loginactivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.databinding.ActivityLoginBinding;
import com.apptec.registrateapp.mainactivity.MainActivity;

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
                    // binding.progressBar.setVisibility(View.INVISIBLE);
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
        loginViewModel.getLoginProgress().observe(this, loginResult -> {
            Timber.d("Login result has changed");

            // Verify is the result is success
            if (loginResult.getProcessStatus() == LoginProgress.SUCCESSFUL) {
                // Log in the user
                // - navigate to logged activity
                navigateToLoggedView();
            } else if (loginResult.getProcessStatus() == LoginProgress.PROCESSING) {
                // Processing
                binding.progressBar.setVisibility(View.VISIBLE);

            } else if (loginResult.getProcessStatus() == LoginProgress.FAILED) {
                // Show the errors
                binding.progressBar.setVisibility(View.INVISIBLE);
                ResultDialog resultDialog = new ResultDialog(loginResult.getTitleError(), loginResult.getError());
                resultDialog.show(getSupportFragmentManager(), "Result");
            }


        });

        // Set if the ream IMEI permission is granted
        loginViewModel.permissionGranted.setValue(this.isReadImeiPermissionGranted());

        loginViewModel.getShouldRequestPermission().observe(this, aBoolean -> {
            if (aBoolean) {
                // Request the permission for read the IMEI
                askReadImeiPermission();
            }
        });

        super.onResume();

    }

    private void askReadImeiPermission() {
        /** Asking for device permissions*/
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.READ_PHONE_STATE
                }, 225);
            }
        }
    }

    private boolean isReadImeiPermissionGranted() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
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
