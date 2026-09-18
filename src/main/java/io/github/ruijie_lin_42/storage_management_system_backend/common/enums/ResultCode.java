package io.github.ruijie_lin_42.storage_management_system_backend.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {

    // success
    SUCCESS(0, HttpStatus.OK, "Success"),

    // =========================== CUSTOM ERROR CODE PATTERN ===========================
    // xxx xx xxx -> http code | module | error number
    // http code -> classification
    // module -> separate error origin
    // error number -> represents exact error

    // =========================== 99 COMMON ERROR CODE ===========================
    // | 400... | BAD_REQUEST |
    PARAM_ERROR(40099001, HttpStatus.BAD_REQUEST, "Parameter error"),
    JSON_SYNTAX_ERROR(40099002, HttpStatus.BAD_REQUEST, "JSON syntax error"),
    ARGUMENT_TYPE_MISMATCH(40099003, HttpStatus.BAD_REQUEST, "Unexpected parameter type"),
    ARGUMENT_NOT_VALID(40099004, HttpStatus.BAD_REQUEST, "Argument contains invalid values"),
    // | 401... | UNAUTHORIZED |
    UNAUTHORIZED(40199001, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    // | 403... | FORBIDDEN |
    FORBIDDEN(40399001, HttpStatus.FORBIDDEN, "Forbidden"),
    INSUFFICIENT_PRIVILEGE(40399002, HttpStatus.FORBIDDEN, "Current user do not have sufficient privilege for current operation"),
    // | 404... | NOT_FOUND |
    NOT_FOUND(40499001, HttpStatus.NOT_FOUND, "Not found"),
    // | 409... | CONFLICT |
    CONFLICT(40999001, HttpStatus.CONFLICT, "Resource duplicated"),
    // | 500... | INTERNAL_SERVER_ERROR |
    SYSTEM_ERROR(50099001, HttpStatus.INTERNAL_SERVER_ERROR, "System error, please try again later"),
    DATABASE_CONNECTION_FAILED(50099002, HttpStatus.INTERNAL_SERVER_ERROR, "system busy, please try again later"),
    INSERT_AFFECTED_ROWS_INVALID(50099003, HttpStatus.INTERNAL_SERVER_ERROR, "System error, please try again later"),
    UPDATE_AFFECTED_ROWS_INVALID(50099004, HttpStatus.INTERNAL_SERVER_ERROR, "System error, please try again later"),
    DELETE_AFFECTED_ROWS_INVALID(50099005, HttpStatus.INTERNAL_SERVER_ERROR, "System error, please try again later"),
    UNKNOWN_ERROR(50099999, HttpStatus.INTERNAL_SERVER_ERROR, "System encounters an unexpected exception, please contact support and try later"),

    // =========================== 01 USER MODULE ===========================
    // | 404... | NOT_FOUND |
    USER_UNAVAILABLE(40401001, HttpStatus.NOT_FOUND, "Requested user unavailable"),
    // | 409... | CONFLICT |
    DUPLICATE_USERNAME(40901001, HttpStatus.CONFLICT, "Username already exists"),

    // =========================== 02 AUTH MODULE ===========================
    // | 400... | BAD_REQUEST |
    LOGIN_FAIL(40002001, HttpStatus.BAD_REQUEST, "Username or password incorrect"),
    PASSWORD_RESET_FAIL(40002002, HttpStatus.BAD_REQUEST, "Username or password incorrect"),
    // | 401... | UNAUTHORIZED |
    INVALID_TOKEN(40102001, HttpStatus.UNAUTHORIZED, "Token invalid"),
    // | 404... | NOT_FOUND |
    USER_NOT_FOUND(40402001, HttpStatus.NOT_FOUND, "User not found"),

    // =========================== 03 STORAGE MODULE ===========================
    // | 404... | NOT_FOUND |
    STORAGE_UNAVAILABLE(40403001, HttpStatus.NOT_FOUND, "Requested storage unavailable"),
    // | 409... | CONFLICT |
    DUPLICATE_STORAGE_NAME(40903001, HttpStatus.CONFLICT, "Storage name already exists"),

    // =========================== 04 ITEMS MODULE ===========================
    // | 400... | BAD_REQUEST |
    INVALID_INCREMENT(40004001, HttpStatus.BAD_REQUEST, "Increment of stock invalid"),
    INVALID_DECREMENT(40004002, HttpStatus.BAD_REQUEST, "Decrement of stock invalid"),
    INVALID_ITEM_STOCK(40004003, HttpStatus.BAD_REQUEST, "Item does not have enough stock"),
    // | 404... | NOT_FOUND
    ITEM_NOT_FOUND(40404001, HttpStatus.NOT_FOUND, "Item does not exist"),

    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ResultCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
