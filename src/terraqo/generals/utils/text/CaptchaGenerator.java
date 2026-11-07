/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.text;


import com.github.potatoxf.generals.symbolic.Generator;

import java.io.IOException;

/**
 * 抽象验证码产生器
 *
 * @author potatoxf
 */
public interface CaptchaGenerator extends Generator<TextImageData, IOException> {
    /**
     * 创建一个验证码
     *
     * @return {@link  TextImageData}
     * @throws IOException 抛出异常
     */
    @Override
    TextImageData generate() throws IOException;
}
