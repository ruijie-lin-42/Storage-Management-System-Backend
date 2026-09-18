package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class DataIntegrityException extends CustomException{

    private final int expectedRows;
    private final int actualRows;
    private final String operation;

    public DataIntegrityException(ResultCode resultCode, int expectedRows, int actualRows, String operation, Map<String, Object> context){
        super(resultCode, context);
        this.expectedRows = expectedRows;
        this.actualRows = actualRows;
        this.operation = operation;
    }

}
