package in.stallats.ecuris.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import in.stallats.ecuris.Supporting.CustomVolleyRequestQueue;
import in.stallats.ecuris.R;

/**
 * Created by User on 25-Mar-17.
 */

public class PackageCategoriesListAdapter extends BaseAdapter {
    private Context context;
    private List<Categories> catList;
    private ImageLoader mImageLoader;

    public PackageCategoriesListAdapter(Context context, List<Categories> catList) {
        this.context = context;
        this.catList = catList;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int i) {
        return catList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.packages_cat_fragment, null);

        NetworkImageView niv = (NetworkImageView) v.findViewById(R.id.cat_image);
        mImageLoader.get(catList.get(i).getPackage_image(), ImageLoader.getImageListener(niv, R.drawable.loader, R.mipmap.ic_launcher));
        niv.setImageUrl(catList.get(i).getPackage_image(), mImageLoader);

        TextView view_name = (TextView) v.findViewById(R.id.cat_name);
        view_name.setText(catList.get(i).getPackage_category());

        v.setTag(catList.get(i).getId());

        return v;
    }
}
