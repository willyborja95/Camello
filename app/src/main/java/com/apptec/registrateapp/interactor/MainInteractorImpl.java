package com.apptec.registrateapp.interactor;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

public class MainInteractorImpl {
    /**
     * Interactor for Main Activity
     */


    public boolean isTheFirstLogin() {
        /**
         * This method return true when is te first run from the user.
         */
        return SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_THE_FIRST_LOGIN, false);
    }

    public boolean isTheLoginFromTheSameUser() {
        /**
         * Return true when the user logged previous logged in this device.
         * Compare the previous user id
         */
        int previous_user_id = SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.PREVIOUS_LOGGED_USER_ID, 0);
        int current_user_id = SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_USER_ID, -1);
        if(previous_user_id == current_user_id){
            // That means that is the same user
            return true;
        }
        // Other wise return false
        return false;
    }


    public void handleFirstLogin(){
        /**
         * This method is called from the MainActivity because at this point we will already have
         * the user data.
         *
         * Here we request the information about this device.
         *
         * The process is describe in this flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         * iI you do not have access to it. Contact Renato with the email renatojobal@gmail.com
         *
         *
         *
         */

        // TODO: Request information about this device



    }






}
