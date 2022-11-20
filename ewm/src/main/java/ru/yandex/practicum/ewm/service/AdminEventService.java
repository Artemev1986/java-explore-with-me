package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewm.dto.AdminUpdateEventRequest;
import ru.yandex.practicum.ewm.dto.EventFullDto;
import ru.yandex.practicum.ewm.exception.EventUpdateValidException;
import ru.yandex.practicum.ewm.mapper.EventMapper;
import ru.yandex.practicum.ewm.model.Category;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.repository.CategoryRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public List<EventFullDto> getEvents(List<Long> users,
                                        List<Long> categories,
                                        List<EventState> states,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from, int size) {

        Pageable page = PageRequest.of(from / size, size);

        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.now().plusYears(9999) : rangeEnd;

        List<EventFullDto> eventsDto = eventRepository.getEventsAdmin(
                users,
                categories,
                states,
                start,
                end,
                page
        ).stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());

        log.debug("Get events. Events counts: {}", eventsDto.size());

        return eventsDto;
    }

    @Transactional
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest updateEventDto) {
        Event eventFromMemory = eventRepository.getById(eventId);

        Category category = categoryRepository.getById(updateEventDto.getCategory());

        Event event = EventMapper.adminUpdateEvent(eventFromMemory, updateEventDto, category);

        log.debug("Event with id: {} was updated", eventId);

        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto publishEvent(long eventId) {
        Event event = eventRepository.getById(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new EventUpdateValidException("The event state must be PENDING");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventUpdateValidException("the start date of the event must be no earlier than one hour from " +
                    "the date of publication");
        }
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);

        log.debug("The event with id: {} was published", eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        Event event = eventRepository.getById(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventUpdateValidException("The event state mustn't be PUBLISHED");
        }
        event.setState(EventState.CANCELED);

        log.debug("The event with id: {} was rejected", eventId);
        return EventMapper.toEventFullDto(event);
    }
}
