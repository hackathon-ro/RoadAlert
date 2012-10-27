
package com.makanstudios.roadalert.ui.misc;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;

import com.kaciula.utils.ui.DialogUtils;

/**
 * BasicListFragment.java - Common operations done by a list fragment
 * 
 * @author ka
 */
public class BasicListFragment extends ListFragment {

    /**
     * The activity that hosts the fragment deals with all dialog interactions
     */
    protected DialogUtils.BasicDialogInterface dialogListener;

    protected Handler handler = new Handler();

    /**
     * We keep a reference to the hosting activity for ease of use in cases
     * where activities can inherit from different classes
     */
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (FragmentActivity) activity;
            dialogListener = (DialogUtils.BasicDialogInterface) activity;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    @Override
    public void onDetach() {
        this.mActivity = null;
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
