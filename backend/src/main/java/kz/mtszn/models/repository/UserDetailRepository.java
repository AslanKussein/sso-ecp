package kz.mtszn.models.repository;

import kz.mtszn.models.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail findByUser_UsernameIgnoreCase(String login);
}
