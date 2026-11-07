package terraqo.arbiter.assist;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * @author Terraqo
 */
public final class AssistText {

    private AssistText() throws IllegalAccessException {
        throw new IllegalAccessException(
                "The instance creation is not allowed,because this is static method utils class");
    }


    public static boolean isEmpty(CharSequence input) {
        return !AssistText.isNoEmpty(input);
    }

    public static boolean isNoEmpty(CharSequence input) {
        return input != null && !input.isEmpty();
    }


    public static boolean isEquals(CharSequence input, int inputStart, int inputLength,
                                   CharSequence other, int otherStart, int otherLength,
                                   boolean ignoreCase) {
        // 长度不一致，直接返回 false
        if (inputLength != otherLength) {
            return false;
        }

        // 长度为 0，视为相等
        if (inputLength == 0) {
            return true;
        }

        // 边界检查：确保起始索引和长度在有效范围内
        int inputEnd = inputStart + inputLength;
        int otherEnd = otherStart + otherLength;

        if (inputStart < 0 || inputEnd > input.length() || otherStart < 0 || otherEnd > other.length()) {
            throw new IndexOutOfBoundsException();
        }

        // 遍历每个 codepoint 进行比较
        int i1 = inputStart;
        int i2 = otherStart;

        while (i1 < inputEnd && i2 < otherEnd) {
            int cp1 = Character.codePointAt(input, i1);
            int cp2 = Character.codePointAt(other, i2);

            if (ignoreCase) {
                // 忽略大小写比较
                // 使用 Character.equalsIgnoreCase 进行更准确且高效的比较
                if (Character.toLowerCase(cp1) != Character.toLowerCase(cp2)) {
                    return false;
                }
//                if (!Character.equalsIgnoreCase(cp1, cp2)) {
//                    return false;
//                }
            } else {
                // 区分大小写比较
                if (cp1 != cp2) {
                    return false;
                }
            }

            // 移动到下一个 codepoint
            i1 += Character.charCount(cp1);
            i2 += Character.charCount(cp2);
        }

        // 如果都遍历完了，说明相等
        return i1 == inputEnd && i2 == otherEnd;
    }

    public static String repeat(Appendable container, Object input, int repeatCount, Object separator,
                                Object elementPrefix, Object elementSuffix, Object prefix, Object suffix) {
        if (repeatCount <= 0) {
            return container == null ? "" : null;
        }
        String inputString = input.toString();
        String separatorString = separator == null ? "" : separator.toString();
        String elementPrefixString = elementPrefix == null ? "" : elementPrefix.toString();
        String elementSuffixString = elementSuffix == null ? "" : elementSuffix.toString();
        String prefixString = prefix == null ? "" : prefix.toString();
        String suffixString = suffix == null ? "" : suffix.toString();
        boolean isReturnResult = false;
        if (container == null) {
            container = new StringBuilder();
            isReturnResult = true;
        }
        try {
            container.append(prefixString);
            container.append(elementPrefixString);
            container.append(inputString);
            container.append(elementSuffixString);
            for (int i = 1; i < repeatCount; i++) {
                container.append(separatorString);
                container.append(elementPrefixString);
                container.append(inputString);
                container.append(elementSuffixString);
            }
            container.append(suffixString);
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
        if (isReturnResult) {
            return container.toString();
        }
        return null;
    }

    public static String layout(Appendable container, Object input, int length, Object padding, Boolean position) {
        String inputString = input.toString();
        if (length <= 0) {
            return inputString;
        }
        int differenceLength = length - inputString.length();
        if (differenceLength <= 0) {
            return inputString;
        }
        String paddingString = padding.toString();
        int paddingStringLength = paddingString.length();
        int count = differenceLength / paddingStringLength;
        int deviation = differenceLength - count * paddingStringLength;
        if (position == null) {

        }
        return "";
    }
}
