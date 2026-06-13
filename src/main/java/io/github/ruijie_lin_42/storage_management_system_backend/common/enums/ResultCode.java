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

    /**
     * 1xxx client error
     */

    // 11xx request parameter error
    JSON_SYNTAX_ERROR(1101, "JSON syntax error"),
    ARGUMENT_TYPE_MISMATCH(1102, "unexpected parameter type"),
    ARGUMENT_NOT_VALID(1103, "argument contains invalid values"),

    /**
     * 2xxx business error
     */

    // 21xx user exception
    DUPLICATE_USERNAME(2101, "username already exists"),
    USER_UNAVAILABLE(2102, "requested user unavailable"),

    // 22xx auth exception
    // 221x user authentication invalid
    LOGIN_FAIL(2201, "username or password incorrect"),
    USER_NOT_FOUND(2202, "user not found"),
    // 222x token invalid
    INVALID_TOKEN(2203, "token invalid"),

    // 23xx storage exception
    DUPLICATE_STORAGE_NAME(2301, "storage name already exists"),
    STORAGE_UNAVAILABLE(2302, "requested storage unavailable"),

    // 24xx items exception
    INVALID_INCREMENT(2401, "increment of stock invalid"),
    INVALID_DECREMENT(2402, "decrement of stock invalid"),
    INVALID_ITEM_STOCK(2403, "item does not have enough stock or doesn't exist"),

    // 29xx database integrity exception
    INSERT_AFFECTED_ROWS_INVALID(2901, "insert failed, affected multiple rows"),
    UPDATE_AFFECTED_ROWS_INVALID(2902, "update failed, affected multiple rows"),
    DELETE_AFFECTED_ROWS_INVALID(2903, "delete failed, affected multiple rows"),

    /**
     * 3xxx system error
     */

    // 31xx database exception
    DATABASE_CONNECTION_FAILED(3101, "system busy, please try later"),

    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
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
