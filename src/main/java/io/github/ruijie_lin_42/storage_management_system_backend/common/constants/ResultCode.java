package io.github.ruijie_lin_42.storage_management_system_backend.common.constants;

public enum ResultCode {

    // 2xx成功
    // 3xx重定向
    // 4xx客户端错误
    // 5xx服务端错误
    SUCCESS(200, "success"),
    PARAM_ERROR(400, "parameter error"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    SYSTEM_ERROR(500, "system error");

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
