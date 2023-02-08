package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "request_text")
    @NotBlank
    @NotEmpty
    private String description;

    @Column(name = "request_created")
    private LocalDateTime created;

    @Column(name = "requester_id")
    private int requesterId;
}
