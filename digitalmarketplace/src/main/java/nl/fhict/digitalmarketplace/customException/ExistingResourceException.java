package nl.fhict.digitalmarketplace.customException;

public class ExistingResourceException extends Exception{
    public ExistingResourceException(String message) {
        super(message);
    }
}
