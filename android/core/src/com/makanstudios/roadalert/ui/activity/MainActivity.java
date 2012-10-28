
package com.makanstudios.roadalert.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.net.DatabaseHandler;
import com.makanstudios.roadalert.net.SendRoadAlertService;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;
import com.makanstudios.roadalert.utils.Config;

public class MainActivity extends MapActivity implements OnRegionChangedListener,
        OnAnnotationSelectionChangedListener, OnClickListener, LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "MainActivity";

    // @formatter:off
    private static final Annotation[] sFrance = {
            new Annotation(
                    new GeoPoint(48635600, -1510600),
                    "Mont Saint Michel",
                    "Mont Saint-Michel is a rocky tidal island and a commune in Normandy, France. It is located approximately one kilometre (just over half a mile) off the country's north-western coast, at the mouth of the Couesnon River near Avranches."),
            new Annotation(new GeoPoint(48856600, 2351000), "Paris", "The city of love"),
            new Annotation(new GeoPoint(44837400, -576100), "Bordeaux",
                    "A port city in southwestern France"),
            new Annotation(new GeoPoint(48593100, -647500), "Domfront",
                    "A commune in the Orne department in north-western France"),
    };

    private PolarisMapView mMapView;

    private static final int LOADER_ID_DATA = 1;

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

        mCursor = DatabaseHandler.getAlerts();
        observer = new AlertsContentObserver(new Handler());

        updateViews();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
        getContentResolver().unregisterContentObserver(observer);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
        CursorLoader loader = null;

        switch (id) {
            case LOADER_ID_DATA:
                // TODO: Get only latest alerts on map
                String selection = Alerts.TIMESTAMP + ">=" + "0";
                loader = new CursorLoader(this, Alerts.CONTENT_URI,
                        AlertsQuery.PROJECTION, selection, null, null);
                break;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        updateViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mCursor = null;
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
            mMapView.getController().setZoom(16);
        }
    }
}
