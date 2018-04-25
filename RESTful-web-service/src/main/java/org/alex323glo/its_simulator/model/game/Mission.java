package org.alex323glo.its_simulator.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex323glo.its_simulator.model.UserGameProfile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Mission model.
 * Is a JPA Entity. Represents 'missions' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_gme_profile_id")
    private UserGameProfile userGameProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_ship_id")
    private SpaceShip spaceShip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "starting_point_id")
    private Planet startingPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_point_id")
    private Planet destinationPoint;

    @Column(nullable = false)
    private LocalDateTime registrationTime;

    @Column(nullable = true)
    private LocalDateTime startTime;

    @Column(nullable = true)
    private LocalDateTime finishTime;

    @Column(precision = 3)
    private Double payload;

    @Column
    private LocalTime duration;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    // TODO complete...
}
