package com.telhai.android_316294321;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String FIREBASE_URL =
            "https://telhai-android-316294321-default-rtdb.europe-west1.firebasedatabase.app";
    private static final Pattern FILENAME_PATTERN = Pattern.compile(".*/(?<name>.*)");
    private static final int REQUEST_CODE_PERMISSION = 1;
    private FirebaseAuth mAuth;
    private LinearLayout linearLayoutSounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        linearLayoutSounds = findViewById(R.id.linearLayoutSounds);
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    Log.d(TAG, String.format("onActivityResult(%s)", uri));
                    if (uri != null) {
                        // Extract filename from URI
                        String segment = uri.getLastPathSegment() == null ? uri.toString() : uri.getLastPathSegment();
                        Matcher matcher = FILENAME_PATTERN.matcher(segment);
                        String name = matcher.find() ? matcher.group("name") : segment;
                        // Create view
                        StorageSoundControlView _view = new StorageSoundControlView(
                                MainActivity.this,
                                name,
                                R.drawable.baseline_audio_file_24,
                                uri);
                        linearLayoutSounds.addView(_view);
                    }
                });
        FloatingActionButton fabImport = findViewById(R.id.fabImport);
        fabImport.setOnClickListener(view -> {
            // Check Permission
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted; Request permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                            REQUEST_CODE_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSION);
                }
            } else {
                // Permission granted; Request audio file.
                mGetContent.launch("audio/*");
            }
        });
        // Firebase
        mAuth = FirebaseAuth.getInstance();

//        // Admin SDK configuration snippet
//        String serviceAccountPath = "firebase-admin-sdk.json";
//        try (InputStream serviceAccount = getAssets().open(serviceAccountPath)){
//            FirebaseOptions options = new FirebaseOptions.Builder()
////                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)) // no .setCredentials method...
//                    .setDatabaseUrl(getString(R.string.firebase))
//                    .build();
//            FirebaseApp.initializeApp(this, options);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // Write a message to the database
        DatabaseReference myRef = getDatabaseReference("message");

        myRef.setValue("Hello, World!");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    private @NonNull DatabaseReference getDatabaseReference(String path) {
        String firebaseUrl = FIREBASE_URL; // TODO: getString(R.string.firebase);
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseUrl);
        return database.getReference(path);
    }

    private void createAccount(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
    }

    private void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }
}