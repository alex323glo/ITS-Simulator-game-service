package org.alex323glo.its_simulator.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex323glo.its_simulator.model.UserGameProfile;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Space ship model.
 * Is a JPA Entity. Represents 'space_ships' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "space_ships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceShip {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_game_profile_id")
    private UserGameProfile userGameProfile;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(precision = 3)
    private Double maxCargoCapacity;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Enumerated(EnumType.STRING)
    private SpaceShipStatus spaceShipStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_contract_id")
    private Mission currentMission;

    // TODO complete...

}