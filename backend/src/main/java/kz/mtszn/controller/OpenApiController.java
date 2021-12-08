package kz.mtszn.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import kz.mtszn.dto.BlockUserDto;
import kz.mtszn.dto.ErrorDto;
import kz.mtszn.models.repository.DepartmentUserContactsRepository;
import kz.mtszn.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/open-api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OpenApiController {

    private final DepartmentUserContactsRepository departmentUserContactsRepository;
    private final UserService userService;

    @GetMapping("/getContacts")
    public ResponseEntity<?> getContacts() {
        return ResponseEntity.ok(departmentUserContactsRepository.findAll());
    }

    @ApiOperation(value = "", notes = "Количество трайов", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BlockUserDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/getCounter/{username}")
    public ResponseEntity<BlockUserDto> getCounter(@PathVariable String username) {
        return ResponseEntity.ok(userService.getCounter(username));
    }
}
