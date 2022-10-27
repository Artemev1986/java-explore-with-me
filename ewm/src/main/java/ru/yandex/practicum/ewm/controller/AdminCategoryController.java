package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.CategoryDto;
import ru.yandex.practicum.ewm.dto.NewCategoryDto;
import ru.yandex.practicum.ewm.service.AdminCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {

    private final AdminCategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestBody NewCategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{catID}")
    public ResponseEntity<Object> deleteCategoryById(@PathVariable long catID) {
        categoryService.deleteCategoryById(catID);
        return ResponseEntity.ok().build();
    }
}
