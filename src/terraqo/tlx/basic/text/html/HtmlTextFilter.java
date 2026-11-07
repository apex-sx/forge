package pxf.toolkit.basic.text.html;

import com.google.common.collect.LinkedListMultimap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.lang.collection.GroupStatisticalMap;

/**
 * HTML filtering utility for protecting against XSS (Cross Site Scripting).
 *
 * <p>This code is licensed LGPLv3
 *
 * <p>This code is a Java port of the original work in PHP by Cal Hendersen.
 * http://code.iamcal.com/php/lib_filter/
 *
 * <p>The trickiest part of the translation was handling the differences in regex handling between
 * PHP and Java. These resources were helpful in the process:
 *
 * <p>http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html
 * http://us2.php.net/manual/en/reference.pcre.pattern.modifiers.php
 * http://www.regular-expressions.info/modifiers.html
 *
 * <p>A note on naming conventions: instance variables are prefixed with a "v"; global constants are
 * in all caps.
 *
 * <p>Sample use: String input = ... String clean = new HTMLFilter().filter( input );
 *
 * <p>The class is not thread safe. Create a new instance if in doubt.
 *
 * <p>If you find bugs or have suggestions on improvement (especially regarding performance), please
 * contact us. The latest version of this source, and our contact details, can be found at
 * http://xss-html-filter.sf.net
 *
 * @author Joseph O'Connell
 * @author Cal Hendersen
 * @author Michael Semb Wever
 * @author potatoxf
 * @date 2021/5/23
 */
public class HtmlTextFilter extends HtmlTextFilterConfig {

  private static final Logger LOG = LoggerFactory.getLogger(HtmlTextFilter.class);
  /** regex flag union representing /si modifiers in php * */
  private static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

  private static final Pattern P_COMMENTS = pc("<!--(.*?)-->", Pattern.DOTALL);
  private static final Pattern P_COMMENT = pc("^!--(.*)--$", REGEX_FLAGS_SI);
  private static final Pattern P_TAGS = pc("<(.*?)>", Pattern.DOTALL);
  private static final Pattern P_END_TAG = pc("^/([a-z0-9]+)", REGEX_FLAGS_SI);
  private static final Pattern P_START_TAG = pc("^([a-z0-9]+)(.*?)(/?)$", REGEX_FLAGS_SI);
  private static final Pattern P_QUOTED_ATTRIBUTES =
      pc("([a-z0-9]+)=([\"'])(.*?)\\2", REGEX_FLAGS_SI);
  private static final Pattern P_UNQUOTED_ATTRIBUTES =
      pc("([a-z0-9]+)(=)([^\"\\s']+)", REGEX_FLAGS_SI);
  private static final Pattern P_PROTOCOL = pc("^([^:]+):", REGEX_FLAGS_SI);
  private static final Pattern P_ENTITY = pc("&#(\\d+);?");
  private static final Pattern P_ENTITY_UNICODE = pc("&#x([0-9a-f]+);?");
  private static final Pattern P_ENCODE = pc("%([0-9a-f]{2});?");
  private static final Pattern P_VALID_ENTITIES = pc("&([^&;]*)(?=(;|&|$))");
  private static final Pattern P_VALID_QUOTES = pc("(>|^)([^<]+?)(<|$)", Pattern.DOTALL);
  private static final Pattern P_END_ARROW = pc("^>");
  private static final Pattern P_BODY_TO_END = pc("<([^>]*?)(?=<|$)");
  private static final Pattern P_XML_CONTENT = pc("(^|>)([^<]*?)(?=>)");
  private static final Pattern P_STRAY_LEFT_ARROW = pc("<([^>]*?)(?=<|$)");
  private static final Pattern P_STRAY_RIGHT_ARROW = pc("(^|>)([^<]*?)(?=>)");
  private static final Pattern P_AMP = pc("&");
  private static final Pattern P_QUOTE = pc("\"");
  private static final Pattern P_LEFT_ARROW = pc("<");
  private static final Pattern P_RIGHT_ARROW = pc(">");
  private static final Pattern P_BOTH_ARROWS = pc("<>");
  private static final Pattern P_DOUBLE_QUOT = pc("&quot;");

  private static Pattern pc(String regexp) {
    return Pattern.compile(regexp);
  }

  private static Pattern pc(String regexp, int flag) {
    return Pattern.compile(regexp, flag);
  }
  // @xxx could grow large... maybe use sesat's ReferenceMap
  private static final ConcurrentMap<String, Pattern> P_REMOVE_PAIR_BLANKS =
      new ConcurrentHashMap<>();
  private static final ConcurrentMap<String, Pattern> P_REMOVE_SELF_BLANKS =
      new ConcurrentHashMap<>();

  /** counts of open tags for each (allowable) html element * */
  private final GroupStatisticalMap<String> vTagCounts = new GroupStatisticalMap<>();

  /** Default constructor. */
  public HtmlTextFilter() {
    this(false);
  }

  /**
   * Set debug flag to true. Otherwise use default settings. See the default constructor.
   *
   * @param debug turn debug on with a true argument
   */
  public HtmlTextFilter(final boolean debug) {
    allowedElement = LinkedListMultimap.create();
    allowedElement.put("a", "href");
    allowedElement.put("a", "target");

    allowedElement.put("img", "src");
    allowedElement.put("img", "width");
    allowedElement.put("img", "height");
    allowedElement.put("img", "alt");

    selfClosingTags = new String[] {"img"};
    needClosingTags = new String[] {"a", "b", "strong", "i", "em"};
    disallowed = new String[] {};
    allowedProtocols = new String[] {"http", "mailto", "https"}; // no ftp.
    protocolAttributes = new String[] {"src", "href"};
    removeBlanksElement = new String[] {"a", "b", "strong", "i", "em"};
    allowedEntities = new String[] {"amp", "gt", "lt", "quot"};
    isStripComment = true;
    isEncodeQuotes = true;
    isAlwaysMakeTags = true;
    isDebug = debug;
  }

  private void debug(final String msg) {
    if (isDebug) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(msg);
      }
    }
  }

  // ---------------------------------------------------------------
  // my versions of some PHP library functions
  public static String chr(final int decimal) {
    return String.valueOf((char) decimal);
  }

  public static String htmlSpecialChars(final String s) {
    String result = s;
    result = regexReplace(P_AMP, "&", result);
    result = regexReplace(P_QUOTE, "\"", result);
    result = regexReplace(P_LEFT_ARROW, "&lt", result);
    result = regexReplace(P_RIGHT_ARROW, ">", result);
    return result;
  }

  // ---------------------------------------------------------------
  /**
   * given a user submitted input String, filter out any invalid or restricted html.
   *
   * @param input text (i.e. submitted by a user) than may contain html
   * @return "clean" version of input, with only valid, whitelisted html elements allowed
   */
  public String filter(final String input) {
    vTagCounts.clear();
    String s = input;

    debug("************************************************");
    debug("              INPUT: " + input);

    s = escapeComments(s);
    debug("     escapeComments: " + s);

    s = balanceHTML(s);
    debug("        balanceHTML: " + s);

    s = checkTags(s);
    debug("          checkTags: " + s);

    s = processRemoveBlanks(s);
    debug("processRemoveBlanks: " + s);

    s = validateEntities(s);
    debug("    validateEntites: " + s);

    debug("************************************************\n\n");
    return s;
  }

  private String escapeComments(final String s) {
    final Matcher m = P_COMMENTS.matcher(s);
    final StringBuilder buf = new StringBuilder();
    if (m.find()) {
      final String match = m.group(1); // (.*?)
      m.appendReplacement(buf, Matcher.quoteReplacement("<!--" + htmlSpecialChars(match) + "-->"));
    }
    m.appendTail(buf);

    return buf.toString();
  }

  private String balanceHTML(String s) {
    if (isAlwaysMakeTags) {
      //
      // try and form html
      //
      s = regexReplace(P_END_ARROW, "", s);
      s = regexReplace(P_BODY_TO_END, "<$1>", s);
      s = regexReplace(P_XML_CONTENT, "$1<$2", s);

    } else {
      //
      // escape stray brackets
      //
      s = regexReplace(P_STRAY_LEFT_ARROW, "<$1", s);
      s = regexReplace(P_STRAY_RIGHT_ARROW, "$1$2><", s);

      //
      // the last regexp causes '<>' entities to appear
      // (we need to do a lookahead assertion so that the last bracket can
      // be used in the next pass of the regexp)
      //
      s = regexReplace(P_BOTH_ARROWS, "", s);
    }

    return s;
  }

  private String checkTags(String s) {
    Matcher m = P_TAGS.matcher(s);

    final StringBuilder buf = new StringBuilder();
    while (m.find()) {
      String replaceStr = m.group(1);
      replaceStr = processTag(replaceStr);
      m.appendReplacement(buf, Matcher.quoteReplacement(replaceStr));
    }
    m.appendTail(buf);

    // these get tallied in processTag
    // (remember to reset before subsequent calls to filter method)
    StringBuilder sBuilder = new StringBuilder(buf.toString());
    for (String key : vTagCounts.keySet()) {
      for (int ii = 0; ii < vTagCounts.get(key); ii++) {
        sBuilder.append("</").append(key).append(">");
      }
    }
    s = sBuilder.toString();

    return s;
  }

  private String processRemoveBlanks(final String s) {
    String result = s;
    for (String tag : removeBlanksElement) {
      if (!P_REMOVE_PAIR_BLANKS.containsKey(tag)) {
        P_REMOVE_PAIR_BLANKS.putIfAbsent(
            tag, Pattern.compile("<" + tag + "(\\s[^>]*)?></" + tag + ">"));
      }
      result = regexReplace(P_REMOVE_PAIR_BLANKS.get(tag), "", result);
      if (!P_REMOVE_SELF_BLANKS.containsKey(tag)) {
        P_REMOVE_SELF_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?/>"));
      }
      result = regexReplace(P_REMOVE_SELF_BLANKS.get(tag), "", result);
    }

    return result;
  }

  private static String regexReplace(
      final Pattern regex_pattern, final String replacement, final String s) {
    Matcher m = regex_pattern.matcher(s);
    return m.replaceAll(replacement);
  }

  private String processTag(final String s) {
    // ending tags
    Matcher m = P_END_TAG.matcher(s);
    if (m.find()) {
      final String name = m.group(1).toLowerCase();
      if (allowed(name)) {
        if (!inArray(name, selfClosingTags)) {
          if (vTagCounts.containsKey(name)) {
            vTagCounts.decrease(name);
            return "</" + name + ">";
          }
        }
      }
    }

    // starting tags
    m = P_START_TAG.matcher(s);
    if (m.find()) {
      final String name = m.group(1).toLowerCase();
      final String body = m.group(2);
      String ending = m.group(3);

      // debug( "in a starting tag, name='" + name + "'; body='" + body + "'; ending='" + ending +
      // "'" );
      if (allowed(name)) {
        StringBuilder params = new StringBuilder();

        final Matcher m2 = P_QUOTED_ATTRIBUTES.matcher(body);
        final Matcher m3 = P_UNQUOTED_ATTRIBUTES.matcher(body);
        final List<String> paramNames = new ArrayList<>();
        final List<String> paramValues = new ArrayList<>();
        while (m2.find()) {
          paramNames.add(m2.group(1)); // ([a-z0-9]+)
          paramValues.add(m2.group(3)); // (.*?)
        }
        while (m3.find()) {
          paramNames.add(m3.group(1)); // ([a-z0-9]+)
          paramValues.add(m3.group(3)); // ([^\"\\s']+)
        }

        String paramName, paramValue;
        for (int ii = 0; ii < paramNames.size(); ii++) {
          paramName = paramNames.get(ii).toLowerCase();
          paramValue = paramValues.get(ii);

          //          debug( "paramName='" + paramName + "'" );
          //          debug( "paramValue='" + paramValue + "'" );
          //          debug( "allowed? " + vAllowed.get( name ).contains( paramName ) );

          if (allowedAttribute(name, paramName)) {
            if (inArray(paramName, protocolAttributes)) {
              paramValue = processParamProtocol(paramValue);
            }
            params.append(" ").append(paramName).append("=\"").append(paramValue).append("\"");
          }
        }

        if (inArray(name, selfClosingTags)) {
          ending = " /";
        }

        if (inArray(name, needClosingTags)) {
          ending = "";
        }

        if (ending == null || ending.length() < 1) {
          vTagCounts.increase(name);
        } else {
          ending = " /";
        }
        return "<" + name + params + ending + ">";
      } else {
        return "";
      }
    }

    // comments
    m = P_COMMENT.matcher(s);
    if (!isStripComment && m.find()) {
      return "<" + m.group() + ">";
    }

    return "";
  }

  private String processParamProtocol(String s) {
    s = decodeEntities(s);
    final Matcher m = P_PROTOCOL.matcher(s);
    if (m.find()) {
      final String protocol = m.group(1);
      if (!inArray(protocol, allowedProtocols)) {
        // bad protocol, turn into local anchor link instead
        s = "#" + s.substring(protocol.length() + 1);
        if (s.startsWith("#//")) {
          s = "#" + s.substring(3, s.length());
        }
      }
    }

    return s;
  }

  private String decodeEntities(String s) {
    StringBuilder buf = new StringBuilder();

    Matcher m = P_ENTITY.matcher(s);
    while (m.find()) {
      final String match = m.group(1);
      final int decimal = Integer.decode(match);
      m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
    }
    m.appendTail(buf);
    s = buf.toString();

    buf = new StringBuilder();
    m = P_ENTITY_UNICODE.matcher(s);
    while (m.find()) {
      final String match = m.group(1);
      final int decimal = Integer.valueOf(match, 16);
      m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
    }
    m.appendTail(buf);
    s = buf.toString();

    buf = new StringBuilder();
    m = P_ENCODE.matcher(s);
    while (m.find()) {
      final String match = m.group(1);
      final int decimal = Integer.valueOf(match, 16);
      m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
    }
    m.appendTail(buf);
    s = buf.toString();

    s = validateEntities(s);
    return s;
  }

  private String validateEntities(final String s) {
    StringBuilder buf = new StringBuilder();

    // validate entities throughout the string
    Matcher m = P_VALID_ENTITIES.matcher(s);
    while (m.find()) {
      final String one = m.group(1); // ([^&;]*)
      final String two = m.group(2); // (?=(;|&|$))
      m.appendReplacement(buf, Matcher.quoteReplacement(checkEntity(one, two)));
    }
    m.appendTail(buf);

    return encodeQuotes(buf.toString());
  }

  private String encodeQuotes(final String s) {
    if (isEncodeQuotes) {
      StringBuilder buf = new StringBuilder();
      Matcher m = P_VALID_QUOTES.matcher(s);
      while (m.find()) {
        final String one = m.group(1); // (>|^)
        final String two = m.group(2); // ([^<]+?)
        final String three = m.group(3); // (<|$)
        m.appendReplacement(
            buf, Matcher.quoteReplacement(one + regexReplace(P_QUOTE, "&quot;", two) + three));
      }
      m.appendTail(buf);
      return buf.toString();
    } else {
      return s;
    }
  }

  private String checkEntity(final String preamble, final String term) {

    return ";".equals(term) && isValidEntity(preamble) ? '&' + preamble : "&" + preamble;
  }

  private boolean isValidEntity(final String entity) {
    return inArray(entity, allowedEntities);
  }

  private static boolean inArray(final String s, final String[] array) {
    for (String item : array) {
      if (item != null && item.equals(s)) {
        return true;
      }
    }
    return false;
  }

  private boolean allowed(final String name) {
    return (allowedElement.isEmpty() || allowedElement.containsKey(name))
        && !inArray(name, disallowed);
  }

  private boolean allowedAttribute(final String name, final String paramName) {
    return allowed(name)
        && (allowedElement.isEmpty() || allowedElement.get(name).contains(paramName));
  }
}
