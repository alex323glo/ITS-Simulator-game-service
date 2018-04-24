package org.alex323glo.its_simulator.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_game_profiles")
@Data
@NoArgsConstructor
@Builder
public class UserGameProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(
            mappedBy = "user_game_profiles",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<SpaceShip> ships;


    private List<Contract> contracts;

    // TODO complete...
}
