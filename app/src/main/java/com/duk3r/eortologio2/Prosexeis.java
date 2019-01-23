package com.duk3r.eortologio2;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;

import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;

public class Prosexeis extends ActionBarActivity implements NavigationDrawerCallbacks, OnClickListener {

    int myDay, mMonth, mYear;
    String font;
    int text_size,title_text_size=20;
    int back_color,text_color,border_color;
    private Toolbar mToolbar;
    private Boolean firstRun = false;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public void get_prefs(){
        SharedPreferences app_preferences = getBaseContext().getSharedPreferences("Shared_Prefs",0);
        font=app_preferences.getString("font", "default");
        text_size=Integer.parseInt(app_preferences.getString("text_size", "16"));
        text_color=app_preferences.getInt("col", Color.WHITE);
        back_color=app_preferences.getInt("col1", 0x37474F);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //get_app_theme_prefs();
        //set_app_theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionprosexis);

        Button button_prev = (Button) findViewById(R.id.button1);
        button_prev.setOnClickListener(this);
        Button button_next = (Button) findViewById(R.id.button2);
        button_next.setOnClickListener(this);
        Button find = (Button) findViewById(R.id.button3);
        find.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Prosexeis.this);
                dialog.setContentView(R.layout.search_dialog);
                dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                dialog.setTitle("Αναζήτηση βάσει ημερομηνίας");
                dialog.setCancelable(true);
                Calendar calendar = Calendar.getInstance();
                final WheelView month1 = (WheelView) dialog.findViewById(R.id.month);
                final WheelView day1 = (WheelView) dialog.findViewById(R.id.day);

                OnWheelChangedListener listener = new OnWheelChangedListener() {
                    public void onChanged(WheelView wheel, int oldValue, int newValue) {
                        updateDays(month1, day1);
                    }
                };
                int curMonth = calendar.get(Calendar.MONTH);
                String months[] = new String[] { "Ιανουάριος", "Φεβρουάριος",
                        "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος",
                        "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος",
                        "Δεκέμβριος" };
                month1.setViewAdapter(new DateArrayAdapter(getBaseContext(), months, curMonth));
                month1.setCurrentItem(curMonth);
                month1.addChangingListener(listener);



                updateDays(month1, day1);
                day1.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
                Button ok = (Button) dialog.findViewById(R.id.okbutton);
                ok.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        myDay = day1.getCurrentItem()+1;
                        mMonth = month1.getCurrentItem()+1;
                        calculate();
                        dialog.dismiss();
                    }

                });
                dialog.show();
            }
        });

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


    public void set_theme(){
        LinearLayout back = (LinearLayout) findViewById(R.id.linearLayout1);
        back.setBackgroundColor(back_color);
        TextView tv2 = (TextView) findViewById(R.id.action2textView2);
        TextView tv3 = (TextView) findViewById(R.id.action2textView3);
        TextView tv4 = (TextView) findViewById(R.id.action2textView4);
        TextView tv5 = (TextView) findViewById(R.id.action2textView5);
        TextView tv6 = (TextView) findViewById(R.id.action2textView6);
        TextView tv7 = (TextView) findViewById(R.id.action2textView7);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv2.setTextSize(text_size);
        tv3.setTextSize(text_size);
        tv4.setTextSize(text_size);
        tv5.setTextSize(24);
        tv6.setTextSize(24);
        tv7.setTextSize(24);
        tv5.setTypeface(tf,Typeface.BOLD);
        tv2.setTypeface(tf);
        tv6.setTypeface(tf, Typeface.BOLD);
        tv3.setTypeface(tf);
        tv7.setTypeface(tf,Typeface.BOLD);
        tv4.setTypeface(tf);
        tv2.setTextColor(text_color);
        tv3.setTextColor(text_color);
        tv4.setTextColor(text_color);
        tv5.setTextColor(getResources().getColor(R.color.greenColor));
        tv6.setTextColor(getResources().getColor(R.color.greenColor));
        tv7.setTextColor(getResources().getColor(R.color.greenColor));
        tv2.setBackgroundResource(border_color);
        tv3.setBackgroundResource(border_color);
        tv4.setBackgroundResource(border_color);
    }

    public void get_date() {
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mMonth++;
        myDay = cal.get(Calendar.DAY_OF_MONTH) + 1; // remove +1 to start Prosexeis from Current Day
    }

    @Override
    public void onResume() {
        super.onResume();
        get_date();
        get_prefs();
        set_theme();
        calculate();

        // Pollfish surveys
        PollFish.init(this, "8395f944-f1e6-4e74-aed1-51420abc1da1", Position.BOTTOM_RIGHT, 5);

    }

    public void calculate(){

        int s1,s2;
        int day=myDay;
        String Data=null;
        for (int i=0;i<3;i++,day++){
            if ((mMonth==2 || mMonth==4  || mMonth==6 || mMonth==8 || mMonth==9 || mMonth==11) && day<=0){
                if (myDay==-2){
                    day=29;
                    myDay=29;
                }
                else if (myDay==-1){
                    day=30;
                    myDay=30;
                }
                else {
                    day=31;
                    myDay=31;
                }
                mMonth--;
            }
            if ((mMonth == 5 || mMonth == 7 || mMonth == 10 || mMonth ==12) && day<=0){
                if (myDay==-2){
                    day=28;
                    myDay=28;
                }
                else if (myDay==-1){
                    day=29;
                    myDay=29;
                }
                else {
                    day=30;
                    myDay=30;
                }

                mMonth--;
            }
            if ((mMonth==3) && day<2 ){
                if (myDay==-2){
                    day=26;
                    myDay=26;
                }
                else if (myDay==-1){
                    day=27;
                    myDay=27;
                }
                else {
                    day=28;
                    myDay=28;
                }
                mMonth--;
            }
            if (mMonth==1 && day<=0){
                if (myDay==-2){
                    day=29;
                    myDay=29;
                }
                else if (myDay==-1){
                    day=30;
                    myDay=30;
                }
                else {
                    day=31;
                    myDay=31;
                }
                mMonth=12;
                mYear--;
            }
            if ((mMonth == 2) && day==29){
                day=1;
                myDay=1;
                mMonth++;
            }
            if ((mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11) && day==31){
                day=1;
                myDay=1;
                mMonth++;
            }
            if ((mMonth == 1 || mMonth == 3 || mMonth == 5 || mMonth == 7 || mMonth == 8 || mMonth == 10 ) && day==32){
                day=1;
                myDay=1;
                mMonth++;
            }
            if (mMonth == 12 && day==32){
                day=1;
                myDay=1;
                mMonth=1;
                mYear++;
            }
            if (i==0) {
                s1 = R.id.action2textView2;
                s2 = R.id.action2textView5;
            }
            else if (i==1) {
                s1 = R.id.action2textView3;
                s2 = R.id.action2textView6;
            }
            else {
                s1 = R.id.action2textView4;
                s2 = R.id.action2textView7;
            }
            String querystring = "SELECT * FROM gr_names WHERE day ="
                    + day + " AND month=" + mMonth;
            String querystring1 = "SELECT * FROM gr_names_kin WHERE day="
                    + day + " AND month=" + mMonth +" AND year=" + mYear;
            AppDataBaseHelper ADBH = new AppDataBaseHelper(this);
            SQLiteDatabase db = ADBH.getWritableDatabase();
            Cursor c=null;
            c = db.rawQuery(querystring,null);
            Data=null;
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                Data = word1;
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    Data = Data + ", " + word1;
                };
                c.close();
            }
            c.deactivate();
            c = db.rawQuery(querystring1,null);
            if (c!=null && c.getCount()>0){
                c.moveToFirst();
                int Column1 = c.getColumnIndex("f_name");
                String word1 = c.getString(Column1);
                if (Data==null) {
                    Data = word1;
                }
                else {
                    Data = Data + ", " + word1;
                }
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    Data = Data + ", " + word1;
                };
                c.close();
            }
            c.deactivate();
            UserDataBaseHelper UDBH = new UserDataBaseHelper(getBaseContext());
            SQLiteDatabase db1 = UDBH.getWritableDatabase();
            String customdb_table = "user_table";
            String querystring4 = "SELECT * FROM "+ customdb_table + " WHERE day='"+day+"' AND month='"+mMonth+"'";
            c=db1.rawQuery(querystring4, null);
            if (c!= null && c.getCount()>0) {
                c.moveToFirst();
                int Column1 = c.getColumnIndex("name");
                String word1 = c.getString(Column1);
                if (Data==null) {
                    Data = word1;
                }
                else {
                    Data = Data + ", " + word1;
                }
                while (c.moveToNext()) {
                    word1 = c.getString(Column1);
                    Data = Data + ", " + word1;
                };
                c.close();
            }
            c.deactivate();
            db.close();
            db1.close();
            TextView tv1 = (TextView) findViewById(s2);
            tv1.setText(day + "-" + mMonth + "-" + mYear);
            TextView tv2 = (TextView) findViewById(s1);
            if (Data==null) {
                tv2.setText("Καμία ονομαστική εορτή");
            }
            else {
                tv2.setText(Data);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v==findViewById(R.id.button2)){
            myDay+=3;
            calculate();
        }
        if (v==findViewById(R.id.button1)){
            myDay-=3;
            calculate();
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
}
