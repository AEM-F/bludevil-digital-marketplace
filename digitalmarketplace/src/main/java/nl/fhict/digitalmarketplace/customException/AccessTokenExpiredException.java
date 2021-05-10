package nl.fhict.digitalmarketplace.customException;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenExpiredException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public AccessTokenExpiredException(String msg) {
        super(msg);
    }
}
