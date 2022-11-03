package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.EventShortDto;
import ru.yandex.practicum.ewm.service.PrivateEventLikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}")
public class PrivateEventLikeController {

    private final PrivateEventLikeService eventLikeService;

    @PutMapping("/like")
    public ResponseEntity<Object> addLike(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        EventShortDto eventShortDto = eventLikeService.addLikeDislike(userId, eventId, true);
        return new ResponseEntity<>(eventShortDto, HttpStatus.OK);
    }

    @PutMapping("/dislike")
    public ResponseEntity<Object> addDislike(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        EventShortDto eventShortDto = eventLikeService.addLikeDislike(userId, eventId, false);
        return new ResponseEntity<>(eventShortDto, HttpStatus.OK);
    }

    @DeleteMapping("/like")
    public ResponseEntity<Object> removeLike(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        eventLikeService.removeLikeDislike(userId, eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<Object> removeDislike(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        eventLikeService.removeLikeDislike(userId, eventId);
        return ResponseEntity.ok().build();
    }
}
