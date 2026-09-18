package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;

import java.util.Map;

public class AuthenticationException extends CustomException{

    public AuthenticationException(ResultCode resultCode, Map<String, Object> context){
        super(resultCode, context);
    }

}
