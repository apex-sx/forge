package pxf.toolkit.basic.lang;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import pxf.toolkit.basic.util.Get;
import pxf.toolkit.basic.util.TimeHelper;

/**
 * 雪花ID算法
 *
 * @author potatoxf
 * @date 2021/3/25
 */
public final class Snowflake {

  private static final long DEFAULT_START_TIMESTAMP = 1610713610939L;
  /** 最大字典ID长度 */
  private static final int DICT_ID_AREA_LNE = 5;
  /** 最大字典ID */
  private static final int MAX_DICT_ID = ~(-1 << DICT_ID_AREA_LNE);
  /** 最大ID长度 */
  private static final int ID_AREA_LNE = 5;
  /** 最大ID */
  private static final int MAX_ID = ~(-1 << ID_AREA_LNE);
  /** 序列在ID中占的位数 */
  private static final int SEQUENCE_LEN = 12;
  /** 生成序列ID的掩码(12位所对应的最大整数值)，这里为4095 (0b111111111111=0xfff=4095) */
  private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_LEN);
  /** 机器ID左移位数 - 12 (即末sequence所占用的位数) */
  private static final int ID_MOVE_BITS = SEQUENCE_LEN;
  /** 数据标识id左移位数 - 17(12+5) */
  private static final int DICT_ID_MOVE_BITS = ID_MOVE_BITS + ID_AREA_LNE;
  /** 时间截向 左移位数 - 22(5+5+12) */
  private static final int TIMESTAMP_MOVE_BITS = DICT_ID_MOVE_BITS + DICT_ID_AREA_LNE;
  /** 初始化标志 */
  private static final AtomicBoolean INIT_FLAG = new AtomicBoolean(false);
  /** 缓存雪花ID生成器 */
  private static final Map<String, Snowflake> CACHE = new ConcurrentHashMap<>(2);
  /** 默认实例 */
  public static final Snowflake INSTANCE = of(0, 0);
  /** 起始时间 */
  private final long systemStartTimestamp;
  /** 目录ID(0~31) */
  private final int dictIdBitValue;
  /** 数据中心ID(0~31) */
  private final int idBitValue;
  /** 毫秒内序列(0~4095) */
  private long sequence = 0L;
  /** 上次生成ID的时间截 */
  private long lastTimestamp = -1L;

  /**
   * 构造函数
   *
   * @param systemStartTimestamp 系统开始时间
   * @param dictId 工作ID (0~31)
   * @param id 数据中心ID (0~31)
   */
  private Snowflake(long systemStartTimestamp, int dictId, int id) {
    this.systemStartTimestamp = systemStartTimestamp;
    this.dictIdBitValue = dictId << DICT_ID_MOVE_BITS;
    this.idBitValue = id << ID_MOVE_BITS;
  }

  /**
   * 创建雪花算法生成器 {@code Snowflake}设置开始时间，并且只能设置一次
   *
   * @param dictId 分类ID
   * @param id ID
   */
  public static synchronized Snowflake of(int dictId, int id) {
    return Snowflake.of(DEFAULT_START_TIMESTAMP, dictId, id);
  }

  /**
   * 创建雪花算法生成器 {@code Snowflake}设置开始时间，并且只能设置一次
   *
   * @param systemStartTimestamp 系统开始时间戳
   * @param dictId 分类ID
   * @param id ID
   */
  public static synchronized Snowflake of(long systemStartTimestamp, int dictId, int id) {
    if (dictId > DICT_ID_AREA_LNE || dictId < 0) {
      throw new IllegalArgumentException(
          String.format(
              "The dictionary id can't be greater than %d or less than 0", DICT_ID_AREA_LNE));
    }
    if (id > ID_AREA_LNE || id < 0) {
      throw new IllegalArgumentException(
          String.format("DataCenter Id can't be greater than %d or less than 0", ID_AREA_LNE));
    }
    if (systemStartTimestamp > System.currentTimeMillis()) {
      throw new IllegalArgumentException(
          "The system start timestamp must be less then current timestamp");
    }
    String key = dictId + "-" + id;
    if (CACHE.containsKey(key)) {
      return CACHE.get(key);
    }
    Snowflake snowflake = new Snowflake(systemStartTimestamp, dictId, id);
    CACHE.put(key, snowflake);
    return snowflake;
  }

  /**
   * 线程安全的获得下一个ID的方法
   *
   * @return 返回long类型的ID
   */
  public synchronized long nextId() {
    final long timestamp = this.generatorTimeMillis();
    // 上次生成ID的时间截
    this.lastTimestamp = timestamp;
    return ((timestamp - systemStartTimestamp) << TIMESTAMP_MOVE_BITS)
        | dictIdBitValue
        | idBitValue
        | sequence;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dictIdBitValue, idBitValue);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Snowflake snowflake = (Snowflake) object;
    return dictIdBitValue == snowflake.dictIdBitValue && idBitValue == snowflake.idBitValue;
  }

  @Override
  public String toString() {
    return (dictIdBitValue >> DICT_ID_AREA_LNE) + "-" + (idBitValue >> ID_AREA_LNE);
  }

  private synchronized long generatorTimeMillis() {
    long timestamp = TimeHelper.currentTimeMillis(lastTimestamp);
    // 如果是同一时间生成的，则进行毫秒内序列
    if (this.lastTimestamp == timestamp) {
      this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
      // 毫秒内序列溢出 即 序列 > 4095
      if (this.sequence == 0) {
        // 阻塞到下一个毫秒,获得新的时间戳
        timestamp = TimeHelper.nextTimeMillis(lastTimestamp);
      }
    }
    // 时间戳改变，毫秒内序列重置
    else {
      this.sequence = 0L;
    }
    return timestamp;
  }
}
