package in.stallats.ecuris.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.stallats.ecuris.R;

/**
 * Created by User on 25-Mar-17.
 */

public class PackagesListAdapter extends BaseAdapter {
    private Context context;
    private List<Packages> testList;

    public PackagesListAdapter(Context context, List<Packages> testList) {
        this.context = context;
        this.testList = testList;
    }

    @Override
    public int getCount() {
        return testList.size();
    }

    @Override
    public Object getItem(int i) {
        return testList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.packages_fragment, null);

        TextView view_name = (TextView) v.findViewById(R.id.test_name);
        view_name.setText(testList.get(i).getTestname());

        TextView store_name = (TextView) v.findViewById(R.id.tests_count);
        store_name.setText(String.valueOf(testList.get(i).getTests_count()));

        TextView actual_price = (TextView) v.findViewById(R.id.actual_test_price);
        actual_price.setText("Rs. " + testList.get(i).getActualamount());
        actual_price.setPaintFlags(actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        TextView final_test_price = (TextView) v.findViewById(R.id.final_test_price);
        final_test_price.setText("Rs. " + testList.get(i).getFinalamount());

        v.setTag(testList.get(i).getId());

        return v;
    }
}
