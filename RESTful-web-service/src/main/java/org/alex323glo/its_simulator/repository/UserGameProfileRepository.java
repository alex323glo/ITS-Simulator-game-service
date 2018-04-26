package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO add doc
 */
@Repository
public interface UserGameProfileRepository extends JpaRepository<UserGameProfile, Long> {

    UserGameProfile findUserGameProfileByUser(User user);
    UserGameProfile findUserGameProfileByUser_Username(String username);

}
