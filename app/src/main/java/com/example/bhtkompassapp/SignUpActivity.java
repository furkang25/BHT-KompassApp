package com.example.bhtkompassapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    Button signup;
    EditText userName, password, passwordRepeat, email;
    TextView hasUser, error;
    ProgressBar loading;
    ImageView hintPassword, hintPasswordRepeat;
    boolean hintPasswordClicked = false, hintPasswordRepeatClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);
        signup = findViewById(R.id.signup);
        hasUser = findViewById(R.id.hasUser);
        email = findViewById(R.id.email);
        error = findViewById(R.id.error);
        loading = findViewById(R.id.loading);
        hintPassword = findViewById(R.id.hintPassword);
        hintPasswordRepeat = findViewById(R.id.hintPasswordRepeat);
        signup.setEnabled(false);
        loading.setVisibility(View.GONE);

        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    signup.setEnabled(true);
                    error.setText("");
                }
                else {
                    error.setText("Passwörter stimmen nicht überein");
                    signup.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    signup.setEnabled(true);
                    error.setText("");
                }
                else {
                    error.setText("Passwörter stimmen nicht überein");
                    signup.setEnabled(false);
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
                    signup.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    signup.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
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

        hintPasswordRepeat.setOnClickListener(view -> {
            if (hintPasswordRepeatClicked) {
                hintPasswordRepeatClicked = false;
                passwordRepeat.setTransformationMethod(new PasswordTransformationMethod());
                hintPasswordRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24_hint));
            } else {
                hintPasswordRepeatClicked = true;
                passwordRepeat.setTransformationMethod(null);
                hintPasswordRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
            }
        });

        hasUser.setOnClickListener(view -> startLoginActivity());
        signup.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            if (validatePassword(password.getText().toString())) {
                ParseUser user = new ParseUser();
                user.setUsername(userName.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.signUpInBackground(e -> {
                    if (e == null) {
                        loading.setVisibility(View.GONE);
                        startProfileActivity();
                    }
                    else if (e.getCode() == 202) {
                        startAlert("Anmeldename", "Anmeldename existiert bereits!");
                        loading.setVisibility(View.GONE);
                    }
                    else if (e.getCode() == 125) {
                        startAlert("E-Mail-Adresse", "Format der E-Mail-Adresse stimmt nicht!");
                        loading.setVisibility(View.GONE);
                    }
                    else if (e.getCode() == 203) {
                        startAlert("E-Mail-Adresse", "Benutzer mit angegebener E-Mail-Adresse existier bereits!");
                        loading.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }
                });
            }
            else {
                startAlert("Passwort nicht valide!", "Passwort muss folgende Voraussetzungen erfüllen: \n- 8 Zeichen\n- Mindestens einen Großbuchtsaben\n- Keine Leerzeichen ");
                loading.setVisibility(View.GONE);
            }
        });


    }
    private void startProfileActivity() {
        Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void startLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void startAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private boolean checkInputComplete () {
        if (password.getText().toString().equals(passwordRepeat.getText().toString()) &&
                !password.getText().toString().isEmpty() && !userName.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean validatePassword (String password) {
        if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
            /*Regex explained:
                ^                 # start-of-string
                (?=.*[0-9])       # at least one digit
                (?=.*[a-z])       # at least one lower case character
                (?=.*[A-Z])       # at least one upper case character
                (?=\S+$)          # no whitespace
                .{8, 20}          # at least 8 characters and max 20
                $                 # end-of-string
             */
            return true;
        }
        return false;
    }
}