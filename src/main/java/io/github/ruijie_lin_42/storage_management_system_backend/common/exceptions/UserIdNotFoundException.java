package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

public class UserIdNotFoundException extends org.springframework.security.core.AuthenticationException {
    /**
     * Constructs a <code>UserIdNotFoundException</code> with the specified message.
     *
     * @param msg the detail message.
     */
    public UserIdNotFoundException(String msg) {
        super(msg);
    }

}
