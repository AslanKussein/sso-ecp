package kz.mtszn.controller.other;

import io.swagger.annotations.*;
import kz.mtszn.dto.*;
import kz.mtszn.models.BlockUser;
import kz.mtszn.security.IAuthenticationFacade;
import kz.mtszn.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserService userService;
    private final IAuthenticationFacade auth;

    @ApiOperation(value = "", notes = "список всех систем", response = UserDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/getUserByName")
    public ResponseEntity<UserDto> getUserByName() {
        return ResponseEntity.ok(userService.findUserByName(auth.getUser().getUsername()));
    }

    @ApiOperation(value = "", notes = "Заблокированные пользователи", response = BlockUser.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BlockUser.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/getListBlockUser")
    public ResponseEntity<List<BlockUser>> getListBlockUser() {
        return ResponseEntity.ok(userService.getListBlockUser());
    }

    @ApiOperation(value = "", notes = "Разблокировать пользователя", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/unlockUser/{id}")
    public ResponseEntity<Void> unlockUser(@PathVariable final Long id) {
        userService.unlockUser(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "", notes = "Последние действия", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserLogsDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/getAllAction")
    public ResponseEntity<PageDto<?>> getAllAction(@RequestBody PageableDTO pageable) {
        return ResponseEntity.ok(userService.getUserAction(auth.getUser().getEmpId(), pageable));
    }

    @ApiOperation(value = "", notes = "Смена пароля", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestHeader final LocalValue lang,
                                               @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePasswordDto(lang, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "", notes = "Все роли юзера", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserLogsDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/getUserRole")
    public ResponseEntity<PageDto<?>> getUserRole(@RequestBody PageableDTO pageable) {
        return ResponseEntity.ok(userService.getUserRole(auth.getUser().getEmpId(), pageable));
    }

}
