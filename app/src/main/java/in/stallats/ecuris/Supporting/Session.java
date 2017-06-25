package in.stallats.ecuris.Supporting;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by ramyapriya on 18-07-2016.
 */
public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("ecuris", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean logedin) {
        editor.putBoolean("loggedinmode", logedin);
        editor.commit();
    }

    public boolean loggedin() {
        return prefs.getBoolean("loggedinmode", false);
    }


    public void setPincode(String pincode) {
        editor.putString("areacode", pincode);
        editor.commit();
    }

    public String getPincode() {
        return prefs.getString("areacode", null);
    }


    public void appOpenTimes(String count) {
        editor.putString("openCount", count);
        editor.commit();
    }

    public String getAppOpenTimes() {
        return prefs.getString("openCount", null);
    }

    public void clearEditorData() {
        editor.clear();
        editor.commit();
    }

    public void set_CartCount(String count) {
        editor.putString("cart_count", count); // Storing string
        editor.commit();
    }

    public HashMap<String, String> getCartCount() {
        HashMap<String, String> cart_c = new HashMap<String, String>();
        cart_c.put("cart_count", prefs.getString("cart_count", null));
        return cart_c;
    }

    public void set_sessionData(String id, String name, String email, String mobile, String refercode) {
        editor.putString("name", name); // Storing string
        editor.putString("email", email); // Storing string
        editor.putString("mobile", mobile); // Storing string
        editor.putString("id", id); // Storing integer
        editor.putString("refercode", refercode);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put("name", prefs.getString("name", null));
        user.put("email", prefs.getString("email", null));
        user.put("mobile", prefs.getString("mobile", null));
        user.put("id", prefs.getString("id", null));
        user.put("refercode", prefs.getString("refercode", null));

        // return user
        return user;
    }

    //Address Management
    public void set_address(String id, String title, String address, Boolean address_mode) {
        editor.putString("address_id", id); // Storing string
        editor.putString("address_title", title); // Storing string
        editor.putString("address_address", address); // Storing string
        editor.putBoolean("address_mode", address_mode);
        editor.commit();
    }

    public HashMap<String, String> getAddress() {
        HashMap<String, String> address = new HashMap<String, String>();
        address.put("address_id", prefs.getString("address_id", null));
        address.put("address_title", prefs.getString("address_title", null));
        address.put("address_address", prefs.getString("address_address", null));
        return address;
    }

    public boolean checkAddressMode() {
        return prefs.getBoolean("address_mode", false);
    }


    //Patient Management
    public void set_patient(String id, String name, String age, String gender, Boolean patient_mode) {
        editor.putString("patient_id", id); // Storing string
        editor.putString("patient_name", name); // Storing string
        editor.putString("patient_age", age); // Storing string
        editor.putString("patient_gender", gender); // Storing string
        editor.putBoolean("patient_mode", patient_mode);
        editor.commit();
    }

    public HashMap<String, String> getPatient() {
        HashMap<String, String> patient = new HashMap<String, String>();
        patient.put("patient_id", prefs.getString("patient_id", null));
        patient.put("patient_name", prefs.getString("patient_name", null));
        patient.put("patient_age", prefs.getString("patient_age", null));
        patient.put("patient_gender", prefs.getString("patient_gender", null));
        return patient;
    }

    public boolean checkPatientMode() {
        return prefs.getBoolean("patient_mode", false);
    }


    //Availabilty Time
    public void set_availTime(String time, String daate, Boolean avail_mode) {
        editor.putString("avail_time", time); // Storing string
        editor.putString("avail_date", daate); // Storing string
        editor.putBoolean("avail_mode", avail_mode);
        editor.commit();
    }

    public void remove_availTime() {
        editor.putString("avail_time", ""); // Storing string
        editor.putString("avail_date", ""); // Storing string
        editor.putBoolean("avail_mode", false);
        editor.commit();
    }

    public HashMap<String, String> getAvailTime() {
        HashMap<String, String> avail = new HashMap<String, String>();
        avail.put("avail_time", prefs.getString("avail_time", null));
        avail.put("avail_date", prefs.getString("avail_date", null));
        return avail;
    }

    public boolean checkAvailMode() {
        return prefs.getBoolean("avail_mode", false);
    }
}
