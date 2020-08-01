package com.apptec.camello.sidefragments;

import com.apptec.camello.R;
import com.apptec.camello.util.Constants;

/**
 * Fragment for show the user manual
 */
public class UserManualFragment extends SideFragment {


    /**
     * @return The url to be presented in the fragment
     */
    @Override
    public String getURL() {
        return Constants.URL_USER_MANUAL;
    }

    /**
     * @return The resource id of with the title of the fragment
     */
    @Override
    public String getFragmentsTitle() {
        return getString(R.string.user_manual_fragment_title);
    }

}
