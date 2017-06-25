package in.stallats.ecuris.Orders;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

/**
 * Created by User on 04-Jun-17.
 */

public class OrdersDiagFragment extends Fragment {
    private Session session;
    private ListView listView;
    private OrdersDiagListAdapter adapter;
    private List<DiagOrders> mDiagOrdersList;
    private String id;
    Context applicationContext;

    public OrdersDiagFragment(Context applicationContext) {
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
        final View v = inflater.inflate(R.layout.fragment_diag_orders, container, false);

        Future<JsonArray> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/orderhistory/" + id)
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
                            mDiagOrdersList = new ArrayList<>();

                            for (int i = 0; i < result.size(); i++) {
                                String x = result.get(i).toString();
                                try {
                                    final JSONObject xx = new JSONObject(x);

                                    JSONObject order_info = xx.getJSONObject("order");

                                    JSONArray item_info = xx.getJSONArray("item");
                                    Integer items_length = item_info.length();

                                    int o_id = order_info.getInt("id");
                                    String order_id = order_info.getString("order_id");
                                    String payment_status = order_info.getString("payment_status");
                                    String total_amount = order_info.getString("total_amount");


                                    final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
                                    final String NEW_FORMAT = "dd-MM-yyyy HH:mm:ss";

                                    String oldDateString = order_info.getString("billed_time");
                                    String newDateString;

                                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                                    Date d = sdf.parse(oldDateString);
                                    sdf.applyPattern(NEW_FORMAT);
                                    newDateString = sdf.format(d);
                                    String billed_time = newDateString;

                                    mDiagOrdersList.add(new DiagOrders(o_id, order_id, total_amount, payment_status, billed_time, items_length));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            adapter = new OrdersDiagListAdapter(applicationContext, mDiagOrdersList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent i2 = new Intent(applicationContext, OrderDiagDescActivity.class);
                                    i2.putExtra("order_id", mDiagOrdersList.get(i).getOrder_id());
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
