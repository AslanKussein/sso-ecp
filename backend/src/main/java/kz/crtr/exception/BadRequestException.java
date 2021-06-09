package kz.crtr.exception;

import kz.crtr.dto.LocalValue;
import kz.crtr.util.BundleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class BadRequestException extends DetailedException {
    public BadRequestException(String description) {
        super(HttpStatus.BAD_REQUEST, description);
        log.error(description);
    }

    public static BadRequestException emptyRefreshToken(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "empty.refresh.token"));
    }

    public static BadRequestException notCorrectUserException(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "not.correct.user.data"));
    }

    public static BadRequestException chooseEsp(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.not.choose"));
    }

    public static BadRequestException userNotEqualsEcpUser(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "user.notsame.ecpiin.user"));
    }

    public static BadRequestException emptyLoginOrPassword(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.login.or.password.empty"));
    }

    public static BadRequestException getEcpError(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp"));
    }

    public static BadRequestException getEcpErrorIin(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.iin"));
    }

    public static BadRequestException getEcpErrorValid(LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.valid"));
    }
}
