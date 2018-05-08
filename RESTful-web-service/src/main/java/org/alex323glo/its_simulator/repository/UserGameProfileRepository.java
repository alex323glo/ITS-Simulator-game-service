package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.UserGameProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameProfileRepository extends JpaRepository<UserGameProfile, Long> {

    UserGameProfile findByUser_Username(String username);

}
