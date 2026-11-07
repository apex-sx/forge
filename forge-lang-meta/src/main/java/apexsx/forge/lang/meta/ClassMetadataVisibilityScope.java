package apexsx.forge.lang.meta;

/**
 * 定义类成员的可见性作用域
 *
 * @author zhitron
 */
public enum ClassMetadataVisibilityScope {
    /**
     * 当前类直接声明的所有成员，包括private、protected、package-private和public
     */
    DECLARED,

    /**
     * 从父类继承而来的成员，包括protected和public成员以及同包下的package-private成员
     */
    INHERITED,

    /**
     * 当前类中声明的public成员
     */
    PUBLIC_DECLARED,

    /**
     * 从父类继承的public成员
     */
    PUBLIC_INHERITED,
}
