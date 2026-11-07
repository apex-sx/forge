
/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */

package apexsx.forge.core;

import java.util.Objects;

/**
 * Category类表示一个分类，包含模块(module)和类型(type)两个维度
 *
 * @param module 所属模块的唯一标识符，必须符合规范符号格式
 * @param type   分类的类型唯一标识符，必须符合规范符号格式
 * @author Terraqo
 */
public record TagKey(Class<?> useClass, Symbol module, Symbol type) implements Comparable<TagKey> {

    /**
     * 创建Category实例的工厂方法，验证module和type参数的合法性
     *
     * @param module 所属模块的唯一标识符
     * @param type   分类的类型唯一标识符
     * @return 新创建的Category实例
     * @throws IllegalArgumentException 当module或type不符合规范符号格式时抛出异常
     */
    public static TagKey of(Class<?> useClass, String module, String type) {
        return new TagKey(useClass, Symbol.of(module), Symbol.of(type));
    }

    /**
     * 获取资源基础路径
     * 构造一个基于类名的路径字符串，用于定位META-INF目录下的资源
     *
     * @return 返回格式为 "META-INF/类名路径" 的路径字符串
     * 其中类名中的点号会被替换为斜杠，形成目录结构
     */
    public String getResourceBasePath() {
        return "META-INF/" + useClass.getName();
    }

    /**
     * 获取模块资源路径
     * 构造一个基于类名和模块名的路径字符串，用于定位META-INF目录下的模块资源
     *
     * @return 返回格式为 "META-INF/类名路径/模块名" 的路径字符串
     * 其中类名中的点号会被替换为斜杠，形成目录结构
     */
    public String getModuleResourcePath() {
        return getResourceBasePath() + '/' + module;
    }

    /**
     * 实现Comparable接口的compareTo方法，按module和type的字典序进行比较
     *
     * @param o 要比较的Category对象
     * @return 比较结果：负数表示当前对象小于参数对象，0表示相等，正数表示大于
     */
    @Override
    public int compareTo(TagKey o) {
        // 首先比较module字段
        int moduleComparison = this.module.compareTo(o.module());
        if (moduleComparison != 0) {
            return moduleComparison;
        }
        // 如果module相等，则比较type字段
        return this.type.compareTo(o.type());
    }

    /**
     * 重写equals方法，判断两个Category对象是否相等
     *
     * @param o 要比较的对象
     * @return 如果两个Category对象的module和type字段都相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof TagKey(Class<?> aClass, Symbol module1, Symbol type1)
                && Objects.equals(useClass, aClass)
                && Objects.equals(module, module1)
                && Objects.equals(type, type1));
    }

    /**
     * 重写hashCode方法，计算Category对象的哈希码
     *
     * @return 基于module和type字段计算的哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(useClass, module, type);
    }

    /**
     * 重写toString方法，返回Category对象的字符串表示
     *
     * @return 格式为"module$type"的字符串
     */
    @Override
    public String toString() {
        return module + "$" + type;
    }
}
