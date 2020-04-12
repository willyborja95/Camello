package com.appTec.RegistrateApp.repository.location;

import android.content.Context;
import android.location.Location;

public class LocationHelper {

    // Singleton
    private static Location location;

    public static Location getLocationHelper(){

        if (location == null){
            location = new Location("");

        }

        return location;
    }



}
