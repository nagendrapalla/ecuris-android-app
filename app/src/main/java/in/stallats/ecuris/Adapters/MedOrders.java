package in.stallats.ecuris.Adapters;

/**
 * Created by User on 04-Jun-17.
 */

public class MedOrders {

    private int id;
    private String order_id;
    private String prescription;
    private String order_status;
    private String ordered_time;

    public MedOrders(int id, String order_id, String prescription, String order_status, String ordered_time) {
        this.id = id;
        this.order_id = order_id;
        this.prescription = prescription;
        this.order_status = order_status;
        this.ordered_time = ordered_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrdered_time() {
        return ordered_time;
    }

    public void setOrdered_time(String ordered_time) {
        this.ordered_time = ordered_time;
    }



}
