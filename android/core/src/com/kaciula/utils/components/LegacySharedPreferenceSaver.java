
package com.kaciula.utils.components;

import android.content.SharedPreferences.Editor;

import com.kaciula.utils.misc.MiscUtils;

/**
 * LegacySharedPreferenceSaver.java - Save preferences on a different thread.
 * 
 * @author ka
 */
public class LegacySharedPreferenceSaver extends SharedPreferenceSaver {

    public LegacySharedPreferenceSaver() {
        super();
    }

    /**
     * Save preferences on a different thread than the UI thread.
     */
    @Override
    public void savePreferences(final Editor editor) {
        if (MiscUtils.isOnUiThread()) {
            new Thread() {
                @Override
                public void run() {
                    editor.commit();
                }
            }.start();
        } else
            editor.commit();
    }
}
