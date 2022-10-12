package com.example.ms.model;

import com.example.ms.common.constant.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {

    private boolean success;
    private Integer code;
    private String message;
    private Object data;

    // Used when you don't need to return data
    public Result(ResultCode code) {
        this.success = code.success;
        this.code = code.code;
        this.message = code.message;
    }

    public Result(ResultCode code, Object data) {
        this.success = code.success;
        this.code = code.code;
        this.message = code.message;
        this.data = data;
    }

    public Result(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    public static Result failure() {
        return new Result(ResultCode.FAILURE);
    }

    public static Result failure(String message) {
        return new Result(ResultCode.ERROR.success, ResultCode.ERROR.code, message);
    }

}
