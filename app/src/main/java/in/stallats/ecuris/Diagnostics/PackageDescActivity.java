package in.stallats.ecuris.Diagnostics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.stallats.ecuris.Adapters.PackageTestListAdapter;
import in.stallats.ecuris.Adapters.Tests;
import in.stallats.ecuris.CartActivity;
import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.BagdeDrawable;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.Session;

public class PackageDescActivity extends AppCompatActivity implements View.OnClickListener {

    String package_id, package_name;
    TextView package_name_text, actual_package_price_text, final_package_price_text, package_desc_text, individual_tests_count_text;
    private Session session;
    private ListView listView;
    private PackageTestListAdapter adapter;
    private List<Tests> testList;
    String cart_cnt_num;
    String cart_price, cart_product_id, cart_product_name, cart_quantity, cart_data, cart_module, cart_vendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_desc);

        session = new Session(this);

        HashMap<String, String> cart_cnt = session.getCartCount();
        cart_cnt_num = cart_cnt.get("cart_count");

        findViewById(R.id.package_add_to_cart).setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            package_id = ((MyApplication) getApplicationContext()).getPackage_slug();
            package_name = ((MyApplication) getApplicationContext()).getPackage_name();
            ((MyApplication) getApplicationContext()).setPackage_slug(package_id);
            ((MyApplication) getApplicationContext()).setPackage_name(package_name);
        } else {
            package_name = extras.getString("package_name").toUpperCase();
            package_id = extras.getString("slug");
            ((MyApplication) getApplicationContext()).setPackage_slug(package_id);
            ((MyApplication) getApplicationContext()).setPackage_name(package_name);
        }

        getSupportActionBar().setTitle("PACKAGE DESCRIPTION");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        package_name_text = (TextView) findViewById(R.id.package_name);
        actual_package_price_text = (TextView) findViewById(R.id.actual_package_price);
        final_package_price_text = (TextView) findViewById(R.id.final_package_price);
        package_desc_text = (TextView) findViewById(R.id.package_desc);
        individual_tests_count_text = (TextView) findViewById(R.id.individual_tests_count);

        Future<JsonObject> jsonObjectFuture = Ion.with(this)
                .load("http://portal.ecuris.in/api/package/" + package_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Sorry, Something went wrong.", Toast.LENGTH_SHORT).show();
                        } else {

                            String x = result.toString();
                            final JSONObject xx;

                            try {
                                xx = new JSONObject(x);
                                JSONObject package_info = xx.getJSONObject("package");

                                cart_price = package_info.getString("final_price");
                                cart_product_id = package_info.getString("id") + '-' + package_info.getString("package_id");
                                cart_product_name = package_info.getString("package_title");
                                cart_quantity = "1";
                                cart_data = "Item: Lab Package";
                                cart_module = "lab-package";
                                cart_vendor_id = package_info.getString("vendors");

                                JSONArray tests_info = xx.getJSONArray("tests");
                                Integer tests_length = tests_info.length();

                                package_name_text.setText(package_info.getString("package_title"));
                                actual_package_price_text.setText("Rs. " + package_info.getString("actual_price"));
                                actual_package_price_text.setPaintFlags(actual_package_price_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                final_package_price_text.setText("Rs. " + package_info.getString("final_price"));
                                String desc = android.text.Html.fromHtml(package_info.getString("description")).toString().trim();
                                package_desc_text.setText(desc);
                                individual_tests_count_text.setText("Test Included: " + tests_length);

                                listView = (ListView) findViewById(R.id.tests_list);
                                testList = new ArrayList<>();

                                for (int i = 0; i < tests_length; i++) {
                                    String imp = null;
                                    imp = tests_info.get(i).toString();
                                    final JSONObject xxx = new JSONObject(imp);

                                    int lab_test_id = Integer.parseInt(xxx.getString("id"));
                                    String lab_test_name = xxx.getString("title");
                                    String lab_test_slug = xxx.getString("slug");

                                    testList.add(new Tests(lab_test_id, lab_test_name, lab_test_slug));
                                }

                                adapter = new PackageTestListAdapter(getApplicationContext(), testList);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent i2 = new Intent(getApplicationContext(), TestDescActivity.class);
                                        i2.putExtra("slug", testList.get(i).getTestslug());
                                        i2.putExtra("test_name", testList.get(i).getTestname());
                                        //startActivity(i2);
                                        //Toast.makeText(getActivity(), testList.get(i).getTestslug(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } catch (JSONException e1) {
                                e1.printStackTrace();
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
            case R.id.nav_cart:
                startActivity(new Intent(this, CartActivity.class));
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

            case R.id.package_add_to_cart:
                if (!session.loggedin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    HashMap<String, String> user = session.getUserDetails();
                    final String id = user.get("id");

                    JsonObject json = new JsonObject();
                    json.addProperty("user_id", id);
                    json.addProperty("price", cart_price);
                    json.addProperty("product_id", cart_product_id);
                    json.addProperty("product_name", cart_product_name);
                    json.addProperty("quantity", cart_quantity);
                    json.addProperty("data", cart_data);
                    json.addProperty("module", cart_module);
                    json.addProperty("vendor_id", cart_vendor_id);

                    Ion.with(getApplicationContext())
                            .load("POST", "http://portal.ecuris.in/api/cart/")
                            .setJsonObjectBody(json)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    if (e != null) {

                                    } else {

                                        if (result.replace("\"", "").equals("Item added")) {
                                            HashMap<String, String> cart_cnt = session.getCartCount();
                                            int cart_cnt_num = Integer.parseInt(cart_cnt.get("cart_count")) + 1;
                                            session.set_CartCount(String.valueOf(cart_cnt_num));
                                        }

                                        Toast.makeText(getApplicationContext(), result.replace("\"", ""), Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            });

                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemCart = menu.findItem(R.id.nav_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, cart_cnt_num);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        searchItem.setVisible(false);

        return true;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BagdeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BagdeDrawable) {
            badge = (BagdeDrawable) reuse;
        } else {
            badge = new BagdeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

}
