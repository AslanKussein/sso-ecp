package kz.mtszn.models.repository;

import kz.mtszn.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "select u from Users u where upper(u.username) = upper(:login) and (u.block <> 1 or u.block is null )")
    Optional<Users> findByUserNameIgnoreCaseAndBlockNotContaining(@Param("login") String login);

    Users findByUsernameIgnoreCase(final String username);

    @Query(value = "select u from Users u where upper(u.userDetail.iin) = upper(:iin) and (u.block <> 1 or u.block is null )")
    Users findByUserDetail_IinAndBlockNot(final String iin);

    Users findByEmpIdAndPassword(final Long empId, final String password);
}
