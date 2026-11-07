
/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

/**
 * 词元解析器接口，定义了解析特定类型词元的方法
 *
 * @author Terraqo
 */
public interface CommonTokenParser {

    /**
     * 获取此解析器对应的词元类型
     *
     * @return 词元类型
     */
    CommonTokenType tokenType();

    /**
     * 从字符加载器中解析出词元字符串
     *
     * @param codePointLoader 字符加载器，提供源码字符流
     * @return 解析得到的词元字符串
     */
    String parseToken(CodePointLoader codePointLoader);
}
