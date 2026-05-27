package io.github.ruijie_lin_42.storage_management_system_backend.common.enums;

public enum ResultCode {

    // 2xx成功
    // 3xx重定向
    // 4xx客户端错误
    // 5xx服务端错误
    // Defaults
    SUCCESS(0, "success"),
    PARAM_ERROR(400, "parameter error"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    SYSTEM_ERROR(500, "system error"),

    // TODO: add custom result codes here
    // 10001 - 19999 user exception
    // 101xx parameter invalid
    DUPLICATE_USERNAME(10101, "username already exists"),
    USER_UNAVAILABLE(10102, "requested user unavailable"),


    // 90001 - 99999 system exception
    INSERT_AFFECTED_ROWS_INVALID(90001, "insert failed, affected multiple rows"),
    UPDATE_AFFECTED_ROWS_INVALID(90002, "update failed, affected multiple rows"),
    DELETE_AFFECTED_ROWS_INVALID(90003, "delete failed, affected multiple rows"),

    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
