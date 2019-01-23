package com.duk3r.eortologio2;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
/*
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
*/
import android.app.AlarmManager;

public class MyService extends Service {

    Integer update_interval;
    public static final String APP_ID = "205010826195183";
    //private Facebook mFacebook;
    //private AsyncFacebookRunner mAsyncRunner;
    private static final String[] PERMS = new String[] { "friends_birthday","publish_stream" };

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {

		/*
		super.onStart(intent, startId);
		SharedPreferences app_preferences = getBaseContext()
				.getSharedPreferences("Shared_Prefs", 0);
		update_interval = Integer.parseInt(app_preferences.getString(
				"fb_update", "0"));
		String token = app_preferences.getString("Token", "none");
		if (update_interval != 0) {
			scheduleNextUpdate();
			mFacebook = new Facebook(APP_ID);
			mAsyncRunner = new AsyncFacebookRunner(mFacebook);
			
			if (token.equalsIgnoreCase("none")) {
			}
			else {
				Log.e("gandevs.eortologio","yay");
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
						mFacebook);
				Bundle params = new Bundle();
				params.putString("fields",
						"id,name,first_name,last_name,birthday");
				params.putString("access_token", token);
				asyncRunner.request("me/friends", params,
						new FriendBdayRequestListener());
			}
			
		}
		else {
			this.stopSelf();
		}
		
		Log.e("gandevs.eortologio", "Service started");
		*/
    }

    private void scheduleNextUpdate() {
        Intent intent = new Intent(this, this.getClass());
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        long currentTimeMillis = System.currentTimeMillis();
        long nextUpdateTimeMillis = currentTimeMillis + update_interval;
        Time nextUpdateTime = new Time();
        nextUpdateTime.set(nextUpdateTimeMillis);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
    }
	
	/*
	private class FriendBdayRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {

			try {
				final JSONObject json = new JSONObject(response);
				JSONArray d = json.getJSONArray("data");
				int l = (d != null ? d.length() : 0);
				FBDataBaseHelper FDBH = new FBDataBaseHelper(
						getApplicationContext());
				SQLiteDatabase db = FDBH.getWritableDatabase();
				
				db.execSQL("DROP TABLE fb_table");
				db.execSQL("CREATE TABLE IF NOT EXISTS " + "fb_table"
						+ " (id TEXT, name TEXT ,first_name TEXT, day INTEGER,"
						+ " month INTEGER);");
				for (int i = 0; i < l; i++) {

					JSONObject o = d.getJSONObject(i);
					String name = o.getString("name");
					String name1 = name.replace("'", "\"");
						name=name1.replace("-", " ");
					String id = o.getString("id");
					String first_name = o.getString("first_name");
					String first_name1 = first_name.replace("'", "\"");
						first_name=first_name1.replace("-", " ");
					String last_name = o.getString("last_name");
					String last_name1 =last_name.replace("'", "\"");
						last_name=last_name1.replace("-", " ");
					String birthday;
					String day;
					String month;
					if (o.has("birthday")) {
						birthday = o.getString("birthday");
						String[] bday = birthday.split("/");
						day = bday[1];
						month = bday[0];

					} else {
						birthday = null;
						day = null;
						month = null;
					}					
					try {
						db.execSQL("INSERT INTO fb_table (id,name, first_name, day, month) VALUES ('"
								+ id
								+ "', '"
								+ name
								+ "','"
								+ first_name
								+ "' ,'"
								+ day + "' ,'" + month + "');");
					} catch (SQLException e) {
						e.printStackTrace();
						continue;
					}
				}
				db.close();
				
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response" + e);
			}
			
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Log.e("gandevs.eortologio", e.toString());
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.e("gandevs.eortologio", e.toString());
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Log.e("gandevs.eortologio", e.toString());
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Log.e("gandevs.eortologio", e.toString());
		}
	}
*/
	/*
	private class LoginDialogListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {
			
		}

		@Override
		public void onFacebookError(FacebookError e) {
			Log.e("gandevs.eortologio", e.toString());
		}

		@Override
		public void onError(DialogError e) {
			Log.e("gandevs.eortologio", e.toString());
		}

		@Override
		public void onCancel() {

		}
	}*/
}