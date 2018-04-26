package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * General User JPA repository.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    User findUserByUserExtension_Email(String email);

}
