package ru.yandex.practicum.ewm.dto;

import lombok.Data;
import ru.yandex.practicum.ewm.model.RequestStatus;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
