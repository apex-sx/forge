package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public final class WorkflowHistoryItem implements Consumer<ResultSet> {
    private long item_serial;
    private long case_serial;
    private int item_status;
    private String item_stripe;
    private String item_values;
    private String item_launch;
    private String item_finish;

    public long getItemSerial() {
        return item_serial;
    }

    public long getCaseSerial() {
        return case_serial;
    }

    public int getItemStatus() {
        return item_status;
    }

    public String getItemStripe() {
        return item_stripe;
    }

    public String getItemValues() {
        return item_values;
    }

    public String getItemLaunch() {
        return item_launch;
    }

    public String getItemFinish() {
        return item_finish;
    }

    public void setItemSerial(long item_serial) {
        this.item_serial = item_serial;
    }

    public void setCaseSerial(long case_serial) {
        this.case_serial = case_serial;
    }

    public void setItemStatus(int item_status) {
        this.item_status = item_status;
    }

    public void setItemStripe(String item_stripe) {
        this.item_stripe = item_stripe;
    }

    public void setItemValues(String item_values) {
        this.item_values = item_values;
    }

    public void setItemLaunch(String item_launch) {
        this.item_launch = item_launch;
    }

    public void setItemFinish(String item_finish) {
        this.item_finish = item_finish;
    }

    @Override
    public void accept(ResultSet resultSet) {
        try {
            this.setItemSerial(resultSet.getLong("item_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseSerial(resultSet.getLong("case_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemStatus(resultSet.getInt("item_status"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemStripe(resultSet.getString("item_stripe"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemValues(resultSet.getString("item_values"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemLaunch(resultSet.getString("item_launch"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemFinish(resultSet.getString("item_finish"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
