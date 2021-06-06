package kz.crtr.exception;

import kz.crtr.dto.LocalValue;
import kz.crtr.util.BundleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NotFoundException extends DetailedException {
    public NotFoundException(String description) {
        super(HttpStatus.NOT_FOUND, description);
        log.error(description);
    }

    public static NotFoundException userNotFound(final LocalValue language, String username) {
        return new NotFoundException(BundleMessageUtil.getLocaledValue(language, "error.user.not.found", username));
    }

}
