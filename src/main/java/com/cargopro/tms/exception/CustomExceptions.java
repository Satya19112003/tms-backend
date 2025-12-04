package com.cargopro.tms.exception;

// Base custom exceptions matching the assignment requirements
public class CustomExceptions {
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String msg) { super(msg); }
    }
    public static class InvalidStatusTransitionException extends RuntimeException {
        public InvalidStatusTransitionException(String msg) { super(msg); }
    }
    public static class InsufficientCapacityException extends RuntimeException {
        public InsufficientCapacityException(String msg) { super(msg); }
    }
    // Used for Rule 4 (LoadAlreadyBookedException equivalent) and Rule 3 (fully allocated)
    public static class ConflictException extends RuntimeException {
        public ConflictException(String msg) { super(msg); }
    }
}
