package potatoxf.helper.api;

import java.io.IOException;
import java.util.Objects;

/**
 * @author potatoxf
 * @date 2022/7/11
 */
public interface CharSequenceAppendable extends Appendable, CharSequence {

  static CharSequenceAppendable ofStringBuffer() {
    return new CharSequenceAppendableImpl<>(new StringBuffer());
  }

  static CharSequenceAppendable ofStringBuffer(int capacity) {
    return new CharSequenceAppendableImpl<>(new StringBuffer(capacity));
  }

  static CharSequenceAppendable ofStringBuffer(StringBuffer input) {
    return new CharSequenceAppendableImpl<>(input);
  }

  static CharSequenceAppendable ofStringBuilder() {
    return new CharSequenceAppendableImpl<>(new StringBuilder());
  }

  static CharSequenceAppendable ofStringBuilder(int capacity) {
    return new CharSequenceAppendableImpl<>(new StringBuilder(capacity));
  }

  static CharSequenceAppendable ofStringBuilder(StringBuilder input) {
    return new CharSequenceAppendableImpl<>(input);
  }
  /**
   * Appends the specified character sequence to this <tt>Appendable</tt>.
   *
   * @param csq The character sequence to append. If <tt>csq</tt> is <tt>null</tt>, then the four
   *     characters <tt>"null"</tt> are appended to this Appendable.
   * @return A reference to this <tt>Appendable</tt>
   */
  @Override
  CharSequenceAppendable append(CharSequence csq);

  /**
   * Appends a subsequence of the specified character sequence to this <tt>Appendable</tt>.
   *
   * <p>An invocation of this method of the form <tt>out.append(csq, start, end)</tt> when
   * <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the same way as the invocation
   *
   * <pre>
   *     out.append(csq.subSequence(start, end)) </pre>
   *
   * @param csq The character sequence from which a subsequence will be appended. If <tt>csq</tt> is
   *     <tt>null</tt>, then characters will be appended as if <tt>csq</tt> contained the four
   *     characters <tt>"null"</tt>.
   * @param start The index of the first character in the subsequence
   * @param end The index of the character following the last character in the subsequence
   * @return A reference to this <tt>Appendable</tt>
   * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are negative,
   *     <tt>start</tt> is greater than <tt>end</tt>, or <tt>end</tt> is greater than
   *     <tt>csq.length()</tt>
   */
  @Override
  CharSequenceAppendable append(CharSequence csq, int start, int end);

  /**
   * Appends the specified character to this <tt>Appendable</tt>.
   *
   * @param c The character to append
   * @return A reference to this <tt>Appendable</tt>
   */
  @Override
  CharSequenceAppendable append(char c);

  class CharSequenceAppendableImpl<T extends Appendable & CharSequence>
      implements CharSequenceAppendable {

    private final T instance;

    private CharSequenceAppendableImpl(T instance) {
      this.instance = Objects.requireNonNull(instance);
    }

    /**
     * Appends the specified character sequence to this <tt>Appendable</tt>.
     *
     * @param csq The character sequence to append. If <tt>csq</tt> is <tt>null</tt>, then the four
     *     characters <tt>"null"</tt> are appended to this Appendable.
     * @return A reference to this <tt>Appendable</tt>
     */
    @Override
    public CharSequenceAppendable append(CharSequence csq) {
      try {
        instance.append(csq);
      } catch (IOException ignored) {
      }
      return this;
    }

    /**
     * Appends a subsequence of the specified character sequence to this <tt>Appendable</tt>.
     *
     * <p>An invocation of this method of the form <tt>out.append(csq, start, end)</tt> when
     * <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the same way as the invocation
     *
     * <pre>
     *     out.append(csq.subSequence(start, end)) </pre>
     *
     * @param csq The character sequence from which a subsequence will be appended. If <tt>csq</tt>
     *     is <tt>null</tt>, then characters will be appended as if <tt>csq</tt> contained the four
     *     characters <tt>"null"</tt>.
     * @param start The index of the first character in the subsequence
     * @param end The index of the character following the last character in the subsequence
     * @return A reference to this <tt>Appendable</tt>
     * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are negative,
     *     <tt>start</tt> is greater than <tt>end</tt>, or <tt>end</tt> is greater than
     *     <tt>csq.length()</tt>
     */
    @Override
    public CharSequenceAppendable append(CharSequence csq, int start, int end) {
      try {
        instance.append(csq, start, end);
      } catch (IOException ignored) {
      }
      return this;
    }

    /**
     * Appends the specified character to this <tt>Appendable</tt>.
     *
     * @param c The character to append
     * @return A reference to this <tt>Appendable</tt>
     */
    @Override
    public CharSequenceAppendable append(char c) {
      try {
        instance.append(c);
      } catch (IOException ignored) {
      }
      return this;
    }

    /**
     * Returns the length of this character sequence. The length is the number of 16-bit <code>char
     * </code>s in the sequence.
     *
     * @return the number of <code>char</code>s in this sequence
     */
    @Override
    public int length() {
      return instance.length();
    }

    /**
     * Returns the <code>char</code> value at the specified index. An index ranges from zero to
     * <tt>length() - 1</tt>. The first <code>char</code> value of the sequence is at index zero,
     * the next at index one, and so on, as for array indexing.
     *
     * <p>If the <code>char</code> value specified by the index is a <a
     * href="{@docRoot}/java/lang/Character.html#unicode">surrogate</a>, the surrogate value is
     * returned.
     *
     * @param index the index of the <code>char</code> value to be returned
     * @return the specified <code>char</code> value
     * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative or not less than
     *     <tt>length()</tt>
     */
    @Override
    public char charAt(int index) {
      return instance.charAt(index);
    }

    /**
     * Returns a <code>CharSequence</code> that is a subsequence of this sequence. The subsequence
     * starts with the <code>char</code> value at the specified index and ends with the <code>char
     * </code> value at index <tt>end - 1</tt>. The length (in <code>char</code>s) of the returned
     * sequence is <tt>end - start</tt>, so if <tt>start == end</tt> then an empty sequence is
     * returned.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if <tt>start</tt> or <tt>end</tt> are negative, if
     *     <tt>end</tt> is greater than <tt>length()</tt>, or if <tt>start</tt> is greater than
     *     <tt>end</tt>
     */
    @Override
    public CharSequence subSequence(int start, int end) {
      return instance.subSequence(start, end);
    }

    /**
     * Returns a string representation of the object. In general, the {@code toString} method
     * returns a string that "textually represents" this object. The result should be a concise but
     * informative representation that is easy for a person to read. It is recommended that all
     * subclasses override this method.
     *
     * <p>The {@code toString} method for class {@code Object} returns a string consisting of the
     * name of the class of which the object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the object. In other words, this
     * method returns a string equal to the value of:
     *
     * <blockquote>
     *
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre>
     *
     * </blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
      return instance.toString();
    }
  }
}
