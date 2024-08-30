package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

public class SystemSoundControlView extends SoundControlView {
    private static final String TAG = "SystemSoundControlView";

    public SystemSoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        try (TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SystemSoundControlView)) {
            int resId = a.getResourceId(R.styleable.SystemSoundControlView_soundPath, 0);
            setMedia(resId);
            Log.i(TAG, String.format("AttrConstructor(resId=%d)", resId));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage() != null ? ex.getMessage() : "attrs Exception");
        } // TODO: finally { a.recycle(); }
    }

    @Override
    public int getLayout() {
        return R.layout.sound_control;
    }
}
