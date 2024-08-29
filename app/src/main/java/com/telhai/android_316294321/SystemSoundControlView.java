package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

public class SystemSoundControlView extends SoundControlView {
    private static final String TAG = "SystemSoundControlView";

//    public SystemSoundControlView(@NonNull Context context, String _soundName, int _soundIcon) {
//        super(context, _soundName, _soundIcon);
//        Log.w(TAG, String.format("ParamConstructor(name=%s icon=%s)", _soundName, _soundIcon));
//    }

    public SystemSoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        try (TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SystemSoundControlView)) {
            int resId = a.getResourceId(R.styleable.SystemSoundControlView_soundPath, 0);
            Log.d(TAG, String.format("Constructor(path=%d)", resId));
            MediaPlayer player = MediaPlayer.create(context, resId);
            setMediaPlayer(player); // NOTE: runs initializeMediaPlayer();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage() != null ? ex.getMessage() : "attrs Exception");
        } // TODO: finally { a.recycle(); }
    }

    @Override
    public int getLayout() {
        return R.layout.sound_control;
    }
}
