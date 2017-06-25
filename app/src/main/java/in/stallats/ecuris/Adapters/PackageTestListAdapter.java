package in.stallats.ecuris.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.stallats.ecuris.R;

/**
 * Created by User on 25-Mar-17.
 */

public class PackageTestListAdapter extends BaseAdapter {
    private Context context;
    private List<Tests> testList;

    public PackageTestListAdapter(Context context, List<Tests> testList) {
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
        View v = View.inflate(context, R.layout.package_tests_fragment, null);

        TextView view_name = (TextView) v.findViewById(R.id.test_name);
        view_name.setText((i + 1) + ". " + testList.get(i).getTestname());

        v.setTag(testList.get(i).getId());

        return v;
    }
}
