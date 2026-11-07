package apexsx.forge.component.workflow;

import java.sql.ResultSet;
import java.util.function.Consumer;

public final class WorkflowDefinitionMeta implements Consumer<ResultSet> {
    private long meta_serial;
    private long meta_tenant;
    private Long meta_symbol;
    private int meta_builds;
    private byte meta_deploy;
    private String meta_banner;
    private String meta_remark;
    private byte meta_status;
    private String meta_family;
    private String meta_module;
    private String meta_vision;
    private String meta_creator;
    private String meta_updater;
    private String meta_created;
    private String meta_updated;

    public long getMeta_serial() {
        return meta_serial;
    }

    public long getMeta_tenant() {
        return meta_tenant;
    }

    public Long getMeta_symbol() {
        return meta_symbol;
    }

    public int getMeta_builds() {
        return meta_builds;
    }

    public byte getMeta_deploy() {
        return meta_deploy;
    }

    public String getMeta_banner() {
        return meta_banner;
    }

    public String getMeta_remark() {
        return meta_remark;
    }

    public byte getMeta_status() {
        return meta_status;
    }

    public String getMeta_family() {
        return meta_family;
    }

    public String getMeta_module() {
        return meta_module;
    }

    public String getMeta_vision() {
        return meta_vision;
    }

    public String getMeta_creator() {
        return meta_creator;
    }

    public String getMeta_updater() {
        return meta_updater;
    }

    public String getMeta_created() {
        return meta_created;
    }

    public String getMeta_updated() {
        return meta_updated;
    }

    public void setMeta_serial(long meta_serial) {
        this.meta_serial = meta_serial;
    }

    public void setMeta_tenant(long meta_tenant) {
        this.meta_tenant = meta_tenant;
    }

    public void setMeta_symbol(Long meta_symbol) {
        this.meta_symbol = meta_symbol;
    }

    public void setMeta_builds(int meta_builds) {
        this.meta_builds = meta_builds;
    }

    public void setMeta_deploy(byte meta_deploy) {
        this.meta_deploy = meta_deploy;
    }

    public void setMeta_banner(String meta_banner) {
        this.meta_banner = meta_banner;
    }

    public void setMeta_remark(String meta_remark) {
        this.meta_remark = meta_remark;
    }

    public void setMeta_status(byte meta_status) {
        this.meta_status = meta_status;
    }

    public void setMeta_family(String meta_family) {
        this.meta_family = meta_family;
    }

    public void setMeta_module(String meta_module) {
        this.meta_module = meta_module;
    }

    public void setMeta_vision(String meta_vision) {
        this.meta_vision = meta_vision;
    }

    public void setMeta_creator(String meta_creator) {
        this.meta_creator = meta_creator;
    }

    public void setMeta_updater(String meta_updater) {
        this.meta_updater = meta_updater;
    }

    public void setMeta_created(String meta_created) {
        this.meta_created = meta_created;
    }

    public void setMeta_updated(String meta_updated) {
        this.meta_updated = meta_updated;
    }

    @Override
    public void accept(ResultSet resultSet) {

    }
}
