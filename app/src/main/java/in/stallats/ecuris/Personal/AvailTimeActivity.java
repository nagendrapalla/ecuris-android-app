package in.stallats.ecuris.Personal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import in.stallats.ecuris.BillingActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class AvailTimeActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    TextView edittext, edittext2;
    Button btn;
    String sl_date, sl_time;
    RadioGroup radioOfferGroup;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_time);

        radioOfferGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        session = new Session(this);

        edittext = (TextView) findViewById(R.id.editText);
        edittext2 = (TextView) findViewById(R.id.editText2);

        if (session.checkAvailMode()) {
            HashMap<String, String> avail = session.getAvailTime();
            edittext.setText(avail.get("avail_date"));
            edittext2.setText(avail.get("avail_time"));
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                sl_date = sdf.format(myCalendar.getTime());

                edittext.setText(sl_date);

                JsonObject json = new JsonObject();
                json.addProperty("date", String.valueOf(sl_date));

                Future<JsonArray> get = Ion.with(AvailTimeActivity.this)
                        .load("POST", "http://portal.ecuris.in/api/availabletime/")
                        .setJsonObjectBody(json)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                final Set booked = new HashSet<String>();
                                if (e != null) {
                                    booked.clear();
                                } else {
                                    for (int i = 0; i < result.size(); i++) {
                                        String x = result.get(i).toString();
                                        try {
                                            final JSONObject xx = new JSONObject(x);
                                            if (Integer.parseInt(xx.getString("count")) >= 5) {
                                                booked.add(xx.getString("available_time"));
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }

                                final Dialog dialog = new Dialog(AvailTimeActivity.this, R.style.ImagePopUpStyle);
                                final LinearLayout imagePopUp = (LinearLayout) LayoutInflater.from(AvailTimeActivity.this).inflate(R.layout.pop_up, null);

                                dialog.setContentView(imagePopUp);
                                dialog.setCanceledOnTouchOutside(true);

                                Display display = getWindowManager().getDefaultDisplay();
                                int width = display.getWidth();
                                int height = display.getHeight();

                                ImageView icon_close = (ImageView) dialog.findViewById(R.id.icon_close);

                                icon_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                for (int row = 0; row < 1; row++) {
                                    final RadioGroup ll = new RadioGroup(getApplicationContext());
                                    ll.setId(R.id.radioGroup1);
                                    ll.setOrientation(LinearLayout.VERTICAL);

                                    for (int i = 5; i <= 23; i++) {
                                        RadioButton rdbtn = new RadioButton(getApplicationContext());
                                        rdbtn.setId((row * 2) + i);
                                        String t;
                                        if ((i + row) < 12) {
                                            t = (((i + row) <= 9) ? "0" + (i + row) : (i + row)).toString() + " AM";
                                        } else if ((i + row) == 12) {
                                            t = (((i + row) <= 9) ? "0" + (i + row) : (i + row)).toString() + " PM";
                                        } else {
                                            t = ((((i + row) - 12) <= 9) ? "0" + ((i + row) - 12) : ((i + row) - 12)).toString() + " PM";
                                        }
                                        rdbtn.setText(t);

                                        rdbtn.setGravity(Gravity.LEFT);
                                        rdbtn.setButtonDrawable(new StateListDrawable());
                                        rdbtn.setPadding(40, 40, 40, 40);
                                        rdbtn.setSelected(false);

                                        if (booked.contains(rdbtn.getText())) {
                                            rdbtn.setEnabled(false);
                                            rdbtn.setBackgroundResource(R.drawable.rbtn_selector_disable);
                                        } else {
                                            rdbtn.setBackgroundResource(R.drawable.rbtn_selector);
                                            rdbtn.setTextColor(getResources().getColorStateList(R.drawable.rbtn_textcolor_selector));
                                        }


                                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(10, 10, 10, 10);
                                        rdbtn.setLayoutParams(params);

                                        ll.addView(rdbtn);
                                    }
                                    ((ViewGroup) dialog.findViewById(R.id.radioLiner)).addView(ll);

                                    Button bt = (Button) dialog.findViewById(R.id.time_slot_ok);
                                    bt.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            int selectedId = ll.getCheckedRadioButtonId();
                                            RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                                            if (selectedId == -1) {
                                                Toast.makeText(AvailTimeActivity.this, "Please choose a slot", Toast.LENGTH_SHORT).show();
                                            } else {
                                                sl_time = (String) radioButton.getText();
                                                edittext2.setText(sl_time);

                                                String time = sl_time;
                                                String daate = sl_date;
                                                Boolean avail_mode = true;

                                                session.set_availTime(time, daate, avail_mode);
                                                dialog.dismiss();

                                                startActivity(new Intent(getApplicationContext(), BillingActivity.class));
                                                finish();
                                            }
                                        }

                                    });

                                }


                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = width * 90 / 100;

                                dialog.getWindow().setAttributes(lp);
                                dialog.show();

                            }
                        });

            }

        };


        btn = (Button) findViewById(R.id.select_date);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(AvailTimeActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                DatePicker dp = dpd.getDatePicker();

                final Calendar c = Calendar.getInstance();

                c.add(Calendar.DAY_OF_MONTH, 1);
                dp.setMinDate(c.getTimeInMillis());

                c.add(Calendar.DAY_OF_MONTH, 10);
                dp.setMaxDate(c.getTimeInMillis());

                dpd.show();
            }
        });

    }
}
