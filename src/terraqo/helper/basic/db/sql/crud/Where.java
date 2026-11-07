package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.Whether;
import potatoxf.helper.api.CharSequenceAppendable;
import potatoxf.helper.api.base.ImmutableTriple;
import potatoxf.helper.api.This;
import potatoxf.helper.api.iterate.ObjectIterable;
import potatoxf.helper.basic.db.sql.CommonKeyWord;
import potatoxf.helper.basic.db.sql.CompareKeyWord;
import potatoxf.helper.basic.db.type.DatabaseType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author potatoxf
 * @date 2022/6/22
 */
public abstract class Where<T extends Where<T>> extends CurdSetting<T>
    implements This<T>, SqlCreator {
  private final List<Object> wheres = new LinkedList<>();
  private boolean isOr = false;
  private List<ImmutableTriple<String, CompareKeyWord, Object>> conditions = new LinkedList<>();

  protected Where(String tableName) {
    super(tableName);
  }

  private static boolean isNoEmptyMaybeString(Object object) {
    return (object instanceof String && Whether.noEmpty((String) object)) || object != null;
  }

  private static boolean isNoEmptyMaybeMultiValue(Object object) {
    return (object instanceof Collection && Whether.noEmpty((Collection<?>) object))
        || (Whether.arrayObj(object) && Array.getLength(object) != 0)
        || object != null;
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T eq(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.EQ, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T eqIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.EQ, value, Where::isNoEmptyMaybeString);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T eqIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.EQ, value, condition);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noEq(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.NO_EQ, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noEqIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NO_EQ, value, Where::isNoEmptyMaybeString);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noEqIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.NO_EQ, value, condition);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gt(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.GT, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gtIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.GT, value, Where::isNoEmptyMaybeString);
  }
  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gtIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.GT, value, condition);
  }
  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gtEq(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.GT_EQ, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gtEqIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.GT_EQ, value, Where::isNoEmptyMaybeString);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T gtEqIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.GT_EQ, value, condition);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T lt(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LT, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T ltIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LT, value, Where::isNoEmptyMaybeString);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T ltIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LT, value, condition);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T ltEq(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LT_EQ, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T ltEqIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.LT_EQ, value, Where::isNoEmptyMaybeString);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T ltEqIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LT_EQ, value, condition);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T like(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LIKE, value, null, v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T likeIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.LIKE, value, Where::isNoEmptyMaybeString, v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T likeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.LIKE, value, condition, v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T leftLike(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LIKE, value, null, v -> "%" + v);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T leftLikeIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.LIKE, value, Where::isNoEmptyMaybeString, v -> "%" + v);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T leftLikeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LIKE, value, condition, v -> "%" + v);
  }
  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T rightLike(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LIKE, value, null, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T rightLikeIfNoEmpty(
      @Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.LIKE, value, Where::isNoEmptyMaybeString, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T rightLikeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(columnName, CompareKeyWord.LIKE, value, condition, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notLike(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, null, v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notLikeIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName,
        CompareKeyWord.NOT_LIKE,
        value,
        Where::isNoEmptyMaybeString,
        v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noLikeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, condition, v -> "%" + v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notLeftLike(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.NOT_LIKE, value, null, v -> "%" + v);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notLeftLikeIfNoEmpty(
      @Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, Where::isNoEmptyMaybeString, v -> "%" + v);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noLeftLikeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, condition, v -> "%" + v);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notRightLike(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.NOT_LIKE, value, null, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notRightLikeIfNoEmpty(
      @Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, Where::isNoEmptyMaybeString, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T noRightLikeIfCondition(
      @Nonnull final String columnName,
      @Nullable final Object value,
      @Nullable Predicate<Object> condition) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_LIKE, value, condition, v -> v + "%");
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T in(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.IN, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T inIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.IN, value, Where::isNoEmptyMaybeMultiValue);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notIn(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(columnName, CompareKeyWord.NOT_IN, value, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public final T notInIfNoEmpty(@Nonnull final String columnName, @Nullable final Object value) {
    return buildConditionIfMatch(
        columnName, CompareKeyWord.NOT_IN, value, Where::isNoEmptyMaybeMultiValue);
  }
  /**
   * 设置条件
   *
   * @param columnName 列名
   * @return {@code this}
   */
  @Nonnull
  public final T isNull(@Nonnull final String columnName) {
    return buildConditionIfMatch(columnName, CompareKeyWord.IS_NULL, null, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @return {@code this}
   */
  @Nonnull
  public final T isNotNull(@Nonnull final String columnName) {
    return buildConditionIfMatch(columnName, CompareKeyWord.IS_NOT_NULL, null, null);
  }

  @Nonnull
  public final T toAnd() {
    if (!conditions.isEmpty()) {
      if (isOr) {
        wheres.add(conditions);
        conditions = new LinkedList<>();
      }
    }
    isOr = false;
    return this$();
  }

  @Nonnull
  public final T toOr() {
    if (!conditions.isEmpty()) {
      if (!isOr) {
        wheres.addAll(conditions);
        conditions.clear();
      }
    }
    isOr = true;
    return this$();
  }

  /**
   * 创建SQL
   *
   * @param databaseType 数据库类型
   * @param charSequenceAppendable 字符串构建器
   * @return 返回字符串参数列表
   */
  @Nonnull
  @Override
  public List<Object> createSql(
      @Nullable final DatabaseType databaseType,
      @Nonnull final CharSequenceAppendable charSequenceAppendable) {
    List<Object> parameters = new ArrayList<>();
    charSequenceAppendable
        .append(getKeywordDelimiter())
        .append(CommonKeyWord.WHERE.getName(databaseType))
        .append(getKeywordDelimiter());
    if (wheres.isEmpty()) {
      appendConditionSegmentList(
          databaseType,
          charSequenceAppendable,
          null,
          null,
          CommonKeyWord.AND.getName(databaseType),
          conditions,
          parameters);
    } else {
      appendObject(
          databaseType,
          charSequenceAppendable,
          CommonKeyWord.AND.getName(databaseType),
          wheres,
          parameters);
      if (!conditions.isEmpty()) {
        charSequenceAppendable
            .append(getKeywordDelimiter())
            .append(CommonKeyWord.AND.getName(databaseType))
            .append(getKeywordDelimiter());
        appendConditionSegmentList(
            databaseType,
            charSequenceAppendable,
            null,
            null,
            CommonKeyWord.AND.getName(databaseType),
            conditions,
            parameters);
      }
    }
    return parameters;
  }

  @SuppressWarnings("unchecked")
  private void appendObject(
      @Nullable final DatabaseType databaseType,
      @Nonnull final CharSequenceAppendable csa,
      @Nonnull final String join,
      @Nonnull final List<Object> objectList,
      @Nonnull final List<Object> parameters) {
    Object object = objectList.get(0);
    if (object instanceof List) {
      appendConditionSegmentList(
          databaseType,
          csa,
          "(",
          ")",
          CommonKeyWord.OR.getName(databaseType),
          (List<ImmutableTriple<String, CompareKeyWord, Object>>) object,
          parameters);
    } else {
      appendConditionSegment(
          databaseType, csa, (ImmutableTriple<String, CompareKeyWord, Object>) object, parameters);
    }
    int size = objectList.size();
    for (int i = 1; i < size; i++) {
      object = objectList.get(i);
      csa.append(getKeywordDelimiter()).append(join).append(getKeywordDelimiter());
      if (object instanceof List) {
        appendConditionSegmentList(
            databaseType,
            csa,
            "(",
            ")",
            CommonKeyWord.OR.getName(databaseType),
            (List<ImmutableTriple<String, CompareKeyWord, Object>>) object,
            parameters);
      } else {
        appendConditionSegment(
            databaseType,
            csa,
            (ImmutableTriple<String, CompareKeyWord, Object>) object,
            parameters);
      }
    }
  }

  private void appendConditionSegmentList(
      @Nullable final DatabaseType databaseType,
      @Nonnull final CharSequenceAppendable csa,
      @Nullable final String prefix,
      @Nullable final String suffix,
      @Nonnull final String join,
      @Nonnull final List<ImmutableTriple<String, CompareKeyWord, Object>> immutableTripleList,
      @Nonnull final List<Object> parameters) {
    int size = immutableTripleList.size();
    if (size == 1) {
      appendConditionSegment(databaseType, csa, immutableTripleList.get(0), parameters);
    } else {
      if (prefix != null) {
        csa.append(prefix);
      }
      appendConditionSegment(databaseType, csa, immutableTripleList.get(0), parameters);
      for (int i = 1; i < size; i++) {
        csa.append(getKeywordDelimiter()).append(join).append(getKeywordDelimiter());
        appendConditionSegment(databaseType, csa, immutableTripleList.get(i), parameters);
      }
      if (suffix != null) {
        csa.append(suffix);
      }
    }
  }

  private void appendConditionSegment(
      @Nullable final DatabaseType databaseType,
      @Nonnull final CharSequenceAppendable csa,
      @Nonnull final ImmutableTriple<String, CompareKeyWord, Object> immutableTriple,
      @Nonnull final List<Object> parameters) {
    Object value = immutableTriple.getThirdElement();
    CompareKeyWord compareKeyWord = immutableTriple.getSecondElement();
    if (compareKeyWord == null) {
      throw new IllegalArgumentException("The Compare keyword is null");
    }
    String compareKeyWordName = compareKeyWord.getName(databaseType);
    if (value == null) {
      if (compareKeyWord.isNoValue()) {
        csa.append(immutableTriple.getFirstElement())
            .append(getKeywordDelimiter())
            .append(compareKeyWordName);
      } else {
        throw new IllegalArgumentException(
            "The value of ["
                + immutableTriple.getFirstElement()
                + " "
                + compareKeyWordName
                + " ?] is null");
      }
    } else {
      csa.append(immutableTriple.getFirstElement())
          .append(getKeywordDelimiter())
          .append(compareKeyWordName)
          .append(getKeywordDelimiter());
      if (compareKeyWord == CompareKeyWord.IN || compareKeyWord == CompareKeyWord.NOT_IN) {
        csa.append("(");
        Iterator<Object> iterator = new ObjectIterable(value).iterator();
        Object object = iterator.next();
        csa.append(getParameterPlaceholder());
        parameters.add(object);
        while (iterator.hasNext()) {
          object = iterator.next();
          csa.append(getFieldDelimiter()).append(" ").append(getParameterPlaceholder());
          parameters.add(object);
        }
        csa.append(")");
      } else {
        csa.append(getParameterPlaceholder());
        parameters.add(value);
      }
    }
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param keyWord 关键字
   * @param value 值
   * @param predicate
   * @return {@code this}
   */
  @Nonnull
  private T buildConditionIfMatch(
      @Nonnull final String columnName,
      @Nonnull final CompareKeyWord keyWord,
      @Nullable final Object value,
      @Nullable final Predicate<Object> predicate) {
    return buildConditionIfMatch(columnName, keyWord, value, predicate, null);
  }

  /**
   * 设置条件
   *
   * @param columnName 列名
   * @param keyWord 关键字
   * @param value 值
   * @param predicate
   * @return {@code this}
   */
  @Nonnull
  private T buildConditionIfMatch(
      @Nonnull final String columnName,
      @Nonnull final CompareKeyWord keyWord,
      @Nullable final Object value,
      @Nullable final Predicate<Object> predicate,
      @Nullable final Function<Object, Object> valueHandler) {
    Object v = value;
    if (predicate == null) {
      if (value == null) {
        if (keyWord.isNoValue()) {
          conditions.add(new ImmutableTriple<>(columnName, keyWord, null));
        } else {
          conditions.add(new ImmutableTriple<>(columnName, CompareKeyWord.IS_NULL, null));
        }
      } else {
        if (valueHandler != null) {
          v = valueHandler.apply(value);
        }
        conditions.add(new ImmutableTriple<>(columnName, keyWord, v));
      }
    } else {
      if (predicate.test(value)) {
        if (valueHandler != null) {
          v = valueHandler.apply(value);
        }
        conditions.add(new ImmutableTriple<>(columnName, keyWord, v));
      }
    }
    return this$();
  }
}
