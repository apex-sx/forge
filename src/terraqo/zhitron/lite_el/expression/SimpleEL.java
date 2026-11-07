package com.github.zhitron.lite_el.expression;// SimpleEL.java - 高性能、无依赖、安全的 EL 表达式引擎

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SimpleEL {

    // endregion

    // endregion

    // region === AST Nodes ===
    abstract static class ASTNode {
        abstract Object evaluate(Context ctx);
    }

    static class LiteralNode extends ASTNode {
        final Object value;

        LiteralNode(Object value) {
            this.value = value;
        }

        @Override
        Object evaluate(Context ctx) {
            return value;
        }
    }

    static class VariableNode extends ASTNode {
        final String name;

        VariableNode(String name) {
            this.name = name;
        }

        @Override
        Object evaluate(Context ctx) {
            return ctx.get(name);
        }
    }

    static class PropertyAccessNode extends ASTNode {
        final ASTNode target;
        final String property;
        final ASTNode index;

        PropertyAccessNode(ASTNode target, String property, ASTNode index) {
            this.target = target;
            this.property = property;
            this.index = index;
        }

        @Override
        Object evaluate(Context ctx) {
            Object obj = target.evaluate(ctx);
            if (obj == null) return null;

            if (index != null) {
                Object idx = index.evaluate(ctx);
                if (obj instanceof java.util.List && idx instanceof Number) {
                    int i = ((Number) idx).intValue();
                    java.util.List<?> list = (java.util.List<?>) obj;
                    return i >= 0 && i < list.size() ? list.get(i) : null;
                } else if (obj instanceof java.util.Map) {
                    java.util.Map<?, ?> map = (java.util.Map<?, ?>) obj;
                    return map.get(idx);
                }
            } else if (property != null) {
                if (obj instanceof java.util.Map) {
                    return ((java.util.Map<?, ?>) obj).get(property);
                }
            }
            return null;
        }
    }

    static class BinaryOpNode extends ASTNode {
        final String op;
        final ASTNode left, right;

        BinaryOpNode(String op, ASTNode left, ASTNode right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        @Override
        Object evaluate(Context ctx) {
            Object l = left.evaluate(ctx);
            Object r = right.evaluate(ctx);

            switch (op) {
                case "+":
                    return coerceAdd(l, r);
                case "-":
                    return coerceNumber(l) - coerceNumber(r);
                case "*":
                case "×":
                    return coerceNumber(l) * coerceNumber(r);
                case "/":
                case "÷": {
                    double den = coerceNumber(r);
                    return den != 0 ? coerceNumber(l) / den : 0.0;
                }
                case "%":
                    return coerceNumber(l) % coerceNumber(r);
                case "^":
                    return Math.pow(coerceNumber(l), coerceNumber(r));
                case "==":
                    return equals(l, r);
                case "!=":
                    return !equals(l, r);
                case "<":
                    return coerceNumber(l) < coerceNumber(r);
                case "<=":
                    return coerceNumber(l) <= coerceNumber(r);
                case ">":
                    return coerceNumber(l) > coerceNumber(r);
                case ">=":
                    return coerceNumber(l) >= coerceNumber(r);
                case "&&":
                    return toBoolean(l) && toBoolean(r);
                case "||":
                    return toBoolean(l) || toBoolean(r);
                default:
                    throw new UnsupportedOperationException("Unknown op: " + op);
            }
        }

        private double coerceNumber(Object o) {
            if (o == null) return 0;
            if (o instanceof Number) return ((Number) o).doubleValue();
            if (o instanceof Boolean) return (Boolean) o ? 1 : 0;
            try {
                return Double.parseDouble(o.toString());
            } catch (Exception e) {
                return 0;
            }
        }

        private Object coerceAdd(Object a, Object b) {
            if (a == null && b == null) return "";
            if (a == null) a = "";
            if (b == null) b = "";
            if (a instanceof String || b instanceof String) {
                return a.toString() + b.toString();
            }
            return coerceNumber(a) + coerceNumber(b);
        }

        private boolean toBoolean(Object o) {
            if (o == null) return false;
            if (o instanceof Boolean) return (Boolean) o;
            if (o instanceof Number) return ((Number) o).doubleValue() != 0;
            return true;
        }

        private boolean equals(Object a, Object b) {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            if (a.getClass() == b.getClass()) return a.equals(b);
            if (a instanceof Number && b instanceof Number) {
                return ((Number) a).doubleValue() == ((Number) b).doubleValue();
            }
            return a.toString().equals(b.toString());
        }
    }

    static class UnaryOpNode extends ASTNode {
        final String op;
        final ASTNode expr;

        UnaryOpNode(String op, ASTNode expr) {
            this.op = op;
            this.expr = expr;
        }

        @Override
        Object evaluate(Context ctx) {
            Object val = expr.evaluate(ctx);
            switch (op) {
                case "-":
                    return -coerceNumber(val);
                case "!":
                    return !toBoolean(val);
                default:
                    throw new UnsupportedOperationException("Unknown unary op: " + op);
            }
        }

        private double coerceNumber(Object o) {
            if (o == null) return 0;
            if (o instanceof Number) return ((Number) o).doubleValue();
            if (o instanceof Boolean) return (Boolean) o ? 1 : 0;
            try {
                return Double.parseDouble(o.toString());
            } catch (Exception e) {
                return 0;
            }
        }

        private boolean toBoolean(Object o) {
            if (o == null) return false;
            if (o instanceof Boolean) return (Boolean) o;
            if (o instanceof Number) return ((Number) o).doubleValue() != 0;
            return true;
        }
    }

    static class FunctionCallNode extends ASTNode {
        final String namespace;
        final String name;
        final List<ASTNode> args;

        FunctionCallNode(String namespace, String name, List<ASTNode> args) {
            this.namespace = namespace;
            this.name = name;
            this.args = args;
        }

        @Override
        Object evaluate(Context ctx) {
            List<Object> evaluatedArgs = new ArrayList<>();
            for (ASTNode arg : args) {
                evaluatedArgs.add(arg.evaluate(ctx));
            }
            return ctx.callFunction(namespace, name, evaluatedArgs);
        }
    }

    static class TernaryNode extends ASTNode {
        final ASTNode condition;
        final ASTNode thenBranch;
        final ASTNode elseBranch;

        TernaryNode(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        Object evaluate(Context ctx) {
            return toBoolean(condition.evaluate(ctx)) ?
                    thenBranch.evaluate(ctx) :
                    elseBranch.evaluate(ctx);
        }

        private boolean toBoolean(Object o) {
            if (o == null) return false;
            if (o instanceof Boolean) return (Boolean) o;
            if (o instanceof Number) return ((Number) o).doubleValue() != 0;
            return true;
        }
    }
    // endregion

    // region === Parser ===
    public static class Parser {
        private final List<Token> tokens;
        private int current = 0;

        public Parser(List<Token> tokens) {
            this.tokens = tokens;
        }

        public ASTNode parse() {
            return expression();
        }

        private ASTNode expression() {
            return ternary();
        }

        private ASTNode ternary() {
            ASTNode condition = logicalOr();
            if (match(TokenType.QUESTION)) {
                ASTNode thenBranch = expression();
                consume(TokenType.COLON, "Expect ':' in ternary operator.");
                ASTNode elseBranch = expression();
                return new TernaryNode(condition, thenBranch, elseBranch);
            }
            return condition;
        }

        private ASTNode logicalOr() {
            ASTNode expr = logicalAnd();
            while (match(TokenType.OR)) {
                Token op = previous();
                ASTNode right = logicalAnd();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode logicalAnd() {
            ASTNode expr = equality();
            while (match(TokenType.AND)) {
                Token op = previous();
                ASTNode right = equality();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode equality() {
            ASTNode expr = comparison();
            while (match(TokenType.EQUAL, TokenType.NOT_EQUAL)) {
                Token op = previous();
                ASTNode right = comparison();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode comparison() {
            ASTNode expr = term();
            while (match(TokenType.LESS_THEN, TokenType.LESS_THEN_OR_EQUAL, TokenType.MORE_THEN, TokenType.MORE_THEN_OR_EQUAL)) {
                Token op = previous();
                ASTNode right = term();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode term() {
            ASTNode expr = factor();
            while (match(TokenType.PLUS, TokenType.MINUS)) {
                Token op = previous();
                ASTNode right = factor();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode factor() {
            ASTNode expr = power();
            while (match(TokenType.MULTIPLY, TokenType.DIVIDING, TokenType.MODULO)) {
                Token op = previous();
                ASTNode right = power();
                expr = new BinaryOpNode(op.lexeme, expr, right);
            }
            return expr;
        }

        private ASTNode power() {
            ASTNode expr = unary();
            if (match(TokenType.POWER)) {
                Token op = previous();
                ASTNode right = unary();
                expr = new BinaryOpNode("^", expr, right);
            }
            return expr;
        }

        private ASTNode unary() {
            if (match(TokenType.MINUS, TokenType.NOT)) {
                Token op = previous();
                ASTNode right = unary();
                return new UnaryOpNode(op.lexeme, right);
            }
            return primary();
        }

        private ASTNode primary() {
            if (match(TokenType.NUMBER)) {
                String num = previous().lexeme;
                if (num.contains(".") || num.contains("e") || num.contains("E")) {
                    return new LiteralNode(Double.parseDouble(num));
                } else {
                    return new LiteralNode(Long.parseLong(num));
                }
            }
            if (match(TokenType.STRING)) {
                return new LiteralNode(previous().lexeme);
            }
            if (match(TokenType.TRUE)) return new LiteralNode(true);
            if (match(TokenType.FALSE)) return new LiteralNode(false);
            if (match(TokenType.NULL)) return new LiteralNode(null);

            if (match(TokenType.LPAREN)) {
                ASTNode expr = expression();
                consume(TokenType.RPAREN, "Expect ')' after expression.");
                return expr;
            }

            if (match(TokenType.IDENTIFIER)) {
                Token id = previous();
                return finishPrimary(id);
            }

            throw error("Expect expression.");
        }

        private ASTNode finishPrimary(Token id) {
            ASTNode node = new VariableNode(id.lexeme);

            while (true) {
                if (match(TokenType.DOT)) {
                    if (!check(TokenType.IDENTIFIER)) {
                        throw error("Expect property name after '.'");
                    }
                    Token prop = advance();
                    node = new PropertyAccessNode(node, prop.lexeme, null);
                } else if (match(TokenType.LBRACKET)) {
                    ASTNode index = expression();
                    consume(TokenType.RBRACKET, "Expect ']' after index.");
                    node = new PropertyAccessNode(node, null, index);
                } else if (match(TokenType.COLON)) {
                    if (!match(TokenType.IDENTIFIER)) {
                        throw error("Expect property name after ':'");
                    }
                    Token name = previous();
                    String namespace = id.lexeme;
                    String funcName = name.lexeme;
                    List<ASTNode> args = new ArrayList<>();
                    if (!check(TokenType.RPAREN)) {
                        do {
                            args.add(expression());
                        } while (match(TokenType.COMMA));
                    }
                    if (!check(TokenType.RPAREN)) {
                        throw error("Expect ')' after arguments.");
                    }
                    node = new FunctionCallNode(namespace, funcName, args);
                    break;
                } else if (match(TokenType.LPAREN)) {
                    List<ASTNode> args = new ArrayList<>();
                    if (!check(TokenType.RPAREN)) {
                        do {
                            args.add(expression());
                        } while (match(TokenType.COMMA));
                    }
                    consume(TokenType.RPAREN, "Expect ')' after arguments.");
                    String fullName = id.lexeme;
                    String namespace = null;
                    String funcName = fullName;
                    if (fullName.contains(":")) {
                        String[] parts = fullName.split(":", 2);
                        namespace = parts[0];
                        funcName = parts[1];
                    }
                    node = new FunctionCallNode(namespace, funcName, args);
                    break;
                } else {
                    break;
                }
            }

            return node;
        }

        private boolean match(TokenType... types) {
            for (TokenType type : types) {
                if (check(type)) {
                    advance();
                    return true;
                }
            }
            return false;
        }

        private Token consume(TokenType type, String message) {
            if (check(type)) return advance();
            throw error(message);
        }

        private boolean check(TokenType type) {
            if (isAtEnd()) return false;
            return peek().tokenType == type;
        }

        private Token advance() {
            if (!isAtEnd()) current++;
            return previous();
        }

        private boolean isAtEnd() {
            return peek().tokenType == TokenType.EOF;
        }

        private Token peek() {
            return tokens.get(current);
        }

        private Token previous() {
            return tokens.get(current - 1);
        }

        private RuntimeException error(String message) {
            System.err.println("[line " + peek().line + "] Error: " + message);
            return new IllegalArgumentException(message);
        }
    }
    // endregion

    // region === Context & Function Registry ===
    public static class Context {
        private final Map<String, Object> variables = new HashMap<>();
        private final Map<String, Map<String, Function<List<Object>, Object>>> functions = new HashMap<>();

        public void setVariable(String name, Object value) {
            variables.put(name, value);
        }

        public Object get(String name) {
            return variables.get(name);
        }

        public void registerFunction(String namespace, String name, Function<List<Object>, Object> impl) {
            functions.computeIfAbsent(namespace, k -> new HashMap<>()).put(name, impl);
        }

        public Object callFunction(String namespace, String name, List<Object> args) {
            if (namespace == null) namespace = "fn";
            Map<String, Function<List<Object>, Object>> ns = functions.get(namespace);
            if (ns != null && ns.containsKey(name)) {
                return ns.get(name).apply(args);
            }
            throw new IllegalArgumentException("Function not found: " + namespace + ":" + name);
        }
    }
    // endregion

    // region === Main Engine ===
    private static final Map<String, ASTNode> cache = new ConcurrentHashMap<>();

    public static Object eval(String expr, Context ctx) {
        if (expr == null || expr.trim().isEmpty()) return null;
        expr = expr.trim();
        return cache.computeIfAbsent(expr, e -> {
            Lexer lexer = new Lexer(e);
            Parser parser = new Parser(lexer.scanTokens());
            ASTNode parse = parser.parse();
            System.out.println("[parse] " + parse);
            return parse;
        }).evaluate(ctx);
    }

    public static void clearCache() {
        cache.clear();
    }
    // endregion

    // region === Test Example ===
    public static void main(String[] args) {
        Context ctx = new Context();
        ctx.setVariable("age", 25);
        ctx.setVariable("score", 75);
        ctx.setVariable("name", "Bob");
        ctx.setVariable("active", false);

        Map<String, Object> user = new HashMap<>();
        user.put("level", 3);
        user.put("tags", Arrays.asList("vip", "new"));
        ctx.setVariable("user", user);

        ctx.registerFunction("fn", "len", as -> {
            Object o = as.get(0);
            if (o instanceof Collection) return ((Collection<?>) o).size();
            if (o instanceof Map) return ((Map<?, ?>) o).size();
            if (o != null) return o.toString().length();
            return 0;
        });

        ctx.registerFunction("str", "contains", as -> {
            String str = as.get(0).toString();
            String sub = as.get(1).toString();
            return str.contains(sub);
        });

        // 测试三元表达式
//        System.out.println(eval("age > 18 ? \"adult\" : \"minor\"", ctx)); // adult
//        System.out.println(eval("score > 80 ? 'A' : score > 60 ? 'B' : 'C'", ctx)); // B
//        System.out.println(eval("active ? user.tags[0] : 'guest'", ctx)); // guest
        System.out.println(eval("fn:len(name)", ctx)); // 3
        System.out.println(eval("str:contains(user.tags[0], 'vip') ? 'yes' : 'no'", ctx)); // yes
//        System.out.println(eval("user.level >= 3 ? 'Premium' : 'Basic'", ctx)); // Premium
    }
    // endregion
}