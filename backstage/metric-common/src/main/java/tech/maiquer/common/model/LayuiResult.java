
package tech.maiquer.common.model;

import lombok.Data;

@Data
public class LayuiResult<T> {
    private Integer code;

    private String message;

    private T data;

    private Long count;

    private LayuiResult() {
    }

    private LayuiResult(Integer code, String message, T data, Long count) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.count = count;
    }

    public static LayuiResult<Object> success() {
        return new LayuiResult(0, "success", null, null);
    }

    public static LayuiResult<Object> fail() {
        return new LayuiResult(-1, "fail", null, null);
    }

    public static LayuiResult<Object> fail(String message) {
        return new LayuiResult(-1, message, null, null);
    }

    public static LayuiResult<Object> success(Object data, Long count) {
        return new LayuiResult(0, "success", data, count);
    }
}
