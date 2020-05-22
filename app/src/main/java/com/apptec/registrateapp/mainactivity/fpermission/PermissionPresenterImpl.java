package com.apptec.registrateapp.mainactivity.fpermission;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionPresenterImpl {

    /**
     * Helps the interaction repository - view
     */
    private static final String TAG = "PermissionPresenterImpl";

    public LiveData<List<PermissionModel>> getLiveDataListPermission() {
        /**
         * It return the live of permission into the database
         */
        return RoomHelper.getAppDatabaseInstance().permissionDao().getLiveDataListPermission();
    }

    public void savePermission(PermissionType selectedItem, Calendar startDate, Calendar endDate) {
        /**
         * Save the permission requested
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * Call the service
                 * on success save the permission into database
                 */
//                LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
//                Call<LoginResponse> call = loginRetrofitInterface.login(userCredential);
//                call.enqueue(new Callback<LoginResponse>() {
//                }}
                Log.w(TAG, "savePermission: " + "starting");
                Date startDateDate = startDate.getTime();
                Date endDateDate = endDate.getTime();

                Long startDate = DateConverter.toTimestamp(startDateDate);
                Long endDate = DateConverter.toTimestamp(endDateDate);

                // Creating the body
                PermissionModel permission = new PermissionModel("", selectedItem.getId(), 1, startDate, endDate);
                PermissionDto permissionDto = new PermissionDto(permission);

                PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
                Call<JsonObject> call = permissionRetrofitInterface.createPermission(
                        ApiClient.getAccessToken(),
                        permissionDto
                );
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        // Save the permission also into the database

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RoomHelper.getAppDatabaseInstance().permissionDao().insert(permission);
                            }
                        }).start();


                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // TODO: Handle this
                        Log.e(TAG, "onFailure: " + t.getMessage());

                    }
                });

            }
        }).start();
    }
}
