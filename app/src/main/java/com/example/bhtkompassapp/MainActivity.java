package com.example.bhtkompassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    LinearLayout module;
    RelativeLayout top;
    TextView name, email, studyName;
    String firstName, lastName, emailAdress, study;
    JSONArray modules;
    ImageView addImage, profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        studyName = findViewById(R.id.studyName);
        email = findViewById(R.id.email);
        module = findViewById(R.id.module);
        addImage = findViewById(R.id.addImage);
        profile_image = findViewById(R.id.profile_image);
        try {
            setImage();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        top = findViewById(R.id.top);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = top.getLayoutParams();
        if (height < 2000) {
            params.height = 550;
            top.setLayoutParams(params);
        } else if (height < 2500) {
            params.height = 650;
            top.setLayoutParams(params);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.homeFragment:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
                case R.id.roomMapFragment:
                    startActivity(new Intent(getApplicationContext(), MapActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
                case R.id.scheduleFragment:
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (day) {
                        case Calendar.TUESDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleTuesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.WEDNESDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleWednesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.THURSDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleThursdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.FRIDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleFridayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        default:
                            startActivity(new Intent(getApplicationContext(), ScheduleMondayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                    }
                    break;
                case R.id.settingsFragment:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
            }
            return true;
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        try {
            readObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        name.setText(firstName + " " + lastName);
        email.setText(emailAdress);
        studyName.setText(study);
        TextView[] textViews = new TextView[modules.length()];
        for (int i = 0; i < textViews.length; i++) {
            try {
                textViews[i] = new TextView(this);
                textViews[i].setText("• " + modules.getString(i));
                Typeface typeface = ResourcesCompat.getFont(this, R.font.rubik);
                textViews[i].setTypeface(typeface);
                textViews[i].setTextSize(25);
                module.addView(textViews[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        addImage.setOnClickListener(view -> {
            selectImage();
        });

    }

    public void setImage () throws ParseException {
        byte[] data = readImage().getData();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        profile_image.setImageBitmap(bitmap);
    }

    public void readObject() throws ParseException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Profile");
        query.whereEqualTo("user", getCurrentUserName());
        ParseObject firstItem = query.getFirst();
        firstName = firstItem.getString("firstName");
        lastName = firstItem.getString("lastName");
        emailAdress = ParseUser.getCurrentUser().getEmail();
        study = firstItem.getString("studyName");
        modules = firstItem.getJSONArray("modules");
    }

    public String getCurrentUserName() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getUsername();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 80, stream);
            try {
                saveImage(stream.toByteArray());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 80, stream);
                saveImage(stream.toByteArray());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public ParseFile readImage () throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        return firstItem.getParseFile("image");
    }

    public void saveImage (byte[] bytes) throws ParseException, InterruptedException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        firstItem.put("image", new ParseFile("image.png", bytes));
        firstItem.saveInBackground();
        Thread.sleep(2000);
        setImage();
    }

    private void selectImage() {
        final CharSequence[] options = { "Kamera", "Galerie","Abbrechen" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Bild hinzufügen/ändern");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Kamera"))
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
            else if (options[item].equals("Galerie"))
            {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
            else if (options[item].equals("Abbrechen")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "Kamerazugriff nicht erlaubt", Toast.LENGTH_LONG).show();
            }
        }
    }

}