package in.stallats.ecuris;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.util.HashMap;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.Session;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;
    ConnectionDetector cd;
    LinearLayout main;
    float tot_price = 0, shipping = 0, tax_rate = 10;
    int tot_items = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        Button btn = (Button) findViewById(R.id.proceed_cart);
        btn.setOnClickListener(this);

        HashMap<String, String> user = session.getUserDetails();
        final String id = user.get("id");

        findViewById(R.id.cart_list).setVisibility(View.GONE);
        findViewById(R.id.nocart).setVisibility(View.GONE);

        main = (LinearLayout) findViewById(R.id.parent_cart);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/cart/" + id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            //Toast.makeText(getApplicationContext(), "Sorry no cart items were found", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            findViewById(R.id.nocart).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            findViewById(R.id.cart_list).setVisibility(View.VISIBLE);
                            final TextView[] cart_product_name = new TextView[result.size()];
                            final TextView[] cart_product_type = new TextView[result.size()];
                            final TextView[] cart_product_final_price = new TextView[result.size()];
                            final TextView[] cart_product_quantity = new TextView[result.size()];

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View view = inflater.inflate(R.layout.cart_layout, main, false);
                                try {
                                    final JSONObject xx = new JSONObject(x);
                                    LinearLayout minus = (LinearLayout) view.findViewById(R.id.qty_minus);
                                    LinearLayout plus = (LinearLayout) view.findViewById(R.id.qty_plus);
                                    LinearLayout remove = (LinearLayout) view.findViewById(R.id.cart_remove);

                                    cart_product_name[i] = (TextView) view.findViewById(R.id.cart_product_name);
                                    cart_product_type[i] = (TextView) view.findViewById(R.id.cart_product_type);
                                    cart_product_final_price[i] = (TextView) view.findViewById(R.id.cart_product_final_price);
                                    cart_product_quantity[i] = (TextView) view.findViewById(R.id.cart_product_quantity);

                                    final String product_id = xx.getString("product_id");
                                    final String product_name = xx.getString("product_name");
                                    final float price = Float.parseFloat(xx.getString("price"));
                                    final int qnty = Integer.parseInt(xx.getString("quantity"));

                                    tot_price = tot_price + (price * qnty);
                                    tot_items = tot_items + qnty;

                                    cart_product_name[i].setText(xx.getString("product_name"));
                                    cart_product_type[i].setText(xx.getString("module").toUpperCase());
                                    cart_product_final_price[i].setText("\u20B9" + (price * qnty));
                                    cart_product_quantity[i].setText(xx.getString("quantity"));


                                    final int finalI = i;
                                    minus.setOnClickListener(new LinearLayout.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int qt = Integer.parseInt((String) cart_product_quantity[finalI].getText());
                                            String rmpr = (String) cart_product_final_price[finalI].getText();
                                            rmpr = rmpr.substring(1);
                                            float pr = Float.parseFloat(rmpr);

                                            if (qt == 1) {
                                                Toast.makeText(getApplicationContext(), "Quantity cannot decreased to below 1", Toast.LENGTH_SHORT).show();
                                            } else {
                                                int val = qt - 1;
                                                float fi = pr - price;
                                                cart_product_quantity[finalI].setText(String.valueOf(val));
                                                cart_product_final_price[finalI].setText("\u20B9" + String.valueOf(fi));

                                                JsonObject json = new JsonObject();
                                                json.addProperty("quantity", String.valueOf(val));

                                                Ion.with(getApplicationContext())
                                                        .load("POST", "http://portal.ecuris.in/api/cart/" + product_id + "/" + id)
                                                        .setJsonObjectBody(json)
                                                        .asString()
                                                        .setCallback(new FutureCallback<String>() {
                                                            @Override
                                                            public void onCompleted(Exception e, String result) {
                                                                if (e != null) {

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), product_name + " quantity decreased", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });

                                    plus.setOnClickListener(new LinearLayout.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int qt = Integer.parseInt((String) cart_product_quantity[finalI].getText());
                                            String rmpr = (String) cart_product_final_price[finalI].getText();
                                            rmpr = rmpr.substring(1);
                                            float pr = Float.parseFloat(rmpr);

                                            if (qt >= 10) {
                                                Toast.makeText(getApplicationContext(), "Quantity cannot increased to above 10", Toast.LENGTH_SHORT).show();
                                            } else {
                                                int val = qt + 1;
                                                float fi = pr + price;
                                                cart_product_quantity[finalI].setText(String.valueOf(val));
                                                cart_product_final_price[finalI].setText("\u20B9" + String.valueOf(fi));

                                                JsonObject json = new JsonObject();
                                                json.addProperty("quantity", String.valueOf(val));

                                                Ion.with(getApplicationContext())
                                                        .load("POST", "http://portal.ecuris.in/api/cart/" + product_id + "/" + id)
                                                        .setJsonObjectBody(json)
                                                        .asString()
                                                        .setCallback(new FutureCallback<String>() {
                                                            @Override
                                                            public void onCompleted(Exception e, String result) {
                                                                if (e != null) {

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), product_name + " quantity increased", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });

                                    remove.setOnClickListener(new LinearLayout.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Ion.with(getApplicationContext())
                                                    .load("DELETE", "http://portal.ecuris.in/api/cart/" + product_id + "/" + id)
                                                    .asString()
                                                    .setCallback(new FutureCallback<String>() {
                                                        @Override
                                                        public void onCompleted(Exception e, String result) {
                                                            if (e != null) {

                                                            } else {

                                                                HashMap<String, String> cart_cnt = session.getCartCount();
                                                                int cart_cnt_num = Integer.parseInt(cart_cnt.get("cart_count")) - 1;

                                                                session.set_CartCount(String.valueOf(cart_cnt_num));

                                                                main.removeView(view);
                                                                Toast.makeText(getApplicationContext(), product_name + " removed from cart", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(getIntent());
                                                            }
                                                        }
                                                    });
                                        }
                                    });


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                main.addView(view);
                            }

                            tax_rate = (float) round(((tot_price / 100) * 13.9), 2);
                            shipping = 10;
                            float tot_final_price = tax_rate + shipping + tot_price;

                            TextView pri = (TextView) findViewById(R.id.cart_price);
                            pri.setText("\u20B9" + String.valueOf(tot_price));

                            TextView tax = (TextView) findViewById(R.id.cart_tax);
                            tax.setText("\u20B9" + String.valueOf(tax_rate));

                            TextView ship = (TextView) findViewById(R.id.cart_shipping);
                            ship.setText("\u20B9" + String.valueOf(shipping));

                            TextView item_c = (TextView) findViewById(R.id.cart_items_count);
                            item_c.setText(String.valueOf(tot_items));

                            TextView tot_c = (TextView) findViewById(R.id.cart_total_cost);
                            tot_c.setText("\u20B9" + String.valueOf(tot_final_price));

                        }
                    }
                });

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
        switch (view.getId()){
            case R.id.proceed_cart:
                startActivity(new Intent(this, BillingActivity.class));
                break;
            default:
        }
    }
}
