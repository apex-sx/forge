package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionLink implements Consumer<ResultSet> {
   private long link_serial;
   private long meta_serial;
   private long link_source;
   private long link_target;
   private int link_course;

    public long getLink_serial() {
        return link_serial;
    }

    public long getMeta_serial() {
        return meta_serial;
    }

    public long getLink_source() {
        return link_source;
    }

    public long getLink_target() {
        return link_target;
    }

    public int getLink_course() {
        return link_course;
    }

    public void setLink_serial(long link_serial) {
        this.link_serial = link_serial;
    }

    public void setMeta_serial(long meta_serial) {
        this.meta_serial = meta_serial;
    }

    public void setLink_source(long link_source) {
        this.link_source = link_source;
    }

    public void setLink_target(long link_target) {
        this.link_target = link_target;
    }

    public void setLink_course(int link_course) {
        this.link_course = link_course;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
