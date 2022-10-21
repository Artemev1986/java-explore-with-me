package ru.yandex.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.ewm.exception.ApiError;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.exception.StateNotValidException;
import ru.yandex.practicum.ewm.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final ApiError apiError = new ApiError();

  @ExceptionHandler(value = {MethodArgumentNotValidException.class,
          ValidationException.class, StateNotValidException.class})
  public ResponseEntity<?> handleMethodArgumentNotValid(final Throwable e) {
      Arrays.stream(e.getStackTrace())
              .map(StackTraceElement::toString)
              .forEach(apiError.getErrors()::add);
      apiError.setMessage(e.getMessage());
      apiError.setReason("");
      apiError.setStatus("BAD_REQUEST");
      apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleNotFoundException(final NotFoundException e) {
      Arrays.stream(e.getStackTrace())
              .map(StackTraceElement::toString)
              .forEach(apiError.getErrors()::add);
      apiError.setMessage(e.getMessage());
      apiError.setReason("");
      apiError.setStatus("NOT_FOUND");
      apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleThrowable(final Throwable e) {
      Arrays.stream(e.getStackTrace())
              .map(StackTraceElement::toString)
              .forEach(apiError.getErrors()::add);
      apiError.setMessage(e.getMessage());
      apiError.setReason("");
      apiError.setStatus("INTERNAL_SERVER_ERROR");
      apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
