package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

@Getter
public class AuthorizationException extends CustomException {

    private final ResultCode code;

    public AuthorizationException(ResultCode resultCode) {
        super(resultCode);
        this.code = resultCode;
    }

}
