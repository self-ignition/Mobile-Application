package com.self_ignition.cabbage2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static android.R.attr.filter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.self_ignition.cabbage2.SeverRequests;
import com.google.android.gms.location.LocationServices;

public class LogInActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});
    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String blockCharacterSet = " !#$%&()*+,-.:;<=>?@[]^_{|}~";
            for (int i = start; i < end; i++) {
                if (source != null && !Character.isLetterOrDigit(source.charAt(i)) && !blockCharacterSet.contains("" + source)) {
                    return "";
                }
            }
            return null;
        }
    };

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        validate();
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        final String _email = email.getText().toString();
        final String _password = password.getText().toString();

        if (_email.isEmpty()) {
            email.setError("Please enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (_password.isEmpty() || _password.length() < 6 || _password.length() > 12) {
            password.setError("Please enter a valid password");
        } else {
            password.setError(null);
        }

        //Login method here.
        SeverRequests req = new SeverRequests();
        req.Login(this, _email, _password);

        //Find location
        try {
            LocationManager locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            Criteria criteria = new Criteria();

            //Check for permission
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Criteria for provider search
                location = locman.getLastKnownLocation(locman.getBestProvider(criteria, true));
            }

            //send location to server
            req.SendLocation(this, _email, location);
        }
        catch (Exception e)
        {
            Log.e("LOCATION ERROR", e.getMessage());
        }

        return valid;
    }

    public void buttonFunction(View v) {
        login();
    }

    public void clickFunction(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

class SaveSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_LOGGED_IN= "LoggedIn";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setLoggedIn(Context ctx, Boolean isLoggedIn)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public static Boolean getLoggedIn(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_LOGGED_IN, false);
    }
}
