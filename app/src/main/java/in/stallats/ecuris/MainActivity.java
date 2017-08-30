package in.stallats.ecuris;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.stallats.ecuris.Common.AreaDetectorActivity;
import in.stallats.ecuris.Common.NoInternetActivity;
import in.stallats.ecuris.Personal.ReferActivity;
import in.stallats.ecuris.Supporting.AbsRuntimePermissions;
import in.stallats.ecuris.Supporting.BagdeDrawable;
import in.stallats.ecuris.Supporting.ConnectionDetector;
import in.stallats.ecuris.Supporting.SearchAdapter;
import in.stallats.ecuris.Supporting.Session;
import in.stallats.ecuris.Supporting.SharedPreference;
import in.stallats.ecuris.Supporting.Utils;

public class MainActivity extends AbsRuntimePermissions implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ConnectionDetector cd;
    Session session;
    EditText etEmail;
    List<String> list;

    String cart_cnt_num = "0";
    String[] toppings = {};
    Dialog toolbarSearchDialog;

    private static final int REQUEST_PERMISSION = 10;
    private ArrayList<String> mCountries;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        requestAppPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                R.string.msg, REQUEST_PERMISSION);


        cd = new ConnectionDetector(this);
        if (!cd.isConnected()) {
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        session = new Session(this);
        String appPincode = session.getPincode();
        if (appPincode == null) {
            startActivity(new Intent(this, AreaDetectorActivity.class).putExtra("value", false));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getImages();

        CardView home_med = (CardView) findViewById(R.id.home_med);
        CardView home_diag = (CardView) findViewById(R.id.home_diag);
        CardView home_doct = (CardView) findViewById(R.id.home_doct);
        LinearLayout home_help = (LinearLayout) findViewById(R.id.home_help);
        CardView home_ord = (CardView) findViewById(R.id.home_ord);

        home_med.setOnClickListener(this);
        home_diag.setOnClickListener(this);
        home_doct.setOnClickListener(this);
        home_help.setOnClickListener(this);
        home_ord.setOnClickListener(this);

        ArrayList<String> titles_arr = new ArrayList<String>();

        try {
            JsonArray test_json = Ion.with(this).load("http://portal.ecuris.in/api/tests_all/").asJsonArray().get();
            for (int i = 0; i < test_json.size(); i++) {
                String x = test_json.get(i).toString();
                try {
                    final JSONObject xx = new JSONObject(x);
                    titles_arr.add(xx.getString("title"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            JsonArray package_json = Ion.with(this).load("http://portal.ecuris.in/api/packages/").asJsonArray().get();
            for (int i = 0; i < package_json.size(); i++) {
                String x = package_json.get(i).toString();
                try {
                    final JSONObject xx = new JSONObject(x);
                    titles_arr.add(xx.getString("package_title"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        toppings = new String[titles_arr.size()];
        toppings = titles_arr.toArray(toppings);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setInputType(InputType.TYPE_NULL);
        etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToolBarSearch();
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(toolbarSearchDialog == null){
            super.onBackPressed();
        }else{
            if(toolbarSearchDialog.isShowing()){
                toolbarSearchDialog.dismiss();
            }else{
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!session.loggedin()) {
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
                                String cart_count = String.valueOf(result.size());
                                session.set_CartCount(cart_count);
                            }
                        }
                    });

            HashMap<String, String> cart_cnt = session.getCartCount();
            cart_cnt_num = cart_cnt.get("cart_count");

            if (cart_cnt_num == null) {
                cart_cnt_num = "0";
            }
        }
        MenuItem itemCart = menu.findItem(R.id.nav_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, cart_cnt_num);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_medicines) {
            startActivity(new Intent(this, MedicineActivity.class));
        } else if (id == R.id.nav_refer) {
            startActivity(new Intent(this, ReferActivity.class));
        } else if (id == R.id.nav_diagnostics) {
            startActivity(new Intent(this, DiagnosticsActivity.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, AccountActivity.class));
        } else if (id == R.id.nav_payhistory) {
            startActivity(new Intent(this, OrdersActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_rating) {
            Toast.makeText(this, "RATE OUR APP", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_med:
                startActivity(new Intent(this, MedicineActivity.class));
                break;
            case R.id.home_diag:
                startActivity(new Intent(this, DiagnosticsActivity.class));
                break;
            case R.id.home_doct:
                Toast.makeText(getApplicationContext(), "Coming soon.....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_ord:
                startActivity(new Intent(this, OrdersActivity.class));
                break;
            case R.id.home_help:
                startActivity(new Intent(this, SupportActivity.class));
                break;

        }
    }

    public void getImages() {
        final ImageView offer_image_1 = (ImageView) findViewById(R.id.offer_img_1);
        final ImageView offer_image_2 = (ImageView) findViewById(R.id.offer_img_2);
        final ImageView offer_image_3 = (ImageView) findViewById(R.id.offer_img_3);
        final ImageView offer_image_4 = (ImageView) findViewById(R.id.offer_img_4);
        final ImageView offer_image_5 = (ImageView) findViewById(R.id.offer_img_5);
        final ImageView offer_image_6 = (ImageView) findViewById(R.id.offer_img_6);
        final ImageView offer_image_7 = (ImageView) findViewById(R.id.offer_img_7);
        final ImageView offer_image_8 = (ImageView) findViewById(R.id.offer_img_8);

        Picasso.with(getApplicationContext()).load("http://discount-coupon-codes.upto75.com/uploadimages/coupons/9986-MedFinder_Banner1.jpg").into(offer_image_1);
        Picasso.with(getApplicationContext()).load("https://shopnix.in/blog/wp-content/uploads/2015/10/online-medicine-store-1.jpg").into(offer_image_2);
        Picasso.with(getApplicationContext()).load("http://discount-coupon-codes.upto75.com/uploadimages/coupons/10814-Magnus_Diagnostic_Centre_Bengaluru_Coupon_2.jpg").into(offer_image_3);
        Picasso.with(getApplicationContext()).load("https://mddirectonline.com/images/banner1.jpg").into(offer_image_4);

        Picasso.with(getApplicationContext()).load("http://www.jaminthompson.com/blog/wp-content/uploads/2010/11/Oxygen_spread1.jpg").into(offer_image_5);
        Picasso.with(getApplicationContext()).load("http://amyfournier.com/wp-content/uploads/Womens-Health-and-Fitness-Mag-Cover-Feature-Article-Feb-2014-Copy_Page_1.jpg").into(offer_image_6);
        Picasso.with(getApplicationContext()).load("http://www.mandjchickens.com.au/images-news/Food-Magazine-June-July-2013-Article.jpg").into(offer_image_7);
        Picasso.with(getApplicationContext()).load("http://www.health-goji-juice.com/images/goji-intouch.jpg").into(offer_image_8);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BagdeDrawable badge;
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

    public void loadToolBarSearch() {
        ArrayList<String> countryStored = SharedPreference.loadList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES);

        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Ex: Crocin, Lipid Profile");

        toolbarSearchDialog = new Dialog(MainActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(true);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        countryStored = (countryStored != null && countryStored.size() > 0) ? countryStored : new ArrayList<String>();
        final SearchAdapter searchAdapter = new SearchAdapter(MainActivity.this, countryStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String country = String.valueOf(adapterView.getItemAtPosition(position));
                SharedPreference.addList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, country);
                startActivity(new Intent(getApplicationContext(), SearchActivity.class).putExtra("qry", country));
            }
        });

        edtToolSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String qry = edtToolSearch.getText().toString();
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class).putExtra("qry", qry));
                    return true;
                }
                return false;
            }
        });

        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String[] country = toppings;
                mCountries = new ArrayList<String>(Arrays.asList(country));
                listSearch.setVisibility(View.VISIBLE);
                searchAdapter.updateList(mCountries, true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < mCountries.size(); i++) {
                        if (mCountries.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {
                            filterList.add(mCountries.get(i));
                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
