package in.stallats.ecuris.Personal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class NewAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ad_title, ad_full_name, ad_mobile_number, ad_pincode, ad_building, ad_street, ad_landmark, ad_city, ad_state;
    private ProgressDialog progressDialog;
    private Button login;
    private Session session;
    String id;
    boolean act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        session = new Session(this);
        HashMap<String, String> user = session.getUserDetails();
        id = user.get("id");

        login = (Button) findViewById(R.id.ad_address);
        login.setOnClickListener(this);

        ad_title = (EditText) findViewById(R.id.ad_title);
        ad_full_name = (EditText) findViewById(R.id.ad_full_name);
        ad_mobile_number = (EditText) findViewById(R.id.ad_mobile_number);
        ad_pincode = (EditText) findViewById(R.id.ad_pincode);
        ad_building = (EditText) findViewById(R.id.ad_building);
        ad_street = (EditText) findViewById(R.id.ad_street);
        ad_landmark = (EditText) findViewById(R.id.ad_landmark);
        ad_city = (EditText) findViewById(R.id.ad_city);
        ad_state = (EditText) findViewById(R.id.ad_state);

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
            case R.id.ad_address:
                add();
                break;
            default:

        }
    }

    public void add() {
        String title = ad_title.getText().toString();
        String name = ad_full_name.getText().toString();
        String mobile = ad_mobile_number.getText().toString();
        String pincode = ad_pincode.getText().toString();
        String bilding = ad_building.getText().toString();
        String street = ad_street.getText().toString();
        String landmark = ad_landmark.getText().toString();
        String city = ad_city.getText().toString();
        String state = ad_state.getText().toString();

        if (title.isEmpty() || name.isEmpty() || mobile.isEmpty() || pincode.isEmpty() || bilding.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty()) {
            displayToast("Please Enter all Fields");
        } else {
            logincheck();
        }

    }

    private void logincheck() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Address Adding...");
        progressDialog.show();


        JsonObject json = new JsonObject();
        json.addProperty("title", ad_title.getText().toString());
        json.addProperty("name", ad_full_name.getText().toString());
        json.addProperty("mobile", ad_mobile_number.getText().toString());
        json.addProperty("pincode", ad_pincode.getText().toString());
        json.addProperty("building", ad_building.getText().toString());
        json.addProperty("street", ad_street.getText().toString());
        json.addProperty("landmark", ad_landmark.getText().toString());
        json.addProperty("city", ad_city.getText().toString());
        json.addProperty("state", ad_state.getText().toString());
        json.addProperty("user_id", id.toString());

        Ion.with(this)
                .load("POST", "http://portal.ecuris.in/api/address/")
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
                            startActivity(new Intent(getApplicationContext(), AddressActivity.class).putExtra("value", act));
                            finish();
                        }
                    }
                });
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
