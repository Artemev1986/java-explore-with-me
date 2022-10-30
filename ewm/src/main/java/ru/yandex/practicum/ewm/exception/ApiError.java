package ru.yandex.practicum.ewm.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiError {
    private List<String> errors = new ArrayList<>();
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
