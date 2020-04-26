package com.appTec.RegistrateApp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.repository.StaticData;
import com.appTec.RegistrateApp.repository.geofence.GeofenceBroadcastReceiver;
import com.appTec.RegistrateApp.repository.geofence.GeofenceConstants;
import com.appTec.RegistrateApp.repository.geofence.GeofenceErrorMessages;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.fragments.device2.DeviceFragment22;
import com.appTec.RegistrateApp.view.fragments.home2.HomeFragment2;
import com.appTec.RegistrateApp.view.fragments.notifications.NotificationsFragment;
import com.appTec.RegistrateApp.view.fragments.permission2.PermissionFragment2;
import com.appTec.RegistrateApp.view.modals.DialogDevice;
import com.appTec.RegistrateApp.view.modals.DialogPermission;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.appTec.RegistrateApp.R.id.user_fullname;

public class MainActivity2 extends AppCompatActivity implements
        DialogDevice.NoticeDialogListener,
        DialogPermission.PermissionDialogListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {

    private static final String TAG = MainActivity2.class.getSimpleName();


    private String lastExitTime;


    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private PendingGeofenceTask pendingGeofenceTask = PendingGeofenceTask.NONE;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;

    // Fragments
    final HomeFragment2 homeFragment2 = HomeFragment2.newInstance();
    final PermissionFragment2 permissionFragment2 = PermissionFragment2.newInstance();
    final DeviceFragment22 deviceFragment2 = new DeviceFragment22();
 //   final AssistanceFragment assistanceFragment = new AssistanceFragment(); TODO: Remove
    final NotificationsFragment notificationsFragment = new NotificationsFragment();


    TelephonyManager telephonyManager;
//    SharedPreferences pref; TODO: Remove
//    DatabaseAdapter databaseAdapter;TODO: Remove

    //UI components
    ActionBar mainActionBar;
    Toolbar fragmentToolBar;
    private DrawerLayout drawer;
    private TextView companyName;
    private TextView userFullName;



//    ArrayList<PermissionType> lstPermissionType; TODO: Remove
//    ArrayList<Permission> lstPermission = new ArrayList<Permission>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);


        //Load data
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
//        user = (User) bundle.getSerializable("user");  TODO: Remove




       // device = (Device) bundle.getSerializable("device");
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
//        lstPermissionType = (ArrayList<PermissionType>) bundle.get("lstPermissionType"); TODO: Remove
//        pref = getApplicationContext().getSharedPreferences("RegistrateApp", 0);
//        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_view_2);


        drawer = findViewById(R.id.drawer_layout_2);




        NavigationView drawerNavigationView = (NavigationView) findViewById(R.id.nav_drawer_2);


        View viewNavHeader = drawerNavigationView.getHeaderView(0);
        companyName = (TextView) viewNavHeader.findViewById(R.id.company_name);
        userFullName = (TextView) viewNavHeader.findViewById(user_fullname);
        companyName.setText(StaticData.getCurrentUser().getCompany().getCompanyName());
        userFullName.setText(StaticData.getCurrentUser().getName()+" "+StaticData.getCurrentUser().getLastName());



        drawerNavigationView.setNavigationItemSelectedListener(this);


        ImageButton menuRight = findViewById(R.id.image_button_side_menu2);
        menuRight.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        TextView lblToolbarName = (TextView) findViewById(R.id.toolbar_name2);
        setSupportActionBar(toolbar);
        //NavigationUI.setupWithNavController(toolbar, navController);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);

        getSupportActionBar().setTitle(null);
        lblToolbarName.setText("Panel Principal");
        getSupportActionBar().show();

        // Last exit time captured by geofencing
        lastExitTime = getLastTimeExited();

        // Setup geofencing
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
       // populateGeofenceList();
        geofencingClient = LocationServices.getGeofencingClient(this);


        homeFragment2.setCompany(StaticData.getCurrentUser().getCompany());
        homeFragment2.setArguments(getIntent().getExtras());
        homeFragment2.setCompany(StaticData.getCurrentUser().getCompany());
        homeFragment2.setDevice(StaticData.getCurrentDevice());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment_2, homeFragment2);
        ft.commit();

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_home:
//                        ft.replace(R.id.nav_host_fragment, homeFragment);
//                        lblToolbarName.setText("Panel Principal");
//                        toolbar.getMenu().clear();
//                        homeFragment.setCompany(StaticData.getCurrentUser().getCompany());
//                        homeFragment.setDevice(StaticData.getCurrentDevice());
//                        break;
//                    case R.id.navigation_notifications:
//                        lblToolbarName.setText("Notificaciones");
//                        toolbar.getMenu().clear();
//                        getSupportActionBar().show();
//
//                        ft.replace(R.id.nav_host_fragment, notificationsFragment);
//                        break;
//
//                    case R.id.navigation_permission:
//                        lblToolbarName.setText("Permisos");
//                        Bundle permissionBundle = new Bundle();
//                        permissionBundle.putSerializable("user", StaticData.getCurrentUser());
//                        permissionBundle.putSerializable("lstPermissionType", lstPermissionType);
//                        permissionFragment.setArguments(permissionBundle);
//                        ft.replace(R.id.nav_host_fragment, permissionFragment);
//                        if (toolbar.getMenu().size() == 0) {
//                            toolbar.inflateMenu(R.menu.toolbar_menu);
//                        }
//                        getSupportActionBar().show();
//                        break;
//                    case R.id.navigation_device:
//                        Bundle deviceBundle = new Bundle();
//                        deviceBundle.putSerializable("device", StaticData.getCurrentDevice());
//                        deviceFragment.setArguments(deviceBundle);
//                        ft.replace(R.id.nav_host_fragment, deviceFragment);
//
//                        lblToolbarName.setText("Equipos");
//                        toolbar.getMenu().clear();
//                        getSupportActionBar().show();
//                        break;
//                }
//                Log.d("isAttached", "Preparando commit !");
//                ft.commit();
//                Log.d("isAttached", "Commit done !");
//                return true;
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!checkPermissions()) {
            requestPermissions();
        } else {
            performPendingGeofenceTask();
        }
    }

    private String getLastTimeExited() {
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        Log.d("test12", sharedPref.getString("test",
                ""));
        return sharedPref.getString(Constants.LAST_EXIT_TIME,
                "");
    }


    /********** GEOFENCING *********/

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    public void addGeofencesHandler() {
        if (!checkPermissions()) {
            pendingGeofenceTask = PendingGeofenceTask.ADD;
            requestPermissions();
            return;
        }
        addGeofences();
    }

    private void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid ->
                        Log.i(MainActivity2.class.getSimpleName(), "Successfully registered geofence"))
                .addOnFailureListener(this, e ->
                        Log.e(MainActivity2.class.getSimpleName(), "There was a problem registering the geofence"))
                .addOnCompleteListener(this);
    }

    public void removeGeofencesHandler() {
        if (!checkPermissions()) {
            pendingGeofenceTask = PendingGeofenceTask.REMOVE;
            requestPermissions();
            return;
        }
        removeGeofences();
    }

    private void removeGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        geofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        pendingGeofenceTask = PendingGeofenceTask.NONE;
        if (task.isSuccessful()) {
            updateGeofencesAdded(!getGeofencesAdded());

            int messageId = getGeofencesAdded() ? R.string.geofences_added :
                    R.string.geofences_removed;
//            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

//    private void populateGeofenceList() {
//        geofenceList.add(new Geofence.Builder()
//                .setRequestId(getClass().getSimpleName())
//                .setCircularRegion(
//                        StaticData.getCurrentUser().getCompany().getLatitude(),
//                        StaticData.getCurrentUser().getCompany().getLongitude(),
//                        (int) StaticData.getCurrentUser().getCompany().getRadius()
//                )
//                .setExpirationDuration(GeofenceConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build());
//    }

    private void showSnackbar(final String text) {

        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                GeofenceConstants.GEOFENCES_ADDED_KEY, false);
    }

    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(GeofenceConstants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    private void performPendingGeofenceTask() {
        if (pendingGeofenceTask == PendingGeofenceTask.ADD) {
            addGeofences();
        } else if (pendingGeofenceTask == PendingGeofenceTask.REMOVE) {
            removeGeofences();
        }
    }

    public boolean checkPermissions() {
        int coarseLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int backgroundLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        return coarseLocation == PackageManager.PERMISSION_GRANTED ||
                backgroundLocation == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(MainActivity2.this,
                                new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                },
                                Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity2.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

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
                performPendingGeofenceTask();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
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
                pendingGeofenceTask = PendingGeofenceTask.NONE;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnUpdatePermissions:
                permissionFragment2.updatePermissions();
                break;
            case R.id.btnLogout:
                SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);
                closeSession();
                break;
        }
        return true;
    }

    @Override
    public void onDeviceSaved(Device device) {
        Log.d("deviceLog", "Hello form bottom navigation");
        deviceFragment2.saveDevice(device);
    }

    @Override
    public void onPermissionSaved(final Permission permission) {
        permissionFragment2.savePermission(permission);
    }




    public void closeSession(){
        Intent loginIntent = new Intent(MainActivity2.this, LoginActivity.class);
        //databaseAdapter.removeData();
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);
        startActivity(loginIntent);
        finish();
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
                closeSession();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
