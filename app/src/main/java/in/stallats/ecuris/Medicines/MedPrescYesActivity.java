package in.stallats.ecuris.Medicines;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import in.stallats.ecuris.Common.LoginActivity;
import in.stallats.ecuris.R;
import in.stallats.ecuris.Supporting.Session;

public class MedPrescYesActivity extends AppCompatActivity implements View.OnClickListener {
    private final int CAMERA_REQUEST = 1100;
    private final int GALLERY_REQUEST = 1695;

    CardView ivCamera, ivGallery, bottom_radios;
    LinearLayout linearMain;
    Button sendNext;
    RadioGroup order_pres_radio_group;
    RadioButton presc_mode;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;

    ArrayList<String> imageList = new ArrayList<>();

    String selectedRadio;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_presc_yes);

        session = new Session(this);
        if (!session.loggedin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        findViewById(R.id.bottom).setVisibility(View.GONE);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());


        bottom_radios = (CardView) findViewById(R.id.bottom_radios);
        bottom_radios.setVisibility(View.GONE);

        order_pres_radio_group = (RadioGroup) findViewById(R.id.order_pres_radio_group);

        ivCamera = (CardView) findViewById(R.id.ivCamera);
        ivGallery = (CardView) findViewById(R.id.ivGallery);
        sendNext = (Button) findViewById(R.id.sendNext);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        sendNext.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivGallery.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photopath = cameraPhoto.getPhotoPath();
                imageList.add(photopath);
                try {
                    Bitmap bitmap = ImageLoader.init().from(photopath).requestSize(512, 512).getBitmap();
                    bottom_radios.setVisibility(View.VISIBLE);
                    findViewById(R.id.bottom).setVisibility(View.VISIBLE);
                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0, 0, 20, 0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);

                    linearMain.addView(imageView);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while loading photos....", Toast.LENGTH_LONG).show();
                }
            } else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photopath = galleryPhoto.getPath();
                imageList.add(photopath);
                try {
                    Bitmap bitmap = ImageLoader.init().from(photopath).requestSize(512, 512).getBitmap();
                    bottom_radios.setVisibility(View.VISIBLE);
                    findViewById(R.id.bottom).setVisibility(View.VISIBLE);
                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0, 0, 20, 0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);

                    linearMain.addView(imageView);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while choosing photos....", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendNext:
                if(selectedRadio == null || selectedRadio.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select atleast one option", Toast.LENGTH_LONG).show();
                }else{

                    if(selectedRadio.equals("Order Only Few Items")){
                        Intent i = new Intent(getApplicationContext(), MedicineRequirementActivity.class);
                        i.putStringArrayListExtra("imgs", imageList);
                        i.putExtra("selectedRadio", selectedRadio);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(getApplicationContext(), MedicineAddress.class);
                        i.putStringArrayListExtra("imgs", imageList);
                        i.putExtra("selectedRadio", selectedRadio);
                        i.putExtra("presc_status", "Yes");
                        i.putExtra("manual_req", "None");
                        startActivity(i);
                    }
                }
                break;
            case R.id.ivCamera:
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while taking photos....", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivGallery:
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                break;
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

    public void onRadioButtonClicked(View view) {
        int selectedId = order_pres_radio_group.getCheckedRadioButtonId();
        presc_mode = (RadioButton) findViewById(selectedId);
        selectedRadio = (String) presc_mode.getText();
    }

}
