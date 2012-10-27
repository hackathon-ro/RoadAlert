
package com.makanstudios.roadalert.ui.misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kaciula.utils.misc.UiUtils;
import com.kaciula.utils.ui.DialogUtils;
import com.kaciula.utils.ui.DialogUtils.BasicDialogInterface;

/**
 * DialogFragments.java - Class that deals with all sorts of dialogs possible.
 * New types of dialogs should be added when necessary.
 * 
 * @author ka
 */
public class DialogFragments extends DialogFragment {

    private BasicDialogInterface dialogListener;

    private Activity activity;

    private static final String KEY_DIALOG_ID = "dialogId";

    private static final String KEY_MESSAGE = "message";

    private static final String KEY_YEAR = "year";

    private static final String KEY_MONTH = "month";

    private static final String KEY_DAY = "day";

    private static final String KEY_TITLE = "title";

    private static final String KEY_ITEMS = "items";

    private static final String KEY_HOUR = "hour";

    private static final String KEY_MINUTE = "minute";

    private static final String KEY_IS_24_HOUR = "is24Hour";

    private static final String KEY_BTN_POSITIVE = "btn_positive";

    private static final String KEY_BTN_NEGATIVE = "btn_negative";

    /**
     * Creates a plain old dialog fragment
     * 
     * @param dialogId - the id of the dialog wanted
     * @return a dialog fragment
     */
    public static DialogFragments newInstance(int dialogId) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Creates a dialog fragment which shows a message dialog
     * 
     * @param dialogId - the id of the dialog wanted
     * @param message - the message wanted in the dialog
     * @return a dialog fragment
     */
    public static DialogFragments newInstance(int dialogId, String message) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        args.putString(KEY_MESSAGE, message);
        frag.setArguments(args);

        return frag;
    }

    public static DialogFragments newInstance(int dialogId, String title, String message,
            String btnPositive, String btnNegative) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        args.putString(KEY_BTN_POSITIVE, btnPositive);
        args.putString(KEY_BTN_NEGATIVE, btnNegative);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Creates a dialog fragment which shows a datepicker
     * 
     * @param dialogId - the id of the dialog wanted
     * @param year - the year
     * @param month - the month
     * @param day - the day
     * @return a dialog fragment
     */
    public static DialogFragments newInstance(int dialogId, int year, int month, int day) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        args.putInt(KEY_YEAR, year);
        args.putInt(KEY_MONTH, month);
        args.putInt(KEY_DAY, day);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Creates a dialog fragment which contains a title and a list of items
     * 
     * @param dialogId - the id of the dialog wanted
     * @param title - the title
     * @param items - the items in the list
     * @return a dialog fragment
     */
    public static DialogFragments newInstance(int dialogId, String title, String items[]) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        args.putString(KEY_TITLE, title);
        args.putStringArray(KEY_ITEMS, items);
        frag.setArguments(args);

        return frag;
    }

    public static DialogFragments newInstance(int dialogId, int hour, int minute, boolean is24Hour) {
        DialogFragments frag = new DialogFragments();

        Bundle args = new Bundle();
        args.putInt(KEY_DIALOG_ID, dialogId);
        args.putInt(KEY_HOUR, hour);
        args.putInt(KEY_MINUTE, minute);
        args.putBoolean(KEY_IS_24_HOUR, is24Hour);
        frag.setArguments(args);

        return frag;
    }

    private int getDialogId() {
        return getArguments().getInt(KEY_DIALOG_ID);
    }

    private String getMessage() {
        return getArguments().getString(KEY_MESSAGE);
    }

    private String getBtnPositive() {
        return getArguments().getString(KEY_BTN_POSITIVE);
    }

    private String getBtnNegative() {
        return getArguments().getString(KEY_BTN_NEGATIVE);
    }

    private int getYear() {
        return getArguments().getInt(KEY_YEAR);
    }

    private int getMonth() {
        return getArguments().getInt(KEY_MONTH);
    }

    private int getDay() {
        return getArguments().getInt(KEY_DAY);
    }

    private String getTitle() {
        return getArguments().getString(KEY_TITLE);
    }

    private String[] getItems() {
        return getArguments().getStringArray(KEY_ITEMS);
    }

    private int getHour() {
        return getArguments().getInt(KEY_HOUR);
    }

    private int getMinute() {
        return getArguments().getInt(KEY_MINUTE);
    }

    private boolean is24Hour() {
        return getArguments().getBoolean(KEY_IS_24_HOUR);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            dialogListener = (BasicDialogInterface) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        dialogListener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;

        switch (DialogUtils.getType(getDialogId())) {
            case DialogUtils.DIALOG_TYPE_LOADING:
                return UiUtils.newLoadingDialog(activity, getMessage());
            case DialogUtils.DIALOG_TYPE_MESSAGE:
                return UiUtils.newMessageDialog(activity, null, getMessage(), "OK", 0);
            case DialogUtils.DIALOG_TYPE_2_BUTTONS_MESSAGE: {
                builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false);
                builder.setPositiveButton(getBtnPositive(), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null)
                            ((DialogUtils.ButtonsDialogInterface) dialogListener)
                                    .onDialogPositiveButton(getDialogId());
                    }
                });
                builder.setNegativeButton(getBtnNegative(), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null)
                            ((DialogUtils.ButtonsDialogInterface) dialogListener)
                                    .onDialogNegativeButton(getDialogId());
                    }
                });

                builder.setTitle(getTitle());
                builder.setMessage(getMessage());
                builder.setIcon(0);

                return builder.create();
            }
            case DialogUtils.DIALOG_TYPE_DATE:
                return new DatePickerDialog(activity, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (dialogListener != null)
                            dialogListener.onDateSet(getDialogId(), view, year, monthOfYear,
                                    dayOfMonth);
                    }
                }, getYear(), getMonth(), getDay());
            case DialogUtils.DIALOG_TYPE_ITEMS:
                builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                        android.R.style.Theme_Dialog));
                builder.setTitle(getTitle());
                builder.setItems(getItems(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null)
                            dialogListener.onItemSelected(getDialogId(), which);
                    }
                });

                return builder.create();
            case DialogUtils.DIALOG_TYPE_TIME:
                return new TimePickerDialog(activity, new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (dialogListener != null)
                            dialogListener.onTimeSet(getDialogId(), view, hourOfDay, minute);
                    }
                }, getHour(), getMinute(), is24Hour());
        }

        return null;
    }
}
