package in.stallats.ecuris.Personal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class NewPatientActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pt_name, pt_age, pt_gender;
    private ProgressDialog progressDialog;
    private Button login;
    private Session session;
    String id;
    boolean act;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        session = new Session(this);
        HashMap<String, String> user = session.getUserDetails();
        id = user.get("id");

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        login = (Button) findViewById(R.id.pt_add);
        login.setOnClickListener(this);

        pt_name = (EditText) findViewById(R.id.pt_name);
        pt_age = (EditText) findViewById(R.id.pt_age);
        //pt_gender = (EditText) findViewById(R.id.pt_gender);
        act = getIntent().getBooleanExtra("value", false);



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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pt_add:
                add();
                break;
            default:

        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void add() {
        String pt_name_s = pt_name.getText().toString();
        String pt_age_s = pt_age.getText().toString();
        //String pt_gender_s = pt_gender.getText().toString();

        if (pt_name_s.isEmpty()) {
            displayToast("Enter Patient Name");
        } else if (pt_age_s.isEmpty()) {
            displayToast("Enter Patient Age");
        } else {
            logincheck();
        }
    }

    private void logincheck() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Patient Adding...");
        progressDialog.show();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        //Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();

        JsonObject json = new JsonObject();
        json.addProperty("name", pt_name.getText().toString());
        json.addProperty("age", pt_age.getText().toString());
        json.addProperty("gender", radioButton.getText().toString());
        json.addProperty("user_id", id.toString());

        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/patients/")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PatientActivity.class).putExtra("value", act)) ;
                            finish();
                        }
                    }
                });
    }

}
