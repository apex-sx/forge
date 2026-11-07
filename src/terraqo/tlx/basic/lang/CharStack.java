package pxf.toolkit.basic.lang;

/**
 * 字符栈
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public class CharStack {

  private final StringBuilder stack;

  public CharStack() {
    this(50);
  }

  public CharStack(int capacity) {
    stack = new StringBuilder(capacity);
  }

  /**
   * 返回栈中元素个数
   *
   * @return 栈中元素个数
   */
  public int size() {
    return stack.length();
  }

  /**
   * 字符栈 是否为空
   *
   * @return 如果为空则为 {@code true}，否则 {@code false}
   */
  public boolean isEmpty() {
    return stack.length() == 0;
  }

  /**
   * 进栈
   *
   * @param c 字符
   */
  public void push(char c) {
    stack.append(c);
  }

  /**
   * 出栈
   *
   * @return 字符，如果栈为空则返回 {@code null}
   */
  public Character pop() {
    Character top = peek();
    if (top == null) {
      return null;
    }
    stack.delete(stack.length() - 1, Integer.MAX_VALUE);
    return top;
  }

  /**
   * 获取栈顶字符
   *
   * @return 如果为空则为 {@code null}
   */
  public Character peek() {
    if (stack.length() == 0) {
      return null;
    }
    return stack.charAt(stack.length() - 1);
  }

  public void clear() {
    stack.setLength(0);
  }

  @Override
  public String toString() {
    return stack.toString();
  }
}
