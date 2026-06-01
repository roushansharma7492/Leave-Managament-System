package com.leavemgmt.exception;

import com.leavemgmt.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandler - Handles exceptions globally across the application
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setError("NOT_FOUND");
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setError("UNAUTHORIZED");
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessLogicException(
            BusinessLogicException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setError("BUSINESS_LOGIC_ERROR");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
            errors.add(error.getDefaultMessage())
        );
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage("Validation Failed: " + String.join(", ", errors));
        errorResponse.setError("VALIDATION_ERROR");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setError("INTERNAL_SERVER_ERROR");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
