package com.apptec.camello.loginactivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.apptec.camello.R;
import com.apptec.camello.databinding.ActivityLoginBinding;
import com.apptec.camello.mainactivity.MainActivity;
import com.apptec.camello.mainactivity.fnotification.NotificationBuilder;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;
import com.apptec.camello.util.EventListener;
import com.apptec.camello.util.EventObserver;

import timber.log.Timber;

/**
 * Login activity
 * <p>
 * This activity is the manager for the user authentication
 */
public class LoginActivity extends AppCompatActivity {


    // View model
    LoginViewModel loginViewModel;

    // Using data binding
    ActivityLoginBinding binding;

    private NavController navController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);                                                  // Showing the splash screen for until the activity is ready
        super.onCreate(savedInstanceState);


        // Verify here if is there a previous user logged
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);            // Set the content view

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);         // Getting the view model

        setUpNavigation();


        loginViewModel.getNewDestination().observe(this, new EventObserver<>(new EventListener<Integer>() {
            @Override
            public void onEvent(Integer integer) {
                // TODO

            }
        }));

        binding.setLoginViewModel(loginViewModel);

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            new Thread(new NotificationBuilder(getIntent().getExtras())).start();
        }

        Timber.d("Finished on create");


    }

    /**
     * We attach the login view model to prevent that the activity dint't build correctly
     */
    @Override
    protected void onResume() {


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

    /**
     * Asking for device permissions
     */
    private void askReadImeiPermission() {

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

    /**
     * Navigate to the next activity
     */
    public void navigateToLoggedView() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Verify if redirect the user to the notifications fragment

        intent.putExtra(Constants.NAVIGATE_TO_NOTIFICATIONS_FRAGMENT,
                SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.NAVIGATE_TO_NOTIFICATIONS_FRAGMENT, false));


        startActivity(intent);
        finish();
    }

    /**
     * Set up the navigation system
     */
    private void setUpNavigation() {

        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.login_nav_host_fragment))
                .getNavController();

        loginViewModel.getNewDestination().observe(this, new EventObserver<>(new EventListener<Integer>() {
            @Override
            public void onEvent(Integer destinationId) {
                Timber.d("New destination. ");
                navigate(destinationId);
            }
        }));
    }

    /**
     * We finally navigate to the specified destination
     *
     * @param destId resource layout id
     */
    private void navigate(int destId) {
        navController.navigate(destId);
    }


}
