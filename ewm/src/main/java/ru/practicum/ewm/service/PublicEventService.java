package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventService {

    private final EventRepository eventRepository;

    public List<EventShortDto> searchPublishedEvents(String text,
                                                     List<Category> categories,
                                                     boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     boolean onlyAvailable,
                                                     SortParameter sort,
                                                     int from, int size) {

        Pageable page = PageRequest.of(from / size, size, Sort.by(sort.toString()).ascending());

        List<EventShortDto> eventsDto = eventRepository.searchEventByText(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                page
        ).stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());

        log.debug("Get events by text. Events counts: {}", eventsDto.size());

        return eventsDto;
    }

    public EventFullDto getEventById(long id) {
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Published event with id " + id + " not found");
        }
        log.debug("Get published event by id: {}", id);
        return EventMapper.toEventFullDto(event);
    }
}
