package in.stallats.ecuris.Personal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.stallats.ecuris.BillingActivity;
import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.Session;

public class PatientActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    ConnectionDetector cd;
    LinearLayout main;
    boolean act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        session = new Session(this);
        if (!session.loggedin()) {
            Toast.makeText(this, "Please login to get your saved cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        main = (LinearLayout) findViewById(R.id.parent_patient);
        //findViewById(R.id.patient_list).setVisibility(View.GONE);

        Button btn = (Button) findViewById(R.id.add_new_patient);
        btn.setOnClickListener(this);

        HashMap<String, String> user = session.getUserDetails();
        final String id = user.get("id");

        act = getIntent().getBooleanExtra("value", false);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/patients/" + id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            //findViewById(R.id.patient_list).setVisibility(View.VISIBLE);

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View view = inflater.inflate(R.layout.patient_fragment, main, false);
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    LinearLayout child_patient = (LinearLayout) view.findViewById(R.id.child_patient);
                                    TextView get_patient_text_name = (TextView) view.findViewById(R.id.get_patient_text_name);
                                    TextView get_patient_text_age = (TextView) view.findViewById(R.id.get_patient_text_age);
                                    TextView get_patient_text_gender = (TextView) view.findViewById(R.id.get_patient_text_gender);

                                    TextView dtxt = (TextView) view.findViewById(R.id.get_patient_delete);

                                    final String pat_id = xx.getString("id");
                                    final String pat_name = xx.getString("patient_name");

                                    dtxt.setOnClickListener(new TextView.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            Ion.with(getApplicationContext())
                                                    .load("DELETE", "http://portal.ecuris.in/api/patients/" + "/" + pat_id)
                                                    .asString()
                                                    .setCallback(new FutureCallback<String>() {
                                                        @Override
                                                        public void onCompleted(Exception e, String result) {
                                                            if (e != null) {

                                                            } else {
                                                                Toast.makeText(getApplicationContext(),  pat_name + " removed", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(getIntent());
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                    get_patient_text_name.setText(xx.getString("patient_name"));
                                    get_patient_text_age.setText("Age : " + xx.getString("age") + " years");
                                    get_patient_text_gender.setText("Gender : " + xx.getString("gender"));

                                    if (act) {
                                        child_patient.setOnClickListener(new LinearLayout.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    String id = xx.getString("id");
                                                    String patient_name = xx.getString("patient_name");
                                                    String age = xx.getString("age");
                                                    String gender = xx.getString("gender");
                                                    Boolean patient_mode = true;
                                                    session.set_patient(id, patient_name, age, gender, patient_mode);
                                                    Toast.makeText(getApplicationContext(), "Patient : " + patient_name + " Selected", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), BillingActivity.class));
                                                    finish();

                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        });
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                main.addView(view);
                            }

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_patient:
                startActivity(new Intent(this, NewPatientActivity.class).putExtra("value", act)) ;
                finish();
                break;
            default:
        }
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
