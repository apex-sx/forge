package apexsx.forge.core.cc;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Terraqo
 */
public class ClassInheritanceHelper {

    /**
     * 获取从指定子类到指定父类（或接口）的完整继承链。
     * <p>
     * 该方法会遍历类的继承体系，收集从 {@code input} 开始，直到 {@code superclass} 结束的所有类或接口。
     * 返回的链表按“从近到远”的顺序排列，即链表头部是 {@code input} 本身，尾部是 {@code superclass}。
     * </p>
     * <p>
     * 处理逻辑说明：
     * <ul>
     *     <li>如果 {@code superclass} 为 {@code null}，则默认目标为 {@link Object#getClass()}。</li>
     *     <li>如果 {@code superclass} 是一个接口：
     *         <ul>
     *             <li>方法会优先在当前类的接口列表中查找能赋值给目标接口的接口，并沿接口继承树向上追溯。</li>
     *             <li>如果在当前层级未找到合适的接口继承路径，且当前类不是接口，则尝试切换到其父类继续查找。</li>
     *             <li>包含防死循环机制，确保在复杂的接口继承结构中安全退出。</li>
     *         </ul>
     *     </li>
     *     <li>如果 {@code superclass} 是一个普通类：
     *         <ul>
     *             <li>方法会通过 {@link Class#getSuperclass()} 逐级向上追溯，直到命中目标类。</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param input      起始子类。必须是有效的类对象，不能为 {@code null}。
     * @param superclass 目标父类或父接口。如果传入 {@code null}，则默认视为 {@link Object#getClass()}。
     * @return 一个 {@link LinkedList}，包含从 {@code input} 到 {@code superclass} 路径上的所有类对象。
     * 列表顺序为：[input, ..., intermediate classes/interfaces, superclass]。
     * @throws IllegalArgumentException 在以下情况抛出：
     *                                  <ul>
     *                                      <li>{@code input} 为 {@code null}。</li>
     *                                      <li>{@code superclass} 不为 {@code null} 但不是 {@code input} 的父类或父接口（即 {@code superclass.isAssignableFrom(input)} 为 false）。</li>
     *                                  </ul>
     */
    public static LinkedList<Class<?>> getInheritanceClassChain(Class<?> input, Class<?> superclass) {
        // 检查子类是否为空，这是必须的输入参数
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }

        // 处理父类为空的情况：默认继承链终点为 Object 类
        if (superclass == null) {
            superclass = Object.class;
        }
        // 检查继承关系的合法性：确保 superclass 确实是 input 的父类或父接口
        else if (!superclass.isAssignableFrom(input)) {
            throw new IllegalArgumentException("superclass is not assignable from input");
        }

        // 初始化结果容器，使用 LinkedList 以便在头部高效插入元素
        LinkedList<Class<?>> classes = new LinkedList<>();

        // --- 核心逻辑：根据目标类型是接口还是类，采用不同的遍历策略 ---

        if (superclass.isInterface()) {
            // 【场景 A】目标是接口：需要处理复杂的接口继承网络
            // 接口继承可能涉及多条路径（类实现的接口、接口继承的接口），需深度优先搜索

            boolean found = false;      // 标记是否已经找到了通往目标接口的有效路径
            boolean foundNext;          // 标记在当前层级是否找到了下一个可跳转的接口

            // 从子类开始循环，只要当前类还能赋值给目标接口，就继续尝试向上追溯
            for (Class<?> cur = input; superclass.isAssignableFrom(cur); ) {
                // 将当前类/接口加入结果集头部（保持从近到远的顺序）
                classes.addFirst(cur);

                foundNext = false;

                // 遍历当前类直接实现的所有接口
                for (Class<?> c : cur.getInterfaces()) {
                    // 如果某个接口能赋值给目标接口，说明找到了向上的路径
                    if (superclass.isAssignableFrom(c)) {
                        cur = c;                // 移动到该接口
                        foundNext = true;       // 标记找到了下一步
                        found = true;           // 标记已进入有效路径
                        break;                  // 跳出接口循环，继续外层循环处理新的 cur
                    }
                }

                // 如果在当前类的接口中没有找到合适的下一步
                if (!foundNext) {
                    // 情况 1：尚未找到有效路径 且 当前是一个普通类（非接口）
                    // 说明需要先上升到父类，再从父类实现的接口中查找
                    if (!found && !cur.isInterface()) {
                        cur = cur.getSuperclass();
                    }
                    // 情况 2：其他情况（已找到路径但断头，或当前是接口但无父接口匹配）
                    // 此时应停止遍历，避免无限循环或错误路径
                    else {
                        break;
                    }
                }
            }
        } else {
            // 【场景 B】目标是普通类：继承链是线性的，直接通过 getSuperclass() 向上追溯

            // 从子类开始，逐级获取父类，直到遇到目标 superclass 为止
            for (Class<?> cur = input; cur != superclass; cur = cur.getSuperclass()) {
                // 将当前类加入结果集头部
                classes.addFirst(cur);

                // 防御性检查：虽然前面的 isAssignableFrom 检查过，但防止极端情况下 getSuperclass() 返回 null 导致死循环
                if (cur.getSuperclass() == null) {
                    break;
                }
            }

            // 循环结束后，cur 等于 superclass，需要将其显式添加到链表头部
            classes.addFirst(superclass);
        }

        return classes;
    }

    /**
     * 查找指定类在其超类或接口中泛型参数的实际类型。
     *
     * @param input      实际类（例如：CB.class）
     * @param superclass 目标超类或接口（例如：IA.class 或 CA.class）
     * @param index      泛型参数在目标超类/接口中的索引（从 0 开始）
     * @param <T>        返回类型的泛型约束
     * @return 解析后的泛型类型对应的 Class 对象，如果无法解析则返回 null
     */
    public static <T> Class<T> getInheritanceGenericClass(Class<?> input, Class<?> superclass, int index) {
        // 检查父类是否是子类的超类
        if (!superclass.isAssignableFrom(input)) {
            throw new IllegalArgumentException("The class [" + superclass + "] is not a superclass of [" + input + "]");
        }
        // 获取父类的泛型参数
        TypeVariable<? extends Class<?>>[] typeParameters = superclass.getTypeParameters();
        if (typeParameters.length == 0 || index >= typeParameters.length) {
            throw new IllegalArgumentException("The index [" + index + "] exceeds generic type list length [" + typeParameters.length + "]");
        }
        Class<?> foundType = Object.class;
        int dimensions = 0;
        if (input == superclass) {
            Type[] bounds = typeParameters[index].getBounds();
            if (bounds[0] instanceof Class<?>) {
                foundType = (Class<?>) bounds[0];
            }
        } else {
            // 获取继承链
            LinkedList<Class<?>> classes = ClassInheritanceHelper.getInheritanceClassChain(input, superclass);
            if (superclass != classes.pollFirst()) {
                throw new IllegalArgumentException("The class [" + superclass + "] is not a superclass of [" + input + "]");
            }
            Type[] bounds = null;
            Type targetType = null;
            boolean isInterface = input.isInterface();
            // 从继承链中查找目标类型
            Class<?> parentClass = superclass;
            for (Class<?> currentClass : classes) {
                if (isInterface) {
                    Type[] genericInterfaces = currentClass.getGenericInterfaces();
                    for (Type genericInterface : genericInterfaces) {
                        if (genericInterface instanceof ParameterizedType parameterizedType) {
                            Type rawType = parameterizedType.getRawType();
                            if (rawType == parentClass) {
                                targetType = parameterizedType.getActualTypeArguments()[index];
                                break;
                            }
                        }
                    }
                    if (targetType == null) {
                        throw new IllegalArgumentException("The class [" + superclass + "] is not a superclass of [" + input + "]");
                    }
                } else {
                    targetType = ((ParameterizedType) currentClass.getGenericSuperclass()).getActualTypeArguments()[index];
                }
                // 找到数组类型
                for (; targetType instanceof GenericArrayType; dimensions++) {
                    targetType = ((GenericArrayType) targetType).getGenericComponentType();
                }
                if (targetType instanceof Class<?>) {
                    foundType = (Class<?>) targetType;
                    break;
                }
                if (targetType instanceof TypeVariable) {
                    // 查找同一泛型确定索引位置
                    typeParameters = currentClass.getTypeParameters();
                    for (int i = 0; i < typeParameters.length; i++) {
                        if (typeParameters[i] != targetType) continue;
                        index = i;
                        bounds = typeParameters[i].getBounds();
                        foundType = (Class<?>) (bounds[0] instanceof ParameterizedType ? ((ParameterizedType) bounds[0]).getRawType() : bounds[0]);
                        break;
                    }
                } else if (targetType instanceof ParameterizedType) {
                    // 确定泛型类型，结束查找
                    foundType = (Class<?>) ((ParameterizedType) targetType).getRawType();
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + targetType);
                }
                parentClass = currentClass;
            }
            if (dimensions > 0) {
                try {
                    foundType = Array.newInstance(foundType, new int[dimensions]).getClass();
                } catch (Throwable e) {
                    throw new UnsupportedOperationException(e);
                }
            }
        }
        return (Class<T>) foundType;
    }


    public static void main(String[] args) {
        System.out.println(getInheritanceGenericClass(List.class, Collection.class, 0));
        ParameterizedType genericSuperclass = (ParameterizedType) HashMap.class.getGenericSuperclass();

        System.out.println(genericSuperclass);

        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        TypeVariable<?> typeParameter = HashMap.class.getTypeParameters()[0];
        System.out.println(actualTypeArgument == typeParameter);


        ParameterizedType genericSuperclass1 = (ParameterizedType) TestMap.class.getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass1.getActualTypeArguments();
        for (Type typeArgument : actualTypeArguments) {
            System.out.println(typeArgument);
        }
    }


    public static class TestMap<T extends Number> extends HashMap<T, List<?>> {
    }

}
