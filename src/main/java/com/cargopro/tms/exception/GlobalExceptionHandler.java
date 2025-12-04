package com.cargopro.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptions.ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({CustomExceptions.InvalidStatusTransitionException.class, CustomExceptions.InsufficientCapacityException.class})
    public ResponseEntity<?> handleBadRequest(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    // Rule 4: Handle Concurrent Booking Conflict (OptimisticLockException)
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, CustomExceptions.ConflictException.class})
    public ResponseEntity<?> handleConflict(Exception e) {
        String message = (e instanceof ObjectOptimisticLockingFailureException) 
                         ? "Load was modified by another transaction. Please try again." 
                         : e.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", message));
    }
}
