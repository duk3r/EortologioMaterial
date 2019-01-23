package com.duk3r.eortologio2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class bdaylist extends Activity {

    public static String WIDGET_THEME="com.duk3r.eortologio2.WIDGET_THEME";

    String font;
    int text_size,title_text_size=20;
    int back_color,text_color;
    private ListView listView;
    private LazyAdapterBdayList adapter;
    public ContentResolver ConRe;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bdaylistactivity);

        get_prefs();
        set_theme();

    }

    @Override
    public void onResume(){
        super.onResume();
        ConRe = getContentResolver();
        listView = (ListView) findViewById(R.id.bdaylistView1);
        adapter = new LazyAdapterBdayList(this);
        listView.setAdapter(adapter);
        List<Map<String, String>> stubItems = new ArrayList<Map<String, String>>();
        Map<String, String> stubItem = new HashMap<String, String>();
        stubItem.put("name", "Loading...");
        stubItem.put("number", "Loading...");
        stubItems.add(stubItem);
        adapter.setItems(stubItems);
        listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                final QuickAction qa = new QuickAction(arg1);
                final ActionItem delete = new ActionItem();
                final ActionItem modify = new ActionItem();
                TextView tv1 = (TextView) arg1.findViewById(R.id.textViewbday1);
                tv1.setTextColor(Color.BLACK);
                TextView tv2 = (TextView) arg1.findViewById(R.id.textViewbday2);
                tv2.setTextColor(Color.BLACK);
                String date = (String) tv1.getText();
                String date1[] = date.split("-");
                final String day = date1[0];
                final String month= date1[1];
                final String name = (String) tv2.getText();
                qa.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
                delete.setTitle("Διαγραφη");
                modify.setTitle("Επεξεργασία");
                delete.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                modify.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_manage));
                delete.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {


                        BdayDataBaseHelper BDBH = new BdayDataBaseHelper(getBaseContext());
                        SQLiteDatabase db = BDBH.getWritableDatabase();
                        try {
                            db.execSQL("DELETE FROM bday_table WHERE name=\""+name+"\" AND day="+day+" AND month="+month+";");
                            Toast.makeText(getBaseContext(), "Επιτυχία", Toast.LENGTH_SHORT).show();
                            new LoadItemsTask2().execute();

                        }catch (SQLException e){
                            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        qa.dismiss();
                    }
                });
                modify.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        qa.dismiss();
                        final Dialog dialog = new Dialog(bdaylist.this);
                        dialog.setContentView(R.layout.bdaylistdialog);
                        dialog.setTitle("Τροποποίηση Γενεθλίων");
                        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
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
                        dialog.show();
                        Button mod = (Button) dialog.findViewById(R.id.bdaylistmodify);
                        mod.setOnClickListener(new OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                int dday = day1.getCurrentItem()+1;
                                int mmonth = month1.getCurrentItem()+1;

                                BdayDataBaseHelper BDBH = new BdayDataBaseHelper(getBaseContext());
                                SQLiteDatabase db = BDBH.getWritableDatabase();
                                try {
                                    db.execSQL("UPDATE bday_table SET day="+dday+",month="+mmonth+" WHERE name=\""+name+"\" AND day="+day+" AND month="+month+";");
                                    Toast.makeText(getBaseContext(), "Επιτυχία", Toast.LENGTH_SHORT).show();
                                    new LoadItemsTask2().execute();

                                }catch (SQLException e){
                                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                });
                qa.addActionItem(delete);
                qa.addActionItem(modify);
                qa.show();
            }

        });
        new LoadItemsTask2().execute();
    }

    private class LoadItemsTask2 extends AsyncTask<Void, Void, Void> {
        private List<Map<String, String>> items;

        @Override
        protected Void doInBackground(Void... params) {
            items = ListData.getDatabdaylist(ConRe,getApplicationContext());
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
                TextView action1textView5 = (TextView) findViewById(R.id.textViewbaylist5);

                action1textView5.setVisibility(View.INVISIBLE);

            } else {
                TextView action1textView5 = (TextView) findViewById(R.id.textViewbaylist5);
                action1textView5.setVisibility(View.VISIBLE);
            }
            Context context = (Context) getBaseContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent theme = new Intent();
            theme.setAction(WIDGET_THEME);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123, theme, 0);
            Calendar now = Calendar.getInstance();
            alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
        }
    }

    public void get_prefs(){
        SharedPreferences app_preferences = getBaseContext().getSharedPreferences("Shared_Prefs",0);
        font=app_preferences.getString("font", "default");
        text_size=Integer.parseInt(app_preferences.getString("text_size", "16"));
        text_color=Color.WHITE;//app_preferences.getInt("col", 0xffffffff);
        //back_color=app_preferences.getInt("col1", 0x00000000);
    }

    public void set_theme(){
        //LinearLayout back = (LinearLayout) findViewById(R.id.linearLayout1);
        //back.setBackgroundColor(back_color);
        TextView tv1 = (TextView) findViewById(R.id.BdayListTextView1);
        TextView tv2 = (TextView) findViewById(R.id.textViewbaylist5);

        tv1.setTextSize(22);
        tv2.setTextSize(text_size);
        tv1.setTextColor(getResources().getColor(R.color.greenColor));
        tv2.setTextColor(text_color);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/roboto.ttf");
        tv1.setTypeface(tf,Typeface.BOLD);
        tv2.setTypeface(tf);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}
