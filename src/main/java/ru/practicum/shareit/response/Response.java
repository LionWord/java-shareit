package ru.practicum.shareit.response;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private int id;
    @Column(name = "request_id")
    private int requestId;
    @Column(name = "item_id")
    private int itemId;
}
