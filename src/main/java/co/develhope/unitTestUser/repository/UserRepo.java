package co.develhope.unitTestUser.repository;

import co.develhope.unitTestUser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
