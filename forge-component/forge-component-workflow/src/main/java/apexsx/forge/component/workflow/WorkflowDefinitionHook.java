package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionHook implements Consumer<ResultSet> {
    private long hook_serial;
    private long data_vision;
    private int hook_sorted;
    private int hook_events;
    private String hook_invoke;

    public long getHook_serial() {
        return hook_serial;
    }

    public long getData_vision() {
        return data_vision;
    }

    public int getHook_sorted() {
        return hook_sorted;
    }

    public int getHook_events() {
        return hook_events;
    }

    public String getHook_invoke() {
        return hook_invoke;
    }

    public void setHook_serial(long hook_serial) {
        this.hook_serial = hook_serial;
    }

    public void setData_vision(long data_vision) {
        this.data_vision = data_vision;
    }

    public void setHook_sorted(int hook_sorted) {
        this.hook_sorted = hook_sorted;
    }

    public void setHook_events(int hook_events) {
        this.hook_events = hook_events;
    }

    public void setHook_invoke(String hook_invoke) {
        this.hook_invoke = hook_invoke;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
