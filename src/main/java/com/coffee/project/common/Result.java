package com.coffee.project.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果类，用于封装接口返回数据。
 * 提供统一的返回格式：状态码、消息、数据。
 *
 * @param <T> 泛型参数，用于表示返回数据的具体类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    /**
     * 返回状态码
     * - 200 表示操作成功
     * - 500 表示操作失败
     * - 可自定义其他状态码
     */
    private Integer code;

    /**
     * 返回消息，描述操作结果或失败原因
     */
    private String message;

    /**
     * 返回数据，泛型类型 T 可以根据需要返回任意类型
     */
    private T data;

    /* ==================== 成功方法 ==================== */

    /**
     * 返回成功结果，默认消息为 "操作成功"
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 返回成功结果，并附带数据
     *
     * @param data 返回数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 返回成功结果，可自定义消息和数据
     *
     * @param data    返回数据
     * @param message 自定义消息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data);
    }

    /**
     * 返回成功结果，可自定义状态码、消息和数据
     */
    public static <T> Result<T> success(Integer code, T data, String message) {
        return new Result<>(code, message, data);
    }

    /* ==================== 失败方法 ==================== */

    /**
     * 返回失败结果，默认状态码 500，消息为 "操作失败"
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }

    /**
     * 返回失败结果，可自定义消息
     *
     * @param message 失败原因
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 返回失败结果，可自定义状态码和消息
     *
     * @param code    状态码
     * @param message 失败原因
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 返回失败结果，可自定义状态码、消息和数据
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
