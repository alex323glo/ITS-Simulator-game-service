package org.alex323glo.its_simulator.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_ship_id")
    private SpaceShip spaceShip;

    @ManyToOne(fetch = FetchType.EAGER)
    private Planet startingPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    private Planet destinationPoint;

    @Column(precision = 3)
    private Double payload;

    private ContractStatus contractStatus;
    // TODO complete...
}
