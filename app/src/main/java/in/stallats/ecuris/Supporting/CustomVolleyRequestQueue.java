package in.stallats.ecuris.Supporting;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by User on 10-Aug-16.
 */
public class CustomVolleyRequestQueue {
    private static CustomVolleyRequestQueue mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mimageLoader;

    public CustomVolleyRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mimageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mimageLoader;
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context){
        if(mInstance == null){
            mInstance = new CustomVolleyRequestQueue(context);
        }
        return mInstance;
    }

}
