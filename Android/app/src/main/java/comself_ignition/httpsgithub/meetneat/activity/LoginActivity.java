package comself_ignition.httpsgithub.meetneat.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;



import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;
import dmax.dialog.SpotsDialog;

import static android.R.attr.filter;

public class LoginActivity extends AppCompatActivity implements VolleyCallback {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});

        email.setText(SaveSharedPreference.getUserName(this));
        if(SaveSharedPreference.getLoggedIn(this)) {
            onSuccess("1");
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
        final AlertDialog dialog = new SpotsDialog(this,"Authenticating");
        dialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        validate();
                        dialog.dismiss();
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
            ServerRequests req = new ServerRequests();
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
            SaveSharedPreference.setLoggedIn(this, true);
            Intent intent = new Intent(this, MainActivity.class);
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
}