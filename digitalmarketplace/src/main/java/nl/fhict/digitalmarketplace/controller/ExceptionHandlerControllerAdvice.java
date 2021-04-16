package nl.fhict.digitalmarketplace.controller;

import nl.fhict.digitalmarketplace.customException.ExistingResourceException;
import nl.fhict.digitalmarketplace.customException.FileException;
import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody MessageDTO handleResourceNotFound(final ResourceNotFoundException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody MessageDTO handleInvalidInput(final InvalidInputException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody MessageDTO handleNumberFormat(final NumberFormatException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(FileException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody MessageDTO handleFile(final FileException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody MessageDTO handleIO(final IOException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(ExistingResourceException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody MessageDTO handleExistingResource(final ExistingResourceException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }
}
