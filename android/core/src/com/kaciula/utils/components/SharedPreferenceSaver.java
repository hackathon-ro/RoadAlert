
package com.kaciula.utils.components;

import android.content.SharedPreferences;

/**
 * Abstract base class that can be extended to provide classes that save
 * {@link SharedPreferences} in the most efficient way possible.
 */
public abstract class SharedPreferenceSaver {

    protected SharedPreferenceSaver() {
    }

    /**
     * Save the Shared Preferences modified through the Editor object.
     * 
     * @param editor Shared Preferences Editor to commit.
     */
    public void savePreferences(SharedPreferences.Editor editor) {
    }
}
