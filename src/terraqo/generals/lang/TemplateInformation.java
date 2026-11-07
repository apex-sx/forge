/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.Kit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

/**
 * 模板信息
 *
 * @author potatoxf
 */
public class TemplateInformation {
    /**
     * 模板名称
     */
    private final String templateName;
    /**
     * 模板路径
     */
    private final String[] templatePathList;
    /**
     * 目标名称
     */
    private final String targetRoot;
    /**
     * 目标路径
     */
    private final String targetPath;
    /**
     * 目标名称
     */
    private final String targetName;
    /**
     * 数据
     */
    private final Map<String, Object> context;

    public static Builder ofBuilder() {
        return new Builder();
    }

    public TemplateInformation(String templateName, Map<String, Object> context, Builder builder) {
        this.templateName = Objects.requireNonNull(templateName);
        this.context = Collections.unmodifiableMap(context);
        this.targetRoot = Objects.requireNonNull(builder.targetRoot);
        this.templatePathList = builder.templatePathList.apply(this.templateName, this.context);
        this.targetPath = builder.targetPath.apply(this.templateName, this.context);
        this.targetName = builder.targetName.apply(this.templateName, this.context);
    }

    public String getTemplateName() {
        return templateName;
    }

    public String[] getTemplatePathList() {
        return templatePathList;
    }

    public Path getTarget() {
        return Paths.get(getTargetRoot(), getTargetPath(), getTargetName());
    }

    public String getTargetRoot() {
        return targetRoot;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public String getTargetName() {
        return targetName;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public static final class Builder {
        /**
         * 模板名称
         */
        private List<String> templateNameList;
        /**
         * 模板路径
         */
        private BiFunction<String, Map<String, Object>, String[]> templatePathList;
        /**
         * 目标根路径
         */
        private String targetRoot;
        /**
         * 目标路径
         */
        private BiFunction<String, Map<String, Object>, String> targetPath;
        /**
         * 目标名称
         */
        private BiFunction<String, Map<String, Object>, String> targetName;

        private Builder() {
        }

        public Builder addTemplateName(String... templateNames) {
            this.templateNameList = Kit.initOrUpdateList(this.templateNameList, templateNames);
            return this;
        }

        public Builder setTemplatePathList(BiFunction<String, Map<String, Object>, String[]> templatePathList) {
            this.templatePathList = templatePathList;
            return this;
        }

        public final Builder setTargetRoot(String targetRoot) {
            this.targetRoot = targetRoot;
            return this;
        }

        public final Builder setTargetPath(BiFunction<String, Map<String, Object>, String> targetPath) {
            this.targetPath = targetPath;
            return this;
        }

        public final Builder setTargetName(BiFunction<String, Map<String, Object>, String> targetName) {
            this.targetName = targetName;
            return this;
        }

        @SafeVarargs
        public final List<TemplateInformation> build(Map<String, Object>... contextList) {
            return this.build(Arrays.asList(contextList));
        }

        public final List<TemplateInformation> build(Collection<? extends Map<String, Object>> contextList) {
            List<TemplateInformation> list = new ArrayList<>(contextList.size());
            for (Map<String, Object> context : contextList) {
                for (String templateName : this.templateNameList) {
                    list.add(new TemplateInformation(templateName, context, this));
                }
            }
            return list;
        }
    }

    @Override
    public String toString() {
        return "TemplateInformation{" +
                "templateName='" + templateName + '\'' +
                ", templatePathList=" + Arrays.toString(templatePathList) +
                ", targetRoot='" + targetRoot + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", targetName='" + targetName + '\'' +
                '}';
    }
}
