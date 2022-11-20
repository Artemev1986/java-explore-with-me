package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.NewUserRequest;
import ru.yandex.practicum.ewm.dto.UserDto;
import ru.yandex.practicum.ewm.service.AdminUserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final AdminUserService userService;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated NewUserRequest userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(
            @RequestParam List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<UserDto> userDtoList = userService.getAllUsers(ids, from, size);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userID) {
        userService.deleteUserById(userID);
        return ResponseEntity.ok().build();
    }
}
