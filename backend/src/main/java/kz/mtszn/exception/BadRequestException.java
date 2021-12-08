package kz.mtszn.exception;

import kz.mtszn.dto.LocalValue;
import kz.mtszn.util.BundleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class BadRequestException extends DetailedException {
    public BadRequestException(String description) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, description);
        log.error(description);
    }

    public static BadRequestException emptyRefreshToken(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "empty.refresh.token"));
    }

    public static BadRequestException notCorrectUserException(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "not.correct.user.data"));
    }

    public static BadRequestException chooseEsp(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.not.choose"));
    }

    public static BadRequestException userNotEqualsEcpUser(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "user.notsame.ecpiin.user"));
    }

    public static BadRequestException emptyLoginOrPassword(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.login.or.password.empty"));
    }

    public static BadRequestException getEcpError(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp"));
    }

    public static BadRequestException getEcpErrorIin(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.iin"));
    }

    public static BadRequestException getEcpErrorValid(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "error.ecp.valid"));
    }

    public static BadRequestException userBlock(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "not.correct.user.block.data"));
    }

    public static BadRequestException passwordNewAndReNewNotSame(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "password.not.same"));
    }

    public static BadRequestException passwordLengthLess8(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "password.length"));
    }

    public static BadRequestException passwordNotHasUpper(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "password.upper"));
    }

    public static BadRequestException passwordNotHasSpecSymbols(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "password.special"));
    }

    public static BadRequestException userHasNotAccessSSO(final LocalValue language) {
        return new BadRequestException(BundleMessageUtil.getLocaledValue(language, "user.not.access.sso"));
    }

}
