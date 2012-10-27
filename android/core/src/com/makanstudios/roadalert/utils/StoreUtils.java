
package com.makanstudios.roadalert.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.kaciula.utils.misc.IntentUtils;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.R;

public class StoreUtils {

    public static void goToApp(Activity act, String packageName, String urlReferrer) {
        if (AppParams.isForGooglePlay) {
            Intent intent;
            if (TextUtils.isEmpty(urlReferrer))
                intent = IntentUtils.newMarketIntent(packageName);
            else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.setData(Uri.parse(urlReferrer));
            }

            if (IntentUtils.isIntentAvailable(intent))
                act.startActivity(intent);
            else
                Toast.makeText(BasicApplication.getContext(), R.string.no_google_play,
                        Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = IntentUtils.newAmazonStoreIntent(packageName);
            if (IntentUtils.isIntentAvailable(intent))
                act.startActivity(intent);
            else
                Toast.makeText(BasicApplication.getContext(), R.string.no_amazon_store,
                        Toast.LENGTH_SHORT).show();
        }
    }

    public static void goToApp(Activity act, String packageName) {
        goToApp(act, packageName, null);
    }

}
