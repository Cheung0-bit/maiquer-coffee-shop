package tech.maiquer.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Result", description = "响应实体封装类")
public class Result<T> implements Serializable {

    @ApiModelProperty(value = "状态码", name = "code")
    private Integer code;

    @ApiModelProperty(value = "响应信息", name = "message")
    private String message;

    @ApiModelProperty(value = "实体类数据", name = "data")
    private T data;

    public T getData(){
        return this.data;
    }

    public Result(String message) {
        this.code = 1;
        this.message = message;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static Result success() {
        Result result = new Result(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result(ResultCode.SUCCESS, data);
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result(resultCode);
        return result;
    }

    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result(resultCode, data);
        return result;
    }

    public static Result failure(String message) {
        Result result = new Result(message);
        return result;
    }


}
