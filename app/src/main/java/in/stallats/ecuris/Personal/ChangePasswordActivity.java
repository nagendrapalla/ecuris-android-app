package in.stallats.ecuris.Personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

import in.stallats.ecuris.AccountActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText change_old_password, change_new_password, change_renew_password;
    private Button change_submit, change_cancel;
    private String oldp, newp, renewp;
    private Session session;
    HashMap<String, String> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session = new Session(this);
        change_old_password = (EditText) findViewById(R.id.change_old_password);
        change_new_password = (EditText) findViewById(R.id.change_new_password);
        change_renew_password = (EditText) findViewById(R.id.change_renew_password);

        change_submit = (Button) findViewById(R.id.change_submit);
        change_submit.setOnClickListener(this);
        change_cancel = (Button) findViewById(R.id.change_cancel);
        change_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_submit:
                oldp = change_old_password.getText().toString();
                newp = change_new_password.getText().toString();
                renewp = change_renew_password.getText().toString();

                if (oldp.isEmpty()) {
                    displayToast("Enter Current Password");
                } else if (newp.isEmpty()) {
                    displayToast("Enter New Password");
                } else if (renewp.isEmpty()) {
                    displayToast("Re Enter New Password");
                } else if (!newp.equals(renewp)) {
                    displayToast("Your New Passwords are not Matching");
                } else {
                    changePassword();
                }

                break;
            case R.id.change_cancel:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            default:

        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void changePassword() {
        user = session.getUserDetails();
        final String id = user.get("id");

        JsonObject json = new JsonObject();
        json.addProperty("old_pwd", oldp);
        json.addProperty("new_pwd", newp);

        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/password/" + id)
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {

                        } else {
                            switch (result) {
                                case "1":
                                    Toast.makeText(getApplicationContext(), "Username Wrong", Toast.LENGTH_SHORT).show();
                                    break;
                                case "2":
                                    Toast.makeText(getApplicationContext(), "Sorry, Your Account not Activated", Toast.LENGTH_SHORT).show();
                                    break;
                                case "3":
                                    Toast.makeText(getApplicationContext(), "Sorry, your current password wrong", Toast.LENGTH_SHORT).show();
                                    break;
                                case "4":
                                    Toast.makeText(getApplicationContext(), "Congrats, Your Password Successfully changed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                                    finish();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
