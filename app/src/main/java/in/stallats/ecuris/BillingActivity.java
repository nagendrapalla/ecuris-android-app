package in.stallats.ecuris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.HashMap;

import in.stallats.ecuris.Common.AreaDetectorActivity;
import in.stallats.ecuris.Common.SuccessActivity;
import in.stallats.ecuris.Personal.AddressActivity;
import in.stallats.ecuris.Personal.AvailTimeActivity;
import in.stallats.ecuris.Personal.PatientActivity;
import in.stallats.ecuris.Supporting.Session;

public class BillingActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    float tot_price = 0, shipping = 0, tax_rate = 10;
    int tot_items = 0;

    HashMap<String, String> user;
    HashMap<String, String> address;
    HashMap<String, String> patient;
    HashMap<String, String> avail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        //Address Management
        LinearLayout address_lo_1 = (LinearLayout) findViewById(R.id.address_lo_1);
        LinearLayout address_lo_2 = (LinearLayout) findViewById(R.id.address_lo_2);
        LinearLayout address_lo_3 = (LinearLayout) findViewById(R.id.address_lo_3);

        //Patient Management
        LinearLayout patient_lo_1 = (LinearLayout) findViewById(R.id.patient_lo_1);
        LinearLayout patient_lo_2 = (LinearLayout) findViewById(R.id.patient_lo_2);
        LinearLayout patient_lo_3 = (LinearLayout) findViewById(R.id.patient_lo_3);

        //Availabilty Date and Time
        LinearLayout avail_lo_1 = (LinearLayout) findViewById(R.id.avail_lo_1);
        LinearLayout avail_lo_2 = (LinearLayout) findViewById(R.id.avail_lo_2);
        LinearLayout avail_lo_3 = (LinearLayout) findViewById(R.id.avail_lo_3);

        TextView txtxpincode = (TextView) findViewById(R.id.txtpincode);

        Button billing_button = (Button) findViewById(R.id.billing_button);

        session = new Session(this);
        user = session.getUserDetails();
        address = session.getAddress();
        patient = session.getPatient();
        avail = session.getAvailTime();

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

        if (pincode == null || pincode.equals("1")) {
            Toast.makeText(getApplicationContext(), "Please Enter your pincode to proceed...", Toast.LENGTH_LONG).show();

            address_lo_1.setVisibility(View.GONE);
            address_lo_2.setVisibility(View.GONE);
            address_lo_3.setVisibility(View.GONE);

            patient_lo_1.setVisibility(View.GONE);
            patient_lo_2.setVisibility(View.GONE);
            patient_lo_3.setVisibility(View.GONE);

            avail_lo_1.setVisibility(View.GONE);
            avail_lo_2.setVisibility(View.GONE);
            avail_lo_3.setVisibility(View.GONE);

            billing_button.setVisibility(View.GONE);
        } else {

            address_lo_1.setVisibility(View.VISIBLE);
            address_lo_2.setVisibility(View.VISIBLE);
            address_lo_3.setVisibility(View.VISIBLE);

            patient_lo_1.setVisibility(View.VISIBLE);
            patient_lo_2.setVisibility(View.VISIBLE);
            patient_lo_3.setVisibility(View.VISIBLE);

            avail_lo_1.setVisibility(View.VISIBLE);
            avail_lo_2.setVisibility(View.VISIBLE);
            avail_lo_3.setVisibility(View.VISIBLE);

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


            patient_lo_3.setOnClickListener(this);

            TextView patient_name_text = (TextView) findViewById(R.id.patient_name_text);
            TextView patient_age_text = (TextView) findViewById(R.id.patient_age_text);
            TextView patient_gender_text = (TextView) findViewById(R.id.patient_gender_text);
            TextView patient_button_text = (TextView) findViewById(R.id.patient_button_text);

            if (!session.checkPatientMode()) {
                patient_lo_1.setVisibility(View.GONE);
                patient_lo_2.setVisibility(View.GONE);
                patient_button_text.setText("Choose Patient");
            } else {
                patient_name_text.setText(patient.get("patient_name"));
                patient_age_text.setText("Age : " + patient.get("patient_age") + " years");
                patient_gender_text.setText("Gender : " + patient.get("patient_gender"));
                patient_button_text.setText("Change Patient");
            }


            avail_lo_3.setOnClickListener(this);

            TextView avai_text_date_n_time = (TextView) findViewById(R.id.avai_text_date_n_time);
            TextView avai_text_button = (TextView) findViewById(R.id.avai_text_button);

            if (!session.checkAvailMode()) {
                avail_lo_1.setVisibility(View.GONE);
                avail_lo_2.setVisibility(View.GONE);
                avai_text_button.setText("Choose Availabile Time");
            } else {
                avai_text_date_n_time.setText(avail.get("avail_date") + " - " + avail.get("avail_time"));
                avai_text_button.setText("Change Available Time");
            }



            if (session.checkAvailMode() && session.checkPatientMode() && session.checkAddressMode()) {
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
                startActivity(new Intent(this, AddressActivity.class).putExtra("value", true));
                break;
            case R.id.patient_lo_3:
                startActivity(new Intent(this, PatientActivity.class).putExtra("value", true));
                break;
            case R.id.avail_lo_3:
                startActivity(new Intent(this, AvailTimeActivity.class));
                break;
            case R.id.pinchange_btn:
                startActivity(new Intent(this, AreaDetectorActivity.class).putExtra("value", true).putExtra("page", "bill"));
                break;
            case R.id.billing_button:

                final String id = user.get("id");
                Future<JsonArray> get = Ion.with(this)
                        .load("http://portal.ecuris.in/api/cart/" + id)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                if (e != null) {

                                } else {

                                    final String order_id = randomString(9).toString().toUpperCase();

                                    JsonObject avail_time = new JsonObject();
                                    avail_time.addProperty("available_date", avail.get("avail_date"));
                                    avail_time.addProperty("available_time", avail.get("avail_time"));
                                    avail_time.addProperty("count", 1);
                                    avail_time.addProperty("status", 0);

                                    final JsonArray order_info = new JsonArray();

                                    for (int i = 0; i < result.size(); i++) {
                                        String x = result.get(i).toString();
                                        try {
                                            final JSONObject xx = new JSONObject(x);

                                            final String product_id = xx.getString("product_id");
                                            final String product_name = xx.getString("product_name");
                                            final float price = Float.parseFloat(xx.getString("price"));
                                            final int qnty = Integer.parseInt(xx.getString("quantity"));

                                            tot_price = tot_price + (price * qnty);
                                            tot_items = tot_items + qnty;

                                            JsonObject order_info_item = new JsonObject();
                                            order_info_item.addProperty("order_id", order_id);
                                            order_info_item.addProperty("product_id", product_id);
                                            order_info_item.addProperty("product_name", product_name);
                                            order_info_item.addProperty("product_type", xx.getString("module"));
                                            order_info_item.addProperty("avaialable_date_n_time", avail.get("avail_date") + " - " + avail.get("avail_time"));
                                            order_info_item.addProperty("price", price);
                                            order_info_item.addProperty("quantity", qnty);
                                            order_info_item.addProperty("sub_total", (price * qnty));
                                            order_info_item.addProperty("status", 0);
                                            order_info_item.addProperty("vendor_id", xx.getString("vendor_id"));
                                            order_info_item.addProperty("user_id", id);

                                            order_info.add(order_info_item);


                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }

                                    float tax_p = (float) 13.9;
                                    tax_rate = (float) round(((tot_price / 100) * tax_p), 2);
                                    shipping = 10;
                                    float tot_final_price = tax_rate + shipping + tot_price;

                                    JsonObject billing_info2 = new JsonObject();
                                    billing_info2.addProperty("tax_rate", tax_p);
                                    billing_info2.addProperty("tax", tax_rate);
                                    billing_info2.addProperty("shipping", shipping);
                                    billing_info2.addProperty("tot_qty", tot_items);


                                    JsonObject billing_info = new JsonObject();
                                    billing_info.addProperty("order_id", order_id);
                                    billing_info.addProperty("address_id", address.get("address_id"));
                                    billing_info.addProperty("avaialable_date_n_time", avail.get("avail_date") + " - " + avail.get("avail_time"));
                                    billing_info.addProperty("patient_id", patient.get("patient_id"));
                                    billing_info.addProperty("payment_mode", "COD");
                                    billing_info.addProperty("payment_status", 0);
                                    billing_info.addProperty("total_amount", tot_final_price);
                                    billing_info.addProperty("status", 0);
                                    billing_info.addProperty("payment_payed_date", "");

                                    JsonObject complete_order = new JsonObject();
                                    complete_order.add("billing_info", billing_info);
                                    complete_order.add("avail_time", avail_time);
                                    complete_order.add("order_info", order_info);
                                    complete_order.add("billing_info2", billing_info2);

                                    //Toast.makeText(getApplicationContext(), complete_order.toString(), Toast.LENGTH_SHORT).show();

                                    Ion.with(getApplicationContext())
                                            .load("POST", "http://portal.ecuris.in/api/order/")
                                            .setJsonObjectBody(complete_order)
                                            .asString()
                                            .setCallback(new FutureCallback<String>() {
                                                @Override
                                                public void onCompleted(Exception e, String result) {
                                                    if (e != null) {
                                                        Toast.makeText(getApplicationContext(), "Sorry something went wrong", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Ion.with(getApplicationContext())
                                                                .load("DELETE", "http://portal.ecuris.in/api/cartempty/" + id)
                                                                .asString()
                                                                .setCallback(new FutureCallback<String>() {
                                                                    @Override
                                                                    public void onCompleted(Exception e, String result) {
                                                                        if (e != null) {

                                                                        } else {
                                                                            session.remove_availTime();
                                                                            Toast.makeText(getApplicationContext(), "Order Successfully Placed", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(), SuccessActivity.class).putExtra("order_id", order_id));
                                                                            finish();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });

                                }
                            }
                        });

                break;
            default:
        }
    }

}
