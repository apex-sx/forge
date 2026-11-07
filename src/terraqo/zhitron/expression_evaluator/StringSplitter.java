package com.github.zhitron.expression_evaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhitron
 */
public abstract class StringSplitter<T> {

    private static final int EOF = -1;
    private boolean isThrowEmptyTokenException = false;
    private int sc = 0, ec = 0, dc = 0;
    private int si = 0, i = 0;
    private char ch;
    private int cp;

    private void reset() {
        sc = 0;
        ec = 0;
        dc = 0;
        si = 0;
        dc = 0;
    }

    protected int currentContextAmount() {
        return dc;
    }

    public final List<T> resolve(String expression) {
        List<T> tokens = new LinkedList<>();
        for (; i < expression.length(); i++) {
            cp = Character.isHighSurrogate(ch = expression.charAt(i)) ? Character.toCodePoint(ch, expression.charAt(++i)) : ch;
            if (Character.isWhitespace(cp)) {
                cp = dc == 0 ? sc++ : ec++;
            } else if (Character.isISOControl(cp)) {
                throw new IllegalArgumentException();
            } else {
                int size = tokens.size();
                if (this.handleSplitter(cp, tokens)) {
                    if (dc != 0) {
                        tokens.add(size,handleToken(expression.substring(si + sc, i - ec)));
                        sc = 0;
                        ec = 0;
                        dc = 0;
                    } else {
                        if (isThrowEmptyTokenException) {
                            throw new IllegalArgumentException();
                        }
                    }
                    si = i + 1;
                } else {
                    if (Character.isHighSurrogate(ch)) dc++;
                    dc += ec + 1;
                    ec = 0;
                }
            }
        }
        return tokens;
    }

    protected abstract boolean handleSplitter(int codepoint, List<T> tokens);

    protected abstract T handleToken(String text);
}
