
/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

import terraqo.lang.ChainManager;

/**
 * 通用令牌解析器管理器
 * 用于管理和注册不同类型的令牌解析器
 *
 * @author Terraqo
 */
public class CommonTokenParserManager extends ChainManager<CommonTokenParser, CommonTokenParserManager> {

    public CommonTokenParserManager() {
        super();
    }

    /**
     * 注册一个实例到管理器中
     *
     * @param commonTokenType 令牌类型，不能为null
     * @param instance        实例对象，不能为null
     * @return 返回当前管理器实例，支持链式调用
     * @throws IllegalArgumentException 如果commonTokenType或instance为null
     */
    public final CommonTokenParserManager registerInstance(CommonTokenType commonTokenType, CommonTokenParser instance) {
        return this.registerInstance(commonTokenType.toString(), instance);
    }
}
