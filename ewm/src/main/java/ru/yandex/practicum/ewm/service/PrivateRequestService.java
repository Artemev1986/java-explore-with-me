package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.ParticipationRequestDto;
import ru.yandex.practicum.ewm.exception.ValidationException;
import ru.yandex.practicum.ewm.mapper.RequestMapper;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.model.ParticipationRequest;
import ru.yandex.practicum.ewm.model.RequestStatus;
import ru.yandex.practicum.ewm.repository.EventRepository;
import ru.yandex.practicum.ewm.repository.RequestRepository;
import ru.yandex.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateRequestService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public List<ParticipationRequestDto> getRequestsByRequestor(long userId) {
        userRepository.getById(userId);

        List<ParticipationRequestDto> requests = requestRepository.findAllByRequesterId(userId)
                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
        log.debug("Requests had got for user with id: {}. Requests counts: {}", userId, requests.size());
        return requests;
    }

    public ParticipationRequestDto addRequest(long userId, long eventId) {
        Event event = eventRepository.getById(eventId);
        if (!(event.getState() == EventState.PUBLISHED)) {
            throw new ValidationException("The event state not PUBLISHED");
        }

        if (event.getRequestModeration()
                && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ValidationException("This event has exceeded the limit of request");
        }

        userRepository.getById(userId);

        if (userId == event.getInitiator().getId()) {
            throw new ValidationException("The event initiator can't add a request to participate in this event");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEventId(eventId);
        request.setRequesterId(userId);

        request.setStatus(RequestStatus.PENDING);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        eventRepository.save(event);
        requestRepository.save(request);

        log.debug("Added new request with id: {}", request.getId());

        return RequestMapper.toRequestDto(request);
    }

    public ParticipationRequestDto canceledRequest(long userId, long requestId) {
        userRepository.getById(userId);

        ParticipationRequest request = requestRepository.getById(requestId);
        if (userId != request.getRequesterId()) {
            throw new ValidationException("The current user can't cancel this request");
        }

        request.setStatus(RequestStatus.CANCELED);

        requestRepository.save(request);

        log.debug("The request with id: {} was canceled", requestId);

        return RequestMapper.toRequestDto(request);
    }
}
