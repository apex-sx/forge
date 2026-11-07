package apexsx.forge.lang.meta;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Terraqo
 */
public class Metadata {
    private static final String EXP_NAME_SEGMENT = "([a-zA-Z][a-zA-Z0-9]*)";
    private static final String EXP_NAME = "(" + EXP_NAME_SEGMENT + "(_|" + EXP_NAME_SEGMENT + ")*)";
    private static final Pattern PATTERN_NAME = Pattern.compile(EXP_NAME);
    private static final Pattern PATTERN_PATH = Pattern.compile(EXP_NAME + "(\\." + EXP_NAME + ")*");

    public static boolean isValidName(String name) {
        return PATTERN_NAME.matcher(name).matches();
    }

    public static String checkName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        return name.toUpperCase();
    }

    public static boolean isValidPath(String referencePath) {
        return PATTERN_PATH.matcher(referencePath).matches();
    }

    public static String checkPath(String referencePath) {
        if (!isValidPath(referencePath)) {
            throw new IllegalArgumentException("Invalid reference path: " + referencePath);
        }
        return referencePath.toLowerCase();
    }


    public static String toCamelCase(String name, boolean upperCase) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        String[] parts = name.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }

            if (i == 0 && !upperCase) {
                result.append(part);
            } else {
                result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }

        return result.toString();
    }

    @Data
    public static final class Module {
        private String author;
        private String moduleName;
        private String modulePath;
        private String moduleDesc;
        private LocalDateTime moduleTime;
        private List<Specie> species = Collections.emptyList();

        public Module setModuleName(String moduleName) {
            this.moduleName = checkName(moduleName);
            return this;
        }

        public Module setModulePath(String modulePath) {
            this.modulePath = checkPath(modulePath);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Module that = (Module) o;
            return Objects.equals(moduleName, that.moduleName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(moduleName);
        }

        @Override
        public String toString() {
            return "[" + moduleName + "]";
        }
    }

    @Data
    public static final class Specie {
        private String author;
        private String moduleName;
        private String specieName;
        private String speciePath;
        private String specieDesc;
        private LocalDateTime specieTime;

        public Specie setModuleName(String moduleName) {
            this.moduleName = Metadata.checkName(moduleName);
            return this;
        }

        public Specie setSpecieName(String specieName) {
            this.specieName = Metadata.checkName(specieName);
            return this;
        }

        public Specie setSpeciePath(String speciePath) {
            this.speciePath = Metadata.checkPath(speciePath);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Specie that = (Specie) o;
            return Objects.equals(moduleName, that.moduleName) && Objects.equals(specieName, that.specieName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(moduleName);
        }

        @Override
        public String toString() {
            return "[" + moduleName + "]" + "[" + specieName + "]";
        }
    }

    @Data
    public static final class Table {
        private String tableName;
        private String tableDesc;
        private String moduleName;
        private String specieName;
        private String entityName;
        private String className;
        private List<Column> columns = Collections.emptyList();

        public Table setModuleName(String moduleName) {
            this.moduleName = Metadata.checkName(moduleName);
            return this;
        }

        public Table setSpecieName(String specieName) {
            this.specieName = Metadata.checkName(specieName);
            return this;
        }

        public Table setTableName(String tableName) {
            this.tableName = Metadata.checkName(tableName);
            this.entityName = Metadata.checkName(tableName);
            this.className = Metadata.toCamelCase(this.tableName, true);
            return this;
        }
    }

    @Data
    public static final class Column {
        private String tableName;
        private String fieldName;
        private String columnName;
        private String columnDesc;
        private DataType columnType;
        private Integer columnSize;
        private Integer columnPres;
        private Boolean isNullable;

        public Column setTableName(String tableName) {
            this.tableName = Metadata.checkName(tableName);
            return this;
        }

        public Column setColumnName(String columnName) {
            this.columnName = Metadata.checkName(columnName);
            this.fieldName = Metadata.toCamelCase(this.columnName, false);
            return this;
        }

        public Column setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Column setNullable(boolean nullable) {
            isNullable = nullable;
            return this;
        }
    }
}
