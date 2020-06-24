package com.apptec.registrateapp.mainactivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.auth.refreshtoken.RefreshTokenWorker;
import com.apptec.registrateapp.mainactivity.fdevice.DevicePresenterImpl;
import com.apptec.registrateapp.mainactivity.fhome.HomePresenterImpl;
import com.apptec.registrateapp.mainactivity.fnotification.NotificationPresenter;
import com.apptec.registrateapp.mainactivity.fpermission.PermissionFull;
import com.apptec.registrateapp.mainactivity.fpermission.PermissionPresenterImpl;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.NotificationModel;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.util.Constants;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;


/**
 * View model shared by the fragments
 */
public class MainViewModel extends AndroidViewModel {




    // To show the notifications
    private final LiveData<List<NotificationModel>> mNotifications; // List of notifications

    // To show devices
    private final LiveData<List<DeviceModel>> mDevices;          // List of user devices

    // To show the permissions
    private LiveData<List<PermissionFull>> mPermissionFullList;

    // Toolbar name according the active fragment
    private MutableLiveData<String> mActiveFragmentName;

    // This info will be on the drawer
    private final LiveData<UserModel> mUser;

    // This boolean variable show if the user is working or not
    private LiveData<WorkingPeriodModel> mLastWorkingPeriod;

    // To handle if needed the first login
    private MutableLiveData<Boolean> isNeededRegisterDevice;

    // This boolean variable is needed to the activity could know if we should logout
    private MutableLiveData<Boolean> isUserLogged;

    // Work manager
    private WorkManager workManager = WorkManager.getInstance(App.getContext());


    // Presenter for each feature
    MainPresenter mainPresenter;
    NotificationPresenter notificationPresenter;
    DevicePresenterImpl devicePresenter;
    HomePresenterImpl homePresenter;
    PermissionPresenterImpl permissionPresenter;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // Initialize the presenters (In the future we could use dagger2)
        mainPresenter = new MainPresenter();
        devicePresenter = new DevicePresenterImpl();
        notificationPresenter = new NotificationPresenter();
        homePresenter = new HomePresenterImpl();
        permissionPresenter = new PermissionPresenterImpl();

        // Load here the live data needed
        mNotifications = notificationPresenter.loadNotificationsLiveData();
        mDevices = devicePresenter.loadAllDevicesLiveData();
        mUser = RoomHelper.getAppDatabaseInstance().userDao().getLiveDataUser();
        mActiveFragmentName = new MutableLiveData<>();      // It is used to set the toolbar title
        mLastWorkingPeriod = homePresenter.getLiveDataLastWorkingPeriod();
        mPermissionFullList = RoomHelper.getAppDatabaseInstance().permissionFullDao().getAllPermissionsFull();


        // Handle the first login if is needed
        isNeededRegisterDevice = new MutableLiveData<>(false);
        initializeDeviceVerification();

        // Start the auto refresh token
        this.initRefreshToken();
        isUserLogged = new MutableLiveData<>(true);

        // Pull data from the permission catalog
        permissionPresenter.syncPermissionsWithNetwork();



    }


    private void initializeDeviceVerification() {
        mainPresenter.initializeDeviceVerification(isNeededRegisterDevice);
    }


    /**
     * Expose the LiveData so the UI can observe it for the fragment Notification
     */
    public LiveData<List<NotificationModel>> getNotifications() {
        /** Exposing the notifications */
        return mNotifications;
    }

    public LiveData<UserModel> getCurrentUser() {
        /** Exposing the user */
        return mUser;
    }

    public LiveData<List<DeviceModel>> getDevices() {
        /** Exposing the list of devices */
        return mDevices;
    }

    public LiveData<WorkingPeriodModel> getLastWorkingPeriod() {
        /** Exposing the last working period */
        return mLastWorkingPeriod;
    }


    /**
     * If the user is working change to no working and vice versa
     */
    public void changeLastWorkingState() {
        homePresenter.changeLastWorkingStatus();

    }

    public LiveData<List<PermissionFull>> getPermissionFullList() {
        /** Exposing the list of permissions */
        return this.mPermissionFullList;
    }


    // Control th ui toolbar name
    public MutableLiveData<String> getActiveFragmentName() {
        return mActiveFragmentName;
    }

    public void setActiveFragmentName(String value) {
        this.mActiveFragmentName.setValue(value);
    }


    public MainPresenter getMainPresenter() {
        return this.mainPresenter;
    }

    public void setIsNeededRegisterDevice(MutableLiveData<Boolean> value) {
        this.isNeededRegisterDevice = value;
    }


    public MutableLiveData<Boolean> getIsNeededRegisterDevice() {
        /**
         *      The activity will be observing this to request new device information or not
         */
        return this.isNeededRegisterDevice;
    }


    public void saveThisDevice(String name, String model) {
        /**
         * Method to save this device to the server
         */
        devicePresenter.saveThisDevice(name, model, this.isNeededRegisterDevice);

    }


    private void initRefreshToken() {
        /**
         * This method we got a worker for refresh the token periodically
         */

        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        PeriodicWorkRequest refreshTokenRequest = new PeriodicWorkRequest.Builder(
                RefreshTokenWorker.class,
                Constants.ACCESS_TOKEN_EXPIRATION,
                Constants.ACCESS_TOKEN_EXPIRATION_UNIT)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniquePeriodicWork(
                "refresher",
                ExistingPeriodicWorkPolicy.REPLACE,
                refreshTokenRequest);

    }


    public void logout() {
        /**
         * Delete credentials and tokens
         *
         * if the user is working, advice him that the work will be finalized
         */
        Timber.d("Login out");
        if (this.getLastWorkingPeriod().getValue() != null) {
            if (this.mLastWorkingPeriod.getValue().getStatus() == Constants.INT_WORKING_STATUS) {
                // TODO: Advice the user that his working period will be ended
                homePresenter.changeLastWorkingStatus();

            }
        }

        App.getAuthHelper().logout();
        workManager.cancelAllWork();
        this.isUserLogged.setValue(false);

    }


    public MutableLiveData<Boolean> getIsUserLogged() {
        /**
         * Expose the flag to know if the user is logged or not
         */
        return this.isUserLogged;
    }


    public void savePermission(PermissionType selectedItem, Calendar startDate, Calendar endDate) {
        /**
         * Save the permission requested
         */

        permissionPresenter.savePermission(selectedItem, startDate, endDate);
    }

    public void syncPermissions() {
        /**
         * Sync the permissions database with the network
         */
        permissionPresenter.syncPermissionsWithNetwork();
    }
}
