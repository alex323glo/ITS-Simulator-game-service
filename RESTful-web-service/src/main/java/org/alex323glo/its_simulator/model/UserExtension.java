package org.alex323glo.its_simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * User extension (additional info part) model.
 * Is a JPA Entity. Represents 'user_extensions' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "user_extensions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExtension {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String email;

    @OneToOne
    @MapsId
    private User user;

    @Column(nullable = false)
    private LocalDateTime registrationTime;

}
