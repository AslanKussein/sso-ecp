package kz.mtszn.models.redisrepository;

import kz.mtszn.models.redis.BlackListUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListUserRepository extends CrudRepository<BlackListUser, String> {

    Optional<BlackListUser> findByAccessToken(final String token);
}
