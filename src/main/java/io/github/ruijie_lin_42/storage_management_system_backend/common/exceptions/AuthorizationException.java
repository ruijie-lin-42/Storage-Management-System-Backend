package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthorizationException extends CustomException {

    private final String operation;

    public AuthorizationException(ResultCode resultCode, String operation, Map<String, Object> context) {
        super(resultCode, context);
        this.operation = operation;
    }

}
