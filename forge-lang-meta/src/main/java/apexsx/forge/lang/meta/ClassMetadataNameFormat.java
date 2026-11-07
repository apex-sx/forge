package apexsx.forge.lang.meta;

import java.lang.reflect.Field;

/**
 * 类元数据命名格式枚举
 * 定义了不同命名规范的标识，用于字段名或方法名的格式化
 *
 * @author zhitron
 */
public enum ClassMetadataNameFormat {
    /**
     * 驼峰命名法，首字母大写
     * 示例: UserName, FirstName, XmlParser
     */
    CAMEL_FIRST_UPPERCASE,

    /**
     * 驼峰命名法，首字母小写
     * 示例: userName, firstName, xmlParser
     */
    CAMEL_FIRST_LOWERCASE,

    /**
     * 下划线命名法，不改变原有大小写
     * 示例: user_Name, first_Name, xml_Parser
     */
    UNDERLINE,

    /**
     * 下划线命名法，明确指定大写
     * 示例: USER_NAME, FIRST_NAME, XML_PARSER
     */
    UNDERLINE_UPPERCASE,

    /**
     * 下划线命名法，全小写
     * 示例: user_name, first_name, xml_parser
     */
    UNDERLINE_LOWERCASE;

    /**
     * 将字段名转换为指定的命名格式
     *
     * @param field  需要转换的字段
     * @param result 用于构建结果的StringBuilder
     * @return 转换后的命名字符串
     */
    public String formatName(Field field, StringBuilder result) {
        return formatName(field.getName(), result);
    }

    /**
     * 将字段名转换为指定的命名格式
     *
     * @param name   需要转换的字段
     * @param result 用于构建结果的StringBuilder
     * @return 转换后的命名字符串
     */
    public String formatName(String name, StringBuilder result) {
        // 清空StringBuilder以确保我们从一个干净的状态开始
        result.setLength(0);
        // 获取字段名称并转换为字符数组以便处理
        char[] names = name.toCharArray();
        // 标记是否需要添加下划线分隔符
        boolean needUnderline = false;
        // s表示当前处理段的起始位置，i是遍历指针
        int s = 0, i = 0;

        // 遍历整个字段名字符数组
        while (i < names.length) {
            // 如果当前字符是小写字母、数字、下划线或$符号，则继续向前移动
            if (Character.isLowerCase(names[i]) || Character.isDigit(names[i]) ||
                    names[i] == '_' || names[i] == '$') {
                i++;
            }
            // 如果遇到大写字母，需要进行特殊处理
            else if (Character.isUpperCase(names[i])) {
                // 如果前面有未处理的字符段，则先处理这一段
                if (i - s != 0) {
                    if (needUnderline) {
                        result.append('_');
                    }
                    appendName(names, s, i, result);
                    // 更新下划线标记，如果前一个字符不是下划线则下次需要添加下划线
                    needUnderline = (this == UNDERLINE_LOWERCASE || this == UNDERLINE_UPPERCASE || this == UNDERLINE) && names[i - 1] != '_';
                }
                // 记录大写字母序列的起始位置
                s = i++;

                // 继续查找连续的大写字母、数字、下划线或$符号
                while (i < names.length) {
                    if (Character.isUpperCase(names[i]) || Character.isDigit(names[i]) ||
                            names[i] == '_' || names[i] == '$') {
                        i++;
                    } else {
                        break;
                    }
                }

                // 如果大写字母序列长度大于1，则需要特殊处理
                if (i - s > 1) {
                    // 回退一位，将前面的连续大写字母作为一个整体处理
                    i--;
                    if (needUnderline) {
                        result.append('_');
                    }
                    appendName(names, s, i, result);
                    needUnderline = (this == UNDERLINE_LOWERCASE || this == UNDERLINE_UPPERCASE || this == UNDERLINE) && names[i - 1] != '_';
                    // 更新新的处理起点
                    s = i;
                    i++;
                }
            }
        }

        // 处理最后剩余的字符段
        if (i - s > 0) {
            if (needUnderline) {
                result.append('_');
            }
            appendName(names, s, i, result);
        }

        return result.toString();
    }

    /**
     * 根据命名格式向结果中添加字符
     *
     * @param chars  字符数组
     * @param start  开始索引
     * @param end    结束索引
     * @param result 用于构建结果的StringBuilder
     */
    private void appendName(char[] chars, int start, int end, StringBuilder result) {
        switch (this) {
            case CAMEL_FIRST_LOWERCASE:
                // 首字母小写的驼峰命名：第一个字符转小写，其余保持原样
                result.append(Character.toLowerCase(chars[start]));
                result.append(chars, start + 1, end - start - 1);
                break;
            case CAMEL_FIRST_UPPERCASE:
                // 首字母大写的驼峰命名：第一个字符转大写，其余保持原样
                result.append(Character.toUpperCase(chars[start]));
                result.append(chars, start + 1, end - start - 1);
                break;
            case UNDERLINE_LOWERCASE:
                // 下划线全小写：所有字符都转为小写
                for (int j = start; j < end; j++) {
                    result.append(Character.toLowerCase(chars[j]));
                }
                break;
            case UNDERLINE_UPPERCASE:
                // 下划线全大写：所有字符都转为大写
                for (int j = start; j < end; j++) {
                    result.append(Character.toUpperCase(chars[j]));
                }
                break;
            default:
                // 默认情况：直接添加原始字符
                result.append(chars, start, end - start);
                break;
        }
    }
}
