package kz.mtszn.util;

import io.jsonwebtoken.*;
import kz.mtszn.dto.PublicKeyDto;
import kz.mtszn.dto.UserTokenState;
import kz.mtszn.models.redis.BlackListUser;
import kz.mtszn.models.redisrepository.AuthorizeUserRepository;
import kz.mtszn.models.redisrepository.BlackListUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import static java.util.Objects.nonNull;
import static kz.mtszn.util.Utils.isNullOrEmpty;

@RequiredArgsConstructor
@Component
@Log
public class JwtTokenUtil {

    @Value("${auth.jwt.secret}")
    public String SECRET;
    @Value("${auth.jwt.expirationSeconds}")
    private long expirationSeconds;
    @Value("${auth.jwt.expirationRefreshDay}")
    private long expirationRefreshDay;
    private final AuthorizeUserRepository authorizeUserRepository;
    private final BlackListUserRepository blackListUserRepository;

    public Long getSessId (String accessTokenString) {
        try {
            Claims cl = Jwts.parser()
                    .setSigningKey(SECRET) //
                    .parseClaimsJws(accessTokenString)
                    .getBody();
            return  ((Integer) cl.get("sessId")).longValue();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public UserTokenState generateToken(String empId, String login) {
        final Date expiry = Date.from(ZonedDateTime.now().plusSeconds(expirationSeconds).toInstant());

        String compact = Jwts.builder()
                .claim("empId", empId)
                .setId(empId)
                .setSubject(login)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return UserTokenState.builder()
                .accessToken(compact)
                .expiresIn(expiry)
                .username(login)
                .active(Boolean.TRUE)
                .refreshToken(refreshToken(compact))
                .empId(Long.parseLong(empId))
                .build();
    }

    public UserTokenState getUserTokenState(String token) {

        Claims claims = getAllClaimsFromToken(token);

        return UserTokenState.builder()
                .accessToken(token)
                .active(validateToken(token))
                .expiresIn(claims.getExpiration())
                .username(getUserName(token))
                .refreshToken(refreshToken(token))
                .empId(Long.parseLong(getUserIdFromJWT(token)))
                .build();
    }

    public String getUserName(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getEmpId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getId();
    }

    public boolean validateToken(String token) {
        try {
            Optional<BlackListUser> blackListUser = blackListUserRepository.findByAccessToken(token);
            if (blackListUser.isPresent()) {
                return Boolean.FALSE;
            }
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            log.severe("Invalid signature");
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String BEARER = "Bearer ";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }

        String ACCESS_TOKEN = "access_token";
        String access_token = request.getParameter(ACCESS_TOKEN);
        if (!isNullOrEmpty(access_token)) {
            return access_token;
        }

        Map<String, String[]> map = request.getParameterMap();
        String[] token = map.get(ACCESS_TOKEN);
        if (nonNull(token) && token.length > 0 && !isNullOrEmpty(token[0])) {
            return token[0];
        }

        if (!isNullOrEmpty(access_token)) {
            log.log(Level.SEVERE, "Responding with unauthorized error. Message - {access_token = " + access_token + "}");
        }

        if (!isNullOrEmpty(bearerToken)) {
            log.log(Level.SEVERE, "Responding with unauthorized error. Message - {bearerToken = " + bearerToken + "}");
        }
        return null;
    }

    public String refreshToken(String token) {
        final Instant expiry = ZonedDateTime.now().plusDays(expirationRefreshDay).toInstant();

        String refreshedToken;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            claims.setIssuedAt(Date.from(expiry));
            refreshedToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(Date.from(expiry))
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getId();
    }

    public void removeToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        claims.remove(token);
    }

    public void removeToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            removeToken(headerAuth.substring(7));
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public PublicKeyDto getPublicKey(final HttpServletRequest request) {
        if (validateToken(getJwtFromRequest(request))) {
            return PublicKeyDto.builder()
                    .key(SECRET)
                    .algorithm("HmacSHA256")
                    .build();
        }
        return null;
    }
}
