
package com.makanstudios.roadalert.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.utils.Constants;
import com.makanstudios.roadalert.utils.FontUtils;

public class CustomButton extends Button {

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CustomTypeface);
        int type = attr.getInt(R.styleable.CustomTypeface_typeface_type,
                Constants.TYPEFACE_DEFAULT);

        if (!isInEditMode())
            setTypeface(FontUtils.getTypeface(type));
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CustomTypeface);
        int type = attr.getInt(R.styleable.CustomTypeface_typeface_type,
                Constants.TYPEFACE_DEFAULT);

        if (!isInEditMode())
            setTypeface(FontUtils.getTypeface(type));
    }

    public CustomButton(Context context) {
        super(context);

        if (!isInEditMode())
            setTypeface(FontUtils.getTypeface(Constants.TYPEFACE_DEFAULT));
    }

}