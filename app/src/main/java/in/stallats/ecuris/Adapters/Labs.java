package in.stallats.ecuris.Adapters;

import org.json.JSONArray;

/**
 * Created by User on 04-Jun-17.
 */

public class Labs {
    private int id;
    private String slug;
    private String offer_name;
    private String store_name;
    private JSONArray accre;

    public Labs(int id, String offer_name, String store_name, JSONArray accre, String slug) {
        this.id = id;
        this.slug = slug;
        this.offer_name = offer_name;
        this.store_name = store_name;
        this.accre = accre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public JSONArray getAccre() {
        return accre;
    }

    public void setAccre(JSONArray accre) {
        this.accre = accre;
    }
}
