package in.stallats.ecuris.Orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.Session;

public class OrderDiagDescActivity extends AppCompatActivity {

    private Session session;
    ConnectionDetector cd;
    String order_id;
    private int count = 0, succ_count = 0, can_count = 0;
    LinearLayout cancel_btn;
    ScrollView scroll_id;

    LinearLayout ll, l;
    Button cancel_btn_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_diag_desc);

        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            //Toast.makeText(this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }


        HashMap<String, String> user = session.getUserDetails();
        String id = user.get("id");

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            order_id = ((MyApplication) getApplicationContext()).getOrder_id();
            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
        } else {
            order_id = extras.getString("order_id");
            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
        }

        getSupportActionBar().setTitle("ORDER ID : " + order_id);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        l = (LinearLayout) findViewById(R.id.order_item_parent);
        cancel_btn = (LinearLayout) findViewById(R.id.cancel_btn);
        scroll_id = (ScrollView) findViewById(R.id.scroll_id);
        cancel_btn_text = (Button) findViewById(R.id.cancel_btn_text);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/orderhistory/" + id + "/" + order_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Sorry no orders found", Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    JSONObject order_info = xx.getJSONObject("order");

                                    TextView order_pay_status = (TextView) findViewById(R.id.order_pay_status);
                                    TextView order_pay_amount = (TextView) findViewById(R.id.order_pay_amount);
                                    TextView order_pay_date = (TextView) findViewById(R.id.order_pay_date);

                                    //Toast.makeText(getApplicationContext(), order_info.getString("payment_status"), Toast.LENGTH_SHORT).show();

                                    if (Integer.parseInt(order_info.getString("payment_status")) == 0) {
                                        order_pay_status.setText("Not Paid");
                                        order_pay_status.setTextColor(Color.parseColor("#D80000"));
                                    } else {
                                        order_pay_status.setText("Paid");
                                        order_pay_status.setTextColor(Color.parseColor("#4caf50"));
                                    }

                                    order_pay_amount.setText(order_info.getString("total_amount"));
                                    order_pay_date.setText(order_info.getString("billed_time"));


                                    JSONArray item_info = xx.getJSONArray("item");
                                    Integer items_length = item_info.length();

                                    for (int j = 0; j < items_length; j++) {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View format = inflater.inflate(R.layout.orderdiagdesclayout, null);

                                        TextView order_item_name = (TextView) format.findViewById(R.id.order_item_name);
                                        TextView order_item_desc_type = (TextView) format.findViewById(R.id.order_item_desc_type);
                                        TextView order_item_desc_price = (TextView) format.findViewById(R.id.order_item_desc_price);
                                        TextView order_item_desc_vendor = (TextView) format.findViewById(R.id.order_item_desc_vendor);
                                        TextView order_item_status = (TextView) format.findViewById(R.id.order_item_status);

                                        LinearLayout download_part = (LinearLayout) format.findViewById(R.id.download_part);

                                        String imp = null;

                                        imp = item_info.get(j).toString();
                                        final JSONObject xxx = new JSONObject(imp);

                                        order_item_name.setText(xxx.getString("product_name"));
                                        order_item_desc_type.setText(xxx.getString("product_type"));
                                        order_item_desc_price.setText("RS." + xxx.getString("price"));
                                        order_item_desc_vendor.setText(xxx.getString("vendor"));

                                        String or_st = "";

                                        if (Integer.parseInt(xxx.getString("status")) > 2) {
                                            count++;
                                        }

                                        switch (Integer.parseInt(xxx.getString("status"))) {
                                            case 0:
                                                or_st = "New Order Placed";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 1:
                                                or_st = "Assigned to Vendor";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 2:
                                                or_st = "Assigned to Collection Boy";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 3:
                                                or_st = "Test Sample collected by Collection Boy";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 4:
                                                or_st = "Test Sample Receieved by Vendor";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 5:
                                                or_st = "Testing on process";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 6:
                                                or_st = "Testing Completed process";
                                                download_part.setVisibility(View.GONE);
                                                break;
                                            case 8:
                                                or_st = "Order Cancelled";
                                                download_part.setVisibility(View.GONE);
                                                can_count++;
                                                break;
                                            case 7:
                                                or_st = "Order Closed";
                                                succ_count++;
                                                download_part.setVisibility(View.VISIBLE);
                                                download_part.setOnClickListener(new LinearLayout.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        try {
                                                            Intent i2 = new Intent(getApplicationContext(), ReportDownloadActivity.class);
                                                            i2.putExtra("order_id", xxx.getString("order_id"));
                                                            i2.putExtra("item_id", xxx.getString("id"));
                                                            i2.putExtra("product_name", xxx.getString("product_name"));
                                                            startActivity(i2);
                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    }
                                                });
                                                break;
                                        }

                                        order_item_status.setText(or_st);
                                        l.addView(format);
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            if(can_count == 0){

                                if (count == 0 && succ_count == 0) {
                                    cancel_btn_text.setText("Cancel Order");
                                    cancel_btn_text.setBackgroundColor(Color.parseColor("#D80000"));
                                    cancel_btn_text.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Future<JsonObject> get = Ion.with(getApplicationContext())
                                                    .load("http://portal.ecuris.in/api/cancelorder/" + order_id)
                                                    .asJsonObject()
                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                        @Override
                                                        public void onCompleted(Exception e, JsonObject result) {
                                                            Toast.makeText(getApplicationContext(), "Order Cancelled", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            startActivity(getIntent());
                                                        }
                                                    });

                                        }
                                    });
                                } else if (count != 0 && succ_count == 0) {
                                    cancel_btn_text.setText("Order in Progress");
                                    cancel_btn_text.setBackgroundColor(Color.parseColor("#FF8C00"));
                                } else {
                                    cancel_btn_text.setText("Order Completed");
                                    cancel_btn_text.setBackgroundColor(Color.parseColor("#228B22"));
                                }
                            }else{
                                cancel_btn_text.setText("Order Cancelled");
                                cancel_btn_text.setBackgroundColor(Color.parseColor("#E400FF"));
                            }

                        }
                    }
                });

    }
}
