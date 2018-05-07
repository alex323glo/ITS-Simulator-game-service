package org.alex323glo.its_simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.SpaceShip;

import javax.persistence.*;
import java.util.List;

/**
 * User's game profile model.
 * Is a JPA Entity. Represents 'user_game_profiles' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "user_game_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "userGameProfile",
            orphanRemoval = true
    )
    private List<SpaceShip> ships;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "userGameProfile",
            orphanRemoval = true
    )
    private List<Mission> missions;

    // TODO complete...
}
