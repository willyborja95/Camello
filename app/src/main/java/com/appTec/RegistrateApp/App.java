package com.appTec.RegistrateApp;

import android.app.Application;

import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;

public class App extends Application {
    DatabaseAdapter databaseAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("LLAMADA DESDE APP 000000000000000000000000000000000000000");
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);
    }


}
