package in.stallats.ecuris.Adapters;

/**
 * Created by User on 25-Mar-17.
 */

public class Packages {

    private int id;
    private String testname;
    private String testslug;
    private String actualamount;
    private String finalamount;
    private int tests_count;

    public Packages(int id, String testname, String testslug, String actualamount, String finalamount, int tests_count) {
        this.id = id;
        this.testname = testname;
        this.testslug = testslug;
        this.actualamount = actualamount;
        this.finalamount = finalamount;
        this.tests_count = tests_count;
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

    public String getActualamount() {
        return actualamount;
    }

    public void setActualamount(String actualamount) {
        this.actualamount = actualamount;
    }

    public String getFinalamount() {
        return finalamount;
    }

    public void setFinalamount(String finalamount) {
        this.finalamount = finalamount;
    }

    public int getTests_count() {
        return tests_count;
    }

    public void setTests_count(int tests_count) {
        this.tests_count = tests_count;
    }
}
