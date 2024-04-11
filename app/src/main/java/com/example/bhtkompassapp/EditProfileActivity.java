package com.example.bhtkompassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back;
    Button save;
    EditText firstName, lastName;
    Spinner dropdownStudy;
    ListView module;
    String studyNameString = "";
    ArrayList<String> studyNames = new ArrayList<>(), modules = new ArrayList<>();
    ArrayList<String> moduleList = new ArrayList<>();
    JSONArray chosenModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back = findViewById(R.id.back);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        dropdownStudy = findViewById(R.id.dropdownStudy);
        module = findViewById(R.id.module);
        save = findViewById(R.id.save);
        save.setEnabled(false);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        setModuleSpinner();
        try {
            setProfileData();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        back.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, modules);
        module.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        module.setAdapter(moduleAdapter);

        save.setOnClickListener(view -> {
            int len = module.getCount();
            SparseBooleanArray checked = module.getCheckedItemPositions();
            for (int j = 0; j < len; j++)
                if (checked.get(j)) {
                    String item = moduleAdapter.getItem(j);
                    moduleList.add(item);
                }
            try {
                updateObject();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
    }

    public void preSetModules () throws ParseException, JSONException, InterruptedException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        chosenModules = firstItem.getJSONArray("modules");
        for (int i = 0; i < chosenModules.length(); i++) {
            int x = modules.indexOf(chosenModules.getString(i));
            module.setItemChecked(x, true);
        }
    }

    public void setModuleSpinner () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Modules");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    studyNames.add(objects.get(i).getString("studyName"));
                }
            } else {
                Log.e("TAG", "Parse Error: ", e);
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkInputComplete()) {
                    save.setEnabled(true);
                }
                else {
                    save.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkInputComplete()) {
                    save.setEnabled(true);
                }
                else {
                    save.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        dropdownStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                studyNameString = dropdownStudy.getItemAtPosition(i).toString();
                try {
                    loadData();
                    preSetModules();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void loadData () throws JSONException, ParseException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Modules");
        query.whereEqualTo("studyName", studyNameString);
        ParseObject firstItem = query.getFirst();
        JSONArray modulesJSON = firstItem.getJSONArray("modules");
        modules.clear();
        for (int i = 0; i < modulesJSON.length(); i++)
        {
            modules.add(modulesJSON.getString(i));
        }
        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, modules);
        module.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        module.setAdapter(moduleAdapter);
    }

    public void setProfileData () throws ParseException, JSONException {
        ParseQuery<ParseObject> query = new ParseQuery<>("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        firstName.setText(firstItem.getString("firstName"));
        lastName.setText(firstItem.getString("lastName"));

        ArrayAdapter<CharSequence> studyAdapter = ArrayAdapter.createFromResource(this, R.array.studyName,
                android.R.layout.simple_spinner_item);
        studyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownStudy.setAdapter(studyAdapter);

        studyNameString = firstItem.getString("studyName");
        ArrayAdapter adapterStudyName = (ArrayAdapter) dropdownStudy.getAdapter();
        int spinnerPositionStudyName = adapterStudyName.getPosition(studyNameString);
        dropdownStudy.setSelection(spinnerPositionStudyName);


    }

    private boolean checkInputComplete () {
        if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public void updateObject() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        firstItem.put("firstName", firstName.getText().toString());
        firstItem.put("lastName", lastName.getText().toString());
        firstItem.put("studyName", dropdownStudy.getSelectedItem().toString());
        firstItem.put("modules", new JSONArray(moduleList));
        firstItem.saveInBackground();
    }
}