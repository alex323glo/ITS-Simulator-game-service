package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.game.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

    Planet findByName(String planetName);

}
