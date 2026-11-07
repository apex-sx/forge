package com.github.zhitron.lite_el.expression;

import com.github.zhitron.lite_el.operator.OperatorType;

/**
 * @author zhitron
 */
class Token {
    final TokenType tokenType;
    final OperatorType operatorType;
    final String lexeme;
    final int line;

    Token(TokenType tokenType, OperatorType operatorType, String lexeme, int line) {
        this.tokenType = tokenType;
        this.operatorType = operatorType;
        this.lexeme = lexeme;
        this.line = line;
    }

    @Override
    public String toString() {
        return lexeme + "(" + tokenType + ")";
    }
}
