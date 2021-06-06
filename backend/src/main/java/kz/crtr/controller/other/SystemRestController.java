package kz.crtr.controller.other;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import kz.crtr.dto.ErrorDto;
import kz.crtr.dto.SystemDto;
import kz.crtr.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/systems", produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemRestController {

    private final SystemService appService;

    @ApiOperation(value = "", notes = "список всех систем", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/all")
    public ResponseEntity<List<SystemDto>> getAll() {
        return ResponseEntity.ok(appService.getAll());
    }


    @ApiOperation(value = "", nickname = "cancelParkingConnect", notes = "список", response = SystemDto.class, authorizations = {
            @Authorization(value = "bearer-key")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SystemDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping("/getSystemList")
    public ResponseEntity<List<SystemDto>> getSystemList() {
        return ResponseEntity.ok(appService.getSystemList());
    }
}
