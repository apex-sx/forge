package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public final class WorkflowHistoryStep implements Consumer<ResultSet> {
    private long step_serial;
    private long case_serial;
    private long item_serial;
    private long node_serial;
    private int step_status;
    private String step_launch;
    private String step_finish;


    public long getStepSerial() {
        return step_serial;
    }

    public long getCaseSerial() {
        return case_serial;
    }

    public long getItemSerial() {
        return item_serial;
    }

    public long getNodeSerial() {
        return node_serial;
    }

    public int getStepStatus() {
        return step_status;
    }

    public String getStepLaunch() {
        return step_launch;
    }

    public String getStepFinish() {
        return step_finish;
    }

    public void setStepSerial(long step_serial) {
        this.step_serial = step_serial;
    }

    public void setCaseSerial(long case_serial) {
        this.case_serial = case_serial;
    }

    public void setItemSerial(long item_serial) {
        this.item_serial = item_serial;
    }

    public void setNodeSerial(long node_serial) {
        this.node_serial = node_serial;
    }

    public void setStepStatus(int step_status) {
        this.step_status = step_status;
    }

    public void setStepLaunch(String step_launch) {
        this.step_launch = step_launch;
    }

    public void setStepFinish(String step_finish) {
        this.step_finish = step_finish;
    }

    @Override
    public void accept(ResultSet resultSet) {
        try {
            this.setStepSerial(resultSet.getLong("step_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseSerial(resultSet.getLong("case_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setItemSerial(resultSet.getLong("item_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setNodeSerial(resultSet.getLong("node_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setStepStatus(resultSet.getInt("step_status"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setStepLaunch(resultSet.getString("step_launch"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setStepFinish(resultSet.getString("step_finish"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
