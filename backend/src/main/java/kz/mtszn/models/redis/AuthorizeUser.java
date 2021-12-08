package kz.mtszn.models.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Builder
@RedisHash
public class AuthorizeUser implements Serializable {
    @Indexed
    private String accessToken;
    @Indexed
    private String id;
    @Indexed
    private ZonedDateTime crDate;
}
