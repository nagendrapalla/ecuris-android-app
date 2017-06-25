package in.stallats.ecuris;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

import in.stallats.ecuris.Diagnostics.LabsFrament;
import in.stallats.ecuris.Diagnostics.PackageCategoriesFragment;
import in.stallats.ecuris.Diagnostics.TestsFragment;
import in.stallats.ecuris.Supporting.BagdeDrawable;
import in.stallats.ecuris.Supporting.Session;

public class DiagnosticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    int fLastTab = -1;

    Session session;
    String cart_cnt_num = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("DIAGNOSTIC LABS"));
        tabLayout.addTab(tabLayout.newTab().setText("POPULAR TESTS"));
        tabLayout.addTab(tabLayout.newTab().setText("HEALTH PACKAGES"));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        session = new Session(this);
        if (!session.loggedin()) {
            //Toast.makeText(this, "Please login to get your saved cart", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, LoginActivity.class));
            //finish();
            session.set_CartCount("0");
        } else {

            HashMap<String, String> user = session.getUserDetails();
            final String id = user.get("id");

            Future<JsonArray> get = Ion.with(this)
                    .load("http://portal.ecuris.in/api/cart/" + id)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (e != null) {
                                session.set_CartCount("0");
                            } else {
                                int cart_count = result.size();
                                session.set_CartCount(String.valueOf(cart_count));
                            }
                        }
                    });

            HashMap<String, String> cart_cnt = session.getCartCount();
            cart_cnt_num = cart_cnt.get("cart_count");
        }

        MenuItem itemCart = menu.findItem(R.id.nav_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, cart_cnt_num);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_cart) {
            startActivity(new Intent(this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BagdeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BagdeDrawable) {
            badge = (BagdeDrawable) reuse;
        } else {
            badge = new BagdeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    LabsFrament tab1 = new LabsFrament();
                    return tab1;
                case 1:
                    TestsFragment tab2 = new TestsFragment();
                    return tab2;
                case 2:
                    PackageCategoriesFragment tab3 = new PackageCategoriesFragment();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
