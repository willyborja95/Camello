package com.apptec.camello.mainactivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.camello.R;

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

    // Progress dialog
    protected ProgressDialog dialog;

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
            Timber.d("Observing the process");
            if (process == null) {
                Timber.d("Null process");
            } else if (process.getProcessStatus() == Process.PROCESSING) {
                onProcessing();
            } else if (process.getProcessStatus() == Process.SUCCESSFUL) {
                onSuccessProcess();
                process = null;
            } else if (process.getProcessStatus() == Process.FAILED) {
                onErrorOccurred(process.getTitleError(), process.getError());
                process = null;
            }
        });

    }

    /**
     * Method to show an error if it happens in some process running in background
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
        dialog.show(getChildFragmentManager(), "ErrorDialog");
    }

    /**
     * Called the moment when  we start a background process
     * Show a progress dialog
     */
    @Override
    public void onProcessing() {
        // Show a progress dialog
        Timber.d("Show a progress dialog");

        dialog = new ProgressDialog(this.getContext());


        dialog.setCancelable(false);

        dialog.setMessage(getString(R.string.processing));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_progress);


    }

    /**
     * Called when a process is successful
     * Present something like a check
     */
    @Override
    public void onSuccessProcess() {
        // Present something like a check
        Timber.d("Progress success");

        if (dialog != null && dialog.isShowing()) {

            dialog.setContentView(R.layout.dialog_success);
            dialog.setCancelable(true);

        }


    }


    protected void showProgressDialog() {

    }

    protected void showSuccessDialog() {
    }


}
