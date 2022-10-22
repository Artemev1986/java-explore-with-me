package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.ParticipationRequestDto;
import ru.yandex.practicum.ewm.service.PrivateRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@Validated
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @GetMapping
    public ResponseEntity<Object> getRequests(@PathVariable Long userId) {
        List<ParticipationRequestDto> requestDtoList = privateRequestService.getRequestsByRequestor(userId);
        return new ResponseEntity<>(requestDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@PathVariable Long userId,
                                             @RequestParam Long eventId) {
        ParticipationRequestDto requestDto = privateRequestService.addRequest(userId, eventId);
        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        ParticipationRequestDto requestDto = privateRequestService.canceledRequest(userId, requestId);
        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }
}
