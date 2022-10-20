package ru.practicum.ewm.exception;

public class EventDateNotValidException extends RuntimeException {
    public EventDateNotValidException(String message) {
        super(message);
    }
}
