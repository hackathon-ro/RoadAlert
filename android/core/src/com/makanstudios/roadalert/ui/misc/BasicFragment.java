
package com.makanstudios.roadalert.ui.misc;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.kaciula.utils.ui.DialogUtils;

/**
 * BasicFragment.java - Common operations done by a fragment
 * 
 * @author ka
 */
public class BasicFragment extends Fragment {

    /**
     * The activity that hosts the fragment deals with all dialog interactions
     */
    protected DialogUtils.BasicDialogInterface dialogListener;

    protected Handler handler = new Handler();

    /**
     * We keep a reference to the hosting activity for ease of use in cases
     * where activities can inherit from different classes
     */
    protected Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            dialogListener = (DialogUtils.BasicDialogInterface) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        dialogListener = null;
        super.onDetach();
    }

    /**
     * @return the dialog listener associated with this fragment
     */
    public DialogUtils.BasicDialogInterface getDialogListener() {
        return dialogListener;
    }

    public Uri getUri() {
        return getArguments().getParcelable("_uri");
    }

    public void setUri(Uri uri) {
        getArguments().putParcelable("_uri", uri);
    }
}
