package kz.mtszn.controller.token;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.mtszn.dto.ErrorDto;
import kz.mtszn.dto.LocalValue;
import kz.mtszn.dto.UserTokenState;
import kz.mtszn.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/open-api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class SignRestController {
    private final SignService signService;

    @ApiOperation(value = "", notes = "authorize and generate token", response = UserTokenState.class, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserTokenState.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDto.class),
            @ApiResponse(code = 400, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> login(@RequestHeader final LocalValue lang,
                                                @RequestBody final LoginRequestDto loginRequest) {
        return ResponseEntity.ok(signService.sign(lang, loginRequest));
    }

    @ApiOperation(value = "", notes = "update expired token using resfresh token", response = UserTokenState.class, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserTokenState.class),
            @ApiResponse(code = 500, message = "Refresh token not correct", response = ErrorDto.class)})
    @PostMapping("/refreshToken")
    public ResponseEntity<UserTokenState> refreshToken(@RequestHeader final LocalValue lang,
                                                       @RequestBody final RefreshTokenRequestDto dto) {
        try {
            return ResponseEntity.ok(signService.refreshToken(dto, lang));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @ApiOperation(value = "", notes = "check validate jwt token", response = UserTokenState.class)
    @PostMapping("/validateToken")
    public ResponseEntity<UserTokenState> validateToken(@RequestBody final TokenRequestDto dto) {
        try {
            return ResponseEntity.ok(signService.validateToken(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
