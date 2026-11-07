package com.github.zhitron.lite_el.operator;

import com.github.zhitron.universal.ConvertingIterator;
import com.github.zhitron.universal.UnidirectionalIterator;
import com.github.zhitron.universal.UnidirectionalNode;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author zhitron
 */
class ClassHierarchyNode implements Iterable<Class<?>>, UnidirectionalNode<ClassHierarchyNode> {
    private final Class<?> type;
    private ClassHierarchyNode next;

    ClassHierarchyNode(Class<?> type) {
        this.type = type;
    }

    public Class<?> type() {
        return type;
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return new ConvertingIterator<>(new UnidirectionalIterator<>(this), (element) -> element.type);
    }

    @Override
    public ClassHierarchyNode next() {
        return next;
    }

    @Override
    public void next(ClassHierarchyNode node) {
        next = node;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClassHierarchyNode)) return false;

        ClassHierarchyNode other = (ClassHierarchyNode) object;
        
        // 比较整个链表中的每个元素
        ClassHierarchyNode currentThis = this;
        ClassHierarchyNode currentOther = other;
        
        while (currentThis != null && currentOther != null) {
            if (!Objects.equals(currentThis.type, currentOther.type)) {
                return false;
            }
            currentThis = currentThis.next();
            currentOther = currentOther.next();
        }
        
        // 确保两个链表同时结束
        return currentThis == null && currentOther == null;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(type);
        ClassHierarchyNode current = next;
        while (current != null) {
            result = 31 * result + Objects.hashCode(current.type);
            current = current.next();
        }
        return result;
    }
}
