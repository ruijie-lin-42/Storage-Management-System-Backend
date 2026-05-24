package io.github.ruijie_lin_42.storage_management_system_backend.common.result;

import io.github.ruijie_lin_42.storage_management_system_backend.common.constants.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public static <T> Result<T> of(ResultCode resultCode, T data){
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data){
        return Result.of(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> error(ResultCode resultCode){
        return Result.of(resultCode, null);
    }

}
