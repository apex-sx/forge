package potatoxf.helper.api;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
public interface CurrentOperator {

  CurrentOperator DEFAULT_INSTANCE = new Default();

  String DEFAULT = "UNKNOWN";

  String getId();

  String getUsername();

  class Default implements CurrentOperator {
    private Default() {}

    @Override
    public String getId() {
      return DEFAULT;
    }

    @Override
    public String getUsername() {
      return DEFAULT;
    }
  }
}
