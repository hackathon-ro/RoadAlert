
package com.makanstudios.roadalert.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kaciula.utils.misc.LogUtils;

/*
 * This receiver is called only if the activities are not currently active
 */
public class OnNewGcmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("New gcm notification on the floor");
    }
}
