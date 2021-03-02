package nl.fhict.digitalmarketplace.customException;

import nl.fhict.digitalmarketplace.model.response.MessageDTO;

public class InvalidInputException extends Exception{
    public InvalidInputException(String message) {
        super(message);
    }
}
