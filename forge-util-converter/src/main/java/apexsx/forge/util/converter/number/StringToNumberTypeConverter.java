package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符串到Number类型转换器
 * <p>
 * 用于将字符串转换为Number类型。
 * 该转换器直接使用Number的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToNumberTypeConverter extends CharSequenceTypeConverter<String, Number> {
    /**
     * 单例实例
     */
    public static final StringToNumberTypeConverter INSTANCE = new StringToNumberTypeConverter();
    private final Map<Integer, Integer> characterMapping;

    /**
     * 构造函数，初始化目标类型为Number.meta
     */
    protected StringToNumberTypeConverter() {
        this(null, null);
    }

    /**
     * 构造函数，初始化目标类型为Number.meta
     *
     * @param source 源字符序列，用于字符映射
     * @param target 目标字符序列，用于字符映射
     */
    protected StringToNumberTypeConverter(CharSequence source, CharSequence target) {
        super(String.class, Number.class);
        if (source == null || target == null) {
            this.characterMapping = Collections.emptyMap();
        } else {
            int sourceLength = source.length(), targetLength = target.length();
            if (sourceLength == 0) {
                this.characterMapping = Collections.emptyMap();
            } else {
                Map<Integer, Integer> map = new HashMap<>();
                for (int i = 0, iv, j = 0, jv; i < sourceLength && j < targetLength; i += Character.charCount(iv), j += Character.charCount(jv)) {
                    iv = Character.codePointAt(source, i);
                    jv = Character.codePointAt(target, j);
                    if (map.containsKey(iv)) {
                        throw new IllegalArgumentException("Duplicate mapping for character: " + iv);
                    }
                    map.put(iv, jv);
                }
                this.characterMapping = map;
            }
        }
    }

    /**
     * 将修剪后的字符串转换为目标类型对象
     *
     * @param input 修剪后的字符串
     * @return 转换后的目标对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Number convertsUncheckedString(String input) throws Throwable {
        // 字符映射处理
        int[] codePoints = input.codePoints()
                .map(cp -> characterMapping.getOrDefault(cp, cp))
                .toArray();
        int len = codePoints.length;
        StringBuilder buffer = new StringBuilder(len + 8);
        int index = 0;

        // 处理正负号
        int ch = codePoints[index];
        if (ch == '+' || ch == '-') {
            buffer.appendCodePoint(ch);
            index++;
            if (index >= len) {
                return BigInteger.ZERO; // 只有符号
            }
        }

        boolean hasDigit = false;
        int dotIndex = -1;

        // 查找小数点位置
        for (int i = 0; i < len; i++) {
            if (codePoints[i] == '.') {
                dotIndex = i;
                break;
            }
        }

        // 整数部分处理，支持千分位分隔符
        while (index < len) {
            ch = codePoints[index];
            if (ch == ',') {
                index++; // 跳过千分位分隔符
            } else if (ch == '.') {
                break;
            } else if (Character.isDigit(ch)) {
                buffer.appendCodePoint(ch);
                hasDigit = true;
                index++;
            } else {
                break;
            }
        }

        // 小数点处理
        if (index < len && codePoints[index] == '.') {
            if (!hasDigit) {
                buffer.append('0');
            }
            buffer.append('.');
            hasDigit = true;
            index++;
            boolean decimalHasDigit = false;
            while (index < len && Character.isDigit(codePoints[index])) {
                buffer.appendCodePoint(codePoints[index]);
                decimalHasDigit = true;
                index++;
            }
            if (!decimalHasDigit) {
                buffer.append('0');
            }
        }

        // 如果没有数字则返回null
        if (!hasDigit) {
            return null;
        }

        // 指数部分处理
        if (index < len && (codePoints[index] == 'e' || codePoints[index] == 'E')) {
            buffer.append('E');
            index++;
            if (index >= len) {
                buffer.setLength(buffer.length() - 1); // 回退 E
            } else {
                ch = codePoints[index];
                if (ch == '+' || ch == '-') {
                    buffer.appendCodePoint(ch);
                    index++;
                } else {
                    buffer.append('+');
                }
                int expStart = buffer.length();
                while (index < len && Character.isDigit(codePoints[index])) {
                    buffer.appendCodePoint(codePoints[index]);
                    index++;
                }
                if (buffer.length() == expStart) {
                    buffer.setLength(buffer.length() - 1); // 去掉符号
                }
            }
        }

        // 如果还有未处理的字符则返回null
        if (index != len) {
            return null;
        }

        try {
            String resultStr = buffer.toString();
            // 根据是否包含小数点决定返回BigDecimal还是BigInteger
            return dotIndex >= 0 ? new BigDecimal(resultStr) : new BigInteger(resultStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
