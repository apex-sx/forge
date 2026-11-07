package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionNode implements Consumer<ResultSet> {
   private long node_serial;
   private long meta_serial;
   private int node_sorted;
   private int node_opener;
   private String node_stripe;
   private String node_method;
   private int node_number;
   private String node_symbol;
   private String node_banner;
   private String node_remark;

    public long getNode_serial() {
        return node_serial;
    }

    public long getMeta_serial() {
        return meta_serial;
    }

    public int getNode_sorted() {
        return node_sorted;
    }

    public int getNode_opener() {
        return node_opener;
    }

    public String getNode_stripe() {
        return node_stripe;
    }

    public String getNode_method() {
        return node_method;
    }

    public int getNode_number() {
        return node_number;
    }

    public String getNode_symbol() {
        return node_symbol;
    }

    public String getNode_banner() {
        return node_banner;
    }

    public String getNode_remark() {
        return node_remark;
    }

    public void setNode_serial(long node_serial) {
        this.node_serial = node_serial;
    }

    public void setMeta_serial(long meta_serial) {
        this.meta_serial = meta_serial;
    }

    public void setNode_sorted(int node_sorted) {
        this.node_sorted = node_sorted;
    }

    public void setNode_opener(int node_opener) {
        this.node_opener = node_opener;
    }

    public void setNode_stripe(String node_stripe) {
        this.node_stripe = node_stripe;
    }

    public void setNode_method(String node_method) {
        this.node_method = node_method;
    }

    public void setNode_number(int node_number) {
        this.node_number = node_number;
    }

    public void setNode_symbol(String node_symbol) {
        this.node_symbol = node_symbol;
    }

    public void setNode_banner(String node_banner) {
        this.node_banner = node_banner;
    }

    public void setNode_remark(String node_remark) {
        this.node_remark = node_remark;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
