package nl.fhict.digitalmarketplace.controller;

import nl.fhict.digitalmarketplace.customException.*;
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
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody MessageDTO handleResourceNotFound(final ResourceNotFoundException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
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

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public @ResponseBody MessageDTO handleTokenRefreshException(final TokenRefreshException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

    @ExceptionHandler(AccessTokenMissingException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody MessageDTO handleTokenRefreshException(final AccessTokenMissingException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }
    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody MessageDTO handleTokenRefreshException(final AccessTokenExpiredException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }
    @ExceptionHandler(AccessTokenMalformedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody MessageDTO handleTokenRefreshException(final AccessTokenMalformedException exception, final HttpServletRequest request){
        return new MessageDTO(exception.getMessage(), MessageDTO.errorType, request.getRequestURI());
    }

}
