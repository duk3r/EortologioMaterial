package com.duk3r.eortologio2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;

public class ListData {

    public static List<Map<String, String>> getDatalist(ContentResolver ConRe,
                                                        Context context) {
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        UserDataBaseHelper BDBH = new UserDataBaseHelper(context);
        SQLiteDatabase db = BDBH.getWritableDatabase();
        String querystring = "SELECT * FROM user_table ORDER BY month ASC,day ASC";
        Cursor c = null;
        c = db.rawQuery(querystring, null);
        if (c != null && c.getCount() > 0) {

            c.moveToFirst();
            do {
                String name = c.getString(c.getColumnIndex("name"));
                int day = c.getInt(c.getColumnIndex("day"));
                int month = c.getInt(c.getColumnIndex("month"));
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("date", day + "-" + month);
                items.add(map);
            } while (c.moveToNext());
            c.close();
        }
        c.deactivate();
        db.close();
        return items;

    }

    public static List<Map<String, String>> getDatabdaylist(
            ContentResolver ConRe, Context context) {

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        String[] projection1 = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
        Uri contacts_tele = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        BdayDataBaseHelper BDBH = new BdayDataBaseHelper(context);
        SQLiteDatabase db = BDBH.getWritableDatabase();
        Cursor c = null;
        String daystring, monthstring;
        String querystring = "SELECT * FROM bday_table ORDER BY month ASC,day ASC";
        c = db.rawQuery(querystring, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                String numCol = "", number = "", numbers = "";
                String name = c.getString(c.getColumnIndex("name"));
                String id = c.getString(c.getColumnIndex("id"));
                int day = c.getInt(c.getColumnIndex("day"));
                int month = c.getInt(c.getColumnIndex("month"));
                Cursor managedCursor = null;
                managedCursor = ConRe.query(contacts_tele, projection1,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + "=?", new String[] { id }, null);
                if (managedCursor != null && managedCursor.getCount() > 0) {
                    managedCursor.moveToFirst();
                    do {
                        numCol = managedCursor.getColumnName(0);
                        numbers = numbers
                                + managedCursor.getString(managedCursor
                                .getColumnIndex(numCol)) + "@";
                        number = number
                                + managedCursor.getString(managedCursor
                                .getColumnIndex(numCol)) + " ";
                    } while (managedCursor.moveToNext());
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("number", number);
                map.put("num", numbers);

                if (day < 10) {
                    daystring = "0" + day;
                } else {
                    daystring = "" + day;
                }
                if (month < 10) {
                    monthstring = "0" + month;
                } else {
                    monthstring = "" + month;
                }
                map.put("date", daystring + "-" + monthstring);
                items.add(map);
            } while (c.moveToNext());
            c.close();
        }
        c.deactivate();
        db.close();
        return items;
    }

    public static List<Map<String, String>> getDatabday(ContentResolver ConRe,
                                                        Context context) {

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        String[] projection1 = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
        Uri contacts_tele = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        BdayDataBaseHelper BDBH = new BdayDataBaseHelper(context);
        SQLiteDatabase db = BDBH.getWritableDatabase();
        final Calendar cal = Calendar.getInstance();
        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        Month++;
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        String daystring, monthstring;

        if ((Month == 2) && Day == 29) {
            Day = 1;
            Month++;
        }
        if ((Month == 4 || Month == 6 || Month == 9 || Month == 11)
                && Day == 31) {
            Day = 1;
            Month++;
        }
        if ((Month == 1 || Month == 3 || Month == 5 || Month == 7 || Month == 8 || Month == 10)
                && Day == 32) {
            Day = 1;
            Month++;
        }
        if (Month == 12 && Day == 32) {
            Day = 1;
            Month = 1;
            Year++;
        }
        Cursor c = null;
        String numCol = "", numbers = "";
        String querystring = "SELECT * FROM bday_table WHERE day='" + Day
                + "' AND month='" + Month + "'";
        c = db.rawQuery(querystring, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                String name = c.getString(c.getColumnIndex("name"));
                String id = c.getString(c.getColumnIndex("id"));
                Cursor managedCursor = null;
                managedCursor = ConRe.query(contacts_tele, projection1,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + "=?", new String[] { id }, null);
                if (managedCursor != null && managedCursor.getCount() > 0) {
                    managedCursor.moveToFirst();
                    do {
                        numCol = managedCursor.getColumnName(0);
                        numbers = numbers
                                + managedCursor.getString(managedCursor
                                .getColumnIndex(numCol)) + "@";
                        // number= number +
                        // managedCursor.getString(managedCursor.getColumnIndex(numCol))+" ";
                    } while (managedCursor.moveToNext());
                }
                if (Day < 10) {
                    daystring = "0" + Day;
                } else {
                    daystring = "" + Day;
                }
                if (Month < 10) {
                    monthstring = "0" + Month;
                } else {
                    monthstring = "" + Month;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                // map.put("number", number);
                map.put("num", numbers);
                map.put("date", daystring + "-" + monthstring);
                items.add(map);
            } while (c.moveToNext());
            c.close();
        }
        c.deactivate();
        db.close();

        c = context
                .getContentResolver()
                .query(Data.CONTENT_URI,
                        new String[] {
                                ContactsContract.RawContacts.CONTACT_ID,
                                ContactsContract.CommonDataKinds.Event.START_DATE,
                                ContactsContract.Contacts.DISPLAY_NAME },
                        Data.MIMETYPE + "=? AND "
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
            final int idColumn = c
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

            do {
                String name = c.getString(nameColumn);
                String date = c.getString(dateColumn);
                String id = c.getString(idColumn);
                String m, d;
                Log.d("Gandevs", "ListData bday " + date);
                int dash_counter = 0;
                for (int i1 = 0; i1 < date.length(); i1++) {
                    char c1 = date.charAt(i1);
                    if (c1 == '-') {
                        dash_counter++;
                    }
                }
                if (dash_counter > 1) {

                    if (date.length() > 7) {
                        String md[] = date.split("-");
                        m = md[1];
                        d = md[2].substring(0, 2);
                    } else {
                        String md[] = date.split("-");
                        m = md[2];
                        d = md[3].substring(0, 2);
                    }
                    Cursor managedCursor = null;
                    String numCol1 = "", numbers1 = "";
                    managedCursor = ConRe.query(contacts_tele, projection1,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=?", new String[] { id }, null);
                    if (managedCursor != null && managedCursor.getCount() > 0) {
                        managedCursor.moveToFirst();
                        do {
                            numCol1 = managedCursor.getColumnName(0);
                            numbers1 = numbers1
                                    + managedCursor.getString(managedCursor
                                    .getColumnIndex(numCol1)) + "@";

                        } while (managedCursor.moveToNext());
                    }

                    if (Integer.parseInt(d) == Day
                            && Integer.parseInt(m) == Month) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", name);
                        map.put("num", numbers1);
                        map.put("date", d + "-" + m);
                        items.add(map);
                    }
                }
            } while (c.moveToNext());
            c.close();
        }
        c.deactivate();
        return items;
    }

    public static List<Map<String, String>> getFBDataBdayList(
            ContentResolver ConRe, Context context) {
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        Cursor c = null;
        String querystring = "SELECT * FROM fb_table WHERE day <>'null' ORDER BY month ASC,day ASC";

        return items;
    }

    public static List<Map<String, String>> getFBDataBday(
            ContentResolver ConRe, Context context) {

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();


        final Calendar cal = Calendar.getInstance();
        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        Month++;
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        String daystring, monthstring;

        if ((Month == 2) && Day == 29) {
            Day = 1;
            Month++;
        }
        if ((Month == 4 || Month == 6 || Month == 9 || Month == 11)
                && Day == 31) {
            Day = 1;
            Month++;
        }
        if ((Month == 1 || Month == 3 || Month == 5 || Month == 7 || Month == 8 || Month == 10)
                && Day == 32) {
            Day = 1;
            Month++;
        }
        if (Month == 12 && Day == 32) {
            Day = 1;
            Month = 1;
            Year++;
        }
        Cursor c = null;

        String querystring = "SELECT * FROM fb_table WHERE day='" + Day
                + "' AND month='" + Month + "'";

        return items;

    }

    public static List<Map<String, String>> getFBData(ArrayList<String> myArr,
                                                      Context ctx) {
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        int x, z = myArr.size();
        for (x = 0; x < z * 3; x++) {

            String[] names = new String[1];
            if (x < z) {
                String string1 = myArr.get(x);
                string1 = string1.substring(0, 1).toUpperCase()
                        + string1.substring(1).toLowerCase();
                names[0] = string1;
            } else if (x < 2 * z) {
                names[0] = myArr.get(x - z).toLowerCase();

            } else if (x < 3 * z) {
                names[0] = myArr.get(x - (2 * z)).toUpperCase();

            }



        }
        return items;
    }

    public static List<Map<String, String>> getData(ArrayList<String> myArr,
                                                    ContentResolver ConRe) {

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME };
        String[] projection1 = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
        Uri contacts = ContactsContract.Data.CONTENT_URI;
        Uri contacts_tele = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        int x, z = myArr.size();
        for (x = 0; x < z * 3; x++) {

            String[] names = new String[1];
            if (x < z) {
                String string1 = myArr.get(x);
                string1 = string1.substring(0, 1).toUpperCase()
                        + string1.substring(1).toLowerCase();
                names[0] = string1;
            } else if (x < 2 * z) {
                names[0] = myArr.get(x - z).toLowerCase();

            } else if (x < 3 * z) {
                names[0] = myArr.get(x - (2 * z)).toUpperCase();

            }

            Cursor managedCursor;
            // startManagingCursor(managedCursor);
            managedCursor = ConRe.query(contacts, projection,
                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                            + "=?", names, null);

            if (managedCursor != null && managedCursor.getCount() > 0) {
                managedCursor.moveToFirst();
                do {
                    String idCol, numCol = "", numbers = "", id, fullnameCol, fullname = "";
                    idCol = managedCursor.getColumnName(1);
                    id = managedCursor.getString(managedCursor
                            .getColumnIndex(idCol));
                    fullnameCol = managedCursor.getColumnName(2); // 2
                    fullname = managedCursor.getString(managedCursor
                            .getColumnIndex(fullnameCol));
                    Cursor managedCursor1 = null;
                    try {
                        managedCursor1 = ConRe
                                .query(contacts_tele,
                                        projection1,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + "=?", new String[] { id },
                                        null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (managedCursor1 != null && managedCursor1.getCount() > 0) {
                        managedCursor1.moveToFirst();

                        do {
                            numCol = managedCursor1.getColumnName(0);
                            numbers = numbers
                                    + managedCursor1.getString(managedCursor1
                                    .getColumnIndex(numCol)) + "@";
                            // number= number +
                            // managedCursor1.getString(managedCursor1.getColumnIndex(numCol))+" ";

                        } while (managedCursor1.moveToNext());
                        managedCursor1.close();
                    }
                    managedCursor1.deactivate();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", fullname);
                    map.put("num", numbers);
                    items.add(map);

                } while (managedCursor.moveToNext());
                managedCursor.close();
            }
            managedCursor.deactivate();
        }
        return items;
    }

}
