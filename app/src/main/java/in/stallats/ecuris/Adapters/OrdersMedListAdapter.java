package in.stallats.ecuris.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.stallats.ecuris.R;

/**
 * Created by User on 04-Jun-17.
 */

public class OrdersMedListAdapter extends BaseAdapter {

    private Context context;
    private List<MedOrders> ordersList;

    public OrdersMedListAdapter(Context context, List<MedOrders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int i) {
        return ordersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_med_orders_list, null);

        TextView order_id_text = (TextView) v.findViewById(R.id.med_order_id);
        order_id_text.setText(ordersList.get(i).getOrder_id());

        TextView order_amount_text = (TextView) v.findViewById(R.id.med_order_presc);
        if(Integer.parseInt(ordersList.get(i).getPrescription()) == 0){
            order_amount_text.setText("No");
        }else{
            order_amount_text.setText("Yes");
        }

        TextView order_payment_status_text = (TextView) v.findViewById(R.id.med_order_status);
        if (Integer.parseInt(ordersList.get(i).getOrder_status()) == 0) {
            order_payment_status_text.setText(" Vendor needs to approve");
            order_payment_status_text.setTextColor(Color.parseColor("#D80000"));
        } else if (Integer.parseInt(ordersList.get(i).getOrder_status()) == 1) {
            order_payment_status_text.setText(" Order In - Progress");
            order_payment_status_text.setTextColor(Color.parseColor("#FFC107"));
        } else {
            order_payment_status_text.setText(" Order Delivered");
            order_payment_status_text.setTextColor(Color.parseColor("#4CAF50"));
        }

        TextView order_billed_time_text = (TextView) v.findViewById(R.id.med_order_billed_time);
        order_billed_time_text.setText(ordersList.get(i).getOrdered_time());

        v.setTag(ordersList.get(i).getId());

        return v;
    }

}
