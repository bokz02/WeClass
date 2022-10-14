package com.example.weclass.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.R;
import com.example.weclass.TermsAndCondition;
import com.example.weclass.dashboard.MainActivity;
import com.example.weclass.subject.Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView signup, forgotpass;
    EditText editTextEmail, editTextPassword;
    Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        mAuth = FirebaseAuth.getInstance();
        Initialized();
        signIn();

        String text = "Don't have an account? Register Here ";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#28908C"));
                ds.setUnderlineText(false);
            }

        };

        ss.setSpan(clickableSpan1, 23, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
        signup.setText(ss);
        signup.setMovementMethod(LinkMovementMethod.getInstance());



    }



    private void Initialized() {
        signIn = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.signUp);
        forgotpass = findViewById(R.id.forgotPassword);
        signIn = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.logInemail);
        editTextPassword = findViewById(R.id.logInpass);
        progressBar = findViewById(R.id.progressBar);


    }

    //Sign In - Firebase
    private void signIn() {


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(email.isEmpty()){
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextEmail.setError("Email Address is not valid!");
                    editTextEmail.requestFocus();
                    return;
                }

                else if(password.isEmpty()){
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Intent loginIntent = new Intent(LoginActivity.this, Subject.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    } else {

                        firebaseUser.sendEmailVerification();
                        mAuth.signOut();
                        showAlerDialog();

                    }


                }else {
                    progressBar.setVisibility(View.GONE);
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextEmail.setError("User does not exist or is no longer valid. Please register again.");
                        editTextEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextEmail.setError("Invalid credentials. Kindly check and re-enter.");
                        editTextEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };

            };
            });

            }
        });
        }

    private void showAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified!");
        builder.setMessage("Please verify your email now. \nYou can not login without Email Verification.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =  new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }


    @Override
    public void onClick(View v) {

    }


    public void forgotPass(View view) {
        startActivity(new Intent(this, ForgotPassword.class));
    }


    //    If may nakalog-in na, automatic pupunta sa MainActivity
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,Subject.class));
            finish();
        } else {
        }
    }
    }
