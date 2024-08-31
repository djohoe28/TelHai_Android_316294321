package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.Locale;

import com.telhai.android_316294321.handlers.PauseOnClickListener;
import com.telhai.android_316294321.handlers.VolumeChangeListener;
import com.telhai.android_316294321.threads.MediaHandler;
import com.telhai.android_316294321.threads.MediaRunnable;

public class SoundControlView extends CardView {
    //#region Properties
    // Static
    private static final String TAG = "SoundControlView";
    // Instance
    /**
     * The thread in charge of the sound.
     */
    private MediaRunnable runnable;

    // Views
    private TextView textName; // TODO: Duplicate speakable text present
    private ImageButton imageButtonPause;
    private SeekBar seekVolume;

    // Attributes
    /**
     * The display name of the sound.
     */
    private String soundName;
    private int soundIcon;
    private Uri soundPath;
    // State
    /**
     * Volume from 0 to 100. Clamped by SeekBar.
     */
    private int volume = 0;
    /**
     * Whether the sound is currently paused.
     */
    private boolean isPaused = false;
    //#endregion


    //#region Constructors
    public SoundControlView(@NonNull Context context, Uri _soundPath, String _soundName, int _soundIcon) {
        super(context);
        initialize();
        // Apply attributes (parameters)
        setSoundPath(_soundPath);
        setSoundIcon(_soundIcon);
        setSoundName(_soundName);
        Log.i(TAG, String.format("ParamConstructor(path=%s, name=%s, icon=%s)", _soundPath, _soundName, _soundIcon));
    }

    public SoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        // Apply attributes (AttributeSet)
        try (TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SoundControlView)) {
            setSoundPath(a.getResourceId(R.styleable.SoundControlView_soundPath, 0));
            setSoundIcon(a.getResourceId(R.styleable.SoundControlView_soundIcon, 0));
            setSoundName(a.getString(R.styleable.SoundControlView_soundName));
            Log.v(TAG, "Attributes read successfully.");
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage() != null ? ex.getMessage() : "attrs Exception");
        } // TODO: finally { a.recycle(); }
        Log.i(TAG, String.format("AttrConstructor(name=%s icon=%s, path=%s)", soundName, soundIcon, soundPath));
    }
    //#endregion

    //#region Methods
    private void initialize() {
        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sound_control, this, true);
        // Get views
        textName = findViewById(R.id.textName);
        imageButtonPause = findViewById(R.id.imageButtonPause);
        seekVolume = findViewById(R.id.seekVolume);
        // View - Pause ImageButton
        imageButtonPause.setOnClickListener(new PauseOnClickListener(this));
        // View - Volume SeekBar
        seekVolume.setProgress(volume); // NOTE: Doing this before adding listener prevents call.
        seekVolume.setOnSeekBarChangeListener(new VolumeChangeListener(this));
    }

    public void togglePaused() {
        setIsPaused(!isPaused);
    }
    //#endregion

    //#region Getters
    public String getSoundName() {
        return soundName;
    }
    //#endregion

    //#region Setters
    public void setSoundIcon(int value) {
        soundIcon = value;
        textName.setCompoundDrawablesWithIntrinsicBounds(soundIcon, 0, 0, 0);
        invalidate();
        requestLayout();
    }

    public void setSoundName(String value) {
        soundName = value;
        textName.setText(soundName);
        invalidate();
        requestLayout();
    }

    public void setSoundPath(int resId) {
        soundPath = Uri.parse(String.format(Locale.getDefault(),
                "android.resource://%s/%d", MainActivity.PACKAGE_NAME, resId));
        // TODO: Merge with URI?
        if (runnable != null && runnable.getHandler() != null) {
            runnable.getHandler().sendEmptyMessage(MediaHandler.TERMINATE_MESSAGE);
        }
        runnable = new MediaRunnable(getContext(), resId, volume, isPaused);
        new Thread(runnable).start();
    }

    public void setSoundPath(Uri uri) {
        soundPath = uri;
        // TODO: Streamline with CountDownLatch?
        // TODO: How to remove sound? With `onDestroy()`? With `Thread.interrupt()`? In `MainActivity`?
        // TODO:
        //  FORTIFY: pthread_mutex_lock called on a destroyed mutex (0x7b9a09802ab8)
        //  Fatal signal 6 (SIGABRT), code -1 (SI_QUEUE) in tid 9707 (hwuiTask0), pid 9622 (droid_316294321)
        if (runnable != null && runnable.getHandler() != null) {
            runnable.getHandler().sendEmptyMessage(MediaHandler.TERMINATE_MESSAGE);
        }
        runnable = new MediaRunnable(getContext(), uri, volume, isPaused);
        new Thread(runnable).start();
    }

    public void setVolume(int value) {
        volume = value;
        if (runnable.getHandler() != null) {
            MediaHandler handler = runnable.getHandler();
            Message message = handler.obtainMessage(MediaHandler.VOLUME_CHANGE_MESSAGE, value);
            handler.sendMessage(message);
        }
        seekVolume.setProgress(volume);
        invalidate();
        requestLayout();
    }

    private void setIsPaused(boolean value) {
        isPaused = value;
        if (runnable.getHandler() != null) {
            MediaHandler handler = runnable.getHandler();
            Message message = handler.obtainMessage(MediaHandler.PAUSED_CHANGE_MESSAGE, value);
            handler.sendMessage(message);
        }
        imageButtonPause.setImageResource(isPaused
                ? R.drawable.baseline_play_circle_filled_24
                : R.drawable.baseline_pause_circle_filled_24);
        invalidate();
        requestLayout();
    }
    //#endregion
}