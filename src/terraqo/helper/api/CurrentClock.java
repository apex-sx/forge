package potatoxf.helper.api;

import java.util.Date;
import java.util.Locale;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
public interface CurrentClock {

  CurrentClock DEFAULT_INSTANCE = new Default();

  Date getCurrentDate();

  Locale getCurrentLocal();

  class Default implements CurrentClock {
    private Default() {}

    @Override
    public Date getCurrentDate() {
      return new Date();
    }

    @Override
    public Locale getCurrentLocal() {
      return Locale.getDefault();
    }
  }
}
