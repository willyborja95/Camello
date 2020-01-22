package com.appTec.RegistrateApp.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.dashboard.DashboardFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home.HomeFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications.NotificationsFragment;
import com.appTec.RegistrateApp.view.activities.modals.DialogDateTime;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNavigation extends AppCompatActivity implements DialogDevice.NoticeDialogListener, DialogPermission.PermissionDialogListener {

    final Fragment fragment1 = new HomeFragment();
    final DashboardFragment fragment2 = new DashboardFragment();
    final NotificationsFragment fragment3 = new NotificationsFragment();
    Fragment active;
    final FragmentManager fm = getSupportFragmentManager();
    TelephonyManager telephonyManager;
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;


    //User data
    Device device;
    User user;
    ArrayList<PermissionType> lstPermissionType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bottom_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        //Load data
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        user = (User) bundle.getSerializable("user");
        device = (Device) bundle.getSerializable("device");
        lstPermissionType = (ArrayList<PermissionType>) bundle.get("lstPermissionType");
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        pref = getApplicationContext().getSharedPreferences("RegistrateApp", 0); // 0 - for private mode
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);

        /*
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        //New Bottom Navigation manager
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                }




                return true;
            }
        });

         */
        active = fragment1;
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").hide(fragment1).commit();
        fm.beginTransaction().hide(active).commit();


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;
                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        fragment2.addArrayListPermissionType(lstPermissionType);
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        if (device != null) {
                            fragment3.addDeviceToList(device);
                        }
                        return true;
                }
                return true;
            }
        });
    }


    @Override
    public void onDeviceSaved(Device device) {
        String deviceImei = "";
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
            } else {
                deviceImei = telephonyManager.getImei();
            }
        }
        device.setImei(deviceImei);

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);


        Call<JsonObject> call = deviceRetrofitInterface.post(pref.getString("token", null), device);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                String deviceName = response.body().getAsJsonObject("data").get("nombre").getAsString();
                String deviceModel = response.body().getAsJsonObject("data").get("modelo").getAsString();
                String deviceImei = response.body().getAsJsonObject("data").get("imei").getAsString();
                boolean deviceStatus = response.body().getAsJsonObject("data").get("estado").getAsBoolean();
                Device device = new Device(id, deviceName, deviceModel, deviceImei, deviceStatus);
                databaseAdapter.insertDevice(device);
                fragment3.addDeviceToList(device);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


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
                System.out.println("VIENDOOOOOOOOOOOOOOOOOOOOOOO");
                System.out.println(response.body());


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

                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPP");
                System.out.println(strPermissionStatus);
                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPP");

                if (strPermissionStatus.equals("enrevision")) {
                    permissionStatus = PermissionStatus.Revisando;
                } else if (strPermissionStatus.equals("aprobado")) {
                    permissionStatus = PermissionStatus.Aprobado;
                } else if (strPermissionStatus.equals("rechazado")) {
                    permissionStatus = PermissionStatus.Rechazado;

                }

                Permission permission1 = new Permission(id, permission, permissionStatus, calendarStartDate, calendarEndDate);
                fragment2.addPermissionToList(permission1);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}
