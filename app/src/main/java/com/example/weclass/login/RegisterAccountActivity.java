package com.example.weclass.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.R;
import com.example.weclass.TermsAndCondition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterAccountActivity extends AppCompatActivity {

   TextView login, terms;
   EditText editTextfullname, editTextemail, editTextpassword;
   Button registerUser, back;
   ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);

        mAuth = FirebaseAuth.getInstance();
        Initialized();
        registerUser();
        backButton();

        String text = "Already have an account? Login Here ";
        String text1 = "By signing up, you've agree to our Terms of Service and Privacy policy.";
        SpannableString ss1 = new SpannableString(text1);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#da4e4e"));
                ds.setUnderlineText(false);
            }

        };

        ClickableSpan clickableSpan3 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            Toast.makeText(RegisterAccountActivity.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#da4e4e"));
                ds.setUnderlineText(false);
            }
        };

        ss1.setSpan(clickableSpan3, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
        ss1.setSpan(clickableSpan1, 35, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
        terms.setText(ss1);
        terms.setMovementMethod(LinkMovementMethod.getInstance());





        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#da4e4e"));
                ds.setUnderlineText(false);
            }
        };


        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void Initialized(){

        registerUser = findViewById(R.id.btnsignUp);
        login = findViewById(R.id.signUpLogin);
        terms = findViewById(R.id.termsOfServices);
        editTextfullname = findViewById(R.id.signUpFullname);
        editTextemail = findViewById(R.id.signUpemail);
        editTextpassword = findViewById(R.id.signUpPassword);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.backButton);
    }

    //BACK TO LOGIN
    private void backButton (){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }


    //REGISTER USER TO FIREBASE
    private void registerUser ()
    {

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextemail.getText().toString().trim();
                String password = editTextpassword.getText().toString().trim();
                String fullname = editTextfullname.getText().toString().trim();

                String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String checkPassword = "^" +
                        //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";

                //TRAPPING
                if (email.isEmpty()) {

                    Toast.makeText(RegisterAccountActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    editTextemail.setError("Email Address is required!");
                    editTextemail.requestFocus();
                    return; }

                else if (!email.matches(checkEmail)) {
                    Toast.makeText(RegisterAccountActivity.this, "Make sure your email is valid.", Toast.LENGTH_LONG).show();
                    editTextemail.setError("Invalid Email");
                    editTextemail.requestFocus();
                    return; }

                else if (fullname.isEmpty()) {
                    Toast.makeText(RegisterAccountActivity.this, "Please enter your username.", Toast.LENGTH_LONG).show();
                    editTextfullname.setError("Username is required!");
                    editTextfullname.requestFocus();
                    return; }

                else if (password.isEmpty()) {
                    Toast.makeText(RegisterAccountActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
                    editTextpassword.setError("Password is required!");
                    editTextpassword.requestFocus();
                    return; }


                else if (!password.matches(checkPassword)) {
                    editTextpassword.setError("Password should contain 4 characters;");
                    editTextpassword.requestFocus();
                    return; }

                //END OF TRAPPING


                //SAVING USER INFO IN FIREBASE
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterAccountActivity.this, "Account has been registered successfully!", Toast.LENGTH_LONG).show();
                                    UserItem user = new UserItem(email, fullname);
                                    FirebaseDatabase.getInstance().getReference("UserItem")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                                }else {
                                    try {
                                        throw task.getException();

                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        editTextemail.setError("Your email is invalid! Please provide valid email.");
                                        editTextemail.requestFocus();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        editTextemail.setError("User is already registered with this email. Use another Email.");
                                        editTextemail.requestFocus();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(RegisterAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                            }

                        });
            }
        });

    }





}
