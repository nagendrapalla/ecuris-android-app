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
import java.util.List;

import in.stallats.ecuris.Adapters.TestListAdapter;
import in.stallats.ecuris.Adapters.Tests;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.MyApplication;

public class SingleLabTestsActivity extends AppCompatActivity {

    private ListView listView;
    private TestListAdapter adapter;
    private List<Tests> testList;
    String store_name, store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_lab_tests);

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

        getSupportActionBar().setTitle(store_name + " - TESTS");
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
                            listView = (ListView) findViewById(R.id.tests_list);
                            testList = new ArrayList<>();

                            String x = result.toString();
                            final JSONObject xx;
                            try {
                                xx = new JSONObject(x);
                                JSONArray vendor_tests = xx.getJSONArray("tests");
                                Integer vendor_tests_length = vendor_tests.length();

                                for (int j = 0; j < vendor_tests_length; j++) {
                                    String imp = null;

                                    imp = vendor_tests.get(j).toString();
                                    final JSONObject xxx = new JSONObject(imp);

                                    int lab_test_id = Integer.parseInt(xxx.getString("lab_test_id"));
                                    String lab_test_name = xxx.getString("title");
                                    String lab_test_slug = xxx.getString("test_slug");
                                    String actualamount = xxx.getString("actual_price");
                                    String finalamount = xxx.getString("final_price");
                                    String lab_name = xxx.getString("store_name");
                                    testList.add(new Tests(lab_test_id, lab_test_name, lab_test_slug, actualamount, finalamount, lab_name));
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                            adapter = new TestListAdapter(getApplicationContext(), testList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(getApplicationContext(), TestDescActivity.class);
                                    i2.putExtra("slug", testList.get(i).getTestslug());
                                    i2.putExtra("test_name", testList.get(i).getTestname());
                                    startActivity(i2);
                                }
                            });

                        }
                    }
                });

    }
}
