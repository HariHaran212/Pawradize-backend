package com.pawradise.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final long timestamp;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // --- Success Methods ---

    // Success with data and a custom message
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    // Success with data and a default message
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    // Success without data, for operations like DELETE
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }


    // --- Error Methods ---

    // Error with a message only (most common case)
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Error with an optional data payload (e.g., validation errors)
    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>(false, message, data);
    }
}