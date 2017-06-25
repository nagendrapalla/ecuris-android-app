package in.stallats.ecuris.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        String act = getIntent().getStringExtra("order_id");

        TextView t = (TextView) findViewById(R.id.order_id_success);
        t.setText("Order ID: " + act);

        Button btn = (Button) findViewById(R.id.go_to_home_btn);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.go_to_home_btn:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
        }
    }
}
