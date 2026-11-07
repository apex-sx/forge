package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionPage implements Consumer<ResultSet> {
    private long page_serial;
    private long node_serial;
    private String data_vision;
    private String page_portal;

    public long getPage_serial() {
        return page_serial;
    }

    public long getNode_serial() {
        return node_serial;
    }

    public String getData_vision() {
        return data_vision;
    }

    public String getPage_portal() {
        return page_portal;
    }

    public void setPage_serial(long page_serial) {
        this.page_serial = page_serial;
    }

    public void setNode_serial(long node_serial) {
        this.node_serial = node_serial;
    }

    public void setData_vision(String data_vision) {
        this.data_vision = data_vision;
    }

    public void setPage_portal(String page_portal) {
        this.page_portal = page_portal;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
