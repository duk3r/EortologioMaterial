package com.duk3r.eortologio2;

import java.util.Collections;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterEortesList extends BaseAdapter{
    private Context context;
    private List<Map<String, String>> items = Collections.emptyList();
    int font;
    Typeface font1;
    int text_color;
    int theme;

    public LazyAdapterEortesList(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int index) {
        return items.get(index);
    }
    @Override
    public long getItemId(int index) {
        return items.get(index).hashCode();
    }

    public void setItems(List<Map<String, String>> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int index, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.bdaylistlist, parent, false);
        }
        TextView text0 = (TextView)view.findViewById(R.id.textViewbday1);
        TextView text1 = (TextView)view.findViewById(R.id.textViewbday2);
        TextView text2 = (TextView)view.findViewById(R.id.textViewbday3);

        SharedPreferences app_preferences = context.getSharedPreferences("Shared_Prefs",0);
        text_color=Color.WHITE;//app_preferences.getInt("col", 0xffffffff);

        String font = app_preferences.getString("font", "default");
        int text_size=Integer.parseInt(app_preferences.getString("text_size", "16"));
        if (font.equalsIgnoreCase("default")){
            text0.setTypeface(Typeface.DEFAULT,1);
            text1.setTypeface(Typeface.DEFAULT,1);
            text2.setTypeface(Typeface.DEFAULT,1);
        }
        else {
            text0.setTypeface(Typeface.createFromAsset(context.getAssets(), font),1);
            text1.setTypeface(Typeface.createFromAsset(context.getAssets(), font),1);
            text2.setTypeface(Typeface.createFromAsset(context.getAssets(), font),1);
        }
        text0.setTextSize(text_size);
        text1.setTextSize(text_size);
        text2.setTextSize(text_size);
        text0.setText(items.get(index).get("date"));
        text1.setText(items.get(index).get("name"));
        text2.setText(items.get(index).get("number"));
        text0.setTextColor(text_color);
        text1.setTextColor(text_color);
        text2.setTextColor(text_color);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/roboto.ttf");
        text0.setTypeface(tf,Typeface.BOLD);
        text1.setTypeface(tf);
        text2.setTypeface(tf);
        return view;
    }

}
