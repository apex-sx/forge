package potatoxf.helper.bloomfilter.filter;

import potatoxf.helper.HelpHash;

public class RSFilter extends FuncFilter {
  private static final long serialVersionUID = 1L;

  public RSFilter(long maxValue) {
    this(maxValue, DEFAULT_MACHINE_NUM);
  }

  public RSFilter(long maxValue, int machineNum) {
    super(maxValue, machineNum, HelpHash::rsHash);
  }
}
