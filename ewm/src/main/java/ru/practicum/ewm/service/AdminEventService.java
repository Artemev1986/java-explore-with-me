package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.exception.EventUpdateValidException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<Long> categories,
                                        List<EventState> states,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from, int size) {

        Pageable page = PageRequest.of(from / size, size);

        List<EventFullDto> eventsDto = eventRepository.getEventsAdmin(
                users,
                categories,
                states,
                rangeStart,
                rangeEnd,
                page
        ).stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());

        log.debug("Get events. Events counts: {}", eventsDto.size());

        return eventsDto;
    }

    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest updateEventDto) {
        Event eventFromMemory = getEventById(eventId);

        Category category = getCategoryById(updateEventDto.getCategory());

        Event event = EventMapper.adminUpdateEvent(eventFromMemory, updateEventDto, category);
        eventRepository.save(event);

        log.debug("Event with id: {} was updated", eventId);

        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto publishEvent(long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new EventUpdateValidException("The event state must be PENDING");
        }
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);

        log.debug("The event with id: {} was published", eventId);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto rejectEvent(long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventUpdateValidException("The event state mustn't be PUBLISHED");
        }
        event.setState(EventState.REJECTED);
        eventRepository.save(event);

        log.debug("The event with id: {} was rejected", eventId);
        return EventMapper.toEventFullDto(event);
    }

    private Category getCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The category with id (" + id + ") not found"));
        log.debug("The category was got by id: {}", id);
        return category;
    }

    private Event getEventById(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The event with id (" + id + ") not found"));
        log.debug("The event was got by id: {}", id);
        return event;
    }
}
