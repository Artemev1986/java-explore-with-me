package ru.yandex.practicum.ewm.exception;

public class StateNotValidException extends RuntimeException {
    public StateNotValidException(String message) {
        super(message);
    }
}