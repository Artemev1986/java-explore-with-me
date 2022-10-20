package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto addUser(NewCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        log.debug("Added new category with id: {}", category.getId());
        return CategoryMapper.toCategoryDto(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getId());
        CategoryDto categoryDtoResponse = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.debug("Category with id: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public void deleteCategoryById(long id) {
        getCategory(id);
        categoryRepository.deleteById(id);
        log.debug("Category with id ({}) was deleted", id);
    }

    private Category getCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id (" + id + ") not found"));
        log.debug("Category get by id: {}", id);
        return category;
    }
}
