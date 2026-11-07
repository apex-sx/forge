package com.github.zhitron.lite_el.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhitron
 */ // region === Lexer ===
public class Lexer {
    private final String source;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.MULTIPLY);
                break;
            case '/':
                addToken(TokenType.DIVIDING);
                break;
            case '%':
                addToken(TokenType.MODULO);
                break;
            case '^':
                addToken(TokenType.POWER);
                break;
            case '(':
                addToken(TokenType.LPAREN);
                break;
            case ')':
                addToken(TokenType.RPAREN);
                break;
            case '[':
                addToken(TokenType.LBRACKET);
                break;
            case ']':
                addToken(TokenType.RBRACKET);
                break;
            case '{':
            case '}':
                throw error("Unsupported character: " + c);
            case '.':
                addToken(TokenType.DOT);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '!':
                addToken(match('=') ? TokenType.NOT_EQUAL : TokenType.NOT);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_THEN_OR_EQUAL : TokenType.LESS_THEN);
                break;
            case '>':
                addToken(match('=') ? TokenType.MORE_THEN_OR_EQUAL : TokenType.MORE_THEN);
                break;
            case '&':
                if (match('&')) addToken(TokenType.AND);
                else throw error("Expected &&");
                break;
            case '|':
                if (match('|')) addToken(TokenType.OR);
                else throw error("Expected ||");
                break;
            case '?':
                addToken(TokenType.QUESTION);
                break;
            case '"':
                string('"');
                break;
            case '\'':
                string('\'');
                break;
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                line += (c == '\n') ? 1 : 0;
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw error("Unexpected character: " + c);
                }
                break;
        }
    }

    private void string(char c) {
        while (peek() != c && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) throw error("Unterminated string.");
        advance(); // consume "
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void number() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(TokenType.NUMBER, source.substring(start, current));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type;
        switch (text) {
            case "true":
                type = TokenType.TRUE;
                break;
            case "false":
                type = TokenType.FALSE;
                break;
            case "null":
                type = TokenType.NULL;
                break;
            default:
                type = TokenType.IDENTIFIER;
                break;
        }
        addToken(type, text);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, String literal) {
        String text = literal != null ? literal : source.substring(start, current);
        tokens.add(new Token(type, text, line));
    }

    private RuntimeException error(String message) {
        System.err.println("[line " + line + "] Error: " + message);
        return new IllegalArgumentException(message);
    }
}
