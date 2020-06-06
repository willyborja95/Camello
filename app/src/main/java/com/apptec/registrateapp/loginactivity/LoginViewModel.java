package com.apptec.registrateapp.loginactivity;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.R;

public class LoginViewModel extends AndroidViewModel {


    private MutableLiveData<LoginFormState> loginFormState;
    private MutableLiveData<LoginResult> loginResult;


    // Presenter that do hard work
    private LoginPresenter loginPresenter;


    public LoginViewModel(@NonNull Application application) {
        /** Constructor */
        super(application);

        loginFormState = new MutableLiveData<>();
        loginResult = new MutableLiveData<>();

        loginPresenter = new LoginPresenter();


    }

    /**
     * Expose the data
     */
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     *
     */
    public void loginClicked() {
        /**
         * Called when the login button is clicked
         * - Validates username and password
         *
         */


    }


    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }


}
