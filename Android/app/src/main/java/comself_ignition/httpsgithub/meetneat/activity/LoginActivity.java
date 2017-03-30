package comself_ignition.httpsgithub.meetneat.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;
import dmax.dialog.SpotsDialog;



public class LoginActivity extends AppCompatActivity implements VolleyCallback {

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});

        if(SaveSharedPreference.getLoggedIn(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
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

    public void validate() {
        final String _email = email.getText().toString();
        final String _password = password.getText().toString();
        String pattern = "@.+\\.(?:com|co)\\.?(?:uk)?";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(_email);
        if (_email.isEmpty()) {
            email.setError("Please enter a valid email address");
        } else if(_password.isEmpty() || _password.length() < 6 || _password.length() > 12) {
            password.setError("Please enter a valid password");
        } else if (_email.length() < 1) {
            email.setError("Please enter a valid email address");
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            ServerRequests req = new ServerRequests();
            req.Login(this, _email, _password, this);

        }else {
            email.setError("Please enter a valid email address");
        }
    }

    public void buttonFunction(View v) {
        login();
    }

    public void clickFunction(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void forgotFunction(View v) {
        ServerRequests serv = new ServerRequests();
        serv.forgotPassword(this, email.getText().toString());
    }

    @Override
    public void onSuccess(String result) {
        CheckBox check = (CheckBox)findViewById(R.id.checkboxLogin);
        if(result.equals("1") && check.isChecked())
        {
            SaveSharedPreference.setEmailAddress(this, email.getText().toString());
            SaveSharedPreference.setLoggedIn(this, true);

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } else if (result.equals("1") && !check.isChecked())
        {
            SaveSharedPreference.setEmailAddress(this, email.getText().toString());
            SaveSharedPreference.setLoggedIn(this, false);

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}

