
package com.kaciula.utils.net;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.NetUtils;

public abstract class ApiService extends IntentService {

    protected String tag;

    public ApiService(String tag) {
        super(tag);
        this.tag = tag;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.d(tag, "Started");

        final ResultReceiver receiver = intent
                .getParcelableExtra(ServiceConstants.EXTRA_STATUS_RECEIVER);
        if (receiver != null)
            receiver.send(ServiceConstants.STATUS_RUNNING, Bundle.EMPTY);

        ServiceReturnInfo retInfo = null;

        if (NetUtils.isInternetAvailable())
            retInfo = onRequest(intent);
        else
            retInfo = new ServiceReturnInfo();

        if (receiver != null) {
            if (retInfo.success) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceConstants.EXTRA_RETURN_INFO, retInfo);

                receiver.send(ServiceConstants.STATUS_SUCCESS, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceConstants.EXTRA_RETURN_INFO, retInfo);

                receiver.send(ServiceConstants.STATUS_FAILURE, bundle);
            }
        }
    }

    protected abstract ServiceReturnInfo onRequest(Intent intent);
}
