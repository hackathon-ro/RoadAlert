
package com.kaciula.utils.misc;

import java.io.File;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Common utilities to build Intents for various purposes
 * 
 * @author ka
 */
public class IntentUtils {

    /**
     * Check if an Intent is available
     * 
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Intent intent) {
        List<ResolveInfo> list = BasicApplication.getContext().getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Check if an Intent is available
     * 
     * @param context
     * @param action
     * @param mimeType
     * @return
     */
    public static boolean isIntentAvailable(String action, String mimeType) {
        final Intent intent = new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = BasicApplication.getContext().getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Create an intent to launch an email client
     * 
     * @param ctx
     * @param address
     * @param subject
     * @param body
     * @param isHtml
     * @param attachUri
     * @return
     */
    public static Intent newEmailIntent(String address, String subject, String body,
            boolean isHtml, Uri attachUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType(MiscConstants.MIME_TYPE_EMAIL);

        if (address != null)
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
                address
            });

        if (subject != null)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (body != null)
            if (isHtml)
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
            else
                intent.putExtra(Intent.EXTRA_TEXT, body);

        if (attachUri != null)
            intent.putExtra(Intent.EXTRA_STREAM, attachUri);

        return intent;
    }

    /**
     * Create an intent to share using all applications that support sharing
     * 
     * @param context
     * @param subject
     * @param message
     * @param chooserDialogTitle
     * @return
     */
    public static Intent newShareIntent(String subject, String message, String address,
            String chooserDialogTitle) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType(MiscConstants.MIME_TYPE_TEXT);

        if (subject != null)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (message != null)
            intent.putExtra(Intent.EXTRA_TEXT, message);

        if (address != null)
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
                address
            });

        return Intent.createChooser(intent, chooserDialogTitle);
    }

    /**
     * Create an intent to launch a sms client
     * 
     * @param ctx
     * @param phone
     * @param text
     * @return
     */
    public static Intent newSmsIntent(String phone, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        Uri data = null;
        if (phone != null)
            data = Uri.parse(MiscConstants.CONTENT_SCHEME_SMS + phone);
        else
            data = Uri.parse(MiscConstants.CONTENT_SCHEME_SMS);
        intent.setData(data);

        if (text != null)
            intent.putExtra(MiscConstants.EXTRA_SMS_BODY, text);

        return intent;
    }

    /**
     * Create an Intent to launch the dialer and call a number
     * 
     * @param ctx
     * @param phone
     * @return
     */
    public static Intent newCallPhoneIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        if (phone != null)
            intent.setData(Uri.parse(MiscConstants.CONTENT_SCHEME_TEL + phone.replace(" ", "")));
        else
            intent.setData(Uri.parse(MiscConstants.CONTENT_SCHEME_TEL));

        return intent;
    }

    /**
     * Create an Intent to launch the dialer with a number
     * 
     * @param ctx
     * @param phone
     * @return
     */
    public static Intent newDialPhoneIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        if (phone != null)
            intent.setData(Uri.parse(MiscConstants.CONTENT_SCHEME_TEL + phone.replace(" ", "")));
        else
            intent.setData(Uri.parse(MiscConstants.CONTENT_SCHEME_TEL));

        return intent;
    }

    /**
     * Create an Intent to launch google maps with a route
     * 
     * @param ctx
     * @param srcLat
     * @param srcLong
     * @param destLat
     * @param destLong
     * @return
     */
    public static Intent newMapsIntent(double srcLat, double srcLong, double destLat,
            double destLong) {
        Uri link = Uri.parse(String.format(MiscConstants.NET_SCHEME_GOOGLE_MAPS, srcLat, srcLong,
                destLat, destLong));
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, link);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        return intent;
    }

    /**
     * Create an Intent to launch google maps with an address
     * 
     * @param address
     * @param placeTitle
     * @return
     */
    public static Intent newMapsIntent(String address, String placeTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append("geo:0,0?q=");

        String addressEncoded = Uri.encode(address);
        sb.append(addressEncoded);
        // pass text for the info window
        String titleEncoded = Uri.encode("(" + placeTitle + ")");
        sb.append(titleEncoded);
        // set locale; probably not required for the maps app?
        sb.append("&hl=" + Locale.getDefault().getLanguage());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        return intent;
    }

    /**
     * Create an Intent to launch gallery-like apps to select a picture
     * 
     * @return
     */
    public static Intent newSelectPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return intent;
    }

    /**
     * Create an Intent to launch the camera to take a picture
     * 
     * @param tempFile
     * @return
     */
    public static Intent newTakePictureIntent(File tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        return intent;
    }

    /**
     * Create an Intent to go to the Home screen
     * 
     * @return
     */
    public static Intent newHomeIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * Create an Intent to go to the android marketplace to a specific
     * application
     * 
     * @param packageName
     * @return
     */
    public static Intent newMarketIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        return intent;
    }

    /**
     * Create an Intent to go to the amazon app store to a specific application
     * 
     * @param packageName
     * @return
     */
    public static Intent newAmazonStoreIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setData(Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + packageName));
        return intent;
    }

    /**
     * Create an Intent to go to uninstall screen for a particular application
     * 
     * @param packageName
     * @return
     */
    public static Intent newUninstallIntent(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        return intent;
    }

    public static Intent newBrowserIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return intent;
    }

    public static void preferPackageForIntent(Intent intent, String packageName) {
        PackageManager pm = BasicApplication.getContext().getPackageManager();
        for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                intent.setPackage(packageName);
                break;
            }
        }
    }
}
