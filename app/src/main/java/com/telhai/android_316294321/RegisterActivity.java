package com.telhai.android_316294321;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.telhai.android_316294321.handlers.RegisterOnCompleteListener;

public class RegisterActivity extends AppCompatActivity {
    //#region Properties
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonSubmitRegister = findViewById(R.id.buttonSubmitRegister);
        buttonSubmitRegister.setOnClickListener(view -> onSubmit());
        mAuth = FirebaseAuth.getInstance();
    }

    private void onSubmit() {
        EditText editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText editTextPass = findViewById(R.id.editTextTextPassword);
        String email = editTextEmail.getText().toString();
        String password = editTextPass.getText().toString();
        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                this,
                new RegisterOnCompleteListener(this));
        finish();
    }

    public void updateUI(@Nullable FirebaseUser user) {
        // TODO: Update UI
    }
}