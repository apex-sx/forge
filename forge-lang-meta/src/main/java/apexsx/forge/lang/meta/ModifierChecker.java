package apexsx.forge.lang.meta;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.function.IntPredicate;

/**
 * 修饰符检查工具类
 * <p>
 * 提供对Java成员（字段、方法、构造函数）的各种修饰符检查功能，
 * 包括访问权限（public、private、protected）、特性修饰符（static、final等）
 * 以及组合修饰符的检查方法。
 *
 * @author zhitron
 */
public class ModifierChecker {
    /**
     * 私有构造函数，防止外部实例化
     */
    protected ModifierChecker() {
    }

    /**
     * 判断给定的成员是否为public访问权限
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public访问权限则返回true，否则返回false
     */
    public static boolean isPublic(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为private访问权限
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private访问权限则返回true，否则返回false
     */
    public static boolean isPrivate(Member member) {
        return member != null && Modifier.isPrivate(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为protected访问权限
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected访问权限则返回true，否则返回false
     */
    public static boolean isProtected(Member member) {
        return member != null && Modifier.isProtected(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为static修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有static修饰则返回true，否则返回false
     */
    public static boolean isStatic(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为final修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有final修饰则返回true，否则返回false
     */
    public static boolean isFinal(Member member) {
        return member != null && Modifier.isFinal(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为synchronized修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有synchronized修饰则返回true，否则返回false
     */
    public static boolean isSynchronized(Member member) {
        return member != null && Modifier.isSynchronized(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为volatile修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有volatile修饰则返回true，否则返回false
     */
    public static boolean isVolatile(Member member) {
        return member != null && Modifier.isVolatile(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为transient修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有transient修饰则返回true，否则返回false
     */
    public static boolean isTransient(Member member) {
        return member != null && Modifier.isTransient(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为native修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有native修饰则返回true，否则返回false
     */
    public static boolean isNative(Member member) {
        return member != null && Modifier.isNative(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为interface
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且是interface则返回true，否则返回false
     */
    public static boolean isInterface(Member member) {
        return member != null && Modifier.isInterface(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为abstract修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有abstract修饰则返回true，否则返回false
     */
    public static boolean isAbstract(Member member) {
        return member != null && Modifier.isAbstract(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为strictfp修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有strictfp修饰则返回true，否则返回false
     */
    public static boolean isStrict(Member member) {
        return member != null && Modifier.isStrict(member.getModifiers());
    }

    /**
     * 判断给定的成员是否为synthetic修饰
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有synthetic修饰则返回true，否则返回false
     */
    public static boolean isSynthetic(Member member) {
        return member != null && member.isSynthetic();
    }

    // 常见组合修饰符判断
    private static boolean isMatchModifier(Member member, IntPredicate... modifierPredicates) {
        if (member == null) return false;
        int modifiers = member.getModifiers();
        for (IntPredicate modifierPredicate : modifierPredicates) {
            if (!modifierPredicate.test(modifiers)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定的成员是否为public abstract
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public abstract修饰则返回true，否则返回false
     */
    public static boolean isPublicAbstract(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPublic, Modifier::isAbstract);
    }

    /**
     * 判断给定的成员是否为public interface
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public interface修饰则返回true，否则返回false
     */
    public static boolean isPublicInterface(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPublic, Modifier::isInterface);
    }

    /**
     * 判断给定的成员是否为public static
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public static修饰则返回true，否则返回false
     */
    public static boolean isPublicStatic(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPublic, Modifier::isStatic);
    }

    /**
     * 判断给定的成员是否为public static final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public static final修饰则返回true，否则返回false
     */
    public static boolean isPublicStaticFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPublic, Modifier::isStatic, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为public final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有public final修饰则返回true，否则返回false
     */
    public static boolean isPublicFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPublic, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为private abstract
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private abstract修饰则返回true，否则返回false
     */
    public static boolean isPrivateAbstract(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPrivate, Modifier::isAbstract);
    }

    /**
     * 判断给定的成员是否为private interface
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private interface修饰则返回true，否则返回false
     */
    public static boolean isPrivateInterface(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPrivate, Modifier::isInterface);
    }

    /**
     * 判断给定的成员是否为private static
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private static修饰则返回true，否则返回false
     */
    public static boolean isPrivateStatic(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPrivate, Modifier::isStatic);
    }

    /**
     * 判断给定的成员是否为private static final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private static final修饰则返回true，否则返回false
     */
    public static boolean isPrivateStaticFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPrivate, Modifier::isStatic, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为private final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有private final修饰则返回true，否则返回false
     */
    public static boolean isPrivateFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isPrivate, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为protected abstract
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected abstract修饰则返回true，否则返回false
     */
    public static boolean isProtectedAbstract(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isProtected, Modifier::isAbstract);
    }

    /**
     * 判断给定的成员是否为protected interface
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected interface修饰则返回true，否则返回false
     */
    public static boolean isProtectedInterface(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isProtected, Modifier::isInterface);
    }

    /**
     * 判断给定的成员是否为protected static
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected static修饰则返回true，否则返回false
     */
    public static boolean isProtectedStatic(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isProtected, Modifier::isStatic);
    }

    /**
     * 判断给定的成员是否为protected static final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected static final修饰则返回true，否则返回false
     */
    public static boolean isProtectedStaticFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isProtected, Modifier::isStatic, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为protected final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有protected final修饰则返回true，否则返回false
     */
    public static boolean isProtectedFinal(Member member) {
        return ModifierChecker.isMatchModifier(member, Modifier::isProtected, Modifier::isFinal);
    }

    /**
     * 判断给定的成员是否为static final
     *
     * @param member 要检查的成员对象，可以是字段、方法或构造函数
     * @return 如果member不为null且具有static final修饰则返回true，否则返回false
     */
    public static boolean isStaticFinal(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers()) && Modifier.isFinal(member.getModifiers());
    }
}
