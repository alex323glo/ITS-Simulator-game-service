package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.model.game.SpaceShipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceShipRepository extends JpaRepository<SpaceShip, Long> {

    SpaceShip findByName(String spaceShipName);

    List<SpaceShip> findAllByUserGameProfile_User_Username(String username);

    List<SpaceShip> findAllByUserGameProfile_User_UsernameAndSpaceShipStatus(String username, SpaceShipStatus status);

}
