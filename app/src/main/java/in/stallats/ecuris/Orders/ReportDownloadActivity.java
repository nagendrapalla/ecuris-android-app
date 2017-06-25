package in.stallats.ecuris.Orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.Session;

public class ReportDownloadActivity extends AppCompatActivity {

    private Session session;
    ConnectionDetector cd;
    String order_id, product_name, item_id;

    LinearLayout ll, l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_download);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            order_id = ((MyApplication) getApplicationContext()).getOrder_id();
            product_name = ((MyApplication) getApplicationContext()).getProduct_name();
            item_id = ((MyApplication) getApplicationContext()).getItem_id();

            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
            ((MyApplication) getApplicationContext()).setProduct_name(product_name);
            ((MyApplication) getApplicationContext()).setItem_id(item_id);
        } else {
            order_id = extras.getString("order_id");
            product_name = extras.getString("product_name");
            item_id = extras.getString("item_id");

            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
            ((MyApplication) getApplicationContext()).setProduct_name(product_name);
            ((MyApplication) getApplicationContext()).setItem_id(item_id);
        }

        getSupportActionBar().setTitle("Reports of " + product_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        HashMap<String, String> user = session.getUserDetails();
        String id = user.get("id");

        l = (LinearLayout) findViewById(R.id.report_item_parent);

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

                                    JSONArray item_info = xx.getJSONArray("reports");
                                    Integer items_length = item_info.length();

                                    for (int j = 0; j < items_length; j++) {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View format = inflater.inflate(R.layout.reportdesclayout, null);

                                        String imp = null;

                                        imp = item_info.get(j).toString();
                                        final JSONObject xxx = new JSONObject(imp);
                                        String item_id = xxx.getString("order_item_id");

                                        if(item_id.equals(item_id)){
                                            TextView order_item_name = (TextView) format.findViewById(R.id.download_part_report);
                                            order_item_name.setText(xxx.getString("report_name"));
                                        }
                                        format.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    Toast.makeText(getApplicationContext(), "Clicked on " + xxx.getString("report_name"), Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://portal.ecuris.in/assets/uploads/reports/" + xxx.getString("report_file"))));
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        });
                                        l.addView(format);
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }


                        }
                    }
                });

    }
}
