package potatoxf.helper.bloomfilter.filter;

import potatoxf.helper.HelpHash;

/**
 * 默认Bloom过滤器，使用Java自带的Hash算法
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class DefaultFilter extends FuncFilter {
  private static final long serialVersionUID = 1L;

  public DefaultFilter(long maxValue) {
    this(maxValue, DEFAULT_MACHINE_NUM);
  }

  public DefaultFilter(long maxValue, int machineNumber) {
    super(maxValue, machineNumber, HelpHash::javaDefaultHash);
  }
}
