package com.example.bhtkompassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity{

    Button save;
    EditText firstName, lastName;
    Spinner dropdownStudy;
    ProgressBar loading;
    String studyNameString = "";
    ListView module;
    ArrayList<String> studyNames = new ArrayList<>(), modules = new ArrayList<>();
    ArrayList<String> moduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        save = findViewById(R.id.save);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        dropdownStudy = findViewById(R.id.dropdownStudy);
        module = findViewById(R.id.module);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        save.setEnabled(false);
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

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, modules);
        module.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        module.setAdapter(moduleAdapter);

        ArrayAdapter<CharSequence> studyAdapter = ArrayAdapter.createFromResource(this, R.array.studyName,
                android.R.layout.simple_spinner_item);
        studyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownStudy.setAdapter(studyAdapter);

        save.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            String studyName = dropdownStudy.getSelectedItem().toString();
            int len = module.getCount();
            SparseBooleanArray checked = module.getCheckedItemPositions();
            for (int j = 0; j < len; j++)
                if (checked.get(j)) {
                    String item = moduleAdapter.getItem(j);
                    moduleList.add(item);
                }
            createObject(firstName.getText().toString(), lastName.getText().toString(), studyName, moduleList);
            startLandingActivity();
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkInputComplete()) {
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
                if(checkInputComplete()) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void createObject(String firstName, String lastName, String studyName, ArrayList<String> modules) {
        ParseObject entity = new ParseObject("Profile");
        ParseUser currentUser = ParseUser.getCurrentUser();
        entity.put("user", currentUser.getUsername());
        entity.put("firstName", firstName);
        entity.put("lastName", lastName);
        entity.put("studyName", studyName);
        entity.put("modules", new JSONArray(modules));

        entity.saveInBackground(e -> {
            if (e==null) {

            } else{
                loading.setVisibility(View.GONE);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startLandingActivity() {
        Intent intent = new Intent(ProfileActivity.this, LandingActivity.class);
        intent.putExtra("firstname", firstName.getText().toString());
        intent.putExtra("lastname", lastName.getText().toString());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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
            ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_multiple_choice, modules);
            module.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            module.setAdapter(moduleAdapter);
        }
    }

    private boolean checkInputComplete () {
        if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }
}