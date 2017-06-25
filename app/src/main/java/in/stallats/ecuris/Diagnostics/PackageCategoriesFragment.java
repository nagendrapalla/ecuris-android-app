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

import in.stallats.ecuris.Adapters.Categories;
import in.stallats.ecuris.Adapters.PackageCategoriesListAdapter;
import in.stallats.ecuris.R;

/**
 * Created by User on 04-Jun-17.
 */

public class PackageCategoriesFragment extends Fragment {

    private ListView listView;
    private PackageCategoriesListAdapter adapter;
    private List<Categories> catlist;

    public PackageCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_package_categories, container, false);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/pcategories/")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            v.findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
                            v.findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                            listView = (ListView) v.findViewById(R.id.cat_list);
                            catlist = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    int cat_id = Integer.parseInt(xx.getString("id"));
                                    String p_cat = xx.getString("package_category");
                                    String p_slug = xx.getString("slug");
                                    String p_image = "http://portal.ecuris.in/assets/uploads/package_categories/" + xx.getString("package_image");

                                    catlist.add(new Categories(cat_id, p_cat, p_slug, p_image));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            adapter = new PackageCategoriesListAdapter(getActivity(), catlist);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(getActivity(), PackagesActivity.class);
                                    i2.putExtra("slug", catlist.get(i).getPackage_slug());
                                    i2.putExtra("cat_name", catlist.get(i).getPackage_category());
                                    startActivity(i2);
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
