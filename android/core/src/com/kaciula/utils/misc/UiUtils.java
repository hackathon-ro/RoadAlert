
package com.kaciula.utils.misc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Common utilities used to show various dialogs
 * 
 * @author ka
 */
public class UiUtils {

    /**
     * Convert from dip/dp to pixels
     * 
     * @param context
     * @param dip
     * @return
     */
    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = BasicApplication.getContext().getResources()
                .getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = BasicApplication.getContext().getResources()
                .getDisplayMetrics();
        WindowManager wm = (WindowManager) BasicApplication.getContext().getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = BasicApplication.getContext().getResources()
                .getDisplayMetrics();
        WindowManager wm = (WindowManager) BasicApplication.getContext().getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * Create a dialog with a title, a message and 2 buttons (yes and no)
     * 
     * @param context
     * @param dialogTitle
     * @param screenMessage
     * @param iconResourceId
     * @param listenerYes
     * @param listenerNo
     * @return
     */
    public static AlertDialog newYesNoDialog(final Context context, String dialogTitle,
            String screenMessage, int iconResourceId, DialogInterface.OnClickListener listenerYes,
            DialogInterface.OnClickListener listenerNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, listenerYes);
        builder.setNegativeButton(android.R.string.no, listenerNo);

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    /**
     * Create a dialog with a title, a message and 2 buttons (OK and Cancel)
     * 
     * @param context
     * @param dialogTitle
     * @param screenMessage
     * @param iconResourceId
     * @param listenerOk
     * @param listenerCancel
     * @return
     */
    public static AlertDialog newOkCancelDialog(final Context context, String dialogTitle,
            String screenMessage, int iconResourceId, DialogInterface.OnClickListener listenerOk,
            DialogInterface.OnClickListener listenerCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, listenerOk);
        builder.setNegativeButton(android.R.string.cancel, listenerCancel);

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    /**
     * Create a dialog with a message and a button
     * 
     * @param context
     * @param dialogTitle
     * @param screenMessage
     * @param btnText
     * @param iconResourceId
     * @return
     */
    public static AlertDialog newMessageDialog(final Context context, String dialogTitle,
            String screenMessage, String btnText, int iconResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    /**
     * Create a loading dialog
     * 
     * @param context
     * @param loadMessage
     * @return
     */
    public static ProgressDialog newLoadingDialog(final Context context, String loadMessage) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(loadMessage);
        dialog.setIndeterminate(true);

        return dialog;
    }

    /**
     * Format phone number US style
     * 
     * @param phoneNo
     * @return
     */
    public static String formatPhoneNumber(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo) && phoneNo.length() == 10) {
            StringBuilder sb = new StringBuilder(14);
            sb.append("(").append(phoneNo.substring(0, 3)).append(") ")
                    .append(phoneNo.substring(3, 6)).append("-").append(phoneNo.substring(6, 10));
            return sb.toString();
        }

        return phoneNo;
    }

    /**
     * Remove whitespaces from a phone number
     * 
     * @param phoneNo
     * @return
     */
    public static String unformatPhoneNumber(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo))
            return phoneNo.replaceAll("\\D", "");

        return phoneNo;
    }

    /**
     * Format zip number
     * 
     * @param zipNo
     * @return
     */
    public static String formatZipNumber(String zipNo) {
        if (!TextUtils.isEmpty(zipNo) && zipNo.length() == 9) {
            StringBuilder sb = new StringBuilder(10);
            sb.append(zipNo.substring(0, 5)).append("-").append(zipNo.subSequence(5, 9));
            return sb.toString();
        }

        return zipNo;
    }

    /**
     * Hide soft keyboard
     * 
     * @param ctx
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) BasicApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show soft keyboard
     * 
     * @param ctx
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) BasicApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, 0);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    /**
     * Format different parts of a text, such as making bold, italic, setting
     * different color etc.
     * 
     * @param text
     * @param token
     * @param cs
     * @return
     */
    public static CharSequence setSpanBetweenTokens(CharSequence text, String token,
            CharacterStyle... cs) {
        // Start and end refer to the points where the span will apply
        int tokenLen = token.length();
        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        if (start > -1 && end > -1) {
            // Copy the spannable string to a mutable spannable string
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            for (CharacterStyle c : cs)
                ssb.setSpan(c, start, end, 0);

            // Delete the tokens before and after the span
            ssb.delete(end, end + tokenLen);
            ssb.delete(start - tokenLen, start);

            text = ssb;
        }

        return text;
    }

    /**
     * Format different parts of a text, such as making bold, italic, setting
     * different color etc. Also make the respectiv portion if text be
     * clickable. Also set if underline or not
     * 
     * @param text
     * @param token
     * @param listener
     * @param cs
     * @return
     */
    public static CharSequence setLinkBetweenTokens(CharSequence text, String token,
            OnClickListener listener, boolean underline, CharacterStyle... cs) {
        // Start and end refer to the points where the span will apply
        int tokenLen = token.length();
        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        if (start > -1 && end > -1) {
            // Copy the spannable string to a mutable spannable string
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);

            ssb.setSpan(new UiUtils().new InternalUrlSpan(listener, underline), start, end, 0);
            for (CharacterStyle c : cs)
                ssb.setSpan(c, start, end, 0);

            // Delete the tokens before and after the span
            ssb.delete(end, end + tokenLen);
            ssb.delete(start - tokenLen, start);

            text = ssb;
        }

        return text;
    }

    /**
     * Format different parts of a text, such as making bold, italic, setting
     * different color etc. Also make the respectiv portion if text be
     * clickable.
     * 
     * @param text
     * @param token
     * @param listener
     * @param cs
     * @return
     */
    public static CharSequence setLinkBetweenTokens(CharSequence text, String token,
            OnClickListener listener, CharacterStyle... cs) {
        return setLinkBetweenTokens(text, token, listener, true, cs);
    }

    /**
     * Make a portion of a text be clickable
     * 
     * @author ka
     */
    private class InternalUrlSpan extends ClickableSpan {

        private OnClickListener listener;

        private boolean underline;

        public InternalUrlSpan(OnClickListener listener, boolean underline) {
            this.listener = listener;
            this.underline = underline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(underline);
        }

        @Override
        public void onClick(View widget) {
            listener.onClick(widget);
        }
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment
     * arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null)
            return arguments;

        final Uri data = intent.getData();
        if (data != null)
            arguments.putParcelable("_uri", data);

        final Bundle extras = intent.getExtras();
        if (extras != null)
            arguments.putAll(intent.getExtras());

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null)
            return intent;

        final Uri data = arguments.getParcelable("_uri");
        if (data != null)
            intent.setData(data);

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }

    public static void recycleImageView(ImageView view) {
        if (view != null) {
            Drawable drawable = view.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null) {
                    bmp.recycle();
                    view.setImageDrawable(null);
                }
            }
        }
    }

    public static void fadeOutInText(final TextView view, final String newText) {
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(300);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(300);
        out.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setText(newText);
                view.startAnimation(in);
            }
        });

        view.startAnimation(out);
    }

    public static void fixBackgroundRepeat(View view) {
        Drawable bg = view.getBackground();
        if (bg != null) {
            if (bg instanceof BitmapDrawable) {
                BitmapDrawable bmp = (BitmapDrawable) bg;
                bmp.mutate(); // make sure that we aren't sharing state anymore
                bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            }
        }
    }

    public static boolean isGoogleTV(Context context) {
        return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
