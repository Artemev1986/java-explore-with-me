package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.EventFullDto;
import ru.yandex.practicum.ewm.dto.EventShortDto;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.mapper.EventMapper;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventService {

    private final EventRepository eventRepository;

    public List<EventShortDto> searchPublishedEvents(String text,
                                                     List<Long> categories,
                                                     boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     boolean onlyAvailable,
                                                     SortParameter sort,
                                                     int from, int size) {
        String sortParameter = "";
        if (sort == SortParameter.EVENT_DATE) {
            sortParameter = "eventDate";
        } else if (sort == SortParameter.VIEWS) {
            sortParameter = "views";
        }

        Pageable page = PageRequest.of(from / size, size, Sort.by(sortParameter).ascending());

        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.MAX : rangeEnd;

        List<EventShortDto> eventsDto = eventRepository.searchEventByText(
                text,
                categories,
                paid,
                start,
                end,
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
