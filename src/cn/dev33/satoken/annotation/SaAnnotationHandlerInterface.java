/*
 * Copyright 2020-2099 sa-token.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dev33.satoken.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 所有注解处理器的父接口
 *
 * @author click33
 * @since 2024/8/2
 */
public interface SaAnnotationHandlerInterface {

    /**
     * 获取所要处理的注解类型
     * @return /
     */
    Class<? extends Annotation> getHandlerAnnotationClass();

    /**
     * 所需要执行的校验方法
     * @param annotation 注解对象
     * @param annotatedElement 被标注的注解的元素(方法/类)引用
     */
    void check(Annotation annotation, AnnotatedElement annotatedElement);

}