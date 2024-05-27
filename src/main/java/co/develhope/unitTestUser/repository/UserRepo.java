package co.develhope.unitTestUser.repository;

import co.develhope.unitTestUser.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
