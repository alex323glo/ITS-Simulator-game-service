package org.alex323glo.its_simulator.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex323glo.its_simulator.model.UserGameProfile;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Contract model.
 * Is a JPA Entity. Represents 'contracts' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_gme_profile_id")
    private UserGameProfile userGameProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_ship_id")
    private SpaceShip spaceShip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "starting_point_id")
    private Planet startingPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_point_id")
    private Planet destinationPoint;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = true)
    private LocalDateTime finishTime;

    @Column(precision = 3)
    private Double payload;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    // TODO complete...
}
