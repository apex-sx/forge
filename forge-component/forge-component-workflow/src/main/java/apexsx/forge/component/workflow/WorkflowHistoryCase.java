package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public final class WorkflowHistoryCase implements Consumer<ResultSet> {
    private long case_serial;
    private long meta_serial;
    private int case_status;
    private String case_holder;
    private String case_banner;
    private String case_remark;
    private String case_launch;
    private String case_finish;

    public long getCaseSerial() {
        return case_serial;
    }

    public long getMetaSerial() {
        return meta_serial;
    }

    public int getCaseStatus() {
        return case_status;
    }

    public String getCaseHolder() {
        return case_holder;
    }

    public String getCaseBanner() {
        return case_banner;
    }

    public String getCaseRemark() {
        return case_remark;
    }

    public String getCaseLaunch() {
        return case_launch;
    }

    public String getCaseFinish() {
        return case_finish;
    }

    public void setCaseSerial(long case_serial) {
        this.case_serial = case_serial;
    }

    public void setMetaSerial(long meta_serial) {
        this.meta_serial = meta_serial;
    }

    public void setCaseStatus(int case_status) {
        this.case_status = case_status;
    }

    public void setCaseHolder(String case_holder) {
        this.case_holder = case_holder;
    }

    public void setCaseBanner(String case_banner) {
        this.case_banner = case_banner;
    }

    public void setCaseRemark(String case_remark) {
        this.case_remark = case_remark;
    }

    public void setCaseLaunch(String case_launch) {
        this.case_launch = case_launch;
    }

    public void setCaseFinish(String case_finish) {
        this.case_finish = case_finish;
    }

    @Override
    public void accept(ResultSet resultSet) {
        try {
            this.setCaseSerial(resultSet.getLong("case_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setMetaSerial(resultSet.getLong("meta_serial"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseStatus(resultSet.getInt("case_status"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseHolder(resultSet.getString("case_holder"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseBanner(resultSet.getString("case_banner"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseRemark(resultSet.getString("case_remark"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseLaunch(resultSet.getString("case_launch"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.setCaseFinish(resultSet.getString("case_finish"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
