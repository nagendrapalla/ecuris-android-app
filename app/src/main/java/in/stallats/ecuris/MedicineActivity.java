package in.stallats.ecuris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Medicines.MedPrescYesActivity;
import in.stallats.ecuris.Medicines.MedicineRequirementActivity;
import in.stallats.ecuris.Supporting.Session;

public class MedicineActivity extends AppCompatActivity implements View.OnClickListener {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        CardView med_prec_yes = (CardView) findViewById(R.id.med_prec_yes);
        CardView med_prec_no = (CardView) findViewById(R.id.med_prec_no);

        med_prec_yes.setOnClickListener(this);
        med_prec_no.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.med_prec_yes:
                startActivity(new Intent(this, MedPrescYesActivity.class));
                break;
            case R.id.med_prec_no:
                startActivity(new Intent(this, MedicineRequirementActivity.class));
                break;
        }
    }
}
