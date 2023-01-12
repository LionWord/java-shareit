package ru.practicum.shareit.user.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name can't be empty")
    @Column(name = "user_name")
    @Length(max = 64)
    private String name;

    @Email(message = "Wrong email")
    @NotBlank
    @Column(name = "user_email", unique = true)
    @Length(max = 64)
    private String email;

}
