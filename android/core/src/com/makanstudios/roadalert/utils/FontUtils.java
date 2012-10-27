
package com.makanstudios.roadalert.utils;

import android.graphics.Typeface;
import android.util.SparseArray;

import com.kaciula.utils.ui.BasicApplication;

public class FontUtils {

    private static final SparseArray<Typeface> typefaces = new SparseArray<Typeface>();

    public static Typeface getTypeface(int type) {
        Typeface typeface = null;

        synchronized (typefaces) {
            typeface = typefaces.get(type);

            if (typeface == null) {
                switch (type) {
                    case Constants.TYPEFACE_ROBOTO_LIGHT:
                        typeface = Typeface.createFromAsset(BasicApplication.getContext()
                                .getAssets(), "fonts/Roboto-Light.ttf");
                        typefaces.put(type, typeface);
                        break;
                    case Constants.TYPEFACE_ROBOTO_REGULAR:
                        typeface = Typeface.createFromAsset(BasicApplication.getContext()
                                .getAssets(), "fonts/Roboto-Regular.ttf");
                        typefaces.put(type, typeface);
                        break;
                    case Constants.TYPEFACE_ROBOTO_BOLD:
                        typeface = Typeface.createFromAsset(BasicApplication.getContext()
                                .getAssets(), "fonts/Roboto-Bold.ttf");
                        typefaces.put(type, typeface);
                        break;
                    case Constants.TYPEFACE_ROBOTO_CONDENSED:
                        typeface = Typeface.createFromAsset(BasicApplication.getContext()
                                .getAssets(), "fonts/Roboto-Condensed.ttf");
                        typefaces.put(type, typeface);
                        break;
                    default:
                        break;
                }
            }
        }

        return typeface;
    }
}
