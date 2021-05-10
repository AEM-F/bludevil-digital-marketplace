package nl.fhict.digitalmarketplace.customException;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenMissingException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public AccessTokenMissingException(String msg) {
        super(msg);
    }
}
