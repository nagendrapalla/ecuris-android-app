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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.stallats.ecuris.CartActivity;
import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.BagdeDrawable;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.Session;

public class TestDescActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    String test_id, test_name;
    String cart_cnt_num;
    TextView test_name_text, actual_test_price_text, final_test_price_text, test_desc_text, test_instructions_text, test_results_text, test_provided_by_text;
    String cart_price, cart_product_id, cart_product_name, cart_quantity, cart_data, cart_module, cart_vendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_desc);

        session = new Session(this);

        HashMap<String, String> cart_cnt = session.getCartCount();
        cart_cnt_num = cart_cnt.get("cart_count");

        findViewById(R.id.test_add_to_cart).setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            test_id = ((MyApplication) getApplicationContext()).getTest_slug();
            test_name = ((MyApplication) getApplicationContext()).getTest_name();
            ((MyApplication) getApplicationContext()).setTest_slug(test_id);
            ((MyApplication) getApplicationContext()).setTest_name(test_name);
        } else {
            test_name = extras.getString("test_name").toUpperCase();
            test_id = extras.getString("slug");
            ((MyApplication) getApplicationContext()).setTest_slug(test_id);
            ((MyApplication) getApplicationContext()).setTest_name(test_name);
        }

        getSupportActionBar().setTitle("TEST DESCRIPTION");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        test_name_text = (TextView) findViewById(R.id.test_name);
        actual_test_price_text = (TextView) findViewById(R.id.actual_test_price);
        final_test_price_text = (TextView) findViewById(R.id.final_test_price);
        test_desc_text = (TextView) findViewById(R.id.test_desc);
        test_instructions_text = (TextView) findViewById(R.id.test_instructions);
        test_results_text = (TextView) findViewById(R.id.test_results);
        test_provided_by_text = (TextView) findViewById(R.id.test_provided_by);

        Future<JsonObject> jsonObjectFuture = Ion.with(this)
                .load("http://portal.ecuris.in/api/test/" + test_id)
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
                                test_name_text.setText(xx.getString("title"));
                                actual_test_price_text.setText("Rs. " + xx.getString("actual_price"));
                                actual_test_price_text.setPaintFlags(actual_test_price_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                final_test_price_text.setText("Rs. " + xx.getString("final_price"));

                                String desc = android.text.Html.fromHtml(xx.getString("description")).toString().trim();
                                String instr = android.text.Html.fromHtml(xx.getString("test_instructions")).toString().trim();
                                String resu = android.text.Html.fromHtml(xx.getString("test_results")).toString().trim();
                                //String dummy_text = " Cumn sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vestibulum id ligula porta felis euismod semper. Maecenas sed diam eget risus varius blandit sit amet non magna. Donec sed odio dui. nascetur ridiculus mus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vestibulum id ligula porta felis euismod semper. Maecenas sed diam eget risus varius blandit sit amet non magna. Donec sed odio dui.";

                                test_desc_text.setText(desc);
                                test_instructions_text.setText(instr);
                                test_results_text.setText(resu);

                                test_provided_by_text.setText(xx.getString("store_name"));

                                cart_price = xx.getString("final_price");
                                cart_product_id = xx.getString("lab_test_id")+'-'+xx.getString("unique_id");
                                cart_product_name = xx.getString("title");
                                cart_quantity = "1";
                                cart_data = "Item: Lab Individual Test";
                                cart_module = "lab-test";
                                cart_vendor_id = xx.getString("vendor_id");


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.test_add_to_cart:
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
