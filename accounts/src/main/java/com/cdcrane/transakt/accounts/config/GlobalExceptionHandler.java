package com.cdcrane.transakt.accounts.config;

import com.cdcrane.transakt.accounts.dto.ExceptionErrorResponse;
import com.cdcrane.transakt.accounts.exception.CannotOpenAnotherAccountException;
import com.cdcrane.transakt.accounts.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // -------------------- GENERIC EXCEPTIONS --------------------

    /**
     * This method catches any exceptions not explicitly caught by any other methods in this handler.
     * @param ex The exception thrown.
     * @return Returns a response entity to the user with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrorResponse> handleException(Exception ex) {

        ExceptionErrorResponse error = ExceptionErrorResponse.builder()
                .message("Unknown error occurred. Please contact the developer for assistance.")
                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(System.currentTimeMillis())
                .build();

        log.error("Generic error triggered: {}", ex.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    /**
     * Handle servlet level resource not found errors (Accessing non-existing endpoints, for example)
     * @param ex Exception thrown.
     * @return Response explaining problem.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {

        ExceptionErrorResponse error = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle the case that the gateway server does not provide a valid UUID customer ID.
     * @param ex Exception thrown.
     * @return Response explaining problem.
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ExceptionErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {

        ExceptionErrorResponse error = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // -------------------- SPECIFIC EXCEPTIONS --------------------

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

        ExceptionErrorResponse error = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CannotOpenAnotherAccountException.class)
    public ResponseEntity<ExceptionErrorResponse> handleCannotOpenAnotherAccount(CannotOpenAnotherAccountException ex) {

        ExceptionErrorResponse error = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
