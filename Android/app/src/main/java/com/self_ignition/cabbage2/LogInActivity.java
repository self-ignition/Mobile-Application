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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static android.R.attr.filter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.self_ignition.cabbage2.SeverRequests;
import com.google.android.gms.location.LocationServices;

public class LogInActivity extends AppCompatActivity implements VolleyCallback {

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

        email.setText(SaveSharedPreference.getUserName(this));

        long Expire = SaveSharedPreference.getDateLoggedIn(this).getTime() + 86400000;
        long Now = new Date().getTime();
        Log.i("COMPARE TIME", "Delta Time: " + (Expire - Now));
        if (Expire <=  Now) {
            this.onSuccess("1");
        }
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
        } else if(_password.isEmpty() || _password.length() < 6 || _password.length() > 12) {
            password.setError("Please enter a valid password");
        } else if (_email.length() < 1) {
            email.setError("Please enter a valid email address");
        } else {
            //Login method here.
            SeverRequests req = new SeverRequests();
            req.Login(this, _email, _password, this);
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

    @Override
    public void onSuccess(String result) {
        if(result.equals("1"))
        {
            SaveSharedPreference.setUserName(this, email.getText().toString());
            SaveSharedPreference.setDateLoggedIn(this, new Date());
            SaveSharedPreference.setLoggedIn(this, true);
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
        }
        else
        {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}

class SaveSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_LOGGED_IN= "LoggedIn";
    static final String PREF_DATE_LOGGED_IN = "date";

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

    public static void setDateLoggedIn(Context ctx, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DATE_LOGGED_IN, sdf.format(date));
        Log.i("date", "setDateLoggedIn: Date ====== " + sdf.format(date));
        editor.commit();
    }

    public static Date getDateLoggedIn(Context ctx){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = getSharedPreferences(ctx).getString(PREF_DATE_LOGGED_IN, "");
        try {
            Log.i("date", "getDateLoggedIn: Date ====== " + date);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally
        {
            return null;
        }

    }
}
