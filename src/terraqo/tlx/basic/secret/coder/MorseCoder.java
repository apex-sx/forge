package pxf.toolkit.basic.secret.coder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import pxf.toolkit.basic.lang.AsciiTableMatcher;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Extract;

/**
 * 摩斯密码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public enum MorseCoder {
  /** 摩斯密码 */
  _A(2, 0b0000_0001, 'A'),
  _B(4, 0b0000_1000, 'B'),
  _C(4, 0b0000_1010, 'C'),
  _D(3, 0b0000_0100, 'D'),
  _E(1, 0b0000_0000, 'E'),
  _F(4, 0b0000_0010, 'F'),
  _G(3, 0b0000_0110, 'G'),
  _H(4, 0b0000_0000, 'H'),
  _I(2, 0b0000_0000, 'I'),
  _J(4, 0b0000_0111, 'J'),
  _K(3, 0b0000_0101, 'K'),
  _L(4, 0b0000_0100, 'L'),
  _M(2, 0b0000_0011, 'M'),
  _N(2, 0b0000_0010, 'N'),
  _O(3, 0b0000_0111, 'O'),
  _P(4, 0b0000_0110, 'P'),
  _Q(4, 0b0000_1101, 'Q'),
  _R(3, 0b0000_0010, 'R'),
  _S(3, 0b0000_0000, 'S'),
  _T(1, 0b0000_0001, 'T'),
  _U(3, 0b0000_0001, 'U'),
  _V(4, 0b0000_0001, 'V'),
  _W(3, 0b0000_0011, 'W'),
  _X(4, 0b0000_1001, 'X'),
  _Y(4, 0b0000_1011, 'Y'),
  _Z(4, 0b0000_1100, 'Z'),
  _0(5, 0b0001_1111, '0'),
  _1(5, 0b0000_1111, '1'),
  _2(5, 0b0000_0111, '2'),
  _3(5, 0b0000_0011, '3'),
  _4(5, 0b0000_0001, '4'),
  _5(5, 0b0000_0000, '5'),
  _6(5, 0b0001_0000, '6'),
  _7(5, 0b0001_1000, '7'),
  _8(5, 0b0001_1100, '8'),
  _9(5, 0b0001_1110, '9'),
  /** . */
  DOT(6, 0b0001_0101, '.'),
  /** : */
  COLON(6, 0b0011_1000, ':'),
  /** , */
  COMMA(6, 0b0011_0011, ','),
  /** ; */
  SEMICOLON(6, 0b0010_1010, ';'),
  /** ? */
  QUESTION(6, 0b0000_1100, '?'),
  /** = */
  EQUAL(5, 0b0001_0001, '='),
  /** ' */
  SINGLE_QUOTE(6, 0b0001_1110, '\''),
  /** / */
  SLASH(5, 0b0001_0010, '/'),
  /** ! */
  EXCLAMATION(6, 0b0010_1011, '!'),
  /** - */
  RUNG(6, 0b0010_0001, '-'),
  /** _ */
  UNDERLINE(6, 0b0000_1101, '_'),
  /** " */
  DOUBLE_QUOTE(6, 0b0001_0010, '"'),
  /** ( */
  LEFT_BRACKET(5, 0b0001_0110, '('),
  /** ) */
  RIGHT_BRACKET(6, 0b0010_1101, ')'),
  /** $ */
  DOLLAR(7, 0b0000_1001, '$'),
  /** & */
  LOGIC_AND(5, 0b0000_1000, '&'),
  /** @ */
  ART(6, 0b0001_1010, '@'),
  /** 空白字符 */
  EMPTY(0, 0b0000_0000, ' ');
  public static final int MAX_MORSE_LENGTH = 8;
  /** 位数 */
  private final byte bitDigit;
  /** 莫斯密码的值 */
  private final byte morseCodeValue;
  /** 莫斯密码对应的字符 */
  private final char valueChar;

  MorseCoder(int bitDigit, int morseCodeValue, char valueChar) {
    this.bitDigit = (byte) bitDigit;
    this.morseCodeValue = (byte) morseCodeValue;
    this.valueChar = valueChar;
  }

  /**
   * 解析莫斯密码
   *
   * @param ch 字符
   * @return {@code MorseCode}
   */
  public static MorseCoder parseMorseCode(char ch) {
    if (AsciiTableMatcher.isMatcherExceptAsciiChar(ch, AsciiTableMatcher.INVISIBLE_CHAR)) {
      return MorseCoder.EMPTY;
    }
    char foundValue = Character.toUpperCase(ch);
    Optional<MorseCoder> first =
        Arrays.stream(MorseCoder.values()).filter(e -> e.valueChar == foundValue).findFirst();
    if (first.isPresent()) {
      return first.get();
    }
    throw new NoSuchElementException("Don't exist the morse code of [" + ch + "]");
  }

  /**
   * 解析莫斯密码
   *
   * @param morseCode 摩斯密码
   * @return {@code MorseCode}
   */
  public static MorseCoder parseMorseCode(String morseCode) throws ParseException {
    if (morseCode.charAt(0) == Empty.CHAR) {
      return MorseCoder.EMPTY;
    }
    byte cv = MorseCoder.parseMorseCodeValue(morseCode);
    Optional<MorseCoder> first =
        Arrays.stream(MorseCoder.values())
            .filter(e -> e.morseCodeValue == cv && e.bitDigit == morseCode.length())
            .findFirst();
    if (first.isPresent()) {
      return first.get();
    }
    throw new NoSuchElementException("Don't exist the morse code of [" + morseCode + "]");
  }

  /**
   * 显示所有摩斯密码映射表
   *
   * @return 所有摩斯密码映射表
   */
  public static String displayMorseCode() {
    StringBuilder sb = new StringBuilder();
    MorseCoder[] morseCoders = MorseCoder.values();
    for (int i = 0; i < morseCoders.length; i++) {
      sb.append(morseCoders[i]);
      if ((i + 1) % 5 == 0) {
        sb.append(System.lineSeparator());
      }
    }
    return sb.toString();
  }

  /**
   * 解析莫斯密码
   *
   * @param morseCode 摩斯密码
   * @return {@code MorseCode的字节值}
   */
  private static byte parseMorseCodeValue(String morseCode) {
    int length = morseCode.length();
    if (length == 0 || length > MAX_MORSE_LENGTH) {
      throw new IllegalArgumentException("The character length of morse code is illegal");
    }
    int codeValue = 0;
    for (int i = 0; i < length; i++) {
      char c = morseCode.charAt(i);
      codeValue = codeValue << 1;
      if (c == '-') {
        codeValue = codeValue ^ 1;
      } else {
        if (c != '.') {
          throw new IllegalArgumentException("Only '.' and '-' characters are allowed");
        }
      }
    }
    return (byte) codeValue;
  }

  @Override
  public String toString() {
    return toCharacter() + " " + String.format("%-8s", toMorseCode());
  }

  /**
   * 转为莫斯密码的字符
   *
   * @return {@code String}
   */
  public String toMorseCode() {
    if (bitDigit == 0) {
      return Empty.STRING;
    }
    StringBuilder sb = new StringBuilder(bitDigit);
    for (int i = bitDigit; i > 0; i--) {
      boolean v = Extract.bitVal(morseCodeValue, i);
      if (v) {
        sb.append('-');
      } else {
        sb.append('.');
      }
    }
    return sb.toString();
  }

  /**
   * 转为普通的字符
   *
   * @return {@code String}
   */
  public String toCharacter() {
    return String.valueOf(valueChar);
  }
}
