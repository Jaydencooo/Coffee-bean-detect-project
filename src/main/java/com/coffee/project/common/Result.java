package com.coffee.project.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *放通用类，供项目其他模块共用。
 * 你现在有一个类：
 * Result：统一返回前端的响应格式，一般用于封装接口返回的数据（成功/失败状态码、消息、数据等）。
 */
/**
 * 通用返回结果类，用于封装接口的返回数据。
 *
 * @param <T> 泛型参数，用于表示返回数据的具体类型。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 返回状态码，用于标识操作是否成功。
     * - 200 表示操作成功。
     * - 500 表示操作失败。
     */
    private Integer code;

    /**
     * 返回消息，用于描述操作的结果或失败原因。
     */
    private String message;

    /**
     * 返回数据，泛型类型 T 可以根据需要返回任意类型的数据。
     */
    private T data;

    /**
     * 创建一个成功的返回结果。
     *
     * @param data 返回的数据
     * @param <T>  数据的类型
     * @return 一个成功的 Result 对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }


    /**
     * 创建一个失败的返回结果。
     *
     * @param message 失败的原因
     * @param <T>     数据的类型（失败时通常为 null）
     * @return 一个失败的 Result 对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}

