package com.appTec.RegistrateApp.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.assistance.AssistanceFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.permission.PermissionFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home.HomeFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.device.DeviceFragment;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNavigation extends AppCompatActivity implements DialogDevice.NoticeDialogListener, DialogPermission.PermissionDialogListener {

    final Fragment homeFragment = new HomeFragment();
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
        fm.beginTransaction().add(R.id.nav_host_fragment, deviceFragment, "4").hide(deviceFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, assistanceFragment, "3").hide(assistanceFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, permissionFragment, "2").hide(permissionFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").hide(homeFragment).commit();
        fm.beginTransaction().show(active).commit();

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
                        //fragmentToolBar.setTitle("hola");
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
                        System.out.println("Permisos");
                        lblToolbarName.setText("Permisos");
                        System.out.println("7777777777777777");
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
        }
        return true;
    }

    @Override
    public void onDeviceSaved(Device device) {
        deviceFragment.saveDevice(device);
    }

    @Override
    public void onPermissionSaved(final Permission permission) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
}
