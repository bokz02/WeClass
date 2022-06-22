package com.example.weclass.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weclass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity {

    FirebaseAuth auth;
    EditText eemail;
    private final static String TAG = "ForgotPasswordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eemail = findViewById(R.id.editTextUserEmail);
    }

    public void forgotPassword(View view) {
        String email = eemail.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(ForgotPassword.this,
                    "Please Enter Your Registered E-mail", Toast.LENGTH_SHORT).show();
            eemail.setError("Field can not be empty");
            eemail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(ForgotPassword.this,
                    "Please Enter Valid E-mail", Toast.LENGTH_SHORT).show();
            eemail.setError("Valid E-mail is Required");
            eemail.requestFocus();
        } else {
            resetPassword(email);
        }
    }
    private void resetPassword(String email) {
        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent loginIntent = new Intent(ForgotPassword.this, PasswordResetLinkSuceed.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                } else {
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        eemail.setError("User does not exist or is no longer valid. Please register again.");
                        eemail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }
}
