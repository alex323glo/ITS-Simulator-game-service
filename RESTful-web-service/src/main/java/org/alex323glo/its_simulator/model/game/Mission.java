package org.alex323glo.its_simulator.model.game;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.util.CustomLocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne
    private UserGameProfile userGameProfile;

    @OneToOne(
            fetch = FetchType.EAGER,
//            mappedBy = "currentMission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private SpaceShip spaceShip;

    @ManyToOne
    private Planet startPoint;

    @ManyToOne
    private Planet destinationPoint;

    @Column(nullable = false)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime registrationTime;

    @Column(nullable = true)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @Column(nullable = true)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime finishTime;

    @Column(precision = 3)
    private Double payload;

    @Column(nullable = false)
    private Long duration;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    // TODO complete...
}
