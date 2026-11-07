/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.text;

import java.util.Random;

/**
 * 验证码文本生成器
 *
 * @author potatoxf
 */
public class TextGeneratorOfArithmetic implements TextGenerator {
    private final int max;

    public TextGeneratorOfArithmetic() {
        this(15);
    }

    public TextGeneratorOfArithmetic(int max) {
        this.max = Math.max(max, 10);
    }

    @Override
    public String getText() {
        int result;
        Random random = new Random();
        int x = random.nextInt(max);
        String xs = String.valueOf(x);
        int y = random.nextInt(max);
        String ys = String.valueOf(y);
        StringBuilder sb = new StringBuilder();
        int type = (int) Math.round(Math.random() * 2);
        if (type == 0) {
            result = x * y;
            sb.append(xs).append("*").append(ys);
        } else if (type == 1) {
            if (!(x == 0) && y % x == 0) {
                result = y / x;
                sb.append(ys).append("/").append(xs);
            } else {
                result = x + y;
                sb.append(xs).append("+").append(ys);
            }
        } else if (type == 2) {
            if (x >= y) {
                result = x - y;
                sb.append(xs).append("-").append(ys);
            } else {
                result = y - x;
                sb.append(ys).append("-").append(xs);
            }
        } else {
            result = x + y;
            sb.append(xs).append("+").append(ys);
        }
        sb.append("=?@").append(result);
        return sb.toString();
    }
}
