package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.item.model.Item;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "start_date")
    private Timestamp start;

    @NotNull
    @Column(name = "end_date")
    private Timestamp end;

    @NotNull
    @Column(name = "item_id")
    private int itemId;

    @NotNull
    @Column(name = "booker_id")
    private int bookerId;

    @NotNull
    @Enumerated
    @Column(name = "status")
    private Status status = Status.WAITING;

}
