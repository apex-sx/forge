package potatoxf.helper.lang.generator;

import potatoxf.helper.lang.id.ObjectId;

/**
 * ObjectId生成器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class ObjectIdGenerator implements Generator<String> {
  @Override
  public String next() {
    return ObjectId.next();
  }
}
