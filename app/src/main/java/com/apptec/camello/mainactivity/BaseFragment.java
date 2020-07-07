package com.apptec.camello.mainactivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import timber.log.Timber;

/**
 * This class is a base class fragment that implements some methods
 * for interact with the UI in the same way in all fragments that extend
 * from this.
 * <p>
 * We handle here the presentation of dialogs for showing not internet or something went wrong.
 */
public class BaseFragment extends Fragment implements BaseProcessListener {


    // Instance of ViewModel
    protected MainViewModel mainViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);            // Getting the view model

    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {Activity.onResume} of the containing
     * Activity's lifecycle.
     * <p>
     * We override it for implement a listener to one process
     */
    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.getProcess().observe(this, process -> {
            if (process == null) {

            } else if (process.getProcessStatus() == Process.PROCESSING) {
                onProcessing();
            } else if (process.getProcessStatus() == Process.SUCCESSFUL) {
                onSuccessProcess();
            } else if (process.getProcessStatus() == Process.FAILED) {
                onErrorOccurred(process.getTitleError(), process.getError());
            }
        });

    }

    /**
     * Method to show an error if it happens in some process
     */
    @Override
    public void onErrorOccurred(int title, int message) {
        // Show error
        Timber.d("onErrorOccurred: " + getString(title) + ". " + getString(message));
        BaseDialog dialog = new BaseDialog(title, message, new DialogListener() {
            @Override
            public void onClose() {
                mainViewModel.processConsumed();
            }
        });
    }

    @Override
    public void onProcessing() {
        //  TODO: Show a progress dialog
        Timber.d("Show a progress dialog");

    }

    @Override
    public void onSuccessProcess() {
        // TODO: Hide the progress dialog and present something like a check
        Timber.d("Progress success");
    }


}
