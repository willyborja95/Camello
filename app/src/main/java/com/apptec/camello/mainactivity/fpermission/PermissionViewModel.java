package com.apptec.camello.mainactivity.fpermission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import timber.log.Timber;

/**
 * Singular view model fro the fragment actions
 * <p>
 * The permission list will be on the main presenter for not be getting the data each time that the fragment onResume
 * <p>
 * Note: Do not put data what will hit the database because it will make the query every time the fragment is onResume.
 * Put it on the main view model instead.
 */
public class PermissionViewModel extends ViewModel {

    // Variables that observe the fragment view from the get methods
    private MutableLiveData<Boolean> _addNewPermission = new MutableLiveData<Boolean>();

    // Instance of permission presenter
    PermissionPresenter permissionPresenter;

    public PermissionViewModel() {
        // Constructor
        Timber.d("Permission view model attached");
        permissionPresenter = new PermissionPresenter();

    }

    /**
     * Expose the data
     * This method will be observed by the fragment view
     */
    public LiveData<Boolean> addNewPermission() {
        return this._addNewPermission;
    }


    // This function is called by the layout
    public void onAddNewPermission(Boolean value) {
        Timber.d("onAddNewPermission clicked");
        this._addNewPermission.setValue(value);
    }


    @Override
    protected void onCleared() {
        Timber.w("View model cleared");
        super.onCleared();
    }


}
