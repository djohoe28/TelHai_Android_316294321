package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.Locale;

import threading.MediaHandler;
import threading.MediaThread;

public class SoundControlView extends CardView {
    //#region Properties
    // Static
    private static final String TAG = "SoundControlView";
    // Instance
    /**
     * The thread in charge of the sound.
     */
    private MediaThread thread;

    // Views
    private TextView textName;
    private ImageButton imageButtonMute; // TODO: Duplicate speakable text present
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
     * Whether the sound is currently "muted" (paused).
     */
    private boolean isMuted = false;
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
        imageButtonMute = findViewById(R.id.imageButtonMute);
        seekVolume = findViewById(R.id.seekVolume);
        // View - Mute ImageButton
        imageButtonMute.setOnClickListener(view -> {
            Log.i(TAG, String.format("toggleMute.onCheckedChanged(%s, %s)", soundName, isMuted));
            setIsMuted(!isMuted);
        });
        // View - Volume SeekBar
        seekVolume.setProgress(volume); // NOTE: Doing this before adding listener prevents call.
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, String.format("seekVolume.onProgressChanged(%s, %d, %s)",
                        soundName, progress, fromUser));
                if (fromUser) setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, String.format("seekVolume.onStartTrackingTouch(%s)", soundName));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, String.format("seekVolume.onStopTrackingTouch(%s)", soundName));
            }
        });
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
        if (thread != null) thread.interrupt();
        thread = new MediaThread(getContext(), resId, volume, isMuted);
        thread.start();
    }

    public void setSoundPath(Uri uri) {
        soundPath = uri;
        if (thread != null) thread.interrupt();
        thread = new MediaThread(getContext(), uri, volume, isMuted);
        thread.start();
    }

    public void setVolume(int value) {
        volume = value;
        if (thread.getHandler() != null) {
            MediaHandler handler = thread.getHandler();
            Message message = handler.obtainMessage(MediaHandler.VOLUME_CHANGE_MESSAGE, value);
            handler.sendMessage(message);
        }
        seekVolume.setProgress(volume);
        invalidate();
        requestLayout();
    }

    public void setIsMuted(boolean value) {
        isMuted = value;
        if (thread.getHandler() != null) {
            MediaHandler handler = thread.getHandler();
            Message message = handler.obtainMessage(MediaHandler.MUTED_CHANGE_MESSAGE, value);
            handler.sendMessage(message);
        }
        imageButtonMute.setImageResource(isMuted
                ? R.drawable.baseline_volume_off_24
                : R.drawable.baseline_volume_up_24);
        invalidate();
        requestLayout();
    }
    //#endregion
}