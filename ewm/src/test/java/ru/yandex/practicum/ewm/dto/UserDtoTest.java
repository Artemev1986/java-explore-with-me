package ru.yandex.practicum.ewm.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<NewUserRequest> json;

    private final NewUserRequest userDto = new NewUserRequest();

    @Test
    void testUserDto() throws IOException {
        userDto.setName("Mikhail");
        userDto.setEmail("Mikhail@gmail.com");

        JsonContent<NewUserRequest> res = json.write(userDto);

        assertThat(res).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(res).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

}