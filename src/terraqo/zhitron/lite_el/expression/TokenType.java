package com.github.zhitron.lite_el.expression;

/**
 * @author zhitron
 */ // region === Token Definitions ===
enum TokenType {
    IDENTIFIER,
    NUMBER,
    STRING,
    OPERATOR,

    LPAREN, RPAREN, LBRACKET, RBRACKET, DOT, COMMA,
    COLON, QUESTION, COLON_ELSE, // ?: support
    TRUE, FALSE, NULL,
    EOF
}
