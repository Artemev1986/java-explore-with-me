package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.CompilationDto;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.mapper.CompilationMapper;
import ru.yandex.practicum.ewm.model.Compilation;
import ru.yandex.practicum.ewm.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCompilationService {

    private final CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<CompilationDto> compilations = compilationRepository.findAllByPinned(pinned, page)
                .stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        log.debug("Get event compilations. Compilations counts: {}", compilations.size());
        return compilations;
    }

    public CompilationDto getCompilationById(long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(()->  new NotFoundException("The compilation with id (" + compilationId + ") not found"));
        log.debug("The compilation was got by id: {}", compilationId);
        return CompilationMapper.toCompilationDto(compilation);
    }
}
