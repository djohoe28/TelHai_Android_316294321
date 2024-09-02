package com.telhai.android_316294321;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //#region Properties
    public static final String PACKAGE_NAME = "com.telhai.android_316294321";
    private TextView textViewUser;
    private FirebaseAuth mAuth;
    //#endregion

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
        Button buttonGoToSounds = findViewById(R.id.buttonGoToSounds);
        buttonGoToSounds.setOnClickListener(view -> startActivity(new Intent(this, SoundActivity.class)));
        Button buttonGoToRegister = findViewById(R.id.buttonGoToRegister);
        buttonGoToRegister.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
        textViewUser = findViewById(R.id.textViewUser);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            textViewUser.setText(String.format("Logged in as: %s", currentUser.getEmail()));
        } else {
            textViewUser.setText(R.string.anon_text);
        }
    }
}