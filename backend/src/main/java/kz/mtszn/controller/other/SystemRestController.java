package kz.mtszn.controller.other;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import kz.mtszn.dto.BlockUserDto;
import kz.mtszn.dto.ErrorDto;
import kz.mtszn.dto.SystemDto;
import kz.mtszn.models.DAlias;
import kz.mtszn.security.IAuthenticationFacade;
import kz.mtszn.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*") //this line

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/systems", produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemRestController {

    private final SystemService appService;
    private final IAuthenticationFacade authenticationFacade;

    @ApiOperation(value = "", notes = "список всех систем", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/all")
    public ResponseEntity<List<SystemDto>> getAll() {
        return ResponseEntity.ok(appService.getAll());
    }


    @ApiOperation(value = "", notes = "список", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/getSystemList")
    public ResponseEntity<List<SystemDto>> getSystemList() {
        return ResponseEntity.ok(appService.getSystemList(authenticationFacade.getUser().getEmpId()));
    }

    @ApiOperation(value = "", notes = "добавление системы", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/create")
    public ResponseEntity<SystemDto> addSystem(@RequestBody SystemDto dto) {
        return ResponseEntity.ok(appService.addSystem(dto));
    }

    @ApiOperation(value = "", notes = "изменения системы", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PutMapping("/{id}")
    public ResponseEntity<SystemDto> editSystem(@PathVariable("id") Long id,
                                                @RequestBody SystemDto dto) {
        return ResponseEntity.ok(appService.editSystem(id, dto));
    }


    @ApiOperation(value = "", notes = "Переход в систему", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping("/redirectSystem")
    public ResponseEntity<Void> redirectSystem(@RequestBody SystemDto systemDto) {
        appService.redirectSystem(authenticationFacade.getUser().getEmpId(), authenticationFacade.getUser().getUsername(), systemDto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "", notes = "Достаем все алиясы", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = DAlias.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/getDAlias")
    public ResponseEntity<List<DAlias>> getDAlias() {
        return ResponseEntity.ok(appService.getDAlias());
    }

    @ApiOperation(value = "", notes = "Достаем заблокированных юзеров", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BlockUserDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/getBlockUserList")
    public ResponseEntity<List<BlockUserDto>> getBlockUserList() {
        return ResponseEntity.ok(appService.getBlockUserList());
    }

    @ApiOperation(value = "", notes = "Разблокируем юзера", response = void.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @DeleteMapping("/unBlockUser/{empId}")
    public ResponseEntity<Void> unBlockUser(@PathVariable final Long empId) {
        appService.unBlockUser(empId);
        return ResponseEntity.noContent().build();
    }
}
