package com.telhai.android_316294321;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

public class StorageSoundControlView extends SoundControlView {
    private static final String TAG = "StorageSoundControlView";

    public StorageSoundControlView(@NonNull Context context, String _soundName, int _soundIcon, Uri _soundPath) {
        super(context, _soundName, _soundIcon);
        setSoundPath(context, _soundPath);
        Log.w(TAG, String.format("ParamConstructor(name=%s icon=%s path=%s)", _soundName, _soundIcon, _soundPath));
    }

    public StorageSoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.w(TAG, "Constructor()");
    }

    @Override
    public int getLayout() {
        return R.layout.sound_control;
    }

    public void setSoundPath(@NonNull Context context, Uri value) {
        MediaPlayer player = MediaPlayer.create(context, value);
        setMediaPlayer(player); // NOTE: runs initializeMediaPlayer();
    }
}
