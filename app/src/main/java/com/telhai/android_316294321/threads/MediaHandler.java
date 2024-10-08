package com.telhai.android_316294321.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MediaHandler extends Handler {
    //#region Properties
    // Static
    private static final String TAG = "MediaHandler";
    public static final int TERMINATE_MESSAGE = -1;
    public static final int VOLUME_CHANGE_MESSAGE = 2;
    public static final int PAUSED_CHANGE_MESSAGE = 3;
    // Instance
    private final MediaRunnable runnable;
    //#endregion

    //#region Constructors
    public MediaHandler(Looper looper, MediaRunnable _runnable) {
        super(looper);
        Log.v(TAG, "Constructor()");
        runnable = _runnable;
    }
    //#endregion

    //#region Overrides
    @Override
    public void handleMessage(@NonNull Message message) {
        Log.v(TAG, String.format("handleMessage(msg=%s)", message));
        switch (message.what) {
            case TERMINATE_MESSAGE:
                runnable.quit();
                break;
            case VOLUME_CHANGE_MESSAGE:
                runnable.setVolume((int) message.obj);
                break;
            case PAUSED_CHANGE_MESSAGE:
                runnable.setIsPaused((boolean) message.obj);
                break;
            default:
                break;
        }
    }
    //#endregion
}
