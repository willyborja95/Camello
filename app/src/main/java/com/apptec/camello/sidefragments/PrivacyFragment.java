package com.apptec.camello.sidefragments;

import com.apptec.camello.R;
import com.apptec.camello.util.Constants;

/**
 * Fragment for the privacy policy
 */
public class PrivacyFragment extends SideFragment {


    /**
     * @return The url to be presented in the fragment
     */
    @Override
    public String getURL() {
        return Constants.URL_PRIVACY_POLICY;
    }

    /**
     * @return The resource id of with the title of the fragment
     */
    @Override
    public String getFragmentsTitle() {
        return getString(R.string.privacy_policy_fragment_title);
    }

}
