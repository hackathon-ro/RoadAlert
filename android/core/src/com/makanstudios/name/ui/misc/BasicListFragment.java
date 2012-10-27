
package com.makanstudios.name.ui.misc;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.kaciula.utils.ui.DialogUtils;

/**
 * BasicListFragment.java - Common operations done by a list fragment
 * 
 * @author ka
 */
public class BasicListFragment extends SherlockListFragment {

    /**
     * The activity that hosts the fragment deals with all dialog interactions
     */
    protected DialogUtils.BasicDialogInterface dialogListener;

    protected Handler handler = new Handler();

    /**
     * We keep a reference to the hosting activity for ease of use in cases
     * where activities can inherit from different classes
     */
    protected SherlockFragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (SherlockFragmentActivity) activity;
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
