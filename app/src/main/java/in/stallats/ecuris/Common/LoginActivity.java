package in.stallats.ecuris.Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.HashMap;

import in.stallats.ecuris.AccountActivity;
import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.Session;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText etEmail, etPass;
    private Session session;
    private AppCompatButton register, btnForgotPass;
    private ProgressDialog progressDialog;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            Toast.makeText(this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        session = new Session(this);
        login = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);

        register = (AppCompatButton) findViewById(R.id.btnReg);
        btnForgotPass = (AppCompatButton) findViewById(R.id.btnForgotPass);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        if (session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, AccountActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReg:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.btnForgotPass:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            default:

        }
    }

    public void login() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if (email.isEmpty()) {
            displayToast("Enter Email / Password");
        } else if (pass.isEmpty()) {
            displayToast("Enter Password");
        } else {
            logincheck();
        }

    }

    private void logincheck() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        JsonObject json = new JsonObject();
        json.addProperty("uname", etEmail.getText().toString());
        json.addProperty("pwd", etPass.getText().toString());
        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/login/")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(LoginActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if ((boolean) jsonObject.get("status") == false) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, (String) jsonObject.get("message"), Toast.LENGTH_SHORT).show();
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
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e1) {
                                progressDialog.dismiss();
                                Log.e(e1.getClass().getName(), e1.getMessage(), e1.getCause());
                                Toast.makeText(LoginActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
