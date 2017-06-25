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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.stallats.ecuris.Adapters.Labs;
import in.stallats.ecuris.Adapters.LabsListAdapter;
import in.stallats.ecuris.R;

/**
 * Created by User on 04-Jun-17.
 */

public class LabsFrament extends Fragment {

    private ListView listView;
    private LabsListAdapter adapter;
    private List<Labs> mLabsList;

    public LabsFrament() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_labs_frament, container, false);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/vendors_all/")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            listView = (ListView) v.findViewById(R.id.offers_list_apperals);
                            mLabsList = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    JSONObject vendor_info = xx.getJSONObject("details");
                                    JSONArray acc_info = xx.getJSONArray("accr");

                                    int vendor_id = Integer.parseInt(vendor_info.getString("vendor_id"));
                                    String image = vendor_info.getString("image");
                                    String vendor_name = vendor_info.getString("store_name");
                                    String store_slug = vendor_info.getString("store_slug");

                                    mLabsList.add(new Labs(vendor_id, "http://portal.ecuris.in/assets/uploads/" + image, vendor_name, acc_info, store_slug));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }


                            adapter = new LabsListAdapter(getActivity(), mLabsList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(getActivity(), LabProfileActivity.class);
                                    i2.putExtra("slug", mLabsList.get(i).getSlug());
                                    i2.putExtra("store_name", mLabsList.get(i).getStore_name());
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
