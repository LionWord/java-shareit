package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItApp.class
)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig({UserController.class, UserServiceImpl.class, ErrorHandler.class, UserRepository.class})
class UserControllerTest {

    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private UserDto user;

    @Autowired
    UserControllerTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        user = UserDto.builder()
                .name("Kokos")
                .email("cuba@diving.com")
                .build();
    }


    @Test
    void createUser_shouldReturnCorrectUser_withId3() throws Exception {
        int newUserExpectedId = 3;
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("id").value(newUserExpectedId));
    }

    @Test
    void createUser_shouldThrowException_notUniqueEmail() throws Exception {
        user.setEmail("wow@uff.com");
        try {
            mvc.perform(post("/users")
                    .content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (NestedServletException ex) {
            assertEquals(DataIntegrityViolationException.class, ex.getCause().getClass());
        }
    }

    @Test
    void createUser_shouldThrowException_blankName() throws Exception {
        user.setName("");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException));
    }

    @Test
    void createUser_shouldThrowException_blankEmail() throws Exception {
        user.setEmail("");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException));
    }

    @Test
    void createUser_shouldThrowException_invalidEmailFormat() throws Exception {
        user.setEmail("yeehaw");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException));
    }

    @Test
    void createUser_shouldThrowException_tooLongEmail() throws Exception {
        user.setEmail("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeehaw@" +
                "yahooooooooooooooooooooooooooooooooooooooooooooooooooooooooo.com");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException));
    }

    @Test
    void createUser_shouldThrowException_tooLongName() throws Exception {
        user.setName("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeehaw" +
                "yahooooooooooooooooooooooooooooooooooooooooooooooooooooooooocom");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ConstraintViolationException));
    }

    @Test
    void getUser_shouldReturnCorrectUser_byId2() throws Exception {
        int expectedId = 2;
        String expectedEmail = "drish@nomuscles.com";
        String expectedName = "Sasha";
        mvc.perform(get("/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(expectedName))
                .andExpect(jsonPath("email").value(expectedEmail))
                .andExpect(jsonPath("id").value(expectedId));
    }

    @Test
    void getUser_shouldThrowException_userDoNotExist() throws Exception {
        mvc.perform(get("/users/99")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchUserException));
    }

    @Test
    void getAllUsers_shouldContainAllUsers() throws Exception {
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse()
                        .getContentAsString()
                        .contains("wow@uff.com")))
                .andExpect(result -> assertTrue(result.getResponse()
                        .getContentAsString()
                        .contains("drish@nomuscles.com")));

    }

    @Test
    void editUser_shouldReturnUpdatedNameAndEmail() throws Exception {
        int editedUser = 1;
        String expectedEmail = "cuba@diving.com";
        String expectedName = "Kokos";
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(expectedName))
                .andExpect(jsonPath("email").value(expectedEmail))
                .andExpect(jsonPath("id").value(editedUser));
    }

    @Test
    void editUser_shouldThrowException_tryingToEditNonExistentUser() throws Exception {
        mvc.perform(patch("/users/99")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchUserException));
    }

    @Test
    void deleteUser_shouldReturnListOfOneUser() throws Exception {
        int expectedUserListLength = 1;
        mvc.perform(delete("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(expectedUserListLength, userRepository.findAll().size());
    }

    @Test
    void deleteUser_shouldThrowException_userDoNotExist() throws Exception {
        try {
            mvc.perform(delete("/users/99")
                    .content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof EmptyResultDataAccessException);
        }

    }
}