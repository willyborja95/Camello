package com.appTec.RegistrateApp.view;

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

import com.appTec.RegistrateApp.BuildConfig;
import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.repository.geofence.GeofenceBroadcastReceiver;
import com.appTec.RegistrateApp.repository.geofence.GeofenceConstants;
import com.appTec.RegistrateApp.repository.geofence.GeofenceErrorMessages;
import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.assistance.AssistanceFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications.NotificationsFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.permission.PermissionFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home.HomeFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.device.DeviceFragment;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigation extends AppCompatActivity implements
        DialogDevice.NoticeDialogListener,
        DialogPermission.PermissionDialogListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {

    private static final String TAG = BottomNavigation.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private String lastExitTime;


    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private PendingGeofenceTask pendingGeofenceTask = PendingGeofenceTask.NONE;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;

    // Fragments
    final HomeFragment homeFragment = HomeFragment.newInstance();
    final PermissionFragment permissionFragment = PermissionFragment.newInstance();
    final DeviceFragment deviceFragment = new DeviceFragment();
    final AssistanceFragment assistanceFragment = new AssistanceFragment();
    final NotificationsFragment notificationsFragment = new NotificationsFragment();


    TelephonyManager telephonyManager;
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;

    //UI components
    ActionBar mainActionBar;
    Toolbar fragmentToolBar;
    private DrawerLayout drawer;
    private TextView companyName;
    private TextView userFullName;


    //User data
    Device device;
    User user;
    ArrayList<PermissionType> lstPermissionType;
    ArrayList<Permission> lstPermission = new ArrayList<Permission>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);


        //Load data
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        user = (User) bundle.getSerializable("user");



        //Hay casos en los quedevice puede venir en null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bug
        device = (Device) bundle.getSerializable("device");
        lstPermissionType = (ArrayList<PermissionType>) bundle.get("lstPermissionType");
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        pref = getApplicationContext().getSharedPreferences("RegistrateApp", 0);






        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);


        drawer = findViewById(R.id.drawer_layout);




        NavigationView drawerNavigationView = (NavigationView) findViewById(R.id.nav_drawer);


        View viewNavHeader = drawerNavigationView.getHeaderView(0);
        companyName = (TextView) viewNavHeader.findViewById(R.id.company_name);
        userFullName = (TextView) viewNavHeader.findViewById(R.id.user_fullname);
        companyName.setText(user.getCompany().getCompanyName());
        userFullName.setText(user.getName()+" "+user.getLastName());



        drawerNavigationView.setNavigationItemSelectedListener(this);












        ImageButton menuRight = findViewById(R.id.leftRight);
        menuRight.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFragmentToolbar);
        TextView lblToolbarName = (TextView) findViewById(R.id.lblToolbarName);
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
        populateGeofenceList();
        geofencingClient = LocationServices.getGeofencingClient(this);


        homeFragment.setCompany(user.getCompany());
        homeFragment.setArguments(getIntent().getExtras());
        homeFragment.setCompany(user.getCompany());
        homeFragment.setDevice(device);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, homeFragment);
        ft.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        ft.replace(R.id.nav_host_fragment, homeFragment);
                        lblToolbarName.setText("Panel Principal");
                        toolbar.getMenu().clear();
                        homeFragment.setCompany(user.getCompany());
                        homeFragment.setDevice(device);
                        break;
                    case R.id.navigation_notifications:
                        lblToolbarName.setText("Notificaciones");
                        toolbar.getMenu().clear();
                        getSupportActionBar().show();

                        ft.replace(R.id.nav_host_fragment, notificationsFragment);
                        break;

                    case R.id.navigation_permission:
                        lblToolbarName.setText("Permisos");
                        Bundle permissionBundle = new Bundle();
                        permissionBundle.putSerializable("user", user);
                        permissionBundle.putSerializable("lstPermissionType", lstPermissionType);
                        permissionFragment.setArguments(permissionBundle);
                        ft.replace(R.id.nav_host_fragment, permissionFragment);
                        if (toolbar.getMenu().size() == 0) {
                            toolbar.inflateMenu(R.menu.toolbar_menu);
                        }
                        getSupportActionBar().show();
                        break;
                    case R.id.navigation_device:
                        Bundle deviceBundle = new Bundle();
                        deviceBundle.putSerializable("device", device);
                        deviceFragment.setArguments(deviceBundle);
                        ft.replace(R.id.nav_host_fragment, deviceFragment);

                        lblToolbarName.setText("Equipos");
                        toolbar.getMenu().clear();
                        getSupportActionBar().show();
                        break;
                }
                Log.d("isAttached", "Preparando commit !");
                ft.commit();
                Log.d("isAttached", "Commit done !");
                return true;
            }
        });
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
                        Log.i(BottomNavigation.class.getSimpleName(), "Successfully registered geofence"))
                .addOnFailureListener(this, e ->
                        Log.e(BottomNavigation.class.getSimpleName(), "There was a problem registering the geofence"))
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

    private void populateGeofenceList() {
        geofenceList.add(new Geofence.Builder()
                .setRequestId(getClass().getSimpleName())
                .setCircularRegion(
                        user.getCompany().getLatitude(),
                        user.getCompany().getLongitude(),
                        (int) user.getCompany().getRadius()
                )
                .setExpirationDuration(GeofenceConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

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
                        ActivityCompat.requestPermissions(BottomNavigation.this,
                                new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                },
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(BottomNavigation.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
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
                permissionFragment.updatePermissions();
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
        this.device = device;
        deviceFragment.saveDevice(device);
    }

    @Override
    public void onPermissionSaved(final Permission permission) {
        permissionFragment.savePermission(permission);
    }

    public void showConectionErrorMessage() {
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
    }



    public void closeSession(){
        Intent loginIntent = new Intent(BottomNavigation.this, LoginActivity.class);
        databaseAdapter.removeData();
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
