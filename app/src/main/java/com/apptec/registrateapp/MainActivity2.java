package com.apptec.registrateapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.apptec.registrateapp.R.id.user_fullname;

public class MainActivity2 extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = MainActivity2.class.getSimpleName();




    TelephonyManager telephonyManager;

    //UI components
    ActionBar mainActionBar;
    Toolbar fragmentToolBar;
    private DrawerLayout drawer;
    private TextView companyName;
    private TextView userFullName;
    private NavController navController;
    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);



        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_view_2);


        drawer = findViewById(R.id.drawer_layout_2);




        NavigationView drawerNavigationView = (NavigationView) findViewById(R.id.nav_drawer_2);


        View viewNavHeader = drawerNavigationView.getHeaderView(0);
        companyName = (TextView) viewNavHeader.findViewById(R.id.company_name);
        userFullName = (TextView) viewNavHeader.findViewById(user_fullname);


        drawerNavigationView.setNavigationItemSelectedListener(this);


        ImageButton menuRight = findViewById(R.id.image_button_side_menu2);
        menuRight.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        TextView lblToolbarName = (TextView) findViewById(R.id.toolbar_name2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(null);
        lblToolbarName.setText("Panel Principal");
        getSupportActionBar().show();





        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:

                        break;
                    case R.id.navigation_notifications:
                        navController.navigate(R.id.action_homeFragment_to_notificationsFragment);
                        break;

                    case R.id.navigation_permission:
                        navController.navigate(R.id.action_notificationsFragment_to_permissionFragment);
                        break;
                    case R.id.navigation_device:

                        break;
                }

                ft.commit();

                return true;
            }
        });


    }






//
//
//    public boolean checkPermissions() {
//        int coarseLocation = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION);
//        int backgroundLocation = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//        return coarseLocation == PackageManager.PERMISSION_GRANTED ||
//                backgroundLocation == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(this,
//                                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//            showSnackbar(R.string.permission_rationale, android.R.string.ok,
//                    view -> {
//                        // Request permission
//                        ActivityCompat.requestPermissions(MainActivity2.this,
//                                new String[]{
//                                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                                },
//                                Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
//                    });
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(MainActivity2.this,
//                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                    Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == Constants.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                //performPendingGeofenceTask(); TODO
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.
//                showSnackbar(R.string.permission_denied_explanation, R.string.settings, TODO
//                        view -> {
//                            // Build intent that displays the App settings screen.
//                            Intent intent = new Intent();
//                            intent.setAction(
//                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package",
//                                    BuildConfig.APPLICATION_ID, null);
//                            intent.setData(uri);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        });
//                pendingGeofenceTask = PendingGeofenceTask.NONE;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnUpdatePermissions:
                //permissionFragment2.updatePermissions();
                break;
            case R.id.btnLogout:
                SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);
                // TODO closeSession();
                break;
        }
        return true;
    }





    //Drawer Item selected logic
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()){
            case R.id.politica_privacidad:
                Intent policiesIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://registrateapp.com.ec/assets/POLI%CC%81TICA_DE_PRIVACIDAD_APP_REGISTRATE.pdf"));
                startActivity(policiesIntent);
                finish();

                break;

            case R.id.manual_usuario:
                Intent guideIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://registrateapp.com.ec/assets/Manual_de_Usuario.pdf"));
                startActivity(guideIntent);
                finish();
                break;

            case R.id.cerrar_sesion:
                // TODO
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
