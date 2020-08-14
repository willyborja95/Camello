package com.apptec.camello.mainactivity;

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

import com.apptec.camello.App;
import com.apptec.camello.auth.SignOutRunnable;
import com.apptec.camello.auth.refreshtoken.RefreshTokenWorker;
import com.apptec.camello.mainactivity.fdevice.DevicePresenterImpl;
import com.apptec.camello.mainactivity.fhome.HomePresenterImpl;
import com.apptec.camello.mainactivity.fhome.geofence.HandleButtonClicked;
import com.apptec.camello.mainactivity.fnotification.NotificationPresenter;
import com.apptec.camello.mainactivity.fpermission.PermissionFull;
import com.apptec.camello.mainactivity.fpermission.PermissionPresenterImpl;
import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.models.UserModel;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.util.Constants;
import com.apptec.camello.util.Event;
import com.apptec.camello.util.Process;

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

    // Handle if needed to request location permissions
    private MutableLiveData<Boolean> _requestLocationPermissions = new MutableLiveData<>(false);

    // This boolean variable is needed to the activity could know if we should logout
    private MutableLiveData<Boolean> isUserLogged;

    // Work manager
    private WorkManager workManager = WorkManager.getInstance(App.getContext());


    // Show a process
    private BaseProcessListener processListener = new BaseProcessListener() {
        @Override
        public void onErrorOccurred(int title, int message) {
            _currentProcess.postValue(new Event<>(new Process(Process.FAILED, title, message)));
        }

        @Override
        public void onProcessing(Integer title, Integer message) {
            _currentProcess.postValue(new Event<>(new Process(Process.PROCESSING, title, message)));
        }

        @Override
        public void onSuccessProcess(Integer title, Integer message) {
            Timber.d("onSuccessProcess");
            _currentProcess.postValue(new Event<>(new Process(Process.SUCCESSFUL, title, message)));
        }
    };

    private final MutableLiveData<Event<Process>> _currentProcess = new MutableLiveData<>(new Event<>(null));


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

        // Sync notifications
        syncNotifications();

    }


    /**
     * Expose the process
     */
    public LiveData<Event<Process>> getProcess() {
        return this._currentProcess;
    }

    /**
     * For be sure that we present the process one time
     */
    public void processConsumed() {
        try {
            this._currentProcess.getValue().consume();
        } catch (NullPointerException npe) {
            Timber.w("Event process is null");
        }

    }


    /**
     * Verify if the user already register this device if needed
     */
    private void initializeDeviceVerification() {
        mainPresenter.initializeDeviceVerification(isNeededRegisterDevice);
    }


    /**
     * Expose the LiveData so the UI can observe it for the fragment Notification
     * <p>
     * Exposing the notifications
     */
    public LiveData<List<NotificationModel>> getNotifications() {
        return mNotifications;
    }

    /**
     * Exposing the user
     */
    public LiveData<UserModel> getCurrentUser() {
        return mUser;
    }

    /**
     * Exposing the list of devices
     */
    public LiveData<List<DeviceModel>> getDevices() {
        return mDevices;
    }

    /**
     * Exposing the last working period
     */
    public LiveData<WorkingPeriodModel> getLastWorkingPeriod() {
        return mLastWorkingPeriod;
    }

    /**
     * Expose if the user has to grant location permissions
     */
    public LiveData<Boolean> isNeededToRequestLocationPermissions() {
        return this._requestLocationPermissions;
    }


    /**
     * If the user is working change to no working and vice versa
     */
    public void changeLastWorkingState() {
        Timber.d("Button clicked");
        _currentProcess.setValue(new Event<>(new Process(Process.NOT_INIT, null, null)));
        /*
          Verify if the user is trying to stop or start
          see {@link HandleButtonClicked}
         */
        new Thread(new HandleButtonClicked(new HandleButtonClicked.Listener() {
            @Override
            public void onErrorOccurred(int title, int message) {
                Timber.e("Error occurred");
                _currentProcess.postValue(new Event<>(new Process(Process.FAILED, title, message)));
            }

            @Override
            public void onProcessing(Integer title, Integer message) {
                _currentProcess.postValue(new Event<>(new Process(Process.PROCESSING, title, message)));
            }

            @Override
            public void onSuccessProcess(Integer title, Integer message) {
                _currentProcess.postValue(new Event<>(new Process(Process.SUCCESSFUL, title, message)));
            }

            @Override
            public void onPermissionDenied() {
                Timber.e("Location permission are denied");
                _currentProcess.postValue(null);
                _requestLocationPermissions.postValue(true);
            }


        }, App.getContext())).start();


    }


    /**
     * Exposing the list of permissions
     */
    public LiveData<List<PermissionFull>> getPermissionFullList() {
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


    /**
     * The activity will be observing this to request new device information or not
     */
    public MutableLiveData<Boolean> getIsNeededRegisterDevice() {
        return this.isNeededRegisterDevice;
    }


    /**
     * Method to save this device to the server
     */
    public void saveThisDevice(String name, String model) {
        devicePresenter.saveThisDevice(name, model, this.isNeededRegisterDevice);
    }

    /**
     * This method we got a worker for refresh the token periodically
     */
    private void initRefreshToken() {

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

    /**
     * Delete credentials and tokens
     * <p>
     * if the user is working, advice him that the work will be finalized
     */
    public void logout() {

        Timber.d("Login out");
        boolean shouldStopWorking = false;
        if (this.getLastWorkingPeriod().getValue() != null) {
            if (this.mLastWorkingPeriod.getValue().getStatus() == Constants.INT_WORKING_STATUS) {
                // TODO: Advice the user that his working period will be ended
                shouldStopWorking = true;


            }
        }


        new Thread(new SignOutRunnable(false, shouldStopWorking, new SignOutRunnable.SignOutListener() {
            @Override
            public void onSuccessFinished() {
                isUserLogged.postValue(false);
            }
        })).start();


    }

    public MutableLiveData<Runnable> refreshButtonAction = new MutableLiveData<>(null);

    public void refreshButtonClicked() {
        if (refreshButtonAction != null) {
            new Thread(refreshButtonAction.getValue()).start();

        }
    }

    /**
     * Expose the flag to know if the user is logged or not
     */
    public MutableLiveData<Boolean> getIsUserLogged() {
        return this.isUserLogged;
    }

    /**
     * Save the permission requested
     */
    public void savePermission(PermissionType selectedItem, Calendar startDate, Calendar endDate, String comment) {
        permissionPresenter.savePermission(selectedItem, startDate, endDate, comment);
    }

    /**
     * Sync the permissions database with the network
     */
    public void syncPermissions() {

        permissionPresenter.syncPermissionsWithNetwork();
    }

    /**
     * Method called by the view to delete a permission
     *
     * @param permission instance of PermissionModel
     */
    public void deletePermission(PermissionModel permission) {
        permissionPresenter.deletePermission(permission, this.processListener);
    }


    /**
     * Method to syn the notifications with the server
     */
    public void syncNotifications() {
        notificationPresenter.syncNotifications(this.processListener);

    }
}
