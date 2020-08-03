package com.apptec.camello.mainactivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.camello.R;
import com.apptec.camello.util.EventListener;
import com.apptec.camello.util.EventObserver;

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
    protected CustomProgressDialog dialog;

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
        mainViewModel.getProcess().observe(this, new EventObserver<>(new EventListener<Process>() {
            @Override
            public void onEvent(Process process) {
                Timber.d("Observing the process");

                if (process == null) {
                    Timber.d("Null process");
                    // Dismiss all dialogs
                    // onNullProcess();
                } else if (process.getProcessStatus() == Process.PROCESSING) {
                    onProcessing(process.getTitleMessage(), process.getMessage());
                } else if (process.getProcessStatus() == Process.SUCCESSFUL) {
                    onSuccessProcess(process.getTitleMessage(), process.getMessage());
                } else if (process.getProcessStatus() == Process.FAILED) {
                    onErrorOccurred(process.getTitleMessage(), process.getMessage());

                }
            }
        }));

    }

    /**
     * Method for dismiss
     */
    private void onNullProcess() {
        // Dismiss the progress dialog
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Method to show an error if it happens in some process running in background
     */
    @Override
    public void onErrorOccurred(int title, int message) {

        // Dismiss the progress dialog
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }


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
     * The process is running
     *
     * @param title   maybe if a custom title is set. It could be null
     * @param message maybe if a custom message is set. I t could be null
     */
    @Override
    public void onProcessing(@Nullable Integer title, @Nullable Integer message) {
        // Show a progress dialog
        Timber.d("Show a progress dialog");

        dialog = new CustomProgressDialog(this.getContext());


        dialog.setCancelable(false);

        dialog.setMessage(getString(R.string.processing));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_progress);


    }

    /**
     * Process finished right
     *
     * @param title
     * @param message
     */
    @Override
    public void onSuccessProcess(@Nullable Integer title, @Nullable Integer message) {
        // Present something like a check
        Timber.d("Progress success");

        if (dialog != null && dialog.isShowing()) {

            dialog.setContentView(R.layout.dialog_success);
            dialog.setCancelable(true);

            if (title != null) {
                dialog.setMessage(getString(title));
            }

            dialog.setOnCancelListener(dialog -> mainViewModel.processConsumed());
            dialog.setOnDismissListener(dialog -> mainViewModel.processConsumed());

        }

    }




}
