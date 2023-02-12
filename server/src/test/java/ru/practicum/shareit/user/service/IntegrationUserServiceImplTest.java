package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItServer.class
)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
class IntegrationUserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void saveUser_shouldReturnNewUser_withNameVladimir() {
        UserDto user = UserDto.builder()
                .name("vladimir")
                .email("uuu@fff.com")
                .build();
        assertEquals("vladimir", userService.saveUser(user).getName());
    }

    @Test
    public void deleteUser_allUsersListSize_shouldDiminishByOne() {
        User user = new User();
        user.setName("vladimir");
        user.setEmail("uuu@fff.com");
        int userId = userRepository.save(user).getId();
        int currentSize = userRepository.findAll().size();
        userService.deleteUser(userId);
        assertNotEquals(currentSize, userRepository.findAll().size());
    }

    @Test
    public void modifyUser_shouldChangeName_toAlexey() {
        int userId = 1;
        UserDto userDto = UserDto.builder()
                .name("alexey")
                .build();
        User modUser = userService.modifyUser(userId, userDto);
        assertEquals("alexey", userService.getUser(modUser.getId()).getName());
    }

    @Test
    public void getUser_byId1_nameShouldBeIgor() {
        int userId = 1;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("Igor");
        expectedUser.setEmail("wow@uff.com");
        User actualUser = userService.getUser(userId);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getAllUsers_addNewUser_shouldReturnBiggerList() {
        int currentUserAmount = userService.getAllUsers().size();
        UserDto newUser = UserDto.builder()
                .name("test")
                .email("test@test.com")
                .build();
        userService.saveUser(newUser);
        int newUserAmount = userService.getAllUsers().size();
        assertEquals(currentUserAmount + 1, newUserAmount);
    }
}