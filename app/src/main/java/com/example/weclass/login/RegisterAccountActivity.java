package com.example.weclass.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        mAuth = FirebaseAuth.getInstance();
        Initialized();
        registerUser();
        backButton();

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
                mAuth.createUserWithEmailAndPassword(editTextemail.getText().toString(), editTextpassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    UserItem user = new UserItem(email, fullname);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterAccountActivity.this, "Account has been registered successfully!.", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(RegisterAccountActivity.this, "Account Registration failed. Please try Again.", Toast.LENGTH_LONG).show();
                                            }
                                            progressBar.setVisibility(View.GONE);
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



    public void terms(View view) {
        startActivity(new Intent(this, TermsAndCondition.class));
    }

    public void logIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
