package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.NewUserRequest;
import ru.yandex.practicum.ewm.dto.UserDto;
import ru.yandex.practicum.ewm.mapper.UserMapper;
import ru.yandex.practicum.ewm.model.User;
import ru.yandex.practicum.ewm.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public UserDto addUser(NewUserRequest userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
        log.debug("Added new user with id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    public void deleteUserById(long userId) {
        userRepository.getById(userId);
        userRepository.deleteById(userId);
        log.debug("User with id ({}) was deleted", userId);
    }

    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<UserDto> usersDto = userRepository.getUsers(ids, page)
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        log.debug("Get users. Current user counts: {}", usersDto.size());
        return usersDto;
    }
}
