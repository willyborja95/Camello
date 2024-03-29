package com.apptec.camello.mainactivity.fpermission;

import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Runnable that will sync the permissions in a background thread
 */
public class SyncPermissions implements Runnable {

    @Nullable BaseProcessListener listener;

    /**
     * Constructor with a listener or not
     */
    public SyncPermissions(@Nullable BaseProcessListener listener) {
        this.listener = listener;
    }

    /**
     * 1. Pull the permission types catalog
     * 2. Pull the permission status catalog
     * 3. Pull the permissions
     */
    @Override
    public void run() {
        if (listener != null) {
            listener.onProcessing(null, null);
        }
        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        pullPermissionTypes(permissionRetrofitInterface);
    }


    public void pullPermissionTypes(PermissionRetrofitInterface permissionRetrofitInterface) {
        // 1. Pull the permission types catalog
        Timber.d("Starting to pull the permission types catalog from network");

        // Pull the permission types

        Call<GeneralResponse<List<PermissionType>>> callTypes = permissionRetrofitInterface.getPermissionTypesWrapped(ApiClient.getAccessToken());

        callTypes.enqueue(new GeneralCallback<GeneralResponse<List<PermissionType>>>(callTypes) {
            /**
             * Method that will be called after the onResponse default method after doing some validations
             * * see {@link GeneralCallback}
             * This need to be override by the classes that implement GeneralCallback
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<GeneralResponse<List<PermissionType>>> call, Response<GeneralResponse<List<PermissionType>>> response) {
                // Save the response into the database
                new Thread(() -> {

                    if (response.body() != null) {
                        RoomHelper.getAppDatabaseInstance().permissionTypeDao().insertAll(response.body().getWrappedData());
                        Timber.d("Permission types catalog pulled successfully");

                        // Pull the permission states catalog
                        pullPermissionStatus(permissionRetrofitInterface);
                    }else {
                        Timber.e("Permission types catalog failed. response.body() == null. Response: %s", response);

                    }
                }).start();
            }
        });
    }


    public void pullPermissionStatus(PermissionRetrofitInterface permissionRetrofitInterface) {
        // 2. Pull the permission status catalog
        Timber.d("Starting to pull the permission status catalog available");

        // Pull the permission status
        Call<GeneralResponse<List<PermissionStatus>>> callStatus = permissionRetrofitInterface.getPermissionStatusWrapped(ApiClient.getAccessToken());

        callStatus.enqueue(new GeneralCallback<GeneralResponse<List<PermissionStatus>>>(callStatus) {

            /**
             * Method that will be called after the onResponse default method after doing some validations
             * * see {@link GeneralCallback}
             * This need to be override by the classes that implement GeneralCallback
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<GeneralResponse<List<PermissionStatus>>> call, Response<GeneralResponse<List<PermissionStatus>>> response) {
                // Save the response into the database

                new Thread(() -> {

                    Timber.d("Permission status catalog pulled successfully");
                    RoomHelper.getAppDatabaseInstance().permissionStatusDao().insertAll(response.body().getWrappedData());

                    // Finally we can sync the list of permission of this user
                    syncPermissionsWithNetwork(permissionRetrofitInterface);
                }).start();
            }


        });

    }

    /**
     * This method will bring the permission of this user and save into the database
     */
    public void syncPermissionsWithNetwork(PermissionRetrofitInterface permissionRetrofitInterface) {
        // 3. Pull the permissions
        Timber.i("Starting to sync the permissions list from network");


        Call<GeneralResponse<List<PermissionDto>>> syncPermissions = permissionRetrofitInterface.getAllPermissions(
                ApiClient.getAccessToken(), // Header
                SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_USER_ID, 0) // + Path user id
        );


        syncPermissions.enqueue(new GeneralCallback<GeneralResponse<List<PermissionDto>>>(syncPermissions) {
            /**
             * Method that will be called after the onResponse default method after doing some validations
             * * see {@link GeneralCallback}
             * This need to be override by the classes that implement GeneralCallback
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<GeneralResponse<List<PermissionDto>>> call, Response<GeneralResponse<List<PermissionDto>>> response) {

                new Thread(() -> {
                    try {
                        for (int i = 0; i < response.body().getWrappedData().size(); i++) {
                            // Save the list of permission into data
                            RoomHelper.getAppDatabaseInstance().permissionDao().insertOrReplace(response.body().getWrappedData().get(i).getAsPermissionModel());
                        }
                        Timber.d("Sync permission succeed");
                        // Notify the listener that all was sync
                        if (listener != null) {
                            listener.onSuccessProcess(null, null);
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                        if (listener != null) {
                            listener.onErrorOccurred(R.string.error, R.string.unknown_error);
                        }
                    }

                }).start();
            }

            /**
             * Method to be override by the calling class
             *
             * @param call call
             * @param t    throwable
             */
            @Override
            public void onFinalFailure(Call<GeneralResponse<List<PermissionDto>>> call, Throwable t) {
                Timber.e(t);
                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });


    }


}
