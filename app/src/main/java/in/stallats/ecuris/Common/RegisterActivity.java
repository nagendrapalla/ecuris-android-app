package in.stallats.ecuris.Common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.Session;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button reg;
    private TextView tvLogin;
    private EditText etName, etEmail, etMobile, etPass, etDob;
    String name, email, mobile, pass, dob, refercode;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Session session;
    private ProgressDialog progressDialog;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            Toast.makeText(this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        reg = (Button) findViewById(R.id.btnReg);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPass = (EditText) findViewById(R.id.etPass);
        etDob = (EditText) findViewById(R.id.etDob);

        session = new Session(this);

        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReg:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            default:

        }
    }

    public void register() {
        email = etEmail.getText().toString();
        name = etName.getText().toString();
        mobile = etMobile.getText().toString();
        pass = etPass.getText().toString();
        dob = etDob.getText().toString();

        if (name.isEmpty()) {
            displayToast("Enter Name");
        } else if (email.isEmpty()) {
            displayToast("Enter Email");
        } else if (!email.matches(emailPattern)) {
            displayToast("Enter Valid Email");
        } else if (mobile.isEmpty()) {
            displayToast("Enter Mobile Number");
        } else if (mobile.length() < 10 || mobile.length() > 10) {
            displayToast("Enter Valid Mobile Number");
        } else if (pass.isEmpty()) {
            displayToast("Enter Password");
        } else {
            signUP();
        }
    }

    private void signUP() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating New Account...");
        progressDialog.show();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        JsonObject json = new JsonObject();

        json.addProperty("name", name);
        json.addProperty("mobile", mobile);
        json.addProperty("email", email);
        json.addProperty("pwd", pass);
        json.addProperty("dob", dob);

        Ion.with(RegisterActivity.this)
                .load("POST", "http://portal.ecuris.in/api/register/")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if ((boolean) jsonObject.get("status") == false) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), (String) jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                logincheck();
                            }
                        } catch (JSONException e1) {
                            progressDialog.dismiss();
                            Log.e(e1.getClass().getName(), e1.getMessage(), e1.getCause());
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logincheck() {
        JsonObject json = new JsonObject();
        json.addProperty("uname", mobile);
        json.addProperty("pwd", pass);
        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/login/")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if ((boolean) jsonObject.get("status") == false) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), (String) jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    String id = (String) jsonObject.get("user_id");
                                    String name = (String) jsonObject.get("name");
                                    String email = (String) jsonObject.get("email");
                                    String mobile = (String) jsonObject.get("mobile");
                                    String refercode = (String) jsonObject.get("refercode");
                                    session.set_sessionData(id, name, email, mobile, refercode);
                                    Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                    session.setLoggedIn(true);
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e1) {
                                progressDialog.dismiss();
                                Log.e(e1.getClass().getName(), e1.getMessage(), e1.getCause());
                                Toast.makeText(getApplicationContext(), "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    @SuppressLint("ShowToast")
    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
