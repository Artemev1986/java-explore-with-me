package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.CategoryDto;
import ru.yandex.practicum.ewm.dto.NewCategoryDto;
import ru.yandex.practicum.ewm.mapper.CategoryMapper;
import ru.yandex.practicum.ewm.model.Category;
import ru.yandex.practicum.ewm.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        log.debug("Added new category with id: {}", category.getId());
        return CategoryMapper.toCategoryDto(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.getById(categoryDto.getId());
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResponse = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.debug("Category with id: {} was updated", category.getId());
        return categoryDtoResponse;
    }

    public void deleteCategoryById(long id) {
        categoryRepository.getById(id);
        categoryRepository.deleteById(id);
        log.debug("Category with id ({}) was deleted", id);
    }
}
