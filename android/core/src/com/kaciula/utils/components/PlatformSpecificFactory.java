
package com.kaciula.utils.components;

import com.kaciula.utils.misc.MiscConstants;
import com.kaciula.utils.net.GingerbreadWebService;
import com.kaciula.utils.net.IWebService;
import com.kaciula.utils.net.LegacyWebService;

/**
 * Returns the most efficient implementation based on the current Android
 * version
 * 
 * @author ka
 */
public class PlatformSpecificFactory {

    /**
     * @return IStrictMode implementation if available
     */
    public static IStrictMode getStrictMode() {
        if (MiscConstants.SUPPORTS_GINGERBREAD)
            return new LegacyStrictMode();
        else
            return null;
    }

    /**
     * @return a SharedPreferenceSaver implementation based on the current
     *         Android version
     */
    public static SharedPreferenceSaver getSharedPreferenceSaver() {
        return MiscConstants.SUPPORTS_GINGERBREAD ? new GingerbreadSharedPreferenceSaver()
                : new LegacySharedPreferenceSaver();
    }

    /**
     * @return a IWebService http implementation based on the current Android
     *         version
     */
    public static IWebService getWebService() {
        return MiscConstants.SUPPORTS_GINGERBREAD ? new GingerbreadWebService()
                : new LegacyWebService();
    }

    /**
     * @return a IWebService https implementation based on the current Android
     *         version
     */
    public static IWebService getWebService(int certRawId, String storeType, String storePass,
            int type) {
        return MiscConstants.SUPPORTS_GINGERBREAD ? new GingerbreadWebService(certRawId, storeType,
                storePass, type) : new LegacyWebService(certRawId, storeType, storePass, type);
    }
}
