package potatoxf.helper.basic.db.sql.formatter.core;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Handles placeholder replacement with given params. */
public interface PlaceholderParams {

  PlaceholderParams EMPTY = new Empty();

  /**
   * @param params query param
   */
  static PlaceholderParams of(Map<String, ?> params) {
    return new NamedPlaceholderParams(params);
  }

  /**
   * @param params query param
   */
  static PlaceholderParams of(List<?> params) {
    return new IndexedPlaceholderParams(params);
  }

  boolean isEmpty();

  Object get();

  Object getByName(String key);

  /**
   * Returns param value that matches given placeholder with param key.
   *
   * @param token token.key Placeholder key token.value Placeholder value
   * @return param or token.value when params are missing
   */
  default Object get(Token token) {
    if (this.isEmpty()) {
      return token.value;
    }
    if (!(token.key == null || token.key.isEmpty())) {
      return this.getByName(token.key);
    } else {
      return this.get();
    }
  }

  class NamedPlaceholderParams implements PlaceholderParams {
    private final Map<String, ?> params;

    NamedPlaceholderParams(Map<String, ?> params) {
      this.params = params;
    }

    public boolean isEmpty() {
      return this.params.isEmpty();
    }

    @Override
    public Object get() {
      return null;
    }

    @Override
    public Object getByName(String key) {
      return this.params.get(key);
    }

    @Override
    public String toString() {
      return this.params.toString();
    }
  }

  class IndexedPlaceholderParams implements PlaceholderParams {
    private final Queue<?> params;

    IndexedPlaceholderParams(List<?> params) {
      this.params = new ArrayDeque<>(params);
    }

    public boolean isEmpty() {
      return this.params.isEmpty();
    }

    @Override
    public Object get() {
      return this.params.poll();
    }

    @Override
    public Object getByName(String key) {
      return null;
    }

    @Override
    public String toString() {
      return this.params.toString();
    }
  }

  class Empty implements PlaceholderParams {
    Empty() {}

    public boolean isEmpty() {
      return true;
    }

    @Override
    public Object get() {
      return null;
    }

    @Override
    public Object getByName(String key) {
      return null;
    }

    @Override
    public String toString() {
      return "[]";
    }
  }
}
