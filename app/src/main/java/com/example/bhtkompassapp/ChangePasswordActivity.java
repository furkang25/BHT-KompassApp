package com.example.bhtkompassapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    ImageView back;
    EditText password, passwordRepeat;
    TextView error;
    Button save;
    ImageView hintNewPassword, hintNewPasswordAgain;
    boolean hintPasswordClicked = false, hintPasswordRepeatClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        password = findViewById(R.id.newPassword);
        passwordRepeat = findViewById(R.id.newPasswordAgain);
        error = findViewById(R.id.error);
        hintNewPassword = findViewById(R.id.hintNewPassword);
        hintNewPasswordAgain = findViewById(R.id.hintNewPasswordAgain);
        save.setEnabled(false);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        back.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        save.setOnClickListener(view -> {
            if (validatePassword(password.getText().toString())) {
                startPasswordAlert("Passwort", "Sind sie sich sicher, dass sie ihr Passwort ändern möchten?");
            } else {
                startAlert("Passwort nicht valide!", "Passwort muss folgende Voraussetzungen erfüllen: \n- 8 Zeichen\n- Mindestens einen Großbuchtsaben\n- Keine Leerzeichen ");
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    save.setEnabled(true);
                    error.setText("");
                }
                else {
                    save.setEnabled(false);
                    error.setText("Passwörter stimmen nicht überein");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
                    save.setEnabled(true);
                    error.setText("");
                }
                else {
                    save.setEnabled(false);
                    error.setText("Passwörter stimmen nicht überein");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        hintNewPassword.setOnClickListener(view -> {
            if (hintPasswordClicked) {
                hintPasswordClicked = false;
                password.setTransformationMethod(new PasswordTransformationMethod());
                hintNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24_hint));
            } else {
                hintPasswordClicked = true;
                password.setTransformationMethod(null);
                hintNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
            }
        });

        hintNewPasswordAgain.setOnClickListener(view -> {
            if (hintPasswordRepeatClicked) {
                hintPasswordRepeatClicked = false;
                passwordRepeat.setTransformationMethod(new PasswordTransformationMethod());
                hintNewPasswordAgain.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24_hint));
            } else {
                hintPasswordRepeatClicked = true;
                passwordRepeat.setTransformationMethod(null);
                hintNewPasswordAgain.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void startAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void startPasswordAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                (dialog, which) -> {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.setPassword(password.getText().toString());
                    currentUser.saveInBackground();
                    ParseUser.logOutInBackground(e -> {
                        if (e == null) {
                            startLoginActivity();
                        }
                    });
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    private boolean checkInputComplete () {
        if (password.getText().toString().equals(passwordRepeat.getText().toString()) &&
                !password.getText().toString().isEmpty()) {
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