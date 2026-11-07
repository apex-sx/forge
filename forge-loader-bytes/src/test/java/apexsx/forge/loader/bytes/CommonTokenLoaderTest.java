/*
 * Copyright (c) 2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Terraqo
 */
public class CommonTokenLoaderTest {
    CommonTokenParserManager commonTokenParserManager;

    @BeforeEach
    void setUp() {
        commonTokenParserManager = new CommonTokenParserManager();
        commonTokenParserManager.registerInstance(CommonTokenType.SYMBOL, new CommonTokenParser() {
            @Override
            public CommonTokenType tokenType() {
                return CommonTokenType.SYMBOL;
            }

            @Override
            public String parseToken(CodePointLoader codePointLoader) {
                int codePoint = codePointLoader.peekCodePoint();
                if (codePoint == '{' || codePoint == '}' || codePoint == '[' || codePoint == ']' || codePoint == ':' || codePoint == ',') {
                    codePointLoader.popCodePoint();
                    return Character.toString(codePoint);
                }
                return null;
            }
        });
        commonTokenParserManager.registerInstance(CommonTokenType.WHITESPACE.toString(), new CommonTokenParser() {
            @Override
            public CommonTokenType tokenType() {
                return CommonTokenType.WHITESPACE;
            }

            @Override
            public String parseToken(CodePointLoader codePointLoader) {
                if (Character.isWhitespace(codePointLoader.peekCodePoint())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    while (Character.isWhitespace(codePointLoader.peekCodePoint())) {
                        stringBuilder.appendCodePoint(codePointLoader.popCodePoint());
                    }
                    return stringBuilder.toString();
                }
                return null;
            }
        });
        commonTokenParserManager.registerInstance(CommonTokenType.STRING.toString(), new CommonTokenParser() {
            @Override
            public CommonTokenType tokenType() {
                return CommonTokenType.STRING;
            }

            @Override

            public String parseToken(CodePointLoader codePointLoader) {
                int codePoint = codePointLoader.peekCodePoint();
                if (codePoint == '"') {
                    StringBuilder stringBuilder = new StringBuilder();
                    // 添加开头的引号
                    stringBuilder.appendCodePoint(codePointLoader.popCodePoint());

                    // 处理字符串内容直到遇到结束引号
                    while (codePointLoader.hasNext() && (codePoint = codePointLoader.peekCodePoint()) != '"') {
                        // 检查是否为转义字符
                        if (codePoint == '\\') {
                            stringBuilder.appendCodePoint(codePointLoader.popCodePoint()); // 添加反斜杠
                            if (codePointLoader.hasNext()) {
                                stringBuilder.appendCodePoint(codePointLoader.popCodePoint()); // 添加转义后的字符
                            }
                        } else {
                            stringBuilder.appendCodePoint(codePointLoader.popCodePoint()); // 添加普通字符
                        }
                    }

                    // 如果还有引号，则添加结尾的引号
                    if (codePointLoader.hasNext() && codePointLoader.peekCodePoint() == '"') {
                        stringBuilder.appendCodePoint(codePointLoader.popCodePoint());
                    }

                    return stringBuilder.toString();
                }
                return null;
            }
        });
        commonTokenParserManager.registerInstance(CommonTokenType.TEXT.toString(), new CommonTokenParser() {
            @Override
            public CommonTokenType tokenType() {
                return CommonTokenType.TEXT;
            }

            @Override
            public String parseToken(CodePointLoader codePointLoader) {
                int codePoint = codePointLoader.peekCodePoint();
                if (codePoint == '{' || codePoint == '}' || codePoint == '[' || codePoint == ']' || codePoint == ':' || codePoint == ',') {
                    return null;
                }
                if (Character.isWhitespace(codePoint)) {
                    return null;
                }
                if (codePoint == '"') {
                    return null;
                }
                StringBuilder stringBuilder = new StringBuilder();
                do {
                    stringBuilder.appendCodePoint(codePointLoader.popCodePoint());
                } while (codePointLoader.hasNext() && !Character.isWhitespace(codePoint = codePointLoader.peekCodePoint()) && codePoint != '{' && codePoint != '}' && codePoint != '[' && codePoint != ']' && codePoint != ':' && codePoint != ',');
                return stringBuilder.toString();
            }
        });
    }

    @Test
    @DisplayName("测试 hasNext 方法")
    public void test() {
        // 假设已有CodePointLoader实例
        CodePointLoader codePointLoader = CodePointLoaderFactory.of("{\"name\":\"test\",\"value\":123}");
        CommonTokenLoader commonTokenLoader = new CommonTokenLoader(codePointLoader, commonTokenParserManager);
        // 流式处理tokens
        while (commonTokenLoader.hasNext()) {
            CommonToken commonToken = commonTokenLoader.next();
            System.out.println(commonToken);
            System.out.println();
        }
    }
}
