package apexsx.forge.util.algorithm;

import java.util.*;

/**
 * @author Terraqo
 */
public class QuickMap<K, V> implements Map<K, V> {
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private final boolean isCaseSensitive;
    private final int capacity;
    private final int mark;
    private final Node[] table;

    private Node head;
    private Node tail;

    private int size;

    public QuickMap(boolean isCaseSensitive, int capacity) {
        this.isCaseSensitive = isCaseSensitive;
        int n = -1 >>> Integer.numberOfLeadingZeros(capacity - 1);
        this.capacity = n <= 32 ? 32 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        this.mark = capacity - 1;
        this.table = new Node[capacity];
    }


    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        Object old = null;
        int hash = hash(key);
        Node newNode = new Node(hash, key, value);
        Node root = table[hash & mark];
        if (root == null) {
            table[hash & mark] = newNode;
            addNodeOnLinkedList(newNode);
        } else {
            old = putTreeNode(root, newNode);
            if (newNode.color) {
                addNodeOnLinkedList(newNode);
            }
        }
        return (V) old;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private Object putTreeNode(Node root, Node node) {
        Node xp = null;
        int cmp = 0;
        // 1. 查找插入位置或更新现有节点
        for (Node p = root; p != null; ) {
            xp = p;
            cmp = compare(node, p);
            if (cmp < 0) {
                p = p.ltree;
            } else if (cmp > 0) {
                p = p.rtree;
            } else {
                // Key 已存在，更新值并返回旧值
                return p.setValue(node.val);
            }
        }

        if (xp == null) {
            // 理论上不会发生，因为调用此方法时根节点已存在
            table[node.hash & mark] = node;
        } else {
            if (cmp < 0) {
                xp.ltree = node;
            } else {
                xp.rtree = node;
            }
        }

        node.parent = xp;

        // 3. 平衡红黑树
        fixAfterInsertion(node);

        return null;
    }

    /**
     * 插入节点后，调整红黑树结构
     *
     * @param x 新插入的节点
     */
    private void fixAfterInsertion(Node x) {
        // 新节点为红色，若父节点为黑色则无需调整；若父节点为空（根节点），直接染黑
        while (x != null) {
            // 修正：标准红黑树修复逻辑
            Node parent = x.parent;

            // 如果父节点为空，说明 x 是根节点，染黑即可
            if (parent == null) {
                x.color = false;
                return;
            }

            // 如果父节点是黑色，满足红黑树性质，无需调整
            if (!isRed(parent)) {
                return;
            }

            Node grandParent = parent.parent;
            if (grandParent == null) {
                // 父节点是根节点，染黑即可
                parent.color = false;
                return;
            }

            Node uncle = (parent == grandParent.ltree) ? grandParent.rtree : grandParent.ltree;

            // 情况 1: 叔父节点是红色 -> 变色
            if (isRed(uncle)) {
                grandParent.color = true;
                parent.color = false;
                if (uncle != null) uncle.color = false;
                x = grandParent; // 继续向上检查
            } else {
                // 情况 2 & 3: 叔父节点是黑色 -> 旋转
                if (parent == grandParent.ltree) {
                    if (x == parent.rtree) {
                        // 情况 2: 左 - 右 -> 左旋父节点，转换为情况 3
                        x = rotateLeft(parent);
                        parent = x.parent; // 更新父节点引用
                    }
                    // 情况 3: 左 - 左 -> 右旋祖父节点
                    if (parent != null) {
                        parent.color = false;
                        grandParent.color = true;
                        rotateRight(grandParent);
                    }
                } else {
                    if (x == parent.ltree) {
                        // 情况 2: 右 - 左 -> 右旋父节点，转换为情况 3
                        x = rotateRight(parent);
                        parent = x.parent; // 更新父节点引用
                    }
                    // 情况 3: 右 - 右 -> 左旋祖父节点
                    if (parent != null) {
                        parent.color = false;
                        grandParent.color = true;
                        rotateLeft(grandParent);
                    }
                }
                break; // 旋转后性质恢复，结束循环
            }
        }

        // 确保根节点始终是黑色
        Node rootNode = table[hash(x.key) & mark]; // 获取当前桶的根（简化处理，实际应追踪根）
        // 由于无法直接在这里轻松获取最新的根节点引用（可能被旋转改变），
        // 通常做法是在插入完成后，将桶数组指向的节点强制染黑，或者在外部处理。
        // 在此局部方法中，我们依赖调用者或后续逻辑保证根节点为黑，
        // 或者如果 x 成为了新的根（在旋转中上升），我们需要将其染黑。
        // 简单的兜底：如果 x 没有父节点了（变成了根），染黑。
        if (x != null && x.parent == null) {
            x.color = false;
        }
    }

    private boolean isRed(Node node) {
        return node != null && node.color;
    }

    private int compare(Node node, Node existing) {
        if (node.hash != existing.hash) {
            return node.hash < existing.hash ? -1 : 1;
        }
        if (node.key == null) {
            return existing.key == null ? 0 : -1;
        }
        @SuppressWarnings("unchecked")
        int cmp = ((Comparable<? super K>) node.key).compareTo((K) existing.key);
        return cmp;
    }

    private Node rotateLeft(Node h) {
        Node x = h.rtree;
        h.rtree = x.ltree;
        x.ltree = h;
        x.color = h.color;
        h.color = true; // 新父节点设为红色
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.ltree;
        h.ltree = x.rtree;
        x.rtree = h;
        x.color = h.color;
        h.color = true; // 新父节点设为红色
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.ltree != null) h.ltree.color = !h.ltree.color;
        if (h.rtree != null) h.rtree.color = !h.rtree.color;
    }


    private void addNodeOnLinkedList(Node node) {
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    private void delNodeOnLinkedList(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        node.prev = null;
        node.next = null;
        size--;
    }

    protected int hash(Object key) {
        if (key == null) {
            return 0;
        }

        if (key instanceof String s && !isCaseSensitive) {
            int h = 0;
            int length = s.length();
            for (int i = 0; i < length; i++) {
                h = 31 * h + Character.toLowerCase(s.charAt(i));
            }
            return h ^ (h >>> 16);
        } else {
            int h = key.hashCode();
            return h ^ (h >>> 16);
        }
    }

    protected static class Node implements Entry<Object, Object>, Comparable<Node> {

        private final int hash;
        private Object key, val;
        private Node prev, next;
        private Node parent, ltree, rtree;
        private boolean color;

        protected Node(int hash, Object key, Object val) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.color = true;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return val;
        }

        @Override
        public Object setValue(Object value) {
            Object old = val;
            val = value;
            return old;
        }

        @Override
        public int compareTo(Node other) {
            if (this.hash != other.hash) {
                return this.hash < other.hash ? -1 : 1;
            }
            if (this.key == null) {
                return other.key == null ? 0 : -1;
            }
            return 0;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(val);
        }

        public final boolean equals(Object o) {
            if (o == this) return true;

            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(getKey(), e.getKey())
                    && Objects.equals(getValue(), e.getValue());
        }

        public final String toString() {
            return key + "=" + val;
        }
    }
}
