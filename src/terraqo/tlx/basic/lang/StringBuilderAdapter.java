package pxf.toolkit.basic.lang;

import java.io.Serializable;
import javax.annotation.Nonnull;

/**
 * {@code StringBuilder}适配器
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public class StringBuilderAdapter implements CharSequence, Serializable {

  private static final long serialVersionUID = -1938247733558382375L;
  private final StringBuilder stringBuilder;

  public StringBuilderAdapter() {
    stringBuilder = new StringBuilder();
  }

  public StringBuilderAdapter(int capacity) {
    stringBuilder = new StringBuilder(capacity);
  }

  public StringBuilderAdapter(String string) {
    stringBuilder = new StringBuilder(string);
  }

  public StringBuilderAdapter append(Object obj) {
    return append(String.valueOf(obj));
  }

  public StringBuilderAdapter append(String str) {
    stringBuilder.append(str);
    return this;
  }

  public StringBuilderAdapter append(StringBuffer sb) {
    stringBuilder.append(sb);
    return this;
  }

  public StringBuilderAdapter append(CharSequence s) {
    stringBuilder.append(s);
    return this;
  }

  public StringBuilderAdapter append(CharSequence s, int start, int end) {
    stringBuilder.append(s, start, end);
    return this;
  }

  public StringBuilderAdapter append(char[] str) {
    stringBuilder.append(str);
    return this;
  }

  public StringBuilderAdapter append(char[] str, int offset, int len) {
    stringBuilder.append(str, offset, len);
    return this;
  }

  public StringBuilderAdapter append(boolean b) {
    stringBuilder.append(b);
    return this;
  }

  public StringBuilderAdapter append(char c) {
    stringBuilder.append(c);
    return this;
  }

  public StringBuilderAdapter append(int i) {
    stringBuilder.append(i);
    return this;
  }

  public StringBuilderAdapter append(long lng) {
    stringBuilder.append(lng);
    return this;
  }

  public StringBuilderAdapter append(float f) {
    stringBuilder.append(f);
    return this;
  }

  public StringBuilderAdapter append(double d) {
    stringBuilder.append(d);
    return this;
  }

  public StringBuilderAdapter appendCodePoint(int codePoint) {
    stringBuilder.appendCodePoint(codePoint);
    return this;
  }

  public StringBuilderAdapter delete(int start, int end) {
    stringBuilder.delete(start, end);
    return this;
  }

  public StringBuilderAdapter deleteCharAt(int index) {
    stringBuilder.deleteCharAt(index);
    return this;
  }

  public StringBuilderAdapter replace(int start, int end, String str) {
    stringBuilder.replace(start, end, str);
    return this;
  }

  public StringBuilderAdapter insert(int index, char[] str, int offset, int len) {
    stringBuilder.insert(index, str, offset, len);
    return this;
  }

  public StringBuilderAdapter insert(int offset, Object obj) {
    stringBuilder.insert(offset, obj);
    return this;
  }

  public StringBuilderAdapter insert(int offset, String str) {
    stringBuilder.insert(offset, str);
    return this;
  }

  public StringBuilderAdapter insert(int offset, char[] str) {
    stringBuilder.insert(offset, str);
    return this;
  }

  public StringBuilderAdapter insert(int dstOffset, CharSequence s) {
    stringBuilder.insert(dstOffset, s);
    return this;
  }

  public StringBuilderAdapter insert(int dstOffset, CharSequence s, int start, int end) {
    stringBuilder.insert(dstOffset, s, start, end);
    return this;
  }

  public StringBuilderAdapter insert(int offset, boolean b) {
    stringBuilder.insert(offset, b);
    return this;
  }

  public StringBuilderAdapter insert(int offset, char c) {
    stringBuilder.insert(offset, c);
    return this;
  }

  public StringBuilderAdapter insert(int offset, int i) {
    stringBuilder.insert(offset, i);
    return this;
  }

  public StringBuilderAdapter insert(int offset, long l) {
    stringBuilder.insert(offset, l);
    return this;
  }

  public StringBuilderAdapter insert(int offset, float f) {
    stringBuilder.insert(offset, f);
    return this;
  }

  public StringBuilderAdapter insert(int offset, double d) {
    stringBuilder.insert(offset, d);
    return this;
  }

  public int indexOf(String str) {
    return stringBuilder.indexOf(str);
  }

  public int indexOf(String str, int fromIndex) {
    return stringBuilder.indexOf(str, fromIndex);
  }

  public int lastIndexOf(String str) {
    return stringBuilder.lastIndexOf(str);
  }

  public int lastIndexOf(String str, int fromIndex) {
    return stringBuilder.lastIndexOf(str, fromIndex);
  }

  public StringBuilderAdapter reverse() {
    stringBuilder.reverse();
    return this;
  }

  @Override
  public int length() {
    return stringBuilder.length();
  }

  @Override
  public char charAt(int index) {
    return stringBuilder.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return stringBuilder.subSequence(start, end);
  }

  @Nonnull
  @Override
  public String toString() {
    // Create a copy, don't share the array
    return stringBuilder.toString();
  }
}
