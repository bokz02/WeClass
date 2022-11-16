package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.dashboard.MainActivity;
import com.example.weclass.subject.Subject;

import java.util.concurrent.Executor;

public class LockScreen extends AppCompatActivity {

    Button fingerprintBtn, patternBtn;
    TextView infoTextView;
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        initialize();
        checkBiometricSupported();
        //executor();


        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LockScreen.this,
                executor, new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //Toast.makeText(LockScreen.this, "Authentication error " +errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LockScreen.this, "Login successful" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LockScreen.this, Subject.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LockScreen.this, "Login failed" , Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setDeviceCredentialAllowed(true);
        biometricPrompt.authenticate(promptInfo.build());

        patternBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
                promptInfo.setDeviceCredentialAllowed(true);
                biometricPrompt.authenticate(promptInfo.build());
            }
        });

    }

    public void initialize(){
        patternBtn = findViewById(R.id.patternButton);
        infoTextView = findViewById(R.id.textViewLockScreen);
    }

    private void checkBiometricSupported(){
        String info = "";
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK
                | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "Biometrics available";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "Biometric feature not available";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric feature unavailable";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Biometric feature not available";
                enableButton(false, true);
                break;
            default:
                info = "Unknown error";
                break;
        }

        TextView infoText = findViewById(R.id.textViewLockScreen);
        infoText.setText(info);
    }

    void enableButton(boolean enable){
        patternBtn.setEnabled(true);
    }

    void enableButton(boolean enable, boolean enroll){
        enableButton(enable);
        if (!enroll)
            return;
        Intent enrollment = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollment.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK);
        startActivity(enrollment);
    }


    BiometricPrompt.PromptInfo.Builder dialogMetric(){
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login");

    }


}