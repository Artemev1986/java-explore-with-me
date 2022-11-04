package ru.yandex.practicum.ewm.mapper;

import ru.yandex.practicum.ewm.dto.NewUserRequest;
import ru.yandex.practicum.ewm.dto.UserDto;
import ru.yandex.practicum.ewm.dto.UserShortDto;
import ru.yandex.practicum.ewm.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRating(user.getRating());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userDto = new UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRating(user.getRating());
        return userDto;
    }

    public static User toUser(NewUserRequest userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRating(0L);
        return user;
    }
}
