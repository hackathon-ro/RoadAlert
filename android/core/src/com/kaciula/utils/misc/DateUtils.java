
package com.kaciula.utils.misc;

import org.joda.time.DateTime;

/**
 * Common date utilities. Makes use of the efficient joda time library.
 * 
 * @author ka
 */
public class DateUtils {

    public static String getThisMonthName() {
        DateTime dt = new DateTime();
        return dt.monthOfYear().getAsText();
    }
}
