package in.stallats.ecuris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.Personal.AddressActivity;
import in.stallats.ecuris.Personal.ChangePasswordActivity;
import in.stallats.ecuris.Personal.PatientActivity;
import in.stallats.ecuris.Supporting.Session;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        findViewById(R.id.change_password).setOnClickListener(this);
        findViewById(R.id.update_address).setOnClickListener(this);
        findViewById(R.id.update_patients).setOnClickListener(this);
        findViewById(R.id.account_logout).setOnClickListener(this);

        session = new Session(this);

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        HashMap<String, String> user = session.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        String mobile = user.get("mobile");

        TextView view_email = (TextView) findViewById(R.id.profile_email);
        view_email.setText(email);

        TextView view_name = (TextView) findViewById(R.id.profile_name);
        view_name.setText(name);

        TextView view_mobile = (TextView) findViewById(R.id.profile_mobile);
        view_mobile.setText(mobile);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.update_address:
                startActivity(new Intent(this, AddressActivity.class).putExtra("value", false));
                break;

            case R.id.update_patients:
                startActivity(new Intent(this, PatientActivity.class).putExtra("value", false)) ;
                break;

            case R.id.account_logout:
                logout();
                break;

            default:
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void logout(){
        session.setLoggedIn(false);
        session.clearEditorData();
        startActivity(new Intent(AccountActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
