package in.stallats.ecuris.Orders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.MyApplication;
import in.stallats.ecuris.Supporting.Session;

public class OrderMedDescActivity extends AppCompatActivity {

    private Session session;
    ConnectionDetector cd;
    String order_id;
    String id;

    LinearLayout ll, l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_med_desc);

        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }


        HashMap<String, String> user = session.getUserDetails();
        id = user.get("id");

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            order_id = ((MyApplication) getApplicationContext()).getOrder_id();
            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
        } else {
            order_id = extras.getString("order_id");
            ((MyApplication) getApplicationContext()).setOrder_id(order_id);
        }

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/orders/medical/" + id + "/" + order_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Sorry no orders found", Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    TextView med_desc_order_id = (TextView) findViewById(R.id.med_desc_order_id);
                                    TextView med_desc_order_presc = (TextView) findViewById(R.id.med_desc_order_presc);
                                    TextView med_desc_order_option = (TextView) findViewById(R.id.med_desc_order_option);
                                    TextView med_desc_order_billed_time = (TextView) findViewById(R.id.med_desc_order_billed_time);
                                    TextView med_desc_order_status = (TextView) findViewById(R.id.med_desc_order_status);

                                    TextView med_desc_order_selected_address = (TextView) findViewById(R.id.med_desc_order_selected_address);
                                    TextView med_desc_order_contact = (TextView) findViewById(R.id.med_desc_order_contact);

                                    TextView med_desc_order_per_req_text = (TextView) findViewById(R.id.med_desc_order_per_req_text);

                                    CardView med_desc_order_presc_status = (CardView) findViewById(R.id.med_desc_order_presc_status);

                                    int id = xx.getInt("id");
                                    String user_id = xx.getString("user_id");
                                    String corder_id = xx.getString("order_id");
                                    String prescrition = xx.getString("prescrition");
                                    String pres_mode = xx.getString("pres_mode");
                                    String status = xx.getString("status");
                                    String created_time = xx.getString("created_time");
                                    String manual_requirements = xx.getString("manual_requirements");
                                    String contact_address = xx.getString("contact_address");
                                    String contact_person = xx.getString("contact_person");
                                    String contact_number = xx.getString("contact_number");

                                    med_desc_order_id.setText(corder_id);
                                    med_desc_order_option.setText(pres_mode);
                                    med_desc_order_selected_address.setText(contact_address);
                                    med_desc_order_contact.setText(contact_person + " - " + contact_number);
                                    med_desc_order_per_req_text.setText(manual_requirements);

                                    if(Integer.parseInt(status) == 0){
                                        med_desc_order_status.setText("Vendor Approval Pending");
                                        med_desc_order_status.setTextColor(Color.parseColor("#D80000"));
                                    }else if(Integer.parseInt(status) == 1){
                                        med_desc_order_status.setText("Order In - Progress");
                                        med_desc_order_status.setTextColor(Color.parseColor("#FFC107"));
                                    }else{
                                        med_desc_order_status.setText("Order Delivered");
                                        med_desc_order_status.setTextColor(Color.parseColor("#4CAF50"));
                                    }

                                    final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
                                    final String NEW_FORMAT = "dd-MM-yyyy HH:mm:ss";

                                    String oldDateString = created_time;
                                    String newDateString;
                                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                                    Date d = sdf.parse(oldDateString);
                                    sdf.applyPattern(NEW_FORMAT);
                                    newDateString = sdf.format(d);
                                    String billed_time = newDateString;
                                    med_desc_order_billed_time.setText(billed_time);

                                    if(Integer.parseInt(prescrition) == 0){
                                        med_desc_order_presc.setText("No");
                                        med_desc_order_presc_status.setVisibility(View.GONE);
                                    }else{
                                        med_desc_order_presc.setText("Yes");
                                        med_desc_order_presc_status.setVisibility(View.VISIBLE);

                                        final LinearLayout linearMedDescMain = (LinearLayout) findViewById(R.id.linearMedDescMain);

                                        Future<JsonArray> get2 = Ion.with(getApplicationContext())
                                                .load("http://portal.ecuris.in/api/orders/presc/" + corder_id)
                                                .asJsonArray()
                                                .setCallback(new FutureCallback<JsonArray>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonArray result2) {
                                                        if (e != null) {

                                                        } else {
                                                            for (int i = 0; i < result2.size(); i++) {
                                                                String tx = result2.get(i).toString();
                                                                try {
                                                                    final JSONObject txx = new JSONObject(tx);
                                                                    String photopath = "http://portal.ecuris.in/assets/uploads/med_presc/" + txx.getString("path");
                                                                    ImageView imageView = new ImageView(getApplicationContext());
                                                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                                    imageView.setLayoutParams(layoutParams);
                                                                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                                                    imageView.setPadding(0, 0, 20, 0);
                                                                    imageView.setAdjustViewBounds(true);
                                                                    linearMedDescMain.addView(imageView);
                                                                    Picasso.with(getApplicationContext()).load(photopath).into(imageView);
                                                                    //new DownloadImageTask(imageView).execute(photopath);
                                                                } catch (JSONException e1) {
                                                                    e1.printStackTrace();
                                                                }


                                                            }
                                                        }
                                                    }
                                                });


                                    }


                                }catch (JSONException e1){
                                    e1.printStackTrace();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
