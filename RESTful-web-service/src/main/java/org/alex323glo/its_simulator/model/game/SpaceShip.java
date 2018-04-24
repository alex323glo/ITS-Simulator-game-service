package org.alex323glo.its_simulator.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "space_ships")
@Data
@NoArgsConstructor
@Builder
public class SpaceShip {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_game_profile_id")
    private UserGameProfile userGameProfile;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(precision = 3)
    private Double maxCargoCapacity;

    // TODO complete...

}
