package com.example.ms.model.vo;

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

    //Used when you don't need to return data
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

    public Result(Integer code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    /* * Call the ResultCode class to encapsulate commonly used return data */
    public static Result SUCCESS() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result SUCCESS(Object data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    public static Result ERROR() {
        return new Result(ResultCode.SERVER_ERROR);
    }

    // WARNING
    public static Result FAILED() {
        return new Result(ResultCode.FAILED);
    }
}
