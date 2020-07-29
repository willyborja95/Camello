package com.apptec.camello.auth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.apptec.camello.App;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import timber.log.Timber;

/**
 * This class will contain auth stuff
 * <p>
 * - This class will login and logout the user
 * - Manage for device permissions stuff
 */
public class AuthHelper {


    // Work manager
    private static WorkManager workManager = WorkManager.getInstance(App.getContext());


    /**
     *
     */
    public static void scheduleSync() {
        Timber.d("Scheduling sync when internet is available");
        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(
                SyncAssistanceWorker.class)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniqueWork(
                Constants.SYNC_JOB_ID + "",
                ExistingWorkPolicy.APPEND,
                syncRequest
        );

    }


    /**
     * Method that return the unique code for the device identifier
     */
    public static String getDeviceUniqueCode() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            // Read the IMEI
            return getLocalImei();


        } else {
            // If the version us up that 29, then provide a unique code from a key file


            File file = new File(Constants.KEY_FILE_NAME);
            if (file.exists()) {                // If the key file exist

                // Read the key from the file
                String key = readFromKeyFile();

                return key;
            } else {                              // If the key file do not exist
                // Create a new key and save it into the key file

                // Create a new key
                String newKey = generateKey();


                // Storage the key on the key file

                storageKey(newKey);

                return newKey;


            }


        }

    }

    /**
     * Save the key into the key file
     *
     * @param key new key to be saved
     */
    private static void storageKey(String key) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(App.getContext().openFileOutput("config.txt",
                            Context.MODE_PRIVATE));
            outputStreamWriter.write(key);
            outputStreamWriter.close();
        } catch (IOException e) {
            Timber.e(e, "Failed to write key file");
        }


    }

    /**
     * Generate a unique secure key
     *
     * @return GUID
     */
    private static String generateKey() {

        return UUID.randomUUID().toString();

    }

    /**
     * Read the key file and
     *
     * @return the key storage in the key file
     */
    private static String readFromKeyFile() {
        String ret = "";

        try {
            InputStream inputStream = App.getContext().openFileInput(Constants.KEY_FILE_NAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Timber.e(e, "Key file not fount");
        } catch (IOException e) {
            Timber.e(e, "Can not read from key file");
        }

        return ret;
    }


    /**
     * Read the IMEI and storage it on an shared preferences's variable.
     * Change to false the flag of "is first running"
     */
    private static String getLocalImei() {

        String imei = SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, "invalid");
        if (imei.equals("invalid")) {
            // Read the IMEI and storage it on an shared preferences's variable.
            TelephonyManager telephonyManager = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);

            // Getting the imei
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                if (App.getContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // The permission need to be granted first
                } else {
                    if (Build.VERSION.SDK_INT < 26) {
                        imei = telephonyManager.getDeviceId();
                    }
                    if (android.os.Build.VERSION.SDK_INT >= 26) {
                        imei = telephonyManager.getImei();
                    }
                    Timber.d("Got IMEI");
                }
            }
            // Saving it on shared preferences
            SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);
            Timber.d("Local IMEI: %s", imei);

        }
        return imei;


    }


}
