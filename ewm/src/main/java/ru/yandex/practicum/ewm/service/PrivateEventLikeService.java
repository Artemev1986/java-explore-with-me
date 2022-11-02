package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.exception.ValidationException;
import ru.yandex.practicum.ewm.model.*;
import ru.yandex.practicum.ewm.repository.EventLikeRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;
import ru.yandex.practicum.ewm.repository.RequestRepository;
import ru.yandex.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateEventLikeService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventLikeRepository eventLikeRepository;
    private final RequestRepository requestRepository;

    public void addLikeDislike(Long userId, Long eventId, Boolean positive) {
        Event event = getEventById(eventId);
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new ValidationException("The event has not yet arrived");
        }

        User user = getUserById(userId);

        ParticipationRequest request = getRequestByRequestorIdAndEventId(userId, eventId);
        if (request.getStatus() != RequestStatus.CONFIRMED) {
            throw new ValidationException("The request status not CONFIRMED");
        }

        EventLike like = new EventLike();
        like.setEventId(eventId);
        like.setUserId(userId);
        like.setPositive(positive);
        eventLikeRepository.save(like);

        User initiator = event.getInitiator();

        if (positive) {
            event.setRating(event.getRating() + 1);
            initiator.setRating(initiator.getRating() + 1);
        } else {
            event.setRating(event.getRating() - 1);
            initiator.setRating(initiator.getRating() - 1);
        }

        eventRepository.save(event);
        userRepository.save(initiator);

        if (positive) {
            log.debug("Like for event with id {} was added by user with id {}", event.getId(), user.getId());
        } else {
            log.debug("Dislike for event with id {} was added by user with id {}", event.getId(), user.getId());
        }
    }

    private User getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id (" + id + ") not found"));
        log.debug("The user was got by id: {}", id);
        return user;
    }

    private Event getEventById(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The event with id (" + id + ") not found"));
        log.debug("The event was got by id: {}", id);
        return event;
    }

    private ParticipationRequest getRequestByRequestorIdAndEventId(long requestorId, long eventId) {
        Optional<ParticipationRequest> request = Optional
                .ofNullable(requestRepository.findParticipationRequestByRequesterIdAndEventId(requestorId, eventId));
        request.orElseThrow(() -> new NotFoundException("request from requestor with id " + requestorId +
                " for event id " + eventId + " not found"));
        log.debug("The request was got by requestorId: {} and eventId: {}", requestorId, eventId);
        return request.get();
    }
}
