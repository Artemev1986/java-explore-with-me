package ru.yandex.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.ewm.exception.ApiError;
import ru.yandex.practicum.ewm.exception.ForbiddenException;
import ru.yandex.practicum.ewm.exception.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final ApiError apiError = new ApiError();

  @ExceptionHandler(value = {MethodArgumentNotValidException.class, ValidationException.class})
  public ResponseEntity<?> handleMethodArgumentNotValid(final Throwable e) {
      Arrays.stream(e.getStackTrace())
              .map(StackTraceElement::toString)
              .forEach(apiError.getErrors()::add);
      apiError.setMessage(e.getMessage());
      apiError.setReason("Bad request");
      apiError.setStatus("BAD_REQUEST");
      apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<?> forbiddenExceptions(final Throwable e) {
        Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .forEach(apiError.getErrors()::add);
        apiError.setMessage(e.getMessage());
        apiError.setReason("The conditions for the transaction are not met");
        apiError.setStatus("FORBIDDEN");
        apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

  @ExceptionHandler
  public ResponseEntity<?> handleNotFoundException(final EntityNotFoundException e) {
      Arrays.stream(e.getStackTrace())
              .map(StackTraceElement::toString)
              .forEach(apiError.getErrors()::add);
      apiError.setMessage(e.getMessage());
      apiError.setReason("Required object not found");
      apiError.setStatus("NOT_FOUND");
      apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      log.warn(String.valueOf(e));
      return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

    @ExceptionHandler
    public ResponseEntity<?> handleSqlException(final SQLException e) {
        Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .forEach(apiError.getErrors()::add);
        apiError.setMessage(e.getMessage());
        apiError.setReason("SQL exception");
        apiError.setStatus("CONFLICT");
        apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
