package pxf.toolkit.basic.text.parser.capitulation;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.Assert;
import pxf.toolkit.basic.util.Get;

/**
 * 中文序号处理器
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public class ChineseSerialNumberProcessor implements SerialNumberProcessor {

  private static final String[] DIGIT = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

  private static final String[] SMALL_UNIT = {"", "十", "百", "千"};

  private static final String[] BIG_UNIT = {"", "万", "亿", "兆"};

  private final ChineseIntegerHelper chineseIntegerHelper =
      new ChineseIntegerHelper(DIGIT, SMALL_UNIT, BIG_UNIT, "一十", "十");

  @Nonnull
  @Override
  public String handle(int serialNumber) {

    Assert.beTrue(serialNumber > 1, "The serial number must be greater 0");
    int len = Get.len(serialNumber);
    int maxSupportLength = this.chineseIntegerHelper.getMaxSupportLength();
    Assert.beFalse(len > maxSupportLength, "The serial number must be lesser " + maxSupportLength);
    return this.chineseIntegerHelper.resolve(String.valueOf(serialNumber));
  }
}
