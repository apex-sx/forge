package potatoxf.helper.api.anno;

import java.lang.annotation.*;

/**
 * 内置的注解，用于标识一些要被跳过的一些类，或者其它用途的类
 *
 * @author potatoxf
 * @date 2021/06/19
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuiltIn {

  /**
   * 信息
   *
   * @return 信息
   */
  String info() default "";
}
