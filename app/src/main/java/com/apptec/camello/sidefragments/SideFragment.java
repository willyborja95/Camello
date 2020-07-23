package com.apptec.camello.sidefragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentWebViewBinding;

import timber.log.Timber;

/**
 * Base fragment for the side fragments
 */
public abstract class SideFragment extends Fragment {


    // Using data binding
    FragmentWebViewBinding binding;


    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false);


        Timber.d("On create fragment");


        getActivity().setTheme(R.style.AppTheme_NoActionBar);

        binding.webView.loadUrl(getURL());


        return binding.getRoot();
    }

    /**
     * @return The url to be presented in the fragment
     */
    public abstract String getURL();


}
