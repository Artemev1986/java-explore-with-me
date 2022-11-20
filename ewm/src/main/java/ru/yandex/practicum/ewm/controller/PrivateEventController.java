package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.*;
import ru.yandex.practicum.ewm.exception.ValidationException;
import ru.yandex.practicum.ewm.service.PrivateEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {

    private final PrivateEventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEventsByUserId(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventShortDto> eventDtoList = eventService.getEventsByUserId(
                userId,
                from,
                size);

        return new ResponseEntity<>(eventDtoList, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Object> patchEvent(@RequestBody UpdateEventRequest updateEventRequest,
                                             @PathVariable Long userId) {
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("the start date of the event " +
                    "must be no earlier than one hour from the date of publication");
        }
        EventFullDto eventDto = eventService.updateEvent(userId, updateEventRequest);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addEvent(@RequestBody @Validated NewEventDto newEventDto, @PathVariable Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date is not valid");
        }
        EventFullDto eventDto = eventService.addEvent(userId, newEventDto);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        EventFullDto eventFullDto = eventService.getEventByEventIdAndUserId(eventId, userId);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        EventFullDto eventFullDto = eventService.cancelEvent(eventId, userId);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        List<ParticipationRequestDto> requests = eventService.getRequestsByEvent(eventId, userId);

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> confirmRequest(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @PathVariable Long reqId) {
        ParticipationRequestDto requestDto = eventService.confirmRequest(eventId, userId, reqId);

        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        ParticipationRequestDto requestDto = eventService.rejectRequest(eventId, userId, reqId);

        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }
}
