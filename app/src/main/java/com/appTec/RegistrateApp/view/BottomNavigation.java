package com.appTec.RegistrateApp.view;

import android.os.Bundle;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomNavigation extends AppCompatActivity implements DialogDevice.NoticeDialogListener, DialogPermission.PermissionDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onDialogPositiveClick(String str) {

        System.out.println("DIO EN POSITIVO!!!!!");
        System.out.println(str);

    }

    @Override
    public void onPermissionSaved(String str) {
        System.out.println("PERMISO GUARDADO");

    }

    @Override
    public void onPermissionNotSaved(String str) {
        System.out.println("PERMISO NO GUARDADO");


    }
}
