package potatoxf.helper.lang.generator;

import potatoxf.helper.util.IdUtil;

/**
 * UUID生成器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class UUIDGenerator implements Generator<String> {
  @Override
  public String next() {
    return IdUtil.fastUUID();
  }
}
