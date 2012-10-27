
package com.kaciula.utils.misc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Common utilities to process bitmaps and byte arrays
 * 
 * @author ka
 */
public class StreamUtils {

    private static final String TAG = "StreamUtils";

    /**
     * Get a string from an asset file. Usually used when we have large texts we
     * need to load.
     * 
     * @param ctx
     * @param fileName
     * @param charset
     * @return
     * @throws IOException
     */
    public static String loadStringFromAsset(String fileName, String charset) throws IOException {
        InputStream is = BasicApplication.getContext().getAssets().open(fileName);

        return readString(is, charset);
    }

    public static String readString(InputStream inputStream, String charset) throws IOException {
        return new String(readByte(inputStream), charset);
    }

    /**
     * Saves an inputstream to a file
     * 
     * @param file
     * @param is
     * @return
     */
    public static boolean saveFile(File file, InputStream is) {
        try {
            OutputStream os = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0)
                os.write(buf, 0, len);
            os.close();
            is.close();
            return true;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return false;
    }

    /**
     * Decodes image and scales it to reduce memory consumption
     * 
     * @param is
     * @param sizeWidth
     * @param sizeHeight
     * @return
     */
    public static Bitmap decodeStream(InputStream is, int sizeWidth, int sizeHeight) {
        try {
            byte[] savedStream;
            savedStream = readByte(is);

            return decodeByteArray(savedStream, sizeWidth, sizeHeight);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return null;
    }

    /**
     * Decodes image and scales it to reduce memory consumption
     * 
     * @param imageData
     * @param sizeWidth
     * @param sizeHeight
     * @return
     */
    public static Bitmap decodeByteArray(byte[] imageData, int sizeWidth, int sizeHeight) {
        // Decode image size
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPurgeable = true;
        BitmapFactory.decodeByteArray(imageData, 0, imageData.length, opts);

        // Find the correct scale value. It should be the power of 2.
        int width = opts.outWidth;
        int height = opts.outHeight;

        int scale = 1;
        while (true) {
            if (width / 2 < sizeWidth || height / 2 < sizeHeight)
                break;
            width /= 2;
            height /= 2;
            scale *= 2;
        }

        BitmapFactory.Options opts2 = new BitmapFactory.Options();
        opts2.inSampleSize = scale;
        opts2.inPurgeable = true;

        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length, opts2);
    }

    /**
     * Decodes image and scales it to reduce memory consumption
     * 
     * @param f
     * @param sizeWidth
     * @param sizeHeight
     * @return
     */
    public static Bitmap decodeFile(File f, int sizeWidth, int sizeHeight) {
        FileInputStream fs = null;
        FileInputStream fs2 = null;

        try {
            // Decode image size
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            opts.inPurgeable = true;
            fs = new FileInputStream(f);
            BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opts);

            // Find the correct scale value. It should be the power of 2.
            int width = opts.outWidth;
            int height = opts.outHeight;

            LogUtils.d(TAG, "BEFORE: bitmap size = " + width + "x" + height);

            int scale = 1;
            while (true) {
                if (width / 2 < sizeWidth || height / 2 < sizeHeight)
                    break;
                width /= 2;
                height /= 2;
                scale *= 2;
            }

            LogUtils.d(TAG, "AFTER: bitmap size = " + width + "x" + height);

            BitmapFactory.Options opts2 = new BitmapFactory.Options();
            opts2.inSampleSize = scale;
            opts2.inPurgeable = true;
            fs2 = new FileInputStream(f);
            return BitmapFactory.decodeFileDescriptor(fs2.getFD(), null, opts2);
        } catch (FileNotFoundException fnfe) {
            LogUtils.printStackTrace(fnfe);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                }
            }
            if (fs2 != null) {
                try {
                    fs2.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * Converts an InputStream to a byte array
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];

        for (int n; 0 < (n = inputStream.read(buf));) {
            into.write(buf, 0, n);
        }
        into.close();

        return into.toByteArray();
    }
}
