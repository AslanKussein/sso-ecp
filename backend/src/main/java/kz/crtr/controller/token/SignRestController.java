package kz.crtr.controller.token;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.crtr.dto.ErrorDto;
import kz.crtr.dto.LocalValue;
import kz.crtr.dto.UserTokenState;
import kz.crtr.service.SignService;
import kz.crtr.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/open-api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class SignRestController {
    private final SignService signService;
    private final JwtTokenUtil tokenUtil;

    @ApiOperation(value = "", notes = "authorize and generate token", response = UserTokenState.class, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserTokenState.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = UserTokenState.class),
            @ApiResponse(code = 400, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> login(@RequestHeader final LocalValue lang,
                                                @RequestBody final LoginRequestDto loginRequest) {
        return ResponseEntity.ok(signService.sign(lang, loginRequest));
    }

    @ApiOperation(value = "", notes = "update expired token using resfresh token", response = UserTokenState.class, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserTokenState.class),
            @ApiResponse(code = 400, message = "Refresh token not correct", response = ErrorDto.class)})
    @PostMapping("/refreshToken")
    public ResponseEntity<UserTokenState> refreshToken(@RequestBody final RefreshTokenRequestDto dto) {
        try {
            UserTokenState userTokenState = tokenUtil.generateToken(tokenUtil.getUserIdFromJWT(dto.getRefreshToken()), tokenUtil.getUserName(dto.getRefreshToken()));
            return ResponseEntity.ok(userTokenState);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
