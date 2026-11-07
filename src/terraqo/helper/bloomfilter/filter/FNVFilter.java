package potatoxf.helper.bloomfilter.filter;

import potatoxf.helper.HelpHash;

public class FNVFilter extends FuncFilter {
  private static final long serialVersionUID = 1L;

  public FNVFilter(long maxValue) {
    this(maxValue, DEFAULT_MACHINE_NUM);
  }

  public FNVFilter(long maxValue, int machineNum) {
    super(maxValue, machineNum, HelpHash::fnvHash);
  }
}
