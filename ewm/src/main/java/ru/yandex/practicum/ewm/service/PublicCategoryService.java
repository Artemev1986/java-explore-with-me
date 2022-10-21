package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.CategoryDto;
import ru.yandex.practicum.ewm.exception.NotFoundException;
import ru.yandex.practicum.ewm.mapper.CategoryMapper;
import ru.yandex.practicum.ewm.model.Category;
import ru.yandex.practicum.ewm.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<CategoryDto> categories = categoryRepository.findAll(page)
                .stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
        log.debug("Get categories. Current category counts: {}", categories.size());
        return categories;
    }

    public CategoryDto getCategoryById(long id) {
        return CategoryMapper.toCategoryDto(getCategory(id));
    }
    private Category getCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id (" + id + ") not found"));
        log.debug("Category get by id: {}", id);
        return category;
    }
}
