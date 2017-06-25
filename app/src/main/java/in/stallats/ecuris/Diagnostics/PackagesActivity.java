package in.stallats.ecuris.Diagnostics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.stallats.ecuris.Adapters.Packages;
import in.stallats.ecuris.Adapters.PackagesListAdapter;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.MyApplication;

public class PackagesActivity extends AppCompatActivity {

    private ListView listView;
    private PackagesListAdapter adapter;
    private List<Packages> packagelist;
    String cat_id, cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            cat_id = ((MyApplication) getApplicationContext()).getCat_id();
            cat_name = ((MyApplication) getApplicationContext()).getCat_name();
            ((MyApplication) getApplicationContext()).setCat_id(cat_id);
            ((MyApplication) getApplicationContext()).setCat_name(cat_name);
        } else {
            cat_name = extras.getString("cat_name").toUpperCase();
            cat_id = extras.getString("slug");
            ((MyApplication) getApplicationContext()).setCat_id(cat_id);
            ((MyApplication) getApplicationContext()).setCat_name(cat_name);
        }

        getSupportActionBar().setTitle(cat_name + " - PACKAGES");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/packages_all/" + cat_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            listView = (ListView) findViewById(R.id.packages_list);
                            packagelist = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);
                                    int lab_package_id = Integer.parseInt(xx.getString("id"));
                                    String lab_test_name = xx.getString("package_title");
                                    String lab_test_slug = xx.getString("slug");
                                    String actualamount = xx.getString("actual_price");
                                    String finalamount = xx.getString("final_price");
                                    String str = xx.getString("individual_tests");

                                    ArrayList aList = new ArrayList(Arrays.asList(str.split(",")));
                                    int testscount = aList.size();

                                    packagelist.add(new Packages(lab_package_id, lab_test_name, lab_test_slug, actualamount, finalamount, testscount));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
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
