package in.stallats.ecuris.Adapters;

/**
 * Created by User on 17-Jul-17.
 */

public class Search {

    private int id;
    private String testname;
    private String testslug;
    private String page;

    public Search(int id, String testname, String testslug, String page) {
        this.id = id;
        this.testname = testname;
        this.testslug = testslug;
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTestslug() {
        return testslug;
    }

    public void setTestslug(String testslug) {
        this.testslug = testslug;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
