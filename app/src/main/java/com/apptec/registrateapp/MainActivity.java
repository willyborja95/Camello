package com.apptec.registrateapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";


    // UI elements (?: Use data binding)
    private BottomNavigationView bottomNavigationView;
    private NavigationView slideNavigationView;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setCollapseIcon(R.drawable.ic_drawer_menu);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_view);
        slideNavigationView = findViewById(R.id.slide_nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setupNavigation();

    }

    private void setupNavigation() {
        Log.i(TAG, "setupNavigation: ");
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.navigation_home,
                        R.id.navigation_notifications,
                        R.id.navigation_permission,
                        R.id.navigation_device) //Pass the ids of fragments from nav_graph which you dont want to show back button in toolbar
                        .setDrawerLayout(drawerLayout)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); //Setup toolbar with back button and drawer icon according to appBarConfiguration
        NavigationUI.setupWithNavController(slideNavigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        /*
         ** Listener for bottomNavigation must be called after been setupWithNavController
         ** This command will override NavigationUI.setupWithNavController(bottomNavigationView, navController)
         ** and the automatic transaction between fragments is lost
         * */
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        slideNavigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: ");
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.i(TAG, "onBackPressed: DRAWER IS OPEN -  CLOSING IT");
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i(TAG, "onSupportNavigateUp: ");
        // replace navigation up button with nav drawer button when on start destination
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected: SIDE BAR");
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        // https://stackoverflow.com/questions/55990820/how-to-use-navigation-drawer-and-bottom-navigation-simultaneously-navigation-a
        // https://stackoverflow.com/questions/58345696/how-to-use-android-navigation-component-bottomnavigationview-navigationview
        // https://stackoverflow.com/questions/55667686/how-to-coordinate-a-navigation-drawer-with-a-buttom-navigation-view
        // https://ux.stackexchange.com/questions/125627/is-it-okay-to-use-both-nav-drawer-and-bottom-nav-in-home-screen-of-an-android-ap?newreg=da5d1cea03db496982a00b256647728d
        if (menuItem.getItemId() == R.id.navigation_home) {
            Log.i(TAG, "Fragment selected: home");
        }
        if (menuItem.getItemId() == R.id.navigation_notifications) {
            Log.i(TAG, "Fragment selected: notifications");
        }
        if (menuItem.getItemId() == R.id.navigation_permission) {
            Log.i(TAG, "Fragment selected: permission");
        }
        if(menuItem.getItemId() == R.id.navigation_device){
            Log.d(TAG, "Fragment selected: device");

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom, menu);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
