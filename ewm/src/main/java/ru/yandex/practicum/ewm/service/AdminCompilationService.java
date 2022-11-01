package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewm.dto.CompilationDto;
import ru.yandex.practicum.ewm.dto.NewCompilationDto;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.mapper.CompilationMapper;
import ru.yandex.practicum.ewm.model.Compilation;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventCompilation;
import ru.yandex.practicum.ewm.repository.CompilationRepository;
import ru.yandex.practicum.ewm.repository.EventCompilationRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventCompilationRepository eventCompilationRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = eventRepository.getEventsForCompilation(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        log.debug("Added new compilation with id: {}", compilation.getId());
        return CompilationMapper.toCompilationDto(compilation);
    }

    public void deleteCompilationById(long compilationId) {
        getCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
        log.debug("The compilation with id: {} was deleted", compilationId);
    }

    @Transactional
    public void deleteEventFromCompilation(long compilationId, long eventId) {
        eventCompilationRepository.deleteEventCompilationByCompilationIdAndEventId(compilationId, eventId);
        log.debug("The event with id: {} was deleted from the compilation with id: {}", eventId, compilationId);
    }

    public void addEventToCompilation(long compilationId, long eventId) {
        getCompilationById(compilationId);
        checkEventById(eventId);
        EventCompilation eventCompilation = new EventCompilation();
        eventCompilation.setCompilationId(compilationId);
        eventCompilation.setEventId(eventId);
        eventCompilationRepository.save(eventCompilation);
        log.debug("The event with id: {} was added to the compilation with id: {}", eventId, compilationId);
    }

    public void unpinCompilation(long compilationId) {
        Compilation compilation = getCompilationById(compilationId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("The compilation with id: {} was unpinned", compilationId);
    }

    public void pinCompilation(long compilationId) {
        Compilation compilation = getCompilationById(compilationId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("The compilation with id: {} was pinned", compilationId);
    }

    private Compilation getCompilationById(long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() ->  new NotFoundException("The compilation with id (" + compilationId + ") not found"));
        log.debug("The compilation was got by id: {}", compilationId);
        return compilation;
    }

    private void checkEventById(long id) {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The event with id (" + id + ") not found"));
        log.debug("The event was got by id: {}", id);
    }
}
