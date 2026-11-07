package com.github.zhitron.essential_management;

import com.github.zhitron.effortless.jdbc.PersistenceException;
import com.github.zhitron.effortless.lang.This;
import com.github.zhitron.effortless.lang.ValueFetcher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用实体基类，包含基础字段和查询条件支持
 *
 * @author zhitron
 */
@Getter
@Accessors(chain = true)
public abstract class EssentialEntity<Entity extends EssentialEntity<Entity>> implements Comparable<Entity>, This<Entity> {

    private static final Map<Class<? extends EssentialEntity<?>>, Table> TABLE = new ConcurrentHashMap<>();
    /**
     * 主键ID
     */
    @EqualsAndHashCode.Include
    private Long cid = null;
    /**
     * 租户ID
     */
    private String tid = null;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * 层级
     */
    private int lvl = 0;
    /**
     * 数据版本号，用于乐观锁控制
     * 默认值为0，每次更新时递增
     */
    private int version = 0;
    /**
     * 排序顺序
     */
    private int dataSorted = 0;
    /**
     * 状态标识
     * 小于0则无效定义
     * 0：正常
     * 其它状态由子类定义
     */
    private int dataStatus = 0;
    /**
     * 删除标志
     * 小于0则无效定义
     * false：未删除
     * true：已删除
     */
    private Boolean removalFlag = null;
    /**
     * 可见标识
     * 小于0则无效定义
     * true：可见
     * false：不可见
     */
    private Boolean visibleFlag = null;
    /**
     * 显示名称，用于描述数据的信息，默认为空
     */
    private String displayName = null;
    /**
     * 显示描述，用于描述数据的信息，默认为空
     */
    private String displayDesc = null;
    /**
     * 定义时间
     */
    private LocalDateTime defineTime = null;
    /**
     * 归属标识ID
     */
    private Long belongByCid = null;
    /**
     * 归属标识类型
     */
    private Integer belongByType = null;
    /**
     * 归属标识名称
     */
    private String belongByName = null;
    /**
     * 创建时间
     */
    private LocalDateTime createTime = null;
    /**
     * 创建人标识ID
     */
    private Long createByCid = null;
    /**
     * 创建人标识类型
     */
    private Integer createByType = null;
    /**
     * 创建人标识名称
     */
    private String createByName = null;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime = null;
    /**
     * 最后更新人标识ID
     */
    private Long updateByCid = null;
    /**
     * 最后更新人标识类型
     */
    private Integer updateByType = null;
    /**
     * 最后更新人标识名称
     */
    private String updateByName = null;
    /**
     * 访问时间
     */
    private LocalDateTime accessTime = null;
    /**
     * 最后访问人标识ID
     */
    private String accessByCid = null;
    /**
     * 最后访问人标识类型
     */
    private String accessByType = null;
    /**
     * 最后访问人标识名称
     */
    private String accessByName = null;

    /**
     * 搜索关键字（非持久化字段）
     */
    private transient String querySearchValue = null;
    /**
     * 分页参数 - 当前页码和每页数量（非持久化字段）
     */
    private transient int queryPageNumber = 1, queryPageAmount = 100;
    /**
     * 排序方向标志（非持久化字段）
     * false: 升序, true: 降序
     */
    private transient boolean querySortedDescending = false;
    /**
     * CID筛选条件（非持久化字段）
     * queryIncludeCidArray/Collection: 包含的CID数组/集合
     * queryExcludeCidArray/Collection: 排除的CID数组/集合
     */
    private transient long[] queryIncludeCidArray = null, queryExcludeCidArray = null;
    private transient Collection<Long> queryIncludeCidCollection = null, queryExcludeCidCollection = null;
    /**
     * PID筛选条件（非持久化字段）
     * queryIncludeCidArray/Collection: 包含的PID数组/集合
     * queryExcludeCidArray/Collection: 排除的PID数组/集合
     */
    private transient long[] queryIncludePidArray = null, queryExcludePidArray = null;
    private transient Collection<Long> queryIncludePidCollection = null, queryExcludePidCollection = null;
    /**
     * 状态筛选条件（非持久化字段）
     * queryIncludeStatusArray/Collection: 包含的状态值数组/集合
     * queryExcludeStatusArray/Collection: 排除的状态值数组/集合
     */
    private transient int[] queryIncludeStatusArray = null, queryExcludeStatusArray = null;
    private transient Collection<Integer> queryIncludeStatusCollection = null, queryExcludeStatusCollection = null;
    /**
     * 归属信息筛选条件（非持久化字段）
     * queryIncludeBelongByCidArray/Collection: 包含的归属CID数组/集合
     * queryExcludeBelongByCidArray/Collection: 排除的归属CID数组/集合
     * queryIncludeBelongByTypeArray/Collection: 包含的归属类型数组/集合
     */
    private transient long[] queryIncludeBelongByCidArray = null, queryExcludeBelongByCidArray = null;
    private transient Collection<Long> queryIncludeBelongByCidCollection = null, queryExcludeBelongByCidCollection = null;
    private transient int[] queryIncludeBelongByTypeArray = null, queryExcludeBelongByTypeArray = null;

    /**
     * 创建者信息筛选条件（非持久化字段）
     * queryIncludeCreateByCidArray/Collection: 包含的创建者CID数组/集合
     * queryExcludeCreateByCidArray/Collection: 排除的创建者CID数组/集合
     * queryIncludeCreateByTypeArray/Collection: 包含的创建者类型数组/集合
     */
    private transient long[] queryIncludeCreateByCidArray = null, queryExcludeCreateByCidArray = null;
    private transient Collection<Long> queryIncludeCreateByCidCollection = null, queryExcludeCreateByCidCollection = null;
    private transient int[] queryIncludeCreateByTypeArray = null, queryExcludeCreateByTypeArray = null;
    /**
     * 最后更新者信息筛选条件（非持久化字段）
     * queryIncludeUpdateByCidArray/Collection: 包含的更新者CID数组/集合
     * queryExcludeUpdateByCidArray/Collection: 排除的更新者CID数组/集合
     * queryIncludeUpdateByTypeArray/Collection: 包含的更新者类型数组/集合
     */
    private transient long[] queryIncludeUpdateByCidArray = null, queryExcludeUpdateByCidArray = null;
    private transient Collection<Long> queryIncludeUpdateByCidCollection = null, queryExcludeUpdateByCidCollection = null;
    private transient int[] queryIncludeUpdateByTypeArray = null, queryExcludeUpdateByTypeArray = null;
    /**
     * 访问者信息筛选条件（非持久化字段）
     * queryIncludeAccessByCidArray/Collection: 包含的访问者CID数组/集合
     * queryExcludeAccessByCidArray/Collection: 排除的访问者CID数组/集合
     * queryIncludeAccessByTypeArray/Collection: 包含的访问者类型数组/集合
     */
    private transient long[] queryIncludeAccessByCidArray = null, queryExcludeAccessByCidArray = null;
    private transient Collection<Long> queryIncludeAccessByCidCollection = null, queryExcludeAccessByCidCollection = null;
    private transient int[] queryIncludeAccessByTypeArray = null, queryExcludeAccessByTypeArray = null;

    /**
     * 定义时间查询条件（非持久化字段）
     * queryDefineTimeStart/End: 起止时间范围
     * queryDefineTimeBegin/Finish: 开始结束时间点
     * queryDefineTimeBefore/After: 临界时间点前后
     */
    private transient LocalDateTime queryDefineTimeStart = null, queryDefineTimeEnd = null;
    private transient LocalDateTime queryDefineTimeBegin = null, queryDefineTimeFinish = null;
    private transient LocalDateTime queryDefineTimeBefore = null, queryDefineTimeAfter = null;

    /**
     * 创建时间查询条件（非持久化字段）
     * queryCreateTimeStart/End: 起止时间范围
     * queryCreateTimeBegin/Finish: 开始结束时间点
     * queryCreateTimeBefore/After: 临界时间点前后
     */
    private transient LocalDateTime queryCreateTimeStart = null, queryCreateTimeEnd = null;
    private transient LocalDateTime queryCreateTimeBegin = null, queryCreateTimeFinish = null;
    private transient LocalDateTime queryCreateTimeBefore = null, queryCreateTimeAfter = null;

    /**
     * 更新时间查询条件（非持久化字段）
     * queryUpdateTimeStart/End: 起止时间范围
     * queryUpdateTimeBegin/Finish: 开始结束时间点
     * queryUpdateTimeBefore/After: 临界时间点前后
     */
    private transient LocalDateTime queryUpdateTimeStart = null, queryUpdateTimeEnd = null;
    private transient LocalDateTime queryUpdateTimeBegin = null, queryUpdateTimeFinish = null;
    private transient LocalDateTime queryUpdateTimeBefore = null, queryUpdateTimeAfter = null;

    /**
     * 访问时间查询条件（非持久化字段）
     * queryAccessTimeStart/End: 起止时间范围
     * queryAccessTimeBegin/Finish: 开始结束时间点
     * queryAccessTimeBefore/After: 临界时间点前后
     */
    private transient LocalDateTime queryAccessTimeStart = null, queryAccessTimeEnd = null;
    private transient LocalDateTime queryAccessTimeBegin = null, queryAccessTimeFinish = null;
    private transient LocalDateTime queryAccessTimeBefore = null, queryAccessTimeAfter = null;
    /**
     * 动态查询条件（非持久化字段）
     */
    private transient Map<String, Object> queryCondition = null;

    /**
     * 获取动态查询条件Map，若为空则初始化
     *
     * @return 条件Map对象
     */
    public final Map<String, Object> getQueryCondition() {
        if (queryCondition == null) {
            queryCondition = new HashMap<>(4, 0.8f);
        }
        return queryCondition;
    }

    /**
     * 增加实体版本号
     *
     * @return 返回增加后的版本号
     */
    public final int nextVersion() {
        // 增加版本号并返回当前对象
        version++;
        return version;
    }

    /**
     * 实现基于主键ID的自然排序逻辑
     *
     * @param that 要比较的另一个通用实体
     * @return 排序结果值：负数表示当前实体较小，正数表示较大，0表示相等
     */
    @Override
    public int compareTo(Entity that) {
        Long idl = this.getCid();
        Long idr = that.getCid();
        if (idl == null) return -1;
        if (idr == null) return 1;
        return idl.compareTo(idr);
    }

    public EssentialEntity() {
        TABLE.computeIfAbsent(thisClass$(), clz -> {
            List<Column> columns = new LinkedList<>();
            columns.add(new Column("CID", this::getCid));
            columns.add(new Column("TID", this::getTid));
            if (isHierarchy()) {
                columns.add(new Column("PID", this::getPid));
                columns.add(new Column("LVL", this::getLvl));
            }
            columns.add(new Column("VERSION", this::getVersion));
            columns.add(new Column("DATA_SORTED", this::getDataSorted));
            columns.add(new Column("DATA_STATUS", this::getDataStatus));
            columns.add(new Column("REMOVAL_FLAG", this::getRemovalFlag));
            columns.add(new Column("VISIBLE_FLAG", this::getVisibleFlag));
            columns.add(new Column("DISPLAY_NAME", this::getDisplayName));
            columns.add(new Column("DISPLAY_DESC", this::getDisplayDesc));

            String[] configColumnName = configColumnName();
            if (configColumnName != null && configColumnName.length > 0) {
                ValueFetcher[] configColumnFetcher = configColumnFetcher();
                if (configColumnFetcher == null || configColumnFetcher.length != configColumnName.length) {
                    throw new PersistenceException("The number of column names does not match the number of column data fetchers");
                }
                for (int i = 0; i < configColumnName.length; i++) {
                    columns.add(new Column(configColumnName[i], configColumnFetcher[i]));
                }
            }

            columns.add(new Column("DEFINE_TIME", this::getDefineTime));
            columns.add(new Column("BELONG_BY_CID", this::getBelongByCid));
            columns.add(new Column("BELONG_BY_TYPE", this::getBelongByType));
            columns.add(new Column("BELONG_BY_NAME", this::getBelongByName));

            columns.add(new Column("CREATE_TIME", this::getCreateTime));
            columns.add(new Column("CREATE_BY_CID", this::getCreateByCid));
            columns.add(new Column("CREATE_BY_TYPE", this::getCreateByType));
            columns.add(new Column("CREATE_BY_NAME", this::getCreateByName));

            columns.add(new Column("UPDATE_TIME", this::getUpdateTime));
            columns.add(new Column("UPDATE_BY_CID", this::getUpdateByCid));
            columns.add(new Column("UPDATE_BY_TYPE", this::getUpdateByType));
            columns.add(new Column("UPDATE_BY_NAME", this::getUpdateByName));

            columns.add(new Column("ACCESS_TIME", this::getAccessTime));
            columns.add(new Column("ACCESS_BY_CID", this::getAccessByCid));
            columns.add(new Column("ACCESS_BY_TYPE", this::getAccessByType));
            columns.add(new Column("ACCESS_BY_NAME", this::getAccessByName));
            return new Table("", columns);
        });
    }

    protected String[] configColumnName() {
        return null;
    }

    protected ValueFetcher[] configColumnFetcher() {
        return null;
    }

    protected boolean isHierarchy() {
        return false;
    }

    public String tableName() {
        return TABLE.get(thisClass$()).name();
    }

    public String[] columnNames() {
        return TABLE.get(thisClass$()).columns().stream().map(Column::name).toArray(String[]::new);
    }

    public ValueFetcher[] columnValues() {
        return TABLE.get(thisClass$()).columns().stream().map(Column::fetcher).toArray(ValueFetcher[]::new);
    }

    /**
     * 返回包含查询条件的字符串表示形式
     *
     * @return 包含所有查询字段的字符串
     */
    public String queryString() {
        return dataString() +
                "  querySearchValue=" + querySearchValue + "\n" +
                "  queryPageNumber=" + queryPageNumber + "\n" +
                "  queryPageAmount=" + queryPageAmount + "\n" +
                "  querySortedDescending=" + querySortedDescending + "\n" +
                "  queryIncludeCidArray=" + Arrays.toString(queryIncludeCidArray) + "\n" +
                "  queryExcludeCidArray=" + Arrays.toString(queryExcludeCidArray) + "\n" +
                "  queryIncludeCidCollection=" + queryIncludeCidCollection + "\n" +
                "  queryExcludeCidCollection=" + queryExcludeCidCollection + "\n" +
                "  queryIncludePidArray=" + Arrays.toString(queryIncludePidArray) + "\n" +
                "  queryExcludePidArray=" + Arrays.toString(queryExcludePidArray) + "\n" +
                "  queryIncludePidCollection=" + queryIncludePidCollection + "\n" +
                "  queryExcludePidCollection=" + queryExcludePidCollection + "\n" +
                "  queryIncludeStatusArray=" + Arrays.toString(queryIncludeStatusArray) + "\n" +
                "  queryExcludeStatusArray=" + Arrays.toString(queryExcludeStatusArray) + "\n" +
                "  queryIncludeStatusCollection=" + queryIncludeStatusCollection + "\n" +
                "  queryExcludeStatusCollection=" + queryExcludeStatusCollection + "\n" +
                "  queryIncludeBelongByCidArray=" + Arrays.toString(queryIncludeBelongByCidArray) + "\n" +
                "  queryExcludeBelongByCidArray=" + Arrays.toString(queryExcludeBelongByCidArray) + "\n" +
                "  queryIncludeBelongByCidCollection=" + queryIncludeBelongByCidCollection + "\n" +
                "  queryExcludeBelongByCidCollection=" + queryExcludeBelongByCidCollection + "\n" +
                "  queryIncludeBelongByTypeArray=" + Arrays.toString(queryIncludeBelongByTypeArray) + "\n" +
                "  queryExcludeBelongByTypeArray=" + Arrays.toString(queryExcludeBelongByTypeArray) + "\n" +
                "  queryIncludeCreateByCidArray=" + Arrays.toString(queryIncludeCreateByCidArray) + "\n" +
                "  queryExcludeCreateByCidArray=" + Arrays.toString(queryExcludeCreateByCidArray) + "\n" +
                "  queryIncludeCreateByCidCollection=" + queryIncludeCreateByCidCollection + "\n" +
                "  queryExcludeCreateByCidCollection=" + queryExcludeCreateByCidCollection + "\n" +
                "  queryIncludeCreateByTypeArray=" + Arrays.toString(queryIncludeCreateByTypeArray) + "\n" +
                "  queryExcludeCreateByTypeArray=" + Arrays.toString(queryExcludeCreateByTypeArray) + "\n" +
                "  queryIncludeUpdateByCidArray=" + Arrays.toString(queryIncludeUpdateByCidArray) + "\n" +
                "  queryExcludeUpdateByCidArray=" + Arrays.toString(queryExcludeUpdateByCidArray) + "\n" +
                "  queryIncludeUpdateByCidCollection=" + queryIncludeUpdateByCidCollection + "\n" +
                "  queryExcludeUpdateByCidCollection=" + queryExcludeUpdateByCidCollection + "\n" +
                "  queryIncludeUpdateByTypeArray=" + Arrays.toString(queryIncludeUpdateByTypeArray) + "\n" +
                "  queryExcludeUpdateByTypeArray=" + Arrays.toString(queryExcludeUpdateByTypeArray) + "\n" +
                "  queryIncludeAccessByCidArray=" + Arrays.toString(queryIncludeAccessByCidArray) + "\n" +
                "  queryExcludeAccessByCidArray=" + Arrays.toString(queryExcludeAccessByCidArray) + "\n" +
                "  queryIncludeAccessByCidCollection=" + queryIncludeAccessByCidCollection + "\n" +
                "  queryExcludeAccessByCidCollection=" + queryExcludeAccessByCidCollection + "\n" +
                "  queryIncludeAccessByTypeArray=" + Arrays.toString(queryIncludeAccessByTypeArray) + "\n" +
                "  queryExcludeAccessByTypeArray=" + Arrays.toString(queryExcludeAccessByTypeArray) + "\n" +
                "  queryDefineTimeStart=" + queryDefineTimeStart + "\n" +
                "  queryDefineTimeEnd=" + queryDefineTimeEnd + "\n" +
                "  queryDefineTimeBegin=" + queryDefineTimeBegin + "\n" +
                "  queryDefineTimeFinish=" + queryDefineTimeFinish + "\n" +
                "  queryDefineTimeBefore=" + queryDefineTimeBefore + "\n" +
                "  queryDefineTimeAfter=" + queryDefineTimeAfter + "\n" +
                "  queryCreateTimeStart=" + queryCreateTimeStart + "\n" +
                "  queryCreateTimeEnd=" + queryCreateTimeEnd + "\n" +
                "  queryCreateTimeBegin=" + queryCreateTimeBegin + "\n" +
                "  queryCreateTimeFinish=" + queryCreateTimeFinish + "\n" +
                "  queryCreateTimeBefore=" + queryCreateTimeBefore + "\n" +
                "  queryCreateTimeAfter=" + queryCreateTimeAfter + "\n" +
                "  queryUpdateTimeStart=" + queryUpdateTimeStart + "\n" +
                "  queryUpdateTimeEnd=" + queryUpdateTimeEnd + "\n" +
                "  queryUpdateTimeBegin=" + queryUpdateTimeBegin + "\n" +
                "  queryUpdateTimeFinish=" + queryUpdateTimeFinish + "\n" +
                "  queryUpdateTimeBefore=" + queryUpdateTimeBefore + "\n" +
                "  queryUpdateTimeAfter=" + queryUpdateTimeAfter + "\n" +
                "  queryAccessTimeStart=" + queryAccessTimeStart + "\n" +
                "  queryAccessTimeEnd=" + queryAccessTimeEnd + "\n" +
                "  queryAccessTimeBegin=" + queryAccessTimeBegin + "\n" +
                "  queryAccessTimeFinish=" + queryAccessTimeFinish + "\n" +
                "  queryAccessTimeBefore=" + queryAccessTimeBefore + "\n" +
                "  queryAccessTimeAfter=" + queryAccessTimeAfter + "\n" +
                "  queryCondition=" + queryCondition + "\n";
    }

    /**
     * 返回基础数据字段的字符串表示形式
     *
     * @return 包含所有基础数据字段的字符串
     */
    public String dataString() {
        return "  cid=" + cid + "\n" +
                "  tid='" + tid + "'\n" +
                "  pid=" + pid + "\n" +
                "  lvl=" + lvl + "\n" +
                "  version=" + version + "\n" +
                "  dataSorted=" + dataSorted + "\n" +
                "  dataStatus=" + dataStatus + "\n" +
                "  removalFlag=" + removalFlag + "\n" +
                "  visibleFlag=" + visibleFlag + "\n" +
                "  displayName='" + displayName + "'\n" +
                "  displayDesc='" + displayDesc + "'\n" +
                "  defineTime=" + defineTime + "\n" +
                "  belongByCid=" + belongByCid + "\n" +
                "  belongByType=" + belongByType + "\n" +
                "  belongByName='" + belongByName + "'\n" +
                "  createTime=" + createTime + "\n" +
                "  createByCid=" + createByCid + "\n" +
                "  createByType=" + createByType + "\n" +
                "  createByName='" + createByName + "'\n" +
                "  updateTime=" + updateTime + "\n" +
                "  updateByCid=" + updateByCid + "\n" +
                "  updateByType=" + updateByType + "\n" +
                "  updateByName='" + updateByName + "'\n" +
                "  accessTime=" + accessTime + "\n" +
                "  accessByCid='" + accessByCid + "'\n" +
                "  accessByType='" + accessByType + "'\n" +
                "  accessByName='" + accessByName + "'\n";
    }

    /**
     * 返回实体对象的字符串表示形式
     *
     * @return 实体对象的字符串表示
     */
    @Override
    public String toString() {
        return "EssentialEntity{\n" + dataString() + "}\n";
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Setter
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final Entity setCid(Long cid) {
        this.cid = cid;
        return this$();
    }

    public final Entity setPid(Long pid) {
        this.pid = pid;
        return this$();
    }

    public final Entity setLvl(int lvl) {
        this.lvl = lvl;
        return this$();
    }

    public final Entity setVersion(int version) {
        this.version = version;
        return this$();
    }

    public final Entity setDataSorted(int dataSorted) {
        this.dataSorted = dataSorted;
        return this$();
    }

    public final Entity setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
        return this$();
    }

    public final Entity setRemovalFlag(Boolean removalFlag) {
        this.removalFlag = removalFlag;
        return this$();
    }

    public final Entity setVisibleFlag(Boolean visibleFlag) {
        this.visibleFlag = visibleFlag;
        return this$();
    }

    public final Entity setDisplayName(String displayName) {
        this.displayName = displayName;
        return this$();
    }

    public final Entity setDisplayDesc(String displayDesc) {
        this.displayDesc = displayDesc;
        return this$();
    }

    public final Entity setDefineTime(LocalDateTime defineTime) {
        this.defineTime = defineTime;
        return this$();
    }

    public final Entity setBelongByCid(Long belongByCid) {
        this.belongByCid = belongByCid;
        return this$();
    }

    public final Entity setBelongByType(Integer belongByType) {
        this.belongByType = belongByType;
        return this$();
    }

    public final Entity setBelongByName(String belongByName) {
        this.belongByName = belongByName;
        return this$();
    }

    public final Entity setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this$();
    }

    public final Entity setCreateByCid(Long createByCid) {
        this.createByCid = createByCid;
        return this$();
    }

    public final Entity setCreateByType(Integer createByType) {
        this.createByType = createByType;
        return this$();
    }

    public final Entity setCreateByName(String createByName) {
        this.createByName = createByName;
        return this$();
    }

    public final Entity setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this$();
    }

    public final Entity setUpdateByCid(Long updateByCid) {
        this.updateByCid = updateByCid;
        return this$();
    }

    public final Entity setUpdateByType(Integer updateByType) {
        this.updateByType = updateByType;
        return this$();
    }

    public final Entity setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
        return this$();
    }

    public final Entity setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
        return this$();
    }

    public final Entity setAccessByCid(String accessByCid) {
        this.accessByCid = accessByCid;
        return this$();
    }

    public final Entity setAccessByType(String accessByType) {
        this.accessByType = accessByType;
        return this$();
    }

    public final Entity setAccessByName(String accessByName) {
        this.accessByName = accessByName;
        return this$();
    }

    public final Entity setTid(String tid) {
        this.tid = tid;
        return this$();
    }

    public final Entity setQuerySearchValue(String querySearchValue) {
        this.querySearchValue = querySearchValue;
        return this$();
    }

    public final Entity setQueryPageNumber(int queryPageNumber) {
        this.queryPageNumber = queryPageNumber;
        return this$();
    }

    public final Entity setQueryPageAmount(int queryPageAmount) {
        this.queryPageAmount = queryPageAmount;
        return this$();
    }

    public final Entity setQueryIncludeCidArray(long... queryIncludeCidArray) {
        this.queryIncludeCidArray = queryIncludeCidArray;
        return this$();
    }

    public final Entity setQueryExcludeCidArray(long... queryExcludeCidArray) {
        this.queryExcludeCidArray = queryExcludeCidArray;
        return this$();
    }

    public final Entity setQuerySortedDescending(boolean querySortedDescending) {
        this.querySortedDescending = querySortedDescending;
        return this$();
    }

    public final Entity setQueryIncludeCidCollection(Collection<Long> queryIncludeCidCollection) {
        this.queryIncludeCidCollection = queryIncludeCidCollection;
        return this$();
    }

    public final Entity setQueryExcludeCidCollection(Collection<Long> queryExcludeCidCollection) {
        this.queryExcludeCidCollection = queryExcludeCidCollection;
        return this$();
    }

    public final Entity setQueryIncludePidArray(long... queryIncludePidArray) {
        this.queryIncludePidArray = queryIncludePidArray;
        return this$();
    }

    public final Entity setQueryExcludePidArray(long... queryExcludePidArray) {
        this.queryExcludePidArray = queryExcludePidArray;
        return this$();
    }

    public final Entity setQueryIncludePidCollection(Collection<Long> queryIncludePidCollection) {
        this.queryIncludePidCollection = queryIncludePidCollection;
        return this$();
    }

    public final Entity setQueryExcludePidCollection(Collection<Long> queryExcludePidCollection) {
        this.queryExcludePidCollection = queryExcludePidCollection;
        return this$();
    }

    public final Entity setQueryIncludeStatusArray(int... queryIncludeStatusArray) {
        this.queryIncludeStatusArray = queryIncludeStatusArray;
        return this$();
    }

    public final Entity setQueryExcludeStatusArray(int... queryExcludeStatusArray) {
        this.queryExcludeStatusArray = queryExcludeStatusArray;
        return this$();
    }

    public final Entity setQueryIncludeStatusCollection(Collection<Integer> queryIncludeStatusCollection) {
        this.queryIncludeStatusCollection = queryIncludeStatusCollection;
        return this$();
    }

    public final Entity setQueryExcludeStatusCollection(Collection<Integer> queryExcludeStatusCollection) {
        this.queryExcludeStatusCollection = queryExcludeStatusCollection;
        return this$();
    }

    public final Entity setQueryIncludeBelongByCidArray(long... queryIncludeBelongByCidArray) {
        this.queryIncludeBelongByCidArray = queryIncludeBelongByCidArray;
        return this$();
    }

    public final Entity setQueryExcludeBelongByCidArray(long... queryExcludeBelongByCidArray) {
        this.queryExcludeBelongByCidArray = queryExcludeBelongByCidArray;
        return this$();
    }

    public final Entity setQueryIncludeBelongByCidCollection(Collection<Long> queryIncludeBelongByCidCollection) {
        this.queryIncludeBelongByCidCollection = queryIncludeBelongByCidCollection;
        return this$();
    }

    public final Entity setQueryExcludeBelongByCidCollection(Collection<Long> queryExcludeBelongByCidCollection) {
        this.queryExcludeBelongByCidCollection = queryExcludeBelongByCidCollection;
        return this$();
    }

    public final Entity setQueryIncludeBelongByTypeArray(int... queryIncludeBelongByTypeArray) {
        this.queryIncludeBelongByTypeArray = queryIncludeBelongByTypeArray;
        return this$();
    }

    public final Entity setQueryExcludeBelongByTypeArray(int... queryExcludeBelongByTypeArray) {
        this.queryExcludeBelongByTypeArray = queryExcludeBelongByTypeArray;
        return this$();
    }

    public final Entity setQueryIncludeCreateByCidArray(long... queryIncludeCreateByCidArray) {
        this.queryIncludeCreateByCidArray = queryIncludeCreateByCidArray;
        return this$();
    }

    public final Entity setQueryExcludeCreateByCidArray(long... queryExcludeCreateByCidArray) {
        this.queryExcludeCreateByCidArray = queryExcludeCreateByCidArray;
        return this$();
    }

    public final Entity setQueryIncludeCreateByCidCollection(Collection<Long> queryIncludeCreateByCidCollection) {
        this.queryIncludeCreateByCidCollection = queryIncludeCreateByCidCollection;
        return this$();
    }

    public final Entity setQueryExcludeCreateByCidCollection(Collection<Long> queryExcludeCreateByCidCollection) {
        this.queryExcludeCreateByCidCollection = queryExcludeCreateByCidCollection;
        return this$();
    }

    public final Entity setQueryIncludeCreateByTypeArray(int... queryIncludeCreateByTypeArray) {
        this.queryIncludeCreateByTypeArray = queryIncludeCreateByTypeArray;
        return this$();
    }

    public final Entity setQueryExcludeCreateByTypeArray(int... queryExcludeCreateByTypeArray) {
        this.queryExcludeCreateByTypeArray = queryExcludeCreateByTypeArray;
        return this$();
    }

    public final Entity setQueryIncludeUpdateByCidArray(long... queryIncludeUpdateByCidArray) {
        this.queryIncludeUpdateByCidArray = queryIncludeUpdateByCidArray;
        return this$();
    }

    public final Entity setQueryExcludeUpdateByCidArray(long... queryExcludeUpdateByCidArray) {
        this.queryExcludeUpdateByCidArray = queryExcludeUpdateByCidArray;
        return this$();
    }

    public final Entity setQueryIncludeUpdateByCidCollection(Collection<Long> queryIncludeUpdateByCidCollection) {
        this.queryIncludeUpdateByCidCollection = queryIncludeUpdateByCidCollection;
        return this$();
    }

    public final Entity setQueryExcludeUpdateByCidCollection(Collection<Long> queryExcludeUpdateByCidCollection) {
        this.queryExcludeUpdateByCidCollection = queryExcludeUpdateByCidCollection;
        return this$();
    }

    public final Entity setQueryIncludeUpdateByTypeArray(int... queryIncludeUpdateByTypeArray) {
        this.queryIncludeUpdateByTypeArray = queryIncludeUpdateByTypeArray;
        return this$();
    }

    public final Entity setQueryExcludeUpdateByTypeArray(int... queryExcludeUpdateByTypeArray) {
        this.queryExcludeUpdateByTypeArray = queryExcludeUpdateByTypeArray;
        return this$();
    }

    public final Entity setQueryIncludeAccessByCidArray(long... queryIncludeAccessByCidArray) {
        this.queryIncludeAccessByCidArray = queryIncludeAccessByCidArray;
        return this$();
    }

    public final Entity setQueryExcludeAccessByCidArray(long... queryExcludeAccessByCidArray) {
        this.queryExcludeAccessByCidArray = queryExcludeAccessByCidArray;
        return this$();
    }

    public final Entity setQueryIncludeAccessByCidCollection(Collection<Long> queryIncludeAccessByCidCollection) {
        this.queryIncludeAccessByCidCollection = queryIncludeAccessByCidCollection;
        return this$();
    }

    public final Entity setQueryExcludeAccessByCidCollection(Collection<Long> queryExcludeAccessByCidCollection) {
        this.queryExcludeAccessByCidCollection = queryExcludeAccessByCidCollection;
        return this$();
    }

    public final Entity setQueryIncludeAccessByTypeArray(int... queryIncludeAccessByTypeArray) {
        this.queryIncludeAccessByTypeArray = queryIncludeAccessByTypeArray;
        return this$();
    }

    public final Entity setQueryExcludeAccessByTypeArray(int... queryExcludeAccessByTypeArray) {
        this.queryExcludeAccessByTypeArray = queryExcludeAccessByTypeArray;
        return this$();
    }

    public final Entity setQueryDefineTimeStart(LocalDateTime queryDefineTimeStart) {
        this.queryDefineTimeStart = queryDefineTimeStart;
        return this$();
    }

    public final Entity setQueryDefineTimeEnd(LocalDateTime queryDefineTimeEnd) {
        this.queryDefineTimeEnd = queryDefineTimeEnd;
        return this$();
    }

    public final Entity setQueryDefineTimeBegin(LocalDateTime queryDefineTimeBegin) {
        this.queryDefineTimeBegin = queryDefineTimeBegin;
        return this$();
    }

    public final Entity setQueryDefineTimeFinish(LocalDateTime queryDefineTimeFinish) {
        this.queryDefineTimeFinish = queryDefineTimeFinish;
        return this$();
    }

    public final Entity setQueryDefineTimeBefore(LocalDateTime queryDefineTimeBefore) {
        this.queryDefineTimeBefore = queryDefineTimeBefore;
        return this$();
    }

    public final Entity setQueryDefineTimeAfter(LocalDateTime queryDefineTimeAfter) {
        this.queryDefineTimeAfter = queryDefineTimeAfter;
        return this$();
    }

    public final Entity setQueryCreateTimeStart(LocalDateTime queryCreateTimeStart) {
        this.queryCreateTimeStart = queryCreateTimeStart;
        return this$();
    }

    public final Entity setQueryCreateTimeEnd(LocalDateTime queryCreateTimeEnd) {
        this.queryCreateTimeEnd = queryCreateTimeEnd;
        return this$();
    }

    public final Entity setQueryCreateTimeBegin(LocalDateTime queryCreateTimeBegin) {
        this.queryCreateTimeBegin = queryCreateTimeBegin;
        return this$();
    }

    public final Entity setQueryCreateTimeFinish(LocalDateTime queryCreateTimeFinish) {
        this.queryCreateTimeFinish = queryCreateTimeFinish;
        return this$();
    }

    public final Entity setQueryCreateTimeBefore(LocalDateTime queryCreateTimeBefore) {
        this.queryCreateTimeBefore = queryCreateTimeBefore;
        return this$();
    }

    public final Entity setQueryCreateTimeAfter(LocalDateTime queryCreateTimeAfter) {
        this.queryCreateTimeAfter = queryCreateTimeAfter;
        return this$();
    }

    public final Entity setQueryUpdateTimeStart(LocalDateTime queryUpdateTimeStart) {
        this.queryUpdateTimeStart = queryUpdateTimeStart;
        return this$();
    }

    public final Entity setQueryUpdateTimeEnd(LocalDateTime queryUpdateTimeEnd) {
        this.queryUpdateTimeEnd = queryUpdateTimeEnd;
        return this$();
    }

    public final Entity setQueryUpdateTimeBegin(LocalDateTime queryUpdateTimeBegin) {
        this.queryUpdateTimeBegin = queryUpdateTimeBegin;
        return this$();
    }

    public final Entity setQueryUpdateTimeFinish(LocalDateTime queryUpdateTimeFinish) {
        this.queryUpdateTimeFinish = queryUpdateTimeFinish;
        return this$();
    }

    public final Entity setQueryUpdateTimeBefore(LocalDateTime queryUpdateTimeBefore) {
        this.queryUpdateTimeBefore = queryUpdateTimeBefore;
        return this$();
    }

    public final Entity setQueryUpdateTimeAfter(LocalDateTime queryUpdateTimeAfter) {
        this.queryUpdateTimeAfter = queryUpdateTimeAfter;
        return this$();
    }

    public final Entity setQueryAccessTimeStart(LocalDateTime queryAccessTimeStart) {
        this.queryAccessTimeStart = queryAccessTimeStart;
        return this$();
    }

    public final Entity setQueryAccessTimeEnd(LocalDateTime queryAccessTimeEnd) {
        this.queryAccessTimeEnd = queryAccessTimeEnd;
        return this$();
    }

    public final Entity setQueryAccessTimeBegin(LocalDateTime queryAccessTimeBegin) {
        this.queryAccessTimeBegin = queryAccessTimeBegin;
        return this$();
    }

    public final Entity setQueryAccessTimeFinish(LocalDateTime queryAccessTimeFinish) {
        this.queryAccessTimeFinish = queryAccessTimeFinish;
        return this$();
    }

    public final Entity setQueryAccessTimeBefore(LocalDateTime queryAccessTimeBefore) {
        this.queryAccessTimeBefore = queryAccessTimeBefore;
        return this$();
    }

    public final Entity setQueryAccessTimeAfter(LocalDateTime queryAccessTimeAfter) {
        this.queryAccessTimeAfter = queryAccessTimeAfter;
        return this$();
    }

    private record Table(String name, List<Column> columns) {
    }

    private record Column(String name, ValueFetcher fetcher) {
    }

}
