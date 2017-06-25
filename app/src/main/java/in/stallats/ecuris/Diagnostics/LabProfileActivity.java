package in.stallats.ecuris.Diagnostics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.CustomVolleyRequestQueue;
import in.stallats.ecuris.Supporting.MyApplication;

public class LabProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String store_name, store_id;
    private ImageLoader mImageLoader;
    String image1 = "", image2 = "", image3 = "", image4 = "", image5 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_profile);

        findViewById(R.id.single_lab_tests).setOnClickListener(this);
        findViewById(R.id.single_lab_packages).setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            store_id = ((MyApplication) getApplicationContext()).getSlug();
            store_name = ((MyApplication) getApplicationContext()).getStore_name();
            ((MyApplication) getApplicationContext()).setLabSlug(store_id);
            ((MyApplication) getApplicationContext()).setStore_name(store_name);
        }else{
            store_name = extras.getString("store_name").toUpperCase();
            store_id = extras.getString("slug");
            ((MyApplication) getApplicationContext()).setLabSlug(store_id);
            ((MyApplication) getApplicationContext()).setStore_name(store_name);
        }

        getSupportActionBar().setTitle(store_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Future<JsonObject> jsonObjectFuture = Ion.with(this)
                .load("http://portal.ecuris.in/api/vendor/" + store_id)
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
                                JSONObject vendor_info = xx.getJSONObject("details");

                                String vendor_name = vendor_info.getString("store_name");
                                String store_slug = vendor_info.getString("store_slug");
                                String store_state = vendor_info.getString("store_state");
                                String store_city = vendor_info.getString("store_city");
                                String store_area = vendor_info.getString("store_area");
                                String store_pincode = vendor_info.getString("store_pincode");
                                String image = "http://portal.ecuris.in/assets/uploads/" + vendor_info.getString("image");

                                mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();

                                NetworkImageView niv = (NetworkImageView) findViewById(R.id.lab_image);
                                mImageLoader.get(image, ImageLoader.getImageListener(niv, R.drawable.loader, R.mipmap.ic_launcher));
                                niv.setImageUrl(image, mImageLoader);

                                TextView store_name_text = (TextView) findViewById(R.id.vendor_name);
                                TextView store_location = (TextView) findViewById(R.id.vendor_location);
                                TextView labprofiletext = (TextView) findViewById(R.id.labprofiletext);

                                store_name_text.setText(vendor_name);
                                store_location.setText(store_area + ", " + store_city + ", " + store_state + ", " + store_pincode);
                                labprofiletext.setText("Cumn sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vestibulum id ligula porta felis euismod semper. Maecenas sed diam eget risus varius blandit sit amet non magna. Donec sed odio dui. nascetur ridiculus mus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vestibulum id ligula porta felis euismod semper. Maecenas sed diam eget risus varius blandit sit amet non magna. Donec sed odio dui");

                                JSONArray acc_info = xx.getJSONArray("accre");
                                Integer accr_length = acc_info.length();

                                for (int j = 0; j < accr_length; j++) {
                                    String imp = null;

                                    imp = acc_info.get(j).toString();
                                    final JSONObject xxx = new JSONObject(imp);
                                    if (j == 0) {
                                        image1 = "http://portal.ecuris.in/assets/uploads/accr/" + xxx.getString("logo");
                                        NetworkImageView niv1 = (NetworkImageView) findViewById(R.id.acc_profile_image_1);
                                        mImageLoader.get(image1, ImageLoader.getImageListener(niv1, R.drawable.loader, R.mipmap.ic_launcher));
                                        niv1.setImageUrl(image1, mImageLoader);
                                    } else if (j == 1) {
                                        image2 = "http://portal.ecuris.in/assets/uploads/accr/" + xxx.getString("logo");
                                        NetworkImageView niv2 = (NetworkImageView) findViewById(R.id.acc_profile_image_2);
                                        mImageLoader.get(image2, ImageLoader.getImageListener(niv2, R.drawable.loader, R.mipmap.ic_launcher));
                                        niv2.setImageUrl(image2, mImageLoader);
                                    } else if (j == 2) {
                                        image3 = "http://portal.ecuris.in/assets/uploads/accr/" + xxx.getString("logo");
                                        NetworkImageView niv3 = (NetworkImageView) findViewById(R.id.acc_profile_image_3);
                                        mImageLoader.get(image3, ImageLoader.getImageListener(niv3, R.drawable.loader, R.mipmap.ic_launcher));
                                        niv3.setImageUrl(image3, mImageLoader);
                                    } else if (j == 3) {
                                        image4 = "http://portal.ecuris.in/assets/uploads/accr/" + xxx.getString("logo");
                                        NetworkImageView niv4 = (NetworkImageView) findViewById(R.id.acc_profile_image_4);
                                        mImageLoader.get(image4, ImageLoader.getImageListener(niv4, R.drawable.loader, R.mipmap.ic_launcher));
                                        niv4.setImageUrl(image4, mImageLoader);
                                    } else if (j == 4) {
                                        image5 = "http://portal.ecuris.in/assets/uploads/accr/" + xxx.getString("logo");
                                        NetworkImageView niv5 = (NetworkImageView) findViewById(R.id.acc_profile_image_5);
                                        mImageLoader.get(image5, ImageLoader.getImageListener(niv5, R.drawable.loader, R.mipmap.ic_launcher));
                                        niv5.setImageUrl(image5, mImageLoader);
                                    }
                                }

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

            case R.id.single_lab_tests:
                Intent i2 = new Intent(getApplicationContext(), SingleLabTestsActivity.class);
                i2.putExtra("slug", store_id);
                i2.putExtra("store_name", store_name);
                startActivity(i2);
                break;

            case R.id.single_lab_packages:
                Intent i3 = new Intent(getApplicationContext(), SingleLabPackageActivity.class);
                i3.putExtra("slug", store_id);
                i3.putExtra("store_name", store_name);
                startActivity(i3);
                break;

            default:
        }
    }
}
