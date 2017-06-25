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

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.Medicines.MedicineAddress;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.Session;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    ConnectionDetector cd;
    LinearLayout main;
    String uname;
    boolean act, med_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

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

        main = (LinearLayout) findViewById(R.id.parent_address);

        Button btn = (Button) findViewById(R.id.add_new_address);
        btn.setOnClickListener(this);

        HashMap<String, String> user = session.getUserDetails();
        final String id = user.get("id");
        uname = user.get("name");

        act = getIntent().getBooleanExtra("value", false);
        med_order = getIntent().getBooleanExtra("med_order", false);


        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/address/" + id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            //findViewById(R.id.address_list).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            //findViewById(R.id.address_list).setVisibility(View.VISIBLE);

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View view = inflater.inflate(R.layout.address_fragment, main, false);
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    LinearLayout child_address = (LinearLayout) view.findViewById(R.id.child_address);
                                    TextView get_address_text_name = (TextView) view.findViewById(R.id.get_address_text_name);
                                    TextView get_address_text_title = (TextView) view.findViewById(R.id.get_address_text_title);
                                    TextView get_address_text_address = (TextView) view.findViewById(R.id.get_address_text_address);

                                    TextView dtxt = (TextView) view.findViewById(R.id.get_address_delete);

                                    final String addr_id = xx.getString("id");
                                    final String addr_title = xx.getString("address_type");

                                    dtxt.setOnClickListener(new TextView.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            Ion.with(getApplicationContext())
                                                    .load("DELETE", "http://portal.ecuris.in/api/address/" + "/" + addr_id)
                                                    .asString()
                                                    .setCallback(new FutureCallback<String>() {
                                                        @Override
                                                        public void onCompleted(Exception e, String result) {
                                                            if (e != null) {

                                                            } else {
                                                                Toast.makeText(getApplicationContext(),  addr_title + " removed", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(getIntent());
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                    get_address_text_name.setText(xx.getString("full_name"));
                                    get_address_text_title.setText(xx.getString("address_type"));
                                    get_address_text_address.setText(xx.getString("building")+", "+xx.getString("street")+", "+xx.getString("landmark")+", "+xx.getString("city")+", "+xx.getString("state")+" - "+xx.getString("pincode"));

                                    if (act) {
                                        child_address.setOnClickListener(new LinearLayout.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    String id = xx.getString("id");
                                                    String title = xx.getString("address_type");
                                                    String address = xx.getString("building")+", "+xx.getString("street")+", "+xx.getString("landmark")+", "+xx.getString("city")+", "+xx.getString("state")+" - "+xx.getString("pincode");
                                                    Boolean address_mode = true;
                                                    session.set_address(id, title, address, address_mode);
                                                    Toast.makeText(getApplicationContext(), "Address : " + title + " Selected", Toast.LENGTH_SHORT).show();

                                                    if(med_order){
                                                        startActivity(new Intent(getApplicationContext(), MedicineAddress.class));
                                                        finish();
                                                    }else{
                                                        //startActivity(new Intent(getApplicationContext(), BillingActivity.class));
                                                        finish();
                                                    }

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
            case R.id.add_new_address:
                startActivity(new Intent(this, NewAddressActivity.class).putExtra("value", act));
                finish();
                break;
            default:
        }
    }

}
