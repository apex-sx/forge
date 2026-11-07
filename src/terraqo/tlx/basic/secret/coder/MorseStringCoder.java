package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * 莫斯字符串编码
 *
 * <p>该类是线程安全的，因为它的属性是不变的，并且其余的都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class MorseStringCoder extends AbstractStringCoder {

  private static final String SPACE = " ";
  private static final String BI_SPACE = "  ";
  private final String blockSplit;
  private final String split;

  public MorseStringCoder() {
    this(BI_SPACE, SPACE);
  }

  public MorseStringCoder(String blockSplit, String split) {
    this.blockSplit = blockSplit;
    this.split = split;
  }

  @Override
  protected String doDecode(String source, Charset charset) throws Exception {
    StringBuilder sb = new StringBuilder();
    for (String string : source.split(blockSplit)) {
      for (String letter : string.split(split)) {
        MorseCoder morseCoder = MorseCoder.parseMorseCode(letter);
        sb.append(morseCoder.toCharacter());
      }
      sb.append(blockSplit);
    }
    return sb.toString().toLowerCase();
  }

  @Override
  protected String doEncode(String source, Charset charset) throws Exception {
    StringBuilder sb = new StringBuilder();
    for (String string : source.split(blockSplit)) {
      for (String word : string.split(split)) {
        int len = word.length();
        for (int i = 0; i < len; i++) {
          char c = source.charAt(i);
          sb.append(MorseCoder.parseMorseCode(c).toMorseCode()).append(SPACE);
        }
      }
      sb.append(blockSplit);
    }
    return sb.toString();
  }
}
