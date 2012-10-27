
package com.kaciula.utils.misc;

import java.io.File;
import java.io.FileOutputStream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Watches SD card and deals with SD card operations
 * 
 * @author ka
 */
public class StorageHelper {

    private static final String TAG = "StorageHelper";

    private static BroadcastReceiver mExternalStorageReceiver;

    private static boolean mExternalStorageAvailable = false;

    private static boolean mExternalStorageWriteable = false;

    private static final String FORMAT_APP_ROOT_PATH = "%s/Android/data/%s";

    private static final String FORMAT_APP_FILES_PATH = FORMAT_APP_ROOT_PATH + "/files/%s";

    private static final String TEMP_PATH = "temp";

    private static final String ATTACH_PNG_NAME = "attach.png";

    // private static final String FORMAT_APP_CACHE_PATH = FORMAT_APP_ROOT_PATH
    // + "/cache/%s";

    /**
     * Register a receiver to receive updates pertaining to the SD card
     * 
     * @param ctx
     */
    public static void startWatchingExternalStorage() {
        if (mExternalStorageReceiver == null) {
            mExternalStorageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LogUtils.d(TAG, "External storage change");

                    updateExternalStorageState();
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            BasicApplication.getContext().registerReceiver(mExternalStorageReceiver, filter);
            updateExternalStorageState();
        }
    }

    public static void stopWatchingExternalStorage() {
        if (mExternalStorageReceiver != null)
            BasicApplication.getContext().unregisterReceiver(mExternalStorageReceiver);
        mExternalStorageReceiver = null;
    }

    private static void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public static boolean isAvailable() {
        return mExternalStorageAvailable;
    }

    public static boolean isAvailableAndWritable() {
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    /**
     * Get a bitmap
     * 
     * @param ctx
     * @param path
     * @param inSampleSize
     * @return
     */
    public static Bitmap getBitmap(String path, int inSampleSize) {
        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fullPath = String.format(FORMAT_APP_FILES_PATH, externalPath, BasicApplication
                    .getContext().getPackageName(), path);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFile(fullPath, options);
        }

        return null;
    }

    /**
     * Get a file
     * 
     * @param ctx
     * @param path
     * @return
     */
    public static File getFile(String path) {
        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fullPath = String.format(FORMAT_APP_FILES_PATH, externalPath, BasicApplication
                    .getContext().getPackageName(), path);
            File file = new File(fullPath);

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            return file;
        }

        return null;
    }

    /**
     * Delete a file
     * 
     * @param ctx
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fullPath = String.format(FORMAT_APP_FILES_PATH, externalPath, BasicApplication
                    .getContext().getPackageName(), path);
            File file = new File(fullPath);

            if (file.exists() && file.isFile()) {
                file.delete();
            }

            return true;
        }

        return false;
    }

    /**
     * Create a directory
     * 
     * @param ctx
     * @param path
     * @return
     */
    public static boolean createDir(String path) {
        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fullPath = String.format(FORMAT_APP_FILES_PATH, externalPath, BasicApplication
                    .getContext().getPackageName(), path);
            File file = new File(fullPath);

            if (!file.exists() || !file.isDirectory())
                file.mkdirs();

            return true;
        }

        return false;
    }

    /**
     * Create a image in the SD card so you can add it as an attachment to an
     * email
     * 
     * @param ctx
     * @param drawable
     * @return
     */
    public static Uri createAttachImage(Drawable drawable) {
        if (!mExternalStorageWriteable)
            return null;

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fullPath = String.format(FORMAT_APP_FILES_PATH, externalPath, BasicApplication
                .getContext().getPackageName(), TEMP_PATH);

        File tempDir = new File(fullPath);

        if (!tempDir.exists())
            tempDir.mkdirs();

        File pngFile = new File(tempDir, ATTACH_PNG_NAME);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        if (bd == null)
            return null;

        Bitmap bitmap = bd.getBitmap();
        try {
            FileOutputStream out = new FileOutputStream(pngFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Save file encoded as PNG
        Uri pngUri = Uri.fromFile(pngFile);

        return pngUri;
    }

    /**
     * SD card is available
     * 
     * @param ctx
     * @param fullPath
     * @return
     */
    public static boolean isAvailable(String fullPath) {
        if (mExternalStorageAvailable) {
            File file = new File(fullPath);
            return file.exists() && file.isFile();
        }

        return false;
    }

    public static String getFilesPath(String packageName) {
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fullPath = String.format(FORMAT_APP_ROOT_PATH + "/files", externalPath, packageName);
        return fullPath;
    }
}
