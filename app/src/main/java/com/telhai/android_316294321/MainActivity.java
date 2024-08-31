package com.telhai.android_316294321;

import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.telhai.android_316294321.handlers.MyValueEventListener;
import com.telhai.android_316294321.handlers.UriActivityResultCallback;

public class MainActivity extends AppCompatActivity {

    public static final String PACKAGE_NAME = "com.telhai.android_316294321";
    private static final String TAG = "MainActivity"; // TODO: Reference this.
    private static final String FIREBASE_URL =
            "https://telhai-android-316294321-default-rtdb.europe-west1.firebasedatabase.app";
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
        // TODO: `mGetContent` can't be called from `FabOnClickListener`...
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new UriActivityResultCallback(this));
        FloatingActionButton fabImport = findViewById(R.id.fabImport);
        fabImport.setOnClickListener(new FabOnClickListener(MainActivity.this, mGetContent));
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
        myRef.addValueEventListener(new MyValueEventListener());
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

    public void addSoundView(SoundControlView view) {
        linearLayoutSounds.addView(view);
    }
}