package in.stallats.ecuris.Medicines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import in.stallats.ecuris.R;

public class MedicineRequirementActivity extends AppCompatActivity {

    EditText med_nxt_req;
    Button med_next_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_requirement);

        med_nxt_req = (EditText) findViewById(R.id.med_nxt_req);
        med_nxt_req.setVisibility(View.GONE);

        med_next_proceed = (Button) findViewById(R.id.med_next_proceed);
        med_next_proceed.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {

            med_nxt_req.setVisibility(View.VISIBLE);
            med_next_proceed.setVisibility(View.VISIBLE);

            final ArrayList<String> ar2 = new ArrayList<String>(Arrays.asList("none"));
            med_next_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String xx = med_nxt_req.getText().toString();
                    if(xx == null || xx.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter requirement", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent i = new Intent(getApplicationContext(), MedicineAddress.class);
                        i.putStringArrayListExtra("imgs", ar2);
                        i.putExtra("selectedRadio", "No Prescription");
                        i.putExtra("presc_status", "No");
                        i.putExtra("manual_req", xx);
                        startActivity(i);
                    }
                }
            });

        }else{
            final ArrayList<String> ar1 = getIntent().getExtras().getStringArrayList("imgs");
            final String selectedRadio = getIntent().getStringExtra("selectedRadio");

            med_nxt_req.setVisibility(View.VISIBLE);
            med_next_proceed.setVisibility(View.VISIBLE);

            med_next_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String xx = med_nxt_req.getText().toString();
                    if(xx == null || xx.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter requirement", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent i = new Intent(getApplicationContext(), MedicineAddress.class);
                        i.putStringArrayListExtra("imgs", ar1);
                        i.putExtra("selectedRadio", selectedRadio);
                        i.putExtra("presc_status", "Yes");
                        i.putExtra("manual_req", xx);
                        startActivity(i);
                    }
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
