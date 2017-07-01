package in.stallats.ecuris.Medicines;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.stallats.ecuris.Common.AreaDetectorActivity;
import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.SuccessActivity;
import in.stallats.ecuris.Personal.AddressActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.MyCommand;
import in.stallats.ecuris.Supporting.Session;

public class MedicineAddress extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    HashMap<String, String> user;
    HashMap<String, String> address;

    ArrayList<String> ar1;
    String selectedRadio;
    String manualRequ, presc_status;
    int presc_num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_address);

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        user = session.getUserDetails();
        address = session.getAddress();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            ar1 = ((MyApplication) getApplicationContext()).getAr1();
            selectedRadio = ((MyApplication) getApplicationContext()).getSelectedRadio();
            manualRequ = ((MyApplication) getApplicationContext()).getManualRequ();
            presc_status = ((MyApplication) getApplicationContext()).getPresc_status();

            ((MyApplication) getApplicationContext()).setAr1(ar1);
            ((MyApplication) getApplicationContext()).setSelectedRadio(selectedRadio);
            ((MyApplication) getApplicationContext()).setManualRequ(manualRequ);
            ((MyApplication) getApplicationContext()).setPresc_status(presc_status);
        } else {

            ar1 = getIntent().getExtras().getStringArrayList("imgs");
            selectedRadio = getIntent().getStringExtra("selectedRadio");
            manualRequ = getIntent().getStringExtra("manual_req");
            presc_status = getIntent().getStringExtra("presc_status");

            ((MyApplication) getApplicationContext()).setAr1(ar1);
            ((MyApplication) getApplicationContext()).setSelectedRadio(selectedRadio);
            ((MyApplication) getApplicationContext()).setManualRequ(manualRequ);
            ((MyApplication) getApplicationContext()).setPresc_status(presc_status);
        }

        if(presc_status.equals("Yes")){
            presc_num = 1;
        }else{
            presc_num = 0;
        }

        TextView txtxpincode = (TextView) findViewById(R.id.txtpincode);

        String pincode = session.getPincode();
        if (pincode == null) {
            txtxpincode.setText("Pincode: ----");
        } else {
            if (pincode.equals("1")) {
                txtxpincode.setText("Pincode: ----");
            } else {
                txtxpincode.setText("Pincode: " + pincode);
            }
        }

        LinearLayout btnChange = (LinearLayout) findViewById(R.id.pinchange_btn);
        btnChange.setOnClickListener(this);

        //Address Management
        LinearLayout address_lo_1 = (LinearLayout) findViewById(R.id.address_lo_1);
        LinearLayout address_lo_2 = (LinearLayout) findViewById(R.id.address_lo_2);
        LinearLayout address_lo_3 = (LinearLayout) findViewById(R.id.address_lo_3);

        Button billing_button = (Button) findViewById(R.id.billing_button);

        if (pincode == null || pincode.equals("1")) {
            Toast.makeText(getApplicationContext(), "Please Enter your pincode to proceed...", Toast.LENGTH_LONG).show();

            address_lo_1.setVisibility(View.GONE);
            address_lo_2.setVisibility(View.GONE);
            address_lo_3.setVisibility(View.GONE);

            billing_button.setVisibility(View.GONE);
        } else {

            address_lo_1.setVisibility(View.VISIBLE);
            address_lo_2.setVisibility(View.VISIBLE);
            address_lo_3.setVisibility(View.VISIBLE);

            billing_button.setVisibility(View.VISIBLE);


            address_lo_3.setOnClickListener(this);

            TextView address_text_button = (TextView) findViewById(R.id.address_text_button);
            TextView address_text_address = (TextView) findViewById(R.id.address_text_address);
            TextView address_text_name = (TextView) findViewById(R.id.address_text_name);
            TextView address_text_title = (TextView) findViewById(R.id.address_text_title);

            if (!session.checkAddressMode()) {
                address_lo_1.setVisibility(View.GONE);
                address_lo_2.setVisibility(View.GONE);
                address_text_button.setText("Choose Address");
            } else {
                final String ti = address.get("address_title");
                final String adid = address.get("address_id");
                final String ad = address.get("address_address");
                final String username = user.get("name");

                address_text_name.setText(username);
                address_text_title.setText(ti);
                address_text_address.setText(ad);
                address_text_button.setText("Change Address");
            }


            if (session.checkAddressMode()) {
                billing_button.setVisibility(View.VISIBLE);
                billing_button.setOnClickListener(this);
            } else {
                billing_button.setVisibility(View.GONE);
            }
        }

    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_lo_3:
                startActivity(new Intent(this, AddressActivity.class).putExtra("value", true).putExtra("med_order", true));
                break;
            case R.id.pinchange_btn:
                startActivity(new Intent(this, AreaDetectorActivity.class).putExtra("value", true).putExtra("page", "med"));
                break;
            case R.id.billing_button:

                final String id = user.get("id");
                final String order_id = randomString(9).toString().toUpperCase();

                JsonObject billing_info = new JsonObject();
                billing_info.addProperty("order_id", order_id);
                billing_info.addProperty("address_id", address.get("address_id"));
                billing_info.addProperty("user_id", id);
                billing_info.addProperty("prescrition", presc_num);
                billing_info.addProperty("pres_mode", selectedRadio);
                billing_info.addProperty("status", 0);
                billing_info.addProperty("manual_requirements", manualRequ);

                JsonObject complete_order = new JsonObject();
                complete_order.add("billing_info", billing_info);

                final JsonArray order_info = new JsonArray();

                if(presc_num == 1){
                    final MyCommand myCommand = new MyCommand(getApplicationContext());
                    for (String s : ar1){
                        try {
                            final String imgName = randomString(24)+".png";
                            Bitmap bitmap = ImageLoader.init().from(s).requestSize(1024, 1024).getBitmap();
                            final String encodedImage = ImageBase64.encode(bitmap);
                            String url = "http://portal.ecuris.in/file_upload.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Something while uploading photos....", Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("image", encodedImage);
                                    params.put("file_name_image", imgName);
                                    return params;
                                }
                            };

                            myCommand.add(stringRequest);

                            JsonObject order_info_item = new JsonObject();
                            order_info_item.addProperty("order_id", order_id);
                            order_info_item.addProperty("path", imgName);
                            order_info_item.addProperty("status", 0);
                            order_info.add(order_info_item);

                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "Something wrong while encoding photos....", Toast.LENGTH_LONG).show();
                        }

                    }

                    myCommand.execute();
                    complete_order.add("order_info", order_info);
                }

                Ion.with(getApplicationContext())
                        .load("POST", "http://portal.ecuris.in/api/medorder/")
                        .setJsonObjectBody(complete_order)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), "Sorry something went wrong", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(getApplicationContext(), SuccessActivity.class).putExtra("order_id", order_id));
                                    finish();
                                }
                            }
                        });
        }
    }


}
