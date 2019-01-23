package com.duk3r.eortologio2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;

public class ActionPagosmies extends ActionBarActivity implements NavigationDrawerCallbacks {

    String font;
    int text_size, title_text_size = 20;
    int back_color, text_color;
    private Boolean firstRun = false;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    public void onResume() {
        super.onResume();
        get_prefs();
        set_theme();

        // Pollfish surveys
        PollFish.init(this, "8395f944-f1e6-4e74-aed1-51420abc1da1", Position.BOTTOM_RIGHT, 5);

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

    public void get_prefs() {
        SharedPreferences app_preferences = getBaseContext().getSharedPreferences("Shared_Prefs", 0);
        font = app_preferences.getString("font", "default");
        text_size = Integer.parseInt(app_preferences.getString("text_size", "16"));
        text_color = app_preferences.getInt("col", Color.WHITE);
        back_color = app_preferences.getInt("col1", 0x37474F);
    }

    public void set_theme() {
        LinearLayout back = (LinearLayout) findViewById(R.id.linearLayoutPagosmies);
        back.setBackgroundColor(back_color);

        //Τίτλοι Activity - Μηνών
        TextView tv1 = (TextView) findViewById(R.id.pagosmiesTitle);
        TextView jan = (TextView) findViewById(R.id.titleJanuary);
        TextView feb = (TextView) findViewById(R.id.titleFebruary);
        TextView mar = (TextView) findViewById(R.id.titleMarch);
        TextView apr = (TextView) findViewById(R.id.titleApril);
        TextView may = (TextView) findViewById(R.id.titleMay);
        TextView jun = (TextView) findViewById(R.id.titleJune);
        TextView jul = (TextView) findViewById(R.id.titleJuly);
        TextView aug = (TextView) findViewById(R.id.titleAugust);
        TextView sep = (TextView) findViewById(R.id.titleSeptember);
        TextView oct = (TextView) findViewById(R.id.titleOctober);
        TextView nov = (TextView) findViewById(R.id.titleNovember);
        TextView dec = (TextView) findViewById(R.id.titleDecember);



        if (font.equalsIgnoreCase("default")) {
            tv1.setTypeface(Typeface.DEFAULT, 1);
            jan.setTypeface(Typeface.DEFAULT, 1);
            feb.setTypeface(Typeface.DEFAULT, 1);
            mar.setTypeface(Typeface.DEFAULT, 1);
            apr.setTypeface(Typeface.DEFAULT, 1);
            may.setTypeface(Typeface.DEFAULT, 1);
            jun.setTypeface(Typeface.DEFAULT, 1);
            jul.setTypeface(Typeface.DEFAULT, 1);
            aug.setTypeface(Typeface.DEFAULT, 1);
            sep.setTypeface(Typeface.DEFAULT, 1);
            oct.setTypeface(Typeface.DEFAULT, 1);
            nov.setTypeface(Typeface.DEFAULT, 1);
            dec.setTypeface(Typeface.DEFAULT, 1);

        } else {
            tv1.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            jan.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            feb.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            mar.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            apr.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            may.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            jun.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            jul.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            aug.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            sep.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            oct.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            nov.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
            dec.setTypeface(Typeface.createFromAsset(this.getAssets(), font), 1);
        }

        // Set Title text size
        tv1.setTextSize(24);
        jan.setTextSize(20);
        feb.setTextSize(20);
        mar.setTextSize(20);
        apr.setTextSize(20);
        may.setTextSize(20);
        jun.setTextSize(20);
        jul.setTextSize(20);
        aug.setTextSize(20);
        sep.setTextSize(20);
        oct.setTextSize(20);
        nov.setTextSize(20);
        dec.setTextSize(20);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv1.setTypeface(tf, Typeface.BOLD);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.actionpagosmies);

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
}
