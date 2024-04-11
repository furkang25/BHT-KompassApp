package com.example.bhtkompassapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText userName, email, password;
    ProgressBar loading, loadingPaswordReset;
    Button login, resetPassword;
    TextView hasNoUser, passwordForgot;
    ImageView hintPassword;
    boolean hintPasswordClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        setContentView(R.layout.activity_login);
        ParseUser.getCurrentUser().logOut();
        ParseInstallation.getCurrentInstallation().saveInBackground();
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        hasNoUser = findViewById(R.id.hasNoUser);
        loading = findViewById(R.id.loading);
        passwordForgot = findViewById(R.id.passwordForgot);
        hintPassword = findViewById(R.id.hintPassword);
        login.setEnabled(false);
        loading.setVisibility(View.GONE);

        login.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), (user, e) -> {
                if (user!=null){
                    loading.setVisibility(View.GONE);
                    startHomeActivity();
                } else if (e.getCode() == 101) {
                    startAlert("Anmeldung fehlgeschlagen", "Anmeldename oder Passwort falsch!");
                    loading.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("e", "" + e.getCode());
                    loading.setVisibility(View.GONE);
                }
            });
        });

        hasNoUser.setOnClickListener(view -> startSignUpActivity());

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    login.setEnabled(true);
                }
                else {
                    login.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    login.setEnabled(true);
                }
                else {
                    login.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        passwordForgot.setOnClickListener(view -> {
            showPopUpWindow();
        });

        hintPassword.setOnClickListener(view -> {
            if (hintPasswordClicked) {
                hintPasswordClicked = false;
                password.setTransformationMethod(new PasswordTransformationMethod());
                hintPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24_hint));
            } else {
                hintPasswordClicked = true;
                password.setTransformationMethod(null);
                hintPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void startAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private boolean checkInputComplete () {
        if (!password.getText().toString().isEmpty() && !userName.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private void showPopUpWindow () {
        Dialog newDialog = new Dialog(this);
        newDialog.setContentView(R.layout.activity_password_forgot_popup);
        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newDialog.show();

        resetPassword = newDialog.findViewById(R.id.resetPassword);
        email = newDialog.findViewById(R.id.email);
        loadingPaswordReset = newDialog.findViewById(R.id.loadingPaswordReset);

        resetPassword.setEnabled(false);
        loadingPaswordReset.setVisibility(View.GONE);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("a", email.getText().toString());
                if (!email.getText().toString().isEmpty()) {
                    resetPassword.setEnabled(true);
                }
                else {
                    resetPassword.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        resetPassword.setOnClickListener(view -> {
            loadingPaswordReset.setVisibility(View.VISIBLE);
            String emailAddress = email.getText().toString();
            boolean valid = validate(emailAddress);
            if (valid) {
                ParseUser.requestPasswordResetInBackground("" + emailAddress, e -> {
                    if (e == null) {
                        newDialog.dismiss();
                        startAlert("Passwort zurücksetzen", "E-Mail wurde verschickt!");
                    } else {
                        newDialog.dismiss();
                        startAlert("Passwort zurücksetzen", e.getMessage());
                    }
                });
            } else {
                startAlert("E-Mail-Adresse", "Eingegebene E-Mail-Adresse ist invalide");
                loadingPaswordReset.setVisibility(View.GONE);
            }
        });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}