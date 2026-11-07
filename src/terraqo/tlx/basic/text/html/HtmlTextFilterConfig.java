package pxf.toolkit.basic.text.html;

import com.google.common.collect.Multimap;

/**
 * @author potatoxf
 * @date 2021/5/23
 */
public class HtmlTextFilterConfig {

  /** set of allowed html elements, along with allowed attributes for each element * */
  volatile Multimap<String, String> allowedElement;
  /** html elements which must always be self-closing (e.g. "<img />") * */
  volatile String[] selfClosingTags;
  /** html elements which must always have separate opening and closing tags (e.g. "<b></b>") * */
  volatile String[] needClosingTags;
  /** set of disallowed html elements * */
  volatile String[] disallowed;
  /** attributes which should be checked for valid protocols * */
  volatile String[] protocolAttributes;
  /** allowed protocols * */
  volatile String[] allowedProtocols;
  /** tags which should be removed if they contain no content (e.g. "<b></b>" or "<b />") * */
  volatile String[] removeBlanksElement;
  /** entities allowed within html markup * */
  volatile String[] allowedEntities;
  /**
   * flag determining whether to try to make tags when presented with "unbalanced" angle brackets
   * (e.g. "<b text </b>" becomes "<b> text </b>"). If set to false, unbalanced angle brackets will
   * be html escaped.
   */
  volatile boolean isAlwaysMakeTags;
  /** flag determining whether comments are allowed in input String. */
  volatile boolean isStripComment;
  /** 是否编码双引号 */
  volatile boolean isEncodeQuotes;
  /** 是否debug */
  volatile boolean isDebug;

  public Multimap<String, String> getAllowedElement() {
    return allowedElement;
  }

  public HtmlTextFilterConfig setAllowedElement(Multimap<String, String> allowedElement) {
    this.allowedElement = allowedElement;
    return this;
  }

  public String[] getSelfClosingTags() {
    return selfClosingTags;
  }

  public HtmlTextFilterConfig setSelfClosingTags(String[] selfClosingTags) {
    this.selfClosingTags = selfClosingTags;
    return this;
  }

  public String[] getNeedClosingTags() {
    return needClosingTags;
  }

  public HtmlTextFilterConfig setNeedClosingTags(String[] needClosingTags) {
    this.needClosingTags = needClosingTags;
    return this;
  }

  public String[] getDisallowed() {
    return disallowed;
  }

  public HtmlTextFilterConfig setDisallowed(String[] disallowed) {
    this.disallowed = disallowed;
    return this;
  }

  public String[] getProtocolAttributes() {
    return protocolAttributes;
  }

  public HtmlTextFilterConfig setProtocolAttributes(String[] protocolAttributes) {
    this.protocolAttributes = protocolAttributes;
    return this;
  }

  public String[] getAllowedProtocols() {
    return allowedProtocols;
  }

  public HtmlTextFilterConfig setAllowedProtocols(String[] allowedProtocols) {
    this.allowedProtocols = allowedProtocols;
    return this;
  }

  public String[] getRemoveBlanksElement() {
    return removeBlanksElement;
  }

  public HtmlTextFilterConfig setRemoveBlanksElement(String[] removeBlanksElement) {
    this.removeBlanksElement = removeBlanksElement;
    return this;
  }

  public String[] getAllowedEntities() {
    return allowedEntities;
  }

  public HtmlTextFilterConfig setAllowedEntities(String[] allowedEntities) {
    this.allowedEntities = allowedEntities;
    return this;
  }

  public boolean isAlwaysMakeTags() {
    return isAlwaysMakeTags;
  }

  public HtmlTextFilterConfig setAlwaysMakeTags(boolean alwaysMakeTags) {
    this.isAlwaysMakeTags = alwaysMakeTags;
    return this;
  }

  public boolean isStripComment() {
    return isStripComment;
  }

  public HtmlTextFilterConfig setStripComment(boolean stripComment) {
    isStripComment = stripComment;
    return this;
  }

  public boolean isEncodeQuotes() {
    return isEncodeQuotes;
  }

  public HtmlTextFilterConfig setEncodeQuotes(boolean encodeQuotes) {
    isEncodeQuotes = encodeQuotes;
    return this;
  }

  public boolean isDebug() {
    return isDebug;
  }

  public HtmlTextFilterConfig setDebug(boolean debug) {
    isDebug = debug;
    return this;
  }
}
