package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Result<Void> handleApiException(CustomException e){
        return Result.fail(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        e.printStackTrace();
        return Result.fail(ResultCode.SYSTEM_ERROR);
    }

}
