package com.example.bhtkompassapp;

import static com.parse.Parse.getApplicationContext;
import android.graphics.RectF;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripperByArea;
import java.io.IOException;
import java.io.InputStream;

public class ImportPDF {

    PDDocument document;
    PDFTextStripperByArea stripper;

    public boolean importPDF (InputStream inputStream) throws IOException {
        document = PDDocument.load(inputStream);
        PDFBoxResourceLoader.init(getApplicationContext());
        stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        if (getData(200, 50, 380, 60).trim().equals("Stundenplan für Studierende") &&
                getData(470, 560, 615, 570).trim().equals("http://www.bht-berlin.de/vrp.")) {
            checkMonday();
            checkTuesday();
            checkWednesday();
            checkThursday();
            checkFriday();
            return true;
        } else {
            return false;
        }
    }

    public void createObject(String room, String module, String prof, String time, String day) {
        ParseObject entity = new ParseObject("Schedule");
        ParseUser currentUser = ParseUser.getCurrentUser();
        entity.put("user", currentUser.getUsername());
        entity.put("prof", prof);
        entity.put("day", day);
        entity.put("time", time);
        entity.put("room", room);
        entity.put("module", module);
        entity.put("dayTimeUser", day + time + currentUser.getUsername());

        entity.saveInBackground(e -> {
            if (e==null) {

            } else {

            }
        });
    }

    public void checkMonday () throws IOException {
        if (!getData(50, 160,150,170).isEmpty()) {
            createObject(getData(50, 160,150,170).trim(),
                    getData(50, 170,200,190).trim(),
                    getData(50, 190,150,200).trim(), "8:00 - 9:30", "Montag");
        }
        if (!getData(50, 220,150,230).isEmpty()) {
            createObject(getData(50, 220,150,230).trim(),
                    getData(50, 230,200,250).trim(),
                    getData(50, 250,150,260).trim(), "10:00 - 11:30", "Montag");
        }
        if (!getData(50, 280,150,290).isEmpty()) {
            createObject(getData(50, 280,150,290).trim(),
                    getData(50, 290,200,310).trim(),
                    getData(50, 310,150,320).trim(), "12:15 - 13:45", "Montag");
        }
        if (!getData(50, 330,150,340).isEmpty()) {
            createObject(getData(50, 330,150,340).trim(),
                    getData(50, 340,200,360).trim(),
                    getData(50, 360,150,370).trim(), "14:15 - 15:45", "Montag");
        }
        if (!getData(50, 380,150,390).isEmpty()) {
            createObject(getData(50, 380,150,390).trim(),
                    getData(50, 390,200,410).trim(),
                    getData(50, 410,150,420).trim(), "16:00 - 17:30", "Montag");
        }
        if (!getData(50, 430,150,440).isEmpty()) {
            createObject(getData(50, 430,150,440).trim(),
                    getData(50, 440,200,460).trim(),
                    getData(50, 460,150,470).trim(), "17:45 - 19:15", "Montag");
        }
        if (!getData(50, 490,150,500).isEmpty()) {
            createObject(getData(50, 490,150,500).trim(),
                    getData(50, 500,200,520).trim(),
                    getData(50, 520,150,530).trim(), "19:30 - 21:00", "Montag");
        }
    }

    public void checkTuesday () throws IOException {
        if (!getData(205, 160,305,170).isEmpty()) {
            createObject(getData(205, 160,305,170).trim(),
                    getData(205, 170,360,190).trim(),
                    getData(205, 190,305,200).trim(), "8:00 - 9:30", "Dienstag");
        }
        if (!getData(205, 220,305,230).isEmpty()) {
            Log.e("test", getData(205, 220,305,230));
            createObject(getData(205, 220,305,230).trim(),
                    getData(205, 230,360,250).trim(),
                    getData(205, 250,305,260).trim(), "10:00 - 11:30", "Dienstag");
        }
        if (!getData(205, 280,305,290).isEmpty()) {
            Log.e("test", getData(205, 280,305,290));
            createObject(getData(205, 280,305,290).trim(),
                    getData(205, 290,360,310).trim(),
                    getData(205, 310,305,320).trim(), "12:15 - 13:45", "Dienstag");
        }
        if (!getData(205, 330,305,340).isEmpty()) {
            Log.e("test", getData(205, 330,305,340));
            createObject(getData(205, 330,305,340).trim(),
                    getData(205, 340,360,360).trim(),
                    getData(205, 360,305,370).trim(), "14:15 - 15:45", "Dienstag");
        }
        if (!getData(205, 380,305,390).isEmpty()) {
            Log.e("test", getData(205, 380,305,390));
            createObject(getData(205, 380,305,390).trim(),
                    getData(205, 390,360,410).trim(),
                    getData(205, 410,305,420).trim(), "16:00 - 17:30", "Dienstag");
        }
        if (!getData(205, 430,305,440).isEmpty()) {
            Log.e("test", getData(205, 430,305,440));
            createObject(getData(205, 430,305,440).trim(),
                    getData(205, 440,360,460).trim(),
                    getData(205, 460,305,470).trim(), "17:45 - 19:15", "Dienstag");
        }
        if (!getData(205, 490,305,500).isEmpty()) {
            createObject(getData(205, 490,305,500).trim(),
                    getData(205, 500,360,520).trim(),
                    getData(205, 520,305,530).trim(), "19:30 - 21:00", "Dienstag");
        }
    }

    public void checkWednesday () throws IOException {
        if (!getData(360, 160,460,170).isEmpty()) {
            createObject(getData(360, 160,460,170).trim(),
                    getData(360, 170,510,190).trim(),
                    getData(360, 190,460,200).trim(), "8:00 - 9:30", "Mittwoch");
        }
        if (!getData(360, 220,460,230).isEmpty()) {
            createObject(getData(360, 220,460,230).trim(),
                    getData(360, 230,510,250).trim(),
                    getData(360, 250,460,260).trim(), "10:00 - 11:30", "Mittwoch");
        }
        if (!getData(360, 280,460,290).isEmpty()) {
            createObject(getData(360, 280,460,290).trim(),
                    getData(360, 290,510,310).trim(),
                    getData(360, 310,460,320).trim(), "12:15 - 13:45", "Mittwoch");
        }
        if (!getData(360, 330,460,340).isEmpty()) {
            createObject(getData(360, 330,460,340).trim(),
                    getData(360, 340,510,360).trim(),
                    getData(360, 360,460,370).trim(), "14:15 - 15:45", "Mittwoch");
        }
        if (!getData(360, 380,460,390).isEmpty()) {
            createObject(getData(360, 380,460,390).trim(),
                    getData(360, 390,510,410).trim(),
                    getData(360, 410,460,420).trim(), "16:00 - 17:30", "Mittwoch");
        }
        if (!getData(360, 430,460,440).isEmpty()) {
            createObject(getData(360, 430,460,440).trim(),
                    getData(360, 440,510,460).trim(),
                    getData(360, 460,460,470).trim(), "17:45 - 19:15", "Mittwoch");
        }
        if (!getData(360, 490,460,500).isEmpty()) {
            createObject(getData(360, 490,460,500).trim(),
                    getData(360, 500,510,520).trim(),
                    getData(360, 520,460,530).trim(), "19:30 - 21:00", "Mittwoch");
        }
    }

    public void checkThursday () throws IOException {
        if (!getData(510, 160,615,170).isEmpty()) {
            createObject(getData(510, 160,615,170).trim(),
                    getData(510, 170,660,190).trim(),
                    getData(510, 190,615,200).trim(), "8:00 - 9:30", "Donnerstag");
        }
        if (!getData(510, 220,615,230).isEmpty()) {
            createObject(getData(510, 220,615,230).trim(),
                    getData(510, 230,660,250).trim(),
                    getData(510, 250,615,260).trim(), "10:00 - 11:30", "Donnerstag");
        }
        if (!getData(510, 280,615,290).isEmpty()) {
            createObject(getData(510, 280,615,290).trim(),
                    getData(510, 290,660,310).trim(),
                    getData(510, 310,615,320).trim(), "12:15 - 13:45", "Donnerstag");
        }
        if (!getData(510, 330,615,340).isEmpty()) {
            createObject(getData(510, 330,615,340).trim(),
                    getData(510, 340,660,360).trim(),
                    getData(510, 360,615,370).trim(), "14:15 - 15:45", "Donnerstag");
        }
        if (!getData(510, 380,615,390).isEmpty()) {
            createObject(getData(510, 380,615,390).trim(),
                    getData(510, 390,660,410).trim(),
                    getData(510, 410,615,420).trim(), "16:00 - 17:30", "Donnerstag");
        }
        if (!getData(510, 430,615,440).isEmpty()) {
            createObject(getData(510, 430,615,440).trim(),
                    getData(510, 440,660,460).trim(),
                    getData(510, 460,615,470).trim(), "17:45 - 19:15", "Donnerstag");
        }
        if (!getData(510, 490,615,500).isEmpty()) {
            createObject(getData(510, 490,615,500).trim(),
                    getData(510, 500,660,520).trim(),
                    getData(510, 520,615,530).trim(), "19:30 - 21:00", "Donnerstag");
        }
    }

    public void checkFriday () throws IOException {
        if (!getData(665, 160,760,170).isEmpty()) {
            createObject(getData(665, 160,760,170).trim(),
                    getData(665, 170,820,190).trim(),
                    getData(665, 190,760,200).trim(), "8:00 - 9:30", "Freitag");
        }
        if (!getData(665, 220,760,230).isEmpty()) {
            createObject(getData(665, 220,760,230).trim(),
                    getData(665, 230,820,250).trim(),
                    getData(665, 250,760,260).trim(), "10:00 - 11:30", "Freitag");
        }
        if (!getData(665, 280,760,290).isEmpty()) {
            createObject(getData(665, 280,760,290).trim(),
                    getData(665, 290,820,310).trim(),
                    getData(665, 310,760,320).trim(), "12:15 - 13:45", "Freitag");
        }
        if (!getData(665, 330,760,340).isEmpty()) {
            createObject(getData(665, 330,760,340).trim(),
                    getData(665, 340,820,360).trim(),
                    getData(665, 360,760,370).trim(), "14:15 - 15:45", "Freitag");
        }
        if (!getData(665, 380,760,390).isEmpty()) {
            createObject(getData(665, 380,760,390).trim(),
                    getData(665, 390,820,410).trim(),
                    getData(665, 410,760,420).trim(), "16:00 - 17:30", "Freitag");
        }
        if (!getData(665, 430,760,440).isEmpty()) {
            createObject(getData(665, 430,760,440).trim(),
                    getData(665, 440,820,460).trim(),
                    getData(665, 460,760,470).trim(), "17:45 - 19:15", "Freitag");
        }
        if (!getData(665, 490,760,500).isEmpty()) {
            createObject(getData(665, 490,760,500).trim(),
                    getData(665, 500,820,520).trim(),
                    getData(665, 520,760,530).trim(), "19:30 - 21:00", "Freitag");
        }
    }

    public String getData (int x, int y, int width, int height) throws IOException {
        stripper.addRegion("class1", new RectF(x, y, width, height));
        stripper.extractRegions(document.getPage(0));
        return stripper.getTextForRegion("class1");
    }
}
