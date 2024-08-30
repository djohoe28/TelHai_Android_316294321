package MediaThreading;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class MediaThread extends Thread {
    //#region Properties
    // Static
    private static final String TAG = "MediaThread";
    // Instance
    private final MediaRunnable runnable;
    //#endregion

    //#region Constructors
    public MediaThread(MediaRunnable _runnable) {
        super(_runnable);
        runnable = _runnable;
        Log.v(TAG, "Constructor()");
    }

    public MediaThread(Context context, int resId, int volume, boolean isMuted) {
        this(new MediaRunnable(context, resId, volume, isMuted));
        Log.v(TAG, String.format("Constructor(resId=%d, volume=%d, isMuted=%s)",
                resId, volume, isMuted));
    }

    public MediaThread(Context context, Uri uri, int volume, boolean isMuted) {
        this(new MediaRunnable(context, uri, volume, isMuted));
        Log.v(TAG, String.format("Constructor(uri=%s, volume=%d, isMuted=%s)",
                uri, volume, isMuted));
    }
    //#endregion

    //#region Overrides
    @Override
    public synchronized void start() {
        super.start();
    }
    //#endregion

    //#region Getters
    public MediaHandler getHandler() {
        return runnable.getHandler();
    }
    //#endregion
}
