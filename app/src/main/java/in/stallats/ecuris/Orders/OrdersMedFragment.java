package in.stallats.ecuris.Orders;

import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.stallats.ecuris.Adapters.DiagOrders;
import in.stallats.ecuris.Adapters.MedOrders;
import in.stallats.ecuris.Adapters.OrdersDiagListAdapter;
import in.stallats.ecuris.Adapters.OrdersMedListAdapter;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

/**
 * Created by User on 04-Jun-17.
 */

public class OrdersMedFragment extends Fragment {

    private Session session;
    private ListView listView;
    private OrdersMedListAdapter adapter;
    private List<MedOrders> mMedOrdersList;
    private String id;
    Context applicationContext;

    public OrdersMedFragment(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(applicationContext);
        HashMap<String, String> user = session.getUserDetails();
        id = user.get("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_med_orders, container, false);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/orders/medical/" + id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {
                            v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(applicationContext, "Sorry no orders found", Toast.LENGTH_SHORT).show();
                        } else {
                            v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            listView = (ListView) v.findViewById(R.id.orders_list);
                            mMedOrdersList = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);
                                    int o_id = xx.getInt("id");
                                    String o_order_id = xx.getString("order_id");
                                    String o_prescription = xx.getString("prescrition");
                                    String o_order_status = xx.getString("status");
                                    String o_ordered_time = xx.getString("created_time");

                                    final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
                                    final String NEW_FORMAT = "dd-MM-yyyy HH:mm:ss";

                                    String oldDateString = xx.getString("created_time");
                                    String newDateString;

                                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                                    Date d = sdf.parse(oldDateString);
                                    sdf.applyPattern(NEW_FORMAT);
                                    newDateString = sdf.format(d);
                                    String billed_time = newDateString;

                                    mMedOrdersList.add(new MedOrders(o_id, o_order_id, o_prescription, o_order_status, billed_time));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            adapter = new OrdersMedListAdapter(applicationContext, mMedOrdersList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(applicationContext, OrderMedDescActivity.class);
                                    i2.putExtra("order_id", mMedOrdersList.get(i).getOrder_id());
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
