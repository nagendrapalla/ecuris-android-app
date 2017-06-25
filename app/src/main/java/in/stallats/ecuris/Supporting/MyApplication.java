package in.stallats.ecuris.Supporting;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by User on 25-Mar-17.
 */

public class MyApplication extends Application {
    public String slug;
    public String store_name;
    public String test_slug, test_name;
    public String package_slug, package_name;
    public String cat_id;
    public String cat_name;
    public String order_id;
    public String product_name, item_id;

    ArrayList<String> ar1;
    String selectedRadio;
    String manualRequ;
    String presc_status;

    public String getPresc_status() {
        return presc_status;
    }

    public void setPresc_status(String presc_status) {
        this.presc_status = presc_status;
    }

    public ArrayList<String> getAr1() {
        return ar1;
    }

    public void setAr1(ArrayList<String> ar1) {
        this.ar1 = ar1;
    }

    public String getSelectedRadio() {
        return selectedRadio;
    }

    public void setSelectedRadio(String selectedRadio) {
        this.selectedRadio = selectedRadio;
    }

    public String getManualRequ() {
        return manualRequ;
    }

    public void setManualRequ(String manualRequ) {
        this.manualRequ = manualRequ;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public void setLabSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getTest_slug() {
        return test_slug;
    }

    public void setTest_slug(String test_slug) {
        this.test_slug = test_slug;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getPackage_slug() {
        return package_slug;
    }

    public void setPackage_slug(String package_slug) {
        this.package_slug = package_slug;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}
