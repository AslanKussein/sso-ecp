package kz.crtr.controller.token;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.crtr.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenRestController {

    private final JwtTokenUtil tokenUtil;

    @ApiOperation(value = "", notes = "remove jwt token", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {}
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenUtil.removeToken(tokenUtil.getJwtFromRequest(request));
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (nonNull(session)) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "", notes = "check validate jwt token", response = Boolean.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {}
    )
    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(HttpServletRequest request) {
        String jwt = tokenUtil.getJwtFromRequest(request);
        return ResponseEntity.ok(tokenUtil.validateToken(jwt));
    }
}
