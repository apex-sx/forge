package pxf.toolkit.basic.lang;

import java.io.IOException;

/**
 * @author potatoxf
 * @date 2021/3/27
 */
public class SafeAppendable {

  private final Appendable appendable;
  private boolean empty = true;

  public SafeAppendable(Appendable appendable) {
    this.appendable = appendable;
  }

  public SafeAppendable append(CharSequence charSequence) {
    try {
      if (empty && charSequence.length() > 0) {
        empty = false;
      }
      appendable.append(charSequence);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public boolean isEmpty() {
    return empty;
  }
}
