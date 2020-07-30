package com.apptec.camello.mainactivity.fpermission;

import androidx.lifecycle.LiveData;

import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Helps the interaction repository - view
 */
public class PermissionPresenterImpl {

    /**
     * It return the live of permission into the database
     */
    public LiveData<List<PermissionModel>> getLiveDataListPermission() {
        return RoomHelper.getAppDatabaseInstance().permissionDao().getLiveDataListPermission();
    }

    /**
     * Save the permission requested
     */
    public void savePermission(PermissionType selectedItem, Calendar startDate, Calendar endDate, String comment) {

        new Thread(() -> {
            // Call the service on success save the permission into database

            Date startDateDate = startDate.getTime();
            Date endDateDate = endDate.getTime();

            Long startDate1 = DateConverter.toTimestamp(startDateDate);
            Long endDate1 = DateConverter.toTimestamp(endDateDate);

            // Creating the body
            PermissionModel permission = new PermissionModel(comment, selectedItem.getId(), 1, startDate1, endDate1);
            PermissionDto permissionDto = new PermissionDto(permission);

            PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
            Call<GeneralResponse<PermissionDto>> call = permissionRetrofitInterface.createPermission(
                    ApiClient.getAccessToken(),
                    permissionDto
            );
            Timber.d(permissionDto.toString(), permissionDto);
            call.enqueue(new GeneralCallback<GeneralResponse<PermissionDto>>(call) {

                /**
                 * Method that will be called after the onResponse default method after doing some validations
                 * * see {@link GeneralCallback}
                 * This need to be override by the classes that implement GeneralCallback
                 *
                 * @param call     call
                 * @param response response
                 */
                @Override
                public void onFinalResponse(Call<GeneralResponse<PermissionDto>> call, Response<GeneralResponse<PermissionDto>> response) {
                    // Save the permission also into the database

                    if (response.isSuccessful()) {

                        // Get the permission id form the response and also the status by the way
                        int correctId = response.body().getWrappedData().getId();
                        int correctStatus = response.body().getWrappedData().getStatusId();
                        Timber.d("Permission: " + response.body().getWrappedData().toString());
                        Timber.i("CorrectId: " + correctId);
                        Timber.i("CorrectStatus: " + correctStatus);

                        // Update the permission with the id and status before save into database
                        permission.setId(correctId);
                        permission.setFkPermissionStatus(correctStatus);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Timber.d("Saving the permission result into database");
                                RoomHelper.getAppDatabaseInstance().permissionDao().insertOrReplace(permission);
                            }
                        }).start();
                    } else {

                        Timber.e("Response not successful");
                    }
                }
            });

        }).start();
    }

    /**
     * Create a new Thread for pull the permissions
     */
    public void syncPermissionsWithNetwork() {
        new Thread(new SyncPermissions()).start();
    }

    /**
     * Method that call the server to delete a permission
     *
     * @param permission the target permission
     * @param listener   listener of the process
     */
    public void deletePermission(@NotNull PermissionModel permission, @Nullable BaseProcessListener listener) {

        if (listener != null) {
            listener.onProcessing();
        }

        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Call<JsonObject> call = permissionRetrofitInterface.deletePermission(
                ApiClient.getAccessToken(),
                permission.getId()
        );
        Timber.d(call.request().toString());
        call.enqueue(new GeneralCallback<JsonObject>(call) {


            /**
             * Method that will be called after the onResponse() default method after doing some validations
             * * see {@link GeneralCallback}
             * This need to be override by the classes that implement GeneralCallback
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Timber.d(response.toString());
                Timber.d(response.body().toString());

                Timber.d("Permission deleted successful");

                // Delete also from database
                new Thread(() -> {

                    RoomHelper.getAppDatabaseInstance().permissionDao().delete(permission);
                    if (listener != null) {
                        Timber.d("Notify the listener");
                        listener.onSuccessProcess();
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
            public void onFinalFailure(Call<JsonObject> call, Throwable t) {
                Timber.e(t);
                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });

    }

}
