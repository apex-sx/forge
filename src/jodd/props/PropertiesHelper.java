package jodd.props;

import java.io.IOException;
import java.util.*;

/**
 * 将键值Map转换为 Jodd Props 配置格式
 * Jodd Props profile语法：key<profile>=value
 */
public class PropertiesHelper {

    /**
     * 配置项索引记录：保存profile名称与对应的值
     */
    private record ProfileItem(String profileName, String value) {
    }

    /**
     * 将基础配置与多环境Profile配置，输出为Jodd Props格式文本
     * <p>输出顺序：
     * 1. 先输出基础配置项；基础配置每一项之后，紧跟各profile中对该key的覆盖值 key<profile>=xxx
     * 2. 再输出profile独有的配置项（该key不存在于基础配置中）
     * </p>
     * <p>性能优化：预先构建key -> profile列表索引，避免嵌套循环遍历全部profile，
     * 将时间复杂度从 O(B*P) 优化为 O(B+P)
     * </p>
     *
     * @param appendable     输出目标（支持Writer、StringBuilder等实现Appendable的对象）
     * @param baseProperties 基础配置键值对，允许null
     * @param profiles       多环境配置：profile名称 -> 该环境下的键值对，允许null
     * @throws IOException 当appendable写入发生IO异常时抛出
     */
    public static void writeAsJoddProps(Appendable appendable,
                                        Map<String, String> baseProperties,
                                        Map<String, Map<String, String>> profiles) throws IOException {
        // null安全兜底
        Map<String, String> baseMap = baseProperties == null ? Collections.emptyMap() : baseProperties;
        Map<String, Map<String, String>> rawProfiles = profiles == null ? Collections.emptyMap() : profiles;
        Set<String> baseKeySet = baseMap.keySet();

        // ========== 预处理：构建全局索引 key -> 该key下所有profile条目 ==========
        Map<String, List<ProfileItem>> keyProfileIndex = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> profileEntry : rawProfiles.entrySet()) {
            String profileName = profileEntry.getKey();
            Map<String, String> envProps = profileEntry.getValue();
            if (envProps == null || envProps.isEmpty()) {
                continue;
            }
            for (Map.Entry<String, String> envEntry : envProps.entrySet()) {
                String key = envEntry.getKey();
                String val = envEntry.getValue();
                keyProfileIndex.computeIfAbsent(key, k -> new ArrayList<>())
                        .add(new ProfileItem(profileName, val));
            }
        }

        // 1. 输出基础配置，以及该key对应的所有profile覆盖配置（直接查索引，不再遍历全部profile）
        for (Map.Entry<String, String> baseEntry : baseMap.entrySet()) {
            String key = baseEntry.getKey();
            String baseVal = baseEntry.getValue();
            appendBaseLine(appendable, key, baseVal);

            List<ProfileItem> profileItems = keyProfileIndex.get(key);
            if (profileItems != null && !profileItems.isEmpty()) {
                for (ProfileItem item : profileItems) {
                    appendProfileLine(appendable, key, item.profileName(), item.value());
                }
            }
        }

        // 2. 输出profile独有的key（不在基础配置内）
        for (Map.Entry<String, Map<String, String>> profileEntry : rawProfiles.entrySet()) {
            String profileName = profileEntry.getKey();
            Map<String, String> envProps = profileEntry.getValue();
            if (envProps == null) {
                continue;
            }
            for (Map.Entry<String, String> envEntry : envProps.entrySet()) {
                String key = envEntry.getKey();
                if (baseKeySet.contains(key)) {
                    continue;
                }
                appendProfileLine(appendable, key, profileName, envEntry.getValue());
            }
        }
    }

    /**
     * 重载方法：无profile环境配置，仅输出基础配置
     *
     * @param appendable     输出目标
     * @param baseProperties 基础配置键值对，允许null
     * @throws IOException 当appendable写入发生IO异常时抛出
     */
    public static void writeAsJoddProps(Appendable appendable,
                                        Map<String, String> baseProperties) throws IOException {
        writeAsJoddProps(appendable, baseProperties, null);
    }

    /**
     * 写入基础配置行：key=value\n
     */
    private static void appendBaseLine(Appendable appendable, String key, String value) throws IOException {
        appendable.append(key);
        appendable.append('=');
        appendable.append(value);
        appendable.append('\n');
    }

    /**
     * 写入profile配置行：key<profile>=value\n
     */
    private static void appendProfileLine(Appendable appendable,
                                          String key,
                                          String profileName,
                                          String value) throws IOException {
        appendable.append(key);
        appendable.append('<');
        appendable.append(profileName);
        appendable.append('>');
        appendable.append('=');
        appendable.append(value);
        appendable.append('\n');
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> base = Map.of(
                "app.name", "demo-app",
                "db.url", "jdbc:mysql://base"
        );

        Map<String, Map<String, String>> profiles = Map.of(
                "dev", Map.of(
                        "db.url", "jdbc:mysql://dev",
                        "log.level", "debug"
                ),
                "test", Map.of(
                        "db.url", "jdbc:mysql://test",
                        "log.level", "info"
                )
        );

        StringBuilder sb = new StringBuilder();
        writeAsJoddProps(sb, base, profiles);
        System.out.println(sb);
    }
}