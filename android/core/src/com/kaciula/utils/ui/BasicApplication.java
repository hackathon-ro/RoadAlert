
package com.kaciula.utils.ui;

import android.app.Application;
import android.content.Context;

import com.kaciula.utils.components.IStrictMode;
import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.misc.Config;

/**
 * BasicApplication.java - Common operations when initializing an application
 * 
 * @author ka
 */
public abstract class BasicApplication extends Application {

    private static Context context;

    /* Get the context when you don't have access to it in any other way */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BasicApplication.context = getApplicationContext();

        /*
         * When in developer mode, we use StrictMode (if available) to detect accidental 
         * disk/network access on the application's main thread 
         */
        if (Config.USE_STRICT_MODE) {
            IStrictMode strictMode = PlatformSpecificFactory.getStrictMode();
            if (strictMode != null)
                strictMode.enableStrictMode();
        }

    }
}
