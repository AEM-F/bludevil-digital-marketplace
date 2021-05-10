package nl.fhict.digitalmarketplace.customException;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenMalformedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public AccessTokenMalformedException(String msg) {
        super(msg);
    }
}
