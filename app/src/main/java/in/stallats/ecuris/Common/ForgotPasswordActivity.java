package in.stallats.ecuris.Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    TextView etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etPass = (TextView) findViewById(R.id.etMobile);
        findViewById(R.id.getsubmit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getsubmit:
                String pass = etPass.getText().toString();
                if (pass.isEmpty()) {
                    displayToast("Enter Mobile Number");
                } else {
                    logincheck();
                }
                break;
        }
    }

    private void logincheck() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        JsonObject json = new JsonObject();
        json.addProperty("mobile", etPass.getText().toString());
        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/forgot/")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Oooops! Something went wrong", Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if ((boolean) jsonObject.get("status") == false) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), (String) jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    displayToast("Password sent to your message");
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
