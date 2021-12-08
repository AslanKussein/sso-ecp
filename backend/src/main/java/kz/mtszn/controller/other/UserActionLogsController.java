package kz.mtszn.controller.other;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import kz.mtszn.dto.UserLogsRequestDTO;
import kz.mtszn.service.UserActionLogs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/user-logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserActionLogsController {

    private final UserActionLogs userActionLogs;


    @ApiOperation(value = "", notes = "create user action logs", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {}
    )
    @PostMapping("/create")
    public ResponseEntity<Void> logout(@RequestBody UserLogsRequestDTO userLogsDTO) {
        userActionLogs.createUserActionLogs(userLogsDTO);

        return ResponseEntity.noContent().build();
    }
}
