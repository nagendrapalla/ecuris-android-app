package in.stallats.ecuris.Adapters;

/**
 * Created by User on 04-Jun-17.
 */

public class DiagOrders {

    private int id;
    private String order_id;
    private String orderamount;
    private String payment_status;
    private String billed_time;
    private int items_count;

    public DiagOrders(int id, String order_id, String orderamount, String payment_status, String billed_time, int items_count) {
        this.id = id;
        this.order_id = order_id;
        this.orderamount = orderamount;
        this.payment_status = payment_status;
        this.billed_time = billed_time;
        this.items_count = items_count;
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

    public String getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(String orderamount) {
        this.orderamount = orderamount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getBilled_time() {
        return billed_time;
    }

    public void setBilled_time(String billed_time) {
        this.billed_time = billed_time;
    }

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

}
