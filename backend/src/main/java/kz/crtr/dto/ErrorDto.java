package kz.crtr.dto;

import lombok.Data;

import java.sql.Date;
import java.time.Instant;

@Data
public class ErrorDto {
    String message;
    Long timestamp;

    public ErrorDto(String message) {
        this.message = message;
        this.timestamp = Date.from(Instant.now()).getTime();
    }
}
