package com.github.zhitron.lite_el.operator;

import com.github.zhitron.lambda.function.SingleFunctionObjectToObject;
import com.github.zhitron.lambda.function.TripleFunctionObjectToObject;
import com.github.zhitron.lambda.function.TwiceFunctionObjectToObject;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 二元操作符类型枚举类，定义了各种支持的二元操作符及其相关功能
 * <p>
 * 该枚举类包含了常见的二元操作符，如算术运算符、位运算符、比较运算符、逻辑运算符等。
 * 每个枚举实例都关联一个操作符标记(token)，并维护一个操作符函数映射表，用于处理不同类型的参数组合。
 *
 * @author zhitron
 */
public enum OperatorType {

    /**
     * 逻辑非操作符 "!"
     */
    NOT("!", 1) {{
        this.register(Boolean.class, (firstValue) -> !firstValue);
        this.register(Byte.class, (firstValue) -> ~firstValue);
        this.register(Short.class, (firstValue) -> ~firstValue);
        this.register(Integer.class, (firstValue) -> ~firstValue);
        this.register(Long.class, (firstValue) -> ~firstValue);
    }},

    /**
     * 逻辑与操作符 "&&"
     */
    AND("&&", 2) {{
        this.register(String.class, String.class, Utils::AND);
        this.register(Boolean.class, Boolean.class, Utils::AND);
        this.register(Character.class, Character.class, Utils::AND);
        this.register(Byte.class, Byte.class, Utils::AND);
        this.register(Short.class, Short.class, Utils::AND);
        this.register(Integer.class, Integer.class, Utils::AND);
        this.register(Long.class, Long.class, Utils::AND);
        this.register(Float.class, Float.class, Utils::AND);
        this.register(Double.class, Double.class, Utils::AND);
    }},

    /**
     * 逻辑或操作符 "||"
     */
    OR("||", 2) {{
        this.register(String.class, String.class, Utils::OR);
        this.register(Boolean.class, Boolean.class, Utils::OR);
        this.register(Character.class, Character.class, Utils::OR);
        this.register(Byte.class, Byte.class, Utils::OR);
        this.register(Short.class, Short.class, Utils::OR);
        this.register(Integer.class, Integer.class, Utils::OR);
        this.register(Long.class, Long.class, Utils::OR);
        this.register(Float.class, Float.class, Utils::OR);
        this.register(Double.class, Double.class, Utils::OR);
    }},
    /**
     * 按位取反操作符 "~"
     */
    BIT_NOT("~", 1) {{
        this.register(Boolean.class, (firstValue) -> !firstValue);
        this.register(Byte.class, (firstValue) -> ~firstValue);
        this.register(Short.class, (firstValue) -> ~firstValue);
        this.register(Integer.class, (firstValue) -> ~firstValue);
        this.register(Long.class, (firstValue) -> ~firstValue);
    }},

    /**
     * 负号操作符 "-"
     */
    NEGATIVE("-", 1) {{
        this.register(Boolean.class, (firstValue) -> !firstValue);
        this.register(Byte.class, (firstValue) -> -firstValue);
        this.register(Short.class, (firstValue) -> -firstValue);
        this.register(Integer.class, (firstValue) -> -firstValue);
        this.register(Long.class, (firstValue) -> -firstValue);
        this.register(Float.class, (firstValue) -> -firstValue);
        this.register(Double.class, (firstValue) -> -firstValue);
    }},
    /**
     * 按位或操作符 "|"
     */
    BIT_OR("|", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::BIT_OR);
        this.register(Character.class, Character.class, Utils::BIT_OR);
        this.register(Byte.class, Byte.class, Utils::BIT_OR);
        this.register(Short.class, Short.class, Utils::BIT_OR);
        this.register(Integer.class, Integer.class, Utils::BIT_OR);
        this.register(Long.class, Long.class, Utils::BIT_OR);
        this.register(Float.class, Float.class, Utils::BIT_OR);
        this.register(Double.class, Double.class, Utils::BIT_OR);
    }},

    /**
     * 按位与操作符 "&"
     */
    BIT_AND("&", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::BIT_AND);
        this.register(Character.class, Character.class, Utils::BIT_AND);
        this.register(Byte.class, Byte.class, Utils::BIT_AND);
        this.register(Short.class, Short.class, Utils::BIT_AND);
        this.register(Integer.class, Integer.class, Utils::BIT_AND);
        this.register(Long.class, Long.class, Utils::BIT_AND);
        this.register(Float.class, Float.class, Utils::BIT_AND);
        this.register(Double.class, Double.class, Utils::BIT_AND);
    }},

    /**
     * 按位异或操作符 "^"
     */
    BIT_XOR("^", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::BIT_XOR);
        this.register(Character.class, Character.class, Utils::BIT_XOR);
        this.register(Byte.class, Byte.class, Utils::BIT_XOR);
        this.register(Short.class, Short.class, Utils::BIT_XOR);
        this.register(Integer.class, Integer.class, Utils::BIT_XOR);
        this.register(Long.class, Long.class, Utils::BIT_XOR);
        this.register(Float.class, Float.class, Utils::BIT_XOR);
        this.register(Double.class, Double.class, Utils::BIT_XOR);
    }},

    /**
     * 左移操作符 "<<"
     */
    BIT_LEFT_SHIFT("<<", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::BIT_LEFT_SHIFT);
        this.register(Character.class, Character.class, Utils::BIT_LEFT_SHIFT);
        this.register(Byte.class, Byte.class, Utils::BIT_LEFT_SHIFT);
        this.register(Short.class, Short.class, Utils::BIT_LEFT_SHIFT);
        this.register(Integer.class, Integer.class, Utils::BIT_LEFT_SHIFT);
        this.register(Long.class, Long.class, Utils::BIT_LEFT_SHIFT);
        this.register(Float.class, Float.class, Utils::BIT_LEFT_SHIFT);
        this.register(Double.class, Double.class, Utils::BIT_LEFT_SHIFT);
    }},

    /**
     * 右移操作符 ">>"
     */
    BIT_RIGHT_SHIFT(">>", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Character.class, Character.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Byte.class, Byte.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Short.class, Short.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Integer.class, Integer.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Long.class, Long.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Float.class, Float.class, Utils::BIT_RIGHT_SHIFT);
        this.register(Double.class, Double.class, Utils::BIT_RIGHT_SHIFT);
    }},

    /**
     * 无符号右移操作符 ">>>"
     */
    UNSIGNED_RIGHT_SHIFT(">>>", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Character.class, Character.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Byte.class, Byte.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Short.class, Short.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Integer.class, Integer.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Long.class, Long.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Float.class, Float.class, Utils::UNSIGNED_RIGHT_SHIFT);
        this.register(Double.class, Double.class, Utils::UNSIGNED_RIGHT_SHIFT);
    }},

    /**
     * 取模操作符 "%"
     */
    MODULO("%", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::MODULO);
        this.register(Character.class, Character.class, Utils::MODULO);
        this.register(Byte.class, Byte.class, Utils::MODULO);
        this.register(Short.class, Short.class, Utils::MODULO);
        this.register(Integer.class, Integer.class, Utils::MODULO);
        this.register(Long.class, Long.class, Utils::MODULO);
        this.register(Float.class, Float.class, Utils::MODULO);
        this.register(Double.class, Double.class, Utils::MODULO);
    }},

    /**
     * 乘法操作符 "*"
     */
    MULTIPLY("*", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::MULTIPLY);
        this.register(Character.class, Character.class, Utils::MULTIPLY);
        this.register(Byte.class, Byte.class, Utils::MULTIPLY);
        this.register(Short.class, Short.class, Utils::MULTIPLY);
        this.register(Integer.class, Integer.class, Utils::MULTIPLY);
        this.register(Long.class, Long.class, Utils::MULTIPLY);
        this.register(Float.class, Float.class, Utils::MULTIPLY);
        this.register(Double.class, Double.class, Utils::MULTIPLY);
    }},

    /**
     * 除法操作符 "/"
     */
    DIVIDING("/", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::DIVIDING);
        this.register(Character.class, Character.class, Utils::DIVIDING);
        this.register(Byte.class, Byte.class, Utils::DIVIDING);
        this.register(Short.class, Short.class, Utils::DIVIDING);
        this.register(Integer.class, Integer.class, Utils::DIVIDING);
        this.register(Long.class, Long.class, Utils::DIVIDING);
        this.register(Float.class, Float.class, Utils::DIVIDING);
        this.register(Double.class, Double.class, Utils::DIVIDING);
    }},

    /**
     * 加法操作符 "+"
     */
    PLUS("+", 2) {{
        this.register(Object.class, Object.class, Utils::PLUS);
        this.register(String.class, String.class, Utils::PLUS);
        this.register(Boolean.class, Boolean.class, Utils::PLUS);
        this.register(Character.class, Character.class, Utils::PLUS);
        this.register(Byte.class, Byte.class, Utils::PLUS);
        this.register(Short.class, Short.class, Utils::PLUS);
        this.register(Integer.class, Integer.class, Utils::PLUS);
        this.register(Long.class, Long.class, Utils::PLUS);
        this.register(Float.class, Float.class, Utils::PLUS);
        this.register(Double.class, Double.class, Utils::PLUS);
    }},

    /**
     * 减法操作符 "-"
     */
    MINUS("-", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::MINUS);
        this.register(Character.class, Character.class, Utils::MINUS);
        this.register(Byte.class, Byte.class, Utils::MINUS);
        this.register(Short.class, Short.class, Utils::MINUS);
        this.register(Integer.class, Integer.class, Utils::MINUS);
        this.register(Long.class, Long.class, Utils::MINUS);
        this.register(Float.class, Float.class, Utils::MINUS);
        this.register(Double.class, Double.class, Utils::MINUS);
    }},

    /**
     * 幂运算操作符 "**"
     */
    POWER("^", 2) {{
        this.register(Boolean.class, Boolean.class, Utils::POWER);
        this.register(Character.class, Character.class, Utils::POWER);
        this.register(Byte.class, Byte.class, Utils::POWER);
        this.register(Short.class, Short.class, Utils::POWER);
        this.register(Integer.class, Integer.class, Utils::POWER);
        this.register(Long.class, Long.class, Utils::POWER);
        this.register(Float.class, Float.class, Utils::POWER);
        this.register(Double.class, Double.class, Utils::POWER);
    }},
    /**
     * 小于比较操作符 "<"
     */
    LESS_THEN("<", 2) {{
        this.register(String.class, String.class, Utils::LESS_THEN);
        this.register(Boolean.class, Boolean.class, Utils::LESS_THEN);
        this.register(Character.class, Character.class, Utils::LESS_THEN);
        this.register(Byte.class, Byte.class, Utils::LESS_THEN);
        this.register(Short.class, Short.class, Utils::LESS_THEN);
        this.register(Integer.class, Integer.class, Utils::LESS_THEN);
        this.register(Long.class, Long.class, Utils::LESS_THEN);
        this.register(Float.class, Float.class, Utils::LESS_THEN);
        this.register(Double.class, Double.class, Utils::LESS_THEN);
    }},

    /**
     * 小于等于比较操作符 "<="
     */
    LESS_THEN_OR_EQUAL("<=", 2) {{
        this.register(String.class, String.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Boolean.class, Boolean.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Character.class, Character.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Byte.class, Byte.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Short.class, Short.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Integer.class, Integer.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Long.class, Long.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Float.class, Float.class, Utils::LESS_THEN_OR_EQUAL);
        this.register(Double.class, Double.class, Utils::LESS_THEN_OR_EQUAL);
    }},

    /**
     * 大于比较操作符 ">"
     */
    MORE_THEN(">", 2) {{
        this.register(String.class, String.class, Utils::MORE_THEN);
        this.register(Boolean.class, Boolean.class, Utils::MORE_THEN);
        this.register(Character.class, Character.class, Utils::MORE_THEN);
        this.register(Byte.class, Byte.class, Utils::MORE_THEN);
        this.register(Short.class, Short.class, Utils::MORE_THEN);
        this.register(Integer.class, Integer.class, Utils::MORE_THEN);
        this.register(Long.class, Long.class, Utils::MORE_THEN);
        this.register(Float.class, Float.class, Utils::MORE_THEN);
        this.register(Double.class, Double.class, Utils::MORE_THEN);
    }},

    /**
     * 大于等于比较操作符 ">="
     */
    MORE_THEN_OR_EQUAL(">=", 2) {{
        this.register(String.class, String.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Boolean.class, Boolean.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Character.class, Character.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Byte.class, Byte.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Short.class, Short.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Integer.class, Integer.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Long.class, Long.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Float.class, Float.class, Utils::MORE_THEN_OR_EQUAL);
        this.register(Double.class, Double.class, Utils::MORE_THEN_OR_EQUAL);
    }},

    /**
     * 等于比较操作符 "=="
     */
    EQUAL("==", 2) {{
        this.register(String.class, String.class, Utils::EQUAL);
        this.register(Boolean.class, Boolean.class, Utils::EQUAL);
        this.register(Character.class, Character.class, Utils::EQUAL);
        this.register(Byte.class, Byte.class, Utils::EQUAL);
        this.register(Short.class, Short.class, Utils::EQUAL);
        this.register(Integer.class, Integer.class, Utils::EQUAL);
        this.register(Long.class, Long.class, Utils::EQUAL);
        this.register(Float.class, Float.class, Utils::EQUAL);
        this.register(Double.class, Double.class, Utils::EQUAL);
    }},

    /**
     * 不等于比较操作符 "!="
     */
    NOT_EQUAL("!=", 2) {{
        this.register(String.class, String.class, Utils::NOT_EQUAL);
        this.register(Boolean.class, Boolean.class, Utils::NOT_EQUAL);
        this.register(Character.class, Character.class, Utils::NOT_EQUAL);
        this.register(Byte.class, Byte.class, Utils::NOT_EQUAL);
        this.register(Short.class, Short.class, Utils::NOT_EQUAL);
        this.register(Integer.class, Integer.class, Utils::NOT_EQUAL);
        this.register(Long.class, Long.class, Utils::NOT_EQUAL);
        this.register(Float.class, Float.class, Utils::NOT_EQUAL);
        this.register(Double.class, Double.class, Utils::NOT_EQUAL);
    }},

    /**
     * 匹配操作符 "=~"
     */
    CONTAIN("==~", 2) {{
        this.register(String.class, String.class, Utils::CONTAIN);
        this.register(Boolean.class, Boolean.class, Utils::CONTAIN);
        this.register(Character.class, Character.class, Utils::CONTAIN);
        this.register(Byte.class, Byte.class, Utils::CONTAIN);
        this.register(Short.class, Short.class, Utils::CONTAIN);
        this.register(Integer.class, Integer.class, Utils::CONTAIN);
        this.register(Long.class, Long.class, Utils::CONTAIN);
        this.register(Float.class, Float.class, Utils::CONTAIN);
        this.register(Double.class, Double.class, Utils::CONTAIN);
    }},

    /**
     * 匹配操作符 "=~"
     */
    CONTAIN_NOT_STRICT("=~", 2) {{
        this.register(String.class, String.class, Utils::CONTAIN);
        this.register(Boolean.class, Boolean.class, Utils::CONTAIN);
        this.register(Character.class, Character.class, Utils::CONTAIN);
        this.register(Byte.class, Byte.class, Utils::CONTAIN);
        this.register(Short.class, Short.class, Utils::CONTAIN);
        this.register(Integer.class, Integer.class, Utils::CONTAIN);
        this.register(Long.class, Long.class, Utils::CONTAIN);
        this.register(Float.class, Float.class, Utils::CONTAIN);
        this.register(Double.class, Double.class, Utils::CONTAIN);
    }},

    /**
     * 匹配操作符 "=^"
     */
    START_WIDTHS("==^", 2) {{
        this.register(String.class, String.class, Utils::START);
        this.register(Boolean.class, Boolean.class, Utils::START);
        this.register(Character.class, Character.class, Utils::START);
        this.register(Byte.class, Byte.class, Utils::START);
        this.register(Short.class, Short.class, Utils::START);
        this.register(Integer.class, Integer.class, Utils::START);
        this.register(Long.class, Long.class, Utils::START);
        this.register(Float.class, Float.class, Utils::START);
        this.register(Double.class, Double.class, Utils::START);
    }},

    /**
     * 匹配操作符 "=^"
     */
    START_WIDTHS_IGNORED_CASE("=^", 2) {{
        this.register(String.class, String.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Boolean.class, Boolean.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Character.class, Character.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Byte.class, Byte.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Short.class, Short.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Integer.class, Integer.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Long.class, Long.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Float.class, Float.class, Utils::START_WIDTHS_IGNORED_CASE);
        this.register(Double.class, Double.class, Utils::START_WIDTHS_IGNORED_CASE);
    }},

    /**
     * 匹配操作符 "=$"
     */
    END_WIDTHS("==$", 2) {{
        this.register(String.class, String.class, Utils::END_WIDTHS);
        this.register(Boolean.class, Boolean.class, Utils::END_WIDTHS);
        this.register(Character.class, Character.class, Utils::END_WIDTHS);
        this.register(Byte.class, Byte.class, Utils::END_WIDTHS);
        this.register(Short.class, Short.class, Utils::END_WIDTHS);
        this.register(Integer.class, Integer.class, Utils::END_WIDTHS);
        this.register(Long.class, Long.class, Utils::END_WIDTHS);
        this.register(Float.class, Float.class, Utils::END_WIDTHS);
        this.register(Double.class, Double.class, Utils::END_WIDTHS);
    }},

    /**
     * 匹配操作符 "=$"
     */
    END_WIDTHS_IGNORED_CASE("=$", 2) {{
        this.register(String.class, String.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Boolean.class, Boolean.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Character.class, Character.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Byte.class, Byte.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Short.class, Short.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Integer.class, Integer.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Long.class, Long.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Float.class, Float.class, Utils::END_WIDTHS_IGNORED_CASE);
        this.register(Double.class, Double.class, Utils::END_WIDTHS_IGNORED_CASE);
    }},
    /**
     * 三元条件操作符 ?:
     * 用于条件表达式，格式为 condition ? valueIfTrue : valueIfFalse
     */
    TERNARY("?:", 3) {{
        this.register(Object.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null ? secondValue : thirdValue);
        this.register(String.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && !firstValue.isEmpty() && !"null".equals(firstValue) ? secondValue : thirdValue);
        this.register(Character.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue != '\0' ? secondValue : thirdValue);
        this.register(Boolean.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue ? secondValue : thirdValue);
        this.register(Byte.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue != 0 ? secondValue : thirdValue);
        this.register(Short.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue != 0 ? secondValue : thirdValue);
        this.register(Integer.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue != 0 ? secondValue : thirdValue);
        this.register(Long.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && firstValue != 0 ? secondValue : thirdValue);
        this.register(Float.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && Float.compare(firstValue, 0) != 0 ? secondValue : thirdValue);
        this.register(Double.class, Object.class, Object.class, (firstValue, secondValue, thirdValue) -> firstValue != null && Double.compare(firstValue, 0) != 0 ? secondValue : thirdValue);

        this.addTypeHierarchy(String.class, Object.class);
        this.addTypeHierarchy(Character.class, Object.class);
        this.addTypeHierarchy(Boolean.class, Object.class);
        this.addTypeHierarchy(Byte.class, Object.class);
        this.addTypeHierarchy(Short.class, Object.class);
        this.addTypeHierarchy(Integer.class, Object.class);
        this.addTypeHierarchy(Long.class, Object.class);
        this.addTypeHierarchy(Float.class, Object.class);
        this.addTypeHierarchy(Double.class, Object.class);
    }};


    /**
     * 操作符的字符串表示
     */
    public final String token;
    private final int parameterCount;

    /**
     * 存储不同类型参数组合对应的操作符函数映射表
     */
    private final Map<OperatorKey, Operator> operators = new ConcurrentHashMap<>();

    /**
     * 构造函数，初始化二元操作符类型
     *
     * @param token          操作符的字符串表示，不能为null
     * @param parameterCount
     */
    OperatorType(String token, int parameterCount) {
        this.token = token;
        this.parameterCount = parameterCount;
    }

    private final Map<Class<?>, ClassHierarchyNode[]> data = new ConcurrentHashMap<>();

    public OperatorType addTypeHierarchy(Class<?>... types) {
        if (types == null || types.length == 0) {
            return this;
        }
        ClassHierarchyNode head = new ClassHierarchyNode(types[0]);
        ClassHierarchyNode current = head;

        // 验证类型链，确保第一个是子类，后续是父类关系
        for (int i = 0; i < types.length - 1; i++) {
            if (!types[i + 1].isAssignableFrom(types[i])) {
                throw new IllegalArgumentException("Type " + types[i].getName() + " is not a subclass of " + types[i + 1].getName());
            }
            ClassHierarchyNode node = new ClassHierarchyNode(types[i + 1]);
            current.next(node);
            current = node;
        }
        data.compute(head.type(), (key, value) -> {
            if (value == null) {
                return new ClassHierarchyNode[]{head};
            } else {
                if (value != null && Arrays.asList(value).contains(head)) {
                    return value;
                }
                ClassHierarchyNode[] newArray = new ClassHierarchyNode[value.length + 1];
                System.arraycopy(value, 0, newArray, 0, value.length);
                newArray[value.length] = head;
                return newArray;
            }
        });

        return this;
    }

    /**
     * 注册一个一元操作符函数，用于处理指定类型的参数并返回结果
     *
     * @param <F>             参数的泛型类型
     * @param firstValueClass 参数的类型Class对象，不能为null
     * @param function        处理参数的函数，不能为null
     * @return 返回当前UnitaryOperatorType实例，支持链式调用
     * @throws OperatorValueException 当任一输入参数为null时抛出
     */
    @SuppressWarnings("unchecked")
    public final <F> OperatorType register(Class<? extends F> firstValueClass, SingleFunctionObjectToObject<? super F, Object> function) {
        // 参数校验
        if (parameterCount != 1) {
            throw new OperatorValueException("OperatorType " + this.token + " is not a unary operator");
        }
        if (firstValueClass == null) {
            throw new OperatorValueException("Input firstValueClass cannot be null");
        }
        if (function == null) {
            throw new OperatorValueException("Input function cannot be null");
        }

        // 创建操作符键并存储到operators映射中
        OperatorKey key = new OperatorKey(firstValueClass);
        operators.put(key, (args) -> {
            if (args.length != 1) {
                throw new OperatorValueException("OperatorType " + this.token + " requires " + parameterCount + " parameters, but " + args.length + " were provided");
            }
            F firstValueReal;

            // 类型转换参数，失败时抛出详细异常
            try {
                firstValueReal = (F) args[0];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to firstValue of type: " + firstValueClass.getName(), e);
            }

            // 应用实际的函数处理逻辑
            return function.apply(firstValueReal);
        });
        return this;
    }


    /**
     * 注册一个二元操作符函数，用于处理指定类型的两个参数并返回结果
     *
     * @param <F>              第一个参数的泛型类型
     * @param <S>              第二个参数的泛型类型
     * @param firstValueClass  第一个参数的类型Class对象，不能为null
     * @param secondValueClass 第二个参数的类型Class对象，不能为null
     * @param function         处理两个参数的BiFunction函数，不能为null
     * @return 返回当前BinaryOperatorType实例，支持链式调用
     * @throws OperatorValueException 当任一输入参数为null时抛出
     */
    @SuppressWarnings("unchecked")
    public <F, S> OperatorType register(Class<? extends F> firstValueClass, Class<? extends S> secondValueClass, TwiceFunctionObjectToObject<? super F, ? super S, Object> function) {
        // 参数校验
        if (parameterCount != 2) {
            throw new OperatorValueException("OperatorType " + this.token + " only support " + parameterCount + " parameters");
        }
        if (firstValueClass == null) {
            throw new OperatorValueException("Input firstValueClass cannot be null");
        }
        if (secondValueClass == null) {
            throw new OperatorValueException("Input secondValueClass cannot be null");
        }
        if (function == null) {
            throw new OperatorValueException("Input function cannot be null");
        }

        // 创建操作符键并存储到operators映射中
        OperatorKey key = new OperatorKey(firstValueClass, secondValueClass);
        operators.put(key, (args) -> {
            if (args.length != 2) {
                throw new OperatorValueException("OperatorType " + this.token + " only support " + parameterCount + " parameters");
            }
            F firstValueReal;
            S secondValueReal;

            // 类型转换第一个参数，失败时抛出详细异常
            try {
                firstValueReal = (F) args[0];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to firstValue of type: " + firstValueClass.getName(), e);
            }

            // 类型转换第二个参数，失败时抛出详细异常
            try {
                secondValueReal = (S) args[1];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to secondValue of type: " + secondValueClass.getName(), e);
            }

            // 应用实际的函数处理逻辑
            return function.apply(firstValueReal, secondValueReal);
        });
        return this;
    }

    /**
     * 注册一个三元操作符函数，用于处理指定类型的三个参数并返回结果
     *
     * @param <F>              第一个参数的泛型类型
     * @param <S>              第二个参数的泛型类型
     * @param <T>              第三个参数的泛型类型
     * @param firstValueClass  第一个参数的类型Class对象，不能为null
     * @param secondValueClass 第二个参数的类型Class对象，不能为null
     * @param thirdValueClass  第三个参数的类型Class对象，不能为null
     * @param function         处理三个参数的TripleFunctionObjectToObject函数，不能为null
     * @return 返回当前TernaryOperatorType实例，支持链式调用
     * @throws OperatorValueException 当任一输入参数为null时抛出
     */
    @SuppressWarnings("unchecked")
    public final <F, S, T> OperatorType register(Class<? extends F> firstValueClass, Class<? extends S> secondValueClass, Class<? extends T> thirdValueClass, TripleFunctionObjectToObject<? super F, ? super S, ? super T, Object> function) {
        // 参数校验
        if (parameterCount != 3) {
            throw new OperatorValueException("OperatorType " + this.token + " only support " + parameterCount + " parameters");
        }
        if (firstValueClass == null) {
            throw new OperatorValueException("Input firstValueClass cannot be null");
        }
        if (secondValueClass == null) {
            throw new OperatorValueException("Input secondValueClass cannot be null");
        }
        if (thirdValueClass == null) {
            throw new OperatorValueException("Input thirdValueClass cannot be null");
        }
        if (function == null) {
            throw new OperatorValueException("Input function cannot be null");
        }

        // 创建操作符键并存储到operators映射中
        OperatorKey key = new OperatorKey(firstValueClass, secondValueClass, thirdValueClass);
        operators.put(key, (args) -> {
            if (args.length != 3) {
                throw new OperatorValueException("OperatorType " + this.token + " only support " + parameterCount + " parameters");
            }
            F firstValueReal;
            S secondValueReal;
            T thirdValueReal;

            // 类型转换第一个参数，失败时抛出详细异常
            try {
                firstValueReal = (F) args[0];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to firstValue of type: " + firstValueClass.getName(), e);
            }

            // 类型转换第二个参数，失败时抛出详细异常
            try {
                secondValueReal = (S) args[1];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to secondValue of type: " + secondValueClass.getName(), e);
            }

            // 类型转换第三个参数，失败时抛出详细异常
            try {
                thirdValueReal = (T) args[2];
            } catch (ClassCastException e) {
                throw new OperatorValueException("Type casting error when applying operator to thirdValue of type: " + thirdValueClass.getName(), e);
            }

            // 应用实际的函数处理逻辑
            return function.apply(firstValueReal, secondValueReal, thirdValueReal);
        });
        return this;
    }

    /**
     * 处理输入值，根据其类型查找并应用相应的三元运算符
     *
     * @return 运算符处理后的结果
     * @throws OperatorValueException 当输入值为null、找不到对应运算符或运算过程中发生错误时抛出
     */
    public final Object handle(Object... args) {
        if (parameterCount != args.length) {
            throw new OperatorValueException("Parameter count mismatch: expected " + parameterCount + ", but got " + args.length);
        }

        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i] == null ? Object.class : args[i].getClass();
        }

        ClassHierarchyNode[][] argNodes = new ClassHierarchyNode[parameterCount][];
        for (int i = 0; i < argTypes.length; i++) {
            ClassHierarchyNode[] classHierarchyNodes = data.get(argTypes[i]);
            int nodeLength = 1 + (classHierarchyNodes == null ? 0 : classHierarchyNodes.length);
            argNodes[i] = new ClassHierarchyNode[nodeLength];
            argNodes[i][0] = new ClassHierarchyNode(argTypes[i]);
            if (classHierarchyNodes != null) {
                System.arraycopy(classHierarchyNodes, 0, argNodes[i], 1, nodeLength - 1);
            }
        }

        Operator operator = findOperator(argNodes, argTypes);

        if (operator == null) {
            throw new OperatorValueException("No operator found for input types: " + Arrays.toString(argTypes));
        }

        // 应用运算符并处理可能的异常
        try {
            return operator.apply(args);
        } catch (OperatorValueException e) {
            throw e;
        } catch (Exception e) {
            throw new OperatorValueException("Error occurred while applying operator: " + e.getMessage(), e);
        }
    }

    private Operator findOperator(ClassHierarchyNode[][] argNodes, Class<?>[] argTypes) {
        switch (parameterCount) {
            case 1:
                return findOneArgOperator(argNodes[0], argTypes[0]);
            case 2:
                return findTwoArgOperator(argNodes[0], argTypes[0], argNodes[1], argTypes[1]);
            case 3:
                return findThreeArgOperator(argNodes[0], argTypes[0], argNodes[1], argTypes[1], argNodes[2], argTypes[2]);
            default:
                throw new OperatorValueException("Unsupported parameter count: " + parameterCount);
        }
    }

    private Operator findOneArgOperator(ClassHierarchyNode[] firstTypeHierarchyList, Class<?> firstArgType) {
        for (ClassHierarchyNode firstTypeHierarchy : firstTypeHierarchyList) {
            for (Class<?> firstType : firstTypeHierarchy) {
                Operator op = operators.get(new OperatorKey(firstType));
                if (op != null && firstType.isAssignableFrom(firstArgType)) {
                    return op;
                }
            }
        }
        return null;
    }

    private Operator findTwoArgOperator(
            ClassHierarchyNode[] firstTypeHierarchyList,
            Class<?> firstArgType,
            ClassHierarchyNode[] secondTypeHierarchyList,
            Class<?> secondArgType) {

        for (ClassHierarchyNode firstTypeHierarchy : firstTypeHierarchyList) {
            for (ClassHierarchyNode secondTypeHierarchy : secondTypeHierarchyList) {
                for (Class<?> firstType : firstTypeHierarchy) {
                    for (Class<?> secondType : secondTypeHierarchy) {
                        Operator op = operators.get(new OperatorKey(firstType, secondType));
                        if (op != null &&
                                firstType.isAssignableFrom(firstArgType) &&
                                secondType.isAssignableFrom(secondArgType)) {
                            return op;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Operator findThreeArgOperator(
            ClassHierarchyNode[] firstTypeHierarchyList,
            Class<?> firstArgType,
            ClassHierarchyNode[] secondTypeHierarchyList,
            Class<?> secondArgType,
            ClassHierarchyNode[] thirdTypeHierarchyList,
            Class<?> thirdArgType) {

        for (ClassHierarchyNode firstTypeHierarchy : firstTypeHierarchyList) {
            for (ClassHierarchyNode secondTypeHierarchy : secondTypeHierarchyList) {
                for (ClassHierarchyNode thirdTypeHierarchy : thirdTypeHierarchyList) {
                    for (Class<?> firstType : firstTypeHierarchy) {
                        for (Class<?> secondType : secondTypeHierarchy) {
                            for (Class<?> thirdType : thirdTypeHierarchy) {
                                Operator op = operators.get(new OperatorKey(firstType, secondType, thirdType));
                                if (op != null &&
                                        firstType.isAssignableFrom(firstArgType) &&
                                        secondType.isAssignableFrom(secondArgType) &&
                                        thirdType.isAssignableFrom(thirdArgType)) {
                                    return op;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


}
