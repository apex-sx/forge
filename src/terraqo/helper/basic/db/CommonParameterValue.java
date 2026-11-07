package potatoxf.helper.basic.db;

import potatoxf.helper.api.Tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author potatoxf
 * @date 2022/05/13
 */
public class CommonParameterValue implements Comparable<CommonParameterValue> {

  /** Token Id */
  @Nonnull private final AtomicInteger tokenId = new AtomicInteger(-1);
  /** 引用键 */
  @Nonnull private final CommonParameterKey key;
  /** 字符串值 */
  @Nonnull private final AtomicReference<String> stringValue = new AtomicReference<>(null);
  /** 数字值 */
  @Nonnull private final AtomicReference<Number> numberValue = new AtomicReference<>(null);
  /** */
  @Nonnull private final AtomicInteger sorted = new AtomicInteger(0);
  /** 描述 */
  @Nonnull private final AtomicReference<String> description = new AtomicReference<>(null);

  private CommonParameterValue(
      @Nonnull final CommonParameterKey key,
      final int tokenId,
      @Nullable final String stringValue,
      @Nullable final Number numberValue,
      final int sorted,
      @Nullable final String description) {
    if (key == null) {
      throw new IllegalArgumentException("The CommonParameterKey must be no null");
    }
    this.key = key;
    this.tokenId.set(tokenId);
    this.stringValue.set(stringValue);
    this.numberValue.set(numberValue);
    this.sorted.set(sorted);
    this.description.set(description);
  }

  @Nonnull
  static CommonParameterValue of(
      @Nonnull final CommonParameterKey key, final int ref, final int tokenId) {
    return of(CommonParameterKey.of(key, ref), tokenId);
  }

  @Nonnull
  static CommonParameterValue of(@Nonnull final CommonParameterKey key, final int tokenId) {
    return of(key, tokenId, null, null, 0, null);
  }

  @Nonnull
  static CommonParameterValue of(
      @Nonnull final CommonParameterKey key,
      final int tokenId,
      @Nullable final String stringValue,
      @Nullable final Number numberValue) {
    return of(key, tokenId, stringValue, numberValue, 0, null);
  }

  @Nonnull
  static CommonParameterValue of(
      @Nonnull final CommonParameterKey key,
      final int tokenId,
      @Nullable final String stringValue,
      @Nullable final Number numberValue,
      final int sorted,
      @Nullable final String description) {
    return new CommonParameterValue(key, tokenId, stringValue, numberValue, sorted, description);
  }

  @Nonnull
  public CommonParameterKey getKey() {
    return key;
  }

  @Nullable
  public String getModule() {
    return key.getModule();
  }

  @Nullable
  public String getType() {
    return key.getType();
  }

  @Nullable
  public String getName() {
    return key.getName();
  }

  @Nullable
  public String getPerson() {
    return key.getPerson();
  }

  public boolean isEnumerable() {
    return key.isEnumerable();
  }

  public int getRef() {
    return key.getRef();
  }

  public int getTokenId() {
    return tokenId.get();
  }

  public void setTokenId(int tokenId) {
    this.tokenId.set(tokenId);
  }

  @Nullable
  public String getStringValue() {
    return stringValue.get();
  }

  public void setStringValue(String stringValue) {
    this.stringValue.set(stringValue);
  }

  @Nullable
  public String getStringValue(@Nullable final String defaultStringValue) {
    return Tools.safeValue(stringValue.get(), defaultStringValue);
  }

  @Nullable
  public Number getNumberValue() {
    return numberValue.get();
  }

  public void setNumberValue(Number numberValue) {
    this.numberValue.set(numberValue);
  }

  @Nullable
  public Number getNumberValue(Number defaultNumberValue) {
    return Tools.safeValue(numberValue.get(), defaultNumberValue);
  }

  public int getSorted() {
    return sorted.get();
  }

  public void setSorted(int sorted) {
    this.sorted.set(sorted);
  }

  public String getDescription() {
    return description.get();
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public String getDescription(String defaultDescription) {
    return Tools.safeValue(description.get(), defaultDescription);
  }

  @Override
  public int compareTo(CommonParameterValue other) {
    return Integer.compare(sorted.get(), other.sorted.get());
  }

  public void update() {}
}
