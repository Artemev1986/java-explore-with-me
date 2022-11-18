package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewm.dto.*;
import ru.yandex.practicum.ewm.exception.ConfirmRequestValidException;
import ru.yandex.practicum.ewm.exception.EventUpdateValidException;
import ru.yandex.practicum.ewm.mapper.EventMapper;
import ru.yandex.practicum.ewm.mapper.RequestMapper;
import ru.yandex.practicum.ewm.model.*;
import ru.yandex.practicum.ewm.repository.CategoryRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;
import ru.yandex.practicum.ewm.repository.RequestRepository;
import ru.yandex.practicum.ewm.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public List<EventShortDto> getEventsByUserId(long userId, int from, int size) {
        User user = userRepository.getById(userId);
        Pageable page = PageRequest.of(from / size, size);
        List<EventShortDto> eventsDto = eventRepository.findEventsByInitiator(user, page)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());

        log.debug("Got events by initiator with id: {}. Events counts: {}", userId, eventsDto.size());
        return eventsDto;
    }

    @Transactional
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        User initiator = userRepository.getById(userId);
        Category category = categoryRepository.getById(newEventDto.getCategory());
        Event event = EventMapper.toEven(newEventDto, category, initiator);
        eventRepository.save(event);
        log.debug("Added new event with id: {}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto updateEvent(long userId, UpdateEventRequest updateEventDto) {
        Event eventFromMemory = eventRepository.getById(updateEventDto.getEventId());
        if (!(eventFromMemory.getState() == EventState.PENDING || eventFromMemory.getState() == EventState.CANCELED)) {
            throw new EventUpdateValidException("The event state must be PENDING or CANCELED");
        }

        userRepository.getById(userId);

        if (userId != eventFromMemory.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }

        Category category = categoryRepository.getById(updateEventDto.getCategory());

        if (eventFromMemory.getState() == EventState.CANCELED) {
            eventFromMemory.setState(EventState.PENDING);
        }

        Event event = EventMapper.updateEvent(eventFromMemory, updateEventDto, category);
        //eventRepository.save(event);

        log.debug("Event with id: {} was updated", event.getId());

        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto getEventByEventIdAndUserId(long eventId, long userId) {
        userRepository.getById(userId);
        Event event = eventRepository.getById(eventId);

        if (userId != event.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }

        log.debug("Got the event with eventId: {} added by the user with id: {}", eventId, userId);
        return EventMapper.toEventFullDto(event);
    }


    public EventFullDto cancelEvent(long eventId, long userId) {
        Event event = eventRepository.getById(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new EventUpdateValidException("The event state must be PENDING");
        }
        userRepository.getById(userId);
        if (userId != event.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);

        log.debug("The event with id: {} was canceled by the user with id: {}", eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    public List<ParticipationRequestDto> getRequestsByEvent(long eventId, long userId) {
        Event event = eventRepository.getById(eventId);
        userRepository.getById(userId);
        if (userId != event.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }
        List<ParticipationRequestDto> requests = requestRepository.findAllByEventId(eventId)
                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
        log.debug("Requests had got for event id: {} created by user id: {}. Requests counts: {}",
                eventId, userId, requests.size());
        return requests;
    }

    public ParticipationRequestDto confirmRequest(long eventId, long userId, long reqId) {
        Event event = eventRepository.getById(eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConfirmRequestValidException("confirmation of the application is not required");
        }
        if (event.getRequestModeration() && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConfirmRequestValidException("This event has exceeded the limit of request");
        }
        userRepository.getById(userId);
        if (userId != event.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }
        ParticipationRequest request = requestRepository.findParticipationRequestByIdAndEventId(reqId, eventId);
        if (request == null) {
            throw new EntityNotFoundException("Request with id " + reqId + " for event with id " + eventId + " not found");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        log.debug("The request with if: {} was confirmed", reqId);
        if (event.getRequestModeration() && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            long cntReq = requestRepository.updateEventRequests(eventId);
            log.debug("The event with id: {} has exceeded the limit of request. Remaining {} event requests canceled",
                    eventId, cntReq);
        }
        return RequestMapper.toRequestDto(request);
    }

    public ParticipationRequestDto rejectRequest(long eventId, long userId, long reqId) {
        Event event = eventRepository.getById(eventId);
        userRepository.getById(userId);
        if (userId != event.getInitiator().getId()) {
            throw new EventUpdateValidException("The initiator is not the current user");
        }
        ParticipationRequest request = requestRepository.findParticipationRequestByIdAndEventId(reqId, eventId);
        if (request == null) {
            throw new EntityNotFoundException("Request with id " + reqId + " for event with id " + eventId + " not found");
        }
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        log.debug("The request with id: {} was rejected", reqId);
        return RequestMapper.toRequestDto(request);
    }
}
