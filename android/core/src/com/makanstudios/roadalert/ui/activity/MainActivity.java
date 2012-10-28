
package com.makanstudios.roadalert.ui.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cyrilmottier.polaris.Annotation;
import com.cyrilmottier.polaris.MapCalloutView;
import com.cyrilmottier.polaris.PolarisMapView;
import com.cyrilmottier.polaris.PolarisMapView.OnAnnotationSelectionChangedListener;
import com.cyrilmottier.polaris.PolarisMapView.OnRegionChangedListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.kaciula.utils.misc.LogUtils;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.net.DatabaseHandler;
import com.makanstudios.roadalert.net.SendRoadAlertService;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;
import com.makanstudios.roadalert.ui.misc.RoadAlertApplication;
import com.makanstudios.roadalert.utils.Config;
import com.makanstudios.roadalert.utils.NotificationUtils;

public class MainActivity extends MapActivity implements OnRegionChangedListener,
        OnAnnotationSelectionChangedListener, OnClickListener {
    private static final String LOG_TAG = "MainActivity";

    private PolarisMapView mMapView;

    private Cursor mCursor;

    private AlertsContentObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.alert).setOnClickListener(this);

        mMapView = (PolarisMapView) findViewById(R.id.polaris_map_view);
        mMapView.setUserTrackingButtonEnabled(true);
        mMapView.setOnRegionChangedListenerListener(this);
        mMapView.setOnAnnotationSelectionChangedListener(this);

        mMapView.getController().setZoom(14);

        mCursor = DatabaseHandler.getAlerts();
        observer = new AlertsContentObserver(new Handler());

        updateCurrentLocation();
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null)
            mCursor.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
        getContentResolver().registerContentObserver(Alerts.CONTENT_URI, true, observer);

        final IntentFilter lftIntentFilter = new IntentFilter(
                LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        registerReceiver(lftBroadcastReceiver, lftIntentFilter);

        LocationLibrary.forceLocationUpdate(this);
        updateViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
        getContentResolver().unregisterContentObserver(observer);

        unregisterReceiver(lftBroadcastReceiver);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onRegionChanged(PolarisMapView mapView) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onRegionChanged");
        }
    }

    @Override
    public void onRegionChangeConfirmed(PolarisMapView mapView) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onRegionChangeConfirmed");
        }
    }

    @Override
    public void onAnnotationSelected(PolarisMapView mapView, MapCalloutView calloutView,
            int position, Annotation annotation) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onAnnotationSelected");
        }
        calloutView.setDisclosureEnabled(true);
        calloutView.setClickable(true);
        if (!TextUtils.isEmpty(annotation.getSnippet())) {
            calloutView.setLeftAccessoryView(getLayoutInflater().inflate(R.layout.accessory,
                    calloutView, false));
        } else {
            calloutView.setLeftAccessoryView(null);
        }
    }

    @Override
    public void onAnnotationDeselected(PolarisMapView mapView, MapCalloutView calloutView,
            int position, Annotation annotation) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onAnnotationDeselected");
        }
    }

    @Override
    public void onAnnotationClicked(PolarisMapView mapView, MapCalloutView calloutView,
            int position, Annotation annotation) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onAnnotationClicked");
        }
        Toast.makeText(this, getString(R.string.annotation_clicked, annotation.getTitle()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SendRoadAlertService.class);
        startService(intent);
    }

    private class AlertsContentObserver extends ContentObserver {

        public AlertsContentObserver(Handler handler) {
            super(handler);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            LogUtils.d("on CHAnge");
            if (mCursor != null)
                mCursor.requery();
            updateViews();
        }
    }

    private void updateViews() {
        if (mCursor != null) {
            final ArrayList<Annotation> annotations = new ArrayList<Annotation>();

            while (mCursor.moveToNext()) {
                int alertId = (int) mCursor.getLong(AlertsQuery.ALERT_ID);
                int lat = (int) mCursor.getLong(AlertsQuery.LAT);
                int lon = (int) mCursor.getLong(AlertsQuery.LON);
                annotations.add(new Annotation(new GeoPoint(lat, lon), "Alert " + alertId));

                LogUtils.d("id: " + alertId);
                LogUtils.d("lat: " + lat);
                LogUtils.d("lon: " + lon);
                LogUtils.d("timestamp: " + mCursor.getLong(AlertsQuery.TIMESTAMP));
                LogUtils.d("-----");
            }

            mMapView.setAnnotations(annotations, R.drawable.map_pin_holed_blue);

        }
    }

    private void updateCurrentLocation() {
        LogUtils.d("HELLOO", "update current location");
        LocationInfo info = RoadAlertApplication.currentLocationData.currentLocationInfo;
        if (info == null)
            return;

        GeoPoint point = new GeoPoint((int) (info.lastLat * 1E6),
                (int) (info.lastLong * 1E6));
        LogUtils.d("HELLOOO", "update current location " + point.getLatitudeE6() + " "
                + point.getLongitudeE6());
        mMapView.getController().setCenter(point);
        mMapView.invalidate();
    }

    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d("Received location update");
            // extract the location info in the broadcast
            final LocationInfo locationInfo = (LocationInfo) intent
                    .getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            RoadAlertApplication.currentLocationData.setCurrentLocation(locationInfo);
            NotificationUtils.showNotification();
            updateCurrentLocation();
        }
    };
}
