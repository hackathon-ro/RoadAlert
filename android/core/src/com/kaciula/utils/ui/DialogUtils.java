
package com.kaciula.utils.ui;

import android.util.SparseIntArray;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * DialogUtils.java - Defines the types of dialogs possible and keeps a mapping
 * between the available dialog ids and their type. Each application defines a
 * DialogConstants class where it sets the mapping.
 * 
 * @author ka
 */
public class DialogUtils {

    private static final String KEY_DIALOG_ID = "dialogId";

    public static final int DIALOG_TYPE_LOADING = 0;

    public static final int DIALOG_TYPE_ITEMS = 1;

    public static final int DIALOG_TYPE_MESSAGE = 2;

    public static final int DIALOG_TYPE_DATE = 3;

    public static final int DIALOG_TYPE_CUSTOM = 4;

    public static final int DIALOG_TYPE_TIME = 5;

    public static final int DIALOG_TYPE_2_BUTTONS_MESSAGE = 6;

    public static SparseIntArray mapping;

    public static String getTag(int dialogId) {
        return KEY_DIALOG_ID + dialogId;
    }

    public static int getType(int dialogId) {
        if (mapping != null)
            return mapping.get(dialogId);

        return DIALOG_TYPE_CUSTOM;
    }

    /**
     * This method is called when starting the application to set up the mapping
     * for the dialogs available
     * 
     * @param map
     */
    public static void setMapping(SparseIntArray map) {
        mapping = map;
    }

    /**
     * Interface for communicating with the dialog fragment
     * 
     * @author ka
     */
    public interface BasicDialogInterface {

        public void showMyDialog(int dialogId);

        public void showMyDialog(int dialogId, String message);

        public void showMyDialog(int dialogId, int year, int month, int day);

        public void showMyDialog(int dialogId, String title, String[] items);

        public void showMyDialog(int dialogId, int hour, int minute, boolean is24Hour);

        public void onItemSelected(int dialogId, int which);

        public void onDateSet(int dialogId, DatePicker view, int year, int monthOfYear,
                int dayOfMonth);

        public void onTimeSet(int dialogId, TimePicker view, int hour, int minute);

        public void dismissMyDialog(int dialogId, boolean allowStateLoss);
    }

    /**
     * Interface for communicating with the dialog fragment which contains also
     * buttons
     * 
     * @author ka
     */
    public interface ButtonsDialogInterface extends BasicDialogInterface {

        public void showMyDialog(int dialogId, String title, String message, String btnPositive,
                String btnNegative);

        public void onDialogPositiveButton(int dialogId);

        public void onDialogNegativeButton(int dialogId);
    }
}
