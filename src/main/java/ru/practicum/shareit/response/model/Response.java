package ru.practicum.shareit.response.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
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
