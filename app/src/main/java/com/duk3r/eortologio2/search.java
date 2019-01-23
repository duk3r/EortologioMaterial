package com.duk3r.eortologio2;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class search extends ActionBarActivity implements NavigationDrawerCallbacks {

    String font;
    int text_size;
    private Boolean firstRun = false;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onResume(){
        super.onResume();
        get_prefs();
        set_theme();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(!firstRun){
            firstRun = true;
            return;
        }
        switch(position){
            case 0:
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
                finish();
                break;
            case 1:
                Intent in2 = new Intent(getApplicationContext(),Prosexeis.class);
                startActivity(in2);
                finish();
                break;
            case 2:
                Intent in3 = new Intent(getApplicationContext(),ActionBday.class);
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
                Intent in5 = new Intent(getApplicationContext(),ActionArgies.class);
                startActivity(in5);
                finish();
                break;
            case 4:
                Intent in6 = new Intent(getApplicationContext(),ActionPagosmies.class);
                startActivity(in6);
                finish();
                break;
        }
    }

    public void set_theme(){
        TextView tv1 = (TextView) findViewById(R.id.textViewsearch);
        if (font.equalsIgnoreCase("default")){
            tv1.setTypeface(Typeface.DEFAULT,1);
        }
        else {
            tv1.setTypeface(Typeface.createFromAsset(this.getAssets(), font),1);
        }
        tv1.setTextSize(text_size);
    }

    public void get_prefs(){
        SharedPreferences app_preferences = getBaseContext().getSharedPreferences("Shared_Prefs",0);
        font=app_preferences.getString("font", "default");
        text_size=Integer.parseInt(app_preferences.getString("text_size", "16"));
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        TextView drawer_text = (TextView)findViewById(R.id.cover_version);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        drawer_text.setText(R.string.app_version);
        drawer_text.setTypeface(tf);
        drawer_text.setTextColor(Color.WHITE);
        drawer_text.setTextSize(18);

        //Enable back navigation at App icon
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("Αναζήτηση");

        //Change button's image
        Button search = (Button) findViewById(R.id.button_go);

        Toast.makeText(getBaseContext(), "Η αναζήτηση με greeklish μπορεί να μην επιστρέψει σωστά αποτελέσματα", Toast.LENGTH_LONG).show();
        AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
        String[] one = ADBH.getAllAppNames("SELECT DISTINCT f_name FROM (SELECT f_name FROM gr_names UNION SELECT f_name FROM gr_names_kin) a");

        final AutoCompleteTextView search_box = (AutoCompleteTextView) findViewById(R.id.input_search_query);
        search_box.setTextColor(Color.WHITE);
        search_box.setThreshold(1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, one);
        search_box.setAdapter(adapter);

        search.setOnClickListener(new OnClickListener(){

            @SuppressLint("DefaultLocale")
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                String getname = search_box.getText().toString();
                if (getname.length()==0){
                    Toast.makeText(getApplicationContext(), "Παρακαλώ εισάγετε όνομα!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String celebrating="";
                    String firstLetter = getname.substring(0, 1);
                    String remaining = getname.substring(1);
                    String name = firstLetter.toUpperCase()	+ remaining.toLowerCase();
                    final Calendar cal = Calendar.getInstance();
                    int mYear = cal.get(Calendar.YEAR);
                    String year = Integer.toString(mYear);
                    String querystring = "SELECT * FROM gr_names WHERE f_name ='"+ name + "'";
                    String querystring1 = "SELECT * FROM gr_names_kin WHERE f_name ='" + name + "' AND year='" + year + "'";
                    String querystring2 = "SELECT * FROM gr_names_en WHERE f_name ='" + name + "'";
                    String querystring4 = "SELECT * FROM user_table WHERE name=\"" + name + "\"";
                    String querystring3 = "SELECT * FROM gr_names_lo WHERE f_name ='" + name + "'";
                    String querystring5 = "SELECT * FROM gr_names_kin_en WHERE f_name ='" + name + "' AND year='" + year + "'";
                    String querystring6 = "SELECT * FROM gr_names_kin_lo WHERE f_name ='" + name + "' AND year='" + year + "'";
                    UserDataBaseHelper UDBH = new UserDataBaseHelper(getBaseContext());
                    SQLiteDatabase dbu = UDBH.getWritableDatabase();
                    Cursor c = dbu.rawQuery(querystring4, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    dbu.close();
                    AppDataBaseHelper ADBH = new AppDataBaseHelper(getBaseContext());
                    SQLiteDatabase db = ADBH.getWritableDatabase();
                    c= db.rawQuery(querystring, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    c= db.rawQuery(querystring1, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    c= db.rawQuery(querystring2, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    c= db.rawQuery(querystring3, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    c= db.rawQuery(querystring5, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    c= db.rawQuery(querystring6, null);
                    if (c!= null && c.getCount() > 0) {
                        c.moveToFirst();
                        int nCol = c.getColumnIndex("f_name");
                        int dCol = c.getColumnIndex("day");
                        int mCol = c.getColumnIndex("month");
                        String name1 = c.getString(nCol);
                        String day = c.getString(dCol);
                        String month = c.getString(mCol);
                        if (celebrating == "") {
                            celebrating = name1 + " : "	+ day + "/" + month;
                        }
                        else
                        {
                            celebrating += ", " + day + "/" + month;
                        }
                        while (c.moveToNext()) {
                            day = c.getString(dCol);
                            month = c.getString(mCol);
                            celebrating += ", " + day + "/" + month;
                        };
                        c.close();
                    }
                    c.deactivate();
                    db.close();
                    TextView celnames = (TextView) findViewById(R.id.textViewsearch);
                    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                    celnames.setTypeface(tf);
                    if (celebrating==""){
                        celnames.setText("Καμία ονομαστική εορτή");
                    }
                    else {
                        celnames.setText(celebrating);
                    }
                }
            }

        });
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
