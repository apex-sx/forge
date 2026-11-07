package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public final class WorkflowHistoryTask implements Consumer<ResultSet> {
    private long task_serial;
    private long case_serial;
    private long item_serial;
    private long step_serial;
    private byte task_status;
    private String task_launch;
    private String task_finish;
    private String task_result;
    private String task_holder;
    private String task_review;

    public long getTaskSerial() {
        return task_serial;
    }

    public long getCaseSerial() {
        return case_serial;
    }

    public long getItemSerial() {
        return item_serial;
    }

    public long getStepSerial() {
        return step_serial;
    }

    public byte getTaskStatus() {
        return task_status;
    }

    public String getTaskLaunch() {
        return task_launch;
    }

    public String getTaskFinish() {
        return task_finish;
    }

    public String getTaskResult() {
        return task_result;
    }

    public String getTaskHolder() {
        return task_holder;
    }

    public String getTaskReview() {
        return task_review;
    }

    public void setTaskSerial(Long task_serial) {
        this.task_serial = task_serial;
    }

    public void setCaseSerial(long case_serial) {
        this.case_serial = case_serial;
    }

    public void setItemSerial(long item_serial) {
        this.item_serial = item_serial;
    }

    public void setStepSerial(long step_serial) {
        this.step_serial = step_serial;
    }

    public void setTaskStatus(byte task_status) {
        this.task_status = task_status;
    }

    public void setTaskLaunch(String task_launch) {
        this.task_launch = task_launch;
    }

    public void setTaskFinish(String task_finish) {
        this.task_finish = task_finish;
    }

    public void setTaskResult(String task_result) {
        this.task_result = task_result;
    }

    public void setTaskHolder(String task_holder) {
        this.task_holder = task_holder;
    }

    public void setTaskReview(String task_review) {
        this.task_review = task_review;
    }


    @Override
    public void accept(ResultSet resultSet) {
        try {
            this.setTaskSerial(resultSet.getLong("task_serial"));
        } catch (SQLException e) {

        }
        try {
            this.setCaseSerial(resultSet.getLong("case_serial"));
        } catch (SQLException e) {

        }
        try {
            this.setItemSerial(resultSet.getLong("item_serial"));
        } catch (SQLException e) {

        }
        try {
            this.setStepSerial(resultSet.getLong("step_serial"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskStatus(resultSet.getByte("task_status"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskLaunch(resultSet.getString("task_launch"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskFinish(resultSet.getString("task_finish"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskResult(resultSet.getString("task_result"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskHolder(resultSet.getString("task_holder"));
        } catch (SQLException e) {

        }
        try {
            this.setTaskReview(resultSet.getString("task_review"));
        } catch (SQLException e) {

        }
    }
}
