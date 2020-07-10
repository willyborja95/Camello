package com.apptec.camello.mainactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apptec.camello.BuildConfig;
import com.apptec.camello.R;
import com.apptec.camello.databinding.ActivityMainBinding;
import com.apptec.camello.loginactivity.LoginActivity;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    /**
     * This class is the MainActivity
     */


    //UI components
    private NavController navController;


    TextView toolbar_name;

    MainViewModel mainViewModel;

    // Using data binding
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);   // Getting the view model

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_view);

        NavigationView drawerNavigationView = (NavigationView) findViewById(R.id.nav_drawer);
        View viewNavHeader = drawerNavigationView.getHeaderView(0);


        /** For control the side drawer onNavigationItemSelected */
        drawerNavigationView.setNavigationItemSelectedListener(this);


        /**
         * Open or close the side menu
         */
        ImageButton menuRight = findViewById(R.id.image_button_side_menu);
        menuRight.setOnClickListener(v -> {

            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_name = (TextView) findViewById(R.id.toolbar_name);


        setSupportActionBar(toolbar);
        mainViewModel.setActiveFragmentName(getString(R.string.home_fragment_title));
        mainViewModel.getActiveFragmentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newActiveFragmentsName) {
                toolbar_name.setText(newActiveFragmentsName);
            }
        });

        getSupportActionBar().setTitle(null);

        getSupportActionBar().show();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        navController.navigate(R.id.homeFragment);
                        break;
                    case R.id.navigation_notifications:
                        navController.navigate(R.id.notificationsFragment);
                        break;

                    case R.id.navigation_permission:
                        navController.navigate(R.id.permissionFragment);
                        break;

                    case R.id.navigation_device:
                        navController.navigate(R.id.deviceFragment);
                        break;
                }

                ft.commit();

                return true;
            }


        });


        // Setting up the names into the nav drawer
        TextView userName = viewNavHeader.findViewById(R.id.user_fullname);
        TextView companyName = viewNavHeader.findViewById(R.id.company_name);
        mainViewModel.getCurrentUser().observe(this, userModel -> {
            if (userModel != null) {
                userName.setText(userModel.getFullName());
                if (userModel.getCompany() != null) {
                    companyName.setText(userModel.getCompany().getCompanyName());
                }

            }
        });


        // Sent the user to the device fragment if needs to register this device
        // Handling here the if the first login of this user
        mainViewModel.getIsNeededRegisterDevice().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    // That mean the user needs to register this device
                    // Register device
                    bottomNavigationView.setSelectedItemId(R.id.navigation_device);
                    navController.navigate(R.id.deviceFragment);

                }
            }
        });


        // Know if logout or not
        mainViewModel.getIsUserLogged().observe(this, loggedUser -> {
            if (!loggedUser) {
                // Logout
                navigateToLogoutView();

            }
        });

        // Verify if we enter from a notification, then navigate to notifications fragment
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean(Constants.NAVIGATE_TO_NOTIFICATIONS_FRAGMENT, false)) {
                // Should navigate to notifications fragment
                bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
                navController.navigate(R.id.notificationsFragment);
            }
        }

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Timber.d("onRequestPermissionResult");
        if (grantResults.length <= 0) {
            // If user interaction was interrupted, the permission request is cancelled and you
            // receive empty arrays.
            Timber.i("User interaction was cancelled.");
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.i("Permission granted.");

        } else {
            Timber.d("Permission denied");
            // Permission denied.

            // Notify the user via a SnackBar that they have rejected a core permission for the
            // app, which makes the Activity useless. In a real app, core permissions would
            // typically be best requested during a welcome-screen flow.
            showSnackBar(R.string.permission_denied_explanation, R.string.settings,
                    view -> {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });

        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /** I do not know if this method is used or not*/
        switch (item.getItemId()) {
            case R.id.btn_update_permissions:
                //permissionFragment2.updatePermissions();
                break;
            case R.id.btn_logout:
                SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);
                mainViewModel.logout();
                break;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /**
         * Drawer Item selected logic
         */
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.privacy_politic:
                Intent policiesIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://registrateapp.com.ec/assets/POLI%CC%81TICA_DE_PRIVACIDAD_APP_REGISTRATE.pdf"));
                startActivity(policiesIntent);
                finish();

                break;

            case R.id.user_manual:
                Intent guideIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://registrateapp.com.ec/assets/Manual_de_Usuario.pdf"));
                startActivity(guideIntent);
                finish();
                break;

            case R.id.logout_button:

                mainViewModel.logout();
                break;
        }
        return true;
    }


    /**
     * Method to show a snack bar
     *
     * @param mainTextStringId An id to of a string resource
     * @param actionStringId
     * @param listener
     */
    private void showSnackBar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_LONG)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Navigate to the login activity
     */
    public void navigateToLogoutView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}