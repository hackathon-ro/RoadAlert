
package com.makanstudios.roadalert.ui.misc;

import android.util.SparseIntArray;

import com.kaciula.utils.ui.DialogUtils;

public class DialogConstants {

    public static final int DIALOG_ID_MISC_PROGRESS = 0;

    public static final SparseIntArray mapping;

    static {
        mapping = new SparseIntArray();
        mapping.put(DIALOG_ID_MISC_PROGRESS, DialogUtils.DIALOG_TYPE_LOADING);
    }
}
