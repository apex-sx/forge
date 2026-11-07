package com.github.zhitron.expression_evaluator;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhitron
 */
public class TokenSplitter extends StringSplitter<Token> {

    Deque<Character> bracketStack = new LinkedList<>();

    @Override
    protected boolean handleSplitter(int codepoint, List<Token> tokens) {
        switch (codepoint) {
            case ')':
            case ']':
            case '}':
                if (bracketStack.isEmpty()) {
                    throw new IllegalArgumentException();
                }
                switch (bracketStack.pop()) {
                    case '(':
                        if (codepoint != ')') {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case '[':
                        if (codepoint != ']') {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case '{':
                        if (codepoint != '}') {
                            throw new IllegalArgumentException();
                        }
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                tokens.add(new Token(Character.toString(codepoint), TokenType.CLOSING_BRACKET));
                return true;
            case '(':
            case '[':
            case '{':
                tokens.add(new Token(Character.toString(codepoint), TokenType.OPENING_BRACKET));
                bracketStack.push((char) codepoint);
                return true;
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '|':
                tokens.add(new Token(Character.toString(codepoint), TokenType.OPERATOR));
                return true;
            default: {
            }
        }
        return false;
    }

    @Override
    protected Token handleToken(String text) {
        return new Token(text, null);
    }
}
