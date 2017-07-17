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
 * Created by User on 17-Jul-17.
 */

public class SearchListAdapter extends BaseAdapter {

    private Context context;
    private List<Search> searcList;

    public SearchListAdapter(Context context, List<Search> searcList) {
        this.context = context;
        this.searcList = searcList;
    }

    @Override
    public int getCount() {
        return searcList.size();
    }

    @Override
    public Object getItem(int i) {
        return searcList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.search_res_fragment, null);

        TextView view_name = (TextView) v.findViewById(R.id.pac_name);
        view_name.setText(searcList.get(i).getTestname());

        TextView store_name = (TextView) v.findViewById(R.id.search_type);
        store_name.setText(String.valueOf(searcList.get(i).getPage()));

        v.setTag(searcList.get(i).getId());
        return v;
    }

}
