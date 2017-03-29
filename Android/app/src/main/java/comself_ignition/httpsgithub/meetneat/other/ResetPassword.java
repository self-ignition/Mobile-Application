package comself_ignition.httpsgithub.meetneat.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.LoginActivity;

public class ResetPassword extends AppCompatActivity implements VolleyCallback {

    EditText currentPassword;
    EditText newPassword;
    EditText cNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //email = (EditText) findViewById(R.id.input_email);

        currentPassword = (EditText) findViewById(R.id.input_currentPassword);
        newPassword = (EditText) findViewById(R.id.input_password1);
        cNewPassword = (EditText) findViewById(R.id.input_password2);

        /*currentPassword.setFilters(new InputFilter[]{filter});
        newPassword.setFilters(new InputFilter[]{filter});
        cNewPassword.setFilters(new InputFilter[]{filter});*/
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

    public void validate(){
        final String _currentPassword = currentPassword.getText().toString();
        final String _newPassword = newPassword.getText().toString();
        final String _cNewPassword = cNewPassword.getText().toString();
        final String _email = SaveSharedPreference.getEmailAddress(this);

        if(_currentPassword.isEmpty()) {
            currentPassword.setError("Please enter your current password");
        }
        else if (_newPassword.isEmpty() || _newPassword.length() < 6 || _newPassword.length() > 12) {
            newPassword.setError("Please enter a valid password");
        } else if(_cNewPassword.isEmpty()) {
            cNewPassword.setError("Please confirm your password");
        }
        else if (!_newPassword.equals(_cNewPassword)) {
            newPassword.setError("Passwords do not match");
            cNewPassword.setError("Passwords do not match");
        }
        else if(_newPassword.equals(_cNewPassword)){
            ServerRequests serverRequest = new ServerRequests();
            serverRequest.changePassword(this, _email, _currentPassword, _newPassword, this);
        }
        else{
            return;
        }
    }

    public void confirmChange(View v){
        validate();
    }


    @Override
    public void onSuccess(String result) {
        if(result.equals("1")){
            Toast.makeText(this, "Successfully Changed Password, Please log in again", Toast.LENGTH_LONG).show();
            SaveSharedPreference.setLoggedIn(this, false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Oops...Something Went Wrong", Toast.LENGTH_LONG).show();
        }
    }
}
