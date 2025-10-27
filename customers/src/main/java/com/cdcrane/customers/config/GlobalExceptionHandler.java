package com.cdcrane.customers.config;

import com.cdcrane.customers.dto.ExceptionErrorResponse;
import com.cdcrane.customers.exception.CustomerNotOldEnoughException;
import com.cdcrane.customers.exception.EmailTakenException;
import com.cdcrane.customers.exception.InvalidVerificationCodeException;
import com.cdcrane.customers.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
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

    // -------------------- SPECIFIC EXCEPTIONS --------------------


    @ExceptionHandler(CustomerNotOldEnoughException.class)
    public ResponseEntity<ExceptionErrorResponse> handleCustomerNotOldEnough(CustomerNotOldEnoughException ex) {

        ExceptionErrorResponse body = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();

        log.warn("Customer trying to register is not old enough: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ExceptionErrorResponse> handleEmailTaken(EmailTakenException ex) {
        ExceptionErrorResponse body = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();

        log.warn("Email taken: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ExceptionErrorResponse> handleInvalidVerificationCode(InvalidVerificationCodeException ex) {
        ExceptionErrorResponse body = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();

        log.warn("Invalid verification code: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ExceptionErrorResponse body = ExceptionErrorResponse.builder()
                .message(ex.getMessage())
                .responseCode(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

}
