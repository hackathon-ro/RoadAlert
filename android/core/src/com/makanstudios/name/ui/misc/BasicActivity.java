
package com.makanstudios.name.ui.misc;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kaciula.utils.ui.DialogFragments;
import com.kaciula.utils.ui.DialogUtils;

public abstract class BasicActivity extends SherlockFragmentActivity implements
        DialogUtils.BasicDialogInterface {

    private boolean wasCreated;

    private boolean wasInterrupted;

    protected Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wasCreated = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wasInterrupted = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasCreated = wasInterrupted = false;
    }

    @Override
    public void showMyDialog(final int dialogId) {
        showDialogs(dialogId, null, null, null, 0, 0, 0);
    }

    @Override
    public void showMyDialog(final int dialogId, String message) {
        showDialogs(dialogId, message, null, null, 0, 0, 0);
    }

    @Override
    public void showMyDialog(int dialogId, int year, int month, int day) {
        showDialogs(dialogId, null, null, null, year, month, day);
    }

    @Override
    public void showMyDialog(int dialogId, int hour, int minute, boolean is24Hour) {
        // Override in child activities
    }

    @Override
    public void showMyDialog(final int dialogId, String title, String[] items) {
        showDialogs(dialogId, null, title, items, 0, 0, 0);
    }

    private void showDialogs(final int dialogId, String message, String title, String[] items,
            int year, int month, int day) {
        final DialogFragment df;
        final boolean cancelable;

        switch (DialogUtils.getType(dialogId)) {
            case DialogUtils.DIALOG_TYPE_LOADING:
            case DialogUtils.DIALOG_TYPE_MESSAGE:
                df = DialogFragments.newInstance(dialogId, message);
                cancelable = false;
                break;
            case DialogUtils.DIALOG_TYPE_ITEMS:
                df = DialogFragments.newInstance(dialogId, title, items);
                cancelable = true;
                break;
            case DialogUtils.DIALOG_TYPE_DATE:
                df = DialogFragments.newInstance(dialogId, year, month, day);
                cancelable = false;
                break;
            default:
                return;

        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                df.setCancelable(cancelable);
                df.show(getSupportFragmentManager(), DialogUtils.getTag(dialogId));
            }
        });
    }

    @Override
    public void onItemSelected(int dialogId, int which) {
        // Override in child activities
    }

    @Override
    public void onDateSet(int dialogId, DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Override in child activities
    }

    @Override
    public void onTimeSet(int dialogId, TimePicker view, int hour, int minute) {
        // Override in child activities
    }

    @Override
    public void dismissMyDialog(final int dialogId, final boolean allowStateLoss) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment frag = getSupportFragmentManager().findFragmentByTag(
                        DialogUtils.getTag(dialogId));
                if (frag != null)
                    ft.remove(frag);
                if (!allowStateLoss)
                    ft.commit();
                else
                    ft.commitAllowingStateLoss();
            }
        });
    }

    public boolean isRestoring() {
        return wasInterrupted;
    }

    public boolean isResuming() {
        return !wasCreated;
    }

    public boolean isLaunching() {
        return !wasInterrupted && wasCreated;
    }
}
