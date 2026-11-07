package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionUser implements Consumer<ResultSet> {
   private long user_serial;
   private long node_serial;
   private String data_vision;
   private String user_stripe;
   private String user_values;

    public long getUser_serial() {
        return user_serial;
    }

    public long getNode_serial() {
        return node_serial;
    }

    public String getData_vision() {
        return data_vision;
    }

    public String getUser_stripe() {
        return user_stripe;
    }

    public String getUser_values() {
        return user_values;
    }

    public void setUser_serial(long user_serial) {
        this.user_serial = user_serial;
    }

    public void setNode_serial(long node_serial) {
        this.node_serial = node_serial;
    }

    public void setData_vision(String data_vision) {
        this.data_vision = data_vision;
    }

    public void setUser_stripe(String user_stripe) {
        this.user_stripe = user_stripe;
    }

    public void setUser_values(String user_values) {
        this.user_values = user_values;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
