package in.stallats.ecuris.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class AreaDetectorActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_detector);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btn = (Button) findViewById(R.id.btnStatus);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStatus:
                try {
                    EditText txt = (EditText) findViewById(R.id.editPincode);
                    if (txt.getText().length() != 6) {
                        Toast.makeText(getApplicationContext(), "Please enter proper pincode", Toast.LENGTH_LONG).show();
                    } else {
                        int sts_pin = Integer.parseInt(Ion.with(this).load("http://portal.ecuris.in/api/pinstatus/" + txt.getText()).asString().get());
                        if (sts_pin == 0) {
                            Toast.makeText(getApplicationContext(), "Sorry, Currently we are not providing our service in your location.", Toast.LENGTH_LONG).show();
                        } else {
                            session = new Session(getApplicationContext());
                            String xtp = String.valueOf(txt.getText());
                            session.setPincode(xtp);
                            Toast.makeText(getApplicationContext(), "Enjoy your shopping. Thank you....", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        }
    }
}
