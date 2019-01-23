package com.duk3r.eortologio2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pollfish.main.PollFish;
import com.pollfish.main.PollFish.ParamsBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.WheelView;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    // old stuff
    ArrayList<String> myArr = new ArrayList<String>();
    ArrayList<String> myArr1 = new ArrayList<String>();

    int mDay, mMonth, mYear;
    private ListView listView;
    private LazyAdapter adapter;
    public ContentResolver ConRe;
    public static final String APP_ID = "205010826195183";
    String font;
    int text_size, title_text_size = 20;
    int back_color, text_color, border_color;
    private Boolean firstRun = false;
    final Context context = this;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    final UserDataBaseHelper UDBH = new UserDataBaseHelper(this);
    public static String WIDGET_THEME = "com.duk3r.eortologio2.WIDGET_THEME";
    public String eortes_name_selected = "";
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int CONTACT_PICKER_RESULT_EORTES = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParamsBuilder paramsBuilder = new ParamsBuilder("8395f944-f1e6-4e74-aed1-51420abc1da1").releaseMode(true).build();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        TextView drawer_text = (TextView) findViewById(R.id.cover_version);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        drawer_text.setText(R.string.app_version);
        drawer_text.setTypeface(tf);
        drawer_text.setTextColor(Color.WHITE);
        drawer_text.setTextSize(18);


        findSunriseSunset();


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item01:
                Intent in = new Intent(getApplicationContext(), Preferences.class);
                startActivity(in);
                return (true);
            case R.id.menu_search:
                startActivity(new Intent(this, search.class));
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (!firstRun) {
            firstRun = true;
            return;
        }
        switch (position) {
            case 0:
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
                finish();
                break;
            case 1:
                Intent in2 = new Intent(getApplicationContext(), Prosexeis.class);
                startActivity(in2);
                finish();
                break;
            case 2:
                Intent in3 = new Intent(getApplicationContext(), ActionBday.class);
                startActivity(in3);
                finish();
                break;
            /*
            case 3:
                Intent in4 = new Intent(getApplicationContext(),ActionFB.class);
                startActivity(in4);
                finish();
                break;
            */
            case 3:
                Intent in5 = new Intent(getApplicationContext(), ActionArgies.class);
                startActivity(in5);
                finish();
                break;
            case 4:
                Intent in6 = new Intent(getApplicationContext(), ActionPagosmies.class);
                startActivity(in6);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    // Old functions
    public void get_prefs() {
        SharedPreferences app_preferences = getBaseContext()
                .getSharedPreferences("Shared_Prefs", 0);
        font = app_preferences.getString("font", "default");
        text_size = Integer.parseInt(app_preferences.getString("text_size", "16"));
        text_color = app_preferences.getInt("col", Color.WHITE);
        back_color = app_preferences.getInt("col1", 0x37474F);
    }

    public void set_theme() {
        LinearLayout back = (LinearLayout) findViewById(R.id.action1_root);
        back.setBackgroundColor(back_color);
        TextView tv2 = (TextView) findViewById(R.id.action1textview2);
        TextView tv3 = (TextView) findViewById(R.id.action1textview3);
        TextView tv5 = (TextView) findViewById(R.id.action1textView5);

        tv2.setTextSize(text_size);
        tv3.setTextSize(24);
        tv5.setTextSize(text_size);
        tv2.setTextColor(text_color);
        tv3.setTextColor(getResources().getColor(R.color.greenColor));
        tv5.setTextColor(text_color);
        tv2.setBackgroundResource(border_color);
        tv5.setBackgroundResource(border_color);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv2.setTypeface(tf);
        tv3.setTypeface(tf, Typeface.BOLD);
    }

    @Override
    public void onResume() {
        super.onResume();
        get_prefs();
        set_theme();

        // Pollfish surveys
        //PollFish.init(this, "8395f944-f1e6-4e74-aed1-51420abc1da1", Position.BOTTOM_RIGHT, 5);
        PollFish.initWith(this, new ParamsBuilder("8395f944-f1e6-4e74-aed1-51420abc1da1").build());

        get_date();
        ConRe = getContentResolver();
        if (myArr != null && myArr.size() > 0) {
            myArr.clear();
        }
        AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
        SQLiteDatabase db = ADBH.getWritableDatabase();

        String querystring = "SELECT * FROM gr_names WHERE day =" + mDay
                + " AND month=" + mMonth;
        String querystring1 = "SELECT * FROM gr_names_en WHERE day =" + mDay
                + " AND month=" + mMonth;
        String querystring2 = "SELECT * FROM gr_names_kin WHERE day=" + mDay
                + " AND month=" + mMonth + " AND year=" + mYear;
        String querystring3 = "SELECT * FROM gr_names_lo WHERE day =" + mDay
                + " AND month=" + mMonth;
        String querystring5 = "SELECT * FROM gr_names_kin_en WHERE day=" + mDay
                + " AND month=" + mMonth + " AND year=" + mYear;
        String querystring6 = "SELECT * FROM gr_names_kin_lo WHERE day ="
                + mDay + " AND month=" + mMonth;
        String Data = null;
        Cursor c = null;
        c = db.rawQuery(querystring, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column1 = c.getColumnIndex("f_name");
            String word1 = c.getString(Column1);
            Data = word1;
            myArr.add(word1);
            while (c.moveToNext()) {
                word1 = c.getString(Column1);
                myArr.add(word1);
                Data = Data + ", " + word1;
            }
            ;
            c.close();
        }
        c.deactivate();

        c = db.rawQuery(querystring1, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column = c.getColumnIndex("f_name");
            String word = c.getString(Column);
            myArr.add(word);
            while (c.moveToNext()) {
                word = c.getString(Column);
                myArr.add(word);
            }
            ;
            c.close();
        }
        c.deactivate();

        c = db.rawQuery(querystring2, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column1 = c.getColumnIndex("f_name");
            String word1 = c.getString(Column1);
            if (Data == null) {
                Data = word1;

            } else {
                Data = Data + ", " + word1;
            }
            myArr.add(word1);
            while (c.moveToNext()) {
                word1 = c.getString(Column1);
                myArr.add(word1);
                Data = Data + ", " + word1;
            }
            ;
            c.close();
        }
        c.deactivate();
        c = db.rawQuery(querystring3, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column1 = c.getColumnIndex("f_name");
            String word = c.getString(Column1);
            myArr.add(word);
            while (c.moveToNext()) {
                word = c.getString(Column1);
                myArr.add(word);
            }
            ;
            c.close();
        }
        c.deactivate();
        c = db.rawQuery(querystring5, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column = c.getColumnIndex("f_name");
            String word = c.getString(Column);
            myArr.add(word);
            while (c.moveToNext()) {
                word = c.getString(Column);
                myArr.add(word);
            }
            ;
            c.close();
        }
        c.deactivate();
        c = db.rawQuery(querystring6, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column = c.getColumnIndex("f_name");
            String word = c.getString(Column);
            myArr.add(word);
            while (c.moveToNext()) {
                word = c.getString(Column);
                myArr.add(word);
            }
            ;
            c.close();
        }
        c.deactivate();
        UserDataBaseHelper UDBH = new UserDataBaseHelper(this);
        SQLiteDatabase db1 = UDBH.getWritableDatabase();
        String customdb_table = "user_table";
        String querystring4 = "SELECT * FROM " + customdb_table
                + " WHERE day='" + mDay + "' AND month='" + mMonth + "'";
        c = db1.rawQuery(querystring4, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Column1 = c.getColumnIndex("name");
            String word1 = c.getString(Column1);
            if (Data == null) {
                Data = word1;
            } else {
                Data = Data + ", " + word1;
            }
            myArr.add(word1);
            while (c.moveToNext()) {
                word1 = c.getString(Column1);
                myArr.add(word1);
                Data = Data + ", " + word1;
            }
            ;
            c.close();
        }
        c.deactivate();
        db1.close();
        db.close();
        TextView action1textview3 = (TextView) findViewById(R.id.action1textview3);
        action1textview3.setText(mDay + "-" + mMonth + "-" + mYear);
        TextView action1textview2 = (TextView) findViewById(R.id.action1textview2);
        action1textview2.setMovementMethod(new ScrollingMovementMethod());

        if (Data != null) {
            action1textview2.setText(Data);

        } else {
            action1textview2.setText("Καμία ονομαστική εορτή");
        }
        listView = (ListView) findViewById(R.id.action1listView1);

        adapter = new LazyAdapter(this);
        listView.setAdapter(adapter);
        List<Map<String, String>> stubItems = new ArrayList<Map<String, String>>();
        Map<String, String> stubItem = new HashMap<String, String>();
        stubItem.put("name", "Loading...");
        stubItem.put("number", "Loading...");
        stubItems.add(stubItem);
        adapter.setItems(stubItems);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                TextView tv = (TextView) arg1.findViewById(R.id.namelisttext02);
                String t = (String) tv.getText();
                String nums[] = t.split(",");
                String num = nums[0].replace("{num=", "");
                final QuickAction qa = new QuickAction(arg1);
                final QuickAction qa1 = new QuickAction(arg1);
                final ActionItem call = new ActionItem();
                final ActionItem sms = new ActionItem();
                qa.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
                if ((num != "") && (num != null)) {
                    final String numbers[] = num.split("@");
                    final int size = numbers.length;
                    call.setTitle("Κλήση");
                    call.setIcon(getResources().getDrawable(R.drawable.call1));
                    call.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qa.dismiss();
                            for (int i = 0; i < size; i++) {
                                String finalnum = numbers[i]
                                        .replaceAll("-", "");
                                final ActionItem number = new ActionItem();
                                number.setTitle(finalnum);
                                qa1.addActionItem(number);
                                number.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String phonenum = "tel:"
                                                + number.getTitle();
                                        startActivity(new Intent(
                                                Intent.ACTION_CALL, Uri
                                                .parse(phonenum)));
                                        qa1.dismiss();
                                    }
                                });
                            }
                            qa1.show();
                        }
                    });
                    qa.addActionItem(call);
                    sms.setTitle("SMS");
                    sms.setIcon(getResources().getDrawable(R.drawable.sms2));
                    sms.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qa.dismiss();
                            for (int i = 0; i < size; i++) {
                                String finalnum = numbers[i]
                                        .replaceAll("-", "");
                                final ActionItem number = new ActionItem();
                                number.setTitle(finalnum);
                                qa1.addActionItem(number);
                                number.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String phonenum = "sms:"
                                                + number.getTitle();
                                        startActivity(new Intent(
                                                Intent.ACTION_VIEW, Uri
                                                .parse(phonenum)));
                                        qa1.dismiss();
                                    }
                                });
                            }
                            qa1.show();
                        }
                    });
                    qa.addActionItem(sms);
                    qa.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Επαφή χωρίς τηλέφωνο!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
        myArr1 = myArr;
        Collections.copy(myArr1, myArr);
        new LoadItemsTask().execute();
    }

    public void get_date() {
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mMonth++;
        mDay = cal.get(Calendar.DAY_OF_MONTH);
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

    private class LoadItemsTask extends AsyncTask<Void, Void, Void> {
        private List<Map<String, String>> items;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                items = ListData.getData(myArr1, ConRe);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            adapter.setItems(items);
            SharedPreferences app_preferences = getBaseContext()
                    .getSharedPreferences("Shared_Prefs", 0);
            SharedPreferences.Editor editor = app_preferences.edit();

            editor.putString("refresh", "0");
            editor.apply();
            if (items.size() == 0) {
                TextView action1textView5 = (TextView) findViewById(R.id.action1textView5);
                action1textView5.setText("Καμία επαφή δε γιορτάζει");
                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                action1textView5.setTypeface(tf, Typeface.BOLD);
                action1textView5.setVisibility(0);
            } else {
                TextView action1textView5 = (TextView) findViewById(R.id.action1textView5);
                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                action1textView5.setTypeface(tf);
                action1textView5.setVisibility(8);
            }

        }
    }

    public void findSunriseSunset() {

        // Calculate Sunrise/Sunset
        Location location = new Location("37.9838", "23.7275");
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, "Europe/Athens");

        String officialSunrise = calculator.getOfficialSunriseForDate(Calendar.getInstance());
        String officialSunset = calculator.getOfficialSunsetForDate(Calendar.getInstance());


        // Calculate Day Duration
        String[] parts1 = officialSunrise.split(":");
        int hours1 = Integer.parseInt(parts1[0]);
        int minutes1 = Integer.parseInt(parts1[1]);

        String[] parts2 = officialSunset.split(":");
        int hours2 = Integer.parseInt(parts2[0]);
        int minutes2 = Integer.parseInt(parts2[1]);

        int sunriseminutes = hours1 * 60 + minutes1;
        int sunsetminutes = hours2 * 60 + minutes2;

        int dayDuration = sunsetminutes - sunriseminutes;

        int durationHours = dayDuration / 60;
        int durationMinutes = dayDuration % 60;


        // Set values in TextViews

        TextView anatoliNumber = (TextView) findViewById(R.id.anatoliNumber);
        anatoliNumber.setText(officialSunrise);

        TextView disiNumber = (TextView) findViewById(R.id.disiNumber);
        disiNumber.setText(officialSunset);

        TextView diarkeiaNumber = (TextView) findViewById(R.id.diarkeiaNumber);
        diarkeiaNumber.setText(String.valueOf(durationHours) + " ώρες και " + String.valueOf(durationMinutes) + " λεπτά");

        // Stylize TextViews
        TextView anatoliText = (TextView) findViewById(R.id.anatoliText);
        TextView disiText = (TextView) findViewById(R.id.disiText);
        TextView diarkeiaText = (TextView) findViewById(R.id.diarkeiaText);

        anatoliText.setTextColor(getResources().getColor(R.color.greenColor));
        disiText.setTextColor(getResources().getColor(R.color.greenColor));
        diarkeiaText.setTextColor(getResources().getColor(R.color.greenColor));

        anatoliNumber.setTextColor(Color.WHITE);
        disiNumber.setTextColor(Color.WHITE);
        diarkeiaNumber.setTextColor(Color.WHITE);


    }


}