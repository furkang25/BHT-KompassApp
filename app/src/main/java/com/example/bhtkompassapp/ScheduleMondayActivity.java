package com.example.bhtkompassapp;

import static android.os.Build.VERSION.SDK_INT;

import static androidx.core.content.ContextCompat.startActivity;
import static com.parse.Parse.getApplicationContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripperByArea;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduleMondayActivity extends AppCompatActivity {

    ImageView addSchedule, cancel;
    ProgressBar loading;
    Button save, monday, tuesday, wednesday, thursday, friday, deleteSchedule1, deleteSchedule2, deleteSchedule3, deleteSchedule4, deleteSchedule5, deleteSchedule6, deleteSchedule7;
    Spinner dropdownModule, dropdownProf, dropdownRoom, dropdownTime;
    ArrayList<String> professors = new ArrayList<>(), modules = new ArrayList<>(), rooms = new ArrayList<>();
    TextView studyName1, profName1, roomName1;
    TextView studyName2, profName2, roomName2;
    TextView studyName3, profName3, roomName3;
    TextView studyName4, profName4, roomName4;
    TextView studyName5, profName5, roomName5;
    TextView studyName6, profName6, roomName6;
    TextView studyName7, profName7, roomName7;
    RelativeLayout schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7;
    int CHOOSE_PDF_FROM_DEVICE = 1001;
    ImportPDF importPDF = new ImportPDF();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_monday);
        addSchedule = findViewById(R.id.addSchedule);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);

        studyName1 = findViewById(R.id.studyName1);
        profName1 = findViewById(R.id.profName1);
        roomName1 = findViewById(R.id.roomName1);

        studyName2 = findViewById(R.id.studyName2);
        profName2 = findViewById(R.id.profName2);
        roomName2 = findViewById(R.id.roomName2);

        studyName3 = findViewById(R.id.studyName3);
        profName3 = findViewById(R.id.profName3);
        roomName3 = findViewById(R.id.roomName3);

        studyName3 = findViewById(R.id.studyName3);
        profName3 = findViewById(R.id.profName3);
        roomName3 = findViewById(R.id.roomName3);

        studyName4 = findViewById(R.id.studyName4);
        profName4 = findViewById(R.id.profName4);
        roomName4 = findViewById(R.id.roomName4);

        studyName5 = findViewById(R.id.studyName5);
        profName5 = findViewById(R.id.profName5);
        roomName5 = findViewById(R.id.roomName5);

        studyName6 = findViewById(R.id.studyName6);
        profName6 = findViewById(R.id.profName6);
        roomName6 = findViewById(R.id.roomName6);

        studyName7 = findViewById(R.id.studyName7);
        profName7 = findViewById(R.id.profName7);
        roomName7 = findViewById(R.id.roomName7);

        schedule1 = findViewById(R.id.schedule1);
        schedule2 = findViewById(R.id.schedule2);
        schedule3 = findViewById(R.id.schedule3);
        schedule4 = findViewById(R.id.schedule4);
        schedule5 = findViewById(R.id.schedule5);
        schedule6 = findViewById(R.id.schedule6);
        schedule7 = findViewById(R.id.schedule7);

        deleteSchedule1 = findViewById(R.id.deleteSchedule1);
        deleteSchedule2 = findViewById(R.id.deleteSchedule2);
        deleteSchedule3 = findViewById(R.id.deleteSchedule3);
        deleteSchedule4 = findViewById(R.id.deleteSchedule4);
        deleteSchedule5 = findViewById(R.id.deleteSchedule5);
        deleteSchedule6 = findViewById(R.id.deleteSchedule6);
        deleteSchedule7 = findViewById(R.id.deleteSchedule7);

        resetScheduleLayout();
        loadData();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        addSchedule.setOnClickListener(view -> {
            chooseAddOption();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.scheduleFragment);
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

        monday.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScheduleMondayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        tuesday.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScheduleTuesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        wednesday.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScheduleWednesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        thursday.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScheduleThursdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        friday.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScheduleFridayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        deleteSchedule1.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 1);
        });
        deleteSchedule2.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 2);
        });
        deleteSchedule3.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 3);
        });
        deleteSchedule4.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 4);
        });
        deleteSchedule5.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 5);
        });
        deleteSchedule6.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 6);
        });
        deleteSchedule7.setOnClickListener(view -> {
            startScheduleAlert("Stundenplaneintrag", "Sind sie sich sicher, dass sie diesen Stundenplaneintrag löschen wollen?", 7);
        });
    }

    private void resetScheduleLayout() {
        schedule1.setBackgroundResource(R.drawable.button_normal);
        schedule2.setBackgroundResource(R.drawable.button_normal);
        schedule3.setBackgroundResource(R.drawable.button_normal);
        schedule4.setBackgroundResource(R.drawable.button_normal);
        schedule5.setBackgroundResource(R.drawable.button_normal);
        schedule6.setBackgroundResource(R.drawable.button_normal);
        schedule7.setBackgroundResource(R.drawable.button_normal);
        deleteSchedule1.setVisibility(View.GONE);
        deleteSchedule2.setVisibility(View.GONE);
        deleteSchedule3.setVisibility(View.GONE);
        deleteSchedule4.setVisibility(View.GONE);
        deleteSchedule5.setVisibility(View.GONE);
        deleteSchedule6.setVisibility(View.GONE);
        deleteSchedule7.setVisibility(View.GONE);
    }

    private void showPopUpWindow () throws JSONException, ParseException {
        Dialog newDialog = new Dialog(this);
        newDialog.setContentView(R.layout.activity_schedule_popup);
        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newDialog.show();
        loading = newDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        cancel = newDialog.findViewById(R.id.cancel);
        save = newDialog.findViewById(R.id.save);
        dropdownModule = newDialog.findViewById(R.id.dropdownModule);
        dropdownProf = newDialog.findViewById(R.id.dropdownProf);
        dropdownRoom = newDialog.findViewById(R.id.dropdownRoom);
        dropdownTime = newDialog.findViewById(R.id.dropdownTime);
        loadModuleDate();
        loadProfDate();
        loadRoomDate();

        cancel.setOnClickListener(view -> {
            newDialog.dismiss();
        });

        save.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            createObject(dropdownModule.getSelectedItem().toString(), dropdownProf.getSelectedItem().toString(),
                            dropdownRoom.getSelectedItem().toString(), dropdownTime.getSelectedItem().toString());
            resetScheduleLayout();
            loadData();
            newDialog.dismiss();
        });
    }

    private void startScheduleAlert (String alert, String message, int scheduleNumber) {
        AlertDialog alertDialog = new AlertDialog.Builder(ScheduleMondayActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                (dialog, which) -> {
                    try {
                        deleteSchedule(scheduleNumber);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    loadData();
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    private void startScheduleImportAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ScheduleMondayActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                (dialog, which) -> {
                    deleteScheduleAll();
                    dialog.dismiss();
                    callChooseFileFromDevice();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    public void deleteSchedule (int scheduleNumber) throws ParseException {
        switch (scheduleNumber) {
            case 1:
                deleteScheduleDatabase("Montag", "8:00 - 9:30", ParseUser.getCurrentUser().getUsername());
                break;
            case 2:
                deleteScheduleDatabase("Montag", "10:00 - 11:30", ParseUser.getCurrentUser().getUsername());
                break;
            case 3:
                deleteScheduleDatabase("Montag", "12:15 - 13:45", ParseUser.getCurrentUser().getUsername());
                break;
            case 4:
                deleteScheduleDatabase("Montag", "14:15 - 15:45", ParseUser.getCurrentUser().getUsername());
                break;
            case 5:
                deleteScheduleDatabase("Montag", "16:00 - 17:30", ParseUser.getCurrentUser().getUsername());
                break;
            case 6:
                deleteScheduleDatabase("Montag", "17:45 - 19:15", ParseUser.getCurrentUser().getUsername());
                break;
            case 7:
                deleteScheduleDatabase("Montag", "19:30 - 21:00", ParseUser.getCurrentUser().getUsername());
                break;
        }
    }

    public void deleteScheduleAll () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Schedule");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    try {
                        objects.get(i).delete();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void chooseAddOption() {
        final CharSequence[] options = { "Eintrag hinzufügen", "Stundenplan importieren","Abbrechen" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleMondayActivity.this);
        builder.setTitle("Stundenplan");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Eintrag hinzufügen"))
            {
                try {
                    showPopUpWindow();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (options[item].equals("Stundenplan importieren"))
            {
                if(SDK_INT >= 30) {
                    if (!Environment.isExternalStorageManager()) {
                        Snackbar.make(findViewById(android.R.id.content), "Genehmigung notwendig!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Settings", v -> {
                                    try {
                                        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, uri);
                                        startActivity(intent);
                                    } catch (Exception ex) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }
                startScheduleImportAlert("Stundenplan importieren", "Um ihren Stundenplan zu importieren wird ihr aktueller Stundenplan zurückgesetzt. Sind sie sich sicher, dass sie ihren Stundenplan importieren möchten?");
            }
            else if (options[item].equals("Abbrechen")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void callChooseFileFromDevice() {
        Intent intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory (Intent.CATEGORY_OPENABLE);
        intent.setType ("application/pdf");
        startActivityForResult(intent, CHOOSE_PDF_FROM_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CHOOSE_PDF_FROM_DEVICE && resultCode == Activity.RESULT_OK) {
            // Handle the result of the activity
            if (resultData != null) {
                Uri selectedPdfFromStorage = resultData.getData();
                try {
                    InputStream inputStream = ScheduleMondayActivity.this.getContentResolver().openInputStream(selectedPdfFromStorage);
                    if (!importPDF.importPDF(inputStream)) {
                        startAlert("Stundenplan", "Eingefügte PDF-Datei ist kein Stundenplan der BHT!");
                    }
                    Thread.sleep(3000);
                    loadData();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startCurrentActivity () {
        startActivity(new Intent(getApplicationContext(), ScheduleMondayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void deleteScheduleDatabase (String day, String time, String user) throws ParseException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Schedule");
        query.whereEqualTo("dayTimeUser", day + time + user);
        ParseObject firstItem = query.getFirst();
        firstItem.delete();
        startCurrentActivity();
    }

    public void createObject(String module, String prof, String room, String time) {
        ParseObject entity = new ParseObject("Schedule");
        ParseUser currentUser = ParseUser.getCurrentUser();
        entity.put("user", currentUser.getUsername());
        entity.put("prof", prof);
        entity.put("day", "Montag");
        entity.put("time", time);
        entity.put("room", room);
        entity.put("module", module);
        entity.put("dayTimeUser", "Montag" + time + currentUser.getUsername());

        entity.saveInBackground(e -> {
            if (e==null) {
                startCurrentActivity();
            } else {
                startAlert("Stundenplaneintrag", "Stundenplaneintrag existiert bereits, bitte zuerst löschen.");
            }
        });
        objectExists();
    }

    public void loadData () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Schedule");
        query.whereEqualTo("user", getCurrentUserName());
        query.whereEqualTo("day", "Montag");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    writeSchedule(objects.get(i).getString("module"), objects.get(i).getString("prof"),
                            objects.get(i).getString("room"), objects.get(i).getString("time"));

                }
            }
        });
    }

    public void writeSchedule (String module, String prof, String room, String time) {
        switch (time) {
            case "8:00 - 9:30":
                schedule1.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule1.setVisibility(View.VISIBLE);
                studyName1.setText(module);
                profName1.setText(prof);
                roomName1.setText(room);
                break;
            case "10:00 - 11:30":
                schedule2.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule2.setVisibility(View.VISIBLE);
                studyName2.setText(module);
                profName2.setText(prof);
                roomName2.setText(room);
                break;
            case "12:15 - 13:45":
                schedule3.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule3.setVisibility(View.VISIBLE);
                studyName3.setText(module);
                profName3.setText(prof);
                roomName3.setText(room);
                break;
            case "14:15 - 15:45":
                schedule4.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule4.setVisibility(View.VISIBLE);
                studyName4.setText(module);
                profName4.setText(prof);
                roomName4.setText(room);
                break;
            case "16:00 - 17:30":
                schedule5.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule5.setVisibility(View.VISIBLE);
                studyName5.setText(module);
                profName5.setText(prof);
                roomName5.setText(room);
                break;
            case "17:45 - 19:15":
                schedule6.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule6.setVisibility(View.VISIBLE);
                studyName6.setText(module);
                profName6.setText(prof);
                roomName6.setText(room);
                break;
            case "19:30 - 21:00":
                schedule7.setBackgroundResource(R.drawable.navbar_design);
                deleteSchedule7.setVisibility(View.VISIBLE);
                studyName7.setText(module);
                profName7.setText(prof);
                roomName7.setText(room);
                break;
        }
    }

    public void objectExists () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Schedule");
        query.whereEqualTo("user", getCurrentUserName());
        query.whereEqualTo("day", "Montag");
        query.findInBackground((objects, e) -> {
            if (e == null) {
            }
        });
    }

    public void loadModuleDate () throws ParseException, JSONException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Profile");
        query.whereEqualTo("user", getCurrentUserName());
        ParseObject firstItem = query.getFirst();
        JSONArray modulesJSON = firstItem.getJSONArray("modules");
        modules.clear();
        for (int i = 0; i < modulesJSON.length(); i++)
        {
            modules.add(modulesJSON.getString(i));
        }
    }

    public void loadProfDate () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Professors");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    professors.add(objects.get(i).getString("profName"));
                }
                loadDropDown();
            } else {

            }

        });
    }

    public void loadRoomDate () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Rooms");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    rooms.add(objects.get(i).getString("rooms"));
                }
                loadDropDown();
            } else {

            }

        });
    }

    public String getCurrentUserName() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getUsername();
    }

    public void loadDropDown () {
        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, modules);
        moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownModule.setAdapter(moduleAdapter);

        ArrayAdapter<String> profAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, professors);
        profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownProf.setAdapter(profAdapter);

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rooms);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownRoom.setAdapter(roomAdapter);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.times,
                android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownTime.setAdapter(timeAdapter);
    }

    private void startAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ScheduleMondayActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}