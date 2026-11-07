/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

import java.util.Objects;

/**
 * TokenLoader类负责从输入流中加载并解析Token
 * 它实现了Iterator<CommonToken>接口，可以逐个获取Token
 *
 * @author Terraqo
 */
public final class CommonTokenLoader extends ElementLookaheadLoaderBase<CommonToken> {

    /**
     * 字符码点加载器，负责从输入源逐个读取字符码点
     * 该加载器提供了访问输入流的功能，包括获取下一个码点、当前位置（行号、列号）等
     * TokenLoader使用此加载器来获取待解析的字符数据
     */
    private final CodePointLoader codePointLoader;

    /**
     * Token解析器链管理器，负责管理和维护Token解析器链
     * 该管理器提供了获取解析器实例链的方法，TokenLoader会依次尝试每个解析器来解析输入
     * 解析器链的顺序决定了Token识别的优先级
     */
    private final CommonTokenParserManager commonTokenParserManager;

    /**
     * 构造函数，初始化TokenLoader
     *
     * @param codePointLoader 字符码点加载器
     */
    public CommonTokenLoader(CodePointLoader codePointLoader, CommonTokenParserManager commonTokenParserManager) {
        this.codePointLoader = Objects.requireNonNull(codePointLoader);
        this.commonTokenParserManager = Objects.requireNonNull(commonTokenParserManager);
    }

    /**
     * 返回结束标识符，用于表示序列的结束
     * 当到达序列末尾时，peek方法将返回此值
     * 默认实现返回null，子类可以根据需要重写此方法
     *
     * @return 序列结束标识符
     */
    @Override
    protected CommonToken eof() {
        return new CommonToken(CommonTokenType.EOF, "", codePointLoader.currentLine(), codePointLoader.currentColumn());
    }

    /**
     * 从底层数据源获取下一个元素
     * 此方法由子类实现以提供实际的数据源逻辑
     *
     * @return 从数据源获取的下一个元素，若已到达数据源末尾则返回 null
     */
    @Override
    protected CommonToken fetchElement() {
        // 如果已经没有更多的字符码点，则返回EOF CommonToken
        if (!codePointLoader.hasNextCodePoint()) {
            return endIndicator();
        }

        // 记录当前的位置信息，用于Token创建
        int currentLine = codePointLoader.currentLine();
        int currentColumn = codePointLoader.currentColumn();

        // 遍历所有TokenParser实例，尝试解析Token
        for (CommonTokenParser commonTokenParser : commonTokenParserManager.getInstanceChain()) {
            // 保存当前位置，以便在解析失败时恢复
            int savedLine = codePointLoader.currentLine();
            int savedColumn = codePointLoader.currentColumn();

            // 尝试使用当前解析器解析Token
            String tokenValue = commonTokenParser.parseToken(codePointLoader);

            // 如果解析成功（返回非null值），则创建并返回Token
            if (tokenValue != null) {
                return new CommonToken(commonTokenParser.tokenType(), tokenValue, currentLine, currentColumn);
            }
        }

        // 如果没有任何解析器能解析，抛出异常
        throw new IllegalStateException("Invalid token at line " + currentLine + ", column " + currentColumn + " with '" + Character.toString(codePointLoader.nextCodePoint()) + "'");

    }
}

