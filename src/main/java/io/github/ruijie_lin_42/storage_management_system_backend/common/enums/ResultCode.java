package io.github.ruijie_lin_42.storage_management_system_backend.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    // success
    SUCCESS(0, "Success"),

    // =========================== CUSTOM ERROR CODE PATTERN ===========================
    // xxx xx xxx -> http code | module | error number
    // http code -> classification
    // module -> separate error origin
    // error number -> represents exact error

    // =========================== 99 COMMON ERROR CODE ===========================
    // | 400... | BAD_REQUEST |
    PARAM_ERROR(40099001, "Parameter error"),
    JSON_SYNTAX_ERROR(40099002, "JSON syntax error"),
    ARGUMENT_TYPE_MISMATCH(40099003, "Unexpected parameter type"),
    ARGUMENT_NOT_VALID(40099004, "Argument contains invalid values"),
    // | 401... | UNAUTHORIZED |
    UNAUTHORIZED(40199001, "Unauthorized"),
    // | 403... | FORBIDDEN |
    FORBIDDEN(40399001, "Forbidden"),
    INSUFFICIENT_PRIVILEGE(40399002, "Current user do not have sufficient privilege for current operation"),
    // | 404... | NOT_FOUND |
    NOT_FOUND(40499001, "Not found"),
    // | 409... | CONFLICT |
    CONFLICT(40999001, "Resource duplicated"),
    // | 500... | INTERNAL_SERVER_ERROR |
    SYSTEM_ERROR(50099001, "System error, please try again later"),
    DATABASE_CONNECTION_FAILED(50099002, "system busy, please try again later"),
    INSERT_AFFECTED_ROWS_INVALID(50099003, "System error, please try again later"),
    UPDATE_AFFECTED_ROWS_INVALID(50099004, "System error, please try again later"),
    DELETE_AFFECTED_ROWS_INVALID(50099005, "System error, please try again later"),
    UNKNOWN_ERROR(50099999, "System encounters an unexpected exception, please contact support and try later"),

    // =========================== 01 USER MODULE ===========================
    // | 404... | NOT_FOUND |
    USER_UNAVAILABLE(40401001, "Requested user unavailable"),
    // | 409... | CONFLICT |
    DUPLICATE_USERNAME(40901001, "Username already exists"),

    // =========================== 02 AUTH MODULE ===========================
    // | 400... | BAD_REQUEST |
    LOGIN_FAIL(40002001, "Username or password incorrect"),
    // | 401... | UNAUTHORIZED |
    INVALID_TOKEN(40102001, "Token invalid"),
    // | 404... | NOT_FOUND |
    USER_NOT_FOUND(40402001, "User not found"),

    // =========================== 03 STORAGE MODULE ===========================
    // | 404... | NOT_FOUND |
    STORAGE_UNAVAILABLE(40403001, "Requested storage unavailable"),
    // | 409... | CONFLICT |
    DUPLICATE_STORAGE_NAME(40903001, "Storage name already exists"),

    // =========================== 04 ITEMS MODULE ===========================
    // | 400... | BAD_REQUEST |
    INVALID_INCREMENT(40004001, "Increment of stock invalid"),
    INVALID_DECREMENT(40004002, "Decrement of stock invalid"),
    INVALID_ITEM_STOCK(40004003, "Item does not have enough stock"),
    // | 404... | NOT_FOUND
    ITEM_NOT_FOUND(40404001, "Item does not exist"),

    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
