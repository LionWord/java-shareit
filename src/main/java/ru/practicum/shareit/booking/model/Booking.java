package ru.practicum.shareit.booking.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "bookings")
@DynamicUpdate
public class Booking {
    @Id
    @Column(name = "booking_id")
    private int id;

    @NotNull
    @Column(name = "start_date")
    private Timestamp startDate;

    @NotNull
    @Column(name = "end_date")
    private Timestamp endDate;

    @NotNull
    @Column(name = "item_id")
    private int itemId;

    @NotNull
    @Column(name = "booker_id")
    private int bookerId;

    @NotNull
    @Enumerated
    private Status status = Status.WAITING;
    @NotNull
    private String itemName;
}
