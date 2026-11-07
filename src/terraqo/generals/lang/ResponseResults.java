/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.symbolic.LiteralConst;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Api调用返回结果
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class ResponseResults<T> implements LiteralConst<ResponseResults<T>> {
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final String MESSAGE_FAILURE = "操作失败！", MESSAGE_SUCCESS = "操作成功！";
    private static final int CODE_FAILURE = 1, CODE_SUCCESS = 0;
    /**
     * 是否成功
     */
    private final boolean success;
    /**
     * 编码
     */
    private final int code;
    /**
     * 数据
     */
    private final T data;
    /**
     * 总记录数
     */
    private long total;
    /**
     * i18n内容
     */
    private final transient SupportI18n i18nContent;

    protected ResponseResults(boolean success, int code, T data, String message, Object... args) {
        this(success, code, data, null, message, args);
    }

    protected ResponseResults(boolean success, int code, T data, String baseName, String message, Object... args) {
        this.success = success;
        this.code = code;
        this.data = data;
        if (data == null) {
            this.total = 0;
        } else if (data instanceof Collection) {
            this.total = ((Collection<?>) data).size();
        } else if (data.getClass().isArray()) {
            this.total = Array.getLength(data);
        } else {
            this.total = 1;
        }
        this.i18nContent = SupportI18n.ofExpression(baseName, message, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //失败
    //------------------------------------------------------------------------------------------------------------------

    public static ResponseResults<Map<String, Object>> errorForMap(int code) {
        return ResponseResults.ofError(code, new HashMap<>(4, 1), MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> errorForMap() {
        return ResponseResults.errorForMap(CODE_FAILURE, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> errorForMap(String message, Object... args) {
        return ResponseResults.errorForMap(CODE_FAILURE, message, args);
    }

    public static ResponseResults<Map<String, Object>> errorForMap(int code, String message, Object... args) {
        return ResponseResults.ofError(code, new HashMap<>(4, 1), message, args);
    }

    public static <T> ResponseResults<T> errorForData(T data) {
        return ResponseResults.ofError(CODE_FAILURE, data, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> error() {
        return ResponseResults.ofError(CODE_FAILURE, null, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> error(String message, Object... args) {
        return ResponseResults.ofError(CODE_FAILURE, null, message, args);
    }

    public static <T> ResponseResults<T> error(T data, String message, Object... args) {
        return ResponseResults.ofError(CODE_FAILURE, data, message, args);
    }

    public static ResponseResults<Map<String, Object>> failureForMap(int code) {
        return ResponseResults.ofError(code, new HashMap<>(4, 1), MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> failureForMap() {
        return ResponseResults.failureForMap(CODE_FAILURE, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> failureForMap(String message, Object... args) {
        return ResponseResults.failureForMap(CODE_FAILURE, message, args);
    }

    public static ResponseResults<Map<String, Object>> failureForMap(int code, String message, Object... args) {
        return ResponseResults.ofError(code, new HashMap<>(4, 1), message, args);
    }

    public static <T> ResponseResults<T> failureForData(T data) {
        return ResponseResults.ofError(CODE_FAILURE, data, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> failure() {
        return ResponseResults.ofError(CODE_FAILURE, null, MESSAGE_FAILURE, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> failure(String message, Object... args) {
        return ResponseResults.ofError(CODE_FAILURE, null, message, args);
    }

    public static <T> ResponseResults<T> failure(T data, String message, Object... args) {
        return ResponseResults.ofError(CODE_FAILURE, data, message, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //成功
    //------------------------------------------------------------------------------------------------------------------

    public static ResponseResults<Map<String, Object>> successForMap(int code) {
        return ResponseResults.ofSuccess(code, new HashMap<>(4, 1), MESSAGE_SUCCESS, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> successForMap() {
        return ResponseResults.successForMap(CODE_SUCCESS, MESSAGE_SUCCESS, EMPTY_OBJECT_ARRAY);
    }

    public static ResponseResults<Map<String, Object>> successForMap(String message, Object... args) {
        return ResponseResults.successForMap(CODE_SUCCESS, message, args);
    }

    public static ResponseResults<Map<String, Object>> successForMap(int code, String message, Object... args) {
        return ResponseResults.ofSuccess(code, new HashMap<>(4, 1), message, args);
    }

    public static <T> ResponseResults<T> successForData(T data) {
        return ResponseResults.ofSuccess(CODE_SUCCESS, data, MESSAGE_SUCCESS, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> success() {
        return ResponseResults.ofSuccess(CODE_SUCCESS, null, MESSAGE_SUCCESS, EMPTY_OBJECT_ARRAY);
    }

    public static <T> ResponseResults<T> success(String message, Object... args) {
        return ResponseResults.ofSuccess(CODE_SUCCESS, null, message, args);
    }

    public static <T> ResponseResults<T> success(T data, String message, Object... args) {
        return ResponseResults.ofSuccess(CODE_SUCCESS, data, message, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //扩展
    //------------------------------------------------------------------------------------------------------------------


    public static <T> ResponseResults<T> ofError(int code, T data, String message, Object... args) {
        return new ResponseResults<>(false, code, data, message, args);
    }
    public static <T> ResponseResults<T> ofSuccess(int code, T data, String message, Object... args) {
        return new ResponseResults<>(true, code, data, message, args);
    }

    public static <T> ResponseResults<T> i18nError(int code, T data, String baseName, String message, Object... args) {
        return new ResponseResults<>(false, code, data, baseName, message, args);
    }

    public static <T> ResponseResults<T> i18nSuccess(int code, T data, String baseName, String message, Object... args) {
        return new ResponseResults<>(true, code, data, baseName, message, args);
    }

    /**
     * 获取{@code SupportI18n}
     *
     * @return 返回{@code SupportI18n}
     */
    @Override
    public SupportI18n supportI18n() {
        return i18nContent;
    }

    /**
     * 获取API结果码
     *
     * @return {@code int}
     */
    @Override
    public int getCode() {
        return code;
    }

    /**
     * 通过唯一名称查找
     *
     * @return 返回名称
     */
    @Override
    public String getName() {
        return String.valueOf(code);
    }

    /**
     * 判断返回结果是否成功
     *
     * @return {@code boolean}
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 判断返回结果是否失败
     *
     * @return {@code boolean}
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * 判断返回结果是否失败
     *
     * @return {@code boolean}
     */
    public boolean isError() {
        return !success;
    }

    /**
     * 获取API结果信息
     *
     * @return {@code int}
     */
    public String getMessage() {
        return i18nContent.getContent();
    }

    /**
     * 获取API结果数据
     *
     * @return {@code T}
     */
    public T getData() {
        return data;
    }

    /**
     * 获取数据总记录数
     *
     * @return {@code long}
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置数据总记录数
     *
     * @param total {@code long}
     */
    public ResponseResults<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        SimpleJSONObjectStringBuilder jsonBuilder = new SimpleJSONObjectStringBuilder();
        jsonBuilder.add("success", isSuccess());
        jsonBuilder.add("failure", isFailure());
        jsonBuilder.add("error", isError());
        jsonBuilder.add("code", getCode());
        jsonBuilder.add("name", getName());
        jsonBuilder.add("total", getTotal());
        jsonBuilder.add("message", getMessage());
        jsonBuilder.add("content", getContent());
        return jsonBuilder.toString();
    }
}
