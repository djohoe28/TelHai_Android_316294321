package com.telhai.android_316294321;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import java.util.Objects;

public class MediaRunnable implements Runnable {
    //#region Properties
    // Static
    private static final String TAG = "MediaRunnable";
    // Instance
    private final MediaPlayer player;
    private MediaHandler handler;
    //#endregion

    //#region Constructors
    public MediaRunnable(Context _context, int resId, int volume, boolean isMuted) {
        Log.v(TAG, String.format("Constructor(resId=%d, volume=%d, isMuted=%s)",
                resId, volume, isMuted));
        player = MediaPlayer.create(_context, resId);
        initializePlayer(volume, isMuted);
    }

    public MediaRunnable(Context _context, Uri uri, int volume, boolean isMuted) {
        Log.v(TAG, String.format("Constructor(uri=%s, volume=%d, isMuted=%s)",
                uri, volume, isMuted));
        player = MediaPlayer.create(_context, uri);
        initializePlayer(volume, isMuted);
    }
    //#endregion

    //#region Methods
    private void initializePlayer(int volume, boolean isMuted) {
        player.setLooping(true);
        setVolume(volume);
        setIsMuted(isMuted);
    }
    //#endregion

    //#region Overrides
    @Override
    public void run() {
        Looper.prepare();
        handler = new MediaHandler(Objects.requireNonNull(Looper.myLooper()), this);
        Looper.loop();
    }
    //#endregion

    //#region Getters
    public MediaHandler getHandler() {
        return handler;
    }
    //#endregion

    //#region Setters
    public void setVolume(int value) {
        if (player == null) return;
        float volumeFloat = value / 100.0f;
        player.setVolume(volumeFloat, volumeFloat);
    }

    public void setIsMuted(boolean value) {
        // NOTE: Technically pausing rather than muting, but I think it's cute.
        if (value) player.pause();
        else player.start();
    }
    //#endregion
}
