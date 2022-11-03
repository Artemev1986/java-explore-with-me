package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.EventShortDto;
import ru.yandex.practicum.ewm.exception.ValidationException;
import ru.yandex.practicum.ewm.mapper.EventMapper;
import ru.yandex.practicum.ewm.model.*;
import ru.yandex.practicum.ewm.repository.EventLikeRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;
import ru.yandex.practicum.ewm.repository.RequestRepository;
import ru.yandex.practicum.ewm.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
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

    public EventShortDto addLikeDislike(Long userId, Long eventId, Boolean positive) {
        Event event = eventRepository.getById(eventId);
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new ValidationException("The event has not yet arrived");
        }

        userRepository.getById(userId);

        ParticipationRequest request = getRequestByRequestorIdAndEventId(userId, eventId);
        if (request.getStatus() != RequestStatus.CONFIRMED) {
            throw new ValidationException("The request status not CONFIRMED");
        }

        EventLike likeLoaded = eventLikeRepository.findEventLikeByUserIdAndEventId(userId, eventId);

        int state = 0;

        if (likeLoaded != null && likeLoaded.getPositive()) {
            state = 1;
        } else if (likeLoaded != null && !likeLoaded.getPositive()) {
            state = 2;
        }

        EventLike like = new EventLike();
        like.setEventId(eventId);
        like.setUserId(userId);
        like.setPositive(positive);
        eventLikeRepository.save(like);
        User initiator = event.getInitiator();

        if (positive && (state == 2 || state == 0)) {
            int rating = state == 0 ? 1 : 2;
            event.setRating(Optional.ofNullable(event.getRating()).orElse(0L) + rating);
            initiator.setRating(Optional.ofNullable(initiator.getRating()).orElse(0L) + rating);
            eventRepository.save(event);
            userRepository.save(initiator);
            log.debug("Like for event with id {} was added by user with id {}", eventId, userId);
        } else if (!positive && (state == 1 || state == 0)) {
            int rating = state == 0 ? 1 : 2;
            event.setRating(Optional.ofNullable(event.getRating()).orElse(0L) - rating);
            initiator.setRating(Optional.ofNullable(initiator.getRating()).orElse(0L) - rating);
            eventRepository.save(event);
            userRepository.save(initiator);
            log.debug("Dislike for event with id {} was added by user with id {}", eventId, userId);
        }

        return EventMapper.toEventShortDto(event);
    }

    public void removeLikeDislike(Long userId, Long eventId) {

        EventLike likeLoaded = eventLikeRepository.findEventLikeByUserIdAndEventId(userId, eventId);

        if (likeLoaded == null) {
            throw new EntityNotFoundException("Like-dislike for event id " + eventId +
                    " from user id " + userId + " not found");
        }

        boolean state = likeLoaded.getPositive();
        eventLikeRepository.delete(likeLoaded);

        Event event = eventRepository.getById(eventId);
        User initiator = event.getInitiator();

        if (state) {
            event.setRating(Optional.ofNullable(event.getRating()).orElse(0L) - 1);
            initiator.setRating(Optional.ofNullable(initiator.getRating()).orElse(0L) - 1);
            eventRepository.save(event);
            userRepository.save(initiator);
            log.debug("Like for event with id {} was deleted by user with id {}", eventId, userId);
        } else {
            event.setRating(Optional.ofNullable(event.getRating()).orElse(0L) + 1);
            initiator.setRating(Optional.ofNullable(initiator.getRating()).orElse(0L) + 1);
            eventRepository.save(event);
            userRepository.save(initiator);
            log.debug("Dislike for event with id {} was deleted by user with id {}", eventId, userId);
        }
    }

    private ParticipationRequest getRequestByRequestorIdAndEventId(long requestorId, long eventId) {
        Optional<ParticipationRequest> request = Optional
                .ofNullable(requestRepository.findParticipationRequestByRequesterIdAndEventId(requestorId, eventId));
        request.orElseThrow(() -> new EntityNotFoundException("request from requestor with id " + requestorId +
                " for event id " + eventId + " not found"));
        log.debug("The request was got by requestorId: {} and eventId: {}", requestorId, eventId);
        return request.get();
    }
}
