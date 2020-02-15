package com.appTec.RegistrateApp.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appTec.RegistrateApp.BuildConfig;
import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.services.alarmmanager.AlarmBroadcastReceiver;
import com.appTec.RegistrateApp.services.geofence.GeofenceBroadcastReceiver;
import com.appTec.RegistrateApp.services.geofence.GeofenceConstants;
import com.appTec.RegistrateApp.services.geofence.GeofenceErrorMessages;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.assistance.AssistanceFragment;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNavigation extends AppCompatActivity implements
        DialogDevice.NoticeDialogListener,
        DialogPermission.PermissionDialogListener,
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

    final HomeFragment homeFragment = new HomeFragment();
    final PermissionFragment permissionFragment = new PermissionFragment();
    final DeviceFragment deviceFragment = new DeviceFragment();
    final AssistanceFragment assistanceFragment = new AssistanceFragment();
    Fragment active;
    final FragmentManager fm = getSupportFragmentManager();
    TelephonyManager telephonyManager;
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;

    //UI components
    ActionBar mainActionBar;
    Toolbar fragmentToolBar;


    //User data
    Device device;
    User user;
    ArrayList<PermissionType> lstPermissionType;
    ArrayList<Permission> lstPermission = new ArrayList<Permission>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Load data
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        user = (User) bundle.getSerializable("user");

        //Hay casos en los quedevice puede venir en null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bug
        device = (Device) bundle.getSerializable("device");
        lstPermissionType = (ArrayList<PermissionType>) bundle.get("lstPermissionType");
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        pref = getApplicationContext().getSharedPreferences("RegistrateApp", 0); // 0 - for private mode

        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);
        setContentView(R.layout.bottom_navigation);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFragmentToolbar);
        TextView lblToolbarName = (TextView) findViewById(R.id.lblToolbarName);
        setSupportActionBar(toolbar);
        //NavigationUI.setupWithNavController(toolbar, navController);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);

        getSupportActionBar().setTitle(null);
        lblToolbarName.setText("Panel Principal");
        getSupportActionBar().show();

        active = homeFragment;
        homeFragment.setCompany(user.getCompany());
        fm.beginTransaction().add(R.id.nav_host_fragment, deviceFragment, "4").hide(deviceFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, assistanceFragment, "3").hide(assistanceFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, permissionFragment, "2").hide(permissionFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").hide(homeFragment).commit();
        fm.beginTransaction().show(active).commit();

        // Last exit time captured by geofencing
        lastExitTime = getLastTimeExited();

        // Setup geofencing
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        populateGeofenceList();
        geofencingClient = LocationServices.getGeofencingClient(this);

        homeFragment.setArguments(getIntent().getExtras());
        homeFragment.setCompany(user.getCompany());
        homeFragment.setDevice(device);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        System.out.println("Dashboard");
                        lblToolbarName.setText("Panel Principal");
                        toolbar.getMenu().clear();
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        homeFragment.setCompany(user.getCompany());
                        homeFragment.setDevice(device);

                        //--change
                        homeFragment.updateTimer();
                        return true;
                    case R.id.navigation_assistance:
                        System.out.println("Asistencia");
                        lblToolbarName.setText("Historial");
                        toolbar.getMenu().clear();
                        getSupportActionBar().show();

                        fm.beginTransaction().hide(active).show(assistanceFragment).commit();
                        active = assistanceFragment;
                        return true;

                    case R.id.navigation_permission:
                        lblToolbarName.setText("Permisos");
                        if (toolbar.getMenu().size() == 0) {
                            toolbar.inflateMenu(R.menu.toolbar_menu);
                        }
                        getSupportActionBar().show();
                        fm.beginTransaction().hide(active).show(permissionFragment).commit();
                        active = permissionFragment;
                        permissionFragment.addArrayListPermissionType(lstPermissionType);
                        return true;
                    case R.id.navigation_device:
                        System.out.println("Equipos");
                        lblToolbarName.setText("Equipos");
                        toolbar.getMenu().clear();
                        getSupportActionBar().show();
                        fm.beginTransaction().hide(active).show(deviceFragment).commit();
                        active = deviceFragment;
                        if (device != null) {
                            deviceFragment.addDeviceToList(device);
                        }
                        return true;
                }
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
                        });
                pendingGeofenceTask = PendingGeofenceTask.NONE;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnUpdatePermissions:
                PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
                Call<JsonObject> permissionCall = permissionRetrofitInterface.get(pref.getString("token", null), user.getId());
                permissionCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonArray permissionListJson = response.body().getAsJsonArray("data");
                        lstPermission.clear();
                        for (int i = 0; i < permissionListJson.size(); i++) {
                            JsonObject permissionJson = permissionListJson.get(i).getAsJsonObject();
                            int id = permissionJson.get("id").getAsInt();
                            String strStartDate = permissionJson.get("fechainicio").getAsString();
                            String strEndDate = permissionJson.get("fechafin").getAsString();
                            String strPermissionType = permissionJson.getAsJsonObject("permiso").get("nombre").getAsString();
                            String strPermissionStatus = permissionJson.get("estado").getAsString();
                            String[] arrayStartDateTime = strStartDate.split(" ");
                            String[] arrayStartTime = arrayStartDateTime[1].split(":");
                            String[] arrayStartDate = arrayStartDateTime[0].split("/");
                            String[] arrayEndDateTime = strEndDate.split(" ");
                            String[] arrayEndTime = arrayEndDateTime[1].split(":");
                            String[] arrayEndDate = arrayEndDateTime[0].split("/");
                            Calendar calendarStartDate = Calendar.getInstance();
                            calendarStartDate.set(Integer.parseInt(arrayStartDate[2]), Integer.parseInt(arrayStartDate[1]), Integer.parseInt(arrayStartDate[0]), Integer.parseInt(arrayStartTime[0]), Integer.parseInt(arrayStartTime[1]));
                            Calendar calendarEndDate = Calendar.getInstance();
                            calendarEndDate.set(Integer.parseInt(arrayEndDate[2]), Integer.parseInt(arrayEndDate[1]), Integer.parseInt(arrayEndDate[0]), Integer.parseInt(arrayEndTime[0]), Integer.parseInt(arrayEndTime[1]));
                            PermissionType permissionType = null;
                            PermissionStatus permissionStatus = null;

                            for (int j = 0; j < lstPermissionType.size(); j++) {
                                if (strPermissionType.equals(lstPermissionType.get(j).getNombe())) {
                                    permissionType = lstPermissionType.get(j);
                                }
                            }

                            if (strPermissionStatus.equals("enrevision")) {
                                permissionStatus = PermissionStatus.Revisando;
                            } else if (strPermissionStatus.equals("aprobado")) {
                                permissionStatus = PermissionStatus.Aprobado;
                            } else if (strPermissionStatus.equals("rechazado")) {
                                permissionStatus = PermissionStatus.Rechazado;
                            }
                            Permission permission = new Permission(id, permissionType, permissionStatus, calendarStartDate, calendarEndDate);
                            lstPermission.add(permission);
                        }
                        permissionFragment.addPermissionList(lstPermission);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        showConectionErrorMessage();
                    }
                });
                break;

            case R.id.btnLogout:
                setNotLoggedUser();
                closeSession();
                break;
        }
        return true;
    }

    @Override
    public void onDeviceSaved(Device device) {
        this.device = device;
        deviceFragment.saveDevice(this.device);
    }

    @Override
    public void onPermissionSaved(final Permission permission) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        permission.getStartDate().getTime().setMonth(permission.getStartDate().getTime().getMonth()-1);
        permission.getEndDate().getTime().setMonth(permission.getEndDate().getTime().getMonth()-1);


        String strStartDate = dateformat.format(permission.getStartDate().getTime());
        final String strEndDate = dateformat.format(permission.getEndDate().getTime());
        JsonObject jsonPermission = new JsonObject();
        jsonPermission.addProperty("fechainicio", strStartDate);
        jsonPermission.addProperty("fechafin", strEndDate);
        jsonPermission.addProperty("permisoid", permission.getPermissionType().getId());
        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Call<JsonObject> permissionCall = permissionRetrofitInterface.post(pref.getString("token", null), jsonPermission);
        permissionCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                String strStartDate = response.body().getAsJsonObject("data").get("fechainicio").getAsString();
                String strEndDate = response.body().getAsJsonObject("data").get("fechafin").getAsString();
                int idPermission = Integer.parseInt(response.body().getAsJsonObject("data").get("permisoid").getAsString());
                String strPermissionStatus = response.body().getAsJsonObject("data").get("estado").getAsString();
                String[] arrayStartDateTime = strStartDate.split(" ");
                String[] arrayStartTime = arrayStartDateTime[1].split(":");
                String[] arrayStartDate = arrayStartDateTime[0].split("/");
                String[] arrayEndDateTime = strEndDate.split(" ");
                String[] arrayEndTime = arrayEndDateTime[1].split(":");
                String[] arrayEndDate = arrayEndDateTime[0].split("/");
                Calendar calendarStartDate = Calendar.getInstance();
                calendarStartDate.set(Integer.parseInt(arrayStartDate[2]), Integer.parseInt(arrayStartDate[1]), Integer.parseInt(arrayStartDate[0]), Integer.parseInt(arrayStartTime[0]), Integer.parseInt(arrayStartTime[1]));
                Calendar calendarEndDate = Calendar.getInstance();
                calendarEndDate.set(Integer.parseInt(arrayEndDate[2]), Integer.parseInt(arrayEndDate[1]), Integer.parseInt(arrayEndDate[0]), Integer.parseInt(arrayEndTime[0]), Integer.parseInt(arrayEndTime[1]));
                PermissionType permission = null;
                PermissionStatus permissionStatus = null;

                for (int i = 0; i < lstPermissionType.size(); i++) {
                    if (idPermission == lstPermissionType.get(i).getId()) {
                        permission = lstPermissionType.get(i);
                    }
                }

                if (strPermissionStatus.equals("enrevision")) {
                    permissionStatus = PermissionStatus.Revisando;
                } else if (strPermissionStatus.equals("aprobado")) {
                    permissionStatus = PermissionStatus.Aprobado;
                } else if (strPermissionStatus.equals("rechazado")) {
                    permissionStatus = PermissionStatus.Rechazado;
                }
                Permission permission1 = new Permission(id, permission, permissionStatus, calendarStartDate, calendarEndDate);
                permissionFragment.addPermissionToList(permission1);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showConectionErrorMessage();
            }
        });
    }

    public void showConectionErrorMessage() {
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
    }

    private void setNotLoggedUser() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LOGIN_USER_STATE, Constants.NOT_LOGGED_USER);
        editor.commit();
    }

    public void closeSession(){
        Intent loginIntent = new Intent(getApplication(), LoginActivity.class);
        databaseAdapter.removeData();
        startActivity(loginIntent);
    }
}
