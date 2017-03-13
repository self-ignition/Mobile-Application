package comself_ignition.httpsgithub.meetneat.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import dmax.dialog.SpotsDialog;

import android.app.ProgressDialog;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {
    //Textbox variables
    EditText username;
    EditText email;
    EditText password;
    EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        password2 = (EditText) findViewById(R.id.input_confirmPassword);

        username.setFilters(new InputFilter[]{filter});
        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});
        password2.setFilters(new InputFilter[]{filter});
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

    public void signup() {
        final AlertDialog dialog = new SpotsDialog(this,"Creating Account");
        dialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        validate();

                        // On complete call either onLoginSuccess or onLoginFailed
                        dialog.dismiss();
                    }
                }, 3000);
    }

    public void validate(){

        final String _user = username.getText().toString();
        final String _email = email.getText().toString();
        final  String _password = password.getText().toString();
        final  String _password2 = password2.getText().toString();


        if (_user.isEmpty()) {
            username.setError("Please enter a valid username");
        } else if (_email.isEmpty()) {
            email.setError("Please enter a valid email address");
        } else if (_password.isEmpty() || _password.length() < 6 || _password.length() > 12) {
            password.setError("Please enter a valid password");
        } else if (!_password.equals(_password2)) {
            password.setError("Passwords do not match");
            password2.setError("Passwords do not match");
        } else if(android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            ServerRequests serverRequest = new ServerRequests();
            Log.i("EMAIL", _email);
            serverRequest.SignUp(this, _user, _email, _password);

            SaveSharedPreference.setUserName(this, _user);
            SaveSharedPreference.setEmailAddress(this, _email);
            SaveSharedPreference.setLoggedIn(this, false);

            Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {

            email.setError("Please enter a valid email address");
        }
    }


    public void clickFunction(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void buttonFunction(View v) {
        signup();
    }
}