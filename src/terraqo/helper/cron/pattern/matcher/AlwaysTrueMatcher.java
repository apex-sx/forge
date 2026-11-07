package potatoxf.helper.cron.pattern.matcher;

import potatoxf.helper.HelpString;

/**
 * 所有值匹配，始终返回{@code true}
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class AlwaysTrueMatcher implements PartMatcher {

  public static AlwaysTrueMatcher INSTANCE = new AlwaysTrueMatcher();

  @Override
  public boolean match(Integer t) {
    return true;
  }

  @Override
  public int nextAfter(int value) {
    return value;
  }

  @Override
  public String toString() {
    return HelpString.format("[Matcher]: always true.");
  }
}
