package com.pawradise.api.exception;

import com.pawradise.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors, such as "Username already taken" or "Invalid password".
     * Returns a 400 Bad Request status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Create an error response using your ApiResponse class
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where a resource (like a Product) is not found.
     * Returns a 404 Not Found status.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(ProductNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles cases where a resource (like a Pet) is not found.
     * Returns a 404 Not Found status.
     */
    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePetNotFoundException(PetNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles cases where a resource (like a Content) is not found.
     * Returns a 404 Not Found status.
     */
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleContentNotFoundException(ContentNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // You can add more handlers for other specific exceptions here
}