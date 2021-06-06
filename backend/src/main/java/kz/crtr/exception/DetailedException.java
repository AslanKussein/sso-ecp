package kz.crtr.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Slf4j
public class DetailedException extends RuntimeException {
    private final Integer statusCode;

    public DetailedException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public DetailedException(HttpStatus httpStatus, String message) {
        super(message);
        this.statusCode = httpStatus.value();
    }
}
