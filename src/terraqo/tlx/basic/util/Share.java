package pxf.toolkit.basic.util;

import pxf.toolkit.basic.lang.Snowflake;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public final class Share {

  public static final Snowflake SNOWFLAKE = Snowflake.of(0, 0);

  private Share() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }
}
