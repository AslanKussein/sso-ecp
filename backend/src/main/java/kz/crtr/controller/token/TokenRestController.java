package kz.crtr.controller.token;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import kz.crtr.dto.ErrorDto;
import kz.crtr.dto.PublicKeyDto;
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


    @ApiOperation(value = "", notes = "get public key and sigh algorithm", response = PublicKeyDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PublicKeyDto.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDto.class),
            @ApiResponse(code = 400, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/tokenKey")
    public ResponseEntity<PublicKeyDto> getTokenKey(HttpServletRequest request) {
        return ResponseEntity.ok(tokenUtil.getPublicKey(request));
    }
}
