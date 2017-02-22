package comself_ignition.httpsgithub.meetneat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;



import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.ServerRequests;
import comself_ignition.httpsgithub.meetneat.VolleyCallback;
import comself_ignition.httpsgithub.meetneat.fragment.HomeFragment;

public class LoginActivity extends AppCompatActivity implements VolleyCallback {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

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
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
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
            //SaveSharedPreference.setUserName(this, email.getText().toString());
            //SaveSharedPreference.setDateLoggedIn(this, new Date());
            //SaveSharedPreference.setLoggedIn(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
        else
        {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}