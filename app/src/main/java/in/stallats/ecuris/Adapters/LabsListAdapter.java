package in.stallats.ecuris.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.CustomVolleyRequestQueue;

/**
 * Created by User on 04-Jun-17.
 */

public class LabsListAdapter extends BaseAdapter {

    private ImageLoader mImageLoader;
    private Context context;
    private List<Labs> mLabsList;

    public LabsListAdapter(Context context, List<Labs> mLabsList) {
        this.context = context;
        this.mLabsList = mLabsList;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return mLabsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mLabsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.labs_list_fragment, null);

        NetworkImageView niv = (NetworkImageView) v.findViewById(R.id.offers_image);
        mImageLoader.get(mLabsList.get(i).getOffer_name(), ImageLoader.getImageListener(niv, R.drawable.loader, R.mipmap.ic_launcher));
        niv.setImageUrl(mLabsList.get(i).getOffer_name(), mImageLoader);

        String image1 = "", image2 = "", image3 = "", image4 = "", image5 = "";


        Integer accr_length = mLabsList.get(i).getAccre().length();
        JSONArray x = mLabsList.get(i).getAccre();

        for (int j = 0; j < accr_length; j++) {
            String imp = null;
            try {
                imp = x.get(j).toString();
                final JSONObject xx = new JSONObject(imp);

                if (j == 0) {
                    image1 = "http://portal.ecuris.in/assets/uploads/accr/" + xx.getString("logo");
                    NetworkImageView niv1 = (NetworkImageView) v.findViewById(R.id.acc_image_1);
                    mImageLoader.get(image1, ImageLoader.getImageListener(niv1, R.drawable.loader, R.mipmap.ic_launcher));
                    niv1.setImageUrl(image1, mImageLoader);
                } else if (j == 1) {
                    image2 = "http://portal.ecuris.in/assets/uploads/accr/" + xx.getString("logo");
                    NetworkImageView niv2 = (NetworkImageView) v.findViewById(R.id.acc_image_2);
                    mImageLoader.get(image2, ImageLoader.getImageListener(niv2, R.drawable.loader, R.mipmap.ic_launcher));
                    niv2.setImageUrl(image2, mImageLoader);
                } else if (j == 2) {
                    image3 = "http://portal.ecuris.in/assets/uploads/accr/" + xx.getString("logo");
                    NetworkImageView niv3 = (NetworkImageView) v.findViewById(R.id.acc_image_3);
                    mImageLoader.get(image3, ImageLoader.getImageListener(niv3, R.drawable.loader, R.mipmap.ic_launcher));
                    niv3.setImageUrl(image3, mImageLoader);
                } else if (j == 3) {
                    image4 = "http://portal.ecuris.in/assets/uploads/accr/" + xx.getString("logo");
                    NetworkImageView niv4 = (NetworkImageView) v.findViewById(R.id.acc_image_4);
                    mImageLoader.get(image4, ImageLoader.getImageListener(niv4, R.drawable.loader, R.mipmap.ic_launcher));
                    niv4.setImageUrl(image4, mImageLoader);
                } else if (j == 4) {
                    image5 = "http://portal.ecuris.in/assets/uploads/accr/" + xx.getString("logo");
                    NetworkImageView niv5 = (NetworkImageView) v.findViewById(R.id.acc_image_5);
                    mImageLoader.get(image5, ImageLoader.getImageListener(niv5, R.drawable.loader, R.mipmap.ic_launcher));
                    niv5.setImageUrl(image5, mImageLoader);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Toast.makeText(context, Integer.parseInt(String.valueOf(x.length())), Toast.LENGTH_SHORT).show();

        TextView view_name = (TextView) v.findViewById(R.id.vendor_name);
        view_name.setText(mLabsList.get(i).getStore_name());

        v.setTag(mLabsList.get(i).getId());
        return v;
    }

}
