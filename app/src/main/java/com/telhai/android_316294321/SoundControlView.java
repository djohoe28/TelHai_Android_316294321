package com.telhai.android_316294321;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public abstract class SoundControlView extends CardView {

    //#region Properties
    private static final String TAG = "SoundControlView";

    // Views
    private final TextView textName;
    private final ImageButton imageButtonMute; // TODO: Duplicate speakable text present
    private final SeekBar seekVolume;
    private MediaPlayer mediaPlayer;
    // Attributes
    private String soundName;
    private int soundIcon;
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


    public SoundControlView(@NonNull Context context, String _soundName, int _soundIcon) {
        super(context);
        // Layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayout(), this, true);
        // Views
        textName = findViewById(R.id.textName);
        imageButtonMute = findViewById(R.id.imageButtonMute);
        seekVolume = findViewById(R.id.seekVolume);
        initialize();
        // Apply attributes (parameters)
        setSoundIcon(_soundIcon);
        setSoundName(_soundName);
        Log.i(TAG, String.format("ParamConstructor(name=%s icon=%s)", _soundName, _soundIcon));
    }

    public SoundControlView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        // Layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayout(), this, true);
        // Views
        textName = findViewById(R.id.textName);
        imageButtonMute = findViewById(R.id.imageButtonMute);
        seekVolume = findViewById(R.id.seekVolume);
        initialize();
        // Apply attributes (AttributeSet)
        try (TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SoundControlView)) {
            setSoundIcon(a.getResourceId(R.styleable.SoundControlView_soundIcon, 0));
            setSoundName(a.getString(R.styleable.SoundControlView_soundName));
            Log.v(TAG, "Attributes read successfully.");
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage() != null ? ex.getMessage() : "attrs Exception");
        } // TODO: finally { a.recycle(); }
        Log.i(TAG, String.format("AttrConstructor(name=%s icon=%s)", soundName, soundIcon));
    }

    private void initialize() {

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

    //#region Getters

    /**
     * Returns the intended Layout Resource ID.
     * Note: used in case I'd like to make different layouts for different subclasses.
     *
     * @return The intended Layout Resource ID.
     */
    public abstract int getLayout();
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

    public void setMediaPlayer(MediaPlayer value) {
        mediaPlayer = value;
        if (mediaPlayer != null) initializeMediaPlayer();
    }

    public void initializeMediaPlayer() {
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
        mediaPlayer.start();
    }

    public void setVolume(int value) {
        volume = value;
        seekVolume.setProgress(volume);
        invalidate();
        requestLayout();
        if (mediaPlayer != null)
            mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);
    }

    public void setIsMuted(boolean value) {
        isMuted = value;
        imageButtonMute.setImageResource(isMuted
                ? R.drawable.baseline_volume_off_24
                : R.drawable.baseline_volume_up_24);
        // NOTE: This is technically not muting but pausing; However, I think it's cute.
        if (isMuted) mediaPlayer.pause();
        else mediaPlayer.start();
        invalidate();
        requestLayout();
    }
    //#endregion
}