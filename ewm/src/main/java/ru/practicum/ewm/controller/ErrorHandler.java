package ru.practicum.shareit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StateNotValidException;
import ru.practicum.shareit.exception.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final ErrorResponse errorResponse = new ErrorResponse();

  @ExceptionHandler(value = {MethodArgumentNotValidException.class,
          ValidationException.class, StateNotValidException.class})
  public ResponseEntity<?> handleMethodArgumentNotValid(final Throwable e) {
      errorResponse.setError(e.getMessage());
      log.warn(String.valueOf(e));

      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleNotFoundException(final NotFoundException e) {
      errorResponse.setError(e.getMessage());
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleThrowable(final Throwable e) {
      errorResponse.setError(e.getMessage());
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
