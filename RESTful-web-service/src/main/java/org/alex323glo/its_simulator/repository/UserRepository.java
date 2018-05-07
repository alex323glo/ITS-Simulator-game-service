package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByUserExtension_Email(String email);

}
