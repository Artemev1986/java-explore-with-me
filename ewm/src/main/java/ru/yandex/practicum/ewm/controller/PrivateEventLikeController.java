package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.EventShortDto;
import ru.yandex.practicum.ewm.service.PrivateEventLikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/like-dislike")
public class PrivateEventLikeController {

    private final PrivateEventLikeService eventLikeService;

    @PutMapping
    public ResponseEntity<Object> addLikeDislike(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestParam(name = "positive", required = false, defaultValue = "true") Boolean positive) {
        EventShortDto eventShortDto = eventLikeService.addLikeDislike(userId, eventId, positive);
        return new ResponseEntity<>(eventShortDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeLikeDislike(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        eventLikeService.removeLikeDislike(userId, eventId);
        return ResponseEntity.ok().build();
    }
}
