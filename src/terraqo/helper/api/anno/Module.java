package potatoxf.helper.api.anno;

import java.lang.annotation.*;

/**
 * @author potatoxf
 * @date 2022/05/21
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

  /**
   * @return
   */
  String name();
}
