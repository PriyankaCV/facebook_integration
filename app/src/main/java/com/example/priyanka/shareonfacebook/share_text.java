package com.example.priyanka.shareonfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Priyanka on 8/26/2015.
 */
public class share_text extends Activity {

    private EditText edit;
    private Button btns;

    Context con;

    /** face book details */
    static final String APP_ID = "facebook app id";
    static final String[] PERMISSIONS = new String[] { "publish_actions" };
    static final String TOKEN = "access_token";
    static final String EXPIRES = "expires_in";
    static final String KEY = "facebook-credentials";
    static Facebook facebook1;
    private boolean FB_LOGIN = false;



    private SharedPreferences prefs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_alert);
        this.prefs1 = PreferenceManager.getDefaultSharedPreferences(this);

        edit = (EditText) findViewById(R.id.editText1);
        btns = (Button) findViewById(R.id.button1);
        //http://stackoverflow.com/questions/12757416/android-os-networkonmainthreadexception-in-facebook-wall-post
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = edit.getText().toString();
                if (str != " ") {

                    postToWall(str,share_text.this);

                } else {
                    edit.setError("Please Enter Text!");
                }

            }
        });
    }

    public void postToWall(String message, Context con) {
        facebook1 = new Facebook(APP_ID);
        String st = get_token__(con);
        Bundle parameters = new Bundle();
        parameters.putString("message", message);
        parameters.putString("description", "topic share");
        if (st.length() > 0) {
            parameters.putString("access_token", "" + st);
        }
        try {
           // new MyAsyncTask().execute(message);
            facebook1.request("me");
            String response = facebook1.request("me/feed", parameters, "POST");
            Log.d("Tests--->*************", "got response: " + response);

            if (response == null
                    || response.equals("")
                    || response.equals("false")
                    || response
                    .equalsIgnoreCase("{\"error\":{\"message\":\"An active access token must be used to query information about the current user.\",\"type\":\"OAuthException\",\"code\":2500}}")) {
                showToast("Blank response. please loginf again in facebook",
                        con);
                clear_fb_data(con);
            } else {
                showToast("Message posted to your facebook wall!", con);
                edit.setText("");

            }
        } catch (Exception e) {
            showToast("Failed to post to wall!", con);
            e.printStackTrace();
        }
    }

    private static String get_token__(Context con) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = con.getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN, null);
    }

    private static void clear_fb_data(Context con) {
        // TODO Auto-generated method stub
        SharedPreferences se = PreferenceManager
                .getDefaultSharedPreferences(con);
        SharedPreferences.Editor editor = se.edit();
        editor.remove(TOKEN);
        editor.remove(EXPIRES);
        editor.commit();
    }

    private static void showToast(String message, Context con) {
        Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
    }


    class MyAsyncTask extends AsyncTask<Context, Void, Boolean> {
        public Boolean doInBackground(Context... contexts) {
            //            facebook1.request("me");
//            String response = facebook1.request("me/feed", parameters, "POST");
//            Log.d("Tests--->*************", "got response: " + response);
//
//            if (response == null
//                    || response.equals("")
//                    || response.equals("false")
//                    || response
//                    .equalsIgnoreCase("{\"error\":{\"message\":\"An active access token must be used to query information about the current user.\",\"type\":\"OAuthException\",\"code\":2500}}")) {
//                showToast("Blank response. please loginf again in facebook",
//                        con);
//                clear_fb_data(con);
//            } else {
//                showToast("Message posted to your facebook wall!", con);
//            }

            Bundle parameters = new Bundle();
            parameters.putString("message", String.valueOf(contexts[0]));
            parameters.putString("description", "topic share");
            try {
                            facebook1.request("me");
            String response = facebook1.request("me/feed", parameters, "POST");
            Log.d("Tests--->*************", "got response: " + response);

            if (response == null
                    || response.equals("")
                    || response.equals("false")
                    || response
                    .equalsIgnoreCase("{\"error\":{\"message\":\"An active access token must be used to query information about the current user.\",\"type\":\"OAuthException\",\"code\":2500}}")) {
               // showToast("Blank response. please login again in facebook",
                     //   con);
                //clear_fb_data(contexts);

            } else {
                //showToast("Message posted to your facebook wall!", con);
            }
//                facebook1.request("me");   //
//                String response = facebook1.request("me/feed", parameters, "POST");
//                Log.d("Tests", "got response: " + response);
//                if (response == null || response.equals("") ||
//                        response.equals("false")) {
//                    //clear_fb_data(con);
//                    return Boolean.FALSE;
//                } else {
//                    //showToast("Message posted to your facebook wall!", con);
//                    return Boolean.TRUE;
//                }
            } catch (Exception e) {

                e.printStackTrace();
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }



        public void onPostExecute(Boolean result) {
            if (result == Boolean.TRUE) {
                showToast("posted successfully");
            } else {
                showToast("couldn't post to FB.");
            }
            finish();
        }
        private void showToast(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
