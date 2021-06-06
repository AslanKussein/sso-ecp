package kz.crtr.models.repository;

import kz.crtr.models.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail findByUser_UsernameIgnoreCase(String login);
}
