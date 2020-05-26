package com.apptec.registrateapp.mainactivity.fpermission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import timber.log.Timber;

public class PermissionViewModel extends ViewModel {
    /**
     * Singular view model fro the fragment actions
     * <p>
     * The permission list will be on the main presenter for not be getting the data each time that the fragment onResume
     */

    // Variables that observe the fragment view from the get methods
    private MutableLiveData<Boolean> _addNewPermission = new MutableLiveData<Boolean>();

    public PermissionViewModel() {
        // Constructor
        Timber.d("Permission view model attached");
    }

    // Expose the data
    public LiveData<Boolean> addNewPermission() {
        /**
         * This method will be observed by the fragment viewe
         */
        return _addNewPermission;
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
