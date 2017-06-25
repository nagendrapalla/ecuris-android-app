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

public class OrdersDiagListAdapter extends BaseAdapter {

    private Context context;
    private List<DiagOrders> ordersList;

    public OrdersDiagListAdapter(Context context, List<DiagOrders> ordersList) {
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
        View v = View.inflate(context, R.layout.item_diag_orders_list, null);
        TextView order_id_text = (TextView) v.findViewById(R.id.order_id);
        order_id_text.setText(ordersList.get(i).getOrder_id());

        TextView order_amount_text = (TextView) v.findViewById(R.id.order_amount);
        order_amount_text.setText("Rs. " + ordersList.get(i).getOrderamount());

        TextView order_items_count = (TextView) v.findViewById(R.id.order_items_count);
        order_items_count.setText("(" + ordersList.get(i).getItems_count() + " Item/s)");

        TextView order_payment_status_text = (TextView) v.findViewById(R.id.order_payment_status);
        if (Integer.parseInt(ordersList.get(i).getPayment_status()) == 1) {
            order_payment_status_text.setText("PAID");
            order_payment_status_text.setTextColor(Color.parseColor("#4caf50"));
        } else {
            order_payment_status_text.setText("NOT PAID");
            order_payment_status_text.setTextColor(Color.parseColor("#D80000"));
        }


        TextView order_billed_time_text = (TextView) v.findViewById(R.id.order_billed_time);
        order_billed_time_text.setText(ordersList.get(i).getBilled_time());

        v.setTag(ordersList.get(i).getId());

        return v;
    }
}
