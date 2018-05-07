package org.alex323glo.its_simulator.repository;

import org.alex323glo.its_simulator.model.UserExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtensionRepository extends JpaRepository<UserExtension, Long> {

    UserExtension findByEmail(String email);

    UserExtension findByUser_Username(String username);

}
