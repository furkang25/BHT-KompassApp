package com.example.bhtkompassapp;

import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripperByArea;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity2 extends AppCompatActivity {

    int CHOOSE_PDF_FROM_DEVICE = 1001;
    private final int STORAGE_PERMISSION_CODE = 1;
    Button button;
    EditText x, y, width, height;
    TextView pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.button);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        pdf = findViewById(R.id.pdf);

        button.setOnClickListener(view -> {
            if(SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
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
            callChooseFileFromDevice();
        });
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
                    InputStream inputStream = MainActivity2.this.getContentResolver().openInputStream(selectedPdfFromStorage);
                    readPdf(Integer.parseInt(x.getText().toString()), Integer.parseInt(y.getText().toString()),
                            Integer.parseInt(width.getText().toString()), Integer.parseInt(height.getText().toString()), inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readPdf (int x, int y, int width, int height, InputStream inputStream) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        PDFBoxResourceLoader.init(getApplicationContext());
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        RectF rect = new RectF(x, y, width, height);
        stripper.addRegion("class1", rect);
        stripper.extractRegions(document.getPage(0));
        pdf.setText(stripper.getTextForRegion("class1"));
    }

    public void saveImage (byte[] bytes) throws ParseException, InterruptedException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        ParseObject firstItem = query.getFirst();
        firstItem.put("schedulePDF", new ParseFile("schedule.pdf", bytes));
        firstItem.saveInBackground();
        Thread.sleep(2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity2.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity2.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}