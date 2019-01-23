package com.duk3r.eortologio2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;

public class ActionArgies extends ActionBarActivity implements NavigationDrawerCallbacks {

    String font;
    int text_size,title_text_size=20;
    int back_color,text_color;
    private Boolean firstRun = false;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;



    @Override
    public void onResume(){
        super.onResume();
        get_prefs();
        set_theme();

        // Pollfish surveys
        PollFish.init(this, "8395f944-f1e6-4e74-aed1-51420abc1da1", Position.BOTTOM_RIGHT, 5);

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

    public void get_prefs(){
        SharedPreferences app_preferences = getBaseContext().getSharedPreferences("Shared_Prefs",0);
        font=app_preferences.getString("font", "default");
        text_size=Integer.parseInt(app_preferences.getString("text_size", "16"));
        text_color=app_preferences.getInt("col", Color.WHITE);
        back_color=app_preferences.getInt("col1", 0x37474F);
    }

    public void set_theme(){
        LinearLayout back = (LinearLayout) findViewById(R.id.linearLayout1);
        back.setBackgroundColor(back_color);

        TextView tv1 = (TextView) findViewById(R.id.imerominies);
        TextView tv2 = (TextView) findViewById(R.id.onomaeortis);
        TextView tv3 = (TextView) findViewById(R.id.meraeortis);
        if (font.equalsIgnoreCase("default")){
            tv1.setTypeface(Typeface.DEFAULT,1);
            tv2.setTypeface(Typeface.DEFAULT,1);
            tv3.setTypeface(Typeface.DEFAULT,1);
        }
        else {
            tv1.setTypeface(Typeface.createFromAsset(this.getAssets(), font),1);
            tv2.setTypeface(Typeface.createFromAsset(this.getAssets(), font),1);
            tv3.setTypeface(Typeface.createFromAsset(this.getAssets(), font),1);
        }
        tv1.setTextSize(text_size);
        tv2.setTextSize(text_size);
        tv3.setTextSize(text_size);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv1.setTypeface(tf,Typeface.BOLD);
        tv1.setTextColor(text_color);
        tv1.setGravity(Gravity.RIGHT);
        tv2.setTypeface(tf);
        tv2.setTextColor(getResources().getColor(R.color.greenColor));
        tv3.setTypeface(tf,Typeface.ITALIC);
        tv3.setTextColor(text_color);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionargies);

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

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((OnItemSelectedListener) new MyOnItemSelectedListener());
    }

    public void argies(String years){
        String querystring = "SELECT * FROM argies WHERE year='"+years+"' ORDER BY month ASC,day ASC" ;
        AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
        SQLiteDatabase db = ADBH.getWritableDatabase();
        String Data1="",Data2="",Data3="";
        Cursor c = null;
        c=db.rawQuery(querystring,null);

        // gonna try sth xtreme here
        TextView tv1 = (TextView) findViewById(R.id.imerominies);
        TextView tv2 = (TextView) findViewById(R.id.onomaeortis);
        TextView tv3 = (TextView) findViewById(R.id.meraeortis);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            int mCol= c.getColumnIndex("month");
            int dCol = c.getColumnIndex("day");
            int nCol = c.getColumnIndex("name");
            int dnCol = c.getColumnIndex("dayname");
            String month = c.getString(mCol);
            String day = c.getString(dCol);
            String name = c.getString(nCol);
            String dayname = c.getString(dnCol);

            //Data = "\n " + day + "/" + month + " " +dayname + " : "+name+"\n ";
            Data1 = day + "/" + month + "\n ";
            Data2 = " " + dayname + "\n ";
            Data3 = " " + name + "\n ";

            while (c.moveToNext()) {
                day = c.getString(dCol);
                month = c.getString(mCol);
                name = c.getString(nCol);
                dayname = c.getString(dnCol);
                //Data += day + "/"+ month + " " + dayname +" : "+name+"\n ";
                Data1 += day + "/" + month + "\n ";
                Data2 += dayname + "\n ";
                Data3 += name + "\n ";

            };
            c.close();
        }
        c.deactivate();
        db.close();
        //TextView tv1 = (TextView) findViewById(R.id.action3TextView1);
        tv1.setText(Data1);
        tv2.setText(Data2);
        tv3.setText(Data3);
    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String years = parent.getItemAtPosition(pos).toString();
            argies(years);
        }

        public void onNothingSelected(AdapterView parent) {
        }
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
                Intent in = new Intent(getApplicationContext(),Preferences.class);
                startActivity(in);
                return (true);
            case R.id.menu_search:
                startActivity(new Intent(this, search.class));
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}
