package com.duk3r.eortologio2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class argiesadd extends Activity implements OnClickListener {

    String font;
    int text_size, title_text_size = 20;
    int back_color, text_color, border_color;
    static final int DEFAULTDATESELECTOR_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(com.duk3r.eortologio2.R.layout.argiesadd);


        Button bt1 = (Button) findViewById(com.duk3r.eortologio2.R.id.button1);
        bt1.setOnClickListener(this);
        Button bt2 = (Button) findViewById(com.duk3r.eortologio2.R.id.button2);
        bt2.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();

        final WheelView month = (WheelView) findViewById(com.duk3r.eortologio2.R.id.month);
        final WheelView day = (WheelView) findViewById(com.duk3r.eortologio2.R.id.day);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(month, day);
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] { "Ιανουάριος", "Φεβρουάριος",
                "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος",
                "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος",
                "Δεκέμβριος" };
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);

        // day
        updateDays(month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

    }

    void updateDays(WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar
                .get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_prefs();
        set_theme();
    }



    public void get_prefs() {
        SharedPreferences app_preferences = getBaseContext()
                .getSharedPreferences("Shared_Prefs", 0);
        font = app_preferences.getString("font", "default");
        text_size = Integer.parseInt(app_preferences.getString("text_size",
                "16"));
        text_color=app_preferences.getInt("col", 0xffffffff);
        //	back_color=app_preferences.getInt("col1", 0x00000000);

    }



    public void set_theme() {
        //LinearLayout back = (LinearLayout) findViewById(R.id.linearLayout1);
        //back.setBackgroundColor(back_color);
        TextView tv1 = (TextView) findViewById(com.duk3r.eortologio2.R.id.textView1);
        Button bt1 = (Button) findViewById(com.duk3r.eortologio2.R.id.button1);
        Button bt2 = (Button) findViewById(com.duk3r.eortologio2.R.id.button2);

        tv1.setTextSize(22);
        bt1.setTextSize(text_size);
        bt2.setTextSize(text_size);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv1.setTypeface(tf,Typeface.BOLD);

    }

    @Override
    public void onClick(View v) {
        Button bt1 = (Button) findViewById(com.duk3r.eortologio2.R.id.button1);
        Button bt2 = (Button) findViewById(com.duk3r.eortologio2.R.id.button2);
        EditText et = (EditText) findViewById(com.duk3r.eortologio2.R.id.editText1);
        final WheelView mymonth = (WheelView) findViewById(com.duk3r.eortologio2.R.id.month);
        final WheelView myday = (WheelView) findViewById(com.duk3r.eortologio2.R.id.day);
        if (v == bt1) {
            if (et.getText().length() > 0) {
                String name = et.getText().toString();
                String dayname = "";
                int day = myday.getCurrentItem() + 1;
                int month = mymonth.getCurrentItem() + 1;
                int year = 2011;
                Cursor c = null;
                String querystring;
                querystring = "SELECT * FROM argies WHERE day=" + day
                        + " AND month=" + month + " AND name='" + name + "'";
                AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
                SQLiteDatabase db = ADBH.getWritableDatabase();
                c = db.rawQuery(querystring, null);
                if (c != null && c.getCount() > 0) {
                    Toast.makeText(getBaseContext(),
                            "Η αργία υπάρχει στη βάση", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    for (; year < 2021; year++) {
                        Calendar now = Calendar.getInstance();
                        now.set(year, month - 1, day);
                        int dayofweek = now.get(Calendar.DAY_OF_WEEK);
                        switch (dayofweek) {
                            case 1:
                                dayname = "Κυριακή";
                                break;
                            case 2:
                                dayname = "Δευτέρα";
                                break;
                            case 3:
                                dayname = "Τρίτη";
                                break;
                            case 4:
                                dayname = "Τετάρτη";
                                break;
                            case 5:
                                dayname = "Πέμπτη";
                                break;
                            case 6:
                                dayname = "Παρασκευή";
                                break;
                            case 7:
                                dayname = "Σάββατο";
                                break;
                        }
                        String querystring1 = "INSERT INTO argies (year,month,day,name,dayname) VALUES ("
                                + year
                                + ","
                                + month
                                + ","
                                + day
                                + ",'"
                                + name
                                + "','" + dayname + "');";

                        db = ADBH.getWritableDatabase();
                        try {
                            db.execSQL(querystring1);
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        db.close();
                    }
                    Toast.makeText(getBaseContext(), "Επιτυχημένη προσθήκη",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Εισάγετε όνομα αργίας",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (v == bt2) {
            if (et.getText().length() > 0) {
                String name = et.getText().toString();
                int day = myday.getCurrentItem() + 1;
                int month = mymonth.getCurrentItem() + 1;
                int year = 2011;
                Cursor c = null;
                String querystring;
                querystring = "SELECT * FROM argies WHERE day=" + day
                        + " AND month=" + month + " AND name='" + name + "'";
                AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
                SQLiteDatabase db = ADBH.getWritableDatabase();
                c = db.rawQuery(querystring, null);
                if (c != null && c.getCount() > 0) {
                    for (; year < 2021; year++) {
                        db.execSQL("DELETE FROM argies WHERE year=" + year
                                + " AND day=" + day + " AND month=" + month
                                + " AND name='" + name + "';");
                    }
                    Toast.makeText(getBaseContext(), "Επιτυχημένη διαγραφή",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            getBaseContext(),
                            "Δεν υπάρχει η αντίστοιχη αργία τη συγκεκριμένη ημερομηνία",
                            Toast.LENGTH_SHORT).show();
                }
                db.close();
            } else {
                Toast.makeText(getBaseContext(), "Εισάγετε όνομα αργίας",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}
