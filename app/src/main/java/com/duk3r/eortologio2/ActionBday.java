package com.duk3r.eortologio2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
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
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class ActionBday extends ActionBarActivity implements NavigationDrawerCallbacks{

    private ListView listView;
    private LazyAdapterBday adapter;
    public ContentResolver ConRe;
    String font;
    int text_size, title_text_size = 20;
    int back_color, text_color, border_color;
    private Toolbar mToolbar;
    private Boolean firstRun = false;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static String WIDGET_THEME = "com.duk3r.eortologio2.WIDGET_THEME";
    final Context context = this;


    public void get_prefs() {
        SharedPreferences app_preferences = getBaseContext()
                .getSharedPreferences("Shared_Prefs", 0);
        text_size = Integer.parseInt(app_preferences.getString("text_size",	"16"));
        text_color=app_preferences.getInt("col", Color.WHITE);
        back_color=app_preferences.getInt("col1", 0x37474F);

    }

    public void set_theme() {
        LinearLayout back = (LinearLayout) findViewById(R.id.linearLayout1);
        back.setBackgroundColor(back_color);
        TextView tv2 = (TextView) findViewById(R.id.textView2bday);
        tv2.setTextSize(text_size);
        tv2.setTextColor(text_color);
        //tv2.setBackgroundResource(border_color);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv2.setTypeface(tf);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //get_app_theme_prefs();
        //set_app_theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbday);


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

    @Override
    public void onResume() {
        super.onResume();
        get_prefs();
        set_theme();

        // Pollfish surveys
        PollFish.init(this, "8395f944-f1e6-4e74-aed1-51420abc1da1", Position.BOTTOM_RIGHT, 5);

        ConRe = getContentResolver();
        listView = (ListView) findViewById(R.id.listView1bday);
        adapter = new LazyAdapterBday(this);
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
                String t = arg0.getItemAtPosition(arg2).toString();
                String nums[] = t.split(",");
                String num = nums[1].replace(" num=", "");
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
        new LoadItemsTask().execute();
    }

    private class LoadItemsTask extends AsyncTask<Void, Void, Void> {
        private List<Map<String, String>> items;

        @Override
        protected Void doInBackground(Void... params) {
            items = ListData.getDatabday(ConRe, getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.setItems(items);
            SharedPreferences app_preferences = getBaseContext()
                    .getSharedPreferences("Shared_Prefs", 0);
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putString("refresh", "0");
            editor.commit();
            if (items.size() == 0) {
                TextView action1textView5 = (TextView) findViewById(R.id.textView2bday);
                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                action1textView5.setTypeface(tf, Typeface.BOLD);
                action1textView5.setVisibility(0);

                // Leave padding if there are no items
                int paddingPixel = 25;
                float density = getResources().getDisplayMetrics().density;
                int paddingDp = (int)(paddingPixel * density);
                action1textView5.setPadding(0,paddingDp,0,0);

            } else {
                TextView action1textView5 = (TextView) findViewById(R.id.textView2bday);
                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
                action1textView5.setTypeface(tf);
                action1textView5.setVisibility(8);
            }
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

    void updateDays(WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar
                .get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
}
