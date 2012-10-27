
package com.kaciula.utils.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Miscellaneous utilities
 * 
 * @author ka
 */
public class MiscUtils {

    private static final String TAG = LogUtils.makeLogTag(MiscUtils.class.getSimpleName());

    public static final int API_LEVEL = Build.VERSION.SDK_INT;

    private static String uniqueDeviceId;

    private static final String DEFAULT_DEVICE_ID = "987654321";

    /**
     * Generate a random id
     * 
     * @param len
     * @return
     */
    public static String generateRandomId(int len) {
        StringBuilder sb = new StringBuilder(len);
        Random randomGenerator = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(randomGenerator.nextInt(10));
        }

        return sb.toString();
    }

    public static String getUniqueDeviceId() {
        if (uniqueDeviceId == null)
            uniqueDeviceId = createUniqueDeviceId();

        return uniqueDeviceId;
    }

    private static String createUniqueDeviceId() {
        return createUniqueDeviceId(0);
    }

    /**
     * Create a unique device id
     * 
     * @param ctx
     * @param size
     * @return
     */
    private static String createUniqueDeviceId(int size) {
        String androidId = Settings.Secure.getString(BasicApplication.getContext()
                .getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        LogUtils.d(TAG, "Android id = " + androidId);
        if (androidId == null)
            androidId = "";

        String tmDeviceId = null;
        PackageManager pm = BasicApplication.getContext().getPackageManager();

        // it's possible that on tablets we don't have this feature
        if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            TelephonyManager tm = (TelephonyManager) BasicApplication.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            tmDeviceId = tm.getDeviceId();
            LogUtils.d(TAG, "Device id = " + tmDeviceId);
        }
        if (tmDeviceId == null)
            tmDeviceId = "";

        String uniqueId = androidId + tmDeviceId;

        if (uniqueId.length() == 0)
            uniqueId = DEFAULT_DEVICE_ID;

        // Fill or cut ID so that it has the desired length
        if (size > 0) {
            int len = uniqueId.length();
            if (len > size)
                uniqueId = uniqueId.substring(0, size);
            else if (len < size) {
                StringBuilder sb = new StringBuilder(size);
                sb.append(uniqueId);
                while (sb.length() < size) {
                    sb.append("0"); // random number
                }
                uniqueId = sb.toString();
            }
        }

        LogUtils.d(TAG, "Unique id = " + uniqueId);

        return uniqueId;
    }

    /**
     * Get application version
     * 
     * @param context
     * @return
     */
    public static String getApplicationVersion() {
        try {
            PackageManager pm = BasicApplication.getContext().getPackageManager();
            PackageInfo info = pm.getPackageInfo(BasicApplication.getContext().getPackageName(), 0);
            return "v" + info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getCurrentVersionCode() {
        try {
            PackageManager pm = BasicApplication.getContext().getPackageManager();
            PackageInfo info = pm.getPackageInfo(BasicApplication.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDeviceInfo() {
        StringBuilder sb = new StringBuilder();

        try {
            PackageManager pm = BasicApplication.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BasicApplication.getContext().getPackageName(), 0);

            sb.append("Device Info:\n");
            sb.append("App package ").append(pi.packageName).append('\n');
            sb.append("App version ").append(pi.versionName).append('\n');
            sb.append(Build.MANUFACTURER).append(' ').append(Build.MODEL).append('\n');
            sb.append("Android version ").append(Build.VERSION.RELEASE);
        } catch (Exception e) {

        }

        return sb.toString();
    }

    /**
     * Check if text is valid
     * 
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        if (TextUtils.isEmpty(s))
            return false;

        return TextUtils.getTrimmedLength(s) != 0;
    }

    /**
     * Clean-up a string to make sure it is valid
     * 
     * @param s
     * @return
     */
    public static String getSanitized(String s) {
        if (isValid(s))
            return s.trim();

        return null;
    }

    /**
     * Clean-up a link to make sure it is valid
     * 
     * @param link
     * @return
     */
    public static String getSanitizedLink(String link) {
        String val = getSanitized(link);

        if (val == null)
            return null;

        try {
            new java.net.URI(val);
        } catch (URISyntaxException urise) {
            // url badly formed
            return null;
        }

        return val;
    }

    /**
     * Common idiom when populating UI
     * 
     * @param view
     * @param text
     */
    public static void showOrVanish(TextView view, String text) {
        if (!TextUtils.isEmpty(text))
            view.setText(text);
        else
            view.setVisibility(View.GONE);
    }

    /**
     * Ellipsize text
     * 
     * @param s
     * @param len
     * @return
     */
    public static String ellipsize(String s, int len) {
        if (s.length() > len)
            return s.substring(0, len - 3) + "...";

        return s;
    }

    /**
     * Parse by hand some simple html
     * 
     * @param html
     * @return
     */
    public static String parseExtraHtml(String html) {
        int start = html.indexOf("<ul>");
        int end = html.indexOf("</ul>");

        if (start >= 0 && start < end) {
            String ul = html.substring(start, end);
            String newUl = ul.replace("<li>", "&#x25E6  ");
            newUl = newUl.replace("</li>", "<br/>");
            newUl = newUl.replace("<ul>", "");
            newUl = newUl.replace("</ul>", "");
            html = html.replace(ul, newUl);
        }

        return html;
    }

    /**
     * Is portrait orientation
     * 
     * @param ctx
     * @return
     */
    public static boolean isPortrait() {
        return BasicApplication.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Is landscape orientation?
     * 
     * @param ctx
     * @return
     */
    public static boolean isLandscape() {
        return BasicApplication.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Send a sms directly
     * 
     * @param ctx
     * @param smsPhone
     * @param smsText
     */
    public static void sendSmsDirectly(String smsPhone, String smsText) {
        SmsManager manager = SmsManager.getDefault();
        List<String> messages = manager.divideMessage(smsText);
        for (String message : messages) {
            // use empty intents because of an Android bug
            PendingIntent sent = PendingIntent.getBroadcast(BasicApplication.getContext(), 0,
                    new Intent(), 0);
            PendingIntent delivered = PendingIntent.getBroadcast(BasicApplication.getContext(), 0,
                    new Intent(), 0);
            manager.sendTextMessage(smsPhone, null, message, sent, delivered);
        }
    }

    /**
     * Get the path of an image from gallery
     * 
     * @param ctx
     * @param uri
     * @return
     */
    public static String getGalleryImagePath(Uri uri) {
        String path = null;
        Cursor cursor = null;

        try {
            String[] projection = {
                    MediaColumns.DATA, MediaColumns.DISPLAY_NAME
            };

            cursor = BasicApplication.getContext().getContentResolver()
                    .query(uri, projection, null, null, null);

            LogUtils.d(TAG, "Get gallery image path from uri " + uri);
            if (cursor != null && cursor.moveToFirst()) {
                if (isPicasaContentUri(uri)) {
                    LogUtils.d(TAG, "It is a picasa image on devices 3.0 and up");
                    int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        LogUtils.d(TAG, "And the uri for the image is this one " + path);
                        path = uri.toString();
                    }
                } else { // it is a regular local image file
                    LogUtils.d(TAG, "It is a regular local image");
                    int columnIndex = cursor.getColumnIndex(MediaColumns.DATA);
                    path = cursor.getString(columnIndex);
                }
            }// If it is a picasa image on devices running OS prior to 3.0
            else if (!TextUtils.isEmpty(uri.toString())) {
                LogUtils.d(TAG, "It is a picasa image on devices prior to 3.0");
                path = uri.toString();
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return path;
    }

    // if it is a picasa image on newer devices with OS 3.0 and up
    public static boolean isPicasaContentUri(Uri uri) {
        return uri.toString().startsWith("content://com.android.gallery3d.provider") ||
                uri.toString().startsWith("content://com.google.android.gallery3d.provider/picasa")
                ||
                uri.toString().startsWith("content://com.android.sec.gallery3d") ||
                uri.toString().startsWith("content://com.sec.android.gallery3d");
    }

    public static int getGalleryImageOrientation(Uri uri) {
        Cursor cursor = null;
        int orientation = -1;

        try {
            String[] projection = {
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = BasicApplication.getContext().getContentResolver()
                    .query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                orientation = cursor.getInt(0);
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return orientation;
    }

    /**
     * Get the memory class of this device (approx. per-app memory limit)
     * 
     * @param context
     * @return
     */
    public static int getMemoryClass() {
        return ((ActivityManager) BasicApplication.getContext().getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static boolean isForGooglePlay(int sigDebug, int sigRelease) {
        try {
            Signature[] sigs = BasicApplication
                    .getContext()
                    .getPackageManager()
                    .getPackageInfo(BasicApplication.getContext().getPackageName(),
                            PackageManager.GET_SIGNATURES).signatures;
            for (Signature sig : sigs) {
                LogUtils.d("Signature hashcode : " + sig.hashCode());
                if (sig.hashCode() != sigDebug && sig.hashCode() != sigRelease)
                    return false;
            }
        } catch (NameNotFoundException nnfe) {
            LogUtils.printStackTrace(nnfe);
        }

        return true;
    }

    public static boolean isKindleFire() {
        return "Amazon".equalsIgnoreCase(Build.MANUFACTURER)
                && "Kindle Fire".equalsIgnoreCase(Build.MODEL);
    }

    public static int getPhotoOrientation(String fullPath) {
        try {
            ExifInterface exif = new ExifInterface(fullPath);

            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return ExifInterface.ORIENTATION_NORMAL;
    }

    public static int getPhotoRotateAngle(String fullPath) {
        try {
            ExifInterface exif = new ExifInterface(fullPath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                default:
                    return 0;
            }
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return 0;
    }

    public static void fixPhotoOrientation(String fullPath, int orientation) {
        int rotate = 0;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                LogUtils.d("ROTATE 270");
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                LogUtils.d("ROTATE 180");
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                LogUtils.d("ROTATE 90");
                rotate = 90;
                break;
            default:
                LogUtils.d("DON'T ROTATE");
                return;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap bmp = BitmapFactory.decodeFile(fullPath, options);

        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Matrix mtx = new Matrix();
        // Do here a postScale to scale and check if scale after height or width
        // (check MacawUtils)
        mtx.postRotate(rotate, w / 2, h / 2);
        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);

        try {
            FileOutputStream fos = new FileOutputStream(new File(fullPath));
            newBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException fnfe) {
            LogUtils.printStackTrace(fnfe);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        bmp.recycle();
        newBmp.recycle();
    }

    /*
     * Need to set permission android.permission.READ_LOGS in the application's
     * manifest From Jelly Bean onward, this doesn't work
     */
    public static void sendLogs(Context ctx, String email, int no) {
        try {
            String logcat = "logcat -d " + no;
            Process process = Runtime.getRuntime().exec(logcat);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
            }

            Intent intent = IntentUtils.newEmailIntent(email,
                    "Logs from user for package " + ctx.getPackageName(), log.toString(), false,
                    null);
            if (IntentUtils.isIntentAvailable(intent))
                ctx.startActivity(intent);
        } catch (IOException ioe) {
            Toast.makeText(BasicApplication.getContext(), "Unable to send logs", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static boolean hasPermission(String permission) {
        PackageManager pm = BasicApplication.getContext().getPackageManager();
        return pm.checkPermission(permission, BasicApplication.getContext().getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
