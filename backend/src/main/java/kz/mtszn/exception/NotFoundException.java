package kz.mtszn.exception;

import kz.mtszn.dto.LocalValue;
import kz.mtszn.util.BundleMessageUtil;
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
