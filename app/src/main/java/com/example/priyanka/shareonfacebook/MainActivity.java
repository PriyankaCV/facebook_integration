package com.example.priyanka.shareonfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class MainActivity extends Activity {
    private LoginButton btn_fb;
    private Button btn_share;

    /**
     * face book details
     */
    static final String APP_ID = "1666489166906223";
    static final String[] PERMISSIONS = new String[]{"publish_actions"};
    static final String TOKEN = "access_token";
    static final String EXPIRES = "expires_in";
    static final String KEY = "facebook-credentials";
    static Facebook facebook1;
    private boolean FB_LOGIN = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        btn_fb = (LoginButton) findViewById(R.id.btn_fb);
        btn_share = (Button) findViewById(R.id.btn_share);

        btn_fb.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
//        if(!isLoggedIn()){
//            btn_fb.setVisibility(View.VISIBLE);
//        }

//        if(FB_LOGIN==false)
//        {
//            btn_fb.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            btn_fb.setVisibility(View.GONE);
//        }

//        if(isLoggedIn())
//        {
//            btn_fb.setVisibility(View.GONE);
//        }else
//        {
//            btn_fb.setVisibility(View.VISIBLE);
//        }




        btn_fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                facebook1 = new Facebook(APP_ID);
                loginAndPostToWall();

            }
        });


        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, share_text.class));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btn_fb.setVisibility(View.GONE);
    }

//    public boolean isLoggedIn() {
//        session_session session = session_session.getActiveSession();
//        return (session != null && session.isOpened());
//    }

    @Override
    public void onBackPressed () {
        moveTaskToBack(true);
    }


    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void loginAndPostToWall() {
        facebook1.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH,
                new LoginDialogListener());

    }

    class LoginDialogListener implements Facebook.DialogListener {

        public void onComplete(Bundle values) {
            saveCredentials(facebook1);
            FB_LOGIN = true;
        }

        public void onFacebookError(FacebookError error) {
            showToast("Authentication with Facebook failed!");
        }

        public void onError(DialogError error) {
            showToast("Authentication with Facebook failed!");
        }

        public void onCancel() {
            showToast("Authentication with Facebook cancelled!");
        }


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public boolean saveCredentials(Facebook facebook) {

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(KEY,
                Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, facebook.getAccessToken());
        editor.putLong(EXPIRES, facebook.getAccessExpires());
        return editor.commit();
    }

    public boolean restoreCredentials(Facebook facebook) {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);

        facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        return facebook.isSessionValid();
    }

}
