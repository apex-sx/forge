/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.symbolic.This;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralTree {

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<Map<String, Object>> filterTreeDataToMap(Collection<? extends T> data,
                                                                                       Predicate<? super K> rootKeyPredicate,
                                                                                       Predicate<? super T> filterCondition,
                                                                                       BiFunction<T, Set<? super K>, Map<String, Object>> converter) {
        return GeneralTree.filterTreeDataToMap(data, null, null, TreeData<K, T>::getCurrentKey, TreeData<K, T>::getParentKey, rootKeyPredicate, filterCondition, converter);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<Map<String, Object>> filterTreeDataToMap(Collection<? extends T> data,
                                                                                       Collection<? extends K> includeKey,
                                                                                       Collection<? extends T> includeDatum,
                                                                                       Predicate<? super K> rootKeyPredicate,
                                                                                       Predicate<? super T> filterCondition,
                                                                                       BiFunction<T, Set<? super K>, Map<String, Object>> converter) {
        return GeneralTree.filterTreeData(data, includeKey, includeDatum, TreeData::getCurrentKey, TreeData::getParentKey, rootKeyPredicate, filterCondition, converter);
    }
    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param keyGetter        键获取器
     * @param parentKeyGetter  父级键获取器
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T> List<Map<String, Object>> filterTreeDataToMap(Collection<? extends T> data,
                                                                Function<T, ? extends K> keyGetter,
                                                                Function<T, ? extends K> parentKeyGetter,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition,
                                                                BiFunction<T, Set<? super K>, Map<String, Object>> converter) {
        return GeneralTree.filterTreeDataToMap(data, null, null, keyGetter, parentKeyGetter, rootKeyPredicate, filterCondition, converter);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param keyGetter        键获取器
     * @param parentKeyGetter  父级键获取器
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T> List<Map<String, Object>> filterTreeDataToMap(Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Function<T, ? extends K> keyGetter,
                                                                Function<T, ? extends K> parentKeyGetter,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition,
                                                                BiFunction<T, Set<? super K>, Map<String, Object>> converter) {
        return GeneralTree.filterTreeData(data, includeKey, includeDatum, keyGetter, parentKeyGetter, rootKeyPredicate, filterCondition, converter);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(Collection<? extends T> data,
                                                                Predicate<? super K> rootKeyPredicate) {
        return GeneralTree.filterTreeData(false, data, null, null, rootKeyPredicate, null, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(Collection<? extends T> data,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(false, data, null, null, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate) {
        return GeneralTree.filterTreeData(false, data, includeKey, includeDatum, rootKeyPredicate, null, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(false, data, includeKey, includeDatum, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition,
                                                                BiFunction<? super T, Set<? super K>, ? extends T> converter) {
        return GeneralTree.filterTreeData(false, data, includeKey, includeDatum, rootKeyPredicate, filterCondition, converter);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param tree             是否构建树型结构
     * @param data             所有数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(boolean tree, Collection<? extends T> data,
                                                                Predicate<? super K> rootKeyPredicate) {
        return GeneralTree.filterTreeData(tree, data, null, null, rootKeyPredicate, null, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param tree             是否构建树型结构
     * @param data             所有数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(boolean tree, Collection<? extends T> data,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(tree, data, null, null, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param tree             是否构建树型结构
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(boolean tree, Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate) {
        return GeneralTree.filterTreeData(tree, data, includeKey, includeDatum, rootKeyPredicate, null, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param tree             是否构建树型结构
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(boolean tree, Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(tree, data, includeKey, includeDatum, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param tree             是否构建树型结构
     * @param data             所有数据
     * @param includeKey       指定包含key
     * @param includeDatum     指定包含数据
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @param converter        数据转换器
     * @return 返回过滤后的数据
     */
    static <K, T extends TreeData<K, T>> List<T> filterTreeData(boolean tree, Collection<? extends T> data,
                                                                Collection<? extends K> includeKey,
                                                                Collection<? extends T> includeDatum,
                                                                Predicate<? super K> rootKeyPredicate,
                                                                Predicate<? super T> filterCondition,
                                                                BiFunction<? super T, Set<? super K>, ? extends T> converter) {
        final List<T> list = GeneralTree.filterTreeData(data, includeKey, includeDatum,
                TreeData<K, T>::getCurrentKey, TreeData<K, T>::getParentKey, rootKeyPredicate, filterCondition, converter);
        if (!tree || list.isEmpty()) return list;
        final Set<K> rootKeySet = new HashSet<>();
        final List<T> result = new ArrayList<>();
        if (rootKeyPredicate != null) {
            for (T datum : list) {
                K parentKey = datum.getParentKey();
                if (rootKeyPredicate.test(parentKey)) {
                    result.add(datum);
                    rootKeySet.add(datum.getCurrentKey());
                }
            }
        }
        if (rootKeySet.isEmpty()) {
            for (T datum : list) {
                K key = datum.getParentKey();
                if (key == null) {
                    rootKeySet.add(null);
                }
            }
        }
        if (!rootKeySet.isEmpty()) {
            final Map<K, List<T>> childrenMap = list.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(TreeData<K, T>::getParentKey, HashMap::new, Collectors.toList()));
            final Queue<T> queue = new LinkedList<>(result);

            while (!queue.isEmpty()) {
                T parent = queue.poll();
                K key = parent.getCurrentKey();
                // 找到当前孩子
                List<T> children = childrenMap.get(key);
                if (children != null && !children.isEmpty()) {
                    parent.children(children);
                    children.forEach(e -> e.parent(parent));
                    queue.addAll(children);
                }

            }
        }
        return result;
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param keyGetter        键获取器
     * @param parentKeyGetter  父级键获取器
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @return 返回过滤后的数据
     */
    static <K, T> List<T> filterTreeData(Collection<? extends T> data,
                                         Function<T, ? extends K> keyGetter,
                                         Function<T, ? extends K> parentKeyGetter,
                                         Predicate<? super K> rootKeyPredicate) {
        return GeneralTree.filterTreeData(data, null, null, keyGetter, parentKeyGetter, rootKeyPredicate, null, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data             所有数据
     * @param keyGetter        键获取器
     * @param parentKeyGetter  父级键获取器
     * @param rootKeyPredicate 判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition  测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T> List<T> filterTreeData(Collection<? extends T> data,
                                         Function<T, ? extends K> keyGetter,
                                         Function<T, ? extends K> parentKeyGetter,
                                         Predicate<? super K> rootKeyPredicate,
                                         Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(data, null, null, keyGetter, parentKeyGetter, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data            所有数据
     * @param includeKey    指定包含key
     * @param includeDatum    指定包含数据
     * @param keyGetter       键获取器
     * @param parentKeyGetter 父级键获取器
     * @param rootKeyPredicate          判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition          测试数据是否保留，如果true则保留
     * @return 返回过滤后的数据
     */
    static <K, T> List<T> filterTreeData(Collection<? extends T> data,
                                         Collection<? extends K> includeKey,
                                         Collection<? extends T> includeDatum,
                                         Function<T, ? extends K> keyGetter,
                                         Function<T, ? extends K> parentKeyGetter,
                                         Predicate<? super K> rootKeyPredicate,
                                         Predicate<? super T> filterCondition) {
        return GeneralTree.filterTreeData(data, includeKey, includeDatum, keyGetter, parentKeyGetter, rootKeyPredicate, filterCondition, (v, s) -> v);
    }

    /**
     * 过滤树结构数据，当叶子节点保留则其父节点以上无条件保留，以保证树结构的完好
     *
     * @param data            所有数据
     * @param includeKey    指定包含key
     * @param includeDatum    指定包含数据
     * @param keyGetter       键获取器
     * @param parentKeyGetter 父级键获取器
     * @param rootKeyPredicate          判断根key判断器，如果是根返回true，否则返回false
     * @param filterCondition          测试数据是否保留，如果true则保留
     * @param converter       数据转换器
     * @return 返回过滤后的数据
     */
    static <K, E, T> List<T> filterTreeData(Collection<? extends E> data,
                                            Collection<? extends K> includeKey,
                                            Collection<? extends E> includeDatum,
                                            Function<E, ? extends K> keyGetter,
                                            Function<E, ? extends K> parentKeyGetter,
                                            Predicate<? super K> rootKeyPredicate,
                                            Predicate<? super E> filterCondition,
                                            BiFunction<? super E, Set<? super K>, ? extends T> converter) {
        //1、过滤有效数据
        final Map<? super K, E> dataMap = data.stream().filter(Objects::nonNull).filter(e -> Objects.nonNull(keyGetter.apply(e)))
                .collect(Collectors.groupingBy(keyGetter, Collectors.collectingAndThen(Collectors.toList(), lst -> lst.get(0))));
        //2、需要包括数据key
        final Set<? super K> includeSet = new HashSet<>();
        if (GeneralIF.isNoEmpty(includeKey)) {
            includeSet.addAll(includeKey);
        }
        if (GeneralIF.isNoEmpty(includeDatum)) {
            for (E datum : includeDatum) {
                if (datum == null) continue;
                K key = keyGetter.apply(datum);
                if (key == null) continue;
                includeSet.add(key);
            }
        }

        final Predicate<E> test = e -> includeSet.contains(keyGetter.apply(e)) || (filterCondition != null && filterCondition.test(e));
        final LinkedList<E> queue = new LinkedList<>(data);
        final Map<? super K, E> exists = new HashMap<>();
        final List<T> result = new ArrayList<>(data.size());
        boolean isParent = false;
        while (!queue.isEmpty()) {
            E e = queue.pollFirst();
            //不是在父级链上并且不符合条件则跳过
            if (!isParent && !test.test(e)) continue;
            K key = keyGetter.apply(e);
            //已存在则跳过
            if (exists.containsKey(key)) {
                isParent = false;
                continue;
            }
            //获得当前数据
            T t = converter.apply(e, includeSet);
            //未返回正确数据，归位状态并跳过
            if (t == null) {
                isParent = false;
                continue;
            }
            exists.put(key, e);
            result.add(t);
            //获得当前数据的父级key
            key = parentKeyGetter.apply(e);
            //不存在父级则跳过
            if (key == null || (rootKeyPredicate != null && rootKeyPredicate.test(key))) {
                isParent = false;
                continue;
            }
            //获取父级实体
            e = dataMap.get(key);
            //不存在父级则跳过
            if (e == null) {
                isParent = false;
                continue;
            }
            //优先遍历父级链
            queue.addFirst(e);
            isParent = true;
        }
        return result;
    }

    /**
     * 树形表格行
     *
     * @param <K>
     * @param <TD>
     */
    interface TreeData<K, TD extends TreeData<K, TD>> extends This<TD> {

        /**
         * 获取当前Key
         *
         * @return {@code Key}
         */
        K getCurrentKey();

        /**
         * 获取当前Key
         *
         * @param key key
         * @return {@code Key}
         */
        TD setCurrentKey(K key);

        /**
         * 获取当前父级Key
         *
         * @return {@code Key}
         */
        K getParentKey();

        /**
         * 获取当前Key
         *
         * @param key 父级key
         * @return {@code Key}
         */
        TD setParentKey(K key);

        /**
         * 获取孩子节点引用列表
         *
         * @return 孩子节点引用
         */
        default List<TD> children() {
            return null;
        }

        /**
         * 设置孩子节点引用列表
         *
         * @param children 孩子节点引用
         */
        default void children(List<TD> children) {
        }

        /**
         * 获取父节点引用
         *
         * @return 父节点引用
         */
        default TD parent() {
            return null;
        }

        /**
         * 设置父节点引用
         *
         * @param parent 父节点引用
         */
        default TD parent(TD parent) {
            return this$();
        }
    }
}
