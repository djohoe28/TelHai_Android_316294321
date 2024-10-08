package com.telhai.android_316294321.threads;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MediaRunnable implements Runnable {
    //#region Properties
    // Static
    private static final String TAG = "MediaRunnable";
    // Instance
    // TODO: These values are used *only* during initialization, under `run()`
    private final Context context;
    private final int resId; // NOTE: Null mutex with `uri`
    private final @Nullable Uri uri; // NOTE: Null mutex with `resId`
    private final int volume;
    private final boolean isPaused;

    private MediaPlayer player;
    private MediaHandler handler;
    //#endregion

    //#region Constructors
    public MediaRunnable(Context _context, int _resId, int _volume, boolean _isPaused) {
        Log.v(TAG, String.format("IntConstructor(resId=%d, volume=%d, isPaused=%s)",
                _resId, _volume, _isPaused));
        context = _context;
        resId = _resId;
        uri = null;
        volume = _volume;
        isPaused = _isPaused;
    }

    public MediaRunnable(Context _context, @NonNull Uri _uri, int _volume, boolean _isPaused) {
        Log.v(TAG, String.format("URIConstructor(uri=%s, volume=%d, isPaused=%s)",
                _uri, _volume, _isPaused));
        context = _context;
        resId = -1;
        uri = _uri;
        volume = _volume;
        isPaused = _isPaused;
    }
    //#endregion

    //#region Methods
    public void quit() {
        Objects.requireNonNull(Looper.myLooper()).quit();
    }
    //#endregion

    //#region Overrides
    @Override
    public void run() {
        if (uri != null)
            player = MediaPlayer.create(context, uri);
        else
            player = MediaPlayer.create(context, resId);
        player.setLooping(true);
        setVolume(volume);
        setIsPaused(isPaused);
        // TODO: Add CountDownLatch for synchronization.
        // TODO: Release `MediaPlayer` resources in `finally` block
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

    public void setIsPaused(boolean value) {
        if (player == null) return;
        // NOTE: Technically pausing rather than muting, but I think it's cute.
        if (value) player.pause();
        else player.start();
    }
    //#endregion
}
