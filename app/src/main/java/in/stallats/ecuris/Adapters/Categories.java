package in.stallats.ecuris.Adapters;

/**
 * Created by User on 25-Mar-17.
 */

public class Categories {

    private int id;
    private String package_category;
    private String package_slug;
    private String package_image;

    public Categories(int id, String package_category, String package_slug, String package_image) {
        this.id = id;
        this.package_category = package_category;
        this.package_slug = package_slug;
        this.package_image = package_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackage_category() {
        return package_category;
    }

    public void setPackage_category(String package_category) {
        this.package_category = package_category;
    }

    public String getPackage_slug() {
        return package_slug;
    }

    public void setPackage_slug(String package_slug) {
        this.package_slug = package_slug;
    }

    public String getPackage_image() {
        return package_image;
    }

    public void setPackage_image(String package_image) {
        this.package_image = package_image;
    }
}
