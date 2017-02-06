package com.self_ignition.cabbage2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        getSupportActionBar().hide();
    }

    public void buttonFunction(View v)
    {
        login();
    }

    public void clickFunction(View v)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void login()
    {
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
    public void onBackPressed()
    {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate()
    {
        boolean valid = true;

        String _email = email.getText().toString();
        String _password = password.getText().toString();

        if (_email.isEmpty())
        {
            email.setError("Please enter a valid email address");
            valid = false;
        }
        else
        {
            email.setError(null);
        }

        if (_password.isEmpty() || _password.length() < 6 || _password.length() > 12)
        {
            password.setError("Please enter a valid password");
        }
        else
        {
            password.setError(null);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        return valid;
    }
}
