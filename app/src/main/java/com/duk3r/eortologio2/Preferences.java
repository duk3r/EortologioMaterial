package com.duk3r.eortologio2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Preferences extends PreferenceActivity implements
        OnPreferenceClickListener {

    public static String CONTACTS_CEL = "com.duk3r.eortologio2.CONTACTS_CEL";
    public static String WIDGET_THEME = "com.duk3r.eortologio2.WIDGET_THEME";
    public static String DAY_CHANGED = "com.duk3r.eortologio2.DAY_CHANGED";
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int CONTACT_PICKER_RESULT_EORTES = 1002;
    private int cid;
    public String name_selected = "";
    public String eortes_name_selected = "";
    public boolean notification;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        Preference eortes = (Preference) findPreference("eortes");
        eortes.setOnPreferenceClickListener(this);
        Preference bday = (Preference) findPreference("bday");
        bday.setOnPreferenceClickListener(this);
        Preference argies = (Preference) findPreference("argies");
        argies.setOnPreferenceClickListener(this);
        Preference bday_list = (Preference) findPreference("bday_list");
        bday_list.setOnPreferenceClickListener(this);
        Preference notify = (Preference) findPreference("notify");
        notify.setOnPreferenceClickListener(this);
        CheckBoxPreference notify1 = (CheckBoxPreference) findPreference("notify1");
        if (notify1.isChecked()) {
            notification = true;
        } else {
            notification = false;
        }
        notify1.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (notification == true) {
                    notification = false;
                } else {
                    notification = true;
                }
                SharedPreferences app_preferences = getSharedPreferences(
                        "Shared_Prefs", MODE_PRIVATE);
                app_preferences.edit().putBoolean("notify1", notification)
                        .commit();
                return notification;
            }
        });

        Preference eortes_list = (Preference) findPreference("eortes_list");
        eortes_list.setOnPreferenceClickListener(this);
        Preference help = (Preference) findPreference("help");
        Preference exportDB = (Preference) findPreference("exportDB");
        exportDB.setOnPreferenceClickListener(this);
        Preference importDB = (Preference) findPreference("importDB");
        importDB.setOnPreferenceClickListener(this);
        Preference importExportHelp = (Preference) findPreference("importExportHelp");
        importExportHelp.setOnPreferenceClickListener(this);
        help.setOnPreferenceClickListener(this);
        Preference about = (Preference) findPreference("about");
        about.setOnPreferenceClickListener(this);
        Preference changelog = (Preference) findPreference("changelog");
        changelog.setOnPreferenceClickListener(this);
        ListPreference text_size = (ListPreference) findPreference("text_size");
        text_size
                .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference,
                                                      Object newValue) {
                        SharedPreferences app_preferences = getSharedPreferences(
                                "Shared_Prefs", MODE_PRIVATE);
                        app_preferences.edit()
                                .putString("text_size", (String) newValue)
                                .apply();
                        return true;
                    }
                });

		/*
		 * ListPreference text_color_widget = (ListPreference)
		 * findPreference("text_color_widget");
		 * text_color_widget.setOnPreferenceChangeListener(new
		 * OnPreferenceChangeListener() {
		 * 
		 * @Override public boolean onPreferenceChange(Preference preference,
		 * Object newValue) { SharedPreferences app_preferences =
		 * getSharedPreferences("Shared_Prefs",MODE_PRIVATE);
		 * app_preferences.edit().putString("text_color_widget",
		 * (String)newValue).commit(); Context context = (Context)
		 * getBaseContext(); AlarmManager alarmManager = (AlarmManager)
		 * context.getSystemService(Context.ALARM_SERVICE); Intent theme = new
		 * Intent(); theme.setAction(WIDGET_THEME); PendingIntent pendingIntent
		 * = PendingIntent.getBroadcast(context, 123, theme, 0); Calendar now =
		 * Calendar.getInstance(); alarmManager.set(AlarmManager.RTC_WAKEUP,
		 * now.getTimeInMillis(), pendingIntent); return true; } });
		 */
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        Preference eortes = (Preference) findPreference("eortes");
        Preference bday = (Preference) findPreference("bday");
        Preference argies = (Preference) findPreference("argies");
        Preference notify = (Preference) findPreference("notify");
        Preference help = (Preference) findPreference("help");
        Preference about = (Preference) findPreference("about");
        Preference bday_list = (Preference) findPreference("bday_list");
        Preference eortes_list = (Preference) findPreference("eortes_list");
        Preference text_color_widget = (Preference) findPreference("text_color_widget");
        Preference changelog = (Preference) findPreference("changelog");
        Preference importDB = (Preference) findPreference("importDB");
        Preference exportDB = (Preference) findPreference("exportDB");
        Preference importExportHelp = (Preference) findPreference("importExportHelp");

        final BdayDataBaseHelper BDBH = new BdayDataBaseHelper(this);
        final UserDataBaseHelper UDBH = new UserDataBaseHelper(this);

        if (preference == eortes) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.eortesadd);
            dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            dialog.setTitle("Τροποποίηση Εορτών");
            Calendar calendar = Calendar.getInstance();
            final WheelView month1 = (WheelView) dialog
                    .findViewById(R.id.month);
            final WheelView day1 = (WheelView) dialog.findViewById(R.id.day);
            OnWheelChangedListener listener = new OnWheelChangedListener() {
                public void onChanged(WheelView wheel, int oldValue,
                                      int newValue) {
                    updateDays(month1, day1);
                }
            };
            int curMonth = calendar.get(Calendar.MONTH);
            String months[] = new String[] { "Ιανουάριος", "Φεβρουάριος",
                    "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος",
                    "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος",
                    "Δεκέμβριος" };
            month1.setViewAdapter(new DateArrayAdapter(getBaseContext(),
                    months, curMonth));
            month1.setCurrentItem(curMonth);
            month1.addChangingListener(listener);

            updateDays(month1, day1);
            day1.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
            dialog.show();
            Button select = (Button) dialog
                    .findViewById(R.id.name_picker_button_eortes);
            select.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            Contacts.CONTENT_URI);
                    startActivityForResult(contactPickerIntent,
                            CONTACT_PICKER_RESULT_EORTES);
                }
            });
            Button save = (Button) dialog.findViewById(R.id.buttonaddeortes01);
            save.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int day = day1.getCurrentItem() + 1;
                    int month = month1.getCurrentItem() + 1;
                    String contactname = eortes_name_selected;
                    SQLiteDatabase db = UDBH.getWritableDatabase();
                    if (contactname.length() > 0) {
                        String customdb_table = "user_table";
                        Cursor c = null;
                        String querystring = "SELECT * FROM " + customdb_table
                                + " WHERE name=\"" + contactname
                                + "\" AND day='" + day + "' AND month='"
                                + month + "';";
                        c = db.rawQuery(querystring, null);

                        if (c != null && c.getCount() > 0) {
                            Toast.makeText(getBaseContext(),
                                    "Η επαφή υπάρχει στην βάση",
                                    Toast.LENGTH_SHORT).show();
                            c.close();
                        } else {
                            String querystring1 = "INSERT INTO "
                                    + customdb_table + " (name, day, month)"
                                    + " VALUES ('" + contactname + "', " + day
                                    + ", " + month + ");";
                            db.execSQL(querystring1);
                            Toast.makeText(getBaseContext(),
                                    "Επιτυχημένη προσθήκη", Toast.LENGTH_SHORT)
                                    .show();
                            Context context = (Context) getBaseContext();
                            AlarmManager alarmManager = (AlarmManager) context
                                    .getSystemService(Context.ALARM_SERVICE);
                            Intent theme = new Intent();
                            theme.setAction(WIDGET_THEME);
                            PendingIntent pendingIntent = PendingIntent
                                    .getBroadcast(context, 123, theme, 0);
                            Calendar now = Calendar.getInstance();
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    now.getTimeInMillis(), pendingIntent);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Επιλέξτε επαφή",
                                Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            });
            Button back = (Button) dialog.findViewById(R.id.buttonbackeortes01);
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }

            });
            Button del = (Button) dialog.findViewById(R.id.buttondeleortes01);
            del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String contactname = eortes_name_selected;
                    int day = day1.getCurrentItem() + 1;
                    int month = month1.getCurrentItem() + 1;
                    if (contactname.length() > 0) {
                        SQLiteDatabase db = UDBH.getWritableDatabase();
                        String customdb_table = "user_table";
                        Cursor c = null;
                        c = db.rawQuery("SELECT * FROM " + customdb_table
                                + " WHERE name=\"" + contactname
                                + "\" AND day=" + day + " AND month=" + month
                                + ";", null);
                        if (c != null && c.getCount() > 0) {
                            db.execSQL("DELETE FROM " + customdb_table
                                    + " WHERE name=\"" + contactname
                                    + "\" AND day=" + day + " AND month="
                                    + month + ";");
                            Toast.makeText(getBaseContext(),
                                    "Επιτυχημένη διαγραφή", Toast.LENGTH_SHORT)
                                    .show();
                            c.close();
                            Context context = (Context) getBaseContext();
                            AlarmManager alarmManager = (AlarmManager) context
                                    .getSystemService(Context.ALARM_SERVICE);
                            Intent theme = new Intent();
                            theme.setAction(WIDGET_THEME);
                            PendingIntent pendingIntent = PendingIntent
                                    .getBroadcast(context, 123, theme, 0);
                            Calendar now = Calendar.getInstance();
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    now.getTimeInMillis(), pendingIntent);
                        } else {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Η επαφή δεν υπάρχει στη βάση τη συγκεκριμένη ημερομηνία",
                                    Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                    } else {
                        Toast.makeText(getBaseContext(), "Επιλέξτε επαφή",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else if (preference == bday) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.bdayadd);
            dialog.setTitle("Τροποποίηση Γενεθλίων");
            dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            Calendar calendar = Calendar.getInstance();
            final WheelView month1 = (WheelView) dialog
                    .findViewById(R.id.month);
            final WheelView day1 = (WheelView) dialog.findViewById(R.id.day);
            OnWheelChangedListener listener = new OnWheelChangedListener() {
                public void onChanged(WheelView wheel, int oldValue,
                                      int newValue) {
                    updateDays(month1, day1);
                }
            };
            int curMonth = calendar.get(Calendar.MONTH);
            String months[] = new String[] { "Ιανουάριος", "Φεβρουάριος",
                    "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος",
                    "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος",
                    "Δεκέμβριος" };
            month1.setViewAdapter(new DateArrayAdapter(getBaseContext(),
                    months, curMonth));
            month1.setCurrentItem(curMonth);
            month1.addChangingListener(listener);

            updateDays(month1, day1);
            day1.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
            dialog.show();
            Button select = (Button) dialog
                    .findViewById(R.id.name_picker_button_bday);
            select.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            Contacts.CONTENT_URI);
                    startActivityForResult(contactPickerIntent,
                            CONTACT_PICKER_RESULT);
                }

            });

            Button save = (Button) dialog.findViewById(R.id.buttonaddbday01);
            save.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int day = day1.getCurrentItem() + 1;
                    int month = month1.getCurrentItem() + 1;

                    String contactname = name_selected;
                    if (contactname.equals("")) {
                        contactname = "";
                    }
                    SQLiteDatabase db = BDBH.getWritableDatabase();
                    if (contactname.length() > 0) {
                        String customdb_table = "bday_table";
                        Cursor c = null;
                        String querystring = "SELECT * FROM " + customdb_table
                                + " WHERE name=\"" + contactname
                                + "\" AND day='" + day + "' AND month='"
                                + month + "' AND id='" + cid + "';";
                        c = db.rawQuery(querystring, null);

                        if (c != null && c.getCount() > 0) {
                            Toast.makeText(getBaseContext(),
                                    "Η επαφή υπάρχει στην βάση",
                                    Toast.LENGTH_SHORT).show();
                            c.close();
                        } else {
                            String querystring1 = "INSERT INTO "
                                    + customdb_table + " (name, day, month,id)"
                                    + " VALUES ('" + contactname + "', " + day
                                    + ", " + month + ", " + cid + ");";
                            db.execSQL(querystring1);
                            Toast.makeText(getBaseContext(),
                                    "Επιτυχημένη προσθήκη", Toast.LENGTH_SHORT)
                                    .show();
                            Context context = (Context) getBaseContext();
                            AlarmManager alarmManager = (AlarmManager) context
                                    .getSystemService(Context.ALARM_SERVICE);
                            Intent theme = new Intent();
                            theme.setAction(WIDGET_THEME);
                            PendingIntent pendingIntent = PendingIntent
                                    .getBroadcast(context, 123, theme, 0);
                            Calendar now = Calendar.getInstance();
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    now.getTimeInMillis(), pendingIntent);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Επιλέξτε επαφή",
                                Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }

            });
            Button back = (Button) dialog.findViewById(R.id.buttonbackbday01);
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }

            });
            Button del = (Button) dialog.findViewById(R.id.buttondelbday01);
            del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String contactname = name_selected;
                    int day = day1.getCurrentItem() + 1;
                    int month = month1.getCurrentItem() + 1;
                    if (contactname.length() > 0) {
                        SQLiteDatabase db = BDBH.getWritableDatabase();
                        String customdb_table = "bday_table";
                        Cursor c = null;
                        c = db.rawQuery("SELECT * FROM " + customdb_table
                                + " WHERE name=\"" + contactname
                                + "\" AND day=" + day + " AND month=" + month
                                + " AND id=" + cid + ";", null);
                        if (c != null && c.getCount() > 0) {
                            db.execSQL("DELETE FROM " + customdb_table
                                    + " WHERE name=\"" + contactname
                                    + "\" AND day=" + day + " AND month="
                                    + month + " AND id=" + cid + ";");
                            Toast.makeText(getBaseContext(),
                                    "Επιτυχημένη διαγραφή", Toast.LENGTH_SHORT)
                                    .show();
                            c.close();
                            Context context = (Context) getBaseContext();
                            AlarmManager alarmManager = (AlarmManager) context
                                    .getSystemService(Context.ALARM_SERVICE);
                            Intent theme = new Intent();
                            theme.setAction(WIDGET_THEME);
                            PendingIntent pendingIntent = PendingIntent
                                    .getBroadcast(context, 123, theme, 0);
                            Calendar now = Calendar.getInstance();
                            alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    now.getTimeInMillis(), pendingIntent);
                        } else {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Η επαφή δεν υπάρχει στη βάση τη συγκεκριμένη ημερομηνία",
                                    Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                    } else {
                        Toast.makeText(getBaseContext(), "Επιλέξτε επαφή",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else if (preference == argies) {
            startActivity(new Intent(this, argiesadd.class));
        } else if (preference == bday_list) {
            startActivity(new Intent(this, bdaylist.class));
        } else if (preference == eortes_list) {
            startActivity(new Intent(this, eorteslist.class));
        } else if (preference == notify) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_notify_dialog1);
            dialog.setTitle("Επιλογή Ώρας Ειδοποίησης");
            SharedPreferences app_preferences = getSharedPreferences(
                    "Shared_Prefs", MODE_PRIVATE);
            int saved_hour = app_preferences.getInt("not_hour", 11);
            int saved_minute = app_preferences.getInt("not_minute", 0);

            final WheelView minute = (WheelView) dialog
                    .findViewById(R.id.mimute);
            final WheelView hour = (WheelView) dialog.findViewById(R.id.hour);
            String minutes[] = new String[] { "00", "01", "02", "03", "04",
                    "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                    "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
                    "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
                    "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
                    "55", "56", "57", "58", "59", "60" };
            minute.setViewAdapter(new DateArrayAdapter(this, minutes,
                    saved_minute));
            minute.setCurrentItem(saved_minute);
            String hours[] = new String[] { "00", "01", "02", "03", "04", "05",
                    "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                    "16", "17", "18", "19", "20", "21", "22", "23" };
            hour.setViewAdapter(new DateArrayAdapter(this, hours, saved_hour));
            hour.setCurrentItem(saved_hour);
            dialog.show();
            Button dialog_button = (Button) dialog
                    .findViewById(R.id.buttondialog1);
            dialog_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int hour1 = hour.getCurrentItem();
                    int minute1 = minute.getCurrentItem();
                    SharedPreferences app_preferences = getSharedPreferences(
                            "Shared_Prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putInt("not_hour", hour1);
                    editor.putInt("not_minute", minute1);
                    editor.commit();
                    dialog.dismiss();
                    Context context = (Context) getBaseContext();
                    AlarmManager alarmManager1 = (AlarmManager) context
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent contactscel = new Intent();
                    contactscel.setAction(CONTACTS_CEL);
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                            context, 123, contactscel, 0);
                    Calendar cele = Calendar.getInstance();
                    cele.set(Calendar.HOUR_OF_DAY, hour1);
                    cele.set(Calendar.MINUTE, minute1);
                    cele.set(Calendar.SECOND, 0);
                    long cele_time = cele.getTimeInMillis();
                    Calendar cal = Calendar.getInstance();
                    long cal_time = cal.getTimeInMillis();
                    long when;
                    if (cele_time > cal_time) {
                        when = cele_time - cal_time;
                    } else {
                        when = cal_time - cele_time;
                    }
                    alarmManager1.set(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + when, pendingIntent2);
                }
            });
        } else if (preference == help) {

            LayoutInflater inflater = LayoutInflater.from(this);

            View view = inflater.inflate(R.layout.help, null);

            Builder builder = new AlertDialog.Builder(this);

            builder.setView(view)
                    .setTitle("Βοήθεια")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

            builder.create().show();
            return (true);

        } else if (preference == about) {

            LayoutInflater inflater = LayoutInflater.from(this);

            View view = inflater.inflate(R.layout.about, null);

            Builder builder = new AlertDialog.Builder(this);

            builder.setView(view)
                    .setTitle("Εορτολόγιο v2.6.1")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

            builder.create().show();
            return (true);

        } else if (preference == changelog) {

            LayoutInflater inflater = LayoutInflater.from(this);

            View view = inflater.inflate(R.layout.changelog, null);

            Builder builder = new AlertDialog.Builder(this);

            builder.setView(view)
                    .setTitle("Changelog")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

            builder.create().show();
            return (true);

        }

        else if (preference == importExportHelp) {

            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.importexporthelp, null);

            Builder builder = new AlertDialog.Builder(this);

            builder.setView(view)
                    .setTitle("Βοήθεια")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

            builder.create().show();
            return (true);


        }

        // BACKUP DATABASE
        else if (preference == exportDB) {

            // Check for directory
            File direct = new File(Environment.getExternalStorageDirectory()
                    + "/EortologioBackup");

            if (!direct.exists()) {
                if (direct.mkdir()) {
                    // directory is created;
                }

            }

            // Actual Export
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + "com.duk3r.eortologio2"
                            + "//databases//" + "userdb";
                    String backupDBPath = "/EortologioBackup/userdb";
                    String currentDBPath2 = "//data//" + "com.duk3r.eortologio2"
                            + "//databases//" + "bdaydb";
                    String backupDBPath2 = "/EortologioBackup/bdaydb";

                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, backupDBPath);
                    File currentDB2 = new File(data, currentDBPath2);
                    File backupDB2 = new File(sd, backupDBPath2);

                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    FileChannel src2 = new FileInputStream(currentDB2)
                            .getChannel();
                    FileChannel dst2 = new FileOutputStream(backupDB2)
                            .getChannel();

                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    dst2.transferFrom(src2, 0, src2.size());
                    src2.close();
                    dst2.close();

                    Toast.makeText(getBaseContext(), "Backup Completed",
                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getBaseContext(), e.toString(),
                        Toast.LENGTH_LONG).show();

            }
        }

        // IMPORT DATABASE
        // --------------- (Need to get some sleep...)
        else if (preference == importDB) {

            try {
                File sd = Environment.getExternalStorageDirectory();
                File data  = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String  currentDBPath= "//data//" + "com.duk3r.eortologio2"
                            + "//databases//" + "userdb";
                    String backupDBPath  = "/EortologioBackup/userdb";
                    String  currentDBPath2= "//data//" + "com.duk3r.eortologio2"
                            + "//databases//" + "bdaydb";
                    String backupDBPath2  = "/EortologioBackup/bdaydb";
                    File  backupDB= new File(data, currentDBPath);
                    File currentDB  = new File(sd, backupDBPath);
                    File  backupDB2= new File(data, currentDBPath2);
                    File currentDB2  = new File(sd, backupDBPath2);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    FileChannel src2 = new FileInputStream(currentDB2).getChannel();
                    FileChannel dst2 = new FileOutputStream(backupDB2).getChannel();

                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    dst2.transferFrom(src2, 0, src2.size());
                    src2.close();
                    dst2.close();

                    Toast.makeText(getBaseContext(), "Import Completed",
                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }

        }

        else if (preference == text_color_widget) {
            final SharedPreferences prefs = getBaseContext()
                    .getSharedPreferences("Shared_Prefs", 0);
            final ColorPickerDialog d = new ColorPickerDialog(this,
                    prefs.getInt("text_color_widget", 0xffffffff));
            // d.setAlphaSliderVisible(true);
            d.setTitle("Επιλογή Χρώματος");
            d.setButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("text_color_widget", d.getColor());
                    editor.commit();
                    Context context = (Context) getBaseContext();
                    AlarmManager alarmManager = (AlarmManager) context
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent theme = new Intent();
                    theme.setAction(WIDGET_THEME);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, 123, theme, 0);
                    Calendar now = Calendar.getInstance();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            now.getTimeInMillis(), pendingIntent);

                }
            });

            d.setButton2("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            d.show();

            return true;
        }
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String name = "";

                    try {
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();
                        cid = Integer.parseInt(id);

                        // query for everything
                        cursor = getContentResolver().query(
                                ContactsContract.Data.CONTENT_URI, null,
                                ContactsContract.Data.CONTACT_ID + "=?",
                                new String[] { id }, null);
                        int nameCol = cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        if (cursor.moveToFirst()) {
                            name = cursor.getString(nameCol);

                        }
                    } catch (Exception e) {

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        // TextView namepicker = (TextView)
                        // findViewById(R.id.name_picker_bday);
                        // namepicker.setText(name);
                        name_selected = name;

                    }
                    break;
                case CONTACT_PICKER_RESULT_EORTES:
                    Cursor cursor1 = null;
                    String name1 = "";

                    try {
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything
                        cursor1 = getContentResolver().query(
                                ContactsContract.Data.CONTENT_URI, null,
                                ContactsContract.Data.CONTACT_ID + "=?",
                                new String[] { id }, null);
                        int nameCol = cursor1
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        if (cursor1.moveToFirst()) {
                            name1 = cursor1.getString(nameCol);
                            String[] names = name1.split(" ");
                            name1 = names[0];
                        }
                    } catch (Exception e) {

                    } finally {
                        if (cursor1 != null) {
                            cursor1.close();
                        }
                        eortes_name_selected = name1;

                    }

                    break;
            }

        } else {
            Toast.makeText(getBaseContext(), "Error getting contact",
                    Toast.LENGTH_SHORT).show();
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
