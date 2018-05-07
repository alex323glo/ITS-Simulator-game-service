package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.game.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findAllByUserGameProfile_User_Username(String username);

}
