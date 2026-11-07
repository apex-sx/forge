//package terraqo.arbiter.assist;
//
//import java.util.Locale;
//
///**
// * ## 字符串工具函数功能归纳
// * <p>
// * ### **一、字符判断类 (Character Checking)**
// * <p>
// * 1. **空白字符判断**
// * - `isBlankChar(int/char)` - 判断是否为空白字符(空格、制表符等)
// * - `isWhitespace(int)` - 检查是否为空白字符
// * <p>
// * 2. **ASCII字符判断**
// * - `isAsciiChar/isAscii(int)` - 判断是否为ASCII字符(0-127)
// * - `isAsciiPrintableChar/isAsciiPrintable(int)` - 判断是否为可见ASCII字符(32-126)
// * - `isAsciiControlChar/isAsciiControl(int)` - 判断是否为ASCII控制符
// * <p>
// * 3. **字母数字判断**
// * - `isLetterChar(int/char)` - 判断是否为字母(A-Z, a-z)
// * - `isLetterUpperChar(int/char)` - 判断是否为大写字母
// * - `isLetterLowerChar(int/char)` - 判断是否为小写字母
// * - `isNumberChar(int/char)` - 判断是否为数字字符(0-9)
// * - `isLetterOrNumberChar(int/char)` - 判断是否为字母或数字
// * - `isNumberHexChar/isHex(int/char)` - 判断是否为十六进制字符
// * <p>
// * 4. **特殊字符判断**
// * - `isChinese(char)` - 判断是否为中日韩文字
// * - `isEmojiChar/isEmoji(int)` - 判断是否为emoji表情
// * - `isPathSeparatorChar/isPathSeparator(int)` - 判断是否为路径分隔符
// * - `isURLCodePoint(int)` - 判断是否为URL有效字符
// * - `isQuadEm(int)` - 判断是否为全角字符
// * <p>
// * ### **二、字符串判空类 (String Validation)**
// * <p>
// * 5. **空值判断**
// * - `isEmpty(CharSequence)` - 判断是否为null或空字符串
// * - `isNotEmpty(CharSequence)` - 判断是否不为空
// * - `isBlank(CharSequence)` - 判断是否为null、空或纯空白
// * - `isNotBlank(CharSequence)` - 判断是否不为空白
// * <p>
// * 6. **批量判断**
// * - `areAllEmpty(CharSequence...)` - 判断是否全部为空
// * - `areAllNotEmpty(CharSequence...)` - 判断是否全部不为空
// * - `areAllBlank(CharSequence...)` - 判断是否全部为空白
// * - `areAllNotBlank(CharSequence...)` - 判断是否全部不为空白
// * <p>
// * ### **三、字符串比较类 (String Comparison)**
// * <p>
// * 7. **相等性比较**
// * - `isEquals(boolean ignoreCase, CharSequence, CharSequence)` - 比较两个字符串是否相等
// * - `isEquals(boolean ignoreCase, int, int)` - 比较两个字符是否相等
// * - `isEqualChar(char, char, boolean)` - 比较字符是否相同
// * <p>
// * 8. **包含关系**
// * - `isContain(boolean, CharSequence, CharSequence)` - 判断是否包含目标字符串
// * - `isContainAll(boolean, CharSequence, CharSequence...)` - 判断是否包含所有目标
// * - `isContainAny(boolean, CharSequence, CharSequence...)` - 判断是否包含任意一个目标
// * <p>
// * 9. **前后缀判断**
// * - `isStartsWith(boolean, CharSequence/char[]/int[], CharSequence/target)` - 判断是否以指定前缀开始
// * - `isEndsWith(boolean, CharSequence/char[]/int[], CharSequence/target)` - 判断是否以指定后缀结束
// * - `isSurround(boolean, CharSequence, prefix, suffix)` - 判断是否被指定前后缀包围
// * <p>
// * 10. **区域匹配**
// * - `regionMatches(boolean, input, inputOffset, target, targetOffset, count)` - 比较指定区域是否匹配
// * <p>
// * ### **四、字符串修剪类 (String Trimming)**
// * <p>
// * 11. **基本修剪**
// * - `trim(CharSequence, Predicate)` - 修剪首尾满足条件的字符
// * - `trimLeading(Predicate)` - 修剪头部满足条件的字符
// * - `trimTrailing(Predicate)` - 修剪尾部满足条件的字符
// * <p>
// * 12. **特定字符修剪**
// * - `trimWhitespace` - 修剪空白字符
// * - `trimCharacter(String)` - 修剪指定字符
// * - `trimLeadingCharacter(String)` - 修剪头部指定字符
// * - `trimTrailingCharacter(String)` - 修剪尾部指定字符
// * <p>
// * 13. **修剪边界计算**
// * - `calculateTrimBounds(CharSequence, ...)` - 计算修剪后的边界
// * <p>
// * ### **五、字符串转换类 (String Conversion)**
// * <p>
// * 14. **大小写转换**
// * - `capitalize(CharSequence, boolean)` - 首字母大写或小写
// * - `uncapitalize(CharSequence)` - 首字母小写
// * - `changeFirstCharacterCase(CharSequence, boolean)` - 转换首字符大小写
// * <p>
// * 15. **命名风格转换**
// * - `toCamelCase(CharSequence, boolean)` - 转换为驼峰命名
// * - `toKebabCase(CharSequence, boolean)` - 转换为短横杠命名
// * - `toUnderlineCase(CharSequence, boolean)` - 转换为下划线命名
// * - `formatNameString(CharSequence, delimiter, isUpper, isUpperHead)` - 格式化名称字符串
// * - `findNameWord(CharSequence, isSplitNumber, isUpper)` - 提取单词
// * <p>
// * 16. **类型转换**
// * - `asString(CharSequence, ...)` - 转换为字符串
// * - `asCharacters(CharSequence, ...)` - 转换为字符数组
// * - `asCodePoints(CharSequence, ...)` - 转换为Unicode码点数组
// * - `toCharacter(Object, defaultValue)` - 转换为Character对象
// * - `toInt(int/char, radix)` - 按进制转换为整数
// * <p>
// * ### **六、字符串查找类 (String Searching)**
// * <p>
// * 17. **位置查找**
// * - `indexOf(boolean, input, start, end, target)` - 查找首次出现位置
// * - `lastIndexOf(boolean, input, start, end, target)` - 查找最后出现位置
// * - `indexOf(..., occurrence)` - 查找第n次出现位置
// * - `lastIndexOf(..., occurrence)` - 查找倒数第n次出现位置
// * <p>
// * 18. **计数统计**
// * - `countOccurrences(boolean, input, target)` - 统计出现次数
// * <p>
// * ### **七、字符串操作类 (String Manipulation)**
// * <p>
// * 19. **替换操作**
// * - `replace(boolean, input, target, replacement, replaceCount)` - 替换指定内容
// * - `replacePlaceholder(String, Map/Function, boolean)` - 替换占位符(${...})
// * - `clean(boolean, input, targets...)` - 清理指定内容
// * - `clean(CharSequence, IntPredicate)` - 根据条件清理
// * <p>
// * 20. **截取操作**
// * - `substring(CharSequence, start, end)` - 截取子串
// * - `subString(String, fromIndex, toIndex)` - 改进版substring
// * - `brief(CharSequence, briefChar, briefLength, leadingLength, trailingLength)` - 缩略显示
// * <p>
// * 21. **分割操作**
// * - `splitLine(CharSequence)` - 按行分割
// * - `split(boolean, delimiter, ..., input)` - 按分隔符分割
// * - `splitString(String, splitChar, trimChar, ...)` - 简单分割字符串
// * - `splitToPair(String, splitSymbol, greedy)` - 分割为键值对
// * <p>
// * 22. **连接操作**
// * - `join(delimiter, elementPrefix, elementSuffix, prefix, suffix, input)` - 连接字符串数组/集合
// * <p>
// * 23. **反转操作**
// * - `reverse(CharSequence)` - 反转字符串
// * <p>
// * ### **八、转义与解析类 (Escaping & Parsing)**
// * <p>
// * 24. **转义处理**
// * - `escapeChar(char, boolean unescape)` - 转义/反转义特殊字符
// * - `escapeQuotes(char[], MutableInteger, end, doubleQuotes)` - 解析引号内数据(支持转义)
// * - `unescapeQuotes(char[], unescapeUnicode, unescapeChars, doubleQuotes)` - 转为引号格式
// * <p>
// * 25. **数字解析**
// * - `resolveNumber(char[], MutableInteger, end)` - 解析数字字符串
// * - `resolveBooleanOrNull(char[], MutableInteger, end)` - 解析布尔值
// * - `isNumber(String)` - 判断是否为数字
// * - `isNumberHuman(String)` - 判断是否为人类可读数字
// * - `isIntegerNumber(String)` - 判断是否为整数
// * - `isDecimalNumber(String)` - 判断是否为浮点数
// * <p>
// * ### **九、其他工具类 (Other Utilities)**
// * <p>
// * 26. **引用包装**
// * - `quote(String)` - 用单引号包裹字符串
// * - `quoteIfString(Object)` - 如果是字符串则用单引号包裹
// * <p>
// * 27. **删除操作**
// * - `deleteString(String, pattern)` - 删除所有出现的子串
// * - `deleteChars(String, chars)` - 删除指定字符
// * - `clearStringBuilder(StringBuilder)` - 清空StringBuilder
// * - `addStringAndClear(Collection, StringBuilder)` - 添加并清空
// * <p>
// * 28. **重复与填充**
// * - `repeatChar(char, count)` - 重复字符
// * <p>
// * 29. **Unicode处理**
// * - `checkGetSingletonChar(int)` - 检查并获取单个字符
// * - `checkGetSingletonChars(String)` - 检查并获取字符数组
// * <p>
// * 30. **杂项**
// * - `containsWhitespace(CharSequence)` - 检查是否包含空白
// * - `containsAny(String, char...)` - 检查是否包含任意指定字符
// * <p>
// * ---
// * <p>
// * ## **主要特点总结:**
// * <p>
// * ✅ **去重后保留约30个核心功能类别**
// * ✅ **支持多种数据类型**: CharSequence, char[], int[], String
// * ✅ **支持忽略大小写**: 大部分比较和查找方法都支持ignoreCase参数
// * ✅ **支持范围操作**: 多数方法支持start/end索引范围
// * ✅ **Unicode友好**: 正确处理代理对和码点
// * ✅ **高性能**: 使用KMP算法进行模式匹配
// * ✅ **函数式编程**: 大量使用Predicate、Function等函数式接口
// *
// * @author Terraqo
// */
//public final class Texts {
//
//    /**
//     * 特殊字符--> {@code \b}
//     */
//    char SC_B = 'b',SC_F = 'f',SC_N = 'n',SC_R = 'r',SC_T = 't';
//    /**
//     * 字符常量：空格符 {@code ' '}
//     */
//    char SPACE = ' ', TAB = '\t', CR = '\r', LF = '\n', DOT = '.',COMMA=',', SLASH = '/', BACKSLASH = '\\';
//    /**
//     * 单引号
//     */
//    char QUOTE = '\'', QUOTES = '"';
//    /**
//     * 字符常量：减号（连接符） {@code '-'}
//     */
//    char DASHED = '-', UNDERLINE = '_';
//    /**
//     * 字符常量：花括号（左） <code>'{'</code>
//     */
//    char DELIM_START = '{';
//    /**
//     * 字符常量：花括号（右） <code>'}'</code>
//     */
//    char DELIM_END = '}';
//    /**
//     * 字符常量：中括号（左） {@code '['}
//     */
//    char BRACKET_START = '[';
//    /**
//     * 字符常量：中括号（右） {@code ']'}
//     */
//    char BRACKET_END = ']';
//    /**
//     * 字符常量：双引号 {@code '"'}
//     */
//    char DOUBLE_QUOTES = '"';
//    /**
//     * 字符常量：单引号 {@code '\''}
//     */
//    char SINGLE_QUOTE = '\'';
//    /**
//     * 字符常量：与 {@code '&'}
//     */
//    char AMP = '&';
//    /**
//     * 字符常量：冒号 {@code ':'}
//     */
//    char COLON = ':';
//    /**
//     * 字符常量：艾特 {@code '@'}
//     */
//    char AT = '@';
//    /**
//     * Ascii 小写字母编码上界
//     */
//    int ASCII_LOWER_LETTER_HI = 122;
//    /**
//     * Ascii 小写字母编码下界
//     */
//    int ASCII_LOWER_LETTER_LO = 97;
//    /**
//     * Ascii 数字编码上界
//     */
//    int ASCII_NUMBER_HI = 57;
//    /**
//     * Ascii 数字编码下界
//     */
//    int ASCII_NUMBER_LO = 48;
//    /**
//     * Ascii 大写字母编码上界
//     */
//    int ASCII_UPPER_LETTER_HI = 90;
//    /**
//     * Ascii 大写字母编码下界
//     */
//    int ASCII_UPPER_LETTER_LO = 65;
//    /**
//     * 大写HEX
//     */
//    char[] UPPER_HEX =
//            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//    /**
//     * 小写HEX
//     */
//    char[] LOWER_HEX =
//            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//    /**
//     *
//     */
//    String EMPTY = "";
//    /**
//     * 字符串常量：{@code "null"} <br>
//     * 注意：{@code "null" != null}
//     */
//    String NULL = "null";
//    /**
//     * 字符串常量：{@code "undefined"} <br>
//     * 注意：{@code "null" != null}
//     */
//    String UNDEFINED = "undefined";
//
//
//    /**
//     * 字符常量：空格符 {@code ' '}
//     */
//    public static final char C_SPACE = HelpChar.SPACE;
//    /**
//     * 字符常量：制表符 {@code '\t'}
//     */
//    public static final char C_TAB = HelpChar.TAB;
//    /**
//     * 字符常量：点 {@code '.'}
//     */
//    public static final char C_DOT = HelpChar.DOT;
//    /**
//     * 字符常量：斜杠 {@code '/'}
//     */
//    public static final char C_SLASH = HelpChar.SLASH;
//    /**
//     * 字符常量：反斜杠 {@code '\\'}
//     */
//    public static final char C_BACKSLASH = HelpChar.BACKSLASH;
//    /**
//     * 字符常量：回车符 {@code '\r'}
//     */
//    public static final char C_CR = HelpChar.CR;
//    /**
//     * 字符常量：换行符 {@code '\n'}
//     */
//    public static final char C_LF = HelpChar.LF;
//    /**
//     * 字符常量：下划线 {@code '_'}
//     */
//    public static final char C_UNDERLINE = HelpChar.UNDERLINE;
//    /**
//     * 字符常量：逗号 {@code ','}
//     */
//    public static final char C_COMMA = HelpChar.COMMA;
//    /**
//     * 字符常量：花括号（左） <code>'{'</code>
//     */
//    public static final char C_DELIM_START = HelpChar.DELIM_START;
//    /**
//     * 字符常量：花括号（右） <code>'}'</code>
//     */
//    public static final char C_DELIM_END = HelpChar.DELIM_END;
//    /**
//     * 字符常量：中括号（左） {@code '['}
//     */
//    public static final char C_BRACKET_START = HelpChar.BRACKET_START;
//    /**
//     * 字符常量：中括号（右） {@code ']'}
//     */
//    public static final char C_BRACKET_END = HelpChar.BRACKET_END;
//    /**
//     * 字符常量：冒号 {@code ':'}
//     */
//    public static final char C_COLON = HelpChar.COLON;
//    /**
//     * 字符常量：艾特 {@code '@'}
//     */
//    public static final char C_AT = HelpChar.AT;
//    /**
//     * 字符串常量：制表符 {@code "\t"}
//     */
//    public static final String TAB = "	";
//    /**
//     * 字符串常量：点 {@code "."}
//     */
//    public static final String DOT = ".";
//    /**
//     * 字符串常量：双点 {@code ".."} <br>
//     * 用途：作为指向上级文件夹的路径，如：{@code "../path"}
//     */
//    public static final String DOUBLE_DOT = "..";
//    /**
//     * 字符串常量：斜杠 {@code "/"}
//     */
//    public static final String SLASH = "/";
//    /**
//     * 字符串常量：反斜杠 {@code "\\"}
//     */
//    public static final String BACKSLASH = "\\";
//    /**
//     * 字符串常量：回车符 {@code "\r"} <br>
//     * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
//     */
//    public static final String CR = "\r";
//    /**
//     * 字符串常量：换行符 {@code "\n"}
//     */
//    public static final String LF = "\n";
//    /**
//     * 字符串常量：Windows 换行 {@code "\r\n"} <br>
//     * 解释：该字符串常用于表示 Windows 系统下的文本换行
//     */
//    public static final String CRLF = "\r\n";
//    /**
//     * 字符串常量：下划线 {@code "_"}
//     */
//    public static final String UNDERLINE = "_";
//    /**
//     * 字符串常量：减号（连接符） {@code "-"}
//     */
//    public static final String DASHED = "-";
//    /**
//     * 字符串常量：逗号 {@code ","}
//     */
//    public static final String COMMA = ",";
//    /**
//     * 字符串常量：花括号（左） <code>"{"</code>
//     */
//    public static final String DELIM_START = "{";
//    /**
//     * 字符串常量：花括号（右） <code>"}"</code>
//     */
//    public static final String DELIM_END = "}";
//    /**
//     * 字符串常量：中括号（左） {@code "["}
//     */
//    public static final String BRACKET_START = "[";
//    /**
//     * 字符串常量：中括号（右） {@code "]"}
//     */
//    public static final String BRACKET_END = "]";
//    /**
//     * 字符串常量：冒号 {@code ":"}
//     */
//    public static final String COLON = ":";
//    /**
//     * 字符串常量：艾特 {@code "@"}
//     */
//    public static final String AT = "@";
//    /**
//     * 字符串常量：HTML 空格转义 {@code "&nbsp;" -> " "}
//     */
//    public static final String HTML_NBSP = XmlUtil.NBSP;
//    /**
//     * 字符串常量：HTML And 符转义 {@code "&amp;" -> "&"}
//     */
//    public static final String HTML_AMP = XmlUtil.AMP;
//    /**
//     * 字符串常量：HTML 双引号转义 {@code "&quot;" -> "\""}
//     */
//    public static final String HTML_QUOTE = XmlUtil.QUOTE;
//    /**
//     * 字符串常量：HTML 单引号转义 {@code "&apos" -> "'"}
//     */
//    public static final String HTML_APOS = XmlUtil.APOS;
//    /**
//     * 字符串常量：HTML 小于号转义 {@code "&lt;" -> "<"}
//     */
//    public static final String HTML_LT = XmlUtil.LT;
//    /**
//     * 字符串常量：HTML 大于号转义 {@code "&gt;" -> ">"}
//     */
//    public static final String HTML_GT = XmlUtil.GT;
//    /**
//     * 字符串常量：空 JSON {@code "{}"}
//     */
//    public static final String EMPTY_JSON = "{}";
//    /**
//     * 字符串常量：空格符 {@code " "}
//     */
//    String SPACE = " ";
//    /**
//     * 字符串常量：制表符 {@code "\t"}
//     */
//    String TAB = "	";
//    /**
//     * 字符串常量：点 {@code "."}
//     */
//    String DOT = ".";
//    /**
//     * 字符串常量：双点 {@code ".."} <br>
//     * 用途：作为指向上级文件夹的路径，如：{@code "../path"}
//     */
//    String DOUBLE_DOT = "..";
//    /**
//     * 字符串常量：斜杠 {@code "/"}
//     */
//    String SLASH = "/";
//    /**
//     * 字符串常量：反斜杠 {@code "\\"}
//     */
//    String BACKSLASH = "\\";
//    /**
//     * 字符串常量：回车符 {@code "\r"} <br>
//     * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
//     */
//    String CR = "\r";
//    /**
//     * 字符串常量：换行符 {@code "\n"}
//     */
//    String LF = "\n";
//    /**
//     * 字符串常量：Windows 换行 {@code "\r\n"} <br>
//     * 解释：该字符串常用于表示 Windows 系统下的文本换行
//     */
//    String CRLF = "\r\n";
//    /**
//     * 字符串常量：下划线 {@code "_"}
//     */
//    String UNDERLINE = "_";
//    /**
//     * 字符串常量：减号（连接符） {@code "-"}
//     */
//    String DASHED = "-";
//    /**
//     * 字符串常量：逗号 {@code ","}
//     */
//    String COMMA = ",";
//    /**
//     * 字符串常量：花括号（左） <code>"{"</code>
//     */
//    String DELIM_START = "{";
//    /**
//     * 字符串常量：花括号（右） <code>"}"</code>
//     */
//    String DELIM_END = "}";
//    /**
//     * 字符串常量：中括号（左） {@code "["}
//     */
//    String BRACKET_START = "[";
//    /**
//     * 字符串常量：中括号（右） {@code "]"}
//     */
//    String BRACKET_END = "]";
//    /**
//     * 字符串常量：冒号 {@code ":"}
//     */
//    String COLON = ":";
//    /**
//     * 字符串常量：艾特 {@code "@"}
//     */
//    String AT = "@";
//    /**
//     * 字符串常量：HTML 空格转义 {@code "&nbsp;" -> " "}
//     */
//    String HTML_NBSP = ToolXML.NBSP;
//    /**
//     * 字符串常量：HTML And 符转义 {@code "&amp;" -> "&"}
//     */
//    String HTML_AMP = ToolXML.AMP;
//    /**
//     * 字符串常量：HTML 双引号转义 {@code "&quot;" -> "\""}
//     */
//    String HTML_QUOTE = ToolXML.QUOTE;
//    /**
//     * 字符串常量：HTML 单引号转义 {@code "&apos" -> "'"}
//     */
//    String HTML_APOS = ToolXML.APOS;
//    /**
//     * 字符串常量：HTML 小于号转义 {@code "&lt;" -> "<"}
//     */
//    String HTML_LT = ToolXML.LT;
//    /**
//     * 字符串常量：HTML 大于号转义 {@code "&gt;" -> ">"}
//     */
//    String HTML_GT = ToolXML.GT;
//    /**
//     * 字符串常量：空 JSON {@code "{}"}
//     */
//    String EMPTY_OBJECT = "{}";
//    /**
//     * 字符串常量：空 JSON {@code "{}"}
//     */
//    String EMPTY_ARRAY = "[]";
//    /**
//     * 无效空白符
//     */
//    String INVALID_BLANK = " \t\n\r\f";
//    /**
//     * 大字母
//     */
//    String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//    /**
//     * 小字母
//     */
//    String LOWER = "abcdefghijklmnopqrstuvwxyz";
//    /**
//     * 系统行分割符
//     */
//    String SYSTEM_LINE_SEPARATOR = ((Supplier<String>) () -> {
//        // avoid security issues
//        StringWriter buf = new StringWriter(2);
//        PrintWriter out = new PrintWriter(buf);
//        out.println();
//        return buf.toString();
//    }).get();
//    /**
//     * ASCII字符对应的字符串缓存
//     */
//    InstanceSupplier<String[]> ASCII_CACHE = InstanceSupplier.of(() -> {
//        String[] cache = new String[128];
//        for (char c = 0; c < cache.length; c++) {
//            cache[c] = String.valueOf(c);
//        }
//        return cache;
//    });
//
//    @Nonnull
//    static BitSet getLetterNumberBitSet() {
//        BitSet safeChars = new BitSet(256);
//        // alpha characters
//        for (int i = 'a'; i <= 'z'; i++) {
//            safeChars.set(i);
//        }
//        for (int i = 'A'; i <= 'Z'; i++) {
//            safeChars.set(i);
//        }
//        // numeric characters
//        for (int i = '0'; i <= '9'; i++) {
//            safeChars.set(i);
//        }
//        return safeChars;
//    }
//
//    /**
//     * 检查并获取unicode的单个Char，如果单个char无法表达则抛出异常
//     *
//     * @param input unicode
//     * @return 返回字符
//     */
//    public static char checkGetSingletonChar(int input) {
//        char[] chars = Character.toChars(input);
//        if (chars.length != 1) {
//            throw new IllegalArgumentException(
//                    "Characters are generated incorrectly, unicode ["
//                            + input
//                            + "] is not in the range of char characters");
//        }
//        return chars[0];
//    }
//
//    /**
//     * 是否空白符<br>
//     * 空白符包括空格、制表符、全角空格和不间断空格<br>
//     *
//     * @param input 字符
//     * @return 是否空白符
//     * @see Character#isWhitespace(int)
//     * @see Character#isSpaceChar(int)
//     */
//    public static boolean isBlankChar(char input) {
//        return CharHelper.isBlankChar((int) input);
//    }
//
//    /**
//     * 是否空白符<br>
//     * 空白符包括空格、制表符、全角空格和不间断空格<br>
//     *
//     * @param input 字符
//     * @return 是否空白符
//     * @see Character#isWhitespace(int)
//     * @see Character#isSpaceChar(int)
//     */
//    public static boolean isBlankChar(int input) {
//        return Character.isWhitespace(input)
//                || Character.isSpaceChar(input)
//                || input == '\ufeff'
//                || input == '\u202a';
//    }
//
//    /**
//     * 是否为ASCII字符，ASCII字符位于0~127之间
//     *
//     * <pre>
//     *   CharUtil.isAscii('a')  = true
//     *   CharUtil.isAscii('A')  = true
//     *   CharUtil.isAscii('3')  = true
//     *   CharUtil.isAscii('-')  = true
//     *   CharUtil.isAscii('\n') = true
//     *   CharUtil.isAscii('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符处
//     * @return true表示为ASCII字符，ASCII字符位于0~127之间
//     */
//    public static boolean isAsciiChar(char input) {
//        return input < 128;
//    }
//
//    /**
//     * 是否为可见ASCII字符，可见字符位于32~126之间
//     *
//     * <pre>
//     *   CharUtil.isAsciiPrintable('a')  = true
//     *   CharUtil.isAsciiPrintable('A')  = true
//     *   CharUtil.isAsciiPrintable('3')  = true
//     *   CharUtil.isAsciiPrintable('-')  = true
//     *   CharUtil.isAsciiPrintable('\n') = false
//     *   CharUtil.isAsciiPrintable('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符处
//     * @return true表示为ASCII可见字符，可见字符位于32~126之间
//     */
//    public static boolean isAsciiPrintableChar(char input) {
//        return input >= 32 && input < 127;
//    }
//
//    /**
//     * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
//     *
//     * <pre>
//     *   CharUtil.isAsciiControl('a')  = false
//     *   CharUtil.isAsciiControl('A')  = false
//     *   CharUtil.isAsciiControl('3')  = false
//     *   CharUtil.isAsciiControl('-')  = false
//     *   CharUtil.isAsciiControl('\n') = true
//     *   CharUtil.isAsciiControl('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为控制符，控制符位于0~31和127
//     */
//    public static boolean isAsciiControlChar(char input) {
//        return input < 32 || input == 127;
//    }
//
//    /**
//     * 判断是否为字母（包括大写字母和小写字母）<br>
//     * 字母包括A~Z和a~z
//     *
//     * <pre>
//     *   CharUtil.isLetter('a')  = true
//     *   CharUtil.isLetter('A')  = true
//     *   CharUtil.isLetter('3')  = false
//     *   CharUtil.isLetter('-')  = false
//     *   CharUtil.isLetter('\n') = false
//     *   CharUtil.isLetter('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
//     */
//    public static boolean isLetterChar(char input) {
//        return CharHelper.isLetterUpperChar(input) || CharHelper.isLetterLowerChar(input);
//    }
//
//    /**
//     * 判断是否为字母（包括大写字母和小写字母）<br>
//     * 字母包括A~Z和a~z
//     *
//     * <pre>
//     *   CharUtil.isLetter('a')  = true
//     *   CharUtil.isLetter('A')  = true
//     *   CharUtil.isLetter('3')  = false
//     *   CharUtil.isLetter('-')  = false
//     *   CharUtil.isLetter('\n') = false
//     *   CharUtil.isLetter('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
//     */
//    public static boolean isLetterChar(int input) {
//        return CharHelper.isLetterUpperChar(input) || CharHelper.isLetterLowerChar(input);
//    }
//
//    /**
//     * 判断是否为大写字母，大写字母包括A~Z
//     *
//     * <pre>
//     *   CharUtil.isLetterUpper('a')  = false
//     *   CharUtil.isLetterUpper('A')  = true
//     *   CharUtil.isLetterUpper('3')  = false
//     *   CharUtil.isLetterUpper('-')  = false
//     *   CharUtil.isLetterUpper('\n') = false
//     *   CharUtil.isLetterUpper('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为大写字母，大写字母包括A~Z
//     */
//    public static boolean isLetterUpperChar(char input) {
//        return input >= 'A' && input <= 'Z';
//    }
//
//    /**
//     * 判断是否为大写字母，大写字母包括A~Z
//     *
//     * <pre>
//     *   CharUtil.isLetterUpper('a')  = false
//     *   CharUtil.isLetterUpper('A')  = true
//     *   CharUtil.isLetterUpper('3')  = false
//     *   CharUtil.isLetterUpper('-')  = false
//     *   CharUtil.isLetterUpper('\n') = false
//     *   CharUtil.isLetterUpper('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为大写字母，大写字母包括A~Z
//     */
//    public static boolean isLetterUpperChar(int input) {
//        return input >= PoolCharacter.ASCII_UPPER_LETTER_LO && input <= PoolCharacter.ASCII_UPPER_LETTER_HI;
//    }
//
//    /**
//     * 检查字符是否为小写字母，小写字母指a~z
//     *
//     * <pre>
//     *   CharUtil.isLetterLower('a')  = true
//     *   CharUtil.isLetterLower('A')  = false
//     *   CharUtil.isLetterLower('3')  = false
//     *   CharUtil.isLetterLower('-')  = false
//     *   CharUtil.isLetterLower('\n') = false
//     *   CharUtil.isLetterLower('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为小写字母，小写字母指a~z
//     */
//    public static boolean isLetterLowerChar(char input) {
//        return input >= 'a' && input <= 'z';
//    }
//
//    /**
//     * 检查字符是否为小写字母，小写字母指a~z
//     *
//     * <pre>
//     *   CharUtil.isLetterLower('a')  = true
//     *   CharUtil.isLetterLower('A')  = false
//     *   CharUtil.isLetterLower('3')  = false
//     *   CharUtil.isLetterLower('-')  = false
//     *   CharUtil.isLetterLower('\n') = false
//     *   CharUtil.isLetterLower('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为小写字母，小写字母指a~z
//     */
//    public static boolean isLetterLowerChar(int input) {
//        return input >= PoolCharacter.ASCII_LOWER_LETTER_LO && input <= PoolCharacter.ASCII_LOWER_LETTER_HI;
//    }
//
//    /**
//     * 检查是否为数字字符，数字字符指0~9
//     *
//     * <pre>
//     *   CharUtil.isNumber('a')  = false
//     *   CharUtil.isNumber('A')  = false
//     *   CharUtil.isNumber('3')  = true
//     *   CharUtil.isNumber('-')  = false
//     *   CharUtil.isNumber('\n') = false
//     *   CharUtil.isNumber('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为数字字符，数字字符指0~9
//     */
//    public static boolean isNumberChar(char input) {
//        return input >= '0' && input <= '9';
//    }
//
//    /**
//     * 检查是否为数字字符，数字字符指0~9
//     *
//     * <pre>
//     *   CharUtil.isNumber('a')  = false
//     *   CharUtil.isNumber('A')  = false
//     *   CharUtil.isNumber('3')  = true
//     *   CharUtil.isNumber('-')  = false
//     *   CharUtil.isNumber('\n') = false
//     *   CharUtil.isNumber('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为数字字符，数字字符指0~9
//     */
//    public static boolean isNumberChar(int input) {
//        return input >= PoolCharacter.ASCII_NUMBER_LO && input <= PoolCharacter.ASCII_NUMBER_HI;
//    }
//
//    /**
//     * 是否为16进制规范的字符，判断是否为如下字符
//     *
//     * <pre>
//     * 1. 0~9
//     * 2. a~f
//     * 4. A~F
//     * </pre>
//     *
//     * @param input 字符
//     * @return 是否为16进制规范的字符
//     */
//    public static boolean isNumberHexChar(char input) {
//        return isNumberChar(input) || (input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F');
//    }
//
//    /**
//     * 是否为16进制规范的字符，判断是否为如下字符
//     *
//     * <pre>
//     * 1. 0~9
//     * 2. a~f
//     * 4. A~F
//     * </pre>
//     *
//     * @param input 字符
//     * @return 是否为16进制规范的字符
//     */
//    public static boolean isNumberHexChar(int input) {
//        return isNumberChar(input) || (input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F');
//    }
//
//    /**
//     * 是否为字符或数字，包括A~Z、a~z、0~9
//     *
//     * <pre>
//     *   CharUtil.isLetterOrNumber('a')  = true
//     *   CharUtil.isLetterOrNumber('A')  = true
//     *   CharUtil.isLetterOrNumber('3')  = true
//     *   CharUtil.isLetterOrNumber('-')  = false
//     *   CharUtil.isLetterOrNumber('\n') = false
//     *   CharUtil.isLetterOrNumber('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为字符或数字，包括A~Z、a~z、0~9
//     */
//    public static boolean isLetterOrNumberChar(char input) {
//        return isLetterChar(input) || isNumberChar(input);
//    }
//
//    /**
//     * 是否为字符或数字，包括A~Z、a~z、0~9
//     *
//     * <pre>
//     *   CharUtil.isLetterOrNumber('a')  = true
//     *   CharUtil.isLetterOrNumber('A')  = true
//     *   CharUtil.isLetterOrNumber('3')  = true
//     *   CharUtil.isLetterOrNumber('-')  = false
//     *   CharUtil.isLetterOrNumber('\n') = false
//     *   CharUtil.isLetterOrNumber('&copy;') = false
//     * </pre>
//     *
//     * @param input 被检查的字符
//     * @return true表示为字符或数字，包括A~Z、a~z、0~9
//     */
//    public static boolean isLetterOrNumberChar(int input) {
//        return isLetterChar(input) || isNumberChar(input);
//    }
//
//    public static boolean isURLCodePoint(int input) {
//        return isLetterOrNumberChar(input) || "/=?!@$+_-.:*&~'()".indexOf(input) >= 0 ||
//                (input >= 0x00A0 && input <= 0xD7FF) || (input >= 0xE000 && input <= 0xFDCF) ||
//                (input >= 0xFDF0 && input <= 0xFFEF) || (input >= 0x10000 && input <= 0x1FFFD) ||
//                (input >= 0x20000 && input <= 0x2FFFD) || (input >= 0x30000 && input <= 0x3FFFD) ||
//                (input >= 0x40000 && input <= 0x4FFFD) || (input >= 0x50000 && input <= 0x5FFFD) ||
//                (input >= 0x60000 && input <= 0x6FFFD) || (input >= 0x70000 && input <= 0x7FFFD) ||
//                (input >= 0x80000 && input <= 0x8FFFD) || (input >= 0x90000 && input <= 0x9FFFD) ||
//                (input >= 0xA0000 && input <= 0xAFFFD) || (input >= 0xB0000 && input <= 0xBFFFD) ||
//                (input >= 0xC0000 && input <= 0xCFFFD) || (input >= 0xD0000 && input <= 0xDFFFD) ||
//                (input >= 0xE0000 && input <= 0xEFFFD) || (input >= 0xF0000 && input <= 0xFFFFD) ||
//                (input >= 0x100000 && input <= 0x10FFFD);
//    }
//
//    /**
//     * 判断是否是中日韩文字
//     */
//    public static boolean isChinese(char c) {
//        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
//    }
//
//    /**
//     * 判断是否为emoji表情符<br>
//     *
//     * @param input 字符
//     * @return 是否为emoji
//     */
//    public static boolean isEmojiChar(char input) {
//        return !(input == 0x0
//                || input == 0x9
//                || input == 0xA
//                || input == 0xD
//                || input >= 0x20 && input <= 0xD7FF
//                || input >= 0xE000 && input <= 0xFFFD);
//    }
//
//    /**
//     * 是否是全角字符串
//     *
//     * @param codepoint unicode 码点
//     * @return 如果是则返回 {@code true}，否则 {@code false}
//     */
//    public static boolean isQuadEm(int codepoint) {
//        boolean isEm;
//        if (codepoint < 0x9FFF) {
//            isEm =
//                    (codepoint >= 0x1100 && codepoint <= 0x11FF)
//                            || (codepoint >= 0x2600 && codepoint <= 0x27BF)
//                            || (codepoint >= 0x2800 && codepoint <= 0x28FF)
//                            || (codepoint >= 0x2E80 && codepoint <= 0x2FDF)
//                            || (codepoint >= 0x2FF0 && codepoint <= 0x318F)
//                            || (codepoint >= 0x31A0 && codepoint <= 0x4DB5)
//                            || (codepoint >= 0x4DC0 && codepoint <= 0x9FBB);
//        } else if (codepoint < 0xFAFF) {
//            isEm =
//                    (codepoint >= 0xA000 && codepoint <= 0xA4CF)
//                            || (codepoint >= 0xAC00 && codepoint <= 0xD7AF)
//                            || (codepoint >= 0xF900 && codepoint <= 0xFA2D)
//                            || (codepoint >= 0xFA30 && codepoint <= 0xFA6A)
//                            || (codepoint >= 0xFA70 && codepoint <= 0xFAD9);
//        } else {
//            isEm =
//                    (codepoint >= 0xFE10 && codepoint <= 0xFE1F)
//                            || (codepoint >= 0xFE30 && codepoint <= 0xFE4F)
//                            || (codepoint >= 0xFF00 && codepoint <= 0xFFEF)
//                            || (codepoint >= 0x1D300 && codepoint <= 0x1D35F)
//                            || (codepoint >= 0x20000 && codepoint <= 0x2A6D6)
//                            || (codepoint >= 0x2F800 && codepoint <= 0x2FA1D);
//        }
//        return isEm;
//    }
//
//    /**
//     * 是否为Windows或者Linux（Unix）文件分隔符<br>
//     * Windows平台下分隔符为\，Linux（Unix）为/
//     *
//     * @param input 字符
//     * @return 是否为Windows或者Linux（Unix）文件分隔符
//     */
//    public static boolean isPathSeparatorChar(char input) {
//        return '/' == input || '\\' == input;
//    }
//
//    /**
//     * 比较两个字符是否相同
//     *
//     * @param c1         字符1
//     * @param c2         字符2
//     * @param ignoreCase 是否忽略大小写
//     * @return 是否相同
//     */
//    public static boolean isEqualChar(char c1, char c2, boolean ignoreCase) {
//        return ignoreCase ? Character.toLowerCase(c1) == Character.toLowerCase(c2) : c1 == c2;
//    }
//
//    /**
//     * 比较字符数组和字符串是否相等
//     *
//     * @param chars        字符数组
//     * @param csi          字符起始位置
//     * @param cei          字符结束位置
//     * @param isIgnoreCase 忽略字符串
//     * @return 返回是否相等
//     */
//    public static boolean isEqualChars(char[] chars, int csi, int cei, char[] other, boolean isIgnoreCase) {
//        return CharHelper.isEqualChars(chars, csi, cei, other, 0, other.length, isIgnoreCase);
//    }
//
//    /**
//     * 比较字符数组和字符串是否相等
//     *
//     * @param chars        字符数组
//     * @param csi          字符起始位置
//     * @param cei          字符结束位置
//     * @param other        字符串
//     * @param osi          字符串起始位置
//     * @param oei          字符串结束位置
//     * @param isIgnoreCase 忽略字符串
//     * @return 返回是否相等
//     */
//    public static boolean isEqualChars(char[] chars, int csi, int cei, char[] other, int osi, int oei, boolean isIgnoreCase) {
//        if (oei - osi == cei - csi) {
//            int len = oei - osi;
//            for (int j = 0; j < len; j++) {
//                if (!CharHelper.isEqualChar(chars[csi + j], other[osi + j], isIgnoreCase)) return false;
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public static Character toCharacter(Object value, Character defaultValue) {
//        if (value == null) return defaultValue;
//        if (value instanceof Character) return (Character) value;
//        if (value instanceof Number) return Character.toChars(((Number) value).intValue())[0];
//        if (value instanceof String) {
//            int length = ((String) value).length();
//            if (length >= 1) {
//                return ((String) value).charAt(0);
//            }
//        }
//        return defaultValue;
//    }
//    /**
//     * Trim leading or trailing trimPredicate from the given String.
//     *
//     * @param input          the char[] to check
//     * @param offset         the char[] to offset
//     * @param count          the char[] to count
//     * @param isTrimLeading  is trim leading
//     * @param isTrimTrailing is trim trailing
//     * @param trimPredicate  the trim Predicate
//     * @return the trimmed String
//     */
//    private static String trim(char[] input, int offset, int count, boolean isTrimLeading, boolean isTrimTrailing, Predicate<Character> trimPredicate) {
//        int si, ei;
//        if (offset >= input.length) {
//            si = input.length - 1;
//            ei = si - 1;
//        } else {
//            si = Math.max(offset, 0);
//            if (si + count >= input.length) ei = input.length - 1;
//            else ei = si + count - 1;
//        }
//        int c = (isTrimTrailing ? 0b10 : 0) | (isTrimLeading ? 0b01 : 0);
//        while (c != 0 && si < ei) {
//            if ((c & 0b01) == 0b01 && trimPredicate.test(input[si])) {
//                si++;
//            } else {
//                c = c & 0b10;
//            }
//            if ((c & 0b10) == 0b10 && trimPredicate.test(input[ei])) {
//                ei--;
//            } else {
//                c = c & 0b01;
//            }
//        }
//        return new String(input, si, ei - si + 1);
//
//    }
//
//    /**
//     * t --> \t
//     * n --> \n
//     * r --> \r
//     * f --> \f
//     * b --> \b
//     *
//     * @param unescape 是否解除转义
//     */
//    public static char escapeChar(char c, boolean unescape) {
//        if (unescape) {
//            switch (c) {
//                case '\t':
//                    return 't';
//                case '\n':
//                    return 'n';
//                case '\r':
//                    return 'r';
//                case '\f':
//                    return 'f';
//                case '\b':
//                    return 'b';
//                default:
//                    return c;
//            }
//        } else {
//            switch (c) {
//                case 't':
//                    return '\t';
//                case 'n':
//                    return '\n';
//                case 'r':
//                    return '\r';
//                case 'f':
//                    return '\f';
//                case 'b':
//                    return '\b';
//                default:
//                    return c;
//            }
//        }
//    }
//
//    /**
//     * 重复Char符号
//     *
//     * @param c     符号
//     * @param count 个数
//     * @return {@code String}
//     */
//    public static String repeatChar(char c, int count) {
//        if (count <= 0) return PoolString.EMPTY;
//        int limit = MathHelper.ceilSqrt(count), len = 10, min = -1;
//        for (int i = 2; i <= limit; i++) {
//            int v = (count / i) + (count % i) + i;
//            if (min < 0 || v < min) {
//                min = v;
//                len = i;
//            }
//        }
//        if (count > len) {
//            char[] chars = new char[len];
//            for (int i = 0; i < len; i++) {
//                chars[i] = c;
//            }
//            StringBuilder sb = new StringBuilder(count);
//            IntStream.range(0, count / chars.length).forEach(i -> sb.append(chars));
//            IntStream.range(0, count % chars.length).forEach(i -> sb.append(chars[0]));
//            return sb.toString();
//        } else {
//            StringBuilder sb = new StringBuilder(count);
//            IntStream.range(0, count).forEach(i -> sb.append(c));
//            return sb.toString();
//        }
//    }
//
//    /**
//     * 解析Boolean或Null
//     *
//     * @param chars    字符序列，只处理范围内的数据{@code start<=&&<=end}
//     * @param startRef 开始索引引用
//     * @param end      结束索引，如果为小于0则为字符数组结尾索引
//     * @return 返回解析后的字符串，如果不符号返回null
//     */
//    public static String resolveBooleanOrNull(char[] chars, MutableInteger startRef, int end) {
//        int start = startRef.get();
//        if (end < 0) end = chars.length - 1;
//        String v = chars[start] == 't' ? "true" : (chars[start] == 'f' ? "false" : "null");
//        int length = v.length();
//        if (start + length >= end) return null;
//        if (v.equalsIgnoreCase(new String(chars, start, length))) {
//            startRef.set(start + length - 1);
//            return v;
//        }
//        return null;
//    }
//
//    /**
//     * 解析引号内的数据，将字符通过{@link #escapeChar(char, boolean)}转换，并将unicode编码转换成文字
//     *
//     * @param chars        字符序列，只处理范围内的数据{@code start<=&&<=end}
//     * @param startRef     开始索引引用，该引用位置必须是双引号
//     * @param end          结束索引，如果为小于0则为字符数组结尾索引，该引用位置必须是双引号
//     * @param doubleQuotes 是否是双引号
//     * @return 返回解析后的字符串
//     * @throws ParseException 如果解析出现了问题
//     */
//    public static String escapeQuotes(char[] chars, MutableInteger startRef, int end, boolean doubleQuotes) throws ParseException {
//        char q = doubleQuotes ? '"' : '\'';
//        int start = startRef.get();
//        if (chars[start] != q) throw new ParseException("", start);
//        if (end < 0) end = chars.length - 1;
//        else if (chars[end] != q) throw new ParseException("", end);
//
//        StringBuilder sb = new StringBuilder(end - start + 1);
//        int current = start;
//        for (current += 1; current <= end; current++) {
//            char ch = chars[current];
//            if (ch == q) break;
//            else if (ch == '\\' && current + 1 <= end) {
//                ch = chars[++current];
//                if (ch == 'u' && current + 4 <= end) {
//                    try {
//                        sb.append(Character.toChars(Integer.decode("0x" + String.copyValueOf(chars, current + 1, 4))));
//                    } catch (Throwable ignored) {
//                        sb.append("\\").append(chars, current, 5);
//                    }
//                    current += 5;
//                } else sb.append(CharHelper.escapeChar(ch, false));
//            } else sb.append(ch);
//        }
//        startRef.set(current);
//        return sb.toString();
//    }
//
//    /**
//     * 解析数据为引号内数据，，并将unicode编码转换成文字
//     *
//     * @param chars           字符序列，全部字符都进行处理
//     * @param unescapeUnicode 是否将字符通过{@link #escapeChar(char, boolean)}转换
//     * @param unescapeChars   是否解析转义字符
//     * @param doubleQuotes    是否是双引号
//     * @return 返回解析后的字符串
//     */
//    public static String unescapeQuotes(char[] chars, boolean unescapeUnicode, String unescapeChars, boolean doubleQuotes) {
//        char q = doubleQuotes ? '"' : '\'';
//        StringBuilder sb = new StringBuilder(chars.length * 2);
//        sb.append(q);
//        char[] temp = new char[1];
//        for (char c : chars) {
//            if (unescapeChars != null && unescapeChars.length() > 0) {
//                if (unescapeChars.indexOf(c) >= 0) sb.append("\\").append(c);
//                else sb.append(c);
//            } else if (c <= 255 && "\"\\\t\f\b\r\n".indexOf(c) >= 0) {
//                sb.append("\\").append(CharHelper.escapeChar(c, true));
//            } else if (unescapeUnicode) {
//                temp[0] = c;
//                int codepoint = Character.codePointAt(temp, 0);
//                sb.append("\\u").append(Integer.toHexString(codepoint));
//            } else sb.append(c);
//        }
//        sb.append(q);
//        return sb.toString();
//    }
//
//    /**
//     * 解析数字
//     *
//     * @param chars    字符数组
//     * @param startRef 开始索引引用
//     * @param end      结束索引，如果为小于0则为字符数组结尾索引
//     * @return 返回数字字符串
//     * @throws ParseException 解析错误
//     */
//    public static String resolveNumber(char[] chars, MutableInteger startRef, int end) throws ParseException {
//        int start = startRef.get();
//        if (!("+-".indexOf(chars[start]) >= 0 || '0' <= chars[start] && chars[start] <= '9'))
//            throw new ParseException("The start char '" + chars[start] + "' not a number symbol", start);
//        if (end < 0) end = chars.length - 1;
//        else if (!('0' <= chars[end] && chars[end] <= '9'))
//            throw new ParseException("The end char '" + chars[start] + "' not a number symbol", end);
//        StringBuilder sb = new StringBuilder(end - start + 1);
//        int current = start;
//        if ("+-".indexOf(chars[current]) >= 0) {
//            current++;
//            if ('-' == chars[current]) sb.append('-');
//        }
//        boolean dot = false, coff = false;
//        for (; current <= end; current++) {
//            char ch = chars[current];
//            if ('0' <= ch && ch <= '9') sb.append(ch);
//            else if (ch == '.') {
//                if (dot) throw new ParseException("A decimal point already exists", current);
//                else dot = true;
//                sb.append(ch);
//            } else if (ch == 'e' || ch == 'E') {
//                if (coff) throw new ParseException("Already contains scientific notation 'e' or 'E'", current);
//                if (current + 1 > end) throw new ParseException("The char '" + ch + "' end", current + 1);
//                coff = true;
//                ch = chars[current + 1];
//                if ("+-".indexOf(ch) >= 0) {
//                    if (current + 2 > end) throw new ParseException("The char '" + ch + "' end", current + 2);
//                    ch = chars[current + 2];
//                    if (!('0' <= ch && ch <= '9')) {
//                        throw new ParseException("The char " + ch + " not a number symbol", current + 2);
//                    }
//                } else if (!('0' <= ch && ch <= '9')) {
//                    throw new ParseException("The char " + ch + " not a number symbol", current + 1);
//                }
//                sb.append(chars, current, 3);
//                current += 2;
//            } else break;
//        }
//        startRef.set(current - 1);
//        return sb.toString();
//    }
//
//    public static int toInt(int input, int radix) {
//        int r;
//        if ('0' <= input && input <= '9') {
//            r = input - '0';
//            if (r >= radix) throw new IllegalArgumentException("The [" + (char) input + "] out of base range" + radix);
//        } else if ('a' <= input && input <= 'z') {
//            r = input - 'a';
//            r += 10;
//            if (r >= radix) throw new IllegalArgumentException("The [" + (char) input + "] out of base range" + radix);
//        } else if ('A' <= input && input <= 'Z') {
//            r = input - 'A';
//            r += 10;
//            if (r >= radix) throw new IllegalArgumentException("The [" + (char) input + "] out of base range" + radix);
//        } else {
//            throw new IllegalArgumentException("The [" + (char) input + "] is not numbers or letters");
//        }
//        return r;
//    }
//
//    public static int toInt(char input, int radix) {
//        return CharHelper.toInt((int) input, radix);
//    }
//
//    public static int toInt(int[] input, int radix) {
//        int r = 0;
//        for (int c : input) {
//            r = r * radix + toInt(c, radix);
//        }
//        return r;
//    }
//
//    public static int toInt(char[] input, int radix) {
//        int r = 0;
//        for (char c : input) {
//            r = r * radix + toInt(c, radix);
//        }
//        return r;
//    }
//
//
//    private static final Pattern LINE = Pattern.compile("[\r\n]");
//
//    public static List<String> splitLine(CharSequence input) {
//        String[] lines = LINE.split(input);
//        List<String> lineList = new ArrayList<>(lines.length);
//        for (String line : lines) {
//            if (line == null || line.isEmpty()) lineList.add("");
//            else lineList.add(line);
//        }
//        return lineList;
//    }
//
//    public static boolean isEmpty(CharSequence input) {
//        return input == null || input.length() == 0;
//    }
//
//    public static boolean isNotEmpty(CharSequence input) {
//        return (input != null && input.length() > 0);
//    }
//
//    public static boolean isBlank(CharSequence input) {
//        if (CharSequenceHelper.isEmpty(input)) return true;
//        int strLen = input.length();
//        for (int i = 0; i < strLen; i++) {
//            if (!CharHelper.isBlankChar(input.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static boolean isNotBlank(CharSequence input) {
//        return !CharSequenceHelper.isBlank(input);
//    }
//
//    /**
//     * 将第一个字母更改为大写根据{@link Character#toUpperCase(char)}，不会更改其他字母。
//     *
//     * @param input 要取消小写的字符串，可能为null
//     * @return 大写开头的字符串
//     */
//    public static String capitalize(CharSequence input) {
//        return CharSequenceHelper.changeFirstCharacterCase(input, true);
//    }
//
//    /**
//     * 将第一个字母更改为小写根据{@link Character#toLowerCase(char)}，不会更改其他字母。
//     *
//     * @param input 要取消大写的字符串，可能为null
//     * @return 小写开头的字符串
//     */
//    public static String uncapitalize(CharSequence input) {
//        return CharSequenceHelper.changeFirstCharacterCase(input, false);
//    }
//
//    /**
//     * 转换首字符大小写
//     *
//     * @param input      字符序列
//     * @param capitalize 如果为true则首字母大写，否则小写
//     * @return {@code CharSequence}
//     */
//
//    private static String changeFirstCharacterCase(CharSequence input, boolean capitalize) {
//        if (input == null) return "";
//        int length = input.length();
//        if (length == 0) return "";
//        String str = input.toString();
//        if (length == 1) return capitalize ? str.toLowerCase() : str.toUpperCase();
//        String h = str.substring(0, 1);
//        return (!capitalize ? h.toLowerCase() : h.toUpperCase()) + str.substring(1);
//    }
//
//    /**
//     * Check whether the given CharSequence contains any whitespace characters.
//     *
//     * @param input the CharSequence to check (may be <code>null</code>)
//     * @return <code>true</code> if the CharSequence is not empty and
//     * contains at least 1 whitespace character
//     * @see Character#isWhitespace
//     */
//    public static boolean containsWhitespace(CharSequence input) {
//        if (CharSequenceHelper.isEmpty(input)) return false;
//        int strLen = input.length();
//        for (int i = 0; i < strLen; i++) {
//            if (Character.isWhitespace(input.charAt(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Trim leading whitespace from the given String.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static <T extends CharSequence> T trimLeadingWhitespace(T input, Func.F_OBiI2O<T, T> sub) {
//        return CharSequenceHelper.trimLeading(input, sub, Character::isWhitespace);
//    }
//
//    /**
//     * Trim leading whitespace from the given String.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static <T extends CharSequence> T trimLeadingWhitespace(T input, Func.F_OBiI2O<T, T> sub, int offset, int count) {
//        return CharSequenceHelper.trimLeading(input, sub, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim leading character from the given String.
//     *
//     * @param input            the CharSequence to check
//     * @param sub              the CharSequence to sub
//     * @param leadingCharacter the leading character to be trimmed
//     * @param <T>              input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimLeadingCharacter(T input, Func.F_OBiI2O<T, T> sub, String leadingCharacter) {
//        return CharSequenceHelper.trimLeading(input, sub, c -> leadingCharacter.indexOf(c) >= 0, 0, input.length());
//    }
//
//    /**
//     * Trim leading character from the given String.
//     *
//     * @param input            the CharSequence to check
//     * @param offset           the CharSequence to offset
//     * @param count            the CharSequence to count
//     * @param sub              the CharSequence to sub
//     * @param leadingCharacter the leading character to be trimmed
//     * @param <T>              input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimLeadingCharacter(T input, Func.F_OBiI2O<T, T> sub, String leadingCharacter, int offset, int count) {
//        return CharSequenceHelper.trimLeading(input, sub, c -> leadingCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim leading predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimLeading(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trimLeading(input, sub, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim leading predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimLeading(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trim(input, sub, true, false, trimPredicate, offset, count);
//    }
//
//    /**
//     * Trim trailing whitespace from the given String.
//     *
//     * @param input the CharSequence to check
//     * @param sub   the CharSequence to sub
//     * @param <T>   input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailingWhitespace(T input, Func.F_OBiI2O<T, T> sub) {
//        return CharSequenceHelper.trimTrailing(input, sub, Character::isWhitespace);
//    }
//
//    /**
//     * Trim trailing whitespace from the given String.
//     *
//     * @param input  the CharSequence to check
//     * @param offset the CharSequence to offset
//     * @param count  the CharSequence to count
//     * @param sub    the CharSequence to sub
//     * @param <T>    input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailingWhitespace(T input, Func.F_OBiI2O<T, T> sub, int offset, int count) {
//        return CharSequenceHelper.trimTrailing(input, sub, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim trailing character from the given String.
//     *
//     * @param input             the CharSequence to check
//     * @param sub               the CharSequence to sub
//     * @param trailingCharacter the trailing character to be trimmed
//     * @param <T>               input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailingCharacter(T input, Func.F_OBiI2O<T, T> sub, String trailingCharacter) {
//        return CharSequenceHelper.trimTrailing(input, sub, c -> trailingCharacter.indexOf(c) >= 0, 0, input.length());
//    }
//
//    /**
//     * Trim trailing character from the given String.
//     *
//     * @param input             the CharSequence to check
//     * @param offset            the CharSequence to offset
//     * @param count             the CharSequence to count
//     * @param sub               the CharSequence to sub
//     * @param trailingCharacter the trailing character to be trimmed
//     * @param <T>               input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailingCharacter(T input, int offset, int count, Func.F_OBiI2O<T, T> sub, String trailingCharacter) {
//        return CharSequenceHelper.trimTrailing(input, sub, c -> trailingCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailing(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trimTrailing(input, sub, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimTrailing(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trim(input, sub, false, true, trimPredicate, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing whitespace from the given String.
//     *
//     * @param input the CharSequence to check
//     * @param sub   the CharSequence to sub
//     * @param <T>   input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimWhitespace(T input, Func.F_OBiI2O<T, T> sub) {
//        return CharSequenceHelper.trim(input, sub, Character::isWhitespace, 0, input.length());
//    }
//
//    /**
//     * Trim leading or trailing whitespace from the given String.
//     *
//     * @param input  the CharSequence to check
//     * @param offset the CharSequence to offset
//     * @param count  the CharSequence to count
//     * @param sub    the CharSequence to sub
//     * @param <T>    input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimWhitespace(T input, Func.F_OBiI2O<T, T> sub, int offset, int count) {
//        return CharSequenceHelper.trim(input, sub, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing character from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param sub           the CharSequence to sub
//     * @param trimCharacter the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimCharacter(T input, Func.F_OBiI2O<T, T> sub, String trimCharacter) {
//        return CharSequenceHelper.trim(input, sub, c -> trimCharacter.indexOf(c) >= 0);
//    }
//
//    /**
//     * Trim leading or trailing character from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param sub           the CharSequence to sub
//     * @param trimCharacter the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trimCharacter(T input, int offset, int count, Func.F_OBiI2O<T, T> sub, String trimCharacter) {
//        return CharSequenceHelper.trim(input, sub, c -> trimCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trim(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trim(input, sub, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param sub           the CharSequence to sub
//     * @param trimPredicate the trim Predicate
//     * @param <T>           input type
//     * @return the trimmed String
//     */
//    public static <T extends CharSequence> T trim(T input, Func.F_OBiI2O<T, T> sub, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trim(input, sub, true, true, trimPredicate, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input          the CharSequence to check
//     * @param sub            the CharSequence to sub
//     * @param isTrimLeading  is trim leading
//     * @param isTrimTrailing is trim trailing
//     * @param trimPredicate  the trim Predicate
//     * @param <T>            input type
//     * @return the trimmed String
//     */
//    private static <T extends CharSequence> T trim(T input, Func.F_OBiI2O<T, T> sub, boolean isTrimLeading, boolean isTrimTrailing, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trim(input, sub, isTrimLeading, isTrimTrailing, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input          the CharSequence to check
//     * @param offset         the CharSequence to offset
//     * @param count          the CharSequence to count
//     * @param sub            the CharSequence to sub
//     * @param isTrimLeading  is trim leading
//     * @param isTrimTrailing is trim trailing
//     * @param trimPredicate  the trim Predicate
//     * @param <T>            input type
//     * @return the trimmed String
//     */
//    private static <T extends CharSequence> T trim(T input, Func.F_OBiI2O<T, T> sub, boolean isTrimLeading, boolean isTrimTrailing, Predicate<Character> trimPredicate, int offset, int count) {
//        if (input == null) return input;
//        final int length = input.length();
//        if (length <= 0) return input;
//        int si, ei, c = (isTrimTrailing ? 0b10 : 0) | (isTrimLeading ? 0b01 : 0);
//        if (offset >= length) {
//            si = length - 1;
//            ei = si - 1;
//        } else {
//            si = Math.max(offset, 0);
//            if (si + count >= length) ei = length - 1;
//            else ei = si + count - 1;
//        }
//        while (c != 0 && si < ei) {
//            if ((c & 0b01) == 0b01 && trimPredicate.test(input.charAt(si))) {
//                si++;
//            } else {
//                c = c & 0b10;
//            }
//            if ((c & 0b10) == 0b10 && trimPredicate.test(input.charAt(ei))) {
//                ei--;
//            } else {
//                c = c & 0b01;
//            }
//        }
//        return ((si > 0) || (ei < length - 1)) ? ((input = sub.apply(input, si, ei + 1)).length() != 0 ? input : null) : input;
//    }
//
//    /**
//     * 驼峰命名
//     *
//     * @param input      待处理字符串
//     * @param capitalize 如果为true则首字母大写，否则小写
//     * @return 驼峰式的字符串
//     */
//
//    public static String toCamelCase(CharSequence input, boolean capitalize) {
//        return CharSequenceHelper.changeFirstCharacterCase(CharSequenceHelper.formatNameString(input, "", false, true), capitalize);
//    }
//
//    /**
//     * 短横杠命名
//     *
//     * @param input   待处理字符串
//     * @param isUpper 是否大写
//     * @return 短横杠式的字符串
//     */
//
//    public static String toKebabCase(CharSequence input, boolean isUpper) {
//        return CharSequenceHelper.formatNameString(input, "-", isUpper, isUpper);
//    }
//
//    /**
//     * 下划线命名
//     *
//     * @param input   待处理字符串
//     * @param isUpper 是否大写
//     * @return 下划线式的字符串
//     */
//
//    public static String toUnderlineCase(CharSequence input, boolean isUpper) {
//        return CharSequenceHelper.formatNameString(input, "_", isUpper, isUpper);
//    }
//
//    /**
//     * 合并关键单词序列
//     *
//     * @param input       字符序列
//     * @param delimiter   分割符
//     * @param isUpper     是否大写
//     * @param isUpperHead 如果为true则首字母大写，否则小写
//     * @return 返回格式化后名称字符串
//     */
//
//    public static String formatNameString(CharSequence input, String delimiter, boolean isUpper, boolean isUpperHead) {
//        List<String> names = findNameWord(input, false, isUpper);
//        StringBuilder sb = new StringBuilder(input.length() + 10);
//        for (String name : names) {
//            name = isUpper ? name.toUpperCase() : name.toLowerCase();
//            if (isUpper != isUpperHead) {
//                name = CharSequenceHelper.changeFirstCharacterCase(name, isUpperHead);
//            }
//            sb.append(name);
//            sb.append(delimiter);
//        }
//        sb.setLength(sb.length() - delimiter.length());
//        return sb.toString();
//    }
//
//    /**
//     * 实现找单词或单词数字形式的字符串
//     *
//     * <p>注意：
//     * 1.如果非ascii会抛出异常
//     * 2.它会将非Java关键字的字符当做分割符
//     * 3.多个分割符只算一个分割符
//     * 4.Java关键字字符 {@code _}当做分割符
//     * 5.Java关键字字符 {@code $}当做前缀符
//     * 6.单词与单词之间通过单词首字符大写来分割
//     * 7.全大写专有名词与单词之间通过单词首字符大写来分割，并且全大写专有名词不会处理大小写
//     * 8.当 {@code isSplitNumber}为 {@code true}时数字跟在字符或 {@code $}后，如果数字前是分割符，则单独在一起
//     *
//     * @param input         单词序列字符串
//     * @param isSplitNumber 是否需要分割数字
//     * @param isUpper       单词是否大写
//     * @return 单词或单词数字列表
//     * @throws IllegalArgumentException 当输入字符串为 {@code null}，或者字符串里包含非Ascii吗则抛出该异常
//     */
//
//    public static List<String> findNameWord(CharSequence input, boolean isSplitNumber, boolean isUpper) {
//        final int len = input.length();
//        final List<String> results = new ArrayList<>(10);
//        final CharStack stack = new CharStack();
//        //如果值栈不为空的话，添加关键字到容器中，并清空值栈
//        IntConsumer addKeyWord = countUpperLetter -> {
//            if (!stack.isEmpty()) {
//                String name = stack.clear();
//                if (countUpperLetter <= 1) {
//                    name = isUpper ? name.toUpperCase() : name.toLowerCase();
//                }
//                results.add(name);
//            }
//        };
//        // 当不分割数字时一直为0
//        int countNumber = 0, countLetter = 0, countDollar = 0;
//        for (int i = 0; i < len; i++) {
//            char c = input.charAt(i);
//            if (AsciiTableMatcher.isNoAsciiChar(c)) {
//                throw new IllegalArgumentException("Non-Ascii codes are not allowed");
//            }
//            if (!AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.JAVA_KEYWORD_CHAR)) {
//                addKeyWord.accept(countLetter);
//                continue;
//            }
//            // 遇到了需要分割的数字
//            if (Character.isDigit(c)) {
//                // 是否要分割数字
//                if (isSplitNumber) {
//                    if (countNumber == 0) {
//                        addKeyWord.accept(countLetter);
//                    }
//                    countNumber++;
//                }
//                stack.push(c);
//            } else if (Character.isLetter(c) || c == '$') {
//                if (countNumber > 0) {
//                    addKeyWord.accept(countLetter);
//                    countNumber = 0;
//                }
//                if (c == '$') {
//                    // 只有在第一个$时分割
//                    if (countDollar == 0) {
//                        addKeyWord.accept(countLetter);
//                        countLetter = 0;
//                    }
//                    countDollar++;
//                    stack.push(c);
//                    continue;
//                }
//                int cc = Character.isLowerCase(c) || Character.isUpperCase(c) ? 1 : 0;
//                if (stack.isEmpty()) {
//                    stack.push(c);
//                    countLetter += cc;
//                } else {
//                    char top = stack.peek();
//                    // 前面是数字需要分割
//                    if (Character.isDigit(top)) {
//                        addKeyWord.accept(countLetter);
//                    }
//                    if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.UPPER_LETTER)) {
//                        if (countLetter == 0 && countDollar == 0) {
//                            addKeyWord.accept(countLetter);
//                        }
//                        countLetter += cc;
//                    } else {
//                        // 只会出现{@code '$'} {@code '大写字符'}的情况，并且是 {@code '$'} {@code '大写字符'}是多个
//                        if (countLetter > 1) {
//                            top = stack.pop();
//                            if (countDollar == 0 || stack.peek() != '$') {
//                                addKeyWord.accept(countLetter);
//                                stack.push(top);
//                            } else {
//                                stack.push(top);
//                                addKeyWord.accept(countLetter);
//                            }
//                        }
//                        countDollar = 0;
//                        countLetter = 0;
//                    }
//                    stack.push(c);
//                }
//            } else {
//                addKeyWord.accept(countLetter);
//            }
//        }
//        addKeyWord.accept(countLetter);
//        return results;
//    }
//
//
//    /**
//     * 检查并获取unicode的单个Char，如果单个char无法表达则抛出异常
//     *
//     * @param input unicode字符
//     * @return 返回字符
//     */
//    public static char[] checkGetSingletonChars(String input) {
//        int[] codePoints = input.codePoints().toArray();
//        char[] result = new char[codePoints.length];
//        for (int i = 0; i < codePoints.length; i++) {
//            result[i] = checkGetSingletonChar(codePoints[i]);
//        }
//        return result;
//    }
//
//    /**
//     * 如果对象是字符串是否为空白，空白的定义如下： <br>
//     * 1、为null <br>
//     * 2、为不可见字符（如空格）<br>
//     * 3、""<br>
//     *
//     * @param input 对象
//     * @return 如果为字符串是否为空串
//     */
//    public static boolean isBlank(Object input) {
//        if (null == input) return true;
//        if (input instanceof CharSequence) return CharSequenceHelper.isBlank((CharSequence) input);
//        return false;
//    }
//
//    /**
//     * 字符串是否为非空白 空白的定义如下： <br>
//     * 1、不为null <br>
//     * 2、不为不可见字符（如空格）<br>
//     * 3、不为""<br>
//     *
//     * @param input 被检测的字符串
//     * @return 是否为非空
//     */
//    public static boolean isNotBlank(Object input) {
//        return !StringHelper.isBlank(input);
//    }
//
//    /**
//     * 判断String是否是整数<br>
//     * 支持10、16进制
//     *
//     * @param input String
//     * @return 是否为整数
//     */
//    public static boolean isIntegerNumber(String input) {
//        boolean[] numberString = StringHelper.isNumberTypes(input);
//        return numberString[0] && !numberString[1];
//    }
//
//    /**
//     * 判断String是否是整数<br>
//     * 支持10、16进制
//     *
//     * @param input String
//     * @return 是否为整数
//     */
//    public static boolean isIntegerNumberHuman(String input) {
//        boolean[] numberString = StringHelper.isNumberTypes(input);
//        return numberString[0] && !numberString[1] && !numberString[2];
//    }
//
//    /**
//     * 判断字符串是否是浮点数
//     *
//     * @param input String
//     * @return 是否为{@link Double}类型
//     */
//    public static boolean isDecimalNumber(String input) {
//        boolean[] numberString = StringHelper.isNumberTypes(input);
//        return numberString[0] && numberString[1];
//    }
//
//    /**
//     * 判断字符串是否是浮点数
//     *
//     * @param input String
//     * @return 是否为{@link Double}类型
//     */
//    public static boolean isDecimalNumberHuman(String input) {
//        boolean[] numberString = StringHelper.isNumberTypes(input);
//        return numberString[0] && !numberString[1] && !numberString[2];
//    }
//
//    /**
//     * 是否为数字
//     *
//     * @param input 字符串值
//     * @return 是否为数字
//     */
//    public static boolean isNumber(String input) {
//        return StringHelper.isNumberTypes(input)[0];
//    }
//
//    /**
//     * 是否为人类易懂数字，排除16进制
//     *
//     * @param input 字符串值
//     * @return 是否为数字
//     */
//    public static boolean isNumberHuman(String input) {
//        boolean[] numberString = StringHelper.isNumberTypes(input);
//        return numberString[0] && !numberString[2];
//    }
//
//    /**
//     * 是否为数字
//     *
//     * @param input 字符串值
//     * @return 是否为数字
//     */
//    private static boolean[] isNumberTypes(String input) {
//        // 0 isNumber 1 isDecimal 2 isHex
//        boolean[] error = {false, false, false};
//        char[] chars = input.toCharArray();
//        int sz = chars.length;
//        boolean hasExp = false;
//        boolean hasDecPoint = false;
//        boolean allowSigns = false;
//        boolean foundDigit = false;
//        // deal with any possible sign up front
//        int start = (chars[0] == '-') ? 1 : 0;
//        if (sz > start + 1) {
//            if (chars[start] == '0' && chars[start + 1] == 'x') {
//                int i = start + 2;
//                if (i == sz) {
//                    // input == "0x"
//                    return error;
//                }
//                // checking hex (it can't be anything else)
//                for (; i < chars.length; i++) {
//                    if ((chars[i] < '0' || chars[i] > '9')
//                            && (chars[i] < 'a' || chars[i] > 'f')
//                            && (chars[i] < 'A' || chars[i] > 'F')) {
//                        return error;
//                    }
//                }
//                return new boolean[]{true, false, true};
//            }
//        }
//        sz--; // don't want to loop to the last char, check it afterwords
//        // for type qualifiers
//        int i = start;
//        // loop to the next to last char or to the last char if we need another digit to
//        // make a valid number (e.g. chars[0..5] = "1234E")
//        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
//            if (chars[i] >= '0' && chars[i] <= '9') {
//                foundDigit = true;
//                allowSigns = false;
//
//            } else if (chars[i] == '.') {
//                if (hasDecPoint || hasExp) {
//                    // two decimal points or dec in exponent
//                    return error;
//                }
//                hasDecPoint = true;
//            } else if (chars[i] == 'e' || chars[i] == 'E') {
//                // we've already taken care of hex.
//                if (hasExp) {
//                    // two E's
//                    return error;
//                }
//                if (!foundDigit) {
//                    return error;
//                }
//                hasExp = true;
//                allowSigns = true;
//            } else if (chars[i] == '+' || chars[i] == '-') {
//                if (!allowSigns) {
//                    return error;
//                }
//                allowSigns = false;
//                foundDigit = false; // we need a digit after the E
//            } else {
//                return error;
//            }
//            i++;
//        }
//        if (i < chars.length) {
//            if (chars[i] >= '0' && chars[i] <= '9') {
//                // no type qualifier, OK
//                return new boolean[]{true, hasDecPoint, false};
//            }
//            if (chars[i] == 'e' || chars[i] == 'E') {
//                // can't have an E at the last byte
//                return error;
//            }
//            if (chars[i] == '.') {
//                if (hasDecPoint || hasExp) {
//                    // two decimal points or dec in exponent
//                    return error;
//                }
//                // single trailing decimal point after non-exponent is ok
//                return new boolean[]{foundDigit, true, false};
//            }
//            if (!allowSigns
//                    && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
//                return new boolean[]{foundDigit, hasDecPoint, false};
//            }
//            if (chars[i] == 'l' || chars[i] == 'L') {
//                // not allowing L with an exponent
//                return new boolean[]{foundDigit && !hasExp, hasDecPoint, false};
//            }
//            // last character is illegal
//            return error;
//        }
//        // allowSigns is true iff the val ends in 'E'
//        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
//        return new boolean[]{!allowSigns && foundDigit, hasDecPoint, false};
//    }
//
//    public static boolean startsWithIgnoreCase(String input, String prefix) {
//        return AggregationHelper.isStartsWith(input.toCharArray(), prefix.toCharArray(), ArrayOperator.L_CHAR, Func.characterForEqualsIgnoreCase());
//    }
//
//    public static boolean startsWith(String input, String prefix) {
//        return AggregationHelper.isStartsWith(input.toCharArray(), prefix.toCharArray(), ArrayOperator.L_CHAR);
//    }
//
//    public static boolean endsWithIgnoreCase(String input, String prefix) {
//        return AggregationHelper.isEndsWith(input.toCharArray(), prefix.toCharArray(), ArrayOperator.L_CHAR, Func.characterForEqualsIgnoreCase());
//    }
//
//    public static boolean endsWith(String input, String prefix) {
//        return AggregationHelper.isEndsWith(input.toCharArray(), prefix.toCharArray(), ArrayOperator.L_CHAR);
//    }
//
//    /**
//     * 将字符串构建器内容添加至容器里并清除字符串串构建器里的内容
//     *
//     * @param container 字符串容器
//     * @param input     字符串串构建器
//     */
//    public static void addStringAndClear(Collection<String> container, StringBuilder input) {
//        String string = input.toString();
//        if (string.length() != 0) {
//            container.add(string);
//        }
//        input.setLength(0);
//    }
//
//    /**
//     * 将字符串构建器内容添加至容器里并清除字符串串构建器里的内容
//     *
//     * @param container 字符串容器
//     * @param input     字符串串构建器
//     */
//    public static void addStringAndClear(Collection<String> container, StringBuffer input) {
//        String string = input.toString();
//        if (string.length() != 0) {
//            container.add(string);
//        }
//        input.setLength(0);
//    }
//
//    /**
//     * 清理字符串构建器
//     *
//     * @param input {@code StringBuilder}
//     * @return 返回清理之前的字符串
//     */
//    public static String clearStringBuilder(StringBuilder input) {
//        String result = input.toString();
//        input.setLength(0);
//        return result;
//    }
//
//    /**
//     * 清理字符串构建器
//     *
//     * @param input {@code StringBuffer}
//     * @return 返回清理之前的字符串
//     */
//    public static String clearStringBuffer(StringBuffer input) {
//        String result = input.toString();
//        input.setLength(0);
//        return result;
//    }
//
//    /**
//     * 获取字符
//     *
//     * @param sb             {@code StringBuffer}
//     * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
//     * @return 字符串
//     */
//
//    public static String deleteStringBuffer(StringBuffer sb, int trimTailLength) {
//        int i = sb.length() - trimTailLength;
//        if (i < 0) return PoolString.EMPTY;
//        return sb.delete(i, Integer.MAX_VALUE).toString();
//    }
//
//    /**
//     * 获取字符
//     *
//     * @param sb             {@code StringBuffer}
//     * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
//     * @return 字符串
//     */
//
//    public static String deleteStringBuilder(StringBuilder sb, int trimTailLength) {
//        int i = sb.length() - trimTailLength;
//        if (i < 0) return PoolString.EMPTY;
//        return sb.delete(i, Integer.MAX_VALUE).toString();
//    }
//
//    /**
//     * Delete all occurrences of the given substring.
//     *
//     * @param input   the original String
//     * @param pattern the pattern to delete all occurrences of
//     * @return the resulting String
//     */
//    public static String deleteString(String input, String pattern) {
//        return StringHelper.replace(input, pattern, "");
//    }
//
//    /**
//     * Delete any character in a given String.
//     *
//     * @param input the original String
//     * @param chars a set of characters to delete.
//     *              E.g. "az\n" will delete 'a's, 'z's and new lines.
//     * @return the resulting String
//     */
//    public static String deleteChars(String input, String chars) {
//        if (StringHelper.isEmpty(input)) return input;
//        if (StringHelper.isNotEmpty(chars)) return input;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < input.length(); i++) {
//            char c = input.charAt(i);
//            if (chars.indexOf(c) >= 0) continue;
//            sb.append(c);
//        }
//        return sb.toString();
//    }
//
//    public static boolean containsAny(String input, char... elements) {
//        return ArrayHelper.containsAny(input.toCharArray(), ArrayOperator.L_CHAR, true, AggregationHelper.toSet(elements));
//    }
//
//    /**
//     * Returns the number of occurrences the substring {@code sub} appears in string {@code input}.
//     *
//     * @param input string to search in. Return 0 if this is null.
//     * @param sub   string to search for. Return 0 if this is null.
//     * @return the number of occurrences the substring {@code sub} appears in string {@code input}.
//     */
//    public static int countOccurrencesOf(String input, String sub) {
//        if (input == null || sub == null || input.isEmpty() || sub.isEmpty()) return 0;
//        int count = 0, pos = 0, idx;
//        while ((idx = input.indexOf(sub, pos)) != -1) {
//            ++count;
//            pos = idx + sub.length();
//        }
//        return count;
//    }
//
//    /**
//     * String --> 'String'
//     * Object --> 'Object'
//     * null --> ''
//     */
//
//    public static String quote(String input) {
//        return (input != null ? "'" + input + "'" : "''");
//    }
//
//    /**
//     * String --> 'String'
//     * Object --> Object
//     * null -->
//     */
//
//    public static Object quoteIfString(Object input) {
//        return (input instanceof String ? StringHelper.quote((String) input) : "");
//    }
//
//    /**
//     * Replace all occurrences of a substring within a string with
//     * another string.
//     *
//     * @param input      String to examine
//     * @param oldPattern String to replace
//     * @param newPattern String to insert
//     * @return a String with the replacements
//     */
//    public static String replace(String input, String oldPattern, String newPattern) {
//        if (StringHelper.isEmpty(input)) return input;
//        if (StringHelper.isEmpty(oldPattern)) return input;
//        if (StringHelper.isEmpty(newPattern)) return input;
//        StringBuilder sb = new StringBuilder();
//        int pos = 0; // our position in the old string
//        int index = input.indexOf(oldPattern);
//        // the index of an occurrence we've found, or -1
//        int patLen = oldPattern.length();
//        while (index >= 0) {
//            sb.append(input, pos, index);
//            sb.append(newPattern);
//            pos = index + patLen;
//            index = input.indexOf(oldPattern, pos);
//        }
//        sb.append(input.substring(pos));
//        // remember to append any characters to the right of a match
//        return sb.toString();
//    }
//
//    /**
//     * 分割字符串成键值对形式
//     *
//     * @param input       输入字符串
//     * @param splitSymbol 分割符号
//     * @param greedy      是否贪婪匹配
//     * @return {@code Pair<String, String>}
//     */
//
//    public static Pair<String, String> splitToPair(String input, String splitSymbol, boolean greedy) {
//        if (input == null) return new Pair<>("", null);
//        if (splitSymbol == null) return new Pair<>(input, null);
//        int i = greedy ? input.lastIndexOf(splitSymbol) : input.indexOf(splitSymbol);
//        if (i <= 0 || i >= input.length()) return new Pair<>(input, null);
//        return new Pair<>(input.substring(0, i), input.substring(i + splitSymbol.length()));
//    }
//
//    /**
//     * 改进JDK subString<br>
//     * index从0开始计算，最后一个字符为-1<br>
//     * 如果from和to位置一样，返回 "" <br>
//     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
//     *
//     * @param input            String
//     * @param fromIndexInclude 开始的index（包括）
//     * @param toIndexExclude   结束的index（不包括）
//     * @return 字串
//     */
//
//    public static String subString(String input, int fromIndexInclude, int toIndexExclude) {
//        if (input == null || input.isEmpty()) return "";
//        int[] idx = ArrayHelper.idx(input.length(), fromIndexInclude, toIndexExclude, true);
//        if (idx[0] == idx[1]) return "";
//        return input.substring(idx[0], idx[1]);
//    }
//
//    /**
//     * 简单切分字符串，不进行其它操作
//     *
//     * @param input     输入字符串
//     * @param splitChar 切分字符
//     * @param trimChar  清除字符
//     * @return 返回切分后的字符串数组
//     */
//
//    public static List<String> splitString(String input, String splitChar, String trimChar) {
//        return StringHelper.splitString(input, splitChar, trimChar, true);
//    }
//
//    /**
//     * 简单切分字符串，不进行其它操作
//     *
//     * @param input       输入字符串
//     * @param splitChar   切分字符
//     * @param trimChar    清除字符
//     * @param ignoreEmpty 是否忽略为空
//     * @return 返回切分后的字符串数组
//     */
//
//    public static List<String> splitString(String input, String splitChar, String trimChar, boolean ignoreEmpty) {
//        return StringHelper.splitString(input, splitChar, trimChar, s -> s, ignoreEmpty);
//    }
//
//    /**
//     * 简单切分字符串，不进行其它操作
//     *
//     * @param input     输入字符串
//     * @param splitChar 切分字符
//     * @param trimChar  清除字符
//     * @param converter 字符串转换器
//     * @return 返回切分后的字符串数组
//     */
//
//    public static <T> List<T> splitString(String input, String splitChar, String trimChar, Function<String, T> converter) {
//        return StringHelper.splitString(input, splitChar, trimChar, converter, true);
//    }
//
//    /**
//     * 简单切分字符串，不进行其它操作
//     *
//     * @param input       输入字符串
//     * @param splitChar   切分字符
//     * @param trimChar    清除字符
//     * @param converter   字符串转换器
//     * @param ignoreEmpty 是否忽略为空
//     * @return 返回切分后的字符串数组
//     */
//
//    public static <T> List<T> splitString(String input, String splitChar, String trimChar, Function<String, T> converter, boolean ignoreEmpty) {
//        if (input == null || input.isEmpty()) return Collections.emptyList();
//        int length = input.length();
//        List<T> list = new ArrayList<>();
//        Consumer<String> consumer = (string) -> {
//            if (trimChar != null) {
//                string = StringHelper.trimCharacter(string, trimChar);
//            }
//            T result = converter.apply(string);
//            if (result != null) {
//                list.add(result);
//            }
//        };
//        if (splitChar == null || splitChar.isEmpty()) {
//            consumer.accept(input);
//            return list;
//        }
//        final int scLength = splitChar.length();
//        int si = 0, i;
//        while ((i = input.indexOf(splitChar, si)) >= 0) {
//            String substring = input.substring(si, i);
//            if (!ignoreEmpty || substring.length() > 0) {
//                consumer.accept(substring);
//            }
//            si = i + scLength;
//        }
//        String substring = input.substring(si);
//        if (!ignoreEmpty || substring.length() > 0) {
//            consumer.accept(input.substring(si));
//        }
//        return list;
//    }
//
//    /**
//     * 清除前导 {@code --}，{@code -}字符串
//     *
//     * @param string 输入字符串
//     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串
//     */
//    public static String trimLeadingHyphens(String string) {
//        return StringHelper.trimLeadingPrefix(string, "--", "-");
//    }
//
//    /**
//     * 清除前缀字符串
//     *
//     * @param input          输入字符串
//     * @param selectPrefixes 选择要删除的前缀
//     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有前缀则原样返回
//     */
//
//    public static String trimLeadingPrefix(String input, String... selectPrefixes) {
//        if (input == null) return "";
//        for (String prefix : selectPrefixes) {
//            if (input.startsWith(prefix)) {
//                return input.substring(prefix.length());
//            }
//        }
//        return input;
//    }
//
//    /**
//     * 清除后缀字符串
//     *
//     * @param input          输入字符串
//     * @param selectSuffixes 选择要删除的后缀
//     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有后缀则原样返回
//     */
//
//    public static String trimLeadingSuffix(String input, String... selectSuffixes) {
//        if (input == null) return "";
//        for (String suffix : selectSuffixes) {
//            if (input.endsWith(suffix)) {
//                return input.substring(0, input.length() - suffix.length());
//            }
//        }
//        return input;
//    }
//
//    /**
//     * Trim <i>all</i> trimPredicate from the given String:
//     * leading, trailing, and intermediate characters.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static String trimAllWhitespace(CharSequence input) {
//        return trimAll(input, Character::isWhitespace);
//    }
//
//    /**
//     * Trim <i>all</i> whitespace from the given String:
//     * leading, trailing, and intermediate characters.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static String trimAll(CharSequence input, Predicate<Character> trimPredicate) {
//        if (input == null || input.length() <= 0) {
//            return null;
//        }
//        StringBuilder sb = new StringBuilder(input);
//        int index = 0;
//        while (sb.length() > index) {
//            if (trimPredicate.test(sb.charAt(index))) {
//                sb.deleteCharAt(index);
//            } else {
//                index++;
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * Trim leading whitespace from the given String.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static String trimLeadingWhitespace(String input) {
//        return CharSequenceHelper.trimLeading(input, String::substring, Character::isWhitespace);
//    }
//
//    /**
//     * Trim leading whitespace from the given String.
//     *
//     * @param input the String to check
//     * @return the trimmed String
//     * @see Character#isWhitespace
//     */
//    public static String trimLeadingWhitespace(String input, int offset, int count) {
//        return CharSequenceHelper.trimLeading(input, String::substring, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim leading character from the given String.
//     *
//     * @param input            the CharSequence to check
//     * @param leadingCharacter the leading character to be trimmed
//     * @return the trimmed String
//     */
//    public static String trimLeadingCharacter(String input, String leadingCharacter) {
//        return CharSequenceHelper.trimLeading(input, String::substring, c -> leadingCharacter.indexOf(c) >= 0, 0, input.length());
//    }
//
//    /**
//     * Trim leading character from the given String.
//     *
//     * @param input            the CharSequence to check
//     * @param offset           the CharSequence to offset
//     * @param count            the CharSequence to count
//     * @param leadingCharacter the leading character to be trimmed
//     * @return the trimmed String
//     */
//    public static String trimLeadingCharacter(String input, String leadingCharacter, int offset, int count) {
//        return CharSequenceHelper.trimLeading(input, String::substring, c -> leadingCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim leading predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimLeading(String input, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trimLeading(input, String::substring, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim leading predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimLeading(String input, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trimLeading(input, String::substring, trimPredicate, offset, count);
//    }
//
//    /**
//     * Trim trailing whitespace from the given String.
//     *
//     * @param input the CharSequence to check
//     * @return the trimmed String
//     */
//    public static String trimTrailingWhitespace(String input) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, Character::isWhitespace);
//    }
//
//    /**
//     * Trim trailing whitespace from the given String.
//     *
//     * @param input  the CharSequence to check
//     * @param offset the CharSequence to offset
//     * @param count  the CharSequence to count
//     * @return the trimmed String
//     */
//    public static String trimTrailingWhitespace(String input, int offset, int count) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim trailing character from the given String.
//     *
//     * @param input             the CharSequence to check
//     * @param trailingCharacter the trailing character to be trimmed
//     * @return the trimmed String
//     */
//    public static String trimTrailingCharacter(String input, String trailingCharacter) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, c -> trailingCharacter.indexOf(c) >= 0, 0, input.length());
//    }
//
//    /**
//     * Trim trailing character from the given String.
//     *
//     * @param input             the CharSequence to check
//     * @param offset            the CharSequence to offset
//     * @param count             the CharSequence to count
//     * @param trailingCharacter the trailing character to be trimmed
//     * @return the trimmed String
//     */
//    public static String trimTrailingCharacter(String input, int offset, int count, String trailingCharacter) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, c -> trailingCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimTrailing(String input, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimTrailing(String input, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trimTrailing(input, String::substring, trimPredicate, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing whitespace from the given String.
//     *
//     * @param input the CharSequence to check
//     * @return the trimmed String
//     */
//    public static String trimWhitespace(String input) {
//        return CharSequenceHelper.trim(input, String::substring, Character::isWhitespace, 0, input.length());
//    }
//
//    /**
//     * Trim leading or trailing whitespace from the given String.
//     *
//     * @param input  the CharSequence to check
//     * @param offset the CharSequence to offset
//     * @param count  the CharSequence to count
//     * @return the trimmed String
//     */
//    public static String trimWhitespace(String input, int offset, int count) {
//        return CharSequenceHelper.trim(input, String::substring, Character::isWhitespace, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing character from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param trimCharacter the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimCharacter(String input, String trimCharacter) {
//        return CharSequenceHelper.trim(input, String::substring, c -> trimCharacter.indexOf(c) >= 0);
//    }
//
//    /**
//     * Trim leading or trailing character from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param trimCharacter the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trimCharacter(String input, int offset, int count, String trimCharacter) {
//        return CharSequenceHelper.trim(input, String::substring, c -> trimCharacter.indexOf(c) >= 0, offset, count);
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trim(String input, Predicate<Character> trimPredicate) {
//        return CharSequenceHelper.trim(input, String::substring, trimPredicate, 0, input.length());
//    }
//
//    /**
//     * Trim leading or trailing predicate from the given String.
//     *
//     * @param input         the CharSequence to check
//     * @param offset        the CharSequence to offset
//     * @param count         the CharSequence to count
//     * @param trimPredicate the trim Predicate
//     * @return the trimmed String
//     */
//    public static String trim(String input, Predicate<Character> trimPredicate, int offset, int count) {
//        return CharSequenceHelper.trim(input, String::substring, trimPredicate, offset, count);
//    }
//
//    /**
//     * Unqualify a string qualified by a '.' dot character. For example,
//     * "this.name.is.qualified", returns "qualified".
//     *
//     * @param qualifiedName the qualified name
//     * @return an unqualified string by stripping all previous text before (and including) the last period character.
//     */
//    public static String unqualify(String qualifiedName) {
//        return StringHelper.unqualify(qualifiedName, '.');
//    }
//
//    /**
//     * Unqualify a string qualified by a separator character. For example,
//     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
//     *
//     * @param qualifiedName the qualified name
//     * @param separator     the separator
//     * @return an unqualified string by stripping all previous text before and including the last {@code separator} character.
//     */
//    public static String unqualify(String qualifiedName, char separator) {
//        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
//    }
//
//    /**
//     * 将给定的{@code String}表示形式解析为{@link Locale}，格式如下：
//     *
//     * <ul>
//     *   <li>en
//     *   <li>en_US
//     *   <li>en US
//     *   <li>en-US
//     * </ul>
//     *
//     * @param localeName 语言环境名称
//     * @return 相应的{@code Locale}实例，如果没有，则为{@code null}
//     * @throws IllegalArgumentException 如果在无效的语言环境规范的情况下
//     */
//
//    public static Locale parseLocale(String localeName) {
//        StringTokenizer st = new StringTokenizer(localeName, "_ ");
//        List<String> tokens = AggregationHelper.toStringList(Iterators.ofStringTokenizer(st));
//        int size = tokens.size();
//        if (size == 1) {
//            Locale resolved = Locale.forLanguageTag(tokens.get(0));
//            if (resolved != null) {
//                String language = resolved.getLanguage();
//                if (language != null && !language.isEmpty()) return resolved;
//            }
//        }
//        Function<String, String> validate = (localePart) -> {
//            for (int i = 0; i < localePart.length(); i++) {
//                char ch = localePart.charAt(i);
//                if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
//                    throw new IllegalArgumentException(
//                            "Locale part \"" + localePart + "\" contains invalid characters");
//                }
//            }
//            return localePart;
//        };
//        String language = validate.apply(size > 0 ? tokens.get(0) : "");
//        String country = validate.apply(size > 1 ? tokens.get(1) : "");
//        String variant = "";
//        if (size > 2) {
//            // There is definitely a variant, and it is everything after the country
//            // code sans the separator between the country code and the variant.
//            int endIndexOfCountryCode = localeName.indexOf(country) + country.length();
//            // Strip off any leading '_' and whitespace, what's left is the variant.
//            variant = localeName.substring(endIndexOfCountryCode).trim();
//            int index = 0;
//            for (int i = 0; i < variant.length(); i++) {
//                if (variant.charAt(i) == '_') index++;
//                else break;
//            }
//            variant = variant.substring(index);
//        }
//        if (variant.isEmpty() && country.startsWith("#")) {
//            variant = country;
//            country = "";
//        }
//        return (language.length() > 0 ? new Locale(language, country, variant) : null);
//    }
//
//    /**
//     * Determine the RFC 3066 compliant language tag,
//     * as used for the HTTP "Accept-Language" header.
//     *
//     * @param locale the Locale to transform to a language tag
//     * @return the RFC 3066 compliant language tag as String
//     */
//    public static String formatLocale(Locale locale) {
//        return locale.getLanguage() + (CharSequenceHelper.isNotBlank(locale.getCountry()) ? "-" + locale.getCountry() : "");
//    }
//
//    /**
//     * 解析路径分段
//     *
//     * @param input 文件路径
//     * @return 返回路径分段，{@code String[]}
//     */
//    public static String[] parsePath(String input) {
//        int[] fi = StringHelper.parsePathIndex(input);
//        return new String[]{input.substring(0, fi[0]), input.substring(fi[0], fi[1]), input.substring(fi[1])};
//    }
//
//    /**
//     * 解析文件名索引
//     *
//     * @param input 文件路径
//     * @return 返回前后索引位置，{@code int{start,end}}
//     */
//    public static int[] parsePathIndex(String input) {
//        final int length = input.length();
//        if (length == 0) return new int[]{0, 0};
//        int d = -1, p = -1;
//        for (int i = length - 1; i >= 0; i--) {
//            char c = input.charAt(i);
//            if (d == -1 && c == '.') d = i;
//            else if (p == -1 && (c == '/' || c == '\\')) p = i;
//        }
//        return new int[]{p < 0 ? 0 : p + 1, d < 0 || d < p ? length : d};
//    }
//
//    /**
//     * 格式化路径
//     *
//     * @param input       路径
//     * @param isDirectory 是否为目录
//     * @return 返回合法化的路径
//     */
//    public static String formatPath(String input, boolean isDirectory) {
//        if (input == null || input.isEmpty()) return isDirectory ? "/" : "";
//        StringBuilder sb = new StringBuilder(input.length());
//        String[] segments = input.split("[/\\\\]");
//        if (segments[0] != null && !segments[0].isEmpty()) {
//            sb.append(segments[0]);
//            if (segments[0].endsWith(":")) sb.append("://");
//            else sb.append("/");
//        }
//        for (int i = 1; i < segments.length; i++) {
//            if (segments[i] != null && !segments[i].isEmpty()) {
//                sb.append(segments[i]).append("/");
//            }
//        }
//        if (!isDirectory) sb.setLength(sb.length() - 1);
//        return sb.toString();
//    }
//
//    /**
//     * 找出指定字符的位置，并且不包含限定字符
//     *
//     * @param input      字符串
//     * @param c          指定字符
//     * @param reserve    是否反向
//     * @param limitChars 限制分割字符
//     * @return 返回
//     */
//    public static int indexOf(String input, char c, boolean reserve, char... limitChars) {
//        if (input == null) return -1;
//        int l = reserve ? input.lastIndexOf(c) : input.indexOf(c);
//        if (l < 0) return -1;
//        if (limitChars.length == 0) return l;
//        IntStream.Builder builder = IntStream.builder();
//        for (char beforeSplitChar : limitChars) {
//            int i = reserve ? input.lastIndexOf(c) : input.indexOf(c);
//            if (i < 0) continue;
//            builder.add(i);
//        }
//        IntStream intStream = builder.build();
//        OptionalInt i = reserve ? intStream.max() : intStream.min();
//        if (i.isPresent()) {
//            return reserve ? (l > i.getAsInt() ? l : -1) : (l < i.getAsInt() ? l : -1);
//        }
//        return l;
//    }
//
//    /**
//     * Imported from
//     * https://github.com/apache/commons-lang/blob/690dc3c9c4cf8a1875d882ae09741c2e6342ad6b/src/main/java/org/apache/commons/lang3/StringUtils.java
//     * <p>
//     * Performs the logic for the {@code split} and
//     * {@code splitPreserveAllTokens} methods that return a maximum array
//     * length.
//     *
//     * @param str               the String to parse, may be {@code null}
//     * @param separatorChars    the separate character
//     * @param max               the maximum number of elements to include in the
//     *                          array. A zero or negative value implies no limit.
//     * @param preserveAllTokens if {@code true}, adjacent separators are
//     *                          treated as empty token separators; if {@code false}, adjacent
//     *                          separators are treated as one separator.
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    private static String[] splitWorker(String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
//        // Performance tuned for 2.0 (JDK1.4)
//        // Direct code is quicker than StringTokenizer.
//        // Also, StringTokenizer uses isSpace() not isWhitespace()
//
//        //XXX: This never happens in Domain.parseDomain
//        //if (str == null) {
//        //    return null;
//        //}
//        final int len = str.length();
//        //if (len == 0) {
//        //    return EMPTY_STRING_ARRAY;
//        //}
//
//        final List<String> list = new ArrayList<>();
//        int sizePlus1 = 1;
//        int i = 0, start = 0;
//        boolean match = false;
//        boolean lastMatch = false;
//        if (separatorChars == null) {
//            // Null separator means use whitespace
//            while (i < len) {
//                if (Character.isWhitespace(str.charAt(i))) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        } else if (separatorChars.length() == 1) {
//            // Optimise 1 character case
//            final char sep = separatorChars.charAt(0);
//            while (i < len) {
//                if (str.charAt(i) == sep) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        } else {
//            // standard case
//            while (i < len) {
//                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        }
//        if (match || preserveAllTokens && lastMatch) {
//            list.add(str.substring(start, i));
//        }
//        return list.toArray(new String[0]);
//    }
//
//
//    /**
//     * 空字符串
//     */
//    private static final String NULLSTR = "";
//
//    /**
//     * 下划线
//     */
//    private static final char SEPARATOR = '_';
//
//    /**
//     * 去空格
//     */
//    public static String trim(String str) {
//        return (str == null ? "" : str.trim());
//    }
//
//    /**
//     * 截取字符串
//     *
//     * @param str   字符串
//     * @param start 开始
//     * @return 结果
//     */
//    public static String substring(final String str, int start) {
//        if (str == null) {
//            return NULLSTR;
//        }
//
//        if (start < 0) {
//            start = str.length() + start;
//        }
//
//        if (start < 0) {
//            start = 0;
//        }
//        if (start > str.length()) {
//            return NULLSTR;
//        }
//
//        return str.substring(start);
//    }
//
//    /**
//     * 截取字符串
//     *
//     * @param str   字符串
//     * @param start 开始
//     * @param end   结束
//     * @return 结果
//     */
//    public static String substring(final String str, int start, int end) {
//        if (str == null) {
//            return NULLSTR;
//        }
//
//        if (end < 0) {
//            end = str.length() + end;
//        }
//        if (start < 0) {
//            start = str.length() + start;
//        }
//
//        if (end > str.length()) {
//            end = str.length();
//        }
//
//        if (start > end) {
//            return NULLSTR;
//        }
//
//        if (start < 0) {
//            start = 0;
//        }
//        if (end < 0) {
//            end = 0;
//        }
//
//        return str.substring(start, end);
//    }
//
//    /**
//     * 下划线转驼峰命名
//     */
//    public static String toUnderScoreCase(String str) {
//        if (str == null) {
//            return null;
//        }
//        StringBuilder sb = new StringBuilder();
//        // 前置字符是否大写
//        boolean preCharIsUpperCase = true;
//        // 当前字符是否大写
//        boolean curreCharIsUpperCase = true;
//        // 下一字符是否大写
//        boolean nexteCharIsUpperCase = true;
//        for (int i = 0; i < str.length(); i++) {
//            char c = str.charAt(i);
//            if (i > 0) {
//                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
//            } else {
//                preCharIsUpperCase = false;
//            }
//
//            curreCharIsUpperCase = Character.isUpperCase(c);
//
//            if (i < (str.length() - 1)) {
//                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
//            }
//
//            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
//                sb.append(SEPARATOR);
//            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
//                sb.append(SEPARATOR);
//            }
//            sb.append(Character.toLowerCase(c));
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
//     *
//     * @param name 转换前的下划线大写方式命名的字符串
//     * @return 转换后的驼峰式命名的字符串
//     */
//    public static String convertToCamelCase(String name) {
//        StringBuilder result = new StringBuilder();
//        // 快速检查
//        if (name == null || name.isEmpty()) {
//            // 没必要转换
//            return "";
//        } else if (!name.contains("_")) {
//            // 不含下划线，仅将首字母大写
//            return name.substring(0, 1).toUpperCase() + name.substring(1);
//        }
//        // 用下划线将原始字符串分割
//        String[] camels = name.split("_");
//        for (String camel : camels) {
//            // 跳过原始字符串中开头、结尾的下换线或双重下划线
//            if (camel.isEmpty()) {
//                continue;
//            }
//            // 首字母大写
//            result.append(camel.substring(0, 1).toUpperCase());
//            result.append(camel.substring(1).toLowerCase());
//        }
//        return result.toString();
//    }
//
//
//    /**
//     * 字符串解析工具类，提供高效可靠的字符串转数字解析功能
//     *
//     * @author zhitron
//     */
//    public class StringParsingUtils {
//        /**
//         * 空字符数组常量
//         * 用于表示空的字符数组，提高代码可读性和避免重复创建空数组实例
//         */
//        private static final char[] EMPTY_CHAR_ARRAY = new char[0];
//        /**
//         * 空字符串常量
//         * 用于表示空字符串，提高代码可读性和避免重复创建空字符串实例
//         */
//        private static final String EMPTY_STRING = "";
//        /**
//         * Integer.MAX_VALUE的字符数组表示
//         * 用于快速比较和验证整数字符串是否超出int类型范围
//         */
//        private static final char[] INTEGER_MAX_CHAR = String.valueOf(Integer.MAX_VALUE).toCharArray();
//        /**
//         * Long.MAX_VALUE的字符数组表示
//         * 用于快速比较和验证整数字符串是否超出long类型范围
//         */
//        private static final char[] LONG_MAX_CHAR = String.valueOf(Long.MAX_VALUE).toCharArray();
//
//        /**
//         * 私有构造函数，防止此类被实例化。
//         * 抛出AssertionError以确保在编译和运行时都无法创建此类的实例。
//         */
//        private StringParsingUtils() {
//            throw new AssertionError("No instances.");
//        }
//
//        /**
//         * 将十六进制字符转换为对应的数字
//         *
//         * @param codepoint 十六进制字符的Unicode码点
//         * @return 对应的数字值，如果输入不是有效的十六进制字符则返回-1
//         */
//        public static int parseHexCharToNumber(int codepoint) {
//            // 判断是否为0-9之间的数字字符
//            if (codepoint >= '0' && codepoint <= '9') {
//                codepoint = codepoint - '0';
//            } else if (codepoint >= 'a' && codepoint <= 'f') {
//                // 判断是否为a-f之间的字母字符
//                codepoint = codepoint - 'a' + 10;
//            } else if (codepoint >= 'A' && codepoint <= 'F') {
//                // 判断是否为A-F之间的字母字符
//                codepoint = codepoint - 'A' + 10;
//            } else {
//                // 如果不是有效的十六进制字符，返回-1
//                codepoint = -1;
//            }
//            return codepoint;
//        }
//
//        /**
//         * 解析Unicode转义序列并返回对应的代码点与码点字符个数
//         * 该方法用于处理形如 \\uXXXX 或 \\UXXXXXXXX 的Unicode转义序列
//         * 支持基本多语言平面（BMP）和辅助平面字符（Supplemental Planes）
//         *
//         * @param codepointPrefetch 一个IntUnaryOperator，用于预取下一个字符的代码点
//         *                          如果是BMP则需要连续取4个十六进制字符，否则需要至少5个
//         * @param isBMP             指示当前处理的是BMP字符（对应'\\u'）还是辅助平面字符（对应'\\U'）
//         * @return 返回长度为2的int数组：
//         * result[0]：解析后的Unicode代码点，如果失败则为-1
//         * result[1]：成功解析的十六进制字符个数
//         */
//        public static int[] parseUnicodeToCodepoint(IntUnaryOperator codepointPrefetch, boolean isBMP) {
//            // 初始化结果数组：[0]存储代码点，[1]存储成功解析的字符个数
//            int[] result = new int[]{0, 0};
//            // 创建一个数组来存储解析后的十六进制数字（最多8位）
//            int[] hexes = new int[8];
//
//            // 尝试解析第一个十六进制字符
//            hexes[result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(0));
//            if (hexes[result[1]] != -1) {
//                // 解析第二个十六进制字符
//                hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(1));
//                if (hexes[result[1]] != -1) {
//                    // 解析第三个十六进制字符
//                    hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(2));
//                    if (hexes[result[1]] != -1) {
//                        // 解析第四个十六进制字符
//                        hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(3));
//                        if (hexes[result[1]] != -1) ++result[1];
//                    }
//                }
//            }
//
//            // 如果成功解析了4个十六进制字符
//            if (result[1] == 4) {
//                if (isBMP) {
//                    // 处理BMP字符（即\\uXXXX格式）
//                    // 组合成16位的Unicode代码点
//                    result[0] = (hexes[0] << 12 | hexes[1] << 8 | hexes[2] << 4 | hexes[3]) & 0xFFFF;
//                } else {
//                    // 处理辅助平面字符（即\\UXXXXXXXX格式）
//                    // 继续解析后续的十六进制字符（最多到8个）
//                    hexes[result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(4));
//                    if (hexes[result[1]] != -1) {
//                        hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(5));
//                        if (hexes[result[1]] != -1) {
//                            hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(6));
//                            if (hexes[result[1]] != -1) {
//                                hexes[++result[1]] = StringParsingUtils.parseHexCharToNumber(codepointPrefetch.applyAsInt(7));
//                                if (hexes[result[1]] != -1) ++result[1];
//                            }
//                        }
//                    }
//
//                    // 如果成功解析超过4个十六进制字符，组合成完整的代码点
//                    if (result[1] > 4) {
//                        for (int i = 0; i < result[1]; i++) {
//                            result[0] = (result[0] << 4) | hexes[i];
//                        }
//                    }
//                }
//            }
//
//            // 验证代码点有效性
//            // 1. 代码点为0（空字符）被视为无效
//            // 2. BMP字符落在0xD800~0xDFFF范围内是非法的（这是UTF-16代理对区域）
//            // 3. 辅助平面字符超过0x10FFFF为非法（Unicode最大有效码点为0x10FFFF）
//            if (result[0] == 0 ||
//                    (result[1] == 4 && result[0] >= 0xD800 && result[0] <= 0xDFFF) ||
//                    (result[1] > 4 && result[0] > 0x10FFFF)) {
//                result[0] = -1;  // 标记为解析失败
//                result[1] = 0;   // 重置解析字符计数
//            }
//
//            return result;
//        }
//
//        /**
//         * 将字符串解析为字符数组
//         * 该方法首先移除字符串两端的空白字符，然后将剩余部分转换为字符数组返回
//         * 如果字符串为空或者仅包含空白字符，则返回一个空的字符数组
//         *
//         * @param string 待解析的字符串
//         * @return 解析后的字符数组，如果输入字符串为空或仅包含空白字符，则返回空数组
//         */
//        public static String parseToTrimString(String string) {
//            return new String(StringParsingUtils.parseToTrimCharArray(string));
//        }
//
//        /**
//         * 解析字符串，判断其是否表示null值
//         * 此方法主要用于判断给定的字符串是否符合null的表示（不考虑大小写），并排除了空字符串和只包含空白字符的情况
//         *
//         * @param string 待解析的字符串
//         * @return 如果字符串表示null（忽略大小写），则返回true；否则返回false
//         */
//        public static boolean parseNull(String string) {
//            // 判断输入字符串是否为空或只包含空白字符，如果是则返回false
//            if (string == null || string.isEmpty() || string.isBlank()) return false;
//
//            // 将输入字符串转换为字符数组
//            char[] chars = StringParsingUtils.parseToTrimCharArray(string);
//
//            // 检查字符串是否为"null"（不考虑大小写）
//            if (chars.length == 4) {
//                return Character.toLowerCase(chars[0]) == 'n' &&
//                        Character.toLowerCase(chars[1]) == 'u' &&
//                        Character.toLowerCase(chars[2]) == 'l' &&
//                        Character.toLowerCase(chars[3]) == 'l';
//            }
//            // 如果字符串不表示"null"，则返回false
//            return false;
//        }
//
//        /**
//         * 将字符串解析为布尔值
//         * 此方法旨在处理包含布尔值的字符串，返回相应的布尔值true或false
//         * 对于非布尔值字符串或空字符串，方法返回null
//         *
//         * @param string 待解析的字符串
//         * @return 解析后的布尔值，如果无法解析则返回null
//         */
//        public static Boolean parseBoolean(String string) {
//            // 判断输入字符串是否为空或只包含空白字符，如果是则返回null
//            if (string == null || string.isEmpty() || string.isBlank()) return null;
//
//            // 将输入字符串转换为字符数组
//            char[] chars = StringParsingUtils.parseToTrimCharArray(string);
//
//            // 检查是否为布尔值字符串
//            if (chars.length == 4) {
//                // 检查 "true" 或 "True" 等变体
//                if (Character.toLowerCase(chars[0]) == 't' &&
//                        Character.toLowerCase(chars[1]) == 'r' &&
//                        Character.toLowerCase(chars[2]) == 'u' &&
//                        Character.toLowerCase(chars[3]) == 'e') {
//                    return true;
//                }
//                // 检查 "false" 的变体（长度为5）
//            } else if (chars.length == 5) {
//                if (Character.toLowerCase(chars[0]) == 'f' &&
//                        Character.toLowerCase(chars[1]) == 'a' &&
//                        Character.toLowerCase(chars[2]) == 'l' &&
//                        Character.toLowerCase(chars[3]) == 's' &&
//                        Character.toLowerCase(chars[4]) == 'e') {
//                    return false;
//                }
//            }
//
//            // 如果不是有效的布尔值字符串，返回null
//            return null;
//        }
//
//
//    }
//
//
//    public static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;
//
//    /**
//     * 字符串常量：{@code "null"} <br>
//     * 注意：{@code "null" != null}
//     */
//    public static final String NULL = "null";
//
//    /**
//     * 字符串常量：空字符串 {@code ""}
//     */
//    public static final String EMPTY = "";
//
//    /**
//     * 字符串常量：空格符 {@code " "}
//     */
//    public static final String SPACE = " ";
//
//    /**
//     * 检查字符串是否为null、“null”、“undefined”
//     *
//     * @param str 被检查的字符串
//     * @return 是否为null、“null”、“undefined”
//     */
//    public static boolean isNullOrUndefined(CharSequence str) {
//        if (null == str) {
//            return true;
//        }
//        return isNullOrUndefinedStr(str);
//    }
//
//    /**
//     * 检查字符串是否为null、“”、“null”、“undefined”
//     *
//     * @param str 被检查的字符串
//     * @return 是否为null、“”、“null”、“undefined”
//     */
//    public static boolean emptyOrUndefined(CharSequence str) {
//        if (Whether.empty(str)) {
//            return true;
//        }
//        return isNullOrUndefinedStr(str);
//    }
//
//    /**
//     * 检查字符串是否为null、空白串、“null”、“undefined”
//     *
//     * @param str 被检查的字符串
//     * @return 是否为null、空白串、“null”、“undefined”
//     */
//    public static boolean isBlankOrUndefined(CharSequence str) {
//        if (Whether.blank(str)) {
//            return true;
//        }
//        return isNullOrUndefinedStr(str);
//    }
//
//    /**
//     * 是否为“null”、“undefined”，不做空指针检查
//     *
//     * @param str 字符串
//     * @return 是否为“null”、“undefined”
//     */
//    private static boolean isNullOrUndefinedStr(CharSequence str) {
//        String strString = str.toString().trim();
//        return NULL.equals(strString) || "undefined".equals(strString);
//    }
//
//    // ------------------------------------------------------------------------ Trim
//
//    /**
//     * 除去字符串头尾部的空白，如果字符串是{@code null}，依然返回{@code null}。
//     *
//     * <p>注意，和{@link String#trim()}不同，此方法使用{@link HelpChar#isBlankChar(char)} 来判定空白，
//     * 因而可以除去英文字符集之外的其它空白，如中文空格。
//     *
//     * <pre>
//     * trim(null)          = null
//     * trim(&quot;&quot;)            = &quot;&quot;
//     * trim(&quot;     &quot;)       = &quot;&quot;
//     * trim(&quot;abc&quot;)         = &quot;abc&quot;
//     * trim(&quot;    abc    &quot;) = &quot;abc&quot;
//     * </pre>
//     *
//     * @param str 要处理的字符串
//     * @return 除去头尾空白的字符串，如果原字串为{@code null}，则返回{@code null}
//     */
//    public static String trim(CharSequence str) {
//        return (null == str) ? null : trim(str, 0);
//    }
//
//    /**
//     * 除去字符串头尾部的空白，如果字符串是{@code null}，返回{@code ""}。
//     *
//     * <pre>
//     * StrUtil.trimToEmpty(null)          = ""
//     * StrUtil.trimToEmpty("")            = ""
//     * StrUtil.trimToEmpty("     ")       = ""
//     * StrUtil.trimToEmpty("abc")         = "abc"
//     * StrUtil.trimToEmpty("    abc    ") = "abc"
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 去除两边空白符后的字符串, 如果为null返回""
//     */
//    public static String trimToEmpty(CharSequence str) {
//        return str == null ? EMPTY : trim(str);
//    }
//
//    /**
//     * 除去字符串头尾部的空白，如果字符串是{@code null}或者""，返回{@code null}。
//     *
//     * <pre>
//     * StrUtil.trimToNull(null)          = null
//     * StrUtil.trimToNull("")            = null
//     * StrUtil.trimToNull("     ")       = null
//     * StrUtil.trimToNull("abc")         = "abc"
//     * StrUtil.trimToEmpty("    abc    ") = "abc"
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 去除两边空白符后的字符串, 如果为空返回null
//     */
//    public static String trimToNull(CharSequence str) {
//        final String trimStr = trim(str);
//        return EMPTY.equals(trimStr) ? null : trimStr;
//    }
//
//    /**
//     * 除去字符串头部的空白，如果字符串是{@code null}，则返回{@code null}。
//     *
//     * <p>注意，和{@link String#trim()}不同，此方法使用{@link HelpChar#isBlankChar(char)} 来判定空白，
//     * 因而可以除去英文字符集之外的其它空白，如中文空格。
//     *
//     * <pre>
//     * trimStart(null)         = null
//     * trimStart(&quot;&quot;)           = &quot;&quot;
//     * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
//     * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
//     * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
//     * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
//     * </pre>
//     *
//     * @param str 要处理的字符串
//     * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
//     */
//    public static String trimStart(CharSequence str) {
//        return trim(str, -1);
//    }
//
//    /**
//     * 除去字符串尾部的空白，如果字符串是{@code null}，则返回{@code null}。
//     *
//     * <p>注意，和{@link String#trim()}不同，此方法使用{@link HelpChar#isBlankChar(char)} 来判定空白，
//     * 因而可以除去英文字符集之外的其它空白，如中文空格。
//     *
//     * <pre>
//     * trimEnd(null)       = null
//     * trimEnd(&quot;&quot;)         = &quot;&quot;
//     * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
//     * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
//     * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
//     * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
//     * </pre>
//     *
//     * @param str 要处理的字符串
//     * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
//     */
//    public static String trimEnd(CharSequence str) {
//        return trim(str, 1);
//    }
//
//    /**
//     * 除去字符串头尾部的空白符，如果字符串是{@code null}，依然返回{@code null}。
//     *
//     * @param str  要处理的字符串
//     * @param mode {@code -1}表示trimStart，{@code 0}表示trim全部， {@code 1}表示trimEnd
//     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
//     */
//    public static String trim(CharSequence str, int mode) {
//        return trim(str, mode, HelpChar::isBlankChar);
//    }
//
//    /**
//     * 按照断言，除去字符串头尾部的断言为真的字符，如果字符串是{@code null}，依然返回{@code null}。
//     *
//     * @param str       要处理的字符串
//     * @param mode      {@code -1}表示trimStart，{@code 0}表示trim全部， {@code 1}表示trimEnd
//     * @param predicate 断言是否过掉字符，返回{@code true}表述过滤掉，{@code false}表示不过滤
//     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
//     */
//    public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
//        String result;
//        if (str == null) {
//            result = null;
//        } else {
//            int length = str.length();
//            int start = 0;
//            int end = length; // 扫描字符串头部
//            if (mode <= 0) {
//                while ((start < end) && (predicate.test(str.charAt(start)))) {
//                    start++;
//                }
//            } // 扫描字符串尾部
//            if (mode >= 0) {
//                while ((start < end) && (predicate.test(str.charAt(end - 1)))) {
//                    end--;
//                }
//            }
//            if ((start > 0) || (end < length)) {
//                result = str.toString().substring(start, end);
//            } else {
//                result = str.toString();
//            }
//        }
//
//        return result;
//    }
//
//    // ------------------------------------------------------------------------ startWith
//
//    /**
//     * 字符串是否以给定字符开始
//     *
//     * @param str 字符串
//     * @param c   字符
//     * @return 是否开始
//     */
//    public static boolean startWith(CharSequence str, char c) {
//        if (Whether.empty(str)) {
//            return false;
//        }
//        return c == str.charAt(0);
//    }
//
//    /**
//     * 是否以指定字符串开头<br>
//     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
//     *
//     * @param str        被监测字符串
//     * @param prefix     开头字符串
//     * @param ignoreCase 是否忽略大小写
//     * @return 是否以指定字符串开头
//     */
//    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
//        return startWith(str, prefix, ignoreCase, false);
//    }
//
//    /**
//     * 是否以指定字符串开头<br>
//     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false<br>
//     *
//     * <pre>
//     *     CharSequenceUtil.startWith("123", "123", false, true);   -- false
//     *     CharSequenceUtil.startWith("ABCDEF", "abc", true, true); -- true
//     *     CharSequenceUtil.startWith("abc", "abc", true, true);    -- false
//     * </pre>
//     *
//     * @param str          被监测字符串
//     * @param prefix       开头字符串
//     * @param ignoreCase   是否忽略大小写
//     * @param ignoreEquals 是否忽略字符串相等的情况
//     * @return 是否以指定字符串开头
//     */
//    public static boolean startWith(
//            CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
//        if (null == str || null == prefix) {
//            if (ignoreEquals) {
//                return false;
//            }
//            return null == str && null == prefix;
//        }
//
//        boolean isStartWith =
//                str.toString().regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());
//
//        if (isStartWith) {
//            return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
//        }
//        return false;
//    }
//
//    /**
//     * 是否以指定字符串开头
//     *
//     * @param str    被监测字符串
//     * @param prefix 开头字符串
//     * @return 是否以指定字符串开头
//     */
//    public static boolean startWith(CharSequence str, CharSequence prefix) {
//        return startWith(str, prefix, false);
//    }
//
//    /**
//     * 是否以指定字符串开头，忽略相等字符串的情况
//     *
//     * @param str    被监测字符串
//     * @param prefix 开头字符串
//     * @return 是否以指定字符串开头并且两个字符串不相等
//     */
//    public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
//        return startWith(str, prefix, false, true);
//    }
//
//    /**
//     * 是否以指定字符串开头，忽略大小写
//     *
//     * @param str    被监测字符串
//     * @param prefix 开头字符串
//     * @return 是否以指定字符串开头
//     */
//    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
//        return startWith(str, prefix, true);
//    }
//
//    /**
//     * 给定字符串是否以任何一个字符串开始<br>
//     * 给定字符串和数组为空都返回false
//     *
//     * @param str      给定字符串
//     * @param prefixes 需要检测的开始字符串
//     * @return 给定字符串是否以任何一个字符串开始
//     */
//    public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
//        if (Whether.empty(str) || Whether.empty(prefixes)) {
//            return false;
//        }
//
//        for (CharSequence suffix : prefixes) {
//            if (startWith(str, suffix, false)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
//     * 给定字符串和数组为空都返回false
//     *
//     * @param str      给定字符串
//     * @param suffixes 需要检测的结尾字符串
//     * @return 给定字符串是否以任何一个字符串结尾
//     */
//    public static boolean startWithAnyIgnoreCase(
//            final CharSequence str, final CharSequence... suffixes) {
//        if (Whether.empty(str) || Whether.empty(suffixes)) {
//            return false;
//        }
//
//        for (final CharSequence suffix : suffixes) {
//            if (startWith(str, suffix, true)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // ------------------------------------------------------------------------ endWith
//
//    /**
//     * 字符串是否以给定字符结尾
//     *
//     * @param str 字符串
//     * @param c   字符
//     * @return 是否结尾
//     */
//    public static boolean endWith(CharSequence str, char c) {
//        if (Whether.empty(str)) {
//            return false;
//        }
//        return c == str.charAt(str.length() - 1);
//    }
//
//    /**
//     * 是否以指定字符串结尾<br>
//     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
//     *
//     * @param str        被监测字符串
//     * @param suffix     结尾字符串
//     * @param ignoreCase 是否忽略大小写
//     * @return 是否以指定字符串结尾
//     */
//    public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
//        return endWith(str, suffix, ignoreCase, false);
//    }
//
//    /**
//     * 是否以指定字符串结尾<br>
//     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
//     *
//     * @param str          被监测字符串
//     * @param suffix       结尾字符串
//     * @param ignoreCase   是否忽略大小写
//     * @param ignoreEquals 是否忽略字符串相等的情况
//     * @return 是否以指定字符串结尾
//     */
//    public static boolean endWith(
//            CharSequence str, CharSequence suffix, boolean ignoreCase, boolean ignoreEquals) {
//        if (null == str || null == suffix) {
//            if (ignoreEquals) {
//                return false;
//            }
//            return null == str && null == suffix;
//        }
//
//        final int strOffset = str.length() - suffix.length();
//        boolean isEndWith =
//                str.toString().regionMatches(ignoreCase, strOffset, suffix.toString(), 0, suffix.length());
//
//        if (isEndWith) {
//            return (false == ignoreEquals) || (false == equals(str, suffix, ignoreCase));
//        }
//        return false;
//    }
//
//    /**
//     * 是否以指定字符串结尾
//     *
//     * @param str    被监测字符串
//     * @param suffix 结尾字符串
//     * @return 是否以指定字符串结尾
//     */
//    public static boolean endWith(CharSequence str, CharSequence suffix) {
//        return endWith(str, suffix, false);
//    }
//
//    /**
//     * 是否以指定字符串结尾，忽略大小写
//     *
//     * @param str    被监测字符串
//     * @param suffix 结尾字符串
//     * @return 是否以指定字符串结尾
//     */
//    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
//        return endWith(str, suffix, true);
//    }
//
//    /**
//     * 给定字符串是否以任何一个字符串结尾<br>
//     * 给定字符串和数组为空都返回false
//     *
//     * @param str      给定字符串
//     * @param suffixes 需要检测的结尾字符串
//     * @return 给定字符串是否以任何一个字符串结尾
//     */
//    public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
//        if (Whether.empty(str) || Whether.empty(suffixes)) {
//            return false;
//        }
//
//        for (CharSequence suffix : suffixes) {
//            if (endWith(str, suffix, false)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
//     * 给定字符串和数组为空都返回false
//     *
//     * @param str      给定字符串
//     * @param suffixes 需要检测的结尾字符串
//     * @return 给定字符串是否以任何一个字符串结尾
//     */
//    public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
//        if (Whether.empty(str) || Whether.empty(suffixes)) {
//            return false;
//        }
//
//        for (CharSequence suffix : suffixes) {
//            if (endWith(str, suffix, true)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // ------------------------------------------------------------------------ contains
//
//    /**
//     * 指定字符是否在字符串中出现过
//     *
//     * @param str        字符串
//     * @param searchChar 被查找的字符
//     * @return 是否包含
//     */
//    public static boolean contains(CharSequence str, char searchChar) {
//        return indexOf(str, searchChar) > -1;
//    }
//
//    /**
//     * 指定字符串是否在字符串中出现过
//     *
//     * @param str       字符串
//     * @param searchStr 被查找的字符串
//     * @return 是否包含
//     */
//    public static boolean contains(CharSequence str, CharSequence searchStr) {
//        if (null == str || null == searchStr) {
//            return false;
//        }
//        return str.toString().contains(searchStr);
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 是否包含任意一个字符串
//     */
//    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
//        return null != getContainsStr(str, testStrs);
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符列表中的任意一个字符
//     *
//     * @param str       指定字符串
//     * @param testChars 需要检查的字符数组
//     * @return 是否包含任意一个字符
//     */
//    public static boolean containsAny(CharSequence str, char... testChars) {
//        if (false == Whether.empty(str)) {
//            int len = str.length();
//            for (int i = 0; i < len; i++) {
//                if (HelpArray.contains(testChars, str.charAt(i))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 检查指定字符串中是否只包含给定的字符
//     *
//     * @param str       字符串
//     * @param testChars 检查的字符
//     * @return 字符串含有非检查的字符，返回false
//     */
//    public static boolean containsOnly(CharSequence str, char... testChars) {
//        if (false == Whether.empty(str)) {
//            int len = str.length();
//            for (int i = 0; i < len; i++) {
//                if (false == HelpArray.contains(testChars, str.charAt(i))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 检查指定字符串中是否含给定的所有字符串
//     *
//     * @param str       字符串
//     * @param testChars 检查的字符
//     * @return 字符串含有非检查的字符，返回false
//     */
//    public static boolean containsAll(CharSequence str, CharSequence... testChars) {
//        if (Whether.blank(str) || Whether.empty(testChars)) {
//            return false;
//        }
//        for (CharSequence testChar : testChars) {
//            if (false == contains(str, testChar)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 给定字符串是否包含空白符（空白符包括空格、制表符、全角空格和不间断空格）<br>
//     * 如果给定字符串为null或者""，则返回false
//     *
//     * @param str 字符串
//     * @return 是否包含空白符
//     */
//    public static boolean containsBlank(CharSequence str) {
//        if (null == str) {
//            return false;
//        }
//        final int length = str.length();
//        if (0 == length) {
//            return false;
//        }
//
//        for (int i = 0; i < length; i += 1) {
//            if (HelpChar.isBlankChar(str.charAt(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 被包含的第一个字符串
//     */
//    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
//        if (Whether.empty(str) || Whether.empty(testStrs)) {
//            return null;
//        }
//        for (CharSequence checkStr : testStrs) {
//            if (str.toString().contains(checkStr)) {
//                return checkStr.toString();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 是否包含特定字符，忽略大小写，如果给定两个参数都为{@code null}，返回true
//     *
//     * @param str     被检测字符串
//     * @param testStr 被测试是否包含的字符串
//     * @return 是否包含
//     */
//    public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
//        if (null == str) {
//            // 如果被监测字符串和
//            return null == testStr;
//        }
//        return indexOfIgnoreCase(str, testStr) > -1;
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
//     * 忽略大小写
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 是否包含任意一个字符串
//     */
//    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
//        return null != getContainsStrIgnoreCase(str, testStrs);
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
//     * 忽略大小写
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 被包含的第一个字符串
//     */
//    public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
//        if (Whether.empty(str) || Whether.empty(testStrs)) {
//            return null;
//        }
//        for (CharSequence testStr : testStrs) {
//            if (containsIgnoreCase(str, testStr)) {
//                return testStr.toString();
//            }
//        }
//        return null;
//    }
//
//    // ------------------------------------------------------------------------ indexOf
//
//    /**
//     * 指定范围内查找指定字符
//     *
//     * @param str        字符串
//     * @param searchChar 被查找的字符
//     * @return 位置
//     */
//    public static int indexOf(CharSequence str, char searchChar) {
//        return indexOf(str, searchChar, 0);
//    }
//
//    /**
//     * 指定范围内查找指定字符
//     *
//     * @param str        字符串
//     * @param searchChar 被查找的字符
//     * @param start      起始位置，如果小于0，从0开始查找
//     * @return 位置
//     */
//    public static int indexOf(CharSequence str, char searchChar, int start) {
//        if (str instanceof String) {
//            return ((String) str).indexOf(searchChar, start);
//        } else {
//            return indexOf(str, searchChar, start, -1);
//        }
//    }
//
//    /**
//     * 指定范围内查找指定字符
//     *
//     * @param text       字符串
//     * @param searchChar 被查找的字符
//     * @param start      起始位置，如果小于0，从0开始查找
//     * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
//     * @return 位置
//     */
//    public static int indexOf(CharSequence text, char searchChar, int start, int end) {
//        if (Whether.empty(text)) {
//            return INDEX_NOT_FOUND;
//        }
//        return new CharFinder(searchChar).setText(text).setEndIndex(end).start(start);
//    }
//
//    /**
//     * 指定范围内查找字符串，忽略大小写<br>
//     *
//     * <pre>
//     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
//     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
//     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
//     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
//     * </pre>
//     *
//     * @param str       字符串
//     * @param searchStr 需要查找位置的字符串
//     * @return 位置
//     */
//    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
//        return indexOfIgnoreCase(str, searchStr, 0);
//    }
//
//    /**
//     * 指定范围内查找字符串
//     *
//     * <pre>
//     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
//     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
//     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
//     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
//     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
//     * </pre>
//     *
//     * @param str       字符串
//     * @param searchStr 需要查找位置的字符串
//     * @param fromIndex 起始位置
//     * @return 位置
//     */
//    public static int indexOfIgnoreCase(
//            final CharSequence str, final CharSequence searchStr, int fromIndex) {
//        return indexOf(str, searchStr, fromIndex, true);
//    }
//
//    /**
//     * 指定范围内查找字符串
//     *
//     * @param text       字符串，空则返回-1
//     * @param searchStr  需要查找位置的字符串，空则返回-1
//     * @param from       起始位置（包含）
//     * @param ignoreCase 是否忽略大小写
//     * @return 位置
//     */
//    public static int indexOf(
//            CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
//        if (Whether.empty(text) || Whether.empty(searchStr)) {
//            if (HelpString.equals(text, searchStr)) {
//                return 0;
//            } else {
//                return INDEX_NOT_FOUND;
//            }
//        }
//        return new StrFinder(searchStr, ignoreCase).setText(text).start(from);
//    }
//
//    /**
//     * 指定范围内查找字符串，忽略大小写
//     *
//     * @param str       字符串
//     * @param searchStr 需要查找位置的字符串
//     * @return 位置
//     */
//    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
//        return lastIndexOfIgnoreCase(str, searchStr, str.length());
//    }
//
//    /**
//     * 指定范围内查找字符串，忽略大小写<br>
//     * fromIndex 为搜索起始位置，从后往前计数
//     *
//     * @param str       字符串
//     * @param searchStr 需要查找位置的字符串
//     * @param fromIndex 起始位置，从后往前计数
//     * @return 位置
//     */
//    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
//        return lastIndexOf(str, searchStr, fromIndex, true);
//    }
//
//    /**
//     * 指定范围内查找字符串<br>
//     * fromIndex 为搜索起始位置，从后往前计数
//     *
//     * @param text       字符串
//     * @param searchStr  需要查找位置的字符串
//     * @param from       起始位置，从后往前计数
//     * @param ignoreCase 是否忽略大小写
//     * @return 位置
//     */
//    public static int lastIndexOf(
//            CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
//        if (Whether.empty(text) || Whether.empty(searchStr)) {
//            if (HelpString.equals(text, searchStr)) {
//                return 0;
//            } else {
//                return INDEX_NOT_FOUND;
//            }
//        }
//        return new StrFinder(searchStr, ignoreCase).setText(text).setNegative(true).start(from);
//    }
//
//    /**
//     * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置。
//     *
//     * <p>如果 str=null 或 searchStr=null 或 ordinal&ge;0 则返回-1<br>
//     * 此方法来自：Apache-Commons-Lang
//     *
//     * <p>例子（*代表任意字符）：
//     *
//     * <pre>
//     * StrUtil.ordinalIndexOf(null, *, *)          = -1
//     * StrUtil.ordinalIndexOf(*, null, *)          = -1
//     * StrUtil.ordinalIndexOf("", "", *)           = 0
//     * StrUtil.ordinalIndexOf("aabaabaa", "a", 1)  = 0
//     * StrUtil.ordinalIndexOf("aabaabaa", "a", 2)  = 1
//     * StrUtil.ordinalIndexOf("aabaabaa", "b", 1)  = 2
//     * StrUtil.ordinalIndexOf("aabaabaa", "b", 2)  = 5
//     * StrUtil.ordinalIndexOf("aabaabaa", "ab", 1) = 1
//     * StrUtil.ordinalIndexOf("aabaabaa", "ab", 2) = 4
//     * StrUtil.ordinalIndexOf("aabaabaa", "", 1)   = 0
//     * StrUtil.ordinalIndexOf("aabaabaa", "", 2)   = 0
//     * </pre>
//     *
//     * @param str       被检查的字符串，可以为null
//     * @param searchStr 被查找的字符串，可以为null
//     * @param ordinal   第几次出现的位置
//     * @return 查找到的位置
//     */
//    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
//        if (str == null || searchStr == null || ordinal <= 0) {
//            return INDEX_NOT_FOUND;
//        }
//        if (searchStr.length() == 0) {
//            return 0;
//        }
//        int found = 0;
//        int index = INDEX_NOT_FOUND;
//        do {
//            index = indexOf(str, searchStr, index + 1, false);
//            if (index < 0) {
//                return index;
//            }
//            found++;
//        } while (found < ordinal);
//        return index;
//    }
//
//    // ------------------------------------------------------------------------ remove
//
//    /**
//     * 移除字符串中所有给定字符串<br>
//     * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
//     *
//     * @param str         字符串
//     * @param strToRemove 被移除的字符串
//     * @return 移除后的字符串
//     */
//    public static String removeAll(CharSequence str, CharSequence strToRemove) {
//        // strToRemove如果为空， 也不用继续后面的逻辑
//        if (Whether.empty(str) || Whether.empty(strToRemove)) {
//            return str(str);
//        }
//        return str.toString().replace(strToRemove, EMPTY);
//    }
//
//    /**
//     * 移除字符串中所有给定字符串，当某个字符串出现多次，则全部移除<br>
//     * 例：removeAny("aa-bb-cc-dd", "a", "b") =》 --cc-dd
//     *
//     * @param str          字符串
//     * @param strsToRemove 被移除的字符串
//     * @return 移除后的字符串
//     */
//    public static String removeAny(CharSequence str, CharSequence... strsToRemove) {
//        String result = str(str);
//        if (Whether.noEmpty(str)) {
//            for (CharSequence strToRemove : strsToRemove) {
//                result = removeAll(result, strToRemove);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 去除字符串中指定的多个字符，如有多个则全部去除
//     *
//     * @param str   字符串
//     * @param chars 字符列表
//     * @return 去除后的字符
//     */
//    public static String removeAll(CharSequence str, char... chars) {
//        if (null == str || Whether.empty(chars)) {
//            return str(str);
//        }
//        final int len = str.length();
//        if (0 == len) {
//            return str(str);
//        }
//        final StringBuilder builder = new StringBuilder(len);
//        char c;
//        for (int i = 0; i < len; i++) {
//            c = str.charAt(i);
//            if (false == HelpArray.contains(chars, c)) {
//                builder.append(c);
//            }
//        }
//        return builder.toString();
//    }
//
//    /**
//     * 去除所有换行符，包括：
//     *
//     * <pre>
//     * 1. \r
//     * 1. \n
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 处理后的字符串
//     */
//    public static String removeAllLineBreaks(CharSequence str) {
//        return removeAll(str, HelpChar.CR, HelpChar.LF);
//    }
//
//    /**
//     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
//     * 例如：str=setName, preLength=3 =》 return name
//     *
//     * @param str       被处理的字符串
//     * @param preLength 去掉的长度
//     * @return 处理后的字符串，不符合规范返回null
//     */
//    public static String removePreAndLowerFirst(CharSequence str, int preLength) {
//        if (str == null) {
//            return null;
//        }
//        if (str.length() > preLength) {
//            char first = Character.toLowerCase(str.charAt(preLength));
//            if (str.length() > preLength + 1) {
//                return first + str.toString().substring(preLength + 1);
//            }
//            return String.valueOf(first);
//        } else {
//            return str.toString();
//        }
//    }
//
//    /**
//     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
//     * 例如：str=setName, prefix=set =》 return name
//     *
//     * @param str    被处理的字符串
//     * @param prefix 前缀
//     * @return 处理后的字符串，不符合规范返回null
//     */
//    public static String removePreAndLowerFirst(CharSequence str, CharSequence prefix) {
//        return lowerFirst(removePrefix(str, prefix));
//    }
//
//    /**
//     * 去掉指定前缀
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
//     */
//    public static String removePrefix(CharSequence str, CharSequence prefix) {
//        if (Whether.empty(str) || Whether.empty(prefix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (str2.startsWith(prefix.toString())) {
//            return subSuf(str2, prefix.length()); // 截取后半段
//        }
//        return str2;
//    }
//
//    /**
//     * 忽略大小写去掉指定前缀
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
//     */
//    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
//        if (Whether.empty(str) || Whether.empty(prefix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (startWithIgnoreCase(str, prefix)) {
//            return subSuf(str2, prefix.length()); // 截取后半段
//        }
//        return str2;
//    }
//
//    /**
//     * 去掉指定后缀
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSuffix(CharSequence str, CharSequence suffix) {
//        if (Whether.empty(str) || Whether.empty(suffix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (str2.endsWith(suffix.toString())) {
//            return subPre(str2, str2.length() - suffix.length()); // 截取前半段
//        }
//        return str2;
//    }
//
//    /**
//     * 去掉指定后缀，并小写首字母
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
//        return lowerFirst(removeSuffix(str, suffix));
//    }
//
//    /**
//     * 忽略大小写去掉指定后缀
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
//        if (Whether.empty(str) || Whether.empty(suffix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (endWithIgnoreCase(str, suffix)) {
//            return subPre(str2, str2.length() - suffix.length());
//        }
//        return str2;
//    }
//
//    /**
//     * 清理空白字符
//     *
//     * @param str 被清理的字符串
//     * @return 清理后的字符串
//     */
//    public static String cleanBlank(CharSequence str) {
//        return filter(str, c -> false == HelpChar.isBlankChar(c));
//    }
//
//    // ------------------------------------------------------------------------ strip
//
//    /**
//     * 去除两边的指定字符串
//     *
//     * @param str            被处理的字符串
//     * @param prefixOrSuffix 前缀或后缀
//     * @return 处理后的字符串
//     */
//    public static String strip(CharSequence str, CharSequence prefixOrSuffix) {
//        if (equals(str, prefixOrSuffix)) {
//            // 对于去除相同字符的情况单独处理
//            return EMPTY;
//        }
//        return strip(str, prefixOrSuffix, prefixOrSuffix);
//    }
//
//    /**
//     * 去除两边的指定字符串
//     *
//     * @param str    被处理的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 处理后的字符串
//     */
//    public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//
//        int from = 0;
//        int to = str.length();
//
//        String str2 = str.toString();
//        if (startWith(str2, prefix)) {
//            from = prefix.length();
//        }
//        if (endWith(str2, suffix)) {
//            to -= suffix.length();
//        }
//
//        return str2.substring(Math.min(from, to), Math.max(from, to));
//    }
//
//    /**
//     * 去除两边的指定字符串，忽略大小写
//     *
//     * @param str            被处理的字符串
//     * @param prefixOrSuffix 前缀或后缀
//     * @return 处理后的字符串
//     */
//    public static String stripIgnoreCase(CharSequence str, CharSequence prefixOrSuffix) {
//        return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
//    }
//
//    /**
//     * 去除两边的指定字符串，忽略大小写
//     *
//     * @param str    被处理的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 处理后的字符串
//     */
//    public static String stripIgnoreCase(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        int from = 0;
//        int to = str.length();
//
//        String str2 = str.toString();
//        if (startWithIgnoreCase(str2, prefix)) {
//            from = prefix.length();
//        }
//        if (endWithIgnoreCase(str2, suffix)) {
//            to -= suffix.length();
//        }
//        return str2.substring(from, to);
//    }
//
//    // ------------------------------------------------------------------------ add
//
//    /**
//     * 如果给定字符串不是以prefix开头的，在开头补充 prefix
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 补充后的字符串
//     * @see #prependIfMissing(CharSequence, CharSequence, CharSequence...)
//     */
//    public static String addPrefixIfNot(CharSequence str, CharSequence prefix) {
//        return prependIfMissing(str, prefix, prefix);
//    }
//
//    /**
//     * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 补充后的字符串
//     * @see #appendIfMissing(CharSequence, CharSequence, CharSequence...)
//     */
//    public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
//        return appendIfMissing(str, suffix, suffix);
//    }
//
//    // ------------------------------------------------------------------------ split
//
//    /**
//     * 切分字符串为long数组
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符
//     * @return 切分后long数组
//     */
//    public static long[] splitToLong(CharSequence str, char separator) {
//        return Convert.convert(long[].class, splitTrim(str, separator));
//    }
//
//    /**
//     * 切分字符串为long数组
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符串
//     * @return 切分后long数组
//     */
//    public static long[] splitToLong(CharSequence str, CharSequence separator) {
//        return Convert.convert(long[].class, splitTrim(str, separator));
//    }
//
//    /**
//     * 切分字符串为int数组
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符
//     * @return 切分后long数组
//     */
//    public static int[] splitToInt(CharSequence str, char separator) {
//        return Convert.convert(int[].class, splitTrim(str, separator));
//    }
//
//    /**
//     * 切分字符串为int数组
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符串
//     * @return 切分后long数组
//     */
//    public static int[] splitToInt(CharSequence str, CharSequence separator) {
//        return Convert.convert(int[].class, splitTrim(str, separator));
//    }
//
//    /**
//     * 切分字符串<br>
//     * a#b#c =》 [a,b,c] <br>
//     * a##b#c =》 [a,"",b,c]
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @return 切分后的集合
//     */
//    public static List<String> split(CharSequence str, char separator) {
//        return split(str, separator, 0);
//    }
//
//    /**
//     * 切分字符串，如果分隔符不存在则返回原字符串
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符
//     * @return 字符串
//     */
//    public static String[] splitToArray(CharSequence str, CharSequence separator) {
//        if (str == null) {
//            return new String[]{};
//        }
//
//        return StrSplitter.splitToArray(str.toString(), str(separator), 0, false, false);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @return 切分后的数组
//     */
//    public static String[] splitToArray(CharSequence str, char separator) {
//        return splitToArray(str, separator, 0);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param text      被切分的字符串
//     * @param separator 分隔符字符
//     * @param limit     限制分片数
//     * @return 切分后的数组
//     */
//    public static String[] splitToArray(CharSequence text, char separator, int limit) {
//        Assert.notNull(text, "Text must be not null!");
//        return StrSplitter.splitToArray(text.toString(), separator, limit, false, false);
//    }
//
//    /**
//     * 切分字符串，不去除切分后每个元素两边的空白符，不去除空白项
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @param limit     限制分片数，-1不限制
//     * @return 切分后的集合
//     */
//    public static List<String> split(CharSequence str, char separator, int limit) {
//        return split(str, separator, limit, false, false);
//    }
//
//    /**
//     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @return 切分后的集合
//     */
//    public static List<String> splitTrim(CharSequence str, char separator) {
//        return splitTrim(str, separator, -1);
//    }
//
//    /**
//     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @return 切分后的集合
//     */
//    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
//        return splitTrim(str, separator, -1);
//    }
//
//    /**
//     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @param limit     限制分片数，-1不限制
//     * @return 切分后的集合
//     */
//    public static List<String> splitTrim(CharSequence str, char separator, int limit) {
//        return split(str, separator, limit, true, true);
//    }
//
//    /**
//     * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符字符
//     * @param limit     限制分片数，-1不限制
//     * @return 切分后的集合
//     */
//    public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
//        return split(str, separator, limit, true, true);
//    }
//
//    /**
//     * 切分字符串，不限制分片数量
//     *
//     * @param str         被切分的字符串
//     * @param separator   分隔符字符
//     * @param isTrim      是否去除切分字符串后每个元素两边的空格
//     * @param ignoreEmpty 是否忽略空串
//     * @return 切分后的集合
//     */
//    public static List<String> split(
//            CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
//        return split(str, separator, 0, isTrim, ignoreEmpty);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param str         被切分的字符串
//     * @param separator   分隔符字符
//     * @param limit       限制分片数，-1不限制
//     * @param isTrim      是否去除切分字符串后每个元素两边的空格
//     * @param ignoreEmpty 是否忽略空串
//     * @return 切分后的集合
//     */
//    public static List<String> split(
//            CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//        return StrSplitter.split(str, separator, limit, isTrim, ignoreEmpty);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param <R>         切分后元素类型
//     * @param str         被切分的字符串
//     * @param separator   分隔符字符
//     * @param limit       限制分片数，-1不限制
//     * @param ignoreEmpty 是否忽略空串
//     * @param mapping     切分后的字符串元素的转换方法
//     * @return 切分后的集合，元素类型是经过 mapping 转换后的
//     */
//    public static <R> List<R> split(
//            CharSequence str,
//            char separator,
//            int limit,
//            boolean ignoreEmpty,
//            Function<String, R> mapping) {
//        return StrSplitter.split(str, separator, limit, ignoreEmpty, mapping);
//    }
//
//    /**
//     * 切分字符串，如果分隔符不存在则返回原字符串
//     *
//     * @param str       被切分的字符串
//     * @param separator 分隔符
//     * @return 字符串
//     */
//    public static List<String> split(CharSequence str, CharSequence separator) {
//        return split(str, separator, false, false);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param str         被切分的字符串
//     * @param separator   分隔符字符
//     * @param isTrim      是否去除切分字符串后每个元素两边的空格
//     * @param ignoreEmpty 是否忽略空串
//     * @return 切分后的集合
//     */
//    public static List<String> split(
//            CharSequence str, CharSequence separator, boolean isTrim, boolean ignoreEmpty) {
//        return split(str, separator, 0, isTrim, ignoreEmpty);
//    }
//
//    /**
//     * 切分字符串
//     *
//     * @param str         被切分的字符串
//     * @param separator   分隔符字符
//     * @param limit       限制分片数，-1不限制
//     * @param isTrim      是否去除切分字符串后每个元素两边的空格
//     * @param ignoreEmpty 是否忽略空串
//     * @return 切分后的集合
//     */
//    public static List<String> split(
//            CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//        final String separatorStr = (null == separator) ? null : separator.toString();
//        return StrSplitter.split(str, separatorStr, limit, isTrim, ignoreEmpty);
//    }
//
//    /**
//     * 根据给定长度，将给定字符串截取为多个部分
//     *
//     * @param str 字符串
//     * @param len 每一个小节的长度
//     * @return 截取后的字符串数组
//     * @see StrSplitter#splitByLength(CharSequence, int)
//     */
//    public static String[] split(CharSequence str, int len) {
//        return StrSplitter.splitByLength(str, len);
//    }
//
//    /**
//     * 将字符串切分为N等份
//     *
//     * @param str        字符串
//     * @param partLength 每等份的长度
//     * @return 切分后的数组
//     */
//    public static String[] cut(CharSequence str, int partLength) {
//        if (null == str) {
//            return null;
//        }
//        int len = str.length();
//        if (len < partLength) {
//            return new String[]{str.toString()};
//        }
//        int part = NumberUtil.count(len, partLength);
//        final String[] array = new String[part];
//
//        final String str2 = str.toString();
//        for (int i = 0; i < part; i++) {
//            array[i] =
//                    str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
//        }
//        return array;
//    }
//
//    // ------------------------------------------------------------------------ sub
//
//    /**
//     * 改进JDK subString<br>
//     * index从0开始计算，最后一个字符为-1<br>
//     * 如果from和to位置一样，返回 "" <br>
//     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
//     * 如果经过修正的index中from大于to，则互换from和to example: <br>
//     * abcdefgh 2 3 =》 c <br>
//     * abcdefgh 2 -3 =》 cde <br>
//     *
//     * @param str              String
//     * @param fromIndexInclude 开始的index（包括）
//     * @param toIndexExclude   结束的index（不包括）
//     * @return 字串
//     */
//    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        int len = str.length();
//
//        if (fromIndexInclude < 0) {
//            fromIndexInclude = len + fromIndexInclude;
//            if (fromIndexInclude < 0) {
//                fromIndexInclude = 0;
//            }
//        } else if (fromIndexInclude > len) {
//            fromIndexInclude = len;
//        }
//
//        if (toIndexExclude < 0) {
//            toIndexExclude = len + toIndexExclude;
//            if (toIndexExclude < 0) {
//                toIndexExclude = len;
//            }
//        } else if (toIndexExclude > len) {
//            toIndexExclude = len;
//        }
//
//        if (toIndexExclude < fromIndexInclude) {
//            int tmp = fromIndexInclude;
//            fromIndexInclude = toIndexExclude;
//            toIndexExclude = tmp;
//        }
//
//        if (fromIndexInclude == toIndexExclude) {
//            return EMPTY;
//        }
//
//        return str.toString().substring(fromIndexInclude, toIndexExclude);
//    }
//
//    /**
//     * 通过CodePoint截取字符串，可以截断Emoji
//     *
//     * @param str       String
//     * @param fromIndex 开始的index（包括）
//     * @param toIndex   结束的index（不包括）
//     * @return 字串
//     */
//    public static String subByCodePoint(CharSequence str, int fromIndex, int toIndex) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//
//        if (fromIndex < 0 || fromIndex > toIndex) {
//            throw new IllegalArgumentException();
//        }
//
//        if (fromIndex == toIndex) {
//            return EMPTY;
//        }
//
//        final StringBuilder sb = new StringBuilder();
//        final int subLen = toIndex - fromIndex;
//        str.toString()
//                .codePoints()
//                .skip(fromIndex)
//                .limit(subLen)
//                .forEach(v -> sb.append(Character.toChars(v)));
//        return sb.toString();
//    }
//
//    /**
//     * 截取部分字符串，这里一个汉字的长度认为是2
//     *
//     * @param str    字符串
//     * @param len    bytes切割到的位置（包含）
//     * @param suffix 切割后加上后缀
//     * @return 切割后的字符串
//     */
//    public static String subPreGbk(CharSequence str, int len, CharSequence suffix) {
//        return subPreGbk(str, len, true) + suffix;
//    }
//
//    /**
//     * 截取部分字符串，这里一个汉字的长度认为是2<br>
//     * 可以自定义halfUp，如len为10，如果截取后最后一个字符是半个字符，{@code true}表示保留，则长度是11，否则长度9
//     *
//     * @param str    字符串
//     * @param len    bytes切割到的位置（包含）
//     * @param halfUp 遇到截取一半的GBK字符，是否保留。
//     * @return 切割后的字符串
//     */
//    public static String subPreGbk(CharSequence str, int len, boolean halfUp) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//
//        int counterOfDoubleByte = 0;
//        final byte[] b = bytes(str, CharsetUtil.CHARSET_GBK);
//        if (b.length <= len) {
//            return str.toString();
//        }
//        for (int i = 0; i < len; i++) {
//            if (b[i] < 0) {
//                counterOfDoubleByte++;
//            }
//        }
//
//        if (counterOfDoubleByte % 2 != 0) {
//            if (halfUp) {
//                len += 1;
//            } else {
//                len -= 1;
//            }
//        }
//        return new String(b, 0, len, CharsetUtil.CHARSET_GBK);
//    }
//
//    /**
//     * 切割指定位置之前部分的字符串
//     *
//     * @param string         字符串
//     * @param toIndexExclude 切割到的位置（不包括）
//     * @return 切割后的剩余的前半部分字符串
//     */
//    public static String subPre(CharSequence string, int toIndexExclude) {
//        return sub(string, 0, toIndexExclude);
//    }
//
//    /**
//     * 切割指定位置之后部分的字符串
//     *
//     * @param string    字符串
//     * @param fromIndex 切割开始的位置（包括）
//     * @return 切割后后剩余的后半部分字符串
//     */
//    public static String subSuf(CharSequence string, int fromIndex) {
//        if (Whether.empty(string)) {
//            return null;
//        }
//        return sub(string, fromIndex, string.length());
//    }
//
//    /**
//     * 切割指定长度的后部分的字符串
//     *
//     * <pre>
//     * StrUtil.subSufByLength("abcde", 3)      =    "cde"
//     * StrUtil.subSufByLength("abcde", 0)      =    ""
//     * StrUtil.subSufByLength("abcde", -5)     =    ""
//     * StrUtil.subSufByLength("abcde", -1)     =    ""
//     * StrUtil.subSufByLength("abcde", 5)       =    "abcde"
//     * StrUtil.subSufByLength("abcde", 10)     =    "abcde"
//     * StrUtil.subSufByLength(null, 3)               =    null
//     * </pre>
//     *
//     * @param string 字符串
//     * @param length 切割长度
//     * @return 切割后后剩余的后半部分字符串
//     */
//    public static String subSufByLength(CharSequence string, int length) {
//        if (Whether.empty(string)) {
//            return null;
//        }
//        if (length <= 0) {
//            return EMPTY;
//        }
//        return sub(string, -length, string.length());
//    }
//
//    /**
//     * 截取字符串,从指定位置开始,截取指定长度的字符串<br>
//     * author weibaohui
//     *
//     * @param input     原始字符串
//     * @param fromIndex 开始的index,包括
//     * @param length    要截取的长度
//     * @return 截取后的字符串
//     */
//    public static String subWithLength(String input, int fromIndex, int length) {
//        return sub(input, fromIndex, fromIndex + length);
//    }
//
//    /**
//     * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
//     * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
//     * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串，举例如下：
//     *
//     * <pre>
//     * StrUtil.subBefore(null, *, false)      = null
//     * StrUtil.subBefore("", *, false)        = ""
//     * StrUtil.subBefore("abc", "a", false)   = ""
//     * StrUtil.subBefore("abcba", "b", false) = "a"
//     * StrUtil.subBefore("abc", "c", false)   = "ab"
//     * StrUtil.subBefore("abc", "d", false)   = "abc"
//     * StrUtil.subBefore("abc", "", false)    = ""
//     * StrUtil.subBefore("abc", null, false)  = "abc"
//     * </pre>
//     *
//     * @param string          被查找的字符串
//     * @param separator       分隔字符串（不包括）
//     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
//     * @return 切割后的字符串
//     */
//    public static String subBefore(
//            CharSequence string, CharSequence separator, boolean isLastSeparator) {
//        if (Whether.empty(string) || separator == null) {
//            return null == string ? null : string.toString();
//        }
//
//        final String str = string.toString();
//        final String sep = separator.toString();
//        if (Whether.empty(sep)) {
//            return EMPTY;
//        }
//        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
//        if (INDEX_NOT_FOUND == pos) {
//            return str;
//        }
//        if (0 == pos) {
//            return EMPTY;
//        }
//        return str.substring(0, pos);
//    }
//
//    /**
//     * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
//     * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
//     * 如果分隔字符串未找到，返回原字符串，举例如下：
//     *
//     * <pre>
//     * StrUtil.subBefore(null, *, false)      = null
//     * StrUtil.subBefore("", *, false)        = ""
//     * StrUtil.subBefore("abc", 'a', false)   = ""
//     * StrUtil.subBefore("abcba", 'b', false) = "a"
//     * StrUtil.subBefore("abc", 'c', false)   = "ab"
//     * StrUtil.subBefore("abc", 'd', false)   = "abc"
//     * </pre>
//     *
//     * @param string          被查找的字符串
//     * @param separator       分隔字符串（不包括）
//     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
//     * @return 切割后的字符串
//     */
//    public static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
//        if (Whether.empty(string)) {
//            return null == string ? null : EMPTY;
//        }
//
//        final String str = string.toString();
//        final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
//        if (INDEX_NOT_FOUND == pos) {
//            return str;
//        }
//        if (0 == pos) {
//            return EMPTY;
//        }
//        return str.substring(0, pos);
//    }
//
//    /**
//     * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
//     * 如果给定的字符串为空串（null或""），返回原字符串<br>
//     * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
//     *
//     * <pre>
//     * StrUtil.subAfter(null, *, false)      = null
//     * StrUtil.subAfter("", *, false)        = ""
//     * StrUtil.subAfter(*, null, false)      = ""
//     * StrUtil.subAfter("abc", "a", false)   = "bc"
//     * StrUtil.subAfter("abcba", "b", false) = "cba"
//     * StrUtil.subAfter("abc", "c", false)   = ""
//     * StrUtil.subAfter("abc", "d", false)   = ""
//     * StrUtil.subAfter("abc", "", false)    = "abc"
//     * </pre>
//     *
//     * @param string          被查找的字符串
//     * @param separator       分隔字符串（不包括）
//     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
//     * @return 切割后的字符串
//     */
//    public static String subAfter(
//            CharSequence string, CharSequence separator, boolean isLastSeparator) {
//        if (Whether.empty(string)) {
//            return null == string ? null : EMPTY;
//        }
//        if (separator == null) {
//            return EMPTY;
//        }
//        final String str = string.toString();
//        final String sep = separator.toString();
//        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
//        if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
//            return EMPTY;
//        }
//        return str.substring(pos + separator.length());
//    }
//
//    /**
//     * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
//     * 如果给定的字符串为空串（null或""），返回原字符串<br>
//     * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
//     *
//     * <pre>
//     * StrUtil.subAfter(null, *, false)      = null
//     * StrUtil.subAfter("", *, false)        = ""
//     * StrUtil.subAfter("abc", 'a', false)   = "bc"
//     * StrUtil.subAfter("abcba", 'b', false) = "cba"
//     * StrUtil.subAfter("abc", 'c', false)   = ""
//     * StrUtil.subAfter("abc", 'd', false)   = ""
//     * </pre>
//     *
//     * @param string          被查找的字符串
//     * @param separator       分隔字符串（不包括）
//     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
//     * @return 切割后的字符串
//     */
//    public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
//        if (Whether.empty(string)) {
//            return null == string ? null : EMPTY;
//        }
//        final String str = string.toString();
//        final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
//        if (INDEX_NOT_FOUND == pos) {
//            return EMPTY;
//        }
//        return str.substring(pos + 1);
//    }
//
//    /**
//     * 截取指定字符串中间部分，不包括标识字符串<br>
//     *
//     * <p>栗子：
//     *
//     * <pre>
//     * StrUtil.subBetween("wx[b]yz", "[", "]") = "b"
//     * StrUtil.subBetween(null, *, *)          = null
//     * StrUtil.subBetween(*, null, *)          = null
//     * StrUtil.subBetween(*, *, null)          = null
//     * StrUtil.subBetween("", "", "")          = ""
//     * StrUtil.subBetween("", "", "]")         = null
//     * StrUtil.subBetween("", "[", "]")        = null
//     * StrUtil.subBetween("yabcz", "", "")     = ""
//     * StrUtil.subBetween("yabcz", "y", "z")   = "abc"
//     * StrUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
//     * </pre>
//     *
//     * @param str    被切割的字符串
//     * @param before 截取开始的字符串标识
//     * @param after  截取到的字符串标识
//     * @return 截取后的字符串
//     */
//    public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
//        if (str == null || before == null || after == null) {
//            return null;
//        }
//
//        final String str2 = str.toString();
//        final String before2 = before.toString();
//        final String after2 = after.toString();
//
//        final int start = str2.indexOf(before2);
//        if (start != INDEX_NOT_FOUND) {
//            final int end = str2.indexOf(after2, start + before2.length());
//            if (end != INDEX_NOT_FOUND) {
//                return str2.substring(start + before2.length(), end);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 截取指定字符串中间部分，不包括标识字符串<br>
//     *
//     * <p>栗子：
//     *
//     * <pre>
//     * StrUtil.subBetween(null, *)            = null
//     * StrUtil.subBetween("", "")             = ""
//     * StrUtil.subBetween("", "tag")          = null
//     * StrUtil.subBetween("tagabctag", null)  = null
//     * StrUtil.subBetween("tagabctag", "")    = ""
//     * StrUtil.subBetween("tagabctag", "tag") = "abc"
//     * </pre>
//     *
//     * @param str            被切割的字符串
//     * @param beforeAndAfter 截取开始和结束的字符串标识
//     * @return 截取后的字符串
//     */
//    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
//        return subBetween(str, beforeAndAfter, beforeAndAfter);
//    }
//
//    /**
//     * 截取指定字符串多段中间部分，不包括标识字符串<br>
//     *
//     * <p>栗子：
//     *
//     * <pre>
//     * StrUtil.subBetweenAll("wx[b]y[z]", "[", "]") 		= ["b","z"]
//     * StrUtil.subBetweenAll(null, *, *)          			= []
//     * StrUtil.subBetweenAll(*, null, *)          			= []
//     * StrUtil.subBetweenAll(*, *, null)          			= []
//     * StrUtil.subBetweenAll("", "", "")          			= []
//     * StrUtil.subBetweenAll("", "", "]")         			= []
//     * StrUtil.subBetweenAll("", "[", "]")        			= []
//     * StrUtil.subBetweenAll("yabcz", "", "")     			= []
//     * StrUtil.subBetweenAll("yabcz", "y", "z")   			= ["abc"]
//     * StrUtil.subBetweenAll("yabczyabcz", "y", "z")   		= ["abc","abc"]
//     * StrUtil.subBetweenAll("[yabc[zy]abcz]", "[", "]");   = ["zy"]           重叠时只截取内部，
//     * </pre>
//     *
//     * @param str    被切割的字符串
//     * @param prefix 截取开始的字符串标识
//     * @param suffix 截取到的字符串标识
//     * @return 截取后的字符串
//     * @author potatoxf
//     * @date 2021/9/26
//     */
//    public static String[] subBetweenAll(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        if (Whether.empty(str, prefix, suffix)
//                ||
//                // 不包含起始字符串，则肯定没有子串
//                false == contains(str, prefix)) {
//            return new String[0];
//        }
//
//        final List<String> result = new LinkedList<>();
//        final String[] split = splitToArray(str, prefix);
//        if (prefix.equals(suffix)) {
//            // 前后缀字符相同，单独处理
//            for (int i = 1, length = split.length - 1; i < length; i += 2) {
//                result.add(split[i]);
//            }
//        } else {
//            int suffixIndex;
//            String fragment;
//            for (int i = 1; i < split.length; i++) {
//                fragment = split[i];
//                suffixIndex = fragment.indexOf(suffix.toString());
//                if (suffixIndex > 0) {
//                    result.add(fragment.substring(0, suffixIndex));
//                }
//            }
//        }
//
//        return result.toArray(new String[0]);
//    }
//
//    /**
//     * 截取指定字符串多段中间部分，不包括标识字符串<br>
//     *
//     * <p>栗子：
//     *
//     * <pre>
//     * StrUtil.subBetweenAll(null, *)          			= []
//     * StrUtil.subBetweenAll(*, null)          			= []
//     * StrUtil.subBetweenAll(*, *)          			= []
//     * StrUtil.subBetweenAll("", "")          			= []
//     * StrUtil.subBetweenAll("", "#")         			= []
//     * StrUtil.subBetweenAll("gotanks", "")     		= []
//     * StrUtil.subBetweenAll("#gotanks#", "#")   		= ["gotanks"]
//     * StrUtil.subBetweenAll("#hello# #world#!", "#")   = ["hello", "world"]
//     * StrUtil.subBetweenAll("#hello# world#!", "#");   = ["hello"]
//     * </pre>
//     *
//     * @param str             被切割的字符串
//     * @param prefixAndSuffix 截取开始和结束的字符串标识
//     * @return 截取后的字符串
//     * @author potatoxf
//     * @date 2021/9/26
//     */
//    public static String[] subBetweenAll(CharSequence str, CharSequence prefixAndSuffix) {
//        return subBetweenAll(str, prefixAndSuffix, prefixAndSuffix);
//    }

//
//    // ------------------------------------------------------------------------ format
//
//    /**
//     * 格式化文本, {} 表示占位符<br>
//     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
//     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
//     * 例：<br>
//     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
//     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
//     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
//     *
//     * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
//     * @param params   参数值
//     * @return 格式化后的文本，如果模板为null，返回"null"
//     */
//    public static String format(CharSequence template, Object... params) {
//        if (null == template) {
//            return NULL;
//        }
//        if (Whether.empty(params) || Whether.blank(template)) {
//            return template.toString();
//        }
//        return StrFormatter.format(template.toString(), params);
//    }
//
//    /**
//     * 有序的格式化文本，使用{number}做为占位符<br>
//     * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
//     *
//     * @param pattern   文本格式
//     * @param arguments 参数
//     * @return 格式化后的文本
//     */
//    public static String indexedFormat(CharSequence pattern, Object... arguments) {
//        return MessageFormat.format(pattern.toString(), arguments);
//    }
//    // ------------------------------------------------------------------------ bytes
//
//    /**
//     * 编码字符串，编码为UTF-8
//     *
//     * @param str 字符串
//     * @return 编码后的字节码
//     */
//    public static byte[] utf8Bytes(CharSequence str) {
//        return bytes(str, CharsetUtil.CHARSET_UTF_8);
//    }
//
//    /**
//     * 编码字符串<br>
//     * 使用系统默认编码
//     *
//     * @param str 字符串
//     * @return 编码后的字节码
//     */
//    public static byte[] bytes(CharSequence str) {
//        return bytes(str, Charset.defaultCharset());
//    }
//
//    /**
//     * 编码字符串
//     *
//     * @param str     字符串
//     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
//     * @return 编码后的字节码
//     */
//    public static byte[] bytes(CharSequence str, String charset) {
//        return bytes(str, Whether.blank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
//    }
//
//    /**
//     * 编码字符串
//     *
//     * @param str     字符串
//     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
//     * @return 编码后的字节码
//     */
//    public static byte[] bytes(CharSequence str, Charset charset) {
//        if (str == null) {
//            return null;
//        }
//
//        if (null == charset) {
//            return str.toString().getBytes();
//        }
//        return str.toString().getBytes(charset);
//    }
//
//    /**
//     * 字符串转换为byteBuffer
//     *
//     * @param str     字符串
//     * @param charset 编码
//     * @return byteBuffer
//     */
//    public static ByteBuffer byteBuffer(CharSequence str, String charset) {
//        return ByteBuffer.wrap(bytes(str, charset));
//    }
//
//    // ------------------------------------------------------------------------ wrap
//
//    /**
//     * 包装指定字符串<br>
//     * 当前缀和后缀一致时使用此方法
//     *
//     * @param str             被包装的字符串
//     * @param prefixAndSuffix 前缀和后缀
//     * @return 包装后的字符串
//     */
//    public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
//        return wrap(str, prefixAndSuffix, prefixAndSuffix);
//    }
//
//    /**
//     * 包装指定字符串
//     *
//     * @param str    被包装的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 包装后的字符串
//     */
//    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        return Tools.safeValue(prefix).concat(Tools.safeValue(str)).concat(Tools.safeValue(suffix));
//    }
//
//    /**
//     * 使用单个字符包装多个字符串
//     *
//     * @param prefixAndSuffix 前缀和后缀
//     * @param strs            多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
//        return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
//    }
//
//    /**
//     * 包装多个字符串
//     *
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @param strs   多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
//        final String[] results = new String[strs.length];
//        for (int i = 0; i < strs.length; i++) {
//            results[i] = wrap(strs[i], prefix, suffix);
//        }
//        return results;
//    }
//
//    /**
//     * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
//     *
//     * @param str    被包装的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 包装后的字符串
//     */
//    public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        int len = 0;
//        if (Whether.noEmpty(str)) {
//            len += str.length();
//        }
//        if (Whether.noEmpty(prefix)) {
//            len += prefix.length();
//        }
//        if (Whether.noEmpty(suffix)) {
//            len += suffix.length();
//        }
//        StringBuilder sb = new StringBuilder(len);
//        if (Whether.noEmpty(prefix) && false == startWith(str, prefix)) {
//            sb.append(prefix);
//        }
//        if (Whether.noEmpty(str)) {
//            sb.append(str);
//        }
//        if (Whether.noEmpty(suffix) && false == endWith(str, suffix)) {
//            sb.append(suffix);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 使用成对的字符包装多个字符串，如果已经包装，则不再包装
//     *
//     * @param prefixAndSuffix 前缀和后缀
//     * @param strs            多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllWithPairIfMissing(
//            CharSequence prefixAndSuffix, CharSequence... strs) {
//        return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
//    }
//
//    /**
//     * 包装多个字符串，如果已经包装，则不再包装
//     *
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @param strs   多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllIfMissing(
//            CharSequence prefix, CharSequence suffix, CharSequence... strs) {
//        final String[] results = new String[strs.length];
//        for (int i = 0; i < strs.length; i++) {
//            results[i] = wrapIfMissing(strs[i], prefix, suffix);
//        }
//        return results;
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str    字符串
//     * @param prefix 前置字符串
//     * @param suffix 后置字符串
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, String prefix, String suffix) {
//        if (isWrap(str, prefix, suffix)) {
//            return sub(str, prefix.length(), str.length() - suffix.length());
//        }
//        return str.toString();
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str    字符串
//     * @param prefix 前置字符
//     * @param suffix 后置字符
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, char prefix, char suffix) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
//            return sub(str, 1, str.length() - 1);
//        }
//        return str.toString();
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str             字符串
//     * @param prefixAndSuffix 前置和后置字符
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, char prefixAndSuffix) {
//        return unWrap(str, prefixAndSuffix, prefixAndSuffix);
//    }
//
//    /**
//     * 指定字符串是否被包装
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, String prefix, String suffix) {
//        if (Whether.anyNvl(str, prefix, suffix)) {
//            return false;
//        }
//        final String str2 = str.toString();
//        return str2.startsWith(prefix) && str2.endsWith(suffix);
//    }
//
//    /**
//     * 指定字符串是否被同一字符包装（前后都有这些字符串）
//     *
//     * @param str     字符串
//     * @param wrapper 包装字符串
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, String wrapper) {
//        return isWrap(str, wrapper, wrapper);
//    }
//
//    /**
//     * 指定字符串是否被同一字符包装（前后都有这些字符串）
//     *
//     * @param str     字符串
//     * @param wrapper 包装字符
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, char wrapper) {
//        return isWrap(str, wrapper, wrapper);
//    }
//
//    /**
//     * 指定字符串是否被包装
//     *
//     * @param str        字符串
//     * @param prefixChar 前缀
//     * @param suffixChar 后缀
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
//        if (null == str) {
//            return false;
//        }
//
//        return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
//    }
//
//    // ------------------------------------------------------------------------ count
//
//    /**
//     * 统计指定内容中包含指定字符串的数量<br>
//     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
//     *
//     * <pre>
//     * StrUtil.count(null, *)       = 0
//     * StrUtil.count("", *)         = 0
//     * StrUtil.count("abba", null)  = 0
//     * StrUtil.count("abba", "")    = 0
//     * StrUtil.count("abba", "a")   = 2
//     * StrUtil.count("abba", "ab")  = 1
//     * StrUtil.count("abba", "xxx") = 0
//     * </pre>
//     *
//     * @param content      被查找的字符串
//     * @param strForSearch 需要查找的字符串
//     * @return 查找到的个数
//     */
//    public static int count(CharSequence content, CharSequence strForSearch) {
//        if (Whether.empty(content)
//                || Whether.empty(strForSearch)
//                || strForSearch.length() > content.length()) {
//            return 0;
//        }
//
//        int count = 0;
//        int idx = 0;
//        final String content2 = content.toString();
//        final String strForSearch2 = strForSearch.toString();
//        while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
//            count++;
//            idx += strForSearch.length();
//        }
//        return count;
//    }
//
//    /**
//     * 统计指定内容中包含指定字符的数量
//     *
//     * @param content       内容
//     * @param charForSearch 被统计的字符
//     * @return 包含数量
//     */
//    public static int count(CharSequence content, char charForSearch) {
//        int count = 0;
//        if (Whether.empty(content)) {
//            return 0;
//        }
//        int contentLength = content.length();
//        for (int i = 0; i < contentLength; i++) {
//            if (charForSearch == content.charAt(i)) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    // ------------------------------------------------------------------------ compare
//
//    /**
//     * 比较两个字符串，用于排序
//     *
//     * <pre>
//     * StrUtil.compare(null, null, *)     = 0
//     * StrUtil.compare(null , "a", true)  &lt; 0
//     * StrUtil.compare(null , "a", false) &gt; 0
//     * StrUtil.compare("a", null, true)   &gt; 0
//     * StrUtil.compare("a", null, false)  &lt; 0
//     * StrUtil.compare("abc", "abc", *)   = 0
//     * StrUtil.compare("a", "b", *)       &lt; 0
//     * StrUtil.compare("b", "a", *)       &gt; 0
//     * StrUtil.compare("a", "B", *)       &gt; 0
//     * StrUtil.compare("ab", "abc", *)    &lt; 0
//     * </pre>
//     *
//     * @param str1       字符串1
//     * @param str2       字符串2
//     * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
//     * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
//     */
//    public static int compare(
//            final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
//        if (str1 == str2) {
//            return 0;
//        }
//        if (str1 == null) {
//            return nullIsLess ? -1 : 1;
//        }
//        if (str2 == null) {
//            return nullIsLess ? 1 : -1;
//        }
//        return str1.toString().compareTo(str2.toString());
//    }
//
//    /**
//     * 比较两个字符串，用于排序，大小写不敏感
//     *
//     * <pre>
//     * StrUtil.compareIgnoreCase(null, null, *)     = 0
//     * StrUtil.compareIgnoreCase(null , "a", true)  &lt; 0
//     * StrUtil.compareIgnoreCase(null , "a", false) &gt; 0
//     * StrUtil.compareIgnoreCase("a", null, true)   &gt; 0
//     * StrUtil.compareIgnoreCase("a", null, false)  &lt; 0
//     * StrUtil.compareIgnoreCase("abc", "abc", *)   = 0
//     * StrUtil.compareIgnoreCase("abc", "ABC", *)   = 0
//     * StrUtil.compareIgnoreCase("a", "b", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("b", "a", *)       &gt; 0
//     * StrUtil.compareIgnoreCase("a", "B", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("A", "b", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("ab", "abc", *)    &lt; 0
//     * </pre>
//     *
//     * @param str1       字符串1
//     * @param str2       字符串2
//     * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
//     * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
//     */
//    public static int compareIgnoreCase(CharSequence str1, CharSequence str2, boolean nullIsLess) {
//        if (str1 == str2) {
//            return 0;
//        }
//        if (str1 == null) {
//            return nullIsLess ? -1 : 1;
//        }
//        if (str2 == null) {
//            return nullIsLess ? 1 : -1;
//        }
//        return str1.toString().compareToIgnoreCase(str2.toString());
//    }
//
//    /**
//     * 比较两个版本<br>
//     * null版本排在最小：即：
//     *
//     * <pre>
//     * StrUtil.compareVersion(null, "v1") &lt; 0
//     * StrUtil.compareVersion("v1", "v1")  = 0
//     * StrUtil.compareVersion(null, null)   = 0
//     * StrUtil.compareVersion("v1", null) &gt; 0
//     * StrUtil.compareVersion("1.0.0", "1.0.2") &lt; 0
//     * StrUtil.compareVersion("1.0.2", "1.0.2a") &lt; 0
//     * StrUtil.compareVersion("1.13.0", "1.12.1c") &gt; 0
//     * StrUtil.compareVersion("V0.0.20170102", "V0.0.20170101") &gt; 0
//     * </pre>
//     *
//     * @param version1 版本1
//     * @param version2 版本2
//     * @return 排序值。负数：version1 &lt; version2，正数：version1 &gt; version2, 0：version1 == version2
//     */
//    public static int compareVersion(CharSequence version1, CharSequence version2) {
//        return VersionComparator.INSTANCE.compare(str(version1), str(version2));
//    }
//
//    // ------------------------------------------------------------------------ append and prepend
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
//     * 不忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param suffix   需要添加到结尾的字符串
//     * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissing(
//            CharSequence str, CharSequence suffix, CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, false, suffixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
//     * 忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param suffix   需要添加到结尾的字符串
//     * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissingIgnoreCase(
//
//            CharSequence str, CharSequence suffix, CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, true, suffixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
//     *
//     * @param str          被检查的字符串
//     * @param suffix       需要添加到结尾的字符串，不参与检查匹配
//     * @param ignoreCase   检查结尾时是否忽略大小写
//     * @param testSuffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissing(
//            CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... testSuffixes) {
//        if (str == null || Whether.empty(suffix) || endWith(str, suffix, ignoreCase)) {
//            return str(str);
//        }
//        if (Whether.noEmpty(testSuffixes)) {
//            for (final CharSequence testSuffix : testSuffixes) {
//                if (endWith(str, testSuffix, ignoreCase)) {
//                    return str.toString();
//                }
//            }
//        }
//        return str.toString().concat(suffix.toString());
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
//     * 不忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param prefix   需要添加到首部的字符串
//     * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissing(
//            CharSequence str, CharSequence prefix, CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, false, prefixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
//     * 忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param prefix   需要添加到首部的字符串
//     * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissingIgnoreCase(
//            CharSequence str, CharSequence prefix, CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, true, prefixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串
//     *
//     * @param str        被检查的字符串
//     * @param prefix     需要添加到首部的字符串
//     * @param ignoreCase 检查结尾时是否忽略大小写
//     * @param prefixes   需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissing(
//            CharSequence str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
//        if (str == null || Whether.empty(prefix) || startWith(str, prefix, ignoreCase)) {
//            return str(str);
//        }
//        if (prefixes != null && prefixes.length > 0) {
//            for (final CharSequence s : prefixes) {
//                if (startWith(str, s, ignoreCase)) {
//                    return str.toString();
//                }
//            }
//        }
//        return prefix.toString().concat(str.toString());
//    }
//
//    // ------------------------------------------------------------------------ replace
//
//    /**
//     * 替换字符串中的指定字符串，忽略大小写
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replaceIgnoreCase(
//            CharSequence str, CharSequence searchStr, CharSequence replacement) {
//        return replace(str, 0, searchStr, replacement, true);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
//        return replace(str, 0, searchStr, replacement, false);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @param ignoreCase  是否忽略大小写
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
//        return replace(str, 0, searchStr, replacement, ignoreCase);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param fromIndex   开始位置（包括）
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @param ignoreCase  是否忽略大小写
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str,
//            int fromIndex,
//            CharSequence searchStr,
//            CharSequence replacement,
//            boolean ignoreCase) {
//        if (Whether.empty(str) || Whether.empty(searchStr)) {
//            return str(str);
//        }
//        if (null == replacement) {
//            replacement = EMPTY;
//        }
//
//        final int strLength = str.length();
//        final int searchStrLength = searchStr.length();
//        if (strLength < searchStrLength) {
//            // issue#I4M16G@Gitee
//            return str(str);
//        }
//
//        if (fromIndex > strLength) {
//            return str(str);
//        } else if (fromIndex < 0) {
//            fromIndex = 0;
//        }
//
//        final StringBuilder result =
//                new StringBuilder(strLength - searchStrLength + replacement.length());
//        if (0 != fromIndex) {
//            result.append(str.subSequence(0, fromIndex));
//        }
//
//        int preIndex = fromIndex;
//        int index;
//        while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
//            result.append(str.subSequence(preIndex, index));
//            result.append(replacement);
//            preIndex = index + searchStrLength;
//        }
//
//        if (preIndex < strLength) {
//            // 结尾部分
//            result.append(str.subSequence(preIndex, strLength));
//        }
//        return result.toString();
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为固定字符<br>
//     * 此方法使用{@link String#codePoints()}完成拆分替换
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @param replacedChar 被替换的字符
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, int startInclude, int endExclude, char replacedChar) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        final String originalStr = str(str);
//        int[] strCodePoints = originalStr.codePoints().toArray();
//        final int strLength = strCodePoints.length;
//        if (startInclude > strLength) {
//            return originalStr;
//        }
//        if (endExclude > strLength) {
//            endExclude = strLength;
//        }
//        if (startInclude > endExclude) {
//            // 如果起始位置大于结束位置，不替换
//            return originalStr;
//        }
//
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < strLength; i++) {
//            if (i >= startInclude && i < endExclude) {
//                stringBuilder.append(replacedChar);
//            } else {
//                stringBuilder.append(new String(strCodePoints, i, 1));
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为指定字符串，字符串只重复一次<br>
//     * 此方法使用{@link String#codePoints()}完成拆分替换
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @param replacedStr  被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, int startInclude, int endExclude, CharSequence replacedStr) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        final String originalStr = str(str);
//        int[] strCodePoints = originalStr.codePoints().toArray();
//        final int strLength = strCodePoints.length;
//        if (startInclude > strLength) {
//            return originalStr;
//        }
//        if (endExclude > strLength) {
//            endExclude = strLength;
//        }
//        if (startInclude > endExclude) {
//            // 如果起始位置大于结束位置，不替换
//            return originalStr;
//        }
//
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < startInclude; i++) {
//            stringBuilder.append(new String(strCodePoints, i, 1));
//        }
//        stringBuilder.append(replacedStr);
//        for (int i = endExclude; i < strLength; i++) {
//            stringBuilder.append(new String(strCodePoints, i, 1));
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换<br>
//     * replaceFun可以通过{@link Matcher}提取出匹配到的内容的不同部分，然后经过重新处理、组装变成新的内容放回原位。
//     *
//     * <pre class="code">
//     *     replace(this.content, "(\\d+)", parameters -&gt; "-" + parameters.group(1) + "-")
//     *     // 结果为："ZZZaaabbbccc中文-1234-"
//     * </pre>
//     *
//     * @param str        要替换的字符串
//     * @param pattern    用于匹配的正则式
//     * @param replaceFun 决定如何替换的函数
//     * @return 替换后的字符串
//     * @see ReUtil#replaceAll(CharSequence, java.util.regex.Pattern, Func1)
//     */
//    public static String replace(
//            CharSequence str,
//            java.util.regex.Pattern pattern,
//            Func1<java.util.regex.Matcher, String> replaceFun) {
//        return ReUtil.replaceAll(str, pattern, replaceFun);
//    }
//
//    /**
//     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换
//     *
//     * @param str        要替换的字符串
//     * @param regex      用于匹配的正则式
//     * @param replaceFun 决定如何替换的函数
//     * @return 替换后的字符串
//     * @see ReUtil#replaceAll(CharSequence, String, Func1)
//     */
//    public static String replace(
//            CharSequence str, String regex, Func1<java.util.regex.Matcher, String> replaceFun) {
//        return ReUtil.replaceAll(str, regex, replaceFun);
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为"*" 俗称：脱敏功能，后面其他功能，可以见：DesensitizedUtil(脱敏工具类)
//     *
//     * <pre>
//     * StrUtil.hide(null,*,*)=null
//     * StrUtil.hide("",0,*)=""
//     * StrUtil.hide("jackduan@163.com",-1,4)   ****duan@163.com
//     * StrUtil.hide("jackduan@163.com",2,3)    ja*kduan@163.com
//     * StrUtil.hide("jackduan@163.com",3,2)    jackduan@163.com
//     * StrUtil.hide("jackduan@163.com",16,16)  jackduan@163.com
//     * StrUtil.hide("jackduan@163.com",16,17)  jackduan@163.com
//     * </pre>
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @return 替换后的字符串
//     */
//    public static String hide(CharSequence str, int startInclude, int endExclude) {
//        return replace(str, startInclude, endExclude, '*');
//    }
//
//    /**
//     * 脱敏，使用默认的脱敏策略
//     *
//     * <pre>
//     * StrUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID)) =  "0"
//     * StrUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME)) = "段**"
//     * StrUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD)) = "5***************1X"
//     * StrUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE)) = "0915*****79"
//     * StrUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE)) = "180****1999"
//     * StrUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS)) = "北京市海淀区马********"
//     * StrUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL)) = "d*************@gmail.com.cn"
//     * StrUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD)) = "**********"
//     * StrUtil.desensitized("苏D40000", DesensitizedUtil.DesensitizedType.CAR_LICENSE)) = "苏D4***0"
//     * StrUtil.desensitized("11011111222233333256", DesensitizedType.BANK_CARD)) = "1101 **** **** **** 3256"
//     * </pre>
//     *
//     * @param str              字符串
//     * @param desensitizedType 脱敏类型;可以脱敏：用户id、中文名、身份证号、座机号、手机号、地址、电子邮件、密码
//     * @return 脱敏之后的字符串
//     * @author potatoxf
//     * @date 2021/9/26
//     * @see DesensitizedUtil 如果需要自定义，脱敏规则，请使用该工具类；
//     */
//    public static String desensitized(
//            CharSequence str, DesensitizedUtil.DesensitizedType desensitizedType) {
//        return DesensitizedUtil.desensitized(str, desensitizedType);
//    }
//
//    /**
//     * 替换字符字符数组中所有的字符为replacedStr<br>
//     * 提供的chars为所有需要被替换的字符，例如："\r\n"，则"\r"和"\n"都会被替换，哪怕他们单独存在
//     *
//     * @param str         被检查的字符串
//     * @param chars       需要替换的字符列表，用一个字符串表示这个字符列表
//     * @param replacedStr 替换成的字符串
//     * @return 新字符串
//     */
//    public static String replaceChars(CharSequence str, String chars, CharSequence replacedStr) {
//        if (Whether.empty(str) || Whether.empty(chars)) {
//            return str(str);
//        }
//        return replaceChars(str, chars.toCharArray(), replacedStr);
//    }
//
//    /**
//     * 替换字符字符数组中所有的字符为replacedStr
//     *
//     * @param str         被检查的字符串
//     * @param chars       需要替换的字符列表
//     * @param replacedStr 替换成的字符串
//     * @return 新字符串
//     */
//    public static String replaceChars(CharSequence str, char[] chars, CharSequence replacedStr) {
//        if (Whether.empty(str) || Whether.empty(chars)) {
//            return str(str);
//        }
//
//        final Set<Character> set = new HashSet<>(chars.length);
//        for (char c : chars) {
//            set.add(c);
//        }
//        int strLen = str.length();
//        final StringBuilder builder = new StringBuilder();
//        char c;
//        for (int i = 0; i < strLen; i++) {
//            c = str.charAt(i);
//            builder.append(set.contains(c) ? replacedStr : c);
//        }
//        return builder.toString();
//    }
//
//    // ------------------------------------------------------------------------ length
//
//    /**
//     * 获取字符串的长度，如果为null返回0
//     *
//     * @param cs a 字符串
//     * @return 字符串的长度，如果为null返回0
//     */
//    public static int length(CharSequence cs) {
//        return cs == null ? 0 : cs.length();
//    }
//
//    /**
//     * 给定字符串转为bytes后的byte数（byte长度）
//     *
//     * @param cs      字符串
//     * @param charset 编码
//     * @return byte长度
//     */
//    public static int byteLength(CharSequence cs, Charset charset) {
//        return cs == null ? 0 : cs.toString().getBytes(charset).length;
//    }
//
//    /**
//     * 给定字符串数组的总长度<br>
//     * null字符长度定义为0
//     *
//     * @param strs 字符串数组
//     * @return 总长度
//     */
//    public static int totalLength(CharSequence... strs) {
//        int totalLength = 0;
//        for (CharSequence str : strs) {
//            totalLength += (null == str ? 0 : str.length());
//        }
//        return totalLength;
//    }
//
//    /**
//     * 限制字符串长度，如果超过指定长度，截取指定长度并在末尾加"..."
//     *
//     * @param string 字符串
//     * @param length 最大长度
//     * @return 切割后的剩余的前半部分字符串+"..."
//     */
//    public static String maxLength(CharSequence string, int length) {
//        Assert.isTrue(length > 0);
//        if (null == string) {
//            return null;
//        }
//        if (string.length() <= length) {
//            return string.toString();
//        }
//        return sub(string, 0, length) + "...";
//    }
//
//    // ------------------------------------------------------------------------ firstXXX
//
//    /**
//     * 返回第一个非{@code null} 元素
//     *
//     * @param strs 多个元素
//     * @param <T>  元素类型
//     * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
//     */
//    @SuppressWarnings("unchecked")
//    public static <T extends CharSequence> T firstNonNull(T... strs) {
//        return HelpArray.firstNonNull(strs);
//    }
//
//    /**
//     * 返回第一个非empty 元素
//     *
//     * @param strs 多个元素
//     * @param <T>  元素类型
//     * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
//     */
//    @SuppressWarnings("unchecked")
//    public static <T extends CharSequence> T firstNonEmpty(T... strs) {
//        return HelpArray.firstMatch(Whether::noEmpty, strs);
//    }
//
//    /**
//     * 返回第一个非blank 元素
//     *
//     * @param strs 多个元素
//     * @param <T>  元素类型
//     * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
//     */
//    @SuppressWarnings("unchecked")
//    public static <T extends CharSequence> T firstNonBlank(T... strs) {
//        return HelpArray.firstMatch(Whether::noBlank, strs);
//    }
//
//    // ------------------------------------------------------------------------ lower and upper
//
//    /**
//     * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get =》 return getName
//     *
//     * @param str       被处理的字符串
//     * @param preString 添加的首部
//     * @return 处理后的字符串
//     */
//    public static String upperFirstAndAddPre(CharSequence str, String preString) {
//        if (str == null || preString == null) {
//            return null;
//        }
//        return preString + upperFirst(str);
//    }
//
//    /**
//     * 大写首字母<br>
//     * 例如：str = name, return Name
//     *
//     * @param str 字符串
//     * @return 字符串
//     */
//    public static String upperFirst(CharSequence str) {
//        if (null == str) {
//            return null;
//        }
//        if (str.length() > 0) {
//            char firstChar = str.charAt(0);
//            if (Character.isLowerCase(firstChar)) {
//                return Character.toUpperCase(firstChar) + subSuf(str, 1);
//            }
//        }
//        return str.toString();
//    }
//
//    /**
//     * 小写首字母<br>
//     * 例如：str = Name, return name
//     *
//     * @param str 字符串
//     * @return 字符串
//     */
//    public static String lowerFirst(CharSequence str) {
//        if (null == str) {
//            return null;
//        }
//        if (str.length() > 0) {
//            char firstChar = str.charAt(0);
//            if (Character.isUpperCase(firstChar)) {
//                return Character.toLowerCase(firstChar) + subSuf(str, 1);
//            }
//        }
//        return str.toString();
//    }
//
//    // ------------------------------------------------------------------------ filter
//
//    /**
//     * 过滤字符串
//     *
//     * @param str    字符串
//     * @param filter 过滤器，{@link Filter#accept(Object)}返回为{@code true}的保留字符
//     * @return 过滤后的字符串
//     */
//    public static String filter(CharSequence str, final Filter<Character> filter) {
//        if (str == null || filter == null) {
//            return str(str);
//        }
//
//        int len = str.length();
//        final StringBuilder sb = new StringBuilder(len);
//        char c;
//        for (int i = 0; i < len; i++) {
//            c = str.charAt(i);
//            if (filter.accept(c)) {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//
//    // ------------------------------------------------------------------------ case
//
//    /**
//     * 给定字符串中的字母是否全部为大写，判断依据如下：
//     *
//     * <pre>
//     * 1. 大写字母包括A-Z
//     * 2. 其它非字母的Unicode符都算作大写
//     * </pre>
//     *
//     * @param str 被检查的字符串
//     * @return 是否全部为大写
//     */
//    public static boolean isUpperCase(CharSequence str) {
//        if (null == str) {
//            return false;
//        }
//        final int len = str.length();
//        for (int i = 0; i < len; i++) {
//            if (Character.isLowerCase(str.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 给定字符串中的字母是否全部为小写，判断依据如下：
//     *
//     * <pre>
//     * 1. 小写字母包括a-z
//     * 2. 其它非字母的Unicode符都算作小写
//     * </pre>
//     *
//     * @param str 被检查的字符串
//     * @return 是否全部为小写
//     */
//    public static boolean isLowerCase(CharSequence str) {
//        if (null == str) {
//            return false;
//        }
//        final int len = str.length();
//        for (int i = 0; i < len; i++) {
//            if (Character.isUpperCase(str.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 切换给定字符串中的大小写。大写转小写，小写转大写。
//     *
//     * <pre>
//     * StrUtil.swapCase(null)                 = null
//     * StrUtil.swapCase("")                   = ""
//     * StrUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 交换后的字符串
//     */
//    public static String swapCase(final String str) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//
//        final char[] buffer = str.toCharArray();
//
//        for (int i = 0; i < buffer.length; i++) {
//            final char ch = buffer[i];
//            if (Character.isUpperCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isTitleCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isLowerCase(ch)) {
//                buffer[i] = Character.toUpperCase(ch);
//            }
//        }
//        return new String(buffer);
//    }
//
//    /**
//     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
//     * 例如：
//     *
//     * <pre>
//     * HelloWorld=》hello_world
//     * Hello_World=》hello_world
//     * HelloWorld_test=》hello_world_test
//     * </pre>
//     *
//     * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
//     * @return 转换后下划线方式命名的字符串
//     * @see NamingCase#toUnderlineCase(CharSequence)
//     */
//    public static String toUnderlineCase(CharSequence str) {
//        return NamingCase.toUnderlineCase(str);
//    }
//
//    /**
//     * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
//     *
//     * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
//     * @param symbol 连接符
//     * @return 转换后符号连接方式命名的字符串
//     * @see NamingCase#toSymbolCase(CharSequence, char)
//     */
//    public static String toSymbolCase(CharSequence str, char symbol) {
//        return NamingCase.toSymbolCase(str, symbol);
//    }
//
//    /**
//     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
//     * 例如：hello_world=》helloWorld
//     *
//     * @param name 转换前的下划线大写方式命名的字符串
//     * @return 转换后的驼峰式命名的字符串
//     * @see NamingCase#toCamelCase(CharSequence)
//     */
//    public static String toCamelCase(CharSequence name) {
//        return NamingCase.toCamelCase(name);
//    }
//
//    /**
//     * 将连接符方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
//     * 例如：hello_world=》helloWorld; hello-world=》helloWorld
//     *
//     * @param name   转换前的下划线大写方式命名的字符串
//     * @param symbol 连接符
//     * @return 转换后的驼峰式命名的字符串
//     * @see NamingCase#toCamelCase(CharSequence, char)
//     */
//    public static String toCamelCase(CharSequence name, char symbol) {
//        return NamingCase.toCamelCase(name, symbol);
//    }
//
//    // ------------------------------------------------------------------------ isSurround
//
//    /**
//     * 给定字符串是否被字符包围
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 是否包围，空串不包围
//     */
//    public static boolean isSurround(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        if (Whether.blank(str)) {
//            return false;
//        }
//        if (str.length() < (prefix.length() + suffix.length())) {
//            return false;
//        }
//
//        final String str2 = str.toString();
//        return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
//    }
//
//    /**
//     * 给定字符串是否被字符包围
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 是否包围，空串不包围
//     */
//    public static boolean isSurround(CharSequence str, char prefix, char suffix) {
//        if (Whether.blank(str)) {
//            return false;
//        }
//        if (str.length() < 2) {
//            return false;
//        }
//
//        return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
//    }
//
//    // ------------------------------------------------------------------------ builder
//
//    /**
//     * 创建StringBuilder对象
//     *
//     * @param strs 初始字符串列表
//     * @return StringBuilder对象
//     */
//    public static StringBuilder builder(CharSequence... strs) {
//        final StringBuilder sb = new StringBuilder();
//        for (CharSequence str : strs) {
//            sb.append(str);
//        }
//        return sb;
//    }
//
//    // ------------------------------------------------------------------------ getter and setter
//
//    /**
//     * 获得set或get或is方法对应的标准属性名<br>
//     * 例如：setName 返回 name
//     *
//     * <pre>
//     * getName =》name
//     * setName =》name
//     * isName  =》name
//     * </pre>
//     *
//     * @param getOrSetMethodName Get或Set方法名
//     * @return 如果是set或get方法名，返回field， 否则null
//     */
//    public static String getGeneralField(CharSequence getOrSetMethodName) {
//        final String getOrSetMethodNameStr = getOrSetMethodName.toString();
//        if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
//            return removePreAndLowerFirst(getOrSetMethodName, 3);
//        } else if (getOrSetMethodNameStr.startsWith("is")) {
//            return removePreAndLowerFirst(getOrSetMethodName, 2);
//        }
//        return null;
//    }
//
//    /**
//     * 生成set方法名<br>
//     * 例如：name 返回 setName
//     *
//     * @param fieldName 属性名
//     * @return setXxx
//     */
//    public static String genSetter(CharSequence fieldName) {
//        return upperFirstAndAddPre(fieldName, "set");
//    }
//
//    /**
//     * 生成get方法名
//     *
//     * @param fieldName 属性名
//     * @return getXxx
//     */
//    public static String genGetter(CharSequence fieldName) {
//        return upperFirstAndAddPre(fieldName, "get");
//    }
//
//    // ------------------------------------------------------------------------ other
//
//    /**
//     * 连接多个字符串为一个
//     *
//     * @param isNullToEmpty 是否null转为""
//     * @param strs          字符串数组
//     * @return 连接后的字符串
//     */
//    public static String concat(boolean isNullToEmpty, CharSequence... strs) {
//        final StringBuilder sb = new StringBuilder();
//        for (CharSequence str : strs) {
//            sb.append(isNullToEmpty ? Tools.safeValue(str) : str);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 将给定字符串，变成 "xxx...xxx" 形式的字符串
//     *
//     * <ul>
//     *   <li>abcdefgh 9 -》 abcdefgh
//     *   <li>abcdefgh 8 -》 abcdefgh
//     *   <li>abcdefgh 7 -》 ab...gh
//     *   <li>abcdefgh 6 -》 ab...h
//     *   <li>abcdefgh 5 -》 a...h
//     *   <li>abcdefgh 4 -》 a..h
//     *   <li>abcdefgh 3 -》 a.h
//     *   <li>abcdefgh 2 -》 a.
//     *   <li>abcdefgh 1 -》 a
//     *   <li>abcdefgh 0 -》 abcdefgh
//     *   <li>abcdefgh -1 -》 abcdefgh
//     * </ul>
//     *
//     * @param str       字符串
//     * @param maxLength 结果的最大长度
//     * @return 截取后的字符串
//     */
//    public static String brief(CharSequence str, int maxLength) {
//        if (null == str) {
//            return null;
//        }
//        final int strLength = str.length();
//        if (maxLength <= 0 || strLength <= maxLength) {
//            return str.toString();
//        }
//
//        // since 5.7.5，特殊长度
//        switch (maxLength) {
//            case 1:
//                return String.valueOf(str.charAt(0));
//            case 2:
//                return str.charAt(0) + ".";
//            case 3:
//                return str.charAt(0) + "." + str.charAt(strLength - 1);
//            case 4:
//                return str.charAt(0) + ".." + str.charAt(strLength - 1);
//        }
//
//        final int suffixLength = (maxLength - 3) / 2;
//        final int preLength = suffixLength + (maxLength - 3) % 2; // suffixLength 或 suffixLength + 1
//        final String str2 = str.toString();
//        return format(
//                "{}...{}", str2.substring(0, preLength), str2.substring(strLength - suffixLength));
//    }
//
//    /**
//     * 以 conjunction 为分隔符将多个对象转换为字符串
//     *
//     * @param conjunction 分隔符 {@link HelpString#COMMA}
//     * @param objs        数组
//     * @return 连接后的字符串
//     */
//    public static String join(CharSequence conjunction, Object... objs) {
//        return HelpArray.join(objs, conjunction);
//    }
//
//    /**
//     * 以 conjunction 为分隔符将多个对象转换为字符串
//     *
//     * @param <T>         元素类型
//     * @param conjunction 分隔符 {@link HelpString#COMMA}
//     * @param iterable    集合
//     * @return 连接后的字符串
//     * @see HelpCollection#join(Iterable, CharSequence)
//     */
//    public static <T> String join(CharSequence conjunction, Iterable<T> iterable) {
//        return HelpCollection.join(iterable, conjunction);
//    }
//
//    /**
//     * 字符串的每一个字符是否都与定义的匹配器匹配
//     *
//     * @param value   字符串
//     * @param matcher 匹配器
//     * @return 是否全部匹配
//     */
//    public static boolean isAllCharMatch(CharSequence value, Matcher<Character> matcher) {
//        if (Whether.blank(value)) {
//            return false;
//        }
//        for (int i = value.length(); --i >= 0; ) {
//            if (false == matcher.match(value.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 检查字符串是否都为数字组成
//     *
//     * @param str 字符串
//     * @return 是否都为数字组成
//     */
//    public static boolean isNumeric(CharSequence str) {
//        return isAllCharMatch(str, Character::isDigit);
//    }
//
//    /**
//     * 循环位移指定位置的字符串为指定距离<br>
//     * 当moveLength大于0向右位移，小于0向左位移，0不位移<br>
//     * 当moveLength大于字符串长度时采取循环位移策略，即位移到头后从头（尾）位移，例如长度为10，位移13则表示位移3
//     *
//     * @param str          字符串
//     * @param startInclude 起始位置（包括）
//     * @param endExclude   结束位置（不包括）
//     * @param moveLength   移动距离，负数表示左移，正数为右移
//     * @return 位移后的字符串
//     */
//    public static String move(CharSequence str, int startInclude, int endExclude, int moveLength) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        int len = str.length();
//        if (Math.abs(moveLength) > len) {
//            // 循环位移，当越界时循环
//            moveLength = moveLength % len;
//        }
//        final StringBuilder StringBuilder = new StringBuilder(len);
//        if (moveLength > 0) {
//            int endAfterMove = Math.min(endExclude + moveLength, str.length());
//            StringBuilder.append(str.subSequence(0, startInclude)) //
//                    .append(str.subSequence(endExclude, endAfterMove)) //
//                    .append(str.subSequence(startInclude, endExclude)) //
//                    .append(str.subSequence(endAfterMove, str.length()));
//        } else if (moveLength < 0) {
//            int startAfterMove = Math.max(startInclude + moveLength, 0);
//            StringBuilder.append(str.subSequence(0, startAfterMove)) //
//                    .append(str.subSequence(startInclude, endExclude)) //
//                    .append(str.subSequence(startAfterMove, startInclude)) //
//                    .append(str.subSequence(endExclude, str.length()));
//        } else {
//            return str(str);
//        }
//        return StringBuilder.toString();
//    }
//
//    /**
//     * 检查给定字符串的所有字符是否都一样
//     *
//     * @param str 字符出啊
//     * @return 给定字符串的所有字符是否都一样
//     */
//    public static boolean isCharEquals(CharSequence str) {
//        Assert.notEmpty(str, "Str to check must be not empty!");
//        return count(str, str.charAt(0)) == str.length();
//    }
//
//    /**
//     * 对字符串归一化处理，如 "Á" 可以使用 "u00C1"或 "u0041u0301"表示，实际测试中两个字符串并不equals<br>
//     * 因此使用此方法归一为一种表示形式，默认按照W3C通常建议的，在NFC中交换文本。
//     *
//     * @param str 归一化的字符串
//     * @return 归一化后的字符串
//     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
//     */
//    public static String normalize(CharSequence str) {
//        return Normalizer.normalize(str, Normalizer.Form.NFC);
//    }
//
//    /**
//     * 在给定字符串末尾填充指定字符，以达到给定长度<br>
//     * 如果字符串本身的长度大于等于length，返回原字符串
//     *
//     * @param str       字符串
//     * @param fixedChar 补充的字符
//     * @param length    补充到的长度
//     * @return 补充后的字符串
//     */
//    public static String fixLength(CharSequence str, char fixedChar, int length) {
//        final int fixedLength = length - str.length();
//        if (fixedLength <= 0) {
//            return str.toString();
//        }
//        return str + repeat(fixedChar, fixedLength);
//    }
//
//    /**
//     * 字符串切分器，封装统一的字符串分割静态方法
//     *
//     * @author potatoxf
//     * @date 2021/9/26
//     */
//    public class StrSplitter {
//
//        // ----------------------------------------------------------------------------------------------
//        // Split by char
//
//        /**
//         * 切分字符串路径，仅支持Unix分界符：/
//         *
//         * @param str 被切分的字符串
//         * @return 切分后的集合
//         */
//        public static List<String> splitPath(CharSequence str) {
//            return splitPath(str, 0);
//        }
//
//        /**
//         * 切分字符串路径，仅支持Unix分界符：/
//         *
//         * @param str 被切分的字符串
//         * @return 切分后的集合
//         */
//        public static String[] splitPathToArray(CharSequence str) {
//            return toArray(splitPath(str));
//        }
//
//        /**
//         * 切分字符串路径，仅支持Unix分界符：/
//         *
//         * @param str   被切分的字符串
//         * @param limit 限制分片数
//         * @return 切分后的集合
//         */
//        public static List<String> splitPath(CharSequence str, int limit) {
//            return split(str, HelpString.C_SLASH, limit, true, true);
//        }
//
//        /**
//         * 切分字符串路径，仅支持Unix分界符：/
//         *
//         * @param str   被切分的字符串
//         * @param limit 限制分片数
//         * @return 切分后的集合
//         */
//        public static String[] splitPathToArray(CharSequence str, int limit) {
//            return toArray(splitPath(str, limit));
//        }
//
//        /**
//         * 切分字符串
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitTrim(CharSequence str, char separator, boolean ignoreEmpty) {
//            return split(str, separator, 0, true, ignoreEmpty);
//        }
//
//        /**
//         * 切分字符串
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
//            return split(str, separator, 0, isTrim, ignoreEmpty);
//        }
//
//        /**
//         * 切分字符串，大小写敏感，去除每个元素两边空白符
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitTrim(
//                CharSequence str, char separator, int limit, boolean ignoreEmpty) {
//            return split(str, separator, limit, true, ignoreEmpty, false);
//        }
//
//        /**
//         * 切分字符串，大小写敏感
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return split(str, separator, limit, isTrim, ignoreEmpty, false);
//        }
//
//        /**
//         * 切分字符串，大小写敏感
//         *
//         * @param <R>         切分后的元素类型
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param ignoreEmpty 是否忽略空串
//         * @param mapping     切分后的字符串元素的转换方法
//         * @return 切分后的集合，元素类型是经过 mapping 转换后的
//         */
//        public static <R> List<R> split(
//                CharSequence str,
//                char separator,
//                int limit,
//                boolean ignoreEmpty,
//                Function<String, R> mapping) {
//            return split(str, separator, limit, ignoreEmpty, false, mapping);
//        }
//
//        /**
//         * 切分字符串，忽略大小写
//         *
//         * @param text        被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitIgnoreCase(
//                CharSequence text, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return split(text, separator, limit, isTrim, ignoreEmpty, true);
//        }
//
//        /**
//         * 切分字符串
//         *
//         * @param text        被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @param ignoreCase  是否忽略大小写
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence text,
//                char separator,
//                int limit,
//                boolean isTrim,
//                boolean ignoreEmpty,
//                boolean ignoreCase) {
//            return split(text, separator, limit, ignoreEmpty, ignoreCase, trimFunc(isTrim));
//        }
//
//        /**
//         * 切分字符串<br>
//         * 如果为空字符串或者null 则返回空集合
//         *
//         * @param <R>         切分后的元素类型
//         * @param text        被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，-1不限制
//         * @param ignoreEmpty 是否忽略空串
//         * @param ignoreCase  是否忽略大小写
//         * @param mapping     切分后的字符串元素的转换方法
//         * @return 切分后的集合，元素类型是经过 mapping 转换后的
//         */
//        public static <R> List<R> split(
//                CharSequence text,
//                char separator,
//                int limit,
//                boolean ignoreEmpty,
//                boolean ignoreCase,
//                Function<String, R> mapping) {
//            if (null == text) {
//                return new ArrayList<>(0);
//            }
//            final SplitIter splitIter =
//                    new SplitIter(text, new CharFinder(separator, ignoreCase), limit, ignoreEmpty);
//            return splitIter.toList(mapping);
//        }
//
//        /**
//         * 切分字符串为字符串数组
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static String[] splitToArray(
//                CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
//        }
//
//        // ----------------------------------------------------------------------------------------------
//        // Split by String
//
//        /**
//         * 切分字符串，不忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence str, String separator, boolean isTrim, boolean ignoreEmpty) {
//            return split(str, separator, -1, isTrim, ignoreEmpty, false);
//        }
//
//        /**
//         * 切分字符串，去除每个元素两边空格，忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitTrim(CharSequence str, String separator, boolean ignoreEmpty) {
//            return split(str, separator, true, ignoreEmpty);
//        }
//
//        /**
//         * 切分字符串，不忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param limit       限制分片数，小于等于0表示无限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return split(str, separator, limit, isTrim, ignoreEmpty, false);
//        }
//
//        /**
//         * 切分字符串，去除每个元素两边空格，忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param limit       限制分片数
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitTrim(
//                CharSequence str, String separator, int limit, boolean ignoreEmpty) {
//            return split(str, separator, limit, true, ignoreEmpty);
//        }
//
//        /**
//         * 切分字符串，忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param limit       限制分片数
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitIgnoreCase(
//                CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return split(str, separator, limit, isTrim, ignoreEmpty, true);
//        }
//
//        /**
//         * 切分字符串，去除每个元素两边空格，忽略大小写
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符串
//         * @param limit       限制分片数
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitTrimIgnoreCase(
//                CharSequence str, String separator, int limit, boolean ignoreEmpty) {
//            return split(str, separator, limit, true, ignoreEmpty, true);
//        }
//
//        /**
//         * 切分字符串<br>
//         * 如果为空字符串或者null 则返回空集合
//         *
//         * @param text        被切分的字符串
//         * @param separator   分隔符字符串
//         * @param limit       限制分片数，小于等于0表示无限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @param ignoreCase  是否忽略大小写
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                CharSequence text,
//                String separator,
//                int limit,
//                boolean isTrim,
//                boolean ignoreEmpty,
//                boolean ignoreCase) {
//            if (null == text) {
//                return new ArrayList<>(0);
//            }
//            final SplitIter splitIter =
//                    new SplitIter(text, new StrFinder(separator, ignoreCase), limit, ignoreEmpty);
//            return splitIter.toList(isTrim);
//        }
//
//        /**
//         * 切分字符串为字符串数组
//         *
//         * @param str         被切分的字符串
//         * @param separator   分隔符字符
//         * @param limit       限制分片数，小于等于0表示无限制
//         * @param isTrim      是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty 是否忽略空串
//         * @return 切分后的集合
//         */
//        public static String[] splitToArray(
//                CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
//        }
//
//        // ----------------------------------------------------------------------------------------------
//        // Split by Whitespace
//
//        /**
//         * 使用空白符切分字符串<br>
//         * 切分后的字符串两边不包含空白符，空串或空白符串并不做为元素之一<br>
//         * 如果为空字符串或者null 则返回空集合
//         *
//         * @param text  被切分的字符串
//         * @param limit 限制分片数
//         * @return 切分后的集合
//         */
//        public static List<String> split(CharSequence text, int limit) {
//            if (null == text) {
//                return new ArrayList<>(0);
//            }
//            final SplitIter splitIter =
//                    new SplitIter(text, new CharMatcherFinder(HelpChar::isBlankChar), limit, true);
//            return splitIter.toList(false);
//        }
//
//        /**
//         * 切分字符串为字符串数组
//         *
//         * @param str   被切分的字符串
//         * @param limit 限制分片数
//         * @return 切分后的集合
//         */
//        public static String[] splitToArray(String str, int limit) {
//            return toArray(split(str, limit));
//        }
//        // ----------------------------------------------------------------------------------------------
//        // Split by regex
//
//        /**
//         * 通过正则切分字符串
//         *
//         * @param text           字符串
//         * @param separatorRegex 分隔符正则
//         * @param limit          限制分片数
//         * @param isTrim         是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty    是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> splitByRegex(
//                String text, String separatorRegex, int limit, boolean isTrim, boolean ignoreEmpty) {
//            final Pattern pattern = PatternPool.get(separatorRegex);
//            return split(text, pattern, limit, isTrim, ignoreEmpty);
//        }
//
//        /**
//         * 通过正则切分字符串<br>
//         * 如果为空字符串或者null 则返回空集合
//         *
//         * @param text             字符串
//         * @param separatorPattern 分隔符正则{@link Pattern}
//         * @param limit            限制分片数
//         * @param isTrim           是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty      是否忽略空串
//         * @return 切分后的集合
//         */
//        public static List<String> split(
//                String text, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
//            if (null == text) {
//                return new ArrayList<>(0);
//            }
//            final SplitIter splitIter =
//                    new SplitIter(text, new PatternFinder(separatorPattern), limit, ignoreEmpty);
//            return splitIter.toList(isTrim);
//        }
//
//        /**
//         * 通过正则切分字符串为字符串数组
//         *
//         * @param str              被切分的字符串
//         * @param separatorPattern 分隔符正则{@link Pattern}
//         * @param limit            限制分片数
//         * @param isTrim           是否去除切分字符串后每个元素两边的空格
//         * @param ignoreEmpty      是否忽略空串
//         * @return 切分后的集合
//         */
//        public static String[] splitToArray(
//                String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
//            return toArray(split(str, separatorPattern, limit, isTrim, ignoreEmpty));
//        }
//        // ----------------------------------------------------------------------------------------------
//        // Split by length
//
//        /**
//         * 根据给定长度，将给定字符串截取为多个部分
//         *
//         * @param text 字符串
//         * @param len  每一个小节的长度
//         * @return 截取后的字符串数组
//         */
//        public static String[] splitByLength(CharSequence text, int len) {
//            if (null == text) {
//                return new String[0];
//            }
//            SplitIter splitIter = new SplitIter(text, new LengthFinder(len), -1, false);
//            return splitIter.toArray(false);
//        }
//        // ---------------------------------------------------------------------------------------------------------- Private method start
//
//        /**
//         * List转Array
//         *
//         * @param list List
//         * @return Array
//         */
//        private static String[] toArray(List<String> list) {
//            return list.toArray(new String[0]);
//        }
//
//        /**
//         * Trim函数
//         *
//         * @param isTrim 是否trim
//         * @return {@link Function}
//         */
//        private static Function<String, String> trimFunc(boolean isTrim) {
//            return (str) -> isTrim ? HelpString.trim(str) : str;
//        }
//        // ---------------------------------------------------------------------------------------------------------- Private method end
//    }
//
//    /**
//     * 给定字符串数组全部做去首尾空格
//     *
//     * @param strs 字符串数组
//     */
//    public static void trim(String[] strs) {
//        if (null == strs) {
//            return;
//        }
//        String str;
//        for (int i = 0; i < strs.length; i++) {
//            str = strs[i];
//            if (null != str) {
//                strs[i] = trim(str);
//            }
//        }
//    }
//
//    /**
//     * 将对象转为字符串<br>
//     *
//     * <pre>
//     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
//     * 2、对象数组会调用Arrays.toString方法
//     * </pre>
//     *
//     * @param obj 对象
//     * @return 字符串
//     */
//    public static String utf8Str(Object obj) {
//        return str(obj, CharsetUtil.CHARSET_UTF_8);
//    }
//
//    /**
//     * 将对象转为字符串
//     *
//     * <pre>
//     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
//     * 2、对象数组会调用Arrays.toString方法
//     * </pre>
//     *
//     * @param obj         对象
//     * @param charsetName 字符集
//     * @return 字符串
//     * @deprecated 请使用 {@link #str(Object, Charset)}
//     */
//    @Deprecated
//    public static String str(Object obj, String charsetName) {
//        return str(obj, Charset.forName(charsetName));
//    }
//
//    /**
//     * 将对象转为字符串
//     *
//     * <pre>
//     * 	 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
//     * 	 2、对象数组会调用Arrays.toString方法
//     * </pre>
//     *
//     * @param obj     对象
//     * @param charset 字符集
//     * @return 字符串
//     */
//    public static String str(Object obj, Charset charset) {
//        if (null == obj) {
//            return null;
//        }
//
//        if (obj instanceof String) {
//            return (String) obj;
//        } else if (obj instanceof byte[]) {
//            return str((byte[]) obj, charset);
//        } else if (obj instanceof Byte[]) {
//            return str((Byte[]) obj, charset);
//        } else if (obj instanceof ByteBuffer) {
//            return str((ByteBuffer) obj, charset);
//        } else if (Whether.arrayObj(obj)) {
//            return HelpArray.toString(obj);
//        }
//
//        return obj.toString();
//    }
//
//    /**
//     * 将byte数组转为字符串
//     *
//     * @param bytes   byte数组
//     * @param charset 字符集
//     * @return 字符串
//     */
//    public static String str(byte[] bytes, String charset) {
//        return str(bytes, CharsetUtil.charset(charset));
//    }
//
//    /**
//     * 解码字节码
//     *
//     * @param data    字符串
//     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
//     * @return 解码后的字符串
//     */
//    public static String str(byte[] data, Charset charset) {
//        if (data == null) {
//            return null;
//        }
//
//        if (null == charset) {
//            return new String(data);
//        }
//        return new String(data, charset);
//    }
//
//    /**
//     * 将Byte数组转为字符串
//     *
//     * @param bytes   byte数组
//     * @param charset 字符集
//     * @return 字符串
//     */
//    public static String str(Byte[] bytes, String charset) {
//        return str(bytes, CharsetUtil.charset(charset));
//    }
//
//    /**
//     * 解码字节码
//     *
//     * @param data    字符串
//     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
//     * @return 解码后的字符串
//     */
//    public static String str(Byte[] data, Charset charset) {
//        if (data == null) {
//            return null;
//        }
//
//        byte[] bytes = new byte[data.length];
//        Byte dataByte;
//        for (int i = 0; i < data.length; i++) {
//            dataByte = data[i];
//            bytes[i] = (null == dataByte) ? -1 : dataByte;
//        }
//
//        return str(bytes, charset);
//    }
//
//    /**
//     * 将编码的byteBuffer数据转换为字符串
//     *
//     * @param data    数据
//     * @param charset 字符集，如果为空使用当前系统字符集
//     * @return 字符串
//     */
//    public static String str(ByteBuffer data, String charset) {
//        if (data == null) {
//            return null;
//        }
//
//        return str(data, CharsetUtil.charset(charset));
//    }
//
//    /**
//     * 将编码的byteBuffer数据转换为字符串
//     *
//     * @param data    数据
//     * @param charset 字符集，如果为空使用当前系统字符集
//     * @return 字符串
//     */
//    public static String str(ByteBuffer data, Charset charset) {
//        if (null == charset) {
//            charset = Charset.defaultCharset();
//        }
//        return charset.decode(data).toString();
//    }
//
//    /**
//     * 调用对象的toString方法，null会返回“null”
//     *
//     * @param obj 对象
//     * @return 字符串
//     * @see String#valueOf(Object)
//     */
//    public static String toString(Object obj) {
//        return String.valueOf(obj);
//    }
//
//    /**
//     * 调用对象的toString方法，null会返回{@code null}
//     *
//     * @param obj 对象
//     * @return 字符串 or {@code null}
//     */
//    public static String toStringOrNull(Object obj) {
//        return null == obj ? null : obj.toString();
//    }
//
//    /**
//     * 创建StringBuilder对象
//     *
//     * @return StringBuilder对象
//     */
//    public static StringBuilder builder() {
//        return new StringBuilder();
//    }
//
//    /**
//     * 创建StringBuilder对象
//     *
//     * @param capacity 初始大小
//     * @return StringBuilder对象
//     */
//    public static StringBuilder builder(int capacity) {
//        return new StringBuilder(capacity);
//    }
//
//    /**
//     * 获得StringReader
//     *
//     * @param str 字符串
//     * @return StringReader
//     */
//    public static StringReader getReader(CharSequence str) {
//        if (null == str) {
//            return null;
//        }
//        return new StringReader(str.toString());
//    }
//
//    /**
//     * 获得StringWriter
//     *
//     * @return StringWriter
//     */
//    public static StringWriter getWriter() {
//        return new StringWriter();
//    }
//
//    /**
//     * 反转字符串<br>
//     * 例如：abcd =》dcba
//     *
//     * @param str 被反转的字符串
//     * @return 反转后的字符串
//     */
//    public static String reverse(String str) {
//        return new String(HelpArray.reverse(str.toCharArray()));
//    }
//
//    // ------------------------------------------------------------------------ fill
//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
//     * 字符填充于字符串前
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @return 填充后的字符串
//     */
//    public static String fillBefore(String str, char filledChar, int len) {
//        return fill(str, filledChar, len, true);
//    }
//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
//     * 字符填充于字符串后
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @return 填充后的字符串
//     */
//    public static String fillAfter(String str, char filledChar, int len) {
//        return fill(str, filledChar, len, false);
//    }
//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @param isPre      是否填充在前
//     * @return 填充后的字符串
//     */
//    public static String fill(String str, char filledChar, int len, boolean isPre) {
//        final int strLen = str.length();
//        if (strLen > len) {
//            return str;
//        }
//
//        String filledStr = HelpString.repeat(filledChar, len - strLen);
//        return isPre ? filledStr.concat(str) : str.concat(filledStr);
//    }
//
//    /**
//     * 计算两个字符串的相似度
//     *
//     * @param str1 字符串1
//     * @param str2 字符串2
//     * @return 相似度
//     */
//    public static double similar(String str1, String str2) {
//        return TextSimilarity.similar(str1, str2);
//    }
//
//    /**
//     * 计算两个字符串的相似度百分比
//     *
//     * @param str1  字符串1
//     * @param str2  字符串2
//     * @param scale 相似度
//     * @return 相似度百分比
//     */
//    public static String similar(String str1, String str2, int scale) {
//        return TextSimilarity.similar(str1, str2, scale);
//    }
//
//    /**
//     * 生成随机UUID
//     *
//     * @return UUID字符串
//     * @see IdUtil#randomUUID()
//     */
//    public static String uuid() {
//        return IdUtil.randomUUID();
//    }
//
//    /**
//     * 格式化文本，使用 {varName} 占位<br>
//     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
//     *
//     * @param template 文本模板，被替换的部分用 {key} 表示
//     * @param map      参数值对
//     * @return 格式化后的文本
//     */
//    public static String format(CharSequence template, Map<?, ?> map) {
//        return format(template, map, true);
//    }
//
//    /**
//     * 格式化文本，使用 {varName} 占位<br>
//     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
//     *
//     * @param template   文本模板，被替换的部分用 {key} 表示
//     * @param map        参数值对
//     * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
//     * @return 格式化后的文本
//     */
//    public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
//        return StrFormatter.format(template, map, ignoreNull);
//    }
//
//
//    private static final RegularAmount CHINESE_CAPITALIZED = new ChineseCapitalized();
//
//    public static String quote(final Object obj) {
//        return (obj != null ? "'" + obj + "'" : "null");
//    }
//
//    public static String mergeStrings(String split, String... strings) {
//        StringBuilder sb = new StringBuilder();
//        for (String string : strings) {
//            sb.append(string).append(split);
//        }
//        sb.delete(sb.length() - split.length(), Integer.MAX_VALUE);
//        return sb.toString();
//    }
//
//    public static String mergeStrings(String split, Iterable<String> strings) {
//        StringBuilder sb = new StringBuilder();
//        for (String string : strings) {
//            sb.append(string).append(split);
//        }
//        sb.delete(sb.length() - split.length(), Integer.MAX_VALUE);
//        return sb.toString();
//    }
//
//    /**
//     * 计算包含字符的所有位置,按从小到大排列
//     *
//     * @param string 查询的字符串
//     * @param ch     字符
//     * @return 返回位置序列
//     */
//    public static int[] countChPosition(final String string, final char ch,
//                                        final boolean isEnableEscape) {
//        return countChPosition(string, String.valueOf(ch), false);
//    }
//
//    /**
//     * 计算包含子字符串的所有位置,按从小到大排列
//     *
//     * @param str 待查询的字符串
//     * @param sub 子字符串
//     * @return 返回位置序列
//     */
//    public static int[] countChPosition(final String str, final String sub,
//                                        final boolean isEnableEscape) {
//        if (UtilsOfJudge.isAnyInvalid(str, sub)) {
//            return new int[0];
//        }
//        int size = 0;
//        final int chLen = sub.length();
//        for (int index = str.indexOf(sub); index != -1; index = str.indexOf(sub, index + chLen)) {
//            if (!isEnableEscape || index == 0 || str.charAt(index - 1) != '\\') {
//                size++;
//            }
//        }
//        final int[] a = new int[size];
//        for (int index = str.indexOf(sub), i = 0; i < size;
//             index = str.indexOf(sub, index + chLen), i++) {
//            if (!isEnableEscape || index == 0 || str.charAt(index - 1) != '\\') {
//                a[i] = index;
//            }
//        }
//        return a;
//    }
//
//    /**
//     * 统计包含字符的次数
//     *
//     * @param str 待查询的字符串
//     * @param ch  字符
//     * @return 字符出现的次数
//     */
//    public static int countChOccurrences(final String str, final char ch) {
//        return countChOccurrences(str, String.valueOf(ch));
//    }
//
//    /**
//     * 统计包含子字符串的次数
//     *
//     * @param str 待查询的字符串
//     * @param sub 子字符串
//     * @return 子字符串出现的次数
//     */
//    public static int countChOccurrences(final String str, final String sub) {
//        if (UtilsOfJudge.isAnyInvalid(str, sub)) {
//            return 0;
//        }
//        int count = 0;
//        int pos = 0;
//        int idx;
//        while ((idx = str.indexOf(sub, pos)) != -1) {
//            ++count;
//            pos = idx + sub.length();
//        }
//        return count;
//    }
//
//    public static Charset detectedCharset(final byte[] bytes) {
//        final UniversalDetector detector = new UniversalDetector(null);
//        final int detectorLength = 4 * 1024 * 1024;//4K
//        detector.handleData(bytes, 0, Math.min(bytes.length, detectorLength));
//        detector.dataEnd();
//        final String encoding = detector.getDetectedCharset();
//        detector.reset();
//        final Charset charset = CharsetType.parseCharset(encoding);
//        if (charset == null) {
//            return UtilsOfConstant.GLOBAL_CHARSET;
//        }
//        return charset;
//    }

//
//    public static String rangeString(long lower, long upper) {
//        return rangeString(lower, upper, '-');
//    }
//
//    public static String rangeString(long lower, long upper, char ch) {
//        return String.valueOf(lower) + ch + upper;
//    }
//
//    public static String rangeString(double lower, double upper) {
//        return rangeString(lower, upper, '-');
//    }
//
//    public static String rangeString(double lower, double upper, char ch) {
//        return String.valueOf(lower) + ch + upper;
//    }
//
//    public static String toString(char... chars) {
//        StringBuilder sb = new StringBuilder(chars.length * 2);
//        for (char c : chars) {
//            sb.append(c);
//        }
//        return sb.toString();
//    }
//
//    public static String toString(Collection<Character> characters) {
//        StringBuilder sb = new StringBuilder(characters.size() * 2);
//        characters.forEach(sb::append);
//        return sb.toString();
//    }
//
//    /**
//     * 首字符小写
//     */
//    public static String headLower(final String str) {
//        return changeFirstCharacterCase(str, false);
//    }
//
//    /**
//     * 首字符大写
//     */
//    public static String headUpper(final String str) {
//        return changeFirstCharacterCase(str, true);
//    }
//
//    /**
//     * 改变首字符大小写
//     *
//     * @param str        字符串
//     * @param capitalize 是否首字母大写
//     */
//    private static String changeFirstCharacterCase(final String str, final boolean capitalize) {
//        if (UtilsOfJudge.isInvalid(str)) {
//            return str;
//        }
//        final char baseChar = str.charAt(0);
//        final char updatedChar;
//        if (capitalize) {
//            updatedChar = Character.toUpperCase(baseChar);
//        } else {
//            updatedChar = Character.toLowerCase(baseChar);
//        }
//        if (baseChar == updatedChar) {
//            return str;
//        }
//        final char[] chars = str.toCharArray();
//        chars[0] = updatedChar;
//        return new String(chars, 0, chars.length);
//    }
//
//    /**
//     * 以逗号为分隔符合并对象成字符串
//     */
//    public String concat(Object... objs) {
//        return concat(",", objs);
//    }
//
//    /**
//     * 以逗号为分隔符合并对象成字符串
//     */
//    public String concat(String delimited, Object... objs) {
//        return concat(delimited, "", "", objs);
//    }
//
//    /**
//     * 指定分隔符和前后缀合并对象成字符串
//     */
//    public String concat(String delimited, String prefix, String suffix, Object... objs) {
//        if (UtilsOfJudge.isEmpty(objs)) {
//            return UtilsOfConstant.EMPTY;
//        }
//        delimited = UtilsOfJudge.valid(delimited, UtilsOfConstant.EMPTY);
//        prefix = UtilsOfJudge.valid(prefix, UtilsOfConstant.EMPTY);
//        prefix = UtilsOfJudge.valid(prefix, UtilsOfConstant.EMPTY);
//        final StringBuilder sb = new StringBuilder();
//        final int len = objs.length;
//        if (len == 1) {
//            sb.append(prefix).append(objs[0]).append(suffix);
//        }
//        for (int i = 1; i < len; i++) {
//            sb.append(delimited).append(prefix).append(objs[i]).append(suffix);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 数字金额大写转换
//     *
//     * @param amount 数字
//     * @return 中文大写数字
//     */
//    public static String convertChineseCapitalizedAmount(String amount) {
//        return CHINESE_CAPITALIZED.parse(amount);
//    }
//
//    /**
//     * 转换为驼峰命名法
//     *
//     * @param name                      名字
//     * @param isFirstWordFirstWordUpper 首字母是否大写
//     * @return 转换后的名字
//     */
//    public static String convertCamelCase(final String name, final boolean isFirstWordFirstWordUpper) {
//        return convertNameByNoDelimiter(name, isFirstWordFirstWordUpper);
//    }
//
//    /**
//     * 转换为无符号名字
//     *
//     * @param name                      名字
//     * @param isFirstWordFirstWordUpper 首字母是否大写
//     * @return 转换后的名字
//     */
//    private static String convertNameByNoDelimiter(final String name, final boolean isFirstWordFirstWordUpper) {
//        UtilsOfAssert.checkValid(name, "Name is invalid");
//        List<String> wordNumbers = findWordNumber(name);
//        if (UtilsOfJudge.isEmpty(wordNumbers)) {
//            return UtilsOfConstant.EMPTY;
//        }
//        StringBuilder sb = new StringBuilder(name.length() + 10);
//        String wordNumber = wordNumbers.get(0);
//        sb.append(isFirstWordFirstWordUpper ? headUpper(wordNumber) : wordNumber);
//        for (int i = 1; i < wordNumbers.size(); i++) {
//            wordNumber = headUpper(wordNumbers.get(i));
//            sb.append(wordNumber);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 转换为下划线命名法
//     *
//     * @param name    名字
//     * @param isUpper 是否大写
//     * @return 转换后的名字
//     */
//    public static String convertUnderScoreCase(final String name, final boolean isUpper) {
//        return convertNameByDelimiter(name, isUpper, '_');
//    }
//
//    /**
//     * 转换为短横线命名法
//     *
//     * @param name    名字
//     * @param isUpper 是否大写
//     * @return 转换后的名字
//     */
//    public static String convertKebabCase(final String name, final boolean isUpper) {
//        return convertNameByDelimiter(name, isUpper, '-');
//    }
//
//    /**
//     * 转换为有分隔符名字
//     *
//     * @param name      名字
//     * @param isUpper   是否大写
//     * @param delimiter 指定分隔符
//     * @return 转换后的名字
//     */
//    private static String convertNameByDelimiter(String name, final boolean isUpper, final char delimiter) {
//        UtilsOfAssert.checkValid(name, "Name is invalid");
//        List<String> wordNumbers = findWordNumber(name, isUpper);
//        if (UtilsOfJudge.isEmpty(wordNumbers)) {
//            return UtilsOfConstant.EMPTY;
//        }
//        StringBuilder sb = new StringBuilder(name.length() + 10);
//        String wordNumber = wordNumbers.get(0);
//        sb.append(wordNumber);
//        for (int i = 1; i < wordNumbers.size(); i++) {
//            wordNumber = wordNumbers.get(i);
//            sb.append(delimiter).append(wordNumber);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 半角转全角
//     *
//     * @param string String
//     * @param chars  不替换的字符集合
//     * @return 全角字符串.
//     */
//    public static String convertDbcToSbc(final String string, final char... chars) {
//        final char[] c = string.toCharArray();
//        for (int i = 0; i < c.length; i++) {
//            if (chars != null) {
//                final int index = Arrays.binarySearch(chars, c[i]);
//                if (index == -1) {
//                    continue;
//                }
//            }
//            if (c[i] == ' ') {
//                c[i] = '\u3000';
//            } else if (c[i] < '\177') {
//                c[i] = (char) (c[i] + 65248);
//            }
//        }
//        return new String(c);
//    }
//
//    /**
//     * 替换全角为半角
//     *
//     * @param string 文本
//     * @param chars  不替换的字符集合
//     * @return 替换后的字符
//     */
//    public static String convertSbcToDbc(final String string, final char... chars) {
//        final char[] c = string.toCharArray();
//        for (int i = 0; i < c.length; i++) {
//            if (chars != null) {
//                final int index = Arrays.binarySearch(chars, c[i]);
//                if (index == -1) {
//                    continue;
//                }
//            }
//            if (c[i] == '\u3000') {
//                c[i] = ' ';
//            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
//                c[i] = (char) (c[i] - 65248);
//            }
//        }
//        return new String(c);
//    }
//
//    /**
//     * 数字金额大写转换 先写个完整的然后将如零拾替换成零
//     *
//     * @param num 数字
//     * @return 中文大写数字
//     */
//    public static String convertUpperDigit(final BigDecimal num) {
//        return null;
//    }
//
//    /**
//     * 找到所有单词在一个字符串中
//     * <p>
//     * 以符号分隔或者大小字母分隔，一般单词有三种写法name Name和专有名词NAME
//     * 如：
//     * findWord   find word
//     * UtilsHTTPUtils   utils http utils
//     *
//     * @param string 字符串
//     * @return 单词列表
//     */
//    public static List<String> findWord(String string) {
//        return findWord(string, false);
//    }
//
//    /**
//     * 找到所有单词在一个字符串中
//     * <p>
//     * 以符号分隔或者大小字母分隔，一般单词有三种写法name Name和专有名词NAME
//     * 如：
//     * findWord   find word
//     * UtilsHTTPUtils   utils http utils
//     *
//     * @param string  字符串
//     * @param isUpper 是否大写
//     * @return 单词列表
//     */
//    public static List<String> findWord(String string, final boolean isUpper) {
//        return findKeyWord(string, false, isUpper);
//    }
//
//    /**
//     * 找到所有单词数字在一个字符串中
//     * <p>
//     * 以符号分隔或者大小字母分隔，一般单词有三种写法name Name和专有名词NAME
//     * 如果有数字将归与前一个单词，如果前面没有单词将忽略
//     * 如：
//     * find123Word   find213 word
//     * UtilsHTTPUtils   utils http utils
//     * X509TrustManager x509 trust manager
//     *
//     * @param string 字符串
//     * @return 单词列表
//     */
//    public static List<String> findWordNumber(String string) {
//        return findWordNumber(string, false);
//    }
//
//    /**
//     * 找到所有单词数字在一个字符串中
//     * <p>
//     * 以符号分隔或者大小字母分隔，一般单词有三种写法name Name和专有名词NAME
//     * 如果有数字将归与前一个单词，如果前面没有单词将忽略
//     * 如：
//     * find123Word   find213 word
//     * UtilsHTTPUtils   utils http utils
//     * X509TrustManager x509 trust manager
//     *
//     * @param string  字符串
//     * @param isUpper 是否大写
//     * @return 单词列表
//     */
//    public static List<String> findWordNumber(String string, final boolean isUpper) {
//        return findKeyWord(string, true, isUpper);
//    }
//
//    /**
//     * 实现找单词或单词数字形式的字符串
//     *
//     * @param string       字符串
//     * @param isNeedNumber 是否需要数字
//     * @param isUpper      单词是否大写
//     * @return 单词或单词数字列表
//     */
//    private static List<String> findKeyWord(String string, final boolean isNeedNumber, final boolean isUpper) {
//        List<String> results = new LinkedList<>();
//        if (UtilsOfJudge.isInvalid(string)) {
//            return results;
//        }
//        Stack<Character> stack = new Stack<>();
//        Predicate<Character> isNumber = c -> isNeedNumber && Character.isDigit(c);
//        Actuator genWord = () -> {
//            String temp = toString(stack).toLowerCase();
//            temp = isUpper ? temp.toUpperCase() : temp.toLowerCase();
//            stack.clear();
//            results.add(temp);
//        };
//        final int len = string.length();
//        for (int i = 0; i < len; i++) {
//            char c = string.charAt(i);
//            //栈底一定是字母
//            if (stack.isEmpty()) {
//                if (Character.isLetter(c)) {
//                    stack.push(c);
//                }
//                continue;
//            }
//            char top = stack.peek();
//            if (Character.isUpperCase(top)) {
//                //当前字符是字母或数字
//                if (Character.isLetter(c) || isNumber.test(c)) {
//                    if (Character.isLowerCase(c) && stack.size() != 1) {
//                        char tempTop = stack.pop();
//                        if (Character.isUpperCase(tempTop)) {
//                            genWord.exec();
//                        }
//                        stack.add(tempTop);
//                    }
//                    stack.add(c);
//                    //不是字母和数字，添加单词
//                } else {
//                    genWord.exec();
//                }
//                continue;
//            }
//            if (Character.isLowerCase(top)) {
//                if (Character.isLowerCase(c) || isNumber.test(c)) {
//                    stack.push(c);
//                } else {
//                    genWord.exec();
//                    i--;
//                }
//                continue;
//            }
//            if (isNumber.test(top)) {
//                if (isNumber.test(c)) {
//                    stack.add(c);
//                } else {
//                    genWord.exec();
//                    i--;
//                }
//            }
//        }
//        if (UtilsOfJudge.isNoEmpty(stack)) {
//            genWord.exec();
//        }
//        return results;
//    }
//
//    /**
//     * 填充模板，以{}为模板
//     * <p>
//     * 如：你好{key}！ key=中国 则得到：您好中国！
//     * <p>
//     * 如果不存在的键将以原来key填充
//     *
//     * @param template 模板字符串
//     * @param param    参数
//     * @return 返回填充后的数据
//     * @throws RuntimeException 当大括号不匹配时抛出异常
//     * @throws AssertionError   参数不符合条件时抛出异常
//     */
//    public static String fillPlaceholderTemplateByBigBraces(String template, final Initiator<Map<String, Object>> param) {
//        final StringBuilder stringBuilder = new StringBuilder();
//        try {
//            fillPlaceholderTemplateByBigBraces(stringBuilder, template, param);
//        } catch (final IOException ignored) {
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 填充模板，以{}为模板
//     * <p>
//     * 如：你好{key}！ key=中国 则得到：您好中国！
//     * <p>
//     * 如果不存在的键将以原来key填充
//     *
//     * @param template 模板字符串
//     * @param param    参数
//     * @throws RuntimeException 当大括号不匹配时抛出异常
//     * @throws AssertionError   参数不符合条件时抛出异常
//     */
//    private static void fillPlaceholderTemplateByBigBraces(final Appendable sb, final String template,
//                                                           final Initiator<Map<String, Object>> param) throws IOException {
//        if (UtilsOfJudge.isInvalid(template)) {
//            return;
//        }
//        UtilsOfAssert.checkIsNoNull(param, "Param must not be null");
//        final Map<String, Object> map = UtilsOfCollection.hashMap();
//        param.accept(map);
//        String handleTemplate = template.trim();
//        final int len = template.length();
//        final boolean isIn = false;
//        char preC = ' ';
//        for (int i = 0; i < len; i++) {
//            final char c = template.charAt(i);
//            if (c == UtilsOfConstant.C_BIG_BRACES_LEFT && preC != UtilsOfConstant.C_ESCAPE) {
//                final String key = getBigBracesLeftToRight(template, i);
//                if (map.containsKey(key)) {
//                    sb.append(Objects.toString(map.get(key)));
//                } else {
//                    sb.append(UtilsOfConstant.BIG_BRACES_LEFT).append(key).append(UtilsOfConstant.BIG_BRACES_RIGHT);
//                }
//                i = i + key.length() + 1;
//            } else if (c == UtilsOfConstant.C_BIG_BRACES_RIGHT && preC != UtilsOfConstant.C_ESCAPE) {
//                throw new RuntimeException(
//                        "Mismatch '{' near that:  " + getBeforeString(template, i, 10));
//            } else if (c != UtilsOfConstant.C_ESCAPE) {
//                sb.append(c);
//            }
//            preC = c;
//        }
//    }
//
//    /**
//     * 顺序替代占位替代占位符
//     * <p>
//     * 如果参数不够时占位符会被去除 如果参数超出时多余的参数会被忽略 如果需要让占位符输出则使用转义
//     *
//     * @param stringPattern 字符串模板
//     * @param placeholder   占位符
//     * @param args          参数列表
//     * @return 结果
//     */
//    public static String fillPlaceholderTemplateByOrder(final String stringPattern, final char placeholder, final Object... args) {
//        final StringBuilder stringBuilder = new StringBuilder();
//        try {
//            fillPlaceholderTemplateByOrder(stringBuilder, stringPattern, placeholder, args);
//        } catch (final IOException ignored) {
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 顺序替代占位替代占位符
//     * <p>
//     * 如果参数不够时占位符会被去除 如果参数超出时多余的参数会被忽略 如果需要让占位符输出则使用转义
//     *
//     * @param contain       字符串容器
//     * @param stringPattern 字符串模板
//     * @param placeholder   占位符
//     * @param args          参数列表
//     */
//    private static void fillPlaceholderTemplateByOrder(final Appendable contain, final String stringPattern, final char placeholder,
//                                                       final Object... args) throws IOException {
//        if (UtilsOfJudge.isInvalid(stringPattern)) {
//            return;
//        }
//        final int[] placeholderIndexes = countChPosition(stringPattern, placeholder, true);
//        //取占位符的有效个数
//        final int validCount = Math.min(args.length, placeholderIndexes.length);
//        int start = 0;
//        for (int i = 0; i < validCount; i++) {
//            contain.append(stringPattern.subSequence(start, placeholderIndexes[i]));
//            contain.append(Objects.toString(args[i]));
//            start = placeholderIndexes[i] + 1;
//        }
//        for (int i = validCount; i < placeholderIndexes.length; i++) {
//            contain.append(stringPattern.subSequence(start, placeholderIndexes[i]));
//            start = placeholderIndexes[i] + 1;
//        }
//        contain.append(stringPattern.subSequence(start, stringPattern.length()));
//    }
//
//    /**
//     * 获取左右大括号中的字符串
//     * <p>
//     * 如：你好{who}! 则who将被获取
//     *
//     * @param template 模板字符串
//     * @param index    当前左括号处在位置
//     * @return 返回占位字符串
//     */
//    private static String getBigBracesLeftToRight(final String template, final int index) {
//        return getStringInRange(template, index, UtilsOfConstant.C_BIG_BRACES_LEFT, UtilsOfConstant.C_BIG_BRACES_RIGHT);
//    }
//
//    /**
//     * 获取左右界限符中的字符串
//     * <p>
//     * 如：你好{who}! 如：你好[who]! 如：你好(who)! 则who将被获取
//     *
//     * @param template 模板字符串
//     * @param index    当前不是转义的左符号处在位置
//     * @param left     左边界限符号
//     * @param right    右边界限符号
//     * @return 返回占位字符串
//     */
//    public static String getStringInRange(final String template, final int index, final char left, final char right) {
//        char preC = left;
//        for (int i = index + 1; i < template.length(); i++) {
//            final char c = template.charAt(i);
//            //找到未转义的右边符号
//            if (c == right && preC != UtilsOfConstant.C_ESCAPE) {
//                //去除前后空格
//                return template.substring(index + 1, i).trim();
//            }
//            //如果还存在左边符号，则模板错误
//            if (c == left && preC != UtilsOfConstant.C_ESCAPE) {
//                break;
//            }
//            preC = c;
//        }
//        throw new RuntimeException(
//                "Mismatch '}' near that:  " + getAfterString(template, index, 10));
//    }
//
//    /**
//     * 获取指定位置前几个字符串
//     *
//     * @param string 字符串
//     * @param index  指定索引
//     * @param amount 需要字符的数量
//     * @return 返回前几个字符串
//     */
//    public static String getBeforeString(final String string, final int index, final int amount) {
//        UtilsOfAssert.checkGreaterEqual(index, 0, "Index out of range for " + index);
//        UtilsOfAssert.checkLesser(index, string.length(), "Index out of range for " + index);
//        UtilsOfAssert.checkGreaterEqual(amount, 0, "Amount of char don't allow lesser 0");
//        int start = index - amount + 1;
//        if (start < 0) {
//            start = 0;
//        }
//        return string.substring(start, index + 1);
//    }
//
//    /**
//     * 获取指定位置后几个字符串
//     *
//     * @param string 字符串
//     * @param index  指定索引
//     * @param amount 需要字符的数量
//     * @return 返回前几个字符串
//     */
//    public static String getAfterString(final String string, final int index, final int amount) {
//        UtilsOfAssert.checkGreaterEqual(index, 0, "Index out of range for " + index);
//        UtilsOfAssert.checkLesser(index, string.length(), "Index out of range for " + index);
//        UtilsOfAssert.checkGreaterEqual(amount, 0, "Amount of char don't allow lesser 0");
//        final int end = index + amount + 1;
//        return string.substring(index, end);
//    }
//
//    /**
//     * 去除从某个字符结束的前缀，如果不包含则原样返回
//     * <p>string:<b>111/222/333</b></p>
//     * <p>cd:<b>/</b></p>
//     * <p>返回:<b>222/333</b></p>
//     *
//     * @param string 字符串
//     * @param ch     切分的字符
//     * @return 返回去除后的字符串
//     */
//    public static String rmPrefixFromCh(final String string, final String ch) {
//        return rmPrefixFromCh(string, ch, 1);
//    }
//
//    /**
//     * 去除从第几个某个字符结束的前缀，如果不包含则原样返回
//     * <p>string:<b>111/222/333</b></p>
//     * <p>ch:<b>/</b></p>
//     * <p>chIndex:<b>2</b></p>
//     * <p>返回:<b>333</b></p>
//     *
//     * @param string  字符串
//     * @param ch      切分的字符
//     * @param chIndex 从前数第几个开始切分,从1,2,3,4,5,6......,或者从后数第几个开始切分，从-1，-2，-3，-4，-5，-6......
//     * @return 返回去除后的字符串
//     */
//    public static String rmPrefixFromCh(final String string, final String ch, int chIndex) {
//        chIndex = UtilsOfJudge.ifElse(chIndex == 0, 1, chIndex);
//        int[] a = countChPosition(string, ch, false);
//        if (chIndex > 0) {
//            Arrays.sort(a);
//        } else {
//            a = IntStream.of(a).boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray();
//            chIndex = -chIndex;
//        }
//        if (chIndex > a.length) {
//            return string;
//        }
//        return string.substring(a[chIndex - 1] + ch.length());
//    }
//
//    /**
//     * 去除后缀
//     *
//     * @param string 要被去除的字符串
//     * @param suffix 后缀字符串
//     * @return 返回去除的结果，没有则返回原字符串
//     */
//    public static String rmSuffix(final String string, final String suffix) {
//        if (!UtilsOfJudge.isHasSuffixIgnoredCase(string, suffix)) {
//            return string;
//        }
//        return string.substring(0, string.length() - suffix.length());
//    }
//
//    /**
//     * 去除前缀
//     *
//     * @param string 要被去除的字符串
//     * @param prefix 前缀字符串
//     * @return 返回去除的结果，没有则返回原字符串
//     */
//    public static String rmPrefix(final String string, final String prefix) {
//        if (!UtilsOfJudge.isHasPrefixIgnoredCase(string, prefix)) {
//            return string;
//        }
//        return string.substring(prefix.length());
//    }
//
//    /**
//     * 去除从某个字符开始的后缀，如果不包含则原样返回
//     * <p>string:<b>111/222/333</b></p>
//     * <p>cd:<b>/</b></p>
//     * <p>返回:<b>111/222</b></p>
//     *
//     * @param string 字符串
//     * @param ch     切分的字符
//     * @return 返回去除后的字符串
//     */
//    public static String rmSuffixFromCh(final String string, final String ch) {
//        return rmSuffixFromCh(string, ch, 1);
//    }
//
//    /**
//     * 去除从第几个某个字符开始的后缀，如果不包含则原样返回
//     * <p>string:<b>111/222/333</b></p>
//     * <p>cd:<b>/</b></p>
//     * <p>chIndex:<b>2</b></p>
//     * <p>返回:<b>111</b></p>
//     *
//     * @param string  字符串
//     * @param ch      切分的字符
//     * @param chIndex 从后数第几个开始切分,从1,2,3,4,5,6......,或者从前数第几个开始切分，从-1，-2，-3，-4，-5，-6......
//     * @return 返回去除后的字符串
//     */
//    public static String rmSuffixFromCh(final String string, final String ch, int chIndex) {
//        chIndex = UtilsOfJudge.ifElse(chIndex == 0, 1, chIndex);
//        int[] a = countChPosition(string, ch, false);
//        if (chIndex > 0) {
//            a = IntStream.of(a).boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray();
//        } else {
//            Arrays.sort(a);
//            chIndex = -chIndex;
//        }
//        if (chIndex > a.length) {
//            return string;
//        }
//        return string.substring(0, a[chIndex - 1]);
//    }
//
//    /**
//     * 移除分割符
//     *
//     * @param string 字符串
//     * @param splits 分割字符串集合
//     * @return 分割后的字符串
//     */
//    public static String rmSplit(String string, final String... splits) {
//        for (final String split : splits) {
//            string = rmSplit(string, split);
//        }
//        return string;
//    }
//
//    /**
//     * 移除分割符
//     *
//     * @param string 字符串
//     * @param split  分割字符串
//     * @return 分割后的字符串
//     */
//    public static String rmSplit(final String string, final String split) {
//        final StringBuilder stringBuilder = new StringBuilder(string.length());
//        Arrays.stream(string.split(split)).forEach(stringBuilder::append);
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 移除不可见字符
//     *
//     * @param string 字符串
//     * @return 移除后的字符串
//     */
//    public static String rmInvisibleCh(String string) {
//        for (final String s : UtilsOfConstant.INVISIBLE_LIST) {
//            string = string.replaceAll(s, "");
//        }
//        return string;
//    }
//
//    /**
//     * 替换Map里值的字符串
//     * <p>map1: k1,v1</p>
//     * <p>map2: k2,v2</p>
//     * 当map1中里的v1包含k2则会将替换v1中的k2为v2
//     * <p>如：k1=1，v1=hello word，k2=hello，v2=hi，则v1将被替换为hi word</p>
//     *
//     * @param map     需要替换的map
//     * @param replace 被替换的键与值，即map里的值里包含replace的key，则会被替换成value
//     * @return 返回一个新的被替换的Map集合
//     */
//    public static Map<String, String> replaceMapValue(final Map<String, String> map,
//                                                      final Map<String, String> replace) {
//        final Map<String, String> result = UtilsOfCollection.hashMap();
//        map.forEach((key, value) -> replace.forEach((k, v) -> {
//            if (!value.contains(k)) {
//                return;
//            }
//            if (result.containsKey(key)) {
//                return;
//            }
//            final String temp = value.replace(k, v);
//            result.put(key, temp);
//        }));
//        if (result.size() == map.size()) {
//            return result;
//        }
//        if (result.size() > map.size()) {
//            throw new RuntimeException();
//        }
//        map.forEach((k, v) -> {
//            if (result.containsKey(k)) {
//                return;
//            }
//            replace.put(k, v);
//        });
//        return result;
//    }
//
//    /**
//     * 清除空格
//     *
//     * @param str          字符串
//     * @param isTrimPrefix 是否清除前缀
//     * @param isTrimSuffix 是否清除后缀
//     */
//    public static String trimWhitespace(final String str, final boolean isTrimPrefix,
//                                        final boolean isTrimSuffix) {
//        return trimCharacter(str, isTrimPrefix, isTrimSuffix, ' ');
//    }
//
//    /**
//     * 清除空格
//     *
//     * @param str          字符串
//     * @param isTrimPrefix 是否清除前缀
//     * @param isTrimSuffix 是否清除后缀
//     * @param chs          要清除的字符集
//     */
//    public static String trimCharacter(final String str, final boolean isTrimPrefix,
//                                       final boolean isTrimSuffix, final char... chs) {
//        if (UtilsOfJudge.isInvalid(str)) {
//            return str;
//        }
//        boolean isBoth = (isTrimPrefix && isTrimSuffix) || !(isTrimPrefix || isTrimSuffix);
//        int beginIndex = 0;
//        int endIndex = str.length() - 1;
//        if (chs.length == 1) {
//            final char c = chs[0];
//            if (Character.isWhitespace(c)) {
//                if (isTrimPrefix || isBoth) {
//                    while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
//                        beginIndex++;
//                    }
//                }
//                if (isTrimSuffix || isBoth) {
//                    while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
//                        endIndex--;
//                    }
//                }
//            } else {
//                while (beginIndex <= endIndex && str.charAt(beginIndex) == c) {
//                    beginIndex++;
//                }
//                while (endIndex > beginIndex && str.charAt(endIndex) == c) {
//                    endIndex--;
//                }
//            }
//        } else {
//            Arrays.sort(chs);
//            final Predicate<Character> isRemove = character -> Arrays.binarySearch(chs, character) > 0;
//            if (isTrimPrefix || isBoth) {
//                while (beginIndex <= endIndex && isRemove.test(str.charAt(beginIndex))) {
//                    beginIndex++;
//                }
//            }
//            if (isTrimSuffix || isBoth) {
//                while (endIndex > beginIndex && isRemove.test(str.charAt(endIndex))) {
//                    endIndex--;
//                }
//            }
//        }
//        return str.substring(beginIndex, endIndex + 1);
//    }
//
//    /**
//     * 清除出现的所有空格
//     *
//     * @param str 字符串
//     */
//    public static String trimAllWhitespace(final String str) {
//        if (UtilsOfJudge.isInvalid(str)) {
//            return str;
//        }
//        final int len = str.length();
//        final StringBuilder sb = new StringBuilder(len);
//        for (int i = 0; i < len; i++) {
//            final char c = str.charAt(i);
//            if (!Character.isWhitespace(c)) {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 默认规则字符串解析成map
//     * <p>例如： 0=男,1=女,2=未知</p>
//     *
//     * @param string 解析的字符串
//     * @return 解析后的map
//     */
//    public static Map<String, String> parseToMap(final String string) {
//        return parseToMap(string, ',', '=', true, true);
//    }
//
//    /**
//     * 默认规则字符串解析成map
//     * <p>例如： 0=男,1=女,2=未知</p>
//     *
//     * @param string     解析的字符串
//     * @param blockSplit 块分割符
//     * @param split      键值分割符
//     * @return 解析后的map
//     */
//    public static Map<String, String> parseToMap(final String string, final char blockSplit, final char split) {
//        return parseToMap(string, blockSplit, split, true, true);
//    }
//
//    /**
//     * 将一定规则字符串解析成map
//     *
//     * @param string          字符串
//     * @param blockSplit      块分割符
//     * @param split           键值分割符
//     * @param isRmInvisibleCh 是否移除不可见字符
//     * @return 解析后的map
//     */
//    public static Map<String, String> parseToMap(String string, final char blockSplit, final char split, final boolean isRmInvisibleCh,
//                                                 final boolean isEnableEscape) {
//        string = isRmInvisibleCh ? rmInvisibleCh(string) : string;
//        final Map<String, String> map = UtilsOfCollection.hashMap();
//        final String[] sa = split(string, blockSplit);
//        for (final String s : sa) {
//            final String[] strings = s.split(String.valueOf(split));
//            if (strings.length == 2) {
//                map.put(strings[0], strings[1]);
//            } else if (strings.length >= 1) {
//                if (UtilsOfJudge.isValid(strings[0])) {
//                    map.put(strings[0], UtilsOfConstant.EMPTY);
//                }
//            }
//        }
//        return map;
//    }
//
//    /**
//     * 分割字符串
//     * <p>
//     * 开启转义 清除空格去 不保留引用符号 双引号作为前后引用符号 逗号作为分隔符
//     *
//     * @param string        字符串
//     * @param removeIndexes 移除索引，支持超出范围{@link UtilsOfFunction#index(int, int)}
//     * @return 分割后的字符串数组，如果字符串不是有效的，返回空的字符串数组
//     */
//    public static String[] split(String string,
//                                 int... removeIndexes) {
//        return split(string, ',', '"', removeIndexes);
//    }
//
//    /**
//     * 分割字符串
//     * <p>
//     * 开启转义 清除空格去 不保留引用符号 双引号作为前后引用符号
//     *
//     * @param string        字符串
//     * @param delimiter     分割符
//     * @param removeIndexes 移除索引，支持超出范围{@link UtilsOfFunction#index(int, int)}
//     * @return 分割后的字符串数组，如果字符串不是有效的，返回空的字符串数组
//     */
//    public static String[] split(String string,
//                                 final char delimiter,
//                                 int... removeIndexes) {
//        return split(string, delimiter, '"', removeIndexes);
//    }
//
//    /**
//     * 分割字符串
//     * <p>
//     * 开启转义 清除空格去 不保留引用符号
//     *
//     * @param string        字符串
//     * @param delimiter     分割符
//     * @param quoteChar     开始结束引用符号
//     * @param removeIndexes 移除索引，支持超出范围{@link UtilsOfFunction#index(int, int)}
//     * @return 分割后的字符串数组，如果字符串不是有效的，返回空的字符串数组
//     */
//    public static String[] split(String string,
//                                 final char delimiter,
//                                 final char quoteChar,
//                                 int... removeIndexes) {
//        return split(string, delimiter, quoteChar, quoteChar, false, true, true, removeIndexes);
//    }
//
//    /**
//     * 分割字符串
//     *
//     * @param string           字符串
//     * @param delimiter        分割符
//     * @param beginQuoteChar   开始引用符号
//     * @param endQuoteChar     结束引用符号
//     * @param retainQuotes     是否保留引用符号
//     * @param isTrimWhitespace 是否清楚前后空格
//     * @param isEnableEscape   是否开启转义
//     * @param removeIndexes    移除索引，支持超出范围{@link UtilsOfFunction#index(int, int)}
//     * @return 分割后的字符串数组，如果字符串不是有效的，返回空的字符串数组
//     */
//    public static String[] split(String string,
//                                 final char delimiter,
//                                 final char beginQuoteChar,
//                                 final char endQuoteChar,
//                                 final boolean retainQuotes,
//                                 final boolean isTrimWhitespace,
//                                 final boolean isEnableEscape,
//                                 int... removeIndexes) {
//        if (UtilsOfJudge.isInvalid(string)) {
//            return UtilsOfConstant.EMPTY_STRING_ARRAY;
//        }
//        string = string.trim();
//        final List<String> tokens = new ArrayList<>();
//        final int totalLength = string.length();
//        final StringBuilder sb = new StringBuilder();
//        final Machining<String> trimMachining = s -> isTrimWhitespace ? s.trim() : s;
//        final BiMachining<Boolean, Character> quoteMachining = (b, c) -> {
//            if (retainQuotes) {
//                sb.append(c);
//            }
//            return !b;
//        };
//        //是否在符号内
//        boolean inQuotes = false;
//        int escapeCount = 0;
//        for (int i = 0; i < totalLength; ++i) {
//            final char c = string.charAt(i);
//            boolean isEscape = escapeCount % 2 != 0;
//            if (c == beginQuoteChar || c == endQuoteChar) {
//                if (isEscape) {//如果转义了
//                    escapeCount = 0;
//                    sb.append(c);
//                } else {
//                    if (c == beginQuoteChar) {
//                        if (!inQuotes) {
//                            inQuotes = quoteMachining.apply(inQuotes, c);
//                        } else {
//                            //前后符合相同说明遇到了结尾符号
//                            if (beginQuoteChar == endQuoteChar) {
//                                inQuotes = quoteMachining.apply(inQuotes, c);
//                            }
//                        }
//                        //清除多余的开始符号
//                        while (i + 1 < totalLength && string.charAt(i + 1) == beginQuoteChar) {
//                            ++i;
//                        }
//                    } else {
//                        if (inQuotes) {
//                            inQuotes = quoteMachining.apply(inQuotes, c);
//                            //清除多余的开始符号
//                            while (i + 1 < totalLength && string.charAt(i + 1) == endQuoteChar) {
//                                ++i;
//                            }
//                        }
//                    }
//                }
//            } else if (c == delimiter && !inQuotes) {
//                if (isEscape) {
//                    escapeCount = 0;
//                    sb.append(c);
//                } else {
//                    tokens.add(trimMachining.apply(sb.toString()));
//                    sb.delete(0, Integer.MAX_VALUE);
//                }
//            } else {
//                //是转义字符
//                if (isEnableEscape && c == UtilsOfConstant.C_ESCAPE) {
//                    escapeCount++;
//                } else {
//                    sb.append(c);
//                }
//            }
//        }
//        tokens.add(trimMachining.apply(sb.toString()));
//        for (int i = 0; i < removeIndexes.length; i++) {
//            int ri = UtilsOfFunction.index(removeIndexes[i], tokens.size());
//            tokens.remove(ri - i);
//        }
//        return tokens.toArray(new String[0]);
//    }
//
//    /**
//     * 截取字符串
//     *
//     * @param string 字符串
//     * @param start  开始
//     * @return 结果
//     */
//    public String substring(final String string, final int start) {
//        return substring(string, start, -1);
//    }
//
//    /**
//     * 截取字符串
//     *
//     * @param string 字符串
//     * @param start  开始
//     * @param end    结束
//     * @return 结果
//     */
//    public String substring(final String string, int start, int end) {
//        if (UtilsOfJudge.isInvalid(string)) {
//            return "";
//        }
//        final int len = string.length();
//        start = UtilsOfFunction.index(start, len);
//        end = UtilsOfFunction.index(end, len);
//        return string.substring(start, end);
//    }
//
//    /**
//     * 是否包含字符串
//     *
//     * @param string       验证字符串
//     * @param isIgnoreCase 是否
//     * @param patterns     包含的字符串组
//     * @return 全含则返回true，否则返回false
//     */
//    public boolean isContainAll(final String string, final boolean isIgnoreCase,
//                                final String... patterns) {
//        int matchCount = 0;
//        for (final String pattern : patterns) {
//            final boolean b = isContain(string, pattern, isIgnoreCase);
//            if (b) {
//                matchCount++;
//            }
//        }
//        return matchCount == patterns.length;
//    }
//
//    /**
//     * 匹配字符串，默认匹配第一个
//     *
//     * @param string       目标字符串
//     * @param pattern      需要匹配的字符串
//     * @param isIgnoreCase 是否忽略大小写
//     * @return true为包含否则为false
//     */
//    public boolean isContain(final String string, final String pattern, final boolean isIgnoreCase) {
//        return indexOf(string, pattern, isIgnoreCase) != -1;
//    }
//
//    /**
//     * 是否包含字符串
//     *
//     * @param string       验证字符串
//     * @param isIgnoreCase 是否
//     * @param patterns     包含的字符串组
//     * @return 全含则返回true，否则返回false
//     */
//    public boolean isContainOne(final String string, final boolean isIgnoreCase,
//                                final String... patterns) {
//        for (final String pattern : patterns) {
//            final boolean b = isContain(string, pattern, isIgnoreCase);
//            if (b) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param string        字符串
//     * @param pattern       模式字符串
//     * @param isIgnoredCase 是否忽略大小写
//     * @return 首次出现的索引，当没有任何匹配时返回-1
//     */
//    public static int indexOf(final String string, final String pattern, final boolean isIgnoredCase) {
//        return indexOf(string, pattern, isIgnoredCase, 0);
//    }
//
//    /**
//     * @param string        字符串
//     * @param pattern       模式字符串
//     * @param isIgnoredCase 是否忽略大小写
//     * @return 首次出现的索引，当没有任何匹配时返回-1
//     */
//    public static int indexOf(final String string, final String pattern, final boolean isIgnoredCase,
//                              final int fromIndex) {
//        if (UtilsOfJudge.isInvalid(string) || UtilsOfJudge.isInvalid(pattern)) {
//            return -1;
//        }
//        return indexOf(string.toCharArray(), pattern.toCharArray(), isIgnoredCase, fromIndex);
//    }
//
//    /**
//     * 匹配第一个相同的字符序列
//     *
//     * @param chars    字符序列
//     * @param patterns 需要匹配的模式字符序列
//     * @return 返回搜索到的首字符序列索引，当没有任何匹配时返回-1
//     */
//    public static int indexOf(final char[] chars, final char[] patterns) {
//        return indexOf(chars, patterns, false);
//    }
//
//    /**
//     * 匹配第一个相同的字符序列
//     *
//     * @param chars         字符序列
//     * @param patterns      需要匹配的模式字符序列
//     * @param isIgnoredCase 是否忽略大小写
//     * @return 返回搜索到的首字符序列索引，当没有任何匹配时返回-1
//     */
//    public static int indexOf(final char[] chars, final char[] patterns,
//                              final boolean isIgnoredCase) {
//        return indexOf(chars, patterns, isIgnoredCase, 0);
//    }
//
//    /**
//     * 匹配第一个相同的字符序列
//     *
//     * @param chars         字符序列
//     * @param patterns      需要匹配的模式字符序列
//     * @param isIgnoredCase 是否忽略大小写
//     * @param fromIndex     从字符序列哪个起始索引开始匹配，必须大于等于0
//     * @return 返回搜索到的首字符序列索引，当没有任何匹配时返回-1
//     */
//    public static int indexOf(final char[] chars, final char[] patterns, final boolean isIgnoredCase,
//                              final int fromIndex) {
//        if (UtilsOfJudge.isEmpty(chars)) {
//            return -1;
//        }
//        if (UtilsOfJudge.isEmpty(patterns)) {
//            return 0;
//        }
//        if (fromIndex < 0) {
//            throw new RuntimeException("Don't allow fromIndex less than 0!");
//        }
//        return isIgnoredCase ? findFirstMatchIgnoredCase(chars, chars.length, patterns, patterns.length,
//                fromIndex
//        ) : findFirstMatch(chars, chars.length, patterns, patterns.length, fromIndex);
//    }
//
//    /**
//     * 匹配第一个字符序列
//     *
//     * @param chars     目标字符序列
//     * @param sLen      字符序列的最大长度
//     * @param patterns  需要匹配的模式字符序列
//     * @param pLen      模式字符序列的匹配长度
//     * @param fromIndex 从字符序列哪个起始索引开始匹配，必须大于等于0
//     * @return 返回搜索到的首字符索引，当没有任何匹配时返回-1
//     */
//    public static int findFirstMatchIgnoredCase(final char[] chars,
//                                                final int sLen,
//                                                final char[] patterns,
//                                                final int pLen,
//                                                final int fromIndex) {
//        final int maxPatternIndex = pLen - 1;
//        final int limit = sLen - pLen;
//        int i = fromIndex, j = 0;
//        while (i < limit) {
//            if (Character.toLowerCase(chars[i]) == Character.toLowerCase(patterns[j])) {
//                if (j == maxPatternIndex) {
//                    return i - j;
//                }
//                i++;
//                j++;
//                continue;
//            }
//            //主串移动模式串长度后的后一个索引
//            final int postNextI = i + pLen - j;
//            final int pos = backwardFoundFirstArise(patterns, maxPatternIndex, chars[postNextI], true);
//            i = pos == -1 ? postNextI + 1 : postNextI - pos;
//            j = 0;
//        }
//        for (j = 0; i < sLen && Character.toLowerCase(chars[i]) == Character.toLowerCase(patterns[j]);
//             i++, j++) {
//            if (j == maxPatternIndex) {
//                return i - j;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 匹配第一个字符序列
//     *
//     * @param chars     目标字符序列
//     * @param sLen      字符序列的最大长度
//     * @param patterns  需要匹配的模式字符序列
//     * @param pLen      模式字符序列的匹配长度
//     * @param fromIndex 从字符序列哪个起始索引开始匹配，必须大于等于0
//     * @return 返回搜索到的首字符索引，当没有任何匹配时返回-1
//     */
//    public static int findFirstMatch(final char[] chars,
//                                     final int sLen,
//                                     final char[] patterns,
//                                     final int pLen,
//                                     final int fromIndex) {
//        final int maxPatternIndex = pLen - 1;
//        final int limit = sLen - pLen;
//        int i = fromIndex, j = 0;
//        while (i < limit) {
//            if (chars[i] == patterns[j]) {
//                if (j == maxPatternIndex) {
//                    return i - j;
//                }
//                i++;
//                j++;
//                continue;
//            }
//            //主串移动模式串长度后的后一个索引
//            final int postNextI = i + pLen - j;
//            final int pos = backwardFoundFirstArise(patterns, maxPatternIndex, chars[postNextI], false);
//            i = pos == -1 ? postNextI + 1 : postNextI - pos;
//            j = 0;
//        }
//        for (j = 0; i < sLen && chars[i] == patterns[j]; i++, j++) {
//            if (j == maxPatternIndex) {
//                return i - j;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 反向找出出现第一个索引位置
//     *
//     * @param chars         字符序列
//     * @param fromIndex     从哪个索引开始
//     * @param target        需要找出的字符
//     * @param isIgnoredCase 是否忽略大小写
//     * @return 返回所在的索引，不存在返回-1
//     */
//    protected static int backwardFoundFirstArise(final char[] chars,
//                                                 final int fromIndex,
//                                                 final char target,
//                                                 final boolean isIgnoredCase) {
//        if (isIgnoredCase) {
//            for (int p = fromIndex; p >= 0; p--) {
//                if (Character.toLowerCase(target) == Character.toLowerCase(chars[p])) {
//                    return p;
//                }
//            }
//        } else {
//            for (int p = fromIndex; p >= 0; p--) {
//                if (target == chars[p]) {
//                    return p;
//                }
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param input  显示对象
//     * @param length 固定长度
//     * @param pad    填充字符
//     * @param isLeft 是否靠左
//     */
//    public static String layout(
//            final CharSequence input, final int length, final char pad, final boolean isLeft) {
//        CharSequenceAppendable charSequenceAppendable = CharSequenceAppendable.ofStringBuilder();
//        layout(charSequenceAppendable, input, length, pad, isLeft);
//        return charSequenceAppendable.toString();
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param container 字符串容器
//     * @param input     显示对象
//     * @param length    固定长度
//     * @param pad       填充字符
//     * @param isLeft    是否靠左
//     */
//    public static void layout(
//            final CharSequenceAppendable container,
//            final CharSequence input,
//            final int length,
//            final char pad,
//            final boolean isLeft) {
//        if (input == null || input.length() == 0) {
//            return;
//        }
//        if (length <= input.length()) {
//            container.append(input);
//            return;
//        }
//        if (isLeft) {
//            container.append(input);
//            repeat(container, pad, length - input.length());
//        } else {
//            repeat(container, pad, length - input.length());
//            container.append(input);
//        }
//    }

//
//    public static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 被包含的第一个字符串
//     */
//    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
//        if (Whether.empty(str) || Whether.empty(testStrs)) {
//            return null;
//        }
//        for (CharSequence checkStr : testStrs) {
//            if (str.toString().contains(checkStr)) {
//                return checkStr.toString();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 是否包含特定字符，忽略大小写，如果给定两个参数都为{@code null}，返回true
//     *
//     * @param str     被检测字符串
//     * @param testStr 被测试是否包含的字符串
//     * @return 是否包含
//     */
//    public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
//        if (null == str) {
//            // 如果被监测字符串和
//            return null == testStr;
//        }
//        return indexOfIgnoreCase(str, testStr) > -1;
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
//     * 忽略大小写
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 是否包含任意一个字符串
//     */
//    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
//        return null != getContainsStrIgnoreCase(str, testStrs);
//    }
//
//    /**
//     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
//     * 忽略大小写
//     *
//     * @param str      指定字符串
//     * @param testStrs 需要检查的字符串数组
//     * @return 被包含的第一个字符串
//     */
//    public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
//        if (Whether.empty(str) || Whether.empty(testStrs)) {
//            return null;
//        }
//        for (CharSequence testStr : testStrs) {
//            if (containsIgnoreCase(str, testStr)) {
//                return testStr.toString();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 移除字符串中所有给定字符串<br>
//     * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
//     *
//     * @param str         字符串
//     * @param strToRemove 被移除的字符串
//     * @return 移除后的字符串
//     */
//    public static String removeAll(CharSequence str, CharSequence strToRemove) {
//        // strToRemove如果为空， 也不用继续后面的逻辑
//        if (Whether.empty(str) || Whether.empty(strToRemove)) {
//            return str(str);
//        }
//        return str.toString().replace(strToRemove, PoolOfString.EMPTY);
//    }
//
//    /**
//     * 移除字符串中所有给定字符串，当某个字符串出现多次，则全部移除<br>
//     * 例：removeAny("aa-bb-cc-dd", "a", "b") =》 --cc-dd
//     *
//     * @param str          字符串
//     * @param strsToRemove 被移除的字符串
//     * @return 移除后的字符串
//     */
//    public static String removeAny(CharSequence str, CharSequence... strsToRemove) {
//        String result = str(str);
//        if (Whether.noEmpty(str)) {
//            for (CharSequence strToRemove : strsToRemove) {
//                result = removeAll(result, strToRemove);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 去除字符串中指定的多个字符，如有多个则全部去除
//     *
//     * @param str   字符串
//     * @param chars 字符列表
//     * @return 去除后的字符
//     */
//    public static String removeAll(CharSequence str, char... chars) {
//        if (null == str || Whether.empty(chars)) {
//            return str(str);
//        }
//        final int len = str.length();
//        if (0 == len) {
//            return str(str);
//        }
//        final StringBuilder builder = new StringBuilder(len);
//        char c;
//        for (int i = 0; i < len; i++) {
//            c = str.charAt(i);
//            if (false == ToolArray.contains(chars, c)) {
//                builder.append(c);
//            }
//        }
//        return builder.toString();
//    }
//
//    /**
//     * 去除所有换行符，包括：
//     *
//     * <pre>
//     * 1. \r
//     * 1. \n
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 处理后的字符串
//     */
//    public static String removeAllLineBreaks(CharSequence str) {
//        return removeAll(str, PoolOfCharacter.CR, PoolOfCharacter.LF);
//    }
//
//    /**
//     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
//     * 例如：str=setName, preLength=3 =》 return name
//     *
//     * @param str       被处理的字符串
//     * @param preLength 去掉的长度
//     * @return 处理后的字符串，不符合规范返回null
//     */
//    public static String removePreAndLowerFirst(CharSequence str, int preLength) {
//        if (str == null) {
//            return null;
//        }
//        if (str.length() > preLength) {
//            char first = Character.toLowerCase(str.charAt(preLength));
//            if (str.length() > preLength + 1) {
//                return first + str.toString().substring(preLength + 1);
//            }
//            return String.valueOf(first);
//        } else {
//            return str.toString();
//        }
//    }
//
//    /**
//     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
//     * 例如：str=setName, prefix=set =》 return name
//     *
//     * @param str    被处理的字符串
//     * @param prefix 前缀
//     * @return 处理后的字符串，不符合规范返回null
//     */
//    public static String removePreAndLowerFirst(CharSequence str, CharSequence prefix) {
//        return lowerFirst(removePrefix(str, prefix));
//    }
//
//    /**
//     * 去掉指定前缀
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
//     */
//    public static String removePrefix(CharSequence str, CharSequence prefix) {
//        if (Whether.empty(str) || Whether.empty(prefix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (str2.startsWith(prefix.toString())) {
//            return subSuf(str2, prefix.length()); // 截取后半段
//        }
//        return str2;
//    }
//
//    /**
//     * 忽略大小写去掉指定前缀
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
//     */
//    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
//        if (Whether.empty(str) || Whether.empty(prefix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (startWithIgnoreCase(str, prefix)) {
//            return subSuf(str2, prefix.length()); // 截取后半段
//        }
//        return str2;
//    }
//
//    /**
//     * 去掉指定后缀
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSuffix(CharSequence str, CharSequence suffix) {
//        if (Whether.empty(str) || Whether.empty(suffix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (str2.endsWith(suffix.toString())) {
//            return subPre(str2, str2.length() - suffix.length()); // 截取前半段
//        }
//        return str2;
//    }
//
//    /**
//     * 去掉指定后缀，并小写首字母
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
//        return lowerFirst(removeSuffix(str, suffix));
//    }
//
//    /**
//     * 忽略大小写去掉指定后缀
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
//     */
//    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
//        if (Whether.empty(str) || Whether.empty(suffix)) {
//            return str(str);
//        }
//
//        final String str2 = str.toString();
//        if (endWithIgnoreCase(str, suffix)) {
//            return subPre(str2, str2.length() - suffix.length());
//        }
//        return str2;
//    }
//
//    /**
//     * 清理空白字符
//     *
//     * @param str 被清理的字符串
//     * @return 清理后的字符串
//     */
//    public static String cleanBlank(CharSequence str) {
//        return filter(str, c -> false == ToolChar.isBlankChar(c));
//    }
//
//    // ------------------------------------------------------------------------ add
//
//    /**
//     * 如果给定字符串不是以prefix开头的，在开头补充 prefix
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @return 补充后的字符串
//     * @see #prependIfMissing(CharSequence, CharSequence, CharSequence...)
//     */
//    public static String addPrefixIfNot(CharSequence str, CharSequence prefix) {
//        return prependIfMissing(str, prefix, prefix);
//    }
//
//    /**
//     * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
//     *
//     * @param str    字符串
//     * @param suffix 后缀
//     * @return 补充后的字符串
//     * @see #appendIfMissing(CharSequence, CharSequence, CharSequence...)
//     */
//    public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
//        return appendIfMissing(str, suffix, suffix);
//    }
//
//
//    /**
//     * 将字符串切分为N等份
//     *
//     * @param str        字符串
//     * @param partLength 每等份的长度
//     * @return 切分后的数组
//     */
//    public static String[] cut(CharSequence str, int partLength) {
//        if (null == str) {
//            return null;
//        }
//        int len = str.length();
//        if (len < partLength) {
//            return new String[]{str.toString()};
//        }
//        int part = ToolNumber.count(len, partLength);
//        final String[] array = new String[part];
//
//        final String str2 = str.toString();
//        for (int i = 0; i < part; i++) {
//            array[i] =
//                    str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
//        }
//        return array;
//    }
//
//    // ------------------------------------------------------------------------ equals
//
//    /**
//     * 比较两个字符串（大小写敏感）。
//     *
//     * <pre>
//     * equals(null, null)   = true
//     * equals(null, &quot;abc&quot;)  = false
//     * equals(&quot;abc&quot;, null)  = false
//     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
//     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
//     * </pre>
//     *
//     * @param str1 要比较的字符串1
//     * @param str2 要比较的字符串2
//     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
//     */
//    public static boolean equals(CharSequence str1, CharSequence str2) {
//        return equals(str1, str2, false);
//    }
//
//    /**
//     * 比较两个字符串（大小写不敏感）。
//     *
//     * <pre>
//     * equalsIgnoreCase(null, null)   = true
//     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
//     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
//     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
//     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
//     * </pre>
//     *
//     * @param str1 要比较的字符串1
//     * @param str2 要比较的字符串2
//     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
//     */
//    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
//        return equals(str1, str2, true);
//    }
//
//    /**
//     * 比较两个字符串是否相等，规则如下
//     *
//     * <ul>
//     *   <li>str1和str2都为{@code null}
//     *   <li>忽略大小写使用{@link String#equalsIgnoreCase(String)}判断相等
//     *   <li>不忽略大小写使用{@link String#contentEquals(CharSequence)}判断相等
//     * </ul>
//     *
//     * @param str1       要比较的字符串1
//     * @param str2       要比较的字符串2
//     * @param ignoreCase 是否忽略大小写
//     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
//     */
//    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
//        if (null == str1) {
//            // 只有两个都为null才判断相等
//            return str2 == null;
//        }
//        if (null == str2) {
//            // 字符串2空，字符串1非空，直接false
//            return false;
//        }
//
//        if (ignoreCase) {
//            return str1.toString().equalsIgnoreCase(str2.toString());
//        } else {
//            return str1.toString().contentEquals(str2);
//        }
//    }
//
//    /**
//     * 给定字符串是否与提供的中任一字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
//     * 如果参与比对的字符串列表为空，返回{@code false}
//     *
//     * @param str1 给定需要检查的字符串
//     * @param strs 需要参与比对的字符串列表
//     * @return 是否相同
//     */
//    public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
//        return equalsAny(str1, true, strs);
//    }
//
//    /**
//     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
//     * 如果参与比对的字符串列表为空，返回{@code false}
//     *
//     * @param str1 给定需要检查的字符串
//     * @param strs 需要参与比对的字符串列表
//     * @return 是否相同
//     */
//    public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
//        return equalsAny(str1, false, strs);
//    }
//
//    /**
//     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
//     * 如果参与比对的字符串列表为空，返回{@code false}
//     *
//     * @param str1       给定需要检查的字符串
//     * @param ignoreCase 是否忽略大小写
//     * @param strs       需要参与比对的字符串列表
//     * @return 是否相同
//     */
//    public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
//        if (Whether.empty(strs)) {
//            return false;
//        }
//
//        for (CharSequence str : strs) {
//            if (equals(str1, str, ignoreCase)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 字符串指定位置的字符是否与给定字符相同<br>
//     * 如果字符串为null，返回false<br>
//     * 如果给定的位置大于字符串长度，返回false<br>
//     * 如果给定的位置小于0，返回false
//     *
//     * @param str      字符串
//     * @param position 位置
//     * @param c        需要对比的字符
//     * @return 字符串指定位置的字符是否与给定字符相同
//     */
//    public static boolean equalsCharAt(CharSequence str, int position, char c) {
//        if (null == str || position < 0) {
//            return false;
//        }
//        return str.length() > position && c == str.charAt(position);
//    }
//
//    /**
//     * 截取第一个字串的部分字符，与第二个字符串比较（长度一致），判断截取的子串是否相同<br>
//     * 任意一个字符串为null返回false
//     *
//     * @param str1       第一个字符串
//     * @param start1     第一个字符串开始的位置
//     * @param str2       第二个字符串
//     * @param ignoreCase 是否忽略大小写
//     * @return 子串是否相同
//     */
//    public static boolean isSubEquals(
//            CharSequence str1, int start1, CharSequence str2, boolean ignoreCase) {
//        return isSubEquals(str1, start1, str2, 0, str2.length(), ignoreCase);
//    }
//
//    /**
//     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
//     * 任意一个字符串为null返回false
//     *
//     * @param str1       第一个字符串
//     * @param start1     第一个字符串开始的位置
//     * @param str2       第二个字符串
//     * @param start2     第二个字符串开始的位置
//     * @param length     截取长度
//     * @param ignoreCase 是否忽略大小写
//     * @return 子串是否相同
//     */
//    public static boolean isSubEquals(
//            CharSequence str1,
//            int start1,
//            CharSequence str2,
//            int start2,
//            int length,
//            boolean ignoreCase) {
//        if (null == str1 || null == str2) {
//            return false;
//        }
//
//        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
//    }
//
//    // ------------------------------------------------------------------------ format
//
//    /**
//     * 有序的格式化文本，使用{number}做为占位符<br>
//     * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
//     *
//     * @param pattern   文本格式
//     * @param arguments 参数
//     * @return 格式化后的文本
//     */
//    public static String indexedFormat(CharSequence pattern, Object... arguments) {
//        return MessageFormat.format(pattern.toString(), arguments);
//    }
//
//    // ------------------------------------------------------------------------ wrap
//
//    /**
//     * 包装指定字符串<br>
//     * 当前缀和后缀一致时使用此方法
//     *
//     * @param str             被包装的字符串
//     * @param prefixAndSuffix 前缀和后缀
//     * @return 包装后的字符串
//     */
//    public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
//        return wrap(str, prefixAndSuffix, prefixAndSuffix);
//    }
//
//    /**
//     * 包装指定字符串
//     *
//     * @param str    被包装的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 包装后的字符串
//     */
//    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        return Safe.value(prefix).concat(Safe.value(str)).concat(Safe.value(suffix));
//    }
//
//    /**
//     * 使用单个字符包装多个字符串
//     *
//     * @param prefixAndSuffix 前缀和后缀
//     * @param strs            多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
//        return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
//    }
//
//    /**
//     * 包装多个字符串
//     *
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @param strs   多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
//        final String[] results = new String[strs.length];
//        for (int i = 0; i < strs.length; i++) {
//            results[i] = wrap(strs[i], prefix, suffix);
//        }
//        return results;
//    }
//
//    /**
//     * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
//     *
//     * @param str    被包装的字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 包装后的字符串
//     */
//    public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
//        int len = 0;
//        if (Whether.noEmpty(str)) {
//            len += str.length();
//        }
//        if (Whether.noEmpty(prefix)) {
//            len += prefix.length();
//        }
//        if (Whether.noEmpty(suffix)) {
//            len += suffix.length();
//        }
//        StringBuilder sb = new StringBuilder(len);
//        if (Whether.noEmpty(prefix) && false == startWith(str, prefix)) {
//            sb.append(prefix);
//        }
//        if (Whether.noEmpty(str)) {
//            sb.append(str);
//        }
//        if (Whether.noEmpty(suffix) && false == endWith(str, suffix)) {
//            sb.append(suffix);
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 使用成对的字符包装多个字符串，如果已经包装，则不再包装
//     *
//     * @param prefixAndSuffix 前缀和后缀
//     * @param strs            多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllWithPairIfMissing(
//            CharSequence prefixAndSuffix, CharSequence... strs) {
//        return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
//    }
//
//    /**
//     * 包装多个字符串，如果已经包装，则不再包装
//     *
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @param strs   多个字符串
//     * @return 包装的字符串数组
//     */
//    public static String[] wrapAllIfMissing(
//            CharSequence prefix, CharSequence suffix, CharSequence... strs) {
//        final String[] results = new String[strs.length];
//        for (int i = 0; i < strs.length; i++) {
//            results[i] = wrapIfMissing(strs[i], prefix, suffix);
//        }
//        return results;
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str    字符串
//     * @param prefix 前置字符串
//     * @param suffix 后置字符串
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, String prefix, String suffix) {
//        if (isWrap(str, prefix, suffix)) {
//            return sub(str, prefix.length(), str.length() - suffix.length());
//        }
//        return str.toString();
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str    字符串
//     * @param prefix 前置字符
//     * @param suffix 后置字符
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, char prefix, char suffix) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
//            return sub(str, 1, str.length() - 1);
//        }
//        return str.toString();
//    }
//
//    /**
//     * 去掉字符包装，如果未被包装则返回原字符串
//     *
//     * @param str             字符串
//     * @param prefixAndSuffix 前置和后置字符
//     * @return 去掉包装字符的字符串
//     */
//    public static String unWrap(CharSequence str, char prefixAndSuffix) {
//        return unWrap(str, prefixAndSuffix, prefixAndSuffix);
//    }
//
//    /**
//     * 指定字符串是否被包装
//     *
//     * @param str    字符串
//     * @param prefix 前缀
//     * @param suffix 后缀
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, String prefix, String suffix) {
//        if (Whether.anyNvl(str, prefix, suffix)) {
//            return false;
//        }
//        final String str2 = str.toString();
//        return str2.startsWith(prefix) && str2.endsWith(suffix);
//    }
//
//    /**
//     * 指定字符串是否被同一字符包装（前后都有这些字符串）
//     *
//     * @param str     字符串
//     * @param wrapper 包装字符串
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, String wrapper) {
//        return isWrap(str, wrapper, wrapper);
//    }
//
//    /**
//     * 指定字符串是否被同一字符包装（前后都有这些字符串）
//     *
//     * @param str     字符串
//     * @param wrapper 包装字符
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, char wrapper) {
//        return isWrap(str, wrapper, wrapper);
//    }
//
//    /**
//     * 指定字符串是否被包装
//     *
//     * @param str        字符串
//     * @param prefixChar 前缀
//     * @param suffixChar 后缀
//     * @return 是否被包装
//     */
//    public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
//        if (null == str) {
//            return false;
//        }
//
//        return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
//    }
//
//    // ------------------------------------------------------------------------ pad
//
//    /**
//     * 补充字符串以满足指定长度，如果提供的字符串大于指定长度，截断之 同：leftPad (org.apache.commons.lang3.leftPad)
//     *
//     * <pre>
//     * StrUtil.padPre(null, *, *);//null
//     * StrUtil.padPre("1", 3, "ABC");//"AB1"
//     * StrUtil.padPre("123", 2, "ABC");//"12"
//     * StrUtil.padPre("1039", -1, "0");//"103"
//     * </pre>
//     *
//     * @param str    字符串
//     * @param length 长度
//     * @param padStr 补充的字符
//     * @return 补充后的字符串
//     */
//    public static String padPre(CharSequence str, int length, CharSequence padStr) {
//        if (null == str) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen == length) {
//            return str.toString();
//        } else if (strLen > length) {
//            // 如果提供的字符串大于指定长度，截断之
//            return subPre(str, length);
//        }
//
//        return repeatByLength(padStr, length - strLen).concat(str.toString());
//    }
//
//    /**
//     * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之 同：leftPad (org.apache.commons.lang3.leftPad)
//     *
//     * <pre>
//     * StrUtil.padPre(null, *, *);//null
//     * StrUtil.padPre("1", 3, '0');//"001"
//     * StrUtil.padPre("123", 2, '0');//"12"
//     * </pre>
//     *
//     * @param str     字符串
//     * @param length  长度
//     * @param padChar 补充的字符
//     * @return 补充后的字符串
//     */
//    public static String padPre(CharSequence str, int length, char padChar) {
//        if (null == str) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen == length) {
//            return str.toString();
//        } else if (strLen > length) {
//            // 如果提供的字符串大于指定长度，截断之
//            return subPre(str, length);
//        }
//
//        return repeat(padChar, length - strLen).concat(str.toString());
//    }
//
//    /**
//     * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之
//     *
//     * <pre>
//     * StrUtil.padAfter(null, *, *);//null
//     * StrUtil.padAfter("1", 3, '0');//"100"
//     * StrUtil.padAfter("123", 2, '0');//"23"
//     * StrUtil.padAfter("123", -1, '0')//"" 空串
//     * </pre>
//     *
//     * @param str     字符串，如果为{@code null}，直接返回null
//     * @param length  长度
//     * @param padChar 补充的字符
//     * @return 补充后的字符串
//     */
//    public static String padAfter(CharSequence str, int length, char padChar) {
//        if (null == str) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen == length) {
//            return str.toString();
//        } else if (strLen > length) {
//            // 如果提供的字符串大于指定长度，截断之
//            return sub(str, strLen - length, strLen);
//        }
//
//        return str.toString().concat(repeat(padChar, length - strLen));
//    }
//
//    /**
//     * 补充字符串以满足最小长度
//     *
//     * <pre>
//     * StrUtil.padAfter(null, *, *);//null
//     * StrUtil.padAfter("1", 3, "ABC");//"1AB"
//     * StrUtil.padAfter("123", 2, "ABC");//"23"
//     * </pre>
//     *
//     * @param str    字符串，如果为{@code null}，直接返回null
//     * @param length 长度
//     * @param padStr 补充的字符
//     * @return 补充后的字符串
//     */
//    public static String padAfter(CharSequence str, int length, CharSequence padStr) {
//        if (null == str) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen == length) {
//            return str.toString();
//        } else if (strLen > length) {
//            // 如果提供的字符串大于指定长度，截断之
//            return subSufByLength(str, length);
//        }
//
//        return str.toString().concat(repeatByLength(padStr, length - strLen));
//    }
//
//    // ------------------------------------------------------------------------ center
//
//    /**
//     * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
//     *
//     * <pre>
//     * StrUtil.center(null, *)   = null
//     * StrUtil.center("", 4)     = "    "
//     * StrUtil.center("ab", -1)  = "ab"
//     * StrUtil.center("ab", 4)   = " ab "
//     * StrUtil.center("abcd", 2) = "abcd"
//     * StrUtil.center("a", 4)    = " a  "
//     * </pre>
//     *
//     * @param str  字符串
//     * @param size 指定长度
//     * @return 补充后的字符串
//     */
//    public static String center(CharSequence str, final int size) {
//        return center(str, size, PoolOfCharacter.SPACE);
//    }
//
//    /**
//     * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
//     *
//     * <pre>
//     * StrUtil.center(null, *, *)     = null
//     * StrUtil.center("", 4, ' ')     = "    "
//     * StrUtil.center("ab", -1, ' ')  = "ab"
//     * StrUtil.center("ab", 4, ' ')   = " ab "
//     * StrUtil.center("abcd", 2, ' ') = "abcd"
//     * StrUtil.center("a", 4, ' ')    = " a  "
//     * StrUtil.center("a", 4, 'y')   = "yayy"
//     * StrUtil.center("abc", 7, ' ')   = "  abc  "
//     * </pre>
//     *
//     * @param str     字符串
//     * @param size    指定长度
//     * @param padChar 两边补充的字符
//     * @return 补充后的字符串
//     */
//    public static String center(CharSequence str, final int size, char padChar) {
//        if (str == null || size <= 0) {
//            return str(str);
//        }
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str.toString();
//        }
//        str = padPre(str, strLen + pads / 2, padChar);
//        str = padAfter(str, size, padChar);
//        return str.toString();
//    }
//
//    /**
//     * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
//     *
//     * <pre>
//     * StrUtil.center(null, *, *)     = null
//     * StrUtil.center("", 4, " ")     = "    "
//     * StrUtil.center("ab", -1, " ")  = "ab"
//     * StrUtil.center("ab", 4, " ")   = " ab "
//     * StrUtil.center("abcd", 2, " ") = "abcd"
//     * StrUtil.center("a", 4, " ")    = " a  "
//     * StrUtil.center("a", 4, "yz")   = "yayz"
//     * StrUtil.center("abc", 7, null) = "  abc  "
//     * StrUtil.center("abc", 7, "")   = "  abc  "
//     * </pre>
//     *
//     * @param str    字符串
//     * @param size   指定长度
//     * @param padStr 两边补充的字符串
//     * @return 补充后的字符串
//     */
//    public static String center(CharSequence str, final int size, CharSequence padStr) {
//        if (str == null || size <= 0) {
//            return str(str);
//        }
//        if (Whether.empty(padStr)) {
//            padStr = PoolOfString.SPACE;
//        }
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str.toString();
//        }
//        str = padPre(str, strLen + pads / 2, padStr);
//        str = padAfter(str, size, padStr);
//        return str.toString();
//    }
//
//    // ------------------------------------------------------------------------ str
//
//    /**
//     * {@link CharSequence} 转为字符串，null安全
//     *
//     * @param cs {@link CharSequence}
//     * @return 字符串
//     */
//    public static String str(CharSequence cs) {
//        return null == cs ? null : cs.toString();
//    }
//
//    // ------------------------------------------------------------------------ count
//
//    /**
//     * 统计指定内容中包含指定字符串的数量<br>
//     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
//     *
//     * <pre>
//     * StrUtil.count(null, *)       = 0
//     * StrUtil.count("", *)         = 0
//     * StrUtil.count("abba", null)  = 0
//     * StrUtil.count("abba", "")    = 0
//     * StrUtil.count("abba", "a")   = 2
//     * StrUtil.count("abba", "ab")  = 1
//     * StrUtil.count("abba", "xxx") = 0
//     * </pre>
//     *
//     * @param content      被查找的字符串
//     * @param strForSearch 需要查找的字符串
//     * @return 查找到的个数
//     */
//    public static int count(CharSequence content, CharSequence strForSearch) {
//        if (Whether.empty(content)
//                || Whether.empty(strForSearch)
//                || strForSearch.length() > content.length()) {
//            return 0;
//        }
//
//        int count = 0;
//        int idx = 0;
//        final String content2 = content.toString();
//        final String strForSearch2 = strForSearch.toString();
//        while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
//            count++;
//            idx += strForSearch.length();
//        }
//        return count;
//    }
//
//    /**
//     * 统计指定内容中包含指定字符的数量
//     *
//     * @param content       内容
//     * @param charForSearch 被统计的字符
//     * @return 包含数量
//     */
//    public static int count(CharSequence content, char charForSearch) {
//        int count = 0;
//        if (Whether.empty(content)) {
//            return 0;
//        }
//        int contentLength = content.length();
//        for (int i = 0; i < contentLength; i++) {
//            if (charForSearch == content.charAt(i)) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    // ------------------------------------------------------------------------ compare
//
//    /**
//     * 比较两个字符串，用于排序
//     *
//     * <pre>
//     * StrUtil.compare(null, null, *)     = 0
//     * StrUtil.compare(null , "a", true)  &lt; 0
//     * StrUtil.compare(null , "a", false) &gt; 0
//     * StrUtil.compare("a", null, true)   &gt; 0
//     * StrUtil.compare("a", null, false)  &lt; 0
//     * StrUtil.compare("abc", "abc", *)   = 0
//     * StrUtil.compare("a", "b", *)       &lt; 0
//     * StrUtil.compare("b", "a", *)       &gt; 0
//     * StrUtil.compare("a", "B", *)       &gt; 0
//     * StrUtil.compare("ab", "abc", *)    &lt; 0
//     * </pre>
//     *
//     * @param str1       字符串1
//     * @param str2       字符串2
//     * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
//     * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
//     */
//    public static int compare(
//            final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
//        if (str1 == str2) {
//            return 0;
//        }
//        if (str1 == null) {
//            return nullIsLess ? -1 : 1;
//        }
//        if (str2 == null) {
//            return nullIsLess ? 1 : -1;
//        }
//        return str1.toString().compareTo(str2.toString());
//    }
//
//    /**
//     * 比较两个字符串，用于排序，大小写不敏感
//     *
//     * <pre>
//     * StrUtil.compareIgnoreCase(null, null, *)     = 0
//     * StrUtil.compareIgnoreCase(null , "a", true)  &lt; 0
//     * StrUtil.compareIgnoreCase(null , "a", false) &gt; 0
//     * StrUtil.compareIgnoreCase("a", null, true)   &gt; 0
//     * StrUtil.compareIgnoreCase("a", null, false)  &lt; 0
//     * StrUtil.compareIgnoreCase("abc", "abc", *)   = 0
//     * StrUtil.compareIgnoreCase("abc", "ABC", *)   = 0
//     * StrUtil.compareIgnoreCase("a", "b", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("b", "a", *)       &gt; 0
//     * StrUtil.compareIgnoreCase("a", "B", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("A", "b", *)       &lt; 0
//     * StrUtil.compareIgnoreCase("ab", "abc", *)    &lt; 0
//     * </pre>
//     *
//     * @param str1       字符串1
//     * @param str2       字符串2
//     * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
//     * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
//     */
//    public static int compareIgnoreCase(CharSequence str1, CharSequence str2, boolean nullIsLess) {
//        if (str1 == str2) {
//            return 0;
//        }
//        if (str1 == null) {
//            return nullIsLess ? -1 : 1;
//        }
//        if (str2 == null) {
//            return nullIsLess ? 1 : -1;
//        }
//        return str1.toString().compareToIgnoreCase(str2.toString());
//    }
//
//    /**
//     * 比较两个版本<br>
//     * null版本排在最小：即：
//     *
//     * <pre>
//     * StrUtil.compareVersion(null, "v1") &lt; 0
//     * StrUtil.compareVersion("v1", "v1")  = 0
//     * StrUtil.compareVersion(null, null)   = 0
//     * StrUtil.compareVersion("v1", null) &gt; 0
//     * StrUtil.compareVersion("1.0.0", "1.0.2") &lt; 0
//     * StrUtil.compareVersion("1.0.2", "1.0.2a") &lt; 0
//     * StrUtil.compareVersion("1.13.0", "1.12.1c") &gt; 0
//     * StrUtil.compareVersion("V0.0.20170102", "V0.0.20170101") &gt; 0
//     * </pre>
//     *
//     * @param version1 版本1
//     * @param version2 版本2
//     * @return 排序值。负数：version1 &lt; version2，正数：version1 &gt; version2, 0：version1 == version2
//     */
//    public static int compareVersion(CharSequence version1, CharSequence version2) {
//        return VersionComparator.INSTANCE.compare(str(version1), str(version2));
//    }
//
//    // ------------------------------------------------------------------------ append and prepend
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
//     * 不忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param suffix   需要添加到结尾的字符串
//     * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissing(
//            CharSequence str, CharSequence suffix, CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, false, suffixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
//     * 忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param suffix   需要添加到结尾的字符串
//     * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissingIgnoreCase(
//            CharSequence str, CharSequence suffix, CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, true, suffixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
//     *
//     * @param str          被检查的字符串
//     * @param suffix       需要添加到结尾的字符串，不参与检查匹配
//     * @param ignoreCase   检查结尾时是否忽略大小写
//     * @param testSuffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String appendIfMissing(
//            CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... testSuffixes) {
//        if (str == null || Whether.empty(suffix) || endWith(str, suffix, ignoreCase)) {
//            return str(str);
//        }
//        if (Whether.noEmpty(testSuffixes)) {
//            for (final CharSequence testSuffix : testSuffixes) {
//                if (endWith(str, testSuffix, ignoreCase)) {
//                    return str.toString();
//                }
//            }
//        }
//        return str.toString().concat(suffix.toString());
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
//     * 不忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param prefix   需要添加到首部的字符串
//     * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissing(
//            CharSequence str, CharSequence prefix, CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, false, prefixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
//     * 忽略大小写
//     *
//     * @param str      被检查的字符串
//     * @param prefix   需要添加到首部的字符串
//     * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissingIgnoreCase(
//            CharSequence str, CharSequence prefix, CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, true, prefixes);
//    }
//
//    /**
//     * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串
//     *
//     * @param str        被检查的字符串
//     * @param prefix     需要添加到首部的字符串
//     * @param ignoreCase 检查结尾时是否忽略大小写
//     * @param prefixes   需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
//     * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
//     */
//    public static String prependIfMissing(
//            CharSequence str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
//        if (str == null || Whether.empty(prefix) || startWith(str, prefix, ignoreCase)) {
//            return str(str);
//        }
//        if (prefixes != null && prefixes.length > 0) {
//            for (final CharSequence s : prefixes) {
//                if (startWith(str, s, ignoreCase)) {
//                    return str.toString();
//                }
//            }
//        }
//        return prefix.toString().concat(str.toString());
//    }
//
//    // ------------------------------------------------------------------------ replace
//
//    /**
//     * 替换字符串中的指定字符串，忽略大小写
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replaceIgnoreCase(
//            CharSequence str, CharSequence searchStr, CharSequence replacement) {
//        return replace(str, 0, searchStr, replacement, true);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
//        return replace(str, 0, searchStr, replacement, false);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @param ignoreCase  是否忽略大小写
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
//        return replace(str, 0, searchStr, replacement, ignoreCase);
//    }
//
//    /**
//     * 替换字符串中的指定字符串
//     *
//     * @param str         字符串
//     * @param fromIndex   开始位置（包括）
//     * @param searchStr   被查找的字符串
//     * @param replacement 被替换的字符串
//     * @param ignoreCase  是否忽略大小写
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str,
//            int fromIndex,
//            CharSequence searchStr,
//            CharSequence replacement,
//            boolean ignoreCase) {
//        if (Whether.empty(str) || Whether.empty(searchStr)) {
//            return str(str);
//        }
//        if (null == replacement) {
//            replacement = PoolOfString.EMPTY;
//        }
//
//        final int strLength = str.length();
//        final int searchStrLength = searchStr.length();
//        if (strLength < searchStrLength) {
//            // issue#I4M16G@Gitee
//            return str(str);
//        }
//
//        if (fromIndex > strLength) {
//            return str(str);
//        } else if (fromIndex < 0) {
//            fromIndex = 0;
//        }
//
//        final StringBuilder result =
//                new StringBuilder(strLength - searchStrLength + replacement.length());
//        if (0 != fromIndex) {
//            result.append(str.subSequence(0, fromIndex));
//        }
//
//        int preIndex = fromIndex;
//        int index;
//        while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
//            result.append(str.subSequence(preIndex, index));
//            result.append(replacement);
//            preIndex = index + searchStrLength;
//        }
//
//        if (preIndex < strLength) {
//            // 结尾部分
//            result.append(str.subSequence(preIndex, strLength));
//        }
//        return result.toString();
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为固定字符<br>
//     * 此方法使用{@link String#codePoints()}完成拆分替换
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @param replacedChar 被替换的字符
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, int startInclude, int endExclude, char replacedChar) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        final String originalStr = str(str);
//        int[] strCodePoints = originalStr.codePoints().toArray();
//        final int strLength = strCodePoints.length;
//        if (startInclude > strLength) {
//            return originalStr;
//        }
//        if (endExclude > strLength) {
//            endExclude = strLength;
//        }
//        if (startInclude > endExclude) {
//            // 如果起始位置大于结束位置，不替换
//            return originalStr;
//        }
//
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < strLength; i++) {
//            if (i >= startInclude && i < endExclude) {
//                stringBuilder.append(replacedChar);
//            } else {
//                stringBuilder.append(new String(strCodePoints, i, 1));
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为指定字符串，字符串只重复一次<br>
//     * 此方法使用{@link String#codePoints()}完成拆分替换
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @param replacedStr  被替换的字符串
//     * @return 替换后的字符串
//     */
//    public static String replace(
//            CharSequence str, int startInclude, int endExclude, CharSequence replacedStr) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        final String originalStr = str(str);
//        int[] strCodePoints = originalStr.codePoints().toArray();
//        final int strLength = strCodePoints.length;
//        if (startInclude > strLength) {
//            return originalStr;
//        }
//        if (endExclude > strLength) {
//            endExclude = strLength;
//        }
//        if (startInclude > endExclude) {
//            // 如果起始位置大于结束位置，不替换
//            return originalStr;
//        }
//
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < startInclude; i++) {
//            stringBuilder.append(new String(strCodePoints, i, 1));
//        }
//        stringBuilder.append(replacedStr);
//        for (int i = endExclude; i < strLength; i++) {
//            stringBuilder.append(new String(strCodePoints, i, 1));
//        }
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 替换指定字符串的指定区间内字符为"*" 俗称：脱敏功能，后面其他功能，可以见：DesensitizedUtil(脱敏工具类)
//     *
//     * <pre>
//     * StrUtil.hide(null,*,*)=null
//     * StrUtil.hide("",0,*)=""
//     * StrUtil.hide("jackduan@163.com",-1,4)   ****duan@163.com
//     * StrUtil.hide("jackduan@163.com",2,3)    ja*kduan@163.com
//     * StrUtil.hide("jackduan@163.com",3,2)    jackduan@163.com
//     * StrUtil.hide("jackduan@163.com",16,16)  jackduan@163.com
//     * StrUtil.hide("jackduan@163.com",16,17)  jackduan@163.com
//     * </pre>
//     *
//     * @param str          字符串
//     * @param startInclude 开始位置（包含）
//     * @param endExclude   结束位置（不包含）
//     * @return 替换后的字符串
//     */
//    public static String hide(CharSequence str, int startInclude, int endExclude) {
//        return replace(str, startInclude, endExclude, '*');
//    }
//
//    /**
//     * 脱敏，使用默认的脱敏策略
//     *
//     * <pre>
//     * StrUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID)) =  "0"
//     * StrUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME)) = "段**"
//     * StrUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD)) = "5***************1X"
//     * StrUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE)) = "0915*****79"
//     * StrUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE)) = "180****1999"
//     * StrUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS)) = "北京市海淀区马********"
//     * StrUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL)) = "d*************@gmail.com.cn"
//     * StrUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD)) = "**********"
//     * StrUtil.desensitized("苏D40000", DesensitizedUtil.DesensitizedType.CAR_LICENSE)) = "苏D4***0"
//     * StrUtil.desensitized("11011111222233333256", DesensitizedType.BANK_CARD)) = "1101 **** **** **** 3256"
//     * </pre>
//     *
//     * @param str              字符串
//     * @param desensitizedType 脱敏类型;可以脱敏：用户id、中文名、身份证号、座机号、手机号、地址、电子邮件、密码
//     * @return 脱敏之后的字符串
//     * @author potatoxf
//     * @see ForDesensitized 如果需要自定义，脱敏规则，请使用该工具类；
//     */
//    public static String desensitized(
//            CharSequence str, ForDesensitized.DesensitizedType desensitizedType) {
//        return ForDesensitized.desensitized(str, desensitizedType);
//    }
//
//    /**
//     * 替换字符字符数组中所有的字符为replacedStr<br>
//     * 提供的chars为所有需要被替换的字符，例如："\r\n"，则"\r"和"\n"都会被替换，哪怕他们单独存在
//     *
//     * @param str         被检查的字符串
//     * @param chars       需要替换的字符列表，用一个字符串表示这个字符列表
//     * @param replacedStr 替换成的字符串
//     * @return 新字符串
//     */
//    public static String replaceChars(CharSequence str, String chars, CharSequence replacedStr) {
//        if (Whether.empty(str) || Whether.empty(chars)) {
//            return str(str);
//        }
//        return replaceChars(str, chars.toCharArray(), replacedStr);
//    }
//
//    /**
//     * 替换字符字符数组中所有的字符为replacedStr
//     *
//     * @param str         被检查的字符串
//     * @param chars       需要替换的字符列表
//     * @param replacedStr 替换成的字符串
//     * @return 新字符串
//     */
//    public static String replaceChars(CharSequence str, char[] chars, CharSequence replacedStr) {
//        if (Whether.empty(str) || Whether.empty(chars)) {
//            return str(str);
//        }
//
//        final Set<Character> set = new HashSet<>(chars.length);
//        for (char c : chars) {
//            set.add(c);
//        }
//        int strLen = str.length();
//        final StringBuilder builder = new StringBuilder();
//        char c;
//        for (int i = 0; i < strLen; i++) {
//            c = str.charAt(i);
//            builder.append(set.contains(c) ? replacedStr : c);
//        }
//        return builder.toString();
//    }
//
//    // ------------------------------------------------------------------------ length
//
//    /**
//     * 获取字符串的长度，如果为null返回0
//     *
//     * @param cs a 字符串
//     * @return 字符串的长度，如果为null返回0
//     */
//    public static int length(CharSequence cs) {
//        return cs == null ? 0 : cs.length();
//    }
//
//    /**
//     * 给定字符串转为bytes后的byte数（byte长度）
//     *
//     * @param cs      字符串
//     * @param charset 编码
//     * @return byte长度
//     */
//    public static int byteLength(CharSequence cs, Charset charset) {
//        return cs == null ? 0 : cs.toString().getBytes(charset).length;
//    }
//
//    /**
//     * 给定字符串数组的总长度<br>
//     * null字符长度定义为0
//     *
//     * @param strs 字符串数组
//     * @return 总长度
//     */
//    public static int totalLength(CharSequence... strs) {
//        int totalLength = 0;
//        for (CharSequence str : strs) {
//            totalLength += (null == str ? 0 : str.length());
//        }
//        return totalLength;
//    }
//
//    /**
//     * 限制字符串长度，如果超过指定长度，截取指定长度并在末尾加"..."
//     *
//     * @param string 字符串
//     * @param length 最大长度
//     * @return 切割后的剩余的前半部分字符串+"..."
//     */
//    public static String maxLength(CharSequence string, int length) {
//        Assert.isTrue(length > 0);
//        if (null == string) {
//            return null;
//        }
//        if (string.length() <= length) {
//            return string.toString();
//        }
//        return sub(string, 0, length) + "...";
//    }
//
//    // ------------------------------------------------------------------------ firstXXX
//
//    /**
//     * 返回第一个非{@code null} 元素
//     *
//     * @param strs 多个元素
//     * @param <T>  元素类型
//     * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
//     */
//    @SuppressWarnings("unchecked")
//    public static <T extends CharSequence> T firstNonNull(T... strs) {
//        return ToolArray.firstNonNull(strs);
//    }
//
//    /**
//     * 返回第一个非empty 元素
//     *
//     * @param strs 多个元素
//     * @param <T>  元素类型
//     * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
//     */
//    @SuppressWarnings("unchecked")
//    public static <T extends CharSequence> T firstNonEmpty(T... strs) {
//        return ToolArray.firstMatch(Whether::noEmpty, strs);
//    }
//
//    // ------------------------------------------------------------------------ case
//
//    /**
//     * 切换给定字符串中的大小写。大写转小写，小写转大写。
//     *
//     * <pre>
//     * StrUtil.swapCase(null)                 = null
//     * StrUtil.swapCase("")                   = ""
//     * StrUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
//     * </pre>
//     *
//     * @param str 字符串
//     * @return 交换后的字符串
//     */
//    public static String swapCase(final String str) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//
//        final char[] buffer = str.toCharArray();
//
//        for (int i = 0; i < buffer.length; i++) {
//            final char ch = buffer[i];
//            if (Character.isUpperCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isTitleCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isLowerCase(ch)) {
//                buffer[i] = Character.toUpperCase(ch);
//            }
//        }
//        return new String(buffer);
//    }
//
//    // ------------------------------------------------------------------------ isSurround
//
//    // ------------------------------------------------------------------------ builder
//
//    /**
//     * 创建StringBuilder对象
//     *
//     * @param strs 初始字符串列表
//     * @return StringBuilder对象
//     */
//    public static StringBuilder builder(CharSequence... strs) {
//        final StringBuilder sb = new StringBuilder();
//        for (CharSequence str : strs) {
//            sb.append(str);
//        }
//        return sb;
//    }
//
//    // ------------------------------------------------------------------------ getter and setter
//
//    /**
//     * 获得set或get或is方法对应的标准属性名<br>
//     * 例如：setName 返回 name
//     *
//     * <pre>
//     * getName =》name
//     * setName =》name
//     * isName  =》name
//     * </pre>
//     *
//     * @param getOrSetMethodName Get或Set方法名
//     * @return 如果是set或get方法名，返回field， 否则null
//     */
//    public static String getGeneralField(CharSequence getOrSetMethodName) {
//        final String getOrSetMethodNameStr = getOrSetMethodName.toString();
//        if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
//            return removePreAndLowerFirst(getOrSetMethodName, 3);
//        } else if (getOrSetMethodNameStr.startsWith("is")) {
//            return removePreAndLowerFirst(getOrSetMethodName, 2);
//        }
//        return null;
//    }
//
//    /**
//     * 字符串的每一个字符是否都与定义的匹配器匹配
//     *
//     * @param value     字符串
//     * @param predicate 匹配器
//     * @return 是否全部匹配
//     */
//    public static boolean isAllCharMatch(CharSequence value, Predicate<Character> predicate) {
//        if (Whether.blank(value)) {
//            return false;
//        }
//        for (int i = value.length(); --i >= 0; ) {
//            if (!predicate.test(value.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 检查字符串是否都为数字组成
//     *
//     * @param str 字符串
//     * @return 是否都为数字组成
//     */
//    public static boolean isNumeric(CharSequence str) {
//        return isAllCharMatch(str, Character::isDigit);
//    }
//
//    /**
//     * 循环位移指定位置的字符串为指定距离<br>
//     * 当moveLength大于0向右位移，小于0向左位移，0不位移<br>
//     * 当moveLength大于字符串长度时采取循环位移策略，即位移到头后从头（尾）位移，例如长度为10，位移13则表示位移3
//     *
//     * @param str          字符串
//     * @param startInclude 起始位置（包括）
//     * @param endExclude   结束位置（不包括）
//     * @param moveLength   移动距离，负数表示左移，正数为右移
//     * @return 位移后的字符串
//     */
//    public static String move(CharSequence str, int startInclude, int endExclude, int moveLength) {
//        if (Whether.empty(str)) {
//            return str(str);
//        }
//        int len = str.length();
//        if (Math.abs(moveLength) > len) {
//            // 循环位移，当越界时循环
//            moveLength = moveLength % len;
//        }
//        final StringBuilder StringBuilder = new StringBuilder(len);
//        if (moveLength > 0) {
//            int endAfterMove = Math.min(endExclude + moveLength, str.length());
//            StringBuilder.append(str.subSequence(0, startInclude)) //
//                    .append(str.subSequence(endExclude, endAfterMove)) //
//                    .append(str.subSequence(startInclude, endExclude)) //
//                    .append(str.subSequence(endAfterMove, str.length()));
//        } else if (moveLength < 0) {
//            int startAfterMove = Math.max(startInclude + moveLength, 0);
//            StringBuilder.append(str.subSequence(0, startAfterMove)) //
//                    .append(str.subSequence(startInclude, endExclude)) //
//                    .append(str.subSequence(startAfterMove, startInclude)) //
//                    .append(str.subSequence(endExclude, str.length()));
//        } else {
//            return str(str);
//        }
//        return StringBuilder.toString();
//    }
//
//    /**
//     * 检查给定字符串的所有字符是否都一样
//     *
//     * @param str 字符出啊
//     * @return 给定字符串的所有字符是否都一样
//     */
//    public static boolean isCharEquals(CharSequence str) {
//        Assert.notEmpty(str, "Str to check must be not empty!");
//        return count(str, str.charAt(0)) == str.length();
//    }
//
//    /**
//     * 对字符串归一化处理，如 "Á" 可以使用 "u00C1"或 "u0041u0301"表示，实际测试中两个字符串并不equals<br>
//     * 因此使用此方法归一为一种表示形式，默认按照W3C通常建议的，在NFC中交换文本。
//     *
//     * @param str 归一化的字符串
//     * @return 归一化后的字符串
//     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
//     */
//    public static String normalize(CharSequence str) {
//        return Normalizer.normalize(str, Normalizer.Form.NFC);
//    }
//
//    /**
//     * 在给定字符串末尾填充指定字符，以达到给定长度<br>
//     * 如果字符串本身的长度大于等于length，返回原字符串
//     *
//     * @param str       字符串
//     * @param fixedChar 补充的字符
//     * @param length    补充到的长度
//     * @return 补充后的字符串
//     */
//    public static String fixLength(CharSequence str, char fixedChar, int length) {
//        final int fixedLength = length - str.length();
//        if (fixedLength <= 0) {
//            return str.toString();
//        }
//        return str + repeat(fixedChar, fixedLength);
//    }
//
//    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{[^{}$]+}");
//
//    /**
//     * Represents a failed index search.
//     */
//    public static final int INDEX_NOT_FOUND = -1;
//    /**
//     * The maximum size to which the padding constant(s) can expand.
//     */
//    private static final int PAD_LIMIT = 8192;
//    /**
//     * A regex pattern for recognizing blocks of whitespace characters. The apparent convolutedness of
//     * the pattern serves the purpose of ignoring "blocks" consisting of only a single space: the
//     * pattern is used only to normalize whitespace, condensing "blocks" down to a single space, thus
//     * matching the same would likely cause a great many noop replacements.
//     */
//    private static final Pattern WHITESPACE_PATTERN =
//            Pattern.compile("(?: |\\u00A0|\\s|[\\s&&[^ ]])\\s*");
//    /**
//     * Operations on char primitives and Character objects.
//     *
//     * <p>This class tries to handle {@code null} input gracefully. An exception will not be thrown
//     * for a {@code null} input. Each method documents its behaviour in more detail.
//     */
//    private static final String[] CHAR_STRING_ARRAY = new String[128];
//
//    static {
//        for (char c = 0; c < CHAR_STRING_ARRAY.length; c++) {
//            CHAR_STRING_ARRAY[c] = String.valueOf(c);
//        }
//    }
//
//    private ToolString() throws IllegalAccessException {
//        throw new IllegalAccessException(
//                "The instance creation is not allowed,because this is static method utils class");
//    }
//
//    /**
//     * 缩写单词
//     *
//     * @param string                 输入字符串
//     * @param oldSplit               老分割符
//     * @param newSplit               新分割符，默认空字符串
//     * @param maxPrefixLength        最大前缀字符串长度，默认0
//     * @param maxSegmentLength       最大段字符串长度
//     * @param totalAbbreviatedLength 总计缩写长度
//     * @return 返回处理后的字符串
//     * @throws IllegalArgumentException {@code totalAbbreviatedLength <= 0}，{@code maxSegmentLength >=
//     *                                  totalAbbreviatedLength}或totalAbbreviatedLength小于总段数
//     */
//    public static String abbreviatedWordByAverageLength(
//            String string,
//            String oldSplit,
//            String newSplit,
//            int maxPrefixLength,
//            int maxSegmentLength,
//            int totalAbbreviatedLength) {
//        String[] ss = splitSimply(string, oldSplit).toArray(new String[0]);
//        if (totalAbbreviatedLength <= 0) {
//            throw new IllegalArgumentException(
//                    "The total string is not allowed to be less than or equal to 0");
//        }
//        if (maxSegmentLength >= totalAbbreviatedLength) {
//            throw new IllegalArgumentException(
//                    "The max segment length must less total abbreviated length");
//        }
//        newSplit = Valid.string(newSplit);
//        maxPrefixLength = Valid.gtEq(maxPrefixLength, 0, 0);
//        maxSegmentLength = Valid.gtEq(maxSegmentLength, 0, 0);
//        if (ss.length == 1) {
//            if (ss[0].length() < totalAbbreviatedLength) {
//                return ss[0];
//            } else {
//                return ss[0].substring(0, totalAbbreviatedLength);
//            }
//        } else {
//            StringBuilder sb = new StringBuilder(string.length());
//            if (ss.length > 1) {
//                if (ss[0].length() < maxPrefixLength) {
//                    String[] temp = new String[ss.length - 1];
//                    System.arraycopy(ss, 1, temp, 0, temp.length);
//                    ss = temp;
//                }
//                // segment string length
//                int ssl = totalAbbreviatedLength / ss.length;
//                if (ssl <= 0) {
//                    throw new IllegalArgumentException(
//                            "The total string must be greater than the number of divisions");
//                }
//                // remaining string length
//                int rest = totalAbbreviatedLength - ssl * ss.length;
//                for (String v : ss) {
//                    sb.append(newSplit);
//                    int vl = v.length();
//                    // distance length
//                    int dl = vl - ssl;
//                    if (vl <= maxSegmentLength) {
//                        sb.append(v);
//                        rest -= dl;
//                    } else {
//                        // less per segment string length
//                        if (dl <= 0) {
//                            sb.append(v, 0, vl);
//                            rest -= dl;
//                        } else {
//                            if (rest >= 0) {
//                                sb.append(v, 0, ssl);
//                                if (dl >= rest) {
//                                    sb.append(v, ssl, ssl + rest);
//                                    rest = 0;
//                                } else {
//                                    sb.append(v, ssl, vl);
//                                    rest -= dl;
//                                }
//                            } else {
//                                sb.append(v, 0, ssl - 1);
//                                rest += 1;
//                            }
//                        }
//                    }
//                }
//                return sb.substring(newSplit.length());
//            } else {
//                throw new IllegalArgumentException("The input string is not allowed to be empty");
//            }
//        }
//    }
//
//    /**
//     * 用分割符连接所有元素
//     *
//     * @param prefix       数组，默认空字符串
//     * @param suffix       数组，默认空字符串
//     * @param delimiter    分割符，默认逗号
//     * @param factoryThrow 映射元素值
//     * @param anyIter      元素数组
//     * @return 返回连接后的字符串
//     */
//    public static <E> String concat(
//            String prefix,
//            String suffix,
//            String delimiter,
//            FunctionThrow<E, Object, RuntimeException> factoryThrow,
//            AnyIter<E> anyIter) {
//        if (anyIter == null) {
//            return "";
//        }
//        if (prefix == null) {
//            prefix = "";
//        }
//        if (suffix == null) {
//            suffix = "";
//        }
//        if (delimiter == null) {
//            delimiter = ",";
//        }
//        StringBuilder sb = new StringBuilder(anyIter.size() * 4);
//        for (E e : anyIter) {
//            if (e == null) {
//                continue;
//            }
//            Object value;
//            if (factoryThrow != null) {
//                value = factoryThrow.apply(e);
//            } else {
//                value = e;
//            }
//            if (value != null) {
//                sb.append(prefix).append(value).append(suffix).append(delimiter);
//            }
//        }
//        if (!delimiter.isEmpty()) {
//            sb.setLength(sb.length() - delimiter.length());
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 清除前缀到目标字符串
//     *
//     * @param string 输入字符串
//     * @param target 指定字符串
//     * @return {@code String}，返回清除前缀的字符串，不存在则原样返回
//     */
//    public static String clearPrefixToTarget(String string, String target) {
//        if (string == null) {
//            return "";
//        }
//        int i = string.indexOf(target);
//        if (i > 0) {
//            return string.substring(i + target.length());
//        }
//        return string;
//    }
//
//    /**
//     * 清除前导 {@code --}，{@code -}字符串
//     *
//     * @param string 输入字符串
//     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串
//     */
//    public static String clearLeadingHyphens(String string) {
//        return clearLeading(string, "--", "-");
//    }
//
//    /**
//     * 清除前导字符串
//     *
//     * @param string   输入字符串
//     * @param prefixes 要删除的前缀
//     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有前缀则原样返回
//     */
//    public static String clearLeading(String string, String... prefixes) {
//        if (string == null) {
//            return "";
//        }
//        for (String prefix : prefixes) {
//            if (string.startsWith(prefix)) {
//                return string.substring(prefix.length());
//            }
//        }
//        return string;
//    }
//
//    /**
//     * Remove the leading and trailing quotes from <code>string</code>. E.g. if string is '"one two"',
//     * then 'one two' is returned.
//     *
//     * @param string The string from which the leading and trailing quotes should be removed.
//     * @return The string without the leading and trailing quotes.
//     */
//    public static String clearLeadingAndTrailingQuotes(String string) {
//        if (string == null) {
//            return "";
//        }
//        int length = string.length();
//        if (length > 1
//                && string.startsWith("\"")
//                && string.endsWith("\"")
//                && string.substring(1, length - 1).indexOf('"') == -1) {
//            string = string.substring(1, length - 1);
//        }
//        return string;
//    }
//
//    /**
//     * 清理路径，格式{@code /a/b/c}或{@code a/b/c}或{@code /a/b/c/}或{@code a/b/c/}
//     *
//     * <p>{@code isPathSplitEnd==true} 则会在路径尾部添加{@code /} {@code isPathSplitEnd==false}
//     * 则会在路径尾部去除{@code /}
//     *
//     * <p>{@code isPathSplitStart==true} {@code file://D:/a/b/c}--->{@code D:/a/b/c/} {@code
//     * file:/a/b/c/}--->{@code /a/b/c/} {@code a/b/c}--->{@code /a/b/c/} {@code a/b/c}--->{@code
//     * /a/b/c/}
//     *
//     * <p>{@code isPathSplitStart==false} {@code file://D:/a/b/c}--->{@code D:/a/b/c/} {@code
//     * file:/a/b/c/}--->{@code a/b/c/} {@code a/b/c}--->{@code a/b/c/} {@code a/b/c}--->{@codea
//     * a/b/c/}
//     *
//     * @param isPathSplitStart 是否为路径分割符开始
//     * @param isPathSplitEnd   是否为路径分割符结尾
//     * @param paths            路径
//     * @return 返回合法化文件路径
//     */
//    public static String clearPath(
//            boolean isPathSplitStart, boolean isPathSplitEnd, String... paths) {
//        if (paths.length == 0) {
//            return isPathSplitEnd || isPathSplitStart ? "/" : "";
//        }
//        StringBuilder sb = new StringBuilder(256);
//        char c;
//        boolean isLastPathSplit = !isPathSplitStart;
//        int s = paths[0].indexOf(":");
//        if (s > 0) {
//            String fs = trim(paths[0].substring(0, s), "\\/");
//            int d = paths[0].indexOf(":", s + 1);
//            if (d > 0) {
//                String ss = trim(paths[0].substring(s + 1, d), "\\/");
//                if (ss.length() == 1) {
//                    sb.append(ss.toUpperCase()).append(":/");
//                    isLastPathSplit = true;
//                }
//                s = d + 1;
//            } else {
//                if (fs.length() == 1) {
//                    sb.append(fs.toUpperCase()).append(":/");
//                    isLastPathSplit = true;
//                }
//                s += 1;
//            }
//            paths[0] = paths[0].substring(s);
//        } else {
//            isLastPathSplit = !isPathSplitStart;
//        }
//        for (String path : paths) {
//            if (Whether.empty(path)) {
//                continue;
//            }
//            if (!isLastPathSplit) {
//                sb.append('/');
//                isLastPathSplit = true;
//            }
//            for (int j = 0; j < path.length(); j++) {
//                c = path.charAt(j);
//                if (c == '\\' || c == '/') {
//                    if (!isLastPathSplit) {
//                        sb.append('/');
//                        isLastPathSplit = true;
//                    }
//                } else {
//                    isLastPathSplit = false;
//                    sb.append(c);
//                }
//            }
//        }
//        if (isLastPathSplit != isPathSplitEnd) {
//            if (isPathSplitEnd) {
//                sb.append('/');
//            } else {
//                if (sb.length() > 0) {
//                    sb.setLength(sb.length() - 1);
//                }
//            }
//        }
//        return sb.toString().replaceAll("%20", " ").trim();
//    }
//
//    /**
//     * 计算文本显示长度，默认半角为 {@code 1}，全角为 {@code 2}
//     *
//     * @param str 文本
//     * @return 返回计算后显示长度
//     */
//    public static double calculateStringLength(String str) {
//        return calculateStringLength(str, 1, 2, 0);
//    }
//
//    /**
//     * 计算文本显示长度
//     *
//     * @param text          文本
//     * @param half          半角长度
//     * @param full          全角长度
//     * @param defaultLength 默认长度，当没有字符时
//     * @return 返回计算后显示长度
//     */
//    public static double calculateStringLength(
//            String text, double half, double full, double defaultLength) {
//        if (text == null || text.length() == 0) {
//            return defaultLength;
//        }
//        double len = 0;
//        for (int i = 0; i < text.length(); ++i) {
//            if (Whether.quadEmChar(text.codePointAt(i))) {
//                len = len + full;
//            } else {
//                len = len + half;
//            }
//        }
//        return len;
//    }
//
//    /**
//     * 计算字符串比例长度
//     *
//     * @param strings 字符串数组
//     * @return 返回字符串占总字符串的比例长度
//     */
//    public static int[] calculateStringScaleLength(String[] strings) {
//        double[] lengthScale = calculateStringLengthScale(strings);
//        int[] result = new int[strings.length];
//        int u = 0, d = 0;
//        for (int j = 0; j < strings.length; j++) {
//            double v = strings[j].length() * lengthScale[j];
//            int i = (int) v;
//            double diff = v - i;
//            if (diff < 1e-10) {
//                if (d > u) {
//                    result[j] = i - 1;
//                    d++;
//                } else if (d < u) {
//                    result[j] = i + 1;
//                    u++;
//                } else {
//                    result[j] = i;
//                }
//            } else {
//                int rd = (int) Math.round(diff);
//                if (rd == 1) {
//                    result[j] = i + 1;
//                    u++;
//                } else {
//                    d++;
//                }
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 计算字符串长度比例
//     *
//     * @param strings 字符串数组
//     * @return 返回字符串占总字符串长度的比例
//     */
//    public static double[] calculateStringLengthScale(String... strings) {
//        double[] result = new double[strings.length];
//        double tl = 0;
//        for (String str : strings) {
//            tl += str.length();
//        }
//        for (int i = 0; i < strings.length; i++) {
//            result[i] = strings[i].length() / tl;
//        }
//        return result;
//    }
//
//    /**
//     * 获取带有小数的数字
//     *
//     * @param input        输入字符串
//     * @param defaultValue 找不到时的默认值
//     * @return 数字字符串
//     */
//    public static String extractDecimalNumber(String input, String defaultValue) {
//        return extractDecimalNumber(input, 1, defaultValue);
//    }
//
//    /**
//     * 提取提取带有小数的数字
//     *
//     * @param input        输入字符串
//     * @param findCount    提取第几个数字
//     * @param defaultValue 找不到时的默认值
//     * @return 数字字符串
//     */
//    public static String extractDecimalNumber(String input, int findCount, String defaultValue) {
//        return extractNumber(input, findCount, true, defaultValue);
//    }
//
//    /**
//     * 提取整数
//     *
//     * @param input        输入字符串
//     * @param defaultValue 找不到时的默认值
//     * @return 数字字符串
//     */
//    public static String extractIntegerNumber(String input, String defaultValue) {
//        return extractIntegerNumber(input, 1, defaultValue);
//    }
//
//    /**
//     * 提取整数
//     *
//     * @param input        输入字符串
//     * @param findCount    提取第几个数字
//     * @param defaultValue 找不到时的默认值
//     * @return 数字字符串
//     */
//    public static String extractIntegerNumber(String input, int findCount, String defaultValue) {
//        return extractNumber(input, findCount, false, defaultValue);
//    }
//
//    /**
//     * 提取数字
//     *
//     * @param input        输入字符串
//     * @param findCount    提取第几个数字
//     * @param defaultValue 找不到时的默认值
//     * @return 数字字符串
//     */
//    private static String extractNumber(
//            String input, int findCount, boolean isDecimal, String defaultValue) {
//        if (findCount <= 0) {
//            throw new IllegalArgumentException("The find count must be greater 0");
//        }
//        if (input == null) {
//            return defaultValue;
//        }
//        int len = input.length();
//        if (len == 0) {
//            return defaultValue;
//        }
//        int startIndex = -1;
//        int count = 0;
//        for (int i = 0; i < len; i++) {
//            char c = input.charAt(i);
//            if (Character.isDigit(c)) {
//                if (startIndex == -1) {
//                    startIndex = i;
//                }
//                continue;
//            }
//            if (startIndex != -1 && !(isDecimal && c == '.')) {
//                count++;
//                if (count != findCount) {
//                    startIndex = -1;
//                    continue;
//                }
//                char pre = input.charAt(startIndex);
//                if (AsciiTableMatcher.isMatcherExceptAsciiChar(pre, AsciiTableMatcher.ARITHMETIC_ADD_SUB)) {
//                    startIndex--;
//                }
//                return input.substring(startIndex, i);
//            }
//        }
//        if (startIndex != -1) {
//            char pre = input.charAt(startIndex - 1);
//            if (AsciiTableMatcher.isMatcherExceptAsciiChar(pre, AsciiTableMatcher.ARITHMETIC_ADD_SUB)) {
//                startIndex--;
//            }
//            return input.substring(startIndex);
//        }
//        return defaultValue;
//    }
//
//    /**
//     * 提取文件路径中的文件名
//     *
//     * @param filePath 文件路径
//     * @return 提取文件路径中的文件名
//     */
//    public static String extractFileName(String filePath) {
//        int si = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
//        if (si < 0) {
//            return filePath;
//        }
//        return filePath.substring(si + 1);
//    }
//
//    /**
//     * 提取文件路径中的文件基础名
//     *
//     * @param filePath 文件路径
//     * @return 提取文件路径中的文件基础名
//     */
//    public static String extractFileBaseName(String filePath) {
//        int si = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
//        int di = filePath.lastIndexOf('.');
//        if (di < si) {
//            di = filePath.length();
//        }
//        if (si < 0) {
//            si = -1;
//        }
//        return filePath.substring(si + 1, di);
//    }
//
//    /**
//     * 提取文件路径中的文件扩展
//     *
//     * @param filePath 文件路径
//     * @return 提取文件路径中的文件扩展
//     */
//    public static String extractFileExtension(String filePath) {
//        int di = filePath.lastIndexOf('.');
//        if (di < 0) {
//            return "";
//        }
//        int si = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
//        if (si < 0) {
//            si = 0;
//        }
//        if (di < si) {
//            return "";
//        }
//        return filePath.substring(di + 1);
//    }
//
//    /**
//     * 简单切分字符串，不进行其它操作
//     *
//     * @param input     输入字符串
//     * @param splitChar 切分字符
//     * @return 返回切分后的字符串数组
//     */
//    public static List<String> splitSimply(String input, String splitChar) {
//        if (input == null) {
//            return Collections.emptyList();
//        }
//        int length = input.length();
//        if (length == 0) {
//            return Collections.emptyList();
//        }
//        if (splitChar == null) {
//            return Collections.singletonList(input);
//        }
//        int scLength = splitChar.length();
//        if (scLength == 0) {
//            return Collections.singletonList(input);
//        }
//        int count = 1;
//        int si = 0, i;
//        List<String> list = new ArrayList<String>();
//        while ((i = input.indexOf(splitChar, si)) != -1) {
//            list.add(input.substring(si, i));
//            si = i + scLength;
//        }
//        list.add(input.substring(si));
//        return list;
//    }
//
//    /**
//     * 切分字符串，清除空字符串
//     *
//     * @param input     输入字符串
//     * @param splitChar 切分字符
//     * @return 返回切分后的字符串数组
//     */
//    public static List<String> splitSafely(String input, String splitChar) {
//        if (input == null) {
//            return Collections.emptyList();
//        }
//        int length = input.length();
//        if (length == 0) {
//            return Collections.emptyList();
//        }
//        if (splitChar == null) {
//            return Collections.singletonList(input);
//        }
//        int scLength = splitChar.length();
//        if (scLength == 0) {
//            return Collections.singletonList(input);
//        }
//        int count = 1;
//        int si = 0, i;
//        List<String> list = new ArrayList<String>();
//        while ((i = input.indexOf(splitChar, si)) != -1) {
//            String substring = input.substring(si, i);
//            if (Whether.empty(substring)) {
//                continue;
//            } else {
//                list.add(substring);
//            }
//            si = i + scLength;
//        }
//        list.add(input.substring(si));
//        return list;
//    }
//
//    /**
//     * 切分字符串转成Integer数组
//     *
//     * @param input     输入字符串
//     * @param splitChar 切分字符
//     * @return 返回切分后的字符串数组
//     */
//    public static List<Integer> splitToInteger(String input, String splitChar) {
//        List<String> strings = splitSafely(input, splitChar);
//        List<Integer> result = new ArrayList<Integer>(strings.size());
//        for (String string : strings) {
//            try {
//                int value = Integer.parseInt(string);
//                result.add(value);
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 自定义分割多层级字符串
//     *
//     * @param input      输入字符串
//     * @param finalIndex 最终分割数组索引
//     * @param splits     多个分割符
//     * @return 返回字符串列表
//     */
//    public static List<String> splitMultilayerSegment(
//            String input, int finalIndex, String... splits) {
//        List<String> list = new ArrayList<String>();
//        splitCustomMultilayerSegmentWithRecursion(
//                String::split, list, new String[]{input}, splits, 0, finalIndex);
//        return list;
//    }
//
//    /**
//     * 自定义分割多层级字符串，列如：对于字符串1-2_A-B,2-3_C-D,3-4_E-F，执行该方法，分隔符为{@code , _ -}指定目标索引为{@code 1}，执行顺序如下：
//     *
//     * <pre>
//     *     1、 1-2_A-B  2-3_C-D  3-4_E-F
//     *     2、 1-2  A-B  2-3  C-D  3-4  E-F
//     *     3、 1 2  A B  2 3  C D  3 4  E F
//     *     结果： 2 B 3 D 4 F
//     * </pre>
//     *
//     * @param splitCustomStringHandler 自定义分割字符串处理器
//     * @param container                字符串列表容器
//     * @param inputs                   输入字符串数组
//     * @param splits                   多个分割符
//     * @param splitIndex               分割索引
//     * @param finalIndex               最终分割数组索引
//     */
//    private static void splitCustomMultilayerSegmentWithRecursion(
//            final BiFunction<String, String, String[]> splitCustomStringHandler,
//            final List<String> container,
//            final String[] inputs,
//            final String[] splits,
//            final int splitIndex,
//            final int finalIndex) {
//        if (splitIndex < 0 || splitIndex >= splits.length) {
//            throw new IllegalArgumentException();
//        } else if (splitIndex == splits.length - 1) {
//            for (String input : inputs) {
//                String[] arr = splitCustomStringHandler.apply(input, splits[splitIndex]);
//                if (finalIndex < arr.length) {
//                    container.add(arr[finalIndex]);
//                }
//            }
//        } else {
//            for (String input : inputs) {
//                splitCustomMultilayerSegmentWithRecursion(
//                        splitCustomStringHandler,
//                        container,
//                        splitCustomStringHandler.apply(input, splits[splitIndex]),
//                        splits,
//                        splitIndex + 1,
//                        finalIndex);
//            }
//        }
//    }
//
//    /**
//     * @param input
//     * @param middleRegexp
//     * @return
//     */
//    public static String[] cutToTriplicate(final String input, final String middleRegexp) {
//        return cutToTriplicate(input, Pattern.compile(middleRegexp));
//    }
//
//    /**
//     * @param input
//     * @param middleRegexp
//     * @return
//     */
//    public static String[] cutToTriplicate(final String input, final Pattern middleRegexp) {
//        Matcher matcher = middleRegexp.matcher(input);
//        if (matcher.find()) {
//            int start = matcher.start();
//            String target = matcher.group();
//            return new String[]{
//                    input.substring(0, start), target, input.substring(start + target.length())
//            };
//        } else {
//            return new String[]{input, PoolOfString.EMPTY, PoolOfString.EMPTY};
//        }
//    }
//
//    /**
//     * 解析特殊字符 {@code \t\n\r\f}
//     *
//     * @param c 字符
//     * @return 返回特殊字符，如果不是则原样返回
//     */
//    public static char resolveSpecialCharacters(char c) {
//        switch (c) {
//            case 't':
//                return '\t';
//            case 'n':
//                return '\n';
//            case 'r':
//                return '\r';
//            case 'f':
//                return '\f';
//            case 'b':
//                return '\b';
//            default:
//                return c;
//        }
//    }
//
//    /**
//     * 数组表达式解析
//     *
//     * @param input 数组表达式
//     * @return 返回解析处理的数组
//     * @throws IllegalArgumentException 当数组表达式错误时
//     * @see #resolveArrayExpression(CharSequence, boolean, char, char, String, String)
//     */
//    public static String[] resolveArrayExpression(CharSequence input) {
//        return resolveArrayExpression(input, ',');
//    }
//
//    /**
//     * 数组表达式解析
//     *
//     * @param input 数组表达式
//     * @return 返回解析处理的数组
//     * @throws IllegalArgumentException 当数组表达式错误时
//     * @see #resolveArrayExpression(CharSequence, boolean, char, char, String, String)
//     */
//    public static String[] resolveArrayExpression(CharSequence input, char splitChar) {
//        return resolveArrayExpression(input, false, splitChar, '\\', "[", "]");
//    }
//
//    /**
//     * 数组表达式解析
//     *
//     * <p>支持特殊字符 {@code \t\n\r\f}
//     *
//     * <p>括号可有可无
//     *
//     * <p>引号内除特殊字符外不用转义
//     *
//     * <p>引号内除特殊字符外不用转义，除开头括号
//     *
//     * <table>
//     * <tbody>
//     * <tr>
//     * <td>[a,b,c]</td>
//     * <td><code>a  b  c</code></td>
//     * </tr>
//     * <tr>
//     * <td>[a a,b b,c c]</td>
//     * <td><code>aa  bb  cc</code></td>
//     * </tr>
//     * <tr>
//     * <td>["a a","b b",c c]</td>
//     * <td><code>a a  b b  cc</code></td>
//     * </tr>
//     * <tr>
//     * <td>["a 'a'","b b",'c c']</td>
//     * <td><code>a 'a'  b b  c c</code></td>
//     * </tr>
//     * <tr>
//     * <td>["a \"a\"","b b",'c c']</td>
//     * <td><code>a "a"  b b  c c</code></td>
//     * </tr>
//     * </tbody>
//     * </table>
//     *
//     * @param input           数组表达式
//     * @param splitChar       指定分隔符
//     * @param escapeChar      指定转义符
//     * @param openingBrackets 左括号
//     * @param closingBrackets 右括号
//     * @return 返回解析处理的数组
//     * @throws IllegalArgumentException 当数组表达式错误时
//     */
//    public static String[] resolveArrayExpression(
//            final CharSequence input,
//            final boolean isIgnoredQuote,
//            final char splitChar,
//            final char escapeChar,
//            final String openingBrackets,
//            final String closingBrackets) {
//        if (input == null) {
//            return new String[0];
//        }
//        int length = input.length();
//        if (length == 0) {
//            return new String[0];
//        }
//        StringBuilder sb = new StringBuilder(length);
//        List<String> list = new ArrayList<String>(length / 20 + 10);
//        int bracketsCount = 0;
//        boolean escape = false, doubleQuotes = false, singleQuotes = false, nextSplit = false;
//        for (int i = 0; i < length; i++) {
//            char c = input.charAt(i);
//            // 跳过引号内的空白符
//            if (!doubleQuotes && !singleQuotes && " \t\n\r\f".indexOf(c) != -1) {
//                continue;
//            }
//            // 跳过开头括号
//            if (Whether.empty(list) && sb.length() == 0 && openingBrackets.indexOf(c) != -1) {
//                continue;
//            }
//            // 记录转义
//            if (c == escapeChar) {
//                // 记录转义
//                if (escape) {
//                    sb.append(escapeChar);
//                }
//                escape = !escape;
//                continue;
//            }
//            // 为转义字符，直接作为普通字符串
//            if (escape) {
//                sb.append(resolveSpecialCharacters(c));
//                escape = false;
//                continue;
//            }
//            // 如果没有忽略引号和不在括号内则处理引号
//            if (!isIgnoredQuote && bracketsCount == 0) {
//                if (c == '"') {
//                    if (singleQuotes) {
//                        sb.append('"');
//                    } else if (!(doubleQuotes = !doubleQuotes)) {
//                        nextSplit = true;
//                    }
//                    continue;
//                }
//                if (!isIgnoredQuote && c == '\'') {
//                    if (doubleQuotes) {
//                        sb.append('\'');
//                    } else if (!(singleQuotes = !singleQuotes)) {
//                        nextSplit = true;
//                    }
//                    continue;
//                }
//            }
//            // 如果没有在在引号内则处理括号
//            if (!singleQuotes && !doubleQuotes) {
//                if (openingBrackets.indexOf(c) != -1) {
//                    bracketsCount++;
//                } else if (closingBrackets.indexOf(c) != -1) {
//                    bracketsCount--;
//                }
//                if (bracketsCount < 0) {
//                    continue;
//                }
//            }
//
//            // 不在引号内，且不在括号内，并且是分割符则获取该值
//            if (!singleQuotes && !doubleQuotes && bracketsCount == 0 && c == splitChar) {
//                ToolCollection.addStringAndClear(list, sb);
//                nextSplit = false;
//            } else {
//                // 下一个期望的是分割符，则报错
//                if (nextSplit) {
//                    throw new IllegalArgumentException(
//                            "Must be an array separator after the end of the quotation mark");
//                }
//                sb.append(c);
//            }
//        }
//        if (doubleQuotes) {
//            throw new IllegalArgumentException("Unterminated double quotation mark");
//        }
//        if (singleQuotes) {
//            throw new IllegalArgumentException("unterminated single quotation mark");
//        }
//        if (bracketsCount != 0 && bracketsCount != -1) {
//            if (bracketsCount > 0) {
//                throw new IllegalArgumentException("Unterminated right brackets mark");
//            } else {
//                throw new IllegalArgumentException("Unterminated left brackets mark");
//            }
//        }
//        if (sb.length() != 0) {
//            ToolCollection.addStringAndClear(list, sb);
//        }
//        return list.toArray(new String[0]);
//    }
//
//    /**
//     * Map表达式解析
//     *
//     * <p>支持特殊字符 {@code \t\n\r\f}
//     *
//     * <p>括号可有可无
//     *
//     * <p>引号内除特殊字符外不用转义
//     *
//     * <p>引号内除特殊字符外不用转义，除开头括号 格式如下：
//     *
//     * <pre>{a:1,b:2,c:3}</pre>
//     *
//     * <pre>a=1 b=2 c=3</pre>
//     *
//     * <pre>{a:1,b:2,c:[1,2,3,4,5]}</pre>
//     *
//     * <pre>a=1  b=2 c=[1,2,3,4,5]</pre>
//     *
//     * <pre>{a:1,b:{d:1,f:=2},c:[1,2,3,4,5]}</pre>
//     *
//     * <pre>a=1  b={d:1,f:=2} c=[1,2,3,4,5]</pre>
//     *
//     * @param input Map表达式
//     * @return {@code Map<String, String>}
//     * @throws IllegalArgumentException 当数组表达式错误时
//     */
//    public static Map<String, String> resolveMapExpression(final CharSequence input) {
//        return resolveMapExpression(input, '=', ';', '\\', "{[", "]}");
//    }
//
//    /**
//     * Map表达式解析
//     *
//     * <p>支持特殊字符 {@code \t\n\r\f}
//     *
//     * <p>括号可有可无
//     *
//     * <p>引号内除特殊字符外不用转义
//     *
//     * <p>引号内除特殊字符外不用转义，除开头括号 格式如下：
//     *
//     * <pre>{a:1,b:2,c:3}</pre>
//     *
//     * <pre>a=1 b=2 c=3</pre>
//     *
//     * <pre>{a:1,b:2,c:[1,2,3,4,5]}</pre>
//     *
//     * <pre>a=1  b=2 c=[1,2,3,4,5]</pre>
//     *
//     * <pre>{a:1,b:{d:1,f:=2},c:[1,2,3,4,5]}</pre>
//     *
//     * <pre>a=1  b={d:1,f:=2} c=[1,2,3,4,5]</pre>
//     *
//     * @param input           Map表达式
//     * @param pairChar        指定键值分隔符
//     * @param splitChar       指定分隔符
//     * @param escapeChar      指定转义符
//     * @param openingBrackets 左括号
//     * @param closingBrackets 右括号
//     * @return {@code Map<String, String>}
//     * @throws IllegalArgumentException 当数组表达式错误时
//     */
//    public static Map<String, String> resolveMapExpression(
//            final CharSequence input,
//            final char pairChar,
//            final char splitChar,
//            final char escapeChar,
//            final String openingBrackets,
//            final String closingBrackets) {
//        String[] strings =
//                resolveArrayExpression(
//                        input, true, splitChar, escapeChar, openingBrackets, closingBrackets);
//        Map<String, String> map = new HashMap<>(strings.length, 1);
//        for (String string : strings) {
//            String[] pair =
//                    resolveArrayExpression(
//                            string, false, pairChar, escapeChar, openingBrackets, closingBrackets);
//            if (pair.length == 2) {
//                map.put(pair[0], pair[1]);
//            } else if (pair.length == 1) {
//                map.put(pair[0], null);
//            }
//        }
//        return map;
//    }
//
//    /**
//     * 替换分割符
//     *
//     * @param string       字符串，如果字符串为空则返回空字符串
//     * @param splitChar    分割符，如果为空则直接返回输入字符串
//     * @param newSplitChar 新的分割符，如果为空则默认去除分割符
//     * @return 返回处理后的字符串
//     */
//    public static String replaceSplitChar(String string, String splitChar, String newSplitChar) {
//        if (string == null) {
//            return "";
//        }
//        int length = string.length();
//        if (length == 0) {
//            return "";
//        }
//        if (splitChar == null || splitChar.length() == 0) {
//            return string;
//        }
//        if (newSplitChar == null) {
//            newSplitChar = "";
//        }
//        StringBuilder sb = new StringBuilder(length);
//        boolean b = false;
//        for (int i = 0; i < length; i++) {
//            int cp = string.codePointAt(i);
//            if (splitChar.indexOf(cp) < 0) {
//                if (b) {
//                    sb.append(newSplitChar);
//                }
//                for (char c : Character.toChars(cp)) {
//                    sb.append(c);
//                }
//                b = false;
//            } else {
//                b = true;
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 解析起始标识字符和结束标识字符中的标识位和后面数据
//     *
//     * <p>标识位是由标识字符符囊括起来，起始位必须是标识字符，后标识字符
//     *
//     * <p>标识位必须满足{@link PoolOfPattern#TOKEN}，否则标识位返回null 数据位返回数据，如果没有标识位，标识位返回null，数据位返回整个数据，
//     * 如果有标识位但不合格则则标识位返回null否则正常返回标识位，数据位返回除了标识位后的数据。
//     *
//     * @param charSequence 字符序列
//     * @param sign         字符标识
//     * @return 返回标识位和数据位
//     */
//    public static String[] parseStartSignAndData(CharSequence charSequence, char sign) {
//        return parseStartSignAndData(charSequence, sign, sign);
//    }
//
//    /**
//     * 解析起始标识字符和结束标识字符中的标识位和后面数据
//     *
//     * <p>标识位是由标识字符符囊括起来，起始位必须是标识字符，后标识字符
//     *
//     * <p>标识位必须满足{@link PoolOfPattern#TOKEN}，否则标识位返回null 数据位返回数据，如果没有标识位，标识位返回null，数据位返回整个数据，
//     * 如果有标识位但不合格则则标识位返回null否则正常返回标识位，数据位返回除了标识位后的数据。
//     *
//     * @param charSequence 字符序列
//     * @param start        开始字符标识
//     * @param end          结束字符标识
//     * @return 返回标识位和数据位
//     */
//    public static String[] parseStartSignAndData(CharSequence charSequence, char start, char end) {
//        if (charSequence != null) {
//            String string = charSequence.toString().trim();
//            if (string.length() > 0) {
//                char c = string.charAt(0);
//                if (c != '\\' && c == start) {
//                    int i = 0;
//                    while ((i = string.indexOf(end, i + 1)) > 0) {
//                        char ch;
//                        boolean escape = false;
//                        for (int j = i - 1; j > 0; j--) {
//                            ch = string.charAt(j);
//                            if (ch == '\\') {
//                                escape = !escape;
//                            } else {
//                                break;
//                            }
//                        }
//                        if (!escape) {
//                            break;
//                        }
//                    }
//                    if (i > 0) {
//                        String sign = string.substring(1, i).trim();
//                        String data = string.substring(i + 1).trim();
//                        if (PoolOfPattern.TOKEN.matcher(sign).matches()) {
//                            return new String[]{sign, data};
//                        } else {
//                            return new String[]{null, data};
//                        }
//                    }
//                }
//            }
//            return new String[]{null, string};
//        }
//        return new String[]{null, ""};
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param input     显示对象
//     * @param fixLength 固定长度
//     * @param pad       填充字符
//     * @param isLeft    是否靠左
//     * @return 返回处理后的字符串
//     */
//    public static String fixed(Object input, int fixLength, char pad, boolean isLeft) {
//        StringBuilder stringBuilder = new StringBuilder();
//        fixed(stringBuilder, input, fixLength, pad, isLeft);
//        return stringBuilder.toString();
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param container 字符串容器
//     * @param input     显示对象
//     * @param fixLength 固定长度
//     * @param pad       填充字符
//     * @param isLeft    是否靠左
//     */
//    public static void fixed(
//            StringBuilder container, Object input, int fixLength, char pad, boolean isLeft) {
//        String string = input.toString();
//        if (fixLength <= string.length()) {
//            container.append(string);
//            return;
//        }
//        if (isLeft) {
//            container.append(string);
//            repeat(container, String.valueOf(pad), fixLength - string.length());
//        } else {
//            repeat(container, String.valueOf(pad), fixLength - string.length());
//            container.append(string);
//        }
//    }
//
//    // Trim
//    // -----------------------------------------------------------------------
//
//    /**
//     * Removes control characters (char &lt;= 32) from both ends of this String, handling {@code null}
//     * by returning {@code null}.
//     *
//     * <p>The String is trimmed using {@link String#trim()}. Trim removes start and end characters
//     * &lt;= 32. To strip whitespace use {@link #strip(String)}.
//     *
//     * <p>To trim your choice of characters, use the {@link #strip(String, String)} methods.
//     *
//     * <pre>
//     * StringHelper.trim(null)          = null
//     * StringHelper.trim("")            = ""
//     * StringHelper.trim("     ")       = ""
//     * StringHelper.trim("abc")         = "abc"
//     * StringHelper.trim("    abc    ") = "abc"
//     * </pre>
//     *
//     * @param str the String to be trimmed, may be null
//     * @return the trimmed string, {@code null} if null String input
//     */
//    public static String trim(final String str) {
//        return str == null ? null : str.trim();
//    }
//
//    // IndexOfAny chars
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks if CharSequence contains a search character, handling {@code null}. This method uses
//     * {@link String#indexOf(int)} if possible.
//     *
//     * <p>A {@code null} or empty ("") CharSequence will return {@code false}.
//     *
//     * <pre>
//     * StringHelper.contains(null, *)    = false
//     * StringHelper.contains("", *)      = false
//     * StringHelper.contains("abc", 'a') = true
//     * StringHelper.contains("abc", 'z') = false
//     * </pre>
//     *
//     * @param seq        the CharSequence to check, may be null
//     * @param searchChar the character to find
//     * @return true if the CharSequence contains the search character, false if not or {@code null}
//     * string input
//     */
//    public static boolean contains(final CharSequence seq, final int searchChar) {
//        if (Whether.empty(seq)) {
//            return false;
//        }
//        return CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
//    }
//
//    /**
//     * Checks if CharSequence contains a search CharSequence, handling {@code null}. This method uses
//     * {@link String#indexOf(String)} if possible.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}.
//     *
//     * <pre>
//     * StringHelper.contains(null, *)     = false
//     * StringHelper.contains(*, null)     = false
//     * StringHelper.contains("", "")      = true
//     * StringHelper.contains("abc", "")   = true
//     * StringHelper.contains("abc", "a")  = true
//     * StringHelper.contains("abc", "z")  = false
//     * </pre>
//     *
//     * @param seq       the CharSequence to check, may be null
//     * @param searchSeq the CharSequence to find, may be null
//     * @return true if the CharSequence contains the search CharSequence, false if not or {@code null}
//     * string input
//     * CharSequence)
//     */
//    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
//        if (seq == null || searchSeq == null) {
//            return false;
//        }
//        return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
//    }
//
//    // ContainsAny
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks if CharSequence contains a search CharSequence irrespective of case, handling {@code
//     * null}. Case-insensitivity is defined as by {@link String#equalsIgnoreCase(String)}.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}.
//     *
//     * <pre>
//     * StringHelper.contains(null, *) = false
//     * StringHelper.contains(*, null) = false
//     * StringHelper.contains("", "") = true
//     * StringHelper.contains("abc", "") = true
//     * StringHelper.contains("abc", "a") = true
//     * StringHelper.contains("abc", "z") = false
//     * StringHelper.contains("abc", "A") = true
//     * StringHelper.contains("abc", "Z") = false
//     * </pre>
//     *
//     * @param str       the CharSequence to check, may be null
//     * @param searchStr the CharSequence to find, may be null
//     * @return true if the CharSequence contains the search CharSequence irrespective of case or false
//     * if not or {@code null} string input
//     * containsIgnoreCase(CharSequence, CharSequence)
//     */
//    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
//        if (str == null || searchStr == null) {
//            return false;
//        }
//        final int len = searchStr.length();
//        final int max = str.length() - len;
//        for (int i = 0; i <= max; i++) {
//            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Check whether the given CharSequence contains any whitespace characters.
//     *
//     * @param seq the CharSequence to check (may be {@code null})
//     * @return {@code true} if the CharSequence is not empty and contains at least 1 whitespace
//     * character
//     * @see Character#isWhitespace
//     */
//    // From org.springframework.util.StringUtils, under Apache License 2.0
//    public static boolean containsWhitespace(final CharSequence seq) {
//        if (Whether.empty(seq)) {
//            return false;
//        }
//        final int strLen = seq.length();
//        for (int i = 0; i < strLen; i++) {
//            if (Character.isWhitespace(seq.charAt(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // IndexOfAnyBut chars
//    // -----------------------------------------------------------------------
//
//    /**
//     * Search a CharSequence to find the first index of any character in the given set of characters.
//     *
//     * <p>A {@code null} String will return {@code -1}. A {@code null} or zero length search array
//     * will return {@code -1}.
//     *
//     * <pre>
//     * StringHelper.indexOfAny(null, *)                = -1
//     * StringHelper.indexOfAny("", *)                  = -1
//     * StringHelper.indexOfAny(*, null)                = -1
//     * StringHelper.indexOfAny(*, [])                  = -1
//     * StringHelper.indexOfAny("zzabyycdxx",['z','a']) = 0
//     * StringHelper.indexOfAny("zzabyycdxx",['b','y']) = 3
//     * StringHelper.indexOfAny("aba", ['z'])           = -1
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the index of any of the chars, -1 if no match or null input
//     * char...)
//     */
//    public static int indexOfAny(final CharSequence cs, final char... searchChars) {
//        if (Whether.empty(cs) || Whether.empty(searchChars)) {
//            return INDEX_NOT_FOUND;
//        }
//        final int csLen = cs.length();
//        final int csLast = csLen - 1;
//        final int searchLen = searchChars.length;
//        final int searchLast = searchLen - 1;
//        for (int i = 0; i < csLen; i++) {
//            final char ch = cs.charAt(i);
//            for (int j = 0; j < searchLen; j++) {
//                if (searchChars[j] == ch) {
//                    if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
//                        // ch is a supplementary character
//                        if (searchChars[j + 1] == cs.charAt(i + 1)) {
//                            return i;
//                        }
//                    } else {
//                        return i;
//                    }
//                }
//            }
//        }
//        return INDEX_NOT_FOUND;
//    }
//
//    /**
//     * Search a CharSequence to find the first index of any character in the given set of characters.
//     *
//     * <p>A {@code null} String will return {@code -1}. A {@code null} search string will return
//     * {@code -1}.
//     *
//     * <pre>
//     * StringHelper.indexOfAny(null, *)            = -1
//     * StringHelper.indexOfAny("", *)              = -1
//     * StringHelper.indexOfAny(*, null)            = -1
//     * StringHelper.indexOfAny(*, "")              = -1
//     * StringHelper.indexOfAny("zzabyycdxx", "za") = 0
//     * StringHelper.indexOfAny("zzabyycdxx", "by") = 3
//     * StringHelper.indexOfAny("aba","z")          = -1
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the index of any of the chars, -1 if no match or null input
//     * String)
//     */
//    public static int indexOfAny(final CharSequence cs, final String searchChars) {
//        if (Whether.empty(cs) || Whether.empty(searchChars)) {
//            return INDEX_NOT_FOUND;
//        }
//        return indexOfAny(cs, searchChars.toCharArray());
//    }
//
//    // ContainsOnly
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks if the CharSequence contains any character in the given set of characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}. A {@code null} or zero length search
//     * array will return {@code false}.
//     *
//     * <pre>
//     * StringHelper.containsAny(null, *)                = false
//     * StringHelper.containsAny("", *)                  = false
//     * StringHelper.containsAny(*, null)                = false
//     * StringHelper.containsAny(*, [])                  = false
//     * StringHelper.containsAny("zzabyycdxx",['z','a']) = true
//     * StringHelper.containsAny("zzabyycdxx",['b','y']) = true
//     * StringHelper.containsAny("aba", ['z'])           = false
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
//     * char...)
//     */
//    public static boolean containsAny(final CharSequence cs, final char... searchChars) {
//        if (Whether.empty(cs) || Whether.empty(searchChars)) {
//            return false;
//        }
//        final int csLength = cs.length();
//        final int searchLength = searchChars.length;
//        final int csLast = csLength - 1;
//        final int searchLast = searchLength - 1;
//        for (int i = 0; i < csLength; i++) {
//            final char ch = cs.charAt(i);
//            for (int j = 0; j < searchLength; j++) {
//                if (searchChars[j] == ch) {
//                    if (Character.isHighSurrogate(ch)) {
//                        if (j == searchLast) {
//                            // missing low surrogate, fine, like String.indexOf(String)
//                            return true;
//                        }
//                        if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
//                            return true;
//                        }
//                    } else {
//                        // ch is in the Basic Multilingual Plane
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Checks if the CharSequence contains any character in the given set of characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}. A {@code null} search CharSequence
//     * will return {@code false}.
//     *
//     * <pre>
//     * StringHelper.containsAny(null, *)            = false
//     * StringHelper.containsAny("", *)              = false
//     * StringHelper.containsAny(*, null)            = false
//     * StringHelper.containsAny(*, "")              = false
//     * StringHelper.containsAny("zzabyycdxx", "za") = true
//     * StringHelper.containsAny("zzabyycdxx", "by") = true
//     * StringHelper.containsAny("aba","z")          = false
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
//     * CharSequence)
//     */
//    public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
//        if (searchChars == null) {
//            return false;
//        }
//        return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
//    }
//
//    // ContainsNone
//    // -----------------------------------------------------------------------
//
//    /**
//     * Searches a CharSequence to find the first index of any character not in the given set of
//     * characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length search
//     * array will return {@code -1}.
//     *
//     * <pre>
//     * StringHelper.indexOfAnyBut(null, *)                              = -1
//     * StringHelper.indexOfAnyBut("", *)                                = -1
//     * StringHelper.indexOfAnyBut(*, null)                              = -1
//     * StringHelper.indexOfAnyBut(*, [])                                = -1
//     * StringHelper.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
//     * StringHelper.indexOfAnyBut("aba", new char[] {'z'} )             = 0
//     * StringHelper.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
//     *
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the index of any of the chars, -1 if no match or null input
//     * char...)
//     */
//    public static int indexOfAnyBut(final CharSequence cs, final char... searchChars) {
//        if (Whether.empty(cs) || Whether.empty(searchChars)) {
//            return INDEX_NOT_FOUND;
//        }
//        final int csLen = cs.length();
//        final int csLast = csLen - 1;
//        final int searchLen = searchChars.length;
//        final int searchLast = searchLen - 1;
//        outer:
//        for (int i = 0; i < csLen; i++) {
//            final char ch = cs.charAt(i);
//            for (int j = 0; j < searchLen; j++) {
//                if (searchChars[j] == ch) {
//                    if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
//                        if (searchChars[j + 1] == cs.charAt(i + 1)) {
//                            continue outer;
//                        }
//                    } else {
//                        continue outer;
//                    }
//                }
//            }
//            return i;
//        }
//        return INDEX_NOT_FOUND;
//    }
//
//    /**
//     * Search a CharSequence to find the first index of any character not in the given set of
//     * characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code -1}. A {@code null} or empty search string
//     * will return {@code -1}.
//     *
//     * <pre>
//     * StringHelper.indexOfAnyBut(null, *)            = -1
//     * StringHelper.indexOfAnyBut("", *)              = -1
//     * StringHelper.indexOfAnyBut(*, null)            = -1
//     * StringHelper.indexOfAnyBut(*, "")              = -1
//     * StringHelper.indexOfAnyBut("zzabyycdxx", "za") = 3
//     * StringHelper.indexOfAnyBut("zzabyycdxx", "")   = -1
//     * StringHelper.indexOfAnyBut("aba","ab")         = -1
//     * </pre>
//     *
//     * @param seq         the CharSequence to check, may be null
//     * @param searchChars the chars to search for, may be null
//     * @return the index of any of the chars, -1 if no match or null input
//     * CharSequence)
//     */
//    public static int indexOfAnyBut(final CharSequence seq, final CharSequence searchChars) {
//        if (Whether.empty(seq) || Whether.empty(searchChars)) {
//            return INDEX_NOT_FOUND;
//        }
//        final int strLen = seq.length();
//        for (int i = 0; i < strLen; i++) {
//            final char ch = seq.charAt(i);
//            final boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
//            if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
//                final char ch2 = seq.charAt(i + 1);
//                if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
//                    return i;
//                }
//            } else {
//                if (!chFound) {
//                    return i;
//                }
//            }
//        }
//        return INDEX_NOT_FOUND;
//    }
//
//    // IndexOfAny strings
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks if the CharSequence contains only certain characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}. A {@code null} valid character array
//     * will return {@code false}. An empty CharSequence (length()=0) always returns {@code true}.
//     *
//     * <pre>
//     * StringHelper.containsOnly(null, *)       = false
//     * StringHelper.containsOnly(*, null)       = false
//     * StringHelper.containsOnly("", *)         = true
//     * StringHelper.containsOnly("ab", '')      = false
//     * StringHelper.containsOnly("abab", 'abc') = true
//     * StringHelper.containsOnly("ab1", 'abc')  = false
//     * StringHelper.containsOnly("abz", 'abc')  = false
//     * </pre>
//     *
//     * @param cs    the String to check, may be null
//     * @param valid an array of valid chars, may be null
//     * @return true if it only contains valid chars and is non-null
//     * char...)
//     */
//    public static boolean containsOnly(final CharSequence cs, final char... valid) {
//        // All these pre-checks are to maintain API with an older version
//        if (valid == null || cs == null) {
//            return false;
//        }
//        if (cs.length() == 0) {
//            return true;
//        }
//        if (valid.length == 0) {
//            return false;
//        }
//        return indexOfAnyBut(cs, valid) == INDEX_NOT_FOUND;
//    }
//
//    /**
//     * Checks if the CharSequence contains only certain characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code false}. A {@code null} valid character String
//     * will return {@code false}. An empty String (length()=0) always returns {@code true}.
//     *
//     * <pre>
//     * StringHelper.containsOnly(null, *)       = false
//     * StringHelper.containsOnly(*, null)       = false
//     * StringHelper.containsOnly("", *)         = true
//     * StringHelper.containsOnly("ab", "")      = false
//     * StringHelper.containsOnly("abab", "abc") = true
//     * StringHelper.containsOnly("ab1", "abc")  = false
//     * StringHelper.containsOnly("abz", "abc")  = false
//     * </pre>
//     *
//     * @param cs         the CharSequence to check, may be null
//     * @param validChars a String of valid chars, may be null
//     * @return true if it only contains valid chars and is non-null
//     * String)
//     */
//    public static boolean containsOnly(final CharSequence cs, final String validChars) {
//        if (cs == null || validChars == null) {
//            return false;
//        }
//        return containsOnly(cs, validChars.toCharArray());
//    }
//
//    // Substring
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks that the CharSequence does not contain certain characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array
//     * will return {@code true}. An empty CharSequence (length()=0) always returns true.
//     *
//     * <pre>
//     * StringHelper.containsNone(null, *)       = true
//     * StringHelper.containsNone(*, null)       = true
//     * StringHelper.containsNone("", *)         = true
//     * StringHelper.containsNone("ab", '')      = true
//     * StringHelper.containsNone("abab", 'xyz') = true
//     * StringHelper.containsNone("ab1", 'xyz')  = true
//     * StringHelper.containsNone("abz", 'xyz')  = false
//     * </pre>
//     *
//     * @param cs          the CharSequence to check, may be null
//     * @param searchChars an array of invalid chars, may be null
//     * @return true if it contains none of the invalid chars, or is null
//     * char...)
//     */
//    public static boolean containsNone(final CharSequence cs, final char... searchChars) {
//        if (cs == null || searchChars == null) {
//            return true;
//        }
//        final int csLen = cs.length();
//        final int csLast = csLen - 1;
//        final int searchLen = searchChars.length;
//        final int searchLast = searchLen - 1;
//        for (int i = 0; i < csLen; i++) {
//            final char ch = cs.charAt(i);
//            for (int j = 0; j < searchLen; j++) {
//                if (searchChars[j] == ch) {
//                    if (Character.isHighSurrogate(ch)) {
//                        if (j == searchLast) {
//                            // missing low surrogate, fine, like String.indexOf(String)
//                            return false;
//                        }
//                        if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
//                            return false;
//                        }
//                    } else {
//                        // ch is in the Basic Multilingual Plane
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks that the CharSequence does not contain certain characters.
//     *
//     * <p>A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array
//     * will return {@code true}. An empty String ("") always returns true.
//     *
//     * <pre>
//     * StringHelper.containsNone(null, *)       = true
//     * StringHelper.containsNone(*, null)       = true
//     * StringHelper.containsNone("", *)         = true
//     * StringHelper.containsNone("ab", "")      = true
//     * StringHelper.containsNone("abab", "xyz") = true
//     * StringHelper.containsNone("ab1", "xyz")  = true
//     * StringHelper.containsNone("abz", "xyz")  = false
//     * </pre>
//     *
//     * @param cs           the CharSequence to check, may be null
//     * @param invalidChars a String of invalid chars, may be null
//     * @return true if it contains none of the invalid chars, or is null
//     * String)
//     */
//    public static boolean containsNone(final CharSequence cs, final String invalidChars) {
//        if (cs == null || invalidChars == null) {
//            return true;
//        }
//        return containsNone(cs, invalidChars.toCharArray());
//    }
//
//    // Left/Right/Mid
//    // -----------------------------------------------------------------------
//
//    /**
//     * Find the first index of any of a set of potential substrings.
//     *
//     * <p>A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length search
//     * array will return {@code -1}. A {@code null} search array entry will be ignored, but a search
//     * array containing "" will return {@code 0} if {@code str} is not null. This method uses {@link
//     * String#indexOf(String)} if possible.
//     *
//     * <pre>
//     * StringHelper.indexOfAny(null, *)                     = -1
//     * StringHelper.indexOfAny(*, null)                     = -1
//     * StringHelper.indexOfAny(*, [])                       = -1
//     * StringHelper.indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
//     * StringHelper.indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
//     * StringHelper.indexOfAny("zzabyycdxx", ["mn","op"])   = -1
//     * StringHelper.indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
//     * StringHelper.indexOfAny("zzabyycdxx", [""])          = 0
//     * StringHelper.indexOfAny("", [""])                    = 0
//     * StringHelper.indexOfAny("", ["a"])                   = -1
//     * </pre>
//     *
//     * @param str        the CharSequence to check, may be null
//     * @param searchStrs the CharSequences to search for, may be null
//     * @return the first index of any of the searchStrs in str, -1 if no match
//     * CharSequence...)
//     */
//    public static int indexOfAny(final CharSequence str, final CharSequence... searchStrs) {
//        if (str == null || searchStrs == null) {
//            return INDEX_NOT_FOUND;
//        }
//        final int sz = searchStrs.length;
//
//        // String's can't have a MAX_VALUEth index.
//        int ret = Integer.MAX_VALUE;
//
//        int tmp;
//        for (final CharSequence search : searchStrs) {
//            if (search == null) {
//                continue;
//            }
//            tmp = CharSequenceUtils.indexOf(str, search, 0);
//            if (tmp == INDEX_NOT_FOUND) {
//                continue;
//            }
//
//            if (tmp < ret) {
//                ret = tmp;
//            }
//        }
//
//        return ret == Integer.MAX_VALUE ? INDEX_NOT_FOUND : ret;
//    }
//
//    /**
//     * Find the latest index of any of a set of potential substrings.
//     *
//     * <p>A {@code null} CharSequence will return {@code -1}. A {@code null} search array will return
//     * {@code -1}. A {@code null} or zero length search array entry will be ignored, but a search
//     * array containing "" will return the length of {@code str} if {@code str} is not null. This
//     * method uses {@link String#indexOf(String)} if possible
//     *
//     * <pre>
//     * StringHelper.lastIndexOfAny(null, *)                   = -1
//     * StringHelper.lastIndexOfAny(*, null)                   = -1
//     * StringHelper.lastIndexOfAny(*, [])                     = -1
//     * StringHelper.lastIndexOfAny(*, [null])                 = -1
//     * StringHelper.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
//     * StringHelper.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
//     * StringHelper.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
//     * StringHelper.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
//     * StringHelper.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
//     * </pre>
//     *
//     * @param str        the CharSequence to check, may be null
//     * @param searchStrs the CharSequences to search for, may be null
//     * @return the last index of any of the CharSequences, -1 if no match
//     * lastIndexOfAny(CharSequence, CharSequence)
//     */
//    public static int lastIndexOfAny(final CharSequence str, final CharSequence... searchStrs) {
//        if (str == null || searchStrs == null) {
//            return INDEX_NOT_FOUND;
//        }
//        final int sz = searchStrs.length;
//        int ret = INDEX_NOT_FOUND;
//        int tmp;
//        for (final CharSequence search : searchStrs) {
//            if (search == null) {
//                continue;
//            }
//            tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
//            if (tmp > ret) {
//                ret = tmp;
//            }
//        }
//        return ret;
//    }
//
//    /**
//     * Gets a substring from the specified String avoiding exceptions.
//     *
//     * <p>A negative start position can be used to start {@code n} characters from the end of the
//     * String.
//     *
//     * <p>A {@code null} String will return {@code null}. An empty ("") String will return "".
//     *
//     * <pre>
//     * StringHelper.substring(null, *)   = null
//     * StringHelper.substring("", *)     = ""
//     * StringHelper.substring("abc", 0)  = "abc"
//     * StringHelper.substring("abc", 2)  = "c"
//     * StringHelper.substring("abc", 4)  = ""
//     * StringHelper.substring("abc", -2) = "bc"
//     * StringHelper.substring("abc", -4) = "abc"
//     * </pre>
//     *
//     * @param str   the String to get the substring from, may be null
//     * @param start the position to start from, negative means count back from the end of the String
//     *              by this many characters
//     * @return substring from start position, {@code null} if null String input
//     */
//    public static String substring(final String str, int start) {
//        if (str == null) {
//            return null;
//        }
//
//        // handle negatives, which means last n characters
//        if (start < 0) {
//            start = str.length() + start; // remember start is negative
//        }
//
//        if (start < 0) {
//            start = 0;
//        }
//        if (start > str.length()) {
//            return EMPTY;
//        }
//
//        return str.substring(start);
//    }
//
//    // SubStringAfter/SubStringBefore
//    // -----------------------------------------------------------------------
//
//    /**
//     * Gets a substring from the specified String avoiding exceptions.
//     *
//     * <p>A negative start position can be used to start/end {@code n} characters from the end of the
//     * String.
//     *
//     * <p>The returned substring starts with the character in the {@code start} position and ends
//     * before the {@code end} position. All position counting is zero-based -- i.e., to start at the
//     * beginning of the string use {@code start = 0}. Negative start and end positions can be used to
//     * specify offsets relative to the end of the String.
//     *
//     * <p>If {@code start} is not strictly to the left of {@code end}, "" is returned.
//     *
//     * <pre>
//     * StringHelper.substring(null, *, *)    = null
//     * StringHelper.substring("", * ,  *)    = "";
//     * StringHelper.substring("abc", 0, 2)   = "ab"
//     * StringHelper.substring("abc", 2, 0)   = ""
//     * StringHelper.substring("abc", 2, 4)   = "c"
//     * StringHelper.substring("abc", 4, 6)   = ""
//     * StringHelper.substring("abc", 2, 2)   = ""
//     * StringHelper.substring("abc", -2, -1) = "b"
//     * StringHelper.substring("abc", -4, 2)  = "ab"
//     * </pre>
//     *
//     * @param str   the String to get the substring from, may be null
//     * @param start the position to start from, negative means count back from the end of the String
//     *              by this many characters
//     * @param end   the position to end at (exclusive), negative means count back from the end of the
//     *              String by this many characters
//     * @return substring from start position to end position, {@code null} if null String input
//     */
//    public static String substring(final String str, int start, int end) {
//        if (str == null) {
//            return null;
//        }
//
//        // handle negatives
//        if (end < 0) {
//            end = str.length() + end; // remember end is negative
//        }
//        if (start < 0) {
//            start = str.length() + start; // remember start is negative
//        }
//
//        // check length next
//        if (end > str.length()) {
//            end = str.length();
//        }
//
//        // if start is greater than end, return ""
//        if (start > end) {
//            return EMPTY;
//        }
//
//        if (start < 0) {
//            start = 0;
//        }
//        if (end < 0) {
//            end = 0;
//        }
//
//        return str.substring(start, end);
//    }
//
//    /**
//     * Gets the leftmost {@code len} characters of a String.
//     *
//     * <p>If {@code len} characters are not available, or the String is {@code null}, the String will
//     * be returned without an exception. An empty String is returned if len is negative.
//     *
//     * <pre>
//     * StringHelper.left(null, *)    = null
//     * StringHelper.left(*, -ve)     = ""
//     * StringHelper.left("", *)      = ""
//     * StringHelper.left("abc", 0)   = ""
//     * StringHelper.left("abc", 2)   = "ab"
//     * StringHelper.left("abc", 4)   = "abc"
//     * </pre>
//     *
//     * @param str the String to get the leftmost characters from, may be null
//     * @param len the length of the required String
//     * @return the leftmost characters, {@code null} if null String input
//     */
//    public static String left(final String str, final int len) {
//        if (str == null) {
//            return null;
//        }
//        if (len < 0) {
//            return EMPTY;
//        }
//        if (str.length() <= len) {
//            return str;
//        }
//        return str.substring(0, len);
//    }
//
//    /**
//     * Gets the rightmost {@code len} characters of a String.
//     *
//     * <p>If {@code len} characters are not available, or the String is {@code null}, the String will
//     * be returned without an an exception. An empty String is returned if len is negative.
//     *
//     * <pre>
//     * StringHelper.right(null, *)    = null
//     * StringHelper.right(*, -ve)     = ""
//     * StringHelper.right("", *)      = ""
//     * StringHelper.right("abc", 0)   = ""
//     * StringHelper.right("abc", 2)   = "bc"
//     * StringHelper.right("abc", 4)   = "abc"
//     * </pre>
//     *
//     * @param str the String to get the rightmost characters from, may be null
//     * @param len the length of the required String
//     * @return the rightmost characters, {@code null} if null String input
//     */
//    public static String right(final String str, final int len) {
//        if (str == null) {
//            return null;
//        }
//        if (len < 0) {
//            return EMPTY;
//        }
//        if (str.length() <= len) {
//            return str;
//        }
//        return str.substring(str.length() - len);
//    }
//
//    /**
//     * Gets {@code len} characters from the middle of a String.
//     *
//     * <p>If {@code len} characters are not available, the remainder of the String will be returned
//     * without an exception. If the String is {@code null}, {@code null} will be returned. An empty
//     * String is returned if len is negative or exceeds the length of {@code str}.
//     *
//     * <pre>
//     * StringHelper.mid(null, *, *)    = null
//     * StringHelper.mid(*, *, -ve)     = ""
//     * StringHelper.mid("", 0, *)      = ""
//     * StringHelper.mid("abc", 0, 2)   = "ab"
//     * StringHelper.mid("abc", 0, 4)   = "abc"
//     * StringHelper.mid("abc", 2, 4)   = "c"
//     * StringHelper.mid("abc", 4, 2)   = ""
//     * StringHelper.mid("abc", -2, 2)  = "ab"
//     * </pre>
//     *
//     * @param str the String to get the characters from, may be null
//     * @param pos the position to start from, negative treated as zero
//     * @param len the length of the required String
//     * @return the middle characters, {@code null} if null String input
//     */
//    public static String mid(final String str, int pos, final int len) {
//        if (str == null) {
//            return null;
//        }
//        if (len < 0 || pos > str.length()) {
//            return EMPTY;
//        }
//        if (pos < 0) {
//            pos = 0;
//        }
//        if (str.length() <= pos + len) {
//            return str.substring(pos);
//        }
//        return str.substring(pos, pos + len);
//    }
//
//    // Substring between
//    // -----------------------------------------------------------------------
//
//    /**
//     * Gets the substring before the first occurrence of a separator. The separator is not returned.
//     *
//     * <p>A {@code null} string input will return {@code null}. An empty ("") string input will return
//     * the empty string. A {@code null} separator will return the input string.
//     *
//     * <p>If nothing is found, the string input is returned.
//     *
//     * <pre>
//     * StringHelper.substringBefore(null, *)      = null
//     * StringHelper.substringBefore("", *)        = ""
//     * StringHelper.substringBefore("abc", "a")   = ""
//     * StringHelper.substringBefore("abcba", "b") = "a"
//     * StringHelper.substringBefore("abc", "c")   = "ab"
//     * StringHelper.substringBefore("abc", "d")   = "abc"
//     * StringHelper.substringBefore("abc", "")    = ""
//     * StringHelper.substringBefore("abc", null)  = "abc"
//     * </pre>
//     *
//     * @param str       the String to get a substring from, may be null
//     * @param separator the String to search for, may be null
//     * @return the substring before the first occurrence of the separator, {@code null} if null String
//     * input
//     */
//    public static String substringBefore(final String str, final String separator) {
//        if (Whether.empty(str) || separator == null) {
//            return str;
//        }
//        if (Whether.empty(separator)) {
//            return EMPTY;
//        }
//        final int pos = str.indexOf(separator);
//        if (pos == INDEX_NOT_FOUND) {
//            return str;
//        }
//        return str.substring(0, pos);
//    }
//
//    /**
//     * Gets the substring after the first occurrence of a separator. The separator is not returned.
//     *
//     * <p>A {@code null} string input will return {@code null}. An empty ("") string input will return
//     * the empty string. A {@code null} separator will return the empty string if the input string is
//     * not {@code null}.
//     *
//     * <p>If nothing is found, the empty string is returned.
//     *
//     * <pre>
//     * StringHelper.substringAfter(null, *)      = null
//     * StringHelper.substringAfter("", *)        = ""
//     * StringHelper.substringAfter(*, null)      = ""
//     * StringHelper.substringAfter("abc", "a")   = "bc"
//     * StringHelper.substringAfter("abcba", "b") = "cba"
//     * StringHelper.substringAfter("abc", "c")   = ""
//     * StringHelper.substringAfter("abc", "d")   = ""
//     * StringHelper.substringAfter("abc", "")    = "abc"
//     * </pre>
//     *
//     * @param str       the String to get a substring from, may be null
//     * @param separator the String to search for, may be null
//     * @return the substring after the first occurrence of the separator, {@code null} if null String
//     * input
//     */
//    public static String substringAfter(final String str, final String separator) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//        if (separator == null) {
//            return EMPTY;
//        }
//        final int pos = str.indexOf(separator);
//        if (pos == INDEX_NOT_FOUND) {
//            return EMPTY;
//        }
//        return str.substring(pos + separator.length());
//    }
//
//    /**
//     * Gets the substring before the last occurrence of a separator. The separator is not returned.
//     *
//     * <p>A {@code null} string input will return {@code null}. An empty ("") string input will return
//     * the empty string. An empty or {@code null} separator will return the input string.
//     *
//     * <p>If nothing is found, the string input is returned.
//     *
//     * <pre>
//     * StringHelper.substringBeforeLast(null, *)      = null
//     * StringHelper.substringBeforeLast("", *)        = ""
//     * StringHelper.substringBeforeLast("abcba", "b") = "abc"
//     * StringHelper.substringBeforeLast("abc", "c")   = "ab"
//     * StringHelper.substringBeforeLast("a", "a")     = ""
//     * StringHelper.substringBeforeLast("a", "z")     = "a"
//     * StringHelper.substringBeforeLast("a", null)    = "a"
//     * StringHelper.substringBeforeLast("a", "")      = "a"
//     * </pre>
//     *
//     * @param str       the String to get a substring from, may be null
//     * @param separator the String to search for, may be null
//     * @return the substring before the last occurrence of the separator, {@code null} if null String
//     * input
//     */
//    public static String substringBeforeLast(final String str, final String separator) {
//        if (Whether.empty(str) || Whether.empty(separator)) {
//            return str;
//        }
//        final int pos = str.lastIndexOf(separator);
//        if (pos == INDEX_NOT_FOUND) {
//            return str;
//        }
//        return str.substring(0, pos);
//    }
//
//    // Nested extraction
//    // -----------------------------------------------------------------------
//
//    // Splitting
//    // -----------------------------------------------------------------------
//
//    /**
//     * Gets the substring after the last occurrence of a separator. The separator is not returned.
//     *
//     * <p>A {@code null} string input will return {@code null}. An empty ("") string input will return
//     * the empty string. An empty or {@code null} separator will return the empty string if the input
//     * string is not {@code null}.
//     *
//     * <p>If nothing is found, the empty string is returned.
//     *
//     * <pre>
//     * StringHelper.substringAfterLast(null, *)      = null
//     * StringHelper.substringAfterLast("", *)        = ""
//     * StringHelper.substringAfterLast(*, "")        = ""
//     * StringHelper.substringAfterLast(*, null)      = ""
//     * StringHelper.substringAfterLast("abc", "a")   = "bc"
//     * StringHelper.substringAfterLast("abcba", "b") = "a"
//     * StringHelper.substringAfterLast("abc", "c")   = ""
//     * StringHelper.substringAfterLast("a", "a")     = ""
//     * StringHelper.substringAfterLast("a", "z")     = ""
//     * </pre>
//     *
//     * @param str       the String to get a substring from, may be null
//     * @param separator the String to search for, may be null
//     * @return the substring after the last occurrence of the separator, {@code null} if null String
//     * input
//     */
//    public static String substringAfterLast(final String str, final String separator) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//        if (Whether.empty(separator)) {
//            return EMPTY;
//        }
//        final int pos = str.lastIndexOf(separator);
//        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
//            return EMPTY;
//        }
//        return str.substring(pos + separator.length());
//    }
//
//    /**
//     * Gets the String that is nested in between two instances of the same String.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} tag returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.substringBetween(null, *)            = null
//     * StringHelper.substringBetween("", "")             = ""
//     * StringHelper.substringBetween("", "tag")          = null
//     * StringHelper.substringBetween("tagabctag", null)  = null
//     * StringHelper.substringBetween("tagabctag", "")    = ""
//     * StringHelper.substringBetween("tagabctag", "tag") = "abc"
//     * </pre>
//     *
//     * @param str the String containing the substring, may be null
//     * @param tag the String before and after the substring, may be null
//     * @return the substring, {@code null} if no match
//     */
//    public static String substringBetween(final String str, final String tag) {
//        return substringBetween(str, tag, tag);
//    }
//
//    /**
//     * Gets the String that is nested in between two Strings. Only the first match is returned.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} open/close returns {@code
//     * null} (no match). An empty ("") open and close returns an empty string.
//     *
//     * <pre>
//     * StringHelper.substringBetween("wx[b]yz", "[", "]") = "b"
//     * StringHelper.substringBetween(null, *, *)          = null
//     * StringHelper.substringBetween(*, null, *)          = null
//     * StringHelper.substringBetween(*, *, null)          = null
//     * StringHelper.substringBetween("", "", "")          = ""
//     * StringHelper.substringBetween("", "", "]")         = null
//     * StringHelper.substringBetween("", "[", "]")        = null
//     * StringHelper.substringBetween("yabcz", "", "")     = ""
//     * StringHelper.substringBetween("yabcz", "y", "z")   = "abc"
//     * StringHelper.substringBetween("yabczyabcz", "y", "z")   = "abc"
//     * </pre>
//     *
//     * @param str   the String containing the substring, may be null
//     * @param open  the String before the substring, may be null
//     * @param close the String after the substring, may be null
//     * @return the substring, {@code null} if no match
//     */
//    public static String substringBetween(final String str, final String open, final String close) {
//        if (str == null || open == null || close == null) {
//            return null;
//        }
//        final int start = str.indexOf(open);
//        if (start != INDEX_NOT_FOUND) {
//            final int end = str.indexOf(close, start + open.length());
//            if (end != INDEX_NOT_FOUND) {
//                return str.substring(start + open.length(), end);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Searches a String for substrings delimited by a start and end tag, returning all matching
//     * substrings in an array.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} open/close returns {@code
//     * null} (no match). An empty ("") open/close returns {@code null} (no match).
//     *
//     * <pre>
//     * StringHelper.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
//     * StringHelper.substringsBetween(null, *, *)            = null
//     * StringHelper.substringsBetween(*, null, *)            = null
//     * StringHelper.substringsBetween(*, *, null)            = null
//     * StringHelper.substringsBetween("", "[", "]")          = []
//     * </pre>
//     *
//     * @param str   the String containing the substrings, null returns null, empty returns empty
//     * @param open  the String identifying the start of the substring, empty returns null
//     * @param close the String identifying the end of the substring, empty returns null
//     * @return a String Array of substrings, or {@code null} if no match
//     */
//    public static String[] substringsBetween(
//            final String str, final String open, final String close) {
//        if (str == null || Whether.empty(open) || Whether.empty(close)) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen == 0) {
//            return PoolOfArray.EMPTY_STRING_ARRAY;
//        }
//        final int closeLen = close.length();
//        final int openLen = open.length();
//        final List<String> list = new ArrayList<String>();
//        int pos = 0;
//        while (pos < strLen - closeLen) {
//            int start = str.indexOf(open, pos);
//            if (start < 0) {
//                break;
//            }
//            start += openLen;
//            final int end = str.indexOf(close, start);
//            if (end < 0) {
//                break;
//            }
//            list.add(str.substring(start, end));
//            pos = end + closeLen;
//        }
//        if (Whether.empty(list)) {
//            return null;
//        }
//        return list.toArray(new String[0]);
//    }
//
//    /**
//     * Splits the provided text into an array, using whitespace as the separator. Whitespace is
//     * defined by {@link Character#isWhitespace(char)}.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as one separator. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.split(null)       = null
//     * StringHelper.split("")         = []
//     * StringHelper.split("abc def")  = ["abc", "def"]
//     * StringHelper.split("abc  def") = ["abc", "def"]
//     * StringHelper.split(" abc ")    = ["abc"]
//     * </pre>
//     *
//     * @param str the String to parse, may be null
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] split(final String str) {
//        return split(str, null, -1);
//    }
//
//    /**
//     * Splits the provided text into an array, separator specified. This is an alternative to using
//     * StringTokenizer.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as one separator. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.split(null, *)         = null
//     * StringHelper.split("", *)           = []
//     * StringHelper.split("a.b.c", '.')    = ["a", "b", "c"]
//     * StringHelper.split("a..b.c", '.')   = ["a", "b", "c"]
//     * StringHelper.split("a:b:c", '.')    = ["a:b:c"]
//     * StringHelper.split("a b c", ' ')    = ["a", "b", "c"]
//     * </pre>
//     *
//     * @param str           the String to parse, may be null
//     * @param separatorChar the character used as the delimiter
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] split(final String str, final char separatorChar) {
//        return splitWorker(str, separatorChar, false);
//    }
//
//    /**
//     * Splits the provided text into an array, separators specified. This is an alternative to using
//     * StringTokenizer.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as one separator. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.split(null, *)         = null
//     * StringHelper.split("", *)           = []
//     * StringHelper.split("abc def", null) = ["abc", "def"]
//     * StringHelper.split("abc def", " ")  = ["abc", "def"]
//     * StringHelper.split("abc  def", " ") = ["abc", "def"]
//     * StringHelper.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
//     * </pre>
//     *
//     * @param str            the String to parse, may be null
//     * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] split(final String str, final String separatorChars) {
//        return splitWorker(str, separatorChars, -1, false);
//    }
//
//    /**
//     * Splits the provided text into an array with a maximum length, separators specified.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as one separator.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
//     * whitespace.
//     *
//     * <p>If more than {@code max} delimited substrings are found, the last returned string includes
//     * all characters after the first {@code max - 1} returned strings (including separator
//     * characters).
//     *
//     * <pre>
//     * StringHelper.split(null, *, *)            = null
//     * StringHelper.split("", *, *)              = []
//     * StringHelper.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
//     * StringHelper.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
//     * StringHelper.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
//     * StringHelper.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
//     * </pre>
//     *
//     * @param str            the String to parse, may be null
//     * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
//     * @param max            the maximum number of elements to include in the array. A zero or negative value
//     *                       implies no limit
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] split(final String str, final String separatorChars, final int max) {
//        return splitWorker(str, separatorChars, max, false);
//    }
//
//    /**
//     * Splits the provided text into an array, separator string specified.
//     *
//     * <p>The separator(s) will not be included in the returned String array. Adjacent separators are
//     * treated as one separator.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separator splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.splitByWholeSeparator(null, *)               = null
//     * StringHelper.splitByWholeSeparator("", *)                 = []
//     * StringHelper.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
//     * StringHelper.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
//     * </pre>
//     *
//     * @param str       the String to parse, may be null
//     * @param separator String containing the String to be used as a delimiter, {@code null} splits on
//     *                  whitespace
//     * @return an array of parsed Strings, {@code null} if null String was input
//     */
//    public static String[] splitByWholeSeparator(final String str, final String separator) {
//        return splitByWholeSeparatorWorker(str, separator, -1, false);
//    }
//
//    // -----------------------------------------------------------------------
//
//    /**
//     * Splits the provided text into an array, separator string specified. Returns a maximum of {@code
//     * max} substrings.
//     *
//     * <p>The separator(s) will not be included in the returned String array. Adjacent separators are
//     * treated as one separator.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separator splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.splitByWholeSeparator(null, *, *)               = null
//     * StringHelper.splitByWholeSeparator("", *, *)                 = []
//     * StringHelper.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
//     * StringHelper.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
//     * StringHelper.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
//     * </pre>
//     *
//     * @param str       the String to parse, may be null
//     * @param separator String containing the String to be used as a delimiter, {@code null} splits on
//     *                  whitespace
//     * @param max       the maximum number of elements to include in the returned array. A zero or negative
//     *                  value implies no limit.
//     * @return an array of parsed Strings, {@code null} if null String was input
//     */
//    public static String[] splitByWholeSeparator(
//            final String str, final String separator, final int max) {
//        return splitByWholeSeparatorWorker(str, separator, max, false);
//    }
//
//    /**
//     * Splits the provided text into an array, separator string specified.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separator splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens(null, *)               = null
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("", *)                 = []
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
//     * </pre>
//     *
//     * @param str       the String to parse, may be null
//     * @param separator String containing the String to be used as a delimiter, {@code null} splits on
//     *                  whitespace
//     * @return an array of parsed Strings, {@code null} if null String was input
//     */
//    public static String[] splitByWholeSeparatorPreserveAllTokens(
//            final String str, final String separator) {
//        return splitByWholeSeparatorWorker(str, separator, -1, true);
//    }
//
//    /**
//     * Splits the provided text into an array, separator string specified. Returns a maximum of {@code
//     * max} substrings.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separator splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
//     * StringHelper.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
//     * </pre>
//     *
//     * @param str       the String to parse, may be null
//     * @param separator String containing the String to be used as a delimiter, {@code null} splits on
//     *                  whitespace
//     * @param max       the maximum number of elements to include in the returned array. A zero or negative
//     *                  value implies no limit.
//     * @return an array of parsed Strings, {@code null} if null String was input
//     */
//    public static String[] splitByWholeSeparatorPreserveAllTokens(
//            final String str, final String separator, final int max) {
//        return splitByWholeSeparatorWorker(str, separator, max, true);
//    }
//
//    /**
//     * Performs the logic for the {@code splitByWholeSeparatorPreserveAllTokens} methods.
//     *
//     * @param str               the String to parse, may be {@code null}
//     * @param separator         String containing the String to be used as a delimiter, {@code null} splits on
//     *                          whitespace
//     * @param max               the maximum number of elements to include in the returned array. A zero or negative
//     *                          value implies no limit.
//     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
//     *                          separators; if {@code false}, adjacent separators are treated as one separator.
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    private static String[] splitByWholeSeparatorWorker(
//            final String str, final String separator, final int max, final boolean preserveAllTokens) {
//        if (str == null) {
//            return null;
//        }
//
//        final int len = str.length();
//
//        if (len == 0) {
//            return PoolOfArray.EMPTY_STRING_ARRAY;
//        }
//
//        if (separator == null || EMPTY.equals(separator)) {
//            // Split on whitespace.
//            return splitWorker(str, null, max, preserveAllTokens);
//        }
//
//        final int separatorLength = separator.length();
//
//        final ArrayList<String> substrings = new ArrayList<String>();
//        int numberOfSubstrings = 0;
//        int beg = 0;
//        int end = 0;
//        while (end < len) {
//            end = str.indexOf(separator, beg);
//
//            if (end > -1) {
//                if (end > beg) {
//                    numberOfSubstrings += 1;
//
//                    if (numberOfSubstrings == max) {
//                        end = len;
//                        substrings.add(str.substring(beg));
//                    } else {
//                        // The following is OK, because String.substring( beg, end ) excludes
//                        // the character at the position 'end'.
//                        substrings.add(str.substring(beg, end));
//
//                        // Set the starting point for the next search.
//                        // The following is equivalent to beg = end + (separatorLength - 1) + 1,
//                        // which is the right calculation:
//                        beg = end + separatorLength;
//                    }
//                } else {
//                    // We found a consecutive occurrence of the separator, so skip it.
//                    if (preserveAllTokens) {
//                        numberOfSubstrings += 1;
//                        if (numberOfSubstrings == max) {
//                            end = len;
//                            substrings.add(str.substring(beg));
//                        } else {
//                            substrings.add(EMPTY);
//                        }
//                    }
//                    beg = end + separatorLength;
//                }
//            } else {
//                // String.substring( beg ) goes from 'beg' to the end of the String.
//                substrings.add(str.substring(beg));
//                end = len;
//            }
//        }
//
//        return substrings.toArray(new String[0]);
//    }
//
//    /**
//     * Splits the provided text into an array, using whitespace as the separator, preserving all
//     * tokens, including empty tokens created by adjacent separators. This is an alternative to using
//     * StringTokenizer. Whitespace is defined by {@link Character#isWhitespace(char)}.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.splitPreserveAllTokens(null)       = null
//     * StringHelper.splitPreserveAllTokens("")         = []
//     * StringHelper.splitPreserveAllTokens("abc def")  = ["abc", "def"]
//     * StringHelper.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
//     * StringHelper.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
//     * </pre>
//     *
//     * @param str the String to parse, may be {@code null}
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitPreserveAllTokens(final String str) {
//        return splitWorker(str, null, -1, true);
//    }
//
//    /**
//     * Splits the provided text into an array, separator specified, preserving all tokens, including
//     * empty tokens created by adjacent separators. This is an alternative to using StringTokenizer.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.splitPreserveAllTokens(null, *)         = null
//     * StringHelper.splitPreserveAllTokens("", *)           = []
//     * StringHelper.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
//     * StringHelper.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
//     * StringHelper.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
//     * StringHelper.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
//     * StringHelper.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
//     * StringHelper.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
//     * StringHelper.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
//     * StringHelper.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
//     * StringHelper.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
//     * StringHelper.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
//     * </pre>
//     *
//     * @param str           the String to parse, may be {@code null}
//     * @param separatorChar the character used as the delimiter, {@code null} splits on whitespace
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitPreserveAllTokens(final String str, final char separatorChar) {
//        return splitWorker(str, separatorChar, true);
//    }
//
//    /**
//     * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that do not
//     * return a maximum array length.
//     *
//     * @param str               the String to parse, may be {@code null}
//     * @param separatorChar     the separate character
//     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
//     *                          separators; if {@code false}, adjacent separators are treated as one separator.
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    private static String[] splitWorker(
//            final String str, final char separatorChar, final boolean preserveAllTokens) {
//
//        if (str == null) {
//            return null;
//        }
//        final int len = str.length();
//        if (len == 0) {
//            return PoolOfArray.EMPTY_STRING_ARRAY;
//        }
//        final List<String> list = new ArrayList<String>();
//        int i = 0, start = 0;
//        boolean match = false;
//        boolean lastMatch = false;
//        while (i < len) {
//            if (str.charAt(i) == separatorChar) {
//                if (match || preserveAllTokens) {
//                    list.add(str.substring(start, i));
//                    match = false;
//                    lastMatch = true;
//                }
//                start = ++i;
//                continue;
//            }
//            lastMatch = false;
//            match = true;
//            i++;
//        }
//        if (match || preserveAllTokens && lastMatch) {
//            list.add(str.substring(start, i));
//        }
//        return list.toArray(new String[0]);
//    }
//
//    /**
//     * Splits the provided text into an array, separators specified, preserving all tokens, including
//     * empty tokens created by adjacent separators. This is an alternative to using StringTokenizer.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. For more control over the split use the StrTokenizer class.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
//     * whitespace.
//     *
//     * <pre>
//     * StringHelper.splitPreserveAllTokens(null, *)           = null
//     * StringHelper.splitPreserveAllTokens("", *)             = []
//     * StringHelper.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
//     * StringHelper.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
//     * StringHelper.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
//     * StringHelper.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
//     * StringHelper.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
//     * StringHelper.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
//     * StringHelper.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
//     * StringHelper.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
//     * StringHelper.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
//     * StringHelper.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
//     * </pre>
//     *
//     * @param str            the String to parse, may be {@code null}
//     * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitPreserveAllTokens(final String str, final String separatorChars) {
//        return splitWorker(str, separatorChars, -1, true);
//    }
//
//    /**
//     * Splits the provided text into an array with a maximum length, separators specified, preserving
//     * all tokens, including empty tokens created by adjacent separators.
//     *
//     * <p>The separator is not included in the returned String array. Adjacent separators are treated
//     * as separators for empty tokens. Adjacent separators are treated as one separator.
//     *
//     * <p>A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
//     * whitespace.
//     *
//     * <p>If more than {@code max} delimited substrings are found, the last returned string includes
//     * all characters after the first {@code max - 1} returned strings (including separator
//     * characters).
//     *
//     * <pre>
//     * StringHelper.splitPreserveAllTokens(null, *, *)            = null
//     * StringHelper.splitPreserveAllTokens("", *, *)              = []
//     * StringHelper.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
//     * StringHelper.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
//     * StringHelper.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
//     * StringHelper.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
//     * StringHelper.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
//     * StringHelper.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
//     * StringHelper.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
//     * </pre>
//     *
//     * @param str            the String to parse, may be {@code null}
//     * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
//     * @param max            the maximum number of elements to include in the array. A zero or negative value
//     *                       implies no limit
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitPreserveAllTokens(
//            final String str, final String separatorChars, final int max) {
//        return splitWorker(str, separatorChars, max, true);
//    }
//
//    // Joining
//    // -----------------------------------------------------------------------
//
//    /**
//     * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that return
//     * a maximum array length.
//     *
//     * @param str               the String to parse, may be {@code null}
//     * @param separatorChars    the separate character
//     * @param max               the maximum number of elements to include in the array. A zero or negative value
//     *                          implies no limit.
//     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
//     *                          separators; if {@code false}, adjacent separators are treated as one separator.
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    private static String[] splitWorker(
//            final String str,
//            final String separatorChars,
//            final int max,
//            final boolean preserveAllTokens) {
//
//        // Direct code is quicker than StringTokenizer.
//        // Also, StringTokenizer uses isSpace() not isWhitespace()
//
//        if (str == null) {
//            return null;
//        }
//        final int len = str.length();
//        if (len == 0) {
//            return PoolOfArray.EMPTY_STRING_ARRAY;
//        }
//        final List<String> list = new ArrayList<String>();
//        int sizePlus1 = 1;
//        int i = 0, start = 0;
//        boolean match = false;
//        boolean lastMatch = false;
//        if (separatorChars == null) {
//            // Null separator means use whitespace
//            while (i < len) {
//                if (Character.isWhitespace(str.charAt(i))) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        } else if (separatorChars.length() == 1) {
//            // Optimise 1 character case
//            final char sep = separatorChars.charAt(0);
//            while (i < len) {
//                if (str.charAt(i) == sep) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        } else {
//            // standard case
//            while (i < len) {
//                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
//                    if (match || preserveAllTokens) {
//                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
//                        list.add(str.substring(start, i));
//                        match = false;
//                    }
//                    start = ++i;
//                    continue;
//                }
//                lastMatch = false;
//                match = true;
//                i++;
//            }
//        }
//        if (match || preserveAllTokens && lastMatch) {
//            list.add(str.substring(start, i));
//        }
//        return list.toArray(new String[0]);
//    }
//
//    /**
//     * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
//     * Groups of contiguous characters of the same type are returned as complete tokens.
//     *
//     * <pre>
//     * StringHelper.splitByCharacterType(null)         = null
//     * StringHelper.splitByCharacterType("")           = []
//     * StringHelper.splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
//     * StringHelper.splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
//     * StringHelper.splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
//     * StringHelper.splitByCharacterType("number5")    = ["number", "5"]
//     * StringHelper.splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
//     * StringHelper.splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
//     * StringHelper.splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
//     * </pre>
//     *
//     * @param str the String to split, may be {@code null}
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitByCharacterType(final String str) {
//        return splitByCharacterType(str, false);
//    }
//
//    /**
//     * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
//     * Groups of contiguous characters of the same type are returned as complete tokens, with the
//     * following exception: the character of type {@code Character.UPPERCASE_LETTER}, if any,
//     * immediately preceding a token of type {@code Character.LOWERCASE_LETTER} will belong to the
//     * following token rather than to the preceding, if any, {@code Character.UPPERCASE_LETTER} token.
//     *
//     * <pre>
//     * StringHelper.splitByCharacterTypeCamelCase(null)         = null
//     * StringHelper.splitByCharacterTypeCamelCase("")           = []
//     * StringHelper.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
//     * StringHelper.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
//     * StringHelper.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
//     * StringHelper.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
//     * StringHelper.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
//     * StringHelper.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
//     * StringHelper.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
//     * </pre>
//     *
//     * @param str the String to split, may be {@code null}
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    public static String[] splitByCharacterTypeCamelCase(final String str) {
//        return splitByCharacterType(str, true);
//    }
//
//    /**
//     * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
//     * Groups of contiguous characters of the same type are returned as complete tokens, with the
//     * following exception: if {@code camelCase} is {@code true}, the character of type {@code
//     * Character.UPPERCASE_LETTER}, if any, immediately preceding a token of type {@code
//     * Character.LOWERCASE_LETTER} will belong to the following token rather than to the preceding, if
//     * any, {@code Character.UPPERCASE_LETTER} token.
//     *
//     * @param str       the String to split, may be {@code null}
//     * @param camelCase whether to use so-called "camel-case" for letter types
//     * @return an array of parsed Strings, {@code null} if null String input
//     */
//    private static String[] splitByCharacterType(final String str, final boolean camelCase) {
//        if (str == null) {
//            return null;
//        }
//        if (Whether.empty(str)) {
//            return PoolOfArray.EMPTY_STRING_ARRAY;
//        }
//        final char[] c = str.toCharArray();
//        final List<String> list = new ArrayList<String>();
//        int tokenStart = 0;
//        int currentType = Character.getType(c[tokenStart]);
//        for (int pos = tokenStart + 1; pos < c.length; pos++) {
//            final int type = Character.getType(c[pos]);
//            if (type == currentType) {
//                continue;
//            }
//            if (camelCase
//                    && type == Character.LOWERCASE_LETTER
//                    && currentType == Character.UPPERCASE_LETTER) {
//                final int newTokenStart = pos - 1;
//                if (newTokenStart != tokenStart) {
//                    list.add(new String(c, tokenStart, newTokenStart - tokenStart));
//                    tokenStart = newTokenStart;
//                }
//            } else {
//                list.add(new String(c, tokenStart, pos - tokenStart));
//                tokenStart = pos;
//            }
//            currentType = type;
//        }
//        list.add(new String(c, tokenStart, c.length - tokenStart));
//        return list.toArray(new String[0]);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No separator is added to the joined String. Null objects or empty strings within the array
//     * are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null)            = null
//     * StringHelper.join([])              = ""
//     * StringHelper.join([null])          = ""
//     * StringHelper.join(["a", "b", "c"]) = "abc"
//     * StringHelper.join([null, "", "a"]) = "a"
//     * </pre>
//     *
//     * @param <T>      the specific type of values to join together
//     * @param elements the values to join together, may be null
//     * @return the joined String, {@code null} if null array input
//     */
//    @SafeVarargs
//    public static <T> String join(final T... elements) {
//        return join(elements, null);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join(["a", "b", "c"], ';')  = "a;b;c"
//     * StringHelper.join(["a", "b", "c"], null) = "abc"
//     * StringHelper.join([null, "", "a"], ';')  = ";;a"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final Object[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final long[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final int[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final short[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final byte[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final char[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final float[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final double[] array, final char separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join(["a", "b", "c"], ';')  = "a;b;c"
//     * StringHelper.join(["a", "b", "c"], null) = "abc"
//     * StringHelper.join([null, "", "a"], ';')  = ";;a"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final Object[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            if (array[i] != null) {
//                buf.append(array[i]);
//            }
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final long[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final int[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final byte[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final short[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final char[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final double[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * array are represented by empty strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)               = null
//     * StringHelper.join([], *)                 = ""
//     * StringHelper.join([null], *)             = ""
//     * StringHelper.join([1, 2, 3], ';')  = "1;2;3"
//     * StringHelper.join([1, 2, 3], null) = "123"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use
//     * @param startIndex the first index to start joining from. It is an error to pass in an end index
//     *                   past the end of the array
//     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end
//     *                   index past the end of the array
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(
//            final float[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            buf.append(array[i]);
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. A {@code null} separator is the same as an
//     * empty String (""). Null objects or empty strings within the array are represented by empty
//     * strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *)                = null
//     * StringHelper.join([], *)                  = ""
//     * StringHelper.join([null], *)              = ""
//     * StringHelper.join(["a", "b", "c"], "--")  = "a--b--c"
//     * StringHelper.join(["a", "b", "c"], null)  = "abc"
//     * StringHelper.join(["a", "b", "c"], "")    = "abc"
//     * StringHelper.join([null, "", "a"], ',')   = ",,a"
//     * </pre>
//     *
//     * @param array     the array of values to join together, may be null
//     * @param separator the separator character to use, null treated as ""
//     * @return the joined String, {@code null} if null array input
//     */
//    public static String join(final Object[] array, final String separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }
//
//    /**
//     * Joins the elements of the provided array into a single String containing the provided list of
//     * elements.
//     *
//     * <p>No delimiter is added before or after the list. A {@code null} separator is the same as an
//     * empty String (""). Null objects or empty strings within the array are represented by empty
//     * strings.
//     *
//     * <pre>
//     * StringHelper.join(null, *, *, *)                = null
//     * StringHelper.join([], *, *, *)                  = ""
//     * StringHelper.join([null], *, *, *)              = ""
//     * StringHelper.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
//     * StringHelper.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
//     * StringHelper.join(["a", "b", "c"], "--", 2, 3)  = "c"
//     * StringHelper.join(["a", "b", "c"], "--", 2, 2)  = ""
//     * StringHelper.join(["a", "b", "c"], null, 0, 3)  = "abc"
//     * StringHelper.join(["a", "b", "c"], "", 0, 3)    = "abc"
//     * StringHelper.join([null, "", "a"], ',', 0, 3)   = ",,a"
//     * </pre>
//     *
//     * @param array      the array of values to join together, may be null
//     * @param separator  the separator character to use, null treated as ""
//     * @param startIndex the first index to start joining from.
//     * @param endIndex   the index to stop joining from (exclusive).
//     * @return the joined String, {@code null} if null array input; or the empty string if {@code
//     * endIndex - startIndex <= 0}. The number of joined entries is given by {@code endIndex -
//     * startIndex}
//     * @throws ArrayIndexOutOfBoundsException ife<br>
//     *                                        {@code startIndex < 0} or <br>
//     *                                        {@code startIndex >= array.length()} or <br>
//     *                                        {@code endIndex < 0} or <br>
//     *                                        {@code endIndex > array.length()}
//     */
//    public static String join(
//            final Object[] array, String separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        if (separator == null) {
//            separator = EMPTY;
//        }
//
//        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
//        //           (Assuming that all Strings are roughly equally long)
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            if (array[i] != null) {
//                buf.append(array[i]);
//            }
//        }
//        return buf.toString();
//    }
//
//    // Delete
//    // -----------------------------------------------------------------------
//
//    /**
//     * Joins the elements of the provided {@code Iterator} into a single String containing the
//     * provided elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * iteration are represented by empty strings.
//     *
//     * <p>See the examples here: {@link #join(Object[], char)}.
//     *
//     * @param iterator  the {@code Iterator} of values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null iterator input
//     */
//    public static String join(final Iterator<?> iterator, final char separator) {
//
//        // handle null, zero and one elements before building a buffer
//        if (iterator == null) {
//            return null;
//        }
//        if (!iterator.hasNext()) {
//            return EMPTY;
//        }
//        final Object first = iterator.next();
//        if (!iterator.hasNext()) {
//            return first == null ? "" : first.toString();
//        }
//
//        // two or more elements
//        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
//        if (first != null) {
//            buf.append(first);
//        }
//
//        while (iterator.hasNext()) {
//            buf.append(separator);
//            final Object obj = iterator.next();
//            if (obj != null) {
//                buf.append(obj);
//            }
//        }
//
//        return buf.toString();
//    }
//
//    // Remove
//    // -----------------------------------------------------------------------
//
//    /**
//     * Joins the elements of the provided {@code Iterator} into a single String containing the
//     * provided elements.
//     *
//     * <p>No delimiter is added before or after the list. A {@code null} separator is the same as an
//     * empty String ("").
//     *
//     * <p>See the examples here: {@link #join(Object[], String)}.
//     *
//     * @param iterator  the {@code Iterator} of values to join together, may be null
//     * @param separator the separator character to use, null treated as ""
//     * @return the joined String, {@code null} if null iterator input
//     */
//    public static String join(final Iterator<?> iterator, final String separator) {
//
//        // handle null, zero and one elements before building a buffer
//        if (iterator == null) {
//            return null;
//        }
//        if (!iterator.hasNext()) {
//            return EMPTY;
//        }
//        final Object first = iterator.next();
//        if (!iterator.hasNext()) {
//            return Valid.string(first);
//        }
//
//        // two or more elements
//        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
//        if (first != null) {
//            buf.append(first);
//        }
//
//        while (iterator.hasNext()) {
//            if (separator != null) {
//                buf.append(separator);
//            }
//            final Object obj = iterator.next();
//            if (obj != null) {
//                buf.append(obj);
//            }
//        }
//        return buf.toString();
//    }
//
//    /**
//     * Joins the elements of the provided {@code Iterable} into a single String containing the
//     * provided elements.
//     *
//     * <p>No delimiter is added before or after the list. Null objects or empty strings within the
//     * iteration are represented by empty strings.
//     *
//     * <p>See the examples here: {@link #join(Object[], char)}.
//     *
//     * @param iterable  the {@code Iterable} providing the values to join together, may be null
//     * @param separator the separator character to use
//     * @return the joined String, {@code null} if null iterator input
//     */
//    public static String join(final Iterable<?> iterable, final char separator) {
//        if (iterable == null) {
//            return null;
//        }
//        return join(iterable.iterator(), separator);
//    }
//
//    /**
//     * Joins the elements of the provided {@code Iterable} into a single String containing the
//     * provided elements.
//     *
//     * <p>No delimiter is added before or after the list. A {@code null} separator is the same as an
//     * empty String ("").
//     *
//     * <p>See the examples here: {@link #join(Object[], String)}.
//     *
//     * @param iterable  the {@code Iterable} providing the values to join together, may be null
//     * @param separator the separator character to use, null treated as ""
//     * @return the joined String, {@code null} if null iterator input
//     */
//    public static String join(final Iterable<?> iterable, final String separator) {
//        if (iterable == null) {
//            return null;
//        }
//        return join(iterable.iterator(), separator);
//    }
//
//    /**
//     * Deletes all whitespaces from a String as defined by {@link Character#isWhitespace(char)}.
//     *
//     * <pre>
//     * StringHelper.deleteWhitespace(null)         = null
//     * StringHelper.deleteWhitespace("")           = ""
//     * StringHelper.deleteWhitespace("abc")        = "abc"
//     * StringHelper.deleteWhitespace("   ab  c  ") = "abc"
//     * </pre>
//     *
//     * @param str the String to delete whitespace from, may be null
//     * @return the String without whitespaces, {@code null} if null String input
//     */
//    public static String deleteWhitespace(final String str) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//        final int sz = str.length();
//        final char[] chs = new char[sz];
//        int count = 0;
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isWhitespace(str.charAt(i))) {
//                chs[count++] = str.charAt(i);
//            }
//        }
//        if (count == sz) {
//            return str;
//        }
//        return new String(chs, 0, count);
//    }
//
//    /**
//     * Removes a substring only if it is at the beginning of a source string, otherwise returns the
//     * source string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string. A {@code null} search string will return the source string.
//     *
//     * <pre>
//     * StringHelper.removeStart(null, *)      = null
//     * StringHelper.removeStart("", *)        = ""
//     * StringHelper.removeStart(*, null)      = *
//     * StringHelper.removeStart("www.domain.com", "www.")   = "domain.com"
//     * StringHelper.removeStart("domain.com", "www.")       = "domain.com"
//     * StringHelper.removeStart("www.domain.com", "domain") = "www.domain.com"
//     * StringHelper.removeStart("abc", "")    = "abc"
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the String to search for and remove, may be null
//     * @return the substring with the string removed if found, {@code null} if null String input
//     */
//    public static String removeStart(final String str, final String remove) {
//        if (Whether.empty(str) || Whether.empty(remove)) {
//            return str;
//        }
//        if (str.startsWith(remove)) {
//            return str.substring(remove.length());
//        }
//        return str;
//    }
//
//    /**
//     * Case insensitive removal of a substring if it is at the beginning of a source string, otherwise
//     * returns the source string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string. A {@code null} search string will return the source string.
//     *
//     * <pre>
//     * StringHelper.removeStartIgnoreCase(null, *)      = null
//     * StringHelper.removeStartIgnoreCase("", *)        = ""
//     * StringHelper.removeStartIgnoreCase(*, null)      = *
//     * StringHelper.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
//     * StringHelper.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
//     * StringHelper.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
//     * StringHelper.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
//     * StringHelper.removeStartIgnoreCase("abc", "")    = "abc"
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the String to search for (case insensitive) and remove, may be null
//     * @return the substring with the string removed if found, {@code null} if null String input
//     */
//    public static String removeStartIgnoreCase(final String str, final String remove) {
//        if (Whether.empty(str) || Whether.empty(remove)) {
//            return str;
//        }
//        if (startsWithIgnoreCase(str, remove)) {
//            return str.substring(remove.length());
//        }
//        return str;
//    }
//
//    // Replacing
//    // -----------------------------------------------------------------------
//
//    /**
//     * Removes a substring only if it is at the end of a source string, otherwise returns the source
//     * string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string. A {@code null} search string will return the source string.
//     *
//     * <pre>
//     * StringHelper.removeEnd(null, *)      = null
//     * StringHelper.removeEnd("", *)        = ""
//     * StringHelper.removeEnd(*, null)      = *
//     * StringHelper.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
//     * StringHelper.removeEnd("www.domain.com", ".com")   = "www.domain"
//     * StringHelper.removeEnd("www.domain.com", "domain") = "www.domain.com"
//     * StringHelper.removeEnd("abc", "")    = "abc"
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the String to search for and remove, may be null
//     * @return the substring with the string removed if found, {@code null} if null String input
//     */
//    public static String removeEnd(final String str, final String remove) {
//        if (Whether.empty(str) || Whether.empty(remove)) {
//            return str;
//        }
//        if (str.endsWith(remove)) {
//            return str.substring(0, str.length() - remove.length());
//        }
//        return str;
//    }
//
//    /**
//     * Case insensitive removal of a substring if it is at the end of a source string, otherwise
//     * returns the source string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string. A {@code null} search string will return the source string.
//     *
//     * <pre>
//     * StringHelper.removeEndIgnoreCase(null, *)      = null
//     * StringHelper.removeEndIgnoreCase("", *)        = ""
//     * StringHelper.removeEndIgnoreCase(*, null)      = *
//     * StringHelper.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
//     * StringHelper.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
//     * StringHelper.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
//     * StringHelper.removeEndIgnoreCase("abc", "")    = "abc"
//     * StringHelper.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
//     * StringHelper.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the String to search for (case insensitive) and remove, may be null
//     * @return the substring with the string removed if found, {@code null} if null String input
//     */
//    public static String removeEndIgnoreCase(final String str, final String remove) {
//        if (Whether.empty(str) || Whether.empty(remove)) {
//            return str;
//        }
//        if (endsWithIgnoreCase(str, remove)) {
//            return str.substring(0, str.length() - remove.length());
//        }
//        return str;
//    }
//
//    /**
//     * Removes all occurrences of a substring from within the source string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string. A {@code null} remove string will return the source string. An empty
//     * ("") remove string will return the source string.
//     *
//     * <pre>
//     * StringHelper.remove(null, *)        = null
//     * StringHelper.remove("", *)          = ""
//     * StringHelper.remove(*, null)        = *
//     * StringHelper.remove(*, "")          = *
//     * StringHelper.remove("queued", "ue") = "qd"
//     * StringHelper.remove("queued", "zz") = "queued"
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the String to search for and remove, may be null
//     * @return the substring with the string removed if found, {@code null} if null String input
//     */
//    public static String remove(final String str, final String remove) {
//        if (Whether.empty(str) || Whether.empty(remove)) {
//            return str;
//        }
//        return replace(str, remove, EMPTY, -1);
//    }
//
//    /**
//     * Removes all occurrences of a character from within the source string.
//     *
//     * <p>A {@code null} source string will return {@code null}. An empty ("") source string will
//     * return the empty string.
//     *
//     * <pre>
//     * StringHelper.remove(null, *)       = null
//     * StringHelper.remove("", *)         = ""
//     * StringHelper.remove("queued", 'u') = "qeed"
//     * StringHelper.remove("queued", 'z') = "queued"
//     * </pre>
//     *
//     * @param str    the source String to search, may be null
//     * @param remove the char to search for and remove, may be null
//     * @return the substring with the char removed if found, {@code null} if null String input
//     */
//    public static String remove(final String str, final char remove) {
//        if (Whether.empty(str) || str.indexOf(remove) == INDEX_NOT_FOUND) {
//            return str;
//        }
//        final char[] chars = str.toCharArray();
//        int pos = 0;
//        for (int i = 0; i < chars.length; i++) {
//            if (chars[i] != remove) {
//                chars[pos++] = chars[i];
//            }
//        }
//        return new String(chars, 0, pos);
//    }
//
//    /**
//     * Replaces a String with another String inside a larger String, once.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op.
//     *
//     * <pre>
//     * StringHelper.replaceOnce(null, *, *)        = null
//     * StringHelper.replaceOnce("", *, *)          = ""
//     * StringHelper.replaceOnce("any", null, *)    = "any"
//     * StringHelper.replaceOnce("any", *, null)    = "any"
//     * StringHelper.replaceOnce("any", "", *)      = "any"
//     * StringHelper.replaceOnce("aba", "a", null)  = "aba"
//     * StringHelper.replaceOnce("aba", "a", "")    = "ba"
//     * StringHelper.replaceOnce("aba", "a", "z")   = "zba"
//     * </pre>
//     *
//     * @param text         text to search and replace in, may be null
//     * @param searchString the String to search for, may be null
//     * @param replacement  the String to replace with, may be null
//     * @return the text with any replacements processed, {@code null} if null String input
//     * @see #replace(String text, String searchString, String replacement, int max)
//     */
//    public static String replaceOnce(
//            final String text, final String searchString, final String replacement) {
//        return replace(text, searchString, replacement, 1);
//    }
//
//    /**
//     * Replaces each substring of the source String that matches the given regular expression with the
//     * given replacement using the {@link Pattern#DOTALL} option. DOTALL is also know as single-line
//     * mode in Perl. This call is also equivalent to:
//     *
//     * <ul>
//     *   <li>{@code source.replaceAll(&quot;(?s)&quot; + regex, replacement)}
//     *   <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement)}
//     * </ul>
//     *
//     * @param source      the source string
//     * @param regex       the regular expression to which this string is to be matched
//     * @param replacement the string to be substituted for each match
//     * @return The resulting {@code String}
//     * @see String#replaceAll(String, String)
//     * @see Pattern#DOTALL
//     */
//    public static String replacePattern(
//            final String source, final String regex, final String replacement) {
//        return Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement);
//    }
//
//    /**
//     * Removes each substring of the source String that matches the given regular expression using the
//     * DOTALL option.
//     *
//     * @param source the source string
//     * @param regex  the regular expression to which this string is to be matched
//     * @return The resulting {@code String}
//     * @see String#replaceAll(String, String)
//     * @see Pattern#DOTALL
//     */
//    public static String removePattern(final String source, final String regex) {
//        return replacePattern(source, regex, ToolString.EMPTY);
//    }
//
//    /**
//     * Replaces all occurrences of a String within another String.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op.
//     *
//     * <pre>
//     * StringHelper.replace(null, *, *)        = null
//     * StringHelper.replace("", *, *)          = ""
//     * StringHelper.replace("any", null, *)    = "any"
//     * StringHelper.replace("any", *, null)    = "any"
//     * StringHelper.replace("any", "", *)      = "any"
//     * StringHelper.replace("aba", "a", null)  = "aba"
//     * StringHelper.replace("aba", "a", "")    = "b"
//     * StringHelper.replace("aba", "a", "z")   = "zbz"
//     * </pre>
//     *
//     * @param text         text to search and replace in, may be null
//     * @param searchString the String to search for, may be null
//     * @param replacement  the String to replace it with, may be null
//     * @return the text with any replacements processed, {@code null} if null String input
//     * @see #replace(String text, String searchString, String replacement, int max)
//     */
//    public static String replace(
//            final String text, final String searchString, final String replacement) {
//        return replace(text, searchString, replacement, -1);
//    }
//
//    // Replace, character based
//    // -----------------------------------------------------------------------
//
//    /**
//     * Replaces a String with another String inside a larger String, for the first {@code max} values
//     * of the search String.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op.
//     *
//     * <pre>
//     * StringHelper.replace(null, *, *, *)         = null
//     * StringHelper.replace("", *, *, *)           = ""
//     * StringHelper.replace("any", null, *, *)     = "any"
//     * StringHelper.replace("any", *, null, *)     = "any"
//     * StringHelper.replace("any", "", *, *)       = "any"
//     * StringHelper.replace("any", *, *, 0)        = "any"
//     * StringHelper.replace("abaa", "a", null, -1) = "abaa"
//     * StringHelper.replace("abaa", "a", "", -1)   = "b"
//     * StringHelper.replace("abaa", "a", "z", 0)   = "abaa"
//     * StringHelper.replace("abaa", "a", "z", 1)   = "zbaa"
//     * StringHelper.replace("abaa", "a", "z", 2)   = "zbza"
//     * StringHelper.replace("abaa", "a", "z", -1)  = "zbzz"
//     * </pre>
//     *
//     * @param text         text to search and replace in, may be null
//     * @param searchString the String to search for, may be null
//     * @param replacement  the String to replace it with, may be null
//     * @param max          maximum number of values to replace, or {@code -1} if no maximum
//     * @return the text with any replacements processed, {@code null} if null String input
//     */
//    public static String replace(
//            final String text, final String searchString, final String replacement, int max) {
//        if (Whether.empty(text) || Whether.empty(searchString) || replacement == null || max == 0) {
//            return text;
//        }
//        int start = 0;
//        int end = text.indexOf(searchString, start);
//        if (end == INDEX_NOT_FOUND) {
//            return text;
//        }
//        final int replLength = searchString.length();
//        int increase = replacement.length() - replLength;
//        increase = Math.max(increase, 0);
//        increase *= max < 0 ? 16 : Math.min(max, 64);
//        final StringBuilder buf = new StringBuilder(text.length() + increase);
//        while (end != INDEX_NOT_FOUND) {
//            buf.append(text, start, end).append(replacement);
//            start = end + replLength;
//            if (--max == 0) {
//                break;
//            }
//            end = text.indexOf(searchString, start);
//        }
//        buf.append(text.substring(start));
//        return buf.toString();
//    }
//
//    /**
//     * Replaces all occurrences of Strings within another String.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op, or if any "search string" or
//     * "string to replace" is null, that replace will be ignored. This will not repeat. For repeating
//     * replaces, call the overloaded method.
//     *
//     * <pre>
//     *  StringHelper.replaceEach(null, *, *)        = null
//     *  StringHelper.replaceEach("", *, *)          = ""
//     *  StringHelper.replaceEach("aba", null, null) = "aba"
//     *  StringHelper.replaceEach("aba", new String[0], null) = "aba"
//     *  StringHelper.replaceEach("aba", null, new String[0]) = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, null)  = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
//     *  StringHelper.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
//     *  (example of how it does not repeat)
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
//     * </pre>
//     *
//     * @param text            text to search and replace in, no-op if null
//     * @param searchList      the Strings to search for, no-op if null
//     * @param replacementList the Strings to replace them with, no-op if null
//     * @return the text with any replacements processed, {@code null} if null String input
//     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
//     *                                  and/or size 0)
//     */
//    public static String replaceEach(
//            final String text, final String[] searchList, final String[] replacementList) {
//        return replaceEach(text, searchList, replacementList, false, 0);
//    }
//
//    // Overlay
//    // -----------------------------------------------------------------------
//
//    /**
//     * Replaces all occurrences of Strings within another String.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op, or if any "search string" or
//     * "string to replace" is null, that replace will be ignored.
//     *
//     * <pre>
//     *  StringHelper.replaceEach(null, *, *, *) = null
//     *  StringHelper.replaceEach("", *, *, *) = ""
//     *  StringHelper.replaceEach("aba", null, null, *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[0], null, *) = "aba"
//     *  StringHelper.replaceEach("aba", null, new String[0], *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
//     *  StringHelper.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
//     *  (example of how it repeats)
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalStateException
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
//     * </pre>
//     *
//     * @param text            text to search and replace in, no-op if null
//     * @param searchList      the Strings to search for, no-op if null
//     * @param replacementList the Strings to replace them with, no-op if null
//     * @return the text with any replacements processed, {@code null} if null String input
//     * @throws IllegalStateException    if the search is repeating and there is an endless loop due to
//     *                                  outputs of one being inputs to another
//     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
//     *                                  and/or size 0)
//     */
//    public static String replaceEachRepeatedly(
//            final String text, final String[] searchList, final String[] replacementList) {
//        // timeToLive should be 0 if not used or nothing to replace, else it's
//        // the length of the replace array
//        final int timeToLive = searchList == null ? 0 : searchList.length;
//        return replaceEach(text, searchList, replacementList, true, timeToLive);
//    }
//
//    // Chomping
//    // -----------------------------------------------------------------------
//
//    /**
//     * Replaces all occurrences of Strings within another String.
//     *
//     * <p>A {@code null} reference passed to this method is a no-op, or if any "search string" or
//     * "string to replace" is null, that replace will be ignored.
//     *
//     * <pre>
//     *  StringHelper.replaceEach(null, *, *, *) = null
//     *  StringHelper.replaceEach("", *, *, *) = ""
//     *  StringHelper.replaceEach("aba", null, null, *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[0], null, *) = "aba"
//     *  StringHelper.replaceEach("aba", null, new String[0], *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
//     *  StringHelper.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
//     *  StringHelper.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
//     *  (example of how it repeats)
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
//     *  StringHelper.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalStateException
//     * </pre>
//     *
//     * @param text            text to search and replace in, no-op if null
//     * @param searchList      the Strings to search for, no-op if null
//     * @param replacementList the Strings to replace them with, no-op if null
//     * @param repeat          if true, then replace repeatedly until there are no more possible replacements or
//     *                        timeToLive < 0
//     * @param timeToLive      if less than 0 then there is a circular reference and endless loop
//     * @return the text with any replacements processed, {@code null} if null String input
//     * @throws IllegalStateException    if the search is repeating and there is an endless loop due to
//     *                                  outputs of one being inputs to another
//     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
//     *                                  and/or size 0)
//     */
//    private static String replaceEach(
//            final String text,
//            final String[] searchList,
//            final String[] replacementList,
//            final boolean repeat,
//            final int timeToLive) {
//
//        // mchyzer Performance note: This creates very few new objects (one major goal)
//        // let me know if there are performance requests, we can create a harness to measure
//
//        if (text == null
//                || Whether.empty(text)
//                || searchList == null
//                || searchList.length == 0
//                || replacementList == null
//                || replacementList.length == 0) {
//            return text;
//        }
//
//        // if recursing, this shouldn't be less than 0
//        if (timeToLive < 0) {
//            throw new IllegalStateException(
//                    "Aborting to protect against StackOverflowError - "
//                            + "output of one loop is the input of another");
//        }
//
//        final int searchLength = searchList.length;
//        final int replacementLength = replacementList.length;
//
//        // make sure lengths are ok, these need to be equal
//        if (searchLength != replacementLength) {
//            throw new IllegalArgumentException(
//                    "Search and Replace array lengths don't match: "
//                            + searchLength
//                            + " vs "
//                            + replacementLength);
//        }
//
//        // keep track of which still have matches
//        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
//
//        // index on index that the match was found
//        int textIndex = -1;
//        int replaceIndex = -1;
//        int tempIndex;
//
//        // index of replace array that will replace the search string found
//        // NOTE: logic duplicated below START
//        for (int i = 0; i < searchLength; i++) {
//            if (noMoreMatchesForReplIndex[i]
//                    || searchList[i] == null
//                    || Whether.empty(searchList[i])
//                    || replacementList[i] == null) {
//                continue;
//            }
//            tempIndex = text.indexOf(searchList[i]);
//
//            // see if we need to keep searching for this
//            if (tempIndex == -1) {
//                noMoreMatchesForReplIndex[i] = true;
//            } else {
//                if (textIndex == -1 || tempIndex < textIndex) {
//                    textIndex = tempIndex;
//                    replaceIndex = i;
//                }
//            }
//        }
//        // NOTE: logic mostly below END
//
//        // no search strings found, we are done
//        if (textIndex == -1) {
//            return text;
//        }
//
//        int start = 0;
//
//        // get a good guess on the size of the result buffer so it doesn't have to double if it goes
//        // over a bit
//        int increase = 0;
//
//        // count the replacement text elements that are larger than their corresponding text being
//        // replaced
//        for (int i = 0; i < searchList.length; i++) {
//            if (searchList[i] == null || replacementList[i] == null) {
//                continue;
//            }
//            final int greater = replacementList[i].length() - searchList[i].length();
//            if (greater > 0) {
//                increase += 3 * greater; // assume 3 matches
//            }
//        }
//        // have upper-bound at 20% increase, then let Java take over
//        increase = Math.min(increase, text.length() / 5);
//
//        final StringBuilder buf = new StringBuilder(text.length() + increase);
//
//        while (textIndex != -1) {
//
//            for (int i = start; i < textIndex; i++) {
//                buf.append(text.charAt(i));
//            }
//            buf.append(replacementList[replaceIndex]);
//
//            start = textIndex + searchList[replaceIndex].length();
//
//            textIndex = -1;
//            replaceIndex = -1;
//            tempIndex = -1;
//            // find the next earliest match
//            // NOTE: logic mostly duplicated above START
//            for (int i = 0; i < searchLength; i++) {
//                if (noMoreMatchesForReplIndex[i]
//                        || searchList[i] == null
//                        || Whether.empty(searchList[i])
//                        || replacementList[i] == null) {
//                    continue;
//                }
//                tempIndex = text.indexOf(searchList[i], start);
//
//                // see if we need to keep searching for this
//                if (tempIndex == -1) {
//                    noMoreMatchesForReplIndex[i] = true;
//                } else {
//                    if (textIndex == -1 || tempIndex < textIndex) {
//                        textIndex = tempIndex;
//                        replaceIndex = i;
//                    }
//                }
//            }
//            // NOTE: logic duplicated above END
//
//        }
//        final int textLength = text.length();
//        for (int i = start; i < textLength; i++) {
//            buf.append(text.charAt(i));
//        }
//        final String result = buf.toString();
//        if (!repeat) {
//            return result;
//        }
//
//        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
//    }
//
//    /**
//     * Replaces all occurrences of a character in a String with another. This is a null-safe version
//     * of {@link String#replace(char, char)}.
//     *
//     * <p>A {@code null} string input returns {@code null}. An empty ("") string input returns an
//     * empty string.
//     *
//     * <pre>
//     * StringHelper.replaceChars(null, *, *)        = null
//     * StringHelper.replaceChars("", *, *)          = ""
//     * StringHelper.replaceChars("abcba", 'b', 'y') = "aycya"
//     * StringHelper.replaceChars("abcba", 'z', 'y') = "abcba"
//     * </pre>
//     *
//     * @param str         String to replace characters in, may be null
//     * @param searchChar  the character to search for, may be null
//     * @param replaceChar the character to replace, may be null
//     * @return modified String, {@code null} if null string input
//     */
//    public static String replaceChars(
//            final String str, final char searchChar, final char replaceChar) {
//        if (str == null) {
//            return null;
//        }
//        return str.replace(searchChar, replaceChar);
//    }
//
//    // Chopping
//    // -----------------------------------------------------------------------
//
//    /**
//     * Replaces multiple characters in a String in one go. This method can also be used to delete
//     * characters.
//     *
//     * <p>For example:<br>
//     * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.
//     *
//     * <p>A {@code null} string input returns {@code null}. An empty ("") string input returns an
//     * empty string. A null or empty set of search characters returns the input string.
//     *
//     * <p>The length of the search characters should normally equal the length of the replace
//     * characters. If the search characters is longer, then the extra search characters are deleted.
//     * If the search characters is shorter, then the extra replace characters are ignored.
//     *
//     * <pre>
//     * StringHelper.replaceChars(null, *, *)           = null
//     * StringHelper.replaceChars("", *, *)             = ""
//     * StringHelper.replaceChars("abc", null, *)       = "abc"
//     * StringHelper.replaceChars("abc", "", *)         = "abc"
//     * StringHelper.replaceChars("abc", "b", null)     = "ac"
//     * StringHelper.replaceChars("abc", "b", "")       = "ac"
//     * StringHelper.replaceChars("abcba", "bc", "yz")  = "ayzya"
//     * StringHelper.replaceChars("abcba", "bc", "y")   = "ayya"
//     * StringHelper.replaceChars("abcba", "bc", "yzx") = "ayzya"
//     * </pre>
//     *
//     * @param str          String to replace characters in, may be null
//     * @param searchChars  a set of characters to search for, may be null
//     * @param replaceChars a set of characters to replace, may be null
//     * @return modified String, {@code null} if null string input
//     */
//    public static String replaceChars(
//            final String str, final String searchChars, String replaceChars) {
//        if (Whether.empty(str) || Whether.empty(searchChars)) {
//            return str;
//        }
//        if (replaceChars == null) {
//            replaceChars = EMPTY;
//        }
//        boolean modified = false;
//        final int replaceCharsLength = replaceChars.length();
//        final int strLength = str.length();
//        final StringBuilder buf = new StringBuilder(strLength);
//        for (int i = 0; i < strLength; i++) {
//            final char ch = str.charAt(i);
//            final int index = searchChars.indexOf(ch);
//            if (index >= 0) {
//                modified = true;
//                if (index < replaceCharsLength) {
//                    buf.append(replaceChars.charAt(index));
//                }
//            } else {
//                buf.append(ch);
//            }
//        }
//        if (modified) {
//            return buf.toString();
//        }
//        return str;
//    }
//
//    // Conversion
//    // -----------------------------------------------------------------------
//
//    // Padding
//    // -----------------------------------------------------------------------
//
//    /**
//     * Overlays part of a String with another String.
//     *
//     * <p>A {@code null} string input returns {@code null}. A negative index is treated as zero. An
//     * index greater than the string length is treated as the string length. The start index is always
//     * the smaller of the two indices.
//     *
//     * <pre>
//     * StringHelper.overlay(null, *, *, *)            = null
//     * StringHelper.overlay("", "abc", 0, 0)          = "abc"
//     * StringHelper.overlay("abcdef", null, 2, 4)     = "abef"
//     * StringHelper.overlay("abcdef", "", 2, 4)       = "abef"
//     * StringHelper.overlay("abcdef", "", 4, 2)       = "abef"
//     * StringHelper.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
//     * StringHelper.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
//     * StringHelper.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
//     * StringHelper.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
//     * StringHelper.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
//     * StringHelper.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
//     * </pre>
//     *
//     * @param str     the String to do overlaying in, may be null
//     * @param overlay the String to overlay, may be null
//     * @param start   the position to start overlaying at
//     * @param end     the position to stop overlaying before
//     * @return overlayed String, {@code null} if null String input
//     */
//    public static String overlay(final String str, String overlay, int start, int end) {
//        if (str == null) {
//            return null;
//        }
//        if (overlay == null) {
//            overlay = EMPTY;
//        }
//        final int len = str.length();
//        if (start < 0) {
//            start = 0;
//        }
//        if (start > len) {
//            start = len;
//        }
//        if (end < 0) {
//            end = 0;
//        }
//        if (end > len) {
//            end = len;
//        }
//        if (start > end) {
//            final int temp = start;
//            start = end;
//            end = temp;
//        }
//        return str.substring(0, start) + overlay + str.substring(end);
//    }
//
//    /**
//     * Removes one newline from end of a String if it's there, otherwise leave it alone. A newline is
//     * &quot;{@code \n}&quot;, &quot;{@code \r}&quot;, or &quot;{@code \r\n}&quot;.
//     *
//     * <p>NOTE: This method changed in 2.0. It now more closely matches Perl chomp.
//     *
//     * <pre>
//     * StringHelper.chomp(null)          = null
//     * StringHelper.chomp("")            = ""
//     * StringHelper.chomp("abc \r")      = "abc "
//     * StringHelper.chomp("abc\n")       = "abc"
//     * StringHelper.chomp("abc\r\n")     = "abc"
//     * StringHelper.chomp("abc\r\n\r\n") = "abc\r\n"
//     * StringHelper.chomp("abc\n\r")     = "abc\n"
//     * StringHelper.chomp("abc\n\rabc")  = "abc\n\rabc"
//     * StringHelper.chomp("\r")          = ""
//     * StringHelper.chomp("\n")          = ""
//     * StringHelper.chomp("\r\n")        = ""
//     * </pre>
//     *
//     * @param str the String to chomp a newline from, may be null
//     * @return String without newline, {@code null} if null String input
//     */
//    public static String chomp(final String str) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//
//        if (str.length() == 1) {
//            final char ch = str.charAt(0);
//            if (ch == PoolOfCharacter.CR || ch == PoolOfCharacter.LF) {
//                return EMPTY;
//            }
//            return str;
//        }
//
//        int lastIdx = str.length() - 1;
//        final char last = str.charAt(lastIdx);
//
//        if (last == PoolOfCharacter.LF) {
//            if (str.charAt(lastIdx - 1) == PoolOfCharacter.CR) {
//                lastIdx--;
//            }
//        } else if (last != PoolOfCharacter.CR) {
//            lastIdx++;
//        }
//        return str.substring(0, lastIdx);
//    }
//
//    /**
//     * Remove the last character from a String.
//     *
//     * <p>If the String ends in {@code \r\n}, then remove both of them.
//     *
//     * <pre>
//     * StringHelper.chop(null)          = null
//     * StringHelper.chop("")            = ""
//     * StringHelper.chop("abc \r")      = "abc "
//     * StringHelper.chop("abc\n")       = "abc"
//     * StringHelper.chop("abc\r\n")     = "abc"
//     * StringHelper.chop("abc")         = "ab"
//     * StringHelper.chop("abc\nabc")    = "abc\nab"
//     * StringHelper.chop("a")           = ""
//     * StringHelper.chop("\r")          = ""
//     * StringHelper.chop("\n")          = ""
//     * StringHelper.chop("\r\n")        = ""
//     * </pre>
//     *
//     * @param str the String to chop last character from, may be null
//     * @return String without last character, {@code null} if null String input
//     */
//    public static String chop(final String str) {
//        if (str == null) {
//            return null;
//        }
//        final int strLen = str.length();
//        if (strLen < 2) {
//            return EMPTY;
//        }
//        final int lastIdx = strLen - 1;
//        final String ret = str.substring(0, lastIdx);
//        final char last = str.charAt(lastIdx);
//        if (last == PoolOfCharacter.LF && ret.charAt(lastIdx - 1) == PoolOfCharacter.CR) {
//            return ret.substring(0, lastIdx - 1);
//        }
//        return ret;
//    }
//
//    /**
//     * Right pad a String with spaces (' ').
//     *
//     * <p>The String is padded to the size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.rightPad(null, *)   = null
//     * StringHelper.rightPad("", 3)     = "   "
//     * StringHelper.rightPad("bat", 3)  = "bat"
//     * StringHelper.rightPad("bat", 5)  = "bat  "
//     * StringHelper.rightPad("bat", 1)  = "bat"
//     * StringHelper.rightPad("bat", -1) = "bat"
//     * </pre>
//     *
//     * @param str  the String to pad out, may be null
//     * @param size the size to pad to
//     * @return right padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String rightPad(final String str, final int size) {
//        return rightPad(str, size, ' ');
//    }
//
//    /**
//     * Right pad a String with a specified character.
//     *
//     * <p>The String is padded to the size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.rightPad(null, *, *)     = null
//     * StringHelper.rightPad("", 3, 'z')     = "zzz"
//     * StringHelper.rightPad("bat", 3, 'z')  = "bat"
//     * StringHelper.rightPad("bat", 5, 'z')  = "batzz"
//     * StringHelper.rightPad("bat", 1, 'z')  = "bat"
//     * StringHelper.rightPad("bat", -1, 'z') = "bat"
//     * </pre>
//     *
//     * @param str     the String to pad out, may be null
//     * @param size    the size to pad to
//     * @param padChar the character to pad with
//     * @return right padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String rightPad(final String str, final int size, final char padChar) {
//        if (str == null) {
//            return null;
//        }
//        final int pads = size - str.length();
//        if (pads <= 0) {
//            return str; // returns original String when possible
//        }
//        if (pads > PAD_LIMIT) {
//            return rightPad(str, size, String.valueOf(padChar));
//        }
//        return str.concat(repeat(padChar, pads));
//    }
//
//    /**
//     * Right pad a String with a specified String.
//     *
//     * <p>The String is padded to the size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.rightPad(null, *, *)      = null
//     * StringHelper.rightPad("", 3, "z")      = "zzz"
//     * StringHelper.rightPad("bat", 3, "yz")  = "bat"
//     * StringHelper.rightPad("bat", 5, "yz")  = "batyz"
//     * StringHelper.rightPad("bat", 8, "yz")  = "batyzyzy"
//     * StringHelper.rightPad("bat", 1, "yz")  = "bat"
//     * StringHelper.rightPad("bat", -1, "yz") = "bat"
//     * StringHelper.rightPad("bat", 5, null)  = "bat  "
//     * StringHelper.rightPad("bat", 5, "")    = "bat  "
//     * </pre>
//     *
//     * @param str    the String to pad out, may be null
//     * @param size   the size to pad to
//     * @param padStr the String to pad with, null or empty treated as single space
//     * @return right padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String rightPad(final String str, final int size, String padStr) {
//        if (str == null) {
//            return null;
//        }
//        if (Whether.empty(padStr)) {
//            padStr = SPACE;
//        }
//        final int padLen = padStr.length();
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str; // returns original String when possible
//        }
//        if (padLen == 1 && pads <= PAD_LIMIT) {
//            return rightPad(str, size, padStr.charAt(0));
//        }
//
//        if (pads == padLen) {
//            return str.concat(padStr);
//        } else if (pads < padLen) {
//            return str.concat(padStr.substring(0, pads));
//        } else {
//            final char[] padding = new char[pads];
//            final char[] padChars = padStr.toCharArray();
//            for (int i = 0; i < pads; i++) {
//                padding[i] = padChars[i % padLen];
//            }
//            return str.concat(new String(padding));
//        }
//    }
//
//    // Centering
//    // -----------------------------------------------------------------------
//
//    /**
//     * Left pad a String with spaces (' ').
//     *
//     * <p>The String is padded to the size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.leftPad(null, *)   = null
//     * StringHelper.leftPad("", 3)     = "   "
//     * StringHelper.leftPad("bat", 3)  = "bat"
//     * StringHelper.leftPad("bat", 5)  = "  bat"
//     * StringHelper.leftPad("bat", 1)  = "bat"
//     * StringHelper.leftPad("bat", -1) = "bat"
//     * </pre>
//     *
//     * @param str  the String to pad out, may be null
//     * @param size the size to pad to
//     * @return left padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String leftPad(final String str, final int size) {
//        return leftPad(str, size, ' ');
//    }
//
//    /**
//     * Left pad a String with a specified character.
//     *
//     * <p>Pad to a size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.leftPad(null, *, *)     = null
//     * StringHelper.leftPad("", 3, 'z')     = "zzz"
//     * StringHelper.leftPad("bat", 3, 'z')  = "bat"
//     * StringHelper.leftPad("bat", 5, 'z')  = "zzbat"
//     * StringHelper.leftPad("bat", 1, 'z')  = "bat"
//     * StringHelper.leftPad("bat", -1, 'z') = "bat"
//     * </pre>
//     *
//     * @param str     the String to pad out, may be null
//     * @param size    the size to pad to
//     * @param padChar the character to pad with
//     * @return left padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String leftPad(final String str, final int size, final char padChar) {
//        if (str == null) {
//            return null;
//        }
//        final int pads = size - str.length();
//        if (pads <= 0) {
//            return str; // returns original String when possible
//        }
//        if (pads > PAD_LIMIT) {
//            return leftPad(str, size, String.valueOf(padChar));
//        }
//        return repeat(padChar, pads).concat(str);
//    }
//
//    /**
//     * Left pad a String with a specified String.
//     *
//     * <p>Pad to a size of {@code size}.
//     *
//     * <pre>
//     * StringHelper.leftPad(null, *, *)      = null
//     * StringHelper.leftPad("", 3, "z")      = "zzz"
//     * StringHelper.leftPad("bat", 3, "yz")  = "bat"
//     * StringHelper.leftPad("bat", 5, "yz")  = "yzbat"
//     * StringHelper.leftPad("bat", 8, "yz")  = "yzyzybat"
//     * StringHelper.leftPad("bat", 1, "yz")  = "bat"
//     * StringHelper.leftPad("bat", -1, "yz") = "bat"
//     * StringHelper.leftPad("bat", 5, null)  = "  bat"
//     * StringHelper.leftPad("bat", 5, "")    = "  bat"
//     * </pre>
//     *
//     * @param str    the String to pad out, may be null
//     * @param size   the size to pad to
//     * @param padStr the String to pad with, null or empty treated as single space
//     * @return left padded String or original String if no padding is necessary, {@code null} if null
//     * String input
//     */
//    public static String leftPad(final String str, final int size, String padStr) {
//        if (str == null) {
//            return null;
//        }
//        if (Whether.empty(padStr)) {
//            padStr = SPACE;
//        }
//        final int padLen = padStr.length();
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str; // returns original String when possible
//        }
//        if (padLen == 1 && pads <= PAD_LIMIT) {
//            return leftPad(str, size, padStr.charAt(0));
//        }
//
//        if (pads == padLen) {
//            return padStr.concat(str);
//        } else if (pads < padLen) {
//            return padStr.substring(0, pads).concat(str);
//        } else {
//            final char[] padding = new char[pads];
//            final char[] padChars = padStr.toCharArray();
//            for (int i = 0; i < pads; i++) {
//                padding[i] = padChars[i % padLen];
//            }
//            return new String(padding).concat(str);
//        }
//    }
//
//    // Case conversion
//    // -----------------------------------------------------------------------
//
//    /**
//     * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
//     *
//     * @param cs a CharSequence or {@code null}
//     * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
//     */
//    public static int length(final CharSequence cs) {
//        return cs == null ? 0 : cs.length();
//    }
//
//    /**
//     * Centers a String in a larger String of size {@code size} using the space character (' ').
//     *
//     * <p>
//     *
//     * <p>If the size is less than the String length, the String is returned. A {@code null} String
//     * returns {@code null}. A negative size is treated as zero.
//     *
//     * <p>Equivalent to {@code center(str, size, " ")}.
//     *
//     * <pre>
//     * StringHelper.center(null, *)   = null
//     * StringHelper.center("", 4)     = "    "
//     * StringHelper.center("ab", -1)  = "ab"
//     * StringHelper.center("ab", 4)   = " ab "
//     * StringHelper.center("abcd", 2) = "abcd"
//     * StringHelper.center("a", 4)    = " a  "
//     * </pre>
//     *
//     * @param str  the String to center, may be null
//     * @param size the int size of new String, negative treated as zero
//     * @return centered String, {@code null} if null String input
//     */
//    public static String center(final String str, final int size) {
//        return center(str, size, ' ');
//    }
//
//    /**
//     * Centers a String in a larger String of size {@code size}. Uses a supplied character as the
//     * value to pad the String with.
//     *
//     * <p>If the size is less than the String length, the String is returned. A {@code null} String
//     * returns {@code null}. A negative size is treated as zero.
//     *
//     * <pre>
//     * StringHelper.center(null, *, *)     = null
//     * StringHelper.center("", 4, ' ')     = "    "
//     * StringHelper.center("ab", -1, ' ')  = "ab"
//     * StringHelper.center("ab", 4, ' ')   = " ab "
//     * StringHelper.center("abcd", 2, ' ') = "abcd"
//     * StringHelper.center("a", 4, ' ')    = " a  "
//     * StringHelper.center("a", 4, 'y')    = "yayy"
//     * </pre>
//     *
//     * @param str     the String to center, may be null
//     * @param size    the int size of new String, negative treated as zero
//     * @param padChar the character to pad the new String with
//     * @return centered String, {@code null} if null String input
//     */
//    public static String center(String str, final int size, final char padChar) {
//        if (str == null || size <= 0) {
//            return str;
//        }
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str;
//        }
//        str = leftPad(str, strLen + pads / 2, padChar);
//        str = rightPad(str, size, padChar);
//        return str;
//    }
//
//    /**
//     * Centers a String in a larger String of size {@code size}. Uses a supplied String as the value
//     * to pad the String with.
//     *
//     * <p>If the size is less than the String length, the String is returned. A {@code null} String
//     * returns {@code null}. A negative size is treated as zero.
//     *
//     * <pre>
//     * StringHelper.center(null, *, *)     = null
//     * StringHelper.center("", 4, " ")     = "    "
//     * StringHelper.center("ab", -1, " ")  = "ab"
//     * StringHelper.center("ab", 4, " ")   = " ab "
//     * StringHelper.center("abcd", 2, " ") = "abcd"
//     * StringHelper.center("a", 4, " ")    = " a  "
//     * StringHelper.center("a", 4, "yz")   = "yayz"
//     * StringHelper.center("abc", 7, null) = "  abc  "
//     * StringHelper.center("abc", 7, "")   = "  abc  "
//     * </pre>
//     *
//     * @param str    the String to center, may be null
//     * @param size   the int size of new String, negative treated as zero
//     * @param padStr the String to pad the new String with, must not be null or empty
//     * @return centered String, {@code null} if null String input
//     * @throws IllegalArgumentException if padStr is {@code null} or empty
//     */
//    public static String center(String str, final int size, String padStr) {
//        if (str == null || size <= 0) {
//            return str;
//        }
//        if (Whether.empty(padStr)) {
//            padStr = SPACE;
//        }
//        final int strLen = str.length();
//        final int pads = size - strLen;
//        if (pads <= 0) {
//            return str;
//        }
//        str = leftPad(str, strLen + pads / 2, padStr);
//        str = rightPad(str, size, padStr);
//        return str;
//    }
//
//    /**
//     * Converts a String to upper case as per {@link String#toUpperCase()}.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.upperCase(null)  = null
//     * StringHelper.upperCase("")    = ""
//     * StringHelper.upperCase("aBc") = "ABC"
//     * </pre>
//     *
//     * <p><strong>Note:</strong> As described in the documentation for {@link String#toUpperCase()},
//     * the result of this method is affected by the current locale. For platform-independent case
//     * transformations, the method {@link #lowerCase(String, Locale)} should be used with a specific
//     * locale (e.g. {@link Locale#ENGLISH}).
//     *
//     * @param str the String to upper case, may be null
//     * @return the upper cased String, {@code null} if null String input
//     */
//    public static String upperCase(final String str) {
//        if (str == null) {
//            return null;
//        }
//        return str.toUpperCase();
//    }
//
//    /**
//     * Converts a String to upper case as per {@link String#toUpperCase(Locale)}.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.upperCase(null, Locale.ENGLISH)  = null
//     * StringHelper.upperCase("", Locale.ENGLISH)    = ""
//     * StringHelper.upperCase("aBc", Locale.ENGLISH) = "ABC"
//     * </pre>
//     *
//     * @param str    the String to upper case, may be null
//     * @param locale the locale that defines the case transformation rules, must not be null
//     * @return the upper cased String, {@code null} if null String input
//     */
//    public static String upperCase(final String str, final Locale locale) {
//        if (str == null) {
//            return null;
//        }
//        return str.toUpperCase(locale);
//    }
//
//    /**
//     * Converts a String to lower case as per {@link String#toLowerCase()}.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.lowerCase(null)  = null
//     * StringHelper.lowerCase("")    = ""
//     * StringHelper.lowerCase("aBc") = "abc"
//     * </pre>
//     *
//     * <p><strong>Note:</strong> As described in the documentation for {@link String#toLowerCase()},
//     * the result of this method is affected by the current locale. For platform-independent case
//     * transformations, the method {@link #lowerCase(String, Locale)} should be used with a specific
//     * locale (e.g. {@link Locale#ENGLISH}).
//     *
//     * @param str the String to lower case, may be null
//     * @return the lower cased String, {@code null} if null String input
//     */
//    public static String lowerCase(final String str) {
//        if (str == null) {
//            return null;
//        }
//        return str.toLowerCase();
//    }
//
//    // Count matches
//    // -----------------------------------------------------------------------
//
//    /**
//     * Converts a String to lower case as per {@link String#toLowerCase(Locale)}.
//     *
//     * <p>A {@code null} input String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.lowerCase(null, Locale.ENGLISH)  = null
//     * StringHelper.lowerCase("", Locale.ENGLISH)    = ""
//     * StringHelper.lowerCase("aBc", Locale.ENGLISH) = "abc"
//     * </pre>
//     *
//     * @param str    the String to lower case, may be null
//     * @param locale the locale that defines the case transformation rules, must not be null
//     * @return the lower cased String, {@code null} if null String input
//     */
//    public static String lowerCase(final String str, final Locale locale) {
//        if (str == null) {
//            return null;
//        }
//        return str.toLowerCase(locale);
//    }
//
//    // Character Tests
//    // -----------------------------------------------------------------------
//
//    /**
//     * Capitalizes a String changing the first letter to title case as per {@link
//     * Character#toTitleCase(char)}. No other letters are changed.
//     *
//     * <p>For a word based algorithm, see {@link #capitalize(String)}. A {@code null} input String
//     * returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.capitalize(null)  = null
//     * StringHelper.capitalize("")    = ""
//     * StringHelper.capitalize("cat") = "Cat"
//     * StringHelper.capitalize("cAt") = "CAt"
//     * </pre>
//     *
//     * @param str the String to capitalize, may be null
//     * @return the capitalized String, {@code null} if null String input
//     * @see #capitalize(String)
//     * @see #uncapitalize(String)
//     */
//    public static String capitalize(final String str) {
//        int strLen;
//        if (str == null || (strLen = str.length()) == 0) {
//            return str;
//        }
//
//        char firstChar = str.charAt(0);
//        if (Character.isTitleCase(firstChar)) {
//            // already capitalized
//            return str;
//        }
//
//        return Character.toTitleCase(firstChar) + str.substring(1);
//    }
//
//    /**
//     * Uncapitalizes a String changing the first letter to title case as per {@link
//     * Character#toLowerCase(char)}. No other letters are changed.
//     *
//     * <p>For a word based algorithm, see {@link #uncapitalize(String)}. A {@code null} input String
//     * returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.uncapitalize(null)  = null
//     * StringHelper.uncapitalize("")    = ""
//     * StringHelper.uncapitalize("Cat") = "cat"
//     * StringHelper.uncapitalize("CAT") = "cAT"
//     * </pre>
//     *
//     * @param str the String to uncapitalize, may be null
//     * @return the uncapitalized String, {@code null} if null String input
//     * @see #uncapitalize(String)
//     * @see #capitalize(String)
//     */
//    public static String uncapitalize(final String str) {
//        if (str == null || str.length() == 0) {
//            return str;
//        }
//
//        char firstChar = str.charAt(0);
//        if (Character.isLowerCase(firstChar)) {
//            // already uncapitalized
//            return str;
//        }
//
//        return Character.toLowerCase(firstChar) + str.substring(1);
//    }
//
//    /**
//     * Swaps the case of a String changing upper and title case to lower case, and lower case to upper
//     * case.
//     *
//     * <ul>
//     *   <li>Upper case character converts to Lower case
//     *   <li>Title case character converts to Lower case
//     *   <li>Lower case character converts to Upper case
//     * </ul>
//     *
//     * <p>For a word based algorithm, see {@link #swapCase(String)}. A {@code null} input String
//     * returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.swapCase(null)                 = null
//     * StringHelper.swapCase("")                   = ""
//     * StringHelper.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
//     * </pre>
//     *
//     * <p>NOTE: This method changed in Lang version 2.0. It no longer performs a word based algorithm.
//     * If you only use ASCII, you will notice no change. That functionality is available in .
//     *
//     * @param str the String to swap case, may be null
//     * @return the changed String, {@code null} if null String input
//     */
//    public static String swapCase(final String str) {
//        if (Whether.empty(str)) {
//            return str;
//        }
//
//        final char[] buffer = str.toCharArray();
//
//        for (int i = 0; i < buffer.length; i++) {
//            final char ch = buffer[i];
//            if (Character.isUpperCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isTitleCase(ch)) {
//                buffer[i] = Character.toLowerCase(ch);
//            } else if (Character.isLowerCase(ch)) {
//                buffer[i] = Character.toUpperCase(ch);
//            }
//        }
//        return new String(buffer);
//    }
//
//    /**
//     * Counts how many times the substring appears in the larger string.
//     *
//     * <p>A {@code null} or empty ("") String input returns {@code 0}.
//     *
//     * <pre>
//     * StringHelper.countMatches(null, *)       = 0
//     * StringHelper.countMatches("", *)         = 0
//     * StringHelper.countMatches("abba", null)  = 0
//     * StringHelper.countMatches("abba", "")    = 0
//     * StringHelper.countMatches("abba", "a")   = 2
//     * StringHelper.countMatches("abba", "ab")  = 1
//     * StringHelper.countMatches("abba", "xxx") = 0
//     * </pre>
//     *
//     * @param str the CharSequence to check, may be null
//     * @param sub the substring to count, may be null
//     * @return the number of occurrences, 0 if either CharSequence is {@code null}
//     * CharSequence)
//     */
//    public static int countMatches(final CharSequence str, final CharSequence sub) {
//        if (Whether.empty(str) || Whether.empty(sub)) {
//            return 0;
//        }
//        int count = 0;
//        int idx = 0;
//        while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != INDEX_NOT_FOUND) {
//            count++;
//            idx += sub.length();
//        }
//        return count;
//    }
//
//    /**
//     * Checks if the CharSequence contains only Unicode letters.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code false}.
//     *
//     * <pre>
//     * StringHelper.isAlpha(null)   = false
//     * StringHelper.isAlpha("")     = false
//     * StringHelper.isAlpha("  ")   = false
//     * StringHelper.isAlpha("abc")  = true
//     * StringHelper.isAlpha("ab2c") = false
//     * StringHelper.isAlpha("ab-c") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains letters, and is non-null
//     */
//    public static boolean isAlpha(final CharSequence cs) {
//        if (Whether.empty(cs)) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isLetter(cs.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only Unicode letters and space (' ').
//     *
//     * <p>{@code null} will return {@code false} An empty CharSequence (length()=0) will return {@code
//     * true}.
//     *
//     * <pre>
//     * StringHelper.isAlphaSpace(null)   = false
//     * StringHelper.isAlphaSpace("")     = true
//     * StringHelper.isAlphaSpace("  ")   = true
//     * StringHelper.isAlphaSpace("abc")  = true
//     * StringHelper.isAlphaSpace("ab c") = true
//     * StringHelper.isAlphaSpace("ab2c") = false
//     * StringHelper.isAlphaSpace("ab-c") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains letters and space, and is non-null
//     */
//    public static boolean isAlphaSpace(final CharSequence cs) {
//        if (cs == null) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (Character.isLetter(cs.charAt(i)) == false && cs.charAt(i) != ' ') {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only Unicode letters or digits.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code false}.
//     *
//     * <pre>
//     * StringHelper.isAlphanumeric(null)   = false
//     * StringHelper.isAlphanumeric("")     = false
//     * StringHelper.isAlphanumeric("  ")   = false
//     * StringHelper.isAlphanumeric("abc")  = true
//     * StringHelper.isAlphanumeric("ab c") = false
//     * StringHelper.isAlphanumeric("ab2c") = true
//     * StringHelper.isAlphanumeric("ab-c") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains letters or digits, and is non-null
//     */
//    public static boolean isAlphanumeric(final CharSequence cs) {
//        if (Whether.empty(cs)) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (Character.isLetterOrDigit(cs.charAt(i)) == false) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only Unicode letters, digits or space ({@code ' '}).
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code true}.
//     *
//     * <pre>
//     * StringHelper.isAlphanumericSpace(null)   = false
//     * StringHelper.isAlphanumericSpace("")     = true
//     * StringHelper.isAlphanumericSpace("  ")   = true
//     * StringHelper.isAlphanumericSpace("abc")  = true
//     * StringHelper.isAlphanumericSpace("ab c") = true
//     * StringHelper.isAlphanumericSpace("ab2c") = true
//     * StringHelper.isAlphanumericSpace("ab-c") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains letters, digits or space, and is non-null
//     * isAlphanumericSpace(CharSequence)
//     */
//    public static boolean isAlphanumericSpace(final CharSequence cs) {
//        if (cs == null) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only ASCII printable characters.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code true}.
//     *
//     * <pre>
//     * StringHelper.isAsciiPrintable(null)     = false
//     * StringHelper.isAsciiPrintable("")       = true
//     * StringHelper.isAsciiPrintable(" ")      = true
//     * StringHelper.isAsciiPrintable("Ceki")   = true
//     * StringHelper.isAsciiPrintable("ab2c")   = true
//     * StringHelper.isAsciiPrintable("!ab-c~") = true
//     * StringHelper.isAsciiPrintable("\u0020") = true
//     * StringHelper.isAsciiPrintable("\u0021") = true
//     * StringHelper.isAsciiPrintable("\u007e") = true
//     * StringHelper.isAsciiPrintable("\u007f") = false
//     * StringHelper.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if every character is in the range 32 thru 126
//     */
//    public static boolean isAsciiPrintable(final CharSequence cs) {
//        if (cs == null) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!isAsciiPrintable(cs.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only Unicode digits. A decimal point is not a Unicode digit
//     * and returns false.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code false}.
//     *
//     * <p>Note that the method does not allow for a leading sign, either positive or negative. Also,
//     * if a String passes the numeric test, it may still generate a NumberFormatException when parsed
//     * by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range for int or long
//     * respectively.
//     *
//     * <pre>
//     * StringHelper.isNumeric(null)   = false
//     * StringHelper.isNumeric("")     = false
//     * StringHelper.isNumeric("  ")   = false
//     * StringHelper.isNumeric("123")  = true
//     * StringHelper.isNumeric("12 3") = false
//     * StringHelper.isNumeric("ab2c") = false
//     * StringHelper.isNumeric("12-3") = false
//     * StringHelper.isNumeric("12.3") = false
//     * StringHelper.isNumeric("-123") = false
//     * StringHelper.isNumeric("+123") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains digits, and is non-null
//     */
//    public static boolean isNumeric(final CharSequence cs) {
//        if (Whether.empty(cs)) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (Character.isDigit(cs.charAt(i)) == false) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    // Defaults
//    // -----------------------------------------------------------------------
//
//    /**
//     * Checks if the CharSequence contains only Unicode digits or space ({@code ' '}). A decimal point
//     * is not a Unicode digit and returns false.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code true}.
//     *
//     * <pre>
//     * StringHelper.isNumericSpace(null)   = false
//     * StringHelper.isNumericSpace("")     = true
//     * StringHelper.isNumericSpace("  ")   = true
//     * StringHelper.isNumericSpace("123")  = true
//     * StringHelper.isNumericSpace("12 3") = true
//     * StringHelper.isNumericSpace("ab2c") = false
//     * StringHelper.isNumericSpace("12-3") = false
//     * StringHelper.isNumericSpace("12.3") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains digits or space, and is non-null
//     */
//    public static boolean isNumericSpace(final CharSequence cs) {
//        if (cs == null) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only whitespace.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code true}.
//     *
//     * <pre>
//     * StringHelper.isWhitespace(null)   = false
//     * StringHelper.isWhitespace("")     = true
//     * StringHelper.isWhitespace("  ")   = true
//     * StringHelper.isWhitespace("abc")  = false
//     * StringHelper.isWhitespace("ab2c") = false
//     * StringHelper.isWhitespace("ab-c") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains whitespace, and is non-null
//     */
//    public static boolean isWhitespace(final CharSequence cs) {
//        if (cs == null) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isWhitespace(cs.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only lowercase characters.
//     *
//     * <p>{@code null} will return {@code false}. An empty CharSequence (length()=0) will return
//     * {@code false}.
//     *
//     * <pre>
//     * StringHelper.isAllLowerCase(null)   = false
//     * StringHelper.isAllLowerCase("")     = false
//     * StringHelper.isAllLowerCase("  ")   = false
//     * StringHelper.isAllLowerCase("abc")  = true
//     * StringHelper.isAllLowerCase("abC") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains lowercase characters, and is non-null
//     */
//    public static boolean isAllLowerCase(final CharSequence cs) {
//        if (cs == null || Whether.empty(cs)) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (Character.isLowerCase(cs.charAt(i)) == false) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the CharSequence contains only uppercase characters.
//     *
//     * <p>{@code null} will return {@code false}. An empty String (length()=0) will return {@code
//     * false}.
//     *
//     * <pre>
//     * StringHelper.isAllUpperCase(null)   = false
//     * StringHelper.isAllUpperCase("")     = false
//     * StringHelper.isAllUpperCase("  ")   = false
//     * StringHelper.isAllUpperCase("ABC")  = true
//     * StringHelper.isAllUpperCase("aBC") = false
//     * </pre>
//     *
//     * @param cs the CharSequence to check, may be null
//     * @return {@code true} if only contains uppercase characters, and is non-null
//     */
//    public static boolean isAllUpperCase(final CharSequence cs) {
//        if (cs == null || Whether.empty(cs)) {
//            return false;
//        }
//        final int sz = cs.length();
//        for (int i = 0; i < sz; i++) {
//            if (!Character.isUpperCase(cs.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Reverses a String as per {@link StringBuilder#reverse()}.
//     *
//     * <p>A {@code null} String returns {@code null}.
//     *
//     * <pre>
//     * StringHelper.reverse(null)  = null
//     * StringHelper.reverse("")    = ""
//     * StringHelper.reverse("bat") = "tab"
//     * </pre>
//     *
//     * @param str the String to reverse, may be null
//     * @return the reversed String, {@code null} if null String input
//     */
//    public static String reverse(final String str) {
//        if (str == null) {
//            return null;
//        }
//        return new StringBuilder(str).reverse().toString();
//    }
//
//    // Difference
//    // -----------------------------------------------------------------------
//
//    /**
//     * Reverses a String that is delimited by a specific character.
//     *
//     * <p>The Strings between the delimiters are not reversed. Thus java.lang.String becomes
//     * String.lang.java (if the delimiter is {@code '.'}).
//     *
//     * <pre>
//     * StringHelper.reverseDelimited(null, *)      = null
//     * StringHelper.reverseDelimited("", *)        = ""
//     * StringHelper.reverseDelimited("a.b.c", 'x') = "a.b.c"
//     * StringHelper.reverseDelimited("a.b.c", ".") = "c.b.a"
//     * </pre>
//     *
//     * @param str           the String to reverse, may be null
//     * @param separatorChar the separator character to use
//     * @return the reversed String, {@code null} if null String input
//     */
//    public static String reverseDelimited(final String str, final char separatorChar) {
//        if (str == null) {
//            return null;
//        }
//        // could implement manually, but simple way is to reuse other,
//        // probably slower, methods.
//        final String[] strs = split(str, separatorChar);
//        ToolArray.reverse(strs);
//        return join(strs, separatorChar);
//    }
//
//    /**
//     * Abbreviates a String using ellipses. This will turn "Now is the time for all good men" into
//     * "Now is the time for..."
//     *
//     * <p>Specifically:
//     *
//     * <ul>
//     *   <li>If {@code str} is less than {@code maxWidth} characters long, return it.
//     *   <li>Else abbreviate it to {@code (substring(str, 0, max-3) + "...")}.
//     *   <li>If {@code maxWidth} is less than {@code 4}, throw an {@code IllegalArgumentException}.
//     *   <li>In no case will it return a String of length greater than {@code maxWidth}.
//     * </ul>
//     *
//     * <pre>
//     * StringHelper.abbreviate(null, *)      = null
//     * StringHelper.abbreviate("", 4)        = ""
//     * StringHelper.abbreviate("abcdefg", 6) = "abc..."
//     * StringHelper.abbreviate("abcdefg", 7) = "abcdefg"
//     * StringHelper.abbreviate("abcdefg", 8) = "abcdefg"
//     * StringHelper.abbreviate("abcdefg", 4) = "a..."
//     * StringHelper.abbreviate("abcdefg", 3) = IllegalArgumentException
//     * </pre>
//     *
//     * @param str      the String to check, may be null
//     * @param maxWidth maximum length of result String, must be at least 4
//     * @return abbreviated String, {@code null} if null String input
//     * @throws IllegalArgumentException if the width is too small
//     */
//    public static String abbreviate(final String str, final int maxWidth) {
//        return abbreviate(str, 0, maxWidth);
//    }
//
//    /**
//     * Abbreviates a String using ellipses. This will turn "Now is the time for all good men" into
//     * "...is the time for..."
//     *
//     * <p>Works like {@code abbreviate(String, int)}, but allows you to specify a "left edge" offset.
//     * Note that this left edge is not necessarily going to be the leftmost character in the result,
//     * or the first character following the ellipses, but it will appear somewhere in the result.
//     *
//     * <p>In no case will it return a String of length greater than {@code maxWidth}.
//     *
//     * <pre>
//     * StringHelper.abbreviate(null, *, *)                = null
//     * StringHelper.abbreviate("", 0, 4)                  = ""
//     * StringHelper.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
//     * StringHelper.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
//     * StringHelper.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
//     * StringHelper.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
//     * StringHelper.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
//     * StringHelper.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
//     * StringHelper.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
//     * StringHelper.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
//     * StringHelper.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
//     * StringHelper.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
//     * StringHelper.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
//     * </pre>
//     *
//     * @param str      the String to check, may be null
//     * @param offset   left edge of source String
//     * @param maxWidth maximum length of result String, must be at least 4
//     * @return abbreviated String, {@code null} if null String input
//     * @throws IllegalArgumentException if the width is too small
//     */
//    public static String abbreviate(final String str, int offset, final int maxWidth) {
//        if (str == null) {
//            return null;
//        }
//        if (maxWidth < 4) {
//            throw new IllegalArgumentException("Minimum abbreviation width is 4");
//        }
//        if (str.length() <= maxWidth) {
//            return str;
//        }
//        if (offset > str.length()) {
//            offset = str.length();
//        }
//        if (str.length() - offset < maxWidth - 3) {
//            offset = str.length() - (maxWidth - 3);
//        }
//        final String abrevMarker = "...";
//        if (offset <= 4) {
//            return str.substring(0, maxWidth - 3) + abrevMarker;
//        }
//        if (maxWidth < 7) {
//            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
//        }
//        if (offset + maxWidth - 3 < str.length()) {
//            return abrevMarker + abbreviate(str.substring(offset), maxWidth - 3);
//        }
//        return abrevMarker + str.substring(str.length() - (maxWidth - 3));
//    }
//
//    /**
//     * Abbreviates a String to the length passed, replacing the middle characters with the supplied
//     * replacement String.
//     *
//     * <p>This abbreviation only occurs if the following criteria is met:
//     *
//     * <ul>
//     *   <li>Neither the String for abbreviation nor the replacement String are null or empty
//     *   <li>The length to truncate to is less than the length of the supplied String
//     *   <li>The length to truncate to is greater than 0
//     *   <li>The abbreviated String will have enough room for the length supplied replacement String
//     *       and the first and last characters of the supplied String for abbreviation
//     * </ul>
//     * <p>
//     * Otherwise, the returned String will be the same as the supplied String for abbreviation.
//     *
//     * <pre>
//     * StringHelper.abbreviateMiddle(null, null, 0)      = null
//     * StringHelper.abbreviateMiddle("abc", null, 0)      = "abc"
//     * StringHelper.abbreviateMiddle("abc", ".", 0)      = "abc"
//     * StringHelper.abbreviateMiddle("abc", ".", 3)      = "abc"
//     * StringHelper.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
//     * </pre>
//     *
//     * @param str    the String to abbreviate, may be null
//     * @param middle the String to replace the middle characters with, may be null
//     * @param length the length to abbreviate {@code str} to.
//     * @return the abbreviated String if the above criteria is met, or the original String supplied
//     * for abbreviation.
//     */
//    public static String abbreviateMiddle(final String str, final String middle, final int length) {
//        if (Whether.empty(str) || Whether.empty(middle)) {
//            return str;
//        }
//
//        if (length >= str.length() || length < middle.length() + 2) {
//            return str;
//        }
//
//        final int targetSting = length - middle.length();
//        final int startOffset = targetSting / 2 + targetSting % 2;
//        final int endOffset = str.length() - targetSting / 2;
//
//        return str.substring(0, startOffset) + middle + str.substring(endOffset);
//    }
//
//    // Misc
//    // -----------------------------------------------------------------------
//
//    /**
//     * Compares two Strings, and returns the portion where they differ. More precisely, return the
//     * remainder of the second String, starting from where it's different from the first. This means
//     * that the difference between "abc" and "ab" is the empty String and not "c".
//     *
//     * <p>For example, {@code difference("i am a machine", "i am a robot") -> "robot"}.
//     *
//     * <pre>
//     * StringHelper.difference(null, null) = null
//     * StringHelper.difference("", "") = ""
//     * StringHelper.difference("", "abc") = "abc"
//     * StringHelper.difference("abc", "") = ""
//     * StringHelper.difference("abc", "abc") = ""
//     * StringHelper.difference("abc", "ab") = ""
//     * StringHelper.difference("ab", "abxyz") = "xyz"
//     * StringHelper.difference("abcde", "abxyz") = "xyz"
//     * StringHelper.difference("abcde", "xyz") = "xyz"
//     * </pre>
//     *
//     * @param str1 the first String, may be null
//     * @param str2 the second String, may be null
//     * @return the portion of str2 where it differs from str1; returns the empty String if they are
//     * equal
//     * @see #indexOfDifference(CharSequence, CharSequence)
//     */
//    public static String difference(final String str1, final String str2) {
//        if (str1 == null) {
//            return str2;
//        }
//        if (str2 == null) {
//            return str1;
//        }
//        final int at = indexOfDifference(str1, str2);
//        if (at == INDEX_NOT_FOUND) {
//            return EMPTY;
//        }
//        return str2.substring(at);
//    }
//
//    /**
//     * Compares two CharSequences, and returns the index at which the CharSequences begin to differ.
//     *
//     * <p>For example, {@code indexOfDifference("i am a machine", "i am a robot") -> 7}
//     *
//     * <pre>
//     * StringHelper.indexOfDifference(null, null) = -1
//     * StringHelper.indexOfDifference("", "") = -1
//     * StringHelper.indexOfDifference("", "abc") = 0
//     * StringHelper.indexOfDifference("abc", "") = 0
//     * StringHelper.indexOfDifference("abc", "abc") = -1
//     * StringHelper.indexOfDifference("ab", "abxyz") = 2
//     * StringHelper.indexOfDifference("abcde", "abxyz") = 2
//     * StringHelper.indexOfDifference("abcde", "xyz") = 0
//     * </pre>
//     *
//     * @param cs1 the first CharSequence, may be null
//     * @param cs2 the second CharSequence, may be null
//     * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
//     * indexOfDifference(CharSequence, CharSequence)
//     */
//    public static int indexOfDifference(final CharSequence cs1, final CharSequence cs2) {
//        if (cs1 == cs2) {
//            return INDEX_NOT_FOUND;
//        }
//        if (cs1 == null || cs2 == null) {
//            return 0;
//        }
//        int i;
//        for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
//            if (cs1.charAt(i) != cs2.charAt(i)) {
//                break;
//            }
//        }
//        if (i < cs2.length() || i < cs1.length()) {
//            return i;
//        }
//        return INDEX_NOT_FOUND;
//    }
//
//    // startsWith
//    // -----------------------------------------------------------------------
//
//    /**
//     * Compares all CharSequences in an array and returns the index at which the CharSequences begin
//     * to differ.
//     *
//     * <p>For example, <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7
//     * </code>
//     *
//     * <pre>
//     * StringHelper.indexOfDifference(null) = -1
//     * StringHelper.indexOfDifference(new String[] {}) = -1
//     * StringHelper.indexOfDifference(new String[] {"abc"}) = -1
//     * StringHelper.indexOfDifference(new String[] {null, null}) = -1
//     * StringHelper.indexOfDifference(new String[] {"", ""}) = -1
//     * StringHelper.indexOfDifference(new String[] {"", null}) = 0
//     * StringHelper.indexOfDifference(new String[] {"abc", null, null}) = 0
//     * StringHelper.indexOfDifference(new String[] {null, null, "abc"}) = 0
//     * StringHelper.indexOfDifference(new String[] {"", "abc"}) = 0
//     * StringHelper.indexOfDifference(new String[] {"abc", ""}) = 0
//     * StringHelper.indexOfDifference(new String[] {"abc", "abc"}) = -1
//     * StringHelper.indexOfDifference(new String[] {"abc", "a"}) = 1
//     * StringHelper.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
//     * StringHelper.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
//     * StringHelper.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
//     * StringHelper.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
//     * StringHelper.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
//     * </pre>
//     *
//     * @param css array of CharSequences, entries may be null
//     * @return the index where the strings begin to differ; -1 if they are all equal
//     * indexOfDifference(CharSequence...)
//     */
//    public static int indexOfDifference(final CharSequence... css) {
//        if (css == null || css.length <= 1) {
//            return INDEX_NOT_FOUND;
//        }
//        boolean anyStringNull = false;
//        boolean allStringsNull = true;
//        final int arrayLen = css.length;
//        int shortestStrLen = Integer.MAX_VALUE;
//        int longestStrLen = 0;
//
//        // find the min and max string lengths; this avoids checking to make
//        // sure we are not exceeding the length of the string each time through
//        // the bottom loop.
//        for (CharSequence charSequence : css) {
//            if (charSequence == null) {
//                anyStringNull = true;
//                shortestStrLen = 0;
//            } else {
//                allStringsNull = false;
//                shortestStrLen = Math.min(charSequence.length(), shortestStrLen);
//                longestStrLen = Math.max(charSequence.length(), longestStrLen);
//            }
//        }
//
//        // handle lists containing all nulls or all empty strings
//        if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
//            return INDEX_NOT_FOUND;
//        }
//
//        // handle lists containing some nulls or some empty strings
//        if (shortestStrLen == 0) {
//            return 0;
//        }
//
//        // find the position with the first difference across all strings
//        int firstDiff = -1;
//        for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
//            final char comparisonChar = css[0].charAt(stringPos);
//            for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
//                if (css[arrayPos].charAt(stringPos) != comparisonChar) {
//                    firstDiff = stringPos;
//                    break;
//                }
//            }
//            if (firstDiff != -1) {
//                break;
//            }
//        }
//
//        if (firstDiff == -1 && shortestStrLen != longestStrLen) {
//            // we compared all of the characters up to the length of the
//            // shortest string and didn't find a match, but the string lengths
//            // vary, so return the length of the shortest string.
//            return shortestStrLen;
//        }
//        return firstDiff;
//    }
//
//    /**
//     * Compares all Strings in an array and returns the initial sequence of characters that is common
//     * to all of them.
//     *
//     * <p>For example, <code>
//     * getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "</code>
//     *
//     * <pre>
//     * StringHelper.getCommonPrefix(null) = ""
//     * StringHelper.getCommonPrefix(new String[] {}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"abc"}) = "abc"
//     * StringHelper.getCommonPrefix(new String[] {null, null}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"", ""}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"", null}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"abc", null, null}) = ""
//     * StringHelper.getCommonPrefix(new String[] {null, null, "abc"}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"", "abc"}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"abc", ""}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
//     * StringHelper.getCommonPrefix(new String[] {"abc", "a"}) = "a"
//     * StringHelper.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
//     * StringHelper.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
//     * StringHelper.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
//     * StringHelper.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
//     * </pre>
//     *
//     * @param strs array of String objects, entries may be null
//     * @return the initial sequence of characters that are common to all Strings in the array; empty
//     * String if the array is null, the elements are all null or if there is no common prefix.
//     */
//    public static String getCommonPrefix(final String... strs) {
//        if (strs == null || strs.length == 0) {
//            return EMPTY;
//        }
//        final int smallestIndexOfDiff = indexOfDifference(strs);
//        if (smallestIndexOfDiff == INDEX_NOT_FOUND) {
//            // all strings were identical
//            if (strs[0] == null) {
//                return EMPTY;
//            }
//            return strs[0];
//        } else if (smallestIndexOfDiff == 0) {
//            // there were no common initial characters
//            return EMPTY;
//        } else {
//            // we found a common initial character sequence
//            return strs[0].substring(0, smallestIndexOfDiff);
//        }
//    }
//
//    /**
//     * Find the Levenshtein distance between two Strings.
//     *
//     * <p>This is the number of changes needed to change one String into another, where each change is
//     * a single character modification (deletion, insertion or substitution).
//     *
//     * <p>The previous implementation of the Levenshtein distance algorithm was from <a
//     * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
//     *
//     * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError which
//     * can occur when my Java implementation is used with very large strings.<br>
//     * This implementation of the Levenshtein distance algorithm is from <a
//     * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
//     *
//     * <pre>
//     * StringHelper.getLevenshteinDistance(null, *)             = IllegalArgumentException
//     * StringHelper.getLevenshteinDistance(*, null)             = IllegalArgumentException
//     * StringHelper.getLevenshteinDistance("","")               = 0
//     * StringHelper.getLevenshteinDistance("","a")              = 1
//     * StringHelper.getLevenshteinDistance("aaapppp", "")       = 7
//     * StringHelper.getLevenshteinDistance("frog", "fog")       = 1
//     * StringHelper.getLevenshteinDistance("fly", "ant")        = 3
//     * StringHelper.getLevenshteinDistance("elephant", "hippo") = 7
//     * StringHelper.getLevenshteinDistance("hippo", "elephant") = 7
//     * StringHelper.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
//     * StringHelper.getLevenshteinDistance("hello", "hallo")    = 1
//     * </pre>
//     *
//     * @param s the first String, must not be null
//     * @param t the second String, must not be null
//     * @return result distance
//     * @throws IllegalArgumentException if either String input {@code null}
//     *                                  getLevenshteinDistance(CharSequence, CharSequence)
//     */
//    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
//        if (s == null || t == null) {
//            throw new IllegalArgumentException("Strings must not be null");
//        }
//
//    /*
//      The difference between this impl. and the previous is that, rather
//      than creating and retaining a matrix of size s.length() + 1 by t.length() + 1,
//      we maintain two single-dimensional arrays of length s.length() + 1.  The first, d,
//      is the 'current working' distance array that maintains the newest distance cost
//      counts as we iterate through the characters of String s.  Each time we increment
//      the index of String t we are comparing, d is copied to p, the second int[].  Doing so
//      allows us to retain the previous cost counts as required by the algorithm (taking
//      the minimum of the cost count to the left, up one, and diagonally up and to the left
//      of the current cost count being calculated).  (Note that the arrays aren't really
//      copied anymore, just switched...this is clearly much better than cloning an array
//      or doing a System.arraycopy() each time  through the outer loop.)
//
//      Effectively, the difference between the two implementations is this one does not
//      cause an out of memory condition when calculating the LD over two very large strings.
//    */
//
//        int n = s.length(); // length of s
//        int m = t.length(); // length of t
//
//        if (n == 0) {
//            return m;
//        } else if (m == 0) {
//            return n;
//        }
//
//        if (n > m) {
//            // swap the input strings to consume less memory
//            final CharSequence tmp = s;
//            s = t;
//            t = tmp;
//            n = m;
//            m = t.length();
//        }
//
//        int[] p = new int[n + 1]; // 'previous' cost array, horizontally
//        int[] d = new int[n + 1]; // cost array, horizontally
//        int[] _d; // placeholder to assist in swapping p and d
//
//        // indexes into strings s and t
//        int i; // iterates through s
//        int j; // iterates through t
//
//        char t_j; // jth character of t
//
//        int cost; // cost
//
//        for (i = 0; i <= n; i++) {
//            p[i] = i;
//        }
//
//        for (j = 1; j <= m; j++) {
//            t_j = t.charAt(j - 1);
//            d[0] = j;
//
//            for (i = 1; i <= n; i++) {
//                cost = s.charAt(i - 1) == t_j ? 0 : 1;
//                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
//                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
//            }
//
//            // copy current distance counts to 'previous row' distance counts
//            _d = p;
//            p = d;
//            d = _d;
//        }
//
//        // our last action in the above loop was to switch d and p, so p now
//        // actually has the most recent cost counts
//        return p[n];
//    }
//
//    /**
//     * Find the Levenshtein distance between two Strings if it's less than or equal to a given
//     * threshold.
//     *
//     * <p>This is the number of changes needed to change one String into another, where each change is
//     * a single character modification (deletion, insertion or substitution).
//     *
//     * <p>This implementation follows from Algorithms on Strings, Trees and Sequences by Dan Gusfield
//     * and Chas Emerick's implementation of the Levenshtein distance algorithm from <a
//     * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
//     *
//     * <pre>
//     * StringHelper.getLevenshteinDistance(null, *, *)             = IllegalArgumentException
//     * StringHelper.getLevenshteinDistance(*, null, *)             = IllegalArgumentException
//     * StringHelper.getLevenshteinDistance(*, *, -1)               = IllegalArgumentException
//     * StringHelper.getLevenshteinDistance("","", 0)               = 0
//     * StringHelper.getLevenshteinDistance("aaapppp", "", 8)       = 7
//     * StringHelper.getLevenshteinDistance("aaapppp", "", 7)       = 7
//     * StringHelper.getLevenshteinDistance("aaapppp", "", 6))      = -1
//     * StringHelper.getLevenshteinDistance("elephant", "hippo", 7) = 7
//     * StringHelper.getLevenshteinDistance("elephant", "hippo", 6) = -1
//     * StringHelper.getLevenshteinDistance("hippo", "elephant", 7) = 7
//     * StringHelper.getLevenshteinDistance("hippo", "elephant", 6) = -1
//     * </pre>
//     *
//     * @param s         the first String, must not be null
//     * @param t         the second String, must not be null
//     * @param threshold the target threshold, must not be negative
//     * @return result distance, or {@code -1} if the distance would be greater than the threshold
//     * @throws IllegalArgumentException if either String input {@code null} or negative threshold
//     */
//    public static int getLevenshteinDistance(CharSequence s, CharSequence t, final int threshold) {
//        if (s == null || t == null) {
//            throw new IllegalArgumentException("Strings must not be null");
//        }
//        if (threshold < 0) {
//            throw new IllegalArgumentException("Threshold must not be negative");
//        }
//
//    /*
//    This implementation only computes the distance if it's less than or equal to the
//    threshold value, returning -1 if it's greater.  The advantage is performance: unbounded
//    distance is O(nm), but a bound of k allows us to reduce it to O(km) time by only
//    computing a diagonal stripe of width 2k + 1 of the cost table.
//    It is also possible to use this to compute the unbounded Levenshtein distance by starting
//    the threshold at 1 and doubling each time until the distance is found; this is O(dm), where
//    d is the distance.
//
//    One subtlety comes from needing to ignore entries on the border of our stripe
//    eg.
//    p[] = |#|#|#|*
//    d[] =  *|#|#|#|
//    We must ignore the entry to the left of the leftmost member
//    We must ignore the entry above the rightmost member
//
//    Another subtlety comes from our stripe running off the matrix if the strings aren't
//    of the same size.  Since string s is always swapped to be the shorter of the two,
//    the stripe will always run off to the upper right instead of the lower left of the matrix.
//
//    As a concrete example, suppose s is of length 5, t is of length 7, and our threshold is 1.
//    In this case we're going to walk a stripe of length 3.  The matrix would look like so:
//
//       1 2 3 4 5
//    1 |#|#| | | |
//    2 |#|#|#| | |
//    3 | |#|#|#| |
//    4 | | |#|#|#|
//    5 | | | |#|#|
//    6 | | | | |#|
//    7 | | | | | |
//
//    Note how the stripe leads off the table as there is no possible way to turn a string of length 5
//    into one of length 7 in edit distance of 1.
//
//    Additionally, this implementation decreases memory usage by using two
//    single-dimensional arrays and swapping them back and forth instead of allocating
//    an entire n by m matrix.  This requires a few minor changes, such as immediately returning
//    when it's detected that the stripe has run off the matrix and initially filling the arrays with
//    large values so that entries we don't compute are ignored.
//
//    See Algorithms on Strings, Trees and Sequences by Dan Gusfield for some discussion.
//     */
//
//        int n = s.length(); // length of s
//        int m = t.length(); // length of t
//
//        // if one string is empty, the edit distance is necessarily the length of the other
//        if (n == 0) {
//            return m <= threshold ? m : -1;
//        } else if (m == 0) {
//            return n <= threshold ? n : -1;
//        }
//
//        if (n > m) {
//            // swap the two strings to consume less memory
//            final CharSequence tmp = s;
//            s = t;
//            t = tmp;
//            n = m;
//            m = t.length();
//        }
//
//        int[] p = new int[n + 1]; // 'previous' cost array, horizontally
//        int[] d = new int[n + 1]; // cost array, horizontally
//        int[] _d; // placeholder to assist in swapping p and d
//
//        // fill in starting table values
//        final int boundary = Math.min(n, threshold) + 1;
//        for (int i = 0; i < boundary; i++) {
//            p[i] = i;
//        }
//        // these fills ensure that the value above the rightmost entry of our
//        // stripe will be ignored in following loop iterations
//        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
//        Arrays.fill(d, Integer.MAX_VALUE);
//
//        // iterates through t
//        for (int j = 1; j <= m; j++) {
//            final char t_j = t.charAt(j - 1); // jth character of t
//            d[0] = j;
//
//            // compute stripe indices, constrain to array size
//            final int min = Math.max(1, j - threshold);
//            final int max = Math.min(n, j + threshold);
//
//            // the stripe may lead off of the table if s and t are of different sizes
//            if (min > max) {
//                return -1;
//            }
//
//            // ignore entry left of leftmost
//            if (min > 1) {
//                d[min - 1] = Integer.MAX_VALUE;
//            }
//
//            // iterates through [min, max] in s
//            for (int i = min; i <= max; i++) {
//                if (s.charAt(i - 1) == t_j) {
//                    // diagonally left and up
//                    d[i] = p[i - 1];
//                } else {
//                    // 1 + minimum of cell to the left, to the top, diagonally left and up
//                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
//                }
//            }
//
//            // copy current distance counts to 'previous row' distance counts
//            _d = p;
//            p = d;
//            d = _d;
//        }
//
//        // if p[n] is greater than the threshold, there's no guarantee on it being the correct
//        // distance
//        if (p[n] <= threshold) {
//            return p[n];
//        }
//        return -1;
//    }
//
//    // endsWith
//    // -----------------------------------------------------------------------
//
//    /**
//     * Check if a CharSequence starts with a specified prefix.
//     *
//     * <p>{@code null}s are handled without exceptions. Two {@code null} references are considered to
//     * be equal. The comparison is case sensitive.
//     *
//     * <pre>
//     * StringHelper.startsWith(null, null)      = true
//     * StringHelper.startsWith(null, "abc")     = false
//     * StringHelper.startsWith("abcdef", null)  = false
//     * StringHelper.startsWith("abcdef", "abc") = true
//     * StringHelper.startsWith("ABCDEF", "abc") = false
//     * </pre>
//     *
//     * @param str    the CharSequence to check, may be null
//     * @param prefix the prefix to find, may be null
//     * @return {@code true} if the CharSequence starts with the prefix, case sensitive, or both {@code
//     * null}
//     * @see String#startsWith(String)
//     * CharSequence)
//     */
//    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
//        return startsWith(str, prefix, false);
//    }
//
//    /**
//     * Case insensitive check if a CharSequence starts with a specified prefix.
//     *
//     * <p>{@code null}s are handled without exceptions. Two {@code null} references are considered to
//     * be equal. The comparison is case insensitive.
//     *
//     * <pre>
//     * StringHelper.startsWithIgnoreCase(null, null)      = true
//     * StringHelper.startsWithIgnoreCase(null, "abc")     = false
//     * StringHelper.startsWithIgnoreCase("abcdef", null)  = false
//     * StringHelper.startsWithIgnoreCase("abcdef", "abc") = true
//     * StringHelper.startsWithIgnoreCase("ABCDEF", "abc") = true
//     * </pre>
//     *
//     * @param str    the CharSequence to check, may be null
//     * @param prefix the prefix to find, may be null
//     * @return {@code true} if the CharSequence starts with the prefix, case insensitive, or both
//     * {@code null}
//     * @see String#startsWith(String)
//     * startsWithIgnoreCase(CharSequence, CharSequence)
//     */
//    public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
//        return startsWith(str, prefix, true);
//    }
//
//    /**
//     * Check if a CharSequence starts with a specified prefix (optionally case insensitive).
//     *
//     * @param str        the CharSequence to check, may be null
//     * @param prefix     the prefix to find, may be null
//     * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
//     * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
//     * @see String#startsWith(String)
//     */
//    private static boolean startsWith(
//            final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
//        if (str == null || prefix == null) {
//            return str == null && prefix == null;
//        }
//        if (prefix.length() > str.length()) {
//            return false;
//        }
//        return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
//    }
//
//    /**
//     * Check if a CharSequence starts with any of an array of specified strings.
//     *
//     * <pre>
//     * StringHelper.startsWithAny(null, null)      = false
//     * StringHelper.startsWithAny(null, new String[] {"abc"})  = false
//     * StringHelper.startsWithAny("abcxyz", null)     = false
//     * StringHelper.startsWithAny("abcxyz", new String[] {""}) = false
//     * StringHelper.startsWithAny("abcxyz", new String[] {"abc"}) = true
//     * StringHelper.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
//     * </pre>
//     *
//     * @param string        the CharSequence to check, may be null
//     * @param searchStrings the CharSequences to find, may be null or empty
//     * @return {@code true} if the CharSequence starts with any of the the prefixes, case insensitive,
//     * or both {@code null}
//     * startsWithAny(CharSequence, CharSequence...)
//     */
//    public static boolean startsWithAny(
//            final CharSequence string, final CharSequence... searchStrings) {
//        if (Whether.empty(string) || Whether.empty(searchStrings)) {
//            return false;
//        }
//        for (final CharSequence searchString : searchStrings) {
//            if (ToolString.startsWith(string, searchString)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Check if a CharSequence ends with a specified suffix.
//     *
//     * <p>{@code null}s are handled without exceptions. Two {@code null} references are considered to
//     * be equal. The comparison is case sensitive.
//     *
//     * <pre>
//     * StringHelper.endsWith(null, null)      = true
//     * StringHelper.endsWith(null, "def")     = false
//     * StringHelper.endsWith("abcdef", null)  = false
//     * StringHelper.endsWith("abcdef", "def") = true
//     * StringHelper.endsWith("ABCDEF", "def") = false
//     * StringHelper.endsWith("ABCDEF", "cde") = false
//     * </pre>
//     *
//     * @param str    the CharSequence to check, may be null
//     * @param suffix the suffix to find, may be null
//     * @return {@code true} if the CharSequence ends with the suffix, case sensitive, or both {@code
//     * null}
//     * @see String#endsWith(String)
//     * CharSequence)
//     */
//    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
//        return endsWith(str, suffix, false);
//    }
//
//    /**
//     * Case insensitive check if a CharSequence ends with a specified suffix.
//     *
//     * <p>{@code null}s are handled without exceptions. Two {@code null} references are considered to
//     * be equal. The comparison is case insensitive.
//     *
//     * <pre>
//     * StringHelper.endsWithIgnoreCase(null, null)      = true
//     * StringHelper.endsWithIgnoreCase(null, "def")     = false
//     * StringHelper.endsWithIgnoreCase("abcdef", null)  = false
//     * StringHelper.endsWithIgnoreCase("abcdef", "def") = true
//     * StringHelper.endsWithIgnoreCase("ABCDEF", "def") = true
//     * StringHelper.endsWithIgnoreCase("ABCDEF", "cde") = false
//     * </pre>
//     *
//     * @param str    the CharSequence to check, may be null
//     * @param suffix the suffix to find, may be null
//     * @return {@code true} if the CharSequence ends with the suffix, case insensitive, or both {@code
//     * null}
//     * @see String#endsWith(String)
//     * endsWithIgnoreCase(CharSequence, CharSequence)
//     */
//    public static boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
//        return endsWith(str, suffix, true);
//    }
//
//    /**
//     * Check if a CharSequence ends with a specified suffix (optionally case insensitive).
//     *
//     * @param str        the CharSequence to check, may be null
//     * @param suffix     the suffix to find, may be null
//     * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
//     * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
//     * @see String#endsWith(String)
//     */
//    private static boolean endsWith(
//            final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
//        if (str == null || suffix == null) {
//            return str == null && suffix == null;
//        }
//        if (suffix.length() > str.length()) {
//            return false;
//        }
//        final int strOffset = str.length() - suffix.length();
//        return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
//    }
//
//    /**
//     * Similar to <a
//     * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize
//     * -space</a>
//     *
//     * <p>The function returns the argument string with whitespace normalized by using <code>
//     * {@link #trim(String)}</code> to remove leading and trailing whitespace and then replacing
//     * sequences of whitespace characters by a single space. In XML Whitespace characters are the same
//     * as those allowed by the <a href="http://www.w3.org/TR/REC-xml/#NT-S">S</a> production, which is
//     * S ::= (#x20 | #x9 | #xD | #xA)+
//     *
//     * <p>Java's regexp pattern \s defines whitespace as [ \t\n\x0B\f\r]
//     *
//     * <p>For reference:
//     *
//     * <ul>
//     *   <li>\x0B = vertical tab
//     *   <li>\f = #xC = form feed
//     *   <li>#x20 = space
//     *   <li>#x9 = \t
//     *   <li>#xA = \n
//     *   <li>#xD = \r
//     * </ul>
//     *
//     * <p>The difference is that Java's whitespace includes vertical tab and form feed, which this
//     * functional will also normalize. Additionally <code>{@link #trim(String)}</code> removes control
//     * characters (char &lt;= 32) from both ends of this String.
//     *
//     * @param str the source String to normalize whitespaces from, may be null
//     * @return the modified string with whitespace normalized, {@code null} if null String input
//     * @see Pattern
//     * @see #trim(String)
//     * @see <a
//     * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize-space</a>
//     */
//    public static String normalizeSpace(final String str) {
//        if (str == null) {
//            return null;
//        }
//        return WHITESPACE_PATTERN.matcher(trim(str)).replaceAll(SPACE);
//    }
//
//    /**
//     * Check if a CharSequence ends with any of an array of specified strings.
//     *
//     * <pre>
//     * StringHelper.endsWithAny(null, null)      = false
//     * StringHelper.endsWithAny(null, new String[] {"abc"})  = false
//     * StringHelper.endsWithAny("abcxyz", null)     = false
//     * StringHelper.endsWithAny("abcxyz", new String[] {""}) = true
//     * StringHelper.endsWithAny("abcxyz", new String[] {"xyz"}) = true
//     * StringHelper.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
//     * </pre>
//     *
//     * @param string        the CharSequence to check, may be null
//     * @param searchStrings the CharSequences to find, may be null or empty
//     * @return {@code true} if the CharSequence ends with any of the the prefixes, case insensitive,
//     * or both {@code null}
//     */
//    public static boolean endsWithAny(
//            final CharSequence string, final CharSequence... searchStrings) {
//        if (Whether.empty(string) || Whether.empty(searchStrings)) {
//            return false;
//        }
//        for (final CharSequence searchString : searchStrings) {
//            if (ToolString.endsWith(string, searchString)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Appends the suffix to the end of the string if the string does not already end in the suffix.
//     *
//     * @param str        The string.
//     * @param suffix     The suffix to append to the end of the string.
//     * @param ignoreCase Indicates whether the compare should ignore case.
//     * @param suffixes   Additional suffixes that are valid terminators (optional).
//     * @return A new String if suffix was appened, the same string otherwise.
//     */
//    private static String appendIfMissing(
//            final String str,
//            final CharSequence suffix,
//            final boolean ignoreCase,
//            final CharSequence... suffixes) {
//        if (str == null || Whether.empty(suffix) || endsWith(str, suffix, ignoreCase)) {
//            return str;
//        }
//        if (suffixes != null && suffixes.length > 0) {
//            for (final CharSequence s : suffixes) {
//                if (endsWith(str, s, ignoreCase)) {
//                    return str;
//                }
//            }
//        }
//        return str + suffix;
//    }
//
//    /**
//     * Appends the suffix to the end of the string if the string does not already end with any the
//     * suffixes.
//     *
//     * <pre>
//     * StringHelper.appendIfMissing(null, null) = null
//     * StringHelper.appendIfMissing("abc", null) = "abc"
//     * StringHelper.appendIfMissing("", "xyz") = "xyz"
//     * StringHelper.appendIfMissing("abc", "xyz") = "abcxyz"
//     * StringHelper.appendIfMissing("abcxyz", "xyz") = "abcxyz"
//     * StringHelper.appendIfMissing("abcXYZ", "xyz") = "abcXYZxyz"
//     * </pre>
//     *
//     * <p>With additional suffixes,
//     *
//     * <pre>
//     * StringHelper.appendIfMissing(null, null, null) = null
//     * StringHelper.appendIfMissing("abc", null, null) = "abc"
//     * StringHelper.appendIfMissing("", "xyz", null) = "xyz"
//     * StringHelper.appendIfMissing("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
//     * StringHelper.appendIfMissing("abc", "xyz", "") = "abc"
//     * StringHelper.appendIfMissing("abc", "xyz", "mno") = "abcxyz"
//     * StringHelper.appendIfMissing("abcxyz", "xyz", "mno") = "abcxyz"
//     * StringHelper.appendIfMissing("abcmno", "xyz", "mno") = "abcmno"
//     * StringHelper.appendIfMissing("abcXYZ", "xyz", "mno") = "abcXYZxyz"
//     * StringHelper.appendIfMissing("abcMNO", "xyz", "mno") = "abcMNOxyz"
//     * </pre>
//     *
//     * @param str      The string.
//     * @param suffix   The suffix to append to the end of the string.
//     * @param suffixes Additional suffixes that are valid terminators.
//     * @return A new String if suffix was appened, the same string otherwise.
//     */
//    public static String appendIfMissing(
//            final String str, final CharSequence suffix, final CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, false, suffixes);
//    }
//
//    /**
//     * Appends the suffix to the end of the string if the string does not already end, case
//     * insensitive, with any of the suffixes.
//     *
//     * <pre>
//     * StringHelper.appendIfMissingIgnoreCase(null, null) = null
//     * StringHelper.appendIfMissingIgnoreCase("abc", null) = "abc"
//     * StringHelper.appendIfMissingIgnoreCase("", "xyz") = "xyz"
//     * StringHelper.appendIfMissingIgnoreCase("abc", "xyz") = "abcxyz"
//     * StringHelper.appendIfMissingIgnoreCase("abcxyz", "xyz") = "abcxyz"
//     * StringHelper.appendIfMissingIgnoreCase("abcXYZ", "xyz") = "abcXYZ"
//     * </pre>
//     *
//     * <p>With additional suffixes,
//     *
//     * <pre>
//     * StringHelper.appendIfMissingIgnoreCase(null, null, null) = null
//     * StringHelper.appendIfMissingIgnoreCase("abc", null, null) = "abc"
//     * StringHelper.appendIfMissingIgnoreCase("", "xyz", null) = "xyz"
//     * StringHelper.appendIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
//     * StringHelper.appendIfMissingIgnoreCase("abc", "xyz", "") = "abc"
//     * StringHelper.appendIfMissingIgnoreCase("abc", "xyz", "mno") = "axyz"
//     * StringHelper.appendIfMissingIgnoreCase("abcxyz", "xyz", "mno") = "abcxyz"
//     * StringHelper.appendIfMissingIgnoreCase("abcmno", "xyz", "mno") = "abcmno"
//     * StringHelper.appendIfMissingIgnoreCase("abcXYZ", "xyz", "mno") = "abcXYZ"
//     * StringHelper.appendIfMissingIgnoreCase("abcMNO", "xyz", "mno") = "abcMNO"
//     * </pre>
//     *
//     * @param str      The string.
//     * @param suffix   The suffix to append to the end of the string.
//     * @param suffixes Additional suffixes that are valid terminators.
//     * @return A new String if suffix was appened, the same string otherwise.
//     */
//    public static String appendIfMissingIgnoreCase(
//            final String str, final CharSequence suffix, final CharSequence... suffixes) {
//        return appendIfMissing(str, suffix, true, suffixes);
//    }
//
//    /**
//     * Prepends the prefix to the start of the string if the string does not already start with any of
//     * the prefixes.
//     *
//     * @param str        The string.
//     * @param prefix     The prefix to prepend to the start of the string.
//     * @param ignoreCase Indicates whether the compare should ignore case.
//     * @param prefixes   Additional prefixes that are valid (optional).
//     * @return A new String if prefix was prepended, the same string otherwise.
//     */
//    private static String prependIfMissing(
//            final String str,
//            final CharSequence prefix,
//            final boolean ignoreCase,
//            final CharSequence... prefixes) {
//        if (str == null || Whether.empty(prefix) || startsWith(str, prefix, ignoreCase)) {
//            return str;
//        }
//        if (prefixes != null && prefixes.length > 0) {
//            for (final CharSequence p : prefixes) {
//                if (startsWith(str, p, ignoreCase)) {
//                    return str;
//                }
//            }
//        }
//        return prefix + str;
//    }
//
//    /**
//     * Prepends the prefix to the start of the string if the string does not already start with any of
//     * the prefixes.
//     *
//     * <pre>
//     * StringHelper.prependIfMissing(null, null) = null
//     * StringHelper.prependIfMissing("abc", null) = "abc"
//     * StringHelper.prependIfMissing("", "xyz") = "xyz"
//     * StringHelper.prependIfMissing("abc", "xyz") = "xyzabc"
//     * StringHelper.prependIfMissing("xyzabc", "xyz") = "xyzabc"
//     * StringHelper.prependIfMissing("XYZabc", "xyz") = "xyzXYZabc"
//     * </pre>
//     *
//     * <p>With additional prefixes,
//     *
//     * <pre>
//     * StringHelper.prependIfMissing(null, null, null) = null
//     * StringHelper.prependIfMissing("abc", null, null) = "abc"
//     * StringHelper.prependIfMissing("", "xyz", null) = "xyz"
//     * StringHelper.prependIfMissing("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
//     * StringHelper.prependIfMissing("abc", "xyz", "") = "abc"
//     * StringHelper.prependIfMissing("abc", "xyz", "mno") = "xyzabc"
//     * StringHelper.prependIfMissing("xyzabc", "xyz", "mno") = "xyzabc"
//     * StringHelper.prependIfMissing("mnoabc", "xyz", "mno") = "mnoabc"
//     * StringHelper.prependIfMissing("XYZabc", "xyz", "mno") = "xyzXYZabc"
//     * StringHelper.prependIfMissing("MNOabc", "xyz", "mno") = "xyzMNOabc"
//     * </pre>
//     *
//     * @param str      The string.
//     * @param prefix   The prefix to prepend to the start of the string.
//     * @param prefixes Additional prefixes that are valid.
//     * @return A new String if prefix was prepended, the same string otherwise.
//     */
//    public static String prependIfMissing(
//            final String str, final CharSequence prefix, final CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, false, prefixes);
//    }
//
//    /**
//     * Prepends the prefix to the start of the string if the string does not already start, case
//     * insensitive, with any of the prefixes.
//     *
//     * <pre>
//     * StringHelper.prependIfMissingIgnoreCase(null, null) = null
//     * StringHelper.prependIfMissingIgnoreCase("abc", null) = "abc"
//     * StringHelper.prependIfMissingIgnoreCase("", "xyz") = "xyz"
//     * StringHelper.prependIfMissingIgnoreCase("abc", "xyz") = "xyzabc"
//     * StringHelper.prependIfMissingIgnoreCase("xyzabc", "xyz") = "xyzabc"
//     * StringHelper.prependIfMissingIgnoreCase("XYZabc", "xyz") = "XYZabc"
//     * </pre>
//     *
//     * <p>With additional prefixes,
//     *
//     * <pre>
//     * StringHelper.prependIfMissingIgnoreCase(null, null, null) = null
//     * StringHelper.prependIfMissingIgnoreCase("abc", null, null) = "abc"
//     * StringHelper.prependIfMissingIgnoreCase("", "xyz", null) = "xyz"
//     * StringHelper.prependIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
//     * StringHelper.prependIfMissingIgnoreCase("abc", "xyz", "") = "abc"
//     * StringHelper.prependIfMissingIgnoreCase("abc", "xyz", "mno") = "xyzabc"
//     * StringHelper.prependIfMissingIgnoreCase("xyzabc", "xyz", "mno") = "xyzabc"
//     * StringHelper.prependIfMissingIgnoreCase("mnoabc", "xyz", "mno") = "mnoabc"
//     * StringHelper.prependIfMissingIgnoreCase("XYZabc", "xyz", "mno") = "XYZabc"
//     * StringHelper.prependIfMissingIgnoreCase("MNOabc", "xyz", "mno") = "MNOabc"
//     * </pre>
//     *
//     * @param str      The string.
//     * @param prefix   The prefix to prepend to the start of the string.
//     * @param prefixes Additional prefixes that are valid (optional).
//     * @return A new String if prefix was prepended, the same string otherwise.
//     */
//    public static String prependIfMissingIgnoreCase(
//            final String str, final CharSequence prefix, final CharSequence... prefixes) {
//        return prependIfMissing(str, prefix, true, prefixes);
//    }
//
//    /**
//     * Converts a <code>byte[]</code> to a String using the specified character encoding.
//     *
//     * @param bytes       the byte array to read from
//     * @param charsetName the encoding to use, if null then use the platform default
//     * @return a new String
//     * @throws UnsupportedEncodingException If the named charset is not supported
//     * @throws NullPointerException         if the input is null
//     * @deprecated use {@link ToolString#toEncodedString(byte[], Charset)} instead of String
//     * constants in your code
//     */
//    @Deprecated
//    public static String toString(final byte[] bytes, final String charsetName)
//            throws UnsupportedEncodingException {
//        return charsetName != null
//                ? new String(bytes, charsetName)
//                : new String(bytes, Charset.defaultCharset());
//    }
//
//    /**
//     * Converts a <code>byte[]</code> to a String using the specified character encoding.
//     *
//     * @param bytes   the byte array to read from
//     * @param charset the encoding to use, if null then use the platform default
//     * @return a new String
//     * @throws UnsupportedEncodingException If the named charset is not supported
//     * @throws NullPointerException         if the input is null
//     */
//    public static String toEncodedString(byte[] bytes, Charset charset)
//            throws UnsupportedEncodingException {
//        return new String(bytes, charset != null ? charset : Charset.defaultCharset());
//    }
//
//    /**
//     * Converts the String to a Character using the first character, returning null for empty Strings.
//     *
//     * <p>For ASCII 7 bit characters, this uses a cache that will return the same Character object
//     * each time.
//     *
//     * <pre>
//     *   toCharacterObject(null) = null
//     *   toCharacterObject("")   = null
//     *   toCharacterObject("A")  = 'A'
//     *   toCharacterObject("BA") = 'B'
//     * </pre>
//     *
//     * @param str the character to convert
//     * @return the Character value of the first letter of the String
//     */
//    public static Character toCharacterObject(final String str) {
//        if (Whether.empty(str)) {
//            return null;
//        }
//        return str.charAt(0);
//    }
//
//    /**
//     * Converts the Character to a char throwing an exception for {@code null}.
//     *
//     * <pre>
//     *   toChar(' ')  = ' '
//     *   toChar('A')  = 'A'
//     *   toChar(null) throws IllegalArgumentException
//     * </pre>
//     *
//     * @param ch the character to convert
//     * @return the char value of the Character
//     * @throws IllegalArgumentException if the Character is null
//     */
//    public static char toChar(final Character ch) {
//        if (ch == null) {
//            throw new IllegalArgumentException("The Character must not be null");
//        }
//        return ch;
//    }
//
//    // -----------------------------------------------------------------------
//
//    /**
//     * Converts the Character to a char handling {@code null}.
//     *
//     * <pre>
//     *   toChar(null, 'X') = 'X'
//     *   toChar(' ', 'X')  = ' '
//     *   toChar('A', 'X')  = 'A'
//     * </pre>
//     *
//     * @param ch           the character to convert
//     * @param defaultValue the value to use if the Character is null
//     * @return the char value of the Character or the default if null
//     */
//    public static char toChar(final Character ch, final char defaultValue) {
//        if (ch == null) {
//            return defaultValue;
//        }
//        return ch;
//    }
//
//    /**
//     * Converts the String to a char using the first character, throwing an exception on empty
//     * Strings.
//     *
//     * <pre>
//     *   toChar("A")  = 'A'
//     *   toChar("BA") = 'B'
//     *   toChar(null) throws IllegalArgumentException
//     *   toChar("")   throws IllegalArgumentException
//     * </pre>
//     *
//     * @param str the character to convert
//     * @return the char value of the first letter of the String
//     * @throws IllegalArgumentException if the String is empty
//     */
//    public static char toChar(final String str) {
//        if (Whether.empty(str)) {
//            throw new IllegalArgumentException("The String must not be empty");
//        }
//        return str.charAt(0);
//    }
//
//    // -----------------------------------------------------------------------
//
//    /**
//     * Converts the String to a char using the first character, defaulting the value on empty Strings.
//     *
//     * <pre>
//     *   toChar(null, 'X') = 'X'
//     *   toChar("", 'X')   = 'X'
//     *   toChar("A", 'X')  = 'A'
//     *   toChar("BA", 'X') = 'B'
//     * </pre>
//     *
//     * @param str          the character to convert
//     * @param defaultValue the value to use if the Character is null
//     * @return the char value of the first letter of the String or the default if null
//     */
//    public static char toChar(final String str, final char defaultValue) {
//        if (Whether.empty(str)) {
//            return defaultValue;
//        }
//        return str.charAt(0);
//    }
//
//    /**
//     * Converts the character to the Integer it represents, throwing an exception if the character is
//     * not numeric.
//     *
//     * <p>This method coverts the char '1' to the int 1 and so on.
//     *
//     * <pre>
//     *   toIntValue('3')  = 3
//     *   toIntValue('A')  throws IllegalArgumentException
//     * </pre>
//     *
//     * @param ch the character to convert
//     * @return the int value of the character
//     * @throws IllegalArgumentException if the character is not ASCII numeric
//     */
//    public static int toIntValue(final char ch) {
//        if (!isAsciiNumeric(ch)) {
//            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
//        }
//        return ch - 48;
//    }
//
//    // -----------------------------------------------------------------------
//
//    /**
//     * Converts the character to the Integer it represents, throwing an exception if the character is
//     * not numeric.
//     *
//     * <p>This method coverts the char '1' to the int 1 and so on.
//     *
//     * <pre>
//     *   toIntValue('3', -1)  = 3
//     *   toIntValue('A', -1)  = -1
//     * </pre>
//     *
//     * @param ch           the character to convert
//     * @param defaultValue the default value to use if the character is not numeric
//     * @return the int value of the character
//     */
//    public static int toIntValue(final char ch, final int defaultValue) {
//        if (!isAsciiNumeric(ch)) {
//            return defaultValue;
//        }
//        return ch - 48;
//    }
//
//    /**
//     * Converts the character to the Integer it represents, throwing an exception if the character is
//     * not numeric.
//     *
//     * <p>This method coverts the char '1' to the int 1 and so on.
//     *
//     * <pre>
//     *   toIntValue('3')  = 3
//     *   toIntValue(null) throws IllegalArgumentException
//     *   toIntValue('A')  throws IllegalArgumentException
//     * </pre>
//     *
//     * @param ch the character to convert, not null
//     * @return the int value of the character
//     * @throws IllegalArgumentException if the Character is not ASCII numeric or is null
//     */
//    public static int toIntValue(final Character ch) {
//        if (ch == null) {
//            throw new IllegalArgumentException("The character must not be null");
//        }
//        return toIntValue(ch.charValue());
//    }
//
//    /**
//     * Converts the character to the Integer it represents, throwing an exception if the character is
//     * not numeric.
//     *
//     * <p>This method coverts the char '1' to the int 1 and so on.
//     *
//     * <pre>
//     *   toIntValue(null, -1) = -1
//     *   toIntValue('3', -1)  = 3
//     *   toIntValue('A', -1)  = -1
//     * </pre>
//     *
//     * @param ch           the character to convert
//     * @param defaultValue the default value to use if the character is not numeric
//     * @return the int value of the character
//     */
//    public static int toIntValue(final Character ch, final int defaultValue) {
//        if (ch == null) {
//            return defaultValue;
//        }
//        return toIntValue(ch.charValue(), defaultValue);
//    }
//
//    /**
//     * Converts the character to a String that contains the one character.
//     *
//     * <p>For ASCII 7 bit characters, this uses a cache that will return the same String object each
//     * time.
//     *
//     * <pre>
//     *   toString(' ')  = " "
//     *   toString('A')  = "A"
//     * </pre>
//     *
//     * @param ch the character to convert
//     * @return a String containing the one specified character
//     */
//    public static String toString(final char ch) {
//        if (ch < 128) {
//            return CHAR_STRING_ARRAY[ch];
//        }
//        return String.valueOf(ch);
//    }
//
//    // -----------------------------------------------------------------------
//
//    /**
//     * Converts the character to a String that contains the one character.
//     *
//     * <p>For ASCII 7 bit characters, this uses a cache that will return the same String object each
//     * time.
//     *
//     * <p>If {@code null} is passed in, {@code null} will be returned.
//     *
//     * <pre>
//     *   toString(null) = null
//     *   toString(' ')  = " "
//     *   toString('A')  = "A"
//     * </pre>
//     *
//     * @param ch the character to convert
//     * @return a String containing the one specified character
//     */
//    public static String toString(final Character ch) {
//        if (ch == null) {
//            return null;
//        }
//        return toString(ch.charValue());
//    }
//
//    /**
//     * Converts the string to the Unicode format '\u0020'.
//     *
//     * <p>This format is the Java source code format.
//     *
//     * <pre>
//     *   unicodeEscaped(' ') = "\u0020"
//     *   unicodeEscaped('A') = "\u0041"
//     * </pre>
//     *
//     * @param ch the character to convert
//     * @return the escaped Unicode string
//     */
//    public static String unicodeEscaped(final char ch) {
//        if (ch < 0x10) {
//            return "\\u000" + Integer.toHexString(ch);
//        } else if (ch < 0x100) {
//            return "\\u00" + Integer.toHexString(ch);
//        } else if (ch < 0x1000) {
//            return "\\u0" + Integer.toHexString(ch);
//        }
//        return "\\u" + Integer.toHexString(ch);
//    }
//
//    // --------------------------------------------------------------------------
//
//    /**
//     * Converts the string to the Unicode format '\u0020'.
//     *
//     * <p>This format is the Java source code format.
//     *
//     * <p>If {@code null} is passed in, {@code null} will be returned.
//     *
//     * <pre>
//     *   unicodeEscaped(null) = null
//     *   unicodeEscaped(' ')  = "\u0020"
//     *   unicodeEscaped('A')  = "\u0041"
//     * </pre>
//     *
//     * @param ch the character to convert, may be null
//     * @return the escaped Unicode string, null if null input
//     */
//    public static String unicodeEscaped(final Character ch) {
//        if (ch == null) {
//            return null;
//        }
//        return unicodeEscaped(ch.charValue());
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param input  显示对象
//     * @param length 固定长度
//     * @param pad    填充字符
//     * @param isLeft 是否靠左
//     */
//    public static String layout(
//            final CharSequence input, final int length, final char pad, final boolean isLeft) {
//        TextBuilder charSequenceAppendable = TextBuilder.of();
//        layout(charSequenceAppendable, input, length, pad, isLeft);
//        return charSequenceAppendable.toString();
//    }
//
//    /**
//     * 按固定长度处理字符串
//     *
//     * @param container 字符串容器
//     * @param input     显示对象
//     * @param length    固定长度
//     * @param pad       填充字符
//     * @param isLeft    是否靠左
//     */
//    public static void layout(
//            final TextBuilder container,
//            final CharSequence input,
//            final int length,
//            final char pad,
//            final boolean isLeft) {
//        if (input == null || input.length() == 0) {
//            return;
//        }
//        if (length <= input.length()) {
//            container.append(input);
//            return;
//        }
//        if (isLeft) {
//            container.append(input);
//            repeat(container, pad, length - input.length());
//        } else {
//            repeat(container, pad, length - input.length());
//            container.append(input);
//        }
//    }

//    /**
//     * 给定字符串数组全部做去首尾空格
//     *
//     * @param strs 字符串数组
//     */
//    public static void trim(String[] strs) {
//        if (null == strs) {
//            return;
//        }
//        String str;
//        for (int i = 0; i < strs.length; i++) {
//            str = strs[i];
//            if (null != str) {
//                strs[i] = CharSequenceUtil.trim(str);
//            }
//        }
//    }

//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
//     * 字符填充于字符串前
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @return 填充后的字符串
//     */
//    public static String fillBefore(String str, char filledChar, int len) {
//        return fill(str, filledChar, len, true);
//    }
//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
//     * 字符填充于字符串后
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @return 填充后的字符串
//     */
//    public static String fillAfter(String str, char filledChar, int len) {
//        return fill(str, filledChar, len, false);
//    }
//
//    /**
//     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串
//     *
//     * @param str        被填充的字符串
//     * @param filledChar 填充的字符
//     * @param len        填充长度
//     * @param isPre      是否填充在前
//     * @return 填充后的字符串
//     */
//    public static String fill(String str, char filledChar, int len, boolean isPre) {
//        final int strLen = str.length();
//        if (strLen > len) {
//            return str;
//        }
//
//        String filledStr = ToolString.repeat(filledChar, len - strLen);
//        return isPre ? filledStr.concat(str) : str.concat(filledStr);
//    }
//
//    /**
//     * 格式化文本，使用 {varName} 占位<br>
//     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
//     *
//     * @param template 文本模板，被替换的部分用 {key} 表示
//     * @param map      参数值对
//     * @return 格式化后的文本
//     */
//    public static String format(CharSequence template, Map<?, ?> map) {
//        return format(template, map, true);
//    }
//
//    /**
//     * 格式化文本，使用 {varName} 占位<br>
//     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
//     *
//     * @param template   文本模板，被替换的部分用 {key} 表示
//     * @param map        参数值对
//     * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
//     * @return 格式化后的文本
//     */
//    public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
//        return StrFormatter.format(template, map, ignoreNull);
//    }
//
//    /**
//     * 连接任意对象
//     *
//     * @param input     输入对象
//     * @param delimiter 分割符号
//     * @return 返回连接后的字符串
//     */
//    @Nonnull
//    public static String any(@Nullable Object input, @Nullable Object delimiter) {
//        return Concat.any(input, delimiter, null, null, null, true, String::valueOf);
//    }
//
//    /**
//     * 连接任意对象
//     *
//     * @param input             输入对象
//     * @param delimiter         分割符号
//     * @param tokenPrefix       token前缀
//     * @param tokenSuffix       token后缀
//     * @param defaultTokenValue 默认token
//     * @param isIgnoredNull     是否忽略null
//     * @param elementHandler    元素处理
//     * @return 返回连接后的字符串
//     */
//    @Nonnull
//    public static String any(
//            @Nullable Object input,
//            @Nullable Object delimiter,
//            @Nullable Object tokenPrefix,
//            @Nullable Object tokenSuffix,
//            @Nullable Object defaultTokenValue,
//            boolean isIgnoredNull,
//            @Nullable Function<Object, String> elementHandler) {
//        final String dtv = isIgnoredNull ? null : Valid.string(defaultTokenValue, Const.NULL_STRING);
//        if (input == null) {
//            if (dtv != null) {
//                return dtv;
//            }
//            return Empty.STRING;
//        }
//        final String d = Valid.string(delimiter, Const.DEFAULT_DELIMITER);
//        final String tp = Valid.string(tokenPrefix, Empty.STRING);
//        final String ts = Valid.string(tokenSuffix, Empty.STRING);
//        final Function<Object, String> eh = Valid.object(elementHandler, Object::toString);
//        StringBuilder sb = new StringBuilder();
//        anyRecursion(sb, input, d, tp, ts, dtv, eh);
//        sb.setLength(sb.length() - d.length());
//        return sb.toString();
//    }
//
//    @SuppressWarnings("unchecked")
//    private static void anyRecursion(
//            @Nonnull StringBuilder container,
//            @Nullable Object input,
//            @Nonnull String delimiter,
//            @Nonnull String tokenPrefix,
//            @Nonnull String tokenSuffix,
//            @Nullable String defaultTokenValue,
//            @Nonnull Function<Object, String> elementHandler) {
//        if (input == null) {
//            if (defaultTokenValue != null) {
//                container.append(tokenPrefix);
//                container.append(defaultTokenValue);
//                container.append(tokenSuffix);
//                container.append(delimiter);
//            }
//            return;
//        }
//        if (input instanceof Collection) {
//            for (Object value : (Collection) input) {
//                anyRecursion(
//                        container,
//                        value,
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//            }
//        } else if (input instanceof Iterable) {
//            Iterable iterable = (Iterable) input;
//            for (Object value : iterable) {
//                anyRecursion(
//                        container,
//                        value,
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//            }
//        } else if (input instanceof Iterator) {
//            Iterator iterator = (Iterator) input;
//            while (iterator.hasNext()) {
//                anyRecursion(
//                        container,
//                        iterator.next(),
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//            }
//        } else if (input instanceof Map) {
//            Set<Map.Entry> set = ((Map) input).entrySet();
//            for (Map.Entry value : set) {
//                anyRecursion(
//                        container,
//                        value.getKey(),
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//                anyRecursion(
//                        container,
//                        value.getValue(),
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//            }
//        } else if (input.getClass().isArray()) {
//            int length = Array.getLength(input);
//            for (int i = 0; i < length; i++) {
//                anyRecursion(
//                        container,
//                        Array.get(input, i),
//                        delimiter,
//                        tokenPrefix,
//                        tokenSuffix,
//                        defaultTokenValue,
//                        elementHandler);
//            }
//        } else {
//            String value = elementHandler.apply(input);
//            container.append(tokenPrefix);
//            container.append(value);
//            container.append(tokenSuffix);
//            container.append(delimiter);
//        }
//    }
//
//
//    /**
//     * 无效空白符
//     */
//    public static final String INVALID_BLANK = " \t\n\r\f";
//    /**
//     * 特殊字符--> {@code \t}
//     */
//    public static final char SC_T = 't';
//    /**
//     * 特殊字符--> {@code \n}
//     */
//    public static final char SC_N = 'n';
//    /**
//     * 特殊字符--> {@code \r}
//     */
//    public static final char SC_R = 'r';
//    /**
//     * 特殊字符--> {@code \f}
//     */
//    public static final char SC_F = 'f';
//    /**
//     * 特殊字符--> {@code \b}
//     */
//    public static final char SC_B = 'b';
//    /**
//     * false
//     */
//    public static final boolean FALSE = false;
//    /**
//     * true
//     */
//    public static final boolean TRUE = true;
//    /**
//     * 单引号
//     */
//    public static final char C_QUOTE = '\'';
//    /**
//     * 双引号
//     */
//    public static final char C_QUOTES = '"';
//    /**
//     * 单引号
//     */
//    public static final String S_QUOTE = "'";
//    /**
//     * 双引号
//     */
//    public static final String S_QUOTES = "\"";
//    /**
//     * 零
//     */
//    public static final byte ZERO_BYTE = 0;
//    /**
//     * 零
//     */
//    public static final short ZERO_SHORT = 0;
//    /**
//     * 零
//     */
//    public static final int ZERO_INT = 0;
//    /**
//     * 零
//     */
//    public static final long ZERO_LONG = 0;
//    /**
//     * 零
//     */
//    public static final float ZERO_FLOAT = 0;
//    /**
//     * 零
//     */
//    public static final double ZERO_DOUBLE = 0;
//    /**
//     * Ascii 数字编码下界
//     */
//    public static final int ASCII_NUMBER_LO = 48;
//    /**
//     * Ascii 数字编码上界
//     */
//    public static final int ASCII_NUMBER_HI = 57;
//    /**
//     * Ascii 大写字母编码下界
//     */
//    public static final int ASCII_UPPER_LETTER_LO = 65;
//    /**
//     * Ascii 大写字母编码上界
//     */
//    public static final int ASCII_UPPER_LETTER_HI = 90;
//    /**
//     * Ascii 小写字母编码下界
//     */
//    public static final int ASCII_LOWER_LETTER_LO = 97;
//    /**
//     * Ascii 小写字母编码上界
//     */
//    public static final int ASCII_LOWER_LETTER_HI = 122;
//    /**
//     * 无效数字，适合所有数字返回值判断
//     */
//    public static final int EOF = -1;
//    /**
//     * 无效字符串，适合所有数字返回值判断
//     */
//    public static final String EMPTY_STRING = "";
//    /**
//     * 无效字符串，适合所有数字返回值判断
//     */
//    public static final String NULL_STRING = "null";
//
//    public static final String DEFAULT_DELIMITER = ",";
//
//    /**
//     * 小写HEX
//     */
//    public static final char[] LOWER_HEX =
//            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//    /**
//     * 大写HEX
//     */
//    public static final char[] UPPER_HEX =
//            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//
//    public static final Map<Character, Integer> LOWER_HEX_NUMBER_MAPPING =
//            Map.ofEntries(
//                    Map.entry('0', 0),
//                    Map.entry('1', 1),
//                    Map.entry('2', 2),
//                    Map.entry('3', 3),
//                    Map.entry('4', 4),
//                    Map.entry('5', 5),
//                    Map.entry('6', 6),
//                    Map.entry('7', 7),
//                    Map.entry('8', 8),
//                    Map.entry('9', 9),
//                    Map.entry('a', 10),
//                    Map.entry('b', 11),
//                    Map.entry('c', 12),
//                    Map.entry('d', 13),
//                    Map.entry('e', 14),
//                    Map.entry('f', 15));
//    public static final Map<Character, Integer> UPPER_HEX_NUMBER_MAPPING =
//            Map.ofEntries(
//                    Map.entry('0', 0),
//                    Map.entry('1', 1),
//                    Map.entry('2', 2),
//                    Map.entry('3', 3),
//                    Map.entry('4', 4),
//                    Map.entry('5', 5),
//                    Map.entry('6', 6),
//                    Map.entry('7', 7),
//                    Map.entry('8', 8),
//                    Map.entry('9', 9),
//                    Map.entry('A', 10),
//                    Map.entry('B', 11),
//                    Map.entry('C', 12),
//                    Map.entry('D', 13),
//                    Map.entry('E', 14),
//                    Map.entry('F', 15));
//
//    private Const() throws IllegalAccessException {
//        throw new IllegalAccessException(
//                "The instance creation is not allowed,because this is static method utils class");
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type  常量类型
//     * @param input 输入
//     * @param <T>   枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindableConstant(Class<T> type, int input) {
//        return parseFindableConstant(type, input, null);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type         常量类型
//     * @param input        输入
//     * @param defaultValue 默认值
//     * @param <T>          枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindableConstant(
//            Class<T> type, int input, T defaultValue) {
//        return parseFindable(type, input, defaultValue);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type  常量类型
//     * @param input 输入
//     * @param <T>   枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindableConstant(
//            Class<T> type, String input) {
//        return parseFindableConstant(type, input, null);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type         常量类型
//     * @param input        输入
//     * @param defaultValue 默认值
//     * @param <T>          枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindableConstant(
//            Class<T> type, String input, T defaultValue) {
//        return parseFindable(type, input, defaultValue);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type  常量类型
//     * @param input 输入
//     * @param <T>   枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindable(Class<T> type, int input) {
//        return parseFindable(type, input, null);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type  常量类型
//     * @param input 输入
//     * @param <T>   枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindable(
//            Class<T> type, int input, T defaultValue) {
//        try {
//            if (FindableConstant.class.isAssignableFrom(type)) {
//                Field[] fields = type.getDeclaredFields();
//                for (Field field : fields) {
//                    int modifiers = field.getModifiers();
//                    if (Modifier.isPublic(modifiers)
//                            && Modifier.isStatic(modifiers)
//                            && Modifier.isFinal(modifiers)
//                            && field.getType().equals(type)) {
//
//                        field.setAccessible(true);
//                        T result = (T) field.get(null);
//                        if (result.identity() == input) {
//                            return result;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return defaultValue;
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type  常量类型
//     * @param input 输入
//     * @param <T>   枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindable(Class<T> type, String input) {
//        return parseFindable(type, input, null);
//    }
//
//    /**
//     * 解析常量
//     *
//     * @param type         常量类型
//     * @param input        输入
//     * @param defaultValue 默认值
//     * @param <T>          枚举类型
//     * @return 返回{@code T extends Findable<T>}
//     */
//    public static <T extends FindableConstant<T>> T parseFindable(
//            Class<T> type, String input, T defaultValue) {
//        try {
//            if (FindableConstant.class.isAssignableFrom(type)) {
//                Field[] fields = type.getDeclaredFields();
//                for (Field field : fields) {
//                    int modifiers = field.getModifiers();
//                    if (Modifier.isPublic(modifiers)
//                            && Modifier.isStatic(modifiers)
//                            && Modifier.isFinal(modifiers)
//                            && field.getType().equals(type)) {
//                        field.setAccessible(true);
//                        T result = (T) field.get(null);
//                        if (result == null) {
//                            continue;
//                        }
//                        if (result.isCustomMatchString() && result.isMatchString(input)) {
//                            return result;
//                        }
//                        String name = result.identityName();
//                        if (name == null) {
//                            continue;
//                        }
//                        if (result.isIgnoreNameCase()) {
//                            if (name.equalsIgnoreCase(input)) {
//                                return result;
//                            }
//                        } else {
//                            if (name.equals(input)) {
//                                return result;
//                            }
//                        }
//                        String[] alias = result.alias();
//                        if (alias == null || alias.length == 0) {
//                            continue;
//                        }
//                        for (String as : alias) {
//                            if (as == null) {
//                                continue;
//                            }
//                            if (result.isIgnoreAliasCase()) {
//                                if (as.equalsIgnoreCase(input)) {
//                                    return result;
//                                }
//                            } else {
//                                if (as.equals(input)) {
//                                    return result;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return defaultValue;
//    }
//
//}
