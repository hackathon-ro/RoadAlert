
package com.makanstudios.roadalert.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kaciula.utils.misc.LogUtils;
import com.makanstudios.roadalert.net.UpdateRoadAlertService;

/**
 * A simple {@link BroadcastReceiver} that triggers a sync. This is used by the
 * GCM code to trigger jittered syncs using {@link android.app.AlarmManager}.
 */
public class TriggerSyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("Trigger sync");

        // TODO: get wake lock
        Intent newIntent = new Intent(context, UpdateRoadAlertService.class);
        context.startService(newIntent);
    }
}
