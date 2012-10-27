
package com.kaciula.utils.components;

import android.annotation.TargetApi;
import android.content.SharedPreferences;

/**
 * Save {@link SharedPreferences} using the asynchronous apply method available
 * in Gingerbread
 */
public class GingerbreadSharedPreferenceSaver extends LegacySharedPreferenceSaver {

    public GingerbreadSharedPreferenceSaver() {
        super();
    }

    @TargetApi(9)
    @Override
    public void savePreferences(SharedPreferences.Editor editor) {
        editor.apply();
    }
}
