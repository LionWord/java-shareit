package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemBookingDto;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD'T'HH:mm:ss")
    private Timestamp start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD'T'HH:mm:ss")
    private Timestamp end;

    private Booker booker;

    private Status status = Status.WAITING;

    private ItemBookingDto item;
}
