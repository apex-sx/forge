/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.converter;

/**
 * 字符序列类型转换器抽象基类
 * <p>
 * 用于将实现了CharSequence接口的源类型转换为目标类型。
 * 该转换器提供了通用的字符串修剪功能，会在转换前自动去除首尾空白字符。
 * 子类需要实现具体的转换逻辑。
 * </p>
 *
 * @param <SourceType> 源类型，必须实现CharSequence接口
 * @param <TargetType> 目标类型
 * @author Terraqo
 */
public abstract class CharSequenceTypeConverter<SourceType extends CharSequence, TargetType> extends AbstractTypeConverter<SourceType, TargetType> {

    /**
     * 构造函数，初始化目标类型和支持的选择器
     *
     * @param targetType         目标类型的Class对象
     * @param supportedSelectors 支持的选择器列表
     * @throws NullPointerException 当targetType为null时抛出
     */
    protected CharSequenceTypeConverter(Class<TargetType> targetType, Object... supportedSelectors) {
        super(targetType, supportedSelectors);
    }

    /**
     * 构造函数，初始化源类型、目标类型和支持的选择器
     *
     * @param sourceType         源类型的Class对象，可以为null
     * @param targetType         目标类型的Class对象
     * @param supportedSelectors 支持的选择器列表
     * @throws NullPointerException 当targetType为null时抛出
     */
    protected CharSequenceTypeConverter(Class<SourceType> sourceType, Class<TargetType> targetType, Object... supportedSelectors) {
        super(sourceType, targetType, supportedSelectors);
    }

    /**
     * 判断指定的源对象是否支持转换
     * 当{@link #supportsSourceType()}返回null时，会调用此方法进行判断
     *
     * @param source 源对象
     * @return 如果支持转换返回true，否则返回false
     */
    @Override
    public final boolean isSupportsSource(Object source) {
        return source instanceof CharSequence;
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * <p>
     * 此方法会对输入的字符序列进行首尾空白字符修剪处理，然后再进行类型转换。
     * 对于空字符串或全空白字符的字符串，会直接返回{@link #convertsUncheckedEmpty()}的结果。
     * </p>
     *
     * @param source 源对象，必须为CharSequence类型
     * @return 转换后的目标对象，可能为null
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    public final TargetType convertsUnchecked(SourceType source) throws Throwable {
        int temp = source.length();
        // 空字符串直接处理
        if (temp == 0) {
            return convertsUncheckedEmpty();
        }
        // 初始化起始位置为0，结束位置为输入序列的最后一个字符索引
        int s = 0, e = temp - 1;
        // 使用位运算标记需要处理的方向：0b01表示处理开头，0b10表示处理结尾
        for (int tag = 0b10 | 0b01, i; tag != 0 && s <= e; ) {
            // 如果需要处理开头且满足移除条件，则移动起始位置
            if ((tag & 0b01) == 0b01) {
                // 获取指定索引处的code point
                temp = Character.codePointAt(source, s);
                // 如果该code point满足修剪条件，则返回其占用的字符数（1或2）
                i = isTrimCodePoint(source, s, temp) ? Character.charCount(temp) : 0;
                if (i > 0) {
                    // 如果需要跳过，则更新起始位置
                    s += i;
                } else {
                    // 否则清除处理开头的标记（将最低位设为0）
                    tag = tag & 0b10;
                }
            }
            // 如果需要处理结尾（tag的第二位为1）
            if ((tag & 0b10) == 0b10) {
                // 获取结束位置处的code point
                temp = Character.codePointBefore(source, e);
                // 如果该code point满足修剪条件，则返回其占用的字符数（1或2）
                i = isTrimCodePoint(source, e, temp) ? Character.charCount(temp) : 0;
                if (i > 0) {
                    // 如果需要跳过，则更新结束位置
                    e -= i;
                } else {
                    // 否则清除处理结尾的标记（将第二位设为0）
                    tag = tag & 0b01;
                }
            }
        }
        // 如果起始位置大于结束位置，说明整个字符串都是空白字符，返回空值处理结果
        if (s > e) {
            return convertsUncheckedEmpty();
        }
        // 如果修剪后的字符串长度等于原字符串长度，说明没有需要修剪的字符，直接转换整个字符串
        if (e - s + 1 == source.length()) {
            return convertsUncheckedString(source.toString());
        }

        // 否则，提取修剪后的子序列并转换
        return convertsUncheckedString(source.subSequence(s, e + 1).toString());
    }

    /**
     * 判断指定位置的代码点是否应该被修剪掉
     *
     * @param source    源字符序列
     * @param index     代码点在源中的索引位置
     * @param codePoint 需要判断的代码点
     * @return 如果应该被修剪返回true，否则返回false
     */
    protected boolean isTrimCodePoint(SourceType source, int index, int codePoint) {
        return Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint);
    }

    /**
     * 处理空字符串或全空白字符字符串的转换结果
     *
     * @return 空字符串转换后的结果，默认为null
     */
    protected TargetType convertsUncheckedEmpty() {
        return null;
    }


    /**
     * 将修剪后的字符串转换为目标类型对象
     *
     * @param input 修剪后的字符串
     * @return 转换后的目标对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    protected abstract TargetType convertsUncheckedString(String input) throws Throwable;
}
