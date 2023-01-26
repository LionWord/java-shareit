package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.BookingSelfOwnedItemException;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.dto.ItemBookingDto;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

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
@SpringJUnitWebConfig({BookingController.class,
        BookingServiceImpl.class,
        ErrorHandler.class,
        BookingRepository.class,
        Booker.class,
        ItemBookingDto.class,
        BookingDto.class})
class BookingControllerTest {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

    private MockMvc mvc;
    private Booking booking;
    private int userId;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        booking = new Booking();
        booking.setItemId(1);
        booking.setStart(LocalDateTime.of(3000, Month.JANUARY, 12, 0, 0, 0));
        booking.setEnd(LocalDateTime.of(3001, Month.JANUARY, 12, 0, 0, 0));
    }

    @Test
    void createBookingRequest_shouldReturnCorrectDto() throws Exception {
        userId = 2;
        Booker booker = new Booker(userId);
        ItemBookingDto item = new ItemBookingDto(1, "drill");
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("start").value(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("end").value(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("item").value(item))
                .andExpect(jsonPath("booker").value(booker))
                .andExpect(jsonPath("status").value(Status.WAITING.name()));
    }

    @Test
    void createBookingRequest_shouldThrowException_tryingToBookNonExistentItem() throws Exception {
        userId = 2;
        booking.setItemId(99);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchItemException));
    }

    @Test
    void createBookingRequest_shouldThrowException_userDoNotExist() throws Exception {
        userId = 99;
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchUserException));
    }

    @Test
    void createBookingRequest_shouldThrowException_tryingToBookOwnItem() throws Exception {
        userId = 1;
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof BookingSelfOwnedItemException));
    }

    @Test
    void approveBookingRequest_shouldReturnApprovedBookingDto() throws Exception {
        userId = 1;
        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(Status.APPROVED.name()));
    }

    @Test
    void approveBookingRequest_shouldReturnRejectedBookingDto() throws Exception {
        userId = 1;
        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "false")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(Status.REJECTED.name()));
    }

    @Test
    void approveBookingRequest_shouldThrowException_onlyOwnerCanApprove() throws Exception {
        userId = 2;
        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "false")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchItemException));
    }

    @Test
    void getBookingInformation_returnCorrectDto_byItemOwnerRequest() throws Exception {
        int ownerId = 1;
        int bookerId = 2;
        Booker booker = new Booker(bookerId);
        ItemBookingDto item = new ItemBookingDto(1, "drill");
        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("item").value(item))
                .andExpect(jsonPath("booker").value(booker))
                .andExpect(jsonPath("status").value(Status.WAITING.name()));
    }

    @Test
    void getBookingInformation_returnCorrectDto_byBookerRequest() throws Exception {
        int bookerId = 2;
        Booker booker = new Booker(bookerId);
        ItemBookingDto item = new ItemBookingDto(1, "drill");
        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("item").value(item))
                .andExpect(jsonPath("booker").value(booker))
                .andExpect(jsonPath("status").value(Status.WAITING.name()));
    }

    @Test
    void getAllOwnerBookings_shouldReturnOneBookingDrill() throws Exception {
        userId = 1;
        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("drill")));
    }

}