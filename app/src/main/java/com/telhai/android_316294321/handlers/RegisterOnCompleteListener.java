package com.telhai.android_316294321.handlers;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.telhai.android_316294321.RegisterActivity;

public class RegisterOnCompleteListener implements OnCompleteListener<AuthResult> {
    //#region Properties
    private static final String TAG = "RegisterOnCompleteListener";
    private final RegisterActivity context;
    private final FirebaseAuth mAuth;

    //#endregion
    public RegisterOnCompleteListener(@NonNull RegisterActivity _context) {
        context = _context;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();
            context.updateUI(user);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            context.updateUI(null);
        }
    }
}
