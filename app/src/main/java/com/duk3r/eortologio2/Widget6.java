package com.duk3r.eortologio2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget6 extends AppWidgetProvider {
    public static String CHANGE_DAY = "com.duk3r.eortologio2.CHANGE_DAY";
    public static String CHANGE_DAY1 = "com.duk3r.eortologio2.CHANGE_DAY1";
    public static String CHANGE_DAY2 = "com.duk3r.eortologio2.CHANGE_DAY2";
    public static String START_APP = "com.duk3r.eortologio2.START_APP";
    public static String DAY_CHANGED = "com.duk3r.eortologio2.DAY_CHANGED";
    public static String CONTACTS_CEL = "com.duk3r.eortologio2.CONTACTS_CEL";
    public static String WIDGET_THEME = "com.duk3r.eortologio2.WIDGET_THEME";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Intent daychanged = new Intent();
        daychanged.setAction(DAY_CHANGED);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 321,
                daychanged, 0);
        AlarmManager alarms = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(pendingIntent1);
        Intent contactscel = new Intent();
        contactscel.setAction(CONTACTS_CEL);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0,
                contactscel, 0);
        AlarmManager alarm = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent2);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ComponentName thisWidget = new ComponentName(context, Widget6.class);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        SharedPreferences app_preferences = context.getSharedPreferences(
                "Shared_Prefs", 0);
        if (intent.getAction().equals(START_APP)) {
            final Intent mainappIntent = new Intent(Intent.ACTION_MAIN, null);
            mainappIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.duk3r.eortologio2",
                    "com.duk3r.eortologio2.MainActivity");
            mainappIntent.setComponent(cn);
            mainappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainappIntent);
        }
        if (intent.getAction().equals(WIDGET_THEME)) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        if (intent.getAction().equals(DAY_CHANGED)) {
            AlarmManager alarmManager1 = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent contactscel = new Intent();
            contactscel.setAction(CONTACTS_CEL);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
                    0, contactscel, 0);
            Calendar cele = Calendar.getInstance();
            int hour = app_preferences.getInt("not_hour", 11);
            int minutes = app_preferences.getInt("not_minute", 0);
            cele.set(Calendar.HOUR_OF_DAY, hour);
            cele.set(Calendar.MINUTE, minutes);
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
            Log.d("Gandevs", "day_change onreceived");
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        if (intent.getAction().equals(CHANGE_DAY)) {
            app_preferences.edit().putInt("widget_day", 0).commit();
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        if (intent.getAction().equals(CHANGE_DAY1)) {
            app_preferences.edit().putInt("widget_day", 1).commit();
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        if (intent.getAction().equals(CHANGE_DAY2)) {
            app_preferences.edit().putInt("widget_day", 2).commit();
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        if (intent.getAction().equals(CONTACTS_CEL)) {
            if (appWidgetManager.getAppWidgetIds(thisWidget).length > 0) {
                if (app_preferences.getBoolean("notify1", true) == true) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    String mDay = Integer.toString(cal
                            .get(Calendar.DAY_OF_MONTH));
                    String mMonth = Integer
                            .toString(cal.get(Calendar.MONTH) + 1);
                    String mYear = Integer.toString(cal.get(Calendar.YEAR));
                    ArrayList<String> myArr = new ArrayList<String>();
                    ArrayList<String> myArr1 = new ArrayList<String>();
                    AppDataBaseHelper ADBH = new AppDataBaseHelper(context);
                    SQLiteDatabase db = ADBH.getWritableDatabase();
                    UserDataBaseHelper UDBH = new UserDataBaseHelper(context);
                    SQLiteDatabase db1 = UDBH.getWritableDatabase();
                    BdayDataBaseHelper BDDBH = new BdayDataBaseHelper(context);
                    SQLiteDatabase db3 = BDDBH.getWritableDatabase();
                    String eortes = null;
                    String bday = null;
                    String querystring = "SELECT * FROM gr_names WHERE day ='"
                            + mDay + "' AND month='" + mMonth + "'";
                    String querystring1 = "SELECT * FROM gr_names_lo WHERE day ='"
                            + mDay + "' AND month='" + mMonth + "'";
                    String querystring2 = "SELECT * FROM gr_names_kin WHERE day='"
                            + mDay
                            + "' AND month='"
                            + mMonth
                            + "' AND year='"
                            + mYear + "'";
                    String querystring3 = "SELECT * FROM gr_names_en WHERE day ='"
                            + mDay + "' AND month='" + mMonth + "'";
                    String querystring4 = "SELECT * FROM  user_table WHERE day='"
                            + mDay + "' AND month='" + mMonth + "'";
                    String querystring5 = "SELECT * FROM gr_names_kin_lo WHERE day='"
                            + mDay
                            + "' AND month='"
                            + mMonth
                            + "' AND year='"
                            + mYear + "'";
                    String querystring6 = "SELECT * FROM  fb_table WHERE day='"
                            + mDay + "' AND month='" + mMonth + "'";
                    String querystring7 = "SELECT * FROM gr_names_kin_en WHERE day='"
                            + mDay
                            + "' AND month='"
                            + mMonth
                            + "' AND year='"
                            + mYear + "'";
                    String querystring8 = "SELECT * FROM  bday_table WHERE day='"
                            + mDay + "' AND month='" + mMonth + "'";

                    Cursor c = null;
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        eortes = word1;
                        myArr.add(word1);
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            eortes = eortes + ", " + word1;
                            myArr.add(word1);
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring1, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring2, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        if (eortes == null) {
                            eortes = word1;
                        } else {
                            eortes = eortes + ", " + word1;
                        }
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                            eortes = eortes + ", " + word1;
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring3, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring5, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    while (db == null || !db.isOpen()) {
                        db = ADBH.getWritableDatabase();
                    }
                    c = db.rawQuery(querystring7, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("f_name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    db.close();
                    while (db1 == null || !db1.isOpen()) {
                        db1 = UDBH.getWritableDatabase();
                    }
                    c = db1.rawQuery(querystring4, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("name");
                        String word1 = c.getString(Column1);
                        myArr.add(word1);
                        if (eortes == null) {
                            eortes = word1;
                        } else {
                            eortes = eortes + ", " + word1;
                        }
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr.add(word1);
                            eortes = eortes + ", " + word1;
                        }
                        ;
                        c.close();
                    }
                    c.deactivate();
                    db1.close();

                    while (db3 == null || !db3.isOpen()) {
                        db3 = BDDBH.getWritableDatabase();
                    }
                    c = db3.rawQuery(querystring8, null);
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        int Column1 = c.getColumnIndex("name");
                        String word1 = c.getString(Column1);
                        myArr1.add(word1);
                        if (bday == null) {
                            bday = word1;
                        } else {
                            bday = bday + ", " + word1;
                        }
                        while (c.moveToNext()) {
                            word1 = c.getString(Column1);
                            myArr1.add(word1);
                            bday = bday + ", " + word1;
                        }
                    }
                    c.deactivate();
                    db3.close();

                    c = context
                            .getContentResolver()
                            .query(Data.CONTENT_URI,
                                    new String[] {
                                            ContactsContract.RawContacts.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Event.START_DATE,
                                            ContactsContract.Contacts.DISPLAY_NAME },
                                    Data.MIMETYPE
                                            + "=? AND "
                                            + ContactsContract.CommonDataKinds.Event.TYPE
                                            + "=?",
                                    new String[] {
                                            Event.CONTENT_ITEM_TYPE,
                                            Integer.toString(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) },
                                    null);
                    if (c.moveToFirst()) {
                        final int dateColumn = c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                        final int nameColumn = c
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        do {
                            String name = c.getString(nameColumn);
                            String dateString = c.getString(dateColumn);
                            String m, d;
                            int dash_counter = 0;
                            for (int i1 = 0; i1 < dateString.length(); i1++) {
                                char c1 = dateString.charAt(i1);
                                if (c1 == '-') {
                                    dash_counter++;
                                }
                            }
                            if (dash_counter > 1) {
                                if (dateString.length() > 7) {
                                    String md[] = dateString.split("-");
                                    m = md[1];
                                    d = md[2].substring(0, 2);
                                } else {
                                    String md[] = dateString.split("-");
                                    m = md[2];
                                    d = md[3].substring(0, 2);
                                }
                                if (Integer.parseInt(d) == Integer
                                        .parseInt(mDay)
                                        && Integer.parseInt(m) == Integer
                                        .parseInt(mMonth)) {

                                    if (bday == null) {
                                        bday = name;
                                        Log.d("Gandevs", bday);
                                        myArr1.add(name);

                                    } else {
                                        bday = bday + ", " + name;
                                        Log.d("Gandevs", bday);
                                        myArr1.add(name);
                                    }
                                }
                            }
                        } while (c.moveToNext());
                        c.close();
                    }
                    c.deactivate();

                    Cursor ccur = null;
                    ContentResolver ConRe = context.getContentResolver();

                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    ccur = ConRe.query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
                    int nameCol;
                    String name;
                    String[] temp;
                    String celcon = "";
                    if (ccur != null && ccur.getCount() > 0) {
                        ccur.moveToFirst();
                        if ((myArr.size() > 0) && (ccur.getCount() > 0)) {
                            do {
                                nameCol = ccur
                                        .getColumnIndex(PhoneLookup.DISPLAY_NAME);
                                name = ccur.getString(nameCol);

                                temp = null;
                                if (name != null) {
                                    temp = name.split("\\s+");
                                    temp.toString().toLowerCase();
                                    int size = myArr.size();
                                    for (int i1 = 0; i1 < size; i1++) {
                                        if (temp[0].equalsIgnoreCase(myArr
                                                .get(i1))) {
                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("name", name);
                                            celcon = celcon + name + ", ";
                                            mylist.add(map);
                                        }
                                    }
                                }
                            } while (ccur.moveToNext());
                            ccur.close();
                        }
                    }
                    int pos = celcon.length();
                    if (pos > 1) {
                        celcon = celcon.substring(0, pos - 2) + ".";
                    }
                    int bdaysize = myArr1.size();
                    String bday_notify = "";
                    for (int i = 0; i < bdaysize; i++) {
                        bday_notify = bday_notify + myArr1.get(i) + ", ";
                    }
                    int pos2 = bday_notify.length();
                    if (pos2 > 1) {
                        bday_notify = bday_notify.substring(0, pos2 - 2) + ".";
                    }
                    if (mylist.size() > 0) {
                        String ns = Context.NOTIFICATION_SERVICE;
                        NotificationManager mNotificationManager = (NotificationManager) context
                                .getSystemService(ns);
                        CharSequence tickerText = "Εορτολόγιο";
                        long when = System.currentTimeMillis();
                        Notification notification = new Notification(
                                R.drawable.ic_launcher, tickerText, when);
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        CharSequence contentTitle = "Εορτολόγιο";
                        CharSequence contentText = "Εορτές : " + celcon;
                        Intent notificationIntent = new Intent(context,
                                MainActivity.class);
                        PendingIntent contentIntent = PendingIntent
                                .getActivity(context, 0, notificationIntent, 0);
                        notification.setLatestEventInfo(context, contentTitle,
                                contentText, contentIntent);
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        int HELLO_ID = 1;
                        mNotificationManager.notify(HELLO_ID, notification);
                    }
                    if (bdaysize > 0) {
                        String ns = Context.NOTIFICATION_SERVICE;
                        NotificationManager mNotificationManager1 = (NotificationManager) context
                                .getSystemService(ns);
                        long when1 = System.currentTimeMillis();
                        CharSequence tickerText = "Εορτολόγιο";
                        Notification notification1 = new Notification(
                                R.drawable.notification_small, tickerText, when1);
                        notification1.flags |= Notification.FLAG_AUTO_CANCEL;
                        CharSequence contentTitle = "Εορτολόγιο";
                        CharSequence contentText1 = "Γενέθλια : " + bday_notify;
                        Intent notificationIntent1 = new Intent(context,
                                MainActivity.class);
                        PendingIntent contentIntent1 = PendingIntent
                                .getActivity(context, 0, notificationIntent1, 0);
                        notification1.setLatestEventInfo(context, contentTitle,
                                contentText1, contentIntent1);
                        notification1.defaults |= Notification.DEFAULT_VIBRATE;
                        notification1.defaults |= Notification.DEFAULT_SOUND;
                        int HELLO_ID1 = 2;
                        mNotificationManager1.notify(HELLO_ID1, notification1);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = null;
            SharedPreferences app_preferences = context.getSharedPreferences(
                    "Shared_Prefs", 0);
            int widget_day = app_preferences.getInt("widget_day", 0);
            int day_drawable = 0;
            int day_drawable_normal = 0, day_drawable_normal1 = 0;
            int widget_src = Integer.parseInt(app_preferences.getString(
                    "widget_src", "1"));
            int display = app_preferences.getInt("width", 240);
            int text_color_widget = app_preferences.getInt("text_color_widget",
                    0xffffffff);
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH) + widget_day);
            String mDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            String mMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
            String mYear = Integer.toString(cal.get(Calendar.YEAR));
            int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
            String day = null;
            switch (dayofweek) {
                case 1:
                    day = "Κυριακή";
                    break;
                case 2:
                    day = "Δευτέρα";
                    break;
                case 3:
                    day = "Τρίτη";
                    break;
                case 4:
                    day = "Τετάρτη";
                    break;
                case 5:
                    day = "Πέμπτη";
                    break;
                case 6:
                    day = "Παρασκευή";
                    break;
                case 7:
                    day = "Σάββατο";
                    break;
            }

            AppDataBaseHelper ADBH = new AppDataBaseHelper(context);
            SQLiteDatabase db = ADBH.getWritableDatabase();
            UserDataBaseHelper UDBH = new UserDataBaseHelper(context);
            SQLiteDatabase db1 = UDBH.getWritableDatabase();
            BdayDataBaseHelper BDDBH = new BdayDataBaseHelper(context);
            SQLiteDatabase db3 = BDDBH.getWritableDatabase();
            String eortes = null;
            String bday = null;

			/*
			// find out the width of the screen
			DisplayMetrics dm = new DisplayMetrics();
			if (dm.density == 240) {

				views = new RemoteViews(context.getPackageName(),
						R.layout.widget_trans);
				break;
			} else if (dm.density == 320) {
				views = new RemoteViews(context.getPackageName(),
						R.layout.widget_trans_medium);
				break;
			} else {
				views = new RemoteViews(context.getPackageName(),
						R.layout.widget_trans);
			}
			*/
            views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_semi);

            ArrayList<String> myArr = new ArrayList<String>();
            ArrayList<String> myArr1 = new ArrayList<String>();
            String querystring = "SELECT * FROM gr_names WHERE day ='" + mDay
                    + "' AND month='" + mMonth + "'";
            String querystring1 = "SELECT * FROM gr_names_lo WHERE day ='"
                    + mDay + "' AND month='" + mMonth + "'";
            String querystring2 = "SELECT * FROM gr_names_kin WHERE day='"
                    + mDay + "' AND month='" + mMonth + "' AND year='" + mYear
                    + "'";
            String querystring3 = "SELECT * FROM gr_names_en WHERE day ='"
                    + mDay + "' AND month='" + mMonth + "'";
            String querystring4 = "SELECT * FROM  user_table WHERE day='"
                    + mDay + "' AND month='" + mMonth + "'";
            String querystring5 = "SELECT * FROM gr_names_kin_lo WHERE day='"
                    + mDay + "' AND month='" + mMonth + "' AND year='" + mYear
                    + "'";
            String querystring6 = "SELECT * FROM  fb_table WHERE day='" + mDay
                    + "' AND month='" + mMonth + "'";
            String querystring7 = "SELECT * FROM gr_names_kin_en WHERE day='"
                    + mDay + "' AND month='" + mMonth + "' AND year='" + mYear
                    + "'";
            String querystring8 = "SELECT * FROM  bday_table WHERE day='"
                    + mDay + "' AND month='" + mMonth + "'";

            Cursor c = null;
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                eortes = word1;
                myArr.add(word1);
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    eortes = eortes + ", " + word1;
                    myArr.add(word1);
                }
                ;
                c.close();
            }
            c.deactivate();
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring1, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                }
                ;
                c.close();
            }
            c.deactivate();
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring2, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                if (eortes == null) {
                    eortes = word1;
                } else {
                    eortes = eortes + ", " + word1;
                }
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                    eortes = eortes + ", " + word1;
                }
                ;
                c.close();
            }
            c.deactivate();
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring3, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                }
                ;
                c.close();
            }
            c.deactivate();
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring5, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                }
                ;
                c.close();
            }
            c.deactivate();
            while (db == null || !db.isOpen()) {
                db = ADBH.getWritableDatabase();
            }
            c = db.rawQuery(querystring7, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                }
                ;
                c.close();
            }
            c.deactivate();
            db.close();
            while (db1 == null || !db1.isOpen()) {
                db1 = UDBH.getWritableDatabase();
            }
            c = db1.rawQuery(querystring4, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("name");
                String word1 = c.getString(Column1);
                myArr.add(word1);
                if (eortes == null) {
                    eortes = word1;
                } else {
                    eortes = eortes + ", " + word1;
                }
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr.add(word1);
                    eortes = eortes + ", " + word1;
                }
                ;
                c.close();
            }
            c.deactivate();
            db1.close();
            while (db3 == null || !db3.isOpen()) {
                db3 = BDDBH.getWritableDatabase();
            }
            c = db3.rawQuery(querystring8, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("name");
                String word1 = c.getString(Column1);
                myArr1.add(word1);
                if (bday == null) {
                    bday = word1;
                } else {
                    bday = bday + ", " + word1;
                }
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    myArr1.add(word1);
                    bday = bday + ", " + word1;
                }
            }
            c.deactivate();
            db3.close();
            c = context
                    .getContentResolver()
                    .query(Data.CONTENT_URI,
                            new String[] {
                                    ContactsContract.RawContacts.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Event.START_DATE,
                                    ContactsContract.Contacts.DISPLAY_NAME },
                            Data.MIMETYPE
                                    + "=? AND "
                                    + ContactsContract.CommonDataKinds.Event.TYPE
                                    + "=?",
                            new String[] {
                                    Event.CONTENT_ITEM_TYPE,
                                    Integer.toString(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) },
                            null);
            if (c.moveToFirst()) {
                final int dateColumn = c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                final int nameColumn = c
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                // final int idColumn =
                // c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

                do {

                    String name = c.getString(nameColumn);
                    String dateString = c.getString(dateColumn);
                    Log.d("Gandevs", dateString);
                    String m, d;
                    int dash_counter = 0;
                    for (int i1 = 0; i1 < dateString.length(); i1++) {
                        char c1 = dateString.charAt(i1);
                        if (c1 == '-') {
                            dash_counter++;

                        }
                    }
                    if (dash_counter > 1) {
                        if (dateString.length() > 7) {
                            String md[] = dateString.split("-");
                            m = md[1];
                            d = md[2].substring(0, 2);
                        } else {
                            String md[] = dateString.split("-");
                            m = md[2];
                            d = md[3].substring(0, 2);
                        }
                        if (Integer.parseInt(d) == Integer.parseInt(mDay)
                                && Integer.parseInt(m) == Integer
                                .parseInt(mMonth)) {

                            if (bday == null) {
                                bday = name;
                                Log.d("Gandevs", bday);
                                myArr1.add(name);

                            } else {
                                bday = bday + ", " + name;
                                Log.d("Gandevs", bday);
                                myArr1.add(name);
                            }
                        }
                    }
                } while (c.moveToNext());
                c.close();
            }
            c.deactivate();

            Cursor ccur = null;
            ContentResolver ConRe = context.getContentResolver();

            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            ccur = ConRe.query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);
            int nameCol;
            String name;
            String[] temp;
            String celcon = "";
            if (ccur != null && ccur.getCount() > 0) {
                ccur.moveToFirst();
                if ((myArr.size() > 0) && (ccur.getCount() > 0)) {
                    do {
                        nameCol = ccur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
                        name = ccur.getString(nameCol);

                        temp = null;
                        if (name != null) {
                            temp = name.split("\\s+");
                            temp.toString().toLowerCase();
                            int size = myArr.size();
                            for (int i1 = 0; i1 < size; i1++) {
                                if (temp[0].equalsIgnoreCase(myArr.get(i1))) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", name);
                                    celcon = celcon + name + ", ";
                                    mylist.add(map);
                                }
                            }
                        }
                    } while (ccur.moveToNext());
                    ccur.close();
                }
            }

            if (mylist.size() > 0 || myArr1.size() > 0) {
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("widget_cel", "1").commit();
            } else {
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("widget_cel", "0").commit();
            }
            if (eortes == null || eortes == "") {
                eortes = "Καμία ονομαστική εορτή";
                views.setTextViewText(R.id.widgettext2, eortes);
            }
            if (bday == null || bday == "") {
                bday = "Καμία επαφή με γενέθλια";
                views.setTextViewText(R.id.widgettext3, bday);
            }
            int widget_cel = Integer.parseInt(app_preferences.getString(
                    "widget_cel", "0"));
            switch (widget_day) {
                case 0:
                    day_drawable = R.id.ImageView01;
                    day_drawable_normal = R.id.ImageView02;
                    day_drawable_normal1 = R.id.imageView03;
                    break;
                case 1:
                    day_drawable = R.id.ImageView02;
                    day_drawable_normal = R.id.ImageView01;
                    day_drawable_normal1 = R.id.imageView03;
                    break;
                case 2:
                    day_drawable = R.id.imageView03;
                    day_drawable_normal = R.id.ImageView01;
                    day_drawable_normal1 = R.id.ImageView02;
                    break;
            }
            if (widget_cel == 1) {
                views.setImageViewResource(day_drawable,
                        R.drawable.yes);
                views.setImageViewResource(day_drawable_normal,
                        android.R.drawable.presence_invisible);
                views.setImageViewResource(day_drawable_normal1,
                        android.R.drawable.presence_invisible);
            } else {
                views.setImageViewResource(day_drawable,
                        R.drawable.no);
                views.setImageViewResource(day_drawable_normal,
                        android.R.drawable.presence_invisible);
                views.setImageViewResource(day_drawable_normal1,
                        android.R.drawable.presence_invisible);
            }
            views.setTextViewText(R.id.widgettext1, day + " " + mDay + "-"
                    + mMonth + "-" + mYear);
            views.setTextViewText(R.id.widgettext2, "Ε: " + eortes);
            views.setTextViewText(R.id.widgettext3, "Γ: " + bday);
            views.setTextColor(R.id.widgettext1, Color.WHITE);
            views.setTextColor(R.id.widgettext2, Color.WHITE);
            views.setTextColor(R.id.widgettext3, Color.WHITE);
            Intent start_app = new Intent(context, Widget6.class);
            start_app.setAction(START_APP);
            PendingIntent pendingstart_app = PendingIntent.getBroadcast(
                    context, 0, start_app, 0);
            views.setOnClickPendingIntent(R.id.widgettext1, pendingstart_app);
            views.setOnClickPendingIntent(R.id.widgettext2, pendingstart_app);
            views.setOnClickPendingIntent(R.id.widgettext3, pendingstart_app);

            Intent change_day = new Intent(context, Widget6.class);
            change_day.setAction(CHANGE_DAY);
            PendingIntent pendingchangeday = PendingIntent.getBroadcast(
                    context, 0, change_day, 0);
            views.setOnClickPendingIntent(R.id.ImageView01, pendingchangeday);
            Intent change_day1 = new Intent(context, Widget6.class);
            change_day1.setAction(CHANGE_DAY1);
            PendingIntent pendingchangeday1 = PendingIntent.getBroadcast(
                    context, 0, change_day1, 0);
            views.setOnClickPendingIntent(R.id.ImageView02, pendingchangeday1);
            Intent change_day2 = new Intent(context, Widget6.class);
            change_day2.setAction(CHANGE_DAY2);
            PendingIntent pendingchangeday2 = PendingIntent.getBroadcast(
                    context, 0, change_day2, 0);
            views.setOnClickPendingIntent(R.id.imageView03, pendingchangeday2);
            Intent daychanged = new Intent();
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            daychanged.setAction(DAY_CHANGED);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
                    321, daychanged, 0);
            Calendar dayc = Calendar.getInstance();
            dayc.set(Calendar.HOUR_OF_DAY, 24);
            dayc.set(Calendar.MINUTE, 0);
            dayc.set(Calendar.SECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, dayc.getTimeInMillis(),
                    pendingIntent1);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
