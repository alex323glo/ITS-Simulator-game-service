package org.alex323glo.its_simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

/**
 * User model.
 * Is a JPA Entity. Represents 'users' SQL table.
 * Also holds logic of Security Authentication Entity (see generateUserDetails() method).
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see UserDetails
 * @see User#generateUserDetails()
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(
            optional = false,
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserExtension userExtension;

    @OneToOne(
            optional = false,
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserGameProfile userGameProfile;

    /**
     * Generates UserDetails object for Security purposes.
     *
     * @return UserDetails object with assigned 'username',
     * 'password' and 'roles="USER"' fields.
     *
     * @see UserDetails
     */
    public UserDetails generateUserDetails() {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .roles("USER")
                .build();
    }
}
