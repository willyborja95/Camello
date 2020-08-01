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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        String uniqueCode = "invalid";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Timber.d("Unique code created by the imei");
            // Read the IMEI
            uniqueCode = getLocalImei();


        } else {
            // If the version us up that 29, then provide a unique code from a key file
            Timber.d("In API >= 29 return a generated a unique code");

            File file = new File(App.getContext().getFilesDir(), Constants.KEY_FILE_NAME);
            if (file.exists()) {                // If the key file exist
                Timber.d("Choose the previous generated key from the key file");
                // Read the key from the file
                uniqueCode = readFromKeyFile();

            } else {                              // If the key file do not exist
                // Create a new key and save it into the key file
                Timber.d("Key file does not exist. Create a new one.");
                // Create a new key

                uniqueCode = generateKey();

                // Storage the key on the key file

                storageKey(uniqueCode);


            }


        }
        Timber.d("Unique code: %s", uniqueCode);
        return uniqueCode;

    }

    /**
     * Save the uniqueCode into the key file
     *
     * @param uniqueCode new uniqueCode to be saved
     */
    private static void storageKey(String uniqueCode) {


        try {
            FileOutputStream fos = App.getContext().openFileOutput(Constants.KEY_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(uniqueCode.getBytes());
        } catch (FileNotFoundException e) {
            Timber.e(e, "File not fount");
        } catch (IOException e) {
            Timber.e(e);
        }


    }

    /**
     * Generate a unique secure key
     *
     * @return GUID
     */
    private static String generateKey() {
        Timber.d("Generating a random unique code");

        return UUID.randomUUID().toString();

    }

    /**
     * Read the key file and
     *
     * @return the key storage in the key file
     */
    private static String readFromKeyFile() {
        Timber.d("Reading from file");

        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = App.getContext().openFileInput(Constants.KEY_FILE_NAME);

            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            String contents = stringBuilder.toString();
            return contents;
        }

    }


    /**
     * Read the IMEI and storage it on an shared preferences's variable.
     * Change to false the flag of "is first running"
     */
    private static String getLocalImei() {

        String imei = SharedPreferencesHelper.getStringValue(Constants.LOCAL_IMEI, "invalid");
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
            SharedPreferencesHelper.putStringValue(Constants.LOCAL_IMEI, imei);
            Timber.d("Local IMEI: %s", imei);

        }
        return imei;


    }


}
