package nl.fhict.digitalmarketplace.customException;

public class TokenRefreshException extends Exception{
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
