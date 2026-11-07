/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public interface GeneralAnnotation {

    /**
     * 获取注解的可重复注解Class
     *
     * @param annotationClass 注解类型
     * @return 返回该注解类型的可重复注解类型
     */
    static Class<? extends Annotation> getAnnotatedRepeatableType(Class<? extends Annotation> annotationClass) {
        Class<? extends Annotation> repeatableAnnotationType = null;
        Repeatable repeatable = annotationClass.getDeclaredAnnotation(Repeatable.class);
        if (repeatable != null) repeatableAnnotationType = repeatable.value();
        return repeatableAnnotationType;
    }

    /**
     * 在指定元素寻找指定注解
     *
     * @param annotatedElement 可被注解的元素
     * @param annotationClass    注解类型
     * @return 返回注解类型实例列表
     */
    static <A extends Annotation> List<A> lookupAnnotation(AnnotatedElement annotatedElement, Class<A> annotationClass) {
        Class<? extends Annotation> repeatableAnnotationType = GeneralAnnotation.getAnnotatedRepeatableType(annotationClass);

        Set<Annotation> argList = new LinkedHashSet<>();
        Collections.addAll(argList, annotatedElement.getDeclaredAnnotationsByType(annotationClass));

        LinkedList<Annotation> stack = new LinkedList<>();
        Collections.addAll(stack, annotatedElement.getDeclaredAnnotations());

        while (!stack.isEmpty()) {
            Annotation annotation = stack.pollFirst();
            if (argList.contains(annotation)) continue;
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (repeatableAnnotationType != null && repeatableAnnotationType == annotationType) continue;
            if (annotationType.getTypeName().startsWith("java.lang.annotation")) continue;

            Collections.addAll(argList, annotationType.getDeclaredAnnotationsByType(annotationType));
            Collections.addAll(stack, annotationType.getDeclaredAnnotations());
        }
        return GeneralCollection.copyElementAndCastToList(argList, ArrayList::new);
    }
}
