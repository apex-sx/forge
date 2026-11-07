package pxf.toolkit.basic.text.coder;

import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Html编码器
 *
 * @author potatoxf
 * @date 2021/5/26
 */
@ThreadSafe
public final class HtmlCoder {

  private static final String HTML_ESCAPE_PREFIX = "&";
  private static final int PL = HTML_ESCAPE_PREFIX.length();
  private static final String HTML_ESCAPE_SUFFIX = ";";
  private static final int SL = HTML_ESCAPE_PREFIX.length();
  private static final int ENTITY_LENGTH = 7;
  private static final Map<Character, String> ENTITIES_ENCODE_MAPPING =
      Map.ofEntries(
          Map.entry(' ', "emsp"),
          Map.entry(' ', "ensp"),
          Map.entry(' ', "nbsp"),
          Map.entry('´', "acute"),
          Map.entry('©', "copy"),
          Map.entry('>', "gt"),
          Map.entry('µ', "micro"),
          Map.entry('®', "reg"),
          Map.entry('&', "amp"),
          Map.entry('°', "deg"),
          Map.entry('¡', "iexcl"),
          Map.entry('»', "raquo"),
          Map.entry('¦', "brvbar"),
          Map.entry('÷', "divide"),
          Map.entry('¿', "iquest"),
          Map.entry('¬', "not"),
          Map.entry('§', "sect"),
          Map.entry('•', "bull"),
          Map.entry('½', "frac12"),
          Map.entry('«', "laquo"),
          Map.entry('¶', "para"),
          Map.entry('¨', "uml"),
          Map.entry('¸', "cedil"),
          Map.entry('¼', "frac14"),
          Map.entry('<', "lt"),
          Map.entry('±', "plusmn"),
          Map.entry('×', "times"),
          Map.entry('¢', "cent"),
          Map.entry('¾', "frac34"),
          Map.entry('¯', "macr"),
          Map.entry('“', "quot"),
          Map.entry('™', "trade"),
          Map.entry('€', "euro"),
          Map.entry('£', "pound"),
          Map.entry('¥', "yen"),
          Map.entry('„', "bdquo"),
          Map.entry('…', "hellip"),
          Map.entry('·', "middot"),
          Map.entry('›', "rsaquo"),
          Map.entry('ª', "ordf"),
          Map.entry('ˆ', "circ"),
          Map.entry('—', "mdash"),
          Map.entry('’', "rsquo"),
          Map.entry('º', "ordm"),
          Map.entry('†', "dagger"),
          Map.entry('‹', "lsaquo"),
          Map.entry('–', "ndash"),
          Map.entry('‚', "sbquo"),
          Map.entry('”', "rdquo"),
          Map.entry('‘', "lsquo"),
          Map.entry('‰', "permil"),
          Map.entry('˜', "tilde"),
          Map.entry('≈', "asymp"),
          Map.entry('⁄', "frasl"),
          Map.entry('←', "larr"),
          Map.entry('∂', "part"),
          Map.entry('♠', "spades"),
          Map.entry('∩', "cap"),
          Map.entry('≥', "ge"),
          Map.entry('≤', "le"),
          Map.entry('′', "prime"),
          Map.entry('∑', "sum"),
          Map.entry('♣', "clubs"),
          Map.entry('↔', "harr"),
          Map.entry('◊', "loz"),
          Map.entry('↑', "uarr"),
          Map.entry('↓', "darr"),
          Map.entry('♥', "hearts"),
          Map.entry('−', "minus"),
          Map.entry('∏', "prod"),
          Map.entry('‍', "zwj"),
          Map.entry('♦', "diams"),
          Map.entry('∞', "infin"),
          Map.entry('≠', "ne"),
          Map.entry('√', "radic"),
          Map.entry('≡', "equiv"),
          Map.entry('∫', "int"),
          Map.entry('‾', "oline"),
          Map.entry('→', "rarr"),
          Map.entry('α', "alpha"),
          Map.entry('η', "eta"),
          Map.entry('μ', "mu"),
          Map.entry('π', "pi"),
          Map.entry('θ', "theta"),
          Map.entry('β', "beta"),
          Map.entry('γ', "gamma"),
          Map.entry('ν', "nu"),
          Map.entry('ψ', "psi"),
          Map.entry('υ', "upsilon"),
          Map.entry('χ', "chi"),
          Map.entry('ι', "iota"),
          Map.entry('ω', "omega"),
          Map.entry('ρ', "rho"),
          Map.entry('ξ', "xi"),
          Map.entry('δ', "delta"),
          Map.entry('κ', "kappa"),
          Map.entry('ο', "omicron"),
          Map.entry('σ', "sigma"),
          Map.entry('ζ', "zeta"),
          Map.entry('ε', "epsilon"),
          Map.entry('λ', "lambda"),
          Map.entry('φ', "phi"),
          Map.entry('τ', "tau"),
          Map.entry('ς', "sigmaf"));
  private static final Map<String, Character> ENTITIES_DECODE_MAPPING =
      Map.ofEntries(
          Map.entry("emsp", ' '),
          Map.entry("ensp", ' '),
          Map.entry("nbsp", ' '),
          Map.entry("acute", '´'),
          Map.entry("copy", '©'),
          Map.entry("gt", '>'),
          Map.entry("micro", 'µ'),
          Map.entry("reg", '®'),
          Map.entry("amp", '&'),
          Map.entry("deg", '°'),
          Map.entry("iexcl", '¡'),
          Map.entry("raquo", '»'),
          Map.entry("brvbar", '¦'),
          Map.entry("divide", '÷'),
          Map.entry("iquest", '¿'),
          Map.entry("not", '¬'),
          Map.entry("sect", '§'),
          Map.entry("bull", '•'),
          Map.entry("frac12", '½'),
          Map.entry("laquo", '«'),
          Map.entry("para", '¶'),
          Map.entry("uml", '¨'),
          Map.entry("cedil", '¸'),
          Map.entry("frac14", '¼'),
          Map.entry("lt", '<'),
          Map.entry("plusmn", '±'),
          Map.entry("times", '×'),
          Map.entry("cent", '¢'),
          Map.entry("frac34", '¾'),
          Map.entry("macr", '¯'),
          Map.entry("quot", '“'),
          Map.entry("trade", '™'),
          Map.entry("euro", '€'),
          Map.entry("pound", '£'),
          Map.entry("yen", '¥'),
          Map.entry("bdquo", '„'),
          Map.entry("hellip", '…'),
          Map.entry("middot", '·'),
          Map.entry("rsaquo", '›'),
          Map.entry("ordf", 'ª'),
          Map.entry("circ", 'ˆ'),
          Map.entry("mdash", '—'),
          Map.entry("rsquo", '’'),
          Map.entry("ordm", 'º'),
          Map.entry("dagger", '†'),
          Map.entry("lsaquo", '‹'),
          Map.entry("ndash", '–'),
          Map.entry("sbquo", '‚'),
          Map.entry("rdquo", '”'),
          Map.entry("lsquo", '‘'),
          Map.entry("permil", '‰'),
          Map.entry("tilde", '˜'),
          Map.entry("asymp", '≈'),
          Map.entry("frasl", '⁄'),
          Map.entry("larr", '←'),
          Map.entry("part", '∂'),
          Map.entry("spades", '♠'),
          Map.entry("cap", '∩'),
          Map.entry("ge", '≥'),
          Map.entry("le", '≤'),
          Map.entry("prime", '′'),
          Map.entry("sum", '∑'),
          Map.entry("clubs", '♣'),
          Map.entry("harr", '↔'),
          Map.entry("loz", '◊'),
          Map.entry("uarr", '↑'),
          Map.entry("darr", '↓'),
          Map.entry("hearts", '♥'),
          Map.entry("minus", '−'),
          Map.entry("prod", '∏'),
          Map.entry("zwj", '‍'),
          Map.entry("diams", '♦'),
          Map.entry("infin", '∞'),
          Map.entry("ne", '≠'),
          Map.entry("radic", '√'),
          Map.entry("equiv", '≡'),
          Map.entry("int", '∫'),
          Map.entry("oline", '‾'),
          Map.entry("rarr", '→'),
          Map.entry("alpha", 'α'),
          Map.entry("eta", 'η'),
          Map.entry("mu", 'μ'),
          Map.entry("pi", 'π'),
          Map.entry("theta", 'θ'),
          Map.entry("beta", 'β'),
          Map.entry("gamma", 'γ'),
          Map.entry("nu", 'ν'),
          Map.entry("psi", 'ψ'),
          Map.entry("upsilon", 'υ'),
          Map.entry("chi", 'χ'),
          Map.entry("iota", 'ι'),
          Map.entry("omega", 'ω'),
          Map.entry("rho", 'ρ'),
          Map.entry("xi", 'ξ'),
          Map.entry("delta", 'δ'),
          Map.entry("kappa", 'κ'),
          Map.entry("omicron", 'ο'),
          Map.entry("sigma", 'σ'),
          Map.entry("zeta", 'ζ'),
          Map.entry("epsilon", 'ε'),
          Map.entry("lambda", 'λ'),
          Map.entry("phi", 'φ'),
          Map.entry("tau", 'τ'),
          Map.entry("sigmaf", 'ς'));
  /**
   * 获取解码字符
   *
   * @param encodeString 编码后字符串
   * @return 返回编码后的字符串
   */
  @Nullable
  public Character getDecode(String encodeString) {
    return ENTITIES_DECODE_MAPPING.get(encodeString);
  }

  /**
   * 获取编码字符串
   *
   * @param ch 解码后的字符
   * @return 返回编码后的字符串
   */
  @Nullable
  public String getEncode(char ch) {
    return ENTITIES_ENCODE_MAPPING.get(ch);
  }

  /**
   * 获取编码字符串
   *
   * @param codepoint unicode码点
   * @return 返回编码后的字符串
   */
  @Nullable
  public String getEncode(int codepoint) {
    char[] chars = Character.toChars(codepoint);
    if (chars.length == 1) {
      return ENTITIES_ENCODE_MAPPING.get(chars[0]);
    }
    return null;
  }

  /**
   * 将html特殊字符编码成转义代码
   *
   * @param input 输入字符串
   */
  public void encode(StringBuilder input) {
    String encodeString;
    for (int i = 0; i < input.length(); i++) {
      int codepoint = input.codePointAt(i);
      char[] chars = Character.toChars(codepoint);
      if (chars.length == 1 && (encodeString = getEncode(chars[0])) != null) {
        input.deleteCharAt(i);
        input.insert(i++, "&");
        input.insert(i, encodeString);
        i += encodeString.length();
        input.insert(i, ";");
      }
    }
  }

  /**
   * 将html特殊字符编码成转义代码
   *
   * @param input 输入字符串
   * @return 返回编码后的字符串
   */
  public String encode(String input) {
    int length = input.length();
    StringBuilder sb = new StringBuilder(length * 4);
    String encodeString;
    for (int i = 0; i < length; i++) {
      int codepoint = input.codePointAt(i);
      char[] chars = Character.toChars(codepoint);
      if (chars.length == 1 && (encodeString = getEncode(chars[0])) != null) {
        sb.append("&").append(encodeString).append(";");
      } else {
        sb.append(chars);
      }
    }
    return sb.toString();
  }

  /**
   * 将带有html特殊字符转义代码解码
   *
   * @param input 输入字符串
   */
  public void decode(StringBuilder input) {
    int psi = 0, ssi;
    while ((psi = input.indexOf(HTML_ESCAPE_PREFIX, psi)) >= 0) {
      ssi = input.indexOf(HTML_ESCAPE_SUFFIX, psi + PL);
      if (ssi < 0) {
        break;
      }
      if (ssi - psi - PL > ENTITY_LENGTH) {
        continue;
      }
      Character decodeCharacter = getDecode(input.substring(psi + PL, ssi));
      if (decodeCharacter != null) {
        input.delete(psi, ssi + SL).insert(psi, decodeCharacter);
        // skip
        psi = ssi + SL;
      }
    }
  }

  /**
   * 将带有html特殊字符转义代码解码
   *
   * @param input 输入字符串
   * @return 返回解码后的字符串
   */
  public String decode(String input) {
    int length = input.length();
    StringBuilder sb = new StringBuilder(length);
    int psi = 0, ssi, asi = 0;
    while ((psi = input.indexOf(HTML_ESCAPE_PREFIX, psi)) >= 0) {
      sb.append(input, asi, psi);
      asi += psi;
      ssi = input.indexOf(HTML_ESCAPE_SUFFIX, psi + PL);
      if (ssi < 0) {
        break;
      }
      if (ssi - psi - PL > ENTITY_LENGTH) {
        continue;
      }
      Character decodeCharacter = getDecode(input.substring(psi + PL, ssi));
      if (decodeCharacter != null) {
        sb.append(decodeCharacter);
        // skip
        psi = ssi + SL;
        asi = psi;
      }
    }
    if (asi != length) {
      sb.append(input, asi, length);
    }
    return sb.toString();
  }
}
