package potatoxf.helper.bloomfilter.filter;

import potatoxf.helper.HelpHash;

public class JSFilter extends FuncFilter {
  private static final long serialVersionUID = 1L;

  public JSFilter(long maxValue) {
    this(maxValue, DEFAULT_MACHINE_NUM);
  }

  public JSFilter(long maxValue, int machineNum) {
    super(maxValue, machineNum, HelpHash::jsHash);
  }
}
