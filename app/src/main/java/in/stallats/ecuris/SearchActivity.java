package in.stallats.ecuris;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.stallats.ecuris.Adapters.Packages;
import in.stallats.ecuris.Adapters.PackagesListAdapter;
import in.stallats.ecuris.Adapters.Search;
import in.stallats.ecuris.Adapters.SearchListAdapter;
import in.stallats.ecuris.Diagnostics.PackageDescActivity;
import in.stallats.ecuris.Diagnostics.TestDescActivity;
import in.stallats.ecuris.Supporting.Session;

public class SearchActivity extends AppCompatActivity {

    private Session session;
    private String querry;
    private ListView listView;
    private List<Search> searchList;
    private SearchListAdapter adapter;
    private int count = 0;
    private LinearLayout no_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        session = new Session(getApplicationContext());
        querry = getIntent().getStringExtra("qry");

        getSupportActionBar().setTitle("QUERY :- " + querry);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.search_list);
        searchList = new ArrayList<>();

        try {
            JsonArray test_json = Ion.with(this).load("http://portal.ecuris.in/api/tests_all/").asJsonArray().get();
            for (int i = 0; i < test_json.size(); i++) {
                String x = test_json.get(i).toString();
                try {
                    final JSONObject xx = new JSONObject(x);
                    if(xx.getString("title").toLowerCase().contains(querry.toLowerCase())){
                        searchList.add(new Search(Integer.parseInt(xx.getString("lab_test_id")), xx.getString("title"), xx.getString("test_slug"), "Lat Test"));
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            JsonArray package_json = Ion.with(this).load("http://portal.ecuris.in/api/packages/").asJsonArray().get();
            for (int i = 0; i < package_json.size(); i++) {
                String x = package_json.get(i).toString();
                try {
                    final JSONObject xx = new JSONObject(x);
                    if(xx.getString("package_title").toLowerCase().contains(querry.toLowerCase())) {
                        searchList.add(new Search(Integer.parseInt(xx.getString("id")), xx.getString("package_title"), xx.getString("slug"), "Lab Package"));
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        adapter = new SearchListAdapter(getApplicationContext(), searchList);
        if(count == 0){
            no_result = (LinearLayout) findViewById(R.id.no_result);
            no_result.setVisibility(View.VISIBLE);
        }else{
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(searchList.get(i).getPage().equals("Lat Test")){
                        Intent i2 = new Intent(getApplicationContext(), TestDescActivity.class);
                        i2.putExtra("slug", searchList.get(i).getTestslug());
                        i2.putExtra("test_name", searchList.get(i).getTestname());
                        startActivity(i2);
                    }else{
                        Intent i2 = new Intent(getApplicationContext(), PackageDescActivity.class);
                        i2.putExtra("slug", searchList.get(i).getTestslug());
                        i2.putExtra("package_name", searchList.get(i).getTestname());
                        startActivity(i2);
                    }
                }
            });
        }



    }
}
