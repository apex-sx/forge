package potatoxf.helper.api.map;

import potatoxf.helper.api.HelperOnCollection;
import potatoxf.helper.api.ValueReferenceGetter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author potatoxf
 * @date 2021/7/27
 */
public class Parametric extends CaseInsensitiveMap<String, Object> implements ValueReferenceGetter {

  public static Parametric of(Map<String, ?> map) {
    Parametric parametric = new Parametric();
    parametric.putAll(map);
    return parametric;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public boolean getBooleanValue(boolean defaultValue) {
    return getBooleanValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public char getCharValue(char defaultValue) {
    return getCharValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public byte getByteValue(byte defaultValue) {
    return getByteValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public short getShortValue(short defaultValue) {
    return getShortValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public int getIntValue(int defaultValue) {
    return getIntValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public long getLongValue(long defaultValue) {
    return getLongValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public float getFloatValue(float defaultValue) {
    return getFloatValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public double getDoubleValue(double defaultValue) {
    return getDoubleValue(null, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public boolean getBooleanValue(String key, boolean defaultValue) {
    return HelperOnCollection.getBooleanValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public char getCharValue(String key, char defaultValue) {
    return HelperOnCollection.getCharValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public byte getByteValue(String key, byte defaultValue) {
    return HelperOnCollection.getByteValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public short getShortValue(String key, short defaultValue) {
    return HelperOnCollection.getShortValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public int getIntValue(String key, int defaultValue) {
    return HelperOnCollection.getIntValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public long getLongValue(String key, long defaultValue) {
    return HelperOnCollection.getLongValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public float getFloatValue(String key, float defaultValue) {
    return HelperOnCollection.getFloatValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  @Override
  public double getDoubleValue(String key, double defaultValue) {
    return HelperOnCollection.getDoubleValue(this, key, defaultValue);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Boolean getBooleanValue() {
    return getBooleanValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Character getCharacterValue() {
    return getCharacterValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Byte getByteValue() {
    return getByteValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Short getShortValue() {
    return getShortValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Integer getIntegerValue() {
    return getIntegerValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Long getLongValue() {
    return getLongValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Float getFloatValue() {
    return getFloatValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Double getDoubleValue() {
    return getDoubleValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Boolean getBooleanValue(String key) {
    return HelperOnCollection.getBooleanValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Character getCharacterValue(String key) {
    return HelperOnCollection.getCharacterValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Byte getByteValue(String key) {
    return HelperOnCollection.getByteValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Short getShortValue(String key) {
    return HelperOnCollection.getShortValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Integer getIntegerValue(String key) {
    return HelperOnCollection.getIntegerValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Long getLongValue(String key) {
    return HelperOnCollection.getLongValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Float getFloatValue(String key) {
    return HelperOnCollection.getFloatValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Double getDoubleValue(String key) {
    return HelperOnCollection.getDoubleValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public BigInteger getBigIntegerValue() {
    return getBigIntegerValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public BigDecimal getBigDecimalValue() {
    return getBigDecimalValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public String getStringValue() {
    return getStringValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Object getObjectValue() {
    return getObjectValue(null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public BigInteger getBigIntegerValue(String key) {
    return HelperOnCollection.getBigIntegerValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public BigDecimal getBigDecimalValue(String key) {
    return HelperOnCollection.getBigDecimalValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public String getStringValue(String key) {
    return HelperOnCollection.getStringValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public String getStringValue(String key, String defaultValue) {
    return HelperOnCollection.getStringValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Object getObjectValue(String key) {
    return HelperOnCollection.getObjectValue(this, key);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  @Override
  public Object getObjectValue(String key, Object defaultValue) {
    return HelperOnCollection.getObjectValue(this, key, defaultValue);
  }
}
