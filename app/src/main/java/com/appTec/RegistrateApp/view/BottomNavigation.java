package com.appTec.RegistrateApp.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.dashboard.DashboardFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home.HomeFragment;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications.NotificationsFragment;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomNavigation extends AppCompatActivity implements DialogDevice.NoticeDialogListener, DialogPermission.PermissionDialogListener {

    final Fragment fragment1 = new HomeFragment();
    final DashboardFragment fragment2 = new DashboardFragment();
    final Fragment fragment3 = new NotificationsFragment();
    Fragment active;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);


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
        fm.beginTransaction().add(R.id.nav_host_fragment,fragment1, "1").hide(fragment1).commit();
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
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;
                }
                return true;
            }
        });
    }






    @Override
    public void onDialogPositiveClick(String str) {

        System.out.println("DIO EN POSITIVO!!!!!");
        System.out.println(str);

    }

    @Override
    public void onPermissionSaved(Permission permission) {
        System.out.println("PERMISO GUARDADO");
        fragment2.addPermissionToList(permission);


    }

    @Override
    public void onPermissionNotSaved(Permission permission) {
        System.out.println("PERMISO NO GUARDADO");


    }
}
