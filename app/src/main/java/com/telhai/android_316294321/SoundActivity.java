package com.telhai.android_316294321;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.telhai.android_316294321.handlers.FabOnClickListener;
import com.telhai.android_316294321.handlers.PreferenceVolumeChangeListener;
import com.telhai.android_316294321.handlers.PreferencesValueEventListener;
import com.telhai.android_316294321.handlers.UriActivityResultCallback;
import com.telhai.android_316294321.models.UserPreferences;

public class SoundActivity extends AppCompatActivity {
    private static final String FIREBASE_URL =
            "https://telhai-android-316294321-default-rtdb.europe-west1.firebasedatabase.app";
    public UserPreferences preferences;
    public DatabaseReference dbRef;
    private SoundControlView waterSound;
    private SoundControlView earthSound;
    private SoundControlView fireSound;
    private SoundControlView airSound;
    private LinearLayout linearLayoutSounds;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sound);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        waterSound = findViewById(R.id.soundWater);
        earthSound = findViewById(R.id.soundEarth);
        fireSound = findViewById(R.id.soundFire);
        airSound = findViewById(R.id.soundAir);
        waterSound.setOnVolumeChangeListener(new PreferenceVolumeChangeListener(waterSound, this));
        earthSound.setOnVolumeChangeListener(new PreferenceVolumeChangeListener(earthSound, this));
        fireSound.setOnVolumeChangeListener(new PreferenceVolumeChangeListener(fireSound, this));
        airSound.setOnVolumeChangeListener(new PreferenceVolumeChangeListener(airSound, this));
        linearLayoutSounds = findViewById(R.id.linearLayoutSounds);
        // TODO: `mGetContent` can't be called from `FabOnClickListener`...
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new UriActivityResultCallback(this));
        FloatingActionButton fabImport = findViewById(R.id.fabImport);
        fabImport.setOnClickListener(new FabOnClickListener(this, mGetContent));
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        getUser();
    }

    private @NonNull DatabaseReference getDatabaseReference(String path) {
        String firebaseUrl = FIREBASE_URL; // TODO: getString(R.string.firebase);
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseUrl);
        return database.getReference(path);
    }

    private void getUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            dbRef = getDatabaseReference(user.getUid());
            dbRef.addValueEventListener(new PreferencesValueEventListener(this));
        }
    }

    public void updatePreferences(@Nullable UserPreferences _preferences) {
        if (_preferences == null) {
            this.preferences = new UserPreferences(0, 0, 0, 0);
            dbRef.setValue(this.preferences);
        } else {
            this.preferences = _preferences;
        }
        updatePreferencesViews();
    }

    private void updatePreferencesViews() {
        waterSound.setVolume(this.preferences.waterVolume);
        earthSound.setVolume(this.preferences.earthVolume);
        fireSound.setVolume(this.preferences.fireVolume);
        airSound.setVolume(this.preferences.airVolume);
    }

    public void addSoundView(SoundControlView view) {
        linearLayoutSounds.addView(view);
    }
}