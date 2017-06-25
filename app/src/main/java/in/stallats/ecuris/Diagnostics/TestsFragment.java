package in.stallats.ecuris.Diagnostics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import in.stallats.ecuris.Adapters.TestListAdapter;
import in.stallats.ecuris.Adapters.Tests;
import in.stallats.ecuris.R;

/**
 * Created by User on 04-Jun-17.
 */

public class TestsFragment extends Fragment {

    private ListView listView;
    private TestListAdapter adapter;
    private List<Tests> testList;

    public TestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tests, container, false);


        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/tests_all/")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            v.findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            v.findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            listView = (ListView) v.findViewById(R.id.tests_list);
                            testList = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    //Toast.makeText(getActivity(), xx.getString("title"), Toast.LENGTH_SHORT).show();

                                    int lab_test_id = Integer.parseInt(xx.getString("lab_test_id"));
                                    String lab_test_name = xx.getString("title");
                                    String lab_test_slug = xx.getString("test_slug");
                                    String actualamount = xx.getString("actual_price");
                                    String finalamount = xx.getString("final_price");
                                    String lab_name = xx.getString("store_name");


                                    testList.add(new Tests(lab_test_id, lab_test_name, lab_test_slug, actualamount, finalamount, lab_name));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }


                            adapter = new TestListAdapter(getActivity(), testList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(getActivity(), TestDescActivity.class);
                                    i2.putExtra("slug", testList.get(i).getTestslug());
                                    i2.putExtra("test_name", testList.get(i).getTestname());
                                    startActivity(i2);
                                    //Toast.makeText(getActivity(), testList.get(i).getTestslug(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
