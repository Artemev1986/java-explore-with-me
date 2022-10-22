package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.AdminUpdateEventRequest;
import ru.yandex.practicum.ewm.dto.EventFullDto;
import ru.yandex.practicum.ewm.exception.ForbiddenException;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.service.AdminEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Slf4j
@Validated
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(
            @RequestParam List<Long> users,
            @RequestParam List<String> states,
            @RequestParam List<Long> categories,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime rangeStart,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {

        List<EventState> eventStates = new ArrayList<>();
        String eventState = "";
        try {
            for (String state: states) {
                eventState = state;
                eventStates.add(EventState.valueOf(state));
            }
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Unknown state: " + eventState);
        }

        List<EventFullDto> eventDtoList = adminEventService.getEvents(
                users,
                categories,
                eventStates,
                rangeStart,
                rangeEnd,
                from,
                size);

        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long eventId,
                                              @RequestBody AdminUpdateEventRequest updateEventDto) {
        EventFullDto eventFullDto = adminEventService.updateEvent(eventId, updateEventDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<Object> publishEvent(@PathVariable Long eventId) {
        EventFullDto eventFullDto = adminEventService.publishEvent(eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<Object> rejectEvent(@PathVariable Long eventId) {
        EventFullDto eventFullDto = adminEventService.rejectEvent(eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }
}
