package in.stallats.ecuris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Supporting.Session;

public class SettingsActivity extends AppCompatActivity {

    private Session session;
    CheckBox s;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new Session(this);
        if (!session.loggedin()) {
            Toast.makeText(this, "Please login to get your saved cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        s = (CheckBox) findViewById(R.id.notify_switch);

        HashMap<String, String> user = session.getUserDetails();
        id = user.get("id");

        Future<JsonObject> get = Ion.with(this)
                .load("http://portal.ecuris.in/api/notify/" + id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {

                        } else {
                            try {
                                final JSONObject xx = new JSONObject(result.toString());
                                int st = Integer.parseInt(xx.getString("status"));
                                //Toast.makeText(getApplicationContext(), xx.getString("status"), Toast.LENGTH_SHORT).show();
                                if (st == 0) {
                                    s.setChecked(true);
                                } else {
                                    s.setChecked(false);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });


        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int val;
                if (s.isChecked()) {
                    val = 0;
                } else {
                    val = 1;
                }
                JsonObject json = new JsonObject();
                json.addProperty("status", val);

                Ion.with(getApplicationContext())
                        .load("POST", "http://portal.ecuris.in/api/notify/" + id)
                        .setJsonObjectBody(json)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if (e != null) {

                                } else {
                                    //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Push Notifications " + ((val == 0) ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }
                        });
            }
        });


    }

}
