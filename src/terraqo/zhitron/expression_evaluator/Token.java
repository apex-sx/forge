package com.github.zhitron.expression_evaluator;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhitron
 */
public record Token(String value, TokenType type) {


    // TODO: 表达式 5+6.5a+(100-c)*6.67/symbol+setting.handle('name',10*symbol)+10^2
    //解析token序列结果
    //  5 + 6.5 * a + (100 - c) * 6.67 / symbol + setting.handle ( "name" ,  10 * symbol ) + 10 ^ 2

    public static void main(String[] args) {
        String expression = "5+6.5a+(100-c)*6.67/symbol+setting.handle('name',10*symbol)+10^2";
        TokenSplitter tokenSplitter = new TokenSplitter();
        List<Token> tokens = tokenSplitter.resolve(expression);
        for (Token token : tokens) {
            System.out.println(token.value);
        }
    }
}
