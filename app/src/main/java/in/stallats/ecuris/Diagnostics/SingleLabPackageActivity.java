package in.stallats.ecuris.Diagnostics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.stallats.ecuris.Adapters.Packages;
import in.stallats.ecuris.Adapters.PackagesListAdapter;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.MyApplication;

public class SingleLabPackageActivity extends AppCompatActivity {

    private ListView listView;
    private PackagesListAdapter adapter;
    private List<Packages> packagelist;
    String store_name, store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_lab_package);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            store_id = ((MyApplication) getApplicationContext()).getSlug();
            store_name = ((MyApplication) getApplicationContext()).getStore_name();
            ((MyApplication) getApplicationContext()).setLabSlug(store_id);
            ((MyApplication) getApplicationContext()).setStore_name(store_name);
        } else {
            store_name = extras.getString("store_name").toUpperCase();
            store_id = extras.getString("slug");
            ((MyApplication) getApplicationContext()).setLabSlug(store_id);
            ((MyApplication) getApplicationContext()).setStore_name(store_name);
        }

        getSupportActionBar().setTitle(store_name + " - PACKAGES");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Future<JsonObject> jsonObjectFuture = Ion.with(this)
                .load("http://portal.ecuris.in/api/vendor/" + store_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            listView = (ListView) findViewById(R.id.packages_list);
                            packagelist = new ArrayList<>();

                            String x = result.toString();
                            final JSONObject xx;
                            try {
                                xx = new JSONObject(x);
                                JSONArray vendor_tests = xx.getJSONArray("packages");
                                Integer vendor_tests_length = vendor_tests.length();

                                for (int j = 0; j < vendor_tests_length; j++) {
                                    String imp = null;

                                    imp = vendor_tests.get(j).toString();
                                    final JSONObject xxx = new JSONObject(imp);

                                    int lab_package_id = Integer.parseInt(xxx.getString("id"));
                                    String lab_test_name = xxx.getString("package_title");
                                    String lab_test_slug = xxx.getString("slug");
                                    String actualamount = xxx.getString("actual_price");
                                    String finalamount = xxx.getString("final_price");
                                    String str = xxx.getString("individual_tests");

                                    ArrayList aList = new ArrayList(Arrays.asList(str.split(",")));
                                    int testscount = aList.size();

                                    //Toast.makeText(getActivity(), String.valueOf(testscount), Toast.LENGTH_SHORT).show();

                                    packagelist.add(new Packages(lab_package_id, lab_test_name, lab_test_slug, actualamount, finalamount, testscount));
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                            adapter = new PackagesListAdapter(getApplicationContext(), packagelist);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(getApplicationContext(), PackageDescActivity.class);
                                    i2.putExtra("slug", packagelist.get(i).getTestslug());
                                    i2.putExtra("package_name", packagelist.get(i).getTestname());
                                    startActivity(i2);
                                    //Toast.makeText(getActivity(), packagelist.get(i).getTestslug(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

    }
}
