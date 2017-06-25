package in.stallats.ecuris.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import in.stallats.ecuris.MainActivity;
import in.stallats.ecuris.R;

public class NoInternetActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btn = (Button) findViewById(R.id.refresh);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh: startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
