package pxf.toolkit.basic.lang;

/**
 * 扩展 {@code StringBuilder}
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public class ExpandStringBuilder extends StringBuilderAdapter {

  private static final long serialVersionUID = 4122546988476782992L;

  public ExpandStringBuilder() {}

  public ExpandStringBuilder(int capacity) {
    super(capacity);
  }

  public ExpandStringBuilder(String string) {
    super(string);
  }

  public ExpandStringBuilder appendWhenNoEmpty(char c) {
    if (length() == 0) {
      return this;
    }
    append(c);
    return this;
  }

  public ExpandStringBuilder appendWhenNoEmpty(CharSequence s) {
    if (length() == 0) {
      return this;
    }
    if (s != null) {
      append(s);
    }
    return this;
  }

  public ExpandStringBuilder appendSpaceWithoutEmpty() {
    return appendWhenNoEmpty(' ');
  }

  public ExpandStringBuilder appendSysLineSeparatorWithoutEmpty() {
    return appendWhenNoEmpty(System.lineSeparator());
  }

  public ExpandStringBuilder appendLFSeparatorWithoutEmpty() {
    return appendWhenNoEmpty('\n');
  }

  public ExpandStringBuilder appendLrLfSeparatorWithoutEmpty() {
    return appendWhenNoEmpty("\r\n");
  }

  public ExpandStringBuilder appendImportantInfo(Object obj) {
    return appendImportantInfo(obj, '\'');
  }

  public ExpandStringBuilder appendImportantInfo(Object obj, char c) {
    return appendImportantInfo(obj, c, c);
  }

  public ExpandStringBuilder appendImportantInfo(Object obj, char l, char r) {
    append(l).append(obj).append(r);
    return this;
  }

  public ExpandStringBuilder appendWhenNoNull(Object obj) {
    if (obj != null) {
      append(obj);
    }
    return this;
  }

  public ExpandStringBuilder appendWhenNoNull(String str) {
    if (str != null) {
      append(str);
    }
    return this;
  }

  public ExpandStringBuilder appendWhenNoNull(StringBuffer sb) {
    if (sb != null) {
      append(sb);
    }
    return this;
  }

  public ExpandStringBuilder appendWhenNoNull(CharSequence s) {
    if (s != null) {
      append(s);
    }
    return this;
  }

  public ExpandStringBuilder appendWhenNoNull(CharSequence s, int start, int end) {
    if (s != null) {
      append(s, start, end);
    }
    return this;
  }

  public ExpandStringBuilder deleteEndingCharacter(char c) {
    int maxI = length() - 1;
    if (maxI >= 0) {
      if (charAt(maxI) == c) {
        deleteCharAt(maxI);
      }
    }
    return this;
  }

  public ExpandStringBuilder deleteEndingCharacter(String c) {
    int si = length() - c.length();
    if (si >= 0) {
      int ei = si + length();
      if (subSequence(si, ei).equals(c)) {
        delete(si, ei);
      }
    }
    return this;
  }

  public ExpandStringBuilder deleteEndingCharacter(int len) {
    int si = length() - len;
    if (si >= 0) {
      delete(si, length());
    }
    return this;
  }

  public ExpandStringBuilder clear() {
    delete(0, length());
    return this;
  }

  @Override
  public ExpandStringBuilder append(Object obj) {
    super.append(obj);
    return this;
  }

  @Override
  public ExpandStringBuilder append(String str) {
    super.append(str);
    return this;
  }

  @Override
  public ExpandStringBuilder append(StringBuffer sb) {
    super.append(sb);
    return this;
  }

  @Override
  public ExpandStringBuilder append(CharSequence s) {
    super.append(s);
    return this;
  }

  @Override
  public ExpandStringBuilder append(CharSequence s, int start, int end) {
    super.append(s, start, end);
    return this;
  }

  @Override
  public ExpandStringBuilder append(char[] str) {
    super.append(str);
    return this;
  }

  @Override
  public ExpandStringBuilder append(char[] str, int offset, int len) {
    super.append(str, offset, len);
    return this;
  }

  @Override
  public ExpandStringBuilder append(boolean b) {
    super.append(b);
    return this;
  }

  @Override
  public ExpandStringBuilder append(char c) {
    super.append(c);
    return this;
  }

  @Override
  public ExpandStringBuilder append(int i) {
    super.append(i);
    return this;
  }

  @Override
  public ExpandStringBuilder append(long lng) {
    super.append(lng);
    return this;
  }

  @Override
  public ExpandStringBuilder append(float f) {
    super.append(f);
    return this;
  }

  @Override
  public ExpandStringBuilder append(double d) {
    super.append(d);
    return this;
  }

  @Override
  public ExpandStringBuilder appendCodePoint(int codePoint) {
    super.appendCodePoint(codePoint);
    return this;
  }

  @Override
  public ExpandStringBuilder delete(int start, int end) {
    super.delete(start, end);
    return this;
  }

  @Override
  public ExpandStringBuilder deleteCharAt(int index) {
    super.deleteCharAt(index);
    return this;
  }

  @Override
  public ExpandStringBuilder replace(int start, int end, String str) {
    super.replace(start, end, str);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int index, char[] str, int offset, int len) {
    super.insert(index, str, offset, len);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, Object obj) {
    super.insert(offset, obj);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, String str) {
    super.insert(offset, str);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, char[] str) {
    super.insert(offset, str);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int dstOffset, CharSequence s) {
    super.insert(dstOffset, s);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
    super.insert(dstOffset, s, start, end);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, boolean b) {
    super.insert(offset, b);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, char c) {
    super.insert(offset, c);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, int i) {
    super.insert(offset, i);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, long l) {
    super.insert(offset, l);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, float f) {
    super.insert(offset, f);
    return this;
  }

  @Override
  public ExpandStringBuilder insert(int offset, double d) {
    super.insert(offset, d);
    return this;
  }
}
